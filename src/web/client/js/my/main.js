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

          $("#btn_addPolypOrMass").click(function(){
            // $("#addPolypOrMass #location").val("Cecum");

          });


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
      $("#identifiers #" + procedure.location).addClass("active");
      $("#identifiers #" + procedure.location + " input").prop('checked', "checked");

      $("#identifiers #" + procedure.faculty_id).addClass("active");
      $("#identifiers #" + procedure.faculty_id + " input").prop('checked', "checked");

      $("#identifiers #" + procedure.fellow).addClass("active");
      $("#identifiers #" + procedure.fellow + " input").prop('checked', "checked");
    });

    $("#btn_preparation").click(function(){
      $("#preparation #" + procedure.pre_drug).addClass("active");
      $("#preparation #" + procedure.pre_drug + " input").prop('checked', "checked");

      $("#preparation #" + procedure.prep_liters).addClass("active");
      $("#preparation #" + procedure.prep_liters + " input").prop('checked', "checked");

      $("#preparation .split #" + procedure.split_prep).addClass("active");
      $("#preparation .split #" + procedure.split_prep + " input").prop('checked', "checked");

      $("#preparation .bisaco #" + procedure.bisacodyl).addClass("active");
      $("#preparation .bisaco #" + procedure.bisacodyl + " input").prop('checked', "checked");

      if (procedure.pre_drug == "Other"){
        $('.other').removeClass('hide');
        $('#pre_drug_other').prop('required',true);
      }


      $("#preparation #pre_drug_selector label").click(function(){
        if($(this).prop('id') == "Other"){
          $('#preparation .other').removeClass('hide');
          $('#preparation #pre_drug_other').prop('required',true);
        } else {
          $('#preparation .other').addClass('hide');
          $('#preparation #pre_drug_other').removeAttr('required');
        }
      });
    });

    $("#btn_indications").click(function(){
      $("#indications #" + procedure.last_colon).addClass("active");
      $("#indications #" + procedure.last_colon + " input").prop('checked', "checked");
    });

    $("#btn_scope").click(function(){
      $("#scope #" + procedure.scope).addClass("active");
      $("#scope #" + procedure.scope + " input").prop('checked', "checked");

      $("#scope .endo #" + procedure.endocuff).addClass("active");
      $("#scope .endo #" + procedure.endocuff + " input").prop('checked', "checked");

      $("#scope .cap #" + procedure.cap_assisted).addClass("active");
      $("#scope .cap #" + procedure.cap_assisted + " input").prop('checked', "checked");

      $("#scope .underwater #" + procedure.underwater).addClass("active");
      $("#scope .underwater #" + procedure.underwater + " input").prop('checked', "checked");

      if (procedure.scope == "Other"){
        $('#scope .other').removeClass('hide');
        $('#scope #scope_other').prop('required',true);
      }

      $("#scope_selector label").click(function(){
        if($(this).prop('id') == "Other"){
          $('#scope .other').removeClass('hide');
          $('#scope #scope_other').prop('required',true);
        } else {
          $('#scope .other').addClass('hide');
          $('#scope #scope_other').removeAttr('required');
        }
      });
    });

    $("#btn_sedation").click(function(){
      $("#sedation #" + procedure.sedation_level).addClass("active");
      $("#sedation #" + procedure.sedation_level + " input").prop('checked', "checked");

      if (procedure.sedation_level == "Moderate"){
        $('#sedation #moderate_sed').removeClass('hide');
        $("#sedation #" + procedure.versed).addClass("active");
        $("#sedation #" + procedure.versed + " input").prop('checked', "checked");

        $("#sedation #" + procedure.fentanyl).addClass("active");
        $("#sedation #" + procedure.fentanyl + " input").prop('checked', "checked");

        $("#sedation #" + procedure.demerol).addClass("active");
        $("#sedation #" + procedure.demerol + " input").prop('checked', "checked");

        $("#sedation #" + procedure.benadryl).addClass("active");
        $("#sedation #" + procedure.benadryl + " input").prop('checked', "checked");
      }

      $("#sedation_level_selector label").click(function(){
        if($(this).prop('id') == "Moderate"){
          $('#sedation #moderate_sed').removeClass('hide');
        } else {
          $('#sedation #moderate_sed').addClass('hide');
        }
      });
    });

    $("#btn_extent").click(function(){
      $("#extent #" + procedure.extent).addClass("active");
      $("#extent #" + procedure.extent + " input").prop('checked', "checked");


      if(!(procedure.extent  == "ti" || procedure.extent  == "cecum" || procedure.extent  == "ileocolonic_anastomosis")){
        $('#extent .other').removeClass('hide');
        $('#extent #extent_other').prop('required',true);
      }

      $("#extent_selector label").click(function(){
        var extent = $(this).prop('id');
        if(extent == "ti" || extent == "cecum" || extent == "ileocolonic_anastomosis"){
          $('#extent .other').addClass('hide');
          $('#extent #extent_other').removeAttr('required');
        } else {
          $('#extent .other').removeClass('hide');
          $('#extent #extent_other').prop('required',true);
        }
      });
    });

    $("#btn_quality").click(function(){
      $("#quality .prep_left #" + procedure.prep_quality_left).addClass("active");
      $("#quality .prep_left #" + procedure.prep_quality_left + " input").prop('checked', "checked");

      $("#quality .prep_mid #" + procedure.prep_quality_mid).addClass("active");
      $("#quality .prep_mid #" + procedure.prep_quality_mid + " input").prop('checked', "checked");

      $("#quality .prep_right #" + procedure.prep_quality_right).addClass("active");
      $("#quality .prep_right #" + procedure.prep_quality_right + " input").prop('checked', "checked");
    });

    $("#btn_times").click(function(){
      $("#times #time_insertion").val(procedure.time_insertion);
      $("#times #time_begin_withdrawal").val(procedure.time_begin_withdrawal);
      $("#times #time_scope_withdrawn").val(procedure.time_scope_withdrawn);

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
    [procedure.last_colon, procedure.primary_indication, "NA", "NA", "NA"]));

    // Scope
    $allTables.push(tableMaker("Scope", "btn_scope", 4, "#scope", "col-md-4", "table3",
    ["Scopes", "EndoCuff", "CapAssisted", "Underwater"],
    [procedure.scope, procedure.endocuff, procedure.cap_assisted, procedure.underwater]));

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

  var buttonListMaker = function(required, valueList, textList, buttonGroupName, height){
    var elementString = "<div class='button-list' style='height:" + height + "'> \
      <div class='btn-group-vertical center-block' data-toggle='buttons'>";


    for (var i = 0; i < valueList.length; i++){
      elementString += " \
      <label id=" + valueList[i] + " class='btn btn-primary'> \
        <input " + required + " type='radio' name=" + buttonGroupName +
        " value=" + valueList[i]  + ">" + textList[i]  +" \
      </label>";
    }

     return $(elementString);
  }

  var buttonListMaker2 = function(required, valueList, textList, buttonGroupName, height){
    var elementString = "<div class='button-list' style='height:" + height + "'> \
      <div class='btn-group-vertical center-block' data-toggle='buttons'>";


    for (var i = 0; i < valueList.length; i++){
      elementString += " \
      <label id=" + valueList[i] + " class='btn btn-primary'> \
        <input " + required + " type='radio' name=" + buttonGroupName +
        " value=" + valueList[i]  + ">" + textList[i]  +" \
      </label>";
    }

     return $(elementString);
  }

  var selectOptionsMaker = function(textList){
    var elementString = "";
    for (var i = 0; i < textList.length; i++){
      elementString += "<option class='my-option'>" +  textList[i] + "</option>";
    }

     return $(elementString);
  }

  // Populate all the button list with these arrays
  var loadButtonLists = function() {
    var locationTextArray = ["Bldg200", "CathLab", "OSS", "OR", "CDDC", "UCI 1", "UCI 2"];
    var locationIDArray = ["bldg200", "cathlab", "OSS", "OR", "CDDC", "UCI_1", "UCI_2"];
    $("#location_selector").append(buttonListMaker("required", locationIDArray,
    locationTextArray, "location", "175px"));

    var facultyTextArray = ["Dr. Karnes", "Dr. Raus", "Dr. Patterson", "Dr. Spock"];
    var facultyIDArray = ["DrKarnes", "DrRaus", "DrPatterson", "DrSpock"];
    $("#faculty_selector").append(buttonListMaker("required", facultyIDArray,
    facultyTextArray, "faculty", "175px"));

    var fellowTextArray = ["None", "Pr. John", "Smith", "Carel", "Mike", "John", "Pat"];
    var fellowIDArray = ["None", "PrJohn", "Smith", "Carel", "Mike", "John", "Pat"];
    $("#fellow_selector").append(buttonListMaker("required", fellowIDArray,
    fellowTextArray, "fellow", "175px"));

    var preDrugTextArray = ["Miralax (or generic)", "PEG w lytes (Golytely, Trilyte, etc)",
    "Sodium picosulfate (Prepopik, Picoprepm, etc)", "MoviPrep", "Suprep",
    "Sodium phosphate (OsmoPrep, Visicol, etc)", "Magnesium citrate", "Other"];
    var preDrugIDArray = ["miralax", "PEG_w_lytes", "Sodium_picosulfate",
    "MoviPrep", "Suprep", "Sodium_phosphate", "Magnesium_citrate", "Other"];
    $("#pre_drug_selector").append(buttonListMaker("required", preDrugIDArray,
    preDrugTextArray, "pre_drug", "275px"));

    var lastColonTextArray = ["None", "< 6 months",
    "6-12 months", "1 year", "2 years", "3 years", "4 years", "5 years",
    "6 years", "7 years", "8 years", "9 years", "10 or more years"];
    var lastColonIDArray = ["None", "<6_m",
    "6-12_m", "1_yr", "2_yr", "3_yr", "4_yr", "5_yr",
    "6_yr", "7_yr", "8_yr", "9_yr", "10_yr"];
    $("#indications #last_colon_selector").append(buttonListMaker("required",
    lastColonIDArray, lastColonTextArray, "last_colon", "275px"));

    var scopeTextArray = ["Pediatric Colonscope", "Adult Colonscope", "EGD Scope",
    "Entro Scope", "Sigmoido Scope", "Other"];
    var scopeIDArray = ["Pediatric", "Adult", "EGD", "Entro", "Sigmoido", "Other"];
    $("#scope #scope_selector").append(buttonListMaker("required", scopeIDArray,
    scopeTextArray, "scope", "200px"));

    var versedIDTextArray = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    $("#sedation #versed_selector").append(buttonListMaker("", versedIDTextArray,
    versedIDTextArray, "versed", "200px"));

    var fentanylIDTextArray = [];
    for(var i = 0; i <= 300; i +=25){
      fentanylIDTextArray.push(i);
    }
    $("#sedation #fentanyl_selector").append(buttonListMaker("", fentanylIDTextArray,
    fentanylIDTextArray, "fentanyl", "200px"));

    var extentTextArray = ["TI", "Cecum", "Ileocolonic Anastomosis", "Ascending",
    "Hepatic Flexure", "Transverse", "Splenic Flexure", "Decending", "Sigmoid", "Rectsigmoid"];
    var extentIDArray = ["ti", "cecum", "ileocolonic_anastomosis", "ascending",
    "hepatic_flexure", "transverse", "splenic_flexure", "decending", "sigmoid", "rectsigmoid"];
    $("#extent #extent_selector").append(buttonListMaker("required", extentIDArray,
    extentTextArray, "extentReached", "350px"));

    var demerolIDTextArray = [];
    for(var i = 0; i <= 300; i +=25){
      demerolIDTextArray.push(i);
      if(i == 25) demerolIDTextArray.push(37.5);
      if(i == 50) demerolIDTextArray.push(62.5);
    }
    $("#sedation #demerol_selector").append(buttonListMaker("", demerolIDTextArray,
    demerolIDTextArray, "demerol", "200px"));

    var benadrylIDTextArray = [];
    for(var i = 0; i <= 100; i +=25){
      benadrylIDTextArray.push(i);
    }
    $("#sedation #benadryl_selector").append(buttonListMaker("", benadrylIDTextArray,
    benadrylIDTextArray, "benadryl", "200px"));

    var testIDTextArray = [];
    for(var i = 0; i <= 300; i +=10){
      testIDTextArray.push(i);
    }
    //TODO fix this
    $("#primaryIndication #screening_selector").append(buttonListMaker2("", testIDTextArray,
    testIDTextArray, "primary", "325px"));

    // $("#addPolypOrMassForm #location_selector").append(buttonListMaker("", testIDTextArray,
    // testIDTextArray, "test", "100px"));
    //
    // $("#primaryIndication #diagnostic_selector").append(buttonListMaker("", testIDTextArray,
    // testIDTextArray, "primary", "325px"));
    //
    // $("#primaryIndication #therapeutic_selector").append(buttonListMaker("", testIDTextArray,
    // testIDTextArray, "primary", "325px"));

    var locationArray = ["TI", "Cecum", "Ascending", "Hepatic Flexure", "Transverse",
    "Splenic Flexure", "Decending", "Sigmoid", "Rectsigmoid", "Rectum", "Anus",
    "Ileocolonic Anastomosis", "colocolonic Anastomosis"];
    $("#addPolypOrMass #location").append(selectOptionsMaker(locationArray));

    var phenotypeArray = ["Flat", "Sessile", "Stalk", "Mass", "Submucosal"];
    $("#addPolypOrMass #phenotype").append(selectOptionsMaker(phenotypeArray));

    var numberArray = [1,2,3,4,5,6,7,8,9,"10 or more"];
    $("#addPolypOrMass #number").append(selectOptionsMaker(numberArray));

    var sizeArray = [1,2,3,4,5,6,7,8,9,"10-14","15-19","20-29","30-39","40-49",">49"];
    $("#addPolypOrMass #size").append(selectOptionsMaker(sizeArray));

    var treatmentArray = ["Cold Bx", "Cold Snare", "EMR (lifted)", "EMR (under water)",
    "Hot Snare", "Hot Biopsy", "None", "Other"];
    $("#addPolypOrMass #treatment").append(selectOptionsMaker(treatmentArray));

    var residualArray = ["No", "Indeterminant", "Yes"];
    $("#addPolypOrMass #residual").append(selectOptionsMaker(residualArray));

    var bottleArray = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19];
    $("#addPolypOrMass #bottle").append(selectOptionsMaker(bottleArray));

    var primaryNiceArray = ["Bland/capillaries", "Dark Spots", "Light Spots",
    "Tubulogyrus", "Other"];
    $("#addPolypOrMass #primary_nice").append(selectOptionsMaker(primaryNiceArray));

    var secondaryNiceArray = ["NA","Bland/capillaries", "Dark Spots", "Light Spots",
    "Tubulogyrus", "Other"];
    $("#addPolypOrMass #secondary_nice").append(selectOptionsMaker(secondaryNiceArray));

    var pathGuessArray = ["Adenoma", "Hyperplastic", "SSA", "Infalammatory",
     "Lymphoid", "Maligancy","Other"];
    $("#addPolypOrMass #path_guess").append(selectOptionsMaker(pathGuessArray));
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

    var pre_drug = $("input[name=pre_drug]:checked").val();
    if (pre_drug == "Other"){
      // TODO: save this in another field
      var pre_drug_other = $("#pre_drug_other").val();
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

    var last_colon = $("#indications input[name=last_colon]:checked").val();
    var filter = $('input[name=filter]:checked').val();

    saveProcedureForm({"last_colon": last_colon}, "indicationForm", "#indications");
  });

  $("#scopeForm").submit(function(){
    event.preventDefault();

    var scope = $("#scope input[name=scope]:checked").val();
    if (scope == "Other"){
      // TODO: save this in another field
      var scope_other = $("#scope #scope_other").val();
    }
    var endocuff = $('#scope input[name=endocuff]:checked').val();
    var cap_assisted = $('#scope input[name=cap_assisted]:checked').val();
    var underwater = $('#scope input[name=underwater]:checked').val();

    saveProcedureForm({"scope": scope, "endocuff": endocuff,
    "cap_assisted": cap_assisted, "underwater": underwater}, "scopeForm", "#scope");
  });

  $("#sedationForm").submit(function(){
    event.preventDefault();
    var sedation_level = $('#sedation input[name=sedation_level]:checked').val();
    var versed = null;
    var fentanyl = null;
    var demerol = null;
    var benadryl = null;
    if (sedation_level == "Moderate"){
      versed = $('#sedation input[name=versed]:checked').val();
      fentanyl = $('#sedation input[name=fentanyl]:checked').val();
      demerol = $('#sedation input[name=demerol]:checked').val();
      benadryl = $('#sedation input[name=benadryl]:checked').val();
    }

    saveProcedureForm({"sedation_level": sedation_level, "versed": versed,
  "fentanyl": fentanyl, "demerol": demerol, "benadryl": benadryl}, "sedationForm", "#sedation");
  });

  $("#extentForm").submit(function(){
    event.preventDefault();
    var extent = $('#extent input[name=extentReached]:checked').val();

    if (extent == "ti" || extent == "cecum" || extent == "ileocolonic_anastomosis"){
      saveProcedureForm({"extent": extent}, "extentForm", "#extent");
    } else {
      var incomplete = $("#extent #extent_other").val();
      if (debug) console.log(incomplete);
      saveProcedureForm({"extent": extent}, "extentForm", "#extent");
    }

  });

  $("#qualityForm").submit(function(){
    event.preventDefault();
    var prep_quality_left = $('#quality input[name=prepLeft]:checked').val();
    var prep_quality_mid = $('#quality input[name=prepMid]:checked').val();
    var prep_quality_right = $('#quality input[name=prepRight]:checked').val();

    saveProcedureForm({"prep_quality_left": prep_quality_left,
    "prep_quality_mid": prep_quality_mid, "prep_quality_right": prep_quality_right},
    "qualityForm", "#quality");

  });

  $("#timesForm").submit(function(){
    event.preventDefault();
    var time_insertion = $('#times #time_insertion').val();
    var time_begin_withdrawal = $('#times #time_begin_withdrawal').val();
    var time_scope_withdrawn = $('#times #time_scope_withdrawn').val();

    saveProcedureForm({"time_insertion": time_insertion,
    "time_begin_withdrawal": time_begin_withdrawal,
    "time_scope_withdrawn": time_scope_withdrawn},
    "timesForm", "#times");

  });

  $("#addPolypOrMassForm").submit(function(){
    event.preventDefault();

    var location = $("#addPolypOrMass #location").val();
    var phenotype = $("#addPolypOrMass #phenotype").val();
    var number = $("#addPolypOrMass #number").val();
    var size = $("#addPolypOrMass #size").val();
    // var clipped = $("#addPolypOrMass #clipped_selector").val();
    var bottle = $("#addPolypOrMass #bottle").val();
    var start_polyp = $("#addPolypOrMass #start_polyp").val();
    var primary_nice = $("#addPolypOrMass #primary_nice").val();
    var secondary_nice = $("#addPolypOrMass #secondary_nice").val();
    var path_guess = $("#addPolypOrMass #path_guess").val();

    var pic = $("#addPolypOrMass #pic").val();
    if (debug) console.log(pic);


    saveProcedureForm({}, "addPolypOrMassForm", "#addPolypOrMass");

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

    $('#time_insertion').datetimepicker({
      format: 'HH:mm'
    });
    $('#time_begin_withdrawal').datetimepicker({
      format: 'HH:mm'
    });
    $('#time_scope_withdrawn').datetimepicker({
      format: 'HH:mm'
    });
    $('#start_polyp').datetimepicker({
      format: 'HH:mm'
    });
    $('#end_polyp').datetimepicker({
      format: 'HH:mm'
    });

    // Link Timepickers
    $("#time_insertion").on("dp.change", function (e) {
        $('#time_begin_withdrawal').data("DateTimePicker").minDate(e.date);
    });

    $("#time_begin_withdrawal").on("dp.change", function (e) {
        $('#time_scope_withdrawn').data("DateTimePicker").minDate(e.date);
    });

    $("#start_polyp").on("dp.change", function (e) {
        $('#end_polyp').data("DateTimePicker").minDate(e.date);
    });
  });

});
