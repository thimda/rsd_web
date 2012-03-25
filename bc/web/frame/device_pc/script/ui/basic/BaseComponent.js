/**
 * 	@fileoverview 此文件定义了所有客户端控件的基类                 
 * @author gd,dengjt,guoweic
 * @version 2.0 此版本作了结构上的变更,将父通知子变为需要的子监听父,充分利用浏览器现有方法.另为window.objects
 * 对象存储的key值为真实控件ID,使此对象能够真正派上用场.  dengjt <b>modified</b>
 * 
 * 1.新增事件处理功能，使控件的每个事件可以增加多个方法，同时提供删除事件的一个、多个或全部方法功能 2.修改event对象的获取。 guoweic <b>modified</b>                            
 */
/* 此全局变量用于保存每个实例变量 */
window.objects = new Object;
/* 上层浮动组件的标准zIndex属性值.浮动组件以此标准加减zIndex值 */ 
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。
//BaseComponent.STANDARD_ZINDEX = 100;

//XP样式下，滚动条比较宽
BaseComponent.SCROLLWIDTH = 18;

//guoweic add start 2009-10-21
BaseComponent.prototype = new ListenerUtil;
//guoweic add end

BaseComponent.ELEMENT_ERROR = "error";
BaseComponent.ELEMENT_WARNING = "warning";
BaseComponent.ELEMENT_NORMAL = "normal";
BaseComponent.ELEMENT_SUCCESS = "success";

/** 
 * 所有基础控件的基类
 * @class 所有基础控件的基类,包含了所有控件的共同属性<br>                       
 * <b>控件特色:</b><br>
 * 1、通过prototype属性建立面向对象的轻量级控件库<br>
 * 2、接口标准化,比如所有事件触发以onXX开放<br>
 * 3、支持动态更改皮肤外观。尽量以css将展现分离,支持单个控件的各个细节的自定义外观修改<br>
 * 4、将控件本身与数据绑定分离．树控件,grid控件均采用MVC设计模式实现<br>
 * 5、使控件适应于tag和ajax等多种环境<br>
 * 6、提供丰富的控件类别。Rich Client控件库提供了几十种轻量级控件,完全满足大多数Web应用的需要<br>
 * 7、同时对各个控件,进行尽量轻度的封装,减轻客户端压力<br>                      
 * 8、提供完善的js调试方式。比如WatchTime和log<br>                
 * 	<b>注意:</b>控件使用过程中,如果要销毁一个控件,必须调用destroySelf方法,这样才能保证控件所占用资源完全被释放<br>
 * 
 * <b>Rich Client控件的一些开发规范:</b><br> 
 * 1、如果子控件的显示对象不是Div_gen,必须覆盖getObjHtml()方法返回真正的显示对象<br>
 * 2、如果objHtml是显示对象,那么使用objHtml.owner可以得到控件的数据对象<br>
 * 3、如果obj是数据对象,那么使用obj.parentHtml可以得到父控件的显示对象<br>
 * 4、如果obj是数据对象,那么使用obj.parentOwner可以得到父控件的数据对象<br>
 * 5、基类BaseComponent采用了template pattern设计模式为控件放入父控件的过程提供了统一的流程,子类必须实现manageSelf方法,子控件在放入父控件后会调用此方法<br>
 * 6、子控件兼听父控件的大小改变调整自己的大小,子控件如果需要改变自己的大小必须提供adjustSelf方法<br>
 * 
 * @constructor
 * @version 2.0
 * 
 * @param name 控件名称
 * @param left 控件左部x坐标
 * @param top 控件顶部y坐标
 * @param width 控件宽度
 * @param height 控件高度
 */
function BaseComponent(name, left, top, width, height) {	
	// 如果是0参数,则表明是子类继承,不需初始化.
	if (arguments.length == 0)
		return;
	ListenerUtil.call(this, true);
    this.id = name;      
    this.left = getInteger(left, 0);  
    this.top = getInteger(top, 0);        
    this.width = getString(width, '100%');
    this.height = getString(height, '100%'); 
    // 标志控件显示或隐藏的属性       
    this.visible = true;
    // 将此组件存储在全局变量objects中,通过组件id可以获取此组件的实例                      
    window.objects[this.id] = this;
    this.allChildObjects = new Array();

	// 快捷键
	this.hotKey = null;
	
	// guoweic: add start 2009-10-21
	ListenerUtil.call(this, true);
	// guoweic: add end
	
};

/**
 * 将控件添加到父控件中,如有特殊要求,子控件可以覆盖此方法。
 * @param parent 此控件将要放入的父容器组件
 */
