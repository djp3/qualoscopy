// @Author Luke Raus
// This js if for managing cookies on the site

// @namespace Initialize
var Cookies = {};

Cookies.setCookie = function (name, value, days) {
  if (days) {
    var date = new Date();
    date.setTime(date.getTime() + (days*24*60*60*1000));
    var expires = "; expires=" + date.toGMTString();
  }
  else var expires = "";
  document.cookie = name + "=" + value + expires + "; path=/";
};

Cookies.addToCookieArray = function (name, data, days) {
  if(getCookie(name) != null){
    var cookieArray = JSON.parse(getCookie(name));
    cookieArray.push(data);
    setCookie(name, JSON.stringify(cookieArray), days);
  }
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

Cookies.clearCookies = function (name) {
  setCookie(name,"",-1);
};
