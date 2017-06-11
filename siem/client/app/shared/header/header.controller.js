(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['CONFIG', '$scope', '$localStorage', '$rootScope', '$log'];

    function HeaderController(CONFIG, $scope, $localStorage, $rootScope, $log) {
        var headerVm = this;

        headerVm.notificationCount = 0;
        headerVm.notifications = [];

        headerVm.resetNotifications = resetNotifications;

        $rootScope.$on('userLoggedIn', function (event, message) {
            connect(message);
        });

        function resetNotifications() {
            headerVm.notificationCount = 0;
            $scope.$apply();
        }

        function connect(username) {
            var socket = new SockJS(CONFIG.SUBSCRIPTION_URL);
            headerVm.stompClient = Stomp.over(socket);
            headerVm.stompClient.connect({}, function (frame) {
                headerVm.stompClient.subscribe('/publish/threat/' + username, function (response) {
                    showAlarm(JSON.parse(response.body));
                });
            });
        }

        function disconnect() {
            if (headerVm.stompClient != null) {
                headerVm.stompClient.disconnect();
            }
            $log.info("Disconnected");
        }

        function showAlarm(data) {
            if (data) {
                headerVm.notificationCount += 1;
                data.log.time = new Date();
                headerVm.notifications.unshift(data);
                if (headerVm.notifications.length > 10) {
                    headerVm.notifications.pop();
                }
                $scope.$apply();
            }
        }

    }
})();
