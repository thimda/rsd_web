/**
 * 抽象格式化类
 */
function AbstractMasker() {	
};

AbstractMasker.prototype.format = function(obj){
	if(obj == null)
		return null;
		
	var fObj = this.formatArgument(obj);
	return this.innerFormat(fObj);
};
	
/**
 * 统一被格式化对象结构
 * 
 * @param obj
 * @return
 */
AbstractMasker.prototype.formatArgument = function(obj){
	
};
	
/**
 * 格式化
 * 
 * @param obj
 * @return
 */
AbstractMasker.prototype.innerFormat = function(obj){
	
};

/**
 * 拆分算法格式化虚基类
 */
AbstractSplitMasker.prototype = new AbstractMasker;
function AbstractSplitMasker() {	
};
AbstractSplitMasker.prototype.elements = new Array ;
AbstractSplitMasker.prototype.format = function(obj){
	if(obj == null)
		return null;
		
	var fObj = this.formatArgument(obj);
	return this.innerFormat(fObj);
};
	
/**
 * 统一被格式化对象结构
 * 
 * @param obj
 * @return
 */
AbstractSplitMasker.prototype.formatArgument = function(obj){
	return obj;
};
	
/**
 * 格式化
 * 
 * @param obj
 * @return
 */
AbstractSplitMasker.prototype.innerFormat = function(obj){
	if(obj == null || obj == "")
	   return new FormatResult(obj);
	this.doSplit();
	var result = "";
	for(var i = 0; i < this.elements.length ; i++){
		if(i != undefined){
			var element = this.elements[i];
			var elementValue = element.getValue(obj);
			if(elementValue != undefined)
				result = result + elementValue;
		}
	}
	return new FormatResult(result);
};

AbstractSplitMasker.prototype.getExpress = function() {
	
};

AbstractSplitMasker.prototype.doSplit = function(){
	var express = this.getExpress();
	if(this.elements == null || this.elements.length == 0)
		this.elements = this.doQuotation(express, this.getSeperators(), this.getReplaceds(), 0);
};


/**
 * 处理引号
 * 
 * @param express
 * @param seperators
 * @param replaced
 * @param curSeperator
 * @param obj
 * @param result
 */
AbstractSplitMasker.prototype.doQuotation = function(express, seperators, replaced, curSeperator){
	if(express.length == 0)
		return null;
	var elements = new Array();
	var pattern = new RegExp('".*?"',"g");
	var fromIndex = 0;
	var result ;
	do
	{
		result = pattern.exec(express);
		if(result != null){
		  var i = result.index;
		  var j = pattern.lastIndex ;
		  if(i != j){
				if(fromIndex < i){
					var childElements = this.doSeperator(express.substring(fromIndex, i), seperators, replaced, curSeperator);
					if(childElements != null && childElements.length > 0){
						elements = elements.concat(childElements);
					}
				}
		  }
			elements.push(new StringElement(express.substring(i + 1, j-1)));
			fromIndex = j;
		}
	}
	while (result!=null)
	
	if(fromIndex < express.length ){
		var childElements = this.doSeperator(express.substring(fromIndex, express.length ), seperators, replaced, curSeperator);
		if(childElements != null && childElements.length > 0)
			elements = elements.concat(childElements);
	}
	return elements;
};
	
/**
 * 处理其它分隔符
 * 
 * @param express
 * @param seperators
 * @param replaced
 * @param curSeperator
 * @param obj
 * @param result
 */
AbstractSplitMasker.prototype.doSeperator = function(express, seperators, replaced, curSeperator){
	if(curSeperator >= seperators.length){
		var elements = new Array;
		elements.push(this.getVarElement(express));
		return elements;
	}
		
	if(express.length == 0)
		return null;
	var fromIndex = 0;
	var elements = new Array();
	var pattern = new RegExp(seperators[curSeperator],"g");
	var result ;
	do
	{
		result = pattern.exec(express);
		if(result != null){
			var i =  result.index ;
			var j = pattern.lastIndex;
			if(i != j){
				if(fromIndex < i){
					var childElements = this.doSeperator(express.substring(fromIndex, i), seperators, replaced, curSeperator + 1);
					if(childElements != null && childElements.length > 0)
						elements = elements.concat(childElements);
				}

				if(replaced[curSeperator] != null){
					elements.push(new StringElement(replaced[curSeperator]));
				}else{
					elements.push(new StringElement(express.substring(i, j)));
				}
				fromIndex = j;
			}
		}
	}
	while (result!=null)
	
	if(fromIndex < express.length ){
		var childElements = this.doSeperator(express.substring(fromIndex, express.length ), seperators, replaced, curSeperator + 1);
		if(childElements != null && childElements.length > 0)
			elements = elements.concat(childElements);
	}
	return elements;
};


