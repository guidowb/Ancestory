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
			otherwise({
				redirectTo: '/names'
			});
		}
	]
);

ancestoryControllers = angular.module('ancestoryControllers', []);
