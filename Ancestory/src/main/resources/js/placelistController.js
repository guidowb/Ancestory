ancestoryControllers.controller('placelistController', ['$scope', '$http',
	function ($scope, $http) {
		$http.get('/api/places').success(function(data) {
			$scope.places = data;
			$scope.show = false;
			$scope.toggle = function (place) {
				place.show = !place.show;
				if (place.show) {
					$http.get('/api/places/' + place.name + '/events').success(function(data) {
						place.events = data;
					});
				}
			}
		});
	}
]);
