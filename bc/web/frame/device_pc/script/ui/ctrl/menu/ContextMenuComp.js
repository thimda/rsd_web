/**
 * @fileoverview 右键菜单控件
 * @author gd, dengjt, guoweic
 * @version NC6.0
 * @since NC5.5
 *
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 
 */

ContextMenuComp.prototype = new BaseComponent;
ContextMenuComp.prototype.componentType = "CONTEXTMENU";
ContextMenuComp.SEP_HEIGHT = 10;
ContextMenuComp.ITEM_WIDTH = 120;

/**
 * ContextMenuComp的构造函数
 * @class 
 * 1、item的parentOwner属性表示此item所在的contextMenu<br>
 * 2、item的childMenu属性表示此item的子menu<br>
 * 3、子menu可以通过parentMenu属性访问父menu<br>
 * 
 * @constructor
 * @param posFix 是否为固定显示位置的contextMenu
 */
function ContextMenuComp(name, left, top, posFix) {
	this.base = BaseComponent;
	this.base(name, left, top, "", "");
	this.width = ContextMenuComp.ITEM_WIDTH;
	this.posFix = getBoolean(posFix, false);
	this.visible = false;
	// 标示此menu是否是某一个item的下一级menu
	this.isChildMenu = false;
	// 此菜单的所有的子项
	this.childItems = new Array();
	// 此菜单所有的子菜单
	this.childMenus = new Array();
	// 分隔条集合
	this.seps = new Array();
	// 分隔条索引
	this.sepIndex = 0;
	this.create();
};

/**
 * ContextMenuComp的创建函数
 * @private
 */
ContextMenuComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("DIV");
	this.Div_gen.className = "contextmenu_div";   
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.position = "absolute";
	this.Div_gen.style.display = "none";
	// guoweic: add start 2009-11-25
	// 放在最上层
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	this.Div_gen.style.zIndex = "10001";
	this.Div_gen.style.zIndex = getZIndex();
	// guoweic: add end
	document.body.appendChild(this.Div_gen);
	
	// 注册外部回掉函数
	window.clickHolders.push(this);
};

/** 
 * 销毁控件
 * @private
 */
ContextMenuComp.prototype.destroySelf = function() {
	// 销毁子项
	for (var i = 0, n = this.childItems.length; i < n; i++) {
		var item = this.childItems[i];
		item.destroySelf();
	}
	// 销毁子菜单
	for (var i = 0, n = this.childMenus.length; i < n; i++) {
		var menu = this.childMenus[i];
		menu.destroySelf();
	}
	this.destroy();
};

/**
 * 外部click事件发生时的回调函数,用来隐藏当前显示的空件
 * @private
 */
ContextMenuComp.prototype.outsideClick = function(e) {
	if (e != null && 2 == e.button)  // 鼠标右键
		return;
	if (this.visible)	
		this.hide();	
};

/**
 * 增加zIndex
 * @private
 */
ContextMenuComp.prototype.addZIndex = function(e) {
	this.Div_gen.style.zIndex = getInteger(this.Div_gen.style.zIndex) + 1;	
};

/**
 * 设置菜单的left值
 * @param left 设置left属性
 */
ContextMenuComp.prototype.setPosLeft = function(left) {  
   this.left = getInteger(left, 0);
   this.Div_gen.style.left = this.left + "px";   
};

/**
 * 设置菜单的top值
 */
ContextMenuComp.prototype.setPosTop = function(top) {   
   this.top = getInteger(top, 0);
   this.Div_gen.style.top = this.top + "px";   
};

/**
 * 设置菜单的宽度
 */
ContextMenuComp.prototype.setWidth = function(width) {
	this.width = getString(width, "100%");
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
};

/**
 * 设置菜单的高度
 */
ContextMenuComp.prototype.setHeight = function(height) {
	this.height = height;
	this.Div_gen.style.height = height + "px";
};

/**
 * 菜单子条目宽度扩大时通知此菜单
 * @private
 */
ContextMenuComp.prototype.childItemWidthUpdated = function(width) {
	if (this.width < width) {	
		// 改变父菜单的宽度
		this.width = width;
		this.Div_gen.style.width = this.width + "px";
		// 通知所有子扩大宽度
		for (var i = 0; i < this.childItems.length; i ++)
			this.childItems[i].setItemWidth(width);
		// 通知所有分隔条扩大宽度
		for (var i = 0; i < this.seps.length; i ++)
			this.seps[i].setItemWidth(width);
	}
};

/**
 * 向菜单中增加子项
 * @param isCheckBoxItem 是否是checkboxitem
 * @param isCheckBoxSelected 此item初始是否选中
 */
ContextMenuComp.prototype.addMenu = function(name, caption, tip, refImg, isCheckBoxItem, isCheckBoxSelected, attrObj) {
	//MenuItemComp(name, caption, refImg, childMenu, className, isMenuBarItem, parentHTML, isCheckBoxGroup, isCheckBoxItem, isCheckBoxSelected)
	var oThis = this;
	var item = new MenuItemComp(this.Div_gen, name, caption, tip, refImg, "menuitem_div", false, null, isCheckBoxItem, isCheckBoxSelected, attrObj); 
	// 存储此菜单的所有子项	
	var index = this.childItems.push(item);	  
	
	// 保存此item的父菜单
	item.parentOwner = this;
	item.index = index - 1; 
	this.addItemHtml(item);
	item.preMenuItem = this.lastAddItem;
	this.lastAddItem = item;
	return item;
};

/**
 * 向菜单中增加分割线
 * @param isCheckBoxItem 是否是checkboxitem
 * @param isCheckBoxSelected 此item初始是否选中
 */
ContextMenuComp.prototype.addSep = function(attrObj) {
	var id = this.id + "_sep_" + this.sepIndex;
	var sep = new MenuSepComp(this.Div_gen, name, "menuitem_div", attrObj); 
	// 存储此菜单的所有分隔条	
	this.sepIndex = this.seps.push(sep);	  

	// 保存此item的父菜单
	sep.parentOwner = this;
	sep.index = this.sepIndex - 1; 
	this.addItemHtml(sep);

	return sep;
};

/**
 * 获取子菜单
 */
ContextMenuComp.prototype.getMenu = function(id) {
	for(var i = 0; i < this.childItems.length; i ++){
		if(id == this.childItems[i].name)
			return this.childItems[i];
	}
	return null;
};

/**
 * 向菜单中增加子项的显示对象
 * 在内部方法addMenu()中使用.
 * @private
 */
ContextMenuComp.prototype.addItemHtml = function(item) {
	// 如果此条目最宽,则通知父菜单
	var itemWidth = item.getItemWidth();
	if (itemWidth > this.width)
		this.childItemWidthUpdated(itemWidth);
	// 否则,根据父菜单宽度调整此条目宽度
	else
		item.setItemWidth(this.width);
		
	this.Div_gen.appendChild(item.Div_gen);
};

/**
 * 增加菜单条目的子菜单
 * 在内部addItem()方法使用.
 * @private
 */
ContextMenuComp.prototype.addItemChildMenu = function(childMenu, item) {
	childMenu.isChildMenu = true;
	childMenu.menuParent = this;
	this.childMenus.push(childMenu);
	this.childMenus = this.childMenus.concat(childMenu.childMenus);
	childMenu.posFix = true;		
	document.body.appendChild(childMenu.Div_gen);
	if (childMenu.childMenus != null) {
		for (var i = 0;i < childMenu.childMenus.length; i++)
			document.body.appendChild(childMenu.childMenus[i].Div_gen);		
	}
	if (item != null)
		item.setChildMenu(childMenu);
};

/**
 * 添加行分隔符
 */
