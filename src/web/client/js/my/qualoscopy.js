$(document).ready(function() {
  var initiateSession = function () {
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
      }
    });
  }

  var checkSession = function(salts, session_id, session_key, user_id){
    var usedSalt = salts[0];
    salts.splice(0,1);
    Cookies.setCookie("salts", JSON.stringify(salts), 1);
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
        var setSalts = JSON.parse(Cookies.getCookie("salts"));
        setSalts.push(data.salt);
        if (debug) console.log(setSalts);
        Cookies.setCookie("salts", JSON.stringify(setSalts), 1);
        window.location.href = "admin.html";
      } else {
        initiateSession();
      }
    });
  }

  var login = function(){
    var user_id = $('#userID').val();
    var pword = $('#pword').val();
    var salts = JSON.parse(Cookies.getCookie("salts"));
    var hashP = Sha256.hash(Sha256.hash(pword) + salts[0]);
    salts.splice(0,1);
    Cookies.setCookie("salts", JSON.stringify(salts), 1);
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
        var setSalts = JSON.parse(Cookies.getCookie("salts"));
        setSalts.push(data.salt);
        if (debug) console.log(setSalts);
        Cookies.setCookie("salts", JSON.stringify(setSalts), 1);
        window.location.href = "admin.html";
      } else {
        initiateSession();
      }
    });
  }

  // For the Login
  $('#login-form').keypress(function(event){
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13'){
      login();
    }
  });
  $('#login').click(function() {
    login();
  });

  // On start up read these fields
  var salts = JSON.parse(Cookies.getCookie("salts"));
  var session_id = Cookies.getCookie("session_id");
  var session_key = Cookies.getCookie("session_key");
  var user_id = Cookies.getCookie("user_id");

  if (debug){
    console.log(salts);
    console.log("session_id=" + session_id
    + " session_key=" + session_key + " user_id=" + user_id);
  }

  if(session_id == null || session_key == null ||
    salts.length == 0 || salts[0] == null){
      initiateSession();
    } else {
      checkSession(salts, session_id, session_key, user_id);
    }

  });
