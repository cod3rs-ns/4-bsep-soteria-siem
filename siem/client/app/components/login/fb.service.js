(function () {
    'use strict';

    angular
        .module('soteria-app')
        .service('fbService', fbService);

    fbService.$inject = ['$http', '$log', '$q', 'CONFIG', '_'];

    function fbService($http, $log, $q, CONFIG, _) {
        var service = {
            checkLoginStatus: checkLoginStatus,
            login: login,
            registerFbUser: registerFbUser
        };

        function checkLoginStatus() {
            var deferred = $q.defer();
            FB.getLoginStatus(
                function (response) {
                    if (_.isEqual(response.status, 'connected')) {
                        var token = response.authResponse.accessToken;
                        FB.api('/me', { fields: CONFIG.FB_FIELDS },
                            function (response) {
                                if (!response || response.error)
                                    deferred.reject('Not logged in');
                                else
                                    deferred.resolve({ 'token': token, 'user': response })
                            })
                    }
                    deferred.reject('Not logged in');
                });

            return deferred.promise;
        }

        function login() {
            var deferred = $q.defer();
            FB.login(
                function (response) {
                    if (_.isEqual(response.status, 'connected')) {
                        if (_.includes(response.authResponse.grantedScopes, 'email')) {
                            var token = response.authResponse.accessToken;
                            FB.api('/me', { fields: CONFIG.FB_FIELDS },
                                function (response) {
                                    if (!response || response.error)
                                        deferred.reject(response);
                                    else
                                        deferred.resolve({ 'token': token, 'user': response })
                                });
                        }
                        else {
                            FB.api('/me/permissions', 'delete',
                                function (response) {
                                    deferred.reject(response);
                                });
                            deferred.reject(response);
                        }
                    }
                },
                {
                    scope: 'email,public_profile',
                    return_scopes: true
                }
            );

            return deferred.promise;
        }

        function registerFbUser(userData) {
            return $http.put(CONFIG.SERVICE_URL + '/users/fb', { 'data': userData })
                .then(function successCallback(response) {
                    return response.data;
                }, function errorCallback(response) {
                    $log.warn(response.data.detail);
                    throw response.data.detail;
                });
        };

        return service;
    }
})();
