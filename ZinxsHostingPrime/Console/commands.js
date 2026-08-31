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
    fetch("http://localhost:8091/zinxshosting/backend/getConsoleCommand/"+command,{
      method: 'GET',
      headers: {
                'Access-Control-Allow-Origin':'*',
                'Access-Control-Allow-Origin':'*'}
    });
        input.value = "";
});

let button = document.getElementById('start');

button.addEventListener('click',function(){
//  console.log(fileName);
fetch("http://localhost:8091/zinxshosting/backend/getConsoleArgs",{
  method: 'GET',
  headers: {
            'Access-Control-Allow-Origin':'*',
            'Access-Control-Allow-Origin':'*'}
});
});
