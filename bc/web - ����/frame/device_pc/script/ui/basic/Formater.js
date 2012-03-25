/**
 * @fileoverview 值格式化器.可将传入值格式化成需要的格式。比如按照精度格式化等
 * @author gd, dengjt, guoweic
 * @version 6.0
 * 
 * 1.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 
 */
var FormaterMap = new HashMap();


/**
 * 格式化器
 * @class 格式化器基类
 * @return
 */
function Formater() {
};

/**
 * 格式化
 */
Formater.prototype.format = function(value, direction) {
	if (direction == null || direction == true)
		return getString(value, "");
	return value;
};

/**
 * 校验
 */
Formater.prototype.valid = function(presssKey, aggValue) {
	return true;
};

/**
 * @private
 */
BooleanFormater.trueArr = [ 1, 'Y', 'y', true, 'true' ];

/**
 * @private
 */
BooleanFormater.falseArr = [ 0, 'N', 'n', false, 'false' ];

/**
 * Boolean格式化器
 * @class Boolean格式化器
 * 将传入值解析，返回true或者false
 */
function BooleanFormater(trueArr, falseArr, defaultValue) {
	if (trueArr == null)
		this.trueArr = BooleanFormater.trueArr;
	else
		this.trueArr = trueArr;

	if (falseArr == null)
		this.falseArr = BooleanFormater.falseArr;
	else
		this.falseArr = falseArr;

	this.defaultValue = getBoolean(defaultValue, false);
};

/**
 * 格式化
 */
BooleanFormater.prototype.format = function(value, direction) {
	if (direction == null || direction == true) {
		if (this.falseArr.indexOf(value) != -1)
			return false;
		if (this.trueArr.indexOf(value) != -1)
			return true;
		return this.defaultValue;
	}
	return value;
};

/**
 * 字符格式化器
 * @class 字符格式化器
 * @param maxSize 最大允许长度
 */
function StringFormater(maxSize) {
	if (maxSize == null || parseInt(maxSize) < 0)
		this.maxSize = -1;
	else
		this.maxSize = maxSize;
};

/**
 * 格式化
 */
StringFormater.prototype.format = function(value) {
	if (value == null || value == "")
		return "";
	if (this.maxSize > 0) {
		// 若输入字节长度大于指定长度,则截去超过指定的最大长度的部分
		if (value.lengthb() > this.maxSize)
			value = value.substrCH(this.maxSize);
	}
	return value;
};

/**
 * 校验
 */
StringFormater.prototype.valid = function(key, aggValue, currValue) {
	if (this.maxSize != -1) {
		if (aggValue.lengthb() > this.maxSize)
			return false;
	}
};

/**
 * 浮点数格式化器
 * @class 浮点数格式化器。
 * 判断传入的是否时浮点数，不足精度的自动补全。不是浮点数的返回空值 如果指定了最大值或者最小值则校验大小范围。
 */
function DicimalFormater(precision, minValue, maxValue) {
	this.precision = getInteger(precision, 2); // 默认精度为2
	this.minValue = minValue;
	this.maxValue = maxValue;
};

/**
 * 格式化
 */
DicimalFormater.prototype.format = function(value) {
	if (value == null || value == "")
		return "";
	if (this.minValue != null && parseFloat(value) < this.minValue)
		return "";
	if (this.maxValue != null && parseFloat(value) > this.maxValue)
		return "";
	value = value + "";

	for ( var i = 0; i < value.length; i++) {
		if ("-0123456789.".indexOf(value.charAt(i)) == -1)
			return "";
	}

	return checkDicimalInvalid(value, this.precision);
};

/**
 * 校验
 */
DicimalFormater.prototype.valid = function(key, aggValue, currValue) {
	var isInvalid = false;
	// 检测输入的是否为数字
	if ("0123456789.-".indexOf(key) == -1)
		isInvalid = false;
	else
		isInvalid = true;

	if (isInvalid == true)
		return checkInputDicimal(aggValue, this.precision);
	else
		return false;
};

/**
 * 浮点型输入框的实时检测
 */
function checkInputDicimal(value, precision) {
	if (value == null || value == "")
		return false;
	value = value + "";
	// 有"."整数位数不能超过12位
	if (value.indexOf(".") != -1) {
		var intNumber = value.substr(0, value.indexOf("."));
		if (intNumber.length > 15 - precision - 1)
			return false;
	}
	// 没"."整数位数不能超过12位
	else if (value.length > 15 - precision - 1)
		return false;

	var num = 0, start = 0;
	if ((start = value.indexOf(".")) != -1) {
		if ((value.substring(start + 1)).length > parseInt(precision))
			return false;
	}
	// 输入的"."不能多于一个,且第一个不能是"."
	for ( var i = 0; i < value.length; i++) {
		if (value.charAt(0) == ".")
			return false;

		if (value.charAt(i) == ".")
			num++;
	}

	if (num > 1)
		return false;
	// 负号只能在第一位且不能多于两个
	for ( var i = 1; i < value.length; i++) {
		if (value.charAt(0) == "-") {
			if (value.charAt(i) == "-")
				return false;
		}
	}
	return true;
};

