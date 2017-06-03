(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['registerService', '$log', 'ngToast'];

    function RegisterController(registerService, $log, ngToast) {
        var registerVm = this;
        
        registerVm.registrationUser = {'role': 'OPERATOR'}
        registerVm.register = register

        function register() {
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
