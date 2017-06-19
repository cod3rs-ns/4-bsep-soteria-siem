(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['$stateParams', '$log', 'reportService', '_'];

    function ReportController($stateParams, $log, reportService, _) {
        var reportVm = this;

        reportVm.reportRequest = {
            type: 'HOST',
            value: '',
            from: "2017-06-10T22:40:22.993+02:00",
            to: "2017-06-30T10:40:22.993+02:00"
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

            reportService.getLogCriteriaReport(reportVm.projectId, reportVm.reportRequest)
                .then(function (response) {
                    reportVm.report = response;
                    var lst = [];
                    _.forEach(reportVm.report.reports, function (value) {
                        reportVm.mainChart.labels.push(value['day']);
                        lst.push(value['day-count']);
                    });
                    reportVm.mainChart.data.push(lst);
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
