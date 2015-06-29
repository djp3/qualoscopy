$(document).ready(function() {
  $('#login').click(function() {
    var email = $('#email').val();
    var pword = $('#pword').val();
    console.log("Email=" + email + " Password=" + pword);
  });
});
