google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    // var data = google.visualization.arrayToDataTable([
    //     ['Time', 'Likes'],
    //     ['2004',  1000],
    //     ['2005',  1170,      460],
    //     ['2006',  660,       1120],
    //     ['2007',  1030,      540]
    // ]);
    var data = google.visualization.arrayToDataTable([
            ['Employee Name', 'Salary'],
            ['Mike', 900500], // Format as "22,500".
            ['Bob', 35000],
            ['Alice', 44000],
            ['Frank', 27000],
            ['Floyd', 92000],
            ['Fritz', 18500]
        ],
        false);

    var options = {
        title: 'Company Performance',
        curveType: 'function',
        legend: { position: 'bottom' }
    };

    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

    chart.draw(data, options);
}