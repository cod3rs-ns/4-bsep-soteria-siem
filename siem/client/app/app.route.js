angular
    .module('soteria-app', [
        'ui.router'
    ])
    .factory('_', ['$window',
        function ($window) {
            // place Lodash include before Angular
            return $window._;
        }
    ])
    .config(function ($stateProvider, $urlRouterProvider) {

        // For any unmatched url, redirect to /login
        $urlRouterProvider.otherwise("/login");

        $stateProvider
            .state('/', {
                url: "/login",
                data: {
                    pageTitle: 'Soteria | Sign In'
                },
                views: {
                    'content@': {
                        templateUrl: "app/components/login/login.html",
                        controller: "LoginController",
                        controllerAs: "loginVm"
                    }
                }
            });
    });
