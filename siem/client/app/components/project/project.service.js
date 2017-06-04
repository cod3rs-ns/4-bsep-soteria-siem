(function() {
    'use strict';

    angular
        .module('soteria-app')
        .service('projectService', projectService);

    projectService.$inject = ['$http', '$log'];

    function projectService($http, $log) {
        var service = {
            getLogs: getLogs
        };

        return service;

        function getLogs(url) {
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
