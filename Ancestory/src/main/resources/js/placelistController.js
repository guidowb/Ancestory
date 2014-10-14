ancestoryControllers.controller('placelistController', ['$scope', '$http',
	function ($scope, $http) {
		$http.get('/api/places').success(function(data) {
			$scope.places = data;
		});
	}
]);
