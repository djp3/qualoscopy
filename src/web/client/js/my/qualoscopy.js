$(document).ready(function() {

  // http://104.237.152.218:9021/version
  var ipAddress = "104.237.152.218";
  var port = 9021;
  var versionNumber = 0.1;

  // For managing Cookies
  function setCookie(name, value, days) {
    if (days) {
      var date = new Date();
      date.setTime(date.getTime() + (days*24*60*60*1000));
      var expires = "; expires=" + date.toGMTString();
    }
    else var expires = "";
    document.cookie = name + "=" + value + expires + "; path=/";
  }

  function addToCookieArray(name, data, days) {
    if(getCookie(name) != null){
      var cookieArray = JSON.parse(getCookie(name));
      cookieArray.push(data);
      setCookie(name, JSON.stringify(cookieArray), days);
    }
  }

  function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
      var c = ca[i];
      while (c.charAt(0)==' ') c = c.substring(1,c.length);
      if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
  }

  function clearCookie(name) {
    setCookie(name,"",-1);
  }



  // For the Login
  $('#login').click(function() {
    var email = $('#email').val();
    var pword = $('#pword').val();
    console.log("Email=" + email + " Password=" + pword);
    window.location.href = "main.html";
  });

  $('#add').click(function() {
    if(getCookie("session_id") != null){
      $.ajax({
        dataType: 'jsonp',
        url: "https://" + ipAddress + ":" + port
            + "/initiate_session",
        data: {"version": versionNumber},
        context: document.body
      }).done(function(data) {
        console.log(data);
        if(data.error == "false"){
          addToCookieArray("salts", data.session_salt, 1);
        }
      });
    }
  });

  $('#read').click(function() {
    console.log(JSON.parse(getCookie("salts")));
    console.log(getCookie("session_id"));
  });


  $('#create').click(function() {
    console.log(JSON.parse(getCookie("salts")));
    console.log(getCookie("session_id"));
    clearCookie("session_id");
    clearCookie("salts");
    if(getCookie("session_id") == null){
      $.ajax({
        dataType: 'jsonp',
        url: "https://" + ipAddress + ":" + port
            + "/initiate_session",
        data: {"version": versionNumber},
        context: document.body
      }).done(function(data) {
        console.log(data);
        if(data.error == "false"){
          setCookie("session_id", data.session_id, 1);
          setCookie("salts", JSON.stringify([data.session_salt]), 1);
        }
      });
    }
  });
});