ContextMenuComp.prototype.addSeparator = function() {
	var oThis = this;
	var sep = $ce("DIV");
	sep.style.position = "relative";
	sep.style.width = "100%";
	sep.style.height = ContextMenuComp.SEP_HEIGHT + "px";
	sep.innerHTML = "<hr/>";
	sep.style.background = "#fff";
	sep.onmouseover = function(e) {
		e = EventUtil.getEvent();
		if (oThis.nowActiveMenu) {		
			oThis.nowActiveMenu.fermezoneMenu();
			oThis.nowActiveMenu="";
		}		
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	this.add(sep); 
};

/**
 * 隐藏菜单
 */
ContextMenuComp.prototype.hide = function() {
	if (!this.visible)
		return;
	// 隐藏之前调用用户的方法
	if (this.onBeforeClose() == false)
		return;
		
	for (var i = 0, count = this.childMenus.length; i < count; i++) {
		if (this.childMenus[i].visible == true)
			this.childMenus[i].hide();
	}	
	
	// 清空触发项
	this.triggerObjHtml = null;	
	// 如果是子菜单，清空父菜单的当前激活子项
	if (this.isChildMenu)
		this.menuParent.nowActiveMenu = null;
		   
	this.Div_gen.style.display = "none";   
	this.visible = false;
	// 隐藏之后调用用户的方法
	this.onclose();
};

/**
 * 调用此方法显示菜单
 */
ContextMenuComp.prototype.show = function(e) {   
	
	e = EventUtil.getEvent();
	// TODO:保存触发该菜单的显示对象(是否作为参数暴露再定)
	if (arguments != null && arguments.length == 2) {
		this.triggerObjHtml = arguments[1];		
	}	
   
	// 在菜单显示之前调用用户的方法,用户返回false则不显示菜单
	if (this.onBeforeShow() == false) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}
   
	this.visible = true;
	this.Div_gen.style.display = "block";
	// 如果是显示子菜单,记录当前激活的子菜单
	if (this.isChildMenu)
		this.menuParent.nowActiveMenu = this;
   	
	if (!this.posFix)
		positionneSelonEvent(this.Div_gen, e);
	else {
		positionneSelonPosFournie(this.Div_gen, this.top, this.left);
	}
	stopDefault(e);
	// 显示之后调用用户的方法
	this.onshow();
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 将触发菜单显示的控件对象绑定到事件对象上
 * @private
 */
ContextMenuComp.prototype.mouseover = function(e) {
	this.onmouseover(e);
};

/**
 * @private
 */
ContextMenuComp.prototype.mouseout = function(item) {
	this.onmouseout(item);
};

/**
 * 默认子菜单事件处理。
 * 子条目被点击后会掉用这个方法。
 * @private
 */
ContextMenuComp.prototype.click = function(e) {
	var menu = e.triggerItem.parentOwner;
	while (menu.isChildMenu)
		menu = menu.menuParent;
	if (menu.menubar)
		menu.menubar.click(e);
	else
		menu.onclick(e);
	menu.hide();
};

/**
 * 获取选中的项
 */
ContextMenuComp.prototype.getSelectedItems = function() {
	var items = this.childItems;
	var selItems = new Array();
	if (items != null && items.length > 0) {
		for (var i = 0,count = items.length; i < count; i++) {
			if (items[i].checkbox && items[i].checkbox.checked)
				selItems.push(items[i]);
		}
	}
	return selItems;
};

/**
 * 获取当前的显示项
 */
ContextMenuComp.prototype.getVisibleItems = function() {
	var items = this.childItems;
	var visibleItems = new Array();
	if (items != null && items.length > 0) {
		for (var i = 0, count = items.length; i < count; i++) {
			if (items[i].visible == true)
				visibleItems.push(items[i]);
		}
	}
	return visibleItems;
};

/**
 * 此方法暂时为portal页面定制。更加通用的方法在以后补充
 * @private
 */
ContextMenuComp.prototype.hideItems = function(indice) {
	if (indice == null)
		return;
	for (var i = 0, count = indice.length; i < count; i++) {
		var index = indice[i];
		var item = this.childItems[index];
		item.Div_gen.style.display = "none";
		item.visible = false;
	}
};

/**
 * 此方法暂时为portal页面定制,更加通用的方法在以后补充
 * @private
 */
ContextMenuComp.prototype.showItems = function(indice) {
	if (indice == null)
		return;
	for (var i = 0, count = indice.length; i < count; i++) {
		var index = indice[i];
		var item = this.childItems[index];
		item.Div_gen.style.display = "block";
		item.visible = true;
	}
};

/**
 * 响应快捷键（只响应第一个匹配子项的点击事件）
 * @private
 */
ContextMenuComp.prototype.handleHotKey = function(key) {
	if (this.Div_gen.style.display == "none" || this.Div_gen.style.visibility == "hidden")
		return null;
	// 匹配子项快捷键
	var childItems = this.childItems;
	if (childItems.length > 0) {
		for (var i = 0, n = childItems.length; i < n; i++) {
			var obj = childItems[i].handleHotKey(key);
			if (obj != null)
				return childItems[i];
		}
	}
	return null;
};

/**
 * 根据索引获取子项对象
 */
ContextMenuComp.prototype.getItemByIndex = function(index) {
	return this.childItems[index];
};

/**
 * 菜单隐藏之前调用的方法,返回false阻止菜单关闭
 * @private
 */
ContextMenuComp.prototype.onBeforeClose = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("beforeClose", ContextMenuListener.listenerType, simpleEvent);
};

/**
 *	菜单隐藏后调用的方法
 * @private
 */
ContextMenuComp.prototype.onclose = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onclose", ContextMenuListener.listenerType, simpleEvent);
};

/**
 * 菜单显示之前调用的方法,返回false阻止菜单显示
 * @private
 */
ContextMenuComp.prototype.onBeforeShow = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("beforeShow", ContextMenuListener.listenerType, simpleEvent);
};

/**
 * 菜单显示之后调用的方法
 * @private
 */
ContextMenuComp.prototype.onshow = function() {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("onshow", ContextMenuListener.listenerType, simpleEvent);
};

/**
 * 鼠标移出事件
 * @private
 */
ContextMenuComp.prototype.onmouseout = function(item) {
	var menuItemEvent = {
			"obj" : this,
			"item" : item
		};
	this.doEventFunc("onmouseout", ContextMenuListener.listenerType, menuItemEvent);
};

/**
 * 鼠标移入事件
 * @private
 */
ContextMenuComp.prototype.onmouseover = function(e){
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseover", MouseListener.listenerType, mouseEvent);
};

/**
 * 鼠标单击事件
 * @private
 */
ContextMenuComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**************************************************
 *
 * @fileoverview
 * MenuItem控件,是ContextMenuComp和MenuBarComp的子项
 * @author gd, dengjt, guoweic
 * @version guoweic NC6.0
 * @since NC5.0
 *
 ***************************************************/

MenuItemComp.prototype = new BaseComponent;

MenuItemComp.prototype.componentType = "MENUITEM";
MenuItemComp.DEFAULT_CAPTION = "EMPTY_CAPTION";
MenuItemComp.DEFAULT_ITEM = "";
MenuItemComp.ITEM_HEIGHT = 27;
MenuItemComp.IMAGE_WIDTH = 22;

MenuItemComp.rightImgDownPath = window.themePath + "/ui/ctrl/menu/images/dropdown.png";
MenuItemComp.rightImgDownOverPath = window.themePath + "/ui/ctrl/menu/images/dropdown_over.png";
MenuItemComp.rightImgDownSelectedPath = window.themePath + "/ui/ctrl/menu/images/dropdown_selected.png";

MenuItemComp.refImgRightPath = window.themePath + "/ui/ctrl/menu/images/dropright.png";
MenuItemComp.refImgRightOverPath = window.themePath + "/ui/ctrl/menu/images/dropright_over.png";
MenuItemComp.refImgRightSelectedPath = window.themePath + "/ui/ctrl/menu/images/dropright_selected.png";


