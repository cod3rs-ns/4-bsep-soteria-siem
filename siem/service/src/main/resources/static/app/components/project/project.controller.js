(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ProjectController', ProjectController);

    ProjectController.$inject = ['CONFIG', '$stateParams', '$log', 'projectService', '_'];

    function ProjectController(CONFIG, $stateParams, $log, projectService, _) {
        var projectVm = this;

        projectVm.info = null;
        projectVm.projectId = $stateParams.id;

        projectVm.logs = {
            'filters': {},
            'data': [],
            'next': null
        };

        projectVm.timeRange = {
            'fromTime': null,
            'date': {
                'startDate': null,
                'endDate': null
            },
            'toTime': null
        };

        projectVm.agents = {
            'data': [],
            'next': null,
            'prev': null
        };

        projectVm.config = {
            'name': '',
            'description': '',
            'version': '1.0.0',
            'os': null,
            'defaultLevel': "DEBUG",
            'paths': [],
            'regexes': [],
            'patterns': [],
            'types': {
                System: false,
                Firewall: false,
                Application: false
            }
        };

        projectVm.numberLogs = {
            'error': 0,
            'warn': 0
        };
        projectVm.levelCheckboxes = {
            DEBUG: false,
            INFO: false,
            NOTICE: false,
            WARN: false,
            ERROR: false,
            CRIT: false,
            ALERT: false,
            EMERG: false
        };

        projectVm.loadProject = loadProject;
        projectVm.loadInitialLogs = loadLogs;
        projectVm.loadMore = loadLogs;
        projectVm.filterLogs = filterLogs;
        projectVm.loadInitialAgents = loadAgents;
        projectVm.nextAgents = loadAgents;
        projectVm.prevAgents = loadAgents;

        projectVm.choosePlatform = chooseAgentPlatform;
        projectVm.saveAgent = saveAgent;

        projectVm.addField = addField;
        projectVm.getLabelColor = getLabelColor;
        projectVm.countLogs = countLogs;
        projectVm.updateMiniReport = updateMiniReport;

        activate();

        function activate () {
            var id = $stateParams.id;
            projectVm.loadProject(id);
            projectVm.loadInitialLogs(CONFIG.SERVICE_URL + '/projects/' + id + '/logs?page[limit]=10');
            projectVm.loadInitialAgents(CONFIG.SERVICE_URL + '/projects/' + id + '/agents');
        }

        function loadProject(id) {
            projectService.getProjectById(id)
                .then(function(response) {
                    projectVm.info = response.data;
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function loadLogs(url) {
            projectService.getLogs(url)
                .then(function(response) {
                    projectVm.logs.data = _.concat(projectVm.logs.data, response.data);
                    projectVm.logs.next = response.links.next;
                    projectVm.updateMiniReport();
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function filterLogs() {
            var from = null;
            var to = null;

            if (!_.isNull(projectVm.timeRange.date.startDate) && !_.isNull(projectVm.timeRange.fromTime)
                && !_.isNull(projectVm.timeRange.date.endDate) && !_.isNull(projectVm.timeRange.toTime)) {
                from = new Date(
                    projectVm.timeRange.date.startDate.year(),
                    projectVm.timeRange.date.startDate.month(),
                    projectVm.timeRange.date.startDate.date(),
                    projectVm.timeRange.fromTime.getHours(),
                    projectVm.timeRange.fromTime.getMinutes()
                ).toISOString();

                to = new Date(
                    projectVm.timeRange.date.endDate.year(),
                    projectVm.timeRange.date.endDate.month(),
                    projectVm.timeRange.date.endDate.date(),
                    projectVm.timeRange.toTime.getHours(),
                    projectVm.timeRange.toTime.getMinutes()
                ).toISOString();
            }

            var filters = createFilters();

            if (!_.isNull(from) && !_.isNull(to)) {
                filters += '&filter[from]=' + from + '&filter[to]=' + to;
            }

            projectVm.logs.data = [];
            projectVm.logs.next = null;
            loadLogs(CONFIG.SERVICE_URL + '/projects/' + projectVm.projectId + '/logs?page[limit]=10' + filters);
        }

        function loadAgents(url) {
            projectService.getAgents(url)
                .then(function(response) {
                    projectVm.agents.data = response.data;
                    projectVm.agents.next = response.links.next;
                    projectVm.agents.prev = response.links.prev;
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function saveAgent() {
            var projectId = $stateParams.id;
            var data = {
                'type': 'agents',
                'attributes': {
                    'name': projectVm.config.name,
                    'description': projectVm.config.description,
                    'agent_version': projectVm.config.version,
                    'agent_type': projectVm.config.os
                },
                'relationships': {
                    'type': 'projects',
                    'id': projectId
                }
            };

            projectService.addAgent(projectId, data)
                .then(function(response) {
                    if (_.size(projectVm.agents.data) < CONFIG.AGENTS_LIMIT) {
                        projectVm.agents.data.push(response.data);
                    }

                    var types = [];
                    _.forEach(projectVm.config.types, function (value, key) {
                        if (true === value) {
                            types.push(key);
                        }
                    });

                    var config = {
                        'type': 'agent-configs',
                        'attributes': {
                            'os': projectVm.config.os,
                            'defaultLevel': projectVm.config.defaultLevel,
                            'paths': _.map(projectVm.config.paths, 'value'),
                            'regexes': _.map(projectVm.config.regexes, 'value'),
                            'patterns': _.map(projectVm.config.patterns, 'value'),
                            'types': types,
                            'agentId': response.data.id
                        }
                    };

                    projectService.downloadAgent(config)
                        .then(function(response) {
                            $log.info("Successfully downloaded agent");
                        })
                        .catch(function(error) {
                            $log.error(error);
                        });
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function createFilters() {
            var filters = "";

            var levels = [];
            _.forEach(projectVm.levelCheckboxes, function (value, key) {
                if (true === value) {
                    levels.push(key);
                }
            });

            if (!_.isEmpty(levels)) {
                filters += '&filter[level]=' + _.join(levels);
            }

            _.forEach(projectVm.logs.filters, function (value, key) {
                if (key !== 'info' && !_.isEmpty(value))
                    filters += '&filter[' + key + ']=' + value;
            });

            var info = projectVm.logs.filters.info;
            if (!_.isUndefined(info)) {
                _.forEach(info, function (value, key) {
                    if (!_.isEmpty(value)) {
                        filters += '&filter[info.' + key + ']=' + value;
                    }
                });
            }

            return filters;
        }

        function chooseAgentPlatform(platform) {
            projectVm.config.os = platform;
        }

        function addField(group) {
            var index = _.size(projectVm.config[group]) + 1;
            projectVm.config[group].push({'id': index, value: ''});
            if ('regexes' === group) {
                addField('patterns');
            }
        }

        function getLabelColor(logLevel) {
            switch(_.toUpper(logLevel)) {
                case 'ERROR': case 'EMERG': case 'ALERT': case 'CRIT':
                    return 'label-danger';
                case 'WARN':
                    return 'label-warning';
                case 'INFO':
                    return 'label-info';
                case 'NOTICE':
                    return 'label-primary';
                case 'DEBUG':
                    return 'label-success';
                default:
                    return 'label-default';
            }
        }

        function updateMiniReport() {
           projectVm.countLogs('error');
           projectVm.countLogs('warn');
        }

        function countLogs(level) {
            projectVm.numberLogs[level] = _.size(_.filter(projectVm.logs.data, function(log) {
                return _.toLower(log.attributes.level) === level;
            }));
        }

        projectVm.options = projectVm.options = {
            locale: {
                applyLabel: "Apply",
                fromLabel: "From",
                format: "YYYY-MM-DD",
                toLabel: "To",
                cancelLabel: 'Cancel',
                customRangeLabel: 'Custom range'
            }
        };
    }
})();
