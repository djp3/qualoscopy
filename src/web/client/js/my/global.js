// These are global variables/functions used throughout site

// https://104.237.152.218:9021/
var ipAddress = "104.237.152.218";
var port = 9021;
var versionNumber = 0.1;
var debug = true;

$(document).ready(function() {
  var signOut = function(salts, session_id, session_key, user_id){
    var usedSalt = salts[0];
    salts.splice(0,1);
    Cookies.setCookie("salts", JSON.stringify(salts), 1);
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
        Cookies.setCookie("session_id", "", 1);
        Cookies.setCookie("salts", JSON.stringify([]), 1);
        Cookies.setCookie("session_key", "", 1);
        Cookies.setCookie("user_id", "", 1);
        window.document.location = "index.html";
      }
    });
  }

  $("#signout").click(function() {
    var salts = JSON.parse(Cookies.getCookie("salts"));
    var session_id = Cookies.getCookie("session_id");
    var session_key = Cookies.getCookie("session_key");
    var user_id = Cookies.getCookie("user_id");
    signOut(salts, session_id, session_key, user_id);
  });
});
