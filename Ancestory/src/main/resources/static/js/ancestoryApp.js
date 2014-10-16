var ancestoryApp = angular.module('ancestoryApp',
	[
	 	'ngRoute',
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
				otherwise({
					redirectTo: '/ancestory/names'
				});
			$locationProvider.html5Mode(true);
		}
	]
);

ancestoryControllers = angular.module('ancestoryControllers', []);
