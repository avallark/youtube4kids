function htmlForTextWithEmbeddedNewlines(text) {
    var htmls = [];
    var lines = text.split(/\n/);
    // The temporary <div/> is to perform HTML entity encoding reliably.
    //
    // document.createElement() is *much* faster than jQuery('<div></div>')
    // http://stackoverflow.com/questions/268490/
    //
    // You don't need jQuery but then you need to struggle with browser
    // differences in innerText/textContent yourself
    var tmpDiv = jQuery(document.createElement('div'));
    for (var i = 0 ; i < lines.length ; i++) {
        htmls.push(tmpDiv.text(lines[i]).html());
    }
    return htmls.join("<br>");
}

function imapsyncDry(){

    var password1 = $('#password1').val();
    var password2 = $('#password2').val();

    var data = {password1: password1, password2: password2};

    $("#dry-run-output-instructions").show();
    $.post('/api/v0/mail/imapsync/dry', data, imapsyncDried, "json");

}

function imapsyncDried(dataJson){

    var message = dataJson["out"];
    var status = dataJson["exit"];


    if (status == 0){
        alert("Your request has been successful. Please click on the Execute Sync Folders button below.");
        $("#execute-sync-folders").show();
    }
    else {
        alert("There is an error in your request. Please make sure you followed the instructions above. Please check the output shown to check what the error was.");
    }
   // $('#dry-run-output').append(message);
    $('#dry-run-output').append(htmlForTextWithEmbeddedNewlines(message));

}

function imapsyncFolders(){

    var password1 = $('#password1').val();
    var password2 = $('#password2').val();

    var data = {password1: password1, password2: password2};

    $("#folder-sync-run-output-instructions").show();
    $.post('/api/v0/mail/imapsync/folders', data, imapsyncFoldersDone, "json");

}

function imapsyncFoldersDone(dataJson){

    var message = dataJson["out"];
    var status = dataJson["exit"];

    if (status == 0){
        alert("Your request has been successful. Please click on the Execute Email Sync button below.");
        $("#execute-sync-email").show();
    }
    else {
        alert("There is an error in your request. Please make sure you followed the instructions above. Please check the output shown to check what the error was.");
    }
    // $('#dry-run-output').append(message);
    $('#sync-folders-output').append(htmlForTextWithEmbeddedNewlines(message));

}

function imapsyncEmail(){

    var password1 = $('#password1').val();
    var password2 = $('#password2').val();

    var data = {password1: password1, password2: password2};

    $("#email-sync-run-output-instructions").show();
    $.post('/api/v0/mail/imapsync/email', data, imapsyncEmailDone, "json");

}

function imapsyncEmailDone(dataJson){

    var message = dataJson["out"];
    var status = dataJson["exit"];

    if (status == 0){
        alert("Your request has been successfuly submitted. You will start seeing your old email in your new Tzana inbox");
        $("#sync-email-done").show();
    }
    else {
        alert("There is an error in your request. Please make sure you followed the instructions above. Please check the output shown to check what the error was.");
    }
    // $('#dry-run-output').append(message);
    $('#sync-email-output').append(htmlForTextWithEmbeddedNewlines(message));

}
