/**
 * Form表单控件
 * 
 * @version NC6.0
 * @since NC5.5
 * 
 */
AutoFormComp.prototype = new ListenerUtil;

AutoFormComp.prototype.componentType = "AUTOFORM";

// 额外的空白高度
AutoFormComp.SPACE_HEIGHT = 1;


/**
 * 表单构造函数 renderType 1 固定布局居中 2 流式布局 3 固定提示布局 4 自由表单 5 固定布局右对齐 
 * 
 * @class 表单控件
 */
function AutoFormComp(parentDiv, id, renderType, renderHiddenEle, rowHeight,
		columnCount, attrArr) {
	// function AutoFormComp(parentDiv, id, renderType, renderHiddenEle) {
	this.id = id;
	this.parentDiv = parentDiv;
	// this.parentDiv.style.borderWidth = "2px";
	// this.parentDiv.style.borderStyle = "solid";
	// this.parentDiv.style.borderColor = "#0000FF";
	// 分别保存当前Form中元素对dataset的绑定索引,绑定key和绑定元素,这三者是一一对应的
	this.indiceArr = null;
	this.keyArr = new Array;
	// 子项集合
	this.eleArr = new Array;
	// 用来记录控件的原始可编辑性
	this.disableArr = new Array;
	// 用来记录控件的原始只读属性
	this.readOnlyArr = new Array;
	this.dataset = null;
	this.rowIndex = -1;
	this.editable = true;
	this.readOnly = false;
	this.tmpEditable = true;
	this.renderHiddenEle = getBoolean(renderHiddenEle, false);
	// 记录违反了数据校验规则还没有修改正确的行,列及违反的规则
	this.dataCheckErrorRows = null;
	this.renderType = renderType;
	// this.rowHeight = getInteger(rowHeight,22);
	// this.columnCount = getInteger(columnCount,2);
	this.labelPosition = "left";
	this.eleWidth = "120"; 
	if(attrArr){
		this.labelPosition = getString(attrArr.labelPosition, this.labelPosition);
		this.eleWidth = getString(attrArr.eleWidth, this.eleWidth);
	}
	var oThis = this;
	if (renderType == 4) { // renxh
		this.pLayout = new Object;
		this.pLayout.paint = function() {

		};
	} else if (renderType == 2) {
		this.pLayout = new FlowGridLayout(parentDiv, "flowGrid_" + id, 0, 0,
				"100%", "100%", "relative", true, 120, 10);
		this.pLayout.Div_gen.style.height = "100px";
		this.pLayout.labelPosition = this.labelPosition;
		this.pLayout.afterRepaint = function() {
			oThis.afterRepaint();
		}

	}
	// dingrf
	else {
		this.pLayout = new FixedGridLayout(parentDiv, "fixedGrid_" + id, 0, 0,
				"100%", "100%", "relative", true, this.eleWidth, 10, rowHeight,
				columnCount, renderType);
		this.pLayout.labelPosition = this.labelPosition;
	}
	this.widget = null;
	ListenerUtil.call(this, true);
};

/**
 * 重新绘制后执行方法
 * 
 * @private
 */
AutoFormComp.prototype.afterRepaint = function() {
	var childNodes = this.parentDiv.childNodes;
	var currNode = null;
	if (childNodes != null) {
		for ( var j = 0; j < childNodes.length; j++) {
			var child = childNodes[j];
			if (!IS_IE) {
				if (child.nodeName == "#text")
					continue;
			}
			currNode = child;
		}
	}
	if(currNode == null){
		return;
	}
	// 设置外层DIV高度
	currNode.style.height = this.getHeight() + AutoFormComp.SPACE_HEIGHT + "px";
};

AutoFormComp.prototype.removeElementById = function(id) {
	if (this.renderType == 4) {// 自由表单删除 renxh
		var comp = this.getElement(id);
		if (comp) {
			var newEles = new Array();
			var newKeyEles = new Array();
			for (var i = 0; i < this.eleArr.length; i++) {
				if (this.eleArr[i] != comp) {
					newEles.push(this.eleArr[i]);
					newKeyEles.push(this.eleArr[i].id);
				}
			}
			this.eleArr = newEles;
			this.keyArr = newKeyEles;
			if (comp.attachedLabel) {
				var labelP = comp.attachedLabel.parentNode;
				if (labelP)
					labelP.removeChild(comp.attachedLabel);
			}
			var Div_gen = comp.getObjHtml();
			if (Div_gen) {
				var p = Div_gen.parentNode;
				p.removeChild(Div_gen);
			}
			comp.destroy();
		}

	} else {
		var comp = this.pLayout.removeCompById(id);
		if (comp) {
			var newEles = new Array();
			for (var i = 0; i < this.eleArr.length; i++) {
				if (this.eleArr[i] != comp) {
					newEles.push(this.eleArr[i]);
				}
			}
			this.eleArr = newEles;
			if (comp.attachedLabel) {
				var labelP = comp.attachedLabel.parentNode;
				if (labelP)
					labelP.removeChild(comp.attachedLabel);
			}
			var Div_gen = comp.getObjHtml();
			if (Div_gen) {
				var p = Div_gen.parentNode;
				p.removeChild(Div_gen);
			}
			comp.destroy();
		}
	}

};

/**
 * 创建子项
 * 
 * eleWidth 控件宽度
 * width 自由表单时 FormElement整体宽度
 * 
 */
