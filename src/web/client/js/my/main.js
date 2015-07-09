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

  // For all the tables
  var $allTables = [];

  // Created each table programically
  // Identifiers
  $allTables.push(tableMaker("Identifiers", 4, "#identifiers", "col-md-6", "table0",
  ["Account Number", "Date of Service", "Faculty", "Fellow"],
  ["00000000", "2/15/2015", "Karnes, William", "None"]));

  // Preparation
  $allTables.push(tableMaker("Preparation", 3, "#preparation", "col-md-6", "table1",
  ["Prep Liters", "Split Prep", "Bisacodyl"],
  ["3", "Yes", "Yes"]));

  // Indications
  $allTables.push(tableMaker("Indications", 5, "#indications", "col-md-6", "table2",
  ["Last Colon", "Indication", "Category", "Subcategory", "Specifics"],
  ["5 yrs", "Surveillance", "Personal History", "Colorectal Cancer", "(One)"]));

  // Scope
  $allTables.push(tableMaker("Scope", 4, "#scope", "col-md-6", "table3",
  ["Scopes", "Underwater", "CapAssisted", "EndoCuff"],
  ["Pediatric Colonscope", "Yes", "No", "Yes"]));

  // Sedation
  $allTables.push(tableMaker("Sedation", 6, "#sedation", "col-md-6", "table4",
  ["Sedation Level", "Versed", "Fentanyl", "Dernerol", "Benadryl", "Other Med"],
  ["Moderate", "3", "75", "0", "0", "NA"]));

  // Extent
  $allTables.push(tableMaker("Extent", 2, "#extent", "col-md-6", "table5",
  ["Extent", "Reson Incomplete"],
  ["Cecum", "N/A"]));

  // Prep Quality
  $allTables.push(tableMaker("Prep Quality", 3, "#quality", "col-md-6", "table6",
  ["Prep Left", "Prep Mid", "Prep Right"],
  ["3", "3", "3"]));

  // Times
  $allTables.push(tableMaker("Times", 3, "#times", "col-md-6", "table7",
  ["Time Insertion", "Time Begin Withdrawal", "Time Scope Withdrawn"],
  ["08:12", "08:12", "08:35"]));

  // Add Polyps or Mass
  $allTables.push(tableMaker("Add Polyps or Mass", 8, "#addPolypOrMass", "col-md-9", "table7",
  ["Loc", "Phe", "Num", "Size", "Tx", "Residual", "Bottle", "Path"],
  ["Cecum", "Sessile", "1", "3", "Cold Snare", "No", "1", "Tubular Adenoma"]));

  for (var i = 0; i < $allTables.length; i++) {
      $("#tables").append($allTables[i]);
  }


  $(function () {
    // Set up date and time pickers
    $('#datepicker1').datetimepicker({
       format: 'MM/DD/YYYY'
    });
    $('#timepicker1').datetimepicker({
      format: 'LT'
    });
    $('#timepicker2').datetimepicker({
      format: 'LT'
    });
    $('#timepicker3').datetimepicker({
      format: 'LT'
    });
    $('#timepicker4').datetimepicker({
      format: 'LT'
    });
    $('#timepicker5').datetimepicker({
      format: 'LT'
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
