/**
 * @fileoverview Label组件
 *
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 */
LabelComp.prototype = new BaseComponent;
LabelComp.prototype.componentType = "LABEL";

/**
 * 标签控件
 * @class LabelComp控件的构造函数
 * @constructor LabelComp控件的构造函数
 */
function LabelComp(parent, name, left, top, text, position, className) {
	this.base = BaseComponent;
	this.base(name, left, top, "", "");
	
	this.text = text;
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.className = getString(className, "label_div");
	this.create();
};

/**
 * LabelComp控件的主体创建函数
 * @private
 */
LabelComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";		
//	this.Div_gen.style.width = this.getTextWidth() + "px";
	this.Div_gen.className = this.className;
	//TODO guoweic
	this.Div_gen.style.textOverflow = "ellipsis";	
	this.Div_gen.style.whiteSpace = "nowrap";
	this.Div_gen.style.cursor = "default";
	this.Div_gen.title = this.text;
 
	// 创建文本节点做为DIV_gen的子节点,内容为text指定的字符串	
	this.Div_gen.appendChild(document.createTextNode(this.text));
	
	// 将label组件添加到父组件中
	if (this.parentOwner) {
		this.placeIn(this.parentOwner);
	}
};

/**
 * LabelComp的二级回调函数
 * @private
 */
LabelComp.prototype.manageSelf = function() {
	var oThis = this;
	
	this.Div_gen.onmouseover = function(e) {
		e = EventUtil.getEvent();
		oThis.onmouseover(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.Div_gen.onmouseout = function(e) {
		e = EventUtil.getEvent();
		oThis.onmouseout(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
};

/**
 * 设置文字颜色
 */
LabelComp.prototype.setColor = function(color) {
	this.color = color;
	this.Div_gen.style.color = this.color;
};

/**
 * 设置文字样式（normal, italic, oblique）
 */
LabelComp.prototype.setStyle = function(style) {
	this.style = style;
	this.Div_gen.style.fontStyle = this.style;
};

/**
 * 设置文字字体粗细（normal, bold, bolder, lighter, 100-900）
 */
LabelComp.prototype.setWeight = function(weight) {
	this.weight = weight;
	this.Div_gen.style.fontWeight = this.weight;
};

/**
 * 设置文字字体大小
 */
LabelComp.prototype.setSize = function(size) {
	this.size = size;
	this.Div_gen.style.fontSize = this.size + "pt";
};

/**
 * 设置文字字体
 */
LabelComp.prototype.setFamily = function(family) {
	this.family = family;
	this.Div_gen.style.fontFamily = this.family;
};

/**
 * 得到文字的宽度
 */
LabelComp.prototype.getTextWidth = function() {
	if(this.text == null)
		this.width = 0;
	else
		this.width = this.text.length * 12;
	return this.width;
};

/**
 * 改变label组件的文字
 */
LabelComp.prototype.changeText = function(text) {
	this.Div_gen.removeChild(this.Div_gen.firstChild);
	this.text = text;
	// 重新计算宽度
//	this.Div_gen.style.width = this.getTextWidth() + "px";
	this.Div_gen.appendChild(document.createTextNode(this.text));
};

/**
 * 改变label组件的内容
 */
LabelComp.prototype.setInnerHTML = function(html) {
	this.Div_gen.innerHTML = html;
};

/**
 * 禁用LabelComp控件
 */
LabelComp.prototype.inactive = function() {
	this.Div_gen.className = "label_inactive";   
	this.disabled = true;
};

/**
 * 激活LabelComp控件
 */
LabelComp.prototype.active = function() {
   this.Div_gen.className = this.className;	
   this.disabled = false;
};

/**
 * 鼠标移入事件
 * @private
 */
LabelComp.prototype.onmouseover = function(e){
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseover", MouseListener.listenerType, mouseEvent);
};

/**
 * 鼠标移出事件
 * @private
 */
LabelComp.prototype.onmouseout = function(e){
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseout", MouseListener.listenerType, mouseEvent);
};

/**
 * 获取对象信息
 * @private
 */
LabelComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.LabelContext";
	context.c = "LabelContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.text = "" + this.text;
	context.color = this.color;
	context.visible = this.visible;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
LabelComp.prototype.setContext = function(context) {
	if (context.enabled  == true)
		this.active();
	else if (context.enabled == false)
		this.inactive();
	if (context.text != null && context.text != this.text)
		this.changeText(context.text);
	if (context.innerHTML != null)
		this.setInnerHTML(context.innerHTML);
	if (context.color != null)
		this.setColor(context.color);
	if (context.style != null)
		this.setStyle(context.style);
	if (context.weight != null)
		this.setWeight(context.weight);
	if (context.size != null)
		this.setSize(context.size);
	if (context.family != null)
		this.setFamily(context.family);
	if (context.visible != this.visible) {
		if (context.visible == true)
			this.show();
		else
			this.hide();	
	}
};