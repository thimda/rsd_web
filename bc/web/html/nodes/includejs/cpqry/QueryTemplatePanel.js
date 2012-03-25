QueryTemplatePanel.prototype = new BaseComponent;
QueryTemplatePanel.prototype.componentType = "QUERY_TEMPLATE_PANEL";

function QueryTemplatePanel(parent, name, left, top, width, height, position)
{
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	//this.overflow = "hidden";
	this.dataset = null;
	this.create();
}

QueryTemplatePanel.prototype.setIfMust = function(ifMust) {
	this.ifMust = ifMust;
};

QueryTemplatePanel.prototype.create = function()
{
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.id;
	this.Div_gen.style.left = this.left;
	this.Div_gen.style.top = this.top;
	this.Div_gen.style.width = this.width;
	//this.Div_gen.style.height = this.height;
	this.Div_gen.style.position = this.position;
	//this.Div_gen.style.overflow = this.overflow;
	if(this.parentOwner)
		this.placeIn(this.parentOwner);
};

QueryTemplatePanel.prototype.setDataset = function(ds)
{
	this.dataset = ds;
	this.dataset.bindComponent(this);
	this.table = $ce("TABLE");
	//this.table.border = "1";
	this.table.style.width = "100%";
	var tbody = $ce("TBODY");
	this.table.appendChild(tbody);
	this.Div_gen.appendChild(this.table);
	
	var rows = this.dataset.getRows();
	if(rows != null){
		for(var i = 0; i < rows.length; i ++){
			if(this.ifMust){
				var fieldType = rows[i].getCellValue(9);
				var logicType = rows[i].getCellValue(14);
				if(fieldType != 0 && fieldType != 1 && logicType != 1)
					continue;				
			}
			this.createElement(rows[i]);
		}
	}
};

QueryTemplatePanel.prototype.onModelChanged = function(event)
{ 
	if(this.ifMust)
		return;
	if(RowSelectEvent.prototype.isPrototypeOf(event))
	{
	}
	else if(DataChangeEvent.prototype.isPrototypeOf(event))
	{
		this.modifyElement(event.currentRow, event.cellRowIndex, event.cellColIndex, event.currentValue, event.oldValue);
	}
//	else if(DatasetUndoEvent.prototype.isPrototypeOf(event))
//	{
//		while(this.table.rows.length > 0)
//			this.table.deleteRow(0);
//		var rows = this.dataset.getRows();
//		if(rows != null){
//			for(var i = 0; i < rows.length; i ++)
//				this.createElement(rows[i]);
//		}
//	}
	
	else if(RowInsertEvent.prototype.isPrototypeOf(event))
	{
		this.createElement(event.insertedRows[0]);
	}
	
	else if(RowDeleteEvent.prototype.isPrototypeOf(event))
	{
		this.removeElement(event.deletedRows[0], event.deletedIndices[0]);
	}
	
	else if (PageChangeEvent.prototype.isPrototypeOf(event))
	{
		this.removeAllElement();
		var rows = this.dataset.getRows();
		if(rows != null){
			for(var i = 0; i < rows.length; i ++)
				this.createElement(rows[i]);
		}
	}
};

QueryTemplatePanel.prototype.createElement = function(dsRow)
{	
	if(dsRow.getCellValue(1) == "$#$")
		return;
	var oThis = this;
	var row = this.table.insertRow(-1);
	row.height = "23";
	
	var cell = row.insertCell(-1);
	cell.width = "20";
	var fieldType = dsRow.getCellValue(9);
	var logicType = dsRow.getCellValue(14);
	var editable = fieldType != 1;
	if((fieldType != 0 && fieldType != 1) && logicType != "1"){
		var img = new ImageComp(cell, "img", window.themeGlobalPath + "/themes/" + window.themeId + "/images/querytemplate/remove.png", 0, 0, "16", "16", "删除条件", null, {position:"relative"});
		img.relationRow = dsRow;
		img.onclick = function()
		{  
			var ds = oThis.dataset;
			var deleteRow = this.relationRow; 
			templateRowDelete(deleteRow);
		}
	}
	cell = row.insertCell(-1);
	cell.width = "150";
	var divStr = "<div style='width:100%;height:100%;border-bottom-width:1px;border-bottom-color:#7f9db9;border-bottom-style:solid;line-height:23px;'></div>";
	cell.innerHTML = divStr;
	if (!IS_IE)
		var label = new LabelComp(cell.firstChild, "label", 0, 0, dsRow.getCellValue(1), "relative", null);
	else
		var label = new LabelComp(cell.firstChild, "label", 0, 6, dsRow.getCellValue(1), "relative", null);
	cell = row.insertCell(-1);
	cell.width = "60";
	var condComb = new ComboComp(cell, "condComb", 0, 0, "70", "relative");
	
	//query_condition_trueid
	var fieldId = dsRow.getCellValue(10);
	var comboData = this.widget.getComboData("comb_" + fieldId);
	condComb.setComboData(comboData);
	condComb.setValue(dsRow.getCellValue(2));
	condComb.relationRow = dsRow;
	
	dsRow.setCellValue(2, condComb.getValue());
	
	if(!editable)
		condComb.setActive(false);
	else{
		condComb.onblur = function()
		{
			this.relationRow.setCellValue(2, this.getValue());
		};
		condComb.valueChanged = function(item, lastItem)
		{
		 	if(item == "between")
		 		oThis.processCondition(2, row, dsRow);
		 	else if(item != "between" && lastItem == "between")
	       		oThis.processCondition(1, row, dsRow);
		};
		condComb.onfocus = function()
		{
			var ds = oThis.dataset;
			this.relationRow.setCellValue(2, this.getValue());
			
			var rowId = this.relationRow.rowId;
			var index = ds.getCurrentRowSet().getCurrentRowData().getRowIndexById(rowId);
			ds.setRowSelected(index);
		};
	}
	row.insertCell(-1);
	row.width = "100%";
	if(dsRow.getCellValue(2) == "between")
		this.processCondition(2, row, dsRow, editable);
	else
		this.processCondition(0, row, dsRow, editable);
};

