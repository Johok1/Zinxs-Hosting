let dataAr = [];
function fetchData() {
    let endpoint = "http://158.69.52.39:8091/zinxshosting/backend/getBackups";
    let logElement = document.getElementById("log");

    fetch(endpoint, {
      headers: {
              'Access-Control-Allow-Origin':'*',
              'Access-Control-Allow-Origin':'*'}
  })
        .then(response => response.text())
        .then(data => {

             if(data == ""){
               console.log("dropped: " + data)
             }else if(dataAr.includes(data)){
                console.log("includes " + data);
             }else{
                dataAr.push(data);
             logElement.innerHTML += "<span style='color: yellow; font-family: \"Times New Roman\";'>" + data + "<br>";
             console.log(data);
           }
           });
   }

setInterval(fetchData,1000);
