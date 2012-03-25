function Application(){
	this.pageUIMap = {};
}

Application.prototype.addPageUI = function(id, pageUI){
	this.pageUIMap[id] = pageUI;	
};

Application.prototype.getPageUI = function(id){
	var pageUI = this.pageUIMap[id];
	if(pageUI == null)
		pageUI = getPopParent().pageUI;
	return pageUI;
};

function getApplication(){
	if(window.application == null)
		window.application = new Application();
	return window.application;
};


/**
 * @fileoverview Lfw客户端全局工具类。
 * @author dengjt
 */

/**
 * 通过此方法，获取json注册服务
 * 
 * @param sName 服务名
 */
function getService(sName) {
	var service = new ServiceProxy(sName);
//	return window.$jsonrpc[sName];
	return service;
};


function ServiceProxy(sName) {
	this.name = sName;
};


ServiceProxy.prototype.execute = function(method, args){
	var obj = {};
	obj.rpcname = this.name;
	obj.method = method;
	for(var i = 1; i < arguments.length; i ++){
		obj["params" + (i - 1)] = arguments[i];
	}
	
	var data = toJSON(obj);
	var ajax = new Ajax();
	ajax.setPath(getCorePath() + "/rpc");
	ajax.setQueryStr("rpcdata=" + data);
	ajax.setReturnFunc(ServiceProxy.$returnFun);
	var innerArgs = [ null, null, ajax ];
	ajax.setReturnArgs(innerArgs);
	return ajax.post(false);
};

ServiceProxy.$returnFun = function(xmlHttpReq, returnArgs, exception) {
	var ajaxObj = returnArgs[2];
	var returnFunc = returnArgs[1];
	var userArgs = returnArgs[0];
	var text = xmlHttpReq.responseText;
	eval("var obj = " + text);
	return obj;
};

/**
 * 获取编辑公式服务
 */
function getFormularService() {
	return getService("nc.uap.lfw.core.model.formular.IEditFormularService");
};

/**
 * 获取上传文件服务
 */
function getFileService() {
	return getService("nc.uap.lfw.core.model.file.IFileService");
};

/**
 * 获取页面参数Map表.此参数是从URL中获取
 * 
 * @return{HashMap} paramsMap
 */
function getParamsMap() {
	return window.$paramsMap;
};

/**
 * 根据key获取页面参数
 */
function getParameter(key) {
	var value = window.$paramsMap.get(key);
	return value == null ? null : decodeURIComponent(value);
};

/**
 * 设置某一参数
 */
function setParameter(key, value) {
	window.$paramsMap.put(key, value);
};

/**
 * 获取客户端session属性map
 */
function getSessionAttributeMap() {
	if (typeof $cs_clientSession != "undefined")
		return $cs_clientSession.map;
};

/**
 * 设置Session属性
 * @param key
 * @param value
 * @return
 */
function setSessionAttribute(key, value) {
	$cs_clientSession.map[key] = value;
};

/**
 * 根据key获取客户段session中对应的value
 * 
 * @param key
 */
function getSessionAttribute(key) {
	if (typeof $cs_clientSession != "undefined")
		return $cs_clientSession.map[key];
};

/**
 * 获取客户端StickString
 */
function getStickString() {
	return window.$cs_clientStickKeys;
};

/*
 * 页面在关闭或者刷新时调用该方法,如果processor中覆盖了基类 的destroy方法将会被调用,但用户可以阻止后台destroy方法的调用,
 * 只要重载onBeforeDestroyPage方法返回false即可
window.onbeforeunload = function() {
	if (window.pageUI == null)
		return;
	if (window.hasCloseConfirmed == true) {
		window.hasCloseConfirmed = null;
		return;
	}
	var result = window.pageUI.$onClosing();
	if (result == false) {
		return "是否关闭";
	} else {
		window.hasCloseConfirmed = null;
		return;
	}
};
 */
EventUtil.addEventHandler(window,"unload" ,function() {
	if (window.pageUI == null)
		return;
	window.pageUI.$onClosed();
});

/**
 * 获取对应的数据格式渲染器
 */
function getMasker(type) {
	if(typeof IntegerTextComp != "undefined" && type == IntegerTextComp.prototype.componentType){
		return new NumberMasker(window.$maskerMeta.NumberFormatMeta);
	}
	if(typeof DateTextComp != "undefined" && type == DateTextComp.prototype.componentType){
		return new DateMasker(window.$maskerMeta.DateFormatMeta);
	}
	if(type == "DateTimeText")
		return new DateTimeMasker(window.$maskerMeta.DateTimeFormatMeta);
	return null;
}

/**
 * 获取页面标识ID
 */
function getPageId() {
	return getSessionAttribute("pageId");
};

/**
 * 获取页面唯一标识
 * @return
 */
function getPageUniqueId() {
	return getSessionAttribute("pageUniqueId");
};

/**
 * 获取application唯一标识
 */
function getAppUniqueId() {
	return getSessionAttribute("appUniqueId");
};

/**
 * 获取web应用context路径
 */
function getRootPath() {
	return window.globalPath;
};

/**
 * 获取Web应用LfwDispatchServlet配置的路径
 */
function getCorePath() {
	return window.corePath;
};

/**
 * 获取单据在nodes目录下的路径
 */
function getNodePath() {
	return window.globalPath + "/html/nodes/" + window.$pageId;
};

/*
 * 非IE浏览器，增加XPath方式解析Dom的能力
 */
