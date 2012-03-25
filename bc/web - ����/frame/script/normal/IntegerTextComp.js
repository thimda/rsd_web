/**
 * @fileoverview   
 *	
 * @author gd
 * @version 5.5
 * 
 */
IntegerTextComp.prototype = new TextComp;
IntegerTextComp.prototype.componentType = "INTEGERTEXT";

/**
 * 整形输入框
 * @class Integer类型的Text输入控件
 * 	
 * @constructor integerText构造函数
 * @author gd
 * @version 1.2
 */
function IntegerTextComp(parent, name, left, top, width, position, maxValue, minValue, attrArr, className) {	
	this.base = TextComp;
	this.maxValue = getInteger(maxValue, "9007199254740992");
	this.minValue = getInteger(minValue, "-9007199254740992");
	if (attrArr != null) {
		if (attrArr.tip == null || attrArr.tip == "") {
			if (minValue != null && maxValue != null)
				attrArr.tip = this.minValue + "～" + this.maxValue;
			else if (minValue != null)
				attrArr.tip = ">=" + this.minValue;
			else if (maxValue != null)
				attrArr.tip = "<=" + this.maxValue;
		}
	}
	this.base(parent, name, left, top, width, "I", position, attrArr, className);
};   

/**
 * 处理回车事件
 * @private
 */
IntegerTextComp.prototype.processEnter = function() {
	 var inputValue = this.getValue().trim();
	 if (inputValue != "") {	 
		 inputValue = this.getFormater().format(inputValue);
		 if (inputValue == "") {
//			 showVerifyMessage(this, trans("ml_integermustbetween") + this.minValue + trans("ml_and") + this.maxValue + trans("ml_between"));
			 showVerifyMessage(this, "只能输入整形数字");
			 this.input.value = ""; 
			 this.setFocus();
		 } else {
			this.setMessage(inputValue);
			this.input.value = inputValue;
		 }
	 }	 
};

/**
 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
 * @private
 */
IntegerTextComp.prototype.createDefaultFormater = function() {
	return new IntegerFormater(this.minValue, this.maxValue);
};

/**
 * 失去焦点时进行检测
 * @private
 */
IntegerTextComp.prototype.verify = function(oldValue) {
	if (this.newValue == "" && this.input.value != "") {
		showVerifyMessage(this, trans("ml_integermustbetween") + this.minValue + trans("ml_and") + this.maxValue + trans("ml_between"));
		this.input.value = "";
		this.setMessage("");
	} 
};

/**
 * 设置最大值
 */
IntegerTextComp.prototype.setIntegerMaxValue = function(maxValue) {
	if (maxValue != null) {	
		// 判断maxValue是否是数字
		if (isNumber(maxValue)) {	
			if (parseInt(maxValue) >= -9007199254740992 && parseInt(maxValue) <= 9007199254740992) {
				this.maxValue = maxValue;
			}
		}	
	}
};

/**
 * 设置最小值
 */
IntegerTextComp.prototype.setIntegerMinValue = function(minValue) {
	if (minValue != null) {	
		// 判断minValue是否是数字
		if (isNumber(minValue)) {	
			if ((parseInt(minValue) >= -9007199254740992) && (parseInt(minValue) <= 9007199254740992)) {  
				this.minValue = minValue;
			}
		}
	}
};

/**
 * 设置值
 */
IntegerTextComp.prototype.setValue = function(text) {
	if (!checkIntegerIsValid(text, null, null)) {
		text = "";
		this.input.value = "";
		this.setMessage("");
//		this.oldValue = "";
	}
	TextComp.prototype.setValue.call(this, text + "");
};

