/**
 * @fileoverview
 * 
 * @author gd, guoweic
 * @version NC6.0
 * @since NC1.2
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 4.修改该组件的单例实现方法 . guoweic <b>modified</b>
 */
ThreeButtonsDialog.prototype.componentType = "THREEBUTTONSDIALOG";
ThreeButtonsDialog.HEIGHT = 230;
ThreeButtonsDialog.WIDTH = 482;

ThreeButtonsDialog.refImgPath = "/ui/ctrl/dialog/images/tip2.png";

ThreeButtonsDialog.prototype = new ListenerUtil;

/**
 * 确认对话框构造函数
 * @class Confirm对话框类。<br>
 *        Confirm对话框是对ModalDialog的封装，在对话框底部添加了OK，
 *        Cancel按钮。使用者只需要给出ContentPane部分内容信息即可 <br>
 *        <b>组件皮肤设置说明：</b><br>
 *        confirmdialog.css是对ConfirmDialog中显示字体的外观控制，如字体、大小等<br>
 *        modaldialog.css是对ConfirmDialog外观整体的控制，包括标题背景，内容区背景等<br>
 *        <br>
 * 
 * <b>使用js，直接通过new的用法生成ConfirmDialog实例：</b><br>
 * confirmdialog = ThreeButtonsDialog.showDialog(msg);// 用需要显示的字符串代替msg<br>
 * confirmdialog.onOk = confirm; //把点击确认按钮后需要执行的函数名赋给onOk属性<br>
 * confirmdialog.onCancel = noconfirm; //把点击取消需要执行的函数名赋给onCancel属性<br>
 * 
 * @constructor ThreeButtonsDialog的构造函数
 * @param name 对话框名字
 * @param msg 需要显示的信息
 * @param title 对话框标题
 * @param refImg 对话框左部要显示的图片(有默认图片)
 * @param{boolean} another 由于确认对话框生命周期的特殊性,特殊时候需要另一个实例
 */
function ThreeButtonsDialog(name, title, left, top, msg, refImg, another) {
	if (!another) {
		// guoweic: modify start 2009-11-9
		/*
		this.getInstance = Singleton;
		if (instance = this.getInstance())
			return instance;
		*/
		// guoweic: modify end
	}
	this.name = name;
	this.title = getString(title, trans("ml_confirmdialog"));
	this.left = left;
	this.top = top;
	this.width = ThreeButtonsDialog.WIDTH;
	this.height = ThreeButtonsDialog.HEIGHT;
	this.msg = getString(msg, "");

	this.refImg = getString(refImg, window.themePath + ThreeButtonsDialog.refImgPath);

	ListenerUtil.call(this, true);
	
	// 调用create方法创建dialog
	this.create();
};

/**
 * ThreeButtonsDialog的创建函数
 * 
 * @private
 */