/**
 * MenuItemComp的构造函数
 * @param tip 鼠标悬停时的提示信息
 * @param childMenu MenuItem子项的子菜单
 * @param isMenuBarItem 如果是menubar中的item,则item不显示左侧的leftDiv,有子menu则显示向下的箭头;
 *					   如果是contextMenu中的item则显示左侧leftDiv,有子显示像右的箭头
 * @param isCheckBoxGroup 如果isMenuBarItem为true,则此参数表示此菜单是否checkbox组,如果是,每个子item的左侧创建checkbox控件
 * @param isCheckBoxSelected{boolean} 如果isMenuBarItem为false,则表示该item项左侧的checkbox初始是否选中
 * @param isCheckBoxItem{boolean} 如果isMenuBarItem为false,则表示该item是否是checkbox项
 * @class
 */
function MenuItemComp(parent, id, caption, tip, refImg, className, isMenuBarItem , isCheckBoxGroup, isCheckBoxItem, isCheckBoxSelected, attrObj) {
	this.base = BaseComponent;
	this.base(id);
	
	var oThis = this;   
	this.caption = getString(caption, MenuItemComp.DEFAULT_ITEM);
	this.refImg = refImg;
	this.refImgOn = null;
	this.refImgDisable = null;
	if (attrObj) {
		if (attrObj.imgIconOn)
			this.refImgOn = attrObj.imgIconOn;
		if (attrObj.imgIconDisable)
			this.refImgDisable = attrObj.imgIconDisable;
	}
	
	this.name = id;
	this.id = id;
	this.height = MenuItemComp.ITEM_HEIGHT;
	this.childMenu = null;
	// 是否禁止此item
	this.disabled = false;
	// 默认不是menubaritem
	this.isMenuBarItem = getBoolean(isMenuBarItem, false);
	// 如果是menubaritem,则此属性表示子菜单项是否是checkbox组
	if (this.isMenuBarItem)
		this.isCheckBoxGroup = getBoolean(isCheckBoxGroup, false);
	// 如果是contextmenu中的item,并且父是checkBoxGroup,则此属性表示默认此项的checkbox是否选中	
	else {
		// 是否是checkbox item
		this.isCheckBoxItem = getBoolean(isCheckBoxItem, false);
		if (this.isCheckBoxItem)
			this.isCheckBoxSelected	= getBoolean(isCheckBoxSelected, false);
	}	
	// 此item的显示属性
	this.visible = true;
	// 如果是menubaritem并且是checkbox组,则此属性记录当前处于选中状态的子菜单
	if (this.isMenuBarItem && this.isCheckBoxGroup)
		this.selectedItem = null;
	this.className = getString(className, "contextmenu_item_div");
	this.parentHTML = parent;
	this.tip = getString(tip, this.caption);
	if(MenuItemComp.SUSPEND_CREATE == null || MenuItemComp.SUSPEND_CREATE == false)
		this.create();
};

/**
 * @private
 */
