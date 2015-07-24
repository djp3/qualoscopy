$(document).ready(function() {
  // The function tableMaker
  var tableMaker = function(buttonName, numberOfColumns, buttonTarget,
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
            <button class='btn btn-primary pull-right' data-toggle='modal' \
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

          $("#tables").append(tableMaker("Add Polyps or Mass", 8, "#addPolypOrMass", "col-md-8", "table7",
          ["Loc", "Phe", "Num", "Size", "Tx", "Residual", "Bottle", "Path"],
          [polyps[0].polyp_id, "NONE", "NONE", "NONE", "NONE", "NONE", "NONE", "NONE"]));

        }
      });
  }

  var populateTables = function(procedure){
    // For all the tables
    var $allTables = [];

    // Created each table programically
    // Identifiers
    $allTables.push(tableMaker("Identifiers", 4, "#identifiers", "col-md-4", "table0",
    ["Account Number", "Date of Service", "Faculty", "Fellow"],
    [procedure.ac_id, procedure.date_time_of_service, procedure.faculty_id, procedure.fellow]));

    // Preparation
    $allTables.push(tableMaker("Preparation", 3, "#preparation", "col-md-3", "table1",
    ["Prep Liters", "Split Prep", "Bisacodyl"],
    [procedure.prep_liters, procedure.split_prep, procedure.bisacodyl]));

    // Indications
    $allTables.push(tableMaker("Indications", 5, "#indications", "col-md-5", "table2",
    ["Last Colon", "Indication", "Category", "Subcategory", "Specifics"],
    [procedure.last_colon + " years", procedure.primary_indication, "NEED THIS", "NEED THIS", "NEED THIS"]));

    // Scope
    $allTables.push(tableMaker("Scope", 4, "#scope", "col-md-4", "table3",
    ["Scopes", "Underwater", "CapAssisted", "EndoCuff"],
    [procedure.scope, procedure.underwater, procedure.cap_assisted, procedure.endocuff]));

    // Sedation
    $allTables.push(tableMaker("Sedation", 6, "#sedation", "col-md-5", "table4",
    ["Sedation Level", "Versed", "Fentanyl", "Demerol", "Benadryl", "Other Med"],
    [procedure.sedation_level, procedure.versed, procedure.fentanyl, procedure.demerol, procedure.benadryl, "NEED THIS"]));

    // Extent
    $allTables.push(tableMaker("Extent", 2, "#extent", "col-md-2", "table5",
    ["Extent", "Reson Incomplete"],
    [procedure.extent, "NEED THIS"]));

    // Prep Quality
    $allTables.push(tableMaker("Prep Quality", 3, "#quality", "col-md-3", "table6",
    ["Prep Left", "Prep Mid", "Prep Right"],
    [procedure.prep_quality_left, procedure.prep_quality_mid, procedure.prep_quality_right]));

    // Times
    $allTables.push(tableMaker("Times", 3, "#times", "col-md-5", "table7",
    ["Time Insertion", "Time Begin Withdrawal", "Time Scope Withdrawn"],
    [procedure.time_insertion, procedure.time_begin_withdrawal, procedure.time_scope_withdrawn]));

    for (var i = 0; i < $allTables.length; i++) {
      $("#tables").append($allTables[i]);
    }
    populatePolypsTable();

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
              window.document.location = "main.html";
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
