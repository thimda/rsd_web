/**
 * @fileoverview 表单中的自定义元素控件
 * 
 * @author guoweic
 * @version 6.0
 * 
 */

SelfDefElementComp.prototype = new BaseComponent;
SelfDefElementComp.prototype.componentType = "SELF_DEF_ELEMENT_COMP";

/**
 * 表单自定义项的构造函数
 * @class 表单自定义项
 */
function SelfDefElementComp(parent, name, left, top, width, height, position, attrArr, className) {
	if (arguments.length == 0)
		return;
		
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.position = getString(position, "absolute");
	this.disabled = false;
	// 真正的内容对象
	this.contentObj = null;
	this.className = className == null ? null : className;
	if (attrArr != null) {   
		this.tabIndex = getInteger(attrArr.tabIndex, 0);
	}	
	this.parentOwner = parent;
	this.create();
};

/**
 * @private
 */
SelfDefElementComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = this.height;
	this.Div_gen.style.position = this.position;
	if (this.className != null)
		this.Div_gen.className = this.className;
	// 设置tabindex属性，以便在Firefox下获取聚焦事件
	this.Div_gen.setAttribute("tabindex", this.tabIndex == null ? 0 : this.tabIndex);
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 设置大小及位置
 * @param width 像素大小
 * @param height 像素大小
 */
SelfDefElementComp.prototype.setBounds = function(left, top, width, height) {	
	// 改变数据对象的值
	this.left = left;
	this.top = top;
	this.width = getInteger(width, "100%");
	this.height = getInteger(height, "100%");
	// 改变显示对象的值
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";
	
	this.Div_gen.tabIndex = "";
	
	var oThis = this;

	//获得键盘焦点时调用
	this.Div_gen.onfocus = function (e) {	 
		e = EventUtil.getEvent();
		oThis.onfocus(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//失去键盘焦点时调用
	this.Div_gen.onblur = function (e) {
		e = EventUtil.getEvent();
		oThis.onblur(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.Div_gen.onkeydown = function(e) {
		e = EventUtil.getEvent();
		oThis.onkeydown(e);	
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	if (!this.rows && (IS_IE6 || IS_IE7)) 
		this.textArea.style.height = this.height - 5 + "px";
};

/**
 * 设置自定义内容的HTML描述字符串
 */
SelfDefElementComp.prototype.setInnerHTML = function(html) {
	if (html != null)
		this.Div_gen.innerHTML = html;
};

/**
 * 设置自定义内容的对象
 * 该对象必须实现：
 * 1、getMainDiv() 方法，返回最外层DIV
 * 2、getValue() 方法
 * 3、setValue() 方法
 * 4、setActive() 方法
 * 5、setActive() 方法
 * 6、getActive() 方法
 */
SelfDefElementComp.prototype.setContent = function(obj) {
	if (obj != null) {
		this.contentObj = obj;
		this.Div_gen.appendChild(obj.getMainDiv());
		this.contentObj.setActive(!this.disabled);
	}
};

/**
 * 获取真正的内容兑现
 */
SelfDefElementComp.prototype.getContent = function() {
	return this.contentObj;
};

/**
 * 得到控件的值
 */
SelfDefElementComp.prototype.getValue = function() {
	if (this.contentObj != null && contentObj.getValue)
		return contentObj.getValue();
	return null;
};

/**
 * 设置控件的值
 */
SelfDefElementComp.prototype.setValue = function(value) {
	if (this.contentObj != null && contentObj.setValue)
		contentObj.setValue(value);
};

/**
 * 设置此控件的激活状态
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
SelfDefElementComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == isActive) {
		this.disabled = !isActive;
		if (this.contentObj != null && this.contentObj.setActive) {
			this.contentObj.setActive(isActive);
		}
	}
};

/**
 * 得到此控件的激活状态
 */
SelfDefElementComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * 聚焦
 */
SelfDefElementComp.prototype.setFocus = function() {
	 this.Div_gen.focus();
};

/**
 * 聚焦事件
 * @private
 */
SelfDefElementComp.prototype.onfocus = function (e) {
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
SelfDefElementComp.prototype.onblur = function (e) {
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
SelfDefElementComp.prototype.onkeydown = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onkeydown", KeyListener.listenerType, keyEvent);
};

/**
 * 获取对象信息
 * @private
 */
SelfDefElementComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.SelfDefElementContext";
	context.c = "SelfDefElementContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.value = this.value;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
SelfDefElementComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.value && context.value != this.textArea.value)
		this.setValue(context.value);
	if (context.focus)
		this.setFocus();
};






