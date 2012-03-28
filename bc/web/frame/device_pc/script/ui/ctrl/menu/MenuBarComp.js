/**
 *														
 * @fileoverview Menubar实现,其级联子菜单由ContextMenu组成				
 * 事件处理可根据粒度来重写不同级别的方�?比如�?			 
 * 可分别重写ContextMenuItem, ContextMenu, MenuBar 
 * 来进行不同粒度控�?任一方法中返回false，阻止父级方法进一步处�?			   
 * 
 * @author dengjt, gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获�?. guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器�? guoweic <b>modified</b>
 */
MenuBarComp.menuHeight = 27;
MenuBarComp.opeWidth = 25;

MenuBarComp.prototype = new BaseComponent;
MenuBarComp.prototype.componentType = "MENUBAR";

/**
 * MenuBarComp构造函�?
 * @class Menubar实现,其级联子菜单由ContextMenu组成				
 *        事件处理可根据粒度来重写不同级别的方�?比如�?			 
 *        可分别重写ContextMenuItem, ContextMenu, MenuBar 
 *        来进行不同粒度控�?任一方法中返回false，阻止父级方法进一步处理�?
 */
function MenuBarComp(parent, name, left, top, width, height, position, className) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.position = getString(position, "relative");
	this.parentOwner = parent;
	// 记录当前显示的子菜单
	this.currVisibleChildMenu = null;
	// 记录当前正在点击的菜单项
	this.nowClickedMenu = null;
	// 保存MenuBar中添加的所有menuitem
	this.menuItems = null;
	this.className = getString(className, "menubar_div");
	this.create();
};

/**
 * MenuBar的创建函�?
 * @private
 */
MenuBarComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.position = this.position;
	if (this.width.indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = MenuBarComp.menuHeight + "px";
	this.Div_gen.className = this.className;
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * MenuBarComp的回调函�?
 * @private
 */
MenuBarComp.prototype.manageSelf = function() {
	var html = "<table style='width:100%;' cellpadding='0' cellspacing='0'><tr><td class='div_left'></td><td valign='top' id='center' class='div_center'></td><td class='div_right'></td></tr></table>";
	this.Div_gen.innerHTML = html;
//	var leftDiv = $ce("DIV");
//	var centerDiv = $ce("DIV");
//	var rightDiv = $ce("DIV");
//	leftDiv.className = "div_left";
//	centerDiv.className = "div_center";
//	var width = 0;
//	if (this.width && this.width.indexOf("%") == -1)
//		width = parseInt(this.width);
//	else
//		width = this.Div_gen.offsetWidth;
////	if (!IS_IE)
////		centerDiv.style.width = width - 15*2 - 1 + "px";//15*2左右边框图片宽度
////	else	
////		centerDiv.style.width = width - 15*2 + "px";//15*2左右边框图片宽度
//	rightDiv.className = "div_right";
//	this.Div_gen.appendChild(leftDiv);
//	this.Div_gen.appendChild(centerDiv);
//	this.Div_gen.appendChild(rightDiv);
	var divCenter = this.Div_gen.firstChild.rows[0].cells[1];
	this.centerDiv = $ce("DIV");
	this.centerDiv.className = "center_div";
	divCenter.appendChild(this.centerDiv);
//	var oThis = this;
//	addResizeEvent(this.Div_gen, function() {
//		var width = 0
//		if (oThis.Div_gen.style.width && oThis.Div_gen.style.width.indexOf("%") == -1)
//			width = parseInt(oThis.Div_gen.style.width);
//		else
//			width = oThis.Div_gen.offsetWidth;
//		if (!IS_IE)	
//			oThis.Div_gen.children[1].style.width = width - 15*2 - 1 + "px";//15*2左右边框图片宽度
//		else	
//			oThis.Div_gen.children[1].style.width = width - 15*2 + "px";//15*2左右边框图片宽度
//	});
};

/** 
 * 销毁控�?
 * @private
 */
MenuBarComp.prototype.destroySelf = function() {
	// 销毁子菜单
	var items = this.menuItems.values();
	for (var i = 0, n = items.length; i < n; i++) {
		var item = items[i];
		item.destroySelf();
	}
	this.destroy();
};

/**
 * 给MenuBar添加子菜单。每个子菜单是MenuItemComp�?
 * @param ContextMenuItem 若此项为null,说明要创建MeneBarComp的Menu,否则传入要添加子菜单的item�?
 * @param isCheckBoxGroup 表示此menu是否是checkboxgroup
 */
MenuBarComp.prototype.addMenu = function(menuId, menuCaption, menuTip, menuSrcImg, menuName, isCheckBoxGroup, attrObj) {	
	if (this.menuItems == null)
		this.menuItems = new HashMap();
	
	var menuItem = new MenuItemComp(this.centerDiv, menuId, menuCaption, menuTip, menuSrcImg, "menu_div"
			, true, isCheckBoxGroup, false, false, attrObj);
	if (this.lastAddItem){
		//处理上一个item
		this.lastAddItem.sep.style.display = "block";
		this.lastAddItem.divRight.style.display = "none";
		this.lastAddItem.divCenter.style.paddingRight = "20px";
		//add by chouhl 2012-2-14
//		var children = this.lastAddItem.divCaption.children;
//		for(var i=0;i<children.length;i++){
//			if(children[i].className == 'divRight_off'){
//				this.lastAddItem.divCaption.removeChild(children[i]);
//				children[i-1].paddingRight = "20px";
//			}
//		}
		
		//处理当前item
		menuItem.divLeft.style.display = "none";
		
	}
	// 将添加的menu放入全局数组
	this.menuItems.put(menuId, menuItem);
	var sep = $ce("DIV");
	sep.className = "menuitem_seperator";
	this.centerDiv.appendChild(sep);
	sep.style.display = "none";
	// 将sep绑定到每个item数据对象
	menuItem.sep = sep;
	menuItem.parentOwner = this;
	if (this.lastAddItem){
		this.lastAddItem.nextMenuItem = menuItem; 
		menuItem.preMenuItem = this.lastAddItem;
	}
	this.lastAddItem = menuItem;
	
	// 保存此菜单的menuBar
	menuItem.menuParent = this;
	return menuItem;
};


/**
 * 向菜单中增加分割
 */
MenuBarComp.prototype.addSep = function() {
	//清空最后一次增加item记录，使下次增加item时，做为首个item
	this.lastAddItem = null;
	var sepDiv = $ce("DIV");
	sepDiv.className = "divSep";
	this.centerDiv.appendChild(sepDiv);
//	var id = this.id + "_sep_" + this.sepIndex;
//	var sep = new MenuSepComp(this.Div_gen, name, "menuitem_div", attrObj); 
//	// 存储此菜单的所有分隔条	
//	this.sepIndex = this.seps.push(sep);	  
//
//	// 保存此item的父菜单
//	sep.parentOwner = this;
//	sep.index = this.sepIndex - 1; 
//	this.addItemHtml(sep);
//
//	return sep;
};

/**
 * 根据menuId得到menu对象
 * @param menuId
 */
MenuBarComp.prototype.getMenu = function(menuId) {
	if (menuId == null || menuId == "")
		return null;
	if (this.menuItems != null)
		return this.menuItems.get(menuId);
};


/**
 * 点击menubar中的menuitem时调用此方法
 * @private
 */
MenuBarComp.prototype.click = function(e) {	
	var item = e.triggerItem;
	if (item.childMenu && item.childMenu.visible == false) {
		item.childMenu.setPosLeft(compOffsetLeft(item.Div_gen));
		item.childMenu.setPosTop(compOffsetTop(item.Div_gen) + item.Div_gen.offsetHeight);
		item.childMenu.show(e);
	} else if (item.childMenu && item.childMenu.visible == true) {
		item.childMenu.hide(e);
	}
	this.onclick(e);
};

/**
 * 鼠标停留在menubar中的menuitem时调用此方法
 * @private
 */
MenuBarComp.prototype.mouseover = function(e) {
	this.showChildMenu(e);
	this.onmouseover(e);
};

/**
 * 显示当前按钮的子菜单
 * @private
 */
MenuBarComp.prototype.showChildMenu = function(e) {
	var item = e.triggerItem;
	// 首先隐藏掉上一个显示出来的子菜�?
	if (this.currVisibleChildMenu != null) {
		this.currVisibleChildMenu.hide();
	}	
	if (item.childMenu) {
		item.childMenu.setPosLeft(compOffsetLeft(item.Div_gen));
		item.childMenu.setPosTop(compOffsetTop(item.Div_gen) + item.Div_gen.offsetHeight);
		item.childMenu.show(e);
		// 记录当前显示的子菜单
		this.currVisibleChildMenu = item.childMenu;
	}
};

/**
 * 鼠标移入menubar中的menuitem时调用此方法
 * @private
 */
MenuBarComp.prototype.onmouseover = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseover", MouseListener.listenerType, mouseEvent);
};

