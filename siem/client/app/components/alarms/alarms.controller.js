(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('AlarmsController', AlarmsController);

    AlarmsController.$inject = ['alarmsService', 'projectService', 'CONFIG', '$log', '_'];

    function AlarmsController(alarmsService, projectService, CONFIG, $log, _) {
        var alarmsVm = this;

        alarmsVm.resolvedAlarms = {
            "data": [],
            "next": null,
            "prev": null
        }

        alarmsVm.notResolvedAlarms = {
            "data": [],
            "next": null,
            "prev": null
        }

        alarmsVm.prevResolvedAlarms = getResolvedAlarms
        alarmsVm.nextResolvedAlarms = getResolvedAlarms
        alarmsVm.prevNotResolvedAlarms = getNotResolvedAlarms
        alarmsVm.nextNotResolvedAlarms = getNotResolvedAlarms

        activate();

        function activate() {
            getUserAlarms(CONFIG.SERVICE_URL + '/alarms', alarmsVm.resolvedAlarms);
            getUserAlarms(CONFIG.SERVICE_URL + '/alarms?page[offset]=0&page[limit]=3&filter[resolved]=false', alarmsVm.notResolvedAlarms);
        }

        function getResolvedAlarms(url) {
            getUserAlarms(url, alarmsVm.resolvedAlarms)
        }

        function getNotResolvedAlarms(url) {
            getUserAlarms(url, alarmsVm.notResolvedAlarms)
        }

        function getUserAlarms(url, alarms) {
            alarmsService.getAlarmsForUser(url)
                .then(function (response) {
                    alarms.data = response.data;
                    _.forEach(alarms.data, function (alarm, index) {
                        alarmsService.getLogById(CONFIG.SERVICE_URL.substr(0, 21) + alarm.relationships.log.links.related)
                            .then(function (response) {
                                alarms.data[index].attributes.log = response.data;
                                projectService.getProjectById(response.data.relationships.project.data.id)
                                    .then(function (response) {
                                        alarms.data[index].attributes.project = response.data;
                                    })
                                    .catch(function (error) {
                                        $log.error(error);
                                    });
                            })
                            .catch(function (error) {
                                $log.error(error);
                            });
                    });

                    alarms.next = response.links.next;
                    alarms.prev = response.links.prev;
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }
    }
})();