AutoFormComp.prototype.createElement = function(eleId, field, eleWidth, height,
		rowSpan, colSpan, type, userObject, disabled, readOnly, dataset,
		labelName, labelColor, nextLine, required, tip, inputAssistant,
		description, isAttachNext, className, eleDiv, width) {
	var pdiv, position;
	if (className == null)
		className = "text_div";
	if (this.renderType == 1 || this.renderType == 3) {
		// dingrf
		// pdiv = $ge("$d_" + this.id + eleId);
		pdiv = this.pLayout;
		position = "relative";
	} else if (this.renderType == 4) {// renxh
		pdiv = eleDiv;
		position = "relative";

	} else {
		pdiv = this.pLayout;
		position = "absolute";
	}

	var ele;
	if (type == EditorType.CHECKBOX) {
		ele = new CheckboxComp(pdiv, field, 0, 0, eleWidth, labelName, false,
				position, {
					"disabled" : disabled,
					"readOnly" : readOnly,
					"inputAssistant" : inputAssistant
				});
		if (userObject && userObject.valuePair)
			ele.setValuePair(userObject.valuePair);
		if (userObject && userObject.visible == false)
			ele.visible = false;
	} else if (type == EditorType.REFERENCE) {
		ele = new ReferenceTextComp(pdiv, field, 0, 0, eleWidth, position, {
			"disabled" : disabled,
			"readOnly" : readOnly,
			"tip" : tip,
			"inputAssistant" : inputAssistant
		}, userObject.refNode, className);
		ele.setBindInfo(dataset, field);
		if (userObject.visible == false)
			ele.visible = false;
	} else if (type == EditorType.COMBOBOX) {
		var dataDivHeight = userObject.dataDivHeight;
		ele = new ComboComp(pdiv, field, 0, 0, eleWidth, position,
				userObject.selectOnly, {
					"disabled" : disabled,
					"readOnly" : readOnly,
					"dataDivHeight" : dataDivHeight,
					"inputAssistant" : inputAssistant
				}, className);
		ele.setShowImgOnly(userObject.imageOnly);
		var comboData = userObject.comboData;
		if (comboData != null) {
			ele.setComboData(comboData);
		}
		if (userObject.visible == false)
			ele.visible = false;

	} else if (type == EditorType.LIST) {
		var dataDivHeight = userObject.dataDivHeight;
		ele = new ListComp(pdiv, field, 0, 0, eleWidth, dataDivHeight, false, position, className);
//		ele.setShowImgOnly(userObject.imageOnly);
		var comboData = userObject.comboData;
		if (comboData != null) {
			ele.setComboData(comboData);
		}
		if (userObject.visible == false)
			ele.visible = false;

	} else if (type == EditorType.RADIOGROUP) {
		var widgetId = this.widget.id;
		// RadioGroup的ID要重新设置，以免不同form中radio的name相同
		ele = new RadioGroupComp(pdiv, field, 0, 0, eleWidth, position, {
			"disabled" : disabled,
			"readOnly" : readOnly,
			"inputAssistant" : inputAssistant
		});
		var comboData = userObject.comboData;
		if (comboData != null) {
			ele.setComboData(comboData);
		}
		if (userObject.visible == false)
			ele.visible = false;

	} else if (type == EditorType.CHECKBOXGROUP) {
		ele = new CheckboxGroupComp(pdiv, field, 0, 0, eleWidth, position, {
			"disabled" : disabled,
			"readOnly" : readOnly,
			"inputAssistant" : inputAssistant
		});
		var comboData = userObject.comboData;
		if (comboData != null) {
			ele.setComboData(comboData);
		}
		if (userObject.visible == false)
			ele.visible = false;

	} else if (type == EditorType.PWDTEXT) {
		ele = new PswTextComp(pdiv, field, 0, 0, eleWidth, position, {
			"disabled" : disabled,
			"readOnly" : readOnly,
			"inputAssistant" : inputAssistant
		}, className);
	} else if (type == EditorType.INTEGERTEXT) {
		var maxValue = userObject.maxValue != null ? userObject.maxValue : null;
		var minValue = userObject.minValue != null ? userObject.minValue : null;
		ele = new IntegerTextComp(pdiv, field, 0, 0, eleWidth, position, maxValue,
				minValue, {
					"disabled" : disabled,
					"readOnly" : readOnly,
					"tip" : tip,
					"inputAssistant" : inputAssistant
				}, className);
		if (userObject.visible == false)
			ele.visible = false;
	} else if (type == EditorType.DECIMALTEXT) {
		ele = new FloatTextComp(pdiv, field, 0, 0, eleWidth, position,
				userObject.precision, {
					"disabled" : disabled,
					"readOnly" : readOnly,
					"tip" : tip,
					"inputAssistant" : inputAssistant
				}, className);
		if (userObject.visible == false)
			ele.visible = false;
	} else if (type == EditorType.TEXTAREA) {
		ele = new TextAreaComp(pdiv, field, 0, 0, "", "", position, readOnly,
				"", eleWidth, height, tip, className);
		var disabled = getBoolean(disabled, true);
		ele.setActive(!disabled);
		// ele.nextLine = true;
	} else if (type == EditorType.DATETEXT) {
		ele = new DateTextComp(pdiv, field, 0, 0, eleWidth, position, {
			"disabled" : disabled,
			"readOnly" : readOnly,
			"tip" : tip,
			"inputAssistant" : inputAssistant
		}, className);
	} else if (type == EditorType.DATETIMETEXT) {
		ele = new DateTextComp(pdiv, field, 0, 0, eleWidth, position, {
			"disabled" : disabled,
			"readOnly" : readOnly,
			"tip" : tip,
			"inputAssistant" : inputAssistant
		}, className);
		ele.setShowTimeBar(true);
	} else if (type == EditorType.IMAGECOMP) {
		// ImageComp(parent, name, refImg1, left, top, eleWidth, height, alt,
		// refImg2, attrObj) {
		var refImg1 = '';
		var refImg2 = '';
		if (userObject.refImage1) {
			refImg1 = userObject.refImage1;
		}
		if (userObject.refImage2) {
			refImg2 = userObject.refImage2;
		}
		ele = new ImageComp(pdiv, field, refImg1, 0, 0, userObject.eleWidth,
				userObject.height, '', refImg2, null);
		ele.baseUrl = userObject.url;
		ele.pkField = userObject.pkfield;
		if (userObject.refImage1 || userObject.refImage2) {
			ele.setValue = function() {
				return;
			};
		} else {
			ele.setValue = function() {
				var row = oThis.dataset.getSelectedRow();
				if (row == null)
					this.changeImage("", "");
				else {
					var pkValue = row.getCellValue(oThis.dataset
							.nameToIndex(this.pkField));
					if (pkValue == null) {
						this.changeImage("", "");
					} else {
						var url = this.baseUrl.replace("$REPLACE$", pkValue);
						this.changeImage(url, url);
					}
				}
			};
		}

	} else if (type == EditorType.RICHEDITOR) {
		ele = new EditorComp(pdiv, field, 0, 0, "100%", "100%", position);
//		ele = new EditorComp(pdiv, field, 0, 0, eleWidth, height, position);
	}
	// form的自定义元素
	else if (type == EditorType.SELFDEFELE) {
		ele = new SelfDefElement(pdiv, field, 0, 0, eleWidth, height, position,
				null);
		var disabled = getBoolean(disabled, true);
		ele.setActive(!disabled);
	}
	else if (type == EditorType.FILECOMP) {
		ele = new FileComp(pdiv, field, 0, 0, eleWidth, height, position, {
			"disabled" : disabled,
			"readOnly" : readOnly,
			"tip" : tip,
			"inputAssistant" : inputAssistant,
			"sizeLimit":userObject.sizeLimit
		}, className);
	}	
	else {
		ele = new StringTextComp(pdiv, field, 0, 0, eleWidth, position, {
			"disabled" : disabled,
			"readOnly" : readOnly,
			"maxSize" : userObject.maxLength,
			"tip" : tip,
			"inputAssistant" : inputAssistant
		}, className);
		if (userObject.visible == false)
			ele.visible = false;
	}
	// dingrf
	// if (this.renderType == 2) {
	ele.labelName = labelName;
	// }

	if (ele.inputAssistant == null)
		ele.inputAssistant = inputAssistant;
	ele.description = description;
	ele.isAttachNext = isAttachNext;

	ele.basewidth = eleWidth;
	ele.colSpan = colSpan;
	ele.rowSpan = rowSpan;
	ele.nextLine = nextLine;
	ele.isRequired = required;
	if (labelColor != "" && labelColor != null)
		ele.labelColor = labelColor;

	//modify start by licza 11/29/11 : drop an exist element,find is addr ,do not add an index  
	var oriIndex = -1;
	for (var xi = 0; xi < this.keyArr.length; xi++) {
		if (this.keyArr[xi] == field){
			oriIndex = xi;
			break;
		}
	}
	if(oriIndex != -1){
		this.eleArr[oriIndex] = ele;
	}else{
		this.keyArr.push(field);
		this.eleArr.push(ele);
	}
	
	//modify end
	this.disableArr.push(disabled ? disabled : false);
	this.readOnlyArr.push(readOnly ? readOnly : false);
	var oThis = this;

	// 数据改变事件（不包括EditorComp子项）
	var textListener = new TextListener();
	textListener.valueChanged = function(valueChangeEvent) {
		if (oThis.rowIndex == -1)
			return;
		var index = oThis.dataset.nameToIndex(this.id);
		if (index == -1)
			return;
		oThis.dataset.setValueAt(oThis.rowIndex, index, this.getValue());
	};
	ele.addListener(textListener);

	// 鼠标移出且内容为空，进行数据校验
	var focusListener = new FocusListener();
	focusListener.onBlur = function(focusEvent) {
		var obj = focusEvent.obj;
		if (ele.componentType == "EDITOR") { // EditorComp子项，为dataset设值
			if (oThis.rowIndex == -1)
				return;
			var index = oThis.dataset.nameToIndex(this.id);
			if (index == -1)
				return;
			// 当前值
			var newValue = this.getValue();
			// 旧值
			var oldValue = oThis.dataset.getValueAt(oThis.rowIndex, index);
			if (oldValue != newValue) { // 值发生变化
				// 更新ds内容
				oThis.dataset.setValueAt(oThis.rowIndex, index, newValue);
			}
		} else { // 非EditorComp子项，鼠标移出且内容为空，进行数据校验
			if (oThis.rowIndex == -1)
				return;
			var row = oThis.dataset.getRow(oThis.rowIndex);
			var colIndex = oThis.dataset.nameToIndex(obj.id);
			if (colIndex == -1)
				return;
			var value = obj.getValue();
			if (value == null || value == "")
				oThis.dataset.checkDatasetCell(value, colIndex, row);
			if (oThis.renderType == 1 || oThis.renderType == 2) { // 固定布局或流式布局
				if (obj.hideFloatMessageDiv) {
					// 隐藏浮动提示信息
					obj.hideFloatMessageDiv();
				}
			}
		}
	};
	focusListener.onFocus = function(focusEvent) {
		var obj = focusEvent.obj;
		if (oThis.renderType == 1 || oThis.renderType == 2) { // 固定布局或流式布局
			if (obj.showFloatMessageDiv) {
				// 显示浮动提示信息
				obj.showFloatMessageDiv();
			}
		}
	};
	ele.addListener(focusListener);

	// 增加回车事件，焦点移到下一个元素
	var formEleKeyListener = new KeyListener();
	formEleKeyListener.onEnter = function(keyEvent) {
		var obj = keyEvent.obj;
		for (var i = 0, n = oThis.eleArr.length - 1; i < n; i++) {
			var ele = oThis.eleArr[i];
			if (obj == ele) {
				// // 是否是最后一个可用元素
				// var isLastEle = true;
				for (var j = i + 1; j < n + 1; j++) {
					if (oThis.eleArr[j].disabled != true
							&& oThis.eleArr[j].componentType != "SELFDEFELEMENT") { // 元素可用
						if (oThis.eleArr[j].setDivFocus) // RadioGroup和Checkbox的聚焦
							oThis.eleArr[j].setDivFocus(true);
						else if (oThis.eleArr[j].input != null) {
							oThis.eleArr[j].input.select();
						}
						if (oThis.eleArr[j - 1]
								&& oThis.eleArr[j - 1].setDivFocus) // 上一个元素的焦点移出
							oThis.eleArr[j - 1].setDivFocus(false);
						// isLastEle = false;
						break;
					}
				}
				// // 如果是最后一个可用元素，则找下一个form表单中的第一个元素聚焦
				// if (isLastEle) {
				// var comps = this.widget.getComponents();
				// for (var i = 0, n = comps.length; i < n; i++) {
				// if (comps[i].componentType == "AUTOFORM" && comps[i].id !=
				// this.id) {
				// var nextElements = comps[i].getElements();
				// for (var j = 0, m = nextElements.length; j < m; j++) {
				// if (!nextElements[i].disabled && !nextElements[i].readOnly) {
				// nextElements[i].setFocus();
				// break;
				// }
				// }
				// break;
				// }
				// }
				// }
			}
		}
		stopAll(keyEvent.event);
	};
	ele.addListener(formEleKeyListener);

	// 编辑态下的测试
	if (window.editMode) {
		if (this.renderType != 4) {
			var params = {
				uiid : "",
				eleid : this.id,
				widgetid : this.widget ? this.widget.id : "",
				type : "form_element",
				subeleid : ele.id,
				renderType : this.renderType
			};
			ele.params = params;
			if(!window.windowEditorMode){
				new EditableListener(ele.getObjHtml(), params, EditableListener.COMPOMENT_TYPE);
			}
		}

	}

	if (this.renderType == 4) { // 创建laybel
//		var divWidth = eleDiv.offsetWidth;
		ele.formWidth = width;
		this.createType4Div(eleDiv, ele);
		
		
//		window.objects[this.id + "$" + ele.id] = ele;
//		eleDiv.style.width = "100%";
//		addResizeEvent(eleDiv, function() {
//			AutoFormComp.resize(oThis.id + "$" + ele.id);
//		});
		
//		var objHtml = ele.getObjHtml();
//		var eleWidth = objHtml.offsetWidth;
//		if(eleWidth == 0)
//			eleWidth = width;
//		var labelWidth = divWidth - eleWidth - 10;
//		if(labelWidth < 0){
//			labelWidth = 20;
//		}
//		if(labelWidth > 120){
//			labelWidth = 120;
//		}
//		ele.attachedLabel = this.createLabel(ele,0,0,labelWidth);
//		objHtml.style.position = "absolute";
//		objHtml.style.left = (labelWidth + 10) + "px";
//		eleDiv.insertBefore(ele.attachedLabel, objHtml);
	}
	return ele;
};