/**
 * 校验浮点类型。对于浮点型输入框帮助用户完成他的无意失误。
 */
function checkDicimalInvalid(str, precision) {
	if (str == null || isNaN(str))
		return "";
	// 浮点数总位数不能超过10位
	if (str.length > 15)
		str = str.substring(0, 15);

	// 默认2位精度
	if (precision == null || !isNumberOnly(precision))
		precision = 2;
	else
		precision = parseInt(precision);
	var digit = parseFloat(str);
	var result = (digit * Math.pow(10, precision) / Math.pow(10, precision))
			.toFixed(precision);
	if (result == "NaN")
		return "";
	return result;

	/*
	 * str += ""; //默认2位精度 if(precision == null || !isNumberOnly(precision))
	 * precision = 2; else precision = parseInt(precision);
	 * 
	 * //精度位数 var currPre = (str.substring(str.indexOf(".") + 1)).length;
	 * //为-1则是整数,否则是浮点数 var flag = str.indexOf("."); //若输入为整数,则自动按提前设定的精度补足"0"
	 * if(flag == -1) { var flag = true; //检测输入的是否是整数(粗检查) for(var i = 0, count =
	 * str.length; i < count; i ++) { if("-0123456789".indexOf(str.charAt(i)) ==
	 * -1) flag = false; }
	 * 
	 * if(flag == true) { // 去掉整数前的"0" var temp1 = 0; for(var i = 0; i <
	 * str.length; i ++) { if(str.charAt(i) == "0") temp1++; else break; }
	 *  // 说明输入的全部是"0" if(str.length == temp1) str = "0"; else { if(temp1 > 1)
	 * str = str.substring(temp1); }
	 * 
	 * str = str + "."; for(var i = 0; i < precision; i++) { str = str + "0"; }
	 * return str; } } //若出现0000.21的情况应将其改为0.21000 else if(flag != -1 &&
	 * (str.charAt(0) == "0" || str.charAt(0) == "-")) { var t = 0;
	 * if(str.charAt(0) == "-") { for(var i = 1; i < str.indexOf(".") - 1; i++) {
	 * if(str.charAt(i) != "0") break; else { t++; } } if(t > 0) { str = "-" +
	 * str.substring(t+1); } } else if(str.charAt(0) == "0") { for(var i = 0; i <
	 * str.indexOf(".") - 1; i++) { if(str.charAt(i) != "0") break; else { t++; } }
	 * if(t > 0) { //去掉整数位多余的0 str = str.substring(t); } }
	 * 
	 * //补足精度 if(currPre < precision) { for(var i = 0, count = precision -
	 * currPre; i < count; i++) { str = str + "0"; } } // 精度超过限制精度则截断精度显示 else
	 * if(currPre > precision) { str = str.substring(0, str.length -
	 * (currPre-precision)); } return str; } //将精度补足 else if(currPre <
	 * precision) { for(var i = 0, count = precision - currPre; i < count; i++) {
	 * str = str + "0"; } return str; } // 精度位数大于指定精度,则截断多余位数 else if(currPre >
	 * precision) { var potIndex = str.indexOf(".") + 1; str = str.substring(0,
	 * potIndex + precision); } return str;
	 */
};

/**
 * 整数格式化器
 * @class 整数格式化器。判断传入的是否是整数，是否介于设定的上下界直接。不符合返回空值。
 */
function IntegerFormater(minValue, maxValue) {
	if (!isNumber(minValue))
		this.minValue = -9007199254740992;
	else
		this.minValue = parseInt(minValue);

	if (!isNumber(maxValue))
		this.maxValue = 9007199254740992;
	else
		this.maxValue = parseInt(maxValue);
};

/**
 * 格式化
 */
IntegerFormater.prototype.format = function(value) {
	if (value == null || value == "")
		return "";
	else if (!isNumber(value)) {
		return "";
	}

	if (checkIntegerIsValid(value, this.minValue, this.maxValue) == true)
		return value;
	else
		return "";
};

/**
 * 整数类型校验
 */
