let chatEl = document.getElementById("firewall_status");



function fetchData() {
  fetch('http://158.69.52.39:8091/zinxshosting/backend/getFirewallStatus')
    .then(response => response.text())
    .then(data => {
      if(data.includes("{")){
        chatEl.innerHTML = "<center>"+ "<center>"+"Status: Pending";
        chatEl.color = "red";
      }
      else{
    let firewall_status = data;

    chatEl.innerHTML = "<center>"+ "<center>"+ firewall_status;
    chatEl.color = "yellow";
}
  });
}

function stringData() {
let cpu = "30%"
      chatElement.innerHTML = "Cpu:" + " " + cpu;
}

setInterval(fetchData, 1000);