/**
 * 地址格式
 */
AddressMasker.prototype = new AbstractSplitMasker;
function AddressMasker(formatMeta) {
	this.formatMeta = formatMeta;
};

AddressMasker.prototype.getExpress = function() {
	return this.formatMeta.express;
};

AddressMasker.prototype.getReplaceds = function() {
	return [this.formatMeta.separator];
};

AddressMasker.prototype.getSeperators = function() {
	return ["(\\s)+?"];
};

AddressMasker.prototype.getVarElement = function(express) {
	var ex = {};
	
	if(express == ("C")) 
			ex.getValue = function(obj){
				return obj.country;
			};
 
		
		if(express == ("S"))
		ex.getValue = function(obj){
			return obj.state;
		};
		 
	
	if(express == ("T"))
		ex.getValue = function(obj){
			return obj.city;
		};
		 
	
	if(express == ("D"))
		ex.getValue = function(obj){
			return obj.section;
		};
		 
	
	if(express == ("R"))
		ex.getValue = function(obj){
			return obj.road;
		};
		 
	if(express == ("P"))
		ex.getValue = function(obj){
			return obj.postcode;
		};

	if(typeof(ex.getValue) == undefined)
		return new StringElement(express);
	else
		return ex;
};
 
AddressMasker.prototype.formatArgument = function(obj){
		return obj;
};

/**
 * <b> 数字格式化  </b>
 *
 * <p> 格式化数字 
 *
 * </p>
 *
 * Create at 2009-3-20 上午08:50:32
 * 
 * @author bq 
 * @since V6.0
 */
NumberMasker.prototype = new AbstractMasker;
NumberMasker.prototype.formatMeta = null;

/**
*格式化对象
*/
NumberMasker.prototype.innerFormat = function (obj){
  var dValue, express, seperatorIndex, strValue;
  dValue = obj.value;
  if (dValue > 0) {
    express = this.formatMeta.positiveFormat;
    strValue = dValue + '';
  }
   else if (dValue < 0) {
    express = this.formatMeta.negativeFormat;
    strValue = (dValue + '').substr(1, (dValue + '').length - 1);
  }
   else {
    express = this.formatMeta.positiveFormat;
    strValue = dValue + '';
  }
  seperatorIndex = strValue.indexOf('.');
  this.setTheSeperator(strValue, seperatorIndex);
  this.setTheMark(strValue, seperatorIndex);
  var color = null;
  if(dValue < 0 && this.formatMeta.isNegRed){
  	color = "FF0000";
  }	
  return new FormatResult(express.replaceAll('n', strValue) ,color);
 
};
/**
*设置标记
*/
NumberMasker.prototype.setTheMark = function (str, seperatorIndex){
  var endIndex, first, index;
  if (!this.formatMeta.isMarkEnable)
    return;
  if(seperatorIndex <= 0)
	seperatorIndex = str.length;
  first = str.charCodeAt(0);
  endIndex = 0;
  if(first == 45)
  	endIndex = 1;
  index = seperatorIndex - 3;
  while (index > endIndex) {
    str = str.substr(0, index - 0) + this.formatMeta.markSymbol + str.substr(index, str.length - index);
    index = index - 3;
  }
  
};
NumberMasker.prototype.setTheSeperator = function(str, seperatorIndex){
	  var ca;
	  if (seperatorIndex > 0) {
	    ca = NumberMasker.toCharArray(str);
	    ca[seperatorIndex] = NumberMasker.toCharArray(this.formatMeta.pointSymbol)[0];
	    str =  ca.join('');
	  }
};
/**
 * 将字符串转换成char数组
 * @param {} str
 * @return {}
 */
