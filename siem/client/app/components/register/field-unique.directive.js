angular
    .module('soteria-app')
    .directive('fieldUnique', fieldUnique);

fieldUnique.$inject = ['$http', 'CONFIG']

function fieldUnique($http, CONFIG) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attrs, ctrl) {
            function customValidator(ngModelValue) {
                attrName = element.attr("name");

                if (attrName == 'username') {
                    return $http.get(CONFIG.SERVICE_URL + "/users/username/available",
                        { params: { username: element.val() } })
                        .then(function (response) {
                            var available = response.data.data.attributes.available;
                            if (available != true && available != false)
                                ctrl.$setValidity('unique', true);
                            else
                                ctrl.$setValidity('unique', available);
                            return ngModelValue;
                        });
                }
                else if (attrName == 'email') {
                    if (/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(ngModelValue)) {
                        ctrl.$setValidity('emailValidator', true);
                    } else {
                        ctrl.$setValidity('emailValidator', false);
                    }

                    return $http.get(CONFIG.SERVICE_URL + "/users/email/available",
                        { params: { email: element.val() } })
                        .then(function (response) {
                            var available = response.data.data.attributes.available;
                            if (available != true && available != false)
                                ctrl.$setValidity('unique', true);
                            else
                                ctrl.$setValidity('unique', available);
                            return ngModelValue;
                        });
                }
            }
            ctrl.$parsers.push(customValidator);
        }
    };
}
