var doctorId = 0;

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
    if (lat != null){
        $("#lattitude").val(lat);
        $("#longitude").val(lon);
    }
    else {
        lat = $("#lattitude").val();
        console.log("Old lat - " + lat);
    }
    console.log(lat+" , "+ lon);
}

function submitwithLocation(doctorIdNew) {
    doctorId = doctorIdNew;
    try {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(postPosition);
        } else {
            console.log("Location not available. Proceeding with null location.");
            postWithoutPosition();
        }
        postWithoutPosition();
    }
    catch(err) {
        console.log(err.message);
        postWithoutPosition();
    }
}

function postPosition(position) {
    var lat = position.coords.latitude;
    var lon = position.coords.longitude;
    if (lat != null){
        var url = "/doctors/call/"+doctorId+"/create?lattitude="+lat+"&longitude="+lon;
        window.location.href = url;
    }
    else {
        lat = $("#lattitude").val();
        console.log("Old lat - " + lat);
    }
    console.log(lat+" , "+ lon);
}

function postWithoutPosition() {
    var lat = 0;
    var lon = 0;
    if (lat != null){
        var url = "/doctors/call/"+doctorId+"/create?lattitude="+lat+"&longitude="+lon;
        window.location.href = url;
    }
    else {
        lat = $("#lattitude").val();
        console.log("Old lat - " + lat);
    }
    console.log(lat+" , "+ lon);
}


function itemPromoted(callId, itemId){
    if ($('#'+itemId+'-item-checkbox').prop("checked") == true){
        var url="/api/v0/call/"+callId+"/items/"+itemId+"/save";
        $.post({"url": url,
                "success": itemsSaved,
                "datatype": "json"});
    }
    else {
        var url="/api/v0/call/"+callId+"/items/"+itemId+"/delete";
        $.post({"url": url,
                "success": itemsDeleted,
                "datatype": "json"});
    }
}

function itemsSaved(dataJson){
    console.log(dataJson["message"]);
}
function itemsDeleted(dataJson){
    console.log(dataJson["message"]);
}
