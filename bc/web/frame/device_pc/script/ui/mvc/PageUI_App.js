var IBillStatusName = new Object;
IBillStatusName.NOPASS = "NOPASS";
IBillStatusName.CHECKPASS = "CHECKPASS";
IBillStatusName.CHECKGOING = "CHECKGOING";
IBillStatusName.COMMIT = "COMMIT";
IBillStatusName.DELETE = "DELETE";
IBillStatusName.CX = "CX";
IBillStatusName.ENDED = "ENDED";
IBillStatusName.FREEZE = "FREEZE";
IBillStatusName.FREE = "FREE";
IBillStatusName.ALL = "ALL";

/* EventContext常量 */
var EventContextConstant = new Object;
EventContextConstant.eventcontext = "e";
EventContextConstant.parentcontext = "pe";
EventContextConstant.params = "ps";
EventContextConstant.groupparams = "gps";
EventContextConstant.param = "p";
EventContextConstant.key = "k";
EventContextConstant.value = "v";
EventContextConstant.context = "c";
EventContextConstant.attributes = "as";
EventContextConstant.attribute = "a";
EventContextConstant.res_parameters = "dsps";
EventContextConstant.req_parameters = "dqps";
EventContextConstant.parameter = "dp";
EventContextConstant.NULL = "。";
EventContextConstant.records = "rs";
EventContextConstant.record = "r";
EventContextConstant.erecord = "er";
EventContextConstant.drecord = "dr";

/*正在执行请求的回调方法时，新请求存放在proxy_array中，延迟发送*/
ServerProxy.PROXY_ARRAY = new Array();
/*请求回调方法执行中的数量*/
ServerProxy.PROXYRETURN_EXECUTING = 0;
/*是否正在执行execServerProxyList方法*/
ServerProxy.PROXYLIST_EXECUTING = false;


PageUI.prototype = new ListenerUtil;

/**
 * 页面对象构造函数
 * @class 页面对象
 */
function PageUI(title) {
	ListenerUtil.call(this, true);
	this.title = title;
	this.widgetMap = new HashMap();
	this.dialogMap = new HashMap();
	// 以source为key进行存储
	this.connMap = new HashMap();
	// this.masterWidgetId = null;
	this.attributeMap = new HashMap();
	this.cardMap = new HashMap();
	this.tabMap = new HashMap();
	this.panelMap = new HashMap();
	this.pageState = null;
	this.operateState = null;
	this.pageStates = [];
	// 页面是否发生变化
	this.hasChanged = false;
};

