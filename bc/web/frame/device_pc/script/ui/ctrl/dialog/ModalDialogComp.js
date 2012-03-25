/**
 *	@fileoverview 
 *	模态对话框控件。提供基本的状态对话框容器。同时由此派生一些
 *	常用对话框。比如MessageDialog,WarningDialog等
 *	@author dengjt, gd, guoweic
 * @version NC6.0
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */

/**
 * 模态对话框的基类是BaseComponent
 */
ModalDialogComp.prototype = new BaseComponent;

// 模态对话框的控件类型
ModalDialogComp.prototype.componentType = "MODALDIALOG";

// 模态对话框的高度常量默认值
ModalDialogComp.HEIGHT = 200;
// 模态对话框的宽度常量默认值
ModalDialogComp.WIDTH = 300;
// 页面Dialog最大的z-index;
ModalDialogComp.ZINDEX = 0;

/**
 * 模态对话框构造方法
 * @class 模态对话框类
 * @constructor
 * @param name 控件名称
 * @param title 控件标题
 * @param left 控件左部x坐标
 * @param top 控件顶部y坐标
 * @param width 控件宽度
 * @param height 控件高度
 * @param className css文件的名字
 */
function ModalDialogComp(name, title, left, top, width, height, className) {
	// 如果是子类扩展声明，则不需初始化
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	// document.body为此ModalDialogComp的父组件
	this.parentOwner = document.body;
	this.title = getString(title, "对话框");
	this.width = width;
	//25 对话框头尾图片与原来图片高度差add by chouhl 2012-1-11
//	if (!isNaN(parseInt(height))){
//		height = parseInt(height) + 25;
//	}
	this.height = height;
	this.className = getString(className, "dialog_div");
	this.create();
};

/**
 * 模态对话框创建函数
 * 
 * @private
 */
ModalDialogComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.style.position = "absolute";
	this.Div_gen.style.left = "0px";
	this.Div_gen.style.top = "0px";
	this.Div_gen.style.width = "100%";
	this.Div_gen.style.height = "100%";
	// 设置zIndex,使它位于正常显示元素之上.如果任何动态弹出元素想在dialog上显示出来,必须将此值设置更大
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	this.Div_gen.style.zIndex = BaseComponent.STANDARD_ZINDEX + 100000;
	this.Div_gen.style.zIndex = getZIndex();
	this.Div_gen.style.visibility = "hidden";

	// 将背景设置透明,以覆盖在所有控件上方
	this.Div_gen.style.background = "url(" + window.globalPath + "/frame/themes/images/transparent.gif)";
//	this.Div_gen.style.backgroundColor = "transparent";
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 此控件的回掉函数
 * 
 * @private
 */
