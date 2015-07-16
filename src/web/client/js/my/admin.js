// JS for admin page
$(document).ready(function() {

  var rowMaker = function(medicalRecord, lastName, firstName,
    dateOfBirth, gender, nextProcedure){

      var $element = $(" \
      <tr class='clickable-row' data-href='patient.html'> \
      <td class='mr-id'>" + medicalRecord + "</td> \
      <td class='last-name'>"+ lastName + "</td> \
      <td class='firt-name'>"+ firstName + "</td> \
      <td class='dob'>"+ dateOfBirth +"</td> \
      <td class='gender'>"+ gender + "</td> \
      <td class='next-procedure'>"+ nextProcedure + "</td> \
      </tr>");

      return $element;
    };

    var salts = JSON.parse(Cookies.getCookie("salts"));

    // Ajax call
    getPatients(salts, session_id, session_key, user_id).done(function(data) {
      if (debug) console.log(data);
      if(data.error == "false"){
        Cookies.addToCookieArray("salts", data.salt, 1);
        var patientsArray = data.patients;
        if (debug) console.log(patientsArray);

        for (var i = 0; i < patientsArray.length; i++) {
          var patient = patientsArray[i];
          $("#paitent-rows").append(rowMaker(patient.mr_id, patient.last,
            patient.first, patient.dob, patient.gender, patient.next_procedure));
          }
          $('#paitents').dataTable();
          $(".clickable-row").click(function() {
            var mr_id = $(this).find('.mr-id').text();
            var last_name = $(this).find('.last-name').text();
            var firt_name = $(this).find('.firt-name').text();
            var dob = $(this).find('.dob').text();
            var next_procedure = $(this).find('.next-procedure').text();

            if (debug) console.log(mr_id);
            Cookies.setCookie("mr_id", mr_id, 1);
            Cookies.setCookie("last_name", last_name, 1);
            Cookies.setCookie("firt_name", firt_name, 1);
            Cookies.setCookie("dob", dob, 1);
            Cookies.setCookie("next_procedure", next_procedure, 1);
            window.document.location = $(this).data("href");
          });
      } else {
        // TODO: Do something
      }
    });

      $('#operationDate').datetimepicker({
        format: 'MM/DD/YYYY'}
      );
      $('#operationTime').datetimepicker({
        format: 'LT'}
      );
});