ThreeButtonsDialog.prototype.create = function() {
	var oThis = this;
	this.modaldialog = new ModalDialogComp(this.name, this.title, this.left,
			this.top, this.width, this.height, "");
	this.modaldialog.contentDiv.style.overflow = "hidden";			

	this.contentDiv = $ce("DIV");
	this.contentDiv.className = "confirm_contentdiv";
	this.modaldialog.getContentPane().appendChild(this.contentDiv);
	this.leftDiv = $ce("DIV");
	this.rightDiv = $ce("DIV");
	this.leftDiv.className = "confirm_leftdiv";
	this.rightDiv.className = "confirm_rightdiv";
	this.contentDiv.appendChild(this.leftDiv);
	this.imgNode = $ce("img");
	this.imgNode.src = this.refImg;
	if(IS_IE6){
		this.imgNode.style.width="32px";
		this.imgNode.style.height="32px";
	}
	
	this.leftDiv.appendChild(this.imgNode);
	this.contentDiv.appendChild(this.rightDiv);
//	this.rightDiv.innerHTML = "<table style='height:100%;width:100%'><tr><td valign='top'></td></tr></table>";
//	this.msgTd = this.rightDiv.firstChild.rows[0].cells[0];
	this.rightDiv.innerHTML = "<div style='margin-top:52px;width:100%;position:relative;overflow-y:auto;'></div>";
	this.msgTd = this.rightDiv.firstChild;
//	if (!IS_IE || IS_IE8) {
	//this.rightDiv.style.width = this.rightDiv.parentNode.offsetWidth - this.leftDiv.offsetWidth - 2 + "px";
//	}
	this.bottomDiv = $ce("DIV");
	this.modaldialog.getContentPane().appendChild(this.bottomDiv);
	this.bottomDiv.className = "confirm_bottomdiv";

	this.okBtDiv = $ce("DIV");
	this.okBtDiv.className = "input_1btdiv";
	this.middleBtDiv = $ce("DIV");
	this.middleBtDiv.className = "input_2btdiv";
	this.cancelBtDiv = $ce("DIV");
	this.cancelBtDiv.className = "input_3btdiv";
//	if (!IS_IE) {
//		this.okBtDiv.style.textAlign = "-moz-right";
//		this.cancelBtDiv.style.textAlign = "-moz-left";
//	}
//	if (IS_IE6) {
//		this.okBtDiv.style.paddingTop = "3px";
//		this.cancelBtDiv.style.paddingTop = "3px";
//	}
	this.bottomDiv.appendChild(this.cancelBtDiv);
	this.bottomDiv.appendChild(this.middleBtDiv);
	this.bottomDiv.appendChild(this.okBtDiv);
	// 生成确定按钮
	this.okBt = new ButtonComp(this.okBtDiv, "okBt", 0, 0, "74", "23",
			trans("ml_ok"), "", '', "relative", "", false, "blue_button_div");
	this.okBt.onclick = function(e) {
		oThis.onOk();
		oThis.hide();
	};
	
	// 生成确定按钮
	this.middleBt = new ButtonComp(this.middleBtDiv, "middleBt", 0, 0, "74", "23",
			"执行", "", '', "relative", "", false, "button_div");
	this.middleBt.onclick = function(e) {
		oThis.onMiddle();
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
	this.contentDiv.style.height = this.modaldialog.getContentPane().offsetHeight - this.bottomDiv.offsetHeight + "px";
};

/**
 * 覆盖父类的close方法实现自己的逻辑处理
 * 
 * @private
 */
ThreeButtonsDialog.prototype.close = function() {
	this.onCancel();
	this.hide();
};

/**
 * 得到要添加内容的面板
 */
ThreeButtonsDialog.prototype.getContentPane = function() {
	return this.panel.getObjHtml();
};

/**
 * 改变ThreeButtonsDialog的显示信息
 * 
 * @param msg 要显示的确认文字
 */
ThreeButtonsDialog.prototype.changeMsg = function(msg) {
	this.msg = msg;
	this.msgTd.innerHTML = this.msg;
	var height = getTextHeight(this.msg, "message_rightdiv");
	if(height > 0){
		this.msgTd.style.marginTop = (52 - (height - 14)/2) + "px";
	}
};

/**
 * 显示对话框
 * 
 * @private
 */
ThreeButtonsDialog.prototype.show = function() {
	this.modaldialog.show();
	this.adjustContentdivWidth();
};

/**
 * 隐藏对话框
 * 
 * @private
 */
ThreeButtonsDialog.prototype.hide = function() {
	this.modaldialog.hide();
};

ThreeButtonsDialog.prototype.adjustContentdivWidth = function(){
	//this.rightDiv.style.width = this.rightDiv.parentNode.offsetWidth - this.leftDiv.offsetWidth - 2 + "px";	
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
 */
ThreeButtonsDialog.showDialog = function(message, okFunc, cancelFunc, middleFunc, btntexts, obj1,
		obj2, obj3, zIndex, another, title) {
	// guoweic: modify start 2009-11-10
	if (!another) {
		if (!window.globalObject.$c_3ButtonsDialog)
			window.globalObject.$c_3ButtonsDialog = new ThreeButtonsDialog("confirmDialog",
					title, "", "", message, "");
		var dialog = window.globalObject.$c_3ButtonsDialog;
	} 
	else {
		var dialog = new ThreeButtonsDialog("confirmDialog",
				title, "", "", message, "");
	}
	// guoweic: modify end
	dialog.changeMsg(message);
	if (zIndex)
		dialog.modaldialog.Div_gen.style.zIndex = zIndex;

	if (okFunc) {
		dialog.onOk = function() {
			if (obj1)
				okFunc.call(obj1);
			else
				okFunc();
		};
	}

	if (cancelFunc) {
		dialog.onCancel = function() {
			if (obj2)
				cancelFunc.call(obj2);
			else
				cancelFunc();
		};
	}
	
	if (middleFunc) {
		dialog.onMiddle = function() {
			if (obj3)
				middleFunc.call(obj3);
			else
				middleFunc();
		};
	}
	
	if(btntexts != null){
		if(btntexts[0] != null){
			dialog.okBt.changeText(btntexts[0]);
		}
		if(btntexts[1] != null){
			dialog.middleBt.changeText(btntexts[1]);
		}
		if(btntexts[2] != null){
			dialog.cancelBt.changeText(btntexts[2]);
		}
	}

	dialog.show();
	return dialog;
};

/**
 * 静态方法,隐藏对话框
 */
ThreeButtonsDialog.hideDialog = function() {
	// guoweic: modify start 2009-11-10
	//var dialog = new ThreeButtonsDialog();
	var dialog = window.globalObject.$c_3ButtonsDialog;
	// guoweic: modify end
	dialog.hide();
};

/**
 * 暴露给用户覆盖的函数,点击确认按钮后调用此函数
 * @private
 */
ThreeButtonsDialog.prototype.onOk = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onOk", DialogListener.listenerType, simpleEvent);
	return true;
};

/**
 * 暴露给用户覆盖的函数,点击确认按钮后调用此函数
 * @private
 */
ThreeButtonsDialog.prototype.onMiddle = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onMiddle", DialogListener.listenerType, simpleEvent);
	return true;
};

/**
 * 暴露给用户覆盖的函数,点击取消按钮后调用此函数
 * @private
 */
ThreeButtonsDialog.prototype.onCancel = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onCancel", DialogListener.listenerType, simpleEvent);
	return true;
};