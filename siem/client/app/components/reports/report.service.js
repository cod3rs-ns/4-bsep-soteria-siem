(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('reportService', reportService);

    reportService.$inject = ['CONFIG', '$http', '$log'];

    function reportService(CONFIG, $http, $log) {
        return {
            get: getReport,
            getLevelReport: getLevelReport,
            getPlatformReport: getPlatformReport
        };

        function getReport(project_id, reportRequest) {
            return $http.post(CONFIG.SERVICE_URL + '/projects/' + project_id + '/report', reportRequest)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getLevelReport(project_id) {
            return $http.get(CONFIG.SERVICE_URL + '/projects/' + project_id + '/report/levels')
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getPlatformReport(project_id) {
            return $http.get(CONFIG.SERVICE_URL + '/projects/' + project_id + '/report/platforms')
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

    }
})();
