(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('LogReportController', LogReportController);

    LogReportController.$inject = ['$stateParams', '$log', 'reportService', '_', 'ngToast'];

    function LogReportController($stateParams, $log, reportService, _, ngToast) {
        var reportVm = this;

        reportVm.mainReportVisible = false;

        reportVm.reportRequest = {
            type: 'LOG_HOST',
            'entity-type': 'LOGS',
            value: '',
            from: null,
            to: null
        };

        reportVm.rangePicker = {
            options: {
                locale: {
                    applyLabel: "Apply",
                    fromLabel: "From",
                    format: "YYYY-MM-DD",
                    toLabel: "To",
                    cancelLabel: 'Cancel',
                    customRangeLabel: 'Custom range'
                },
                ranges: {
                    'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                    'Last 30 Days': [moment().subtract(29, 'days'), moment()]
                }
            },
            date: {
                startDate: moment().subtract(6, 'days'),
                endDate: moment()
            }
        };

        reportVm.mainChart = {
            labels: [],
            series: ['times occurred'],
            data: [],
            datasetOverride: [
                {
                    yAxisID: 'y-axis-1'
                }
            ],
            options: {
                scales: {
                    yAxes: [
                        {
                            id: 'y-axis-1',
                            type: 'linear',
                            display: true,
                            position: 'right'
                        }
                    ]
                }
            }
        };

        reportVm.levelChart = {
            labels: [],
            series: ['times occurred'],
            data: [],
            options: {
                colors: ['#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
                legend: {
                    display: true,
                    position: 'bottom'
                }
            }
        };

        reportVm.platformChart = {
            labels: [],
            series: ['times occurred'],
            data: [],
            options: {
                colors: ['#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
                legend: {
                    display: true,
                    position: 'bottom'
                }
            }
        };

        reportVm.loadReport = loadReport;

        activate();

        function activate() {
            reportVm.projectId = $stateParams.id;
            loadLevelReport();
            loadPlatformReport();
        }

        function loadReport() {
            // clear chart data
            reportVm.mainChart.labels = [];
            reportVm.mainChart.data = [];

            // map values
            reportVm.reportRequest.from = reportVm.rangePicker.date.startDate;
            reportVm.reportRequest.to = reportVm.rangePicker.date.endDate;

            reportService.getCriteriaReport(reportVm.projectId, reportVm.reportRequest)
                .then(function (response) {
                    reportVm.report = response;
                    try {
                        reportVm.mainReportVisible = response.reports.length > 0;
                    } catch (e) {
                        reportVm.mainReportVisible = false;
                    }
                    var lst = [];
                    _.forEach(reportVm.report.reports, function (value) {
                        reportVm.mainChart.labels.push(value['day']);
                        lst.push(value['day-count']);
                    });
                    reportVm.mainChart.data.push(lst);
                    ngToast.create({
                        className: 'success',
                        content: '<strong>Successfully retrieved report.</strong>'
                    });
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function loadLevelReport() {
            reportService.getStandardReport(reportVm.projectId, 'LOG_LEVELS')
                .then(function (response) {
                    reportVm.levelReport = response;
                    _.forEach(reportVm.levelReport.reports, function (value) {
                        reportVm.levelChart.labels.push(value['name']);
                        reportVm.levelChart.data.push(value['value']);
                    });
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function loadPlatformReport() {
            reportService.getStandardReport(reportVm.projectId, 'LOG_PLATFORMS')
                .then(function (response) {
                    reportVm.platformReport = response;
                    _.forEach(reportVm.platformReport.reports, function (value) {
                        reportVm.platformChart.labels.push(value['name']);
                        reportVm.platformChart.data.push(value['value']);
                    });
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

    }
})();
