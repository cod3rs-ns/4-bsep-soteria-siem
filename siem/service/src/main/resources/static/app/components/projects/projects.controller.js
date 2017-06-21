(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ProjectsController', ProjectsController);

    ProjectsController.$inject = ['projectsService', 'usersService', 'ngToast', 'CONFIG', '$log', '$state', '_'];

    function ProjectsController(projectsService, usersService, ngToast, CONFIG, $log, $state, _) {
        var projectsVm = this;

        projectsVm.ownedProjects = {
            "data": [],
            "prev": null,
            "next": null
        };

        projectsVm.membershipProjects = {
            "data": [],
            "prev": null,
            "next": null
        };

        projectsVm.projectDialogInfo = {
            "id": null,
            "collaborators": []
        };

        projectsVm.createProjectDialog = {
            "project": {
                "name": "",
                "description": ""
            }
        };

        projectsVm.message = "";

        projectsVm.prevOwnedProjects = getOwnedProjects;
        projectsVm.nextOwnedProjects = getOwnedProjects;
        projectsVm.prevMembershipProjects = getMembershipProjects;
        projectsVm.nextMembershipProjects = getMembershipProjects;

        projectsVm.setDialogId = setProjectId;
        projectsVm.addCollaborator = addCollaborator;
        projectsVm.getCollaborators = getCollaborators;
        projectsVm.createProject = createProject;

        activate();

        function activate() {
            getOwnedProjects(CONFIG.SERVICE_URL + '/projects/owned');
            getMembershipProjects(CONFIG.SERVICE_URL + '/projects/member-of');
        }

        function getOwnedProjects(url) {
            projectsService.getOwnedProjects(url)
                .then(function (response) {
                    projectsVm.ownedProjects.data = response.data;
                    projectsVm.ownedProjects.next = response.links.next;
                    projectsVm.ownedProjects.prev = response.links.prev;
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function getMembershipProjects(url) {
            projectsService.getMembershipProjects(url)
                .then(function (response) {
                    projectsVm.membershipProjects.data = response.data;
                    projectsVm.membershipProjects.next = response.links.next;
                    projectsVm.membershipProjects.prev = response.links.prev;
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function addCollaborator(email) {
            usersService.getUserByEmail(email)
                .then(function (response) {
                    var userId = response.data.id;
                    var projectId = projectsVm.projectDialogInfo.id;

                    projectsVm.message = "";

                    projectsService.addCollaborator(projectId, userId)
                        .then(function (response) {
                            var collaborator = response.data;
                            var defaultProfileImage = 'http://i.imgur.com/HQ3YU7n.gif';
                            if (_.isUndefined(collaborator.attributes.imagepath)) {
                                collaborator.attributes.imagepath = defaultProfileImage;
                            }
                            projectsVm.collaboratorEmail = "";
                            projectsVm.projectDialogInfo.collaborators.push(collaborator);
                        })
                        .catch(function (error) {
                            projectsVm.message = error;
                        });
                })
                .catch(function (error) {
                    $log.info(error);
                    projectsVm.message = "There's no registered user with this email."
                });
        }

        function getCollaborators(projectId) {
            projectsService.getCollaborators(projectId)
                .then(function (response) {
                    projectsVm.projectDialogInfo.collaborators = response.data;
                    var defaultProfileImage = 'http://i.imgur.com/HQ3YU7n.gif';
                    _.forEach(projectsVm.projectDialogInfo.collaborators, function(collaborator) {
                        if (_.isUndefined(collaborator.attributes.imagepath)) {
                            collaborator.attributes.imagepath = defaultProfileImage;
                        }
                    });
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function setProjectId(id) {
            projectsVm.projectDialogInfo.id = id;
            projectsVm.message = "";
            getCollaborators(id);
        }

        function createProject() {
            projectsService.createProject({'type': 'projects', 'attributes':  projectsVm.createProjectDialog.project})
                .then(function(response) {
                    ngToast.create({
                        className: 'success',
                        content: '<strong>Successfully create project.</strong>'
                    });
                    $state.go('project', {'id': response.data.id});
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }
    }
})();
