// (c) Luke Raus 2015
$(document).ready(function() {
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

  var salts = JSON.parse(Cookies.getCookie("salts"));

  if (debug){
    console.log("salts=" + salts + " session_id=" + session_id
    + " session_key=" + session_key + " user_id=" + user_id);
  }


  if(user_id == null || user_id == "" || session_id == null ||
    session_key == null || salts == null || salts.length == 0 ||
    salts[0] == null){
      sessionInitiate();
    } else {
      sessionCheck(salts, session_id, session_key, user_id);
    }

    //u: demo p: 2015_08_20_01

});
