var ancestoryApp = angular.module('ancestoryApp', []);

ancestoryApp.controller('NameListCtrl', function ($scope, $http) {
	$http.get('/api/names').success(function(data) {
		$scope.names = data;
	});
});