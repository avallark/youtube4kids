function saveTp(userId, dateOn, num){

    var beltId = $("#" + num + "-working-belt-id option:selected").val();

    var data = {"user-id": userId,
                "date-on": dateOn,
                "belt-id": beltId,
                "num": num};
    var previousInsertId = $("#"+num+"-working-belt-id").attr("data-insert-id");

    if (previousInsertId > 0) {
        console.log("Previously inserted. Need to delete - " + previousInsertId);
        var delUrl = "/api/v0/tp/"+previousInsertId;
        $.ajax({url: delUrl,
                type: "DELETE",
                success: function(result){console.log("deleted - "+previousInsertId);}});
    }

    var url = "/api/v0/tp/user/"+userId+"/create";

    $.post({url: url,
            data: data,
            success: savedTp,
            datatype: "json"});
}

function saveTp2(userId, dateOn, num){

    var beltId = $("#" + num + "-working-belt-id-2 option:selected").val();

    var data = {"user-id": userId,
                "date-on": dateOn,
                "belt-id": beltId,
                "num": num};

    var previousInsertId = $("#"+num+"-working-belt-id-2").attr("data-insert-id");

    if (previousInsertId > 0) {
        console.log("Previously inserted. Need to delete - "+previousInsertId);
        var delUrl = "/api/v0/tp/"+previousInsertId;
        $.ajax({url: delUrl,
                type: "DELETE",
                success: function(result){console.log("deleted - "+previousInsertId);}});
    }

    var url = "/api/v0/tp/user/"+userId+"/create";

    $.post({url: url,
            data: data,
            success: savedTp2,
            datatype: "json"});
}

function savedTp(dataJson){
    if (dataJson["status"] == 0) {
        console.log(dataJson["message"]);

    }
    else {
        console.log("Saved - " + dataJson["num"] + " - " + dataJson["insert-id"]);
        $("#"+dataJson["num"]+"-working-belt-id").attr("data-insert-id", dataJson["insert-id"]);
    }
}

function savedTp2(dataJson){
    if (dataJson["status"] == 0) {
        console.log(dataJson["message"]);
    }
    else {
        console.log("Saved - " + dataJson["num"] + " - " + dataJson["insert-id"]);
        $("#"+dataJson["num"]+"-working-belt-id-2").attr("data-insert-id", dataJson["insert-id"]);
    }
}

function showSelect(num){
    console.log("adding column");
    $("#"+num+"-button-div").hide();
    $("#"+num+"-select-div-for-button").show();
}

function resetForm(dateOn, num, userId){


    var previousInsertId = $("#"+num+"-working-belt-id").attr("data-insert-id");

    console.log("1 - "+previousInsertId);

    if (previousInsertId > 0) {
        console.log("Previously inserted. Need to delete - "+previousInsertId);
        var delUrl = "/api/v0/tp/"+previousInsertId;
        $.ajax({url: delUrl,
                type: "DELETE",
                success: function(result){console.log("deleting - "+previousInsertId);}});
    }

    var previousInsertId2 = $("#"+num+"-working-belt-id-2").attr("data-insert-id");

    if (previousInsertId2 > 0) {
        console.log("Previously inserted. Need to delete - "+previousInsertId);
        var delUrl = "/api/v0/tp/"+previousInsertId2;
        $.ajax({url: delUrl,
                type: "DELETE",
                success: function(result){console.log("deleting - "+previousInsertId2);}});
    }

    $("#create-tp-form-"+num)[0].reset();
    $("#"+num+"-button-div").show();
    $("#"+num+"-select-div-for-button").hide();

}

function saveTpStatus(month, year, userId, createdBy){
    var url = "/api/v0/tp/status/create";
    var data = {"month": month,
                "year": year,
                "user-id": userId,
                "created-by": createdBy};
    $.post({"url": url,
            "data": data,
           "success": savedTpStatus,
           "datatype": "json"});
}

function savedTpStatus(dataJson){
    if (dataJson["status"] == 1){
        console.log("Saved - " + dataJson["tp-status-id"]);
        alert("Tour Programme sent for approval.");
        location.reload();
    }
    else {
        console.log("Failed creating TP status entry.");
    }
}