/**
 * 只针对type = 4
 * 
 * @param {} id
 */
//AutoFormComp.resize = function(id){
//	var ele = window.objects[id];
//	try{
//		AutoFormComp.type4Resize(ele);
//	}catch(e){}
//};
//
//AutoFormComp.type4Resize = function(ele){
////	if (this.eleArr.length == 0) return;
////	ele = this.eleArr[0];
//	if (ele == null) return;
//	if (ele.pDiv){
//		if (ele.attachedLabel){
//				ele.pDiv.style.width =  ele.pDiv.parentNode.offsetWidth - ele.attachedLabel.offsetWidth + "px";
//		}
//		else	
//			ele.pDiv.style.width = ele.pDiv.parentNode.offsetWidth + "px";
//	}		
//};

/**
 * 更新FormElement的宽度
 */
AutoFormComp.prototype.updateType4Size = function(eleId, eleDiv, eleWidth) {
	var ele = this.getElement(eleId);
	var divWidth = eleDiv.offsetWidth;
	if(eleWidth == null){
		eleWidth = ele.getObjHtml().offsetWidth;
	}
	if((typeof(eleWidth) == "string") && (eleWidth.indexOf("%") != -1)){
		ele.getObjHtml().style.width=eleWidth;
		return;
	}
	var labelWidth = divWidth - eleWidth - 10;
	if(labelWidth < 0){
		labelWidth = 20;
	}
	if(labelWidth > 120){
		labelWidth = 120;
	}
	ele.attachedLabel.style.width = labelWidth + "px";
	ele.getObjHtml().style.left = (labelWidth + 10) + "px";
};


/**
 * 设置绑定ds特定元素的值(用于EditorComp的情况)
 * 
 * @param id
 *            元素绑定控件ID
 * @param value
 *            值，若为null则取当前元素绑定控件的值
 * @private
 */
AutoFormComp.prototype.setDsCellValue = function(id, value) {
	if (this.rowIndex == -1)
		return;
	var index = this.dataset.nameToIndex(id);
	if (index == -1)
		return;
	var ele = this.getElement(id);
	value = value == null ? ele.getValue() : value;
	this.dataset.setValueAt(this.rowIndex, index, value);
};

/**
 * 隐藏子项
 */
