BetweenPanel.prototype = new ListenerUtil;

/**
* 查询模板专用的between关系的控件对
* @author dengjt
*/
function BetweenPanel(parent, name, left, top, width, height, position, type, userobj) {
	this.parent = parent;
	this.id = name;
	this.left = getInteger(left, 0);
	this.top = getInteger(top, 0);
	this.width = getString(width, "100");
	this.height = getString(height, "20");
	this.position = getString(position, "absolute");
	this.type = type;
	this.userobj = userobj;
	ListenerUtil.call(this, true);
	this.create();
};

BetweenPanel.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.owner = this;
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left;
	this.Div_gen.style.top = this.top;
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = this.height;
	this.parent.appendChild(this.Div_gen);
	
	this.table = $ce("TABLE");
	this.table.style.width = "100%";
	this.table.style.height = "100%";
	this.table.cellSpacing = "0";
	this.table.cellPadding = "0";
	var tbody = $ce("TBODY");
	this.table.appendChild(tbody);
	var row = this.table.insertRow(-1);
	var cell1 = row.insertCell(-1);
	var spaceCell = row.insertCell(-1);
	spaceCell.style.width = "10";
	var cell2 = row.insertCell(-1);
	this.Div_gen.appendChild(this.table);
	this.createPairComp(cell1, cell2);
};

BetweenPanel.prototype.createPairComp = function(cell1, cell2) {
	this.smallValueComp = null;
	this.bigValueComp = null;
	if (this.type == EditorType.REFERENCE) {	
		//eval("var obj = " + dsRow.getCellValue(7));
		this.smallValueComp = new ReferenceTextComp(cell1, "smallValueComp", 0, 0, "112", "relative", null, this.userobj);
		this.smallValueComp.beforeOpenRefDialog = function() {
			var ds = getDataset("queryConditionDataset");
			ds.setRowSelected(ds.getRowIndex(this.relationRow));
		}
		
		this.bigValueComp = new ReferenceTextComp(cell2, "bigValueComp", 45, 0, "112", "relative", null, this.userobj);
		this.bigValueComp.beforeOpenRefDialog = function() {
			var ds = getDataset("queryConditionDataset");
			ds.setRowSelected(ds.getRowIndex(this.relationRow));
		}
	}
	else if(this.type == EditorType.COMBOBOX) {
		var comboData = this.userobj;
		this.smallValueComp = new ComboComp(cell1, "smallvalueComp", 0, 0, "112", "relative", true, {"readOnly": false, "dataDivHeight": 60}, null);
		this.bigValueComp = new ComboComp(cell2, "bigvalueComp", 0, 0, "112", "relative", true, {"readOnly": false, "dataDivHeight": 60}, null);
		valueComp.setComboData(comboData);
	}
	else if(this.type == EditorType.DATETEXT) {
		this.smallValueComp = new DateTextComp(cell1, "smallValueComp", 0, 0, "112", "relative", null);
		this.bigValueComp = new DateTextComp(cell2, "bigValueComp", 0, 0, "112", "relative", null);
	}
	else {
	    this.smallValueComp = new StringTextComp(cell1, "smallValueComp", 0, 0, "112", "relative", null, null);
	    this.bigValueComp = new StringTextComp(cell2, "bigValueComp", 0, 0, "112", "relative", null, null);
	}
	var oThis = this;
	this.smallValueComp.onblur = function(e) {
		e = EventUtil.getEvent();
		oThis.onblur(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	this.bigValueComp.onblur = function(e) {
		e = EventUtil.getEvent();
		oThis.onblur(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	this.smallValueComp.onFocus = function(e) {
		e = EventUtil.getEvent();
		oThis.onfocus(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
	this.bigValueComp.onFocus = function(e) {
		e = EventUtil.getEvent();
		oThis.onfocus(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};	
};

BetweenPanel.prototype.getValue = function() {
	return this.smallValueComp.getValue() + "," + this.bigValueComp.getValue();
};

BetweenPanel.prototype.setValue = function(value) {
	if (value == null)
		return;
	var valueArr = value.split(",");
	this.smallValueComp.setValue(valueArr[0]);
	if (valueArr[1] == null)
		valueArr[1] = valueArr[0];
	this.bigValueComp.setValue(valueArr[1]);
};

BetweenPanel.prototype.onblur = function(e) {
	this.setFocusStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onBlur", FocusListener.listenerType, focusEvent);
};

BetweenPanel.prototype.onfocus = function(e) {
	this.setFocusStyle();
	var focusEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onFocus", FocusListener.listenerType, focusEvent);
};

BetweenPanel.prototype.destroySelf = function() {
	this.smallValueComp.destroySelf();
	this.bigValueComp.destroySelf();
	this.Div_gen.parentNode.removeChild(this.Div_gen);
    this.eventMap = null;
};