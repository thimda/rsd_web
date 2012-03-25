/**
 *														
 * @fileoverview Menubar实现,其级联子菜单由ContextMenu组成				
 * 事件处理可根据粒度来重写不同级别的方法,比如：				 
 * 可分别重写ContextMenuItem, ContextMenu, MenuBar 
 * 来进行不同粒度控制,任一方法中返回false，阻止父级方法进一步处理.			   
 * 
 * @author dengjt, gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 
 * 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 */
MenuBarComp.menuHeight = 25;
MenuBarComp.opeWidth = 25;

MenuBarComp.prototype = new BaseComponent;
MenuBarComp.prototype.componentType = "MENUBAR";

/**
 * MenuBarComp构造函数
 * @class Menubar实现,其级联子菜单由ContextMenu组成				
 *        事件处理可根据粒度来重写不同级别的方法,比如：				 
 *        可分别重写ContextMenuItem, ContextMenu, MenuBar 
 *        来进行不同粒度控制,任一方法中返回false，阻止父级方法进一步处理。
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
 * MenuBar的创建函数
 * @private
 */
MenuBarComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
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
 * MenuBarComp的回调函数
 * @private
 */
MenuBarComp.prototype.manageSelf = function() {
	this.centerDiv = $ce("DIV");
	this.centerDiv.className = "center_div";
	this.Div_gen.appendChild(this.centerDiv);
};

/** 
 * 销毁控件
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
 * 给MenuBar添加子菜单。每个子菜单是MenuItemComp。
 * @param ContextMenuItem 若此项为null,说明要创建MeneBarComp的Menu,否则传入要添加子菜单的item项
 * @param isCheckBoxGroup 表示此menu是否是checkboxgroup
 */
MenuBarComp.prototype.addMenu = function(menuId, menuCaption, menuTip, menuSrcImg, menuName, isCheckBoxGroup, attrObj) {	
	if (this.menuItems == null)
		this.menuItems = new HashMap();
	
	var menuItem = new MenuItemComp(this.centerDiv, menuId, menuCaption, menuTip, menuSrcImg, "menu_div"
			, true, isCheckBoxGroup, false, false, attrObj);
	if (this.lastAddItem)
		this.lastAddItem.sep.style.display = "block";
	// 将添加的menu放入全局数组中
	this.menuItems.put(menuId, menuItem);
	
	// 分割条图片
	var sep = $ce("DIV");
	sep.className = "menuitem_seperator";
	this.centerDiv.appendChild(sep);
	sep.style.display = "none";
	if (!IS_IE || IS_IE8)
		sep.style.height = "17px";
	// 将sep绑定到每个item数据对象上
	menuItem.sep = sep;
	menuItem.parentOwner = this;
	this.lastAddItem = menuItem;
	
	// 保存此菜单的menuBar
	menuItem.menuParent = this;
	return menuItem;
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
	// 首先隐藏掉上一个显示出来的子菜单
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
 * 响应快捷键（只响应第一个匹配子项的点击事件）
 * @private
 */
MenuBarComp.prototype.handleHotKey = function(key) {
	if (this.Div_gen.style.display == "none" || this.Div_gen.style.visibility == "hidden")
		return null;
	// 匹配子项快捷键
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
 * 增加自定义子项
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
			this.menuItems.get(context.childItemContexts[i].id).setContext(context.childItemContexts[i]);
		}
	}
	//TODO
	
};

