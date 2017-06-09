(function() {
    'use strict';

    angular
        .module('soteria-app')
        .service('projectsService', projectsService);

    projectsService.$inject = ['$http', '$log'];

    function projectsService($http, $log) {
        var service = {
          getOwnedProjects: getOwnedProjects,
          getMembershipProjects: getMembershipProjects
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
        };

        function getMembershipProjects(url) {
            return $http.get(url)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };
    }
})();
