(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['loginService', '$log', '$state', '$http', '$localStorage', 'jwtHelper', 'CONFIG', '_'];

    function LoginController(loginService, $log, $state, $http, $localStorage, jwtHelper, CONFIG, _) {
        var loginVm = this;
        
        loginVm.credentials = {};
        loginVm.badCredentials = false;

        loginVm.login = login;

        function login() {
            loginService.auth(loginVm.credentials.username, loginVm.credentials.password)
                .then(function(response) {
                    var token = response.data.attributes.accessToken;
                    var tokenPayload = jwtHelper.decodeToken(token);

                    if (!_.isUndefined(token)) {
                        $http.defaults.headers.common[CONFIG.AUTH_TOKEN] = token;

                        $localStorage.token = token;
                        $localStorage.user = tokenPayload.sub;
                        $localStorage.role = tokenPayload.role.authority;

                        $state.go('home');
                    }
                })
                .catch(function(error) {
                    if (error === "Bad credentials") {
                        loginVm.credentials = {}
                        loginVm.badCredentials = true;
                    }
                });
        }     
    }
})();