BaseComponent.prototype.placeIn = function(parent) {
	// 得到此组件的显示对象
    var objHtml = this.getObjHtml();
    // 将真正的数据对象保存到显示对象的owner属性中     
    objHtml.owner = this;
    // 将此组件的实例放到oThis中,方法placeIn()内的方法(例:oncontextmenu方法)中this的值指的是该内部方法,不是此组件对象
    var oThis = this;
  
    // 给组件显示对象添加右键菜单
    objHtml.oncontextmenu = function(e) {
		e = EventUtil.getEvent();
		
		oThis.onBeforeShowMenu(e);
    	// 调用真正的数据对象的方法
		oThis.oncontextmenu(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
    };
    
    // 父控件定义了特定的添加方法
    if (parent.add) { 
        parent.add(objHtml);  
    } else {
        parent.appendChild(objHtml);                            
    }
    // 将此组件的父组件的显示对象存放到parentHtml属性中
    this.parentHtml = objHtml.parentNode;	
    
    // 调用二级初始化方法
    this.manageSelf();
};

/** 
 * 获取控件的显示对象
 * @return 此控件的显示对象，如果控件的显示对象非Div_gen，需覆盖getObjHtml函数
 */
BaseComponent.prototype.getObjHtml = function() {
    return this.Div_gen;
};

/** 
 * 默认控件右键菜单显示．如果需要额外控制,覆盖此函数
 * @private
 */
BaseComponent.prototype.oncontextmenu = function(e) {
	e = EventUtil.getEvent();
	if(this.contextMenu) {	
		// triggerObj保存是谁触发的此控件,可能有很多控件触发此菜单控件
		this.contextMenu.triggerObj = this;
		this.contextMenu.show(e);
		stopEvent(e);
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 添加子控件默认方法。如果需要特殊控制,需覆盖此方法
 * @param ObjHtml 子控件对象的显示对象
 */
BaseComponent.prototype.add = function(ObjHtml) {
	if (ObjHtml.owner) {
		// 添加到子控件数组中
		this.allChildObjects.push(ObjHtml.owner);
		ObjHtml.owner.parentOwner = this;
	}
	this.getObjHtml().appendChild(ObjHtml);
};

/** 
 * 添加进父控件之后,如果控件需要依据父控件设定值,则覆盖此函数
 */
BaseComponent.prototype.manageSelf = function() {
};

/** 
 * 设置控件位置
 * @param left 控件左侧X坐标
 * @param top  控件顶部Y坐标
 */
BaseComponent.prototype.setPosition = function(left, top) {	
	// 改变数据对象的值
	this.left = getInteger(left, 0);
	this.top = getInteger(top, 0);
	// 改变显示对象的值
	this.getObjHtml().style.left = this.left + "px";
	this.getObjHtml().style.top = this.top + "px";
};

/**
 * 设置控件大小
 * @param width  控件的宽度
 * @param height 控件的高度
 */
BaseComponent.prototype.setSize = function(width, height) {
	this.width = getString(width, "100%");
	this.height = getString(height, "100%");
	if(width != -1)
		this.getObjHtml().style.width = this.width;
	if(height != -1)
		this.getObjHtml().style.height = this.height;
};

/**
 * 设置控件边界值.子控件可根据实际情况覆盖此函数
 * @param left   控件左侧X坐标
 * @param top    控件顶部Y坐标
 * @param width  控件的宽度
 * @param height 控件的高度
 */
BaseComponent.prototype.setBounds = function(left, top, width, height) {	
	// 改变数据对象的值
	this.left = getInteger(left, 0);
	this.top = getInteger(top, 0);
	this.width = getString(width, "100%");
	this.height = getString(height, "100%");
	// 改变显示对象的值
	this.getObjHtml().style.left = this.left + "px";	
	this.getObjHtml().style.top = this.top + "px";
	this.getObjHtml().style.width = this.width;
	this.getObjHtml().style.height = this.height;
};

/**
 * 得到组件的width属性
 * @return 控件的宽度
 */
BaseComponent.prototype.getCompWidth = function() {
	return this.width;
};

/**
 * 得到组件的height属性
 * @return 控件的高度
 */
BaseComponent.prototype.getCompHeight = function() {
 	return this.height;
};

/**
 * 设置控件立体高度
 * @param zIndex 控件的第三维高度
 */
BaseComponent.prototype.setZIndex = function(zIndex) {
	this.getObjHtml().style.zIndex = zIndex;
};

/**
 * 得到容器内容部分的高度。
 * 各个控件根据具体情况返回值。
 */
BaseComponent.prototype.getContentHeight = function() {
	return this.getObjHtml().offsetHeight;
};

/**
 * 得到容器内容部分的宽度。
 * 各个控件根据具体情况返回值。
 */
BaseComponent.prototype.getContentWidth = function() {
	return this.getObjHtml().offsetWidth;
};

/** 
 * 销毁控件
BaseComponent.prototype.destroySelf = function() {
	if (this.allChildObjects != null) {
		for (var i = 0; i < this.allChildObjects.length; i ++) {
			this.allChildObjects[i].destroySelf();
		}
		this.allChildObjects = null;
	}
    var objHtml = this.getObjHtml();
    this.parentHtml.removeChild(objHtml);
	objHtml = null;
    // 将保存在全局变量中的此控件引用置空
    window.objects[this.id] = null;
   	
   	// 从clickHolders中删除此对象
    for (var i = 0; i < window.clickHolders.length; i++) {
    	if (window.clickHolders[i] == this) {
    		// 此处应调用数组删除元素方法
    		// gd:check 09-04
    		//window.clickHolders[i] = window.clickHolders.splice(i, 1);
    		window.clickHolders.splice(i, 1);
    		break;
    	}
    }
    
    this.name = null;
    this.id = null;
    this.left = null;
    this.top = null;
    this.width = null;    
    this.height = null;
    this.visible = null;
    this.childObjects = null;  
    this.allChildObjects = null;
    
    this.eventMap = null;
    this.contextMenu = null;
}; 
 */

/** 
 * 销毁控件（子类中如果必要须重写该方法）
 */
BaseComponent.prototype.destroySelf = function() {
	this.destroy();
};

/**
 * 销毁控件执行方法，该方法仅销毁自身，不包括销毁控件包含的子控件（供子类调用）
 */
BaseComponent.prototype.destroy = function() {
	if (this.allChildObjects != null) {
		for (var i = 0; i < this.allChildObjects.length; i++) {
			this.allChildObjects[i].destroySelf();
		}
		this.allChildObjects = null;
	}
    var objHtml = this.getObjHtml();
    if (objHtml){
    	objHtml.owner = null;
    	// 删除所有子节点
    	removeAllChildNodes(objHtml);
    	clearNodeProperties(objHtml);
    }
    this.Div_gen = null;
    //TODO removeChild()方法会引发溢出
//    if (this.parentHtml) {
//    	try {
//    		this.parentHtml.removeChild(objHtml);
//    		delete objHtml;
//    	} 
//    	catch(e) {
//    	}
//    }
    this.parentHtml = null;
    this.parentOwner = null;
    // 将保存在全局变量中的此控件引用置空
    window.objects[this.id] = null;
   	
   	// 从clickHolders中删除此对象
    for (var i = 0; i < window.clickHolders.length; i++) {
    	if (window.clickHolders[i] == this) {
    		// 此处应调用数组删除元素方法
    		// gd:check 09-04
    		//window.clickHolders[i] = window.clickHolders.splice(i, 1);
    		window.clickHolders.splice(i, 1);
    		break;
    	}
    }
    
    if(this.destroyFurther)
    	this.destroyFurther();
    
    clearNodeProperties(this);
};

/** 
 * 隐藏控件(显示属性是display)
 */
BaseComponent.prototype.hide = function() {                
    var obj = this.getObjHtml();
    if(obj != null)
    	obj.style.display = "none";
    this.visible = false;
};

/** 
 * 显示控件(显示属性是display)
 */
BaseComponent.prototype.show = function() {                    
    var obj = this.getObjHtml();
    obj.style.display = "block";
    this.visible = true;
};

/**
 * 隐藏控件(显示属性是visibility)
 */
BaseComponent.prototype.hideV = function() {
	var obj = this.getObjHtml();
	if(obj != null)
    	obj.style.visibility = "hidden";
    this.visible = false;
};

/**
 * 显示控件(显示属性是visibility)
 */
BaseComponent.prototype.showV = function() {
	var obj = this.getObjHtml();
    obj.style.visibility = "";
    this.visible = true;
};

/**
 * 设置CSS类名
 */
BaseComponent.prototype.setClass = function(key, className) {
	if (this.classMap == null)
		this.classMap = new HashMap();
	this.classMap.put(key, className);
	this.onClassChange(key);
};

/**
 * CSS类改变事件
 * @private
 */
BaseComponent.prototype.onClassChange = function(key) {
};

/**
 * 设置右键菜单
 */
BaseComponent.prototype.setContextMenu = function(menu){
	this.contextMenu = menu;
};

/**
 * 获取右键菜单
 */
BaseComponent.prototype.getContextMenu = function() {
	return this.contextMenu;
};

/**
 * 显示右键菜单
 */
BaseComponent.prototype.showMenu = function(e) {
	if (this.contextMenu != null)
		this.contextMenu.show(e);
};

/**
 * 右键菜单显示前执行事件方法
 * @private
 */
BaseComponent.prototype.onBeforeShowMenu = function(e) {
	var simpleEvent = {
			"obj" : this
		};
	this.doEventFunc("beforeShow", ContextMenuListener.listenerType, simpleEvent);
};

/**
 * 响应快捷键
 */
BaseComponent.prototype.handleHotKey = function(key) {
	if (this.hotKey != null) {
		if (key == this.hotKey && this.onclick) {
			this.onclick(null);
			return this;
		}
	}
	return null;
};

/**
 * 设置快捷键
 */
BaseComponent.prototype.setHotKey = function(hotKey) {
	this.hotKey = hotKey;
};

/**
 * 获取快捷键
 */
BaseComponent.prototype.getHotKey = function() {
	return this.hotKey;
};

/**
 * 获取对象信息
 * @private
 */
BaseComponent.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "";
	context.c = "";
	return context;
};

/**
 * 设置对象信息
 * @private
 */
BaseComponent.prototype.setContext = function(context) {
	
};
