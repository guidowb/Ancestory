ancestoryControllers.controller('userlistController', ['$scope', '$modal', '$http',
	function ($scope, $modal, $http) {
		$scope.update = function() {
			$http.get('/api/users').success(function(data) {
				$scope.users = data;
			});
		}
		$scope.addUser = function() {
			var userDialog = $modal.open({
				templateUrl: 'views/user-dialog.html',
				controller: 'userdialogController'
			});
			userDialog.result.then(function (user) {
				$scope.update();
			});
		}
		$scope.update();
	}
]);
