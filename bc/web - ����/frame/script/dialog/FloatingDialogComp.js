/*******************************************************************************
 * 
 * 浮动对话框。无拖动条。主要应用于Portal中
 * 
 * @author dengjt, gd, guoweic
 * @version NC6.0
 * 
 * 1.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 
 ******************************************************************************/

FloatingDialogComp.prototype = new BaseComponent;
FloatingDialogComp.prototype.componentType = "FLOATINGMODALDIALOG";

// 常量默认值
FloatingDialogComp.HEIGHT = 200;
FloatingDialogComp.WIDTH = 300;

/**
 * FloatingDialogComp构造函数
 * @class 浮动对话框
 * @param name 组件名称
 * @param title 组件标题
 * @param left 组件左部x坐标
 * @param top 组件顶部y坐标
 * @param width 组件宽度
 * @param height 组件高度
 */
function FloatingDialogComp(name, parent, title, left, top, width, height,
		className) {
	// 如果是子类扩展声明，则不需初始化
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	this.base(name, left, top, "", "");

	// document.body为此FloatingDialogComp的父组件
	if (parent)
		this.parentOwner = parent;
	else
		this.parentOwner = document.body;
	this.title = getString(title, "Dialog");

	this.width = getInteger(width, FloatingDialogComp.WIDTH);
	this.height = getInteger(height, FloatingDialogComp.HEIGHT);

	this.oldWidth = this.width;
	this.oldHeight = this.height;
	// 标示当前窗口的状态
	this.isMax = false;
	// 表示是否是第一次显示窗口
	this.firstShow = true;

	this.className = getString(className, "dialog_div");
	this.create();
};

/**
 * FloatingDialogComp对话框创建函数
 * @private
 */
