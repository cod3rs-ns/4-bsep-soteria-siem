(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('AlarmDefinitionsController', AlarmDefinitionsController);

    AlarmDefinitionsController.$inject = ['CONFIG', '$stateParams', '$log', 'definitionService', '_', '$scope', 'ngToast'];

    function AlarmDefinitionsController(CONFIG, $stateParams, $log, definitionService, _, $scope, ngToast) {
        var defVm = this;

        defVm.projectId = $stateParams.id;

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
        defVm.getLabelColor = getLabelColor;

        activate();

        function activate() {
            var defaultUrl = CONFIG.SERVICE_URL + '/projects/' + defVm.projectId + '/alarm-definitions?page[offset]=0&page[limit]=' + CONFIG.DEFINITIONS_LIMIT;
            defVm.loadAlarmDefinitions(defaultUrl);
        }

        function loadAlarmDefinitions(url) {
            // reset
            defVm.definitions = {
                'data': [],
                'next': null,
                'prev': null
            };
            definitionService.getAll(url)
                .then(function (response) {
                    defVm.definitions.data = _.concat(defVm.definitions.data, response.data);
                    defVm.definitions.next = response.links.next != undefined ? response.links.next : null;
                    defVm.definitions.prev = response.links.prev;
                })
                .catch(function (error) {
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
            definitionService.create(projectId, data)
                .then(function (response) {
                    ngToast.create({
                        className: 'success',
                        content: '<strong>Successfully created new alarm definition.</strong>'
                    });
                    if (_.size(defVm.definitions.data) < CONFIG.DEFINITIONS_LIMIT) {
                        defVm.definitions.data.push(response.data);
                    }
                    // reset model
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

                    // reset form validation
                    $scope.definitionForm.$setPristine();
                    $scope.rulesForm.$setPristine();
                    $scope.multiRuleForm.$setPristine();
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function addSingleRule() {
            var index = _.size(defVm.newDefinition.rules) + 1;
            defVm.newDefinition.rules.push({
                id: index,
                value: '',
                method: 'EQUALS',
                field: 'MESSAGE',
                type: 'single-rules'
            });
        }

        function getLabelColor(logLevel) {
            switch (_.toUpper(logLevel)) {
                case 'ERROR':
                    return 'label label-danger';
                case 'HIGH':
                    return 'label label-warning';
                case 'MEDIUM':
                    return 'label label-primary';
                case 'LOW':
                    return 'label label-default';
                case 'INFO':
                    return 'label label-info';
                default:
                    return 'label label-default';
            }
        }
    }
})();
