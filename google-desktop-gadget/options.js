/*
Copyright (C) 2008 Jeff Black

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/ 

function onOpen() {

  // map minutes to droplist index
  var validMinuteIntervals = new Object();
  validMinuteIntervals[1] = 0;
  validMinuteIntervals[2] = 1;
  validMinuteIntervals[5] = 2;
  validMinuteIntervals[10] = 3;

  hudsonUrlEdit.value = options.getValue("hudsonUrlProp");
  intervalMinutes.selectedIndex= validMinuteIntervals[options.getValue("intervalMinutesProp")];

}

function onCancel() {
  // No settings will be saved.
}

function onOk() {
  var continueAndClose = false;
  if (validateProp()) {
   if (hudsonUrlEdit.value.substring(hudsonUrlEdit.value.length-1) != "/") {
     hudsonUrlEdit.value += "/";
   }
   options.putValue("hudsonUrlProp", hudsonUrlEdit.value);
   options.putValue("intervalMinutesProp", intervalMinutes.value);
   continueAndClose = true;
  }
  return continueAndClose;
}

function validateProp() {
  var hudsonUrl = hudsonUrlEdit.value;
  var isValidate = true;
  var errorMessage;

  var protocol = hudsonUrl.substring(4,0);
  if (hudsonUrl == null || hudsonUrl.length == 0 || protocol != "http") {
    isValidate = false;
    errorMessage = ERR_URL_EMPTY;
  }

  if (!isValidate) {
    view.alert(errorMessage);
  }

  return isValidate;
}

function intervalMinutes_onchange() {
  var intervalMinutesValue = intervalMinutes.value;
  // debug.trace("intervalMinutes=" + intervalMinutes);
}

/* 
  workaround until the next release of google desktop which will have the option to stop the behavior of
  clicking in this field launching the URL in the default browser
*/
function clearUrl_onclick() {
  hudsonUrlEdit.value = "";
}
