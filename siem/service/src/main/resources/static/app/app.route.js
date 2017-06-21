angular
    .module('soteria-app', [
        'ui.router',
        'angular-jwt',
        'ngStorage',
        'ngToast',
        'ngStomp',
        'angularMoment',
        'ngSanitize',
        'easypiechart',
        'chart.js',
        'daterangepicker',
        'ui.bootstrap'
    ])
    .factory('_', ['$window',
        function ($window) {
            // place Lodash include before Angular
            return $window._;
        }
    ])
    .constant(
    'CONFIG', {
        'SERVICE_URL': 'https://localhost:8443/api',
        'SUBSCRIPTION_URL': 'https://localhost:8443/subscriber/register',
        'AUTH_TOKEN': 'X-Auth-Token',
        'AGENTS_LIMIT': 6,
        'FB_FIELDS': 'id,first_name,last_name,email',
        'DEFINITIONS_LIMIT': 9
    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider) {

        // For any unmatched url, redirect to /login
        $urlRouterProvider.otherwise('/login');

        $stateProvider
            .state('home', {
                url: '/home',
                data: {
                    pageTitle: 'Soteria | Home'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/home/home.html',
                        controller: 'HomeController',
                        controllerAs: 'homeVm'
                    }
                }
            })
            .state('login', {
                url: '/login',
                data: {
                    pageTitle: 'Soteria | Sign In'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/login/login.html',
                        controller: 'LoginController',
                        controllerAs: 'loginVm'
                    }
                }
            })
            .state('register', {
                url: '/register',
                data: {
                    pageTitle: 'Soteria | Sign Up'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/register/register.html',
                        controller: 'RegisterController',
                        controllerAs: 'registerVm'
                    }
                }
            })
            .state('projects', {
                url: '/projects',
                data: {
                    pageTitle: 'Soteria | Your projects'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/projects/projects.html',
                        controller: 'ProjectsController',
                        controllerAs: 'projectsVm'
                    }
                }
            })
            .state('project', {
                url: '/project/:id',
                data: {
                    pageTitle: 'Soteria | Project details'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/project/project.html',
                        controller: 'ProjectController',
                        controllerAs: 'projectVm'
                    }
                }
            })
            .state('alarm', {
                url: '/project/:projectId/alarm/:alarmId',
                data: {
                    pageTitle: 'Soteria | Alarm details'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/alarms/alarm.html',
                        controller: 'AlarmController',
                        controllerAs: 'alarmVm'
                    }
                }
            })
            .state('alarms', {
                url: '/alarms',
                data: {
                    pageTitle: 'Soteria | Alarms'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/alarms/alarms.html',
                        controller: 'AlarmsController',
                        controllerAs: 'alarmsVm'
                    }
                }
            })
            .state('pageNotFound', {
                url: '/page-not-found',
                views: {
                    'content@': {
                        templateUrl: 'app/components/error-templates/page-not-found.html'
                    }
                }
            })
            .state('internalServerError', {
                url: '/internal-server-error',
                views: {
                    'content@': {
                        templateUrl: 'app/components/error-templates/internal-server-error.html'
                    }
                }
            })
            .state('project.alarm-definitions', {
                parent: 'project',
                url: '/alarm-definitions',
                data: {
                    pageTitle: 'Soteria | Alarm Definitions'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/alarm-definitions/alarm-definitions.html',
                        controller: 'AlarmDefinitionsController',
                        controllerAs: 'defVm'
                    }
                }
            })
            .state('project.alarm-definition', {
                parent: 'project',
                url: '/alarm-definition/:definition_id',
                data: {
                    pageTitle: 'Soteria | Alarm Definition'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/alarm-definitions/alarm-definition-preview.html',
                        controller: 'AlarmDefinitionPreviewController',
                        controllerAs: 'defPreviewVm'
                    }
                }
            })
            .state('project.logs-report', {
                parent: 'project',
                url: '/logs-report',
                data: {
                    pageTitle: 'Soteria | Logs Report'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/reports/reports-logs.html',
                        controller: 'LogReportController',
                        controllerAs: 'reportVm'
                    }
                }
            })
            .state('project.alarms-report', {
                parent: 'project',
                url: '/alarms-report',
                data: {
                    pageTitle: 'Soteria | Alarms Report'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/components/reports/reports-alarms.html',
                        controller: 'AlarmReportController',
                        controllerAs: 'reportVm'
                    }
                }
            });


        $httpProvider.interceptors.push(['$q', '$location', '$localStorage', 'jwtHelper', '_', function ($q, $location, $localStorage, jwtHelper, _) {
            return {
                // Set Header to Request if user is logged
                'request': function (config) {
                    var token = $localStorage.token;

                    if (token != 'null') {
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
                    if ('/login' !== $location.path()) {

                        // It should reject for not found user for collaborator or duplicated collaborator
                        if ((response.status === 404 || response.status === 400) && '/projects' === $location.path()) {
                            return $q.reject(response);
                        }

                        if (response.status === 401 || response.status === 403 || response.status === 404) {
                            $location.path('/page-not-found');
                        } else {
                            $location.path('/internal-server-error');
                        }
                    }
                    return $q.reject(response);
                }
            };
        }]);
    });
