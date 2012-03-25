function chart_function_createtab(src,id){
	
	var proxy = new ServerProxy(null,null,false); //代理类
	proxy.addParam('clc', 'a.TestController' );//控制类
	proxy.addParam('m_n', 'onPopViewByHyperLink');//方法名
	proxy.addParam('src',src);//参数
	proxy.addParam('id',id);//参数	
	proxy.execute();
}
function chart_function_opentitle(url,chrtid){
	var proxy = new ServerProxy(null,null,false); //代理类
	proxy.addParam('clc', 'a.TestController' );//控制类
	proxy.addParam('m_n', 'onPopViewByHyperLink');//方法名
	proxy.addParam('src',url);//参数
	proxy.addParam('chrtid',chrtid);//参数	
	proxy.execute();
}

/**
 * 创建自定义控件
 * 该对象必须实现：
 * 1.getMainDiv() 方法，返回最外层DIV
 * 2.getSelfCtx() 方法，获取其自定义的Context对象，Context对象必须包括javaClass属性
 * 	例如：	var otherCtx = new Object;
			otherCtx.javaClass = "nc.vo.personvo";
 * 3.setSelfCtx(otherCtx) 方法，设置其自定义的Context对象
 * 
 */
 /**文件选择控件*****************************************************************************************************/
function FileControlComp() {	
	// 自身创建方法
	this.createSelf();
	// 校验失败提示内容
	this.failedHTML = "<div style=\"color:red;font-size:18px;\">*</div>";
};
/**
 * 创建自身
 */
FileControlComp.prototype.createSelf = function() {
	// 主DIV
	this.mainDiv = $ce("DIV");
	this.mainDiv.style.width = "200px";	
	this.titleDiv = $ce("DIV");
	this.titleDiv.setAttribute("style","width:49px;height:23px;float:left;position:relative");
	
	this.titleDiv.title = "  源文件";	
	this.titleDiv.setAttribute("class","label_div");
	this.titleDiv.appendChild(document.createTextNode("源文件"))
	this.mainDiv.appendChild(this.titleDiv);
	
	
	this.textdiv = $ce("DIV");
	this.textdiv.setAttribute("style","width:235px;height:20px;float:right;position:relative");
	this.textdiv.setAttribute("class","text_div");
	this.FileCtr = $ce("input");
	this.FileCtr.style.width = "100%";
	this.FileCtr.style.height = "100%";
	//this.FileCtr.type = "file";
	this.FileCtr.setAttribute("type","file");
	this.FileCtr.setAttribute("accept","image/jpg,image/bmp,image/jpeg");
	this.textdiv.appendChild(this.FileCtr);
	
	this.mainDiv.appendChild(this.textdiv);
}

/**
 * 返回主DIV
 */
FileControlComp.prototype.getMainDiv = function() {
	return this.mainDiv;
};
FileControlComp.prototype.getSelfCtx = function() {
	// 创建Context对象
	var ctx = new Object;
	// 自定义对象的对应Java类，必须包含该属性！
	ctx.javaClass = "";
	ctx.focusItemId = this.focusItemId;
	var filename = "";
	if(this.FileCtr)
		filename = this.FileCtr.value ;
	ctx.fileName = filename;
	return ctx;
};
FileControlComp.prototype.setSelfCtx = function(ctx) {
	if(ctx){
		var filename = ctx.filename;
		if(filename == undefined)
			filename = "";
		//this.FileCtr.value = filename;
		this.FileCtr = $ce("input");
		this.FileCtr.style.width = "100%";
		this.FileCtr.style.height = "100%";
		//this.FileCtr.type = "file";
		this.FileCtr.setAttribute("type","file");
		this.FileCtr.setAttribute("accept","image/jpg,image/bmp,image/jpeg");
		this.FileCtr.setAttribute("value",filename);
		this.textdiv.innerHTML = "";
		this.textdiv.appendChild(this.FileCtr);
	}
}