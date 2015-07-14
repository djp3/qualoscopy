$(document).ready(function() {
    $('#procedure-table').dataTable();

    $(".clickable-row").click(function() {
      window.document.location = $(this).data("href");
    });

    $('#operationDate').datetimepicker({
      format: 'MM/DD/YYYY'}
    );
    $('#operationTime').datetimepicker({
      format: 'LT'}
    );
} );
