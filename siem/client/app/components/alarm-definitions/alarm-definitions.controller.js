(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('AlarmDefinitionsController', AlarmDefinitionsController);

    AlarmDefinitionsController.$inject = ['CONFIG', '$stateParams', '$log', 'definitionService', '_', '$scope'];

    function AlarmDefinitionsController(CONFIG, $stateParams, $log, definitionService, _, $scope) {
        var defVm = this;

        defVm.possibleLevels = [
            'INFO',
            'LOW',
            'MEDIUM',
            'HIGH',
            'SEVERE'
        ];

        defVm.definitions = {
            'data': [],
            'next': null,
            'prev': null
        };

        defVm.newDefintion = {
            'name': null,
            'description': null,
            'level': null,
            'rules': []
        };

        defVm.loadAlarmDefinitions = loadAlarmDefinitions;
        //defVm.createAlarmDefinition = createAlarmDefinition;
        defVm.addSingleRule = addSingleRule;

        activate();

        function activate () {
            var id = $stateParams.id;
            defVm.loadAlarmDefinitions(id);
        }

        function loadAlarmDefinitions(project_id) {
            definitionService.getAll(project_id)
                .then(function(response) {
                    defVm.definitions.data = _.concat(defVm.definitions.data, response.data);
                    defVm.definitions.next = response.links.next;
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function saveAgent() {
            var projectId = $stateParams.id;
            var data = {
                'type': 'agents',
                'attributes': {
                    'name': defVm.config.name,
                    'description': defVm.config.description,
                    'agent_version': defVm.config.version,
                    'agent_type': defVm.config.os
                },
                'relationships': {
                    'type': 'projects',
                    'id': projectId
                }
            };

            definitionService.addAgent(projectId, data)
                .then(function(response) {
                    if (_.size(defVm.agents.data) < CONFIG.AGENTS_LIMIT) {
                        defVm.agents.data.push(response.data);
                    }
                    // TODO Download with provided configuration
                })
                .catch(function(error) {
                    $log.error(error);
                });
        }

        function addSingleRule(group) {
            var index = _.size(defVm.config[group]) + 1;
            defVm.config[group].push({'id': index, value: ''});
        }
    }
})();
