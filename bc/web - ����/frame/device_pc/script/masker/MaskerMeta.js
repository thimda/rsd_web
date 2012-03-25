/**
 * @fileoverview 数字格式化信息
 * @author licza
 * @version NC6.0
 * 
 */
NumberFormatMeta.prototype = new Object;
NumberFormatMeta.prototype.metaType = "NumberFormatMeta";
function NumberFormatMeta(){
};
/**
*是否负数红字
*/
NumberFormatMeta.prototype.isNegRed = false;
NumberFormatMeta.prototype.isMarkEnable = false;
NumberFormatMeta.prototype.markSymbol = ",";
NumberFormatMeta.prototype.pointSymbol = ".";
NumberFormatMeta.prototype.positiveFormat = "n";
NumberFormatMeta.prototype.negativeFormat = "-n";

/**
 * 货币格式信息
 * @author licza
 * @version NC6.0
 * 
 */
CurrencyFormatMeta.prototype = new NumberFormatMeta;
CurrencyFormatMeta.prototype.metaType = "CurrencyFormatMeta";
CurrencyFormatMeta.prototype.curSymbol = "";
/**
 * 构造方法
 */
function CurrencyFormatMeta(){
	this.positiveFormat = "$n";	
	this.negativeFormat = "$-n";
};

/**
 * 时间格式化信息
 */
DateTimeFormatMeta.prototype = new Object();
DateTimeFormatMeta.prototype.metaType = "DateTimeFormatMeta";
DateTimeFormatMeta.prototype.format = "yyyy-MM-dd hh:mm:ss";
DateTimeFormatMeta.prototype.speratorSymbol = "-";
function DateTimeFormatMeta(){
};
/**
 * 日期格式化信息
 */
DateFormatMeta.prototype = new DateTimeFormatMeta();
function DateFormatMeta(){
	this.format = "yyyy-MM-dd";
};
 
TimeFormatMeta.prototype = new DateTimeFormatMeta;
function TimeFormatMeta(){
	this.format = "hh:mm:ss";
};
/**
 * 地址格式化信息
 */
AddressFormatMeta.prototype = new Object;

function AddressFormatMeta(){
	this.express = "C S T R P";
	this.separator = " ";
};