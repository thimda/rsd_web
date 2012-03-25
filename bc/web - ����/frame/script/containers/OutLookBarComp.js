/**
 * @fileoverview OutLookBarComp控件,提供类似百叶窗式的树行控件
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.0
 * 
 * 1.修改event对象的获取 . guoweic <b>modified</b>
 * 2.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */

OutLookBarComp.prototype = new BaseComponent;
OutLookBarComp.prototype.componentType = "OUTLOOKBAR";

/**
 * OutLookBarComp构造函数
 * @class OutLookBarComp控件，提供类似百叶窗式的树行控件。
 * @constructor OutLookBarComp构造函数
 */
function OutLookBarComp(parent, name, left, top, width, height, position,
		className) {
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");

	// 存放所有item项的数组
	this.items = null;

	// 存放当前打开的是哪个content
	this.showContent = null;
	this.className = getString(className, "outlookbar_div");
	this.create();
};

/**
 * OutLookBarComp主体创建函数
 * @private
 */
OutLookBarComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("div");
	this.Div_gen.id = this.id;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.overflow = "hidden";
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	if (this.height.toString().indexOf("%") == -1)
		this.Div_gen.style.height = this.height + "px";
	else
		this.Div_gen.style.height = this.height;
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.className = this.className;
	this.Div_gen.onresize = function() {
		oThis.adjustSelf();
	};
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 创建每个子项
 */
OutLookBarComp.prototype.addItem = function(itemName, itemCaption, itemSrcImg,
		itemTitle) {
	var item = new OutLookBarItem(this, itemName, itemCaption, itemSrcImg,
			itemTitle);
	return item;
};

/**
 * 激活某一subitem
 * 
 * @param index 要激活项的索引值(注:索引值从0开始)
 */
OutLookBarComp.prototype.activeItem = function(index) {
	if (!this.items)
		return;
	if (index < 0 || index > this.items.length - 1)
		return;

	// 获取要激活的子项
	var subItem = this.items[index];
	// 没有任何一个子项激活
	if (!this.showContent) {
		subItem.content.style.display = "";
		var newHeight = this.Div_gen.offsetHeight - this.items.length
				* subItem.itemDiv.offsetHeight;
		subItem.content.style.height = newHeight + "px";
		// 记录当前显示的内容区是哪个
		this.showContent = subItem.content;
		subItem.setActiveClass(true);
		this.afterActivedItemChangeForInternal(subItem);
	} else {
		if (this.showContent == subItem.content) {
			return;
//			subItem.content.style.display = "none";
//			this.showContent = null;
//			subItem.setActiveClass(false);
		} else if (this.showContent != null
				&& this.showContent != subItem.content) {
			// 首先隐藏掉上次打开的项
			this.showContent.style.display = "none";
			// 保存当前激活的itemContent
			this.showContent = subItem.content;
			subItem.setActiveClass(false);

			subItem.content.style.display = "";
			subItem.setActiveClass(true);
			var newHeight = this.Div_gen.offsetHeight - this.items.length
					* subItem.itemDiv.offsetHeight;
			subItem.content.style.height = newHeight + "px";
			this.afterActivedItemChangeForInternal(subItem);
		}
	}
};

/**
 * 设置outlookbar高度
 */
OutLookBarComp.prototype.setHeight = function(newHeight) {
	newHeight = parseInt(newHeight);
	if (this.showContent != null) {
		var contentHeight = newHeight - this.items.length
				* this.items[0].itemDiv.offsetHeight;
		if (contentHeight < 20)
			return;
		this.showContent.style.height = (newHeight - this.items.length
				* this.items[0].itemDiv.offsetHeight) + "px";
	}
	this.height = newHeight;
	this.Div_gen.style.height = this.height + "px";
};

/**
 * 得到所有的子项
 */
OutLookBarComp.prototype.getAllItems = function() {
	return this.items;
};

/**
 * 自动调整大小
 * @private
 */
OutLookBarComp.prototype.adjustSelf = function() {
	if (!this.showContent)
		return;
	var newHeight = this.Div_gen.offsetHeight - this.items.length
			* this.items[0].itemDiv.offsetHeight;
	this.showContent.style.height = newHeight + "px";
};

/**
 * 供内部使用的方法
 * @private
 */
OutLookBarComp.prototype.afterActivedItemChangeForInternal = function(currItem) {
	// alert(currItem.caption)
};

/**
 * item子项构造函数
 * @class item子项
 */
