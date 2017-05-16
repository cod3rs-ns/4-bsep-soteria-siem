(function() {
    'use strict';

    angular
        .module('soteria-app')
        .controller('LoginController', LoginController);

    LoginController.$inject = [];

    function LoginController() {
        var loginVm = this;
    }
})();
