(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ProjectsController', ProjectsController);

    ProjectsController.$inject = [];

    function ProjectsController() {
        var projectsVm = this;
    }
})();
