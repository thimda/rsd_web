 
DefaultRender.HEADERROW_HEIGHT = 29;

DefaultRender.plusImgSrc = window.themePath + "/ui/ctrl/grid/images/folder_off.png";
DefaultRender.minusImgSrc = window.themePath + "/ui/ctrl/grid/images/folder_on.png";

function DefaultRender() {
};

DefaultRender.render = function(rowIndex, colIndex, value, header, cell, parentRowIndex) {
	var cellStyle = cell.style;
	cellStyle.overflow = "hidden";
	cellStyle.textOverflow = "ellipsis";
	cellStyle.cursor = "default";
	if (cell.offsetHeight > DefaultRender.HEADERROW_HEIGHT){
		cellStyle.whiteSpace = "normal";
		cellStyle.wordWrap = "break-word";
		cellStyle.lineHeight = "";	
		cellStyle.overflow = "auto";	
	} 
	if(header.textAlign != null && header.textAlign != "")
		cellStyle.textAlign = header.textAlign;
	
	if (value == null)
		value = "";
	cell.tip = value;
	
	if(parentRowIndex != null && colIndex == 0){
	    if(cell.level != null){
	    	for(var i = 0; i < cell.level; i ++){
				cell.innerHTML += '<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;</span>';
			}
	    }
	    else
			cell.innerHTML = '<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;</span>';
	}
	else
		cell.innerHTML = "";
	var grid = this;
	if(grid.model.treeLevel != null && colIndex == 0){
		var row = grid.model.getRow(rowIndex).rowData;
		var loadImg = $ce("IMG");
		//loadImg.marginTop = "5px";
		loadImg.style[ATTRFLOAT] = "left";
		if(cell.hasChildren && cell.hasChildren != null && cell.hasChildren == true)	
			loadImg.src = DefaultRender.plusImgSrc;
		else		
			loadImg.src = DefaultRender.minusImgSrc;
		loadImg.plus = true;
		loadImg.initialized = false;
		loadImg.style.margin = "6px";
		loadImg.style.marginTop = "9px";
		loadImg.style.width = "8px";
		loadImg.style.height = "8px";
		loadImg.owner = grid;
		loadImg.row = row;
		loadImg.cell = cell;
		if(cell.level == null)
			cell.level = 0;
		loadImg.onclick = expandGridChild;
		cell.appendChild(loadImg);
	}
	var userDiv = null;
	if (typeof fillCellContent != "undefined"){
		userDiv = fillCellContent.call(this, grid, rowIndex, colIndex, value, header, parentRowIndex);
	}
	if (userDiv == null)
		cell.appendChild(document.createTextNode(value));
	else	
		cell.appendChild(userDiv);
};

function expandGridChild(e){
	var grid = this.owner;
	if(this.plus){
		if(!this.initialized){
			var ds = grid.model.dataset;
			var rowIndex = grid.getCellRowIndex(this.parentNode);
			var index = ds.nameToIndex(grid.model.treeLevel.recursiveKeyField);
			var row = this.row;
			var key = row.getCellValue(index);
			grid.loadChild(key, rowIndex + 1, this.cell);
			this.initialized = true;
		}
		else{
			var rowIndex = grid.getCellRowIndex(this.parentNode);
			grid.showChildRows(rowIndex);
		}
		this.plus = false;
		this.src = DefaultRender.minusImgSrc;
	}
	else{
		var rowIndex = grid.getCellRowIndex(this.parentNode);
		grid.hideChildRows(rowIndex);
		this.plus = true;
		this.src = DefaultRender.plusImgSrc;
	}
};

function ImageRender() {
};

ImageRender.render = function(rowIndex, colIndex, value, header, cell) {
};

function BooleanRender() {
};

BooleanRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.style.overflow = "hidden"; 
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	value = (value == null)? "": value.toString();
	var grid = header.owner;
	if (GridComp.ROW_HEIGHT == DefaultRender.HEADERROW_HEIGHT)
		cell.style.paddingTop = "0px";
	else
		cell.style.paddingTop = "0px";//Math.floor((GridComp.ROW_HEIGHT - 20)/2) + "px";
	
	var checkBox = $ce("INPUT");
	checkBox.type = "checkbox";
	checkBox.style.marginTop = "5px";
	checkBox.rowIndex = rowIndex;
	checkBox.colIndex = colIndex;
	cell.appendChild(checkBox);
	if (header.valuePair != null) {	
		if (value == header.valuePair[0]) {	
			cell.trueValue = value;
			checkBox.defaultChecked = true;
			checkBox.checked = true;
		} else if (value == header.valuePair[1]) {	
			cell.trueValue = value;
			checkBox.defaultChecked = false;
			checkBox.checked = false;
		}
		if (header.columEditable == false || grid.editable == false)
			checkBox.disabled = true;
		
		checkBox.onmousedown = function(e) {	
			if (this.checked == true)
				grid.model.setValueAt(rowIndex, colIndex, header.valuePair[1]);
			else	
				grid.model.setValueAt(rowIndex, colIndex, header.valuePair[0]);	
		};
		checkBox.onclick = function(e) {
			e = EventUtil.getEvent();
			stopDefault(e);
			stopEvent(e);
			clearEventSimply(e);
		};
	}
};

