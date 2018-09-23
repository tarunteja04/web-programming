var app = angular.module("ttApp",['ngSanitize']);

app.controller('ttCtrl', function ($scope, $http) {

    $scope.langList = [];
    // retreving all the data from the yandex by using get method
    $http.get("https://translate.yandex.net/api/v1.5/tr.json/getLangs?ui=en&key=trnsl.1.1.20180922T231915Z.73a1312edb27e918.0484e11c6e414f0f7957d494cb3d8e1aaad0a429").success(function (data) {
        if(data != null && data.langs != null){
            // assigning the data from the json object data to the langlist
            $scope.langList = data.langs;
        }
    });
    // If the data is not valid
    $http.get("https://translate.yandex.net/api/v1.5/tr.json/getLangs?ui=en&key=trnsl.1.1.20180922T231915Z.73a1312edb27e918.0484e11c6e414f0f7957d494cb3d8e1aaad0a429").error(function (data) {
        alert("There was some error processing your request. Please try after some time.");
    });
//for translating the text here gettranlatetext function is used
    $scope.getTranslateText = function () {
        $scope.textOut = "";
        var sourceText = $scope.sourceText;
        var sourceLan = $scope.sourceLan;
        var destLan = $scope.destLan;
        if (sourceText != null && sourceText != "" && sourceLan != null && sourceLan != "") {
            if(destLan == null || destLan == ""){
                destLan = 'en';
            }
            //handler is assigned with the json object data
            var handler = $http.get("https://translate.yandex.net/api/v1.5/tr.json/translate?" +
                "key=trnsl.1.1.20180922T231915Z.73a1312edb27e918.0484e11c6e414f0f7957d494cb3d8e1aaad0a429&text=" + sourceText + "&" +
                "lang=" + sourceLan + "-" + destLan);
            handler.success(function (data) {
                if (data != null && data.text != null) {
                    $scope.textOut = "<strong>Translated Text : "+ data.text[0]+"</strong>";
                }else{
                    $scope.textOut = "<strong>No Translation Details exist for the Input Details</strong>";
                }
            });
            handler.error(function (data) {
                alert("There was some error processing your request. Please try after some time.");//given data and language not equal
            });
        }else{
            $scope.textOut = "<strong>Source Text or Source Language cannot be empty</strong>";
        }
    }
});