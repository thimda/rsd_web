
Widget.prototype = new ListenerUtil;

/**
 * 页面片段构造方法
 * @class 页面Widget的类
 */
function Widget(id, visible, dialog) {
	ListenerUtil.call(this, true);
	this.id = id;
	this.comps = new HashMap;
	this.dss = new HashMap;
	this.combodatas = new HashMap;
	this.refnodes = new HashMap;
	// dsRelation集合
	this.dsRelations = null;
	this.menuMap = new HashMap();
	this.cardMap = new HashMap();
	this.tabMap = new HashMap();
	this.panelMap = new HashMap();
	this.outlookMap = new HashMap();
	this.splitMap = new HashMap();
	
	this.plugOutMap = new HashMap();
	
	this.visible = visible;
	this.dialog = dialog;
	this.pageState = null;
	this.operateState = null; 
};

/**
 * 增加split
 */
Widget.prototype.addSplit = function(split){
	this.splitMap.put(split.id, split);
};

/**
 * 获取split
 */
Widget.prototype.getSplit = function(id){
	return this.splitMap.get(id);
};

/**
 * 增加Tab
 */
Widget.prototype.addTab = function(tab) {
	this.tabMap.put(tab.id, tab);
};

/**
 * 获取Tab
 */
Widget.prototype.getTab = function(id) {
	return this.tabMap.get(id);
};

/**
 * 增加Card 
 */
Widget.prototype.addCard = function(card) {
	this.cardMap.put(card.id, card);
};

/**
 * 获取Card
 */
Widget.prototype.getCard = function(id) {
	return this.cardMap.get(id);
};

/**
 * 获取Panel
 */
Widget.prototype.getPanel = function(id) {
	return this.panelMap.get(id);
};

/**
 * 增加Panel
 */
Widget.prototype.addPanel = function(panel) {
	this.panelMap.put(panel.id, panel);
};

/**
 * 增加Outlook
 */
Widget.prototype.addOutlook = function(outlook) {
	this.outlookMap.put(outlook.id, outlook);
};

/*
 * 获取Outlook
 */
Widget.prototype.getOutlook = function(id){
	return this.outlookMap.get(id);
};

Widget.prototype.removePanel = function(id) {
	this.panelMap.remove(id);
};

/**
 * 获取PlugOut
 */
Widget.prototype.getPlugOut = function(id) {
	return this.plugOutMap.get(id);
};

/**
 * 增加PlugOut
 */
Widget.prototype.addPlugOut = function(plugout) {
	this.plugOutMap.put(plugout.id, plugout);
};

/**
 * 增加菜单
 */
Widget.prototype.addMenu = function(menu) {
	this.menuMap.put(menu.id, menu);
};

/**
 * 获取所有菜单
 */
Widget.prototype.getMenus = function() {
	return this.menuMap.values();
};

/**
 * 获取菜单
 */
Widget.prototype.getMenu = function(id) {
	return this.menuMap.get(id);
};

/**
 * 设置可见性
 */
Widget.prototype.setVisible = function(visible) {
	if (this.visible != visible) {
		this.visible = visible;
		if (this.dialog) {
			if (visible) {
				if (this.onBeforeShow() == false)
					return;
				pageUI.getDialog(this.id).show();
			} 
			else {
				pageUI.getDialog(this.id).close();
			}
		}
	}
	var dlgListener = new DialogListener();
	var oThis = this;
	dlgListener.afterClose = function(simpleEvent) {
		oThis.visible = false;
	};

	pageUI.getDialog(this.id).addListener(dlgListener);
	if (this.lazyInit) {
		this.lazyInit();
		this.lazyInit = null;
		this.$beforeInitData();
	}
};

Widget.prototype.onBeforeShow = function() {
	var simpleEvent = {
		"obj" : this
	};
	this.doEventFunc("beforeShow", DialogListener.listenerType, simpleEvent);
};

/**
 * 更新全部的按钮状态
 * @private
 */
