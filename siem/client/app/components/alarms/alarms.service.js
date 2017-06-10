(function() {
    'use strict';

    angular
        .module('soteria-app')
        .service('alarmsService', alarmsService);

    alarmsService.$inject = ['$http', '$log', 'CONFIG'];

    function alarmsService($http, $log, CONFIG) {
        var service = {
          getAlarmsForUser: getAlarmsForUser,
          getLogById: getLogById
        };

        return service;

        function getAlarmsForUser(url) {
            return $http.get(url)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };

        function getLogById(url) {
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
