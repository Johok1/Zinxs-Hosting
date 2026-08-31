let memElement = document.getElementById("memory");



function fetchData() {
  fetch('http://158.69.52.39:8091/zinxshosting/backend/getRam')
    .then(response => response.text())
    .then(data => {
    let cpu = data;

      memElement.innerHTML = "Ram usage Mb: "+ cpu;
  });
}

function stringData() {
let mem = "10gb / 23gb"
       memElement.innerHTML = "Memory:" + " " + mem;
}

setInterval(fetchData, 500);
