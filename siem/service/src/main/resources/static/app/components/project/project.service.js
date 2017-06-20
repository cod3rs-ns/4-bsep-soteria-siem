(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('projectService', projectService);

    projectService.$inject = ['CONFIG', '$http', '$log'];

    function projectService(CONFIG, $http, $log) {
        var service = {
            getProjectById: getProjectById,
            getLogs: getLogs,
            getAgents: getAgents,
            addAgent: addAgent,
            downloadAgent: downloadAgent
        };

        return service;

        function getProjectById(id) {
            return $http.get(CONFIG.SERVICE_URL + '/projects/' + id)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getLogs(url) {
            return $http.get(url)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getAgents(url) {
            return $http.get(url)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function addAgent(id, agentData) {
            return $http.post(CONFIG.SERVICE_URL + '/projects/' + id + '/agents', { 'data': agentData })
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function downloadAgent(agentConfigData) {
            return $http.post(CONFIG.SERVICE_URL + '/agents', { 'data': agentConfigData }, { responseType: 'arraybuffer' })
                .then(function successCallback(response) {
                    var a = document.createElement('a');
                    var blob = new Blob([response.data], {'type':"application/octet-stream"});
                    a.href = URL.createObjectURL(blob);
                    a.download = "agent.zip";
                    a.click();
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }
    }
})();