ModalDialogComp.prototype.manageSelf = function () {
    var oThis = this;
    this.divdialog = $ce("DIV");
    this.divdialog.className = this.className;
    this.divdialog.style.width = convertWidth(this.width);
    this.divdialog.style.height = convertWidth(this.height);

    var frmstr = '<iframe src="" style="position:absolute; visibility:inherit; top:0px; left:0px; width:100%; height:100%; z-index:-1; border:none;" frameborder="0"></<iframe>';
    this.divdialog.innerHTML = frmstr;

    // 将对话框添加到透明背景上
    this.Div_gen.appendChild(this.divdialog);

    this.layout = $ce("table");
    this.layout.border = "0";
    this.layout.style.height = "100%"; 
    this.layout.style.width = "100%"; 
    this.layout.cellPadding = "0";
    this.layout.cellSpacing = "0"; 
    this.firstRow = this.layout.insertRow(-1);
    this.titleL = this.firstRow.insertCell(-1);
    this.titleC = this.firstRow.insertCell(-1);
    this.titleR = this.firstRow.insertCell(-1);
    
    this.secondRow = this.layout.insertRow(-1);
    this.cL = this.secondRow.insertCell(-1);
    this.cC = this.secondRow.insertCell(-1);
    //70为table的 firstRow 跟 thirdRow的高度之和
    this.cC.height = this.divdialog.offsetHeight - 66 + "px";
//    this.cC.height = "100%";
    this.cR = this.secondRow.insertCell(-1);

    this.thirdRow = this.layout.insertRow(-1);
    this.bL = this.thirdRow.insertCell(-1);
    this.bC = this.thirdRow.insertCell(-1);
    this.bR = this.thirdRow.insertCell(-1);
    this.divdialog.appendChild(this.layout);
    
//    this.thirdRow.height = "22px";
//   	this.firstRow.height = "48px"; 
//   	alert(this.cC.offsetHeight);
//   	this.cC.height = this.divdialog.offsetHeight - 70 + "px";
    
    this.firstRow.className = "dialog_titlebar";
    this.thirdRow.className = "bottomdiv";
    this.titleL.className = "leftheadborderdiv";
    this.titleC.className = "centerheaddiv";
    this.titleR.className = "rightheadborderdiv";


    // 对话框标题
    this.titleDiv = $ce("DIV");
    this.titleDiv.className = "dialog_title";
    this.titleDiv.innerHTML = this.title.replace("\<", "&lt;").replace("\>", "&gt");
    this.titleC.appendChild(this.titleDiv);

    this.closeBt = $ce('DIV');
    this.closeBt.className = "closebt_off";
    this.titleC.appendChild(this.closeBt);
    this.closeBt.onmouseover = function (e) {
        this.className = "closebt_on";
    };
    this.closeBt.onmouseout = function (e) {
        this.className = "closebt_off";
    };
    this.closeBt.onmousedown = function (e) {
        this.className = "closebt_click";
    };

    this.closeBt.onclick = function () {
        oThis.close();
    };


    this.cL.className = "leftbodyborderdiv";
	this.cR.className = "rightbodyborderdiv";
	this.cC.className = "centerbodydiv";
	this.cC.vAlign = "top";

    this.contentDiv = $ce("DIV");
    this.contentDiv.className = "dialog_content";
    this.cC.appendChild(this.contentDiv);

    this.bL.className = "leftbottomborderdiv";
    this.bR.className = "rightbottomborderdiv";
    this.bC.className = "centerbottomdiv";

    var drag = false;
    var oldX = 0;
    var oldY = 0;
    this.firstRow.onmousedown = function (e) {
        e = EventUtil.getEvent();
        drag = true;
        if (IS_IE) {
            oldX = e.clientX;
            oldY = e.clientY;
        } else {
            oldX = e.pageX;
            oldY = e.pageY;
        }
        if (IS_IE)
            this.setCapture();
        else
            window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
        // 删除事件对象（用于清除依赖关系）
        clearEventSimply(e);
    };

    this.firstRow.onmouseup = function (e) {
        drag = false;
        if (IS_IE)
            this.releaseCapture();
        else
            window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
    };

    // guoweic: modify firefox事件反应比较慢，鼠标移动时很容易移出titleBar start 2009-11-9
    if (!IS_IE) {
        window.onmouseup = function () {
            drag = false;
            window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
        };
    }
    // guoweic: modify end

    this.firstRow.onmousemove = function (e) {
        e = EventUtil.getEvent();
        if (drag) {
            if (IS_IE) {
                if (e.clientX <= 0 || e.clientX >= document.body.clientWidth)
                    return false;
                if (e.clientY <= 0 || e.clientY >= document.body.clientHeight)
                    return false;
                var offsetX = e.clientX - oldX;
                var offsetY = e.clientY - oldY;
                oldX = e.clientX;
                oldY = e.clientY;
                oThis.divdialog.style.left = oThis.divdialog.style.pixelLeft + offsetX + "px";
                oThis.divdialog.style.top = oThis.divdialog.style.pixelTop + offsetY + "px";
            } else {
                var offsetX = e.pageX - oldX;
                var offsetY = e.pageY - oldY;
                oldX = e.pageX;
                oldY = e.pageY;
                oThis.divdialog.style.left = oThis.divdialog.offsetLeft + offsetX + "px";
                oThis.divdialog.style.top = oThis.divdialog.offsetTop + offsetY + "px";
            }
        }
        // 删除事件对象（用于清除依赖关系）
        clearEventSimply(e);
    };
};

/**
 * 显示模式对话框
 */
