// Get references to the form and input elements
let form = document.querySelector("form");
let input = document.getElementById("exampleInputEmail1");

// Add an event listener to handle form submission
form.addEventListener("submit", function(event) {
    // Prevent the default form submission behavior
    event.preventDefault();
    console.log("event.preventDefault() called");

    // Get the command from the input element
    let command = input.value;

    // Send the command to the back end using a POST request
    fetch("/your-backend-endpoint", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ command: command })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`An error occurred: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        // Handle the response from the back end
        console.log("Response from back end:", data);
    })
    .catch(error => {
        // Handle any errors that occur
        console.error("Error sending command to back end:", error);
    });
});