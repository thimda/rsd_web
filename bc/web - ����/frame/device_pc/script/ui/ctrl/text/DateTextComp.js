/**
 * @fileoverview date类型的Text输入控件.日期格式为 YYYY-MM-DD
 * 此控件输入合法性的校验是在输入框失去焦点或者按下回车键的时候进行一次校验
 * @author gd, guoweic
 * @version NC6.0
 * @since   NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 4.修正firefox下的校验问题 . guoweic <b>modified</b> 
 */

DateTextComp.prototype = new TextComp;
DateTextComp.prototype.componentType = "DATETEXT";

// 默认日期DIV高度
DateTextComp.CALANDER_HEIGHT = 225;
// 默认日期DIV宽度
DateTextComp.CALANDER_WIDTH = 255;

//日期时间Long值转换的时候 后台UFDate 跟 前台的 UTC 时间 差了8小时   8 * 60 * 60 * 1000 = 28800000
//DateTextComp.DATETME_LONG_OFFSET = 28800000;

/*add by chouhl 2012-1-10*/
DateTextComp.prototype.inputClassName_init = IS_IE7? "text_input" : "input_normal_center_bg text_input";
DateTextComp.prototype.inputClassName_inactive = IS_IE7? "text_input_inactive" : "input_normal_center_bg text_input_inactive";
//按钮图片宽高
DateTextComp.prototype.imageWidth = 15;
DateTextComp.prototype.imageHeight = 16;

/**
 * DateText构造函数
 * @class 日期输入框
 * @constructor	DateText构造函数
 */
function DateTextComp(parent, name, left, top, width, position, attrArr, className) {	
	//long转为时间
	if (attrArr && attrArr.value && attrArr.value.indexOf("/") == -1 && attrArr.value.indexOf("-")){
		var value = parseInt(attrArr.value);
		if (value == attrArr.value){
			var date = new Date();
			date.setTime(value);
			attrArr.value = this.dateFormat(date); 
		}
	}
	this.base = TextComp;
	this.base(parent, name, left, top, width, "D", position, attrArr, className);
	// 是否包括时间，默认为不包括
	this.showTimeBar = false;
};

/**
 * 覆盖父类的方法
 * @private
 */					  
