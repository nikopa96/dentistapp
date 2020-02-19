$(document).ready(function(){
  $('#chooseDate').datepicker({
    language: "et",
    orientation: "bottom right",
    daysOfWeekDisabled: "0,6",
    todayHighlight: true,
    autoclose: true
  });
});