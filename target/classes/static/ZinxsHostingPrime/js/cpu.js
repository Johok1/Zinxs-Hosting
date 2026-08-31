let chatElement = document.getElementById("cpu");



function fetchData() {
  fetch('http://localhost:8091/zinxshosting/backend/getCPU')
    .then(response => response.text())
    .then(data => {
    let cpu = data;

    chatElement.innerHTML = ""+ cpu;

  });
}

function stringData() {
let cpu = "30%"
      chatElement.innerHTML = "Cpu:" + " " + cpu;
}

setInterval(fetchData, 500);
