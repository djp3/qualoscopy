$(document).ready(function() {

  // http://104.237.152.218:9021/version
  var ipAddress = "104.237.152.218";
  var port = 9021;
  var versionNumber = 0.1;

  // For the Login
  $('#login').click(function() {
    var email = $('#email').val();
    var pword = $('#pword').val();
    console.log("Email=" + email + " Password=" + pword);
    window.location.href = "main.html";
  });

  $('#jsonp').click(function() {
    $.ajax({
      dataType: 'jsonp',
      url: "http://" + ipAddress + ":" + port + "/version",
      data: { version: versionNumber},
      context: document.body
    }).done(function(data) {
      console.log(data);
      document.getElementById("jtext").innerHTML='Hello jsonp! error='
      + data.error + " version= " + data.version;
    });
  });
});
