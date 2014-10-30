ancestoryControllers.controller('userdialogController', ['$scope', '$modalInstance', '$http',
	function ($scope, $dialog, $http) {
		$scope.username = '';
		$scope.password = '';
		$scope.password2 = '';
		$scope.ok = function() {
			var user = { username: $scope.username, password: $scope.password };
			$http.post('/api/users', user).success(function(data) {
				$dialog.close($scope.user);
			});
		}
		$scope.cancel = function() {
			$dialog.dismiss('cancel');
		}
	}
]);

ancestoryControllers.directive('usernameValidator', [ '$q', '$http', function($q, $http) {
	return {
		restrict: 'A',
		require: '?ngModel',
		link: function(scope, element, attrs, ngModel) {
			if (!ngModel) return;
			if (!ngModel.$validators) return;
			console.log(ngModel);
			ngModel.$validators.required = function(modelValue, viewValue) {
				if (ngModel.$isEmpty(modelValue)) return false;
				return true;
			}
			ngModel.$asyncValidators.unique = function(modelValue, viewValue) {
				var deferred = $q.defer();
				$http.get('/api/users/' + modelValue).success(function() {
					deferred.reject(); // Username was found
				}).error(function() {
					deferred.resolve(); // Either not found or we can't be sure
				});
				return deferred.promise;
			}
		}
	}
}]);

ancestoryControllers.directive('passwordValidator', function() {
	return {
		restrict: 'A',
		require: '?ngModel',
		link: function(scope, element, attrs, ngModel) {
			if (!ngModel) return;
			if (!ngModel.$validators) return;
			ngModel.$validators.required = function(modelValue, viewValue) {
				if (ngModel.$isEmpty(modelValue)) return false;
				return true;
			}
		}
	}
});

ancestoryControllers.directive('equals', function() {
	return {
		restrict: 'A',
		require: '?ngModel',
		link: function(scope, element, attrs, ngModel) {
			if (!ngModel) return;
			if (!ngModel.$validators) return;
			attrs.$observe('equals', function() { ngModel.$validate(); })
			ngModel.$validators.equals = function(value) {
				return value == attrs.equals;
			}
		}
	}
});