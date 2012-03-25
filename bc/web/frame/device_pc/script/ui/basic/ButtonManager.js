/**
 * @fileoverview 此文件中定义了Button状态管理器
 * 
 * @author dengjt
 * 
 * @version NC6.0
 * @since NC5.5
 * 
 */

/**
 * Button状态管理器
 * @class Button状态管理器。此管理器可管理多个menubar，因此在WebFrame页面中只存在一个实例。使用时，可通过
 *        ButtonManager.getInstance()获取实例。
 *        它的工作机制是通过预设各个menu可接受操作状态或业务状态，以“,”分隔，在每次调用setOperateStatus
 *        或者setBusinessStatus时，ButtonManager自动比对各个菜单可接收的状态，设置菜单的激活状态。
 * @see IBillOperate
 * @author dengjt
 */
function ButtonManager() {
	// 按钮热键map
	this.hotKeyMap = new HashMap();
	this.menuBars = new Array();
	this.menuMaps = new HashMap();
	this.operStatus = null;// 操作状态
	this.busiStatus = null;// 业务状态
}

/**
 * 获得ButtonManager当前页面的实例
 * @return ButtonManager唯一实例
 */
ButtonManager.getInstance = function() {
	if (window.$buttonManager == null)
		window.$buttonManager = new ButtonManager();
	return window.$buttonManager;
};

/**
 * 添加一个MenuBar
 * @private 
 * @param menuBar
 */
ButtonManager.prototype.addMenuBar = function(menuBar) {
	//	var posMenubars = this.menuBars.get(menuBar.posIdentity);
	// var posMenus = this.menuMaps.get(menuBar.posIdentity);
	// if(posMenubars == null){
	// posMenubars = new Array;
	// posMenus = new Array;
	// this.menuBars.put(menuBar.posIdentity, posMenubars);
	// this.menuMaps.put(menuBar.posIdentity, posMenus);
	// }
	// // if(posMenubars.length != 0){
	// // menuBar.parentHtml.style.display = "none";
	// // }
	// posMenubars.push(menuBar);
	// posMenus.push(new HashMap());
	this.menuBars.push(menuBar);
};

// /**
// * 创建一个MenuBar
// * @param parent
// * @param name
// * @param left
// * @param top
// * @param width
// * @param height
// * @param position
// * @return 创建的menubar实例
// */
// ButtonManager.prototype.createMenuBar = function(parent, name, left, top,
// width, height, position, className, posIdentity)
// {
// var menuBar = new MenuBarComp(parent, name, left, top, width, height,
// position, className);
// menuBar.posIdentity = posIdentity;
// this.addMenuBar(menuBar);
// return menuBar;
// };

/*
 * @param isCheckBoxGroup 是否是checkboxgroup
 * @param hotKey{Array} 热键及修饰符数组
 */
// ButtonManager.prototype.createMenu = function(menubar, parent, menuid,
// menuName, menuTip, menuImg, menuVO, isCheckBoxGroup, hotKey, attrObj)
// {
// //var p_watch1 = window.$debugMonitor.getWatch('createMenu====' + menuName);
// //p_watch1.start();
// var menu = menubar.addMenu(parent, menuid, menuName, menuTip, menuImg,
// menuName, isCheckBoxGroup, attrObj);
// menu.menuAttr = menuVO;
// // 注册热键(hotkey + modifiers作为"键")
// if(hotKey != null && hotKey.length == 2)
// {
// if(hotKey[0] == null || hotKey[0] == "")
// hotKey[0] = "";
// this.hotKeyMap.put(hotKey[1] + hotKey[0], menu);
// }
//		
// //this.registMenuItem(menuid, menu);
// this.putToMenuMap(menubar, menuid, menu);
// //p_watch1.stop();
// return menu;
// };
// ButtonManager.prototype.createMenuItem = function(menuBar, parent, menuid,
// menuName, menuTip, menuImg, menuVO, isCheckBoxItem, isCheckBoxSelected,
// hotKey)
// {
// //var p_watch1 = window.$debugMonitor.getWatch('createMenuItem====' +
// menuName);
// //p_watch1.start();
// var menuItem = menuBar.addMenuItem(parent, menuid, menuName, menuTip,
// menuImg, isCheckBoxItem, isCheckBoxSelected);
// menuItem.menuAttr = menuVO;
//	
// // 注册热键(hotkey + modifiers作为"键")
// if(hotKey != null && hotKey.length == 2)
// {
// if(hotKey[0] == null || hotKey[0] == "")
// hotKey[0] = "";
// this.hotKeyMap.put(hotKey[1] + hotKey[0], menuItem);
// }
//	   
// //this.registMenuItem(menuid, menuItem);
// this.putToMenuMap(menuBar, menuid, menuItem);
// //p_watch1.stop();
// return menuItem;
// };
// ButtonManager.prototype.putToMenuMap = function(menubar, menuid, menuItem){
// var keySet = this.menuBars.keySet();
// for(var i = 0; i < keySet.length; i ++){
// var menubarArr = this.menuBars.get(keySet[i]);
// for(var j = 0; j < menubarArr.length; j ++){
// if(menubar == menubarArr[j]){
// var menus = this.menuMaps.get(keySet[i]);
// menus[j].put(menuid, menuItem);
// break;
// }
// }
// }
// };
//
// ButtonManager.prototype.getFromMenuMap = function(menuId){
// var menusArr = this.menuMaps.values();
// for(var i = 0; i < menusArr.length; i ++){
// var menu = menusArr[i].get(menuId);
// if(menu != null)
// return menu;
// }
// return null;
// };
// ButtonManager.prototype.registMenuItem = function(menuid, menuItem)
// {
// this.menuMap.put(menuid, menuItem);
// };
// ButtonManager.prototype.unRegistMenuItem = function(menuid)
// {
// this.menuMap.remove(menuid);
// };
/**
 * @private
 */