ModalDialogComp.prototype.show = function(refDiv) {
	//if (this.onBeforeShow() == false)
		//return;
	this.Div_gen.style.visibility = "visible";
	this.Div_gen.style.zIndex = getZIndex();
	//this.adjustCenterHeadDivWidth();
	//this.adjustCenterBodyDivWidth();
	//this.adjustCenterBottomDivWidth();
	this.visible = true;
	var sctop = 0;
	var clinetHeight = 0;
	sctop = compFirstScrollTop(this.Div_gen);
	clinetHeight = compFirstScrollClientHeight(this.Div_gen); 
	this.Div_gen.top = sctop;
	
	if(this.divdialog.offsetHeight > document.body.clientHeight)
		document.body.style.height = (this.divdialog.offsetHeight + 10) + "px";
	// 将控件显示在父控件的中间
	positionElementToCenter(this.divdialog, sctop, clinetHeight);
	setTimeout("document.body.style.height = '100%'", 1000);
	window.onresize();
	adjustContainerFramesHeight();
};



/**
 * 重载父类的方法
 * 
 * @param left 新的左部x坐标
 * @param top 新的顶部y坐标
 * @param width 新宽度
 * @param height 新高度
 */
ModalDialogComp.prototype.setBounds = function(left, top, width, height) {
	BaseComponent.prototype.setBounds.call(this, left, top, width, height);
};

/**
 * 隐藏模式对话框
 */
ModalDialogComp.prototype.hide = function() {
	if(this.Div_gen)
		this.Div_gen.style.visibility = "hidden";
	this.visible = false;
};

/**
 * 设置对话框大小
 * 
 * @param width 新宽度
 * @param height 新高度
 */
ModalDialogComp.prototype.setSize = function(width, height) {
	this.width = parseInt(width, 10);
	//modify by chouhl 2012-1-12 新背景头尾图片高度共增加25px
	this.height = parseInt(height, 10) + 25;
	this.divdialog.style.width = this.width + "px";
	this.divdialog.style.height = this.height + "px";
	this.cC.height = this.divdialog.offsetHeight - 70 + "px";
};

/**
 * 得到内容面版
 */
ModalDialogComp.prototype.getContentPane = function() {
	return this.contentDiv;
};

/**
 * 给对话框添加一个组件,覆盖base的add方法
 * 
 * @param objHtml 显示对象
 */
ModalDialogComp.prototype.add = function(objHtml) {
	this.getContentPane().appendChild(objHtml);
};

/**
 * 设置对话框的标题
 * 
 * @param title 标题文字
 */
ModalDialogComp.prototype.setTitle = function(title) {
	this.title = title;
	this.titleDiv.innerHTML = this.title;
};

/**
 * 关闭对话框内部处理函数,子类如果需要特殊处理需要覆盖此方法
 * 
 * @private
 */
ModalDialogComp.prototype.close = function() {
	// 关闭之前调用用户的处理
	if (this.onBeforeClose() == false)
		return;
	this.onClosing();
	this.hide();
	this.onAfterClose();
};

/**
 * 返回false阻止对话框显示
 * @private
 */
ModalDialogComp.prototype.onBeforeShow = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("beforeShow", DialogListener.listenerType, simpleEvent);
};

/**
 * 暴露的方法,在对话框关闭的时候调用此方法
 * @private
 */
ModalDialogComp.prototype.onBeforeClose = function() {
	var simpleEvent = {
			"obj" : this
		};
	return this.doEventFunc("beforeClose", DialogListener.listenerType, simpleEvent);
};

/**
 * 暴露的方法,在对话框关闭之后调用此方法
 * @private
 */
ModalDialogComp.prototype.onAfterClose = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("afterClose", DialogListener.listenerType, simpleEvent);
};

/**
 * 暴露的方法,对话框关闭时调用此方法
 * @private
 */
ModalDialogComp.prototype.onClosing = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onclose", DialogListener.listenerType, simpleEvent);
};

/**
 * 获取对象信息
 * @private
 */
ModalDialogComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ModalDialogComp";
	context.c = "ModalDialogComp";
	
	return context;
};

/**
 * 设置对象信息
 * @private
 */
ModalDialogComp.prototype.setContext = function(context) {
	
};