AutoFormComp.prototype.hideElement = function(key) {
	for (var i = 0; i < this.keyArr.length; i++) {
		if (this.keyArr[i] == key)
			this.eleArr[i].hide();
	}
};

/**
 * 显示子项
 */
AutoFormComp.prototype.showElement = function(key) {
	for (var i = 0; i < this.keyArr.length; i++) {
		if (this.keyArr[i] == key)
			this.eleArr[i].show();
	}
};

/**
 * 获取子项
 */
AutoFormComp.prototype.getElement = function(key) {
	for (var i = 0; i < this.keyArr.length; i++) {
		if (this.keyArr[i] == key)
			return this.eleArr[i];
	}
	return null;
};

/**
 * 根据索引获取子项
 */
AutoFormComp.prototype.getElementByIndex = function(index) {
	return this.eleArr[index];
};

/**
 * 获取所有子项
 */
AutoFormComp.prototype.getElements = function() {
	return this.eleArr;
};

/**
 * 设置绑定的dataset
 */
AutoFormComp.prototype.setDataset = function(ds) {
	this.dataset = ds;
	var rows = ds.getSelectedRows();
	var row = rows == null ? null : rows[0];
	this.indiceArr = new Array;
	// 一次性计算绑定列在Dataset中的索引值，便于后面数据读取。
	for (var i = 0, count = this.keyArr.length; i < count; i++) {
		// debugger;
		this.indiceArr[i] = ds.nameToIndex(this.keyArr[i]);
		// 设置精度
		var metadata = ds.metadata[this.indiceArr[i]];
		if (metadata && metadata.precision != null
				&& this.eleArr[i].componentType == "FLOATTEXT")
			this.eleArr[i].setPrecision(metadata.precision, true);
		// 顺便绑定当前值
		if (row != null) {
			this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
			this.rowIndex = ds.getRowIndex(row);
		} else
			this.eleArr[i].setValue(null);
	}
	ds.bindComponent(this);
	this.setEditable(ds.isEditable());
	// dingrf
	// if (this.renderType == 2)
	this.pLayout.paint();

	if (typeof globalFormInitialized != "undefined")
		globalFormInitialized(this);
};

/**
 * 设置控件的可编辑性
 * 
 */
AutoFormComp.prototype.setEditable = function(editable) { // 实际上调用的是disable属性而不是readonly属性
	var e = editable;
	if (this.dataset.editable == false)
		e = false;
	this.editable = e;
	this.tmpEditable = e;
	// 如果没有选中行，不能edit
	if (this.dataset.editable == false || this.dataset.getSelectedRow() == null) {
		this.tmpEditable = false;
	}
	for (var i = 0, count = this.eleArr.length; i < count; i++) {
		if (this.readOnlyArr[i] && this.tmpEditable)
			continue;
		// 如果原始不可编辑，在设置为可编辑时，不做动作
		if (this.disableArr[i] && this.tmpEditable)
			continue;
		if (this.tmpEditable)
			this.eleArr[i].setActive(true);
		else
			this.eleArr[i].setActive(false);
	}
};

/**
 * 设置控件的只读属性
 */
AutoFormComp.prototype.setReadOnly = function(readOnly) {
	this.readOnly = readOnly;
	this.tmpReadOnly = readOnly;
	// 如果没有选中行，不能ReadOnly
	if (this.dataset.getSelectedRow() == null) {
		this.tmpReadOnly = false;
	}
	for (var i = 0, count = this.eleArr.length; i < count; i++) {
		if (this.disableArr[i] && !this.tmpReadOnly)
			continue;
		// 如果原始只读，在设置为不只读不做动作
		if (this.readOnlyArr[i] && !this.tmpReadOnly)
			continue;
		if (this.tmpReadOnly)
			this.eleArr[i].setReadOnly(true);
		else
			this.eleArr[i].setReadOnly(false);
	}
};

/**
 * 获取高度
 */
AutoFormComp.prototype.getHeight = function() {
	if (this.renderType == 1 || this.renderType == 3) {
		var pdiv = $ge("$d_" + this.id);
		return (pdiv.firstChild.rows.length - 1) * 22;
	} else
		return this.pLayout.getHeight();
};

/**
 * Dataset改变后回调方法
 * 
 * @private
 */