ButtonManager.prototype.showMenuBarOnly = function(index, posIdentity) {
	if (true)
		return;
	// var menubarArr = this.menuBars.get(posIdentity);
	// if(menubarArr == null)
	// return;
	// for(var i = 0, count = menubarArr.length; i < count; i++ ){
	// if(i == index)
	// menubarArr[i].parentHtml.style.display = "block";
	// else
	// menubarArr[i].parentHtml.style.display = "none";
	// }
};

/**
 * 设置可用状态
 */
ButtonManager.prototype.setMenuEnable = function(menuid, enable) {
	if (enable) {
		this.getFromMenuMap(menuid).setActive(false);
	} else {
		this.getFromMenuMap(menuid).setActive(true);
	}
};

/**
 * 设置操作状态
 */
ButtonManager.prototype.setOperateStatus = function(operStatus) {
	this.operStatus = operStatus;
	// var menusArr = this.menuMaps.values();
	// for(var i = 0; i < menusArr.length; i ++){
	// for(var j = 0; j < menusArr[i].length; j ++){
	// var menuMap = menusArr[i][j];
	// var keySet = menuMap.keySet();
	// for(var k = 0, count = keySet.length; k < count; k++)
	// {
	// var menuItem = menuMap.get(keySet[k]);
	// if (menuItem == null)
	// continue;
	// if(menuItem.menuAttr.opStatusArray == null)
	// continue;
	// var isEanble = false;
	// for(var m = 0, count1 = menuItem.menuAttr.opStatusArray.length; m <
	// count1; m++)
	// {
	// if (menuItem.menuAttr.opStatusArray[m] == operStatus ||
	// menuItem.menuAttr.opStatusArray[m] == IBillOperate.OP_ALL)
	// {
	// isEanble = true;
	// //alert(menuItem.caption + ":" + operStatus + ":" +
	// menuItem.menuAttr.opStatusArray)
	// menuItem.setActive(true);
	// break;
	// }
	// }
	// if(!isEanble){
	// menuItem.setActive(false);
	// }
	// }
	// }
	// }
};

/**
 * 单独设置按钮的状态
 * 
 * @param map(key:按钮id, value:设置状态)
 */
ButtonManager.prototype.setDefBusinessStatus = function(map) {
	if (map == null)
		return;
	var menusArr = this.menuMaps.values();
	for ( var i = 0; i < menusArr.length; i++) {
		for ( var k = 0; k < menusArr[i].length; k++) {
			var menuMap = menusArr[i][k];
			var keySet = menuMap.keySet();
			for ( var j = 0, count = keySet.length; j < count; j++) {
				var menuItem = menuMap.get(keySet[j]);
				if (menuItem == null)
					continue;
				if (map.containsKey(menuItem.name)) {
					menuItem.setActive(map.get(menuItem.name));
				}
			}
		}
	}
};

/**
 * 设置业务按钮的状态
 */
ButtonManager.prototype.setBusinessStatus = function(busiStatus) {
	this.busiStatus = busiStatus;
	// var menusArr = this.menuMaps.values();
	// for(var i = 0; i < menusArr.length; i ++){
	// for(var k = 0; k < menusArr[i].length; k ++){
	// var menuMap = menusArr[i][k];
	// var keySet = menuMap.keySet();
	// for(var j = 0, count = keySet.length; j < count; j++ )
	// {
	// var menuItem = menuMap.get(keySet[j]);
	// if (menuItem == null)
	// continue;
	// if( menuItem.menuAttr.busiStatusArray == null)
	// continue;
	// var isEanble = false;
	// var busiStatusArr = menuItem.menuAttr.busiStatusArray;
	// for (var m = 0, count1 = busiStatusArr.length; m < count1; m++)
	// {
	// if (busiStatusArr[m] == busiStatus || busiStatusArr[m]==
	// IBillStatus.ALL){
	// menuItem.setActive(true);
	// isEanble = true;
	// break;
	// }
	// }
	// if(!isEanble){
	// menuItem.setActive(false);
	// }
	// }
	// }
	// }
};

