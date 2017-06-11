(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('AlarmsController', AlarmsController);

    AlarmsController.$inject = ['alarmsService', 'projectService', 'projectsService', 'CONFIG', '$log', '$localStorage', '_'];

    function AlarmsController(alarmsService, projectService, projectsService, CONFIG, $log, $localStorage, _) {
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

        alarmsVm.prevResolvedAlarms = getResolvedAlarms;
        alarmsVm.nextResolvedAlarms = getResolvedAlarms;
        alarmsVm.prevNotResolvedAlarms = getNotResolvedAlarms;
        alarmsVm.nextNotResolvedAlarms = getNotResolvedAlarms;
        alarmsVm.resolveAlarm = resolveAlarm;

        activate();

        function activate() {
            getUserAlarms(CONFIG.SERVICE_URL + '/alarms', alarmsVm.resolvedAlarms);
            getUserAlarms(CONFIG.SERVICE_URL + '/alarms?page[offset]=0&page[limit]=3&filter[resolved]=false', alarmsVm.notResolvedAlarms);
            connect($localStorage.user);
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

        function resolveAlarm(alarmId) {
            alarmsService.resolveAlarm(alarmId)
                .then(function (response) {
                    activate();
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }

        function connect(username) {
            var socket = new SockJS(CONFIG.SUBSCRIPTION_URL);
            alarmsVm.stompClient = Stomp.over(socket);
            alarmsVm.stompClient.connect({}, function (frame) {
                alarmsVm.stompClient.subscribe('/publish/threat/' + username, function (response) {
                    showAlarm(JSON.parse(response.body));
                });
            });
        }

        function disconnect() {
            if (alarmsVm.stompClient != null) {
                alarmsVm.stompClient.disconnect();
            }
            $log.info("Disconnected");
        }

        function showAlarm(data) {
            if (data) {
                $log.info('Alarm triggered: ' + data);
                getUserAlarms(CONFIG.SERVICE_URL + '/alarms?page[offset]=0&page[limit]=3&filter[resolved]=false', alarmsVm.notResolvedAlarms);
            }
        }
    }
})();