QueryTemplatePanel.prototype.removeElement = function(row, index)
{
	if(row.getCellValue(1) == "$#$")
		return;
	var rows = this.dataset.getRows();
	if(rows != null)
	{
		var count = 0;
		for(var i = 0; i < rows.length && i < index; i ++)
		{
			if(rows[i].getCellValue(1) != "$#$")
				count ++;
		}
		if(count != -1)
			this.table.deleteRow(count);
	}
};

QueryTemplatePanel.prototype.removeAllElement = function()
{
	var n = this.table.rows.length;
	if(n > 0)
	{
		for(var i = 0; i < n; i++)
		{
			this.table.deleteRow(0);
		}
	}
};

QueryTemplatePanel.prototype.modifyElement = function(row, cellRowIndex, cellColIndex, value, oldValue)
{
	if(row.getCellValue(1) == "$#$")
		return;
	var rows = this.dataset.getRows();
	if(rows != null)
	{
		var count = 0;
		for(var i = 0; i < rows.length && i < cellRowIndex; i ++)
		{
			if(rows[i].getCellValue(1) != "$#$")
				count ++;
		}
	}
	if(cellColIndex != 2 && cellColIndex != 3)
		return;
	this.table.firstChild.rows[count].cells[cellColIndex].firstChild.owner.setValue(value);
};


/**
  */
QueryTemplatePanel.prototype.processCondition = function(flag, row, dsRow, editable)
{ 
	var cell = row.cells[3];
	if(flag != 0){
    	if(cell.firstChild && cell.firstChild.owner) {
    		cell.firstChild.owner.destroySelf();
    		if (cell.firstChild)
    			cell.removeChild(cell.firstChild);
    	}
    }
    var valueComp = null;
    if(flag == 2)
    {
    	var type = dsRow.getCellValue(5);
    	var obj = null;
    	if(type == EditorType.REFERENCE)
    		obj = this.widget.getRefNode(dsRow.getCellValue(7));
    	valueComp = new BetweenPanel(cell, "betweenPanel", 0, 0, "100%", "20", "relative", type, obj);
	}
	else
	{
		if(dsRow.getCellValue(5) == EditorType.REFERENCE)
		{
			var oThis = this;
			var nodeInfo = this.widget.getRefNode(dsRow.getCellValue(7));
			var attr = new Object;
			valueComp = new ReferenceTextComp(cell, "valueComp", 0, 0, "100%", "relative", attr, nodeInfo, null, true);
			valueComp.widget = this.widget;
			valueComp.setBindInfo(this.dataset.id);
			valueComp.beforeOpenRefDialog = function(){
				var ds = oThis.dataset;
				ds.setRowSelected(ds.getRowIndex(this.relationRow));
			}
		}
		else if(dsRow.getCellValue(5) == EditorType.DATETEXT){
			valueComp = new DateTextComp(cell, "valueComp", 0, 0, "100%", "relative", null);
		}
		else if(dsRow.getCellValue(5) == EditorType.COMBOBOX){
			var fieldId = dsRow.getCellValue(10);
			var comboData = this.widget.getComboData("comb_" + fieldId + "_value");
			valueComp = new ComboComp(cell, "valueComp", 0, 0, "100%", "relative", true, {"readOnly": false, "dataDivHeight": 60}, null);
			valueComp.setComboData(comboData);
		}
		else if(dsRow.getCellValue(5) == EditorType.INTEGERTEXT){
			valueComp = new IntegerTextComp(cell, "valueComp", 0, 0, "100%", "relative", null, null, null, null);
		}
		else if(dsRow.getCellValue(5) == EditorType.DECIMALTEXT){
			//TODO
			var precision = 2;
			valueComp = new FloatTextComp(cell, "valueComp", 0, 0, "100%", "relative", precision, null, null);
		}
		else
		{
	    	valueComp = new StringTextComp(cell, "valueComp", 0, 0, "100%", "relative", null, null);
	    }
	}
	var cellValue = dsRow.getCellValue(3);
	valueComp.setValue(cellValue);
	if(editable == false)
		valueComp.setActive(false);
	else{
		valueComp.relationRow = dsRow;
		oThis = this;
		valueComp.onblur = function()
		{
			var ds = oThis.dataset;
			var value = null;
			if(valueComp.input)
				value = valueComp.input.value;
			//参照类型需要过滤数据，调用后台的过滤方法
			if(valueComp instanceof ReferenceTextComp)
	   			valueComp.doBlurSearch(value);
			ds.setValueAt(ds.getRowIndex(this.relationRow), 3, this.getValue());
		}
	}
	
	var focusListener = new FocusListener(false);
	focusListener.onFocus = function(focusEvent) {
		var ds = oThis.dataset;
		this.relationRow.setCellValue(3, this.getValue());
		var rowId = this.relationRow.rowId;
		var index = ds.getCurrentRowSet().getCurrentRowData().getRowIndexById(rowId);
		ds.setRowSelected(index);
	}
	valueComp.addListener(focusListener);
	
	//TODO
//	valueComp.setBounds(0, 0, cell.offsetWidth - 25, null);
//	if (IS_IE)
//		valueComp.setBounds(0, 0, 280, 18);
	
};