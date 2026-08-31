let fileContent = "";

let path = "";

let endpoint = "";

function fetchDataInit() {
    endpoint += '&opt&ftp';
    // send GET request to backend to get list of files
    fetch('http://158.69.52.39:8091/zinxshosting/backend/getConsoleDirectory/'+endpoint)
        .then(response => response.text())
        .then(data => {
            data = data.replaceAll('[','');
            data = data.replaceAll(']','');
            initFileList(data.split(","));
        });
}

function fetchData() {
    console.log(endpoint);
    // send GET request to backend to get list of files
    fetch('http://158.69.52.39:8091/zinxshosting/backend/getConsoleDirectory/'+endpoint)
        .then(response => response.text())
        .then(data => {
            data = data.replaceAll('[','');
            data = data.replaceAll(']','');
            initFileList(data.split(","));
        });
}

function postFileData(){
  filepath = endpoint.replaceAll('&opt','');
  fetch('http://158.69.52.39:8091/zinxshosting/backend/postFtpFile/'+filepath,{
    method: 'POST',
    headers:{
      'Access-Control-Allow-Origin':'*',
      'Access-Control-Allow-Origin':'*',
      'Content-Type': 'plain/text'
    },
    body: fileContent,
  })
  .then((response) =>{
    endpoint = endpoint.replaceAll(endpoint.slice(endpoint.lastIndexOf('&')),'');
    console.log(endpoint);
    fetchData()
});
}

function getContent(filepath){
  filepath = filepath.replaceAll('&opt','');
  fetch('http://158.69.52.39:8091/zinxshosting/backend/getFtpFile/'+filepath)
  .then(response => response.text())
  .then(data => {
    data = data.replaceAll('[','');
    data = data.replaceAll(']','');
    data = data.replaceAll(',','');
    displayFileContent(data.split("\n"))
  });
}


function displayFileContent(files) {
    let filesElement = document.getElementById("files");
    // clear existing file elements
    filesElement.innerHTML = '';
    let masterLabel = document.createElement('label');
    //masterLabel.setText("<html>")
    masterLabel.style.color = "yellow";
    masterLabel.contentEditable = "true";
    for (let i = 0; i < files.length; i++) {
        console.log("1: " + i + ': ' + files[i]);
        let fileName = files[i].trim();
        let indent = files[i].length - fileName.length;
        fileContent += files[i] + "\n";

    //    let label = document.createElement('label')
      //  label.contentEditable="true"
        masterLabel.innerHTML += files[i]+"<br>";
      //  label.style.color = "yellow";
      //  masterLabel.appendChild(label);
      //  masterLabel.appendChild(document.createElement('br'));
      }
      filesElement.appendChild(masterLabel);
      //filesElement.appendChild(iframe);
      console.log(fileContent);
      createDownloadButton(filesElement,masterLabel);
      createBackButton(filesElement);
    //  addBottomBar(filesElement);
  }

  function createBackButton(filesElement){
    if(!(endpoint.normalize() === "&opt&ftp".normalize())){
    // create download button
    let backButton = document.createElement('button');
    backButton.id = 'backButton';
    backButton.innerHTML = 'Back';
    backButton.addEventListener('click', function() {
      let endpoints = endpoint.split('&');
      endpoint = '';
      for(let i = 0; i< endpoints.length;i++){
        if(i != endpoints.length-1){
          if(endpoints[i] != ""){
            endpoint += "&" + endpoints[i];
          }
        }
      }
      fetchData();
    });
    filesElement.appendChild(backButton);
}
  }




  function createDownloadButton(filesElement, masterLabel){
    // create download button
    let downloadButton = document.createElement('button');
    downloadButton.id = 'downloadButton';
    downloadButton.innerHTML = 'Save Changes';
    downloadButton.addEventListener('click', function() {
        fileContent = masterLabel.innerHTML;
        postFileData();
    });
    filesElement.appendChild(downloadButton);

  }

