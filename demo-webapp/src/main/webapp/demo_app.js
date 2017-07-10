/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

var RADIUS = 5;
var capturedClicksInput = {};
var clickNumber = 0;
var captchaImage;
var canvas;

function initPixelcaptcha() {

    document.getElementById("pixelcaptcha_canvas").onclick = captureClickCoordinates;
    document.getElementById("new_captcha_button").onclick = retrieveCaptchaAndDraw;
    document.getElementById("reset_solution_button").onclick = resetClientState;

    captchaImage = new Image();
    canvas = document.getElementById('pixelcaptcha_canvas');
}

function retrieveCaptchaAndDraw() {
    clearCapturedCoordinates();
    var request = new XMLHttpRequest();
    request.open("GET", "/getCaptcha");
    request.onreadystatechange = function () {
        if (request.readyState == 4 && request.status == 200) {
            loadCaptchaFromResponse(request.responseText);
        }
    }
    request.send();
}

function convertStringToJson(jsonString) {
    return JSON.parse(jsonString);
}

function loadCaptchaFromResponse(jsonString) {
    var json = convertStringToJson(jsonString);
    var id = document.getElementById('pixelcaptcha_id');
    id.value = json["id"];

    canvas.width = json["width"];
    canvas.height = json["height"];

    var img = new Image();
    //img.src = json["uri"];
    img.onload = function () {
        var context = canvas.getContext("2d");
        captchaImage = img;
        context.drawImage(img, 0, 0, canvas.width, canvas.height);
    }

    img.src = json["image"];
    //var context = canvas.getContext("2d");
    //context.drawImage(img, 0, 0, canvas.width, canvas.height);
    //captchaImage = img;
}

function clearCapturedCoordinates() {
    capturedClicksInput = {};
    clickNumber = 0;
}

function drawPixelCaptcha() {
    var context = canvas.getContext("2d");
    context.drawImage(captchaImage, 0, 0, canvas.width, canvas.height);
}


function resetClientState() {
    clearCapturedCoordinates();
    drawPixelCaptcha();
}


function captureClickCoordinates(event) {
    capturedClicksInput["p" + clickNumber] = {"x": event.offsetX, "y": event.offsetY};
    clickNumber++;

    var canvas = document.getElementById('pixelcaptcha_canvas');
    var c2d = canvas.getContext("2d");
    c2d.beginPath();
    c2d.arc(event.offsetX, event.offsetY, RADIUS, 0, 2 * Math.PI, true);
    c2d.fillStyle = "red";
    c2d.fill();

}

function submitSolution() {
    makeStatusGray();
    var payload = {};
    var id = document.getElementById("pixelcaptcha_id").value;
    payload["pixelcaptchaId"] = id;
    payload["solutionCoordinates"] = capturedClicksInput;
    var request = new XMLHttpRequest();
    request.open("POST", "/verifySolution");
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.onreadystatechange = function () {
        if (request.readyState == 4 && request.status == 200) {
            displayResponse("Validation Response", request.responseText);
            //retrieveCaptchaAndDraw(); Enable this to retrieve new CAPTCHA on submit
        }
    }
    request.send(JSON.stringify(payload));
    return false;
}

function displayResponse(label, responseText) {
    var json = JSON.parse(responseText);
    var verificationStatus = document.getElementById("info_content");
    var info_label = document.getElementById("info_label");
    if(json.status === "success") {
        info_label.className = "successbold";
        verificationStatus.className = "success";
    } else if (json.status === "failure") {
        info_label.className = "failurebold";
        verificationStatus.className = "failure";
    }

    verificationStatus.innerText = JSON.stringify(json, null, 2);
    info_label.innerText = label;
}

function setSubmitMethod() {
    var btn = document.getElementById("pixelcaptcha_submit_button");
    btn.onclick = submitSolution;
}

setSubmitMethod();
initPixelcaptcha();
retrieveCaptchaAndDraw();

/* Drawing transparent drawing in canvas
 context.fillStyle = 'rgba(0,0,255,0.2)';
 http://www.html5canvastutorials.com/advanced/html5-canvas-global-alpha-tutorial/
 */


function setCaptchaConfig() {
    makeStatusGray();
    var payload = {};
    var e;
    e = document.getElementById("challengeCount");
    payload["challengeCount"] = e.options[e.selectedIndex].value;
    e = document.getElementById("responseCount");
    payload["responseCount"] = e.options[e.selectedIndex].value;
    e = document.getElementById("orientation");
    payload["orientation"] = e.options[e.selectedIndex].value;
    e = document.getElementById("codePoints");
    payload["codePoints"] = e.options[e.selectedIndex].value;
    e = document.getElementById("ordered");
    payload["ordered"] = e.options[e.selectedIndex].value;


    var request = new XMLHttpRequest();
    request.open("POST", "/setConfig");
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.onreadystatechange = function () {
        if (request.readyState == 4 && request.status == 200) {
            setConfigurationStatus(request.responseText);
        }
    }
    request.send(JSON.stringify(payload));
    return false;
}

function setConfigurationStatus(jsonString) {
    var json = JSON.parse(jsonString);
    setInstructions(json);
    var valueToWrite = JSON.stringify(json, null, 2);

    setTimeout(function() {
        var td = document.getElementById("info_content");
        var info_label = document.getElementById("info_label");

        info_label.className = "configurationbold";
        td.className = "configuration";

        info_label.innerText = "Current Configuration";
        td.innerText = valueToWrite;
    }, 0);
}

function setInstructions(json) {
    var startOfMessage = "It's easy...   Just \"click on the ";
    var endOfMessage = " black characters\" similar to the blue ones and hit submit";
    var middle;
    switch(json.configuration.challengeCount) {
        case "2":
            middle = "two";
            break;
        case "3":
            middle = "three";
            break;
        case "4":
            middle = "four";
            break;
    }
    var e = document.getElementById("howtosolve");
    e.innerText = startOfMessage + middle + endOfMessage;
    debugger;
}

function setCaptchaConfigMethod() {
    var btn = document.getElementById("set_captcha_config");
    btn.onclick = setCaptchaConfig;
}

function setOnLoadMethod() {
    window.addEventListener("load", function () {
        displayConfig();
    })
}

function displayConfig() {
    makeStatusGray();
    var request = new XMLHttpRequest();
    request.open("GET", "/getConfig");
    request.onreadystatechange = function () {
        if (request.readyState == 4 && request.status == 200) {
            setConfigurationStatus(request.responseText);
        }
    }
    request.send();
}

setCaptchaConfigMethod();
setOnLoadMethod();

function makeStatusGray() {
    var td = document.getElementById("info_content");
    var info_label = document.getElementById("info_label");

    info_label.className = "waitingbold";
    td.className = "waiting";

}

setTimeout(function() {
    var e = document.getElementById("pixelcaptcha_form");
    e.className = "show";
}, 3000);