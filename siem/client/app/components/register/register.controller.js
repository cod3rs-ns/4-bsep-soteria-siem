(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['registerService', '$log', 'ngToast', '$scope'];

    function RegisterController(registerService, $log, ngToast, $scope) {
        var registerVm = this;
        
        registerVm.registrationUser = {'role': 'OPERATOR'}
        registerVm.register = register

        function register() {
            registerVm.registrationUser.email = registerVm.registrationUser.email.$$state.value;
            registerVm.registrationUser.username = registerVm.registrationUser.username.$$state.value;

            var user = angular.copy(registerVm.registrationUser);

            $scope.registrationForm.$setPristine();
            $scope.registrationForm.$setDirty();

            registerVm.passwordRetyped = undefined;

            registerService.register({'type': 'users', 'attributes': registerVm.registrationUser})
                .then(function(response) {
                    ngToast.create({
                        className: 'success',
                        content: '<strong>Successfully registered user.</strong>'
                    });
                    registerVm.registrationUser = {};
                })
                .catch(function(error) {
                    $log.error(error);
                });
        };
    }
})();
