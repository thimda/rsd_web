/**
 * @fileoverview MessageDialogComp组件。
 * @auther gd, guoweic
 * @version NC6.0
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 4.修改该组件的单例实现方法 . guoweic <b>modified</b>
 */

MessageDialogComp.prototype.componentType = "MESSAGEDIALOG";
MessageDialogComp.HEIGHT = 180;
MessageDialogComp.WIDTH = 350;

MessageDialogComp.refImgPath = "/ui/ctrl/dialog/images/message_dialog.gif";

MessageDialogComp.prototype = new ListenerUtil;

/**
 * 信息对话框构造函数
 * @class 信息对话框类，通过输入指定字符串和显示图片提供此对话框。<br>
 *        <br>
 * 
 * <b>组件皮肤设置说明：</b><br>
 * modaldialog.css对对话框中显示字体的外观控制，如字体，大小等。<br>
 * <br>
 * 
 * <b>使用js，直接通过new的用法生成MessageDialog实例：</b><br>
 * messagedialog = MessageDialogComp.showDialog(msg); // 用户需要显示的字符串代替msg
 * @constructor MessageDialogComp构造函数
 * @author gd
 * @version 1.2
 * 
 * @param name 控件的名称(也就是id)
 * @param title 对话框标题
 * @param left 控件左部x坐标
 * @param top 控件顶部y坐标
 * @param msg 显示的信息,用户输入
 * @param refImg 信息对话框左侧的显示图片的url
 */
function MessageDialogComp(name, title, left, top, msg, refImg) {
	// guoweic: modify start 2009-11-9
	/*
	this.getInstance = Singleton;
	var instance = null;

	if (arguments.length == 0) {
		instance = this.getInstance(false);
		return instance;
	}
	if (instance = this.getInstance(true))
		return instance;
	*/
	// guoweic: modify end

	this.name = name;
	this.title = title;
	this.left = left;
	this.top = top;
	this.width = MessageDialogComp.WIDTH;
	this.height = MessageDialogComp.HEIGHT;

	this.tmpDiv = $ce("div");
	this.tmpDiv.innerHTML = getString(msg, ""); // 设置显示的信息

	this.refImg = getString(refImg, window.themePath + MessageDialogComp.refImgPath);

	ListenerUtil.call(this, true);
	
	this.create(); // 调用create方法创建dialog
};

/**
 * @private
 */
MessageDialogComp.prototype.create = function() {
	var oThis = this;
	this.modaldialog = new ModalDialogComp(this.name, this.title, this.left,
			this.top, this.width, this.height, "");
	this.modaldialog.contentDiv.style.overflow = "hidden";		

	this.contentDiv = $ce("DIV");
	this.contentDiv.className = "message_contentdiv";
	this.modaldialog.getContentPane().appendChild(this.contentDiv);
	this.leftDiv = $ce("DIV");
	this.rightDiv = $ce("DIV");
	this.leftDiv.className = "message_leftdiv";
	this.rightDiv.className = "message_rightdiv";
//	this.rightDiv.innerHTML = "<table style='height:100%;width:100%'><tr><td valign='top'></td></tr></table>";
//	this.msgTd = this.rightDiv.firstChild.rows[0].cells[0];
	this.rightDiv.innerHTML = "<div style='height:100%;width:100%;position:relative;overflow-y:auto;'></div>";
	this.msgTd = this.rightDiv.firstChild;
	this.contentDiv.appendChild(this.leftDiv);
	this.imgNode = $ce("img");
	this.imgNode.src = this.refImg;
	
	if(IS_IE6){
		this.imgNode.style.width="32px";
		this.imgNode.style.height="32px";
	}
	this.leftDiv.appendChild(this.imgNode);
	this.contentDiv.appendChild(this.rightDiv);
//	if (!IS_IE || IS_IE8) {
	this.rightDiv.style.width = this.rightDiv.parentNode.offsetWidth - this.leftDiv.offsetWidth + "px";
//	}
	this.bottomDiv = $ce("DIV");
	this.modaldialog.getContentPane().appendChild(this.bottomDiv);
	this.bottomDiv.className = "message_bottomdiv";

	this.okBtDiv = $ce("DIV");
	this.okBtDiv.className = "message_okbtdiv";
	this.cancelBtDiv = $ce("DIV");
	this.cancelBtDiv.className = "message_cancelbtdiv";
	if (!IS_IE) {
		this.okBtDiv.style.textAlign = "-moz-center";
	}
	this.bottomDiv.appendChild(this.okBtDiv);
	// 生成确定按钮
	this.okBt = new ButtonComp(this.okBtDiv, "okBt", 0, 0, "74", "23",
			trans("ml_ok"), "", '', "relative", "", false, "button_div");
	this.okBt.onclick = function(e) {
		e = EventUtil.getEvent();
		oThis.onclick(e);
		oThis.hide();
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	if (IS_IE8)
		this.okBt.Div_gen.style.margin = "0 auto";
	this.contentDiv.style.height = this.modaldialog.getContentPane().offsetHeight - this.bottomDiv.offsetHeight + "px";
};

/**
 * 改变MessageDialog的显示信息
 * 
 * @param msg 要显示的提示信息文字
 */
MessageDialogComp.prototype.changeMsg = function(msg) {
	this.msg = msg;
	this.msgTd.innerHTML = "<div style='top:20px;position:relative;'>" + this.msg + "</div>";
};

/**
 * 显示对话框
 * 
 * @private
 */
MessageDialogComp.prototype.show = function() {
	this.modaldialog.show();
	this.adjustContentdivWidth();
//	MessageDialogComp.CurrentDialog = this;
//	setTimeout("MessageDialogComp.hideCurrentDialog()",3000);
};

//MessageDialogComp.hideCurrentDialog = function(){
//	if (MessageDialogComp.CurrentDialog != null){
//		MessageDialogComp.CurrentDialog.okBt.onclick();
//		MessageDialogComp.CurrentDialog == null;
//	}
//};


MessageDialogComp.prototype.adjustContentdivWidth = function(){
	this.rightDiv.style.width = this.rightDiv.parentNode.offsetWidth - this.leftDiv.offsetWidth + "px";	
};


/**
 * 隐藏对话框
 * 
 * @private
 */
MessageDialogComp.prototype.hide = function() {
	this.modaldialog.hide();
};

/**
 * 创建一个对话框的类方法
 * 
 * @param message 要显示的提示信息文字
 */
MessageDialogComp.showDialog = function(message, title) {
	// guoweic: modify start 2009-11-10
	//var dialog = new MessageDialogComp("MessageDialog",
	//		trans("ml_messagedialog"), "", "", message, "");
	if (!window.globalObject.$c_MessageDialog)
		window.globalObject.$c_MessageDialog = new MessageDialogComp("MessageDialog",
						trans("ml_messagedialog"), "", "", message, "");
	var dialog = window.globalObject.$c_MessageDialog;
	// guoweic: modify end
	dialog.changeMsg(message);
	if(title != null)
		dialog.setTitle(title);
	dialog.show();
	return dialog;
};

/**
 * 静态方法,隐藏对话框
 */
MessageDialogComp.hideDialog = function() {
	try {
		// guoweic: modify start 2009-11-10
		//var dialog = new MessageDialogComp();
		var dialog = window.globalObject.$c_MessageDialog;
		// guoweic: modify end
		dialog.hide();
	} catch (error) {
	}
};

/**
 * 暴露给用户覆盖的方法,点击确定按钮后调用此方法
 * @private
 */
MessageDialogComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};