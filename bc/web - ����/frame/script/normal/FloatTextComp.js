/**
 * @fileoverview float类型的Text输入控件.  
 *	
 * @author gd
 * @version 5.5
 * @since NC5.0
 *	
 */
FloatTextComp.prototype = new TextComp;
FloatTextComp.prototype.componentType = "FLOATTEXT";

/**
 * 浮点型输入框构造函数
 * @class 浮点型输入框
 * @constructor floatText构造函数
 */
function FloatTextComp(parent, name, left, top, width, position, precision, attrArr, className) {	
	this.precision = getString(precision, "2");
	this.maxValue = null;
	this.minValue = null;
	this.base = TextComp;
	this.base(parent, name, left, top, width, "N", position, attrArr, className);
};

/**
 * 处理回车事件
 * @private
 */
FloatTextComp.prototype.processEnter = function() {
	 var inputValue = this.getValue().trim();
	 if (inputValue != "") {
		 inputValue = this.getFormater().format(inputValue, this.minValue, this.maxValue);
		 if (inputValue == "") {
			 this.input.value = "";
			 this.setMessage("");
			 this.setFocus();
		 } else {
			 this.setCorrectValue(inputValue);
		 }
	 }	 
};

/**
 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
 * @private
 */
FloatTextComp.prototype.createDefaultFormater = function() {
	return new DicimalFormater(this.precision, this.minValue, this.maxValue);
};

/**
 * 失去焦点时进行检测
 * @private
 */
FloatTextComp.prototype.blur = function() {	
//	var value = this.getValue().trim();
	var value = this.input.value.trim();
	if (this.dataType == 'N' && value == "") {
		this.setCorrectValue("");
	}	
	if (this.dataType == 'N' && value != "") {	
		value = this.getFormater().format(value, this.minValue, this.maxValue);
		// 检测输入的是否是浮点数,若不是则设置焦点要求用户重新输入
		if (value == "") {
			this.input.value = "";
			this.setMessage("");
			this.setFocus();
		} else {
			this.setCorrectValue(value);
		}
	}
	this.newValue = this.input.value;
	if (parseFloat(this.newValue) != parseFloat(this.oldValue))
		this.valueChanged(this.oldValue, this.newValue);
	this.onblur();
};

/**
 * 设置精度
 */
FloatTextComp.prototype.setPrecision = function(precision,fromDs) {
	fromDs = (fromDs == null) ? false : fromDs;
	if (fromDs == true){
		this.precisionFromDs = true;
	}
	//以ds设置的精度为准
	if (this.precisionFromDs != null && this.precisionFromDs == true && fromDs == false)
		return;
	this.precision = parseInt(precision);
	this.getFormater().precision = this.precision;
	this.setValue(this.getValue());
};

/**
 * 设置值
 */
FloatTextComp.prototype.setValue = function(text) {
	text = getString(text, "");
	if (text == "") {
		this.input.value = "";
		this.setMessage("");
//		this.oldValue = text;
	}
	//else if(checkInputDicimal(text, this.precision))
	//{	
	//	text = checkDicimalInvalid(text, this.precision);
	//}
	text = this.getFormater().format(text);
	TextComp.prototype.setValue.call(this, text);
};





/**
 * 这个方法和上面的方法的唯一区别是这个方法设置的值一定保证正确
 * @private 私有方法 
 */
FloatTextComp.prototype.setCorrectValue = function(text) {
	this.setMessage(text);
	this.input.value = text;
};


/**
	得到context信息
**/
FloatTextComp.prototype.getContext = function() {
	var context = TextComp.prototype.getContext.call(this);
//	var context = new Object;
	context.c = "FloatTextContext";
	context.id = this.id;
	context.precision = this.precision;
	return context;
};


/**
	设置context信息
**/
FloatTextComp.prototype.setContext = function(context) {
	TextComp.prototype.setContext.call(this,context);
	if (context.precision != null){
		this.setPrecision(context.precision);
//		this.processEnter();
	}
};

/**
 * 设置最大值
 */
FloatTextComp.prototype.setMaxValue = function(maxValue) {
	if (!isNaN(parseFloat(maxValue))) {
		this.maxValue = parseFloat(maxValue);
	}
	else
		this.maxValue = null;
};

/**
 * 设置最小值
 */
FloatTextComp.prototype.setMinValue = function(minValue) {
	if (!isNaN(parseFloat(minValue))) {
		this.minValue = parseFloat(minValue);
	} 
	else
		this.minValue = null;
};
	