PageUI.prototype.getContext = function(submitRule) {
	var context = new Object;
	context.c = "PageUIContext";
	context.currentPageState = this.pageState;
	context.currentOperateState = this.operateState;
	context.currentBusiState = this.businessState;
	context.currentUserState = this.userState;
	context.hasChanged = this.hasChanged;
	
	var contextXmlArray = new Array();
	contextXmlArray.push("<" + EventContextConstant.context + ">");
	contextXmlArray.push(toJSON(context));
	contextXmlArray.push("</" + EventContextConstant.context + ">");

	if (submitRule != null) {
		var widgets = this.widgetMap.values();
		if (widgets != null && widgets.length > 0) {
			for ( var i = 0; i < widgets.length; i++) {
				var widget = widgets[i];
				contextXmlArray.push("<widget id=\"" + widget.id + "\">");
				var widgetRule = submitRule.getWidgetRule(widget.id);
				contextXmlArray.push(widget.getContext(widgetRule));
				contextXmlArray.push("</widget>");
			}
		}

		if (submitRule.getTabSubmit()) {
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

		if (submitRule.getCardSubmit()) {
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

		if (submitRule.getPanelSubmit()) {
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

		if (submitRule.widgetRuleMap != null) {
			var keys = submitRule.widgetRuleMap.keySet();
			if (keys != null) {
				for ( var i = 0; i < keys.length; i++) {
					// 尚未初始化
					if (this.widgetMap.get(keys[i]) == null) {
						contextXmlArray.push("<widget id=\"" + keys[i]
								+ "\" init=\"false\"></widget>");
					}
				}
			}
		}
	}

	return contextXmlArray.join("");
};

PageUI.prototype.setContext = function(context) {
	var currPageState = context.currentPageState;
	var currOperateState = context.currentOperateState;
	var currBusiState = context.currentBusiState;
	var currUserState = context.currentUserState;
	var hasChanged = context.hasChanged;
	if (currPageState != this.pageState)
		this.setPageState(currPageState);

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
	if (hasChanged != this.hasChanged){
		this.setChanged(hasChanged);
	}
	if (isUpdBtns == true)
		this.updateButtons();
};

/**
 * 设置页面状态所有菜单组中的菜单条根据该状态确定是否显示
 * @private
 */
PageUI.prototype.setPageState = function(state) {
	this.pageState = state;
	this.updateButtons();
};

/**
 * 设置页面的操作状态
 * @private
 */
PageUI.prototype.setOperateState = function(state) {
	this.operateState = state;
};

/**
 * 设置单据的业务状态
 * 
 * @param 单据的审批状态
 * @private
 */
PageUI.prototype.setBusinessState = function(state) {
	this.businessState = state;
};

/**
 * 设置自定义状态
 * 
 * @private
 */
PageUI.prototype.setUserState = function(state) {
	this.userState = state;
};

/**
 * 自定义按钮状态(用户指定)
 * @private
 */
PageUI.prototype.getUserStateMap = function() {

};

/**
 * 更新全部的按钮状态
 * 
 * @private
 */
PageUI.prototype.updateButtons = function() {
	// 根据当前操作状态设置
	var widgets = this.getWidgets();
	if (widgets != null && widgets.length > 0) {
		for ( var i = 0; i < widgets.length; i++) {
			var dss = widgets[i].getDatasets();
			if (dss != null && dss.length > 0) {
				for ( var j = 0; j < dss.length; j++) {
					var dsStatusArr = dss[j].operateStateArray;
					var editable = false;
					if (dsStatusArr != null) {
						for ( var k = 0; k < dsStatusArr.length; k++) {
							if (dsStatusArr[k] == this.operateState)
								editable = true;
						}
						dss[j].setEditable(editable);
					}
				}
			}
		}
	}
};

/**
 * 增加Widget
 * @private
 */
PageUI.prototype.addWidget = function(widget) {
	//	widget.dsLoaded = false;
	this.widgetMap.put(widget.id, widget);
};

/**
 * 获取Widget
 */
PageUI.prototype.getWidget = function(widgetId) {
	return this.widgetMap.get(widgetId);
};

/**
 * 获取所有Widget
 * @return Widget数组
 */
PageUI.prototype.getWidgets = function() {
	return this.widgetMap.values();
};

/**
 * 增加对话框
 * @private
 */
PageUI.prototype.addDialog = function(id, dialog) {
	this.dialogMap.put(id, dialog);
};

/**
 * 获取对话框
 */
PageUI.prototype.getDialog = function(id) {
	return this.dialogMap.get(id);
};

/**
 * 获取所有对话框
 * @return 对话框数组
 */
PageUI.prototype.getDialogs = function() {
	return this.dialogMap.values();
};

/**
 * 增加属性
 * @private
 */
PageUI.prototype.addAttribute = function(name, value) {
	this.attributeMap.put(name, value);
};

/**
 * 获取属性
 */
PageUI.prototype.getAttribute = function(name) {
	return this.attributeMap.get(name);
};

/**
 * 增加连接对象
 * @private
 */
PageUI.prototype.addConnector = function(conn) {
	var arr = this.connMap.get(conn.source);
	if (arr == null) {
		arr = new Array;
		this.connMap.put(conn.source, arr);
	}
	arr.push(conn);
};

/**
 * 获取连接对象
 */
PageUI.prototype.getConnectors = function(source, signal) {
	var arr = this.connMap.get(source);
	if (arr == null)
		return null;
	var connArr = new Array;
	for ( var i = 0; i < arr.length; i++) {
		if (arr[i].signal == signal)
			connArr.push(arr[i]);
	}
	return connArr;
};

/**
 * 页面初始化前执行方法
 * @private
 */
PageUI.prototype.$beforePageInit = function() {
	this.doEventFunc("beforePageInit", PageListener.listenerType, null);
};

/**
 * 页面初始话后执行方法
 * @private
 */
PageUI.prototype.$afterPageInit = function() {
	this.doEventFunc("afterPageInit", PageListener.listenerType, null);
};

/**
 * 初始化数据前执行方法
 * @private
 */
PageUI.prototype.$beforeInitData = function() {
	var wds = this.getWidgets();
	for ( var i = 0; i < wds.length; i++) {
		wds[i].$beforeInitData();
		// if(wds[i].dsLoaded == false){
		// wds[i].dsLoaded = true;
		// }
	}
};

/**
 * 更新已加载的Widget
 * @private
 */
PageUI.prototype.updateInitedWidgets = function() {
	this.$beforeInitData();
};

/**
 * @private
 */
PageUI.prototype.$externalInit = function() {
	this.doEventFunc("externalInit", PageListener.listenerType, null);
};

/**
 * 激活前事件
 * @private
 */
PageUI.prototype.$beforeActive = function() {
	this.doEventFunc("beforeActive", PageListener.listenerType, null);
};

/**
 * 关闭页面事件
 * @private
 */
PageUI.prototype.$onClosing = function() {
	return this.doEventFunc("onClosing", PageListener.listenerType, null);
};

/**
 * 页面关闭后事件
 * @private
 */
PageUI.prototype.$onClosed = function() {
	this.doEventFunc("onClosed", PageListener.listenerType, null);
};

/**
 * 出错事件
 * @private
 */
PageUI.prototype.onerror = function(msg, url, line) {
	showMessageDialog(msg);
};

/**
 * 增加Tab页签
 * @private
 */
PageUI.prototype.addTab = function(tab) {
	this.tabMap.put(tab.id, tab);
};

/**
 * 获取Tab页签
 */
PageUI.prototype.getTab = function(id) {
	return this.tabMap.get(id);
};

/**
 * 增加卡片
 * @private
 */
PageUI.prototype.addCard = function(card) {
	this.cardMap.put(card.id, card);
};

/**
 * 获取卡片
 */
PageUI.prototype.getCard = function(id) {
	return this.cardMap.get(id);
};

/**
 * 增加Panel
 * @private
 */
PageUI.prototype.addPanel = function(panel) {
	this.panelMap.put(panel.id, panel);
};

/**
 * 获取Panel
 */
PageUI.prototype.getPanel = function(id) {
	return this.panelMap.get(id);
};

PageUI.prototype.removePanel = function(id) {
	this.panelMap.remove(id);
};



PageUI.prototype.recordUndo = function() {
	this.oldOperateState = this.operateState;
	this.oldBusinessState = this.businessState;
	this.oldUserState = this.userState;
};

PageUI.prototype.undo = function() {
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
 * 显示关闭页面的提示信息
 */
PageUI.prototype.showCloseConfirm = function(obj) {
	if (this.hasChanged == true) {
		if(!window.SaveAndExit){
			require("confirmdialog", function(){ConfirmDialogComp.showDialog("确定关闭窗口", PageUI.okClose,null, obj, null)});
			return false; 
		}
		else {
			require("threebuttondialog", function(){ThreeButtonsDialog.showDialog("确定关闭窗口", PageUI.okClose, null, window.SaveAndExit, ["不保存", "保存", "取消"], obj)});
			return false;
		}

	}else{
		return true;
	}
};

/**
 * 关闭dialog窗口
 */
PageUI.okClose = function(){
	this.onClosing();
	this.hide();
	this.onAfterClose();
};

/**
 * 没有提示的关闭
 */
PageUI.prototype.closeSilent = function(obj){
//	debugger;
	pageUI.hasChanged = false; 
	closeWinWithNoWarn();

};

/**
 * 关闭页面
 */
PageUI.prototype.toClose = function() {
	var result = this.$onClosing();
	if (result == false)
		return false;
	else
		this.$onClosed();
};

/**
 * 设置页面是否改变
 * @private
 */
PageUI.prototype.setChanged = function(hasChanged) {
	this.hasChanged = hasChanged;
};

/**
 * 获取页面是否改变
 */
PageUI.prototype.hasChanged = function() {
	return this.hasChanged;
};

/**
 * 页面事件监听器
 */
PageListener.listenerType = "$PAGE_LISTENER";

/**
 * @private
 */
function PageListener() {
	this.type = PageListener.listenerType;
};

PageListener.prototype.beforePageInit = function() {

};

PageListener.prototype.afterPageInit = function() {

};

// PageListener.prototype.beforeInitData = function(loader) {
//	
// };

PageListener.prototype.externalInit = function() {

};

PageListener.prototype.beforeActive = function() {

};

PageListener.prototype.onClosing = function() {

};

PageListener.prototype.onClosed = function() {

};

/**
 * 连接器对象
 * @class 连接器对象
 * @param id
 * @param source
 * @param target
 * @param signal
 * @param slot
 * @return
 */
function Connector(id, source, target, signal, slot) {
	this.id = id;
	this.source = source;
	this.target = target;
	this.signal = signal;
	this.slot = slot;
};


/**
 * 提交规则描述
 * @class 提交规则描述
 */
function SubmitRule() {
	this.widgetRuleMap = null;
	this.tabSubmit = false;
	this.cardSubmit = false;
	this.panelSubmit = false;
	this.parentSubmitRule = null;
	this.paramMap = null;
};

/**
 * 增加参数
 */
SubmitRule.prototype.addParam = function(key, value) {
	if (this.paramMap == null)
		this.paramMap = new HashMap();
	this.paramMap.put(key, value);
};

/**
 * 获取参数集合
 */
SubmitRule.prototype.getParamMap = function() {
	return this.paramMap;
};

/**
 * 设置父提交规则
 */
SubmitRule.prototype.setParentSubmitRule = function(submitRule) {
	this.parentSubmitRule = submitRule;
};

/**
 * 增加要提交的widget
 */
SubmitRule.prototype.addWidgetRule = function(id, widgetRule) {
	if (this.widgetRuleMap == null)
		this.widgetRuleMap = new HashMap;
	this.widgetRuleMap.put(id, widgetRule);
};

/**
 * 获取Widget提交规则
 */
SubmitRule.prototype.getWidgetRule = function(id) {
	if (this.widgetRuleMap == null)
		return null;
	return this.widgetRuleMap.get(id);
};

/**
 * 设置Tab页签提交规则
 */
SubmitRule.prototype.setTabSubmit = function(submit) {
	this.tabSubmit = submit;
};

/**
 * 获取Tab页签提交规则
 */
SubmitRule.prototype.getTabSubmit = function() {
	return this.tabSubmit;
};

/**
 * 设置卡片提交规则
 */
SubmitRule.prototype.setCardSubmit = function(submit) {
	this.cardSubmit = submit;
};

/**
 * 获取卡片提交规则
 */
SubmitRule.prototype.getCardSubmit = function() {
	return this.cardSubmit;
};

/**
 * 设置Panel提交规则
 */
SubmitRule.prototype.setPanelSubmit = function(submit) {
	this.panelSubmit = submit;
};

/**
 * 获取Panel提交规则
 */
SubmitRule.prototype.getPanelSubmit = function() {
	return this.panelSubmit;
};

/**
 * Widget提交规则
 * @class Widget提交规则
 * @param id
 * @return
 */
function WidgetRule(id) {
	this.id = id;
	this.dsRuleMap = null;
	this.treeRuleMap = null;
	this.gridRuleMap = null;
	
	this.tabSubmit = false;
	this.cardSubmit = false;
	this.panelSubmit = false;
};

/**
 * 增加Dataset提交规则
 */
WidgetRule.prototype.addDsRule = function(id, dsRule) {
	if (this.dsRuleMap == null)
		this.dsRuleMap = new HashMap();
	this.dsRuleMap.put(id, dsRule);
};

/**
 * 移出Dataset提交规则
 */
WidgetRule.prototype.removeDsRule = function(id) {
	if (this.dsRuleMap == null)
		return;
	this.dsRuleMap.remove(id);
};

/**
 * 设置Tab页签提交规则
 */
WidgetRule.prototype.setTabSubmit = function(submit) {
	this.tabSubmit = submit;
};

/**
 * 获取Tab页签提交规则
 */
WidgetRule.prototype.getTabSubmit = function() {
	return this.tabSubmit;
};

/**
 * 设置卡片提交规则
 */
WidgetRule.prototype.setCardSubmit = function(submit) {
	this.cardSubmit = submit;
};

/**
 * 获取卡片提交规则
 */
WidgetRule.prototype.getCardSubmit = function() {
	return this.cardSubmit;
};

/**
 * 设置Panel提交规则
 */
WidgetRule.prototype.setPanelSubmit = function(submit) {
	this.panelSubmit = submit;
};

/**
 * 获取Panel提交规则
 */
WidgetRule.prototype.getPanelSubmit = function() {
	return this.panelSubmit;
};

/**
 * 获取Widget的ID
 */
WidgetRule.prototype.getId = function() {
	return this.id;
};

/**
 * 增加Grid提交规则
 */
WidgetRule.prototype.addGridRule = function(id, grid) {
	if (this.gridRuleMap == null)
		this.gridRuleMap = new HashMap();
	this.gridRuleMap.put(id, grid);
};

/**
 * 获取Grid提交规则
 */
WidgetRule.prototype.getGridRule = function(id) {
	if (this.gridRuleMap == null)
		return null;
	return this.gridRuleMap.get(id);
};

/**
 * 增加树提交规则
 */
WidgetRule.prototype.addTreeRule = function(id, tree) {
	if (this.treeRuleMap == null)
		this.treeRuleMap = new HashMap();
	this.treeRuleMap.put(id, tree);
};

/**
 * 获取树提交规则
 */
WidgetRule.prototype.getTreeRule = function(id) {
	if (this.treeRuleMap == null)
		return null;
	return this.treeRuleMap.get(id);
};

/**
 * 获取Dataset提交规则
 */
WidgetRule.prototype.getDsRule = function(id) {
	if (this.dsRuleMap == null)
		return null;
	return this.dsRuleMap.get(id);
};
WidgetRule.prototype.getFormRule = function(id) {
	if (this.formRuleMap == null)
		return null;
	return this.formRuleMap.get(id);
};
WidgetRule.prototype.addFormRule = function(id, form) {
	if (this.formRuleMap == null)
		this.formRuleMap = new HashMap();
	this.formRuleMap.put(id, form);
};
/**
 * Dataset提交规则
 * @class Dataset提交规则
 * @param id
 * @param type
 * @return
 */
function DatasetRule(id, type) {
	this.id = id;
	this.type = type;
};

/**
 * 获取ID
 */
DatasetRule.prototype.getId = function() {
	return this.id;
};

/**
 * 获取类型
 */
DatasetRule.prototype.getType = function() {
	return this.type;
};

/**
 * 树提交规则
 * @class 树提交规则
 * @param id
 * @param type
 * @return
 */
function TreeRule(id, type) {
	this.id = id;
	this.type = type;
};

/**
 * 获取ID
 */
TreeRule.prototype.getId = function() {
	return this.id;
};

/**
 * 获取类型
 */
TreeRule.prototype.getType = function() {
	return this.type;
};

/**
 * Grid提交规则
 * @class Grid提交规则
 * @param id
 * @param type
 * @return
 */
function GridRule(id, type) {
	this.id = id;
	this.type = type;
};

/**
 * 获取ID
 */
GridRule.prototype.getId = function() {
	return this.id;
};

/**
 * 获取类型
 */
GridRule.prototype.getType = function() {
	return this.type;
};

/**
 * Form提交规则
 * @class Form提交规则
 * @param id
 * @param type
 * @return
 */
function FormRule(id, type) {
	this.id = id;
	this.type = type;
};

/**
 * 获取ID
 */
FormRule.prototype.getId = function() {
	return this.id;
};

/**
 * 获取类型
 */
FormRule.prototype.getType = function() {
	return this.type;
};

/**
 * Ajax����������
 * @class Ajax����������
 */
function ServerProxy(listener, eventName, async) {
	this.params = new HashMap();
	this.returnParams = new HashMap();
	this.returnFunc = null;
	this.submitRule = null;
	this.eventName = eventName;
	this.listener = listener;
	this.async = async;
};


ServerProxy.prototype.setAsync = function(async) {
	this.async = async;
};


ServerProxy.prototype.setParamMap = function(paramsMap) {
	this.params = paramsMap;
};

ServerProxy.prototype.addParam = function(key, value) {
	this.params.put(key, value);
};


ServerProxy.prototype.setSubmitRule = function(submitRule) {
	this.submitRule = submitRule;
};


ServerProxy.prototype.setReturnArgs = function(returnArgs) {
	this.returnArgs = returnArgs;
};


ServerProxy.prototype.setReturnFunc = function(returnFunc) {
	this.returnFunc = returnFunc;
};

ServerProxy.prototype.getSubmitRule = function() {
	var useSubmitRule = this.submitRule;
	if (useSubmitRule == null && this.listener != null) {
		useSubmitRule = this.listener[this.eventName].submitRule;
	}
	if (useSubmitRule == null)
		useSubmitRule = new SubmitRule();
	this.submitRule = useSubmitRule;
	return this.submitRule;
};

ServerProxy.prototype.getParamString = function(useSubmitRule) {
	var contextXmlArray = new Array();
	contextXmlArray.push("<" + EventContextConstant.params + ">");
	if (this.listener != null) {
		if (this.listener.source_id != null) {
			contextXmlArray.push("<" + EventContextConstant.param + ">");
			contextXmlArray.push("<" + EventContextConstant.key + ">source_id</" + EventContextConstant.key + ">");
			contextXmlArray.push("<" + EventContextConstant.value + ">" + this.listener.source_id + "</" + EventContextConstant.value + ">");
			contextXmlArray.push("</" + EventContextConstant.param + ">");
		}
		if (this.listener.listener_id != null) {
			contextXmlArray.push("<" + EventContextConstant.param + ">");
			contextXmlArray.push("<" + EventContextConstant.key + ">listener_id</" + EventContextConstant.key + ">");
			contextXmlArray.push("<" + EventContextConstant.value + ">" + this.listener.listener_id + "</" + EventContextConstant.value + ">");
			contextXmlArray.push("</" + EventContextConstant.param + ">");
		}
		if (this.listener.widget_id != null) {
			contextXmlArray.push("<" + EventContextConstant.param + ">");
			contextXmlArray.push("<" + EventContextConstant.key + ">widget_id</" + EventContextConstant.key + ">");
			contextXmlArray.push("<" + EventContextConstant.value + ">" + this.listener.widget_id + "</" + EventContextConstant.value + ">");
			contextXmlArray.push("</" + EventContextConstant.param + ">");
		}
		if (this.eventName != null) {
			contextXmlArray.push("<" + EventContextConstant.param + ">");
			contextXmlArray.push("<" + EventContextConstant.key + ">event_name</" + EventContextConstant.key + ">");
			contextXmlArray.push("<" + EventContextConstant.value + ">" + this.eventName + "</" + EventContextConstant.value + ">");
			contextXmlArray.push("</" + EventContextConstant.param + ">");
		}
		if (this.listener.source_type != null) {
			contextXmlArray.push("<" + EventContextConstant.param + ">");
			contextXmlArray.push("<" + EventContextConstant.key + ">source_type</" + EventContextConstant.key + ">");
			contextXmlArray.push("<" + EventContextConstant.value + ">" + this.listener.source_type + "</" + EventContextConstant.value + ">");
			contextXmlArray.push("</" + EventContextConstant.param + ">");
		}
		if (this.listener.parent_source_id != null) {
			contextXmlArray.push("<" + EventContextConstant.param + ">");
			contextXmlArray.push("<" + EventContextConstant.key + ">parent_source_id</" + EventContextConstant.key + ">");
			contextXmlArray.push("<" + EventContextConstant.value + ">" + this.listener.parent_source_id
					+ "</" + EventContextConstant.value + ">");
			contextXmlArray.push("</" + EventContextConstant.param + ">");
		}
	}
	if (this.params != null && this.params.size() > 0) {
		var keySet = this.params.keySet();
		var size = this.params.size();
		for ( var i = 0; i < size; i++) {
			var key = keySet[i];
			var value = this.params.get(key);
			if (value == null)
				value = "";
			contextXmlArray.push("<" + EventContextConstant.param + ">");
			contextXmlArray.push("<" + EventContextConstant.key + ">" + key + "</" + EventContextConstant.key + ">");
			contextXmlArray.push("<" + EventContextConstant.value + ">" + value + "</" + EventContextConstant.value + ">");
			contextXmlArray.push("</" + EventContextConstant.param + ">");
		}
	}
	if (pageUI.hasChanged != null) {
		contextXmlArray.push("<" + EventContextConstant.param + ">");
		contextXmlArray.push("<" + EventContextConstant.key + ">hasChanged</" + EventContextConstant.key + ">");
		contextXmlArray.push("<" + EventContextConstant.value + ">" + pageUI.hasChanged
				+ "</" + EventContextConstant.value + ">");
		contextXmlArray.push("</" + EventContextConstant.param + ">");
	}
	

	var paramMap = useSubmitRule.getParamMap();
	if (paramMap != null) {
		var keys = paramMap.keySet();
		for ( var i = 0; i < keys.length; i++) {
			var key = keys[i];
			var value = paramMap.get(key);
			if (value == null)
				value = "";
			value = getParameter(value);
			if (value == null)
				value = getSessionAttribute(value);
			if (value == null)
				value = paramMap.get(key);
			contextXmlArray.push("<" + EventContextConstant.param + ">");
			contextXmlArray.push("<" + EventContextConstant.key + ">" + key + "</" + EventContextConstant.key + ">");
			contextXmlArray.push("<" + EventContextConstant.value + ">" + value + "</" + EventContextConstant.value + ">");
			contextXmlArray.push("</" + EventContextConstant.param + ">");
		}
	}

	contextXmlArray.push("</" + EventContextConstant.params + ">");
	return contextXmlArray.join("");
};

ServerProxy.prototype.getPageCtxStr = function(useSubmitRule) {
	var contextXmlArray = new Array;
	contextXmlArray.push(pageUI.getContext(useSubmitRule));
	return contextXmlArray.join("");
};

ServerProxy.prototype.getParentPageCtxStr = function(useSubmitRule) {
	if (useSubmitRule != null && useSubmitRule.parentSubmitRule != null) {
		var contextXmlArray = new Array;
		contextXmlArray.push("<" + EventContextConstant.parentcontext + ">");
		contextXmlArray.push("<" + EventContextConstant.eventcontext + " id='" + getPopParent().getPageId() + "'>");
		contextXmlArray.push(getPopParent().pageUI.getContext(useSubmitRule.parentSubmitRule));
		contextXmlArray.push("</" + EventContextConstant.eventcontext + ">");
		//contextXmlArray.push(pcontext);
		// contextXmlArray.push(encodeURIComponent(pcontext));
		contextXmlArray.push("</" + EventContextConstant.parentcontext + ">");
		return contextXmlArray.join("");
	}
	return "";
};
ServerProxy.prototype.execute = function() {
	if (ServerProxy.PROXYRETURN_EXECUTING > 0){
		ServerProxy.PROXY_ARRAY.push(this);		
		setTimeout("ServerProxy.execServerProxyList()",100);
		return;
	} 
	ServerProxy.PROXYRETURN_EXECUTING = ServerProxy.PROXYRETURN_EXECUTING + 1;
	var useSubmitRule = this.getSubmitRule();
	
	var contextXmlArray = new Array;
	contextXmlArray.push("<root><" + EventContextConstant.eventcontext + " id=\"" + getPageId() + "\">");
	contextXmlArray.push(this.getParamString(useSubmitRule));
	contextXmlArray.push(this.getPageCtxStr(useSubmitRule));
	contextXmlArray.push("</" + EventContextConstant.eventcontext + ">");
	contextXmlArray.push(this.getParentPageCtxStr(useSubmitRule));
	contextXmlArray.push("</root>");
	var contextXml = contextXmlArray.join("");
	contextXml = encodeURIComponent(contextXml);
	
	var innerArgsList = [[this.returnArgs, this.returnFunc]];
	ServerProxy.proxyCall(contextXml, innerArgsList, this.async);
};

ServerProxy.proxyCall = function(contextXml, innerArgsList, async) {
	var requestXml = "type=processEvent&xml=";
	requestXml += contextXml;
	var ajax = new Ajax();
	ajax.setPath(getCorePath());
	ajax.setQueryStr(requestXml);
	ajax.setReturnFunc(ServerProxy.$returnFun);
	innerArgsList.push(ajax);
	ajax.setReturnArgs(innerArgsList);
	return ajax.post(async == null ? true : async);
};

/**
 * Ajax返回后调用方法
 * @private
 */
ServerProxy.$returnFun = function(xmlHttpReq, returnArgsList, exception) {
	var execList = [];
	try{
		var ajaxObj = returnArgsList[returnArgsList.length - 1];
		if (handleException(xmlHttpReq, exception, returnArgsList, ajaxObj)) {
			//TODO 这个参数需要斟酌 
			var userArgs = returnArgsList[0][0];
			var doc = xmlHttpReq.responseXML;
			var rootNode = doc.documentElement;
			var contentsNode = rootNode.selectSingleNode("contents");
			var contentNodes = contentsNode.selectNodes("content");
			var result = true;
			if (contentNodes != null) {
				for ( var i = 0, count = contentNodes.length; i < count; i++) {
					var content = getNodeValue(contentNodes[i]);
					content = content.replaceAll("<CD>", "<![CDATA[");
					content = content.replaceAll("</CD>", "]]>");
//					content = decodeURIComponent(content);
					var resultDoc = createXmlDom(content);
					var eventContextNode = resultDoc.documentElement;
					content = ServerProxy.$updateCtx(eventContextNode, userArgs, false);
					
					var afterExecNode = eventContextNode.selectSingleNode("afterExec");
					if (afterExecNode != null) {
						var script = getNodeValue(afterExecNode);
						if (script != null && script != "") {
							execList.push(script);
						}
					}
				}
			}
			for(var i = 0; i < returnArgsList.length - 1; i ++){
				var args = returnArgsList[i];
				var returnFunc = args[1];
				var userArg = args[0];
				if (returnFunc != null)
					returnFunc.call(this, userArg, true);
			}
			hideDefaultLoadingBar();
			return result;
		}
		hideDefaultLoadingBar();
	}
	finally{
		ServerProxy.PROXYRETURN_EXECUTING = ServerProxy.PROXYRETURN_EXECUTING - 1;
		//由于IE 9在执行了div删除之后，会销毁全局变量，需将这段逻辑挪到后面处理
		for(var i = 0; i < execList.length; i ++){
			eval(execList[i]);
		}
		adjustContainerFramesHeight();
	}
};

ServerProxy.wrapProxy = function(proxy){
	if(ServerProxy.suspend){
		ServerProxy.PROXY_ARRAY.push(proxy);
	}
	else{
		ServerProxy.cleanProxy();
	}
};

ServerProxy.cleanProxy = function(){
	ServerProxy.suspend = false;
	ServerProxy.execServerProxyList();
};

/**
 * 如果有请求正在执行返回逻辑，延迟发送新请求
 * 
 */
ServerProxy.execServerProxyList = function(){
	if (ServerProxy.PROXYLIST_EXECUTING) {
		setTimeout("ServerProxy.execServerProxyList()",100);
		return;
	}
	ServerProxy.PROXYLIST_EXECUTING = true;
	try{
//		var count = ServerProxy.PROXY_ARRAY.length;
//		for (var i = 0 ; i< count; i++){
//			var serverProxy = ServerProxy.PROXY_ARRAY[0];
//			ServerProxy.PROXY_ARRAY.shift();
//			serverProxy.execute();
//		}
		ServerProxy.aggregateProxyRequest();
	}
	finally{
		ServerProxy.PROXYLIST_EXECUTING = false;	
	}
};

ServerProxy.aggregateProxyRequest = function(){
	if (ServerProxy.PROXYRETURN_EXECUTING > 0){
		setTimeout("ServerProxy.execServerProxyList()",100);
		return;
	} 
	ServerProxy.PROXYRETURN_EXECUTING = ServerProxy.PROXYRETURN_EXECUTING + 1;
	var count = ServerProxy.PROXY_ARRAY.length;
	if(count == 0){
		ServerProxy.PROXYRETURN_EXECUTING = ServerProxy.PROXYRETURN_EXECUTING - 1;
		return;
	}
	var submitRules = [];
	var paramList = [];
	var innerArgsList = [];
	for (var i = 0; i < count; i++){
		var proxy = ServerProxy.PROXY_ARRAY[i];
		var rule = proxy.getSubmitRule();
		submitRules.push(rule);
		paramList[i] = proxy.getParamString(rule);
		var arr = [];
		arr.push(proxy.returnArgs);
		arr.push(proxy.returnFunc);
		innerArgsList[i] = arr;
	}
	
	var useSubmitRule = ServerProxy.mergeSubmitRules(submitRules);
	var contextXmlArray = new Array;
	contextXmlArray.push("<root><" + EventContextConstant.eventcontext + " id=\"" + getPageId() + "\">");
	contextXmlArray.push("<" + EventContextConstant.groupparams + ">");
	for(var i = 0; i < count; i ++){
		contextXmlArray.push(paramList[i]);
	}
	contextXmlArray.push("</" + EventContextConstant.groupparams + ">");
	contextXmlArray.push(pageUI.getContext(useSubmitRule));
	contextXmlArray.push("</" + EventContextConstant.eventcontext + ">");
	
	if (useSubmitRule != null && useSubmitRule.parentSubmitRule != null) {
		contextXmlArray.push("<" + EventContextConstant.parentcontext + ">");
		contextXmlArray.push("<" + EventContextConstant.eventcontext + " id='" + getPopParent().getPageId() + "'>");
		contextXmlArray.push(getPopParent().pageUI.getContext(useSubmitRule.parentSubmitRule));
		contextXmlArray.push("</" + EventContextConstant.eventcontext + ">");
		//contextXmlArray.push(pcontext);
		// contextXmlArray.push(encodeURIComponent(pcontext));
		contextXmlArray.push("</" + EventContextConstant.parentcontext + ">");
	}
	contextXmlArray.push("</root>");
	var contextXml = contextXmlArray.join("");
	contextXml = encodeURIComponent(contextXml);
	
	showDefaultLoadingBar();
	ServerProxy.PROXY_ARRAY = [];
	ServerProxy.proxyCall(contextXml, innerArgsList, true);
};

/**
 * 合并提交规则，参数不合并
 * 
 * @param {} rules
 * @return {}
 */
ServerProxy.mergeSubmitRules = function(rules){
//	for (var i = 0; i < rules.length; i++){
//		if (rules[i].parentSubmitRule != null)
//			return rules[i]; 
//	}
//	return rules[0];
//
	var submitRule = new SubmitRule();
	for (var i = 0; i < rules.length; i++){
		ServerProxy.mergeSubmitRule(submitRule, rules[i]);
	}
	return submitRule;
};

ServerProxy.mergeSubmitRule = function(targetSubmitRule, sourceSubmitRule){
	if (sourceSubmitRule == null) return;
	if (sourceSubmitRule.tabSubmit == true)
		targetSubmitRule.tabSubmit = true;
	if (sourceSubmitRule.cardSubmit == true)
		targetSubmitRule.cardSubmit = true;
	if (sourceSubmitRule.panelSubmit == true)
		targetSubmitRule.panelSubmit = true;
	if (targetSubmitRule.widgetRuleMap == null)
		targetSubmitRule.widgetRuleMap = sourceSubmitRule.widgetRuleMap;
	else if (sourceSubmitRule.widgetRuleMap != null) {
		var keys = sourceSubmitRule.widgetRuleMap.keySet();
		if (keys != null) {
			for ( var i = 0; i < keys.length; i++) {
				var sourceWidgetRule = sourceSubmitRule.widgetRuleMap.get(keys[i]);
				var targetWidgetRule = targetSubmitRule.widgetRuleMap.get(keys[i]);
				if (targetWidgetRule != null)
					ServerProxy.mergeWidgetRule(targetWidgetRule, sourceWidgetRule);
				else
					targetSubmitRule.addWidgetRule(sourceWidgetRule.getId(), sourceWidgetRule);
			}	
		}
	}
	if (targetSubmitRule.parentSubmitRule == null)
		targetSubmitRule.parentSubmitRule = sourceSubmitRule.parentSubmitRule;
	else
		ServerProxy.mergeSubmitRule(targetSubmitRule.parentSubmitRule,sourceSubmitRule.parentSubmitRule);
};

/**
 * 合并widget提交规则
 * 
 * @param {} targetWidgetRule
 * @param {} sourceWidgetRule
 */
ServerProxy.mergeWidgetRule = function(targetWidgetRule, sourceWidgetRule){
	if (sourceWidgetRule == null) return;
	if (sourceWidgetRule.tabSubmit == true)
		targetWidgetRule.tabSubmit = true;
	if (sourceWidgetRule.cardSubmit == true)
		targetWidgetRule.cardSubmit = true;
	if (sourceWidgetRule.panelSubmit == true)
		targetWidgetRule.panelSubmit = true;
	if (targetWidgetRule.dsRuleMap == null)
		targetWidgetRule.dsRuleMap = sourceWidgetRule.dsRuleMap;
	else if (sourceWidgetRule.dsRuleMap != null) {
		var keys = sourceWidgetRule.dsRuleMap.keySet();
		if (keys != null) {
			for ( var i = 0; i < keys.length; i++) {
				var sourceDsRule = sourceWidgetRule.dsRuleMap.get(keys[i]);
				var targetDsRule = targetWidgetRule.dsRuleMap.get(keys[i]);
				if (targetDsRule != null)
					ServerProxy.mergeDsRule(targetDsRule, sourceDsRule);
				else
					targetWidgetRule.addDsRule(sourceDsRule.getId(), sourceDsRule);
			}	
		}
	}	
	if (targetWidgetRule.treeRuleMap == null)
		targetWidgetRule.treeRuleMap = sourceWidgetRule.treeRuleMap;
	else if (sourceWidgetRule.treeRuleMap != null) {
		var keys = sourceWidgetRule.treeRuleMap.keySet();
		if (keys != null) {
			for ( var i = 0; i < keys.length; i++) {
				var sourceTreeRule = sourceWidgetRule.treeRuleMap.get(keys[i]);
				var targetTreeRule = targetWidgetRule.treeRuleMap.get(keys[i]);
				if (targetTreeRule != null)
					ServerProxy.mergeTreeRule(targetTreeRule, sourceTreeRule);
				else
					targetWidgetRule.addTreeRule(sourceTreeRule.getId(), sourceTreeRule);
			}	
		}
	}	
	if (targetWidgetRule.gridRuleMap == null)
		targetWidgetRule.gridRuleMap = sourceWidgetRule.gridRuleMap;
	else if (sourceWidgetRule.gridRuleMap != null) {
		var keys = sourceWidgetRule.gridRuleMap.keySet();
		if (keys != null) {
			for ( var i = 0; i < keys.length; i++) {
				var sourceGridRule = sourceWidgetRule.gridRuleMap.get(keys[i]);
				var targetGridRule = targetWidgetRule.gridRuleMap.get(keys[i]);
				if (targetGridRule != null)
					ServerProxy.mergeGridRule(targetGridRule, sourceGridRule);
				else
					targetWidgetRule.addGridRule(sourceGridRule.getId(), sourceGridRule);
			}	
		}
	}	
	if (targetWidgetRule.formRuleMap == null)
		targetWidgetRule.formRuleMap = sourceWidgetRule.formRuleMap;
	else if (sourceWidgetRule.formRuleMap != null) {
		var keys = sourceWidgetRule.formRuleMap.keySet();
		if (keys != null) {
			for ( var i = 0; i < keys.length; i++) {
				var sourceFormRule = sourceWidgetRule.formRuleMap.get(keys[i]);
				var targetFormRule = targetWidgetRule.formRuleMap.get(keys[i]);
				if (targetFormRule != null)
					ServerProxy.mergeFormRule(targetFormRule, sourceFormRule);
				else
					targetWidgetRule.addFormRule(sourceFormRule.getId(), sourceFormRule);
			}	
		}
	}	
}; 

/**
 * 合并ds提交规则
 * 
 * @param {} targetDsRule
 * @param {} sourceDsRule
 */
ServerProxy.mergeDsRule = function(targetDsRule, sourceDsRule){
	var targetRuleValue = ServerProxy.getDsRuleValue(targetDsRule.type); 
	var sourceRuleValue = ServerProxy.getDsRuleValue(sourceDsRule.type);
	if (targetRuleValue < sourceRuleValue)
		targetDsRule.type = sourceDsRule.type;
	else if (targetRuleValue ==  sourceRuleValue &&  targetDsRule.type != sourceDsRule.type)
		targetDsRule.type = "ds_all_line"; 
};

ServerProxy.getDsRuleValue = function (type){
	if (type = "ds_current_line")
		return 0;
	else if (type = "ds_current_key")
		return 1;
	else if (type = "ds_current_page")
		return 1;
	else if (type = "ds_all_sel_line")
		return 1;
	else if (type = "ds_all_line")
		return 2;
	else return -1;	
};

/**
 * 合并tree提交规则
 * 
 * @param {} targetTreeRule
 * @param {} sourceTreeRule
 */
ServerProxy.mergeTreeRule = function(targetTreeRule, sourceTreeRule){
	var targetRuleValue = ServerProxy.getTreeRuleValue(targetTreeRule.type); 
	var sourceRuleValue = ServerProxy.getTreeRuleValue(sourceTreeRule.type);
	if (targetRuleValue < sourceRuleValue)
		targetTreeRule.type = sourceTreeRule.type;
};

ServerProxy.getTreeRuleValue = function (type){
	if (type = "tree_current_parent")
		return 0;
	else if (type = "tree_current_parent_children")
		return 1;
	else if (type = "tree_all")
		return 2;
	else return -1;	
};

/**
 * 合并grid提交规则
 * 
 * @param {} targetGridRule
 * @param {} sourceGridRule
 */
ServerProxy.mergeGridRule = function(targetGridRule, sourceGridRule){
	var targetRuleValue = ServerProxy.getGridRuleValue(targetGridRule.type); 
	var sourceRuleValue = ServerProxy.getGridRuleValue(sourceGridRule.type);
	if (targetRuleValue < sourceRuleValue)
		targetGridRule.type = sourceGridRule.type;
};

ServerProxy.getGridRuleValue = function (type){
	if (type = "grid_current_row")
		return 0;
	else if (type = "grid_all_row")
		return 1;
	else return -1;	
};

/**
 * 合并form提交规则
 * 
 * @param {} targetFormRule
 * @param {} sourceFormRule
 */
ServerProxy.mergeFormRule = function(targetFormRule, sourceFormRule){
	var targetRuleValue = ServerProxy.getFormRuleValue(targetFormRule.type); 
	var sourceRuleValue = ServerProxy.getFormRuleValue(sourceFormRule.type);
	if (targetRuleValue < sourceRuleValue)
		targetFormRule.type = sourceFormRule.type;
};

ServerProxy.getFormRuleValue = function (type){
	if (type = "no_child")
		return 0;
	else if (type = "all_child")
		return 1;
	else return -1;	
};


PageUI.prototype.handleHotKey = function(key) {
	var widgets = this.widgetMap.values();
	if (widgets != null && widgets.length > 0) {
		for ( var i = 0; i < widgets.length; i++) {
			var widget = widgets[i];
			// 匹配Widget的Toolbar和Button
			var comps = widget.getComponents();
			for ( var j = 0, m = comps.length; j < m; j++) {
				var comp = comps[j];
				if (comp.componentType == "TOOLBAR"
						|| comp.componentType == "BUTTON") {
					var obj = comp.handleHotKey(key);
					if (obj != null)
						return obj;
				}
			}
			// 匹配Widget的Menubar和ContextMenu
			var menus = widget.getMenus();
			for ( var j = 0, m = menus.length; j < m; j++) {
				var menu = menus[j];
				var obj = menu.handleHotKey(key);
				if (obj != null)
					return obj;
			}
		}
	}
	return null;

};

ServerProxy.$updateCtx = function(eventContextNode, userArgs, isParent) {
	var nowUI = pageUI;
	if (isParent){
		nowUI = getPopParent().pageUI;
	}
	var result = true;
	var resultNode = eventContextNode.selectSingleNode("result");
	if (resultNode != null) {
		var rv = getNodeValue(resultNode);
		if (rv == "false")
			result = false;
	}

	var beforeExecNode = eventContextNode.selectSingleNode("beforeExec");
	if (beforeExecNode != null) {
		var script = getNodeValue(beforeExecNode);
		if (script != null && script != "") {
			//script = decodeURIComponent(script);
			eval(script);
		}
	}

	var attrNodes = eventContextNode.selectSingleNode(EventContextConstant.attributes);
	if (attrNodes != null) {
		var attrs = attrNodes.selectNodes(EventContextConstant.attribute);
		if (attrs != null && attrs.length > 0) {
			for ( var i = 0; i < attrs.length; i++) {
				var node = attrs[i];
				var keyNode = node.selectSingleNode(EventContextConstant.key);
				var valueNode = node.selectSingleNode(EventContextConstant.value);
				var key = getNodeValue(keyNode);
				var value = getNodeValue(valueNode);
				setSessionAttribute(key, value);
			}
		}
	}

	var pagemeta = eventContextNode.selectSingleNode("pagemeta");
	ServerProxy.$processContext(pagemeta, nowUI);

	var pcards = eventContextNode.selectNodes("card");
	if (pcards.length > 0) {
		for ( var i = 0; i < pcards.length; i++) {
			var child = pcards[i];
			var layoutId = getNodeAttribute(child, "id");
			var layout = nowUI.cardMap.get(layoutId);
			ServerProxy.$processContext(child, layout);
		}
	}
	var ptabs = eventContextNode.selectNodes("tab");
	if (ptabs.length > 0) {
		for ( var i = 0; i < ptabs.length; i++) {
			var child = ptabs[i];
			var layoutId = getNodeAttribute(child, "id");
			var layout = nowUI.tabMap.get(layoutId);
			ServerProxy.$processContext(child, layout);
		}
	}
	var pPanels = eventContextNode.selectNodes("panel");
	if (pPanels.length > 0) {
		for ( var i = 0; i < pPanels.length; i++) {
			var child = pPanels[i];
			var layoutId = getNodeAttribute(child, "id");
			var layout = nowUI.panelMap.get(layoutId);
			ServerProxy.$processContext(child, layout);
		}
	}
	var pmenubars = eventContextNode.selectNodes("menubar");
	if (pmenubars.length > 0) {
		for ( var i = 0; i < pmenubars.length; i++) {
			var child = pmenubars[i];
			var menubarId = getNodeAttribute(child, "id");
//			var menubar = nowUI.menubarMap.get(menubarId);
			var menubar = nowUI.getMenubar(menubarId);
			ServerProxy.$processContext(child, menubar);
		}
	}

	var widgetNodes = pagemeta.selectNodes("widget");
	if (widgetNodes != null) {
		for ( var k = 0; k < widgetNodes.length; k++) {
			var widgetNode = widgetNodes[k];
			var widgetId = getNodeAttribute(widgetNode, "id");
			var widget = nowUI.getWidget(widgetId);
			if (widget == null)
				continue;
			ServerProxy.$processContext(widgetNode, widget);
			var childNodes = widgetNode.childNodes;
			if (childNodes != null) {
				for ( var j = 0; j < childNodes.length; j++) {
					var child = childNodes[j];
					if (!IS_IE) {
						if (child.nodeName == "#text")
							continue;
					}
					if (child.nodeName == EventContextConstant.context)
						continue;

					if (child.nodeName == "card") {
						var layoutId = getNodeAttribute(child, "id");
						var layout = widget.cardMap.get(layoutId);
						ServerProxy.$processContext(child, layout);
					} else if (child.nodeName == "tab") {
						var layoutId = getNodeAttribute(child, "id");
						var layout = widget.tabMap.get(layoutId);
						ServerProxy.$processContext(child, layout);
					} else if (child.nodeName == "panel") {
						var layoutId = getNodeAttribute(child, "id");
						var layout = widget.panelMap.get(layoutId);
						ServerProxy.$processContext(child, layout);
					}
//					else if (child.nodeName == "comp") {
//						var compId = getNodeAttribute(child, "id");
//						var comp = widget.getComponent(compId);
//						ServerProxy.$processContext(child, comp);
//					}
				}
				for ( var j = 0; j < childNodes.length; j++) {
					var child = childNodes[j];
					if (!IS_IE) {
						if (child.nodeName == "#text")
							continue;
					}
					if (child.nodeName == EventContextConstant.context)
						continue;
					if (child.nodeName == "dataset") {
						var dsId = getNodeAttribute(child, "id");
						var ds = widget.getDataset(dsId);
						if (ds == null)
							continue;
						var data = child.selectSingleNode("data");
						var dataXml = getNodeValue(data);
						ds.setData(dataXml, userArgs);
					}
				}
				for ( var j = 0; j < childNodes.length; j++) {
					var child = childNodes[j];
					if (!IS_IE) {
						if (child.nodeName == "#text")
							continue;
					}
					if (child.nodeName == EventContextConstant.context)
						continue;
					if (child.nodeName == "contextmenu") {
						var menuId = getNodeAttribute(child, "id");
						var menu = widget.getMenu(menuId);
						ServerProxy.$processContext(child, menu);
					} else if (child.nodeName == "menubar") {
						var menubarId = getNodeAttribute(child, "id");
						// TODO ?????????
						var menubar = widget.getMenu(menubarId);
						if (null == menubar)
							menubar = nowUI.getMenubar(menubarId);
						ServerProxy.$processContext(child, menubar);
					} else {
						var compId = getNodeAttribute(child, "id");
						var comp = widget.getComponent(compId);
						ServerProxy.$processContext(child, comp);
					}
				}
			}
		}
	}

	var execNode = eventContextNode.selectSingleNode("exec");
	if (execNode != null) {
		var script = getNodeValue(execNode);
		if (script != null && script != "") {
			eval(script);
		}
	}

	var pctx = eventContextNode.selectSingleNode(EventContextConstant.parentcontext);
	if (pctx != null) {
		var pctxResult = pctx.selectSingleNode(EventContextConstant.eventcontext);
		ServerProxy.$updateCtx(pctxResult, null, true);
	}
	
	return result;

};


ServerProxy.$processContext = function(node, source) {
	if (source == null)
		return;
	var contextNode = node.selectSingleNode(EventContextConstant.context);
	if (contextNode == null)
		return;
	var contextStr = getNodeValue(contextNode);
	if (contextStr != null && contextStr != "") {
		eval("var context = " + contextStr);
		source.setContext(context);
	}
};


/**
 *触发plugout 
 * 
 * @param {} widgetId
 * @param {} plugoutId
 * @param {} submitRule 提交规则
 */
function triggerPlugout_bak(submitRule, paramMap){
//	 var plugout = pageUI.getWidget(widgetId).getPlugout(id);
//	 if (plugout == null) return;
//	 var items = plugout.getItems();
//	 if (items == null) return;
//	 for (var i = 0 ; i < items.lenght ; i++){
	var proxy = new ServerProxy();
	if (paramMap){
		var paramToJson = toJSON(paramMap);
		submitRule.addParam('plugparams', paramToJson);
	}
	proxy.submitRule = submitRule;
//	proxy.addParam(paramMap);
	proxy.execute();
};

/**
 *触发plugout 
 * 
 */
function triggerPlugout(widgetId, plugoutId){
	var plugout = pageUI.getWidget(widgetId).getPlugOut(plugoutId);
	if (plugout == null) return; 
	var proxy = new ServerProxy();
	if (plugout.submitRule != null)
		proxy.submitRule = plugout.submitRule;
	proxy.execute();
}




/**
 * 响应快捷键（只响应第一个匹配子项的点击事件）
 * @private
 */
//PageUI.prototype.handleHotKey = function(key) {
//	return null;
//
//};
