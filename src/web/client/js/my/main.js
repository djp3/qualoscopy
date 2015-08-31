// (c) Luke Raus 2015
$(document).ready(function() {

  // Load buttons and tables for main page
  onLoad();

  // Set up save buttons for each form
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

    var primary = $("#primaryIndication input[name=primary]:checked").val()
    $("#indications #primaryLabel").val(primary);
    $("#indications #primaryLabel").text(primary);
    $("#primaryIndication").modal('toggle');
  });

  $("#otherIndicationForm").submit(function(){
    event.preventDefault();

    var checkedIndications = [];
    var checkedString = ""
    $("#otherIndication input[type=checkbox]").each(function (){
      if (this.checked){
        checkedIndications.push($(this).val());
        checkedString += $(this).val() + "_";
      }

    });

    checkedString = checkedString.substring(0, checkedString.length - 1);

    if(debug) console.log(checkedIndications);
    $("#indications #otherLabel").val(checkedString);
    $("#indications #otherLabel").text(checkedIndications.length + " Checked");
    $("#otherIndication").modal('toggle');
  });

  $("#indicationForm").submit(function(){
    event.preventDefault();

    var last_colon = $("#indications input[name=last_colon]:checked").val();
    var filter = $('input[name=filter]:checked').val();
    var primary_indication = $("#indications #primaryLabel").val();
    var other_indication = $("#indications #otherLabel").val();
    if(debug) console.log(primary_indication);
    if(debug) console.log(other_indication);

    saveProcedureForm({"last_colon": last_colon,
    "primary_indication": primary_indication,
    "other_indication": other_indication}, "indicationForm", "#indications");
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

  $("#addPolypOrMass #pic").change(function(e) {
      var preview = document.getElementById('preview') //selects the query named img
      var file    = document.getElementById('pic').files[0]; //sames as here
      var reader  = new FileReader();

      reader.onloadend = function () {
          preview.src = reader.result; // Bytes
      }

      if (file) {
          reader.readAsDataURL(file); //reads the data as a URL
      } else {
          preview.src = "assets/addImage.png";
      }
      $("#addPolypOrMass #preview").attr("height", "271px");
  });

  $("#addPolypOrMass #remove").click(function() {
     var preview = document.getElementById('preview') //selects the query named img
     var reader  = new FileReader();

     reader.onloadend = function () {
         preview.src = reader.result;
     }

    preview.src = "assets/addImage.png";

     $("#addPolypOrMass #preview").attr("height", "271px");
  });


  // Sets up date and timepicker Widgets
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
