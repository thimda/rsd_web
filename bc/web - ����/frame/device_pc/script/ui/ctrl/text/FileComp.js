FileComp.prototype = new BaseComponent;
FileComp.prototype.componentType = "FILECOMP";
/**
 * 文件上传控件
 */
function FileComp(parent, name, left, top, width, height, position, attrArr, className) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.create();
};

/**
 * 创建整体显示对象
 * 
 * @private
 */
FileComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = document.createElement("DIV");
	this.Div_gen.id = this.id;
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
};

/**
 * @private
 */
FileComp.prototype.manageSelf = function() {
};

/**
 * 设值
 */
FileComp.prototype.setValue = function(value) {
	if (this.value == value)
		return;
	this.value = value;
	this.repaintFileList();
};

FileComp.prototype.repaintFileList = function() {
	this.cleanElement();
	if (this.value == null || this.value == "")
		return;
	else{
		var files = getFileService().execute("getFiles", this.value);
		for (var i = 0; i < files.length; i ++){
			this.createFileElement(files[i]);
		}
		this.createUploadElement();
	}	
};

FileComp.prototype.cleanElement = function() {
	this.Div_gen.innerHTML = "";
};

FileComp.prototype.createFileElement = function(fileName) {
	var fileDiv = $ce("DIV");
	fileDiv.style[ATTRFLOAT] = "left";
	fileDiv.innerHTML = fileName + "&nbsp;&nbsp;";
	this.Div_gen.appendChild(fileDiv);
};

FileComp.prototype.createUploadElement = function() {
	var oThis = this;
	var uploadDiv = $ce("DIV");
	uploadDiv.style[ATTRFLOAT] = "left";
	var a = $ce("a");
	a.href = "javascript:void(0)";
	a.innerHTML = "上传";
	a.onclick = function(e){
		oThis.uploadFile();
//		oThis.repaintFileList();
		e = EventUtil.getEvent();
		stopAll(e);
	}
	uploadDiv.appendChild(a);
	this.Div_gen.appendChild(uploadDiv);
}

FileComp.prototype.uploadFile = function() {
	var url =window.globalPath +  "/core/file.jsp?pageId=file&billitem=" + this.value + "&method=afterFileUpload";
//	openWindowInCenter(url,"上传文件","450px","450px")
	window._FileComp = this;
	showDialog(url, "上传文件", 450 ,450, "file select", true ,false, null) ;
};

function afterFileUpload(fid){
//	alert(window._FileComp);
	if (window._FileComp != null){
		window._FileComp.repaintFileList();
		window._FileComp = null;
	}
}

/**
 * 获取editor的内容
 */
FileComp.prototype.getValue = function() {
};

/**
 * 设置可编辑状态
 */
FileComp.prototype.setActive = function(active) {
	
	
};

/**
 * 用户自定义blur事件
 * 
 * @private
 */
FileComp.prototype.onblur = function(e) {
	
};

/**
 * 获取对象信息
 * 
 * @private
 */
FileComp.prototype.getContext = function() {
//	var context = new Object;
//// context.javaClass = "nc.uap.lfw.core.comp.ctx.EditorContext";
//	context.c = "EditorContext";
//	context.id = this.id;
//	context.enabled = !this.disabled;
//	context.value = this.getValue();
//	// 处理连接符(+,&)丢失问题
//	context.value = context.value.replace(/\+/g, "%2B");
//	context.value = context.value.replace(/\&/g, "%26");
//	return context;
	return null;
};

/**
 * 设置对象信息
 * 
 * @private
 */
FileComp.prototype.setContext = function(context) {
//	if (context.enabled != null)
//		this.setActive(context.enabled);
//	if (context.readOnly != null)
//		this.setActive(!context.readOnly);
//	if (context.value != null && context.value != this.getValue())
//		this.setValue(context.value);
};
