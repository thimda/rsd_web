/***********************************************************
 *	数据类型定义。严格的说，还包括控件类型的定义。由于javascript控件特性，
 *	并没有将数据类型和控件类型拆分开定义。此定义应用于TextField输入框和
 *	Grid中。
 *	
 *	@author dengjt
 ************************************************************/
/**
 * 数据类型定义
 * @class 数据类型
 */
var DataType = new Object;
/**
 * STRING类型
 * @constant
 */
DataType.STRING = "String";
/**
 * Integer类型
 * @constant
 */
DataType.INTEGER = "Integer";
/**
 * int类型
 * @constant
 */
DataType.INT = "int";
/**
 * Double类型
 * @constant
 */
DataType.DOUBLE = "Double";
/**
 * double类型
 * @constant
 */
DataType.dOUBLE = "double";
/**
 * UFDouble类型
 * @constant
 */
DataType.UFDOUBLE = "UFDouble";
/**
 * Float类型
 * @constant
 */
DataType.FLOAT = "Float";
/**
 * float类型
 * @constant
 */
DataType.fLOAT = "float";
/**
 * Byte类型
 * @constant
 */
DataType.BYTE = "Byte";
/**
 * byte类型
 * @constant
 */
DataType.bYTE = "byte";
/**
 * Boolean类型
 * @constant
 */
DataType.BOOLEAN = "Boolean";
/**
 * boolean类型
 * @constant
 */
DataType.bOOLEAN = "boolean";
/**
 * UFBoolean类型
 * @constant
 */
DataType.UFBOOLEAN = "UFBoolean";
/**
 * Date类型
 * @constant
 */
DataType.DATE = "Date";
/**
 * BigDecimal类型
 * @constant
 */
DataType.BIGDECIMAL = "BigDecimal";
/**
 * Long类型
 * @constant
 */
DataType.LONG = "Long";
/**
 * long类型
 * @constant
 */
DataType.lONG = "long";
/**
 * char类型
 * @constant
 */
DataType.CHAR = "char";
/**
 * Character类型
 * @constant
 */
DataType.CHARACTER = "Character";
/**
 * UFDateTime类型
 * @constant
 */
DataType.UFDATETIME = "UFDateTime";
/**
 * UFDate类型
 * @constant
 */
DataType.UFDATE = "UFDate";
/**
 * UFTime类型
 * @constant
 */
DataType.UFTIME = "UFTime";
/**
 * UFLiteralDate类型
 * @constant
 */
DataType.UFLITERALDATE = "UFLiteralDate";

DataType.UFDATEBEGIN = "UFDate_begin";

DataType.UFDATEEND = "UFDate_end";


/**
 * UFNumberFormat类型
 * @constant
 */
DataType.UFNUMBERFORMAT = "UFNumberFormat";
/**
 * Decimal类型
 * @constant
 */
DataType.Decimal = "Decimal";
/**
 * Entity类型
 * @constant
 */
DataType.Entity = "Entity";

/**
 * 编辑类型定义
 * @class 编辑类型
 */
var EditorType = new Object;
/**
 * CheckBox类型
 * @constant
 */
EditorType.CHECKBOX = "CheckBox";
/**
 * StringText类型
 * @constant
 */
EditorType.STRINGTEXT = "StringText";
/**
 * IntegerText类型
 * @constant
 */
EditorType.INTEGERTEXT = "IntegerText";
/**
 * DecimalText类型
 * @constant
 */
EditorType.DECIMALTEXT = "DecimalText";
/**
 * "自定义"元素类型,是form的一种element
 * @constant
 */
EditorType.SELFDEFELE = "SelfDef";
/**
 * RadioGroup类型
 * @constant
 */
EditorType.RADIOGROUP = "RadioGroup";
/**
 * CheckboxGroup类型
 * @constant
 */
EditorType.CHECKBOXGROUP = "CheckboxGroup";
/**
 * Reference类型
 * @constant
 */
EditorType.REFERENCE = "Reference";
/**
 * ComboBox类型
 * @constant
 */
EditorType.COMBOBOX = "ComboBox";
/**
 * List类型
 * @constant
 */
EditorType.LIST = "List";

/**
 * PwdText类型
 * @constant
 */
EditorType.PWDTEXT = "PwdText";
/**
 * DateText类型
 * @constant
 */
