let logElement = document.getElementById("log");

// Function to fetch data from the backend
function fetchData(endpoint) {
    fetch(endpoint)
        .then(response => response.text())
        .then(data => {
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
        });
}

function stringData() {
let data = "[4/14/23][12:17][ERROR][OMG YOUR SERVER IS DYING DUMBY]"
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

// Fetch data from the backend every 50 milliseconds
setInterval(fetchData("endpoint"), 500);
//setInterval(fetchData, 50);
