/**
 *	@fileoverview 事件处理方法
 *	@author guoweic
 *	@version 
 */
 
/**
 * 事件工具
 * @class 事件工具
 */
var EventUtil = new Object;

/**
 * 当前时间对象
 * @public
 */
EventUtil.currentEvent = null;

/**
 * 增加事件
 */
EventUtil.addEventHandler = function(oTarget, sEventType, fnHandler) {
	if (oTarget.addEventListener) {  // 用于支持DOM的浏览器
		oTarget.addEventListener(sEventType, fnHandler, true);
	} else if (oTarget.attachEvent) {  // 用于IE浏览器
		oTarget.attachEvent("on" + sEventType, fnHandler);
	} else {  // 用于其它浏览器
		oTarget["on" + sEventType] = fnHandler;
	}
};

/**
 * 移除事件
 */
EventUtil.removeEventHandler = function(oTarget, sEventType, fnHandler) {
	if (oTarget.removeEventListener) {  // 用于支持DOM的浏览器
		oTarget.removeEventListener(sEventType, fnHandler, false);
	} else if (oTarget.detachEvent) {  // 用于IE浏览器
		oTarget.detachEvent("on" + sEventType, fnHandler);
	} else {  // 用于其它浏览器
		oTarget["on" + sEventType] = null;
	}
};

/**
 * 格式化Event对象，使IE的event事件模型接近于DOM事件模型
 */
EventUtil.formatEvent = function(oEvent) {
	if (IS_IE) {  
		oEvent.eventPhase = 2;
		oEvent.isChar = (oEvent.charCode > 0);
		oEvent.pageX = oEvent.clientX + document.body.scrollLeft;
		oEvent.pageY = oEvent.clientY + document.body.scrollTop;
		if (oEvent.type == "mouseout") {
			oEvent.relatedTarget = oEvent.toElement;
		} else if (oEvent.type == "mouseover") {
			oEvent.relatedTarget = oEvent.fromElement;
		}
		
		oEvent.preventDefault = function() {  // 在Event.js中有类似方法
			this.returnValue = false;
		};
		
		oEvent.stopPropagation = function() {  // 在Event.js中有类似方法
			this.cancelBubble = true;
		};
		
		oEvent.target = oEvent.srcElement;
		oEvent.time = (new Date()).getTime();
	}
	return oEvent;
};

/**
 * 获取事件对象
 */
EventUtil.getEvent = function() {
	var currentEvent;
	if (window.event) {  // 如果是IE浏览器
		currentEvent = this.formatEvent(window.event);
	} 
	else {  // 如果是其他浏览器
		currentEvent = EventUtil.getEvent.caller.arguments[0];
	}
	if(currentEvent != null && currentEvent.type.startWith("key")){
		currentEvent.key = currentEvent.keyCode ? currentEvent.keyCode : currentEvent.charCode ? currentEvent.charCode : currentEvent.which ? currentEvent.which : void 0;
	}
	EventUtil.currentEvent = currentEvent;
	return currentEvent;
};



