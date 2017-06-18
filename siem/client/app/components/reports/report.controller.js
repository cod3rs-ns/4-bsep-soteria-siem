(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['$stateParams', '$log', 'reportService', '_'];

    function ReportController($stateParams, $log, reportService, _) {
        var reportVm = this;

        reportVm.reportRequest = {
            type: 'LEVEL',
            value: 'ERROR',
            from: 1,
            to: 2
        };



        activate();

        function activate() {
            var project_id = $stateParams.id;
            loadReport(project_id);
        }

        function loadReport(project_id) {
            reportService.get(project_id, reportVm.reportRequest)
                .then(function (response) {
                    reportVm.labels = [ ];
                    reportVm.series = ['Times occurred'];
                    reportVm.data = [ ];
                    reportVm.onClick = function (points, evt) {
                        console.log(points, evt);
                    };
                    reportVm.datasetOverride = [{ yAxisID: 'y-axis-1' }];
                    reportVm.options = {
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
                    };
                    reportVm.report = response;
                    console.log(reportVm.report);
                    var lst = [];
                    _.forEach(reportVm.report.reports, function(value) {
                        reportVm.labels.push(value['day']);
                        lst.push(value['day-count']);
                    });
                    reportVm.data.push(lst);
                    console.log(reportVm.data);
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }
    }
})();