AutoFormComp.prototype.onModelChanged = function(event) {
	if (RowSelectEvent.prototype.isPrototypeOf(event) || FocusChangeEvent.prototype.isPrototypeOf(event)) {
		var row = event.currentRow;
		this.rowIndex = event.currentRowIndex;
		for (var i = 0; i < this.indiceArr.length; i++) {
			if (row != null) {
				this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
			} else
				this.eleArr[i].setValue(null);
		}

		if (this.editable != this.tmpEditable) {
			this.tmpEditable = this.editable;
			this.setEditable(this.editable);
		}

		// dengjt delete，暂且不知道有什么影响（估计是校验部分）
		// // 清除当前所有元素的css外观,不清除editor的css外观
		// for(var i = 0, count = this.indiceArr.length; i < count; i ++)
		// {
		// //TODO:将checkbox的css和text统一
		// if(this.eleArr[i].componentType != null &&
		// this.eleArr[i].componentType != "EDITOR" &&
		// this.eleArr[i].componentType != "CHECKBOX")
		// this.eleArr[i].Div_gen.className = "text_div";
		// if(this.eleArr[i].componentType != null &&
		// this.eleArr[i].componentType == "CHECKBOX")
		// this.eleArr[i].Div_gen.className = "checkbox_div";
		// }

		// 当前选中行包括未通过检验的字段
		if (row != null) {
			// 清除行中所有内容的错误提示
			for (var i = 0; i < this.indiceArr.length; i++) {
				var eleHtml = this.eleArr[i].Div_gen;
				// 设置普通样式
				if (this.eleArr[i].Div_text) {
					this.eleArr[i].setNormalStyle();
				} else if (eleHtml != null && eleHtml.className.indexOf(" checkedborder") != -1)
					eleHtml.className = eleHtml.className.replace(
							" checkedborder", "");
				if (this.eleArr[i].setMessage)
					this.eleArr[i].setMessage(row
							.getCellValue(this.indiceArr[i]));
			}
			if (this.dataCheckErrorRows != null
					&& this.dataCheckErrorRows.containsKey(row.rowId)) {
				var value = this.dataCheckErrorRows.get(row.rowId);
				for (var i = 0; i < value[0].length; i++) {
					var index = value[0][i];
					var eleHtml = this.eleArr[index].Div_gen;

					if (this.eleArr[index].setError)
						this.eleArr[index].setError(true);

					// 设置错误样式
					if (this.eleArr[index].Div_text) {
						if (value[2][i] == BaseComponent.ELEMENT_ERROR)
							this.eleArr[index].setErrorStyle();
						else if (value[2][i] == BaseComponent.ELEMENT_WARNING)
							this.eleArr[index].setWarningStyle();
					} else if (eleHtml.className.indexOf("checkedborder") == -1)
						eleHtml.className += " checkedborder";
					if (this.eleArr[index].setErrorMessage)
						this.eleArr[index].setErrorMessage(value[1][i]); 
				}
			}
		}
	}

	else if (RowUnSelectEvent.prototype.isPrototypeOf(event)) {
		this.rowIndex = -1;
		var count = this.indiceArr.length;
		for (var i = 0; i < count; i++)
			this.eleArr[i].setValue(null);
	}

	else if (DataChangeEvent.prototype.isPrototypeOf(event)) {
		for (var i = 0, count = this.indiceArr.length; i < count; i++) {
			if (this.indiceArr[i] == event.cellColIndex) {
				if (this.eleArr[i].getValue() != event.currentValue) {
					this.eleArr[i].setValue(event.currentValue);
				}
				// 移除未通过检验的行在map中的相应数据
				if (this.dataCheckErrorRows != null
						&& this.dataCheckErrorRows
								.containsKey(event.currentRow.rowId)) {
					var value = this.dataCheckErrorRows
							.get(event.currentRow.rowId);
					var colIndex = value[0].indexOf(event.cellColIndex);
					if (colIndex != -1) {
						value[0].splice(colIndex, 1);
						value[1].splice(colIndex, 1);
					}
					if (value[0].length == 0)
						this.dataCheckErrorRows.remove(event.currentRow.rowId);
				}
				break;
			}
		}
	} else if (DatasetUndoEvent.prototype.isPrototypeOf(event)) {
		var row = this.dataset.getSelectedRow();
		if (row != null) {
			this.rowIndex = this.dataset.getRowIndex(row);
			for (var i = 0, count = this.indiceArr.length; i < count; i++)
				this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
		} else {
			this.rowIndex = -1;
			for (var i = 0, count = this.indiceArr.length; i < count; i++)
				// 此处是否应该设置成默认值
				this.eleArr[i].setValue("");
		}
	} else if (RowInsertEvent.prototype.isPrototypeOf(event)) {
		var row = this.dataset.getSelectedRow();
		if (row != null) {
			for (var i = 0; i < this.indiceArr.length; i++)
				this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
		}
		// else{
		// for(var i = 0; i < this.indiceArr.length; i ++)
		// //此处是否应该设置成默认值
		// this.eleArr[i].setValue("");
		// }
	} else if (PageChangeEvent.prototype.isPrototypeOf(event)) {
		this.rowIndex = this.dataset.getSelectedIndex();
		var row = this.dataset.getSelectedRow();
		for (var i = 0; i < this.indiceArr.length; i++) {
			if (row != null)
				this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
			else
				this.eleArr[i].setValue(null);
		}
		if (this.editable != this.tmpEditable) {
			this.tmpEditable = this.editable;
			this.setEditable(this.editable);
		}
	} else if (RowDeleteEvent.prototype.isPrototypeOf(event)) {
		this.rowIndex = this.dataset.getSelectedIndex();
		for (var i = 0; i < this.indiceArr.length; i++)
			this.eleArr[i].setValue(null);
	}
	// 数据校验事件
	else if (DataCheckEvent.prototype.isPrototypeOf(event)) {
		var colIndice = event.cellColIndices;
		var describes = event.rulesDescribe;
		var row = event.currentRow;
		var eleIndex = -1;
		var success = true;
		for (var i = 0, count = colIndice.length; i < count; i++) {
			if ((eleIndex = this.indiceArr.indexOf(colIndice[i])) != -1) {
				// 获取当前值
				var value = row.getCellValue(colIndice);
				// 获取当前元素是否为必填项
				var eleRequired = this.eleArr[eleIndex].isRequired;
				// 如果ds校验通过，但当前值为空且form中不允许该值为空，则设置为校验不通过，设置描述信息
				if (describes[i] == ""
						&& ((value == null || value == "") && eleRequired)) {
					describes[i] = trans('ml_thisfieldcannotnull');
				}

				var eleHtml = this.eleArr[eleIndex].Div_gen;
				if (describes[i] != ""
						&& ((value != null && value != "") || ((value == null || value == "") && eleRequired))) { // 校验失败（若ds校验是因为非空而报错，而当前form中允许该元素为空，则不认为是错误）
					// 错误类型，包括错误BaseComponent.ELEMENT_ERROR和警告BaseComponent.ELEMENT_WARNING
					var errorType = BaseComponent.ELEMENT_ERROR;
					// 自定义校验结果（包含2个变量：1："type"错误类型，包括BaseComponent.ELEMENT_ERROR和BaseComponent.ELEMENT_WARNING；2："describe"：错误描述信息）
					var userCheckResult = null;
					if (typeof selfDefDataCheck != "undefined") { // 如果在当前节点下的include.js中定义了自定义数据校验方法，则进行自定义校验
						// 自定义数据校验方法：方法名为selfDefDataCheck，变量1为dataset的ID，变量2为列id，变量3为输入值
						userCheckResult = selfDefDataCheck(this.dataset.id,
								this.dataset.metadata[colIndice[i]].key, value);
						if (userCheckResult)
							errorType = userCheckResult.type;
					}

					// 设置错误提示信息
					if (userCheckResult != null) { // 有自定义校验结果
						if (userCheckResult.type == BaseComponent.ELEMENT_ERROR) {
							AutoFormComp.setElementErrorMessage(
									this.eleArr[eleIndex],
									BaseComponent.ELEMENT_ERROR,
									userCheckResult.describe, this.renderType);
						} else if (userCheckResult.type == BaseComponent.ELEMENT_WARNING) {
							AutoFormComp.setElementErrorMessage(
									this.eleArr[eleIndex],
									BaseComponent.ELEMENT_WARNING,
									userCheckResult.describe, this.renderType);
						}
					} else { // 默认校验结果
						AutoFormComp.setElementErrorMessage(
								this.eleArr[eleIndex],
								BaseComponent.ELEMENT_ERROR, describes[i],
								this.renderType);
					}

					if (this.eleArr[eleIndex].setError)
						this.eleArr[eleIndex].setError(true);

					// 设置错误样式和校验结果类型
					if (this.eleArr[eleIndex].Div_text) {
						if (userCheckResult != null) { // 有自定义校验结果
							if (userCheckResult.type == BaseComponent.ELEMENT_ERROR) {
								// 设置错误样式
								this.eleArr[eleIndex].setErrorStyle();
								// 设置校验结果类型
								if (this.eleArr[eleIndex].setCheckResult)
									this.eleArr[eleIndex]
											.setCheckResult(BaseComponent.ELEMENT_ERROR);
							} else if (userCheckResult.type == BaseComponent.ELEMENT_WARNING) {
								// 设置错误样式
								this.eleArr[eleIndex].setWarningStyle();
								// 设置校验结果类型
								if (this.eleArr[eleIndex].setCheckResult)
									this.eleArr[eleIndex]
											.setCheckResult(BaseComponent.ELEMENT_WARNING);
							}
						} else {
							this.eleArr[eleIndex].setErrorStyle();
						}
					} else if (eleHtml.className.indexOf(" checkedborder") == -1) {
						eleHtml.className += " checkedborder";
					}

					// 设置鼠标悬停提示
					if (this.eleArr[eleIndex].setErrorMessage) {
						if (userCheckResult != null) // 有自定义校验结果
							this.eleArr[eleIndex]
									.setErrorMessage(userCheckResult.describe);
						else
							this.eleArr[eleIndex].setErrorMessage(describes[i]);
					}

					if (this.dataCheckErrorRows == null)
						this.dataCheckErrorRows = new HashMap();
					// 当前未通过校验的行没有在map中,则在map中添加该数据
					if ((this.dataCheckErrorRows.containsKey(row.rowId)) == false)
						this.dataCheckErrorRows.put(row.rowId, [[eleIndex],
								[describes[i]], [errorType]]);
					else {
						var value = this.dataCheckErrorRows.get(row.rowId);
						value[0].push(eleIndex);
						value[1].push(describes[i]);
						value[2].push(errorType);
					}
					// 失败
					success = false;
				} else { // 校验成功
					if (value == null || value == "") {
						// 校验成功后去掉失败时的提示，恢复默认提示
						AutoFormComp.setElementErrorMessage(
								this.eleArr[eleIndex],
								BaseComponent.ELEMENT_NORMAL,
								this.eleArr[eleIndex].inputAssistant,
								this.renderType);
						// 设置校验结果类型
						if (this.eleArr[eleIndex].setCheckResult)
							this.eleArr[eleIndex]
									.setCheckResult(BaseComponent.ELEMENT_NORMAL);
					} else {
						// 校验成功后设置成功提示
						AutoFormComp.setElementErrorMessage(
								this.eleArr[eleIndex],
								BaseComponent.ELEMENT_SUCCESS, "",
								this.renderType);
						// 设置校验结果类型
						if (this.eleArr[eleIndex].setCheckResult)
							this.eleArr[eleIndex]
									.setCheckResult(AutoFormComp.ELEMENT_SUCESS);
					}

					// 设置普通样式
					if (this.eleArr[eleIndex].Div_text) {
						this.eleArr[eleIndex].setNormalStyle();
					} else {
						var className = eleHtml.className.replace(
								" checkedborder", "");
						eleHtml.className = className;
					}

					if (this.eleArr[eleIndex].setError)
						this.eleArr[eleIndex].setError(false);
					// 设置鼠标悬停提示
					var title = row.getCellValue(colIndice);
					if (this.eleArr[eleIndex].setMessage)
						this.eleArr[eleIndex].setMessage(title);

					if (this.dataCheckErrorRows == null)
						this.dataCheckErrorRows = new HashMap();
					// 当前通过校验的行在map中,则在map中删除该数据
					if ((this.dataCheckErrorRows.containsKey(row.rowId)) == true)
						this.dataCheckErrorRows.remove(row.rowId);
				}
			}
		}
		// 调用AutoForm整体校验结果事件
		if (success == true) {
			this.DataCheckSuccess(event);
		} else {
			this.DataCheckFailed(event);
		}
	}
	// metadata changeg 事件
	else if (MetaChangeEvent.prototype.isPrototypeOf(event)) {
		// 处理精度
		if (event.precision != null) {
			var index = this.indiceArr.indexOf(event.colIndex);
			this.eleArr[index].setPrecision(event.precision, true);
		}
	}
};

