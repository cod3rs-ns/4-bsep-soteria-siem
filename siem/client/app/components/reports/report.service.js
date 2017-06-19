(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('reportService', reportService);

    reportService.$inject = ['CONFIG', '$http', '$log'];

    function reportService(CONFIG, $http, $log) {
        return {
            getLogCriteriaReport: getLogCriteriaReport,
            getStandardReport: getStandardReport
        };

        function getLogCriteriaReport(project_id, report_request) {
            return $http.post(CONFIG.SERVICE_URL + '/projects/' + project_id + '/logs/report', report_request)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

        function getStandardReport(project_id, report_type) {
            return $http.get(CONFIG.SERVICE_URL + '/projects/' + project_id + '/std-reports/' + report_type)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }

    }
})();
