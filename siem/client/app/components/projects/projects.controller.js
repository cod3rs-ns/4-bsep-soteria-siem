(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ProjectsController', ProjectsController);

    ProjectsController.$inject = ['projectsService', 'CONFIG'];

    function ProjectsController(projectsService, CONFIG) {
        var projectsVm = this;
        
        projectsVm.ownedProjects = {
            "data": [],
            "prev": null,
            "next": null
        }

        projectsVm.membershipProjects = {
            "data": [],
            "prev": null,
            "next": null
        }

        projectsVm.prevOwnedProjects = getOwnedProjects;
        projectsVm.nextOwnedProjects = getOwnedProjects;
        projectsVm.prevMembershipProjects = getMembershipProjects;
        projectsVm.nextMembershipProjects = getMembershipProjects;

        activate();

        function activate() {
            getOwnedProjects(CONFIG.SERVICE_URL + '/projects/owned');
            getMembershipProjects(CONFIG.SERVICE_URL + '/projects/member-of');
        };

        function getOwnedProjects(url) {
            projectsService.getOwnedProjects(url)
                .then(function(response) {
                    projectsVm.ownedProjects.data = response.data;
                    projectsVm.ownedProjects.next = response.links.next;
                    projectsVm.ownedProjects.prev = response.links.prev;
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function getMembershipProjects(url) {
            projectsService.getMembershipProjects(url)
                .then(function(response) {
                    projectsVm.membershipProjects.data = response.data;
                    projectsVm.membershipProjects.next = response.links.next;
                    projectsVm.membershipProjects.prev = response.links.prev;
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }
    }
})();
