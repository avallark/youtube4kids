$("#hospital-name-select-div").hide();
function searchHospitals() {

    var inputStr = $("#hospital-name").val();
    var districtId = $("#district-id").val();

    if (inputStr.length > 5){
  	console.log("The value of the input field was changed.");
        var url =  "/api/v0/hospitals/"+inputStr+"/district/"+districtId+"/list";
        console.log(url);
        $.get({"url": url,
               "success": searchHospitalsList,
               "dataType": "json"});
    }
    else {
        return;
    }
}

function searchHospitalsList(dataJson) {

    var hospitals = dataJson["hospitals"];
    if (dataJson["status"] == 1){

        if (hospitals.length > 0) {
            $("#hospital-name-select").html(" ");

            for (i=0; i < hospitals.length; i++) {
                optionLine = "<option value=" + hospitals[i]["hospital-id"] + ">" + hospitals[i]["hospital-name"] + "</option>"
                $("#hospital-name-select").html($("#hospital-name-select").html()+optionLine);
            }

            document.getElementById("hospital-name-select").addEventListener("onselect", SelectedHospital);

            $("#hospital-name-input-div").hide();
            $("#hospital-name-select-div").show();
        }
        else {
            console.log("No results found. Will have to create the Hospital");
        }
    }
}

function SelectedHospital() {
    $("#hospital-id").val($("#hospital-name-select").val());
    $("#hospital-name").val($("#hospital-name-select").text());
}

function resetForm() {
    $("#doctor-edit-form")[0].reset();
    $("#hospital-name-select-div").hide();
    $("#hospital-name-input-div").show();
}

function districtChanged(){
    var districtId = $("#district-id").val();
    console.log(districtId);
    // get all working belts in this district
    url = "/api/v0/working-belt/"+ districtId +"/all";
    $.get({"url": url,
           "success": districtsFetched,
           "dataType": "json"});
    console.log("Get working belt called for district - " + districtId);
}

function districtsFetched(dataJson){
    if (dataJson["status"] == 1){
        var workingBelts = dataJson["working-belts"];
        if (workingBelts.length >= 1){
            $("#working-belt-id").html(" ");
            for (i=0; i < workingBelts.length; i++){
                $("#working-belt-id").html($("#working-belt-id").html() +
                                           "<option value="+
                                           workingBelts[i]["working-belt-id"] +
                                           ">" +
                                           workingBelts[i]["working-belt-name"] +
                                           "</option>");
            }
        }
    }
    else {
        console.log("Error fetching working belts for district");
        console.log(dataJson);
    }
}