MenuItemComp.prototype.create = function() {
	var oThis = this;
	// item的整体div
	this.Div_gen = $ce("DIV");
	this.parentHTML.appendChild(this.Div_gen);
	this.Div_gen.className = this.className;
	
	// guoweic: modify 修正IE6中的高度自动加上padding-top问题 start 2009-10-30
	//this.Div_gen.style.overflowX = "hidden";
	this.Div_gen.style.overflow = "hidden";
	// guoweic: modify end
	this.Div_gen.style.position = "relative";
	// 设置Div_gen的宽度和divCaption的宽度
	this.Div_gen.style.width = "auto";
	this.Div_gen.style.height = this.height + "px";
	this.Div_gen.owner = this;
	
	var content = this.createContent();
	if (null != content)
		this.Div_gen.appendChild(content);
	
	this.Div_gen.onclick = function (e) {
		
		e = EventUtil.getEvent();
		e.triggerItem = oThis;
		
		// 显示Menubar第一层按钮的子菜单
		if (oThis.isMenuBarItem == true && oThis.parentOwner.componentType == "MENUBAR" && oThis.childMenu != null) {
			
			// 隐藏不该显示的子菜单
			if (oThis.parentOwner.nowActiveMenu != null) {
				MenuItemComp.hideChildMenus(oThis.parentOwner.nowActiveMenu);
				oThis.parentOwner.nowActiveMenu.hide();
			}
			oThis.showChildMenu(e);
		}
		
		//licza 点击的是非末级菜单,停止事件传播 
		if (oThis.childMenu != null && oThis.childMenu != "") {
			stopAll(e);
			// 删除事件对象（清除依赖关系）
//			clearEvent(e);
			e.triggerItem = null;
			clearEventSimply(e);
			return;
		}

		//TODO:事件应先传到menu再到menubar
		e.triggerObj = oThis.parentOwner.triggerObj;
		if (oThis.onclick(e) != false)	   
			oThis.parentOwner.click(e);
		
		// 删除事件对象（清除依赖关系）
//		clearEvent(e);
		e.triggerItem = null;
		e.triggerObj = null;
		clearEventSimply(e);
	};
	
	//右键菜单事件
	this.Div_gen.oncontextmenu = function(e) {
		e = EventUtil.getEvent();
		oThis.onBeforeShowMenu(e);
		oThis.oncontextmenu(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	
};

/**
 * 创建内容
 * @private
 */
MenuItemComp.prototype.createContent = function() {
	var oThis = this;
	// item中左侧显示img的div(只有contextMenu才加此leftDiv)
	if (this.isMenuBarItem == false) {	
	   	this.leftDiv = $ce("DIV");
		this.Div_gen.appendChild(this.leftDiv);
	 	this.leftDiv.className = "contextmenu_item_left_div_off";
		
		// 创建checkbox控件
		if (this.isCheckBoxItem) {
			this.checkbox = $ce("INPUT");
			this.checkbox.type = "checkbox";
			this.checkbox.className = "checkbox_box";
			this.leftDiv.appendChild(this.checkbox);
			if (this.isCheckBoxSelected) {
				// 声明在默认情况下是否被选中了
				this.checkbox.defaultChecked = true;
				this.checkbox.checked = true;		
			} else {
				this.checkbox.defaultChecked = false;
				this.checkbox.checked = false;   
			}
			
			this.checkbox.onclick = function(e) {
				e = EventUtil.getEvent();
				stopEvent(e);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
			}					
		}
	}
			 
	if (this.refImg) {
		// 只有contextMenu才会给leftDiv中加img
  		if (this.isMenuBarItem == false) {
			this.changeImg(this.refImg);
  		}	
	}
	
	// 显示文字的Div
	this.divCaption = $ce("DIV");		 
	this.Div_gen.appendChild(this.divCaption);
	this.divCaption.className = "contextmenu_item_caption_off";
	//add by chouhl 2012-2-13
	this.divLeft = $ce("DIV");
	this.divCenter = $ce("DIV");
	this.divRight = $ce("DIV");
	this.divLeft.className = "divLeft_off";
	this.divCenter.className = "divCenter_off";
	this.divRight.className = "divRight_off";
	
	this.divCaption.appendChild(this.divLeft);
	this.divCenter.style.paddingLeft = (20 - 6) + "px";
//	if(this.parentHTML.children.length == 1){
//		this.divCaption.appendChild(this.divLeft);
//		this.divCenter.style.paddingLeft = (20 - 6) + "px";
//	}
	this.divCaption.appendChild(this.divCenter);
	this.divCaption.appendChild(this.divRight);
	// 显示文字的Div中的图片部分
	this.captionImgDiv = $ce("DIV");
	this.captionImgDiv.style[ATTRFLOAT] = "left";
	//modify by chouhl 2012-2-13
	if(this.divCenter && this.divCenter != 'undefined'){
		this.divCenter.appendChild(this.captionImgDiv);
	}else{
		this.divCaption.appendChild(this.captionImgDiv);
	}
	
	if (this.refImg && this.isMenuBarItem != false) {  // 是Menubar子项并且有图片
		if (this.refImg) {  // 有图片
			this.divCaption.style.paddingTop = "0px";
			// 显示图片
			this.changeImg(this.refImg);
		}
		// 显示文字的Div
		this.captionTextDiv = $ce("DIV");
		this.captionTextDiv.style[ATTRFLOAT] = "left";
		this.captionTextDiv.style.paddingTop = "2px";
		// 显示文字
		this.changeCaption(this.caption);
		//modify by chouhl 2012-2-13
		if(this.divCenter && this.divCenter != 'undefined'){
			this.divCenter.appendChild(this.captionTextDiv);
		}else{
			this.divCaption.appendChild(this.captionTextDiv);
		}
	}
	else if (this.isMenuBarItem != false){  // 是Menubar子项
		//modify by chouhl 2012-2-13
		if(this.divCenter && this.divCenter != 'undefined'){
			if(this.divCenter.children && this.divCenter.children.length > 0){
				this.divCenter.children[0].innerHTML = this.caption;	
			}else{
				var captionDiv = $ce("DIV");
				captionDiv.className = "captionTextDiv";
				captionDiv.innerHTML = this.caption;
				this.divCenter.appendChild(captionDiv);
			}
		}else{
			this.divCaption.innerHTML = this.caption;
		}
	}
	else if (!IS_IE){
		//modify by chouhl 2012-2-13
		if(this.divCenter && this.divCenter != 'undefined'){
			if(this.divCenter.children && this.divCenter.children.length > 0){
				this.divCenter.children[0].innerHTML = "&nbsp;" + this.caption;	
			}else{
				var captionDiv = $ce("DIV");
				captionDiv.className = "captionTextDiv";
				captionDiv.innerHTML = "&nbsp;" + this.caption;
				this.divCenter.appendChild(captionDiv);
			}
		}else{
			this.divCaption.innerHTML = "&nbsp;" + this.caption;
		}
	}
	else{
		//modify by chouhl 2012-2-13
		if(this.divCenter && this.divCenter != 'undefined'){
			if(this.divCenter.children && this.divCenter.children.length > 0){
				this.divCenter.children[0].innerHTML = "&nbsp;&nbsp;&nbsp;" + this.caption;	
			}else{
				var captionDiv = $ce("DIV");
				captionDiv.className = "captionTextDiv";
				captionDiv.innerHTML = "&nbsp;&nbsp;&nbsp;" + this.caption;
				this.divCenter.appendChild(captionDiv);
			}
		}else{
			this.divCaption.innerHTML = "&nbsp;&nbsp;&nbsp;" + this.caption;
		}
	}
	this.divCaption.title = this.tip;
	
	if (this.childMenu != null && this.childMenu != "") {	
		this.createRightDiv();
	}
	
	// 鼠标移动到item上
	this.Div_gen.onmouseover = function(e) {
		e = EventUtil.getEvent();
		
		e.triggerItem = oThis;
		var parentOwner = oThis.parentOwner;
		e.triggerObj = parentOwner.triggerObj;
		
		oThis.divCaption.className = "contextmenu_item_caption_on";
		//add by chouhl 2012-2-14
		var hasLeft = false;
		var hasRight = false;
		if(oThis.divLeft && this.divLeft != 'undefined'){
			oThis.divLeft.className = 'divLeft_on';
			hasLeft = true;
		}
		if(oThis.divCenter && this.divCenter != 'undefined'){
			oThis.divCenter.className = 'divCenter_on';
		}
		if(oThis.divRight && this.divRight != 'undefined'){
			oThis.divRight.className = 'divRight_on';
			hasRight = true;
		}
		if(oThis.rightImg){
			oThis.rightImg.src = MenuItemComp.rightImgDownSelectedPath;
		}
		if(hasLeft && hasRight){
			
		}else if(hasLeft){
			oThis.sep.className = 'menuitem_seperator_right_on';
		}else if(hasRight){
			if(oThis.preMenuItem != null && oThis.preMenuItem.sep != null)
				oThis.preMenuItem.sep.className = 'menuitem_seperator_left_on';
		}else{
			if(oThis.preMenuItem != null && oThis.preMenuItem.sep != null){
				oThis.preMenuItem.sep.className = 'menuitem_seperator_left_on';
			}
			oThis.sep.className = 'menuitem_seperator_right_on';
		}
		
		if (oThis.isMenuBarItem == false)
			oThis.leftDiv.className = "contextmenu_item_left_div_on";
		
		// 调用该子项的mouseover事件
		oThis.onmouseover(e);
		
		// Menubar的第一层按钮鼠标滑过时不显示子菜单
		if (oThis.isMenuBarItem == true && oThis.parentOwner.componentType == "MENUBAR") {
			// 删除事件对象（清除依赖关系）
//			clearEvent(e);
			e.triggerItem = null;
			e.triggerObj = null;
			clearEventSimply(e);
			return;
		}
			
		// 隐藏不该显示的子菜单
		if (parentOwner.nowActiveMenu != null) {
			MenuItemComp.hideChildMenus(parentOwner.nowActiveMenu);
//			parentOwner.nowActiveMenu.hide();
		}
		
		// 如果有子菜单则显示子菜单
		if (oThis.childMenu != null && oThis.childMenu != "") {	
//			 // 设置rightDiv的css
//	   	 	oThis.rightDiv.className = "right_div_on";
//			if (!oThis.isMenuBarItem) {
//				this.style.backgroundColor = "#e5ebcc";
//			}	
//				
//			// 记录当前激活子菜单
//			parentOwner.nowActiveMenu = oThis.childMenu;
//			oThis.childMenu.setPosLeft(compOffsetLeft(parentOwner.Div_gen, document.body) + parentOwner.Div_gen.offsetWidth - 1);
//			oThis.childMenu.setPosTop(compOffsetTop(this, document.body));
//			oThis.childMenu.show(e);
			
			oThis.showChildMenu(e);
		}
		
   		// 将此item对象传给父组件
		if (parentOwner.mouseover != null)  
			parentOwner.mouseover(e);

		stopEvent(e);	  
		// 删除事件对象（清除依赖关系）
//		clearEvent(e);
		e.triggerItem = null;
		e.triggerObj = null;
		clearEventSimply(e);
	};
	
	// 鼠标离开item
	this.Div_gen.onmouseout = function (e) {
		e = EventUtil.getEvent();
		
		e.triggerItem = oThis;
		if (oThis.parentOwner)
			e.triggerObj = oThis.parentOwner.triggerObj;
			
		oThis.divCaption.className = "contextmenu_item_caption_off";
		//add by chouhl 2012-2-14
		var hasLeft = false;
		var hasRight = false;
		if(oThis.divLeft && this.divLeft != 'undefined'){
			oThis.divLeft.className = 'divLeft_off';
			hasLeft = true;
		}
		if(oThis.divCenter && this.divCenter != 'undefined'){
			oThis.divCenter.className = 'divCenter_off';
		}
		if(oThis.divRight && this.divRight != 'undefined'){
			oThis.divRight.className = 'divRight_off';
			hasRight = true;
		}
		if(oThis.rightImg){
			oThis.rightImg.src = MenuItemComp.rightImgDownPath;
		}
		if(hasLeft && hasRight){
			
		}else if(hasLeft){
			oThis.sep.className = 'menuitem_seperator';
		}else if(hasRight){
			if(oThis.preMenuItem != null && oThis.preMenuItem.sep != null){
				oThis.preMenuItem.sep.className = 'menuitem_seperator';
			}
		}else{
			if(oThis.preMenuItem != null && oThis.preMenuItem.sep != null){
				oThis.preMenuItem.sep.className = 'menuitem_seperator';
			}
			oThis.sep.className = 'menuitem_seperator';
		}
		
		if (oThis.isMenuBarItem == false)
			oThis.leftDiv.className = "contextmenu_item_left_div_off";
	   	
	   	if (oThis.childMenu != null && oThis.childMenu != "") {
	   		// 设置rightDiv的css
			oThis.rightDiv.className = "right_div";
			if (!oThis.isMenuBarItem) {
				this.style.backgroundColor = "#fff";
			}	
	   	}	
	   	
	   	//TODO:设置整个div背景色
		if (oThis.isMenuBarItem)
			this.style.backgroundColor = "transparent";
		
		if (oThis.parentOwner && oThis.parentOwner.mouseout != null)  
			oThis.parentOwner.mouseout(e);
		
		stopEvent(e);
		// 删除事件对象（清除依赖关系）
//		clearEvent(e);
		e.triggerItem = null;
		e.triggerObj = null;
		clearEventSimply(e);
	};
};

/** 
 * 销毁控件
 * @private
 */
MenuItemComp.prototype.destroySelf = function() {
	// 销毁子菜单
	if (this.childMenu)
		this.childMenu.destroySelf();
	this.destroy();
};

/**
 * 显示子菜单
 * @private
 */
MenuItemComp.prototype.showChildMenu = function(e) {
	// 设置rightDiv的css
	this.rightDiv.className = "right_div_on";
	if (!this.isMenuBarItem) {
		this.Div_gen.style.backgroundColor = "#e5ebcc";
	}	
		
	// 记录当前激活子菜单
	this.parentOwner.nowActiveMenu = this.childMenu;
	if (this.isMenuBarItem == true && this.parentOwner.componentType == "MENUBAR") {  // 是Menubar的第一级子菜单
		this.childMenu.setPosLeft(compOffsetLeft(this.Div_gen, document.body));
		this.childMenu.setPosTop(compOffsetTop(this.Div_gen, document.body) + this.Div_gen.offsetHeight + 1);
	} else {
		this.childMenu.setPosLeft(compOffsetLeft(this.parentOwner.Div_gen, document.body) + this.parentOwner.Div_gen.offsetWidth - 1);
		this.childMenu.setPosTop(compOffsetTop(this.Div_gen, document.body));
	}
	this.childMenu.show(e);
};

/**
 * 隐藏菜单当前显示的所有子菜单
 * @private
 */
MenuItemComp.hideChildMenus = function(menu) {
	if (menu.nowActiveMenu != null) {
		MenuItemComp.hideChildMenus(menu.nowActiveMenu);
		menu.nowActiveMenu = null;
	}
	menu.hide();
};

/**
 * 如果此item是menubaritem,并且此菜单项是checkgroup,
 * 则通过此方法设置哪个子菜单item选中
 * @param menuItem 子菜单中要选中的item
 */
MenuItemComp.prototype.setSelectedItem = function(menuItem) {
	if (menuItem == null)
		return;
	if (this.isMenuBarItem && this.isCheckBoxGroup) {
		// 设置选中项
		var items = this.parentOwner.childMenu.childItems;
		if (items != null && items.length > 0) {
			for (var i = 0,count = items.length; i < count; i++) {
				if (items[i].name == menuItem.name)
					items[i].checkbox.checked = true;
				else
					items[i].checkbox.checked = false;					
			}
		}
		// 记录选中项	
		this.selectedItem = menuItem;
	}	
};

/**
 * 获得菜单条目宽度,包括图片宽度
 */
MenuItemComp.prototype.getItemWidth = function() {
	var textWidth = getTextWidth(this.caption, "contextmenu_item_caption_on") + 10;
	// 得到css中设置的padding值
	var padding = getStyleAttribute(this.divCaption, "paddingRight");
	if (padding == null || padding == "")
		padding = 0;
	else
		padding = parseInt(padding,10);
	
	// 是menuBar的item项
	if(this.isMenuBarItem == true) {
		if (this.childMenu)
			return textWidth + 2*padding + MenuItemComp.IMAGE_WIDTH;
		else
			return textWidth + 2*padding + MenuItemComp.IMAGE_WIDTH;
	}
	else if (this.isMenuBarItem == false) { 
		if (this.childMenu)
			return textWidth + 2*padding + 2*MenuItemComp.IMAGE_WIDTH;
		else
			return textWidth + 2*padding + MenuItemComp.IMAGE_WIDTH;
	}	
};

/**
 * 设置item宽度
 */
MenuItemComp.prototype.setItemWidth = function(eleWidth) {
	var width = new String(eleWidth);
	if (width.indexOf("%") != -1)
		width = this.Div_gen.offsetWidth;
	this.width = width - 1;
	if (this.isMenuBarItem == true)
		this.Div_gen.style.width = (width) + "px";
	else
		this.Div_gen.style.width = (width - 2) + "px";
	
	// 改变caption的宽度,auto不会自动变化改变自己宽度
	if (this.isMenuBarItem == true) {
		if (this.leftDiv)
			this.divCaption.style.width = width - MenuItemComp.IMAGE_WIDTH - 2 + "px";
	}
	else {
		if (this.childMenu){
			this.divCaption.style.width = width - MenuItemComp.IMAGE_WIDTH - 10 + "px";
		}
		else
			this.divCaption.style.width = width - MenuItemComp.IMAGE_WIDTH - 2 + "px";
	}
};

/**
 * 设置item子菜单
 */
MenuItemComp.prototype.setChildMenu = function(childMenu) {
	if(isNull(childMenu, false)) {
		throw Error("can't add an empty menu to item " + this.name);
		return;
	}
	
	// 如果已经添加过子菜单，则不需要改此条目。只需要设置上就行了
	if (isNotNull(this.childMenu, false)) {
		this.childMenu = childMenu;
		childMenu.parentMenu = this.parentOwner;		
	} 
	else {
		this.createRightDiv();
		this.childMenu = childMenu;
		childMenu.parentMenu = this.parentOwner;
		if (this.width != null)
			this.setItemWidth(this.width);
	}
};

/**
 * 根据ID获取子菜单
 */
MenuItemComp.prototype.getMenu = function(id) {
	if(this.childMenu != null)
		return this.childMenu.getMenu(id);
};

/**
 * 增加子菜单
 */
MenuItemComp.prototype.addMenu = function(itemName, itemCapiton, itemTip, itemRefImg, isCheckBoxItem, isCheckBoxSelected, attrObj) {
	if (this.childMenu == null || this.childMenu == "") {	
		var div = this.doCreateContainer();
		var cMenu;
		if (div == null) {
			cMenu = new ContextMenuComp("", "", "", true);
		}
		else {
			cMenu = div;
			div.style.position = "absolute";
			div.style.display = "none";
			div.style.border = "1px solid blue";
			div.menus = [];
			div.addMenu = function(itemName, itemCapiton, itemTip, itemRefImg, isCheckBoxItem, isCheckBoxSelected, attrObj) {
				if(this.getItemContainer == null){
					alert("must implement getItemContainer");
					return;
				}
				var itemContainer = this.getItemContainer();
				var item = null;
				if(this.createItem != null){
					MenuItemComp.SUSPEND_CREATE = true;
					item = new MenuItemComp(itemContainer, itemName, itemCapiton, itemTip, itemRefImg, "menuitem_div", false, null, isCheckBoxItem, isCheckBoxSelected, attrObj);
					MenuItemComp.SUSPEND_CREATE = false;
					this.createItem(item);
					item.create();
				}
				else
					item = new MenuItemComp(itemContainer, itemName, itemCapiton, itemTip, itemRefImg, "menuitem_div", false, null, isCheckBoxItem, isCheckBoxSelected, attrObj);
				div.menus.push(item);
				item.parentOwner = this;
				return item;
			};
			
			div.getMenu = function(id) {
				for (var i = 0, n = this.menus.length; i < n; i++) {
					if (this.menus[i].id == id) {
						return this.menus[i];
					}
				}
			};
			
			div.setPosLeft = function(left) {
				this.style.left = left + "px";
			};
			
			div.setPosTop = function(top) {
				this.style.top = top + "px";
			};
			
			div.show = function() {
				this.style.display = "";
			};
			
			div.hide = function() {
				this.style.display = "none";
			};
			
			div.click = function(e) {
				this.hide();
			};
			
			// 注册外部回掉函数
			window.clickHolders.push(div);
			div.outsideClick = function(e) {
				if (e != null && 2 == e.button)  // 鼠标右键
					return;
				this.hide();	
			};

			document.body.appendChild(div);
		}
		cMenu.menubar = this.menubar;
		// 保存是谁触发的此contextMenu对象
		cMenu.triggerObj = this;
		this.setChildMenu(cMenu);
	}
	var item = this.childMenu.addMenu(itemName, itemCapiton, itemTip, itemRefImg, isCheckBoxItem, isCheckBoxSelected, attrObj);
	return item;
};

/**
 * 向菜单中增加分割线
 * @param isCheckBoxItem 是否是checkboxitem
 * @param isCheckBoxSelected 此item初始是否选中
 */
MenuItemComp.prototype.addSep = function(attrObj) {
	var sep = this.childMenu.addSep(attrObj); 
	return sep;
};

/**
 * 获取子项容器（用户自定义）
 * @private
 */
MenuItemComp.prototype.doCreateContainer = function() {
	return this.onContainerCreate();
};

/**
 * 创建子项容器事件，事件中需要返回创建的容器对象
 * 该容器对象必须实现addMenu方法
 * @private
 */
MenuItemComp.prototype.onContainerCreate = function() {
	var simpleEvent = {
			"obj" : this
		};
	var result = this.doEventFunc("onContainerCreate", ContainerListener.listenerType, simpleEvent);
	return result;
};

/**
 * 创建右侧div
 * @private
 */
MenuItemComp.prototype.createRightDiv = function() {
	this.rightDiv = $ce("DIV");
	this.rightDiv.className = "right_div";
	if(this.divCenter && this.divCenter != 'undefined'){
		this.divCenter.appendChild(this.rightDiv);
	}else{
		this.Div_gen.appendChild(this.rightDiv);
	}
	this.rightImg = $ce("IMG");
	if(this.isMenuBarItem == true) {
		this.rightImg.style.width = "11px";
		this.rightImg.style.height = "8px";
		this.rightImg.style.marginTop = "10px";
		this.rightImg.src = MenuItemComp.rightImgDownPath;
		//if (!IS_IE || IS_IE8)
		//	this.rightDiv.style.paddingTop = "3px";
	} 
	//else if(this.isMenuBarItem == false) {
	else {
		this.rightImg.style.width = "11px";
		this.rightImg.style.height = "8px";
		//this.rightImg.style.top = "3px";
		this.rightImg.style.position = "relative";
		this.rightImg.src = MenuItemComp.refImgRightPath;
	}
	
	this.rightDiv.appendChild(this.rightImg);
	
	// 需通知父菜单此条目已经更改宽度
	//this.parentHTML.owner.childItemWidthUpdated(this.getItemWidth());
};

/**
 * 删除子菜单
 */
MenuItemComp.prototype.removeChildMenu = function() {
	if (isNull(this.childMenu, false)) {
		log("the item" + this.name + "'s child menu is already null");
		return;
	}
	
	this.childMenu = "";
	this.imgWidth -= MenuItemComp.IMAGE_WIDTH;
	this.Div_gen.removeChild(this.rightImg);
	this.Div_gen.style.width = this.getItemWidth() + "px";
};

/**
 * 更新按钮状态
 * 
 * @param  operateState  业务状态
 * @param  businessState 操作状态 
 */
MenuItemComp.prototype.updateState = function(operateState, businessState, userState) {
	//是否可见  -1：未设置状态  0:状态为false  1：状态为true
	var operVisibleFlag = -1;
	var busiVisibleFlag = -1;
	var userVisibleFlag = -1;
	// 操作可视状态
	var operVisibleStatusArr = this.operateVisibleStateArray;
	var busiVisibleStatusArr = this.businessVisibleStateArray;
	var userVisibleStatusArr = this.userVisibleStateArray;
	if (operVisibleStatusArr != null && operateState != null) {
		operVisibleFlag = 0;
		for ( var m = 0, count1 = operVisibleStatusArr.length; m < count1; m++) {
			if (operVisibleStatusArr[m] == operateState){
				operVisibleFlag = 1;
				break;
			}
		}
	} 
//	else{
//		/*
//		 * 1.操作状态不处理时，如果处理业务状态，设置operVisibleFlag为true.
//		 * 2.操作状态不处理时，如果业务状态也不处理，设置operFlag为按钮之前的状态.
//		 */
//		if (busiVisibleStatusArr != null && businessState != null)
//			operVisibleFlag = true;
//		else if (this.visible == true)
//			operVisibleFlag = true;
//	}
	// 业务可视状态
	if (busiVisibleStatusArr != null && businessState != null) {
		busiVisibleFlag = 0;
		for ( var m = 0, count1 = busiVisibleStatusArr.length; m < count1; m++) {
			if (busiVisibleStatusArr[m] == businessState){
				busiVisibleFlag = 1;
				break;
			}
		}
	} 
//	else{
//		/*
//		 * 1.业务状态不处理时，如果处理操作状态，设置operFlag为true.
//		 * 2.业务状态不处理时，如果操作状态也不处理，设置operFlag为按钮之前的状态.
//		 */
//		if (operVisibleStatusArr != null && operateState != null)
//			busiVisibleFlag = true;
//		else if (this.visible == true)
//			busiVisibleFlag = true;
//	}
	// 业务可视状态
	if (userVisibleStatusArr != null && userState != null) {
		userVisibleFlag = 0;
		for ( var m = 0, count1 = userVisibleStatusArr.length; m < count1; m++) {
			if (userVisibleStatusArr[m] == userState){
				busiVisibleFlag = 1;
				break;
			}
		}
	} 
	
	if ((operVisibleFlag + busiVisibleFlag + userVisibleFlag) > -3){ //存在状态设置
		if (operVisibleFlag == 0 || busiVisibleFlag == 0 || userVisibleFlag == 0)
			this.hide();
		else
			this.show();
	}	
//	if (operVisibleFlag && busiVisibleFlag && userVisibleFlag)
//		this.show();
//	else
//		this.hide();
		
	//是否只读	
	var operFlag = -1;
	var busiFlag = -1;
	var userFlag = -1;
	
	// 操作状态
	var operStatusArr = this.operateStateArray;
	var busiStatusArr = this.businessStateArray;
	var userStatusArr = this.userStateArray;
	if (operStatusArr != null && operateState != null) {
		operFlag = 0;
		for ( var m = 0, count1 = operStatusArr.length; m < count1; m++) {
			if (operStatusArr[m] == operateState){
				operFlag = 1;
				break;
			}
		}
	} 
//	else{
//		/*
//		 * 1.操作状态不处理时，如果处理业务状态，设置operFlag为true.
//		 * 2.操作状态不处理时，如果业务状态也不处理，设置operFlag为按钮之前的状态.
//		 */
//		if (busiStatusArr != null && businessState != null)
//			operFlag = true;
//		else if (this.disabled == false)
//			operFlag = true;
//	}
	// 业务状态
	if (busiStatusArr != null && businessState != null) {
		busiFlag = 0;
		for ( var m = 0, count1 = busiStatusArr.length; m < count1; m++) {
			if (busiStatusArr[m] == businessState){
				busiFlag = 1;
				break;
			}
		}
	} 
//	else{
//		/*
//		 * 1.业务状态不处理时，如果处理操作状态，设置operFlag为true.
//		 * 2.业务状态不处理时，如果操作状态也不处理，设置operFlag为按钮之前的状态.
//		 */
//		if (operStatusArr != null && operateState != null)
//			busiFlag = true;
//		else if (this.disabled == false)
//			busiFlag = true;
//	}
	// 业务状态
	if (userStatusArr != null && userState != null) {
		userFlag = 0;
		for ( var m = 0, count1 = userStatusArr.length; m < count1; m++) {
			if (userStatusArr[m] == userState){
				userFlag = 1;
				break;
			}
		}
	} 
	if ((operFlag + busiFlag + userFlag) > -3){ //存在状态设置
		if (operFlag == 0 || busiFlag == 0 || userFlag == 0)
			this.setActive(false);
		else
			this.setActive(true);
	}	
//	if (operFlag && busiFlag && userFlag)
//		this.setActive(true);
//	else
//		this.setActive(false);	
};

/**
 * 设置MenuItem控件的激活状态.
 * @param isActive true表示处于激活状态,否则表示禁用状态
 * @private
 */
MenuItemComp.prototype.setActive = function(isActive) {	
	var isActive = getBoolean(isActive, false);
	// 控件处于激活状态变为非激活状态
	if (this.disabled == false && isActive == false) {
		this.mouseoutFunc = this.Div_gen.onmouseout;
		this.mouseoverFunc = this.Div_gen.onmouseover;
		this.clickFunc = this.Div_gen.onclick;
		this.divCaption.className = "contextmenu_item_caption_banned";
		this.Div_gen.onmouseout = function(){};
		this.Div_gen.onmouseover = function(){};
		this.Div_gen.onclick = function(){};
		this.disabled = true;
		if (this.refImg && this.isMenuBarItem != false) {
			if (this.refImgDisable != null && this.refImgDisable != ""){
				//modify by chouhl 2012-2-13
				if(this.divCenter && this.divCenter != 'undefined'){
					this.divCenter.firstChild.firstChild.src = this.refImgDisable;
				}else{
					this.divCaption.firstChild.firstChild.src = this.refImgDisable;
				}
			}
		}
	}
	// 控件处于禁用状态变为激活状态
	else if (this.disabled == true && isActive == true) {	
		this.Div_gen.onmouseout = this.mouseoutFunc;
		this.Div_gen.onmouseover = this.mouseoverFunc;
		this.Div_gen.onclick = this.clickFunc;
		this.divCaption.className = "contextmenu_item_caption_off";
		this.disabled = false;
		if (this.refImg && this.isMenuBarItem != false) {
			//modify by chouhl 2012-2-13
			if(this.divCenter && this.divCenter != 'undefined'){
				this.divCenter.firstChild.firstChild.src = this.refImg;
			}else{
				this.divCaption.firstChild.firstChild.src = this.refImg;
			}
		}
	}
};

/**	
 * 隐藏控件(显示属性是display)
 */
MenuItemComp.prototype.hide = function() {				
	if (this.visible == false)
		return;
	this.Div_gen.style.display = "none";
	if (this.sep)
		this.sep.style.display = "none";
	this.visible = false;
	
	//如果有显示左边，下一个MenuItem显示左边
	if (this.divLeft.style.display == "block" || this.divLeft.style.display == ""){
		this.showNextItemLeftDiv();
	}
	//哪果有显示右边，上一个MenuItem显示右边
	if (this.divRight.style.display = "block" || this.divRight.style.display == ""){
		this.showPreItemRigetDiv();
	}	
};

/**	
 * 显示控件(显示属性是display)
 */
MenuItemComp.prototype.show = function() {					
	if (this.visible == true)
		return;
	this.Div_gen.style.display = "block";
	if (this.sep)
		this.sep.style.display = "block";
	this.visible = true;
	
	//前面有显示出来的item时,隐藏自身左侧div
	if (this.hidePreItemRigetDiv())
		this.divLeft.style.display = "none";
		
	//后面有显示出来的item时,隐藏自身右侧div	
	if (this.hideNextItemLeftDiv()){
		if (this.sep)
			this.sep.style.display = "block";
		this.divRight.style.display = "none";
		this.divCenter.style.paddingRight = "20px";
	}
	//后面没有item时
	else{
		if (this.sep)
			this.sep.style.display = "none";
	}
};

//显示下一个Item的左侧DIV
MenuItemComp.prototype.showNextItemLeftDiv = function(){
	if (this.nextMenuItem == null) return;
	if (this.nextMenuItem.visible == false) this.nextMenuItem.showNextItemLeftDiv();
	this.nextMenuItem.divLeft.style.display = "block";
};

//隐藏下一个Item的左侧DIV
MenuItemComp.prototype.hideNextItemLeftDiv = function(){
	if (this.nextMenuItem == null) return false;
	if (this.nextMenuItem.visible == false) return this.nextMenuItem.hideNextItemLeftDiv();
	this.nextMenuItem.divLeft.style.display = "none";
	return true;
};

//显示上一个Item的右侧DIV
MenuItemComp.prototype.showPreItemRigetDiv = function(){
	if (this.preMenuItem == null) return;
	if (this.preMenuItem.visible == false) this.preMenuItem.showPreItemRigetDiv();
	if (this.preMenuItem.sep)
		this.preMenuItem.sep.style.display = "none";
	this.preMenuItem.divRight.style.display = "block";
	this.preMenuItem.divCenter.style.paddingRight = "0px";
};

//隐藏上一个Item的右侧DIV 隐藏成功，返回true 否则返回false
MenuItemComp.prototype.hidePreItemRigetDiv = function(){
	if (this.preMenuItem == null) return false;
	if (this.preMenuItem.visible == false)  return this.preMenuItem.showPreItemRigetDiv();
	if (this.preMenuItem.sep)
		this.preMenuItem.sep.style.display = "block";
	this.preMenuItem.divRight.style.display = "none";
	this.preMenuItem.divCenter.style.paddingRight = "20px";
	return true;
};

MenuItemComp.prototype.hidePreMenuItemRight = function(){
	
};

/**
 * 得到激活状态
 */
MenuItemComp.prototype.isActive = function() {
	return !this.disabled;
};

/**
 * 更改显示字符串
 */
MenuItemComp.prototype.changeCaption = function(caption) {
//	caption = getString(caption, MenuItemComp.DEFAULT_CAPTION);
	caption = getString(caption, "");
	if (this.caption != caption) {
		this.caption = caption;
	}
	if (this.isMenuBarItem == false) {  // 不是Menubar的子项
		//modify by chouhl 2012-2-13
		if(this.divCenter && this.divCenter != 'undefined'){
			if(this.divCenter.children && this.divCenter.children.length > 0){
				this.divCenter.children[0].innerHTML = this.caption;	
			}else{
				var captionDiv = $ce("DIV");
				captionDiv.className = "captionTextDiv";
				captionDiv.innerHTML = this.caption;
				this.divCenter.appendChild(captionDiv);
			}
		}else{
			this.divCaption.innerHTML = this.caption;
		}
	} else {  // 是Menubar的子项
		if (this.refImg) {
			if (this.caption == null || this.caption == "") {
				this.captionTextDiv.innerHTML = "";
			} else if (!IS_IE)
				this.captionTextDiv.innerHTML = "&nbsp;" + this.caption;
			else
				this.captionTextDiv.innerHTML = "&nbsp;&nbsp;&nbsp;" + this.caption;
		} else {
			//modify by chouhl 2012-2-13
			if(this.divCenter && this.divCenter != 'undefined'){
				if(this.divCenter.children && this.divCenter.children.length > 0){
					this.divCenter.children[0].innerHTML = this.caption;	
				}else{
					var captionDiv = $ce("DIV");
					captionDiv.className = "captionTextDiv";
					captionDiv.innerHTML = this.caption;
					this.divCenter.appendChild(captionDiv);
				}
			}else{
				this.divCaption.innerHTML = this.caption;
			}
		}
	}
};

/**
 * 更改显示图片
 */
MenuItemComp.prototype.changeImg = function(refImg) {
	if (this.refImg != refImg) {
		this.refImg = refImg;
	}
	if (this.leftImg != null) {
		this.leftImg.src = this.refImg;
	} else {
		// 增加图片
		this.leftImg = $ce("IMG"); 
		this.leftImg.src = this.refImg;
		if (this.isMenuBarItem == false) {  // 不是Menubar的子项
			this.leftImg.width = MenuItemComp.IMAGE_WIDTH - 4;
			this.leftImg.height = MenuItemComp.IMAGE_WIDTH - 4;
			this.leftImg.style.left = "2px";
			this.leftDiv.appendChild(this.leftImg);
  		} else {  // 是Menubar的子项
			this.leftImg.style.height = "16px";
			this.leftImg.style.marginTop = "2px";
			this.captionImgDiv.appendChild(this.leftImg);
  		}
	}
};

/**
 * 返回MenuItemComp的显示对象
 */
MenuItemComp.prototype.getObjHtml = function() {
	return this.Div_gen;
};

/**
 * 鼠标单击事件
 * @private
 */
MenuItemComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	return this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 鼠标移入事件
 * @private
 */
MenuItemComp.prototype.onmouseover = function(e){
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseover", MouseListener.listenerType, mouseEvent);
};

/**
 * 响应快捷键（只响应第一个匹配子项的点击事件）
 * @private
 */
MenuItemComp.prototype.handleHotKey = function(key) {
	if (this.isActive() == false)
		return null;
	if (this.hotKey != null) {
		if (key == this.hotKey && this.onclick) {
			this.onclick(null);
			return this;
		}
	}
	// 匹配子项快捷键
	if (this.childMenu) {
		var childItems = this.childMenu.childItems;
		if (childItems != null && childItems.length > 0) {
			for (var i = 0, n = childItems.length; i < n; i++) {
				var obj = childItems[i].handleHotKey(key);
				if (obj != null)
					return obj;
			}
		}
	}
	return null;
};

/**
 * 获取对象信息
 * @private
 */
ContextMenuComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.ContextMenuContext";
	context.c = "ContextMenuContext";
	context.id = this.id;
	if (this.triggerObj != null)
		context.triggerId = this.triggerObj.id;
	// 获取子项Context
	if (this.childItems.length > 0) {
		context.childItemContexts = new Array();
		for (var i = 0, n = this.childItems.length; i < n; i++) {
			context.childItemContexts.push(this.childItems[i].getContext());
		}
	}
	// 获取子菜单Context
//	if (this.childMenus.length > 0) {
//		context.childMenuContexts = new Array();
//		for (var i = 0, n = this.childMenus.length; i < n; i++) {
//			context.childMenuContexts.push(this.childMenus[i].getContext());
//		}
//	}
	//TODO
	
	return context;
};

/**
 * 设置对象信息
 * @private
 */
ContextMenuComp.prototype.setContext = function(context) {
	// 为子项设置Context
	if (context.childItemContexts) {
		for (var i = 0, n = context.childItemContexts.length; i < n; i++) {
			for (var j = 0, m = this.childItems.length; j < m; j++) {
				if (this.childItems[j].id == context.childItemContexts[i].id) {
					this.childItems[j].setContext(context.childItemContexts[i]);
					break;
				}
			}
		}
	}
	// 为子菜单设置Context
//	if (context.childMenuContexts) {
//		for (var i = 0, n = context.childMenuContexts.length; i < n; i++) {
//			for (var j = 0, m = this.childMenus.length; j < m; j++) {
//				if (this.childMenus[j].id == context.childMenuContexts[i].id) {
//					this.childMenus[j].setContext(context.childMenuContexts[i]);
//					break;
//				}
//			}
//		}
//	}
	//TODO
	
};

/**
 * 获取对象信息
 * @private
 */
MenuItemComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.MenuItemContext";
	context.c = "MenuItemContext";
	context.enabled = !this.disabled;
	context.visible = this.visible;
	context.refImg = this.refImg;
	context.text = this.caption;
	context.id = this.id;
	// 获取子项Context
	if (this.childMenu) {
		var childItems = this.childMenu.childItems;
		if (childItems != null && childItems.length > 0) {
			context.childItemContexts = new Array();
			for (var i = 0, n = childItems.length; i < n; i++) {
				context.childItemContexts.push(childItems[i].getContext());
			}
		}
	}
	//TODO
	
	return context;
};

