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

        projectVm.loadInitialLogs = loadLogs;
        projectVm.loadMore = loadLogs;

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
    }
})();