/**
 * 设置元素的错误信息
 * 
 * @param element
 *            元素对象
 * @param type
 *            类型（"error"、"warning"、"normal"）
 * @param message
 *            信息
 * @param renderType
 *            布局类型（1-固定布局；2-流式布局；3-固定提示布局）
 * 
 * @private
 */
AutoFormComp.setElementErrorMessage = function(element, type, message,
		renderType) {
	if (renderType == 3) { // 固定提示布局类型
		var messageDiv = null;
		if (element.Div_gen.parentNode.nextSibling && element.Div_gen.parentNode.nextSibling.nextSibling)
			messageDiv = element.Div_gen.parentNode.nextSibling.nextSibling;
		if 	(messageDiv == null) return;
		if (type == BaseComponent.ELEMENT_ERROR) {
			messageDiv.style.color = "red";
			var innerHTML = "<div style='float:left'><img style='height:16px;width:16px;margin-top:0px;' src='"
					+ window.themePath + "/images/text/error.gif" + "'/></div>";
			if (!IS_IE)
				innerHTML = innerHTML
						+ "<div style='float:left;padding-top:1px'>&nbsp;"
						+ message + "</div>";
			else
				innerHTML = innerHTML
						+ "<div style='float:left;padding-top:1px'>&nbsp;&nbsp;&nbsp;"
						+ message + "</div>";
			messageDiv.innerHTML = innerHTML;
		} else if (type == BaseComponent.ELEMENT_WARNING) {
			messageDiv.style.color = "#F99F0F";
			var innerHTML = "<div style='float:left'><img style='height:16px;width:16px;margin-top:0px;' src='"
					+ window.themePath
					+ "/images/text/warning.gif"
					+ "'/></div>";
			if (!IS_IE)
				innerHTML = innerHTML
						+ "<div style='float:left;padding-top:1px'>&nbsp;"
						+ message + "</div>";
			else
				innerHTML = innerHTML
						+ "<div style='float:left;padding-top:1px'>&nbsp;&nbsp;&nbsp;"
						+ message + "</div>";
			messageDiv.innerHTML = innerHTML;
		} else if (type == BaseComponent.ELEMENT_SUCCESS) {
			messageDiv.style.color = "#000000";
			var innerHTML = "<div style='float:left'><img style='height:16px;width:16px;margin-top:0px;' src='"
					+ window.themePath
					+ "/images/text/success.gif"
					+ "'/></div>";
			messageDiv.innerHTML = innerHTML;
		} else if (type == BaseComponent.ELEMENT_NORMAL) {
			messageDiv.style.color = "gray";
			messageDiv.innerHTML = message;
		}
	}
};

/**
 * 数据校验成功事件
 * 
 * @private
 */
AutoFormComp.prototype.DataCheckSuccess = function(dataCheckEvent) {
	this.doEventFunc("onSuccess", DataCheckListener.listenerType,
			dataCheckEvent);
};

/**
 * 数据校验失败事件
 * 
 * @private
 */
AutoFormComp.prototype.DataCheckFailed = function(dataCheckEvent) {
	this
			.doEventFunc("onFailed", DataCheckListener.listenerType,
					dataCheckEvent);
};

