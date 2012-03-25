FileComp.prototype = new BaseComponent;
FileComp.prototype.componentType = "FILECOMP";
FileComp.FILE_PK = "filepk";
FileComp.deleteImgPath = window.themePath + "/ui/ctrl/text/images/remove.png";
FileComp.FileTypeArray = new Array("doc", "docx", "xls", "xlsx", "ppt", "pdf", "rar", "zip", "jar", "txt", "jpg", "jpeg", "gif", "png", "htm", "html");
/**
 * 文件上传控件
 */
function FileComp(parent, name, left, top, width, height, position, attrArr, className) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.readOnly = false;
	if (attrArr != null) {
		this.readOnly = getBoolean(attrArr.readOnly, false);
		this.sizeLimit = attrArr.sizeLimit;
	}
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
	this.Div_gen.style.overflow = "auto";
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
	if (this.readOnly == true)
		this.setReadOnly(this.readOnly);
	//多值的情况下要只读	
	this.multValue = false;
	//值为filePk的时候要只读
	this.containFilePk = false;
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
	if (this.value != null & this.value != ""){
		if (this.value.indexOf(",") != -1)
			this.multValue = true;
		if (this.value.indexOf(FileComp.FILE_PK) != -1)	
			this.containFilePk = true;
	}
	this.repaintFileList();
};


/**
 * 设置只读状态
 */
