/**
 * @fileoverview 此控件是对html textarea的封装,可以设置rows和cols
 * 
 * @author gd, guoweic
 * @version 6.0
 * @since 5.5 
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */

TextAreaComp.prototype = new BaseComponent;
TextAreaComp.prototype.componentType = "TEXTAREA";

/**
 * TextArea控件的构造函数
 * @class TextArea控件
 * @param rows 该元素有多少行高
 * @param cols 列宽(以字符记)
 * @param readOnly 若为true,则用户不能编辑任何显示的文本
 */
function TextAreaComp(parent, name, left, top, rows, cols, position, readOnly, value, width, height, tip, className) {
	if (arguments.length == 0)
		return;
		
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.value = getString(value, "");
	this.rows = rows;
	this.cols = cols;
	this.position = getString(position, "absolute");
	this.readOnly = getBoolean(readOnly, false);
	this.disabled = false;
	this.tip = getString(tip, "");
	this.className = getString(className, "text_div");
	this.parentOwner = parent;
	
	this.create();
};

/**
 * 创建textarea
 * @private
 */
TextAreaComp.prototype.create = function() {	
	var oThis = this;
	//创建显示div对象
	this.Div_gen = $ce("div");
	this.Div_gen.className = this.className;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	if (this.position != "relative")
		this.Div_gen.style.position = this.position;
	if (this.height.toString().indexOf("%") == -1)
		this.Div_gen.style.height = this.height + "px";
	else
		this.Div_gen.style.height = this.height;
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	this.Div_gen.style.borderWidth = "0px";
	this.Div_gen.style.overflow = "hidden";
	
	//创建textarea对象
	this.textArea = $ce("textarea");
	if (this.cols)
		this.textArea.cols = this.cols;
	else
		if (this.width.toString().indexOf("%") == -1)
			this.textArea.style.width = (this.width - 4) + "px";
		else
			this.textArea.style.width = "100%";

	if (this.rows)
		this.textArea.rows = this.rows;
	else
		if (this.height.toString().indexOf("%") == -1)
			this.textArea.style.height = (this.height - 4) + "px";
		else
			this.textArea.style.height = "99%";
	
	this.textArea.className = "text_area";
	
	if (this.readOnly == true)
		this.setReadOnly(this.readOnly);

	//设置初始值
	if (this.value != null)
		this.setValue(this.value);
	else
		this.showTip();
	
	this.Div_gen.appendChild(this.textArea);
	
	//获得键盘焦点时调用
	this.textArea.onfocus = function (e) {	 
		e = EventUtil.getEvent();
		oThis.oldValue = oThis.getValue();
		oThis.onfocus(e);
		oThis.hideTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//失去键盘焦点时调用
	this.textArea.onblur = function (e) {
		e = EventUtil.getEvent();
		oThis.newValue = oThis.getValue();
		this.value = oThis.newValue;
		if (oThis.newValue != oThis.oldValue)
			oThis.valueChanged(oThis.oldValue, oThis.newValue);
		oThis.onblur(e);
		oThis.showTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.textArea.onclick = function(e) {
		e = EventUtil.getEvent();
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.textArea.onkeydown = function(e) {
		// readOnly时不允许输入
		if(this.readOnly == true)
			return false;
				
		e = EventUtil.getEvent();
		// 调用用户的方法
		oThis.onkeydown(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.textArea.onselect = function(e) {	
		e = EventUtil.getEvent();
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.textArea.onselectstart = function(e) {
		e = EventUtil.getEvent();
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	
	
	if (this.parentOwner) 
		this.placeIn(this.parentOwner);
	
//	if (!this.rows && (IS_IE6 || IS_IE7))
//		this.textArea.style.height = this.Div_gen.offsetHeight - 5 + "px";
//	
};

/**
 * @private
 */
TextAreaComp.prototype.setError = function() {
	
};

/**
 * 校验是否有默认提示信息
 * @private
 */
TextAreaComp.prototype.checkTip = function() {
	if (this.tip != null && this.tip != "")
		return true;
	else
		return false;
};

/**
 * 显示提示信息
 * @private
 */
TextAreaComp.prototype.showTip = function() {
	if (this.checkTip()) {
		if (this.textArea.value == "") {
			this.textArea.value = this.tip;
			this.textArea.style.color = "gray";
		}
	}
};

/**
 * 隐藏提示信息
 * @private
 */
TextAreaComp.prototype.hideTip = function() {
	if (this.checkTip()) {
		if (this.textArea.value == this.tip) {
			this.textArea.value = "";
			this.textArea.style.color = "black";
		}
	}
};

/**
 * 设置大小及位置
 * @param width 像素大小
 * @param height 像素大小
 */
TextAreaComp.prototype.setBounds = function(left, top, width, height) {	
	// 改变数据对象的值
	this.left = left;
	this.top = top;
	this.width = getInteger(width, this.Div_gen.offsetWidth);
	this.height = getInteger(height, this.Div_gen.offsetHeight);
	// 改变显示对象的值
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";
	
//	if (!this.rows && (IS_IE6 || IS_IE7)) 
	this.textArea.style.height = this.height - 5 + "px";
	this.textArea.style.width = this.width - 5 + "px";
	
};

/**
 * 得到textarea中的文本内容
 */
TextAreaComp.prototype.getValue = function() {
	if (this.checkTip()) {
		if (this.textArea.value == this.tip && this.textArea.style.color != "black")
			return "";
	}
	return this.textArea.value;
};

/**
 * 设置textarea中的文本内容
 */
TextAreaComp.prototype.setValue = function(value) {
	this.oldValue = this.getValue();
	value = getString(value, "");
	this.textArea.value = value;
	if (this.checkTip()) {
		if (this.textArea.value == "")
			this.showTip();
		else
			this.textArea.style.color = "black";
	}
	this.newValue = value;
	if (this.newValue != this.oldValue)
		this.valueChanged(this.oldValue, this.newValue);
};

/**
 * 设置此TextArea控件的激活状态
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
TextAreaComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		this.textArea.disabled = true;
		this.disabled = true;
		this.textArea.className = "text_area inactive_bgcolor";
		this.Div_gen.className = this.className + " inactive_bgcolor";
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {	
		this.textArea.disabled = false;
		this.disabled = false;
		this.textArea.className = "text_area";
		this.Div_gen.className = this.className;
	}
};

/**
 * 设置只读状态
 */
TextAreaComp.prototype.setReadOnly = function(readOnly) {
//	if(this.readOnly==readOnly)
//		return;
//	if(this.disabled){
//		this.setActive(true);
//	}
	this.textArea.readOnly = readOnly;
	this.readOnly = readOnly;
	if (readOnly) {
		this.textArea.className = "text_area_readonly";
	} else {
		this.textArea.className = "text_area";
	}
};

/**
 * 设置聚焦
 */
TextAreaComp.prototype.setFocus = function() {
	if (IS_IE) {
		this.textArea.focus();
		this.textArea.select();
	} else {  // firefox等浏览器不能及时执行focus方法
		var oThis = this;
		window.setTimeout(function(){oThis.textArea.focus();oThis.textArea.select();}, 50); 
	}
	 
};

/**
 * 得到输入框的激活状态
 */
TextAreaComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * 聚焦事件
 * @private
 */
TextAreaComp.prototype.onfocus = function (e) {
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onFocus", FocusListener.listenerType, focusEvent);
};

/**
 * 焦点移出事件
 * @private
 */
TextAreaComp.prototype.onblur = function (e) {
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
};

/**
 * 按键事件
 * @private
 */
TextAreaComp.prototype.onkeydown = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onkeydown", KeyListener.listenerType, keyEvent);
};

/**
 * 值改变事件
 * @private
 */
TextAreaComp.prototype.valueChanged = function(oldValue, newValue) {
	var valueChangeEvent = {
			"obj" : this,
			"oldValue" : oldValue,
			"newValue" : newValue
		};
	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
};

/**
 * 获取对象信息
 * @private
 */
TextAreaComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.TextAreaContext";
	context.c = "TextAreaContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.readOnly = this.readOnly;
	context.value = this.getValue();
	context.visible = this.visible;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
TextAreaComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.readOnly != null && this.readOnly != context.readOnly)
		this.setReadOnly(context.readOnly);
	if (context.value && context.value != this.textArea.value)
		this.setValue(context.value);
//	if (context.visible != this.visible) {
		if (context.visible)
			this.showV();
		else
			this.hideV();
//	}
	if (context.focus)
		this.setFocus();
};