DateTextComp.prototype.manageSelf = function() {
	TextComp.prototype.manageSelf.call(this);
	
	var oThis = this;
	//加入日期选择控件
	var height = this.Div_text.offsetHeight;
	var width = this.Div_text.offsetWidth;
	if (this.Div_text.className == "text_div"){
		width = width - 2;
		if (IS_FF)	
			width = width - 5;
	}
	if (!(IS_IE7 || IS_FF || IS_CHROME))
		width = width - 2;
	//ipad需要再减10	
	if (IS_IPAD){
		width = width - 10;
	}
		
	if (width == 0)
		width = parseInt(this.width);
	this.input.style.width = width - this.imageWidth + "px";

	this.divButton = $ce("DIV");
	//if (IS_IE7){
	//	this.divButton.style.position = "absolute";
	//	this.divButton.style.left = width - 18 + "px";
	//}
	//else{
		this.divButton.style.position = "relative";
		this.input.style.position = "relative";
		//this.divButton.style.left = "0px";
		if (IS_IE){
			this.divButton.style.styleFloat = "left";
			this.input.style.styleFloat = "left";
		}
		else{
			this.divButton.style.cssFloat = "left";
			this.input.style.cssFloat = "left";
		}
	//}		
	this.divButton.style.width = this.imageWidth + "px";
	this.divButton.id = "$date_sel_button";

	this.refImg = $ce("IMG");
	this.refImg.src = window.themePath + '/ui/ctrl/text/images/date_nm.gif';
	this.refImg.style[ATTRFLOAT] = "right";
	//this.refImg.style.marginRight = "1px";
	this.refImg.style.height = this.imageHeight + "px";
	this.refImg.style.width = this.imageWidth + "px";
	this.divButton.appendChild(this.refImg);
	/*modify by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		this.Div_text.children[1].appendChild(this.divButton);
	}else{
		this.Div_text.appendChild(this.divButton);
	}
	/********/
	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		var centerWidth = width - 3*2;//3*2左右边框图片宽度
		this.Div_text.children[1].style.width = centerWidth + "px";
		var imgWidth = (this.Div_text.children[1].children.length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
		this.input.style.width = (centerWidth - 2*2 - imgWidth) + "px";//2*2 input输入框距离左右间距
	}
	/********/
	// 设置图片的竖直位置聚中
	var offHgt = this.Div_text.offsetHeight;
	if (offHgt > 0 && (offHgt - this.refImg.height) > 0) {
		this.refImg.style.marginTop = "1px";
	}
	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		if(this.Div_text.children[1].offsetHeight > 0 && this.refImg.height > 0 && (this.Div_text.children[1].offsetHeight - this.refImg.height) > 0){
			this.refImg.style.marginTop = (this.Div_text.children[1].offsetHeight - this.refImg.height)/2 + "px";
		}
	}
	/********/
	// 失去键盘焦点时调用
	this.input.onblur = function(e) {	 
		//清除只读状态下的失去焦点操作
		//modify licza 2010-11-12
		if(this.readOnly)
			return;
		
		e = EventUtil.getEvent();
		/*add by chouhl 2012-1-10*/
		if(oThis.Div_text.children.length == 3){
			var children = oThis.Div_text.children;
			for(var i=0;i<children.length;i++){
				children[i].className = children[i].className.replaceStr('input_highlight','input_normal');
			}
			oThis.input.className = oThis.input.className.replaceStr('input_highlight_center_bg','input_normal_center_bg');
		}
		/********/
		oThis.blur(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.divButton.onmouseover = function(e) {
		oThis.refImg.src = window.themePath + "/ui/ctrl/text/images/date_on.gif";
	};
	
	this.divButton.onmouseout = function(e) {
		oThis.refImg.src = window.themePath + "/ui/ctrl/text/images/date_nm.gif";
	};
	
	this.divButton.onclick = function(e) {
		e = EventUtil.getEvent();
		// 调用该方法是为了点击该日期控件时隐藏别的日期控件使其失去焦点并将输入值设入到model
		e.calendar = true;
		var event = e;
		if (this.stopHideDiv != true) {
			window.clickHolders.trigger = oThis;
			document.onclick();
		}
		oThis.openCalendar(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//初始控件为禁用状态
	if (this.disabled) {	
		TextComp.prototype.setActive.call(this, false);
		//将与下拉按钮的各种动作事件保存
		this.divButtonClickFunc = this.divButton.onclick;
		this.divButtonMouseOutFuc = this.divButton.onmouseout;
		this.divButtonMouseOverFuc = this.divButton.onmouseover;
		 
		this.divButton.onclick = null;
		this.divButton.onmouseout = null;
		this.divButton.onmouseover = null;
		this.input.className = this.inputClassName_inactive;//"text_input_inactive";
		this.Div_text.className = this.className + " " + this.className + "_inactive_bgcolor";
		this.refImg.src = window.themePath + "/ui/ctrl/text/images/date_disable.gif";
	}
   
	//设置初始值
	if (this.value)
		this.setValue(this.value);
};

/**
 * 设置是否包括时间
 */
DateTextComp.prototype.setShowTimeBar = function(showTimeBar) {
	this.showTimeBar = showTimeBar;
};

/**
 * 处理回车事件
 * @private
 */
DateTextComp.prototype.processEnter = function() {
	var inputValue = this.input.value.trim();
	if (inputValue != "") {
		var dateValue = this.getFormater().format(this.getValue().trim());
		if (dateValue == "") {
			showVerifyMessage(this, trans("ml_dateisinvalid"));
			this.setFocus();
		} else {	
			this.setMessage(dateValue);
			this.input.value = dateValue;
		}
	}
	// 如果没有输入任何值点击回车,则设置今天日期
	else {
		var date = new Date();		
		var day = date.getDate();
		if (day < 10)
			day = new String("0" + day);
		var month = date.getMonth() + 1;
		if (month < 10)
			month = new String("0" + month);
		var year = date.getFullYear();
		this.setMessage(year + "-" + month + "-" + day);
		this.input.value = year + "-" + month + "-" + day;
	}	
	this.valueChanged(this.oldValue, this.input.value);
};

/**
 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
 * @private
 */
DateTextComp.prototype.createDefaultFormater = function() {	
	return new DateFormater();
};

/**
 * 失去焦点时进行输入的类型检查
 * @private
 */
DateTextComp.prototype.blur = function() {
	var value = this.input.value;
	if (value == ""){
		if (this.oldValue !=""){
			this.newValue = "";
			this.valueChanged(this.oldValue, this.newValue);
			this.onblur();
		}
		return;
	}
	if (!this.showTimeBar)
		this.newValue = this.getFormater().format(value);
	else
		this.newValue = this.getFormater().formatDateTime(value);
	if(this.verify(value)){
		if (this.newValue != this.oldValue)
			this.valueChanged(this.oldValue, this.newValue);
	}
	this.onblur();
};

/**
 * 取对应long值
 * private
 */
DateTextComp.prototype.getUtcValue = function() {
	var date = this.newValue;
	if (date.indexOf("-") > -1)
		date = date.replace(/\-/g,"/");
	var utcValue = Date.parse(date);
	if (isNaN(utcValue)) 
		return "";
	return utcValue.toString();	
};

/**
 * 把date类型格式化成yyyy-MM-dd HH:Mi:SS
 * @param {date} date
 * @return {String}   YYYY-MM-DD HH:Mi:SS
 * private
 */
DateTextComp.prototype.dateFormat = function(date) {
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	if (parseInt(month)<10) month = "0" + month;
	var day = date.getDate();
	if (parseInt(day)<10) day = "0" + day;
	var hours = date.getHours();
	if (parseInt(hours)<10) hours = "0" + hours;
	var minutes = date.getMinutes();
	if (parseInt(minutes)<10) minutes = "0" + minutes;
	var seconds = date.getSeconds();
	if (parseInt(seconds)<10) seconds = "0" + seconds;
	var formatString = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
	return formatString;
};

/**
	得到context信息
**/
DateTextComp.prototype.getContext = function() {
//	debugger;
	var context = TextComp.prototype.getContext.call(this);
	context.value = this.getUtcValue();
	return context;
};


/**
	设置context信息
**/
DateTextComp.prototype.setContext = function(context) {
//	debugger;
	if (context.value && context.value != ""){
		var date = new Date();
		date.setTime(parseInt(context.value));
		context.value = this.dateFormat(date); 
	}
	TextComp.prototype.setContext.call(this,context);
};


DateTextComp.prototype.verify = function(oldVlaue) {
	if (this.newValue == "") {
		this.input.value = "";
		showVerifyMessage(this, trans("ml_dateisinvalid"));
		this.valueChanged(this.oldValue, "");
		return false;
	}
	return true;
};
/**
 * 打开日期控件
 * @private
 */
DateTextComp.prototype.openCalendar = function(e) {		 
	var oThis = this;		
	var left = 0; 
	if (document.body.clientWidth < e.clientX + 100)
		left = e.clientX - 200 - 20;
	else
		left = e.clientX - 100 - 20;
	
	if(left < 0)
		left = 0;
	
	if (!this.calendar)
		this.calendar = new CalendarComp(false);
	this.calendar.onclick = function(date) {
		oThis.setFocus();		
		oThis.oldValue = oThis.getValue();
		var dateStr = date;
		if (!oThis.showTimeBar)
			dateStr = oThis.getFormater().format(date);
		// 通过选择获取日期不通过setValue设置值,避免通过setValue方法每次oldValue均相同的问题 gd:2008-01-29
		//othis.setValue(date);
		oThis.setValue(dateStr);
		oThis.valueChanged(oThis.oldValue, dateStr);
		
	};
	var top = compOffsetTop(this.input) + 20;
	//超过下边界
	if (top + DateTextComp.CALANDER_HEIGHT > document.body.clientHeight) {
		top = document.body.clientHeight - DateTextComp.CALANDER_HEIGHT;
	}
	//超过右边界
	if (left + DateTextComp.CALANDER_WIDTH > document.body.clientWidth){
		left = document.body.clientWidth - DateTextComp.CALANDER_WIDTH - 2;
	}
	
	this.calendar.show(left, top, this.showTimeBar);
};

/**
 * 覆盖基类的此方法,用于隐藏已经打开的日历控件
 */
DateTextComp.prototype.hideV = function() {	
	this.Div_gen.style.visibility = "hidden";
	this.visible = false;
	if (this.calendar != null)
		this.calendar.hide();
};

/**
 * 显示控件(显示属性是visibility)
 */
DateTextComp.prototype.showV = function() {
	var obj = this.getObjHtml();
	obj.style.visibility = "";
	this.visible = true;
	// 设置图片的竖直位置聚中
	var offHgt = this.Div_text.offsetHeight;
	if (offHgt > 0 && (offHgt - this.refImg.height) > 0) {
		this.refImg.style.marginTop = "1px";
	}
	if(this.Div_text.children.length == 3){
		if(this.Div_text.children[1].offsetHeight > 0 && this.refImg.height > 0 && (this.Div_text.children[1].offsetHeight - this.refImg.height) > 0){
			this.refImg.style.marginTop = (this.Div_text.children[1].offsetHeight - this.refImg.height)/2 + "px";
		}
	}
};

/**
 * 设置值
 */
DateTextComp.prototype.setValue = function(text) {
//	debugger;
	var tempText = text;
	if (!this.showTimeBar)
		tempText = this.getFormater().format(text);
	TextComp.prototype.setValue.call(this, tempText);	
};

DateTextComp.prototype.maskValue = function(){
	var maskerType = this.showTimeBar ? "DateTimeText" : this.componentType;
	var masker = getMasker(maskerType);
	if(masker != null)
		this.showValue = toColorfulString(masker.format(this.newValue));
	else
		this.showValue = this.newValue;
};
/**
 * 设置大小和位置
 */
DateTextComp.prototype.setBounds = function(left, top, width, height) {
	this.left = left;
	this.top = top;
	this.width = getString(width, "100%");
	this.height = getString(height, "100%");
	
	// 设置最外层的大小
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";

	// 设置输入区域的大小
	this.Div_text.style.width = this.width - 4 + "px";
	if (this.hasLabel)
		this.Div_text.style.width = this.width - this.labelWidth - 10 + "px";
	//this.Div_text.style.height = this.height - 4 + "px";
	
	var pixelHeight = this.Div_text.offsetHeight;
	var pixelWidth = this.Div_text.offsetWidth;
	if (this.Div_text.className == "text_div" || this.Div_text.className == "text_div text_div_focus_bgcolor"){
		pixelWidth = pixelWidth - 2;
		if (IS_FF)	
			pixelWidth = pixelWidth - 5;
	}
	if (!(IS_IE7 || IS_FF || IS_CHROME))
		pixelWidth = pixelWidth - 2; 
	//ipad需要再减10	
	if (IS_IPAD){
		pixelWidth = pixelWidth - 10;
	}
	this.input.style.width = pixelWidth - this.imageWidth + "px";
	if (IS_IE7)
		//this.divButton.style.left = pixelWidth - 18 + "px";
	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		var centerWidth = pixelWidth - 3*2;//3*2左右边框图片宽度
		this.Div_text.children[1].style.width = centerWidth + "px";
		var imgWidth = (this.Div_text.children[1].children.length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
		this.input.style.width = (centerWidth - 2*2 - imgWidth) + "px";//2*2 input输入框距离左右间距
	}
	/********/
};

/**
 * 设置日期输入框控件的激活状态.
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
DateTextComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	//清除只读状态
//	if(this.readOnly&&!this.disabled) 
//		this.setReadOnly(false); 
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		TextComp.prototype.setActive.call(this, false);
		//将与下拉按钮的各种动作事件保存
		this.divButtonClickFunc = this.divButton.onclick;
		this.divButtonMouseOutFuc = this.divButton.onmouseout;
		this.divButtonMouseOverFuc = this.divButton.onmouseover;
		 
		this.divButton.onclick = null;
		this.divButton.onmouseout = null;
		this.divButton.onmouseover = null;
		this.refImg.src = window.themePath + "/ui/ctrl/text/images/date_disable.gif";
		this.input.className = this.inputClassName_inactive;//"text_input_inactive";
		this.Div_text.className = this.className + " " + this.className + "_inactive_bgcolor";
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {	
		TextComp.prototype.setActive.call(this, true);
		this.divButton.onclick = this.divButtonClickFunc;
		this.divButton.onmouseout = this.divButtonMouseOutFuc;
		this.divButton.onmouseover = this.divButtonMouseOverFuc;
		this.refImg.src = window.themePath + "/ui/ctrl/text/images/date_nm.gif";
		this.input.className = this.inputClassName_init;//"text_input";
		this.Div_text.className = this.className;
	}
};
/**
 * 设置只读状态
 */
DateTextComp.prototype.setReadOnly = function(readOnly) {
//	if (this.readOnly==readOnly)
//		return;
//	if (this.disabled&&!this.readOnly) {
//		this.setActive(true);
//	}
	this.input.readOnly = readOnly;
	this.readOnly=readOnly;
	if (readOnly) {
//		this.Div_text.style.borderWidth="0";
		this.Div_text.className = this.className + " " + this.className + "_readonly";
		this.input.className = this.inputClassName_init + " text_input_readonly";//"text_input text_input_readonly";
//		this.refImg.style.visibility="hidden";
		this.divButton.style.visibility="hidden";
	} else {
//		this.Div_text.style.borderWidth="1px";
		this.Div_text.className = this.className;
		this.input.className = this.inputClassName_init;//"text_input";
//		this.refImg.style.visibility="";
		this.divButton.style.visibility="";
	}
};

/**
 * 得到输入框的激活状态
 */
DateTextComp.prototype.isActive = function() {
	return !this.disabled;
};