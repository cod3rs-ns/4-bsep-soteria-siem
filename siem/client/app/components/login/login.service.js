(function() {
    'use strict';

    angular
        .module('soteria-app')
        .service('loginService', loginService);

    loginService.$inject = ['$http', '$log', 'CONFIG'];

    function loginService($http, $log, CONFIG) {
        var service = {
          auth: auth
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
    }
})();
