(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('projectsService', projectsService);

    projectsService.$inject = ['CONFIG', '$http', '$log'];

    function projectsService(CONFIG, $http, $log) {
        var service = {
            getOwnedProjects: getOwnedProjects,
            getMembershipProjects: getMembershipProjects,
            getCollaborators: getCollaborators,
            addCollaborator: addCollaborator,
            createProject: createProject
        };

        return service;

        function getOwnedProjects(url) {
            return $http.get(url)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getMembershipProjects(url) {
            return $http.get(url)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getCollaborators(projectId) {
            return $http.get(CONFIG.SERVICE_URL + '/projects/' + projectId + '/users')
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function addCollaborator(projectId, collaboratorId) {
            return $http.post(CONFIG.SERVICE_URL + '/projects/' + projectId + '/users/' + collaboratorId)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function createProject(project) {
            return $http.post(CONFIG.SERVICE_URL + '/projects', { 'data': project })
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }
    }
})();
