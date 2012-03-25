/**
 * @fileoverview Radio集合组件
 * @author guoweic
 * @version NC6.0
 * 
 */
RadioGroupComp.prototype = new BaseComponent;
RadioGroupComp.prototype.componentType = "RADIOGROUP";
// 每个Radio元素的高度
RadioGroupComp.ITEM_HEIGHT = "20px";
RadioGroupComp.IMAGE_SIDE_LENGTH = 15;

/**
 * 单选控件组构造方法
 * @class Radio集合类 
 * 
 * @constructor RadioGroupComp的构造函数
 * @param parent 父控件
 * @param name 此控件的名字,是对此控件的引用
 * @param left 控件左部x坐标
 * @param top 控件顶部y坐标
 * @param width 控件宽度
 * @param position 控件的定位属性
 * @param attrArr 属性对象
 * @param className css文件的名字
 */
function RadioGroupComp(parent, name, left, top, width, position, attrArr, className) {
	this.base = BaseComponent;
	this.base(name, left, top, width);
	
	this.className = getString(className, "radiogroup_div");
	
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
	
	this.groupId = this.id + "_radio_group";
	this.position = getString(position, "relative");
	this.parentOwner = parent;
	// 保存所有的radio子项
	this.radios = new Array();
	// 保存当前选中的值索引
	this.selectedIndex = -1;
	// 保存上次选中的旧值索引
	this.lastSelectedIndex = -1;
	if (attrArr != null) {
		
	}
	this.create();
};

/**
 * @private
 */
RadioGroupComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.position = this.position;
	if (this.width.indexOf("%") == -1){
		if(!this.width.endWith("px"))
			this.Div_gen.style.width = this.width + "px";
		else
			this.Div_gen.style.width = this.width;
	}
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
			// 左方向键、右方向键
			if (keyCode == 37) {
				if (oThis.selectedIndex <= 0) {
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
					return;
				}
				else
					oThis.setSelectedItem(oThis.selectedIndex - 1);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return;
			} else if (keyCode == 39) {
				if (oThis.selectedIndex >= oThis.radios.length - 1) {
					// 删除事件对象（用于清除依赖关系）
					clearEventSimply(e);
					return;
				}
				else
					oThis.setSelectedItem(oThis.selectedIndex + 1);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
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
RadioGroupComp.prototype.manageSelf = function() {
	this.Div_text = $ce("DIV");
	this.Div_text.id = this.id + "_textdiv";
	this.Div_text.style.position = "relative";
	this.Div_text.style.width = this.Div_gen.offsetWidth - 4 + "px";
	this.Div_text.className = "div_blur";
	
	if (this.hasLabel)
		this.Div_text.style.width = this.Div_gen.offsetWidth - this.labelWidth - 4 + "px";
	
	this.Div_text.style.width = "100%";
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
 * 增加radio子项
 * 
 * @param text 子项的显示值
 * @param value 子项的真实值
 * @param checked 设置是否为初始选中项
 * @param sepWidth radio的间隔宽度
 */
RadioGroupComp.prototype.addRadio = function(text, value, checked, sepWidth, imageSrc) {
	var oThis = this;
	// 索引值
	var index = this.radios.length;
	
	var realWidth = getInteger(sepWidth, 0);
	if (text && text != "")
		realWidth += getTextWidth(text) + 25;
	if(realWidth < 25)
		realWidth = 25;
	
	//add by chouhl 2012-3-5
	if(imageSrc){
		var attrArr = {};
		attrArr.imgsrc = imageSrc;
		realWidth += RadioGroupComp.IMAGE_SIDE_LENGTH;
	}
	
	if(this.parentOwner.offsetWidth < realWidth){
		this.parentOwner.style.width = realWidth + "px";
	}
	
	var radioDiv = $ce("DIV");
	if (!this.changeLine) {
		radioDiv.style[ATTRFLOAT] = "left";
		radioDiv.style.styleFloat = "left";
	}
	radioDiv.style.height = RadioGroupComp.ITEM_HEIGHT;
	radioDiv.style.width = realWidth + "px";
	radioDiv.style.marginTop = "2px";
	radioDiv.style.marginBottom = "2px";
	
	this.Div_text.appendChild(radioDiv);
	
	var radio = new RadioComp(radioDiv, this.id + "_radio_" + index, 0, 0, this.groupId, value, text, checked, "relative", null, attrArr);
	
	// Radio的鼠标事件
	var radioMouseListener = new MouseListener();
	radioMouseListener.onclick = function(mouseEvent) {
		oThis.setSelectedItem(index);
		
	};
	radio.addListener(radioMouseListener);
	
	this.radios.push(radio);
	radio.index = index;
	
	if (checked == true)
		this.setSelectedItem(index);
};

/**
 * 设置位置
 */
RadioGroupComp.prototype.setBounds = function(left, top, width, height) {
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
		this.Div_text.style.width = this.width - this.labelWidth - 2 + "px";
	
};

/**
 * 显示控件(显示属性是visibility)
 */
RadioGroupComp.prototype.showV = function() {
	var obj = this.getObjHtml();
	obj.style.visibility = "";
	this.visible = true;
};

/**
 * 隐藏控件(显示属性是visibility)
 */
RadioGroupComp.prototype.hideV = function() {
	BaseComponent.prototype.hideV.call(this);
	//this.hideData();
};

/**
 * 得到所有的radio项
 */
RadioGroupComp.prototype.getRadios = function() {
	if (this.radios != null)
		return this.radios;
	else
		return null;
};

/**
 * 清除所有的radio
 */
RadioGroupComp.prototype.clearRadios = function() {
	if (this.radios == null)
		return;
	this.Div_text.innerHTML = "";

	this.radios = new Array();
	this.selectedIndex = -1;
};

/**
 * 清除参数指定的radio
 * 
 * @param value radio的真实值
 */
RadioGroupComp.prototype.clearRadio = function(value) {
	if (value == null || value == "")
		return;
	var radios = this.radios;
	if (radios != null && radios.length > 0) {
		for (var i = 0; i < radios.length; i++) {
			if (radios[i].getValue() == value) {
				this.Div_text.removeChild(radios[i].parentOwner);
				this.radios.splice(i, 0, 1);
				return;
			}
		}
	}
};

/**
 * 设置数据
 * 
 * @param comboData 数据内容
 * @param sepWidth radio的间隔宽度
 */
RadioGroupComp.prototype.setComboData = function(comboData, sepWidth) {
	this.clearRadios();
	if (!comboData)
		return;
	var nameArr = comboData.getNameArray();
	var valueArr = comboData.getValueArray();
	var imageArr = comboData.getImageArray();
	if (nameArr != null) {
		for (var i = 0; i < nameArr.length; i++) {
			var checked = false;
//			if (i == 0)  // 默认选中第一条
//				checked = true;
			if (i == (nameArr.length - 1))
				sepWidth = 0;
			this.addRadio(nameArr[i], valueArr[i], checked, sepWidth, imageArr[i]);
		}
	}
	this.comboData = comboData;
	if (this.disabled == true)  // 设置不可编辑
		this.setActive(false);
};

/**
 * 设置选中项
 * 
 * @param index 要设置选中项的索引值,index小于0表示不设置任何radio为选中状态
 */
RadioGroupComp.prototype.setSelectedItem = function(index) {
	index = parseInt(index);
	if (isNaN(index) || index > this.radios.length)
		return;

	// index小于0表示不改变任何radio为选中状态
	if (index < 0) {
		return;
	}

	var oldSelectedIndex = this.selectedIndex;
	var radio = this.radios[index];
	this.selectedIndex = radio.index;
	radio.setChecked(true);
	if (index != oldSelectedIndex) {
		this.value = radio.getValue();
		this.text = radio.getText();
		// 选中项改变调用用户重载的方法
		this.valueChanged(radio.getValue(), this.radios[oldSelectedIndex] == null ? null : this.radios[oldSelectedIndex].getValue());
	}
};

/**
 * 获取选中项
 */
RadioGroupComp.prototype.getSelectedItem = function() {
	if (this.selectedIndex == -1)
		return null;
	return this.radios[this.selectedIndex];
};

/**
 * 设置value,如果value不是任何一个radios的value,则设置为空
 * 
 * @param value 要设置的值
 */
RadioGroupComp.prototype.setValue = function(value) {
	for (var i = 0; i < this.radios.length; i++) {
		if (value == this.radios[i].getValue()) {
			this.setSelectedItem(i);
			return true;
		}
	}
};

/**
 * 得到选择项的value值
 */
RadioGroupComp.prototype.getValue = function() {
	return this.value;
};

/**
 * 得到选择项的Text值
 */
RadioGroupComp.prototype.getText = function() {
	return this.text;
};

/**
 * 得到指定value的索引值
 * 
 * @param value 指定的value
 */
RadioGroupComp.prototype.getValueIndex = function(value) {
	if (value != null) {
		for ( var i = 0; i < this.radios.length; i++) {
			if (this.radios[i].getValue() == value)
				return i;
		}
	}
	return -1;
};

/**
 * 得到选中的radio的index值
 */
RadioGroupComp.prototype.getSelectedIndex = function() {
	return this.selectedIndex;
};

/**
 * 得到选择的radio的text
 */
RadioGroupComp.prototype.getSelectedText = function() {
	if (this.selectedIndex == -1)
		return null;
	return this.radios[this.selectedIndex].getText();
};

/**
 * 设置控件的激活状态.
 * 
 * @param isActive true表示处于激活状态,否则表示禁用状态
 */
RadioGroupComp.prototype.setActive = function(isActive) {
	for (var i = 0, n = this.radios.length; i < n; i++) {
		this.radios[i].setActive(isActive);
	}
	this.disabled = !isActive;
};

/*
 * 暴露给用户重载的方法,当选中值改变时会调用该方法
 * 
 * @param newItem 新选中的Radio对象
 * @param lastItem 上一个选中的Radio对象
RadioGroupComp.prototype.valueChanged = function(newItem, lastItem) {
	var valueChangeEvent = {
			"obj" : this,
			"newValue" : newItem.value,
			"oldValue" : (lastItem == null) ? null : lastItem.value
		};
	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
};
 */

/**
 * 暴露给用户重载的方法,当选中值改变时会调用该方法
 * 
 * @param newItem 新选中的Radio对象
 * @param lastItem 上一个选中的Radio对象
 * @private
 */
RadioGroupComp.prototype.valueChanged = function(newValue, oldValue) {
	var valueChangeEvent = {
			"obj" : this,
			"newValue" : newValue,
			"oldValue" : oldValue
		};
	this.doEventFunc("valueChanged", TextListener.listenerType, valueChangeEvent);
};

/**
 * 设置聚焦
 */
RadioGroupComp.prototype.setDivFocus = function(isFocus) {
	if (isFocus == false)
		this.Div_gen.blur();
	else {
		this.Div_gen.focus();
	}
};

/**
 * 设置出错时样式
 * @private
 */
RadioGroupComp.prototype.setErrorStyle = function() {
	if(!this.readOnly) {
		if (this.Div_text)
			this.Div_text.className = "div_error";
	}
};

/**
 * 设置聚焦时样式
 * @private
 */
RadioGroupComp.prototype.setFocusStyle = function() {
	if(!this.readOnly) {
		if (this.Div_text && this.Div_text.className != "div_error")
			this.Div_text.className = "div_focus";
	}
};

/**
 * 设置焦点移出时样式
 * @private
 */
RadioGroupComp.prototype.setBlurStyle = function() {
	if(!this.readOnly) {
		if (this.Div_text && this.Div_text.className != "div_error")
			this.Div_text.className = "div_blur";
	}
};

/**
 * 设置普通样式
 * @private
 */
RadioGroupComp.prototype.setNormalStyle = function() {
	if(!this.readOnly) {
		if (this.Div_text)
			this.Div_text.className = "div_blur";
	}
};

/**
 * 设置只读状态
 */
RadioGroupComp.prototype.setReadOnly = function(readOnly) {
//	if (this.readOnly == readOnly)
//		return;
	if (this.disabled){
		this.setActive(true);
	}
	for (var j = 0, m = this.radios.length; j < m; j++) {
		this.radios[j].setReadOnly(readOnly);
	}
	this.readOnly = readOnly;
};

/**
 * 暴露给用户重载的函数
 * @private
 */
RadioGroupComp.prototype.onfocus = function(e) {
	this.setFocusStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onFocus", FocusListener.listenerType, focusEvent);
};

/**
 * 暴露给用户重载的函数,失去焦点时调用该方法
 * @private
 */
RadioGroupComp.prototype.onblur = function(e) {
	this.setBlurStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
};

/*
 * 
RadioGroupComp.prototype.processEnter = function(e) {
	
};
 */

/**
 * 暴露给用户重载的函数,点击回车键时调用该方法
 * @private
 */
RadioGroupComp.prototype.onenter = function(e) {
	var keyEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onEnter", KeyListener.listenerType, keyEvent);
};

/**
 * 获取对象信息
 * @private
 */
RadioGroupComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.RadioGroupContext";
	context.c = "RadioGroupContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.comboDataId = this.comboData == null ? null : this.comboData.id;
	context.index = this.selectedIndex;
	context.value = this.getValue();
	context.readOnly = this.readOnly;
	// 获取子项Context
//	var radios = this.radios;
//	if (radios.length > 0) {
//		context.radioContexts = new Array();
//		for (var i = 0, n = radios.length; i < n; i++) {
//			context.radioContexts.push(radios[i].getContext());
//		}
//	}
	return context;
};

/**
 * 设置对象信息
 * @private
 */
RadioGroupComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.value && context.value != this.getValue())
		this.setValue(context.value);
	if (context.readOnly != null && this.readOnly != context.readOnly){
		this.setReadOnly(context.readOnly);
	}
	// 为子项设置Context
//	if (context.radioContexts) {
//		for (var i = 0, n = context.radioContexts.length; i < n; i++) {
//			var radioContext = context.radioContexts[i];
//			for (var j = 0, m = this.radios.length; j < m; j++) {
//				if (this.radios[j].id == context.radioContexts[i].id)
//					this.radios[j].setContext(radioContext);
//			}
//		}
//	}
	//TODO
	
};