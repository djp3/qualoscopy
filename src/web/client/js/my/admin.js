// JS for admin page
$(document).ready(function() {

  var rowMaker = function(number, medicalRecord, lastName, firstName,
    dateOfBirth, gender, nextProcedure){

      var $element = $(" \
      <tr class='clickable-row' data-href='patient.html'> \
      <td style='display:none' class='number'>" + number + "</td> \
      <td class='mr-id'>" + medicalRecord + "</td> \
      <td class='last-name'>"+ lastName + "</td> \
      <td class='firt-name'>"+ firstName + "</td> \
      <td class='dob'>"+ dateOfBirth +"</td> \
      <td class='gender'>"+ gender + "</td> \
      <td class='next-procedure'>"+ nextProcedure + "</td> \
      </tr>");

      return $element;
    };

    $('#dob').datetimepicker({
      format: 'MM/DD/YYYY'}
    );
    $('#operationDate').datetimepicker({
      format: 'MM/DD/YYYY'}
    );
    $('#operationTime').datetimepicker({
      format: 'LT'}
    );

    var salts = JSON.parse(Cookies.getCookie("salts"));
    if(debug) console.log(salts);
    // Ajax call
    getPatients(salts, session_id, session_key, user_id).done(function(data) {
      if (debug) console.log(data);
      if(data.error == "false"){
        Cookies.addToCookieArray("salts", data.salt, 1);
        var patientsArray = data.patients;
        if (debug) console.log(patientsArray);

        for (var i = 0; i < patientsArray.length; i++) {
          var patient = patientsArray[i];
          $("#paitent-rows").append(rowMaker(i, patient.mr_id,
            patient.last, patient.first, patient.dob, patient.gender,
            patient.next_procedure));
          }
          $(".clickable-row").click(function() {
            var mr_id = $(this).find('.mr-id').text();
            var last_name = $(this).find('.last-name').text();
            var firt_name = $(this).find('.firt-name').text();
            var dob = $(this).find('.dob').text();
            var next_procedure = $(this).find('.next-procedure').text();
            var patientNumber = $(this).find('.number').text();
            var patient_id = patientsArray[patientNumber].patient_id;

            if (debug) console.log(patient_id);
            Cookies.setCookie("mr_id", mr_id, 1);
            Cookies.setCookie("last_name", last_name, 1);
            Cookies.setCookie("firt_name", firt_name, 1);
            Cookies.setCookie("dob", dob, 1);
            Cookies.setCookie("next_procedure", next_procedure, 1);
            Cookies.setCookie("patient_id", patient_id, 1);
            window.document.location = $(this).data("href");
          });
          $('#paitents').dataTable();
      } else {
        // TODO: Do something
      }
    });

    $("#add-p").click(function(){
      var salts = JSON.parse(Cookies.getCookie("salts"));
      addPatient(salts, session_id, session_key, user_id);
    });

    $("#save").click(function(){
      var salts = JSON.parse(Cookies.getCookie("salts"));
      var patient_id = Cookies.getCookie("patient_id");
      if(patient_id != null){
        var mrid = $("#mr").val();
        var firstName = $("#first-name").val();
        var lastName = $("#last-name").val();
        var gender = $("#gender").val();
        var dob = $("#dob").val();
        console.log(mrid + "" + firstName + lastName + gender + dob);
        updatePatrient(salts, session_id, session_key, user_id, mrid,
          lastName, firstName, dob, gender, patient_id);
      }

      // var ac = $("#ac").val();
      // var operationDate = $("#operationDate").val();
      // var operationTime = $("#operationTime").val();
      // var faculty = $("#faculty").val();

    });

});
