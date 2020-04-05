function populateHospitals() {
    var districtId = $("#district-id").val();
    var url = "/api/v0/hospitals/"+districtId+"/all";
    $.get({"url": url,
          "success": populateHospitalsNow,
           "datatype": "json"});
}
function populateHospitalsNow(dataJson){
    if (dataJson["status"] == 1){
        var hospitals = dataJson["hospitals"];
        if (hospitals.length > 0) {
            var htmltbody = " ";

            if ( $.fn.dataTable.isDataTable( '#hospitals-list-table' ) ) {
                var table = $('#hospitals-list-table').DataTable();
                table.destroy();
            }

            $("#hospitals-table-body").html(htmltbody);

            for (i=0;i<hospitals.length;i++){
                htmltbody += "<tr>"+
                    "<td>"+hospitals[i]["hospital-name"]+"</td>"+
		    "<td>"+hospitals[i]["hospital-flag"]+"</td>"+
                    "<td>"+hospitals[i]["hospital-address"]+"</td>"+
                    "<td>"+hospitals[i]["hospital-pin-code"]+"</td>"+
                    "<td>"+hospitals[i]["hospital-dl-no"]+"</td>"+
                    "<td>"+hospitals[i]["hospital-gst-no"]+"</td>"+
                    "<td>"+hospitals[i]["hospital-other-info"]+"</td>"+
                    "<td> <a href='/hospitals/"+hospitals[i]["hospital-id"]+"/edit'> Edit </a> </td>"+
                    "</tr> ";
	    }

	    $("#hospitals-table-body").html(htmltbody);

            $('#hospitals-list-table').DataTable({
                pageLength: 10,
                dom: '<"html5buttons"B>lTfgitp',
                responsive: true,
                buttons: [
                    {extend: 'copy'},
                    {extend: 'csv'},
                    {extend: 'excel', title: 'Doctors-List'},
                    {extend: 'pdf',
                     title: 'Hospitals List',
                     exportOptions: {
                         columns: [0,1,2,3,4,5]
                     }}
                ]
            });
            $("#hospitals-table").show();
        }
    }
    else {
        alert("Something went wrong while getting hospitals. Try later.");
    }
}
