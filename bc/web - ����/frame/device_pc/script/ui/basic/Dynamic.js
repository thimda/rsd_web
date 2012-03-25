window.currentLoadingLib = [];
window.idFuncArr = {};
window.FUNC_ID = 1;
function require(id, func, uuid){
	if(window.loadedLib[id] != null){
		if(uuid != null){
			func = idFuncArr[uuid];
			if(func)
				func();
		}
		else{
			if(func)
				func();
		}
		return;
	}
	if(window.currentLoadingLib[id] != null){
		if(uuid == null){
			uuid = window.FUNC_ID ++;
		}
		idFuncArr[uuid] = func;
		setTimeout("require('" + id + "', null, '" + uuid + "')", 100);
		return;
	}
	var filelib = calculateAllLibs(id);
	var idlib = filelib.idlib;
	if(idlib != null){
		for(var i = 0; i < idlib.length; i ++)
			window.currentLoadingLib[idlib[i]] = 1;
	}
	require_css(filelib.csslib);
	require_js(filelib.jslib, func, filelib.idlib);
}

function calculateAllLibs(id){
	var jsArr = [];
	var cssArr = [];
	var idArr = [];
	calculateLibs(id, jsArr, cssArr, idArr)
	var filelib = {};
	filelib.csslib = cssArr;
	filelib.jslib = jsArr;
	filelib.idlib = idArr;
	return filelib;
}
function calculateLibs(id, jsArr, cssArr, idArr) {
	if(window.loadedLib[id] != null){
		return;
	}
	var lib = window.libArray[id];
	var dps = lib.dp;
	if(dps != null){
		for(var i = 0; i < dps.length; i ++){ 	
			calculateLibs(dps[i], jsArr, cssArr, idArr);
		}
	}
	if(lib.jslib != null){
		for(var i = 0; i < lib.jslib.length; i ++)
			jsArr.push(lib.jslib[i]);
	}
	if(lib.csslib != null){
		for(var i = 0; i < lib.csslib.length; i ++)
			cssArr.push(lib.csslib[i]);	
	}
	idArr.push(id);
}
window.cssLoadRecord = {};
window.jsLoadRecord = {};
function require_css(files) {
	if(files == null || files.length == 0)
		return;
	var html_doc = document.getElementsByTagName('head')[0];
	for(var i = 0; i < files.length; i ++){
		if(window.cssLoadRecord[files[i]] != null)
			continue;
		window.cssLoadRecord[files[i]] = 1;
		var css = document.createElement('link');
		css.setAttribute('rel', 'stylesheet');
		css.setAttribute('type', 'text/css');
		
		var path = window.themePath + "/" + files[i].replaceAll("\\.", "/") + ".css";
		css.setAttribute('href', path);
		html_doc.appendChild(css);
	}
}
function require_js(files, func, ids) {
	if(files == null || files.length == 0){
		for(var i = 0; i < ids.length; i ++)
			window.loadedLib[ids[i]] = 1;
		func();
		return;
	}
	var html_doc = document.getElementsByTagName('head')[0];
	if(window.jsLoadRecord[files[0]] != null){
		if(files.length == 1){
			for(var i = 0; i < ids.length; i ++){
				window.currentLoadingLib[ids[i]] = null;
				window.loadedLib[ids[i]] = 1;
			}
			func();
		}
		else{
			files.splice(0, 1);
			require_js(files, func, ids);
		}
		return;
	}
	var js = document.createElement('script');
	js.setAttribute('type', 'text/javascript');
	var path = window.baseGlobalPath + "/frame/device_pc/script/" + files[0].replaceAll("\\.", "/") + ".js";
	js.setAttribute('src', path);
	html_doc.appendChild(js);
	
	js.onreadystatechange = function () {
		if (js.readyState == 'complete') {
			window.jsLoadRecord[files[0]] = 1;
			if(files.length == 1){
				for(var i = 0; i < ids.length; i ++){
					window.currentLoadingLib[ids[i]] = null;
					window.loadedLib[ids[i]] = 1;
				}
				func();
			}
			else{
				files.splice(0, 1);
				require_js(files, func, ids);
			}
		}
	}
	js.onload = function () {
		window.jsLoadRecord[files[0]] = 1;
		if(files.length == 1){
				for(var i = 0; i < ids.length; i ++){
					window.currentLoadingLib[ids[i]] = null;
					window.loadedLib[ids[i]] = 1;
				}
				func();
		}
		else{
			files.splice(0, 1);
			require_js(files, func, ids);
		}
	}
} 