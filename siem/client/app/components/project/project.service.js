(function() {
    'use strict';

    angular
        .module('soteria-app')
        .service('projectService', projectService);

    projectService.$inject = ['$http', '$log', 'CONFIG'];

    function projectService($http, $log, CONFIG) {
        var service = {
            getLogs: getLogs
        };

        return service;

        function getLogs(projectId) {
            return $http.get(CONFIG.SERVICE_URL + '/projects/' + projectId + '/logs')
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };
    }
})();
