(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('AlarmDefinitionPreviewController', AlarmDefinitionPreviewController);

    AlarmDefinitionPreviewController.$inject = ['CONFIG', '$stateParams', '$log', 'definitionService', 'alarmsService', '_'];

    function AlarmDefinitionPreviewController(CONFIG, $stateParams, $log, definitionService, alarmsService, _) {
        var defPreviewVm = this;

        defPreviewVm.definition = {};
        defPreviewVm.meta = {};
        defPreviewVm.rules = [];
        defPreviewVm.multiRule = {};
        defPreviewVm.alarms = [];
        defPreviewVm.resolvedPercentage = 73;
        defPreviewVm.resolvedAlarms = 0;
        defPreviewVm.chartOptions = {
            animate: {
                duration: 5,
                enabled: true
            },
            barColor: '#00897B',
            scaleColor: true,
            lineWidth: 10,
            lineCap: 'circle'
        };

        activate();

        function activate() {
            var project_id = $stateParams.id;
            var definition_id = $stateParams.definition_id;
            loadAlarmDefinition(project_id, definition_id);
        }

        function loadAlarmDefinition(project_id, definition_id) {
            definitionService.getOne(project_id, definition_id)
                .then(function (response) {
                    console.log(response);
                    defPreviewVm.meta = response.meta;
                    defPreviewVm.definition = response.data;
                    defPreviewVm.rules = response.data.relationships['single-rules'].data;
                    defPreviewVm.multiRule = response.data.relationships['multi-rule'].data;
                    loadAlarms(project_id, definition_id);
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function loadAlarms(project_id, defintion_id) {
            alarmsService.getAllByDefinition(project_id, defintion_id)
                .then(function (response) {
                    defPreviewVm.alarms = response.data;
                    countResolved();
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function countResolved() {
            defPreviewVm.resolvedAlarms = 0;
            defPreviewVm.resolvedPercentage = 0;
            _.forEach(defPreviewVm.alarms, function (value) {
                if (value.attributes.resolved) {
                    defPreviewVm.resolvedAlarms += 1;
                }
            });
            defPreviewVm.resolvedPercentage = Math.round(defPreviewVm.resolvedAlarms / defPreviewVm.alarms.length * 100.0);
        }

    }
})();
