/**
 *	
 * String类型的Text输入控件.
 *	
 * @author gd
 * @version NC6.0
 *	
 */
StringTextComp.prototype = new TextComp;
StringTextComp.prototype.componentType = "STRINGTEXT";

/**
 * 字符串输入框构造函数
 * @class 字符串输入框
 * @param maxSize 此变量指定的是字节数,中文每个汉字的字节数是2,英文每个字母的字节数是1
 */
function StringTextComp(parent, name, left, top, width, position, attrArr, className) {	
	this.base = TextComp;
	this.base(parent, name, left, top, width, "A", position, attrArr, className);
};

StringTextComp.prototype.verify = function(oldValue) {
	var value = this.getValue().trim();
	var beforeFormatL = oldValue.length;
	var afterFormatL = this.newValue.length;
	if (beforeFormatL > afterFormatL)
		showVerifyMessage(this, trans("ml_thevaluebeyondthemaxlength")); 
};

/**
* 创建默认格式化器
* @private
*/
StringTextComp.prototype.createDefaultFormater = function() {	
	return new StringFormater(this.maxSize);
};

/**
 * 处理回车事件
 * @private
 */
StringTextComp.prototype.processEnter = function() {
	 var inputValue = this.getValue().trim();
	 var beforeFormatL = inputValue.length;
	 value = this.getFormater().format(inputValue);
	 var afterFormatL = value.length;
	 if (beforeFormatL > afterFormatL)
		showVerifyMessage(this, trans("ml_thevaluebeyondthemaxlength"));
	 this.setMessage(value);
	 this.input.value = value;
};

/**
 * 设置最大值
 */
StringTextComp.prototype.setMaxSize = function(size) {
	this.maxSize = parseInt(size);
};
