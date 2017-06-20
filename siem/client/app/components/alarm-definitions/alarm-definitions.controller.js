(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('AlarmDefinitionsController', AlarmDefinitionsController);

    AlarmDefinitionsController.$inject = ['CONFIG', '$stateParams', '$log', 'definitionService', '_', '$scope'];

    function AlarmDefinitionsController(CONFIG, $stateParams, $log, definitionService, _, $scope) {
        var defVm = this;

        defVm.definitions = {
            'data': [],
            'next': null,
            'prev': null
        };

        defVm.newDefinition = {
            name: null,
            description: null,
            level: 'INFO',
            message: null,
            type: 'SINGLE',
            multiRule: {
                type: 'multi-rule',
                interval: null,
                'repetition-trigger': null
            },
            rules: []
        };

        defVm.loadAlarmDefinitions = loadAlarmDefinitions;
        defVm.saveAlarmDefinition = saveDefinition;
        defVm.addSingleRule = addSingleRule;

        activate();

        function activate () {
            //$scope.definitionForm.$setPristine();
            //$scope.definitionForm.$setDirty();

            var project_id = $stateParams.id;
            defVm.loadAlarmDefinitions(project_id);
        }

        function loadAlarmDefinitions(project_id) {
            definitionService.getAll(project_id)
                .then(function(response) {
                    defVm.definitions.data = _.concat(defVm.definitions.data, response.data);
                    defVm.definitions.next = response.links.next;
                    defVm.definitions.prev = response.links.prev;
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function saveDefinition() {
            var projectId = $stateParams.id;
            var data = {
                'type': 'alarm-definitions',
                'attributes': {
                    'name': defVm.newDefinition.name,
                    'description': defVm.newDefinition.description,
                    'level': defVm.newDefinition.level,
                    'message': defVm.newDefinition.message,
                    'type': defVm.newDefinition.type
                },
                'relationships': {
                    'single-rules': defVm.newDefinition.rules,
                    'multi-rule': defVm.newDefinition.multiRule
                }
            };
            console.log(data);


            definitionService.create(projectId, data)
                .then(function(response) {
                    if (_.size(defVm.definitions.data) < CONFIG.AGENTS_LIMIT + 21) {
                        defVm.definitions.data.push(response.data);
                    }
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function addSingleRule() {
            var index = _.size(defVm.newDefinition.rules) + 1;
            defVm.newDefinition.rules.push({id: index, value: '', method: 'EQUALS', field: 'MESSAGE', type: 'single-rules'});
        }
    }
})();
