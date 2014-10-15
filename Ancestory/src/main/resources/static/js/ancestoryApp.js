var ancestoryApp = angular.module('ancestoryApp',
	[
	 	'ngRoute',
	 	'ancestoryControllers'
	]
);

ancestoryApp.config(
	['$routeProvider',
	 	function($routeProvider) {
			$routeProvider.
			when('/names', {
				templateUrl: 'views/name-list.html',
				controller: 'namelistController'
			}).
			when('/places', {
				templateUrl: 'views/place-list.html',
				controller: 'placelistController'
			}).
			otherwise({
				redirectTo: '/names'
			});
		}
	]
);

ancestoryControllers = angular.module('ancestoryControllers', []);