Widget.prototype.updateButtons = function() {
	var activedMenubars = this.getActivedMenubars();
	if (activedMenubars != null && activedMenubars.length > 0) {
		for ( var n = 0, count = activedMenubars.length; n < count; n++) {
			var menubar = activedMenubars[n];
			var menubarItems = menubar.menuItems;

			if (menubarItems != null) {
				var menuItems = menubarItems.values();
				for ( var i = 0, count1 = menuItems.length; i < count1; i++) {
					if (menuItems[i].contextMenu != null) {
						var cItems = menuItems[i].contextMenu.childItems;
						for ( var j = 0; j < cItems.length; j++)
							menuItems.push(cItems[j]);
					}
				}

				//特殊处理状态
				var userMap = this.getUserStateMap();
//				if (menuItems != null && menuItems.length > 0) {
//					for ( var i = 0; i < menuItems.length; i++) {
//						var operFlag = false;
//						var busiFlag = false;
//						var userFlag = true;
//						var menuItem = menuItems[i];
//						// 操作状态
//						var operStatusArr = menuItem.operateStateArray;
//						if (operStatusArr != null && this.operateState != null) {
//							for ( var m = 0, count1 = operStatusArr.length; m < count1; m++) {
//								if (operStatusArr[m] == this.operateState /*
//																			 * ||
//																			 * operStatusArr[m] ==
//																			 * IBillOperate.OP_ALL
//																			 */)
//									operFlag = true;
//							}
//						} else
//							operFlag = true;
//
//						// 业务装态
//						var busiStatusArr = menuItem.businessStateArray;
//						if (busiStatusArr != null && this.businessState != null) {
//							for ( var m = 0, count1 = busiStatusArr.length; m < count1; m++) {
//								if (busiStatusArr[m] == this.businessState /*
//																			 * ||
//																			 * busiStatusArr[m]==
//																			 * billUI.eventHandler.getBillStatusValue(IBillStatusName.ALL)
//																			 */)
//									busiFlag = true;
//							}
//						} else
//							busiFlag = true;
//
//						// if(userMap != null)
//						// {
//						// var menuItem = menuMap.get(keySet[j]);
//						// if (menuItem == null)
//						// continue;
//						// if(userMap.containsKey(menuItem.name)){
//						// userFlag = userMap.get(menuItem.name);
//						// }
//						// }
//
//						if (operFlag && busiFlag && userFlag)
//							menuItem.setActive(true);
//						else
//							menuItem.setActive(false);
//					}
//				}
//				
				if (menuItems != null && menuItems.length > 0) {
					for ( var i = 0; i < menuItems.length; i++) {
						menuItems[i].updateState(this.operateState, this.businessState, this.userState);
					}
				}				
			}
		}
	}
};

/**
 * 自定义按钮状态(用户指定)
 * @private
 */
Widget.prototype.getUserStateMap = function() {

};

/**
 * 获取当前激活的menubargroup中的menubar和单独显示的menubar
 */
Widget.prototype.getActivedMenubars = function() {
	var menubars = [];
	// 当前可见的menubar
	var menus = this.menuMap.values();
	for ( var i = 0, count = menus.length; i < count; i++) {
		menubars.push(menus[i]);
	}
	return menubars;
};

/**
 * @private
 */
