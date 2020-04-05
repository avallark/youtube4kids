function saveCallDetails(doctorId){
    var url="/api/v0/doctors/call/"+doctorId+"/create";

    var data={ "call_sales_rep_user_i": $("#"+doctorId+"-call-sales-rep-user-id").val(),
               "call-doctor-id": $("#"+doctorId+"-call-doctor-id").val(),
               "call-comments": $("#"+doctorId+"-call-comments").val(),
               "created-by": $("#"+doctorId+"-createdBy").val(),
               "call-loc-lattitude": $("#lattitude").val(),
               "call-loc-longitude": $("#longitude").val()};
    $.post({"url": url,
            "data": data,
            "success": savedCallDetails,
            "datatype": "json"});

}
function savedCallDetails(dataJson){
    if (dataJson["status"] == 1) {
        $("#"+dataJson["doctor-id"]+"-call-seen-button").hide();
        $("#"+dataJson["doctor-id"]+"-call-seen-already").show();

        var callId = dataJson["call-id"];
        var doctorId = dataJson["doctor-id"];

        $("#"+doctorId+"-call-from input:checkbox:checked").each(function() {
            var itemId = $(this).attr("data-item-id"); // do your staff with each checkbox
            console.log("saving item id is - "+ itemId);
            var url="/api/v0/call/"+callId+"/items/"+itemId+"/save";
            $.post({"url": url,
                    "success": savedCallItems,
                    "datatype": "json"});
        });
    }
    else {
        alert(dataJson["message"]);
    }
}
function savedCallItems(dataJson){
    console.log(dataJson["message"]);
}

function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else {
        x.innerHTML = "Geolocation is not supported by this browser.";
    }
}

function showPosition(position) {
    var lat = position.coords.latitude;
    var lon = position.coords.longitude;
    $("#lattitude").val(lat);
    $("#longitude").val(lon);
    console.log(lat+" , "+ lon);
}
