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

  // Populating Polyps table
  var populatePolypsTable = function(){
    var salts = JSON.parse(Cookies.getCookie("salts"));
    var procedure_id = Cookies.getCookie("procedure_id");
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

  // Create click functions for buttons
  var createButtonFunctions = function(procedure){
    $("#btn_identifiers").click(function(){
      $("#ac_id").val(procedure.ac_id);
      // document.getElementById("location").value = procedure.location;
      var dateArray = procedure.date_time_of_service.split(" ");
      $("#date_of_service").val(dateArray[0]);
      $("#service_time").val(dateArray[1]);
      $("#" + procedure.location).addClass("active");
      $("#" + procedure.location + " input").prop('checked', "checked");

      $("#" + procedure.faculty_id).addClass("active");
      $("#" + procedure.faculty_id + " input").prop('checked', "checked");

      $("#" + procedure.fellow).addClass("active");
      $("#" + procedure.fellow + " input").prop('checked', "checked");

    });

    $("#btn_preparation").click(function(){
      $("#pre_drug").on('change', function(){
        if(this.value == "Other"){
          $('.other').removeClass('hide');
          $('#pre_drug_other').prop('required',true);
        } else {
          $('.other').addClass('hide');
          $('#pre_drug_other').removeAttr('required');
        }
      });

    });

  }

  var populateTables = function(procedure){
    // For all the tables
    var $allTables = [];

    // Created each table programically
    // Identifiers
    $allTables.push(tableMaker("Identifiers", "btn_identifiers", 5, "#identifiers", "col-md-5", "table0",
    ["Account Number", "Service Time", "Location", "Faculty", "Fellow"],
    [procedure.ac_id, procedure.date_time_of_service, procedure.location, procedure.faculty_id, procedure.fellow]));

    // Preparation
    $allTables.push(tableMaker("Preparation", "btn_preparation", 4, "#preparation", "col-md-4", "table1",
    ["Prep Drug", "Prep Liters", "Split Prep", "Bisacodyl"],
    [procedure.pre_drug, procedure.prep_liters, procedure.split_prep, procedure.bisacodyl]));

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
    createButtonFunctions(procedure);
    populatePolypsTable();
  }

  var updateTables = function(procedure){
    $("#tables").empty();
    populateTables(procedure);
  }

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

  var toggleProgressBar = function(show){
    if (show == true){
      $('.progress').removeClass('hide');
    } else {
      $(".progress").addClass('hide');
    }
  }


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

  var buttonListMaker = function(valueList, buttonGroupName){
    var elementString = "<div class='button-list'> \
      <div class='btn-group-vertical center-block' data-toggle='buttons'>";

    for (var i = 0; i < valueList.length; i++){
      elementString += " \
      <label id=" + valueList[i] + " class='btn btn-primary'> \
        <input required type='radio' name=" + buttonGroupName +
        " value=" + valueList[i]  + ">" + valueList[i]  +" \
      </label>";
    }

     return $(elementString);
  }

  var loadButtonLists = function() {
    var locationArray = ["Bldg200", "CathLab", "OSS", "OR", "CDDC", "UCI 1", "UCI 2"];
    $("#location_selector").append(buttonListMaker(locationArray, "location"));

    var facultyArray = ["DrKarnes", "DrRaus", "DrPatterson", "DrSpock"];
    $("#faculty_selector").append(buttonListMaker(facultyArray, "faculty"));

    var fellowArray = ["None", "Pr. John", "Smith", "Carel", "Mike", "John", "Pat"];
    $("#fellow_selector").append(buttonListMaker(fellowArray, "fellow"));
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

  onLoad();




  $("#identifiersForm").submit(function(){
    event.preventDefault();

    var ac_id = $("#ac_id").val();
    var date_of_service = $("#date_of_service").val();
    var service_time = $("#service_time").val();
    var date_time_of_service = date_of_service + " " + service_time;
    var location = $("input[name=location]:checked").val();
    var faculty_id = $("input[name=faculty]:checked").val();
    var fellow_id = $("input[name=fellow]:checked").val();

    saveProcedureForm({"ac_id": ac_id,
    "date_time_of_service": date_time_of_service, "location": location,
    "faculty_id": faculty_id, "fellow": fellow_id}, "identifiersForm", "#identifiers");
  });

  $("#preparationForm").submit(function(){
    event.preventDefault();

    var pre_drug = $("#pre_drug").val();
    if (pre_drug == "Other"){
      pre_drug = $("#pre_drug_other").val();
    }
    var prep_liters = $('input[name=prep_liters]:checked').val();
    var split_prep = $('input[name=split_prep]:checked').val();
    var bisacodyl = $('input[name=bisacodyl]:checked').val();

    saveProcedureForm({"pre_drug": pre_drug,
    "prep_liters": prep_liters, "split_prep": split_prep,
    "bisacodyl": bisacodyl}, "preparationForm", "#preparation");

  });

  $("#indicationForm").submit(function(){
    event.preventDefault();

    var last_colon = $("#last_colon").val();
    var filter = $('input[name=filter]:checked').val();
    if(debug) console.log(last_colon);
    if(debug) console.log(filter);




    saveProcedureForm({}, "indicationForm", "#indications");
    // var pre_drug = $("#pre_drug").val();
    // var prep_liters = $('.prep_liters input[name=optradio]:checked').val();
    // var split_prep = $('.split_prep input[name=optradio]:checked').val();
    // var bisacodyl = $('.bisacodyl input[name=optradio]:checked').val();
    //
    // if (pre_drug == "None"){
    //   alert("Please select a Prep Drug");
    // } else if (prep_liters == null){
    //   alert("Please choose number of liters for Prep Drug");
    // } else if (split_prep == null) {
    //   alert("Please respond to Split Prep (Yes/No)");
    // } else if (bisacodyl == null) {
    //   alert("Please respond to Bisacodyl (Yes/No)");
    // } else {
    //   saveProcedureForm({"pre_drug": pre_drug,
    //   "prep_liters": prep_liters, "split_prep": split_prep,
    //   "bisacodyl": bisacodyl}, "savePrep", "preparationForm", "#preparation");
    // }

  });

  $(function () {
    $(".today").click(function(){
      var today = new Date();
      var dd = today.getDate();
      var mm = today.getMonth()+1; //January is 0!
      var yyyy = today.getFullYear();

      if(dd<10) {
          dd='0'+dd
      }

      if(mm<10) {
          mm='0'+mm
      }

      today = mm+'/'+dd+'/'+yyyy;
      $(".service_date").val(today);
    });

    $(".now").click(function(){
      var today = new Date();
     var hh = today.getHours();
     var mm = today.getMinutes();
     // add a zero in front of numbers<10
     if (mm < 10) {
         mm = "0" + mm;
     }
      today = hh + ':' + mm;
      $(".service_time").val(today);
    });

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