Widget.prototype.getContext = function(widgetRule) {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.WidgetUIContext";
	context.c = "WidgetUIContext";
	context.visible = this.visible;
	context.cos = this.operateState;
	context.cbs = this.businessState;
	context.cus = this.userState;
	
	var contextXmlArray = new Array();
	contextXmlArray.push("<" + EventContextConstant.context + ">");
	contextXmlArray.push(toJSON(context));
	contextXmlArray.push("</" + EventContextConstant.context + ">");

	var comps = this.comps.values();
	if (comps != null && comps.length > 0) {
		for ( var i = 0; i < comps.length; i++) {
			var comp = comps[i];
			var compType = comp.componentType.toLowerCase();

			var type = null;
			if (typeof TreeViewComp != "undefined" && comp.componentType == TreeViewComp.prototype.componentType) {
				var rule = widgetRule == null ? null : widgetRule
						.getTreeRule(comp.id);
				if (rule != null)
					type = rule.type;
			}
			if (typeof AutoFormComp != "undefined" && comp.componentType == AutoFormComp.prototype.componentType) {
				var rule = widgetRule == null ? null : widgetRule
						.getFormRule(comp.id);
				if (rule != null)
					type = rule.type;
			}
			if (typeof GridComp != "undefined" && typeof GridComp != 'undefined' && comp.componentType == GridComp.prototype.componentType) {
				var rule = widgetRule == null ? null : widgetRule
						.getGridRule(comp.id);
				if (rule != null)
					type = rule.type;
			}
			var ctx = comp.getContext(type);
			contextXmlArray.push("<" + compType + " id=\"" + comp.id + "\">");
			contextXmlArray.push("<" + EventContextConstant.context + "><![CDATA[");
			contextXmlArray.push(toJSON(ctx));
			contextXmlArray.push("]]></" + EventContextConstant.context + ">");
			contextXmlArray.push("</" + compType + ">");
		}
	}
	var dss = this.dss.values();
	if (dss != null && dss.length > 0) {
		for ( var i = 0; i < dss.length; i++) {
			var ds = dss[i];
			var dsRule = null;
			var type = null;
			if (widgetRule != null) {
				dsRule = widgetRule.getDsRule(ds.id);
				if (dsRule != null)
					type = dsRule.type;
			}
			contextXmlArray.push("<dataset id=\"" + ds.id + "\">");
			contextXmlArray.push("<data>");
			// var dsContent = encodeURIComponent(searializeDataset(ds, type));
			var dsContent = searializeDataset(ds, type);
			contextXmlArray.push("<![CDATA[" + dsContent + "]]>");
			contextXmlArray.push("</data>");
			contextXmlArray.push("</dataset>");
		}
	}

	var menus = this.menuMap.values();
	if (menus != null && menus.length > 0) {
		for ( var i = 0; i < menus.length; i++) {
			var menu = menus[i];
			if (menu.componentType == ContextMenuComp.prototype.componentType) {
				contextXmlArray.push("<ctxmenu id=\"" + menu.id + "\">");
			} else {
				contextXmlArray.push("<menubar id=\"" + menu.id + "\">");
			}
			contextXmlArray.push("<" + EventContextConstant.context + "><![CDATA[");
			contextXmlArray.push(toJSON(menu.getContext()));
			contextXmlArray.push("]]></" + EventContextConstant.context + ">");
			if (menu.componentType == ContextMenuComp.prototype.componentType) {
				contextXmlArray.push("</ctxmenu>");
			} else
				contextXmlArray.push("</menubar>");
		}
	}

	if (widgetRule != null && widgetRule.getTabSubmit()) {
		var tabs = this.tabMap.values();
		if (tabs != null && tabs.length > 0) {
			for ( var i = 0; i < tabs.length; i++) {
				contextXmlArray.push("<tab id=\"" + tabs[i].id + "\">");
				contextXmlArray.push("<" + EventContextConstant.context + "><![CDATA[");
				contextXmlArray.push(toJSON(tabs[i].getContext()));
				contextXmlArray.push("]]></" + EventContextConstant.context + ">");
				contextXmlArray.push("</tab>");
			}
		}
	}

	if (widgetRule != null && widgetRule.getCardSubmit()) {
		var cards = this.cardMap.values();
		if (cards != null && cards.length > 0) {
			for ( var i = 0; i < cards.length; i++) {
				contextXmlArray.push("<card id=\"" + cards[i].id + "\">");
				contextXmlArray.push("<" + EventContextConstant.context + "><![CDATA[");
				contextXmlArray.push(toJSON(cards[i].getContext()));
				contextXmlArray.push("]]></" + EventContextConstant.context + ">");
				contextXmlArray.push("</card>");
			}
		}
	}

	if (widgetRule != null && widgetRule.getPanelSubmit()) {
		var panels = this.panelMap.values();
		if (panels != null && panels.length > 0) {
			for ( var i = 0; i < panels.length; i++) {
				contextXmlArray.push("<panel id=\"" + panels[i].id + "\">");
				contextXmlArray.push("<" + EventContextConstant.context + "><![CDATA[");
				contextXmlArray.push(toJSON(panels[i].getContext()));
				contextXmlArray.push("]]></" + EventContextConstant.context + ">");
				contextXmlArray.push("</panel>");
			}
		}
	}
	return contextXmlArray.join("");
};