/**
 * 更新全部的按钮状态
 */
ButtonManager.prototype.updateButtons = function() {
	var menusArr = this.menuMaps.values();
	// 特殊处理状态
	var userMap = this.getUserStateMap();
	for ( var i = 0; i < menusArr.length; i++) {
		for ( var k = 0; k < menusArr[i].length; k++) {
			var menuMap = menusArr[i][k];
			var keySet = menuMap.keySet();

			for ( var j = 0, count = keySet.length; j < count; j++) {
				var operFlag = false;
				var busiFlag = false;
				var userFlag = true;
				var menuItem = menuMap.get(keySet[j]);
				if (menuItem == null)
					continue;
				// 操作状态
				var operStatusArr = menuItem.menuAttr.opStatusArray;
				if (operStatusArr != null && this.operStatus != null) {
					for ( var m = 0, count1 = operStatusArr.length; m < count1; m++) {
						if (operStatusArr[m] == this.operStatus
								|| operStatusArr[m] == IBillOperate.OP_ALL) {
							operFlag = true;
						}
					}
				} else
					operFlag = true;
				// 业务装态
				var busiStatusArr = menuItem.menuAttr.busiStatusArray;
				if (busiStatusArr != null && this.busiStatus != null) {
					for ( var m = 0, count1 = busiStatusArr.length; m < count1; m++) {
						if (busiStatusArr[m] == this.busiStatus
								|| busiStatusArr[m] == billUI.eventHandler
										.getBillStatusValue(IBillStatusName.ALL)) {
							busiFlag = true;
						}
					}
				} else
					busiFlag = true;

				if (userMap != null) {
					var menuItem = menuMap.get(keySet[j]);
					if (menuItem == null)
						continue;
					if (userMap.containsKey(menuItem.name)) {
						userFlag = userMap.get(menuItem.name);
					}
				}
				if (operFlag && busiFlag && userFlag)
					menuItem.setActive(true);
				else {
					menuItem.setActive(false);
				}
			}

		}
	}
};

/**
 * 自定义按钮状态
 * @private
 */
ButtonManager.prototype.getUserStateMap = function() {

};

/**
 * 设置含有业务数据的操作按钮处理
 */
ButtonManager.prototype.setButtonByBillStatus = function(ds, currentRow,
		isEditInGoing) {
	var billStatus = billUI.eventHandler.getBillStatus(ds, currentRow);
	var menusArr = this.menuMaps.values();
	for ( var i = 0; i < menusArr.length; i++) {
		for ( var k = 0; k < menusArr[i].length; k++) {
			var menuMap = menusArr[i][k];
			var keySet = menuMap.keySet();
			for ( var j = 0, count = keySet.length; j < count; j++) {
				var menuItem = menuMap.get(keySet[j]);
				var menuAttr = menuItem.menuAttr;

				// 设置审批进行中"修改"按钮状态
				if (billStatus == billUI.eventHandler
						.getBillStatusValue(IBillStatusName.CHECKGOING)
						&& menuAttr.btnNo == 3) {
					if (isEditInGoing)
						menuItem.setActive(true);
					else
						menuItem.setActive(false);
				}
			}
		}
	}
};

/**
 * 设置含有业务数据的操作按钮处理
 */
ButtonManager.prototype.setButtonByextendStatus = function(currentRows) {
	var extendStatus = this.getExtendStatus(currentRows);
	if (extendStatus == -1)
		return;
	for ( var i = 0; i < menusArr.length; i++) {
		for ( var k = 0; k < menusArr[i].length; k++) {
			var menuMap = menusArr[i][k];
			var keySet = menuMap.keySet();
			for ( var m = 0, count = keySet.length; m < count; m++) {
				var menuItem = this.menuMap.get(keySet[m]);
				var menuAttr = menuItem.menuAttr;
				var intExtendStatusAry = menuAttr.extendStatusArray;
				if (intExtendStatusAry != null) {
					menuItem.setActive(false);
					for ( var j = 0, count1 = intExtendStatusAry.length; j < count1; j++) {
						if (intExtendStatusAry[j] == extendStatus
								|| intExtendStatusAry[j] == IExtendStatus.ALL)
							menuItem.setActive(true);
					}
				}
			}
		}
	}
};

/**
 * @private
 */
ButtonManager.prototype.setPageButtonState = function(ds) {
};

/**
 * @private
 */
ButtonManager.prototype.getExtendStatus = function(rows) {
	return -1;
};