FloatingDialogComp.prototype.create = function() {
	// Div_gen1只是用来显示半透明效果的一个层
	this.Div_gen1 = $ce("DIV");
	this.Div_gen1.style.position = "absolute";
	this.Div_gen1.style.left = "0px";
	this.Div_gen1.style.top = "0px";
	this.Div_gen1.style.width = "100%";
	this.Div_gen1.style.height = "100%";
	this.Div_gen1.style.background = "black";
	this.Div_gen1.style.filter = "alpha(opacity=20)";
	// 设置zIndex,使它位于正常显示元素之上.如果任何动态弹出元素想在dialog上显示出来,必须将此值设置更大
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	this.Div_gen1.style.zIndex = BaseComponent.STANDARD_ZINDEX + 99;
	this.Div_gen1.style.zIndex = getZIndex();
	this.Div_gen1.style.visibility = "hidden";
	document.body.appendChild(this.Div_gen1);

	this.Div_gen = $ce("DIV");
	this.Div_gen.style.position = "absolute";
	this.Div_gen.style.left = "0px";
	this.Div_gen.style.top = "0px";
	this.Div_gen.style.width = "100%";
	this.Div_gen.style.height = "100%";

	// 设置zIndex,使它位于正常显示元素之上.如果任何动态弹出元素想在dialog上显示出来,必须将此值设置更大
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	this.Div_gen.style.zIndex = BaseComponent.STANDARD_ZINDEX + 100;
	this.Div_gen.style.zIndex = getZIndex();
	this.Div_gen.style.visibility = "hidden";

	// 将背景设置透明，以覆盖在所有控件上方
//	this.Div_gen.style.background = "url(" + window.globalPath
//			+ "/frame/themes/images/transparent.gif)";
	this.Div_gen.style.backgroundColor = "transparent";
	this.Div_gen.onclick = function() {
	};

	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 此控件的回掉函数
 * @private
 */
FloatingDialogComp.prototype.manageSelf = function() {
	var oThis = this;
	this.divdialog = $ce("DIV");
	this.divdialog.style.width = this.width + "px";
	this.divdialog.style.height = this.height + "px";
	this.divdialog.className = this.className;
	// 将对话框添加到透明背景上
	this.Div_gen.appendChild(this.divdialog);

	this.titleBar = $ce("DIV");
	this.titleBar.className = "dialog_titlebar";
	this.divdialog.appendChild(this.titleBar);
	this.titleBar.ondblclick = function() {
		// 没有最大化设置为最大化
		if (oThis.isMax == false) {
			oThis.setBounds(0, 0, parseInt(document.body.clientWidth),
					parseInt(document.body.clientHeight));
			oThis.isMax = true;
		} else {
			// 恢复为初始显示模式
			oThis.setBounds(oThis.oldLeft, oThis.oldTop, oThis.oldWidth,
					oThis.oldHeight);
			oThis.isMax = false;
		}
	};

	this.closeButLeft = this.titleBar.offsetWidth - 20;
	attrObj = {
		position :'absolute',
		boolFloatRight :false
	};
	this.closeBt = new ImageComp(this.titleBar, "closeBt", window.themePath
			+ "/images/dialog/close_nm.gif", this.closeButLeft, 2, "", "",
			"Next Month", window.themePath + "/images/dialog/close_over.gif",
			attrObj);
	this.closeBt.onclick = function() {
		oThis.isMax = false;
		oThis.onclose();
	};

	this.titleDiv = $ce("DIV");
	this.titleBar.appendChild(this.titleDiv);
	this.titleDiv.innerHTML = this.title;

	this.contentDiv = $ce("DIV");
	this.contentDiv.className = "dialog_content";
	this.contentDiv.style.overflow = "auto";
	this.contentDiv.marginLeft = "5px";
	this.contentDiv.marginRight = "5px";
	this.contentDiv.style.height = this.divdialog.offsetHeight - 22 + "px";
	this.divdialog.appendChild(this.contentDiv);
};

/**
 * 显示浮动对话框
 */
FloatingDialogComp.prototype.show = function() {
	// 第一次显示
	if (this.firstShow == true) {
		this.Div_gen.style.visibility = "visible";
		this.Div_gen1.style.visibility = "visible";
		this.visible = true;
		// 将控件显示在父控件的中间
		positionElementToCenter(this.divdialog);
		this.oldLeft = this.divdialog.offsetLeft;
		this.oldTop = this.divdialog.offsetTop;
		this.firstShow = false;
	} else {
		this.setBounds(this.oldLeft, this.oldTop, this.oldWidth,
						this.oldHeight);
		this.Div_gen.style.visibility = "visible";
		this.Div_gen1.style.visibility = "visible";
		this.visible = true;
	}
};

/**
 * 隐藏浮动对话框
 */
FloatingDialogComp.prototype.hide = function() {
	this.Div_gen.style.visibility = "hidden";
	this.Div_gen1.style.visibility = "hidden";
	this.visible = false;
};

/**
 * 设置浮动对话框大小
 * 
 * @param width 宽度
 * @param height 高度
 */
FloatingDialogComp.prototype.setSize = function(width, height) {
	this.width = width;
	this.height = height;
	this.divdialog.style.width = width + "px";
	this.divdialog.style.height = height + "px";
};

/**
 * 覆盖父类此方法
 */
FloatingDialogComp.prototype.setBounds = function(left, top, width, height) {
	this.width = width;
	this.height = height;
	this.left = left;
	this.top = top;
	this.divdialog.style.width = width + "px";
	this.divdialog.style.height = height + "px";
	this.divdialog.style.left = left + "px";
	this.divdialog.style.top = top + "px";
	this.contentDiv.style.height = height - 22 + "px";
	this.closeBt.Div_gen.style.left = width - 20 + "px";
};

/**
 * 得到内容面版
 */
FloatingDialogComp.prototype.getContentPane = function() {
	return this.contentDiv;
};

/**
 * 改变浮动对话框的标题
 * 
 * @param title 新的标题
 */
FloatingDialogComp.prototype.changeTitle = function(title) {
	this.title = getString(title, "Dialog");
	this.titleDiv.innerHTML = this.title;
};

/**
 * 覆盖base的add方法
 */
FloatingDialogComp.prototype.add = function(obj) {
	this.getContentPane().appendChild(obj);
};

/**
 * 点击关闭按钮的事件处理
 * @private
 */
FloatingDialogComp.prototype.onclose = function() {
	this.hide();
};