/**
 * @private
 */
Widget.prototype.setContext = function(context) {
	this.setVisible(context.visible);
	//var currPageState = context.cos;
	var currOperateState = context.cos;
	var currBusiState = context.cbs;
	var currUserState = context.cus;
	//if (currPageState != this.pageState)
	//	this.setPageState(currPageState);

	var isUpdBtns = false;
	if (currOperateState != this.operateState) {
		this.setOperateState(currOperateState);
		isUpdBtns = true;
	}
	if (currBusiState != this.businessState) {
		isUpdBtns = true;
		this.setBusinessState(currBusiState);
	}
	if (currUserState != this.userState) {
		isUpdBtns = true;
		this.setUserState(currUserState);
	}
	if (isUpdBtns == true)
		this.updateButtons();
};

Widget.prototype.recordUndo = function() {
	this.oldOperateState = this.operateState;
	this.oldBusinessState = this.businessState;
	this.oldUserState = this.userState;
};

Widget.prototype.undo = function() {
	var isUpdBtns = false;
	if (this.oldOperateState != null && this.oldOperateState != this.operateState) {
		this.setOperateState(this.oldOperateState);
		isUpdBtns = true;
	}
	if (this.oldBusinessState != null && this.oldBusinessState != this.businessState) {
		isUpdBtns = true;
		this.setBusinessState(this.oldBusinessState);
	}
	if (this.oldUserState != null && this.oldUserState != this.userState) {
		isUpdBtns = true;
		this.setUserState(this.oldUserState);
	}
	if (isUpdBtns == true)
		this.updateButtons();
};

/**
 * 设置片段的操作状态
 */
Widget.prototype.setOperateState = function(state) {
	this.operateState = state;
};

/**
 * 设置片段的业务状态
 * 
 * @param 单据的审批状态
 */
Widget.prototype.setBusinessState = function(state) {
	this.businessState = state;
};

/**
 * 设置片段的自定义状态
 * 
 */
Widget.prototype.setUserState = function(state) {
	this.userState = state;
};

/**
 * 增加组件
 */
Widget.prototype.addComponent = function(comp) {

	this.comps.put(comp.id, comp);
};

/**
 * 增加Dataset
 */
Widget.prototype.addDataset = function(ds) {
	this.dss.put(ds.id, ds);
};

/**
 * 增加ComboData
 */
Widget.prototype.addComboData = function(combodata) {
	this.combodatas.put(combodata.id, combodata);
};

/**
 * 获取ComboData
 */
Widget.prototype.getComboData = function(id) {
	return this.combodatas.get(id);
};

/**
 * 增加参照节点
 */
Widget.prototype.addRefNode = function(refnode) {
	this.refnodes.put(refnode.id, refnode);
};

/**
 * 获取参照节点
 */
Widget.prototype.getRefNode = function(id) {
	return this.refnodes.get(id);
};

/**
 * 设置Dataset关联关系
 */
Widget.prototype.setDsRelations = function(dsRelations) {
	this.dsRelations = dsRelations;
};

/**
 * 获取Dataset关联关系
 */
Widget.prototype.getDsRelations = function() {
	return this.dsRelations;
};

/**
 * 获取所有组件
 */
Widget.prototype.getComponents = function() {
	return this.comps.values();
};

/**
 * 获取组件
 */
Widget.prototype.getComponent = function(id) {
	return this.comps.get(id);
};

/**
 * 获取Dataset
 */
