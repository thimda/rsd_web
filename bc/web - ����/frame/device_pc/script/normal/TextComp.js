/**
 * @fileoverview Text输入控件基类,所有的输入控件均继承此类.
 * DataType: A:字符,R:reference 
 * @author gd, guoweic
 * @version 6.0
 * @since 5.5 
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 4.修正firefox下的校验问题 . guoweic <b>modified</b>
 */
TextComp.prototype = new BaseComponent;
TextComp.prototype.componentType = "TEXT";
/*add by chouhl 2012-1-10*/
TextComp.prototype.inputClassName_init = IS_IE7? "text_input" : "input_normal_center_bg text_input";
TextComp.prototype.inputClassName_inactive = IS_IE7? "text_input_inactive" : "input_normal_center_bg text_input_inactive";
/********/
/**
 * 文本输入框控件构造函数
 * @class 文本输入框控件基类 
 * @constructor TextComp的构造函数	
 *	
 * @param maxSize 此变量指定的是字节数,中文每个汉字的字节数是2,英文每个字母的字节数是1
 */
function TextComp(parent, name, left, top, width, dataType, position, attrArr, className) {
	if (arguments.length == 0)
		return;
		
	this.base = BaseComponent;
	this.base(name, left, top, width, "");  
	this.dataType = getString(dataType, "A");		
	this.parentOwner = parent;
	
	this.position = getString(position, "absolute");
	if (this.dataType == "P")
		this.inputType= "password";
	else if (this.dataType == "HI")
		this.inputType = "hidden";
   	else if (this.dataType == "C")
   		this.inputType = "checkbox";
   	else if (this.dataType == "F") {
   		this.inputType = "file";
   	}
   	else
		this.inputType= "text";
	
	//是否大写
	this.uppercase = false;
	//输入框默认大小
	this.maxSize = -1;
	this.value = getString(this.value, "");
	this.disabled = false;
	this.readOnly = false;
	//准备Focus 
	this.mayFocus = false;
	// 当前是否是校验失败状态
	this.isError = false;
	// 当前输入框校验状态，包括：AutoFormComp.ELEMENT_ERROR、AutoFormComp.ELEMENT_WARNING、AutoFormComp.ELEMENT_NORMAL、AutoFormComp.ELEMENT_SUCCESS，默认为AutoFormComp.ELEMENT_SUCCESS
	this.checkResult = AutoFormComp.ELEMENT_NORMAL;
	// 信息（包括title）
	this.message;
	// 错误信息（包括title和错误提示）
	this.errorMessage;
	
	if (attrArr != null) {   
		this.maxSize = getInteger(attrArr.maxSize, this.maxSize);	
		this.disabled = getBoolean(attrArr.disabled, false);	 
		this.value = getString(attrArr.value, this.value);		
		this.readOnly = getBoolean(attrArr.readOnly, false);
		this.tabIndex = getInteger(attrArr.tabIndex, -1);
		this.tip = getString(attrArr.tip, "");
		// 输入辅助提示信息（和错误提示相互替代）
		this.inputAssistant = getString(attrArr.inputAssistant, "");
		if (attrArr.height != null)
			this.height = attrArr.height;
		
		//TODO 标签属性
		if (this.dataType != "C" && this.dataType != "RA") {
			this.labelText = getString(attrArr.labelText, "");
			if ("" != this.labelText)
				this.hasLabel = true;
			this.labelAlign = getString(attrArr.labelAlign, "left");
			this.labelWidth = getInteger(attrArr.labelWidth, 0);
			if (0 == this.labelWidth && "" != this.labelText)
				this.labelWidth = getTextWidth(this.labelText) + 5;
		}
		
		// 是否阻止document.click();方法的执行（用于在打开的Div中定义该输入框的情况）
		this.stopHideDiv = attrArr.stopHideDiv == true ? true : false;
		
	}	  
	// 只有字符串类型才需要此处理
	if (this.dataType == 'A' || this.dataType == 'P') {
		if( this.maxSize != -1) {
			// 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
			if (this.value.lengthb() > this.maxSize)
				this.value = this.value.substrCH(this.maxSize);
		}		
	}	
	 
	// 如果是日期类型或者参照类,因为有下拉框.需要外部事件回调
//	if(this.dataType == "D" || this.dataType == "R")
//	   window.clickHolders.push(this);
	this.className = getString(className, "text_div");
	this.create();
};

