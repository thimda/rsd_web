/**
 * @fileoverview
 * 
 * @author guoweic
 * @version NC6.0
 * 
 */
InputDialogComp.prototype.componentType = "INPUTDIALOG";
InputDialogComp.HEIGHT = 90;
InputDialogComp.WIDTH = 350;

InputDialogComp.LABEL_WIDTH = 80;
InputDialogComp.ITEM_HEIGHT = 30;

InputDialogComp.STRING_TYPE = "string";
InputDialogComp.PSWTEXT_TYPE = "pswtext";
InputDialogComp.INT_TYPE = "int";
InputDialogComp.DECIMAL_TYPE = "decimal";
InputDialogComp.COMBO_TYPE = "combo";
InputDialogComp.RADIO_TYPE = "radio";
InputDialogComp.LABEL_TYPE = "label";

InputDialogComp.prototype = new ListenerUtil;

/**
 * Input对话框构造函数
 * @class Input对话框类。<br>
 *        Input对话框是对ModalDialog的封装，在对话框底部添加了OK，
 *        Cancel按钮。使用者只需要给出ContentPane部分内容信息即可。<br>
 *        <b>组件皮肤设置说明：</b><br>
 *        inputdialog.css是对InputDialog中显示字体的外观控制，如字体、大小等。<br>
 *        modaldialog.css是对InputDialog外观整体的控制，包括标题背景，内容区背景等。<br>
 *        <br>
 * 
 * <b>使用js，直接通过new的用法生成InputDialog实例：</b><br>
 * inputdialog = InputDialogComp.showDialog(text); // 用需要显示的字符串代替text<br>
 * inputdialog.onOk = input; //把点击确认按钮后需要执行的函数名赋给onOk属性<br>
 * inputdialog.onCancel = noinput; //把点击取消需要执行的函数名赋给onCancel属性<br>
 * 
 * @constructor InputDialogComp的构造函数
 * @param name 对话框名字
 * @param title 对话框标题
 * @param labelWidth label的宽度
 * @param okFunc 点击确定按钮时调用的函数名
 * @param cancelFunc 点击取消按钮时调用的函数名
 * @param obj1 调用okFunc函数的对象
 * @param obj2 调用cancelFunc函数的对象
 * @param{boolean} another 由于确认对话框生命周期的特殊性,特殊时候需要另一个实例
 */
function InputDialogComp(name, title, left, top, width, height, labelWidth, okFunc, cancelFunc, obj1, obj2, zIndex, another) {
	if (!another) {
		/*
		this.getInstance = Singleton;
		if (instance = this.getInstance())
			return instance;
		*/
	}
	this.name = name;
	this.title = getString(title, "Dialog");
	this.left = left;
	this.top = top;
	this.width = width ? width : InputDialogComp.WIDTH;
	this.height = height ? height : InputDialogComp.HEIGHT;
	if (height)  // 固定高度
		this.fixHeight = true;
	this.labelWidth = labelWidth ? labelWidth : InputDialogComp.LABEL_WIDTH;
	// 输入项集合
	this.items = new HashMap;

	ListenerUtil.call(this, true);
	
	// 调用create方法创建dialog
	this.create();

	if (zIndex)
		this.modaldialog.Div_gen.style.zIndex = zIndex;
	
	if (okFunc) {
		this.onOk = function() {
			if (obj1)
				return okFunc.call(obj1);
			else
				return okFunc();
		};
	}

	if (cancelFunc) {
		this.onCancel = function() {
			if (obj2)
				cancelFunc.call(obj2);
			else
				cancelFunc();
		};
	}
};

/**
 * InputDialogComp的创建函数
 * 
 * @private
 */
