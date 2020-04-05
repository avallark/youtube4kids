function calculateTA(employeeId, year, month){
    var url = "/api/v0/salary/employee/"+employeeId+"/year/"+year+"/month/"+month+"/calculate/ta";
    console.log(url);
    $.get(url, TACalculated, "json");
}
function TACalculated(dataJson){
    console.log(dataJson);
    if (dataJson["status"] == 1){
        $("#ta-p").html(dataJson["ta"]);
        $("#ta").val(dataJson["ta"]);
        $("#total-cm").prop("readonly", false);
    }
    else {
        $("#ta-p").html(dataJson["message"]);
        $("#ta").val(0);
    }
}

function calculateIncentive(){
    var cm = $("#total-cm").val();
    var url = "/api/v0/salary/cm/"+cm+"/calculate/incentive";
    $.get(url, incentiveCalculated, "json");
}


function incentiveCalculated(dataJson){
    var ta = $("#ta").val();
    var incentiveAdjusted = parseInt(dataJson["incentive"]) - parseInt(ta);
    console.log(dataJson);

    if (dataJson["status"] == 1){

        if (incentiveAdjusted < 0){
            var taAdjusted = parseInt($("#ta").val()) + incentiveAdjusted;
            $("#ta-p").html(taAdjusted);
            $("#ta").val(taAdjusted);
            $("#sales-incentive").val(0);
            $("#sales-incentive-p").html(0);
            alert("Incentive negative. TA adjusted");
        }
        else {
            $("#sales-incentive").val(incentiveAdjusted);
            $("#sales-incentive-p").html(incentiveAdjusted);
        }
    }
    else{
        console.log(dataJson["message"]);
    }
}
