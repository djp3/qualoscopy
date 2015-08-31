// (c) Luke Raus 2015

// The function tableMaker
var tableMaker = function(buttonName, buttonID, numberOfColumns, buttonTarget,
    tableLength, tableId, columnNames, columnValues){

     var tableHeading = "";
     var tableValues = "";
     for(var i = 0; i < numberOfColumns; i++){
       if(columnValues[i] == null){
         tableHeading += "<th class=incomplete>" + columnNames[i] + "</th>\n";
         tableValues += "<td class=incomplete>NONE</td>\n";
       } else {
         tableHeading += "<th class=complete>" + columnNames[i] + "</th>\n";
         tableValues += "<td class=complete>" + columnValues[i] + "</td>\n";
       }

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

// The function rowMakerPolyps
var rowMakerPolyps = function(data){

     var $element = "<tr class='clickable-row pointer'>";
     for(var i = 0; i < data.length; i++){
       $element += "<td>" + data[i] + "</td>";
     }
    $element += "</tr>";

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
        $("#tableBody").empty();
        for (var i = 0; i < polyps.length; i++){
          $("#tableBody").append(rowMakerPolyps(
          [polyps[i].polyp_id, polyps[i].time_removed, "NONE", "NONE", "NONE", "NONE", "NONE", "NONE"]));
        }
        $(".clickable-row").click(function() {
          $('#addPolypOrMass').modal("toggle");
        });

        if ( ! $.fn.DataTable.isDataTable( '#tablePolyps' ) ){
          $('#tablePolyps').dataTable( {
            "paging": false,
            "bFilter": false,
            "info": false
          });
        } else {
          // if (debug) console.log("loaded")
        }


        $("#btn_addPolypOrMass").click(function(){
          // $("#addPolypOrMass #location").val("Cecum");

        });


      }
    });
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
  var other_indication_count = 0;
  if (procedure.other_indication != null){
    other_indication_count = procedure.other_indication.split("_").length;
  }
  $allTables.push(tableMaker("Indications", "btn_indications", 3, "#indications", "col-md-5", "table2",
  ["Last Colon", "Indication", "Other Indications"],
  [procedure.last_colon, procedure.primary_indication, other_indication_count + " Selected"]));

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
  populatePolypsTable();
  createButtonFunctions(procedure);

}

// Update the tables be deleting them and creating them with new data
var updateTables = function(procedure){
  $("#tables").empty();
  populateTables(procedure);
}