/**
 * 鼠标离开menubar中的menuitem时调用此方法
 * @private
 */
MenuBarComp.prototype.onmouseout = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onmouseout", MouseListener.listenerType, mouseEvent);
};

/**
 * 菜单触发已经绑定到triggerItem属性上。可重写此属性进行全局处理
 * @private
 */
MenuBarComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

/**
 * 响应快捷键（只响应第一个匹配子项的点击事件�?
 * @private
 */
MenuBarComp.prototype.handleHotKey = function(key) {
	if (this.Div_gen.style.display == "none" || this.Div_gen.style.visibility == "hidden")
		return null;
	// 匹配子项快捷�?
	var childItems = this.menuItems.values();
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
 * 增加自定义子�?
 * @param width 宽度
 * @param innerHTML 内容代码
 */
MenuBarComp.prototype.setSelfDefItem = function(width, innerHTML) {
	var selfDef = $ce("DIV");
	selfDef.style.width = width.indexOf("%") == -1 ? getInteger(width) + "px" : width;
	selfDef.style.height = "100%";
	selfDef.style.styleFloat = "right";
	selfDef.style[ATTRFLOAT] = "right";
	selfDef.style.marginRight = "5px";
	this.centerDiv.appendChild(selfDef);
	selfDef.innerHTML = innerHTML;

	return selfDef;
	
};

/**
 * 获取对象信息
 * @private
 */
MenuBarComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.MenubarContext";
	context.c = "MenubarContext";
	context.id = this.id;
	if (!this.menuItems) {
		context.childItemContexts = new Array();
		return context;
	}
	
	// 获取子项Context
	var childItems = this.menuItems.values();
	if (childItems.length > 0) {
		context.childItemContexts = new Array();
		for (var i = 0, n = childItems.length; i < n; i++) {
			context.childItemContexts.push(childItems[i].getContext());
		}
	}
	//TODO
	
	return context;
};

/**
 * 设置对象信息
 * @private
 */
MenuBarComp.prototype.setContext = function(context) {
	// 为子项设置Context
	if (context.childItemContexts) {
		for (var i = 0, n = context.childItemContexts.length; i < n; i++) {
			var item = this.menuItems.get(context.childItemContexts[i].id);
			if (item)
				item.setContext(context.childItemContexts[i]);
		}
	}
	//TODO
	
};