InputDialogComp.prototype.create = function() {
	var oThis = this;
	this.modaldialog = new ModalDialogComp(this.name, this.title, this.left,
			this.top, this.width, this.height, "");
	this.modaldialog.contentDiv.style.overflow = "hidden";		

	this.contentDiv = $ce("DIV");
	this.contentDiv.className = "input_contentdiv";
	if (this.fixHeight) {
		this.contentDiv.style.height = this.height - 105 + "px";
		this.contentDiv.style.overflowY = "auto";
	}
	this.modaldialog.getContentPane().appendChild(this.contentDiv);
	
	this.bottomDiv = $ce("DIV");
	this.modaldialog.getContentPane().appendChild(this.bottomDiv);
	this.bottomDiv.className = "input_bottomdiv";

	this.okBtDiv = $ce("DIV");
	this.okBtDiv.className = "input_okbtdiv";
	this.cancelBtDiv = $ce("DIV");
	this.cancelBtDiv.className = "input_cancelbtdiv";
	if (!IS_IE) {
		this.okBtDiv.style.textAlign = "-moz-right";
		this.cancelBtDiv.style.textAlign = "-moz-left";
	}
	if (IS_IE6) {
		this.okBtDiv.style.paddingTop = "3px";
		this.cancelBtDiv.style.paddingTop = "3px";
	}
	this.bottomDiv.appendChild(this.cancelBtDiv);
	this.bottomDiv.appendChild(this.okBtDiv);

	// 生成确定按钮
	this.okBt = new ButtonComp(this.okBtDiv, "okBt", 0, 0, "74", "23",
			trans("ml_ok"), "", '', "relative", "", false, "button_div");
	this.okBt.onclick = function(e) {
		var ishide = oThis.onOk();
		if(ishide || ishide == undefined)
			oThis.hide();
	};
	// 生成取消按钮
	this.cancelBt = new ButtonComp(this.cancelBtDiv, "cancelBt", 0, 0, "74",
			"23", trans("ml_cancel"), "", '', "relative", "", false, "button_div");
	this.cancelBt.onclick = function(e) {
		oThis.onCancel();
		oThis.hide();
	};
	if (IS_IE8) {
		this.okBt.Div_gen.style[ATTRFLOAT] = "right";
	}
	
};

/**
 * 增加输入框
 * 
 * @param labelText label显示文字
 * @param inputId 输入框ID
 * @param inputType 输入框类型（string, int, combo）
 * @param isMust 是否必填
 * @param attrArr 输入框额外属性
 * @param attrArr.maxValue 整型输入框时的最大值
 * @param attrArr.minValue 整型输入框时的最小值
 * @param attrArr.precision 数字型输入框时的精确度
 * @param attrArr.selectOnly 下拉输入框时是否只能选择（不能手工输入）
 * @param attrArr.comboData 下拉输入框时的内容
 */
InputDialogComp.prototype.addItem = function(labelText, inputId, inputType, isMust, attrArr, value,className) {
	// 创建子项
	var itemDiv = $ce("DIV");
	itemDiv.className = "input_itemdiv";
	if (!this.fixHeight) {  // 不是固定高度
		if (inputType == InputDialogComp.RADIO_TYPE){
			if (attrArr && attrArr.comboData) {
				this.height += InputDialogComp.ITEM_HEIGHT * attrArr.comboData.combItems.length;		
				this.contentDiv.style.height = (this.contentDiv.offsetHeight + InputDialogComp.ITEM_HEIGHT * attrArr.comboData.combItems.length) + "px";	
			}
		}
		else{
			this.height += InputDialogComp.ITEM_HEIGHT;
			this.contentDiv.style.height = this.contentDiv.offsetHeight + InputDialogComp.ITEM_HEIGHT + "px";
		}
		this.modaldialog.setSize(this.width, this.height);
	}
	
	this.contentDiv.appendChild(itemDiv);
	
	// 左边Label区域
	itemDiv.leftDiv = $ce("DIV");
	itemDiv.leftDiv.className = "input_leftdiv";
	itemDiv.leftDiv.style.width = this.labelWidth + "px";
	itemDiv.appendChild(itemDiv.leftDiv);
	if (inputType == InputDialogComp.LABEL_TYPE){
		itemDiv.leftDiv.style.width = "0";
	}
	else
		var label = new LabelComp(itemDiv.leftDiv, "", 10, 9, labelText, "relative", null);
	// 右边输入区域
	itemDiv.rightDiv = $ce("DIV");
	itemDiv.rightDiv.className = "input_rightdiv";
	itemDiv.appendChild(itemDiv.rightDiv);
	itemDiv.rightDiv.style.width = itemDiv.rightDiv.parentNode.offsetWidth - itemDiv.leftDiv.offsetWidth - 30 + "px";
	
	var inputComp;
	var inputWidth = this.width - this.labelWidth - 80 + "px";
	if (inputType == InputDialogComp.STRING_TYPE) {  // 字符串输入框
		inputComp = new StringTextComp(itemDiv.rightDiv, inputId, 10, 5, inputWidth, "relative", attrArr, null);
	}else if (inputType == InputDialogComp.PSWTEXT_TYPE) {  // 整型输入框
		inputComp = new PswTextComp(itemDiv.rightDiv, inputId, 10, 5, inputWidth, "relative", attrArr ? attrArr.maxValue : null, attrArr ? attrArr.minValue : null, attrArr, null);
	} else if (inputType == InputDialogComp.INT_TYPE) {  // 整型输入框
		inputComp = new IntegerTextComp(itemDiv.rightDiv, inputId, 10, 5, inputWidth, "relative", attrArr ? attrArr.maxValue : null, attrArr ? attrArr.minValue : null, attrArr, null);
	}else if (inputType == InputDialogComp.LABEL_TYPE) {  // 整型输入框
		inputComp = new LabelComp(itemDiv.rightDiv, inputId, 10, 5, labelText,"relative",className);
	} else if (inputType == InputDialogComp.DECIMAL_TYPE) {
		inputComp = new FloatTextComp(itemDiv.rightDiv, inputId, 10, 5, inputWidth, "relative", attrArr ? attrArr.precision : null, attrArr, null);
	} 
	else if (inputType == InputDialogComp.COMBO_TYPE) {  // 下拉输入框
		inputComp = new ComboComp(itemDiv.rightDiv, inputId, 10, 5, inputWidth, "relative", attrArr ? attrArr.selectOnly : true, attrArr, null);
		if (attrArr && attrArr.comboData) {
			inputComp.setComboData(attrArr.comboData);
			if (attrArr.selectIndex)
				inputComp.setSelectedItem(attrArr.selectIndex);
		}
	} 
	else if (inputType == InputDialogComp.RADIO_TYPE){
		attrArr.changeLine = true;
		inputComp = new RadioGroupComp(itemDiv.rightDiv, inputId, 10, 5, inputWidth, "relative",  attrArr, null);
		if (attrArr && attrArr.comboData) {
			inputComp.setComboData(attrArr.comboData);
			if (attrArr.selectIndex)
				inputComp.setSelectedItem(attrArr.selectIndex);
		}
	}
	if (inputComp)
		this.items.put(inputId, inputComp);
	
	if(value != null)
		inputComp.setValue(value);
	// 必输项提示
	if (isMust == true) {
		itemDiv.mustDiv = $ce("DIV");
		itemDiv.mustDiv.className = "input_mustdiv";
		itemDiv.mustDiv.innerHTML = "<font color='red'>*</font>";
		itemDiv.appendChild(itemDiv.mustDiv);
	}
};

