$(document).ready(function() {
    // $.ajax({
    //     url: "test_servlet",
    //     type: "post",
    //     datatype: "json",
    //     success: function(result, status, xhr) {
    //         var chart = c3.generate({
    //             data: {
    //                 columns: [
    //                     ['sample', 30, 200, 100, 400, 150, 250]
    //                 ]
    //             },
    //             subchart: {
    //                 show: true
    //             }
    //         });
    //
    //
    //
    //
    //
    //     }
    // });

    var chart = c3.generate({
        bindto: '#my-chart',
        data: {
            json: [
                {name: 'www.site1.com', upload: 200, download: 200, total: 400},
                {name: 'www.site2.com', upload: 100, download: 300, total: 400},
                {name: 'www.site3.com', upload: 300, download: 200, total: 500},
                {name: 'www.site4.com', upload: 400, download: 100, total: 500}
            ],
            keys: {
                // x: 'name', // it's possible to specify 'x' when category axis
                value: ['upload', 'download']
            }
        },
        axis: {
            x: {
                // type: 'category'
            }
        }
    });


});




