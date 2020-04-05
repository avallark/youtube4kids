function drawCharts(userId){
    /* top doctors */

    var url = "/api/v0/performance/user-id/"+userId+"/top-doctors";
    $.get(url, topDoctors, "json");
    console.log(url);
    var url2 = "/api/v0/performance/user-id/"+userId+"/top-items";
    $.get(url2, topItems, "json");
    console.log(url2);
}

function topDoctors(dataJson){
    if (dataJson["status"] == 1){
        var barData = {
            labels: dataJson["top-doctors-list-bar"]["labels"],
            datasets: [
                {
                    label: "Doctor Visited - 6 months",
                    backgroundColor: ["rgba(26,179,148,0.5)",
                                      "rgba(54, 162, 235, 0.2)",
                                      'rgba(255, 206, 86, 0.2)',
                                      'rgba(75, 192, 192, 0.2)',
                                      'rgba(153, 102, 255, 0.2)',
                                      'rgba(255, 159, 64, 0.2)'],
                    borderColor: ["rgba(26,179,148,0.7)" ,
                                  "rgba(54, 162, 235, 1)",
                                  'rgba(255, 206, 86, 1)',
                                  'rgba(75, 192, 192, 1)',
                                  'rgba(153, 102, 255, 1)',
                                  'rgba(255, 159, 64, 1)'],
                    pointBackgroundColor: "rgba(26,179,148,1)",
                    pointBorderColor: "#fff",
                    data: dataJson["top-doctors-list-bar"]["values"]
                }
            ]
        };

        var barOptions = {
            responsive: true,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        };

        var ctx2 = document.getElementById("barChart1").getContext("2d");
        new Chart(ctx2, {type: 'bar', data: barData, options:barOptions});

    }
    else{
        console.log(dataJson["message"]);
    }
}

function topItems(dataJson){
    if (dataJson["status"] == 1){
        var barData = {
            labels: dataJson["top-items-promoted-bar"]["labels"],
            datasets: [
                {
                    label: "Items Promoted",
                    backgroundColor: ["rgba(26,179,148,0.5)",
                                      "rgba(54, 162, 235, 0.2)",
                                      'rgba(255, 206, 86, 0.2)',
                                      'rgba(75, 192, 192, 0.2)',
                                      'rgba(153, 102, 255, 0.2)',
                                      'rgba(255, 159, 64, 0.2)'],
                    borderColor: ["rgba(26,179,148,0.7)" ,
                                  "rgba(54, 162, 235, 1)",
                                  'rgba(255, 206, 86, 1)',
                                  'rgba(75, 192, 192, 1)',
                                  'rgba(153, 102, 255, 1)',
                                  'rgba(255, 159, 64, 1)'],
                    pointBackgroundColor: "rgba(26,179,148,1)",
                    pointBorderColor: "#fff",
                    data: dataJson["top-items-promoted-bar"]["values"]
                }
            ]
        };
        var barOptions = {
            responsive: true,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        };

        var ctx2 = document.getElementById("barChart2").getContext("2d");
        new Chart(ctx2, {type: 'bar', data: barData, options:barOptions});
    }
    else{
        console.log(dataJson["message"]);
    }
}
