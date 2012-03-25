/**
 * @fileoverview 前台异常信息监视器.
 * 功能: 
 *	1.显示友好信息
 *	2.显示错误信息
 *	3.显示异常栈信息
 * 
 * @author gd, guoweic
 * @version NC6.0
 * @since NC1.0
 * 
 * 1.修改该组件的单例实现方法 . guoweic <b>modified</b>
 * 2.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */

/**
 * 异常信息监测器构造函数
 * @class 异常信息监测器，为前台的异常信息展现提供了多种形式。<br>
 *        用法: PageUtil中提供了showExceptionDialog(friendMsg, errorMsg,
 *        stackMsg)方法用以显示异常对话框。
 * 
 * @constructor
 * @param friendMsg 异常友好信息
 * @param errorMsg 异常错误信息
 * @param stackMsg 异常栈信息
 */
function ExceptionDialog(friendMsg, errorMsg, stackMsg) {
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

	this.friendMsg = friendMsg;
	this.errorMsg = errorMsg;
	this.stackMsg = stackMsg;
};

/**
 * 显示对话框的静态方法
 */
ExceptionDialog.showDialog = function(friendMsg, errorMsg, stackMsg) {
	// guoweic: modify start 2009-11-10
	//var dialog = new ExceptionDialog(friendMsg, errorMsg, stackMsg);
	if (!window.globalObject.$c_ExceptionDialog)
		window.globalObject.$c_ExceptionDialog = new ExceptionDialog(friendMsg, errorMsg, stackMsg);
	var dialog = window.globalObject.$c_ExceptionDialog;
	// guoweic: modify end
	dialog.show();
	dialog.changeMsg(friendMsg, errorMsg, stackMsg);
	return dialog;
};

/**
 * 隐藏对话框的静态方法
 */
ExceptionDialog.hideDialog = function() {
	try {
		// guoweic: modify start 2009-11-10
		//var dialog = new ExceptionDialog();
		var dialog = window.globalObject.$c_ExceptionDialog;
		// guoweic: modify end
		dialog.hide();
	} catch (error) {
	}
};

/**
 * 改变显示信息
 */
ExceptionDialog.prototype.changeMsg = function(friendMsg, errorMsg, stackMsg) {
	this.tabItem_friend.divContent.firstChild.innerHTML = friendMsg;
	this.tabItem_error.divContent.firstChild.innerHTML = errorMsg;
	this.tabItem_stack.divContent.firstChild.innerHTML = stackMsg;
};

/**
 * @private
 */
ExceptionDialog.prototype.show = function() {

	if (this.modaldialog == null) {
		this.modaldialog = new ModalDialogComp("debugMonitor",
				trans("ml_exceptiondialog"), "", "", 400, 280, "");
		this.modaldialog.create();
		this.modaldialog.contentDiv.style.overflow = "hidden";		

		// 得到内容面板
		this.content = this.modaldialog.getContentPane();
		this.tab = new TabComp(this.content, "exceptionInfo", 0, 0, "100%",
				"100%");
		this.tabItem_friend = this.tab.createItem("friendInfo",
				trans("ml_friendmsg"));
		this.tabItem_error = this.tab.createItem("errorInfo",
				trans("ml_errormsg"));
		this.tabItem_stack = this.tab.createItem("stackInfo",
				trans("ml_exceptionstack"));

		this.createFriendInfoContent();
		this.createErrorInfoContent();
		this.createStackInfoContent();
	}

	this.modaldialog.show();
	this.tab.activeTab(0);
};

/**
 * @private
 */
ExceptionDialog.prototype.hide = function() {
	this.modaldialog.hide();
};

/**
 * @private
 */
ExceptionDialog.prototype.createFriendInfoContent = function() {
	var reportDiv = $ce("div");
	this.tabItem_friend.divContent.appendChild(reportDiv);
	reportDiv.style.overflow = "auto";
	reportDiv.style.width = "100%";
	reportDiv.style.height = "100%";
	reportDiv.style.borderTop = "solid gray 1px";
};

/**
 * @private
 */
ExceptionDialog.prototype.createErrorInfoContent = function() {
	var reportDiv = $ce("div");
	this.tabItem_error.divContent.appendChild(reportDiv);
	reportDiv.style.overflow = "auto";
	reportDiv.style.width = "100%";
	reportDiv.style.height = "100%";
	reportDiv.style.borderTop = "solid gray 1px";
};

/**
 * @private
 */
ExceptionDialog.prototype.createStackInfoContent = function() {
	var reportDiv = $ce("div");
	this.tabItem_stack.divContent.appendChild(reportDiv);
	reportDiv.style.overflow = "auto";
	reportDiv.style.width = "100%";
	reportDiv.style.height = "100%";
	reportDiv.style.borderTop = "solid gray 1px";
};
