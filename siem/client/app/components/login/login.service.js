(function() {
    'use strict';

    angular
        .module('soteria-app')
        .service('loginService', loginService);

    loginService.$inject = ['$http', '$log', 'CONFIG'];

    function loginService($http, $log, CONFIG) {
        var service = {
          auth: auth,
          loggedInUser: loggedInUser
        };

        return service;

        function auth(username, password) {
            return $http.post(CONFIG.SERVICE_URL + '/users/auth?username=' + username + '&password=' + password)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };

        function loggedInUser() {
            return $http.get(CONFIG.SERVICE_URL + '/users/me')
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };
    }
})();
