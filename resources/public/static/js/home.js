function saveCallDetails(doctorId){
    var url="/api/v0/doctors/call/"+doctorId+"/create";
    var data={ "call_sales_rep_user_i": $("#"+doctorId+"-call-sales-rep-user-id").val(),
               "call-doctor-id": $("#"+doctorId+"-call-doctor-id").val(),
               "call-comments": $("#"+doctorId+"-call-comments").val(),
               "created-by": $("#"+doctorId+"-createdBy").val()};
    $.post({"url": url,
            "data": data,
            "success": savedCallDetails,
            "datatype": "json"});
}
function savedCallDetails(dataJson){
    if (dataJson["status"] == 1) {
        var callId = dataJson["call-id"];
        var doctorId = dataJson["doctor-id"];
        var listchecked = $("#"+doctorId+"-call-form").find;
        $(" input:checkbox:checked").each(function() {
            var itemId = $(this).attr("data-item-id").val(); // do your staff with each checkbox
            console.log("item id is - "+ itemId);
            var url="/api/v0/call/"+callId+"/items/"+itemId;
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
