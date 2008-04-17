/*
Copyright (C) 2007 Olga Khylkouskaya

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

/**
 * list of hudson jobs to check status
 * @type Array
 */ 
var jobs = new Array();

/**
 * Amount of time to wait until displaying 'timed out' message
 * @type Number
 */
var TIMEOUT_MS = 10000;  // 10 seconds

/**
 * How often to check for new messages, under normal circumstances
 * @type Number
 */
var BASE_UPDATE_INTERVAL_MS = 300000;  // 300 seconds
// var BASE_UPDATE_INTERVAL_MS = 20000;  // 20 seconds
// var BASE_UPDATE_INTERVAL_MS = 1000;  // 1 seconds

/**
 * How often to check for new messages, if we're offline
 * @type Number
 */
var CHECK_ONLINE_STATUS_INTERVAL_MS = 30000;  // 30 seconds

/**
 * Keeps track of the current updateStatus timer. Used to ensure that
 * we don't have multiple updateStatus timers at the same time.
 */
var g_updateStatusTimer = null;

/**
 * interval between rows
 * @type Number
 */ 
var yinterval = 20;

/**
 * Default to a local Hudson instance on port 8080
 */
var DEFAULT_HUDSON_URL = "http://localhost:8080/";
// var defaultHudsonUrl = "http://simile.mit.edu/hudson/";
// var defaultHudsonUrl = "http://build.sourcelabs.org/hudson/";

/**
 * @fileoverview hudson viewer gadget
 * 
 * view the status of projects in hudson
 */ 
var hudsonUrl = DEFAULT_HUDSON_URL;

/**
 * Default to a local Hudson instance on port 8080
 */
var pollingInterval = BASE_UPDATE_INTERVAL_MS;

var updateFailCount = 0;

function view_onOpen() {
  initializeStoredOptions();
  hudsonUrl = options.getValue('hudsonUrlProp');
  updateStatus();
}

function initializeStoredOptions() {
  options.putDefaultValue('hudsonUrlProp', DEFAULT_HUDSON_URL);
  options.putDefaultValue('intervalProp', BASE_UPDATE_INTERVAL_MS);
}

/**
 * force a refresh if hudson url or polling interval changes
 * 
 */
function onOptionChanged() {
  if (g_updateStatusTimer) {
    view.clearTimeout(g_updateStatusTimer);
    g_updateStatusTimer = null;
  }

  hudsonUrl = options.getValue('hudsonUrlProp');
  pollingInterval = options.getValue('intervalProp');

  updateStatus();
}

/**
 * delete all previous elements, create elements according to jobs array
 * create row(job link, status image, updated info) for each job
 * 
 */ 
function createElements() {

  errorDiv.visible = false;
  contentDiv.visible = true;
  contentDiv.removeAllElements();

  var newHeight = yinterval * jobs.length;
  if (newHeight < yinterval) {
    newHeight = yinterval;
  }
  var addedHeight = newHeight - contentDiv.height;
  contentDiv.height = newHeight;
  // changeViewSize(addedHeight);

  for (var i=0; i<jobs.length;++i) {
    var job = jobs[i];
    var label = "<a height='16' width='120' x='0' y='" + (i*yinterval) + "' href='" + job.url + "'>" + job.name + "</a>";
    contentDiv.appendElement(label);
    var img = "<img height='16' name='" + job.name + "Img' width='16' x='130' y='" + (i*yinterval)+ "' src='images/" + job.color + ".gif'/>";
    contentDiv.appendElement(img);
  }

}

/**
 * Sets a timeout for updateStatus to be called depending on the
 * online/offline state
 */
function registerUpdateStatus() {
  var timeout;
  if (framework.system.network.online == false) {
    timeout = CHECK_ONLINE_STATUS_INTERVAL_MS;
  } else {
    timeout = pollingInterval;
  }

  if (g_updateStatusTimer) {
    view.clearTimeout(g_updateStatusTimer);
    g_updateStatusTimer = null;
  }

  g_updateStatusTimer = view.setTimeout(updateStatus, timeout);
}

/**
 * send request to hudson url using ajax
 * 
 * @param {String} url The url to hudson dashboard
 */ 
function updateStatus() {
  var apiUrl = hudsonUrl + "/api/json";
  httpRequest = new XMLHttpRequest();
  httpRequest.open("GET", apiUrl, true);
  httpRequest.onreadystatechange = parseJSON;
  httpRequest.send(null);

  setViewPollTime();

  // make sure updateStatus gets called again
  registerUpdateStatus();
}

function setViewPollTime() {
  var currentTime = new Date();
  var hours = currentTime.getHours();
  var minutes = currentTime.getMinutes();
  if (minutes < 10) {
    minutes = "0" + minutes;
  }
  lastPollTime.value = hours + ":" + minutes;
}

function parseJSON() {
  if (httpRequest.readyState == 4) {
    var nextRefreshTime = 2 * 60 * 1000;  // in error cases try after 2 min
    if (httpRequest.status == 200) {
      try {
        jsonObj = eval("(" + httpRequest.responseText + ")");
        jobs = jsonObj.jobs;
        updateFailCount = 0;  // successfully parsed stuff
        createElements();
      } catch(e) {
        jobs = null;
        updateFailCount++;
      }
    } else {
      updateFailCount++;
    }
    httpRequest = null;

    if (updateFailCount > 3) {
      debug.trace("network failure");
      // too many failures so stop retrying
      contentDiv.removeAllElements();
      contentDiv.visible = false;
      errorDiv.visible = true;
    }
  }
}

/**
 * change size of view
 * 
 * @param {Number} val on how much to change size, can be negative
 */ 
function changeViewSize(val) {
    var zoomHeight = view.height;
    view.resizable = 'true';
    var normalHeight = view.height;
    var koeff = zoomHeight/normalHeight;
    view.resizable = 'zoom';
    var newViewHeight = view.height + val*koeff;
    view.height = newViewHeight
}

function onOpenOptionsClick() {
  pluginHelper.ShowOptionsDialog();
} 

function jobLabel_onclick() {

}