//AutoFormComp.prototype.createLabel = function(pDiv, comp) {
AutoFormComp.prototype.createType4Div = function(pDiv, comp) {
	var outerWidth = 0;
	if (comp.formWidth == null)
		outerWidth = pDiv.offsetWidth;
	else{
		if (comp.formWidth.indexOf("%") != -1)
			outerWidth = parseInt(comp.formWidth) * pDiv.offsetWidth / 100;
		else	
			outerWidth = comp.formWidth;
	}	
	var outerDiv = $ce("DIV");
	outerDiv.style.width = outerWidth + "px";
	outerDiv.style.height = "100%";
	pDiv.appendChild(outerDiv);
	
	//标签
	var labelWidth = 0;
	var label = comp.labelName;
	var labelDiv = null;
	//百分比情况下，在控件外包一层div
	var compOuterDiv = null;
	if(label != null && label != ""){
		// checkbox不设置label
		if (comp.componentType == "CHECKBOX")
			label = "";
		labelDiv = $ce("DIV");
		labelDiv.style.textAlign = "right";	
		labelDiv.style.paddingTop = "2px";
		labelDiv.style.paddingRight = "5px";
		labelDiv.style[ATTRFLOAT] = "left";
		labelDiv.style.position = "relative";
		labelDiv.innerHTML = label;
		//lable颜色
		if (comp.labelColor)
			labelDiv.style.color = comp.labelColor;
		//必填项，加红色 *	
		if (comp.isRequired && label != null){
			var span = $ce("span");
			span.style.color = "red";
			span.innerHTML = "*"; 
			labelDiv.appendChild(span);
			labelWidth = getTextWidth(label + "*", null);
		}
		else
			labelWidth = getTextWidth(label, null);
		outerDiv.appendChild(labelDiv);
		//控件宽度在百分比和非百分比情况	
		if (comp.basewidth.indexOf('%') == -1){
			labelWidth = outerWidth - comp.basewidth - 5;
		}	
		else{
			compOuterDiv = $ce("DIV");
			compOuterDiv.id = "compOuter";
			compOuterDiv.style[ATTRFLOAT] = "right";
			compOuterDiv.style.height = "100%";
			compOuterDiv.style.width = outerWidth - labelWidth - 5 + "px"; 
		}
		labelDiv.style.width = labelWidth + "px"; 	
	}
	
	//控件
	var objHtml = comp.getObjHtml();
	if (objHtml != null){
		if (compOuterDiv != null){
			outerDiv.appendChild(compOuterDiv);
			compOuterDiv.appendChild(objHtml);
		}
		else{
			objHtml.style[ATTRFLOAT] = "right";
			outerDiv.appendChild(objHtml);
		}
	}
	comp.attachedLabel = labelDiv; 

	
	
	
	////////////////////////////////////////////////////
//	var divWidth = pDiv.offsetWidth;
//	var labelWidth = 0;
//	var label = comp.labelName;
//	var labelDiv = null;
//	if(label != null && label != ""){
//		// checkbox不设置label
//		if (comp.componentType == "CHECKBOX")
//			label = "";
//		labelDiv = $ce("DIV");
//		labelDiv.style.textAlign = "right";	
//		labelDiv.style.paddingTop = "2px";
////		labelDiv.style.paddingRight = "5px";
//		labelDiv.style[ATTRFLOAT] = "left";
//		labelDiv.style.position = "relative";
//		labelDiv.innerHTML = label;
//		//lable颜色
//		if (comp.labelColor)
//			labelDiv.style.color = comp.labelColor;
//		//必填项，加红色 *	
//		if (comp.isRequired && label != null){
//			var span = $ce("span");
//			span.style.color = "red";
//			span.innerHTML = "*"; 
//			labelDiv.appendChild(span);
//			labelWidth = getTextWidth(label + "*", null);
//		}
//		else
//			labelWidth = getTextWidth(label, null);
//		labelDiv.style.width = labelWidth + "px"; 	
//		pDiv.appendChild(labelDiv);
//	}
//	var eleDiv = $ce("DIV");
//	eleDiv.style[ATTRFLOAT] = "left";
//	eleDiv.style.position = "relative";
//	eleDiv.style.height = "100%";
//	var eleDivWidth = (divWidth - labelWidth) > 0 ? (divWidth - labelWidth) : 0;
//	eleDiv.style.width = eleDivWidth + "px";
//	comp.pDiv = eleDiv; 
//	pDiv.appendChild(eleDiv);
//	
//	var objHtml = comp.getObjHtml();
//	if (objHtml != null){
//		objHtml.style[ATTRFLOAT] = "right";
//		eleDiv.appendChild(objHtml);
//	}
//	comp.attachedLabel = labelDiv; 
////	return labelDiv;
	
	////////////////////////////////////////////////////////////////
//	var divLabel = document.createElement("div");
//	divLabel.style.textAlign = "right";
//	divLabel.style.position = "absolute";
//	if (width <= 0)
//		width = 20;
//
//	divLabel.style.width = FlowGridLayout.getLabelDivWidth(width) + "px";
//	divLabel.style.height = "100%";
//	divLabel.style.left = (left + FlowGridLayout.COLSPACE - 2) + "px";
//	divLabel.style.top = top + "px";
//	divLabel.style.paddingTop = "4px";
//
//	divLabel.style.textOverflow = "ellipsis";
//	divLabel.style.whiteSpace = "nowrap";
//	var htmlStr = null;
//	if (comp.isRequired)
//		htmlStr = "<div style='position:relative;right:8px;'>"
//				+ (comp.componentType == "CHECKBOX" ? "" : comp.labelName)
//				+ "<span style='color:red'>*</span></div>";
//	else {
//		htmlStr = "<div style='position:relative;right:8px;'>"
//				+ (comp.componentType == "CHECKBOX" ? "" : comp.labelName)
//				+ "</div>";
//		if (comp.labelColor)
//			divLabel.style.color = comp.labelColor;
//	}
//	divLabel.innerHTML = htmlStr;
//	return divLabel;
};


AutoFormComp.prototype.createType4DivTop = function(pDiv, comp) {
//	var divWidth = pDiv.offsetWidth;
//	var labelWidth = 0;
	var label = comp.labelName;
	var labelDiv = null;
	if(label != null && label != ""){
		// checkbox不设置label
		if (comp.componentType == "CHECKBOX")
			label = "";
		labelDiv = $ce("DIV");
		labelDiv.style.textAlign = "left";	
		labelDiv.style.paddingTop = "2px";
//		labelDiv.style.paddingRight = "5px";
//		labelDiv.style[ATTRFLOAT] = "left";
		labelDiv.style.position = "relative";
		labelDiv.innerHTML = label;
		//lable颜色
		if (comp.labelColor)
			labelDiv.style.color = comp.labelColor;
		//必填项，加红色 *	
		if (comp.isRequired && label != null){
			var span = $ce("span");
			span.style.color = "red";
			span.innerHTML = "*"; 
			labelDiv.appendChild(span);
//			labelWidth = getTextWidth(label + "*", null);
		}
//		else
//			labelWidth = getTextWidth(label, null);
//		labelDiv.style.width = labelWidth + "px"; 	
		pDiv.appendChild(labelDiv);
	}
//	var eleDiv = $ce("DIV");
//	eleDiv.style[ATTRFLOAT] = "left";
//	eleDiv.style.position = "relative";
//	eleDiv.style.height = "100%";
//	var eleDivWidth = (divWidth - labelWidth) > 0 ? (divWidth - labelWidth) : 0;
//	eleDiv.style.width = eleDivWidth + "px";
//	comp.pDiv = eleDiv; 
//	pDiv.appendChild(eleDiv);
	
	var objHtml = comp.getObjHtml();
	if (objHtml != null){
//		objHtml.style[ATTRFLOAT] = "right";
		pDiv.appendChild(objHtml);
	}
	comp.attachedLabel = labelDiv; 
};
/**
 * @fileoverview form控件的自定义element,该组件使form包含任意用户自定义控件成为可能,组件本身采用委托模式,该组件发生状态
 *               变换或者发生事件时,将把该状态变化,事件派发出去,用户可以重载该组件暴露的方法观察该组件的变化,从而改变该组件内部的自定义控件
 *               的状态,或者处理自己的逻辑
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */
SelfDefElement.prototype = new BaseComponent;
SelfDefElement.prototype.componentType = "SELFDEFELEMENT";

/**
 * 表单的自定义元素
 * 
 * @class form控件的自定义element，该组件使form包含任意用户自定义控件成为可能，组件本身采用委托模式，该组件发生状态
 *        变换或者发生事件时，将把该状态变化用事件派发出去，用户可以重载该组件暴露的方法观察该组件的变化，从而改变该组件内部的自定义控件
 *        的状态，或者处理自己的逻辑。
 */
function SelfDefElement(parent, name, left, top, width, height, position,
		attrArr) {
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.disabled = false;
	this.value = "";
	if (attrArr != null) {
		this.disabled = getBoolean(attrArr.disabled, false);
		this.value = getString(attrArr.value, this.value);
	}
	this.labelName = "";

	this.create();
};

/**
 * @private
 */
SelfDefElement.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.name;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	if (this.width != null && this.width != "") {
		if (this.width.toString().indexOf("%") == -1)
			this.Div_gen.style.width = this.width + "px";
		else
			this.Div_gen.style.width = this.width;
	}
	if (this.height != null && this.height != "") {
		if (this.height.toString().indexOf("%") == -1)
			this.Div_gen.style.height = this.height + "px";
		else
			this.Div_gen.style.height = this.height;
	}
	this.Div_gen.style.overflow = "hidden";

	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * @private
 */