EditorType.DATETEXT = "DateText";
/**
 * DateTimeText类型
 * @constant
 */
EditorType.DATETIMETEXT = "DateTimeText";
/**
 * TextArea类型
 * @constant
 */
EditorType.TEXTAREA = "TextArea";
/**
 * RichEditor类型
 * @constant
 */
EditorType.RICHEDITOR = "RichEditor";
EditorType.IMAGECOMP = "ImageComp";
EditorType.FILECOMP = "FileComp";


/**
 * 字符串去掉左右空格
 */
String.prototype.trim = function() {
	return this.replace(/^\s*(\b.*\b|)\s*$/, "$1");
};

/**
 * 字符串替换
 */
String.prototype.replaceStr = function(strFind, strRemp) {
	var tab = this.split(strFind);
	return new String(tab.join(strRemp));
};

/**
 * 获得字符串的字节长度
 */
String.prototype.lengthb = function() {
	return this.replace(/[^\x00-\xff]/g, "**").length;
};

/**
 * 将AFindText全部替换为ARepText
 */
String.prototype.replaceAll = function(AFindText, ARepText) {
	//自定义String对象的方法
	raRegExp = new RegExp(AFindText, "g");
	return this.replace(raRegExp, ARepText);
};

/**
 * 按字节数截取字符串 例:"e我是d".nLen(4)将返回"e我"
 */
String.prototype.substrCH = function(nLen) {
	var i = 0;
	var j = 0;
	while (i < nLen && j < this.length) {  // 循环检查制定的结束字符串位置是否存在中文字符
		if (this.charCodeAt(j) > 256 && i == nLen - 1) {
			break;
		} else if (this.charCodeAt(j) > 256) {  // 返回指定下标字符编码，大于265表示是中文字符
			i = i + 2;
		} //是中文字符，那计数增加2
		else {
			i = i + 1;
		} //是英文字符，那计数增加1
		j = j + 1;
	}
	;
	return this.substr(0, j);
};

/**
 * 校验字符串是否以指定内容开始
 */
String.prototype.startWith = function(strChild) {
	return this.indexOf(strChild) == 0;
};

/**
 * 判断字符串是否以指定参数的字符串结尾
 * 
 * @param strChild
 */
String.prototype.endWith = function(strChild) {
	var index = this.indexOf(strChild);
	if (index == -1)
		return;
	else
		return index == this.length - strChild.length;
};

///**
// * 字符串全半角转换
// * 
// * @param boo true为半角转全角；false为全角转半角
// * @return
// */
//String.prototype.changeCharset = function(boo) {
//	var result = "";
//	var charlist = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//	charlist += "abcdefghijklmnopqrstuvwxyz";
//	charlist += "0123456789";
//	charlist += " `~!@#$%^&*()_+-={}|[]:\";'<>?,./";// 半角空格
//	for ( var i = 0; i < this.length; i++) {  // 字符串str中的字符
//	
//		var c1 = this.charAt(i);
//		var c2 = this.charCodeAt(i);
//		if (charlist.indexOf(c1) > -1) {
//			if (" " == c1) {
//				result += "　";
//			} else {
//				result += String.fromCharCode(this.charCodeAt(i) + 65248);
//			}
//		} else {
//			if (boo) {
//				result += String.fromCharCode(this.charCodeAt(i));
//			} else {
//				if ("　" == c1) {
//					result += " ";
//				} else {
//					if (charlist.indexOf(String
//							.fromCharCode(this.charCodeAt(i) - 65248)) > -1) {
//						result += String
//								.fromCharCode(this.charCodeAt(i) - 65248);
//					} else {
//						result += String.fromCharCode(this.charCodeAt(i));
//					}
//				}
//			}
//		}
//	}
//	return result;
//};

/**
 * 获取AAAAMMJJ类型字符串
 */
Date.prototype.getAAAAMMJJ = function() {
	//date du jour
	var jour = this.getDate();
	if (jour < 10)
		(jour = "0" + jour);
	var mois = this.getMonth() + 1;
	if (mois < 10)
		(mois = "0" + mois);
	var annee = this.getYear();
	return annee + "" + mois + "" + jour;
};

/**
 * 获取YYYY-MM-DD类型字符串
 */
