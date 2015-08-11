// (c) Luke Raus 2015
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

    $("#viewTodaysPatients").click(function(){
      var search = document.getElementById('search');
      var today = new Date();
      var dd = today.getDate();
      var mm = today.getMonth()+1; //January is 0!

      var yyyy = today.getFullYear();
      if(dd<10){
          dd='0'+dd
      }
      if(mm<10){
          mm='0'+mm
      }
      var today = mm+'/'+dd+'/'+yyyy;
      search.value = today;
      search.focus();
      // TODO: make it so a keypress happens
    });


    $("#viewAllPatients").click(function(){
      var search = document.getElementById('search');
      search.value = "";
      search.focus();
      // TODO: make it so a keypress happens
    });

    var salts = JSON.parse(Cookies.getCookie("salts"));
    // Ajax call
    getPatients(salts, session_id, session_key, user_id).done(function(data) {
      if (debug) console.log(data);
      if(data.error == "false"){
        Cookies.addToCookieArray("salts", data.salt, 1);
        var patientsArray = data.patients;

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

    $("#addPatientForm").submit(function(){
      event.preventDefault();
      var salts = JSON.parse(Cookies.getCookie("salts"));
      var patient_id = Cookies.getCookie("patient_id");
      if(patient_id != null){
        var mrid = $("#mr").val();
        var firstName = $("#first-name").val();
        var lastName = $("#last-name").val();
        var gender = $("#gender").val();
        var dob = $("#dob").val();
        if (debug) console.log(mrid + "" + firstName + lastName + gender + dob);

        updatePatient(salts, session_id, session_key, user_id, mrid,
          lastName, firstName, dob, gender, patient_id).done(function(data) {
            if (debug) console.log(data);
            if(data.error == "false"){
              Cookies.addToCookieArray("salts", data.salt, 1);
              window.document.location = "admin.html";
            };
      });
    }
  });

});
