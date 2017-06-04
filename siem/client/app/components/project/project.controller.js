(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ProjectController', ProjectController);

    ProjectController.$inject = ['$log', 'projectService'];

    function ProjectController($log, projectService) {
        var projectVm = this;

        projectVm.logs = {
            'data': {},
            'next': null
        };

        projectVm.retrieveLogs = retrieveLogs;

        activate();

        function activate () {
            projectVm.retrieveLogs(1);
        }

        function retrieveLogs(projectId) {
            projectService.getLogs(projectId)
                .then(function(response) {
                    projectVm.logs.data = response.data;
                    projectVm.logs.next = response.links.next;
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }
    }
})();
