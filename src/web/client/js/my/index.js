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
    console.log(salts);
    console.log("session_id=" + session_id
    + " session_key=" + session_key + " user_id=" + user_id);
  }


  if(session_id == null || session_key == null || user_id == null ||
    salts == null || salts.length == 0 || salts[0] == null){
      sessionInitiate();
    } else {
      sessionCheck(salts, session_id, session_key, user_id);
    }

});
