/**
 *	
 * Password类型的Text输入控件  
 * 
 * @author gd
 *	
 */
PswTextComp.prototype = new TextComp;
PswTextComp.prototype.componentType = "PSWTEXT";

/**
 * 密码控件构造函数
 * @class 密码控件 
 */
function PswTextComp(parent, name, left, top, width, position, attrArr, className) {
	this.base = TextComp;
	this.base(parent, name, left, top, width, "P", position, attrArr, className);
};

/**
 * 创建默认格式化器
 * @private
 */
PswTextComp.prototype.createDefaultFormater = function() {	
	return new StringFormater(this.maxSize);
};

/*
 * 处理回车事件
PswTextComp.prototype.processEnter = function() {
	
};
 */

/**
 * 设值，设置值时要检测类型
 */
PswTextComp.prototype.setValue = function(text) {
	TextComp.prototype.setValue.call(this,text);
};

/**
 * 鼠标移入事件
 * @private
 */
PswTextComp.prototype.onmouseover = function(e) {
	TextComp.prototype.onmouseover.call(this,e);
	this.input.title = " ";
};

