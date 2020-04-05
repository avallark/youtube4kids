function approveTp(userId, month, year, updatedBy){
    var data = {"user-id": userId,
                "month": month,
                "year": year,
                "updated-by": updatedBy};
    var url = "/api/v0/tp/status/approve";

    $.post({"url": url,
            "data": data,
            "datatype": "json",
            "success":approvedTp});
}
function approvedTp(dataJson){
    if (dataJson["status"] == 1){
        alert("TP Approved");
        location.reload();
    }
    else {
        console.log(dataJson["message"]);
        console.log(dataJson["params"]);
    }
}

function rejectTp(userId, month, year){
    var data = {"user-id": userId,
                "month": month,
                "year": year};
    var url = "/api/v0/tp/status/reject";

    $.post({"url": url,
            "data": data,
            "datatype": "json",
            "success":rejectedTp});
}
function rejectedTp(dataJson){
    if (dataJson["status"] == 1){
        alert("TP Rejected");
        location.reload();
    }
    else {
        console.log(dataJson["message"]);
        console.log(dataJson["params"]);
    }
}
