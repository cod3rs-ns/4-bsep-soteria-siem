(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$log', 'CONFIG'];

    function HomeController($log, CONFIG) {
        var homeVm = this;

        connect(1);

        function connect(projectId) {
            var socket = new SockJS(CONFIG.SUBSCRIPTION_URL);
            homeVm.stompClient = Stomp.over(socket);
            homeVm.stompClient.connect({}, function(frame) {
                $log.info('Connected: ' + frame);
                homeVm.stompClient.subscribe('/publish/threat/' + projectId, function(response) {
                    showAlarm(JSON.parse(response.body));
                });
            });
        }

        function disconnect() {
            if (homeVm.stompClient != null) {
                homeVm.stompClient.disconnect();
            }
            $log.info("Disconnected");
        }

        function showAlarm(data) {
            if (data) {
                $log.info('Alarm triggered: ' + data);
            }
        }
    }
})();