function OutLookBarItem(outlook, name, caption, srcImg1, title) {
	if (!outlook.items)
		outlook.items = new Array();
	outlook.items.push(this);

	var oThis = this;
	this.parentOwner = outlook;
	this.name = name;
	this.caption = caption;
	this.title = title;
	this.srcImg = srcImg1;

	this.itemBarDiv = $ce('DIV');
	this.itemBarDiv.className = "itembardiv";
	outlook.Div_gen.appendChild(this.itemBarDiv);

	this.leftBorderDiv = $ce('DIV');
	this.rightBorderDiv = $ce('DIV');
	// item子项的主体div
	this.itemDiv = $ce("DIV");
	this.itemDiv.title = this.title;
	this.itemDiv.id = name;

	this.itemBarDiv.appendChild(this.itemDiv);
	//去掉左右div
//	this.itemBarDiv.appendChild(this.leftBorderDiv);
//	this.itemBarDiv.appendChild(this.rightBorderDiv);
	
	// 显示箭头图片
	this.divArrowImg = $ce("DIV");
	this.divArrowImg.className = "item_arrow_div";
	this.arrowImg = $ce("IMG");
	this.arrowImg.className = "item_arrow_img";
	this.divArrowImg.appendChild(this.arrowImg);
	this.itemDiv.appendChild(this.divArrowImg);
	
	this.setActiveClass(false);
	//宽度为100%
//	this.itemDiv.style.width = this.itemDiv.parentNode.offsetWidth - this.leftBorderDiv.offsetWidth - this.rightBorderDiv.offsetWidth + "px";
	
	if (this.srcImg) {
		// item图片
		this.divImg = $ce("DIV");
		this.itemDiv.appendChild(this.divImg);
		this.divImg.className = "itemimg";
		this.img = $ce("IMG");
		if (this.srcImg == "default")  // 为"default"时显示默认图片
			this.img.src = window.themePath + "/images/outlookbar/subitem.gif";
		else
			this.img.src = this.srcImg;
		this.img.style.width = "12px";
		this.img.style.height = "13px";
		this.divImg.appendChild(this.img);
	}
	// item文字div
	this.divCaption = $ce("DIV");
	this.divCaption.className = "itemtitle";
	
	this.itemDiv.appendChild(this.divCaption);
	this.divCaption.appendChild(document.createTextNode(this.caption));
	
	// 撑出空白区域
	this.divBlank = $ce("DIV");
	this.divBlank.className = "itemblank";
	this.itemDiv.appendChild(this.divBlank);
	
	// item的内容区,每个item绑定一个自己的内容区
	this.content = $ce("div");
	this.content.style.overflow = "auto";
	this.content.style.width = "100%";
	this.content.className = "outlooktreeitem_content_div";
	outlook.Div_gen.appendChild(this.content);
	this.content.style.display = "none";
	// 保存和此content相关的item
	this.content.relateItem = this;
	this.itemDiv.owner = this;
	var newHeight = outlook.Div_gen.offsetHeight - outlook.items.length
			* oThis.itemDiv.offsetHeight;
	oThis.content.style.height = newHeight + "px";
	this.itemDiv.objType = "outLookItem"; //编辑态标识，不删除事件
	// item子项点击事件
	this.itemDiv.onclick = function(e) {
//		e = EventUtil.getEvent();

		// 没有项处于打开状态
		if (!oThis.parentOwner.showContent) {
			// this.className = "outlooktreeitem_div_showcontent";
			var newHeight = oThis.parentOwner.Div_gen.offsetHeight - oThis.parentOwner.items.length
					* (this.offsetHeight + 2);
			oThis.content.style.height = newHeight + "px";
			oThis.content.style.display = "";
			oThis.setActiveClass(true);
			// 记录当前显示的内容区是哪个
			oThis.parentOwner.showContent = oThis.content;
			oThis.parentOwner.afterActivedItemChangeForInternal(oThis);
		} else {
			if (oThis.parentOwner.showContent == oThis.content) {
				// 如果点击的是最后一个item,则不隐藏
				if (oThis.parentOwner.items[oThis.parentOwner.items.length - 1] == oThis) {
					return;
				}
				oThis.content.style.display = "none";
				oThis.setActiveClass(false);
				oThis.parentOwner.showContent = null;
			} else if (oThis.parentOwner.showContent != null
					&& oThis.parentOwner.showContent != oThis.content) {
				// 首先隐藏掉上次打开的项
				oThis.parentOwner.showContent.style.display = "none";
				var relateItem = oThis.parentOwner.showContent.relateItem;
				relateItem.setActiveClass(false);
				// 保存当前激活的itemContent
				oThis.parentOwner.showContent = oThis.content;

				var newHeight = oThis.parentOwner.Div_gen.offsetHeight
						- oThis.parentOwner.items.length * (oThis.itemDiv.offsetHeight + 2);
				oThis.content.style.height = newHeight + "px";
				oThis.content.style.display = "";
				oThis.setActiveClass(true);
				oThis.parentOwner.afterActivedItemChangeForInternal(oThis);
			}
		}
		if(window.editMode == true){
			layoutInitFunc();
		}

	};

};

/**
 * 设置外观显示
 * @param isActive 是否是激活项
 */
OutLookBarItem.prototype.setActiveClass = function(isActive) {
	if (isActive) {
		this.itemDiv.className = "outlooktreeitem_div_showcontent";
		this.leftBorderDiv.className = "leftborderdiv_showcontent";
		this.rightBorderDiv.className = "rightborderdiv_showcontent";
		this.arrowImg.src = window.themePath + "/images/outlookbar/outlook_arrow_on.png";
	} else {
		this.itemDiv.className = "outlooktreeitem_div";
		this.leftBorderDiv.className = "leftborderdiv";
		this.rightBorderDiv.className = "rightborderdiv";
		this.arrowImg.src = window.themePath + "/images/outlookbar/outlook_arrow.png";
	}
};

/**
 * 增加子项，返回子项的显示对象
 */
OutLookBarItem.prototype.add = function(obj) {
	this.content.appendChild(obj);
};

/**
 * 获取对象信息
 * @private
 */
OutLookBarComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.OutlookbarComp";
	context.c = "OutlookbarContext";
	context.id = this.id;
	context.currentIndex = this.currentIndex;
	
	return context;
};

/**
 * 设置对象信息
 * @private
 */
OutLookBarComp.prototype.setContext = function(context) {
	var index = context.currentIndex;
	if(index != null)
		this.activeItem(index);
};
