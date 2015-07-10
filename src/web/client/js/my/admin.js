$(document).ready(function() {
  // TODO fix this function
  $(".clickable-row").click(function() {
    console.log("table click");
     window.document.location = $(this).data("href");
  });
  

  $('#operationDate').datetimepicker({
    format: 'MM/DD/YYYY'}
  );
  $('#operationTime').datetimepicker({
    format: 'LT'}
  );

});
