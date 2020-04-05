function removeDoctor(doctorId, salesRepId){
    var url = "/api/v0/doctors/"+doctorId+"/sales-rep/"+salesRepId+"/remove";
    $.post({"url": url,
            "success": removedDoctor,
            "dataType": "json"});
}

function removedDoctor(dataJson){
    if (dataJson["status"] == 1){
        $("#row-"+dataJson["doctor-id"]).hide();
    }
}
