/**
 * 
 * @fileoverview 进度条
 * 
 * @author dengjt, guoweic
 * @version 6.0
 * 
 * 
 */

ProgressBarComp.prototype = new BaseComponent;
ProgressBarComp.prototype.componentType = "PROGRESS";

/**
 * 进度条构造函数
 * @class 进度条
 */
function ProgressBarComp(parent, name, left, top, width, height, position, valueAlign,
		className) {
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.overflow = "hidden";
	this.className = getString(className, "panel_div");
	this.value = 0;
	// 百分比显示位置，默认为右侧
	this.valueAlign = getString(valueAlign, "right");
	this.create();
};

/**
 * @private
 */
ProgressBarComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.overflow = this.overflow;
	this.Div_gen.style.visibility = "hidden";
	this.Div_gen.className = this.className;
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * @private
 */
ProgressBarComp.prototype.manageSelf = function() {
	if (this.valueAlign == "center") {
		// TODO
		this.barOuterDiv = $ce('DIV');
		this.Div_gen.appendChild(this.barOuterDiv);
		this.barOuterDiv.style.width = this.Div_gen.offsetWidth - 2 + "px";
		this.barOuterDiv.style.border = "1px solid red";
	
		this.percentDiv = $ce('DIV');
		this.percentDiv.style.left = parseInt(this.Div_gen.offsetWidth / 2) - 10 + "px";
		this.percentDiv.style.zIndex = "2";
		this.percentDiv.style.color = "yellow";
		this.percentDiv.style.position = "absolute";
		this.barOuterDiv.appendChild(this.percentDiv);
		this.percentDiv.innerHTML = "0%";
		var height = this.height;
		this.barOuterDiv.style.height = height + "px";
		this.Div_gen.style.height = parseInt(height) + 2 + "px";
	
		this.barDiv = $ce('DIV');
		this.barOuterDiv.appendChild(this.barDiv);
		this.barDiv.style.width = "0px";
		this.barDiv.style.background = "blue";
		this.barDiv.style.zIndex = "1";
		this.barDiv.style.position = "absolute";
		this.barDiv.style.height = height + "px";
		
	} else {
		this.barOuterDiv = $ce('DIV');
		this.Div_gen.appendChild(this.barOuterDiv);
		this.barOuterDiv.style.width = this.Div_gen.offsetWidth - 30 + "px";
		this.barOuterDiv.style.height = "80%";
		if (this.valueAlign == "right")
			this.barOuterDiv.style[ATTRFLOAT] = "left";
		else
			this.barOuterDiv.style[ATTRFLOAT] = "right";
		this.barOuterDiv.style.border = "1px solid red";
	
		this.barDiv = $ce('DIV');
		this.barOuterDiv.appendChild(this.barDiv);
		this.barDiv.style.width = "0px";
		this.barDiv.style.height = "100%";
		this.barDiv.style.background = "blue";
	
		this.percentDiv = $ce('DIV');
		this.Div_gen.appendChild(this.percentDiv);
		this.percentDiv.style.border = "1px solid green";
		if (this.valueAlign == "right")
			this.percentDiv.style[ATTRFLOAT] = "right";
		else
			this.percentDiv.style[ATTRFLOAT] = "left";
		this.percentDiv.innerHTML = "0%";
		this.percentDiv.parentNode.style.height = this.percentDiv.offsetHeight + 1 + "px";
	}
};

/**
 * 设值
 */
ProgressBarComp.prototype.setValue = function(newValue) {
	this.value = newValue;
	this.barDiv.style.width = newValue + "%";
	this.percentDiv.innerHTML = newValue + "%";
};

/**
 * 取值
 */
ProgressBarComp.prototype.getValue = function(newValue) {
	return this.value;
};

/**
 * 显示进度条
 */
ProgressBarComp.prototype.show = function() {
	this.Div_gen.style.visibility = "visible";
	this.visible = true;
};

/**
 * 隐藏进度条
 */
ProgressBarComp.prototype.hide = function() {
	this.Div_gen.style.visibility = "hidden";
	this.visible = false;
};

/**
 * 获取对象信息
 * @private
 */
ProgressBarComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.ProgressBarContext";
	context.c = "ProgressBarContext";
	context.id = this.id;
	context.value = this.value;
	context.visible = this.visible;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
ProgressBarComp.prototype.setContext = function(context) {
	if (context.value && context.value != this.value)
		this.setValue(context.value);
	if (context.visible != null) {
		if (context.visible == true)
			this.show();
		else if (context.visible == false)
			this.hide();
	}
};
