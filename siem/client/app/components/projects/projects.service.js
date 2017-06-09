(function() {
    'use strict';

    angular
        .module('soteria-app')
        .service('projectsService', projectsService);

    projectsService.$inject = ['$http', '$log', 'CONFIG'];

    function projectsService($http, $log, CONFIG) {
        var service = {
          getOwnedProjects: getOwnedProjects,
          getMembershipProjects: getMembershipProjects
        };

        return service;

        function getOwnedProjects() {
            return $http.get(CONFIG.SERVICE_URL + '/projects/owned')
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };

        function getMembershipProjects() {
            return $http.get(CONFIG.SERVICE_URL + '/projects/only-member-of')
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };
    }
})();
