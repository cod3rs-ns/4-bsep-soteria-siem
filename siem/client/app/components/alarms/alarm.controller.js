(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('AlarmController', AlarmController);

    AlarmController.$inject = ['alarmsService', 'CONFIG', '$log', '$localStorage', '$stateParams', '_'];

    function AlarmController(alarmsService, CONFIG, $log, $localStorage, $stateParams, _) {
        var alarmVm = this;

        alarmVm.alarm = {};
        alarmVm.logs = [];

        alarmVm.levelClasses = {
            "INFO": "label-info",
            "LOW": "label-primary",
            "MEDIUM": "label-default",
            "HIGH": "label-warning",
            "SEVERE": "label-danger"
        }

        alarmVm.loadAlarm = loadAlarm;
        alarmVm.loadLogs = loadLogs;
        alarmVm.resolveAlarm = resolveAlarm;

        activate();

        function activate() {
            var alarmId = $stateParams.alarmId;
            var projectId = $stateParams.projectId;

            alarmVm.loadAlarm(projectId, alarmId);
            alarmVm.loadLogs(alarmId);
        }

        function loadAlarm(projectId, alarmId) {
            alarmsService.getAlarm(projectId, alarmId)
                .then(function (response) {
                    alarmVm.alarm = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function loadLogs(alarmId) {
            alarmsService.getLogsForAlarm(alarmId)
                .then(function (response) {
                    alarmVm.logs = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function resolveAlarm() {
            alarmsService.resolveAlarm($stateParams.alarmId)
                .then(function (response) {
                    alarmVm.alarm = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }
    }
})();
