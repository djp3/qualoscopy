// (c) Luke Raus 2015

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

// Save the procuder form (ops is an javascript object with mutiple keys and values)
var saveProcedureForm = function(opts, formID, modalID){
  toggleProgressBar(true);
  toggleForm(formID, true);

  var salts = JSON.parse(Cookies.getCookie("salts"));
  var procedure_id = Cookies.getCookie("procedure_id");
  var patient_id = Cookies.getCookie("patient_id");

  if (procedure_id != null){
    updateProcedure(salts, user_id, session_id, session_key, patient_id,
      procedure_id, opts).done(
        function(data) {
          if (debug) console.log(data);
          if(data.error == "false"){
            Cookies.addToCookieArray("salts", data.salt, 1);
            updateTables(data.procedure);
            toggleForm(formID, false);
            toggleProgressBar(false);
            $(modalID).modal('toggle');
          }
        });
    }
}

// Populate the tables on reloads
var onLoad = function(){
  var salts = JSON.parse(Cookies.getCookie("salts"));
  var patient_id = Cookies.getCookie("patient_id");
  var procedure_id = Cookies.getCookie("procedure_id");
  updateProcedure(salts, user_id, session_id, session_key,
    patient_id, procedure_id,{}).done(function(data) {
      if (debug) console.log(data);
      if(data.error == "false") {
        Cookies.addToCookieArray("salts", data.salt, 1);
        populateTables(data.procedure);
        loadButtonLists();
      }
  });
}
