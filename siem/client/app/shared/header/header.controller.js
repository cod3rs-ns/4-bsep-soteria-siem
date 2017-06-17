(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['CONFIG', 'loginService', '$scope', '$localStorage', '$rootScope', '$log', '$state'];

    function HeaderController(CONFIG, loginService, $scope, $localStorage, $rootScope, $log, $state) {
        var headerVm = this;

        headerVm.notificationCount = 0;
        headerVm.notifications = [];
        headerVm.$storage = $localStorage.$default({
            role: 'guest'
        });
        headerVm.loggedInUser = {};

        headerVm.resetNotifications = resetNotifications;
        headerVm.logout = logout;

        $rootScope.$on('userLoggedIn', function (event, message) {
            connect(message);
            me();
        });

        function resetNotifications() {
            headerVm.notificationCount = 0;
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

        function logout() {
            headerVm.notificationCount = 0;
            headerVm.notifications = [];
            disconnect();
            $localStorage.$reset();
            $localStorage.role = 'guest';
            $state.go('login');
        }

        function me() {
            loginService.loggedInUser()
                .then(function (response) {
                    headerVm.loggedInUser = response.data;
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }
    }
})();
