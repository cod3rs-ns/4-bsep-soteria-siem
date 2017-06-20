(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('registerService', registerService);

    registerService.$inject = ['$http', '$log', 'CONFIG'];

    function registerService($http, $log, CONFIG) {
        var service = {
            register: register
        };

        return service;

        function register(userData) {
            return $http.post(CONFIG.SERVICE_URL + '/users', { 'data': userData })
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };
    }
})();
