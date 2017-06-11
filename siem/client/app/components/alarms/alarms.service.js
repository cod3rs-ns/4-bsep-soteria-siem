(function() {
    'use strict';

    angular
        .module('soteria-app')
        .service('alarmsService', alarmsService);

    alarmsService.$inject = ['$http', '$log', 'CONFIG'];

    function alarmsService($http, $log, CONFIG) {
        var service = {
          getAlarmsForUser: getAlarmsForUser,
          getLogById: getLogById,
          resolveAlarm: resolveAlarm
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

        function resolveAlarm(alarmId) {
            return $http.put(CONFIG.SERVICE_URL + "/alarms/" + alarmId + "/resolve")
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };
    }
})();
