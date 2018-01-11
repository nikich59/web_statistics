var app = angular.module("myApp", []);
app.controller("myCtrl", function ($scope, $interval, $http) {
    $scope.sleuthList = [];
    $scope.statisterList = [];
    $scope.update = function () {
        $http.get("list?type=sleuth")
            .then(function (response) {
                    $scope.sleuthList = response.data.items;
                    $scope.sleuthList.forEach(function (p1, p2, p3) {
                        p1.onRemoveClick = function () {
                            $scope.removeSleuth(p1.id);
                        }
                    });
                }
            );
        $http.get("list?type=sleuth_template")
            .then(function (response) {
                    $scope.sleuthTemplates = response.data.items;
                    console.info(response.data);
                }
            );
        $http.get("list?type=statister_template")
            .then(function (response) {
                    $scope.statisterTemplates = response.data.items;
//                    console.info(response.data);
                }
            );
        $http.get("list?type=statister")
            .then(function (response) {
                    $scope.statisterList = response.data.items;
                    $scope.statisterList.forEach(function (p1, p2, p3) {
                        p1.value_description.forEach(function (v, i, a) {
                            v.last_value = p1.last_data[i];
                        });
                        p1.onRemoveClick = function () {
                            $scope.removeStatister(p1.id);
                        }
                    });
                }
            );
    };
    $scope.onAddStatisterButtonClick = function () {
        $scope.add_template = true;
        $scope.templates = $scope.statisterTemplates;
        $scope.onAddTemplateSubmitClick = $scope.onAddStatisterSubmitClick;
        $(".add_sleuth_dialog").show();
    };
    $scope.onAddSleuthButtonClick = function () {
        $scope.add_template = true;
        $scope.templates = $scope.sleuthTemplates;
        $scope.onAddTemplateSubmitClick = $scope.onAddSleuthSubmitClick;
        $(".add_sleuth_dialog").show();
    };
    $scope.onAddTemplateDialogClose = function () {
        $scope.add_template = false;
//        $(".add_sleuth_dialog").close();
    };
    $scope.add_template = false;
    $scope.onSelectedSleuthTemplateChanged = function () {
        var selectedTemplateIndex = $("#sleuth_template_select").prop('selectedIndex');
        $scope.selectedSleuthTemplate = $scope.templates[selectedTemplateIndex];
        $scope.selectedSleuthTemplate.parameters.forEach(function (p1, p2, p3) {
            if (p1.data_type.pref_value != null) {
                p1.value = p1.data_type.pref_value;
            }
        });
    };
    $scope.templates = [];
    $scope.selectedTemplate = {};
    $scope.onAddTemplateSubmitClick = function () {

    };

    $scope.onAddSleuthSubmitClick = function () {
        $scope.addSleuth($scope.selectedSleuthTemplate);
//        var res = $http.post('control', dataObj);
        $scope.onAddTemplateDialogClose();
    };
    $scope.onAddStatisterSubmitClick = function () {
        $scope.addStatister($scope.selectedSleuthTemplate);
//        var res = $http.post('control', dataObj);
        $scope.onAddTemplateDialogClose();
    };

    $scope.addStatister = function (template) {
        var dataObj = {
            command: "add",
            type: "statister",
            template: template
        };
        var res = $http.post('control', dataObj);
    };

    $scope.addSleuth = function (template) {
        var dataObj = {
            command: "add",
            type: "sleuth",
            template: template
        };
        var res = $http.post('control', dataObj);
    };
    $scope.removeSleuth = function (id) {
        var dataObj = {
            command: "remove",
            type: "sleuth",
            id: id
        };
        var res = $http.post('control', dataObj);
    };
    $scope.removeStatister = function (id) {
        var dataObj = {
            command: "remove",
            type: "statister",
            id: id
        };
        var res = $http.post('control', dataObj);
    };

    /*
     var dataObj = {
     name: "name",
     employees: "emp",
     headoffice: "ho"
     };
     var res = $http.post('control', dataObj);
     */

    $scope.updateTimer = $interval(function () {
            $scope.update();
        },
        1000
    );

    $(document).ready(function () {
        $scope.update();
    });
});


