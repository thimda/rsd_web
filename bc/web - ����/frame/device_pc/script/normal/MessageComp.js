/**
 * 
 * 提示信息控件
 * 
 * @author guoweic
 * @version NC6.0
 * 
 */
MessageComp.prototype = new BaseComponent;
MessageComp.prototype.componentType = "MESSAGE";

MessageComp.DEFAULT_X = 30;
MessageComp.DEFAULT_Y = 30;

/**
 * 提示信息控件构造方法
 * 
 * @class 提示信息控件
 * 
 * @param name 名称
 * @param left
 * @param top
 * @param width 宽度
 * @param height 高度
 * @param text 显示文字
 * @param showPosition 显示位置类型 ("top-left","top-center","top-right","center","bottom-left","bottom-center","bottom-right")
 * @param x 横向偏差
 * @param y 纵向偏差
 * @param className 类名
 */
function MessageComp(name, left, top, width, text, showPosition, x, y, className) {
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.className = getString(className, "message_div");
	height = getCssHeight(this.className + "_DEFAULT_HEIGHT");
	this.base(name, left, top, width, height);
	this.parentOwner = document.body;
	this.text = text;
	this.showPosition = showPosition;
	this.x = x;
	this.y = y;
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	this.zIndex = 999999;
	this.zIndex = getZIndex();
	this.create();
};

/**
 * @private
 */
MessageComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.display = "none";
	this.Div_gen.style.zIndex = this.zIndex;
	
	// 设置显示大小和位置
	this.setStyle();

	// 显示文字
	this.textDiv = $ce("DIV");
	this.textDiv.className = "message_text_div";
	this.textDiv.innerHTML = this.text;
	if (this.text != null) {
		this.Div_gen.appendChild(this.textDiv);
	}

	// 关闭按钮
	this.closeImg = $ce("IMG");
	this.closeImg.src =  window.themePath+"/images/message/message_close.png";
	this.closeImg.className = "message_close_img";
	this.Div_gen.appendChild(this.closeImg);
	this.closeImg.onclick = function() {
		oThis.hideMsg();
	};

	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 设置显示大小和位置
 * @private
 */
MessageComp.prototype.setStyle = function() {
	this.Div_gen.className = this.className;
	this.Div_gen.style.filter = "alpha(opacity=0)";
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";
	var left = 0;
	var top = 0;
	// 显示位置
	if (this.showPosition == "top-left") {
		left = MessageComp.DEFAULT_X + (this.x == null ? 0 : getInteger(this.x));
		top = MessageComp.DEFAULT_Y + (this.y == null ? 0 : getInteger(this.y));
	} else if (this.showPosition == "top-center") {
		left = (screen.width - this.width - (this.x == null ? 0 : getInteger(this.x)))/2;
		top = MessageComp.DEFAULT_Y + (this.y == null ? 0 : getInteger(this.y));
	} else if (this.showPosition == "top-right") {
		left = screen.width - this.width - MessageComp.DEFAULT_X - (this.x == null ? 0 : getInteger(this.x));
		top = MessageComp.DEFAULT_Y + (this.y == null ? 0 : getInteger(this.y));
	} else if (this.showPosition == "center") {
		left = (screen.width - this.width - (this.x == null ? 0 : getInteger(this.x)))/2;
		top = (screen.height - this.height - MessageComp.DEFAULT_Y - (this.y == null ? 0 : getInteger(this.y)))/2;
	} else if (this.showPosition == "bottom-left") {
		left = MessageComp.DEFAULT_X + (this.x == null ? 0 : getInteger(this.x));
		top = screen.height - this.height - MessageComp.DEFAULT_Y - (this.y == null ? 0 : getInteger(this.y));
	} else if (this.showPosition == "bottom-center") {
		left = (screen.width - this.width - (this.x == null ? 0 : getInteger(this.x)))/2;
		top = screen.height - this.height - MessageComp.DEFAULT_Y - (this.y == null ? 0 : getInteger(this.y));
	} else { // bottom-right
		left = screen.width - this.width - MessageComp.DEFAULT_X - (this.x == null ? 0 : getInteger(this.x));
		top = screen.height - this.height - MessageComp.DEFAULT_Y - (this.y == null ? 0 : getInteger(this.y));
	}
	this.Div_gen.style.left = left + "px";
	this.Div_gen.style.top = top + "px";
};

/**
 * 设置显示文字
 * @private
 */
MessageComp.prototype.setText = function(text) {
	this.text = text;
	if (text != null)
		this.textDiv.innerHTML = this.text;
};

/**
 * 设置透明度
 * @private
 */
function setOpacity(obj, value) {
	if (document.all) {
		if (value == 100) {
			obj.style.filter = "";
		} else {
			obj.style.filter = "alpha(opacity=" + value + ")";
		}
	} else {
		obj.style.opacity = value / 100;
	}
};
/**
 * 用setTimeout循环减少透明度
 * @private
 */
MessageComp.prototype.changeOpacity = function(startValue, endValue, step, speed) {
	var oThis = this;
	if (this.Div_gen.style.display == "none") {
		this.Div_gen.style.display = "block";
	}
	if (startValue <= 0) {  // 隐藏控件
		this.Div_gen.style.display = "none";
		return;
	}
	setOpacity(this.Div_gen, startValue);
	if (this.timeoutFunc)
		clearTimeout(this.timeoutFunc);
	this.timeoutFunc = setTimeout( function() {
		oThis.changeOpacity(startValue - step, endValue, step, speed);
	}, speed);
};

/**
 * 显示提示信息（先显示后隐藏）
 * @private
 */
MessageComp.prototype.showMsg = function() {
	this.step = 5;
	this.speed = 200;
	this.changeOpacity(100, 0, this.step, this.speed);
};

/**
 * 直接隐藏提示信息
 * @private
 */
MessageComp.prototype.hideMsg = function() {
	if (this.timeoutFunc)
		clearTimeout(this.timeoutFunc);
	this.Div_gen.style.display = "none"
};

/**
 * 显示提示信息（外部调用）
 */
MessageComp.showMessage = function(showPosition, text, width, x, y, className) {
	if (!window.$_messageComp) {
		window.$_messageComp = new MessageComp(name, 0, 0, width, text, 
				showPosition, x, y, className);
	} else {
		window.$_messageComp.showPosition = showPosition;
		window.$_messageComp.width = width;
		window.$_messageComp.x = x;
		window.$_messageComp.y = y;
		window.$_messageComp.className = getString(className, window.$_messageComp.className);
		window.$_messageComp.setStyle();
		window.$_messageComp.setText(text);
	}
	window.$_messageComp.showMsg();
};

/**
 * @private
 */
MessageComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "";
	context.c = "";
	context.id = this.id;
	return context;
};

/**
 * @private
 */
MessageComp.prototype.setContext = function(context) {

};
