// JS for patient screen
$(document).ready(function() {

  var patientRowMaker = function(mr_id, lastName, firstName, dob, gender){

    var $element = $(" \
    <td>" + mr_id + "</td> \
    <td>"+ lastName + "</td> \
    <td>"+ firstName + "</td> \
    <td>"+ dob + "</td> \
    <td>"+ gender +"</td>");

    return $element;
  };

  var procedureRowMaker = function(ac, dos, completed, factulty){

    var $element = $(" \
    <tr class='clickable-row' data-href='main.html'> \
    <td class='ac-id'>" + ac + "</td> \
    <td>"+ dos + "</td> \
    <td>"+ completed + "</td> \
    <td>"+ factulty +"</td> \
    </tr>");

    return $element;
  };


  var patient_id = Cookies.getCookie("patient_id");
  if(debug) console.log(patient_id);
  var mr_id = Cookies.getCookie("mr_id");
  var last_name = Cookies.getCookie("last_name");
  var firt_name = Cookies.getCookie("firt_name");
  var dob = Cookies.getCookie("dob");
  var next_procedure = Cookies.getCookie("next_procedure");

  $("#patient-info").append(patientRowMaker(mr_id, last_name, firt_name,
    dob, next_procedure));

  var salts = JSON.parse(Cookies.getCookie("salts"));

  getPatientProcedure(salts, session_id, session_key,
    user_id, mr_id, patient_id).done(function(data) {
    if (debug) console.log(data);
    if(data.error == "false"){
      Cookies.addToCookieArray("salts", data.salt, 1);

      var procedureArray = data.procedures;
      if (debug) console.log(procedureArray);

      for (var i = 0; i < procedureArray.length; i++) {
        var procedure = procedureArray[i];
        $("#procedure-rows").append(procedureRowMaker(procedure.ac_id,
          procedure.dos, procedure.completed, procedure.faculty));
        }
        $(".clickable-row").click(function() {
          var ac_id = $(this).find('.ac-id').text();
          if (debug) console.log(ac_id);
          Cookies.setCookie("ac_id", ac_id, 1);
          window.document.location = $(this).data("href");
        });
        $('#procedure-table').dataTable();
      } else {
        // window.location.href = "admin.html";
      }
    });

    $('#operationDate').datetimepicker({
      format: 'MM/DD/YYYY'}
    );

    $('#operationTime').datetimepicker({
      format: 'LT'}
    );

    $("#save").click(function(){
      var $ac = $("#ac").val();
      var $operationDate = $("#operationDate").val();
      var $operationTime = $("#operationTime").val();
      var $faculty = $("#faculty").val();

      console.log($ac + "" + $operationDate + $operationTime + $faculty);
    });

});
