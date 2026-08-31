let chatElement = document.getElementById("chat");

// Function to fetch data from the backend
function fetchData() {
    fetch('<URL>')
        .then(response => response.text())
        .then(data => {
            // Split the data into individual chat messages
            let chats = data.split("][");

            // Iterate over the chat messages
            for (let i = 0; i < chats.length; i += 4) { 
                // Extract the date, time, player, and playermessage from the chat message
                let date = chats[i].startsWith('[') ? chats[i].slice(1) : chats[i];
                let time = chats[i + 1];
                let player = chats[i + 2];
                let playermessage = chats[i + 3].endsWith(']') ? chats[i + 3].slice(0,-1) : chats[i + 3];

                // Display the chat message's contents
                chatElement.innerHTML += "<span style='color: gray; font-family: \"Times New Roman\";'>" + date + " ";
                chatElement.innerHTML += "<span style='color: gray; font-family: \"Times New Roman\";'>" + time + " ";
                chatElement.innerHTML += "<span style='color: white; font-family: \"Times New Roman\";'>" + player + ": " + playermessage + "<br>";
            }

            // Remove a line after 100 lines
            let lines = chatElement.innerHTML.split("<br>");
            if (lines.length > 100) {
                lines.shift();
                chatElement.innerHTML = lines.join("<br>");
            }
        });
}

// Fetch data from the backend every 50 milliseconds
setInterval(fetchData, 50);