/******************************************************************
 *
 * @fileoverview 列渲染器。列渲染器用于渲染每个单元格，应尽量采用提供的默认渲染器，
 * 这样可以保证渲染的效率，如果自己撰写渲染器，一定要在参考默认提供的渲染器的前提下编写，
 * 避免不必要的循环，减少多次取属性c采用a.b.c这样的写法，否则很可能导致grid展示变慢，
 * 成为grid渲染的时间瓶颈。
 * @author gd, guoweic
 * @version 6.0
 * @since 5.5
 *
 ******************************************************************/

/**
 * 默认渲染器
 * @class 默认渲染器
 */
function DefaultRender() {
};

DefaultRender.render = function(rowIndex, colIndex, value, header, cell, parentRowIndex) {
	var cellStyle = cell.style;
	cellStyle.overflow = "hidden";
	cellStyle.textOverflow = "ellipsis";
	cellStyle.cursor = "default";
	//如果grid行高比高于默认行高，内容可换行显示
	if (cell.offsetHeight > 22){
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
	
	if(parentRowIndex != null){
		cell.innerHTML = '<span style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;</span>';
	}
	var grid = this;
	if(grid.model.treeLevel != null && colIndex == 0 && parentRowIndex == null){
		var row = grid.model.getRow(rowIndex).rowData;
		var loadImg = $ce("IMG");
		loadImg.style[ATTRFLOAT] = "left";
		loadImg.src = window.themePath + "/images/grid/plus.gif";
		loadImg.plus = true;
		loadImg.initialized = false;
		loadImg.style.top = "4px";
		loadImg.style.width = "20px";
		loadImg.style.height = "20px";
		loadImg.owner = grid;
		loadImg.row = row;
		loadImg.cell = cell;
		loadImg.onclick = expandGridChild;
		cell.appendChild(loadImg);
	}
	cell.appendChild(document.createTextNode(value));
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
		this.src = window.themePath + "/images/grid/minus.gif";
	}
	else{
		var rowIndex = grid.getCellRowIndex(this.parentNode);
		grid.hideChildRows(rowIndex);
		this.plus = true;
		this.src = window.themePath + "/images/grid/plus.gif";
	}
};

function ImageRender() {
};

ImageRender.render = function(rowIndex, colIndex, value, header, cell) {
};
/**
 * 布尔类型渲染器
 * @class 布尔类型渲染器(默认渲染为checkbox)
 */
function BooleanRender() {
};

BooleanRender.render = function(rowIndex, colIndex, value, header, cell) {
	cell.style.overflow = "hidden";
	cell.style.textOverflow = "ellipsis";
	cell.style.cursor = "default";
	value = (value == null)? "": value.toString();
	var grid = header.owner;
	// 调整checkbox的padding,使checkbox居中于cell
	if (GridComp.ROW_HEIGHT == 22)
		cell.style.paddingTop = "0px";
	else
		cell.style.paddingTop = Math.floor((GridComp.ROW_HEIGHT - 20)/2) + "px";
	
	var checkBox = $ce("INPUT");
	checkBox.type = "checkbox";
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
		// 整体或者此列不可编辑,把checkbox变为inactive
		if (header.columEditable == false || grid.editable == false)
			checkBox.disabled = true;
		
		checkBox.onmousedown = function(e) {	
			if (this.checked == true)
				grid.model.setValueAt(rowIndex, colIndex, header.valuePair[1]);
			else	
				grid.model.setValueAt(rowIndex, colIndex, header.valuePair[0]);	
		};
		// 并阻止事件的进一步向上传播
		checkBox.onclick = function(e) {
			e = EventUtil.getEvent();
			// 阻止默认选中事件和事件上传,放在onmousedown中不会起作用,但通知model行选中必须放在onmousedown中!
			stopDefault(e);
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		};
	}
};

/**
 * 布尔类型图片渲染器
 * @class 布尔类型图片渲染器(渲染为图片)
 */
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

/**
 * 整形渲染器 
 * @class 整形默认渲染器 
 */
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

/**
 * decimal渲染器
 * @class 数字型默认渲染器
 */
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

/**
 * 下拉框渲染器
 * @class 下拉框默认渲染器
 */
function ComboRender() {
};

/**
 * 渲染cell。用户可以个性化的渲染cell，包括value渲染成的显示样式，背景色，字体，对齐方式。
 * 提供了强大的扩展方式。
 * @param rowIndex 行索引
 * @param colIndex 列索引
 * @param value	真实值
 * @param header
 * @param cell
 */
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

	// 仅显示图片的combox
	if (header.showImgOnly == true) {
		cell.style.background = "url(" + parsedValue + ") no-repeat center";
		cell.style.backgroundColor = "";	
	}	
	// 显示文字的combox
	else {	
		if (header.textAlign != null && header.textAlign != "")
			cell.style.textAlign = header.textAlign;
		else	
			cell.style.textAlign = "center";
		cell.tip = parsedValue;
		cell.appendChild(document.createTextNode(parsedValue));	
	}		
};

/**
 * 改变下拉框的内容
 * @param grid  GridComp对象
 * @param colIndex
 * @param keys
 * @param captions
 * @return
 */
ComboRender.changeRender = function(grid, colIndex, comboData) {
	var header = grid.basicHeaders[colIndex];
	header.setHeaderComboBoxComboData(comboData);
	
	var comp = header.comboComp;
	
	if (comp) {
		comp.clearOptions();
		if (comboData != null) {
			var keyValues = comboData.getValueArray();
			var captionValues = comboData.getNameArray();
			// 仅显示图片的combo
			if (header.showImgOnly) {
				for (var j = 0, count = keyValues.length; j < count; j++)
					comp.createOption(captionValues[j], keyValues[j], null, false, -1, true);
			}
			// 显示文字的combo
			else {
				for (var j = 0, count = keyValues.length; j < count; j++)
					comp.createOption(captionValues[j], keyValues[j], null, false, -1, false);
			}
		}
	}
};

/**
 * Email渲染器
 * @class Email的默认渲染器
 */
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
		
		//TODO:解析email地址
		//var cellValue = GridComp.parseData(header, value);
		var mailContent = "<a href='mailto:" + value + "?subject='' enctype='text/plain' target='_blank'>" + value + "</a>";
		cell.tip = value;
		cell.innerHTML = mailContent;
	}	
};	

/**
 * URL渲染器
 * @class URL的默认渲染器，默认以新窗口的方式打开链接。
 */
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

/**
 * 选择框渲染器
 * @class 选择框渲染器，采用两张图片表示选中和不选中状态
 */
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

/**
 * 性别渲染器
 * @class 性别默认渲染器，真实值1表示女，0表示男
 */
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

/**
 * 日期渲染器
 * @class 日期默认渲染器
 */
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
/**
 * 日期渲染器
 * @class 日期默认渲染器
 */
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
/*******************************************
 *
 * 通用函数
 *
 *******************************************/

/**
 * 比较函数(实现数字从小到大排列)
 */
function ascendRule_int(a, b) {
	if (parseInt(a) < parseInt(b))
		return -1;
	else if (parseInt(a) > parseInt(b))	
		return 1;
	else
		return 0;	
};

	
