(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ProjectsController', ProjectsController);

    ProjectsController.$inject = ['projectsService'];

    function ProjectsController(projectsService) {
        var projectsVm = this;
        projectsVm.ownedProjects = []
        projectsVm.membershipProjects = []
        
        activate();

        function activate() {
            getOwnedProjects();
            getMembershipProjects();
        };

        function getOwnedProjects() {
            projectsService.getOwnedProjects()
                .then(function (response) {
                    projectsVm.ownedProjects = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function getMembershipProjects() {
            projectsService.getMembershipProjects()
                .then(function (response) {
                    projectsVm.membershipProjects = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }
    }
})();
