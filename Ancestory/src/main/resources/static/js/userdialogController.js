ancestoryControllers.controller('userdialogController', ['$scope', '$modalInstance', '$http',
	function ($scope, $dialog, $http) {
		$scope.username = {};
		$scope.password = {};
		$scope.ok = function() {
			var user = { username: $scope.username.value, password: $scope.password.value };
			$http.post('/api/users', user).success(function(data) {
				$dialog.close($scope.user);
			});
		}
		$scope.cancel = function() {
			$dialog.dismiss('cancel');
		}
		$scope.valid = function() {
			if (!$scope.username.valid()) return false;
			if (!$scope.password.valid()) return false;
			return true;
		}
		$scope.username.valid = function() {
			$scope.username.errors = {};
			if ($scope.username.value == null || $scope.username.value == '') $scope.username.errors.unset = true;
			for (key in $scope.username.errors) return false;
			return true;
		}
		$scope.password.valid = function() {
			$scope.password.errors = {};
			if ($scope.password.value == null || $scope.password.value == '') $scope.password.errors.unset = true;
			if ($scope.password.value != $scope.password.repeat) $scope.password.errors.dontmatch = true;
			for (key in $scope.password.errors) return false;
			return true;
		}
	}
]);