function stringData(str) {
    let files = str.split("\n");
    updateFileList(files);
}

function initFileList(files) {
    let filesElement = document.getElementById("files");
    // clear existing file elements
    filesElement.innerHTML = '';

    for (let i = 0; i < files.length; i++) {

        let fileName = files[i].trim();
        let indent = files[i].length - fileName.length;

        // create checkbox input element
        if(fileName != ""){

        let button = document.createElement('button');
        button.id = 'dir'+i;
        button.innerHTML = fileName;
        button.style.marginLeft = (indent * 10) + 'px';
        button.addEventListener('click',function(){

           if(fileName.includes(".")){
             endpoint +=  "&"+fileName;
             getContent(endpoint);
           }else{
             endpoint += "&"+fileName;
             fetchData(endpoint);
           }

        });

        filesElement.appendChild(button);
        filesElement.appendChild(document.createElement('br'));
      }
    }
    createBackButton(filesElement);
    //  addBottomBar(filesElement);
  }

  function addBottomBar(filesElement){
    // create move button
    let moveButton = document.createElement('button');
    moveButton.id = 'moveButton';
    moveButton.innerHTML = 'Move Selected Files';
    moveButton.addEventListener('click', function() {
      // show overlay and move form
      document.getElementById("overlay").style.display = "block";
    });
    //filesElement.appendChild(moveButton);


    // create download button
    let downloadButton = document.createElement('button');
    downloadButton.id = 'downloadButton';
    downloadButton.innerHTML = 'Download Selected Files';
    downloadButton.addEventListener('click', function() {
        postFileData();
    });
    filesElement.appendChild(downloadButton);

    // create delete button
    let deleteButton = document.createElement('button');
    deleteButton.id = 'deleteButton';
    deleteButton.innerHTML = 'Delete Selected Files';
    deleteButton.addEventListener('click', function() {
        // get all checked checkboxes
        let checkedBoxes = document.querySelectorAll("input[type='checkbox']:checked");
        // create array of file names to delete
        let filesToDelete = [];
        for (let i = 0; i < checkedBoxes.length; i++) {
            let fileName = checkedBoxes[i].dataset.fileName;
            filesToDelete.push(fileName);
        }
        // send request to backend to delete selected files
        //fetch('/delete-files', {
          //  method: 'POST',
        //    headers: {
          //      'Content-Type': 'application/json'
    //        },
     //       body: JSON.stringify({files: filesToDelete})
    //    }).then(() => {
            // update file list after deleting selected files
      //      fetchData();
 //       });
});
    filesElement.appendChild(deleteButton);
  }

  function updateFileList(files) {
      let filesElement = document.getElementById("files");
      // clear existing file elements
      filesElement.innerHTML = '';

      for (let i = 0; i < files.length; i++) {
          console.log(i + ': ' + files[i]);
          let fileName = files[i].trim();
          let indent = files[i].length - fileName.length;

          // create checkbox input element
          if(fileName != ""){
          if(fileName.includes(".")){
          let checkbox = document.createElement('input');
          checkbox.type = 'checkbox';
          checkbox.id = 'file' + i;
          checkbox.dataset.fileName = fileName;
          checkbox.innerHTML = fileName;
          filesElement.appendChild(checkbox);
          let label = document.createElement('label')
          label.innerHTML = fileName;
          filesElement.appendChild(label);
        }else{
          let button = document.createElement('button');
          button.id = 'dir'+i;
          button.innerHTML = fileName;
          button.style.marginLeft = (indent * 10) + 'px';
          button.addEventListener('click',function(){
          //  console.log(fileName);
            fetchData("&opt&minecraft&"+fileName+"&",button)
        });
          filesElement.appendChild(button);
        }

          filesElement.appendChild(document.createElement('br'));
        }
      }
      //  addBottomBar(filesElement);
    }

function logEndpoint(){
  console.log(endpoint);
}

setInterval(logEndpoint, 1000);
fetchDataInit(); // update file list using data from string
