function confirmedDistrict(){
    $("#create-hospital-form")[0].reset();
    $("#rest-of-hospital-form").show();
    $("#hospital-district-id").val($("#hospital-district-id-1 option:selected").val());
    $("#hospital-district-name").html($("#hospital-district-id-1 option:selected").text());
}

function districtChosen(){
    $("#create-hospital-form")[0].reset();
    $("#hospital-district-id").val($("#hospital-district-id-1 option:selected").val());
    $("#hospital-district-name").html($("#hospital-district-id-1 option:selected").text());
}

function checkHospitalName(){
    var hospitalName = $("#hospital-name").val();
    var districtId = $("#hospital-district-id").val();
    var url = "/api/v0/hospitals/"+hospitalName+"/district/"+districtId+"/list";
    $.get({"url": url,
           "success": checkedHospitalName,
           "datatype": "json"});
}

function checkedHospitalName(dataJson){
    if (dataJson["status"] == 1){
        var hospitalNames = "";
        var hospitals = dataJson["hospitals"];
        if (hospitals.length > 0){
            for (i=0;i<hospitals.length; i++){
                hospitalNames = hospitalNames + hospitals[i]["hospital-name"]+ " - " + hospitals[i]["hospital-id"] + "\n";
            }
            alert("Similar hospital names exists. Check this list -  \n - " + hospitalNames);
            console.log("Similar hospital names exists. Check this list -  \n - " + hospitalNames);
            $("#hospital-name")[0].setCustomValidity("Hospital Name Exists");
        }
        else{
            $("#hospital-name")[0].setCustomValidity("");
        }
    }
    else {
        console.log("Something went wrong while calling the API. Check logs");
    }
}
