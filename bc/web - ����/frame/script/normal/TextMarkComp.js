/**
 * @fileoverview TextMark 输入控件的图片替换类，用于在输入框不可见时替换为图片
 * @author guoweic
 * @version 6.0
 * 
 */
TextMarkComp.prototype = new BaseComponent;
TextMarkComp.prototype.componentType = "TEXT";
	
/**
 * 文本输入框控件构造函数
 * @class 文本输入框控件基类 
 * @constructor TextMarkComp的构造函数	
 *	
 * @param maxSize 此变量指定的是字节数,中文每个汉字的字节数是2,英文每个字母的字节数是1
 */
function TextMarkComp(parent, name, left, top, width, dataType, position, attrArr, className) {
	if (arguments.length == 0)
		return;
		
	this.base = BaseComponent;
	this.base(name, left, top, width, "");  
	this.dataType = getString(dataType, "A");		
	this.parentOwner = parent;
	
	this.position = getString(position, "absolute");
	this.value = getString(this.value, "");
	
	this.className = getString(className, "text_div");
	this.create();
};

/**
 * 真正的创建函数
 * @private
 */
TextMarkComp.prototype.create = function() {		
	var oThis = this;
	this.Div_gen = $ce("DIV");
	
	this.Div_gen.id = this.id;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";

	if (!isPercent(this.width)) {
		this.width = parseInt(this.width) + "px";
	}
	this.Div_gen.style.width = this.width;
	if (!isPercent(this.height)) {
		this.height = parseInt(this.height) + "px";
	}
	this.Div_gen.style.height = this.height;
	
  	this.Div_gen.style.overflow = "hidden";
	if (this.parentOwner) 
		this.placeIn(this.parentOwner);
};


/**
 * TextMarkComp的二级回掉函数
 * @private
 */
TextMarkComp.prototype.manageSelf = function() {
	this.img = $ce("img");
	this.img.style.width = "100px";
	this.img.style.height = "20px";
	this.img.src = window.themePath + "/images/text/mark.gif";
	this.Div_gen.appendChild(this.img);
};

/**
 * 设置组件大小及位置
 */
TextMarkComp.prototype.setBounds = function(left, top, width, height) {
	this.left = left;
	this.top = top;
	this.width = getString(width, "100%");
	this.height = getString(height, "100%");
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";
};

/**
 * 得到value值
 */
TextMarkComp.prototype.getValue = function() {
	return this.value;
};

/**
 * 设置value值
 */
TextMarkComp.prototype.setValue = function(value) {
	this.value = getString(value, "");
	
};

/**
 * 获取对象信息
 * @private
 */
TextMarkComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.TextContext";
	context.c = "TextContext";
	return context;
};

/**
 * 设置对象信息
 * @private
 */
TextMarkComp.prototype.setContext = function(context) {
	
};