IntegerFormater.prototype.valid = function(key, aggValue, currValue) {
	var isInvalid = false;
	// 检测输入的是否为数字
	if ("-0123456789".indexOf(key) == -1)
		isInvalid = false;
	else
		isInvalid = true;

	if (isInvalid == true) {
		// 只输入一个负号时认为合法
		if (aggValue == "-")
			return true;
		else
			return isNumber(aggValue);
	} else
		return false;
	return true;
};

/**
 * 校验整数类型
 * @param value
 * @param minValue
 * @param maxValue
 * @return
 */
function checkIntegerIsValid(value, minValue, maxValue) {
	// 确定传入参数的合法性
	if (!isNumber(value))
		return false;
	value = parseInt(value);
	if (!isNumber(minValue)) {
		minValue = -9007199254740992;
	}

	if (!isNumber(maxValue)) {
		maxValue = 9007199254740992;
	}
	// 整数不能超过范围
	if (value > maxValue || value < minValue)
		return false;
	else
		return true;
};

/**
 * 日期格式化器
 * @class 日期格式化器。将传入值解析，若传入正确，则返回正确格式的日期，否则返回空值。
 */
function DateFormater() {
};

DateFormater.prototype.valid = function(key, aggValue, currValue) {
	return true;
};

/**
 * 格式化。传入java中new Date()输出的字符串。
 */
DateFormater.prototype.format = function(value) {
	if (value == null)
		return "";
	// 若value值超过10位长度,则直接截取前10位进行检测
	else if (value.length > 10)
		value = value.substring(0, 10);

	// 若符合YYYY-MM-DD格式,则直接返回此串
	if (CheckDateFormat(value)) {
		return value;
	}

	var changeValue = value.replace(/-/ig, "/");
	if (!isNaN(Date.parse(changeValue))) {
		var d = new Date(changeValue);

		var y = d.getYear(); // 得到年份

		var m = d.getMonth() + 1;// 得到月份
		if (parseInt(m) < 10) {
			m = "0" + m;
		}

		var d = d.getDate();// 得到日期
		if (d < 10) {
			d = "0" + d;
		}

		return (y + "-" + m + "-" + d);
	} else {
		return "";
	}
};


/**
 * 格式化DateTime
 */
DateFormater.prototype.formatDateTime = function(value) {
	if (value == null)
		return "";
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/; 
	var r = value.match(reg); 
	if(r==null)
		return ""; 
	var d= new Date(r[1], r[3]-1,r[4],r[5],r[6],r[7]); 
	if (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]&&d.getHours()==r[5]&&d.getMinutes()==r[6]&&d.getSeconds()==r[7])
		return r[1] + "-" + r[3] + "-" + r[4] + " " + r[5] + ":" + r[6] + ":" + r[7];
	else
		return "";
};


/**
 * 校验日期类型
 * @param value
 * @return
 */
function CheckDateFormat(value) {
	var strDate = value;
	if (strDate == null)
		return false;

	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})/;
	var r = strDate.match(reg);

	if (r == null) {
		return false;
	} else if (r == false) {
		return false;
	}

	var d = new Date(r[1], r[3] - 1, r[4]);
	if (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3]
			&& d.getDate() == r[4]) {
		return true;
	} else {
		return false;
	}
};

/**
 * 显示错误提示浮动条
 */
function showVerifyMessage(obj, msg) {
	if (window.sys_verifyMessageDiv == null) {
		var div = $ce("DIV");
		div.style.background = "#D8E3E8";
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。		
//		div.style.zIndex = 10000;
		div.style.zIndex = getZIndex();
		div.style.position = "absolute";
		div.appendChild(document.createTextNode(""));
		window.sys_verifyMessageDiv = div;
		document.body.appendChild(window.sys_verifyMessageDiv);
		window.sys_verifyMessageDiv.style.display = "none";
	}

	window.sys_verifyCount = 0;
	var top = compOffsetTop(obj.getObjHtml()) + obj.getObjHtml().offsetHeight;
	var left = compOffsetLeft(obj.getObjHtml());

	window.sys_verifyMessageDiv.replaceChild(document.createTextNode(msg),
			window.sys_verifyMessageDiv.firstChild);
	window.sys_verifyMessageDiv.style.top = top + 4 + "px";
	window.sys_verifyMessageDiv.style.left = left + "px";
	window.sys_verifyMessageDiv.style.display = "block";
	setTimeout("startVerifyMessageFloating()", 100);
};

function startVerifyMessageFloating() {
	if (window.sys_verifyCount < 10) {
		window.sys_verifyMessageDiv.style.top = window.sys_verifyMessageDiv.offsetTop - 4 + "px";
		window.sys_verifyCount++;
		setTimeout("startVerifyMessageFloating()", 100);
	} else {
		window.sys_verifyMessageDiv.style.display = "none";
	}
};

