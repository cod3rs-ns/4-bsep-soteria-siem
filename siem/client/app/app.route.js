angular
    .module('soteria-app', [
        'ui.router',
        'angular-jwt',
        'ngStorage',
        'ngToast',
        'ngStomp',
        'angularMoment'
    ])
    .factory('_', ['$window',
        function ($window) {
            // place Lodash include before Angular
            return $window._;
        }
    ])
    .constant(
    'CONFIG', {
        'SERVICE_URL': 'http://localhost:9091/api',
        'SUBSCRIPTION_URL': 'http://localhost:9091/subscriber/register',
        'AUTH_TOKEN': 'X-Auth-Token',
        'AGENTS_LIMIT': 6
    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider) {

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
                url: "/project/:id",
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
            })
            .state('create-project', {
                url: "/create-project",
                data: {
                    pageTitle: "Soteria | Create project"
                },
                views: {
                    'content@': {
                        templateUrl: "app/components/project/project-creation.html",
                        controller: "ProjectCreationController",
                        controllerAs: "projectCreationVm"
                    }
                }
            })
            .state('alarms', {
                url: "/alarms",
                data: {
                    pageTitle: "Soteria | Alarms"
                },
                views: {
                    'content@': {
                        templateUrl: "app/components/alarms/alarms.html",
                        controller: "AlarmsController",
                        controllerAs: "alarmsVm"
                    }
                }
            })
            .state('pageNotFound', {
                url: '/page-not-found',
                views: {
                    'content@': {
                        templateUrl: "app/components/error-templates/page-not-found.html",
                    }
                }
            });


        $httpProvider.interceptors.push(['$q', '$location', '$localStorage', 'jwtHelper', '_', function ($q, $location, $localStorage, jwtHelper, _) {
            return {
                // Set Header to Request if user is logged
                'request': function (config) {
                    var token = $localStorage.token;

                    if (token != "null") {
                        config.headers['X-Auth-Token'] = token;
                    }
                    return config;
                },

                // Catch refreshed token
                'response': function (response) {
                    var token = response.headers('X-Auth-Token');
                    if (!_.isNull(token) && !_.isNull(response.headers('X-Auth-Refreshed'))) {
                        var tokenPayload = jwtHelper.decodeToken(token);
                        $localStorage.token = token;
                        $localStorage.user = tokenPayload.sub;
                        $localStorage.role = tokenPayload.role.authority;
                    }
                    return response;
                },

                // When try to get Unauthorized or Forbidden page
                'responseError': function (response) {
                    // If you get Unauthorized on login page you should just write message
                    if ("/login" !== $location.path()) {
                        if (response.status === 401 || response.status === 403) {
                            $location.path('/page-not-found');
                        }
                        return $q.reject(response);
                    }
                }
            };
        }]);
    });
