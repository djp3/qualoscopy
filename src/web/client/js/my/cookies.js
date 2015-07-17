// @Author Luke Raus
// This js if for managing cookies on the site

// @namespace Initialize
var Cookies = {};

Cookies.setCookie = function (name, value, hours) {
  if (hours) {
    var date = new Date();
    date.setTime(date.getTime() + (1*hours*60*60*1000));
    var expires = "; expires=" + date.toUTCString();
  }
  else var expires = "";
  document.cookie = name + "=" + value + expires + "; path=/";
};

Cookies.getCookie = function (name) {
  var nameEQ = name + "=";
  var ca = document.cookie.split(';');
  for(var i=0;i < ca.length;i++) {
    var c = ca[i];
    while (c.charAt(0)==' ') c = c.substring(1,c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
  }
  return null;
};

Cookies.addToCookieArray = function (name, data, hours) {
  if(this.getCookie(name) != null){
    var cookieArray = JSON.parse(this.getCookie(name));
    cookieArray.push(data);
    this.setCookie(name, JSON.stringify(cookieArray), hours);
  }
};

Cookies.popFromCookieArray = function (name, data, hours) {
  if(this.getCookie(name) != null){
    var popedCookie = data[0];
    data.splice(0,1);
    this.setCookie(name, JSON.stringify(data), hours);
    return popedCookie;
  }
};

Cookies.clearCookies = function (name) {
  this.setCookie(name,"",-1);
};

Cookies.clearAllCookies = function() {
  var cookies = document.cookie.split(";");

  for (var i = 0; i < cookies.length; i++) {
    this.clearCookies(cookies[i]);
  }
};