function BooleanImageRender() {
};

BooleanImageRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	if (header.textAlign != null && header.textAlign != "")
		cell.style.textAlign = header.textAlign;
	else	
		cell.style.textAlign = "center";
	value = (value == null) ? "" : value.toString();
	var grid = header.owner;
	
	if (header.valuePair != null) {	
		if (value == header.valuePair[0])
			cell.style.background = "#ffffff url('" + window.globalPath + "/html/image/checked.gif" + "') no-repeat center center";
		else if (value == header.valuePair[1])
			cell.style.background = "#ffffff url('" + window.globalPath + "/html/image/unchecked.gif" + "') no-repeat center center";
	}
};

function IntegerRender() {
};

IntegerRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	if (header.textAlign != null && header.textAlign != "")
		cell.style.textAlign = header.textAlign;
	else	
		cell.style.textAlign = "right";
	var cellValue = GridComp.parseData(header, value);
	var result = getMasker(IntegerTextComp.prototype.componentType).format(cellValue);
	var cellDisplayVal = toColorfulString(result);
	cell.tip = cellDisplayVal;
	cell.appendChild(document.createTextNode(cellDisplayVal));
};

function DecimalRender() {
};

//DecimalRender.render = IntegerRender.render;
DecimalRender.render = function(rowIndex, colIndex, value, header, cell) {	
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	if (header.textAlign != null && header.textAlign != "")
		cell.style.textAlign = header.textAlign;
	else	
		cell.style.textAlign = "right";
	var cellValue = GridComp.parseData(header, value);
	cell.tip = cellValue;
	cell.appendChild(document.createTextNode(cellValue));
};

function ComboRender() {
};

ComboRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	var parsedValue = "";
	if (header.comboData != null && header.comboData.getValueArray() != null){
		var varr = header.comboData.getValueArray();
		var vs = (value == null)? [] : value.split(",");
		var indices = new Array;
		for(var i = 0; i < vs.length; i ++){
			var index = varr.indexOf(vs[i]);
			indices.push(index);
		}
		var parsedValueArr = new Array;
		if (indices.length > 0) {
			var narr = header.comboData.getNameArray();
			for(var i = 0; i < indices.length; i ++){
				if(indices[i] != null && indices[i] != -1)
					parsedValueArr.push(narr[indices[i]]);
				else
					parsedValueArr.push("");
			}
		}
		parsedValue = parsedValueArr.join(",");
	}

	if (header.showImgOnly == true) {
		cell.style.background = "url(" + parsedValue + ") no-repeat center";
		cell.style.backgroundColor = "";	
	}	
	else {	
		if (header.textAlign != null && header.textAlign != "")
			cell.style.textAlign = header.textAlign;
		else	
			cell.style.textAlign = "center";
		cell.tip = parsedValue;
		cell.appendChild(document.createTextNode(parsedValue));	
	}		
};

ComboRender.changeRender = function(grid, colIndex, comboData) {
	var header = grid.basicHeaders[colIndex];
	header.setHeaderComboBoxComboData(comboData);
	
	var comp = header.comboComp;
	
	if (comp) {
		comp.clearOptions();
		if (comboData != null) {
			var keyValues = comboData.getValueArray();
			var captionValues = comboData.getNameArray();
			if (header.showImgOnly) {
				for (var j = 0, count = keyValues.length; j < count; j++)
					comp.createOption(captionValues[j], keyValues[j], null, false, -1, true);
			}
			else {
				for (var j = 0, count = keyValues.length; j < count; j++)
					comp.createOption(captionValues[j], keyValues[j], null, false, -1, false);
			}
		}
	}
};

function EmailRender() {
};

EmailRender.render = function(rowIndex, colIndex, value, header, cell) {
	if (value != null && value.trim() != "") {
		cell.style.overflow = "hidden";
		cell.style.textOverflow = "ellipsis";
		cell.style.cursor = "default";
		if (header.textAlign != null && header.textAlign != "")
			cell.style.textAlign = header.textAlign;
		else	
			cell.style.textAlign = "left";
		
		var mailContent = "<a href='mailto:" + value + "?subject='' enctype='text/plain' target='_blank'>" + value + "</a>";
		cell.tip = value;
		cell.innerHTML = mailContent;
	}	
};	

