/** 
 * tab_listener.js
 * 
 * Author: Ning Kevin Chen
 * NetID: nkc160130
 * Coded in: JavaScript
 * Class: SE 4348
 * Professor: Greg Ozbirn
 */
function onTabClick(alg) {
  let alg_list = ["FCFS", "RR", "SPN", "SRT", "HRRN", "FB"];

  alg_list.splice(
    alg_list.findIndex(function(alg_find) {
      return alg_find === alg;
    }),
    1
  );

  // Hide all other tables
  for (let i = 0; i < alg_list.length; i++) {
    $("#tab" + alg_list[i]).removeClass("is-active");
    $("#table" + alg_list[i]).hide();
  }

  // Show this table
  $("#tab" + alg).addClass("is-active");
  $("#table" + alg).fadeIn();

  return $("#table" + alg);
}