SelfDefElement.prototype.manageSelf = function() {
	var oThis = this;
	// 初始禁用
	if (this.disabled == true) {
		this.setActive(false);
	}

	if (this.value)
		this.setValue(this.value);

	// 失去键盘焦点时调用
	this.Div_gen.onblur = function(e) {
		e = EventUtil.getEvent();
		oThis.onblur(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	this.Div_gen.onmouseover = function(e) {
		e = EventUtil.getEvent();
		oThis.onmouseover(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	this.Div_gen.onclick = function(e) {
		e = EventUtil.getEvent();
		oThis.onclick(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
};

/**
 * 增加内容
 */
SelfDefElement.prototype.setContent = function(contentObj) {
	this.content = contentObj;
	if (contentObj instanceof BaseComponent){
		if(contentObj.Div_gen.parentNode)
			this.Div_gen.appendChild(contentObj.Div_gen.parentNode);
		else
			this.Div_gen.appendChild(contentObj.Div_gen);
	}
	else
		this.Div_gen.appendChild(contentObj);
};

/**
 * 获取内容
 */
SelfDefElement.prototype.getContent = function() {
	return this.content;
};

/**
 * @private
 */
SelfDefElement.prototype.setActive = function(isActive) {
	var isActive = getBoolean(isActive, false);
	if (isActive)
		this.active();
	else
		this.inActive();
	this.disabled = !isActive;
	if (this.content && this.content.setActive)
		this.content.setActive(isActive);
};

/**
 * 用户重载函数,激活元素
 * 
 * @private
 */
SelfDefElement.prototype.active = function() {
	var simpleEvent = {
		"obj" : this
	};
	this.doEventFunc("active", AutoformListener.listenerType, simpleEvent);
};

/**
 * 用户重载函数,禁用元素
 * 
 * @private
 */
SelfDefElement.prototype.inActive = function() {
	var simpleEvent = {
		"obj" : this
	};
	this.doEventFunc("inActive", AutoformListener.listenerType, simpleEvent);
};

/**
 * 设值
 * 
 * @private
 */
SelfDefElement.prototype.setValue = function(value) {
	if (this.content != null && this.content.contentObj != null)
		this.content.contentObj.setValue(value);
//	var setValueEvent = {
//		"obj" : this,
//		"value" : value
//	};
//	this.doEventFunc("setValue", AutoformListener.listenerType, setValueEvent);
};

/**
 * 聚焦事件
 * 
 * @private
 */
SelfDefElement.prototype.onblur = function(e) {
	var focusEvent = {
		"obj" : this,
		"event" : e
	};
	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
};

/**
 * 点击事件
 * 
 * @private
 */
SelfDefElement.prototype.onclick = function(e) {
	var mouseEvent = {
		"obj" : this,
		"event" : e
	};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 鼠标移入事件
 * 
 * @private
 */
SelfDefElement.prototype.onmouseover = function(e) {
	var mouseEvent = {
		"obj" : this,
		"event" : e
	};
	this.doEventFunc("onmouseover", MouseListener.listenerType, mouseEvent);
};

/**
 * 必须实现,获取控件的值
 */
SelfDefElement.prototype.getValue = function() {
	if (this.content != null && this.content.contentObj != null)
		return this.content.contentObj.getValue();
//	var simpleEvent = {
//		"obj" : this
//	};
//	this.doEventFunc("getValue", AutoformListener.listenerType, simpleEvent);
};

/**
 * 获取对象信息
 * 
 * @private
 */
SelfDefElement.prototype.getContext = function() {
	var context = new Object;
	context.id = this.id;
	context.enabled = !this.disabled;
	context.readyOnly = this.readyOnly;
	return context;
};

/**
 * 设置对象信息
 * 
 * @private
 */
SelfDefElement.prototype.setContext = function(context) {
	if (context.enabled != null
			&& (this.disabled == null || this.disabled == context.enabled))
		this.setActive(context.enabled);
	if (context.readyOnly != null
			&& (this.readyOnly == null || this.readyOnly == context.readyOnly))
		this.setReadyOnly(context.readyOnly);
};

/**
 * 设置大小及位置
 * @param width 像素大小
 * @param height 像素大小
 */
SelfDefElement.prototype.setBounds = function(left, top, width, height) {	
	this.left = left;
	this.top = top;
	
	this.width = width;
	this.height = height;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	
	if (typeof(width) == "string"  && width.indexOf("%") != -1)
		this.Div_gen.style.width = this.width;
	else	
		this.Div_gen.style.width = this.width + "px";
	
	if (typeof(height) == "string"  && height.indexOf("%") != -1)	
		this.Div_gen.style.height = this.height;
	else
		this.Div_gen.style.height = this.height + "px";
	
};


/**
 * 获取对象信息
 * 
 * @private
 */
AutoFormComp.prototype.getContext = function(type) {
	var context = new Object;
	context.c = "AutoFormContext";
	context.id = this.id;
	context.enabled = this.editable;
	context.readOnly = this.readOnly;

	if (type == "all_child") {
		context.elementContexts = new Array();
		for (var i = 0, n = this.getElements().length; i < n; i++) {
			var ctx = this.getElementByIndex(i).getContext();
			ctx.c = "FormElementContext";
			ctx.label = this.getElementByIndex(i).labelName;
			ctx.labelColor = this.getElementByIndex(i).labelColor;
			context.elementContexts.push(ctx);
		}
	}
	return context;
};

/**
 * 设置对象信息
 * 
 * @private
 */
AutoFormComp.prototype.setContext = function(context) {
	//debugger;
	if (context.enabled != null
			&& (this.editable == null || this.editable != context.enabled))
		this.setEditable(context.enabled);
	if (context.readOnly != null
			&& (this.readOnly == null || this.readOnly != context.readOnly))
		this.setReadOnly(context.readOnly);
	var needRepaint = false;
	if (context.elementContexts) {
		for (var i = 0, n = context.elementContexts.length; i < n; i++) {
			for (var j = 0, m = this.getElements().length; j < m; j++) {
				if (this.getElementByIndex(j).id == context.elementContexts[i].id) {
					var ctx = context.elementContexts[i];
					var comp = this.getElementByIndex(j);
					if (comp.visible != ctx.visible) {
						comp.visible = ctx.visible;
						needRepaint = true;
					}
					if (ctx.label && comp.labelName != ctx.label) {
						comp.labelName = ctx.label;
						needRepaint = true;
					}
					if (ctx.labelColor != null && ctx.labelColor != comp.labelColor){
						comp.labelColor = ctx.labelColor;
						if (comp.attachedLabel != null)
							comp.attachedLabel = null;
						needRepaint = true;
					}
					ctx.isform = true;
					// 表单值以Dataset为准
					if (this.editable == false)
						ctx.enabled = false;
					comp.setContext(ctx);
				}
			}
		}
	}
	if (needRepaint) {
		// dingrf
		// if (this.renderType == 2) {
		this.pLayout.paint(true);
		// }
	}
};

/**
 * 固定布局时，Context变化后，重绘方法
 */
AutoFormComp.prototype.paint = function() {
	// 如果是流式布局，退出
	if (this.renderType == 2)
		return;
	// 取出table的列数
	var table = this.parentDiv.childNodes[0];
	var cells = table.rows.item(0).cells.length;

	// 删除form中的table
	this.parentDiv.removeChild(this.parentDiv.childNodes[0]);
	for (var i = 0, n = this.getElements().length; i < n; i++) {
		var ctx = this.getContext();
	}

};