function UrlRender() {
};

UrlRender.render = function(rowIndex, colIndex, value, header, cell) {
	if (value != null && value.trim() != "") {
		cell.style.overflow = "hidden";
		cell.style.textOverflow = "ellipsis";
		cell.style.cursor = "default";
		if (header.textAlign != null && header.textAlign != "")
			cell.style.textAlign = header.textAlign;
		else
			cell.style.textAlign = "left";
		var urlContent = "<a href='#' onclick=\"window.open('http://" + value + "');return false;\">" + value + "</a>";
		cell.tip = value;
		cell.innerHTML = urlContent;
	}	
};

function CheckBoxRender() {
};

CheckBoxRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	if (header.textAlign != null && header.textAlign != "")
		cell.style.textAlign = header.textAlign;
	else		
		cell.style.textAlign = "center";
	
	if (value == 1)
		cell.style.background = "#ffffff url('" + window.globalPath + "/html/image/checked.gif" + "') no-repeat center center";
	else if (value == 0)
		cell.style.background = "#ffffff url('" + window.globalPath + "/html/image/unchecked.gif" + "') no-repeat center center";	
};

function SexRender() {
};

SexRender.render = function(rowIndex, colIndex, value, header, cell) {	
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	if (header.textAlign != null && header.textAlign != "")
		cell.style.textAlign = header.textAlign;
	else	
		cell.style.textAlign = "center";
	
	if (value == 1)
		cell.style.background = "#ffffff url('" + window.globalPath + "/html/image/woman.gif" + "') no-repeat center center";
	else if(value == 0)
		cell.style.background = "#ffffff url('" + window.globalPath + "/html/image/man.gif" + "') no-repeat center center";	
};

function DateRender() {
};

DateRender.render = function(rowIndex, colIndex, value, header, cell) {	
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	if (header.textAlign != null && header.textAlign != "")
		cell.style.textAlign = header.textAlign;
	else	
		cell.style.textAlign = "center";
	
	if (value != null && value.length > 10)
		value = value.substring(0, 10);
	else if (value == null)
		value = "";
	else if(value == 0)
		value = "";	
    if (value == null)
		value = "";
	var showValue = value;
	var maskerType = "DATETEXT"; 
	var masker = getMasker(maskerType);
	if(masker != null)
		showValue = toColorfulString(masker.format(value));
	cell.tip = showValue;
	cell.innerHTML = showValue;
};

function DateTimeRender() {
};

DateTimeRender.render = function(rowIndex, colIndex, value, header, cell) {	
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	if (header.textAlign != null && header.textAlign != "")
		cell.style.textAlign = header.textAlign;
	else	
		cell.style.textAlign = "center";
    if (value == null)
		value = "";
	var showValue = value;
	var maskerType = "DateTimeText"; 
	var masker = getMasker(maskerType);
	if(masker != null)
		showValue = toColorfulString(masker.format(value));
	cell.tip = showValue;
	cell.innerHTML = showValue;
};	

function ascendRule_int(a, b) {
	if (parseInt(a) < parseInt(b))
		return -1;
	else if (parseInt(a) > parseInt(b))	
		return 1;
	else
		return 0;	
};


/**
 * 默认行渲染
 * 
 */
function DefaultRowRender() {
};

/**
 * 默认行渲染
 * 
 * @param {} row 数据行
 * @param {} rowCells 当前渲染行中的所有cell，通过cell.colIndex 可以取到该cell对应ds中的第几列
 * 
 */
DefaultRowRender.render = function(row, rowCells) {
	// e.g
//	var value = row.getCellValueByFieldName("filed1");
//	if (value == "Y"){
//		var colIndex = row.getColIndexByFieldName("filed1");	
//		for (var i = 0; i < rowCells.length; i++){
//			if (rowCells[i].colIndex == colIndex)			
//				rowCells[i].style.backgroundColor = "red";		
//		}
//	}
};

/***
 * 创建单元格控件
 */
function CellEditor(parent, row, colIndex){
//  var comp = null	
//	if (row.getCellValueByFieldName("f2") == "1" && colIndex == 1){
//		comp = new DateTextComp(parent, "dateText", 0, 0, "100%",
//				"absolute", {
//					readOnly : false
//				});
//		comp.setShowTimeBar(true);
//	}
//	return comp;
//	
};


