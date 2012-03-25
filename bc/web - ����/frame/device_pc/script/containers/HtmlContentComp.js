

HtmlContentComp.prototype = new BaseComponent;
HtmlContentComp.prototype.componentType = "HTMLCONTENT";

function HtmlContentComp(parent, name, left, top, width, height, position,className){
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
//	this.position = position;
//	this.overflow = 'auto';
//	this.className = className;
	this.create();
}

HtmlContentComp.prototype.create = function() {
//	this.Div_gen = $ce("DIV");
//	this.Div_gen.id = this.id;
//	this.Div_gen.style.left = this.left + "px";
//	this.Div_gen.style.top = this.top + "px";
//	this.Div_gen.style.width = this.width;
//	this.Div_gen.style.height = this.height;
//	this.Div_gen.style.position = this.position;
//	this.Div_gen.style.overflow = this.overflow;
//	if(this.className && this.className != null){
//		this.Div_gen.className = this.className;
//	}	
//	if (this.parentOwner)
//		this.placeIn(this.parentOwner);
	this.Div_gen = this.parentOwner;
	
};

/**
 * 设置内容
 */
HtmlContentComp.prototype.setContent = function(html) {
	this.Div_gen.innerHTML = html;
};
/**
 * 获得内容
 * @return {}
 */
HtmlContentComp.prototype.getContent = function() {
	return this.Div_gen.innerHTML;
};
/**
 * 清空内容
 * @return {}
 */

HtmlContentComp.prototype.clearContent = function() {
	return this.Div_gen.innerHTML = '';
};


HtmlContentComp.prototype.addContent = function(obj) {
	this.Div_gen.appendChild(obj);
};

HtmlContentComp.prototype.removeContent = function(obj) {
	this.Div_gen.removeChild(obj);
};

/**
 * @private
 */
HtmlContentComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.PanelContext";
	context.c = "PartCompContext";
	context.id = this.id;
	return context;
};

/**
 * @private
 */
HtmlContentComp.prototype.setContext = function(context) {
	if(context.innerHTML){
		this.setContent(context.innerHTML);
	}
};