/**
 * 真正的创建函数
 * @private
 */
TextComp.prototype.create = function() {		
	var oThis = this;
	this.Div_gen = $ce("DIV");
	
	this.Div_gen.id = this.id;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";

	if (!isPercent(this.width)) {
		this.width = parseInt(this.width) + "px";
	}
	this.Div_gen.style.width = this.width;
	/*add by chouhl 2012-1-10*/
	this.Div_gen.style.height = "100%";
	/********/
  	this.Div_gen.style.overflow = "hidden";
	if (this.parentOwner) 
		this.placeIn(this.parentOwner);
	//modify by licza 2010-11-12
	this.setReadOnly(this.readOnly);
};


/**
 * TextComp的二级回掉函数
 * @private
 */
TextComp.prototype.manageSelf = function() {
	
	this.Div_text = $ce("DIV");
	this.Div_text.id = this.id + "_textdiv";
	this.Div_text.className = this.className;
	this.Div_text.style.position = "relative";
	this.Div_text.style.top = "0px";
	if (this.width && this.width.indexOf("%") == -1)
		var width = this.width.replace("px","");
	else
		var width = this.Div_gen.offsetWidth;
	this.Div_text.style.width = (width - 4) < 0?0:(width - 4) + "px";
	if (this.hasLabel)
		this.Div_text.style.width = (this.Div_gen.offsetWidth - this.labelWidth - 12) < 0?0:(this.Div_gen.offsetWidth - this.labelWidth - 12) + "px";
  	this.Div_text.style.overflow = "hidden";

	if (this.hasLabel) {  // 有标签
		this.labelDiv = $ce("DIV");
		this.labelDiv.style.width = (this.labelWidth - 4) < 0?0:(this.labelWidth - 4) + "px";
		
		if (this.labelAlign == "left") {
			this.labelDiv.style[ATTRFLOAT] = "left";
			this.Div_text.style[ATTRFLOAT] = "right";
			this.Div_gen.appendChild(this.labelDiv);
		  	this.Div_gen.appendChild(this.Div_text);
		} else if (this.labelAlign == "right") {
			this.Div_text.style[ATTRFLOAT] = "left";
			this.labelDiv.style[ATTRFLOAT] = "right";
		  	this.Div_gen.appendChild(this.Div_text);
			this.Div_gen.appendChild(this.labelDiv);
		}
		
		this.label = new LabelComp(this.labelDiv, this.id + "_label", "0px", "3px", this.labelText, "relative", null);
	} else {
		this.Div_gen.appendChild(this.Div_text);
	}
	
	var oThis = this;
	this.input = $ce("INPUT");
//	if(window.editMode && this.inputType == "text" || this.inputType == "password"){
//		this.input.type = "button";
//	}else
	this.input.type = this.inputType;
	
	/*add by chouhl 2012-1-10*/
	if(this.Div_gen.id != 'userid' && this.Div_gen.id != 'password'){
		var left_div = $ce("DIV");
		left_div.className = "input_normal_left_bg";
		this.Div_text.appendChild(left_div);
		
		var center_div = $ce("DIV");
		center_div.className = "input_normal_center_bg";
		center_div.style.width = (parseInt(this.Div_text.style.width) - 3*2) + "px";//3*2左右边框图片宽度
		center_div.appendChild(this.input);
		this.Div_text.appendChild(center_div);
		if(IS_IE7){
			this.input.style.marginTop = "3px";
		}
		
		var right_div = $ce("DIV");
		right_div.className = "input_normal_right_bg";
		this.Div_text.appendChild(right_div);
	}else{
		this.Div_text.appendChild(this.input);
	}
	/********/
	
	// 数字型控件的数字居右显示
	if (this.dataType == "I" || this.dataType == "N")
		this.input.style.textAlign = "right";
	this.input.name = this.id;
	if (this.tabIndex != -1)
		this.input.tabIndex = this.tabIndex;
	if (this.maxSize != -1)
		this.input.maxLength = this.maxSize;
	
	this.input.readOnly = this.readOnly;
	/** Ios **/
	if(IS_IOS)
		this.input.style.width = (this.Div_text.offsetWidth - 6) + "px" ;
	else
		this.input.style.width = (this.Div_text.offsetWidth - 10) + "px";
	/*add by chouhl 2012-1-10*/
	if(this.Div_text.children.length == 3){
		var centerWidth = this.Div_text.offsetWidth - 3*2;//3*2左右边框图片宽度
		this.Div_text.children[1].style.width = centerWidth + "px";
		this.input.style.width = (centerWidth - 2*2) + "px";//2*2 input输入框距离左右间距
	}
	/********/
	if (!IS_IE6)
		this.input.style.height = "100%";
	this.input.className = this.inputClassName_init;//"input_normal_center_bg text_input";
	
	if (this.disabled) {
		this.input.disabled = true;	
	   	this.input.className = this.inputClassName_inactive;//"input_normal_center_bg text_input_inactive";
		this.Div_text.className = this.className + " text_inactive_bgcolor";
	}
	
	this.input.onkeyup = function(e) {
		e = EventUtil.getEvent();
		oThis.onkeyup(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	this.input.onkeydown = function(e) {
		// readOnly时不允许输入字母和汉字
		// "tab"键,"shift+tab"键,"enter"键即使在readOnly下也是允许输入
		//var con = ((keyCode == 9 && e.shiftKey) || keyCode == 9 || keyCode == 13);
		e = EventUtil.getEvent();
		var keyCode = e.key;
				
		// 调用用户的方法	
		if (oThis.onkeydown(e) == false) {
			stopAll(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}	
		if (keyCode == 8 || keyCode == 46)
			oThis.haschanged(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//当用户按下一个键或放开一个键时调用
	this.input.onkeypress = function (e) {
		e = EventUtil.getEvent();
		var keyCode = e.key;
		// readOnly时不允许输入字母和汉字
		// "enter"键即使在readOnly下也是允许输入
		var con = (keyCode == 13 || (keyCode == 9 && e.shiftKey) || keyCode == 9);
		// readOnly时不允许输入
		if (this.readOnly == true && !con) {
			stopAll(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return false;
		}
		//得到格式化器
		var formater = oThis.getFormater();
		//获取输入字符
		if (keyCode == 13) {
			oThis.newValue = oThis.input.value;
			if (oThis.processEnter)
				oThis.processEnter();
			oThis.onenter(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			//回车后失去焦点
			oThis.input.blur();
			return true;
		}
		//for firefox
		else if (keyCode == 8 || keyCode == 46) {
			oThis.haschanged(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return true;
		}
		// firefox中忽略校验 左方向键、右方向键、Tab键
		if (!IS_IE && (keyCode == 37 || keyCode == 39 || keyCode == 9)) {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return true;
		}
		var key;
		if (keyCode) {	
			//从字符编码创建一个字符串,字符串中的每个字符串都由单独的数字Unicode编码指定
			key = String.fromCharCode(keyCode);
		}
		//IE下增加录入时即校验的特性，在firefox下还无法实现（guoweic: 已实现）
		var currValue = oThis.input.value;
		if (oThis.dataType != "F") {
			var aggValue = insertAtCursor(this, key);
		}
		//guoweic: firefox中insertAtCursor方法只能得到输入前的值
		if (!IS_IE && aggValue == "") {
			aggValue = key;
		}
		//this.focus(e);
		if (IS_IE) {
			// guoweic: 该方法只有IE支持
			document.execCommand("undo");
		}
		// 获取keyValue,currValue,aggValue,
		if (formater.valid(key, aggValue, currValue) == false) {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return false;
	   	}
	   	//验证完成输入合法性后调用程序的内部处理
	   	if (oThis.afterValid(keyCode, key) == false) {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
	   		return false;
	   	}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	   	return true;
	};
	
	//得到键盘焦点时调用
	this.input.onfocus = function (e) { 
		e = EventUtil.getEvent();
		if (oThis.input.value != oThis.newValue){
			oThis.input.value = oThis.newValue;
			//保证光标在最后
			//var r = oThis.input.createTextRange();
			//r.collapse(true);
			//r.moveStart('character',oThis.input.value.length);
			//r.select();
		}
		/*add by chouhl 2012-1-10*/
		if(oThis.Div_text.children.length == 3){
			var children = oThis.Div_text.children;
			for(var i=0;i<children.length;i++){
				children[i].className = children[i].className.replaceStr('input_normal','input_highlight');
			}
			oThis.input.className = oThis.input.className.replaceStr('input_normal_center_bg','input_highlight_center_bg');
		}
		/********/
		oThis.focus(e);
		oThis.hideTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//失去键盘焦点时调用
	this.input.onblur = function (e) {
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
		//var newValue = oThis.getFormater().format(this.value);
		if(!oThis.ingrid){
			oThis.setValue(oThis.input.value);
		}
		oThis.showTip();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.input.onselect = function (e) {
		e = EventUtil.getEvent();
		oThis.onselect(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.input.onmouseover = function (e) {
		e = EventUtil.getEvent();
		oThis.onmouseover(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	this.input.onclick = function(e) {
		e = EventUtil.getEvent();
		oThis.onclick(e);
		window.clickHolders.trigger = oThis;
		document.onclick(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
	//设置初始值
	if (this.value != null)
		this.setValue(this.value);
	else
		this.showTip();
};

/**
 * 设置当前是否是校验失败状态
 * @private
 */
TextComp.prototype.setError = function(error) {
	this.isError = error;
};

/**
 * 校验是否有默认提示信息
 * @private
 */
TextComp.prototype.checkTip = function() {
	if (this.tip != null && this.tip != "" && this.input.type == "text")
		return true;
	else
		return false;
};

/**
 * 显示提示信息
 * @private
 */
TextComp.prototype.showTip = function() {
	if (this.checkTip()) {
		if (this.input.value == "") {
			this.input.value = this.tip;
			this.input.style.color = "gray";
		}
	}
};

/**
 * 隐藏提示信息
 * @private
 */
TextComp.prototype.hideTip = function() {
	if (this.checkTip()) {
		if (this.input.value == this.tip) {
			this.input.value = "";
			this.input.style.color = "black";
		}
	}
};

/**
 * 设置控件的格式化器
 * @param formater 格式化器实例
 * @private
 */
TextComp.prototype.setFormater = function(formater) {
	this.formater = formater;	
};

/**
 * @private
 */
TextComp.prototype.setTabIndex = function(index) {
	this.input.tabIndex = index;
};

/**
 * 得到格式化器
 * @private
 */
TextComp.prototype.getFormater = function() {
	if(this.formater == null)
		return	this.createDefaultFormater();
};

/**
 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
 * @private
 */
TextComp.prototype.createDefaultFormater = function() {
	return new StringFormater(this.maxSize);
};

/**
 * 输入验证合法后,要做的事情,比如处理回车事件等.子类必须覆盖此函数
 * @param key 键盘输入的字符
 * @param keyCode 键盘码值
 * @private
 */
TextComp.prototype.afterValid = function(keyCode, key) {
};

/**
 * 设置组件大小及位置
 */
TextComp.prototype.setBounds = function(left, top, width, height) {
	this.left = left;
	this.top = top;
	this.width = getString(width, "100%");
	this.height = getString(height, "100%");
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";
	
	if (this.Div_text != null) {
		this.Div_text.style.width = this.width - 4 + "px";
		if (this.hasLabel)
			this.Div_text.style.width = this.width - this.labelWidth - 10 + "px";
			
		//this.Div_text.style.height = this.height - 4 + "px";
		/*add by chouhl 2012-1-10*/
		if(this.Div_text.children.length == 3){
			var centerWidth = this.Div_text.offsetWidth - 3*2;//3*2左右边框图片宽度
			this.Div_text.children[1].style.width = centerWidth + "px";
			this.input.style.width = (centerWidth - 2*2) + "px";//2*2 input输入框距离左右间距
		}
		/********/
		//this.input.style.height = this.height - 4 + "px";
	}
};

/**	
 * 控件外部右键点击事件回调函数。须关闭此控件的下拉菜单
 * @private
 */
TextComp.prototype.outsideContextMenuClick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("outsideContextMenuClick", MouseListener.listenerType, mouseEvent);
};

/**	
 * 控件外部点击事件回调函数。须关闭此控件的下拉菜单
 * @private
 */
TextComp.prototype.outsideClick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("outsideClick", MouseListener.listenerType, mouseEvent);
};

/**
 * 选择事件
 * @private
 */
TextComp.prototype.onselect = function () {
	if(this.maxSize != -1)
		this.input.value.maxLength = (this.maxSize + 1);
	
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onselect", TextListener.listenerType, simpleEvent);
};

/**
 * 得到键盘焦点时
 * @private	
 */
TextComp.prototype.focus = function (e) {	
	// 输入框必须先获得焦点然后再调用select()方法(并不是所有的浏览器都要求这样,但安全起见,最好每次都先调用focus())
	// this.input.select();
	this.oldValue = this.input.value;
	// 为避免tab键进入密码框时不能输入字符的bug
	if (this.visible == true) {
		if (this.dataType == "P") {	
			this.input.select();
		}
		this.onfocus(e);
	}
};
		   
/**
 * 失去焦点时进行输入的类型检查
 * @private
 */
TextComp.prototype.blur = function(e) {
	if (this.visible == true) {
		var value = this.input.value;
		if (this.dataType!='P') 
			this.setMessage(value);
		this.newValue = this.getFormater().format(value);
		var verifyR = this.verify(value);
		if(verifyR == null || verifyR){
			if (this.newValue != this.oldValue)
				this.valueChanged(this.oldValue, this.newValue);
		}
   		this.onblur(e);
	}
};

TextComp.prototype.verify = function(oldValue) {
	return true;
};

/**
 * 设置提示信息
 */
TextComp.prototype.setMessage = function(message) {
	if (!this.isError) {
		this.message = message;
		this.errorMessage = "";
		this.setTitle(message);
	}
};

/**
 * 设置错误提示信息
 */
TextComp.prototype.setErrorMessage = function(errorMessage) {
	if (this.isError) {
		this.message = "";
		this.errorMessage = errorMessage;
		this.setTitle(errorMessage);
	}
};

/**
 * 设置标题
 * modify by licza 统一设置标题显示方式,子类可以覆盖实现自定义显示
 */
TextComp.prototype.setTitle = function(title) {
    if (title != null && title != "") {
        if (this.input!=null)
            this.input.title = title;
        if(this.Div_gen!=null)
            this.Div_gen.title = title;
    } else if (title == "") {
        if (this.input!=null)
            this.input.title = "";
        if(this.Div_gen!=null)
            this.Div_gen.title = "";
    }
};

/**
 * 设置校验结果
 * @param checkResult 校验结果类型，包括：AutoFormComp.ELEMENT_ERROR、AutoFormComp.ELEMENT_WARNING、AutoFormComp.ELEMENT_NORMAL、AutoFormComp.ELEMENT_SUCCESS
 * @private
 */
TextComp.prototype.setCheckResult = function(checkResult) {
	this.checkResult = checkResult;
};

/**
 * 显示浮动的提示信息
 */
TextComp.prototype.showFloatMessageDiv = function() {
	// 要显示的信息
	var text = "";
	// 提示信息类型，包括：AutoFormComp.ELEMENT_ERROR、AutoFormComp.ELEMENT_WARNING、AutoFormComp.ELEMENT_NORMAL、AutoFormComp.ELEMENT_SUCCESS
	var messageType = this.checkResult;
	if (messageType == AutoFormComp.ELEMENT_SUCESS) {  // 当前状态为成功，则直接返回
		return;
	}
	else if (messageType == AutoFormComp.ELEMENT_ERROR || messageType == AutoFormComp.ELEMENT_WARNING) {  // 获取错误提示或警告提示
		if (this.isError && this.errorMessage != null && this.errorMessage != "") {
			text = this.errorMessage;
		} else {
			return;
		}
	} 
	else if (messageType == AutoFormComp.ELEMENT_NORMAL) {  // 获取常规输入辅助提示
		if (this.inputAssistant != null && this.inputAssistant != "") {
			text = this.inputAssistant;
		} else {
			return;
		}
	}
	// 计算文字宽度
	var textWidth = getTextWidth(text, this.className + "_FLOAT_MESSAGE_TEXT");
	if (textWidth == null || textWidth < 150)
		textWidth = 150;
	if (!window.floatMessageDiv) {  // 创建浮动提示框
		// 信息浮动框
		var floatMessageDiv = $ce("div");
		floatMessageDiv.style.display = "none";
		floatMessageDiv.style.position = "absolute";
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。		
//		floatMessageDiv.style.zIndex = "999999";
		floatMessageDiv.style.zIndex = getZIndex();
		window.floatMessageDiv = floatMessageDiv;
		document.body.appendChild(floatMessageDiv);
		
		// 左侧DIV
		var leftDiv = $ce("div");
		leftDiv.className = "div_left";
		window.floatMessageDiv.appendChild(leftDiv);
		
		// 右侧DIV
		var rightDiv = $ce("div");
		rightDiv.className = "div_right";
		window.floatMessageDiv.appendChild(rightDiv);
		
		// 中间DIV
		var centerDiv = $ce("div");
		centerDiv.className = "div_center";
		window.floatMessageDiv.appendChild(centerDiv);
		window.floatMessageDiv.centerDiv = centerDiv;
		
		// 箭头DIV
		var arrowDiv = $ce("div");
		arrowDiv.className = "div_arrow";
		window.floatMessageDiv.appendChild(arrowDiv);
		
		// 文字DIV
		var textDiv = $ce("div");
		textDiv.className = "div_text";
		window.floatMessageDiv.appendChild(textDiv);
		window.floatMessageDiv.textDiv = textDiv;
		
	}
	// 重新调整样式、大小、显示位置、显示内容
	if (messageType == AutoFormComp.ELEMENT_ERROR)  // 设置错误提示样式
		window.floatMessageDiv.className = this.className + "_float_message_div_error";
	else if (messageType == AutoFormComp.ELEMENT_WARNING)  // 设置警告提示样式
		window.floatMessageDiv.className = this.className + "_float_message_div_warning";
	else if (messageType == AutoFormComp.ELEMENT_NORMAL)  // 设置常规提示样式
		window.floatMessageDiv.className = this.className + "_float_message_div_normal";
	// 重新调整大小
	window.floatMessageDiv.style.width = (textWidth + getCssHeight(this.className + "_FLOAT_MESSAGE_LEFT_WIDTH") + getCssHeight(this.className + "_FLOAT_MESSAGE_RIGHT_WIDTH")) + "px";
	window.floatMessageDiv.centerDiv.style.width = textWidth + "px";
	// 重新调整显示位置
//	window.floatMessageDiv.style.top = (compOffsetTop(this.Div_gen, document.body) - getCssHeight(this.className + "_FLOAT_MESSAGE_HEIGHT")) + "px";
//	window.floatMessageDiv.style.left = (compOffsetLeft(this.Div_gen, document.body) - 20) + "px";
	window.floatMessageDiv.style.top = (compOffsetTop(this.Div_gen, document.body) + this.Div_gen.offsetHeight) + "px";
	window.floatMessageDiv.style.left = compOffsetLeft(this.Div_gen, document.body) + "px";
	// 重新调整内容
	window.floatMessageDiv.textDiv.innerHTML = text;
	// 显示
	window.floatMessageDiv.style.display = "block";
};

/**
 * 隐藏浮动的提示信息
 */
TextComp.prototype.hideFloatMessageDiv = function() {
	if (window.floatMessageDiv) {
		window.floatMessageDiv.style.display = "none";
	}
};

/**
 * 获得输入焦点
 */
TextComp.prototype.setFocus = function() {
	if (this.visible == true){
		if(this.disabled){
			this.mayFocus=true;
		}else{
			var oThis = this;
			if (IS_IE) {
				this.input.focus();
				this.input.select();
			} else {  // firefox等浏览器不能及时执行focus方法
				window.setTimeout(function(){oThis.input.focus();oThis.input.select();}, 50); 
			}
		}
	}
};

/**
 * 得到value值
 */
TextComp.prototype.getValue = function() {
	//if (this.checkTip()) {
	//	if (this.input.value == this.tip && this.input.style.color != "black")
	//		return "";
	//}
	return this.newValue;
};

/**
 * 设置值时要检测类型
 */
TextComp.prototype.setValue = function(text) {
	text = getString(text, "");
	// 只有字符串和密码类型才需要此处理
	if (this.dataType == 'A' || this.dataType == 'P') {
		if (this.maxSize != -1) {
			// 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
			if (text.lengthb() > this.maxSize)
				text = text.substrCH(this.maxSize);
		}
	}
	if (this.dataType != 'P') 
		this.setMessage(text);
	// 记录旧值
	this.oldValue = this.input.value;
	this.newValue = text;
	this.maskValue();
	this.input.value = this.showValue;
	
	if (this.checkTip()) {
		if (this.input.value == "")
			this.showTip();
		else
			this.input.style.color = "black";
	}
	if (this.newValue != this.oldValue)
		if (!this.disabled)   //TODO
			this.valueChanged(this.oldValue, this.newValue);
};

/**
 * 在末尾插入字符（包括退格键，用于直接通过虚拟键盘进行设值操作）
 * @param charCode 字符Unicode编码
 */
TextComp.prototype.addCharCode = function(charCode) {
	if (charCode == null)
		return;
	// 原始值
	var oldValue = this.getValue();
	if (oldValue != null)
		oldValue = oldValue.toString();
	else
		oldValue = "";
	// 新值
	var newValue = "";
	// 得到格式化器
	var formater = this.getFormater();
	if (charCode == 8) {  // 退格键
		if (oldValue == "") {
			return;
		} else {
			newValue = oldValue.substring(0, oldValue.length - 1);
			this.setValue(newValue);
		}
	} else {  // 其它键
		// 字符真实值
		var charValue = String.fromCharCode(charCode);
		newValue = oldValue + charValue;
		
		// 获取keyValue,currValue,aggValue,
		if (formater.valid(charValue, newValue, oldValue) == false)
			return;
	   	// 验证完成输入合法性后调用程序的内部处理
	   	if (this.afterValid(charCode, charValue) == false)
	   		return;
		
		this.setValue(newValue);
	}
};

TextComp.prototype.getShowValue = function() {
	return this.showValue;
};
TextComp.prototype.setShowValue = function(text) {
	this.showValue = text;
	this.input.value = text;
};

/**
 * 获取显示值
 *
 */
TextComp.prototype.maskValue = function(){
	var masker = getMasker(this.componentType);
	if(masker != null)
		this.showValue = toColorfulString(masker.format(this.newValue));
	else
		this.showValue = this.newValue;
};

/**
 * 设置此输入框控件的激活状态
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
TextComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		this.disabled = true;
		// 清除只读状态
		if (this.readOnly) 
			this.setReadOnly(false); 
		this.input.disabled = true;
		this.input.className = this.inputClassName_inactive;//"input_normal_center_bg text_input_inactive";
		if (this.Div_text != null)
			this.Div_text.className = this.className + " " + this.className + "_inactive_bgcolor";
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {
		this.disabled = false;
		// 清除只读状态
		if (this.readOnly) 
			this.setReadOnly(false);
		this.input.disabled = false;
		this.input.className = this.inputClassName_init;//"input_normal_center_bg text_input";
		if (this.Div_text != null)
			this.Div_text.className = this.className;
		if(this.mayFocus){
			this.input.focus();
			this.mayFocus=false;
		}
	}
};

/**
 * 得到输入框的激活状态
 */
TextComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * 检测是否是整数
 * @private
 */
TextComp.prototype.checkInteger = function(data) {
	//确定传入参数的合法性
	 if (data == null || data == "")
		return false;
	 else if (!isNumber(data))
		return false;
	 
	 return true;		
};

/**
 * 设置最大值
 */
TextComp.prototype.setMaxSize = function(size) {
	this.maxSize = parseInt(size);
};

/**
 * 设置只读状态
 */
TextComp.prototype.setReadOnly = function(readOnly) {
//	if (this.readOnly==readOnly)
//		return;
//	if (this.disabled){
//		this.setActive(true);
//	}
	this.input.readOnly = readOnly;
	this.readOnly=readOnly;
	if (readOnly) {
//		this.Div_text.style.borderWidth="0";
		this.input.className = this.inputClassName_init + " text_input_readonly";//"input_normal_center_bg text_input text_input_readonly";
		this.Div_text.className = this.className + " " + this.className + "_readonly";
	} else {
//		this.Div_text.style.borderWidth="1px";
		this.input.className = this.inputClassName_init;//"input_normal_center_bg text_input";
		this.Div_text.className = this.className;
	}
};

/**
 * 设置出错时样式
 * @private
 */
TextComp.prototype.setErrorStyle = function() {
	if (!this.readOnly) {
		if (this.Div_text != null)
			this.Div_text.className = this.className + " " + this.className + "_error_bgcolor";
	}
};

/**
 * 设置警告时样式
 * @private
 */
TextComp.prototype.setWarningStyle = function() {
	if (!this.readOnly) {
		if (this.Div_text != null)
			this.Div_text.className = this.className + " " + this.className + "_warning_bgcolor";
	}
};

/**
 * 设置聚焦时样式
 * @private
 */
TextComp.prototype.setFocusStyle = function() {
	if (!this.readOnly) {
		if (this.Div_text != null && -1 == this.Div_text.className.indexOf("_error_bgcolor") && -1 == this.Div_text.className.indexOf("_warning_bgcolor"))
			this.Div_text.className = this.className + " " + this.className + "_focus_bgcolor";
	}
};

/**
 * 设置焦点移出时样式
 * @private
 */
TextComp.prototype.setBlurStyle = function() {
	if (!this.readOnly) {
		if (this.Div_text != null && -1 == this.Div_text.className.indexOf("_error_bgcolor") && -1 == this.Div_text.className.indexOf("_warning_bgcolor"))
			this.Div_text.className = this.className;
	}
};

/**
 * 设置普通样式（校验通过后样式）
 * @private
 */
TextComp.prototype.setNormalStyle = function() {
	if (!this.readOnly) {
		if (this.Div_text != null)
			this.Div_text.className = this.className;
	}
};

/**
 * 获取Label对象
 */
TextComp.prototype.getLabel = function() {
	return this.label;
};

/**
 * 聚焦事件
 * @private
 */
TextComp.prototype.onblur = function (e) {
	this.setBlurStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
};

/**
 * 单击事件
 * @private
 */
TextComp.prototype.onclick = function (e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 聚焦事件
 * @private
 */
TextComp.prototype.onfocus = function (e) {
	this.setFocusStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onFocus", FocusListener.listenerType, focusEvent);
};

/**
 * 改变事件
 * @private
 */
TextComp.prototype.haschanged = function (e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onTextChanged", KeyListener.listenerType, keyEvent);
};

/**
 * 回车事件
 * @private
 */
TextComp.prototype.onenter = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onEnter", KeyListener.listenerType, keyEvent);
};

/**
 * 按键事件
 * @private
 */
TextComp.prototype.onkeydown = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onKeyDown", KeyListener.listenerType, keyEvent);
};

/**
 * 按键抬起事件
 * @private
 */
TextComp.prototype.onkeyup = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onKeyUp", KeyListener.listenerType, keyEvent);
};

/**
 * 鼠标移入事件
 * @private
 */
TextComp.prototype.onmouseover = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onMouseOver", MouseListener.listenerType, mouseEvent);
};

/**
 * 值改变事件
 * @private
 */
TextComp.prototype.valueChanged = function(oldValue, newValue) {
	var valueChangeEvent = {
			"obj" : this,
			"oldValue" : oldValue,
			"newValue" : newValue
		};
	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
};

/**
 * 获取对象信息
 * @private
 */
TextComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.TextContext";
	context.c = "TextContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.value = this.newValue;
	//处理连接符(+,&)丢失问题
	if (context.value){
		context.value = context.value.replace(/\+/g, "%2B");
		context.value = context.value.replace(/\&/g, "%26");
	}
	context.readOnly = this.readOnly;
	context.visible = this.visible;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
TextComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.focus)
		this.setFocus();
	if(!context.isform){
		if (context.value != null && context.value != this.input.value)
			this.setValue(context.value);
	}
	if (context.readOnly != null && this.readOnly != context.readOnly)
		this.setReadOnly(context.readOnly);
//	if (context.visible != this.visible) {
		if (context.visible)
			this.showV();
		else
			this.hideV();
//	}
	
};
