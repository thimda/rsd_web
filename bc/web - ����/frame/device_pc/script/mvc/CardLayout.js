/**
 * @fileoverview 卡片型布局管理器
 * 
 * @author guoweic
 * @version NC6.0
 */
CardLayout.prototype.componentType = "CARD";
CardLayout.prototype = new ListenerUtil;

/**
 * 卡片型布局管理器构造函数
 * @class 卡片型布局管理器
 */
function CardLayout(id, cardDiv, index) {
	this.id = id;
	this.cardDivId = cardDiv.getAttribute("id");

	// 事件处理方法集合
	ListenerUtil.call(this, true);

	if (IS_IE && !IS_IE9)
		this.pages = cardDiv.childNodes;
	else {
		this.pages = new Array;
		for (var i = 0; i < cardDiv.childNodes.length;i ++) {
			var node = cardDiv.childNodes[i];
			//TODO 这样处理是否适应全部浏览器？？？
			if (!(node instanceof Text))
				this.pages.push(node);		
		}
	}
	this.currentIndex = 0;
	if(index != null)
		this.currentIndex = index;
	this.setPage();
};

/**
 * 设置显示页面
 */
CardLayout.prototype.setPage = function(index) {
	if (index != null) {
		if (this.currentIndex == index)
			return;
		this.currentIndex = index;
	}
//	if (this.onBeforePageChange(this.currentIndex) == false)
//		return; 
	for (var i = 0; i < this.pages.length; i ++) {
		if (i != this.currentIndex){
			this.pages[i].style.display = "none";
			this.pages[i].style.visibility = "hidden";
		}
	}
	this.pages[this.currentIndex].style.display = "";
	this.pages[this.currentIndex].style.visibility = "";
	var func;
	if ((func = window["$" + this.id + "_item" + this.currentIndex]) != null) {
		this.beforePageInit(this.currentIndex);
		func();
		window["$" + this.id + "_item" + this.currentIndex] = null;
		this.afterPageInit(this.currentIndex);
	}
	// 调用布局初始化方法，以调整Card中包含的其他布局
	layoutInitFunc();
};

/**
 * 页面初始化前事件
 * @private
 */
CardLayout.prototype.beforePageInit = function(index) {
	var cardEvent = {
			"obj" : this,
			"index" : index
		};
	this.doEventFunc("beforePageInit", CardListener.listenerType, cardEvent);
};

/**
 * 页面初始化后事件
 * @private
 */
CardLayout.prototype.afterPageInit = function(index) {
	var cardEvent = {
			"obj" : this,
			"index" : index
		};
	this.doEventFunc("afterPageInit", CardListener.listenerType, cardEvent);
};

/**
 * 获取当前显示页面索引
 */
CardLayout.prototype.getCurrentPageIndex = function(){
	return this.currentIndex;
};

/**
 * 显示下个页面
 */
CardLayout.prototype.nextPage = function() {
	if(this.currentIndex >= this.pages.length - 1)
		this.currentIndex = 0; 
	else
		this.currentIndex++;
	this.setPage();
};

/**
 * 显示最后一页
 */
CardLayout.prototype.lastPage = function() {
	this.setPage(this.pages.length - 1);
};

/**
 * 显示上个页面
 */
CardLayout.prototype.prePage = function() {
	if(this.currentIndex == 0)
		this.currentIndex = this.pages.length - 1;
	else
		this.currentIndex--;
	this.setPage();
};

/**
 * 显示第一页
 */
CardLayout.prototype.firstPage = function() {
	this.setPage(0);
};

/**
 * 显示页面改变前事件
 * @private
 */
CardLayout.prototype.onBeforePageChange = function(index) {
	var cardEvent = {
			"obj" : this,
			"index" : index
		};
	this.doEventFunc("beforePageChange", CardListener.listenerType, cardEvent);
	return true;
};

/**
 * @private
 */
CardLayout.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.CardContext";
	context.c = "CardContext";
	context.currentIndex = this.currentIndex;
	context.id = this.id;
	var ids = [];
	for(var i = 0; i < this.pages.length; i ++){
		var pageid = this.pages[i].getAttribute("id");
		ids[i] = pageid.replace(this.cardDivId + "_" , ""); 
//		ids[i] = "" + i;
	}
	context.pageIds = ids;
	return context;
};

/**
 * @private
 */
CardLayout.prototype.setContext = function(context) {
	var index = context.currentIndex;
	if (index != this.currentIndex) {	
		this.setPage(index);
	}	
};