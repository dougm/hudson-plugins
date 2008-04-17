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
  hudsonUrlEdit.value = options.getValue("hudsonUrlProp");
  intervalEdit.value  = options.getValue("intervalProp");
}

function onCancel() {
  // No settings will be saved.
}

function onOk() {
  var continueAndClose = false;
  if (validateProp()) {
   options.putValue("hudsonUrlProp", hudsonUrlEdit.value);
   options.putValue("intervalProp", intervalEdit.value);
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

  var number = parseInt(intervalEdit.value);
  if (isNaN(number)) {
    isValidate = false;
    errorMessage = intervalEdit.value + " " + VALID_TIMEOUT;
  }

  if (!isValidate) {
    view.alert(errorMessage);
  }

  return isValidate;
}

function hudsonUrlEdit_onclick() {

}
