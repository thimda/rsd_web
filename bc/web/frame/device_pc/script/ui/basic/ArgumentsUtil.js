/**
 * @fileoverview 各种工具方法,包括各种表单域的验证方法.
 * 
 * @author gd,dengjt
 * @version 1.2
 */

/**
 * 获取布尔值
 * @param value 输入值
 * @param defaultValue
 * @return
 */
function getBoolean(value, defaultValue) {
	if (value == 'false')
		return false;
	else if (value == 'true')
		return true;
	else if (value != false && value != true)
		return defaultValue;
	else
		return value;
};

/**
 * 获取字符串的值
 * @param value
 * @param defaultValue
 * @return
 */
function getString(value, defaultValue) {
	if (value == null || value == "")
		return defaultValue;
	return value;
};

/**
 * 获取整型值
 * @param value
 * @param defaultValue
 * @return
 */
function getInteger(value, defaultValue) {
	if (isNaN(parseInt(value)))
		return defaultValue;
	return parseInt(value);
};

/**
 * 获取浮点值
 * @param value
 * @param defaultValue
 * @return
 */
function getFloat(value, defaultValue) {
	if (isNaN(parseFloat(value)))
		return defaultValue;
	return parseFloat(value);
};

/**
 * 判断是否为空
 * @param value
 * @param canBlank
 * @return
 */
function isNull(value, canBlank) {
	if (value == null)
		return true;
	if (value == "" && !canBlank)
		return true;
	return false;
};

/**
 * 判断是否为非空
 * @param value
 * @param canBlank
 * @return
 */
function isNotNull(value, canBlank) {
	return !isNull(value, canBlank);
};

/**
 * 把百分比转成小数
 * @param per
 * @return
 */
function convertPerToDecimal(per) {
	if (per.indexOf("%") == -1)
		return;

	var decimal = per.substring(0, per.length - 1);
	decimal = parseInt(decimal) / 100;
	return decimal;
};

/**
 * 判断是否为百分数
 * @param value
 * @return
 */
function isPercent(value) {
	return value == null ? false : ("" + value).indexOf("%") != -1;
};

/**
 * 
 * 验证传入的str是否是数字 alert(validate("1.22")) //true alert(validate("111")) //true
 * alert(validate("1..22")) //false alert(validate("1.2a2")) //false
 * alert(validate("1.")) //false
 * @param str
 * @return
 */
function isDigital(str) {
	var re = /^((-?)([1-9]+[0-9]*|0{1}))(\.\d+)?$/;
	return re.test(str);
};

/**
 * 验证传入的字符是否是字母
 * @param str
 * @return
 */
function isAlpha(str) {
	var patrn = /^[A-Za-z]+$/;
	if (!patrn.exec(str))
		return false;
	return true;
};

/**
 * 验证是否仅是(0-9)的数字
 * @param str
 * @return
 */
function isNumberOnly(str) {
	var patrn = /^[0-9]+$/;
	if (!patrn.exec(str))
		return false;
	return true;
};

/**
 * 验证传入的字符是否是整数(0-9包括"-"),且整数必须介于javascript规定的最大值最小值之间
 * @param str
 * @return
 */
function isNumber(str) {
	// 将输入的str转化为字符串.否则js认为000 == "0"为true
	str = str + "";
	if (str == "0")
		return true;

	var patrn = /(^-[1-9]\d*$)|^([1-9]\d*$)/;
	if (patrn.exec(str) == null)
		return false;
	else {
		if (parseInt(str) >= -9007199254740992
				&& parseInt(str) <= 9007199254740992)
			return true;
		else
			return false;
	}
};

/**
 * 验证输入的是否为中文
 * @param s
 * @return
 */
function isChinese(s) {
	var patrn = /^[\u0391-\uFFE5]+$/;
	if (!patrn.exec(s))
		return false;
	return true;
};

/**
 * 
 * 验证传入串是否是合法的javascript标示符
 * javascript合法标示符:第一个字符必须是字母、下划线(_)、美元符号($),接下来的字符可以是字母、数字或下划线
 * @param str
 * @return
 */
function isValidIdentifier(str) {
	str = str.trim();
	var flag = true;
	var first = str.charAt(0);
	if (!(isAlpha(first) || first == "_" || first == "$"))
		return false;

	for ( var i = 1; i < str.length; i++) {
		let = str.charAt(i);
		if (!(isAlpha(let) || isDigital(let) || let == "_")) {
			flag = false;
			break;
		}
	}
	return flag;
};

/**
 * 验证传入串是否email格式
 * @param s
 * @return
 */
function isEmail(s) {
	var patrn = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	if (!patrn.exec(s))
		return false;
	return true;
};

/**
 * 是否是电话号码 匹配: 0910-123456 或 029-123456 或029-12345678 或 12345678912
 * @param s
 * @return
 */
function isPhone(s) {
	var patrn = /^(?:0[0-9]{2,3}[-\\s]{1}|\\(0[0-9]{2,4}\\))[0-9]{6,8}$|^[1-9]{1}[0-9]{5,7}$|^[1-9]{1}[0-9]{10}$/;
	if (!patrn.exec(s))
		return false;
	return true;
}

/**
 * 转义xml关键字符
 * @param str
 * @return
 */
function convertXml(str) {
	if (str != null) {
		str = str + "";
		var reg1 = new RegExp("&", "g");
		str = str.replace(reg1, "&amp;");

		var reg2 = new RegExp("<", "g");
		str = str.replace(reg2, "&lt;");

		var reg3 = new RegExp(">", "g");
		str = str.replace(reg3, "&gt;");

		var reg4 = new RegExp("'", "g");
		str = str.replace(reg4, "&apos;");

		var reg5 = new RegExp("\"", "g");
		str = str.replace(reg5, "&quot;");
	}
	return str;
};

/**
 * 在光标位置插入数据（暂时只支持IE）
 * @param myField
 * @param myValue
 * @return
 */
function insertAtCursor(myField, myValue) {
	if (document.selection) {
		if (document.selection.type == 'Text') {
			document.selection.clear();
		}
		sel = document.selection.createRange();
		sel.text = myValue;
	}
	return myField.value;
};

