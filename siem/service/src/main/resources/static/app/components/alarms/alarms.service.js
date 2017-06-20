(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('alarmsService', alarmsService);

    alarmsService.$inject = ['$http', '$log', 'CONFIG'];

    function alarmsService($http, $log, CONFIG) {
        return {
            getAlarmsForUser: getAlarmsForUser,
            getLogById: getLogById,
            resolveAlarm: resolveAlarm,
            getAllByDefinition: getAlarmsByDefinition,
            getAlarm: getAlarm,
            getLogsForAlarm: getLogsForAlarm
        };

        function getAlarmsForUser(url) {
            return $http.get(url)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getAlarmsByDefinition(project_id, definition_id) {
            return $http.get(CONFIG.SERVICE_URL + '/projects/' + project_id + '/alarm-definitions/' + definition_id + '/alarms')
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }


        function getLogById(url) {
            return $http.get(url)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function resolveAlarm(alarmId) {
            return $http.put(CONFIG.SERVICE_URL + "/alarms/" + alarmId + "/resolve")
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getAlarm(projectId, alarmId) {
            return $http.get(CONFIG.SERVICE_URL + "/projects/" + projectId + "/alarms/" + alarmId)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getLogsForAlarm(alarmId) {
            return $http.get(CONFIG.SERVICE_URL + "/logs/alarms/" + alarmId)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }
    }
})();
