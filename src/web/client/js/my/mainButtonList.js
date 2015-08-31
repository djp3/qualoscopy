// (c) Luke Raus 2015

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
var buttonListMakerTwoCol = function(listID, required, idList1, idList2,
  textList1, textList2, buttonGroupName, height, type){

  var elementString = "<div id=" + listID + " class='button-list' style='height:" + height + "'> \
    <div data-toggle='buttons'> \
      <div class='col-md-6 btn-group-vertical center-block btn-group'>";


  for (var i = 0; i < idList1.length; i++){
    elementString += " \
    <label id=" + idList1[i] + " class='btn btn-primary'> \
      <input " + required + " type=" + type + " name=" + buttonGroupName +
      " value=" + idList1[i]  + ">" + textList1[i]  +" \
    </label>";
  }

  elementString += "</div><div class='col-md-6 btn-group-vertical center-block btn-group'>";

  for (var i = 0; i < idList2.length; i++){
    elementString += " \
    <label id=" + idList2[i] + " class='btn btn-primary'> \
      <input " + required + " type=" + type + " name=" + buttonGroupName +
      " value=" + idList2[i]  + ">" + textList2[i]  +" \
    </label>";
  }

  elementString += "</div>";

   return $(elementString);
}

// Create HTML button list with 3 columns
var buttonListMakerThreeCol = function(listID, required, idList1, idList2, idList3,
  textList1, textList2, textList3, buttonGroupName, height, type){
  var elementString = "<div id=" + listID + " class='button-list' style='height:" + height + "'> \
    <div data-toggle='buttons'> \
      <div class='col-md-4 btn-group-vertical center-block btn-group'>";


  for (var i = 0; i < idList1.length; i++){
    elementString += " \
    <label id=" + idList1[i] + " class='btn btn-primary'> \
      <input " + required + " type=" + type + " name=" + buttonGroupName +
      " value=" + idList1[i]  + ">" + textList1[i]  +" \
    </label>";
  }

  elementString += "</div><div class='col-md-4 btn-group-vertical center-block btn-group'>";

  for (var i = 0; i < idList2.length; i++){
    elementString += " \
    <label id=" + idList2[i] + " class='btn btn-primary'> \
      <input " + required + " type=" + type + " name=" + buttonGroupName +
      " value=" + idList2[i]  + ">" + textList2[i]  +" \
    </label>";
  }

  elementString += "</div><div class='col-md-4 btn-group-vertical center-block btn-group'>";

  for (var i = 0; i < idList3.length; i++){
    elementString += " \
    <label id=" + idList3[i] + " class='btn btn-primary'> \
      <input " + required + " type=" + type + " name=" + buttonGroupName +
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
var loadScreeningIndicationsButtonList = function(listID, screenID, required, type){
  var screening1IDArray = ["Screening-1","Screening-2","Screening-3","Screening-4",
  "Screening-5","Screening-6","Screening-7"];
  var screening1TextArray = [
  "1. Average Rist No Gi Sx, Signs, Hx or FHx",
  "2. Family History Colorectal Cancer <br> (One immediate family member (age 50-75))",
  "3. Family History Colorectal Cancer <br> (2 or more immediate family members)",
  "4. Family History Colorectal Cancer <br> (one or more family members under age 50)",
  "5. Family History Colorectal Cancer <br> (Unknown number/relationships)",
  "6. Family History Genetic Cancer Syndrome <br> (Lynch Syndrome HNPCC)",
  "7. Family History Genetic Cancer Syndrome <br> (Familial Colon Cancer Type X FCCTX)"];

  var screening2IDArray = ["Screening-8","Screening-9","Screening-10","Screening-11",
  "Screening-12","Screening-13","Screening-14","Screening-15","Screening-16"];
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

  $(screenID).append(buttonListMakerTwoCol(listID,
  required, screening1IDArray, screening2IDArray, screening1TextArray,
  screening2TextArray, "primary", "390px", type));
}

// Load the button list for Surveillance Indications Modal
var loadSurveillanceIndicationsButtonList = function(listID, screenID, required, type){
  var surveillance1IDArray = ["Surveillance-1","Surveillance-2","Surveillance-3","Surveillance-4",
  "Surveillance-5","Surveillance-6","Surveillance-7","Surveillance-8"];
  var surveillance1TextArray = [
  "1. Personal History Colorectal Cancer (one)",
  "2. Personal History Colorectal Cancer <br> (more than one)",
  "3. Personal History Colorectal Cancer <br> (under age 50)",
  "4. Personal History Colorectal Cancer <br> (Unknown number/type)",
  "5. Personal History Infalmmatory Bowel Disease <br> (Pancolits => 8 years)",
  "6. Personal History Infalmmatory Bowel Disease <br> (Left-sided Colitis => 15 years)",
  "7. Personal History Polyps Adenomas (2 or less)",
  "8. Personal History Polyps Adenomas (3-10)"];

  var surveillance2IDArray = ["Surveillance-9","Surveillance-10",
  "Surveillance-11","Surveillance-12","Surveillance-13","Surveillance-14","Surveillance-15","Surveillance-16"];
  var surveillance2TextArray = [
  "9. Personal History Polyps Adenomas (11-19)",
  "10. Personal History Polyps Adenomas (20 or more)",
  "11. Personal History Polyps Adenomas (Villous adenoma)",
  "12. Personal History Polyps Serrated Polyps <br> (1-5, None > 9 mm)",
  "13. Personal History Polyps Serrated Polyps <br> (1-5, Any > 9 mm)",
  "14. Personal History Polyps Serrated Polyps <br> (5-19, right-sided, one > 9 mm)",
  "15. Personal History Polyps Serrated Polyps <br> (20 or more, any size, anywhere)",
  "16. Personal History Polyps Serrated Polyps with dysplasia"];

  $(screenID).append(buttonListMakerTwoCol(listID,
  required, surveillance1IDArray, surveillance2IDArray, surveillance1TextArray,
  surveillance2TextArray, "primary", "390px", type));
}

// Load the button list for Diagnostic Indications Modal
var loadDiagnosticIndicationsButtonList = function(listID, screenID, required, type){
  var diagnostic1IDArray = ["Diagnostic-1","Diagnostic-2","Diagnostic-3",
  "Diagnostic-4","Diagnostic-5","Diagnostic-6","Diagnostic-7","Diagnostic-8",
  "Diagnostic-9","Diagnostic-10"];
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

  var diagnostic2IDArray = ["Diagnostic-11","Diagnostic-12","Diagnostic-13"
  ,"Diagnostic-14","Diagnostic-15","Diagnostic-16","Diagnostic-17","Diagnostic-18"
  ,"Diagnostic-19"];
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

  var diagnostic3IDArray = ["Diagnostic-20","Diagnostic-21",
  "Diagnostic-22","Diagnostic-23","Diagnostic-24"];
  var diagnostic3TextArray = [
  "20. Pain Abdomen (Generalized)",
  "21. Pain Anorectal",
  "22. Pain Back",
  "23. Pain Chest",
  "24. Weight Loss (Unexplained)"];

  $(screenID).append(buttonListMakerThreeCol(listID,
  required, diagnostic1IDArray, diagnostic2IDArray, diagnostic3IDArray,
   diagnostic1TextArray, diagnostic2TextArray, diagnostic3TextArray,
   "primary", "390px", type));
}

// Load the button list for Therapeutic Indications Modal
var loadTherapeuticIndicationsButtonList = function(listID, screenID, required, type){
  var therapeutic1IDArray = ["Therapeutic-1","Therapeutic-2","Therapeutic-3","Therapeutic-4",
  "Therapeutic-5","Therapeutic-6","Therapeutic-7","Therapeutic-8","Therapeutic-9",
  "Therapeutic-10"];
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

  var therapeutic2IDArray = ["Therapeutic-11","Therapeutic-12",
  "Therapeutic-13"];
  var therapeutic2TextArray = [
  "11. Stricture Dilation",
  "12. Stricture Stent (Placement)",
  "13. Stricture Stent (Replacement or Repeat)"];

  $(screenID).append(buttonListMakerTwoCol(listID,
  required, therapeutic1IDArray, therapeutic2IDArray, therapeutic1TextArray,
  therapeutic2TextArray, "primary", "390px", type));
}

// Load the button list for Preoperative Indications Modal
var loadPreoperativeIndicationsButtonList = function(listID, screenID, required, type){

  var preoperative1IDArray = ["Preoperative-1","Preoperative-2",
  "Preoperative-3"];
  var preoperative1TextArray = [
  "1. Colostomy takedown",
  "2. Indentify or Clear Synchronoous Lesion",
  "3. Mass/Polyp Mark/Tatoo"];

  var preoperative2IDArray = [];
  var preoperative2TextArray = [];

  $(screenID).append(buttonListMakerTwoCol(listID,
  required, preoperative1IDArray, preoperative2IDArray,
   preoperative1TextArray, preoperative2TextArray, "primary", "390px", type));

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
  loadScopeButtonList();
  loadSedationButtonList();
  loadExtentButtonList();
  loadAddPolypSelectOptions();
}
