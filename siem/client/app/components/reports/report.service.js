(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('reportService', reportService);

    reportService.$inject = ['CONFIG', '$http', '$log'];

    function reportService(CONFIG, $http, $log) {
        return {
            getCriteriaReport: getCriteriaReport,
            getStandardReport: getStandardReport
        };

        function getCriteriaReport(project_id, report_request) {
            return $http.post(CONFIG.SERVICE_URL + '/projects/' + project_id + '/report', report_request)
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
