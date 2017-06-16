(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$log', 'CONFIG', '$localStorage', '$scope'];

    function HomeController($log, CONFIG, $localStorage, $scope) {
        var homeVm = this;

        homeVm.alarm = null;

        activate();

        function activate() {
            connect($localStorage.user);
        }

        function connect(username) {
            var socket = new SockJS(CONFIG.SUBSCRIPTION_URL);
            homeVm.stompClient = Stomp.over(socket);
            homeVm.stompClient.connect({}, function (frame) {
                $log.info('Connected: ' + frame);
                homeVm.stompClient.subscribe('/publish/threat/' + username, function (response) {
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
                homeVm.alarm = data;
                $scope.$apply();

                var ringSound = new Audio();

                if ( navigator.userAgent.match("Firefox/") ) {
                    ringSound.src = "assets/audio/bell-ringing.ogg";
                }else {
                    ringSound.src = "assets/audio/bell-ringing.mp3";
                }

                ringSound.play();

            }

        }
    }
})();
