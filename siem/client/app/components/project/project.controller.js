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
            'data': [],
            'next': null
        };

        projectVm.agents = {
            'data': [],
            'next': null,
            'prev': null
        };

        projectVm.config = {
            'os': null,
            'defaultLevel': null,
            'paths': [],
            'regexes': []
        };

        projectVm.loadProject = loadProject;
        projectVm.loadInitialLogs = loadLogs;
        projectVm.loadMore = loadLogs;
        projectVm.loadInitialAgents = loadAgents;
        projectVm.nextAgents = loadAgents;
        projectVm.prevAgents = loadAgents;

        projectVm.addField = addField;

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

        function addField(group) {
            var index = _.size(projectVm.config[group]) + 1;
            projectVm.config[group].push({'id': index, value: ''});
        }
    }
})();
