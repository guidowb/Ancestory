var ancestoryApp = angular.module('ancestoryApp',
	[
	 	'ngRoute',
	 	'ui.bootstrap',
	 	'ancestoryControllers'
	]
);

ancestoryApp.config(
	['$routeProvider', '$locationProvider',
	 	function($routeProvider, $locationProvider) {
			$routeProvider.
				when('/ancestory/names', {
					templateUrl: 'views/name-list.html',
					controller: 'namelistController'
				}).
				when('/ancestory/places', {
					templateUrl: 'views/place-list.html',
					controller: 'placelistController'
				}).
				when('/ancestory/users', {
					templateUrl: 'views/user-list.html',
					controller: 'userlistController'
				}).
				otherwise({
					redirectTo: '/ancestory/names'
				});
			$locationProvider.html5Mode(true);
		}
	]
);

ancestoryApp.run(
	['$http',
	 	function($http) {
			$http.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
		}
	]
);

ancestoryControllers = angular.module('ancestoryControllers', []);