NumberMasker.toCharArray = function(str){
	var charArray = new Array();
	for(var i=0 ; i< str.length ;i++){
	    charArray.push(str[i]);
	}
	return charArray;
};

/**
*构造方法
*/
function NumberMasker(formatMeta){
  this.formatMeta = formatMeta;
};
/**
*默认构造方法
*/
NumberMasker.prototype.formatArgument = function(obj){
	var numberObj = {};
	numberObj.value = obj;
	return numberObj;
}; 

/**
 * 货币格式
 */
CurrencyMasker.prototype = new NumberMasker;
CurrencyMasker.prototype.formatMeta = null;
function CurrencyMasker(formatMeta){
	this.formatMeta = formatMeta;
};
/**
 * 重载格式方法
 * @param {} obj
 * @return {}
 */
CurrencyMasker.prototype.innerFormat = function(obj){
	var fo = (new NumberFormat(this.formatMeta)).innerFormat(obj);
	fo.value = fo.value.replace("$", this.formatMeta.curSymbol);
	return fo;
};

DateTimeMasker.prototype = new AbstractSplitMasker;
/**
 * 英文短日期
 */
DateTimeMasker.enShortMonth = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
/**
 * 英文长日期 
 */
DateTimeMasker.enLongMonth = ["January","February","March","April","May","June","July","August","September","October","November","December"];
DateTimeMasker.prototype.formatMeta = null;
function DateTimeMasker(formatMeta){
	this.formatMeta = formatMeta;
};
	
DateTimeMasker.prototype.doOne = function(express){
	if(express.length == 0)
		return new "";
	var obj = new Object;
	if(express == "yyyy"){
		obj.getValue = function(o) {
			return DateTimeMasker.getyyyy(o);;
		};
	}
	 if(express == "yy"){
		obj.getValue = function(o) {
			return DateTimeMasker.getyy(o);
		};
	}
	if(express == "MMMM"){
		obj.getValue = function(o) {
			return DateTimeMasker.getMMMM(o);
		};
	}
		
	if(express == "MMM"){
		obj.getValue = function(o) {
			return DateTimeMasker.getMMM(o);
		};
	}
			
	if(express == "MM"){
		obj.getValue = function(o) {
			return DateTimeMasker.getMM(o);
		};
	}
			
	if(express == "M"){
		obj.getValue = function(o) {
			return DateTimeMasker.getM(o);
		};
	}
	
	if(express == "dd"){
		obj.getValue = function(o) {
			return DateTimeMasker.getdd(o);
		};
	}
	
	if(express == "d"){
		obj.getValue = function(o) {
			return DateTimeMasker.getd(o);
		};
	}
	
	if(express == "hh"){
		obj.getValue = function(o) {
			return DateTimeMasker.gethh(o);
		};
	}
	
	if(express == "h"){
		obj.getValue = function(o) {
			return DateTimeMasker.geth(o);
		};
	}
	
	if(express == "mm"){
		obj.getValue = function(o) {
			return DateTimeMasker.getmm(o);
		};
	}
	
	if(express == "m"){
		obj.getValue = function(o) {
			return DateTimeMasker.getm(o);
		};
	}
	
	if(express == "ss"){
		obj.getValue = function(o) {
			return DateTimeMasker.getss(o);
		};
	}
	
	if(express == "s"){
		obj.getValue = function(o) {
			return DateTimeMasker.gets(o);
		};
	}
	
	if(express == "HH"){
		obj.getValue = function(o) {
			return DateTimeMasker.getHH(o);
		};
	}
	
	if(express == "H"){
		obj.getValue = function(o) {
			return DateTimeMasker.getH(o);
		};
	}
	if(express == "t"){
		obj.getValue = function(o) {
			return DateTimeMasker.gett(o);
		};
	}
	if(typeof(obj.getValue) == undefined)
		return express;
	else
		return obj;
};
	
DateTimeMasker.getyyyy = function(date){
	return date.getFullYear();
};
	
