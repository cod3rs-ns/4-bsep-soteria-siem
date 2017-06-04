(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ProjectController', ProjectController);

    ProjectController.$inject = ['CONFIG', '$stateParams', '$log', 'projectService', '_'];

    function ProjectController(CONFIG, $stateParams, $log, projectService, _) {
        var projectVm = this;

        projectVm.logs = {
            'data': [],
            'next': null
        };

        projectVm.config = {
            'os': null,
            'defaultLevel': null,
            'paths': [],
            'regexes': []
        };

        projectVm.loadInitialLogs = loadLogs;
        projectVm.loadMore = loadLogs;

        projectVm.addField = addField;

        activate();

        function activate () {
            projectVm.loadInitialLogs(CONFIG.SERVICE_URL + '/projects/' + $stateParams.id + '/logs?page[limit]=2');
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

        function addField(group) {
            var index = _.size(projectVm.config[group]) + 1;
            projectVm.config[group].push({'id': index, value: ''});
        }
    }
})();
