(function () {
    'use strict';

    angular
        .module('soteria-app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$scope', 'loginService', 'fbService', '$log', '$state', '$http', '$localStorage', 'jwtHelper', 'ngToast', 'CONFIG', '_'];

    function LoginController($scope, loginService, fbService, $log, $state, $http, $localStorage, jwtHelper, ngToast, CONFIG, _) {
        var loginVm = this;

        loginVm.credentials = {};
        loginVm.badCredentials = false;

        loginVm.login = login;
        loginVm.fbLogin = fbLogin;

        function login() {
            loginService.auth(loginVm.credentials.username, loginVm.credentials.password)
                .then(function (response) {
                    var token = response.data.attributes.accessToken;
                    var tokenPayload = jwtHelper.decodeToken(token);

                    if (!_.isUndefined(token)) {
                        $http.defaults.headers.common[CONFIG.AUTH_TOKEN] = token;

                        $localStorage.token = token;
                        $localStorage.user = tokenPayload.sub;
                        $localStorage.role = tokenPayload.role.authority;
                        $scope.$emit('userLoggedIn', $localStorage.user);

                        $state.go('home');
                    }
                })
                .catch(function (error) {
                    if (error === "Bad credentials") {
                        loginVm.credentials = {}
                        loginVm.badCredentials = true;
                    }
                });
        }

        function fbLogin() {
            fbService.checkLoginStatus()
                .then(function (response) {
                    saveFbUserIfNotExists(response.token, response.user)
                })
                .catch(function (response) {
                    if (_.isEqual(response, "Not logged in")) {
                        fbService.login()
                            .then(function (response) {
                                saveFbUserIfNotExists(response.token, response.user);
                            })
                            .catch(function (response) {
                                $log.error("Can't connect to fb: " + response);
                                ngToast.create({
                                    className: 'error',
                                    content: '<strong>Problem connect with fb credentials. Need to allow all permissions.</strong>'
                                });
                            })
                    }
                });
        }

        function saveFbUserIfNotExists(token, user) {
            fbService.registerFbUser({ 'type': 'users', 'attributes': createUserFromFb(user) })
                .then(function (response) {
                    $localStorage.token = "bearer " + token;
                    $localStorage.user = response.data.attributes.username;
                    $localStorage.role = "OPERATOR";

                    $scope.$emit('userLoggedIn', $localStorage.user)
                    $state.go('home');
                })
                .catch(function (error) {
                    $log.error("Can't create fb user in db: " + response);
                })
        }

        function createUserFromFb(user) {
            return {
                'username': 'fb_' + user.id,
                'email': user.email,
                'role': 'OPERATOR',
                'password': 'default',
                'firstName': user.first_name,
                'lastName': user.last_name
            }
        }
    }
})();
