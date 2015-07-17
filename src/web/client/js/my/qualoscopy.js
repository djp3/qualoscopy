// My functions for server api calls

// Check cookies to see if session is still valid
var checkSession;
// Get all the patients
var getPatients;
// Get procedure
var getProcedure;
// create a session
var initiateSession;
// Login to the system
var login;
// Sign out of the system
var signOut;

checkSession = function(salts, session_id, session_key, user_id){
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/session/check",
    data: {"user_id": user_id, "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt), "version": versionNumber},
    context: document.body
  }).done(function(data) {
    if (debug) console.log(data);
    if(data.error == "false" && data.valid == "true"){
      Cookies.addToCookieArray("salts", data.salt, 1);
      window.location.href = "admin.html";
    } else {
      initiateSession();
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
    data: {"user_id": user_id, "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt), "version": versionNumber},
    context: document.body
  });
}

// Return ajax object so .done can be used elsewhere
getProcedure = function(salts, session_id, session_key, user_id, mr_id) {
  var usedSalt = Cookies.popFromCookieArray("salts", salts, 1);
  return $.ajax({
    dataType: 'jsonp',
    url: "https://" + ipAddress + ":" + port
    + "/get/patient/procedures",
    data: {"user_id": user_id, "shsid": Sha256.hash(session_id + "" + usedSalt),
    "shsk": Sha256.hash(session_key + "" + usedSalt), "mr_id": mr_id, "version": versionNumber},
    context: document.body
  });
}

initiateSession = function () {
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
      Cookies.setCookie("salts", JSON.stringify([data.session_salt]), 1);
      Cookies.setCookie("session_key", "", 1);
      Cookies.setCookie("user_id", "", 1);
    } else {
      // TODO: Do Something
    }
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
    data: {"user_id": user_id, "session_id": Cookies.getCookie("session_id"),
    shp: hashP,  "version": versionNumber},
    context: document.body
  }).done(function(data) {
    if (debug) console.log(data);
    if(data.error == "false"){
      Cookies.setCookie("session_key", data.session_key, 1);
      Cookies.addToCookieArray("salts", data.salt, 1);
      window.location.href = "admin.html";
    } else {
      initiateSession();
    }
  });
}

signOut = function(salts, session_id, session_key, user_id){
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
