(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('usersService', usersService);

    usersService.$inject = ['CONFIG', '$http', '$log'];

    function usersService(CONFIG, $http, $log) {
        var service = {
            getUserByEmail: getUserByEmail
        };

        return service;

        function getUserByEmail(email) {
            return $http.get(CONFIG.SERVICE_URL + '/users/' + email)
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        }
    }
})();
