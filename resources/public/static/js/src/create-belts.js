function salesMarked(workingBeltId, userId){
    if ($('#'+userId+'-sales-checkbox').prop("checked") == true){
        var url= "/api/v0/working-belt/"+workingBeltId+"/user/"+userId+"/add";
        $.post({"url": url,
                "success": salesSaved,
                "datatype": "json"});
    }
    else {
        var url= "/api/v0/working-belt/"+workingBeltId+"/user/"+userId+"/delete";
        $.post({"url": url,
                "success": salesDeleted,
                "datatype": "json"});
    }
}

function salesSaved(dataJson){
    console.log(dataJson["message"]);
}
function salesDeleted(dataJson){
    console.log(dataJson["message"]);
}
