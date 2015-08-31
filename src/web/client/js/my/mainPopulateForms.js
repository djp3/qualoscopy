// (c) Luke Raus 2015
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

// populate primary indications form
var polulatePrimaryIndicationForm = function(procedure){
  $("#indications #primaryLabel").click(function(){
    $("#primaryIndication .nav.nav-pills li").removeClass("active");
    $("#primaryIndication #indication_selector").empty();
    var primary = procedure.primary_indication;
    var primary_label = $("#indications #primaryLabel").val();
    if (primary != null || primary_label != ""){
      var primary_category;
      if (primary_label == ""){
        primary_category = primary.split("-")[0];
      } else {
        primary_category = primary_label.split("-")[0];
        primary = primary_label;
      }


      if (primary_category == "Screening"){
        $("#primaryIndication #pill_selector #1").addClass("active");
        loadScreeningIndicationsButtonList("screening_list",
          "#primaryIndication #indication_selector", "required", "radio");

      } else if (primary_category == "Surveillance"){
        $("#primaryIndication #pill_selector #2").addClass("active");
        loadSurveillanceIndicationsButtonList("surveillance_list",
          "#primaryIndication #indication_selector", "required", "radio");

      } else if (primary_category == "Diagnostic"){
        $("#primaryIndication #pill_selector #3").addClass("active");
        loadDiagnosticIndicationsButtonList("diagnostic_list",
          "#primaryIndication #indication_selector", "required", "radio");

      } else if (primary_category == "Therapeutic"){
        $("#primaryIndication #pill_selector #4").addClass("active");
        loadTherapeuticIndicationsButtonList("therapeutic_list",
          "#primaryIndication #indication_selector", "required", "radio");

      } else if (primary_category == "Preoperative"){
        $("#primaryIndication #pill_selector #5").addClass("active");
        loadPreoperativeIndicationsButtonList("preoperative_list",
          "#primaryIndication #indication_selector", "required", "radio");
      }

      $("#primaryIndication #" + primary).addClass("active");
      $("#primaryIndication #" + primary + " input").prop('checked', "checked");

    } else {
      $("#primaryIndication #pill_selector #1").addClass("active");
      loadScreeningIndicationsButtonList("screening_list",
        "#primaryIndication #indication_selector", "required", "radio");
    }

    $("#primaryIndication #pill_selector li").on("click", function(){
      $("#primaryIndication .nav.nav-pills li").removeClass("active");
      $(this).addClass("active");

      $("#primaryIndication #indication_selector").empty();
      var pill_id = $(this).attr('id');
      if(pill_id == 1) {
        loadScreeningIndicationsButtonList("screening_list",
          "#primaryIndication #indication_selector", "required", "radio");
      }else if(pill_id == 2){
        loadSurveillanceIndicationsButtonList("surveillance_list",
          "#primaryIndication #indication_selector", "required", "radio");
      }else if(pill_id == 3){
        loadDiagnosticIndicationsButtonList("diagnostic_list",
          "#primaryIndication #indication_selector", "required", "radio");
      }else if(pill_id == 4){
        loadTherapeuticIndicationsButtonList("therapeutic_list",
          "#primaryIndication #indication_selector", "required", "radio");
      }else if(pill_id == 5){
        loadPreoperativeIndicationsButtonList("preoperative_list",
          "#primaryIndication #indication_selector", "required", "radio");
      }
    });

  });
}

// populate other indications form
var populateOtherIndicationsForm = function(procedure){
  $("#indications #otherLabel").click(function(){
    $("#otherIndication #indication_selector").empty();

    loadScreeningIndicationsButtonList("1",
      "#otherIndication #indication_selector", "", "checkbox");
    loadSurveillanceIndicationsButtonList("2",
      "#otherIndication #indication_selector", "", "checkbox");
    loadDiagnosticIndicationsButtonList("3",
      "#otherIndication #indication_selector", "", "checkbox");
    loadTherapeuticIndicationsButtonList("4",
      "#otherIndication #indication_selector", "", "checkbox");
    loadPreoperativeIndicationsButtonList("5",
      "#otherIndication #indication_selector", "", "checkbox");


    for (var i = 1; i <=5; i++ ){
      $("#otherIndication #indication_selector #"+ i).addClass("hide");
    }

    $(this).removeClass("active");


    if (procedure.other_indication != null){
      var other = procedure.other_indication;;
      var other_array = other_array = other.split("_");
      for(var i = 0; i < other_array.length; i++){
        $("#otherIndication #" + other_array[i]).addClass("active");
        $("#otherIndication #" + other_array[i] + " input").prop('checked', "checked");
      }
      for (var i = 1; i <= 5; i++){
        var count = $("#otherIndication #indication_selector #"+ i +" :checked").length;
        $("#otherIndication #pill_selector #"+ i +" .badge").text(count);
      }

      var init_pill_id = $('#otherIndication .nav.nav-pills li.active').attr('id');
      $("#otherIndication #indication_selector #"+ init_pill_id).removeClass("hide");
    } else {
      $('#otherIndication .nav.nav-pills li #1').addClass("active");
      $("#otherIndication #indication_selector #1").removeClass("hide");
    }




    $("#otherIndication #pill_selector li").on("click", function(){
      $("#otherIndication .nav.nav-pills li").removeClass("active");
      $(this).addClass("active");

      $("#otherIndication #indication_selector .button-list").addClass("hide");
      var pill_id = $(this).attr('id');
      $("#otherIndication #indication_selector #" + pill_id).removeClass("hide");
    });

    $("#otherIndication #indication_selector .button-list input").change(function(){
      var id = $('#otherIndication .nav.nav-pills li.active').attr('id');
      var count = $("#otherIndication #indication_selector #" + id +" :checked").length;
      $("#otherIndication #pill_selector #"+ id +" .badge").text(count);
    });
  });
}

// populate Indications Form
var populateIndicationsForm = function(procedure){
  $("#btn_indications").click(function(){
    $("#indications #" + procedure.last_colon).addClass("active");
    $("#indications #" + procedure.last_colon + " input").prop('checked', "checked");
    if(procedure.primary_indication != null){
      var primary = procedure.primary_indication;
      $("#indications #primaryLabel").val(primary);
      $("#indications #primaryLabel").text(primary);
    }

    if (procedure.other_indication != null){
      var other = procedure.other_indication;
      $("#indications #otherLabel").val(other);
      var other_array = other.split("_");
      if(debug) console.log(other_array);
      $("#indications #otherLabel").text(other_array.length + " Checked");
    }

    polulatePrimaryIndicationForm(procedure);
    populateOtherIndicationsForm(procedure);
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
