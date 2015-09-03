// (c) Luke Raus 2015
// JS for admin page
$(document).ready(function() {

  // Turn on off the elements in an form
  var toggleForm = function(id, disabled){
    var form = document.getElementById(id);
    var elements = form.elements;
    if (disabled == true){
      for (var i = 0, len = elements.length; i < len; ++i) {
        elements[i].setAttribute("disabled", true);
      }
    } else {
      for (var i = 0, len = elements.length; i < len; ++i) {
        elements[i].removeAttribute("disabled");
      }
    }
  }

  // Toggle the progress bar on and off
  var toggleProgressBar = function(show){
    if (show == true){
      $('.progress').removeClass('hide');
    } else {
      $(".progress").addClass('hide');
    }
  }

  // Create a list of <option> HTML elements from an array
  var selectOptionsMaker = function(textList){
    var elementString = "";
    for (var i = 0; i < textList.length; i++){
      elementString += "<option>" +  textList[i] + "</option>";
    }

     return $(elementString);
  }

  var updateProcedureHelper = function(){
    // Update procedure if checkbox checked

      salts = JSON.parse(Cookies.getCookie("salts"));
      var ac_id = $("#addPatient #ac").val();
      var date_of_service = $("#addPatient #operationDate").val();
      var service_time = $("#addPatient #operationTime").val();
      var date_time_of_service = date_of_service + " " + service_time;
      var faculty_id = $("#addPatient #faculty").val();
      var patient_id = Cookies.getCookie("patient_id");
      var procedure_id = Cookies.getCookie("procedure_id");

      updateProcedure(salts, user_id, session_id, session_key, patient_id,
        procedure_id, {"ac_id": ac_id,
        "date_time_of_service": date_time_of_service,
        "faculty_id": faculty_id}).done(
          function(data) {
            if (debug) console.log(data);
            if(data.error == "false"){
              Cookies.addToCookieArray("salts", data.salt, 1);
              toggleForm("addPatientForm", false);
              toggleProgressBar(false);
              window.document.location = "admin.html";
            } else {
              // Error catch
            }
          });
  }

  // Row maker for tables
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

    var default_dob = new Date();
    default_dob.setFullYear(default_dob.getFullYear() - 50);


    $('#dob').datetimepicker({
      format: 'MM/DD/YYYY',
      defaultDate: default_dob}
    );
    $('#operationDate').datetimepicker({
      format: 'MM/DD/YYYY'}
    );
    $('#operationTime').datetimepicker({
      format: 'HH:mm'}
    );


    var salts = JSON.parse(Cookies.getCookie("salts"));
    // Ajax call
    getPatients(salts, session_id, session_key, user_id).done(function(data) {
      if (debug) console.log(data);
      if(data.error == "false"){
        Cookies.addToCookieArray("salts", data.salt, 1);
        var patientsArray = data.patients;

        // populate Table
        for (var i = 0; i < patientsArray.length; i++) {
          var patient = patientsArray[i];
          // Uncomment to hide null
          // if (patient.mr_id != null){
            $("#paitent-rows").append(rowMaker(i, patient.mr_id,
              patient.last, patient.first, patient.dob, patient.gender,
              patient.next_procedure));
            // }
        }

        // Add clicks to bring to patients
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
      $('#paitents').DataTable().search(today).draw();
    });

    $("#viewAllPatients").click(function(){
      var search = document.getElementById('search');
      search.value = "";
      search.focus();
      $('#paitents').DataTable().search("").draw();
    });

    var doctors = ["Karnes","Raus","Don"];
    $("#addPatient #faculty").append(selectOptionsMaker(doctors));

    $("#add-p").click(function(){
      var salts = JSON.parse(Cookies.getCookie("salts"));
      addPatient(salts, session_id, session_key, user_id);
    });

    $("#addPatient #schedule").change(function(){
      if(this.checked){
        $("#addPatient #ac").attr("required", true);
        $("#addPatient #operationDate").attr("required", true);
        $("#addPatient #operationTime").attr("required", true);
        $("#addPatient #ac").attr("disabled", false);
        $("#addPatient #operationDate").attr("disabled", false);
        $("#addPatient #operationTime").attr("disabled", false);
        $("#addPatient #faculty").attr("disabled", false);
        var salts = JSON.parse(Cookies.getCookie("salts"));
        var patient_id = Cookies.getCookie("patient_id");
        addProcedure(salts, session_id, session_key, user_id, patient_id);
      } else {
        $("#addPatient #ac").attr("required", false);
        $("#addPatient #operationDate").attr("required", false);
        $("#addPatient #operationTime").attr("required", false);
        $("#addPatient #ac").attr("disabled", true);
        $("#addPatient #operationDate").attr("disabled", true);
        $("#addPatient #operationTime").attr("disabled", true);
        $("#addPatient #faculty").attr("disabled", true);
      }
    });


    $("#addPatientForm").submit(function(evt){
      evt.preventDefault();
      var salts = JSON.parse(Cookies.getCookie("salts"));
      var patient_id = Cookies.getCookie("patient_id");
      if(patient_id != null){
        toggleProgressBar(true);
        toggleForm("addPatientForm", true);
        var mrid = $("#mr").val();
        var firstName = $("#first-name").val();
        var lastName = $("#last-name").val();
        var gender = $("#gender").val();
        var dob = $("#dob").val();
        if (debug) console.log(dob);
        if (debug) console.log(mrid + "" + firstName + lastName + gender + dob);

        // Update Patient First
        updatePatient(salts, session_id, session_key, user_id, mrid,
          lastName, firstName, dob, gender, patient_id).done(function(data) {
            if (debug) console.log(data);
            if(data.error == "false"){
              Cookies.addToCookieArray("salts", data.salt, 1);

              if($("#addPatient #schedule").is(":checked")){
                updateProcedureHelper();
              } else {
                toggleForm("addPatientForm", false);
                toggleProgressBar(false);
                window.document.location = "admin.html";
              }
            } else {
              // Error Catch
            }
        });
      };
    });

});
