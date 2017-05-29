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
            .state('home', {
                url: "/home",
                data: {
                    pageTitle: "Soteria | Home"
                },
                views: {
                    'content@': {
                        templateUrl: "app/components/home/home.html",
                        controller: "HomeController",
                        controllerAs: "homeVm"
                    }
                }
            })
            .state('login', {
                url: "/login",
                data: {
                    pageTitle: "Soteria | Sign In"
                },
                views: {
                    'content@': {
                        templateUrl: "app/components/login/login.html",
                        controller: "LoginController",
                        controllerAs: "loginVm"
                    }
                }
            })
            .state('register', {
                url: "/register",
                data: {
                    pageTitle: "Soteria | Sign Up"
                },
                views: {
                    'content@': {
                        templateUrl: "app/components/register/register.html",
                        controller: "RegisterController",
                        controllerAs: "registerVm"
                    }
                }
            })
            .state('projects', {
                url: "/projects",
                data: {
                    pageTitle: "Soteria | Your projects"
                },
                views: {
                    'content@': {
                        templateUrl: "app/components/projects/projects.html",
                        controller: "ProjectsController",
                        controllerAs: "projectsVm"
                    }
                }
            })
            .state('project', {
                url: "/project",
                data: {
                    pageTitle: "Soteria | Project details"
                },
                views: {
                    'content@': {
                        templateUrl: "app/components/project/project.html",
                        controller: "ProjectController",
                        controllerAs: "projectVm"
                    }
                }
            });
    });
