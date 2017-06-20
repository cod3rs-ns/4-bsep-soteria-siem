(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['CONFIG', 'loginService', '$scope', '$localStorage', '$rootScope', '$log', '$state', '_'];

    function HeaderController(CONFIG, loginService, $scope, $localStorage, $rootScope, $log, $state, _) {
        var headerVm = this;

        headerVm.notificationCount = 0;
        headerVm.notifications = [];
        headerVm.$storage = $localStorage.$default({
            role: 'guest'
        });
        headerVm.loggedInUser = {};

        headerVm.resetNotifications = resetNotifications;
        headerVm.logout = logout;

        activate();

        function activate() {
            if(!_.isUndefined($localStorage.user)) {
                connect($localStorage.user);
            }
        }

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
                data.time = new Date();
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
                    $localStorage.image = 'https://i.imgur.com/HQ3YU7n.gif';
                    $localStorage.userInfo = headerVm.loggedInUser.attributes.firstName + " " + headerVm.loggedInUser.attributes.lastName;
                    if (_.isEqual($localStorage.role, 'OPERATOR')) {
                        $localStorage.image = 'https://graph.facebook.com/' + $localStorage.user.substring(3) + '/picture?type=square'
                    }
                })
                .catch(function (error) {
                    $log.error(error);
                });
        }
    }
})();
