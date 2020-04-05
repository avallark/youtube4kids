$(document).ready(function() {
    //jQuery code goes here

    $("#password").prop("disabled", true);
    $("#repeat-password").prop("disabled", true);
    //$("#change-password-button").prop("disabled", true);
    $('#change-password-button').click(function(e){e.preventDefault();});

});


function validateOld(){

    var oldPassword = $('#old-password').val();
    var emailId = $("#email-id").val();
    var data = {password: oldPassword, user: emailId };

    $.post('/api/v0/user/login', data, validatedLogin, "json");

}


function validatedLogin(dataJson){

    var status = dataJson["status"];

    if (status == 0){
        alert("Your old password is not valid. Re-enter.");
        $("#change-password-form")[0].reset();
    }
    else{
        $("#password").prop("disabled", false);
        $("#repeat-password").prop("disabled", false);
    }

}

function validatePasswordConfirm(){
    var password = $("#password").val();
    var repeatPass = $("#repeat-password").val();

    if (password == repeatPass){
        $("#passok").show();
        $("#change-password-button").unbind("click");
    }
    else{
        alert("Your passwords do not match! Please re-enter");
    }

}

function changePassword(emailId){
    var password = $("#password").val();

    var data = {password: password, user: emailId};

    $.post('/api/v0/user/password/change', data, changedPassword, "json");
}

function changedPassword(dataJson){
    var status = dataJson["status"];

    if (status == 1){
        alert("Your password has changed. Congratulations.");
    }
    else{
        alert("Something's rotten in the state of Denmark. Get help.");
    }
}