Widget.prototype.getDataset = function(id) {
	return this.dss.get(id);
};

/**
 * 获取所有Dataset
 */
Widget.prototype.getDatasets = function() {
	return this.dss.values();
};

/**
 * 删除组件
 */
Widget.prototype.removeComponent = function(id) {
	this.comps.remove(id);
};

/**
 * 删除Dataset
 */
Widget.prototype.removeDataset = function(id) {
	return this.dss.remove(id);
};

/**
 * Widget初始化前执行方法
 * @private
 */
Widget.prototype.beforeWidgetInit = function() {
	this.doEventFunc("beforeWidgetInit", WidgetListener.listenerType, null);
};

/**
 * Widget初始化后执行方法
 * @private
 */
Widget.prototype.afterWidgetInit = function() {
};

/**
 * Widget激活前执行方法
 * @private
 */
Widget.prototype.afterActive = function() {

};

/**
 * 数据初始化前执行方法
 * @private
 */
Widget.prototype.$beforeInitData = function() {
	if (this.lazyInit != null)
		return;
	var openBillId = getParameter("openBillId");
	var openDsId = getParameter("openDsId");
	var dss = this.getDatasets();
	if (dss != null) {
		for ( var j = 0; j < dss.length; j++) {
			if (openDsId == dss[j].id)
				dss[j].addReqParameter("openBillId", openBillId);
			if (openDsId == dss[j].id) {
				if (dss[j].dsLoaded)
					continue;
				dss[j].dsLoaded = true;
				dss[j].setCurrentPage(Dataset.MASTER_KEY, 0);
				dss[j].reqParameterMap.remove("openBillId");
			}
			if(!dss[j].dsLoaded && !dss[j].lazyLoad){
				if(dss[j].compArr.length == 0 && dss[j].hasComp)
					continue;
				dss[j].dsLoaded = true;
				dss[j].setCurrentPage(Dataset.MASTER_KEY, 0);
			}
		}
	}
};

/**
 * 删除ComboData
 */
Widget.prototype.replaceComboData = function(cbId, keyArr, valueArr) {
	var cb = new ComboData(cbId);
	if (keyArr != null && keyArr.length > 0) {
		for ( var i = 0; i < keyArr.length; i++) {
			var item = new ComboItem(keyArr[i], valueArr[i]);
			cb.addItem(item);
		}
	}
	this.combodatas.put(cb.id, cb);
	var comps = this.comps.values();
	for ( var i = 0; i < comps.length; i++) {
		var comp = comps[i];
		if (typeof ComboComp != "undefined" && typeof ComboComp != 'undefined' && comp instanceof ComboComp) {
			this.replaceCombo(comp, cbId, cb);
		} 
		else if (typeof AutoFormComp != "undefined" && typeof AutoFormComp != 'undefined' &&  comp instanceof AutoFormComp) {
			for ( var j = 0; j < comp.eleArr.length; j++) {
				if (comp.eleArr[j] instanceof ComboComp) {
					this.replaceCombo(comp.eleArr[j], cbId, cb);
				}
			}
		}
		else if (typeof GridComp != "undefined" && typeof GridComp != 'undefined' && comp instanceof GridComp) {
			var gridcomps = (comp.compsMap == null ? null : comp.compsMap.values());
			if(gridcomps != null && gridcomps.length > 0) {
				for ( var j = 0; j < gridcomps.length; j++) {
					if (gridcomps[j] instanceof ComboComp) {
						this.replaceCombo(gridcomps[j], cbId, cb);
					}
				}
			}
		}
	}
};

/**
 * 替换ComboData
 */
Widget.prototype.replaceCombo = function(comp, cbId, cb) {
	if (comp.comboData != null && comp.comboData.id == cbId) {
		comp.setComboData(cb);
	}
};

/**
 * Widget关闭时执行方法
 * @private
 */
Widget.prototype.widgetClosing = function() {

};

/**
 * Widget关闭后执行方法
 * @private
 */
Widget.prototype.widgetClosed = function() {

};

