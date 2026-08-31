let logElement = document.getElementById("log");

// Function to fetch data from the backend
function fetchData(endpoint) {
    fetch(endpoint, { method: "GET", mode: 'no-cors'})
        .then(response => response.text())
        .then(data => {

             logElement.innerHTML += "<span style='color: white; font-family: \"Times New Roman\";'>" + data + "<br>";
             console.log(data)
          /*
            // Split the data into individual log messages
            let logs = data.split("][");

            // Iterate over the log messages
            for (let i = 0; i < logs.length; i += 4) {
                // Extract the date, time, type, and message from the log message
                let date = logs[i].startsWith('[') ? logs[i].slice(1) : logs[i];
                let time = logs[i + 1];
                let type = logs[i + 2];
                let message = logs[i + 3].endsWith(']') ? logs[i + 3].slice(0,-1) : logs[i + 3];

                // Display the log message's contents
                logElement.innerHTML += "<span style='color: gray; font-family: \"Times New Roman\";'>" + date + " ";
                logElement.innerHTML += "<span style='color: gray; font-family: \"Times New Roman\";'>" + time + " ";

                // Change the color of the type based on its value
                if (type === "INFO") {
                    logElement.innerHTML += "<span style='color: blue; font-family: \"Times New Roman\";'>" + "[" + type + "]" + "</span> ";
                } else if (type === "ERROR") {
                    logElement.innerHTML += "<span style='color: red; font-family: \"Times New Roman\";'>" + "[" + type + "]" + "</span> ";
                } else if (type === "WARN") {
                    logElement.innerHTML += "<span style='color: orange; font-family: \"Times New Roman\";'>" + "[" + type + "]" + "</span> ";
                } else {
                    logElement.innerHTML += type + " ";
                }

                logElement.innerHTML += "<span style='color: white; font-family: \"Times New Roman\";'>" + message + "<br>";
            }
            */
            // Remove a line after 100 lines
            let lines = logElement.innerHTML.split("<br>");
            if (lines.length > 100) {
                lines.shift();
                logElement.innerHTML = lines.join("<br>");
            }
        });
}

function stringData(data) {

            // Split the data into individual log messages
            let logs = data.split("][");

            // Iterate over the log messages
            for (let i = 0; i < logs.length; i += 4) {
                // Extract the date, time, type, and message from the log message
                let date = logs[i].startsWith('[') ? logs[i].slice(1) : logs[i];
                let time = logs[i + 1];
                let type = logs[i + 2];
                let message = logs[i + 3].endsWith(']') ? logs[i + 3].slice(0,-1) : logs[i + 3];

                // Display the log message's contents
                logElement.innerHTML += "<span style='color: gray; font-family: \"Times New Roman\";'>" + date + " ";
                logElement.innerHTML += "<span style='color: gray; font-family: \"Times New Roman\";'>" + time + " ";

                // Change the color of the type based on its value
                if (type === "INFO") {
                    logElement.innerHTML += "<span style='color: blue; font-family: \"Times New Roman\";'>" + "[" + type + "]" + "</span> ";
                } else if (type === "ERROR") {
                    logElement.innerHTML += "<span style='color: red; font-family: \"Times New Roman\";'>" + "[" + type + "]" + "</span> ";
                } else if (type === "WARN") {
                    logElement.innerHTML += "<span style='color: orange; font-family: \"Times New Roman\";'>" + "[" + type + "]" + "</span> ";
                } else {
                    logElement.innerHTML += type + " ";
                }

                logElement.innerHTML += "<span style='color: white; font-family: \"Times New Roman\";'>" + message + "<br>";
            }

            // Remove a line after 100 lines
            let lines = logElement.innerHTML.split("<br>");
            if (lines.length > 100) {
                lines.shift();
                logElement.innerHTML = lines.join("<br>");
            }
}

function doSomething(){
  fetch('http://localhost:8091/zinxshosting/backend/getConsole',
    {
      method: 'GET',
      headers: {
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Origin':'*'}
    })
  .then(response => response.text())
  .then(data => {
    if(data == ""){
      //do nothing
    }else {
    logElement.innerHTML += "<span style='color: yellow; font-family: \"Times New Roman\";'>" + data + "<br>";
    }
       let lines = logElement.innerHTML.split("<br>");
                   if (lines.length > 100) {
                       lines.shift();
                       logElement.innerHTML = lines.join("<br>");
                   }

  })
  .catch(error => console.error(error));


}

function scrollDown(){
  logElement.scrollIntoView(alignToTop=false);
}

// Fetch data from the backend every 50 milliseconds
setInterval(doSomething, 150);
//setInterval(scrollDown, 100);
//setInterval(stringData, 50);
