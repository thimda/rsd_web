/**
 * @fileoverview WebFrame Editor控件定义文件
 * @author gd, dengjt, guoweic
 * @version NC6.0
 * 
 * 注：一个页面只能有一个EditorComp控件！（因为设置了window.currentEditor）
 */
EditorComp.prototype = new BaseComponent;
EditorComp.prototype.componentType = "EDITOR";
/**
 * 高级文本编辑器构造函数
 * 
 * @class 高级文本编辑器
 * @constructor
 * @param{Array} hideBarIndices 一维数组,指定要隐藏的工具条
 * @param{Array} hideImageIndices 二维数组,指定要隐藏的每行的按钮,形式如[[0,1],[],[2]]
 * @param hideBarIndices
 *            需要隐藏的行
 */
function EditorComp(parent, name, left, top, width, height, position,
		hideBarIndices, hideImageIndices, className) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.className = getString(className, "cms_editor");
//	this.imageNode = null;
//	this.liNode = null;
//	this.hiddenContent = null;
//	this.frame = null;
//	this.hideBarIndices = hideBarIndices;
//	this.hideImageIndices = hideImageIndices;
//	this.charset = "UTF-8";
//	// 当前模式 0 代码,1 design
//	this.mode = 1;
//	this.toolbars = new Array;
//	this.btDesign = null;
//	this.btView = null;
//	this.btHtml = null;
//	this.filterScript = false;
//	// 用来记录当前是否初始化完成。防止iframe多线程操作出问题
//	this.initialized = false;
//	// 当前编辑器是否可以编辑
//	this.disabled = false;
//	this.value = "";
//
//	// 在页面上设置当前编辑器对象
//	window.currentEditor = this;
//	
//	// redo栈，当执行undo时，将undo前的信息保存在栈中
//	this.redoStack = new Array();
	
	this.create();
};

/**
 * 创建整体显示对象
 * 
 * @private
 */
EditorComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = document.createElement("DIV");
	this.Div_gen.id = this.id;
	// guoweic: modify start 2009-11-10
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	if (this.height.toString().indexOf("%") == -1)
		this.Div_gen.style.height = this.height + "px";
	else
		this.Div_gen.style.height = this.height;
	// guoweic: modify end
	this.Div_gen.style.position = this.position;
	this.Div_gen.className = this.className;
	this.Div_gen.style.overflow = "hidden";
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
	this.createFrame();
};

/**
 * 创建编辑器的toolbar,内容区,操作bar
 * 
 * @private
 */
EditorComp.prototype.manageSelf = function() {
	
//	this.textarea = $ce("TEXTAREA");
//	this.textarea.style.width = "100%";
//	this.textarea.style.height = "100%";
//	this.Div_gen.appendChild(this.textarea);
	this.contentId = this.id + "_content";
//	this.textarea.name = this.contentId;
	this.Div_gen.innerHTML = '<textarea name="' + this.contentId + '" style="width:100%;height:30px;"></textarea>';
//	this.Div_gen.innerHTML = '<textarea name="' + this.contentId + '" style="width:100%;height:100%;visibility:hidden;">ddd</textarea>';
//	this.createToolBars();
//	this.initEditor();
//	this.createOperateBars();
};

/**
 * @private
 */
EditorComp.prototype.createFrame = function() {
//	var oThis = this;
	CKEDITOR.basePath = window.baseGlobalPath + "/frame/script/ckeditor/";
	CKEDITOR.replace(this.contentId);
//	KindEditor.ready(
//			function(K) {
//				var editor1 = K.create('textarea[name="' + oThis.contentId + '"]', {allowFileManager : true});
//				oThis.editor = editor1;
//				editor1.afterBlur = function(e){
//					oThis.onblur(e);
//				}
//			}
//		
//		);
};



/**
 * 清除内容
 * 
 * @private
 */
EditorComp.prototype.cleanHtml = function() {
	// guoweic: modify start 2009-11-24
	var win = this.editorWindow;
	if (IS_IE) {
		var fonts = win.document.body.all.tags("FONT");
	} else {
		win = this.frame.contentWindow;
		var fonts = win.document.getElementsByTagName("FONT");
	}
	// guoweic: modify end
	var curr;
	for ( var i = fonts.length - 1; i >= 0; i--) {
		curr = fonts[i];
		if (curr.style.backgroundColor == "#ffffff")
			curr.outerHTML = curr.innerHTML;
	}

};

/**
 * 插入内容
 * 
 * @private
 */
EditorComp.prototype.oblog_InsertSymbol = function(str1) {
		this.editorWindow.focus();
		// TODO guoweic: modify start 2009-11-23
		var oblog_selection;
		if(this.oblog_selection){
			oblog_selection = this.oblog_selection;
		}else{
			oblog_selection = this.oblog_selectRange();
		}
		
		if (IS_IE){
			var oRange = oblog_selection.createRange();
			// EditorComp.currectSelectRange(oRange,EditorComp.$oRange);
			oRange.pasteHTML(str1);
		}
			
		else {
	        var spanElement = document.createElement("span");
	        spanElement.innerHTML = str1;
			EditorComp.insertElement(this.frame.contentWindow, spanElement);
		}
	
	// guoweic: modify end
};

/**
 * 设值
 */
EditorComp.prototype.setValue = function(value) {
	if(!this.editor) return;
	if(value != null){
		this.editor.html(value);
	}
	else{
		this.editor.html("");
	}
};

/**
 * 追加值
 */
EditorComp.prototype.appendValue = function(value) {
	if(value != null){
		if(this.editor)
			this.editor.appendHtml(value);
	}
};

/**
 * 插入值
 */
EditorComp.prototype.insertValue = function(value) {
	if(value != null){
		if(this.editor)
			this.editor.insertHtml(value);
	}
};

/**
 * 获取editor的内容
 */
EditorComp.prototype.getValue = function() {
	if(this.editor.html)
		return this.editor.html();
	return null;
};

/**
 * 设置可编辑状态
 */
EditorComp.prototype.setActive = function(active) {
	
	
};

/**
 * 用户自定义blur事件
 * 
 * @private
 */
EditorComp.prototype.onblur = function(e) {
	var focusEvent = {
			"obj" : this.editor,
			"event" : e
		};
	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
	
};

/**
 * 获取对象信息
 * 
 * @private
 */
EditorComp.prototype.getContext = function() {
	var context = new Object;
// context.javaClass = "nc.uap.lfw.core.comp.ctx.EditorContext";
	context.c = "EditorContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	context.value = this.getValue();
	// 处理连接符(+,&)丢失问题
	context.value = context.value.replace(/\+/g, "%2B");
	context.value = context.value.replace(/\&/g, "%26");
	return context;
};

/**
 * 设置对象信息
 * 
 * @private
 */
EditorComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.readOnly != null)
		this.setActive(!context.readOnly);
	if (context.value != null && context.value != this.getValue())
		this.setValue(context.value);
};
