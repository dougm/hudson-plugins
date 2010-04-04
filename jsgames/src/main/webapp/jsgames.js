// http://blogger.ziesemer.com/2008/05/javascript-namespace-function.html
var namespace = function(name, separator, container){
  var ns = name.split(separator || '.'),
    o = container || window,
    i,
    len;
  for(i = 0, len = ns.length; i < len; i++){
    o = o[ns[i]] = o[ns[i]] || {};
  }
  return o;
};
namespace('org_hudsonci_jsgames');

org_hudsonci_jsgames.selectmenuitem = function(id) {
    org_hudsonci_jsgames.resetmenuitems();
    var gameDiv = document.getElementById('jsgames_' + id);
    gameDiv.style.visibility = 'visible';
    gameDiv.style.height = '600px';
    gameDiv.style.width = '600px';
}

org_hudsonci_jsgames.resetmenuitems = function() {
    var gameDivs = document.getElementsByName('jsgames_game');
    for (var i = 0; i < gameDivs.length; i++) {
        gameDivs[i].style.visibility = 'hidden';
        gameDivs[i].style.height = 0;
        gameDivs[i].style.width = 0;
    }
}