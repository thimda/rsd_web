/**
 * 
 * 多选框控件
 * 
 * @author dengjt, gd, guoweic
 * @version NC6.0
 * @since   NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */
CheckboxComp.prototype = new TextComp;
CheckboxComp.prototype.componentType = "CHECKBOX";
CheckboxComp.IMAGE_SIDE_LENGTH = 15;

/**
 * CheckBox控件的构造函数
 * @class 复选框控件
 * @param parent CheckBox控件的父控件
 * @param name 控件名字
 * @param left 控件左坐标
 * @param top 控件顶部坐标
 * @param text 控件显示的名字
 * @param checked 是否被选中
 * @param position 定位属性
 */
function CheckboxComp(parent, name, left, top, width, text, checked, position, className, attrArr) {
	this.text = getString(text, "");
	this.checked = getBoolean(checked, false);
	this.valuePair = [ "true", "false" ];
	if(attrArr){
		this.checkboxImgSrc = getString(attrArr.imgsrc, this.checkboxImgSrc);
	}
	this.parentOwner = parent;
	this.base = TextComp;
	this.base(parent, name, left, top, width, "C", position, attrArr, "checkbox_div");
};

/**
 * CheckboxComp的二级回掉函数
 * @private
 */
CheckboxComp.prototype.manageSelf = function() {

	// 设置tabindex属性，以便在Firefox下获取聚焦事件
	this.Div_gen.setAttribute("tabindex", this.tabIndex == null ? 0 : this.tabIndex);
	
	if (IS_IE6) {
		this.Div_gen.id = this.name;
		this.Div_gen.className = this.className;
		this.Div_gen.style.position = this.position;
		this.Div_gen.style.left = this.left + "px";
		this.Div_gen.style.top = this.top + "px";
		if (this.width.toString().indexOf("%") == -1 && this.width.toString().indexOf("px") == -1)
			this.Div_gen.style.width = this.width + "px";
		else
			this.Div_gen.style.width = this.width;
		this.Div_gen.style.overflow = "hidden";
	}
	var oThis = this;
	this.input = $ce("INPUT");
	this.input.name = this.id;
	this.input.id = this.id;
	this.input.type = "checkbox";
	this.input.className = "checkbox_box";
	this.input.style.left = "0px";
	this.input.style.position = "absolute";
	this.Div_gen.style.height = "18px";

	this.Div_gen.appendChild(this.input);
	if (this.checked) {
		// 声明在默认情况下是否被选中了
		this.input.defaultChecked = true;
		this.input.checked = true;
	} else {
		this.input.defaultChecked = false;
		this.input.checked = false;
	}

	if (this.disabled) {
		this.input.disabled = true;
		this.input.className = "checkbox_box inactive_bgcolor";
	}

	this.input.value = this.input.checked;
	
	this.Div_gen.onfocus = function(e) {
//		this.style.border = "dotted black 1px";
	};
	
	this.Div_gen.onblur = function(e) {
		this.style.border = "0px";
		oThis.onblur();
	};
	
	// 处理回车键事件
	this.Div_gen.onkeypress = function (e) {
		e = EventUtil.getEvent();
		if (oThis.disabled != true) {
			// 获取输入字符
			var keyCode = e.key;
			// 回车键
			if (keyCode == 13) {
				oThis.onenter(e);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return true;
			}
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	   	return true;
	};
	// 处理空格键事件
	this.Div_gen.onkeydown = function(e) {
		e = EventUtil.getEvent();
		if (oThis.disabled != true) {
			// 获取输入字符
			var keyCode = e.key;
			// 空格键
			if (keyCode == 32) {
				oThis.input.click(e);
				oThis.Div_gen.focus(e);
				stopAll(e);
			}
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	this.input.onclick = function(e) {
		e = EventUtil.getEvent();
		oThis.Div_gen.focus();
		
		// 获取新值
		var newValue = oThis.input.checked;
		oThis.checked = oThis.input.checked;
		oThis.valueChanged(newValue, !newValue);
		
		oThis.onclick(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	// 显示文字的div
	this.textLabel = $ce("DIV");
	this.textLabel.style.left = "20px";
	if (IS_IE)
		this.textLabel.style.top = "5px";
	else
		this.textLabel.style.top = "3px";
	this.textLabel.style.position = "relative";
	this.textLabel.htmlFor = this.input.id;
	this.textLabel.appendChild(document.createTextNode(this.text));
	this.Div_gen.appendChild(this.textLabel);
	
	var realWidth = getTextWidth(this.text) + 25;
	
	if(this.checkboxImgSrc){
		var imgDiv = $ce("DIV");
		imgDiv.style.marginRight = "1px";
		imgDiv.style.width = CheckboxComp.IMAGE_SIDE_LENGTH + "px";
		imgDiv.style.height = CheckboxComp.IMAGE_SIDE_LENGTH + "px";
		imgDiv.style[ATTRFLOAT] = "left";
		var img = $ce("IMG");
		img.src = window.themePath + this.checkboxImgSrc;
		imgDiv.appendChild(img);
		var div = $ce("DIV");
		div.appendChild(imgDiv);
		this.textLabel.innerHTML = div.innerHTML + this.textLabel.innerHTML;
		realWidth += CheckboxComp.IMAGE_SIDE_LENGTH;
	}
	
	if(this.parentOwner != document.body && this.parentOwner.style){
		this.parentOwner.style.width = realWidth + "px";
	}
	
	if (!IS_IE) {  // firefox下的label点击绑定特别处理
		this.textLabel.onclick = function(e) {
			oThis.input.click(e);
		};
	}
};

/**
 * 设置此checkbox状态
 * @param{boolean} checked
 */
CheckboxComp.prototype.setChecked = function(checked) {
	if (checked == null || checked == "")
		this.checked = false;
	else
		this.checked = checked;
	this.input.checked = this.checked;
	this.input.value = this.checked;

	// 设置此属性后意味用户点击了此checkbox,所以要调用暴露给用户的onclick函数
	// 影响checkboxGroup的setValue方法，delete by guoweic
//	this.onclick();
};

/**
 * 得到指定按钮状态
 */
CheckboxComp.prototype.getChecked = function() {
	return this.input.checked;
};

/**
 * 设置只读状态
 */
CheckboxComp.prototype.setReadOnly = function(readOnly) {
	if (this.readOnly==readOnly)
		return;
	if (this.disabled){
		this.setActive(true);
	}
	// 控件处于只读状态变为非只读状态
	if (this.readOnly == true && readOnly == false) {
		this.input.disabled = false;
		this.readOnly = false;
		this.input.className = "checkbox_box";
		this.Div_gen.className = "checkbox_box";
	}
	// 控件处于只读状态变为只读状态
	else if (this.readOnly == false && readOnly == true) {
		this.input.disabled = true;
		this.readOnly = true;
		this.input.className = "checkbox_box inactive_bgcolor";
		this.Div_gen.className = "checkbox_box inactive_bgcolor";
	}
};

/**
 * 选中后执行方法
 * @private
 */
CheckboxComp.prototype.onselect = function () {
	if(this.maxSize != -1)
		this.input.value.maxLength = (this.maxSize + 1);
	
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onselect", TextListener.listenerType, simpleEvent);
};

/**
 * 回车事件
 * @private
 */
CheckboxComp.prototype.onenter = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onEnter", KeyListener.listenerType, keyEvent);
};

/**
 * 单击事件
 * @private
 */
CheckboxComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 值改变事件
 * @private
 */
CheckboxComp.prototype.valueChanged = function(newValue, oldValue) {
	var valueChangeEvent = {
			"obj" : this,
			"newValue" : newValue,
			"oldValue" : oldValue
		};
	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
	return true;
};

/**
 * 设置值对
 * @param{Array} arr
 */
CheckboxComp.prototype.setValuePair = function(arr) {
	this.valuePair = arr;
};

/**
 * 获取当前值
 */
CheckboxComp.prototype.getValue = function() {
	return this.getChecked() ? this.valuePair[0] : this.valuePair[1];
};

/**
 * 设值
 */
CheckboxComp.prototype.setValue = function(value) {
	this.setChecked(value == this.valuePair[0]);
};

/**
 * 设置此checkbox控件的激活状态
 * 
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
CheckboxComp.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		this.input.disabled = true;
		this.disabled = true;
		this.input.className = "checkbox_box inactive_bgcolor";
		this.Div_gen.className = "checkbox_box inactive_bgcolor";
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {
		this.input.disabled = false;
		this.disabled = false;
		this.input.className = "checkbox_box";
	}
};

/**
 * 设置聚焦
 */
CheckboxComp.prototype.setDivFocus = function(isFocus) {
	if (isFocus == false)
		this.Div_gen.blur();
	else {
		this.Div_gen.focus();
	}
};

/**
 * 获取显示值
 */
CheckboxComp.prototype.getText = function() {
	return this.text;
};

/**
 * 获取对象信息
 * @private
 */
CheckboxComp.prototype.getContext = function() {
	var context = TextComp.prototype.getContext.call(this);
//	var context = new Object;
	context.c = "CheckBoxContext";
//	context.id = this.id;
//	context.enabled = !this.disabled;
	context.checked = this.input.checked;
	return context;
};

/**
 * 设置对象信息
 * @private
 */
CheckboxComp.prototype.setContext = function(context) {
	TextComp.prototype.setContext.call(this,context);
//	if (context.enabled != null)
//		this.setActive(context.enabled);
	if (context.checked != null)
		this.setChecked(context.checked);
	//TODO
	
};