DateTimeMasker.getyy = function(date){
	return (""+date.getFullYear()).substring(2);
};
	
DateTimeMasker.getM = function(date){
	return ""+(date.getMonth() + 1);
};
	
DateTimeMasker.getMM = function(date){
	var month = date.getMonth() + 1;
	if(month < 10)
		return "0" + month;
	return month;
};
	
DateTimeMasker.getMMM = function(date){
	return this.enShortMonth[date.getMonth()-1];
};
	
DateTimeMasker.getMMMM = function(date){
	return this.enLongMonth[date.getMonth()-1];
};
	
DateTimeMasker.getdd = function(date){
	var day = date.getDate();
	if(day < 10)
		return "0" + day;
	return date.getDate()+"";
};
	
DateTimeMasker.getd = function(date){
	return date.getDate()+"";
};
	
DateTimeMasker.gethh = function(date){
	var hh = date.getHours();
	if(hh < 10)
		return "0" + hh;
	
	return (date.getHours())+"";
};
	
DateTimeMasker.geth = function(date){
	return (date.getHours())+"";
};
	
DateTimeMasker.getHH = function(date){
	var HH = date.getHours();
	
	if(HH >= 12)
		HH = HH - 12;
	
	if(HH < 10)
		return "0" + HH;
	return (HH)+"";
};
	
DateTimeMasker.getH = function(date){
	var HH = date.getHours();
	
	if(HH >= 12)
		HH = HH - 12;
	
	return (HH)+"";
};
	
DateTimeMasker.getmm = function(date){
	var mm = date.getMinutes();
	if(mm < 10)
		return "0" + mm;
	
	return  (date.getMinutes())+"";
};
	
DateTimeMasker.getm = function(date){
	return ""+(date.getMinutes());
};
	
DateTimeMasker.getss = function(date){
	var ss = date.getSeconds();
	if(ss < 10)
		return "0" + ss;
	
	return (ss)+"";
};
	
DateTimeMasker.gets = function(date){
	return (date.getSeconds())+"";
};
	
DateTimeMasker.gett = function(date){
	var hh = date.getHours();
	if(hh <= 12)
		return "AM";
	else
		return "PM";
};
	
DateTimeMasker.prototype.getExpress = function() {
	return this.formatMeta.format;
};

DateTimeMasker.prototype.getReplaceds = function() {
	return [" ",this.formatMeta.speratorSymbol,":"];
};

DateTimeMasker.prototype.getSeperators = function() {
	return ["(\\s)+?","-",":"];
};

DateTimeMasker.prototype.getVarElement = function(express) {
	return this.doOne(express);
};

DateTimeMasker.prototype.formatArgument = function(obj){ 
	if (obj == 0) return "";
	if(obj == null || obj =="")
		return obj;
	if((typeof obj) == "string"){
		var dateArr = obj.split(" ");
		if(dateArr.length >0){
			var arr0 = dateArr[0].split("-");
			var date = new Date();
			//先把日期设置为1日，解决bug:当前日期为2011-08-31时，选择日期为2011-09-X，会把日期格式化为2011-10-X
			date.setDate(1);
			date.setFullYear(parseInt(arr0[0],10));
			date.setMonth(parseInt(arr0[1],10) -1);
			date.setDate(parseInt(arr0[2],10));
			if(dateArr.length == 2 && dateArr[1] != undefined){
				var arr1 = dateArr[1].split(":");
				date.setHours(parseInt(arr1[0],10));
				date.setMinutes(parseInt(arr1[1],10));
				date.setSeconds(parseInt(arr1[2],10));
				if(arr1.length > 3)
					date.setMilliseconds(parseInt(arr1[3],10));
			}
		}
		return date;
	}
	return (obj);
};

/**
 * 日期格式化
 */
DateMasker.prototype = new DateTimeMasker;
function DateMasker(formatMeta){
   this.formatMeta = formatMeta; 
};
/**
 * 时间格式化
 */
TimeMasker.prototype = new DateTimeMasker;
function TimeMasker(formatMeta){
	 this.formatMeta = formatMeta;
};