/**
 * 设置对象信息
 * @private
 */
MenuItemComp.prototype.setContext = function(context) {
	if (context.refImg != null && "" != context.refImg && context.refImg != this.refImg)
		this.changeImg(context.refImg);
	if (context.text != null && "" != context.text && context.text != this.caption)
		this.changeCaption(context.text);
	if (context.enabled != null && context.enabled == this.disabled)
		this.setActive(context.enabled);
	if (context.visible != null && context.visible != this.visible) {
		if (context.visible == false)
			this.hide();
		else
			this.show();
	}
	// 为子项设置Context
	if (context.childItemContexts) {
		for (var i = 0, n = context.childItemContexts.length; i < n; i++) {
			for (var j = 0, m = this.childMenu.childItems.length; j < m; j++) {
				if (this.childMenu.childItems[j].id == context.childItemContexts[i].id) {
					this.childMenu.childItems[j].setContext(context.childItemContexts[i]);
					break;
				}
			}
		}
	}
	//TODO
	
};

/**
 * MenuSepComp的构造函数，分隔条
 */
function MenuSepComp(parent, id, className, attrObj) {
	this.base = BaseComponent;
	this.base(id);
	
	var oThis = this;   
	if (attrObj) {
	}
	
	this.name = id;
	this.id = id;
	// 是否禁止此item
	this.disabled = false;
	// 此item的显示属性
	this.visible = true;
	this.className = getString(className, "contextmenu_item_div");
	this.height = getCssHeight(this.className + "_SEP_HEIGHT");
	this.parentHTML = parent;
	
	this.create();
	
};