Date.prototype.getFomatDate = function() {
	var year = this.getFullYear();
	var month = this.getMonth() + 1;
	if (month < 10)
		month = "0" + month;
	var day = this.getDate();
	if (day < 10)
		day = "0" + day;
	return year + "-" + month + "-" + day;
};

/**
 * 获取YYYY-MM-DD HH:MM:SS类型字符串
 */
Date.prototype.getFomatDateTime = function() {
	var year = this.getFullYear();
	var month = this.getMonth() + 1;
	if (month < 10)
		month = "0" + month;
	var day = this.getDate();
	if (day < 10)
		day = "0" + day;
	var hours = this.getHours();
	if (hours < 10)
		hours = "0" + hours;
	var minutes = this.getMinutes();
	if (minutes < 10)
		minutes = "0" + minutes;
	var seconds = this.getSeconds();
	if (seconds < 10)
		seconds = "0" + seconds;
	return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
};

/**
 * 返回obj在数组中的位置
 */
Array.prototype.indexOf = function(obj) {
	for ( var i = 0; i < this.length; i++) {
		if (this[i] == obj)
			return i;
	}
	return -1;
};

/**
 * 按照index remove
 */
Array.prototype.remove = function(index) {
	if (index < 0 || index > this.length) {
		alert("index out of bound");
		return;
	}
	this.splice(index, 1);
};

/**
 * 按照数组的元素remove
 */
Array.prototype.removeEle = function(ele) {
	for ( var i = 0, count = this.length; i < count; i++) {
		if (this[i] == ele) {
			this.splice(i, 1);
			return;
		}
	}
};

/**
 * 将指定值ele插入到index处
 */
Array.prototype.insert = function(index, ele) {
	if (index < 0 || index > this.length) {
		alert("index out of bound");
		return;
	}
	this.splice(index, 0, ele);
};

/**
 * 得到和索引相对应的数组中的值
 */
Array.prototype.values = function(indices) {
	if (indices == null)
		return null;
	var varr = new Array();
	for ( var i = 0; i < indices.length; i++) {
		varr.push(this[indices[i]]);
	}
	return varr;
};

/**
 * 清空数组
 */
Array.prototype.clear = function() {
	this.splice(0, this.length);
};

/**
 * 强制截断浮点数
 * 
 * @param floatValue：浮点数
 * @param precision：保留小数点后位数
 * @author guoweic 2009-10-16
 */
function truncFloat(floatValue, precision) {
	if (precision) {
		if (!floatValue)
			floatValue = "0";
		if (floatValue.toString().indexOf(".") == -1 && precision > 0) {
			floatValue += ".";
			for ( var i = 0; i < precision; i++) {
				floatValue += "0";
			}
		} else if (floatValue.toString().indexOf(".") != -1) {
			if (precision <= 0)
				floatValue = floatValue.toString().substring(0,
						floatValue.toString().indexOf(".") + 1);
			else {
				floatValue = floatValue.toString();
				if (floatValue.length < floatValue.indexOf(".") + precision + 1) {
					for ( var i = 0, n = floatValue.indexOf(".") + precision
							+ 1 - floatValue.length; i < n; i++) {
						floatValue += "0";
					}
				} else
					floatValue = floatValue.substring(0, floatValue
							.indexOf(".")
							+ precision + 1);
			}
		}
	}
	return floatValue;
};

/**
 * 此方法是让target继承自source
 * 
 * @param source
 * @author gd 2007-12-13
 */
//function extend(target, source) {
//	// 保存CardEventHandler的prototype中原有方法,复制到CardEventHandler中
//	var oriMethod = new HashMap();
//	for ( var i in target.prototype)
//		oriMethod.put(i, target.prototype[i]);
//
//	target.prototype = new source;
//
//	var keySet = oriMethod.keySet();
//	if (keySet.length > 0) {
//		for ( var i = 0, count = keySet.length; i < count; i++)
//			target.prototype[keySet[i]] = oriMethod.get(keySet[i]);
//	}
//};

if (IS_IE && !IS_IE9) {
	window.$ge = document.getElementById;
	window.$ce = document.createElement;
} 
else {
	/**
	 * 等同于document.getElementById(id);
	 */
	function $ge(id) {
		return document.getElementById(id);
	}
	/**
	 * 等同于document.createElement(obj);
	 * @param obj
	 * @return
	 */
	function $ce(obj) {
		return document.createElement(obj);
	}
}