/**
 * 清除当前widget的所有dataset的状态
 */
Widget.prototype.callAllDsClearStatus = function() {
	var dss = this.getDatasets();
	if (dss != null && dss.length != 0) {
		for ( var i = 0, count = dss.length; i < count; i++)
			dss[i].clearState();
	}
};

/**
 * 清除当前widget的所有dataset的撤销记录
 */
Widget.prototype.callAllDsClearUndo = function() {
	var dss = this.getDatasets();
	if (dss != null && dss.length != 0) {
		for ( var i = 0; i < dss.length; i++)
			dss[i].clearUndo();
	}
};

/**
 * 分派事件到Dataset
 * @private
 */
Widget.prototype.dispatchEvent2Ds = function(event, masterDsId) {
	if (event == null || masterDsId == null)
		return;
	// 分发RowSelectEvent事件进行单独处理
	if (RowSelectEvent.prototype.isPrototypeOf(event)) {
	} else if (RowUnSelectEvent.prototype.isPrototypeOf(event)) {
	} else if (DataChangeEvent.prototype.isPrototypeOf(event)) {
		if (event.oldValue == event.currentValue) {
			return;
		}
		var ds = this.getDataset(masterDsId);
		/* 基本的数学计算式在前台来完成计算 */
		var formular = ds.metadata[event.cellColIndex].clientFormular;
		if (formular != null && formular != '') {
			var tmpKey = ds.metadata[event.cellColIndex].key;
			var a = decodeURIComponent(formular);
			var fArray = eval(a);
			while (fArray.length != 0) { //多个公式，则从后向前来运算
				var editFormulars = fArray.pop();
				if (editFormulars == null)
					continue;
				var formulars = editFormulars.formular;// 公式中右边的
				for ( var k = 0; k < formulars.length; k++) {
					if (formulars[k].value == tmpKey) {
						$countFormular(editFormulars, ds, event);
						break;
					}
				}
			}
		}

		/*调用NC编辑公式*/
		formular = ds.metadata[event.cellColIndex].formular;
		var vf = ds.metadata[event.cellColIndex].editFormular;
		if(vf != null){
			if (formular != null && formular != '') {
				if (vf != null && vf != '')
					formular += ";";
				formular += vf;
			} 
			else
				formular = vf;
		}
		if (formular != null && formular != '') {
			var row = event.currentRow;
			var obj = new Object;
			obj.javaClass = 'java.util.HashMap';
			var objMap = new Object;
			obj.map = objMap;
			for ( var i = 0, len = ds.metadata.length; i < len; i++) {
				var md = ds.metadata[i];
				var value = row.getCellValue(i);
				// 将"."替换成 _$_
				var replaceField = null;
				if (md.field != null && md.field != "")
					replaceField = md.field.replace(".", "_$_");
				if (replaceField == null || replaceField == "")
					replaceField = md.key;
				objMap[replaceField] = value;
			}
			var arr = decodeURIComponent(formular).split(';');
			for ( var i = 0, len = arr.length; i < len; i++) {
				if (arr[i] == null || arr[i].trim() == '')
					continue;
				var dtMap = createDsDataTypeMap(ds);
				var result = getFormularService().execute("executeFormular", obj, encodeURIComponent(arr[i]), dtMap);

				// TODO 增加警告提示
				if ($executeResult(result, ds, event))
					continue;

				var resultValue = result.map["formular_value"];
				if (resultValue == '$NULL$')
					resultValue = null;
				ds.setValueAt(event.cellRowIndex, ds
						.nameToIndex(result.map["formular_key"]), resultValue,
						true);
			}
		}

		/*用户自定义的公式执行*/
		formular = ds.metadata[event.cellColIndex].defEditFormular;
		if (formular != null) {
			if (typeof myDefEditInvoke != "undefined") {
				myDefEditInvoke(decodeURIComponent(formular), ds, event);// 此方法需用户自己实现
			}
		}
	}
};

/**
 * 创建Dataset数据类型集合
 * @private
 */