/**
 * @private
 */
MenuSepComp.prototype.create = function() {
	var oThis = this;
	// item的整体div
	this.Div_gen = $ce("DIV");
	this.parentHTML.appendChild(this.Div_gen);
	this.Div_gen.className = this.className;
	
	this.Div_gen.style.overflow = "hidden";
	this.Div_gen.style.position = "relative";
	// 设置Div_gen的宽度和divCaption的宽度
	this.Div_gen.style.width = "auto";
	this.Div_gen.style.height = this.height + "px";
	this.Div_gen.owner = this;
	
	var content = this.createContent();
	if (null != content)
		this.Div_gen.appendChild(content);
};

/**
 * 创建内容
 * @private
 */
MenuSepComp.prototype.createContent = function() {
	var oThis = this;
   	this.leftDiv = $ce("DIV");
	this.Div_gen.appendChild(this.leftDiv);
 	this.leftDiv.className = "contextmenu_item_left_div_off";
	
	// 显示文字的div
	this.divCaption = $ce("DIV");		 
	this.Div_gen.appendChild(this.divCaption);
	this.divCaption.className = "contextmenu_sep_caption";
};

/**
 * 设置item宽度
 */
MenuSepComp.prototype.setItemWidth = function(eleWidth) {
	var width = new String(eleWidth);
	if (width.indexOf("%") != -1)
		width = this.Div_gen.offsetWidth;
	this.width = width - 1;
	this.Div_gen.style.width = (width - 2) + "px";
	
	// 改变caption的宽度,auto不会自动变化改变自己宽度
	this.divCaption.style.width = width - MenuItemComp.IMAGE_WIDTH - 2 + "px";
	
};

/**
 * 获得分割条宽度,包括图片宽度
 */
MenuSepComp.prototype.getItemWidth = function() {
	var textWidth = 0;
	// 得到css中设置的padding值
	var padding = getStyleAttribute(this.divCaption, "paddingRight");
	if (padding == null || padding == "")
		padding = 0;
	else
		padding = parseInt(padding,10);
	return textWidth + 2*padding + MenuItemComp.IMAGE_WIDTH;
};

