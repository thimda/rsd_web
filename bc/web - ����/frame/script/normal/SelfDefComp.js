/**
 * 
 * 自定义控件
 * 
 * @author guoweic
 * @version NC6.0
 * 
 */
SelfDefComp.prototype = new BaseComponent;
SelfDefComp.prototype.componentType = "SELF_DEF_COMP";

/**
 * 自定义控件构造函数
 * @class 自定义控件
 * 
 * @param parent 父对象
 * @param name 名称
 * @param left 
 * @param top 
 * @param width 宽度
 * @param height 高度
 * @param position 位置
 */
function SelfDefComp(parent, name, left, top, width, height, position,
		className) {
	if (arguments.length == 0)
		return;

	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.overflow = "hidden";
	this.className = getString(className, "self_def_div");
	this.visible = true;
	// 包含自定义对象
	this.contentObj = null;
	this.create();
};

/**
 * @private
 */
SelfDefComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = this.height;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.overflow = this.overflow;
	this.Div_gen.className = this.className;
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 设置自定义内容的HTML描述字符串
 */
SelfDefComp.prototype.setInnerHTML = function(html) {
	if (html != null)
		this.Div_gen.innerHTML = html;
};

/**
 * 设置自定义内容的对象
 * 该对象必须实现：
 * 1、.getMainDiv() 方法，返回最外层DIV
 * 2、getSelfCtx() 方法，获取其自定义的Context对象，Context对象必须包括javaClass属性
 * 	例如：	var otherCtx = new Object;
			otherCtx.javaClass = "nc.vo.personvo";
 * 3、setSelfCtx(otherCtx) 方法，设置其自定义的Context对象
 */
SelfDefComp.prototype.setContent = function(obj) {
	if (obj != null) {
		this.Div_gen.appendChild(obj.getMainDiv());
		this.contentObj = obj;
	}
};

/**
 * 获取自身Dom对象
 */
SelfDefComp.prototype.getSelfObj = function() {
	return this.Div_gen;
};

/**
 * 获取包含的自定义对象
 */
SelfDefComp.prototype.getContentObj = function() {
	return this.contentObj;
};

/**
 * 设置可见性
 */
SelfDefComp.prototype.setVisible = function(visible) {
	this.Div_gen.style.visibility = visible == true ? "visible" : "hidden";
	this.visible = visible;
};

/**
 * 创建事件
 * @private
 */
SelfDefComp.prototype.oncreate = function() {
	var createEvent = {
			"obj" : this
		};
	this.doEventFunc("oncreate", SelfDefListener.listenerType, createEvent);
};

/**
 * 包含内容的自定义事件
 * @private
 */
SelfDefComp.prototype.onSelfDefEvent = function (e, triggerId) {
	this.triggerId = triggerId;
	var selfDefEvent = {
			"obj" : this,
			"triggerId" : triggerId,
			"event" : e
		};
	this.doEventFunc("onSelfDefEvent", SelfDefListener.listenerType, selfDefEvent);
};

/**
 * @private
 */
SelfDefComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.SelfDefCompContext";
	context.c = "SelfDefCompContext";
	context.id = this.id;
	context.visible = this.visible;
	if (this.triggerId != null)
		context.triggerId = this.triggerId;
	// 包含内容的附加Context
	if (this.contentObj && this.contentObj.getSelfCtx){
		context.otherCtx = this.contentObj.getSelfCtx();
	}
	return context;
};

/**
 * @private
 */
SelfDefComp.prototype.setContext = function(context) {
	if (context.visible != null && context.visible != this.visible)
		this.setVisible(context.visible);
	// 包含内容的附加Context
	if (context.otherCtx && this.contentObj && this.contentObj.setSelfCtx)
		this.contentObj.setSelfCtx(context.otherCtx);
};




