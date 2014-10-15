ancestoryControllers.controller('namelistController', ['$scope', '$http',
	function ($scope, $http) {
		$http.get('/api/names').success(function(data) {
			$scope.names = data;
		});
	}
]);
