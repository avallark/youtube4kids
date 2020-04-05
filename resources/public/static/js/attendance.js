//$( document ).ready(function() {
//    markCheckboxes();
//});
function markUnmark(employeeId, dateOn, checkboxId){

    var data = {"employee-id": employeeId, "date-on": dateOn}

    if ($('#'+checkboxId).is(':checked')) {
        $.post("/api/v0/attendance/mark", data, markedAttendance, "json");
    }
    else {
        $.post("/api/v0/attendance/unmark", data, markedLeave, "json");
    }
}

function markedAttendance(dataJson){
    if (dataJson["status"] == 1){
        alert("Attendance marked successfully");
    }
    else {
        alert(dataJson["message"]);
    }
}

function markedLeave(dataJson){
    if (dataJson["status"] == 1){
        alert("Leave marked successfully");
    }
    else {
        alert(dataJson["message"]);
    }
}
