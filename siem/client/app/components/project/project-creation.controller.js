(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ProjectCreationController', ProjectCreationController);

    ProjectCreationController.$inject = ['projectService', '$state', '$log', 'ngToast'];

    function ProjectCreationController(projectService, $state, $log, ngToast) {
        var projectCreationVm = this;
        
        projectCreationVm.project = {
            "name": "",
            "description": ""
        }
        
        projectCreationVm.createProject = createProject;

        function createProject() {
            projectService.createProject({'type': 'projects', 'attributes':  projectCreationVm.project})
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
