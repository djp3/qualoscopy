// (c) Luke Raus 2015
// My functions for server api calls

// add a patient to the databese
var addPatient;
// add a procedure
var addProcedure
// Get all the patients
var getPatients;
// Get procedure
var getPatientProcedures;
// get procedure Polyps
var getProceduresPolyps;
// Login to the system
var login;
// Check cookies to see if session is still valid
var sessionCheck;
// create a session
var sessionInitiate;
// Sign out of the system / kill sesssion
var sessionKill;
// update patient info
var updatePatient;
// update proceudre info
var updateProcedure;

addPatient = function(salts, session_id, session_key, user_id){
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/add/patient",
    data: {"version": versionNumber, "user_id": user_id,
    "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt) },
    context: document.body
  }).done(function(data) {
    if (debug) console.log(data);
    if(data.error == "false"){
      Cookies.addToCookieArray("salts", data.salt, 1);
      Cookies.setCookie("patient_id", data.patient_id, 1);
    }
  });
}

addProcedure = function(salts, session_id, session_key, user_id, mr_id, patient_id){
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/add/procedure",
    data: {"version": versionNumber, "user_id": user_id,
    "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt),
    "mr_id": mr_id, "patient_id": patient_id},
    context: document.body
  }).done(function(data) {
    if (debug) console.log(data);
    if(data.error == "false"){
      Cookies.addToCookieArray("salts", data.salt, 1);
      Cookies.setCookie("procedure_id", data.procedure_id, 1);
    }
  });
}

// Return ajax object so .done can be used elsewhere
getPatients = function(salts, session_id, session_key, user_id) {
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  return $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/get/patients",
    data: {"version": versionNumber, "user_id": user_id,
    "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt)},
    context: document.body
  });
}

// Return ajax object so .done can be used elsewhere
getPatientProcedure = function(salts, session_id, session_key,
  user_id, mr_id, patient_id) {
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  return $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/get/patient/procedures",
    data: {"version": versionNumber, "user_id": user_id, "patient_id": patient_id,
    "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt), "mr_id": mr_id},
    context: document.body
  });
}

getProceduresPolyps = function(salts, user_id, session_id,
  session_key, procedure_id) {
    var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
    return $.ajax({
      dataType: 'jsonp',
      url: "https://" + ipAddress + ":" + port
      + "/get/procedure/polyps",
      data: {"version": versionNumber, "user_id": user_id,
      "shsid": Sha256.hash(session_id + "" + usedSalt),
      "shsk": Sha256.hash(session_key + "" + usedSalt),
      "procedure_id": procedure_id},
      context: document.body
    });
}

login = function(){
  var user_id = $('#userID').val();
  var pword = $('#pword').val();
  var salts = JSON.parse(Cookies.getCookie("salts"));
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  var hashP = Sha256.hash(Sha256.hash(pword) + usedSalt);
  Cookies.setCookie("user_id", user_id, 1);

  if (debug) console.log("Email=" + user_id + " Password=" + pword);
  $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/login",
    data: {"version": versionNumber, "user_id": user_id,
    "session_id": Cookies.getCookie("session_id"), shp: hashP },
    context: document.body
  }).done(function(data) {
    if (debug) console.log(data);
    if(data.error == "false"){
      Cookies.setCookie("session_key", data.session_key, 1);
      Cookies.addToCookieArray("salts", data.salt, 1);
      window.location.href = "admin.html";
    } else {
      sessionInitiate();
    }
  });
}

sessionCheck = function(salts, session_id, session_key, user_id){
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/session/check",
    data: {"version": versionNumber, "user_id": user_id,
    "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt)},
    context: document.body
  }).done(function(data) {
    if (debug) console.log(data);
    if(data.error == "false" && data.valid == "true"){
      Cookies.addToCookieArray("salts", data.salt, 1);
      window.location.href = "admin.html";
    } else {
      sessionInitiate();
    }
  });
}

sessionInitiate = function () {
  $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/session/initiate",
    data: {"version": versionNumber},
    context: document.body
  }).done(function(data) {
    if (debug) console.log(data);
    if(data.error == "false"){
      Cookies.setCookie("session_id", data.session_id, 1);
      Cookies.setCookie("salts", JSON.stringify([data.salt]), 1);
      Cookies.setCookie("session_key", "", 1);
      Cookies.setCookie("user_id", "", 1);
    } else {
      // TODO: Do Something
    }
  });
}

sessionKill = function(salts, session_id, session_key, user_id){
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/session/kill",
    data: {"user_id": user_id, "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt), "version": versionNumber},
    context: document.body
  }).done(function(data) {
    if (debug) console.log(data);
    if(data.error == "false"){
      Cookies.clearAllCookies();
      if (debug) console.log(document.cookie.split(";"));
      window.document.location = "index.html";
    }
  });
}

// Return ajax call
updatePatient = function(salts, session_id, session_key, user_id, mr_id,
  lastName, firstName, dob, gender, patient_id){
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  return $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/update/patient",
    data: {"version": versionNumber, "user_id": user_id,
    "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt),
    "patient_id": patient_id, "mr_id": mr_id,
    "last": lastName, "first": firstName, "dob": dob, "gender": gender},
    context: document.body
  });
}


// Return ajax call
updateProcedure = function(salts, user_id, session_id, session_key, patient_id,
  procedure_id, opts){
    var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
    var data_dict = {"version": versionNumber, "user_id": user_id,
    "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt),
    "patient_id": patient_id, "procedure_id": procedure_id}
    for(var key in opts){
      data_dict[key] = opts[key];
    }
    return $.ajax({
      dataType: 'jsonp',
      url: "https://" + ipAddress + ":" + port
      + "/update/procedure",
      data: data_dict,
      context: document.body
  });
}
