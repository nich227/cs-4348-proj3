function renderGraph(arr, job_ids, alg, hide_graph) {
  if (hide_graph) {
    // Add tab to webpage (inactive)
    $("#tabs > ul").append("<li id='tab" + alg + "'><a>" + alg + "</a></li>");
  } else {
    // Add tab to webpage (active)
    $("#tabs > ul").append(
      "<li id='tab" + alg + "' class='is-active'><a>" + alg + "</a></li>"
    );
  }

  let colour = "";

  // Setting colour
  switch (alg) {
    case "FCFS":
      colour = "primary";
      break;
    case "RR":
      colour = "danger";
      break;
    case "SPN":
      colour = "warning";
      break;
    case "SRT":
      colour = "info";
      break;
    case "HRRN":
      colour = "link";
      break;
    case "FB":
      colour = "success";
      break;
    default:
      colour = "black";
      break;
  }
  
  //Adding a table to the interface
  $("body").append(
    $("#tableTmpl")
      .attr("id", "table" + alg)
      .show()
  );

  //Initializing columns
  $("#table" + alg + " > thead > tr").append("<th class='job_col'>Job</th>");
  for (let i = 0; i <= arr.length; i++) {
    $("#table" + alg + " > thead > tr").append(
      "<th class='time-intervals'>" + i + "</th>"
    );
  }

  //Initializing rows
  let total_duration = 0;
  $("#table" + alg + " > tbody > tr").remove();
  for (let i = 0; i < job_ids.length; i++) {
    let j = 0;
    let duration = 0;
    
    $("#table" + alg + " > tbody").append("<tr id='" + alg + job_ids[i] + "'></tr>");

    $("#table" + alg + " > tbody > #" + alg + job_ids[i]).append(
      "<th class='job_col'>" + job_ids[i] + "</th>"
    );

    // Iterating through all the time slots
    while (j < arr.length) {
      
      // Color (job is running)
      if (j < arr.length && arr[j][i] === true) {
        while (j < arr.length && arr[j][i] === true) {
          duration++;
          j++;
        }
        $("#table" + alg + " > tbody > #" + alg + job_ids[i]).append(
          "<td colspan='" +
            duration +
            "'>" +
            "<button class='button is-" +
            colour +
            " has-tooltip-" +
            colour +
            "' data-tooltip='Duration: " +
            duration +
            "'></button>" +
            "</td>"
        );
        duration = 0;
      }

      // Color(job is not running)
      if (j < arr.length && arr[j][i] === false) {
        while (j < arr.length && arr[j][i] === false) {
          duration++;
          j++;
        }
        $("#table" + alg + " > tbody > #" + alg + job_ids[i]).append(
          "<td colspan='" +
            duration +
            "'>" +
            "<button class='button is-white has-tooltip-light' data-tooltip='Duration: " +
            duration +
            "'></button>" +
            "</td>"
        );
        duration = 0;
      }
    }
  }

  if (hide_graph) {
    $("#table" + alg).hide();
  }
}
