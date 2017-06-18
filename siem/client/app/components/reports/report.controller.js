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

        reportVm.loadReport = loadReport;

        activate();

        function activate() {
            reportVm.projectId = $stateParams.id;
        }

        function loadReport() {
            // clear chart data
            reportVm.mainChart.labels = [];
            reportVm.mainChart.data = [];

            // map values
            reportVm.reportRequest.from = reportVm.rangePicker.date.startDate;
            reportVm.reportRequest.to = reportVm.rangePicker.date.endDate;

            reportService.get(reportVm.projectId, reportVm.reportRequest)
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
    }
})();
