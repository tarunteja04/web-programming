var angularTodo = angular.module('angularTodo', []);

angularTodo.controller('angularTodoC', ['$scope', '$interval', '$filter', function ($scope, $interval, $filter) {
    $scope.todoItem = "";
    $scope.items = [];
    $scope.Timer = null;
    $scope.daymsg = "";

    $scope.init = function () {
        $scope.Timer = $interval(function () {
            //Display the current time.
            var time = $filter('date')(new Date(), 'HH:mm:ss');
            $scope.Message = time;
        }, 1000);

        let today = new Date()
        let curHr = today.getHours()

        if (curHr < 12) {
            $scope.daymsg = 'morning';
        } else if (curHr < 18) {
            $scope.daymsg = 'afternoon';
        } else {
            $scope.daymsg = 'evening';
        }
        if (localStorage.getItem("todos") === null) {
            localStorage.setItem("todos", JSON.stringify($scope.items));
        }
        else {
            $scope.items = JSON.parse(localStorage.getItem("todos"));
        }
    }

    $scope.init();
    $scope.submitNewItem = function () {
        $scope.items.push({"todo": $scope.todoItem, "status": false});
        localStorage.setItem("todos", JSON.stringify($scope.items));
        $scope.todoItem = "";
    };
    $scope.completeItem = function (index) {
        $scope.items[index].status = true;
        localStorage.setItem("todos", JSON.stringify($scope.items));
    };
    $scope.pendingItem = function (index) {
        $scope.items[index].status = false;
        localStorage.setItem("todos", JSON.stringify($scope.items));
    };

    $scope.deleteItem = function (index) {
        $scope.items.splice(index, 1);
        localStorage.setItem("todos", JSON.stringify($scope.items));
    };

}]);