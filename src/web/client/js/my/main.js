// (c) Luke Raus 2015
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
          [polyps[0].polyp_id, "NONE", "NONE", "NONE", "NONE", "NONE", "NONE", "NONE"]));

          $("#btn_addPolypOrMass").click(function(){
            // $("#addPolypOrMass #location").val("Cecum");

          });


        }
      });
  }

  // populate identifiers Form
  var populateIdentifiersForm = function(procedure){
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
  }

  // populate preparation Form
  var populatePreparationForm = function(procedure){
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
  }

  // populate Indications Form
  var populateIndicationsForm = function(procedure){
    $("#btn_indications").click(function(){
      $("#indications #" + procedure.last_colon).addClass("active");
      $("#indications #" + procedure.last_colon + " input").prop('checked', "checked");

      $("#primaryIndication #pill_selector li").on("click", function(){
        $(".nav.nav-pills li").removeClass("active");
        $(this).addClass("active");

        $("#primaryIndication #indication_selector").empty();
        var pill_id = $(this).attr('id');
        if(pill_id == 1) {
          loadScreeningIndicationsButtonList();
        }else if(pill_id == 2){
          loadSurveillanceIndicationsButtonList();
        }else if(pill_id == 3){
          loadDiagnosticIndicationsButtonList();
        }else if(pill_id == 4){
          loadTherapeuticIndicationsButtonList();
        }else if(pill_id == 5){
          loadPreoperativeIndicationsButtonList();
        }
      });
    });
  }

  // populate Scope Form
  var populateScopeForm = function(procedure){
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
  }

  // populate Sedation Form
  var populateSedationForm = function(procedure){
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
  }

  // populate Extent Form
  var populateExtentForm = function(procedure){
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
  }

  // populate quality Form
  var populateQualityForm = function(procedure){
    $("#btn_quality").click(function(){
      $("#quality .prep_left #" + procedure.prep_quality_left).addClass("active");
      $("#quality .prep_left #" + procedure.prep_quality_left + " input").prop('checked', "checked");

      $("#quality .prep_mid #" + procedure.prep_quality_mid).addClass("active");
      $("#quality .prep_mid #" + procedure.prep_quality_mid + " input").prop('checked', "checked");

      $("#quality .prep_right #" + procedure.prep_quality_right).addClass("active");
      $("#quality .prep_right #" + procedure.prep_quality_right + " input").prop('checked', "checked");
    });
  }

  // populate quality Form
  var populateTimesForm = function(procedure){
    $("#btn_times").click(function(){
      $("#times #time_insertion").val(procedure.time_insertion);
      $("#times #time_begin_withdrawal").val(procedure.time_begin_withdrawal);
      $("#times #time_scope_withdrawn").val(procedure.time_scope_withdrawn);
    });
  }

  // Create click functions for buttons
  var createButtonFunctions = function(procedure){
    populateIdentifiersForm(procedure);
    populatePreparationForm(procedure);
    populateIndicationsForm(procedure);
    populateScopeForm(procedure);
    populateSedationForm(procedure);
    populateExtentForm(procedure);
    populateQualityForm(procedure);
    populateTimesForm(procedure);
  }

  // Populate the tables of the main screen with Procedure Info
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

  // Create HTML button list
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

  // Create HTML button list with 2 columns
  var buttonListMakerTwoCol = function(required, idList1, idList2,
    textList1, textList2, buttonGroupName, height){

    var elementString = "<div class='button-list' style='height:" + height + "'> \
      <div data-toggle='buttons'> \
        <div class='col-md-6 btn-group-vertical center-block btn-group'>";


    for (var i = 0; i < idList1.length; i++){
      elementString += " \
      <label id=" + idList1[i] + " class='btn btn-primary'> \
        <input " + required + " type='radio' name=" + buttonGroupName +
        " value=" + idList1[i]  + ">" + textList1[i]  +" \
      </label>";
    }

    elementString += "</div><div class='col-md-6 btn-group-vertical center-block btn-group'>";

    for (var i = 0; i < idList2.length; i++){
      elementString += " \
      <label id=" + idList2[i] + " class='btn btn-primary'> \
        <input " + required + " type='radio' name=" + buttonGroupName +
        " value=" + idList2[i]  + ">" + textList2[i]  +" \
      </label>";
    }

    elementString += "</div>";

     return $(elementString);
  }

  // Create HTML button list with 3 columns
  var buttonListMakerThreeCol = function(required, idList1, idList2, idList3,
    textList1, textList2, textList3, buttonGroupName, height){
    var elementString = "<div class='button-list' style='height:" + height + "'> \
      <div data-toggle='buttons'> \
        <div class='col-md-4 btn-group-vertical center-block btn-group'>";


    for (var i = 0; i < idList1.length; i++){
      elementString += " \
      <label id=" + idList1[i] + " class='btn btn-primary'> \
        <input " + required + " type='radio' name=" + buttonGroupName +
        " value=" + idList1[i]  + ">" + textList1[i]  +" \
      </label>";
    }

    elementString += "</div><div class='col-md-4 btn-group-vertical center-block btn-group'>";

    for (var i = 0; i < idList2.length; i++){
      elementString += " \
      <label id=" + idList2[i] + " class='btn btn-primary'> \
        <input " + required + " type='radio' name=" + buttonGroupName +
        " value=" + idList2[i]  + ">" + textList2[i]  +" \
      </label>";
    }

    elementString += "</div><div class='col-md-4 btn-group-vertical center-block btn-group'>";

    for (var i = 0; i < idList3.length; i++){
      elementString += " \
      <label id=" + idList3[i] + " class='btn btn-primary'> \
        <input " + required + " type='radio' name=" + buttonGroupName +
        " value=" + idList3[i]  + ">" + textList3[i]  +" \
      </label>";
    }

    elementString += "</div>";

     return $(elementString);
  }

  // Create a list of <option> HTML elements from an array
  var selectOptionsMaker = function(textList){
    var elementString = "";
    for (var i = 0; i < textList.length; i++){
      elementString += "<option class='my-option'>" +  textList[i] + "</option>";
    }

     return $(elementString);
  }

  // Load the button list for Identifiers Modal
  var loadIdentifiersButtonList = function(){
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

  }

  // Load the button list for Preparation Modal
  var loadPrepButtonList = function(){
    var preDrugTextArray = ["Miralax (or generic)", "PEG w lytes (Golytely, Trilyte, etc)",
    "Sodium picosulfate (Prepopik, Picoprepm, etc)", "MoviPrep", "Suprep",
    "Sodium phosphate (OsmoPrep, Visicol, etc)", "Magnesium citrate", "Other"];
    var preDrugIDArray = ["miralax", "PEG_w_lytes", "Sodium_picosulfate",
    "MoviPrep", "Suprep", "Sodium_phosphate", "Magnesium_citrate", "Other"];
    $("#pre_drug_selector").append(buttonListMaker("required", preDrugIDArray,
    preDrugTextArray, "pre_drug", "275px"));
  }

  // Load the button list for Indications Modal
  var loadIndicationsButtonList = function(){
    var lastColonTextArray = ["None", "< 6 months",
    "6-12 months", "1 year", "2 years", "3 years", "4 years", "5 years",
    "6 years", "7 years", "8 years", "9 years", "10 or more years"];
    var lastColonIDArray = ["None", "6_m",
    "6-12_m", "1_yr", "2_yr", "3_yr", "4_yr", "5_yr",
    "6_yr", "7_yr", "8_yr", "9_yr", "10_yr"];
    $("#indications #last_colon_selector").append(buttonListMaker("required",
    lastColonIDArray, lastColonTextArray, "last_colon", "275px"));
  }

  // Load the button list for Screening Indications Modal
  var loadScreeningIndicationsButtonList = function(){
    var screening1IDArray = ["Screening:1","Screening:2","Screening:3","Screening:4",
    "Screening:5","Screening:6","Screening:7"];
    var screening1TextArray = [
    "1. Average Rist No Gi Sx, Signs, Hx or FHx",
    "2. Family History Colorectal Cancer <br> (One immediate family member (age 50-75))",
    "3. Family History Colorectal Cancer <br> (2 or more immediate family members)",
    "4. Family History Colorectal Cancer <br> (one or more family members under age 50)",
    "5. Family History Colorectal Cancer <br> (Unknown number/relationships)",
    "6. Family History Genetic Cancer Syndrome <br> (Lynch Syndrome HNPCC)",
    "7. Family History Genetic Cancer Syndrome <br> (Familial Colon Cancer Type X FCCTX)"];

    var screening2IDArray = ["Screening:8","Screening:9","Screening:10","Screening:11",
    "Screening:12","Screening:13","Screening:14","Screening:15","Screening:16"];
    var screening2TextArray = [
    "8. Family History Genetic Cancer Syndrome <br> (Peutz-Jegher Syndrome)",
    "9. Family History Genetic Cancer Syndrome <br> (MYH-Associated Polyposis)",
    "10. Family History Genetic Cancer Syndrome <br> (Cowden's)",
    "11. Family History Polyp(s) (Unknown number/type)",
    "12. Family History Polyp(s) (Multiple Adenomas)",
    "13. Family History Polyp(s) (Serrated Polyposis)",
    "14. Fecal Test DNA",
    "15. Fecal Test FIT",
    "16. Fecal Test FOBT"];

    $("#primaryIndication #indication_selector").append(buttonListMakerTwoCol(
    "required", screening1IDArray, screening2IDArray, screening1TextArray,
    screening2TextArray, "primary", "390px"));
  }

  // Load the button list for Surveillance Indications Modal
  var loadSurveillanceIndicationsButtonList = function(){
    var surveillance1IDArray = ["Surveillance:1","Surveillance:2","Surveillance:3","Surveillance:4",
    "Surveillance:5","Surveillance:6","Surveillance:7","Surveillance:8"];
    var surveillance1TextArray = [
    "1. Personal History Colorectal Cancer (one)",
    "2. Personal History Colorectal Cancer <br> (more than one)",
    "3. Personal History Colorectal Cancer <br> (under age 50)",
    "4. Personal History Colorectal Cancer <br> (Unknown number/type)",
    "5. Personal History Infalmmatory Bowel Disease <br> (Pancolits => 8 years)",
    "6. Personal History Infalmmatory Bowel Disease <br> (Left-sided Colitis => 15 years)",
    "7. Personal History Polyps Adenomas (2 or less)",
    "8. Personal History Polyps Adenomas (3-10)"];

    var surveillance2IDArray = ["Surveillance:9","Surveillance:10",
    "Surveillance:11","Surveillance:12","Surveillance:13","Surveillance:14","Surveillance:15","Surveillance:16"];
    var surveillance2TextArray = [
    "9. Personal History Polyps Adenomas (11-19)",
    "10. Personal History Polyps Adenomas (20 or more)",
    "11. Personal History Polyps Adenomas (Villous adenoma)",
    "12. Personal History Polyps Serrated Polyps <br> (1-5, None > 9 mm)",
    "13. Personal History Polyps Serrated Polyps <br> (1-5, Any > 9 mm)",
    "14. Personal History Polyps Serrated Polyps <br> (5-19, right-sided, one > 9 mm)",
    "15. Personal History Polyps Serrated Polyps <br> (20 or more, any size, anywhere)",
    "16. Personal History Polyps Serrated Polyps with dysplasia"];

    $("#primaryIndication #indication_selector").append(buttonListMakerTwoCol(
    "required", surveillance1IDArray, surveillance2IDArray, surveillance1TextArray,
    surveillance2TextArray, "primary", "390px"));
  }

  // Load the button list for Diagnostic Indications Modal
  var loadDiagnosticIndicationsButtonList = function(){
    var diagnostic1IDArray = ["Diagnostic:1","Diagnostic:2","Diagnostic:3",
    "Diagnostic:4","Diagnostic:5","Diagnostic:6","Diagnostic:7","Diagnostic:8",
    "Diagnostic:9","Diagnostic:10"];
    var diagnostic1TextArray = [
    "1. Abnormal Finding Endoscopic",
    "2. Abnormal Finding Imaging",
    "3. Abnormal Finding Surgical",
    "4. Altered Stool Change <br> in bowel movements",
    "5. Anemia Iron Deficiency",
    "6. Anemia Unspecified",
    "7. Bleeding Melena",
    "8. Bleedning Rectal",
    "9. Constipation Chronic (>6week)",
    "10. Diarrhea Chronic (>6 week)"];

    var diagnostic2IDArray = ["Diagnostic:11","Diagnostic:12","Diagnostic:13"
    ,"Diagnostic:14","Diagnostic:15","Diagnostic:16","Diagnostic:17","Diagnostic:18"
    ,"Diagnostic:19"];
    var diagnostic2TextArray = [
    "11. Inflammatory Bowel Disease <br> Assess Extent/Severity",
    "12. Inflammatory Bowel Disease <br> Establish Specific Diagnosis",
    "13. Pain Abdomen (RLQ)",
    "14. Pain Abdomen (LLQ)",
    "15. Pain Abdomen (Suprapubic)",
    "16. Pain Abdomen (Periumbilical)",
    "17. Pain Abdomen (RUQ)",
    "18. Pain Abdomen (Epigastric)",
    "19. Pain Abdomen (LUQ)"];

    var diagnostic3IDArray = ["Diagnostic:20","Diagnostic:21",
    "Diagnostic:22","Diagnostic:23","Diagnostic:24"];
    var diagnostic3TextArray = [
    "20. Pain Abdomen (Generalized)",
    "21. Pain Anorectal",
    "22. Pain Back",
    "23. Pain Chest",
    "24. Weight Loss (Unexplained)"];

    $("#primaryIndication #indication_selector").append(buttonListMakerThreeCol(
    "required", diagnostic1IDArray, diagnostic2IDArray, diagnostic3IDArray,
     diagnostic1TextArray, diagnostic2TextArray, diagnostic3TextArray,
     "primary", "390px"));
  }

  // Load the button list for Therapeutic Indications Modal
  var loadTherapeuticIndicationsButtonList = function(){
    var therapeutic1IDArray = ["Therapeutic:1","Therapeutic:2","Therapeutic:3","Therapeutic:4",
    "Therapeutic:5","Therapeutic:6","Therapeutic:7","Therapeutic:8","Therapeutic:9",
    "Therapeutic:10"];
    var therapeutic1TextArray = [
    "1. Bleeding Control of Bleeding (Known AVM(s))",
    "2. Bleeding Control of Bleeding (post-polypectomy bleed)",
    "3. Bleeding Control of Bleeding (Known Mass/Polyp)",
    "4. Bleeding Control of Bleeding <br> (Known Diverticular bleed)",
    "5. Bleeding Control of Bleeding (Other)",
    "6. C. Difficle Fecal Microbiota Transplantaion",
    "7. Foreign Body Stent (Removal)",
    "8. Foreign Body Unspecified (Removal)",
    "9. IBD Fecal Microbiota Transplantaion",
    "10. Mass/Polup Resection"];

    var therapeutic2IDArray = ["Therapeutic:11","Therapeutic:12",
    "Therapeutic:13"];
    var therapeutic2TextArray = [
    "11. Stricture Dilation",
    "12. Stricture Stent (Placement)",
    "13. Stricture Stent (Replacement or Repeat)"];

    $("#primaryIndication #indication_selector").append(buttonListMakerTwoCol(
    "required", therapeutic1IDArray, therapeutic2IDArray, therapeutic1TextArray,
    therapeutic2TextArray, "primary", "390px"));
  }

  // Load the button list for Preoperative Indications Modal
  var loadPreoperativeIndicationsButtonList = function(){

    var preoperative1IDArray = ["Preoperative:1","Preoperative:2",
    "Preoperative:3"];
    var preoperative1TextArray = [
    "1. Colostomy takedown",
    "2. Indentify or Clear Synchronoous Lesion",
    "3. Mass/Polyp Mark/Tatoo"];

    var preoperative2IDArray = [];
    var preoperative2TextArray = [];

    $("#primaryIndication #indication_selector").append(buttonListMakerTwoCol(
    "required", preoperative1IDArray, preoperative2IDArray,
     preoperative1TextArray, preoperative2TextArray, "primary", "390px"));

  }

  // Load the button list for Scope Modal
  var loadScopeButtonList = function(){
      var scopeTextArray = ["Pediatric Colonscope", "Adult Colonscope", "EGD Scope",
      "Entro Scope", "Sigmoido Scope", "Other"];
      var scopeIDArray = ["Pediatric", "Adult", "EGD", "Entro", "Sigmoido", "Other"];
      $("#scope #scope_selector").append(buttonListMaker("required", scopeIDArray,
      scopeTextArray, "scope", "200px"));
  }

  // Load the button list for Sedation Modal
  var loadSedationButtonList = function(){
    var versedIDTextArray = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
    $("#sedation #versed_selector").append(buttonListMaker("", versedIDTextArray,
    versedIDTextArray, "versed", "200px"));

    var fentanylIDTextArray = [];
    for(var i = 0; i <= 300; i +=25){
      fentanylIDTextArray.push(i);
    }
    $("#sedation #fentanyl_selector").append(buttonListMaker("", fentanylIDTextArray,
    fentanylIDTextArray, "fentanyl", "200px"));

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
  }

  // Load the button list for Extent Modal
  var loadExtentButtonList = function(){
    var extentTextArray = ["TI", "Cecum", "Ileocolonic Anastomosis", "Ascending",
    "Hepatic Flexure", "Transverse", "Splenic Flexure", "Decending", "Sigmoid", "Rectsigmoid"];
    var extentIDArray = ["ti", "cecum", "ileocolonic_anastomosis", "ascending",
    "hepatic_flexure", "transverse", "splenic_flexure", "decending", "sigmoid", "rectsigmoid"];
    $("#extent #extent_selector").append(buttonListMaker("required", extentIDArray,
    extentTextArray, "extentReached", "350px"));
  }

  // Load the button list for Extent Modal
  var loadAddPolypSelectOptions = function(){
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

  // Populate all the button list with these arrays
  var loadButtonLists = function() {
    loadIdentifiersButtonList();
    loadPrepButtonList();
    loadIndicationsButtonList();
    loadScreeningIndicationsButtonList();
    loadScopeButtonList();
    loadSedationButtonList();
    loadExtentButtonList();
    loadAddPolypSelectOptions();
  }

  // Update the tables be deleting them and creating them with new data
  var updateTables = function(procedure){
    $("#tables").empty();
    populateTables(procedure);
  }

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

  $("#primaryIndicationForm").submit(function(){
    event.preventDefault();

    var primary = $("#primaryIndication input[name=primary]:checked").val();
    $("#indications #primary").val(primary);
    $("#indications #primaryLabel").text(primary);
    $("#primaryIndication").modal('toggle');
  });

  $("#indicationForm").submit(function(){
    event.preventDefault();

    var last_colon = $("#indications input[name=last_colon]:checked").val();
    var filter = $('input[name=filter]:checked').val();
    // Getting the text might not be the best way to do this but it works
    var primary_indication = $("#indications #primaryLabel").text();
    if(debug) console.log(primary_indication);

    saveProcedureForm({"last_colon": last_colon,
    "primary_indication": primary_indication}, "indicationForm", "#indications");
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

  // Sets up date and timepicker Widgets
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