if (!IS_IE) {
	Element.prototype.selectNodes = function(xPath) {
		// XPathEvaluator利用方法evaluate()计算XPath表达式的值
		var evaluator = new XPathEvaluator();
		// evaluate的5个参数说明:XPath表达式,上下文节点,命名空间解释程序(只有在XML代码用到XML命名空间时才是必要的,通常留空null),
		// 返回的结果的类型(有10个常量值可以选择),在XPathResult中存放结果(通常为null)
		var nodeList = evaluator.evaluate(xPath, this, null,
				XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		var nodes = new Array();
		if (nodeList != null) {
			var node = nodeList.iterateNext();
			while (node) {
				nodes.push(node);
				node = nodeList.iterateNext();
			}
		}
		return nodes;
	};

	Element.prototype.selectSingleNode = function(xPath) {
		var evaluator = new XPathEvaluator();
		var oResult = evaluator.evaluate(xPath, this, null,
				XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		if (oResult != null) {
			var node = oResult.iterateNext();
			return node;
		} else {
			return null;
		}
	};
};

/**
 * 将传入string解析成Dom对象。避免浏览器兼容问题
 */
function createXmlDom(strXML) {
	// IE
	if (window.ActiveXObject) {
		var sigArr = [ "MSXML2.DOMDocument.5.0", "MSXML2.DOMDocument.4.0",
				"MSXML2.DOMDocument.3.0", "MSXML2.DOMDocument",
				"Microsoft.XmlDom" ];
		for ( var i = 0; i < sigArr.length; i++) {
			try {
				var xmlDom = new ActiveXObject(sigArr[i]);
				// 文件按同步方式载入,默认为异步模式
				xmlDom.async = false;
				// loadXML()方法直接接受XML字符串
				// load()(参数为要载入的文件名)方法用于从服务器上载入XML文件(只能载入与包含JavaScript的页面存储于同一服务器上的文件)
				xmlDom.loadXML(strXML);
				return xmlDom;
			} catch (error) {
				// ignore
			}
		}
	}
	// FireFox
	else if (document.implementation && document.implementation.createDocument) {
		return new DOMParser().parseFromString(strXML, "text/xml");
	} else {
		throw new Error("Your browser doesn't support an XML DOM object.");
	}
};

/**
 * 显示进度对话框
 * 
 * @param message 要显示标题
 * @param attachComp 绑定控件，将显示于指定控件中心位置
 */
function showProgressDialog(message, attachComp) {
	ProgressDialogComp.showDialog(message);
};

/**
 * 隐藏进度对话框
 */
function hideProgressDialog() {
	ProgressDialogComp.hideDialog();
};

/**
 * 显示错误对话框
 */
function showErrorDialog(msg) {
	require('errordialog', function(){ErrorDialogComp.showDialog(msg)});
};

/**
 * 隐藏错误对话框
 */
function hideErrorDialog() {
	ErrorDialogComp.hideDialog();
};

/**
 * 显示警告对话框
 */
function showWarningDialog(msg) {
	return WarningDialogComp.showDialog(msg);
};

/**
 * 隐藏警告对话框
 */
function hideWarningDialog() {
	WarningDialogComp.hideDialog();
};

/**
 * 显示信息对话框
 */
function showMessageDialog(msg) {
	require('messagedialog', function(){MessageDialogComp.showDialog(msg)});
};

/**
 * 隐藏信息对话框
 */
function hideMessageDialog() {
	MessageDialogComp.hideDialog();
};

/**
 * 在正中位置打开窗口
 */
function openWindowInCenter(url, title, height, width) {
	var bodyWidth = window.screen.availWidth - 30;
	var bodyHeight = window.screen.availHeight - 30;
	var left = bodyWidth > width ? (bodyWidth - width)/2 : 0;
	var top = bodyHeight > height ? (bodyHeight - height)/2 : 0;
//	if(IS_IE)
		window.showModalDialog(url, self, "status:no;dialogHeight:" + height + "px;dialogWidth:" + width + "px;dialogLeft:" + left + "px;dialogTop:" + top + "px");
//	else
//		window.open(url, title, "modal=yes, height=" + height + ", width=" + width + ", left=" + left + ", top=" + top, true);
};

/**
 * 在正中位置打开窗口(非模态)
 * @param url
 * @param title
 * @param height
 * @param width
 * @param closeParent
 * @return
 */
function openNormalWindowInCenter(url, title, height, width, closeParent) {
	var bodyWidth = window.screen.availWidth - 30;
	var bodyHeight = window.screen.availHeight - 30;
	var left = bodyWidth > width ? (bodyWidth - width)/2 : 0;
	var top = bodyHeight > height ? (bodyHeight - height)/2 : 0;
	var win = window.open(url, title, "modal=yes, height=" + height + ", width=" + width + ", left=" + left + ", top=" + top, true);
	if(closeParent){
		if(win == window)
			return;
		closeWinWithNoWarn();
	}
};

/**
 * 最大化显示窗口
 * @param url
 * @param title
 * @param closeParent
 * @return
 */
function showMaxWin(url, title, closeParent) {
	//showMaxWin('" + url + "', '" + title + "', " + closeParent + ")");
	var win = window.open(url, title, 'resizable=no,scrollbars=yes');
	win.moveTo(-4, -4);
	var width = screen.availWidth+8;
	var height = screen.availHeight+7;

	win.resizeTo(width, height);
	if(closeParent){
		if(win == window)
			return;
		closeWinWithNoWarn();
	}
};

/**
 * 强行关闭窗口
 * @return
 */
function closeWinWithNoWarn(){ 
	var browserName=navigator.appName; 
	
	if(IS_IE) { 
		window.opener=null; 
		window.open('','_self'); 
		window.close(); 
	} 
	//if (browserName=="Netscape") { 
	else{	
		window.open('','_parent',''); 
		window.close(); 
	} 
};

/**
 * 打开一个模式窗体
 */
function showWin(pageUrl, width, height) {
	var pos = pageUrl.indexOf("?");
	var randId = (Math.random() * 10000).toString().substring(0, 4);
	if (pos == -1)
		pageUrl += "?randid=" + randId;
	else
		pageUrl += "&randid=" + randId;
	if (width == null || width == "")
		width = parseInt(window.screen.width) - 200;
	if (height == null || height == "")
		height = parseInt(window.screen.height) - 300;
	window.showModalDialog(pageUrl, self, "dialogHeight:" + height
			+ "px;dialogWidth:" + width
			+ "px;center:yes;resizable:yes;status:no");
};

/**
 * 显示导向某一页面的ModalDialog,id用来区分要显示的Dialog
 */
function showDialog(pageUrl, title, width, height, id, refresh, clearState, refDiv) {
	require("modaldialog", function(){
		if (showDialog.dialogCount == null)
			showDialog.dialogCount = 0;
		if (showDialog.dialogsTrueParent == null)
			showDialog.dialogsTrueParent = new Array();
	
		var dialogName = "$modalDialog" + showDialog.dialogCount;
		if (id == null)
			id = "";
		var nowWidth = document.body.clientWidth;
		var nowHeight = document.body.clientHeight;
		if(width == null)
			width = 400;
		if(height == null)
			height = 300;
		if(width < 0)
			width = nowWidth + width;
		if(height < 0)
			height = nowHeight + height;
			
		if(title == null)
			title = "对话框";
		if (nowWidth < (width - 40) || nowHeight < (height - 40)) {
			if(window != top && parent.showDialog != null){
				showDialog.showInParent = true;
				parent.showDialog.dialogsTrueParent[parent.showDialog.dialogCount] = window; 
				parent.showDialog(pageUrl, title, width, height, id, refresh, clearState);
				return;
			}
		}
		if (window[dialogName] == null) {
			window[dialogName] = new ModalDialogComp("g_modalDialog", title, 0, 0,
					width, height, null);
			window[dialogName].clearState = clearState;
			/**
			 * @private
			 */
			window[dialogName].onClosing = function() {
				var frame = this.getContentPane().firstChild;
				if(frame){
					if (this.clearState)
						frame.src = "";
		//			showDialog.trueParent = null;
					showDialog.dialogsTrueParent[showDialog.dialogCount] = null;
					showDialog.dialogCount--;
					
					this.getContentPane().removeChild(frame);
				}
			};
			
			var dls = new DialogListener();
			dls.source_id = dialogName;
			dls.listener_id = 'onBeforeClose_' + dialogName;
			
			dls.beforeClose = function (){
				if (typeof (window["$modalDialogFrame" + id].contentWindow) == "unknown") return;
				if(window["$modalDialogFrame" + id].contentWindow == null) return;
				var pageui = window["$modalDialogFrame" + id].contentWindow.pageUI;
				if(pageui)
					return pageui.showCloseConfirm(window[dialogName]);
			};
			window[dialogName].addListener(dls);
		} 
		else {
			window[dialogName].setSize(width, height);
			window[dialogName].setTitle(title);
		}
		if (window["$modalDialogFrame" + id] == null) {
			var iframe = $ce("iframe");
			iframe.name = "in_frame";
			iframe.id = "in_frame";
			iframe.allowTransparency = "true";
			iframe.frameBorder = 0;
			iframe.style.width = "100%";
			iframe.style.height = "100%";
			window["$modalDialogFrame" + id] = iframe;
		}
		window[dialogName].getContentPane().appendChild(
				window["$modalDialogFrame" + id]);
		var reload = false;
		if (window["$modalDialogFrame" + id].src == null
				|| window["$modalDialogFrame" + id].src == "" || refresh) {
			reload = true;
			window["$modalDialogFrame" + id].src = pageUrl;
		}
		
		window[dialogName].closeBt.style.visibility = "hidden";
		window[dialogName].show(refDiv);
		showDialogColseIcon(id, dialogName);
		
		// iframe显示之前调用此方法(父调用子)
		if (!reload && window["$modalDialogFrame" + id].contentWindow.onBeforeActive != null) {
	
			window["$modalDialogFrame" + id].contentWindow.onBeforeActive();
		}
		// window.$modalDialog_instanceframe = window["$modalDialogFrame" + id];
		showDialog.dialogCount++;
//		return window[dialogName];
	});
};

/**
 * 显示弹出窗close图标
 * @param {} id
 * @param {} dialogName
 * @param {} count  setTimeout次数，限制在10次
 */
function showDialogColseIcon(id, dialogName, count){
	if (count == null) count = 1;
	if (window["$modalDialogFrame" + id].contentWindow.renderDone == true || count >=10){
		window[dialogName].closeBt.style.visibility = "";
	}
	else{
		count += 1;
		setTimeout("showDialogColseIcon('" + id+ "', '" + dialogName + "'," + count + ")",500);
	}
}


/**
 * 隐藏对话框
 * @param id
 * @param clearState
 * @return
 */
function hideDialog(id, clearState) {
	if (showDialog.showInParent) {
		showDialog.showInParent = false;
		parent.hideDialog(id, clearState);
		return;
	}
	var dialogName = "$modalDialog" + (showDialog.dialogCount - 1);
	// var frame = null;
	var dialog = window[dialogName];
	// if(id == null)
	// frame = window.$modalDialog_instanceframe;
	// else
	// frame = window["$modalDialogFrame" + id];
	if (dialog) {
		dialog.clearState = clearState;
		dialog.close();
	}
};

/**
 * 获取当前对话框
 * @param isOpen
 * @return
 */
function getCurrentDialog(isOpen) {
	var dialogName = null;
	if (isOpen == null || isOpen == false)
		dialogName = "$modalDialog" + (showDialog.dialogCount);
	else
		dialogName = "$modalDialog" + (showDialog.dialogCount - 1);
	return window[dialogName];
};

/**
 * 获取真正所属父窗口，并非目前显示的父窗口
 */
function getTrueParent() {
//	return parent.showDialog.trueParent != null ? parent.showDialog.trueParent
//			: parent;
	if (parent.showDialog.dialogsTrueParent == null)
		return parent;
	else	
		return parent.showDialog.dialogsTrueParent[parent.showDialog.dialogCount - 1] != null ? parent.showDialog.dialogsTrueParent[parent.showDialog.dialogCount - 1]
				: parent;
};

/**
 * 获取顶端页面
 * @return
 */
function getPopParent() {
	if(window.parentWindow != null)
		return window.parentWindow;
	try{
//		debugger;
//		if (window.location.indexOf("app/infpubcard/reference?") > 0)
//			return getTrueParent();
		if (window.parent != window)
			return getTrueParent();
		if (window.dialogArguments != null
				&& window.parent.dialogArguments == window.dialogArguments)
			return window.dialogArguments;
		if (window.opener)
			return window.opener;
	}
	catch(error){
		return null;
	}
};

window.parentWindow = getPopParent();
/**
 * 获取顶端窗口
 * @return
 */
function getTop() {
	var win = window;
	while(win != null && (win.lfwTop == null || win.lfwTop == false)) {
		win = getPopParent();
	}
	return win;
};

/**
 * 为文本编辑器所定制的显示对话框的方法
 */
function showCommonDialog(pageUrl, title, width, height, id) {
	if (id == null)
		id = "";

	var returnValue = showModalDialog(pageUrl, window, "dialogWidth:"
			+ width + ";dialogHeight:" + height + ";status:0;help:0;");
	return returnValue;
};

/**
 * 处理Ajax请求的异常信息
 * 
 * @return true 没有后台异常，处理成功；false表示不成功，并在异常对话框中显示后台的异常信息
 */
function handleException(xmlHttpReq, exception, ajaxArgs, ajaxObj) {
	var doc = xmlHttpReq.responseXML;
	return handleExceptionByDoc(doc, exception, ajaxArgs, ajaxObj, xmlHttpReq);
};

/**
 * 进入登录页面
 */
function openLoginPage() {
	var url = window.corePath + "/login.jsp?pageId=login&randid=" + (new Date()).getTime();
	if (window.top != window) {
		var parentPage = parent;
		while (parentPage){
			if (parentPage == parentPage.parent)
				break;
			parentPage = parentPage.parent;
		}
		if (parentPage)
			parentPage.location.href = url;
	} else {
		window.location.href = url;
	}
};

/**
 * 如果有异常发生，将后台的异常信息显示在异常对话框中，返回false。否则直接返回true
 */
function handleExceptionByDoc(doc, exception, ajaxArgs, ajaxObj, xmlHttpReq) {
	if (exception) {
		if (xmlHttpReq != null && xmlHttpReq.status == 306) {  // 如果是会话失效，则跳转到登陆页
			openLoginPage();
		} else {
			showErrorDialog(exception);
		}
		return false;
	}

	if(doc == null)
		return;
	var rootNode = doc.documentElement;
	if (rootNode == null)
		return;
	var successNode = rootNode.selectSingleNode("success");
	if(successNode == null)
		return;
	success = successNode.firstChild.nodeValue;
	if (success == "false") {
		var expTextNode = rootNode.selectSingleNode("exp-text");
		var expStackNode = rootNode.selectSingleNode("exp-stack");
		var showMessageNode = rootNode.selectSingleNode("show-message");
		var expText = expTextNode.firstChild == null ? ""
				: expTextNode.firstChild.nodeValue;
		if (IS_IE)
			expStack = expStackNode.firstChild.nodeValue;
		else
			expStack = expStackNode.firstChild.nextSibling.data;

		var showMessage = showMessageNode.firstChild == null ? "error occurred"
				: showMessageNode.firstChild.nodeValue;
		showErrorDialog(showMessage);
		return false;
	} 
	else if (success == "interaction") {
//		var showMessageNode = rootNode.selectSingleNode("show-message");
//		var key = rootNode.selectSingleNode("exp-text").firstChild.nodeValue;
//		rePostReq.ajaxArgs = ajaxArgs;
//		rePostReq.key = key;
//		showConfirmDialog(showMessageNode.firstChild.nodeValue, rePostReq, cancelPost);
//		return window.isContinuePost;
		var rootNode = doc.documentElement;
		var contentsNode = rootNode.selectSingleNode("contents"); 
		var contentNodes = contentsNode.selectNodes("content");
		var content = getNodeValue(contentNodes[0]);
		content = decodeURIComponent(content);
		eval("var interationInfo = " + content);
		rePostReq.dialogId = interationInfo.id;
		if(interationInfo.type == "OKCANCEL_DIALOG") {
			var msg = interationInfo.msg;
			var okText = interationInfo.okText;
			var cancelText = interationInfo.cancelText;
			rePostReq.ajaxObj = ajaxObj;
			showConfirmDialog(msg, rePostOk, rePostCancel, null, null, null, okText, cancelText);
		}
		else if(interationInfo.type == "THREE_BUTTONS_DIALOG") {
			var msg = interationInfo.msg;
			rePostReq.ajaxObj = ajaxObj;
			require("threebuttondialog", function(){ThreeButtonsDialog.showDialog(msg, rePostOk, rePostCancel, rePostMiddle, interationInfo.btnTexts)});
		}
		else if (interationInfo.type == "MESSAGE_DIALOG") {
			var msg = interationInfo.msg;
			rePostReq.ajaxObj = ajaxObj;
			rePostReq.okReturn = interationInfo.okReturn;
			showMessageDialog(msg, rePostOk);
		}
		else if(interationInfo.type == "INPUT_DIALOG") {
			var items = interationInfo.items;
			if (items != null) {
				var title = interationInfo.title;
				var id = rePostReq.dialogId;
				ajaxObj.addParam(id + "interactflag", "true");
				rePostReq.ajaxObj = ajaxObj;
				require("inputdialog", function(){
					var inputDlg = new InputDialogComp("input_dialog", title, 0, 0, null, null, null, rePostReq);
					rePostReq.inputDlg = inputDlg;
					for (var i = 0; i < items.length; i++) {
						var item = items[i];
						var inputId = item.inputId;
						var labelText = item.labelText;
						var inputType = item.inputType;
						var required = item.required; 
						var value = item.value;
						if (inputType == "string") {
							inputDlg.addItem(labelText, inputId, inputType, required, null, value);
						}
						else if (inputType == "int") {
							var attr = new Object;
							attr.minValue = item.minValue;
							attr.maxValue = item.maxValue;
							inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
						}
						else if(inputType == "combo") {
							var datas = item.comboData.allCombItems;
							var comboData = new ComboData();
							var attr = new Object;
							attr.selectOnly = item.selectOnly;
							var combData = new ComboData();
							for(var j = 0; j < datas.length; j++){
								var text = datas[j].text;
								if(text == null)
									text = datas[j].i18nName;
								combData.addItem(new ComboItem(text, datas[j].value));
							}
							attr.comboData = combData;	
							inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
						}
						else if(inputType == "radio"){
							var datas = item.comboData.allCombItems;
							var comboData = new ComboData();
							var attr = new Object;
							attr.selectOnly = item.selectOnly;
							var combData = new ComboData();
							for(var j = 0; j < datas.length; j++){
								var text = datas[j].text;
								if(text == null)
									text = datas[j].i18nName;
								combData.addItem(new ComboItem(text, datas[j].value));
							}
							attr.comboData = combData;	
							inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
						}
					}
					inputDlg.show();
				});
			}
		}
		return false;
	}
	return true;
};

/**
 * 点OK后执行方法
 */
function rePostOk() {
	if(rePostReq.okReturn == null || rePostReq.okReturn == true){
		var ajaxObj = rePostReq.ajaxObj;
		var id = rePostReq.dialogId;
		ajaxObj.addParam(id + "interactflag", "true");
		ajaxObj.post();
	}
	rePostReq.okReturn = null;
};

function rePostMiddle() {
	if(rePostReq.okReturn == null || rePostReq.okReturn == true){
		var ajaxObj = rePostReq.ajaxObj;
		var id = rePostReq.dialogId;
		ajaxObj.addParam(id + "interactflag", "middle");
		ajaxObj.post();
	}
	rePostReq.okReturn = null;
};

/**
 * 点CANCEL后执行方法
 */
function rePostCancel() {
	var ajaxObj = rePostReq.ajaxObj;
	var id = rePostReq.dialogId;
	ajaxObj.addParam(id + "interactflag", "false");
	ajaxObj.post();
};

/**
 * @private
 */
function rePostReq() {
	var ajaxObj = rePostReq.ajaxObj;
	if (rePostReq.inputDlg) {
		var dlg = rePostReq.inputDlg;
		var itemsMap = dlg.getItems();
		var resultStr = "";
		
		var keySet = itemsMap.keySet();
		for(var i = 0, count = keySet.length; i < count; i++)
		{
			var inputId = keySet[i];
			var inputComp = itemsMap.get(inputId);
			resultStr += inputId + "=" + inputComp.getValue();
			if(i != count - 1)
				resultStr += ",";
		}
		
		ajaxObj.addParam("interactresult", resultStr);
		rePostReq.inputDlg = null;
	}
	ajaxObj.post();
};

/**
 * 显示异常对话框
 */
function showExceptionDialog(friendMsg, errorMsg, stackMsg) {
	return showErrorDialog(friendMsg);
	/*
	// 如果当前框小到没法容纳错误框，则尝试在父框中显示
	if (document.body.clientWidth < 400 || document.body.clientHeight < 280) {
		if (typeof (parent.showExceptionDialog) != "undefined") {
			if (window.debugMode == "production")
				parent.showErrorDialog(friendMsg);
			else
				parent.showExceptionDialog(friendMsg, errorMsg, stackMsg);
		} else
			alert(friendMsg);
	} else {
		if (window.debugMode == "production")
			showErrorDialog(friendMsg);
		else
			ExceptionDialog.showDialog(friendMsg, errorMsg, stackMsg);
	}
	*/
};

/**
 * 隐藏异常对话框
 */
function hideExceptionDialog() {
	ExceptionDialog.hideDialog();
};

/**
 * 显示信息对话框
 * 
 * @param func 点击对话框确认按钮要执行的函数
 */
function showMessageDialog(msg, func, left, top) {
	require("messagedialog", function(){
		var dialog = MessageDialogComp.showDialog(msg);
		if (left != null) {
			var leftValue = parseInt(left);
			dialog.modaldialog.divdialog.style.left = leftValue;
		}
		if (top != null) {
			var topValue = parseInt(top);
			dialog.modaldialog.divdialog.style.top = topValue;
		}
	
		if (func != null)
			dialog.onclick = func;
	});
//	return dialog;
};

/**
 * 显示反馈信息对话框
 * @param title
 * @param width
 * @return
 */
function showMessage(title){
	require("messagecomp",function(){ MessageComp.showMessage("center",title,"0","30","")})
};

/**
 * 隐藏信息对话框
 */
function hideMessageDialog() {
	MessageDialogComp.hideDialog();
};

/**
 * 显示警告对话框
 */
function showWarningDialog(msg, func) {
	var dialog = WarningDialogComp.showDialog(msg);
	if (func != null)
		dialog.onclick = func;
	return dialog;
};

/**
 * 隐藏警告对话框
 */
function hideWarningDialog() {
	WarningDialogComp.hideDialog();
};

/**
 * 显示确认对话框
 */
function showConfirmDialog(msg, okFunc, cancelFunc, obj1, obj2, zIndex, okText, cancelText) {
	require("confirmdialog", function(){ConfirmDialogComp.showDialog(msg, okFunc, cancelFunc, obj1, obj2, null, null, okText, cancelText)});
};

/**
 * 隐藏确认对话框
 */
function hideConfirmDialog() {
	ConfirmDialogComp.hideDialog();
};

/**
 * 获得节点值
 */
function getNodeValue(node) {
	if (IS_IE)
		return node.text;
	var firstNode = node.firstChild;
	if(firstNode == null)
		return null;
	var nextSibling = firstNode.nextSibling;
	if(nextSibling == null)
		return firstNode.data;
	return nextSibling.data;
	//return node.textContent;
};

/**
 * 获取节点属性
 * @param node
 * @param attrName
 * @return
 */
function getNodeAttribute(node, attrName) {
	if (IS_IE)
		return node.getAttribute(attrName);
	var attrs = node.attributes;
	if (attrs == null)
		return null;
	for ( var i = 0; i < attrs.length; i++) {
		if (attrs[i].nodeName == attrName)
			return attrs[i].nodeValue;
	}
	return null;
};

/**
 * 根据类型获取子项
 * @param node
 * @param type
 * @param index
 * @return
 */
function getChildForType(node, type, index) {
	var nodes = node.childNodes;
	if (nodes == null)
		return null;
	var count = -1;
	for ( var i = 0; i < nodes.length; i++) {
		if (nodes[i].nodeName != null && nodes[i].nodeName.toLowerCase() == type) {
			count++;
			if (count == index)
				return nodes[i];
		}
	}
	return null;
};

/**
 * 从数组中删除子项
 * @param arr
 * @param ele
 * @return
 */
function removeFromArray(arr, ele) {
	if (!arr)
		return false;
	for ( var i = 0; i < arr.length; i++) {
		if (arr[i] == ele) {
			arr.splice(i, 1);
			return true;
		}
	}
	return false;
};

/**
 * 提交表单
 * @param formName
 * @param actionUrl
 * @param target
 * @param method
 * @return
 */
function submitForm(formName, actionUrl, target, method) {
	var form = document[formName];
	form.action = actionUrl;
	form.target = getString(target, '_self');
	form.method = getString(method, 'post');
	// if(form.o )
	// if(!form.onsubmit())
	// return;
	form.submit();
};

/**
 * 显示状态信息
 * @param msg
 * @return
 */
function showStatusMsg(msg) {
	window.status = msg;
};

/**
 * 根据key获取用户配置属性
 */
function getConfigAttribute(key) {
	var value = getConfigFromCookieById(key);
	if (value != null)
		return value;
	else
		return getSessionAttribute(key);
};

/**
 * 从Cookie中获取配置信息
 * @param key
 * @return
 */
function getConfigFromCookieById(key) {
	var allCookie = document.cookie;
	var pos = allCookie.indexOf("LFW_CONFIG_KEY=");
	if (pos != -1) {
		var start = pos + 15;
		var end = allCookie.indexOf(";", start);
		if (end == -1)
			end = allCookie.length;
		var value = allCookie.substring(start, end);
		var v = value.split("$");
		if (key == "connectServerCycle")
			return v[0];
		else if (key == "theme")
			return v[1];
		else if (key == "openNodeMode")
			return v[2];
		else if (key == "noticeRefreshCycle")
			return v[3];
		else if (key == "jobRefreshCycle")
			return v[4];
	} else
		return null;
};

/**
 * document对象有个cookie特性，是包含给定页面所有可访问cookie的字符串。Cookie特性也很特别，因为将这个cookie特性
 * 设置为新值只会警告对页面可访问的cookie，并不会真正改变cookie(特性)本身。即使制定了cookie的其他特性，如失效时间，
 * document.cookie也只是返回每个cookie的名称和值，并用分号来分隔这些cookie
 */

/**
 * 设置Cookie
 * @param sName cookie名称,cookie名称本来不区分大小写，但最好认为区分
 * @param sValue 保存在cookie中的值，这个值在存储之前必须用encodeURIComponent编码,以免丢失数据或占用了Cookie.
 *               名称和值加起来不能超过4095字节,4K
 * @param sDomain 域，出于安全考虑，网站不能访问由其他域创建的cookie。创建cookie后，域的信息会作为cookie的一部分
 *                存储起来，不过，虽然不常见，但还是可以覆盖这个设置以允许另一个网站访问这个cookie
 * @param sPath 路径，另一个cookie的安全特征，路径限制了对Web服务器上的特定目录的访问。
 * @param{Object} oExpires Date对象
 * @param bSecure 一个true/false值，用于表示cookie是否只能从安全网站(使用SSL和https协议的网站)中访问。可将这个值设为
 *                true以提供加强的保护，进而确保cookie不被其他网站访问
 */
function setCookie(sName, sValue, oExpires, sPath, sDomain, bSecure) {
	var sCookie = sName + "=" + encodeURIComponent(sValue);
	if (oExpires)
		sCookie += "; expires=" + oExpires.toGMTString();
	if (sPath)
		sCookie += "; path=" + sPath;
	if (sDomain)
		sCookie += "; domain=" + sDomain;
	if (bSecure)
		sCookie += "; secure=" + bSecure;
	document.cookie = sCookie;
};

/**
 * 获取Cookie
 * @param sName
 * @return
 */
function getCookie(sName) {
	var sRE = "(?:; )?" + sName + "=([^;]*);?";
	var oRE = new RegExp(sRE);

	if (oRE.test(document.cookie)) {
		return decodeURIComponent(RegExp["$1"]);
	} else
		return null;
};

/**
 * 删除Cookie
 * @param sName
 * @param sPath
 * @param sDomain
 * @return
 */
function deleteCookie(sName, sPath, sDomain) {
	setCookie(sName, "", new Date(0), sPath, sDomain);
}

/**
 * 上传成功后执行方法
 * @param data
 * @param targetComp
 * @return
 * @private
 */
function uploadSuccess(data, targetComp) {
	// alert("上传成功");
	var comp = getComponent(targetComp);
	comp.onUploaded(data);
};

/**
 * @private
 */
function correctPNG() {
	for ( var i = 0; i < document.images.length; i++) {
		var img = document.images[i];
		var imgName = img.src.toUpperCase();
		if (imgName.length > 3
				&& imgName.substring(imgName.length - 3, imgName.length) == "PNG") {
			var imgID = (img.id) ? "id='" + img.id + "' " : "";
			var imgClass = (img.className) ? "class='" + img.className + "' "
					: "";
			var imgTitle = (img.title) ? "title='" + img.title + "' "
					: "title='" + img.alt + "' ";
			var imgStyle = "display:inline-block;" + img.style.cssText;
			if (img.align == "left")
				imgStyle = "float:left;" + imgStyle;
			if (img.align == "right")
				imgStyle = "float:right;" + imgStyle;
			if (img.parentElement.href)
				imgStyle = "cursor:hand;" + imgStyle;
			var strNewHTML = "<span "
					+ imgID
					+ imgClass
					+ imgTitle
					+ " style=\""
					+ "width:"
					+ img.width
					+ "px; height:"
					+ img.height
					+ "px;"
					+ imgStyle
					+ ";"
					+ "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"
					+ "(src=\'" + img.src
					+ "\', sizingMethod='scale');\"></span>";
			img.outerHTML = strNewHTML;
			i = i - 1;
		}
	}
};

/**
 * 从缓存中获取内容
 * @param key
 * @return
 */
function getFromCache(key) {
	return window.globalObject[key];
};

/**
 * 向缓存中存入内容
 * @param key
 * @param value
 * @return
 */
function putToCache(key, value) {
	window.globalObject[key] = value;
};

/**
 * 获取所有Dataset实例
 */
function getAllDatasets() {
	var dsArr = new Array;
	var widgets = pageUI.getWidgets();
	for (var i = 0, n = widgets.length; i < n; i++) {
		var dss = widgets[i].getDatasets();
		for (var j = 0, m = dss.length; j < m; j++) {
			dsArr.push(dss[j]);
		}
	}
	return dsArr;
};

/**
 * 从页面删除一个控件并销毁所占资源
 */
function removeComponent(compId) {
	var comp = window["$c_" + compId];
	if (comp) {
		comp.destroySelf();
		window["$c_" + compId] = null;
	}
};

/**
 * 从删除页面所有控件并销毁所占资源
 */
function removeAllComponent() {
	for (var i in window.objects) {
		var comp = window.objects[i];
		if (comp && comp.destroySelf) {
			comp.destroySelf();
		}
	}
	var dss = getAllDatasets();
	for (var i = 0; i < dss.length; i ++) {
		dss[i].destroySelf();
	}
	if (window.billUI != null) {
		window.billUI.billController = null;
		window.billUI.eventHandler = null;
	}
	
	removeAllChildNodes(document.body);
	clearNodeProperties(document.body);
	
	for (var i in window) {
		try {
			if (i == 'onunload' || i == 'onbeforeunload' || i == 'location' || i == 'status')
				continue;
			//TODO 有问题！！！
//			window[i] = null;
		}
		catch(error) {
		}
	}
//	delete window.objects;
};

/**
 * 删除事件对象（用于清除依赖关系）
 * @param event
 * @return
 */
function clearEvent(event) {
	try {
	    for (var i in event) {
	    	if (i == "type" || i == "eventPhase" || i == "bubbles"
//		    	|| i == "originalTarget"
//	    		|| i == "target"
//				|| i == "currentTarget" 
//				|| i == "relatedTarget" 
//				|| i == "fromElement" 
//				|| i == "srcElement" 
//				|| i == "toElement" 
				|| i == "cancelable" || i == "timeStamp" || i == "which"
				|| i == "rangeParent" || i == "rangeOffset" || i == "pageX"
				|| i == "pageY" || i == "isChar" || i == "getPreventDefault"
				|| i == "screenX" || i == "screenY" || i == "clientX"
				|| i == "clientY" || i == "ctrlKey" || i == "shiftKey"
				|| i == "altKey" || i == "metaKey" || i == "button"
				|| i == "initMouseEvent"
				|| i == "stopPropagation" || i == "preventDefault"
				|| i == "initEvent" || i == "view" || i == "detail"
				|| i == "initUIEvent" || i == "CAPTURING_PHASE" || i == "AT_TARGET"
				|| i == "BUBBLING_PHASE" || i == "mozPressure"
				|| i == "initNSMouseEvent" || i == "explicitOriginalTarget"
				|| i == "preventBubble" || i == "preventCapture"
				|| i == "isTrusted" || i == "layerX" || i == "layerY"
				|| i == "cancelBubble" || i == "MOUSEDOWN" || i == "MOUSEUP"
				|| i == "MOUSEOVER" || i == "MOUSEOUT" || i == "MOUSEMOVE"
				|| i == "MOUSEDRAG" || i == "CLICK" || i == "DBLCLICK"
				|| i == "KEYDOWN" || i == "KEYUP" || i == "KEYPRESS"
				|| i == "DRAGDROP" || i == "FOCUS" || i == "BLUR" || i == "SELECT"
				|| i == "CHANGE" || i == "RESET" || i == "SUBMIT" || i == "SCROLL"
				|| i == "LOAD" || i == "UNLOAD" || i == "XFER_DONE" || i == "ABORT"
				|| i == "ERROR" || i == "LOCATE" || i == "MOVE" || i == "RESIZE"
				|| i == "FORWARD" || i == "HELP" || i == "BACK" || i == "TEXT"
				|| i == "ALT_MASK" || i == "CONTROL_MASK" || i == "SHIFT_MASK"
				|| i == "META_MASK" || i == "SCROLL_PAGE_UP"
				|| i == "SCROLL_PAGE_DOWN") {
	    		continue;
			}
			try {
				event[i] = null;
			}
			catch (error) {
				
			}
	    }
	}
	catch (error) {
		
	}
	event = null;
};

/**
 * 删除无自定义属性的事件对象（用于清除依赖关系）
 * @param event
 * @return
 */
function clearEventSimply(event) {
	if(event){
		if (IS_IE) {
			if (event.originalTarget)
				event.originalTarget = null;
			if (event.target)
				event.target = null;
			if (event.currentTarget)
				event.currentTarget = null;
			if (event.relatedTarget)
				event.relatedTarget = null;
			try {
				event.fromElement = null;
			} catch (error) {
				
			}
			try {
				event.toElement = null;
			} catch (error) {
				
			}
			try {
				event.srcElement = null;
			} catch (error) {
				
			}
		}
		event = null;
	}
};

/**
 * 删除对象属性
 * @param objHtml
 * @return
 */
function clearNodeProperties(objHtml) {
	//TODO 执行太慢！
//	try {
//	    for (var i in objHtml) {
//			try {
//				if (i == 'outerText') {
//					objHtml[i] = '';
//					continue;
//				}
//				objHtml[i] = null;
//			}
//			catch (error) {
//				
//			}
//	    }
//	}
//	catch(error) {
//		
//	}
};

/**
 * 删除所有子节点
 * @param obj
 * @return
 */
function removeAllChildNodes(obj) {
	//TODO 此方法好像没什么作用，而且会增加溢出！
//	if (obj && obj.childNodes) {
//		for (var i = obj.childNodes.length - 1 ; i >= 0; i--) {
//			var tempNode = obj.childNodes[i];
//			if (tempNode.childNodes && tempNode.childNodes.length > 0) {
//				removeAllChildNodes(tempNode);
//			}
//			// 删除子节点（此方法会引发内存溢出）
//			obj.removeChild(tempNode);
//			clearNodeProperties(tempNode);
//			if (tempNode) {
//				try {
//					delete tempNode;
//					tempNode = null;
//				} catch(e) {
//					alert("清除子节点出错！");
//				}
//			}
//		}
//	}
};
/**
 * 注册样式表
 * @param {} cssString
 */
function addCssByStyle(cssString){
    var doc=document;  
    var style=doc.createElement("style");
    style.setAttribute("type", "text/css");
    if(style.styleSheet){// IE
        style.styleSheet.cssText = cssString;
    } else {// w3c
        var cssText = doc.createTextNode(cssString);  
         style.appendChild(cssText);  
     }  
     var heads = doc.getElementsByTagName("head");  
     if(heads.length)
         heads[0].appendChild(style);  
     else 
         doc.documentElement.appendChild(style);  
 } 
/**
 * 调整容器Frame高度
 */
function adjustContainerFramesHeight(){
	if(document._pt_frame_id){
		try {
			var frame =	parent.getParentsContainer(document._pt_frame_id);
			parent.adjustIFramesHeightOnLoad(frame);
		} catch (e) {
		}
	}else if(parent.document._pt_frame_id){
			try {
				var frame =	parent.parent.getParentsContainer(parent.document._pt_frame_id);
				parent.parent.adjustIFramesHeightOnLoad(frame);
			} catch (e) {
			}
	}else if(window.opener){
		if(window.opener.document._pt_frame_id){
			try {
				var frame =	window.opener.parent.getParentsContainer(window.opener.document._pt_frame_id);
				window.opener.parent.adjustIFramesHeightOnLoad(frame);
			} catch (e) {
			}
		}
	}
}