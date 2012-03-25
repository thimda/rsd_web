window.clickHolders = new Array();
// guoweic: add start 2009-11-10
if (window.globalObject == null)
	window.globalObject = new Object;
// guoweic: add end

SHIFT_MASK = 1 << 0;
CTRL_MASK = 1 << 1;
ALT_MASK = 1 << 3;

/*******************************************************************************
 * 
 * @auther dengjt,gd
 * 
 ******************************************************************************/

/**
 * 右键点击事件
 * @private
 */
document.oncontextmenu = function() {
	for ( var i = 0; i < window.clickHolders.length; i++) {
		if (window.clickHolders[i].outsideContextMenuClick)
			window.clickHolders[i].outsideContextMenuClick();
	}
	if (window.debugMode == 'debug')
		return true;
	return false;
};

/**
 * 左键点击事件
 * @private
 */
document.onclick = function(e) {
	//for iframe
//	try{
////		if(parent != window)
//		parent.document.onclick(e);
//	}
//	catch(e){
//	}
	e = EventUtil.getEvent();
	for ( var i = 0; i < window.clickHolders.length; i++) {
		if (window.clickHolders[i].outsideClick)
			window.clickHolders[i].outsideClick(e);
	}
	window.clickHolders.trigger = null;
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
	//return true;
};

/**
 * 鼠标覆盖事件
 * @private
 */
document.onmouseover = function() {
	for ( var i = 0; i < window.clickHolders.length; i++) {
		if (window.clickHolders[i].outsideOver)
			window.clickHolders[i].outsideOver();
	}
};

/**
 * 滚轮事件处理方法
 * @param e
 * @return
 * @private
 */
function documentMouseWheelFunc(e) {
	for ( var i = 0; i < window.clickHolders.length; i++) {
		if (window.clickHolders[i].outsideMouseWheelClick)
			window.clickHolders[i].outsideMouseWheelClick(e);
	}
};

/**
 * 添加滚轮事件
 * @private
 */
if (document.attachEvent) {  // For IE
	document.attachEvent("onmousewheel", documentMouseWheelFunc); 
} else {  // For Firefox
	window.addEventListener("DOMMouseScroll", documentMouseWheelFunc, false); 
}

/**
 * 屏蔽浏览器默认的一些快捷键操作,避免和自定义的快捷键操作冲突
 * @private
 */
document.onkeydown = function(e) {
	if (!e)
		e = window.event;
	var keyCode = e.keyCode ? e.keyCode : e.charCode ? e.charCode
			: e.which ? e.which : void 0;
	/**
	 * 屏蔽浏览器默认快捷方式
	 */
	// 屏蔽 Ctrl+n
	if ((e.ctrlKey) && (keyCode == 78)) {
		stopDefault(e);
		return false;
	}
	// 屏蔽 shift+F10
	else if ((e.shiftKey) && (keyCode == 121)) {
		stopDefault(e);
		return false;
	}
	// 屏蔽Ctrl+B
	else if ((e.ctrlKey) && (keyCode == 66)) {
		stopDefault(e);
		return false;
	}
	// 屏蔽Ctrl+E
	else if ((e.ctrlKey) && (keyCode == 69)) {
		stopDefault(e);
	}
	// 屏蔽Ctrl+R(重新载入)
	else if ((e.ctrlKey) && (keyCode == 82)) {
		stopDefault(e);
		return false;
	}
	// 屏蔽Ctrl+A
	else if ((e.ctrlKey) && (keyCode == 65)) {
		stopDefault(e);
		return false;
	}
	// 屏蔽Ctrl+P
	else if (keyCode == 80 && e.ctrlKey) {
		e.keyCode = 0;
		stopDefault(e);
	}
	// 屏蔽Ctrl+L
	else if (keyCode == 76 && e.ctrlKey) {
		stopDefault(e);
		return false;
	}
	// 屏蔽Ctrl+I
	else if (keyCode == 73 && e.ctrlKey) {
		stopDefault(e);
	}
	// 屏蔽Ctrl+C
	else if (e.ctrlKey && keyCode == 67) {
		// stopDefault(e);
		// return false;
	}
	// 屏蔽Ctrl+V
	else if (e.ctrlKey && keyCode == 86) {
		// stopDefault(e);
		// return false;
	}
	// 屏蔽Ctrl+D
	else if (e.ctrlKey && keyCode == 68) {
		stopDefault(e);
		return false;
	}
	// 屏蔽Alt+Z
	else if (e.altKey && keyCode == 90) {
		stopDefault(e);
		return false;
	} else if (keyCode == 114) {
		e.keyCode = 505;
		stopDefault(e);
		return false;
	} else if (keyCode == 8) {
		if (e.srcElement && e.srcElement.tagName != "INPUT"
				&& e.srcElement.tagName != "TEXTAREA") {
			stopAll(e);
			return false;
		}
	}
	return true;
};

/**
 * 按键抬起事件
 * @private
 */
document.onkeyup = function(e) {
	return $pageKeyUpFunc(e);
};

/**
 * 键盘点击事件执行方法
 * @private
 */
function $pageKeyUpFunc(e) {
	if (!e)
		e = window.event;
	var keyCode = e.keyCode ? e.keyCode : e.charCode ? e.charCode
			: e.which ? e.which : void 0;

	// 处理自定义快捷键操作
	if (keyCode == 68 && e.ctrlKey && e.shiftKey) {
		// 调试模式才启动调试监测器
		if (window.debugMode != "production")
			showDebugDialog();
	}

	// 检索快捷键
	if (ButtonManager.getInstance().hotKeyMap != null) {
		// 修饰符
		var modifier = null;
		// 获取当前的修饰mask,暂时只允许为Ctrl,Alt,Shift
		if (e.ctrlKey)
			modifier = CTRL_MASK;
		if (e.shiftKey) {
			if (modifier == null)
				modifier = SHIFT_MASK;
			else
				modifier = modifier | SHIFT_MASK;
		}
		if (e.altKey) {
			if (modifier == null)
				modifier = ALT_MASK;
			else
				modifier = modifier | ALT_MASK;
		}

		// 快捷键
		var hotKey = String.fromCharCode(keyCode);
		if (keyCode == 114)
			hotKey = "F3";
		else if (keyCode == 46)
			hotKey = "DEL";
		else if (keyCode == 34)
			hotKey = "PAGE_DOWN";
		else if (keyCode == 33)
			hotKey = "PAGE_UP";
		else if (keyCode == 36)
			hotKey = "HOME";
		else if (keyCode == 35)
			hotKey = "END";

		if (hotKey != null) {
			var obj = null;
			
			if (modifier != null)
				obj = pageUI.handleHotKey(hotKey + modifier);
			else
				obj = pageUI.handleHotKey(hotKey);

			stopDefault(e);
			stopEvent(e);
		}
	}

	// 调用父页面键盘点击事件方法
	if (window.top != window) {
		var parentPage = parent;
		while (parentPage && !parentPage.$pageKeyUpFunc){
			if(parentPage == parentPage.parent)
				break;
			parentPage = parentPage.parent;
		}
		if (parentPage && parentPage.$pageKeyUpFunc)
			return parentPage.$pageKeyUpFunc(e);
	}
	
	return true;
	
};

/**
 * 页面关闭事件
 */
window.onbeforeunload = function() {
	if(pageUI.showCloseConfirm())
		if(IS_IE)
			;
		else
			removeAllComponent();
	else
		return false;
};



