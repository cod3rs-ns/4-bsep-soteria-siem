(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('definitionService', definitionService);

    definitionService.$inject = ['CONFIG', '$http', '$log'];

    function definitionService(CONFIG, $http, $log) {
        return {
            getAll: getAlarmDefinitions,
            getOne: getAlarmDefinitionById,
            create: createAlarmDefinition,
            delete: deleteAlarmDefinition
        };

        function getAlarmDefinitionById(project_id, definition_id) {
            return $http.get(CONFIG.SERVICE_URL + '/projects/' + project_id + '/alarm-definitions/' + definition_id)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getAlarmDefinitions(project_id) {
            return $http.get(CONFIG.SERVICE_URL + '/projects/' + project_id + '/alarm-definitions')
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function deleteAlarmDefinition(project_id, definition_id) {
            return $http.delete(CONFIG.SERVICE_URL + '/projects/' + project_id + '/alarm-definitions/' + definition_id)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function createAlarmDefinition(project_id, definitionData) {
            return $http.post(CONFIG.SERVICE_URL + '/projects/' + project_id + '/alarm-definitions', {'data': definitionData})
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }
    }
})();
