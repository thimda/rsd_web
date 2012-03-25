/**
 * @fileoverview ProgressDialogComp组件。 通过显示正在进行的动作描述告知用户正在进行的事情.
 * @auther gd, guoweic
 * @version NC6.0
 * @since NC1.2
 * 
 * 1.修改该组件的单例实现方法 . guoweic <b>modified</b>
 * 
 */
ProgressDialogComp.prototype.componentType = "PROGRESSDIALOG";
ProgressDialogComp.HEIGHT = 180;
ProgressDialogComp.WIDTH = 300;

/**
 * 进度对话框构造函数
 * @class 进度对话框类，通过输入指定字符串提供此对话框。<br>
 *        <b>组件皮肤设置说明：</b><br>
 *        modaldialog.css对对话框中显示字体的外观控制，如字体、大小等。<br>
 *        <br>
 * 
 * <b>使用js，直接通过new的用法生成WarningDialog实例：</b><br>
 * progressdialog = ProgressDialogComp.showDialog(msg); // 用需要显示的字符串代替msg
 * @constructor ProgressDialogComp构造函数
 * @author gd
 * @version 1.2
 * 
 * ProgressDialogComp构造函数
 * @param name 控件的名称(也就是id)
 * @param title 对话框标题
 * @param left  控件左部x坐标
 * @param top 控件顶部y坐标
 * @param msg 显示的进度提示信息,用户输入
 * @param refImg 进度提示对话框左侧的显示图片的url
 */
function ProgressDialogComp(name, title, left, top, msg, refImg) {
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
	this.width = ProgressDialogComp.WIDTH;
	this.height = ProgressDialogComp.HEIGHT;
	this.msg = getString(msg, "");

	this.refImg = getString(refImg, window.themePath
			+ "/images/dialog/progress_dialog.gif");
	this.create(); // 调用create方法创建dialog
};

/**
 * ProgressDialogComp的创建函数
 * 
 * @private
 */
ProgressDialogComp.prototype.create = function() {
	var oThis = this;
	this.modaldialog = new ModalDialogComp(this.name, this.title, this.left,
			this.top, this.width, this.height, "");
	this.modaldialog.closeBt.style.visibility = "hidden";
	this.modaldialog.contentDiv.style.overflow = "hidden";

	this.contentDiv = $ce("DIV");
	this.contentDiv.className = "progress_contentdiv";
	this.modaldialog.getContentPane().appendChild(this.contentDiv);

	this.leftDiv = $ce("DIV");
	this.rightDiv = $ce("DIV");
	this.leftDiv.className = "progress_leftdiv";
	this.rightDiv.className = "progress_rightdiv";
	this.rightDiv.innerHTML = "<table style='height:100%;width:100%'><tr><td valign='top'></td></tr></table>";
	this.msgTd = this.rightDiv.firstChild.rows[0].cells[0];
	this.contentDiv.appendChild(this.leftDiv);
	this.imgNode = $ce("img");
	this.imgNode.src = this.refImg;
	this.leftDiv.appendChild(this.imgNode);
	this.contentDiv.appendChild(this.rightDiv);
	if (!IS_IE || IS_IE8) {
		this.rightDiv.style.width = this.rightDiv.parentNode.offsetWidth - this.leftDiv.offsetWidth + "px";
	}
};

/**
 * 显示对话框
 * 
 * @private
 */
ProgressDialogComp.prototype.show = function() {
	this.modaldialog.show();
};

/**
 * 隐藏对话框
 * 
 * @private
 */
ProgressDialogComp.prototype.hide = function() {
	this.modaldialog.hide();
};

/**
 * 改变ErrorDialog的显示信息
 * 
 * @param msg 要显示的信息
 */
ProgressDialogComp.prototype.changeMsg = function(msg) {
	this.msg = msg;
	this.msgTd.innerHTML = "<div style='top:20px;position:relative;'>" + this.msg + "</div>";
};

/**
 * 创建一个对话框的类方法
 * 
 * @param message 要显示的信息
 */
ProgressDialogComp.showDialog = function(message) {
	// guoweic: modify start 2009-11-10
	//var dialog = new ProgressDialogComp("ProgressDialog",
	//		trans("ml_progressdialog"), "", "", message, "");
	if (!window.globalObject.$c_ProgressDialog)
		window.globalObject.$c_ProgressDialog = new ProgressDialogComp("ProgressDialog",
						trans("ml_progressdialog"), "", "", message, "");
	var dialog = window.globalObject.$c_ProgressDialog;
	// guoweic: modify end
	dialog.changeMsg(message);
	dialog.show();
	return dialog;
};

/**
 * 静态方法,隐藏对话框
 */
ProgressDialogComp.hideDialog = function() {
	try {
		// guoweic: modify start 2009-11-10
		//var dialog = new ProgressDialogComp();
		var dialog = window.globalObject.$c_ProgressDialog;
		// guoweic: modify end
		dialog.hide();
	} catch (error) {
		// take it easy
	}
};