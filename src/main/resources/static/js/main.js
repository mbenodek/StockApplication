'use strict'

let uploadForm = document.querySelector("#fileUploadForm");
let uploadFormInput = document.querySelector("#fileUploadInput");
let downloadFile = document.querySelector("#downloadFileUrl");

let record = document.querySelector("#record");
let uploadSingleRecordForm = document.querySelector("#searchRecordForm");
let stockName = document.querySelector("#stockName");





function uploadFile(file) {
    let formData = new FormData();
    formData.append("file", file);

    let req = new XMLHttpRequest();
    req.open("POST", "http://localhost:24103/upload")

    req.onload = function () {
        console.log(req.responseText);

        let response = req.responseText;

        if (response !== null) {
            let downloadUrl = "http://localhost:24103/download/" + response;

            downloadFile.innerHTML = '<p>File Upoaded Successfully. <br/> <a href="' + downloadUrl + '" target="_self">Download File</a></p>';
            downloadFile.style.display = "block";
        } else {
            alert("Error Occured! No file returned");
        }
    }

    req.send(formData);
}

uploadForm.addEventListener('submit', function (event) {
    const files = uploadFormInput.files;

    if (files.length !== 0 ) {
        uploadFile(files[0]);
        event.preventDefault();
    } else {
        alert('Please select a file')
    }

}, true);


function upLoadSingleRecord(record) {
    let formData = new FormData();


    let req = new XMLHttpRequest();
    req.open("POST", "http://localhost:24103")

    req.onload = function () {
        console.log(req.responseText);

        let response = req.responseText;

        if (response !== null) {


            record.innerHTML = '<p>Record Upoaded Successfully. </p><br/>' ;
            record.style.display = "block";
        } else {
            alert("Error Occured! No Record Uploaded");
        }
    }

    req.send(formData);
}

uploadSingleRecordForm.addEventListener('submit', function (event) {

   if (record.length !== 0 ) {
     upLoadSingleRecord(record)
      event.preventDefault();
   }else {
            alert('Please Search by giving stock Name')
        }


}, true);


