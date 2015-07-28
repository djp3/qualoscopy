// var itemval= '<option>TEST</option>';
// $("#faculty").append(itemval);

$(document).ready(function() {
  // The function tableMaker
  var tableMaker = function(buttonName, buttonID, numberOfColumns, buttonTarget,
      tableLength, tableId, columnNames, columnValues){

       var tableHeading = "";
       for(var i = 0; i < numberOfColumns; i++){
         tableHeading += "<th>" + columnNames[i] + "</th>\n";
       }

       var tableValues = "";
       for(var i = 0; i < numberOfColumns; i++){
         tableValues += "<td>" + columnValues[i] + "</td>\n";
       }

       var $element = $(" \
        <div class='row'> \
          <div class='col-md-2'> \
            <button id='" + buttonID + "' class='btn btn-primary pull-right' data-toggle='modal' \
            data-target='" + buttonTarget + "'>" + buttonName + "</button> \
          </div> \
          <div class=" + tableLength + "> \
            <table id="+ tableId +" class='table table-condensed table-striped'> \
              <tr> \
                " + tableHeading + " \
              </tr> \
              <tr> \
                " + tableValues + " \
              </tr> \
            </table> \
          </div> \
        </div> ");

        return $element;
  };

  var salts = JSON.parse(Cookies.getCookie("salts"));
  var patient_id = Cookies.getCookie("patient_id");
  var procedure_id = Cookies.getCookie("procedure_id");
  var procedure;


  var populatePolypsTable = function(){
    var salts = JSON.parse(Cookies.getCookie("salts"));
    getProceduresPolyps(salts, user_id, session_id, session_key,
      procedure_id).done(function(data){
        if (debug) console.log(data);
        if(data.error == "false") {
          var polyps = data.polyps;
          Cookies.addToCookieArray("salts", data.salt, 1);
          // Add Polyps or Mass

          $("#tables").append(tableMaker("Add Polyps or Mass", "btn_addPolypOrMass", 8, "#addPolypOrMass", "col-md-8", "table7",
          ["Loc", "Phe", "Num", "Size", "Tx", "Residual", "Bottle", "Path"],
          ["NA", "NONE", "NONE", "NONE", "NONE", "NONE", "NONE", "NONE"]));

        }
      });
  }

  var createButtonFunctions = function(){
    $("#btn_identifiers").click(function(){
      document.getElementById("ac_id").value = procedure.ac_id;
      document.getElementById("location").value = procedure.location;
      var dateArray = procedure.date_time_of_service.split(" ");
      document.getElementById("date_of_service").value = dateArray[0];
      document.getElementById("service_time").value = dateArray[1];
      // var itemval= '<option>'+ procedure.faculty_id + '</option>';
      // $("#faculty").append(itemval);
      // TODO I can populate this list with options from the database
      // facultySelect.options[0] = new Option(procedure.faculty_id, "faculty_id", true, true);
      // var fellowSelect = document.getElementById("fellow");
      // // TODO I can populate this list with options from the database
      // fellowSelect.options[0] = new Option(procedure.fellow, "fellow_id", false, false);
    });
  }

  var populateTables = function(procedure){
    // For all the tables
    var $allTables = [];

    // Created each table programically
    // Identifiers
    $allTables.push(tableMaker("Identifiers", "btn_identifiers", 4, "#identifiers", "col-md-5", "table0",
    ["Account Number", "Date of Service", "Faculty", "Fellow"],
    [procedure.ac_id, procedure.date_time_of_service, procedure.faculty_id, procedure.fellow]));

    // Preparation
    $allTables.push(tableMaker("Preparation", "btn_preparation", 3, "#preparation", "col-md-3", "table1",
    ["Prep Liters", "Split Prep", "Bisacodyl"],
    [procedure.prep_liters, procedure.split_prep, procedure.bisacodyl]));

    // Indications
    $allTables.push(tableMaker("Indications", "btn_indications", 5, "#indications", "col-md-5", "table2",
    ["Last Colon", "Indication", "Category", "Subcategory", "Specifics"],
    [procedure.last_colon + " years", procedure.primary_indication, "NA", "NA", "NA"]));

    // Scope
    $allTables.push(tableMaker("Scope", "btn_scope", 4, "#scope", "col-md-4", "table3",
    ["Scopes", "Underwater", "CapAssisted", "EndoCuff"],
    [procedure.scope, procedure.underwater, procedure.cap_assisted, procedure.endocuff]));

    // Sedation
    $allTables.push(tableMaker("Sedation", "btn_sedation", 6, "#sedation", "col-md-5", "table4",
    ["Sedation Level", "Versed", "Fentanyl", "Demerol", "Benadryl", "Other Med"],
    [procedure.sedation_level, procedure.versed, procedure.fentanyl, procedure.demerol, procedure.benadryl, "NA"]));

    // Extent
    $allTables.push(tableMaker("Extent", "btn_extent", 2, "#extent", "col-md-2", "table5",
    ["Extent", "Reson Incomplete"],
    [procedure.extent, "NA"]));

    // Prep Quality
    $allTables.push(tableMaker("Prep Quality", "btn_quality", 3, "#quality", "col-md-3", "table6",
    ["Prep Left", "Prep Mid", "Prep Right"],
    [procedure.prep_quality_left, procedure.prep_quality_mid, procedure.prep_quality_right]));

    // Times
    $allTables.push(tableMaker("Times", "btn_times", 3, "#times", "col-md-5", "table7",
    ["Time Insertion", "Time Begin Withdrawal", "Time Scope Withdrawn"],
    [procedure.time_insertion, procedure.time_begin_withdrawal, procedure.time_scope_withdrawn]));

    for (var i = 0; i < $allTables.length; i++) {
      $("#tables").append($allTables[i]);
    }
    createButtonFunctions();
    populatePolypsTable();
  }

  var updateTables = function(procedure){
    $("#tables").empty();
    populateTables(procedure);
  }

  var updateProgressBar = function(show){
    if (show == true ){
      $('.progress').removeClass('hide');
    } else {
      $(".progress").addClass('hide');
    }
  }




  updateProcedure(salts, user_id, session_id, session_key,
    patient_id, procedure_id,{}).done(function(data) {
      if (debug) console.log(data);
      if(data.error == "false") {
        procedure = data.procedure;
        Cookies.addToCookieArray("salts", data.salt, 1);
        populateTables(procedure);
      }
  });




  $("#identifiersForm").submit(function(){
    event.preventDefault();
    updateProgressBar(true);
    var form = document.getElementById("identifiersForm");
    var elements = form.elements;
    for (var i = 0, len = elements.length; i < len; ++i) {
      elements[i].readOnly = true;
    }
    document.getElementById("save1").setAttribute("disabled", true);
    var patient_id = Cookies.getCookie("patient_id");
    var salts = JSON.parse(Cookies.getCookie("salts"));
    var ac_id = $("#ac_id").val();
    var date_of_service = $("#date_of_service").val();
    var service_time = $("#service_time").val();
    var date_time_of_service = date_of_service + " " + service_time;
    var location = $("#location").val();
    var faculty_id = $("#faculty").val();
    var fellow_id = $("#fellow").val();
    var procedure_id = Cookies.getCookie("procedure_id");


    if (procedure_id != null){
      updateProcedure(salts, user_id, session_id, session_key, patient_id,
        procedure_id, {"ac_id": ac_id,
        "date_time_of_service": date_time_of_service, "location": location,
        "faculty_id": faculty_id, "fellow": fellow_id}).done(
          function(data) {
            if (debug) console.log(data);
            if(data.error == "false"){
              Cookies.addToCookieArray("salts", data.salt, 1);
              updateTables(data.procedure);
              for (var i = 0, len = elements.length; i < len; ++i) {
                elements[i].readOnly = false;
              }
              document.getElementById("save1").removeAttribute("disabled");
              updateProgressBar(false);
              $("#identifiers").modal('toggle');

              // window.document.location = "main.html";
            }
          });
      }
  });

  $("#preparationForm").submit(function(){
    event.preventDefault();
  });

  $(function () {
    // Set up date and time pickers
    $('#date_of_service').datetimepicker({
       format: 'MM/DD/YYYY'
    });
    $('#service_time').datetimepicker({
      format: 'HH:mm'
    });
    $('#timepicker1').datetimepicker({
      format: 'HH:mm'
    });
    $('#timepicker2').datetimepicker({
      format: 'HH:mm'
    });
    $('#timepicker3').datetimepicker({
      format: 'HH:mm'
    });
    $('#timepicker4').datetimepicker({
      format: 'HH:mm'
    });
    $('#timepicker5').datetimepicker({
      format: 'HH:mm'
    });

    // Link Timepickers
    $("#timepicker1").on("dp.change", function (e) {
        $('#timepicker2').data("DateTimePicker").minDate(e.date);
    });

    $("#timepicker2").on("dp.change", function (e) {
        $('#timepicker3').data("DateTimePicker").minDate(e.date);
    });

    $("#timepicker4").on("dp.change", function (e) {
        $('#timepicker5').data("DateTimePicker").minDate(e.date);
    });
  });

});
