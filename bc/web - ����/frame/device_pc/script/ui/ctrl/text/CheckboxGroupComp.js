/**
 * @fileoverview Checkbox集合组件
 * @author guoweic
 * @version NC6.0
 * 
 */
CheckboxGroupComp.prototype = new BaseComponent;
CheckboxGroupComp.prototype.componentType = "RADIOGROUP";
CheckboxGroupComp.ITEM_HEIGHT = "20px";

/**
 * Checkbox集合控件构造函数
 * @class Checkbox集合类 
 * 
 * @constructor CheckboxGroupComp的构造函数
 * @param parent 父控件
 * @param name 此控件的名字,是对此控件的引用
 * @param left 控件左部x坐标
 * @param top 控件顶部y坐标
 * @param width 控件宽度
 * @param position 控件的定位属性
 * @param attrArr 属性对象
 * @param className css文件的名字
 */
function CheckboxGroupComp(parent, name, left, top, width, position, attrArr, className) {
	this.base = BaseComponent;
	this.base(name, left, top, width);
	
	this.className = getString(className, "checkboxgroup_div");
	
	if (attrArr != null) {   
		this.disabled = getBoolean(attrArr.disabled, false);	 
		this.readOnly = getBoolean(attrArr.readOnly, false);
		this.tabIndex = getInteger(attrArr.tabIndex, 0);
		// 输入辅助提示信息（和错误提示相互替代）
		this.inputAssistant = getString(attrArr.inputAssistant, "");
		
		// 标签属性
		this.labelText = getString(attrArr.labelText, "");
		if ("" != this.labelText)
			this.hasLabel = true;
		this.labelAlign = getString(attrArr.labelAlign, "left");
		this.labelWidth = getInteger(attrArr.labelWidth, 0);
		if (0 == this.labelWidth && "" != this.labelText)
			this.labelWidth = getTextWidth(this.labelText) + 5;
		// 是否每个子项占一行
		this.changeLine = getBoolean(attrArr.changeLine, false);
		
	}	
	
	this.groupId = this.id + "_checkbox_group";
	this.position = getString(position, "relative");
	this.parentOwner = parent;
	// 保存所有的checkbox子项
	this.checkboxs = new Array();
	// 保存当前聚焦的值索引
	this.focusIndex = -1;
	// 旧值
	this.oldValue = null;
	if (attrArr != null) {
		
	}
	this.create();
};

/**
 * @private
 */
CheckboxGroupComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.position = this.position;
	if (this.width.indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = "100%";
	this.Div_gen.className = this.className;

	// 设置tabindex属性，以便在Firefox下获取聚焦事件
	this.Div_gen.setAttribute("tabindex", this.tabIndex == null ? 0 : this.tabIndex);
	
	this.Div_gen.onfocus = function(e) {
		e = EventUtil.getEvent();
		oThis.isFocus = true;
		oThis.onfocus(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	this.Div_gen.onblur = function(e) {
		e = EventUtil.getEvent();
		oThis.isFocus = false;
		oThis.onblur(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	// 处理回车事件
	this.Div_gen.onkeypress = function(e) {
		e = EventUtil.getEvent();
		if (oThis.disabled != true) {
			// 获取输入字符
			var keyCode = e.key;
			if (keyCode == 13) {
				oThis.onenter(e);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return;
			}
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	   	return;
	};
	// 处理左右方向键事件
	this.Div_gen.onkeydown = function(e) {
		e = EventUtil.getEvent();
		if (oThis.disabled != true) {
			// 获取输入字符
			var keyCode = e.key;
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			// 左方向键、右方向键
			if (keyCode == 37) {
				if (oThis.focusIndex <= 0)
					return;
				else
					oThis.setFocusItem(oThis.focusIndex - 1);
				return;
			} else if (keyCode == 39) {
				if (oThis.focusIndex >= oThis.checkboxs.length - 1)
					return;
				else
					oThis.setFocusItem(oThis.focusIndex + 1);
				return;
			}
		}
	};
	
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 回掉函数
 * @private
 */
CheckboxGroupComp.prototype.manageSelf = function() {
	this.Div_text = $ce("DIV");
	this.Div_text.id = this.id + "_textdiv";
	this.Div_text.style.position = "relative";
	this.Div_text.style.width = this.Div_gen.offsetWidth - 4 + "px";
	this.Div_text.className = "div_blur";
	
	if (this.hasLabel)
		this.Div_text.style.width = this.Div_gen.offsetWidth - this.labelWidth - 4 + "px";
	
  	this.Div_text.style.overflow = "hidden";

	if (this.hasLabel) {  // 有标签
		this.labelDiv = $ce("DIV");
		this.labelDiv.style.width = this.labelWidth - 2 + "px";
		this.labelDiv.style.position = "relative";
		this.labelDiv.style.top = "2px";
		
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
		
		this.label = new LabelComp(this.labelDiv, this.id + "_label", "0px", "0px", this.labelText, "relative", null);
	} else {
		this.Div_gen.appendChild(this.Div_text);
	}

};

/**
 * 增加checkbox子项
 * 
 * @param text 子项的显示值
 * @param value 子项的真实值
 * @param checked 设置是否为初始选中项
 * @param sepWidth checkbox的间隔宽度
 */
CheckboxGroupComp.prototype.addCheckbox = function(text, value, checked, sepWidth) {
	var oThis = this;
	// 索引值
	var index = this.checkboxs.length;
	var checkboxDiv = $ce("DIV");
	if (!this.changeLine) {
		checkboxDiv.style[ATTRFLOAT] = "left";
		checkboxDiv.style.styleFloat = "left";
	}
	checkboxDiv.style.height = "100%";
	var realWidth = getInteger(sepWidth, 0);
	if (text && text != "")
		realWidth += getTextWidth(text) + 25;
	checkboxDiv.style.width = realWidth + "px";
	this.Div_text.appendChild(checkboxDiv);
	checkboxDiv.style.height = CheckboxGroupComp.ITEM_HEIGHT;
	
	checkboxDiv.style.marginTop = "2px";
	checkboxDiv.style.marginBottom = "2px";
	
	var checkbox = new CheckboxComp(checkboxDiv, this.id + "_checkbox_" + index, 0, 0, realWidth - 2, text, checked, "relative");
	checkbox.value = value;
	
	// 单击子项后设置外层DIV聚焦
	var clickListener = new MouseListener();
   	clickListener.onclick = function(mouseEvent) {
   		oThis.Div_gen.focus();
   	};
   	checkbox.addListener(clickListener);
   	
   	// 值改变事件
   	var valueChangeListener = new TextListener();
   	valueChangeListener.valueChanged = function(valueChangeEvent) {
   		var newValue = oThis.getValue();
   		oThis.valueChanged(oThis.oldValue, newValue);
   		oThis.oldValue = newValue;
   	};
   	checkbox.addListener(valueChangeListener);
   	
   	
	// Checkbox的点击事件
	var mouseListener = new MouseListener();
	mouseListener.onclick = function(mouseEvent) {
   		oThis.onclick(checkbox, mouseEvent.event);
   	};
   	checkbox.addListener(mouseListener);
	
	this.checkboxs.push(checkbox);
	checkbox.index = index;
	
	this.focusIndex = 0;
};

/**
 * 设置位置
 */
CheckboxGroupComp.prototype.setBounds = function(left, top, width, height) {
	this.left = left;
	this.top = top;
	this.width = getString(width, this.Div_gen.offsetWidth);
	this.height = getString(height, this.Div_gen.offsetHeight);

	// 设置最外层的大小
	this.Div_gen.style.left = left + "px";
	this.Div_gen.style.top = top + "px";
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";

	this.Div_text.style.width = this.width - 4 + "px";
	if (this.hasLabel)
		this.Div_text.style.width = this.width - this.labelWidth - 4 + "px";
	
};

/**
 * 显示控件(显示属性是visibility)
 */
CheckboxGroupComp.prototype.showV = function() {
	var obj = this.getObjHtml();
	obj.style.visibility = "";
	this.visible = true;
};

/**
 * 隐藏控件(显示属性是visibility)
 */
CheckboxGroupComp.prototype.hideV = function() {
	BaseComponent.prototype.hideV.call(this);
	//this.hideData();
};

/**
 * 得到所有的checkbox项
 */
CheckboxGroupComp.prototype.getCheckboxs = function() {
	if (this.checkboxs != null)
		return this.checkboxs;
	else
		return null;
};

/**
 * 清除所有的checkbox
 */
CheckboxGroupComp.prototype.clearCheckboxs = function() {
	if (this.checkboxs == null)
		return;
	this.Div_text.innerHTML = "";

	this.checkboxs = new Array();
	this.focusIndex = -1;
};

/**
 * 清除参数指定的checkbox
 * 
 * @param value checkbox的真实值
 */
CheckboxGroupComp.prototype.clearCheckbox = function(value) {
	if (value == null || value == "")
		return;
	var checkboxs = this.checkboxs;
	if (checkboxs != null && checkboxs.length > 0) {
		for (var i = 0; i < checkboxs.length; i++) {
			if (checkboxs[i].value == value) {
				this.Div_text.removeChild(checkboxs[i].parentOwner);
				this.checkboxs.splice(i, 0, 1);
				return;
			}
		}
	}
};

/**
 * 设置数据
 * 
 * @param comboData 数据内容
 * @param sepWidth checkbox的间隔宽度
 */
CheckboxGroupComp.prototype.setComboData = function(comboData, sepWidth) {
	this.clearCheckboxs();
	if (!comboData)
		return;
	var nameArr = comboData.getNameArray();
	var valueArr = comboData.getValueArray();
	if (nameArr != null) {
		for (var i = 0; i < nameArr.length; i++) {
			var checked = false;
			if (i == (nameArr.length - 1))
				sepWidth = 0;
			this.addCheckbox(nameArr[i], valueArr[i], checked, sepWidth);
		}
	}
	this.comboData = comboData;
	if (this.disabled == true)  // 设置不可编辑
		this.setActive(false);
};

/**
 * 设置聚焦项
 * 
 * @param index 要设置聚焦项的索引值,index小于0表示不设置任何checkbox为聚焦状态
 */
CheckboxGroupComp.prototype.setFocusItem = function(index) {
	index = parseInt(index);
	if (isNaN(index) || index > this.checkboxs.length)
		return;

	// index小于0表示不改变任何checkbox为聚焦状态
	if (index < 0) {
		return;
	}

	var checkbox = this.checkboxs[index];
	for (var i = 0; i < this.checkboxs.length; i++) {
		if (i == index)
			this.checkboxs[i].setDivFocus(true);
		else
			this.checkboxs[i].setDivFocus(false);
	}
	this.focusIndex = checkbox.index;

	//TODO 选中项改变调用用户重载的方法
//	this.valueChanged(checkbox, this.checkboxs[oldSelectedIndex]);
	
};

/**
 * 获取聚焦项
 */
CheckboxGroupComp.prototype.getFocusItem = function() {
	if (this.focusIndex == -1)
		return null;
	return this.checkboxs[this.focusIndex];
};

/**
 * 设置value，如果text不是任何一个checkboxs的text，则忽略
 * 
 * @param values 要设置的值
 */
CheckboxGroupComp.prototype.setValue = function(values) {
	if (values != null && values != "") {
		var valueArr = values.split(",");
		for (var i = 0; i < this.checkboxs.length; i++) {
			var checked = false;
			for (var j = 0; j < valueArr.length; j++) {
				if (valueArr[j].trim() == this.checkboxs[i].value) {
					this.checkboxs[i].setChecked(true);
					checked = true;
				}
			}
			if (checked == false)
				this.checkboxs[i].setChecked(false);
		}
	}
	else{
		for (var i = 0; i < this.checkboxs.length; i++) {
			this.checkboxs[i].setChecked(false);
		}
	}
	var newValue = this.getValue();
	this.valueChanged(this.oldValue, newValue);
	this.oldValue = this.getValue();
	return true;
};

/**
 * 得到选择项的value值
 */
CheckboxGroupComp.prototype.getValue = function() {
	var value = "";
	for (var i = 0; i < this.checkboxs.length; i++) {
		if (this.checkboxs[i].getChecked() == true)
			value += this.checkboxs[i].value + ",";
	}
	if (value.length > 0)
		value = value.substr(0, value.length - 1);
	return value;
};

/**
 * 得到选择项的Text值
 */
CheckboxGroupComp.prototype.getText = function() {
	var text = "";
	for (var i = 0; i < this.checkboxs.length; i++) {
		if (checkboxs[i].getChecked() == true)
			text += this.checkboxs[i].getText() + ",";
	}
	if (text.length > 0)
		text = text.substr(0, value.length - 1);
	return text;
};

/**
 * 得到指定value的索引值
 * 
 * @param value 指定的value
 */
CheckboxGroupComp.prototype.getValueIndex = function(value) {
	if (value != null) {
		for ( var i = 0; i < this.checkboxs.length; i++) {
			if (this.checkboxs[i].value == value)
				return i;
		}
	}
	return -1;
};

/**
 * 得到聚焦的checkbox的index值
 */
CheckboxGroupComp.prototype.getFocusIndex = function() {
	return this.focusIndex;
};

/**
 * 得到选择的checkbox的text
 */
CheckboxGroupComp.prototype.getFocusText = function() {
	if (this.focusIndex == -1)
		return null;
	return this.checkboxs[this.focusIndex].getText();
};

/**
 * 设置控件的激活状态
 * 
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
CheckboxGroupComp.prototype.setActive = function(isActive) {
	for (var i = 0, n = this.checkboxs.length; i < n; i++) {
		this.checkboxs[i].setActive(isActive);
	}
	this.disabled = !isActive;
};

//TODO
/*
 * 暴露给用户重载的方法,当选中值改变时会调用该方法
 * 
 * @param newItem 新选中的Checkbox对象
 * @param lastItem 上一个选中的Checkbox对象
CheckboxGroupComp.prototype.valueChanged = function(newItem, lastItem) {
	var valueChangeEvent = {
			"obj" : this,
			"newValue" : newItem.value,
			"oldValue" : (lastItem == null) ? null : lastItem.value
		};
	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
};
 */

/**
 * 设置聚焦
 */
CheckboxGroupComp.prototype.setDivFocus = function(isFocus) {
	if (isFocus == false) {
		this.Div_gen.blur();
		if (this.checkboxs.length > 0) {
			for (var i = 0; i < this.checkboxs.length; i++) {
				this.checkboxs[i].setDivFocus(false);
			}
		}
	} else {
		this.Div_gen.focus();
		if (this.checkboxs.length > 0) {
			if (this.focusIndex != -1)
				this.checkboxs[this.focusIndex].setDivFocus(true);
			else
				this.checkboxs[0].setDivFocus(true);
		}
	}
};

/**
 * 设置只读状态
 */
CheckboxGroupComp.prototype.setReadOnly = function(readOnly) {
//	if (this.readOnly == readOnly)
//		return;
	if (this.disabled){
		this.setActive(true);
	}
	for (var j = 0, m = this.checkboxs.length; j < m; j++) {
		this.checkboxs[j].setReadOnly(readOnly);
	}
	this.readOnly = readOnly;
};

/**
 * 设置出错时样式
 * @private
 */
CheckboxGroupComp.prototype.setErrorStyle = function() {
	if(!this.readOnly) {
		if (this.Div_text)
			this.Div_text.className = "div_error";
	}
};

/**
 * 设置聚焦时样式
 * @private
 */
CheckboxGroupComp.prototype.setFocusStyle = function() {
	if(!this.readOnly) {
		if (this.Div_text && this.Div_text.className != "div_error")
			this.Div_text.className = "div_focus";
	}
};

/**
 * 设置焦点移出时样式
 * @private
 */
CheckboxGroupComp.prototype.setBlurStyle = function() {
	if(!this.readOnly) {
		if (this.Div_text && this.Div_text.className != "div_error")
			this.Div_text.className = "div_blur";
	}
};

/**
 * 设置普通样式
 * @private
 */
CheckboxGroupComp.prototype.setNormalStyle = function() {
	if(!this.readOnly) {
		if (this.Div_text)
			this.Div_text.className = "div_blur";
	}
};

/**
 * 暴露给用户重载的方法,当选中值改变时会调用该方法
 * 
 * @param newItem 新选中的Radio对象
 * @param lastItem 上一个选中的Radio对象
 * @private
 */
CheckboxGroupComp.prototype.valueChanged = function(newValue, oldValue) {
	var valueChangeEvent = {
			"obj" : this,
			"newValue" : newValue,
			"oldValue" : oldValue
		};
	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
};

/**
 * 聚焦事件
 * @private
 */
CheckboxGroupComp.prototype.onfocus = function(e) {
	this.setFocusStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onFocus", FocusListener.listenerType, focusEvent);
};

/**
 * 失去焦点事件
 * @private
 */
CheckboxGroupComp.prototype.onblur = function(e) {
	this.setBlurStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
};

/**
 * 回车事件
 * @private
 */
CheckboxGroupComp.prototype.onenter = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onEnter", KeyListener.listenerType, keyEvent);
};

/**
 * 子项点击事件
 * @private
 */
CheckboxGroupComp.prototype.onclick = function(obj, e) {
	var mouseEvent = {
			"obj" : obj,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 获取对象信息
 * @private
 */
CheckboxGroupComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.CheckboxGroupContext";
	context.c = "CheckboxGroupContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.visible = this.visible;
	context.comboDataId = this.comboData == null ? null : this.comboData.id;
	context.value = this.getValue();
	context.readOnly = this.readOnly;
	// 获取子项Context
	var checkboxs = this.checkboxs;
	if (checkboxs.length > 0) {
		context.checkboxContexts = new Array();
		for (var i = 0, n = checkboxs.length; i < n; i++) {
			context.checkboxContexts.push(checkboxs[i].getContext());
		}
	}
	return context;
};

/**
 * 设置对象信息
 * @private
 */
CheckboxGroupComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.value && context.value != this.getValue())
		this.setValue(context.value);
	if (context.visible != this.visible) {
		if (context.visible)
			this.showV();
		else
			this.hideV();
	}
	if (context.readOnly != null && this.readOnly != context.readOnly){
		this.setReadOnly(context.readOnly);
	}
	// 为子项设置Context
	if (context.checkboxContexts) {
		for (var i = 0, n = context.checkboxContexts.length; i < n; i++) {
			var checkboxContext = context.checkboxContexts[i];
			for (var j = 0, m = this.checkboxs.length; j < m; j++) {
				if (this.checkboxs[j].id == context.checkboxContexts[i].id)
					this.checkboxs[j].setContext(checkboxContext);
			}
		}
	}
	//TODO
	
};