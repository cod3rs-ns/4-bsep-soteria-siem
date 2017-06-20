(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ProjectController', ProjectController);

    ProjectController.$inject = ['CONFIG', '$stateParams', '$log', 'projectService', '_'];

    function ProjectController(CONFIG, $stateParams, $log, projectService, _) {
        var projectVm = this;

        projectVm.info = null;

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

        activate();

        function activate () {
            var id = $stateParams.id;
            projectVm.loadProject(id);
            projectVm.loadInitialLogs(CONFIG.SERVICE_URL + '/projects/' + id + '/logs?page[limit]=2');
            projectVm.loadInitialAgents(CONFIG.SERVICE_URL + /projects/ + id + '/agents');
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
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function filterLogs() {
            $log.info(projectVm.timeRange.fromTime);
            $log.info(projectVm.timeRange.toTime);
            $log.info(projectVm.timeRange.date);

            $log.info(new Date(projectVm.timeRange.date.startDate.format('LL')).setTime(projectVm.timeRange.fromTime.getTime()));

            $log.info(projectVm.timeRange.fromTime.toISOString());
            $log.info(projectVm.timeRange.toTime.toISOString());

            var id = $stateParams.id;
            var filters = createFilters();
            projectVm.logs.data = [];
            projectVm.logs.next = null;
            loadLogs(CONFIG.SERVICE_URL + '/projects/' + id + '/logs?page[limit]=2' + filters);
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
                            'patterns':  _.map(projectVm.config.patterns, 'value'),
                            'types': types,
                            'agentId': response.data.id
                        }
                    };

                    projectService.downloadAgent(config)
                        .then(function(response) {
                            // TODO Add some message :)
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