function createDsDataTypeMap(ds) {
	var cache = getFromCache("dstype_key");
	if (cache == null) {
		cache = new Object;
		putToCache("dstype_key", cache);
	}

	var obj = cache[ds];
	if (obj == null) {
		obj = new Object;
		obj.javaClass = 'java.util.HashMap';
		var objMap = new Object;
		obj.map = objMap;
		for ( var i = 0, len = ds.metadata.length; i < len; i++) {
			var md = ds.metadata[i];
			var replaceField = null;
			if (md.field != null && md.field != "")
				replaceField = md.field.replace(".", "_$_");
			if (replaceField == null || replaceField == "")
				replaceField = md.key;
			// var replaceField = md.field.replace(".", "_$_");
			objMap[replaceField] = md.dataType;
		}
	}
	return obj;
}

/**
 * 直接打开参照页面
 * 
 * @param param 类似"a=b&c=d"形式的参数串
 */
Widget.prototype.openReference = function(refNodeId, returnFuncName,
		dialogWidth, dialogHeight, param, refresh, filterValue) {
	var refNode = this.getRefNode(refNodeId);
	if (refNode == null) {
		showErrorDialog("Can not find refnode by id:" + refNodeId);
		return;
	}
	var trueDialogWidth = getInteger(dialogWidth, 650);
	var trueDialogHeight = getInteger(dialogHeight, 400);
	var trueReadDsName = getString(refNode.readDs, "masterDs");
	var url = window.corePath + "/" + refNode.path + "?pageId="
			+ refNode.pageMeta + "&widgetId=" + this.id + "&otherPageUniqueId="
			+ getPageUniqueId() + "&readDs=" + trueReadDsName + "&nodeId="
			+ refNodeId;
	if (returnFuncName != null)
		url += "&returnFunc=" + returnFuncName;
	if (refNode.multiSel)
		url += "&multiSel=1";
	if (refNode.delegator != null)
		url += "&delegator=" + refNode.delegator;
	if (refNode.fromNc) {
		url += "&fromNc=Y";
	}
	if (param != null)
		url += "&" + param;

	if (refresh)
		url += "&refresh=1";
	showDialog(url, refNode.name, trueDialogWidth, trueDialogHeight, 0, false,
			true);

	Widget.doFilterRefDialog(getString(filterValue, ""), 0);

};

/**
 * 参照对话框的内容过滤（显示）
 * @private
 */
Widget.doFilterRefDialog = function(value, id) {
	if (Widget.waitFilterRt != null)
		clearTimeout(Widget.waitFilterRt);
	// 判断Div是否完全打开
	if (window["$modalDialogFrame" + id].contentWindow == null
			|| window["$modalDialogFrame" + id].contentWindow.renderDone == null) {
		Widget.waitFilterRt = setTimeout("Widget.doFilterRefDialog('" + value
				+ "','" + id + "');", 50);
		return;
	}
	window["$modalDialogFrame" + id].contentWindow.doFilter(value);
};

WidgetListener.listenerType = "WIDGET_LISTENER";

/**
 * Widget事件
 * @private
 */
function WidgetListener() {
}

/**
 * @private
 */
WidgetListener.prototype.beforeWidgetInit = function() {
};

/**
 * @private
 */
WidgetListener.prototype.afterWidgetInit = function() {
};

/**
 * Widget中的plugout
 */
function PlugOut(id){
	this.id = id;
};

PlugOut.prototype.getItems = function(){
	return this.items; 
};

PlugOut.prototype.getItem = function(itemId){
	return this.items.get(id); 
};

PlugOut.prototype.addItem = function (item){
	if (this.items == null)
		this.items = new HashMap();
	this.items.put(item.id,item);	
};

/**
 * plugoutitem对象
 * @param {} name
 * @param {} type
 * @param {} source
 * @param {} desc
 */

function PlugOutItem(name, type, source, desc){
	this.id = name;
	this.type = type;
	this.source = source;
	this.desc = desc;
};