/**
 * 获取所有输入子项集合
 */
InputDialogComp.prototype.getItems = function() {
	return this.items;
};

/**
 * 根据ID获取输入子项
 */
InputDialogComp.prototype.getItem = function(id) {
	if (this.items.containsKey(id))
		return this.items.get(id);
	else
		return null;
};

/**
 * 关闭对话框
 * @private
 */
InputDialogComp.prototype.close = function() {
	this.onCancel();
	this.hide();
};

/**
 * 得到要添加内容的面板
 */
InputDialogComp.prototype.getContentPane = function() {
	return this.panel.getObjHtml();
};

/**
 * 显示对话框
 * 
 * @private
 */
InputDialogComp.prototype.show = function() {
	this.modaldialog.show();
};

/**
 * 隐藏对话框
 * 
 * @private
 */
InputDialogComp.prototype.hide = function() {
	this.modaldialog.hide();
};

/**
 * 创建一个对话框的类方法
 * 
 * @param message 要显示的信息
 * @param okFunc 点击确定按钮时调用的函数名
 * @param cancelFunc 点击取消按钮时调用的函数名
 * @param obj1 调用okFunc函数的对象
 * @param obj2 调用cancelFunc函数的对象
 * @param{boolean} another 由于确认对话框生命周期的特殊性,特殊时候需要另一个实例
InputDialogComp.showDialog = function(name, title, left, top, width, height, labelWidth, okFunc, cancelFunc, obj1, obj2, zIndex, another) {
	if (!another) {
		if (!window.globalObject.$c_InputDialog)
			window.globalObject.$c_InputDialog = new InputDialogComp("inputDialog", trans("ml_inputdialog"), 
					left, top, width, height, labelWidth, okFunc, cancelFunc, obj1, obj2, zIndex, another);
		var dialog = window.globalObject.$c_InputDialog;
	} else {
		var dialog = new InputDialogComp("inputDialog", trans("ml_inputdialog"), 
				left, top, width, height, labelWidth, okFunc, cancelFunc, obj1, obj2, zIndex, another);
	}

	dialog.show();
	return dialog;
};
 */

/**
 * 隐藏对话框
 */
InputDialogComp.hideDialog = function() {
	var dialog = window.globalObject.$c_InputDialog;
	dialog.hide();
};

/**
 * 暴露给用户覆盖的函数,点击确认按钮后调用此函数
 * @private
 */
InputDialogComp.prototype.onOk = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onOk", DialogListener.listenerType, simpleEvent);
	return true;
};

/**
 * 暴露给用户覆盖的函数,点击取消按钮后调用此函数
 * @private
 */
InputDialogComp.prototype.onCancel = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onCancel", DialogListener.listenerType, simpleEvent);
	return true;
};