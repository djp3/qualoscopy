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


var mr_id = Cookies.getCookie("mr_id");
var last_name = Cookies.getCookie("last_name");
var firt_name = Cookies.getCookie("firt_name");
var dob = Cookies.getCookie("dob");
var next_procedure = Cookies.getCookie("next_procedure");

$("#patient-info").append(patientRowMaker(mr_id, last_name, firt_name, dob, next_procedure));

var salts = JSON.parse(Cookies.getCookie("salts"));
var session_id = Cookies.getCookie("session_id");
var session_key = Cookies.getCookie("session_key");
var user_id = Cookies.getCookie("user_id");


var usedSalt = salts[0];
salts.splice(0,1);
Cookies.setCookie("salts", JSON.stringify(salts), 1);
$.ajax({
  dataType: 'jsonp',
  url: "https://" + ipAddress + ":" + port
  + "/get/patient/procedures",
  data: {"user_id": user_id, "shsid": Sha256.hash(session_id + "" + usedSalt),
  "shsk": Sha256.hash(session_key + "" + usedSalt), "mr_id": mr_id, "version": versionNumber},
  context: document.body
}).done(function(data) {
  if (debug) console.log(data);
  if(data.error == "false"){
    var setSalts = JSON.parse(Cookies.getCookie("salts"));
    setSalts.push(data.salt);
    if (debug) console.log(setSalts);
    Cookies.setCookie("salts", JSON.stringify(setSalts), 1);

    var procedureArray = data.procedures;
    if (debug) console.log(procedureArray);

    for (var i = 0; i < procedureArray.length; i++) {
      var procedure = procedureArray[i];
      $("#procedure-rows").append(procedureRowMaker(procedure.ac_id, procedure.dos,
        procedure.completed, procedure.faculty));
    }
    $('#procedure-table').dataTable();
    $(".clickable-row").click(function() {
      var ac_id = $(this).find('.ac-id').text();
      if (debug) console.log(ac_id);
      Cookies.setCookie("ac_id", ac_id, 1);
      window.document.location = $(this).data("href");
    });
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

});
