var app = angular.module("myApp", []);
app.controller("myCtrl", function ($scope, $interval, $http) {
    $scope.selectedIndex = -1;
    $scope.onSelectedStatsChanged = function () {
        $scope.selectedIndex = $("#stats_select").prop('selectedIndex');
        $scope.drawBasic();
    };
    $scope.stats = {};

    $scope.model = null;

    $scope.stats.items = [
        {
            id: "1",
            headline: "first"
        },
        {
            id: "2",
            headline: "first"
        },
        {
            id: "3",
            headline: "first"
        }
    ];

    $http.get("visio_list")
        .then(function (response) {
                $scope.stats = response.data;
                console.info($scope.stats);
            }
        );
    /*
     $.get("visio_list", function (data) {
     $scope.stats = $.parseJSON(data);
     console.info($scope.stats);
     $("#stats_select").show();
     });
     */
    google.charts.load('current', {packages: ['corechart', 'line']});
//    google.charts.setOnLoadCallback($scope.drawBasic);

    $scope.drawBasic = function () {
        if ($scope.selectedIndex < 0) {
            return;
        }
        var tableData = new google.visualization.DataTable();
        tableData.addColumn('number', 'X');


        var statistics = {};

        $.get("visio_data?id=" + $scope.stats.items[$scope.selectedIndex].id, function (data) {
            statistics = $.parseJSON(data);
            console.info(statistics);

            statistics.column_names.forEach(function (p1, p2, p3) {
                tableData.addColumn('number', p1);
            });

            tableData.addRows(statistics.items/*.slice(1, 1500)*/);

            var options = {
                hAxis: {
                    title: 'Time'
                }
                /*
                 ,
                 vAxis: {
                 title: 'Popularity'
                 }*/
            };

            var chart = new google.visualization.LineChart(document.getElementById('chart_div'));

            chart.draw(tableData, options);
        });


    }
});


