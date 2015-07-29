// TODO: need a call to populate Faculty
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
      //TODO Still cant figure out selectors
      // var selectedIndex = 0;
      // var facultyArray = [];
      // $("#faculty option").each(function(){
      //   facultyArray.push($(this).val())
      // });
      // for(var i = 0; i < facultyArray.length; i++){
      //   if (facultyArray[i] == procedure.faculty_id){
      //     break;
      //   } else {
      //     selectedIndex++;
      //   }
      // }
      // if (debug) console.log(facultyArray);
      // To select via index
      // $('#faculty :nth-child(' + selectedIndex + ')').prop('selected', true);
      // $("#faculty").change();
      // var itemval= '<option>'+ procedure.faculty_id + '</option>';
      // $("#faculty").append(itemval);
      // facultySelect.options[0] = new Option(procedure.faculty_id, "faculty_id", true, true);
      // var fellowSelect = document.getElementById("fellow");
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

  var toggleForm = function(id, readOnly){
    var form = document.getElementById(id);
    var elements = form.elements;
    for (var i = 0, len = elements.length; i < len; ++i) {
      elements[i].readOnly = readOnly;
    }
  }

  var toggleProgressBar = function(show){
    if (show == true ){
      $('.progress').removeClass('hide');
    } else {
      $(".progress").addClass('hide');
    }
  }

  var toggleSaveButton = function(id, state){
    var button = document.getElementById(id);
    if(state == false){
      button.setAttribute("disabled", true);
    } else {
      button.removeAttribute("disabled");
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
    toggleSaveButton("save1", false);
    toggleForm("identifiersForm", true);
    toggleProgressBar(true);

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
              toggleSaveButton("save1", true);
              toggleForm("identifiersForm", false);
              toggleProgressBar(false);
              $("#identifiers").modal('toggle');
            }
          });
      }
  });

  $("#savePrep").click(function(){
    toggleSaveButton("savePrep", false);
    toggleForm("preparationForm", true);
    toggleProgressBar(true);

    var prep_liters;
    var salts = JSON.parse(Cookies.getCookie("salts"));
    var procedure_id = Cookies.getCookie("procedure_id");

    var prep_drug = $("#prep_drug").val();

    // $(".prep_liters .input").click(function() {
    //   // whenever a button is clicked, set the hidden helper
    //   prep_liters = $(this).text();
    // });



    var prep_liters = $('input[name=liters]:checked', '#preparationForm').val();
    var split_prep = $('input[name=radioName]:checked', '#myForm').val();
    var bisacodyl = $('input[name=radioName]:checked', '#myForm').val();
    if (debug) console.log(prep_liters);

    var patient_id = Cookies.getCookie("patient_id");




    if (procedure_id != null){
      updateProcedure(salts, user_id, session_id, session_key, patient_id,
        procedure_id, {"prep_drug": prep_drug,
        "prep_liters": prep_liters, "split_prep": split_prep,
        "bisacodyl": bisacodyl}).done(
          function(data) {
            if (debug) console.log(data);
            if(data.error == "false"){
              Cookies.addToCookieArray("salts", data.salt, 1);
              updateTables(data.procedure);
              toggleSaveButton("savePrep", true);
              toggleForm("preparationForm", false);
              toggleProgressBar(false);
              $("#preparation").modal('toggle');
            }
          });
      }
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