FileComp.prototype.setReadOnly = function(readOnly) {
	if (this.readOnly == readOnly) return;
	this.readOnly = readOnly;
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

FileComp.prototype.createFileElement = function(file) {
	if (file == "") return;
	var oThis = this;
	var fileDiv = $ce("DIV");
	fileDiv.style.overflowY = "auto";
//	fileDiv.style[ATTRFLOAT] = "left";
	file = eval("(" + file + ")");
	var fileName = file.name;
	
	var typeImg= $ce("IMG");
	typeImg.style[ATTRFLOAT] = "left";
	typeImg.src = this.getTypeImgSrc(file.type);
	fileDiv.appendChild(typeImg);
	
	var textDiv = $ce("DIV"); 
	textDiv.style[ATTRFLOAT] = "left";
	textDiv.style.marginRight = "10px";
	textDiv.style.marginLeft = "10px";
	
	var a = $ce("a");
	a.href = "javascript:void(0)";
	if (file.filesize){
		var filesize = (file.filesize / 1024).toFixed(2);
		a.innerHTML = fileName + " (" + filesize + "KB)";
	}
	else
		a.innerHTML = fileName;
	a.onclick = function(e){
		oThis.downLoadFile(file.id);
		e = EventUtil.getEvent();
		stopAll(e);
	};
	textDiv.appendChild(a);
	
	fileDiv.appendChild(textDiv);
	this.createDeleteElement(fileDiv,file.id);
	this.Div_gen.appendChild(fileDiv);
};

FileComp.prototype.getTypeImgSrc = function(fileType){
	var imgPath =  window.themePath + "/ui/ctrl/text/images/filetype/";
	for (var i = 0; i < FileComp.FileTypeArray.length; i++){
		if (fileType.toLowerCase() == FileComp.FileTypeArray[i]){
			return imgPath + FileComp.FileTypeArray[i] + ".gif"; 
		}
	}
	return imgPath + "unknown.gif";
	
};

FileComp.prototype.createUploadElement = function() {
	if (this.readOnly == true || this.multValue == true || this.containFilePk == true)
		return;
	var oThis = this;
	this.uploadDiv = $ce("DIV");
	this.uploadDiv.style[ATTRFLOAT] = "left";
	var a = $ce("a");
	a.href = "javascript:void(0)";
	a.innerHTML = "上传";
	a.onclick = function(e){
		oThis.uploadFile();
		e = EventUtil.getEvent();
		stopAll(e);
	};
	this.uploadDiv.appendChild(a);
	this.Div_gen.appendChild(this.uploadDiv);
};

FileComp.prototype.createDownloadElement = function (parentDiv, fileId) {
	var oThis = this;
	var downLoadDiv = $ce("DIV");
	downLoadDiv.style[ATTRFLOAT] = "left";
	var a = $ce("a");
	a.href = "javascript:void(0)";
	a.innerHTML = "下载&nbsp;&nbsp;";
	a.onclick = function(e){
		oThis.downLoadFile(fileId);
		e = EventUtil.getEvent();
		stopAll(e);
	};
	downLoadDiv.appendChild(a);
	parentDiv.appendChild(downLoadDiv);
};

FileComp.prototype.createDeleteElement = function (parentDiv, fileId) {
	if (this.readOnly == true || this.multValue == true || this.containFilePk == true)
		return;
	var oThis = this;
	var deleteDiv = $ce("DIV");
	deleteDiv.style[ATTRFLOAT] = "left";
	var a = $ce("a");
	a.href = "javascript:void(0)";
	var delImg = $ce("IMG");
	delImg.src = FileComp.deleteImgPath;
	delImg.style[ATTRFLOAT] = "right";
	a.appendChild(delImg);
	a.onclick = function(e){
		oThis.currentFileId = fileId;
		require("confirmdialog", function(){ConfirmDialogComp.showDialog("确定删除文件", FileComp.prototype.deleteFile,null, oThis, null)});
//		oThis.deleteFile(fileId);
		e = EventUtil.getEvent();
		stopAll(e);
	};
	deleteDiv.appendChild(a);
	parentDiv.appendChild(deleteDiv);
};

FileComp.prototype.uploadFile = function() {
	var url =window.globalPath +  "/core/file.jsp?pageId=file&billitem=" + this.value + "&method=afterFileUpload&isalter=false";
	if(this.sizeLimit != null)
		url += "&sizeLimit=" + this.sizeLimit;
	window._FileComp = this;
	showDialog(url, "上传文件", 450 ,450, "file select", true ,false, null) ;
};

FileComp.prototype.downLoadFile = function(fileId) {
	if (window.sys_DownFileFrame == null) {
		var frm = $ce('iframe');
		frm.frameborder = 0;
		frm.vspace = 0;
		frm.hspace = 0;
		frm.style.width = '1px';
		frm.style.heigh = 0;
		window.sys_DownFileFrame = frm;
		document.body.appendChild(window.sys_DownFileFrame);
	}
	var url = getFileService().execute("getDownUrl", fileId);
	window.sys_DownFileFrame.src = url;	
};

FileComp.prototype.deleteFile = function() {
	if (this.currentFileId == null) return;
	var fileId = this.currentFileId;
	getFileService().execute("deleteFile", fileId);
	this.repaintFileList();	
};

function afterFileUpload(fid){
	if (window._FileComp != null){
		window._FileComp.repaintFileList();
		window._FileComp = null;
	}
};

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
	var context = new Object;
//// context.javaClass = "nc.uap.lfw.core.comp.ctx.EditorContext";
	context.c = "FormElementContext";
	context.id = this.id;
	context.editable = !this.readOnly;
	context.sizeLimit = this.sizeLimit;
//	context.enabled = !this.disabled;
//	context.value = this.getValue();
//	// 处理连接符(+,&)丢失问题
//	context.value = context.value.replace(/\+/g, "%2B");
//	context.value = context.value.replace(/\&/g, "%26");
	return context;
//	return null;
};

/**
 * 设置对象信息
 * 
 * @private
 */
FileComp.prototype.setContext = function(context) {
	if (context.editable != null)
		this.setReadOnly(!context.editable);
	if(context.sizeLimit != null)
		this.sizeLimit = context.sizeLimit;
//	if (context.enabled != null)
//		this.setActive(context.enabled);
//	if (context.readOnly != null)
//		this.setActive(!context.readOnly);
//	if (context.value != null && context.value != this.getValue())
//		this.setValue(context.value);
};

/**
 * 设置大小及位置
 * @param width 像素大小
 * @param height 像素大小
 */
FileComp.prototype.setBounds = function(left, top, width, height) {	
	// 改变数据对象的值
	this.left = left;
	this.top = top;
	this.width = getInteger(width, this.Div_gen.offsetWidth);
	this.height = getInteger(height, this.Div_gen.offsetHeight);
	// 改变显示对象的值
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	
	this.Div_gen.style.width = this.width + "px";
	this.Div_gen.style.height = this.height + "px";
	
//	if (!this.rows && (IS_IE6 || IS_IE7)) 
//	this.textArea.style.height = this.height - 5 + "px";
//	this.textArea.style.width = this.width - 5 + "px";
	
};

