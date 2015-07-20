// These are global variables/functions used throughout site

// https://104.237.152.218:9021/
var ipAddress = "104.237.152.218";
var port = 9021;
var versionNumber = 0.1;
var debug = true;

// On start up read these Cookies
var session_id = Cookies.getCookie("session_id");
var session_key = Cookies.getCookie("session_key");
var user_id = Cookies.getCookie("user_id");

$(document).ready(function() {

  // For the signout button on the toolbar
  $("#signout").click(function() {
    var salts = JSON.parse(Cookies.getCookie("salts"));
    sessionKill(salts, session_id, session_key, user_id);
  });
});
