/**
 * 
 * 固定布局管理器
 * 
 * @auther dingrf
 * @version NC6.0
 * 
 */

FixedGridLayout.prototype.className = "FixedGridLayout";
FixedGridLayout.prototype = new PanelComp;

// 常量设置
//FlowGridLayout.BASEWIDTH = 120;
//FlowGridLayout.COLSPACE = 16;
//FlowGridLayout.ROWSPACE = 6;
//FlowGridLayout.COMPHEIGHT = 22;

// 额外的空白高度
FixedGridLayout.SPACE_HEIGHT = 10;

/**
 * 表单固定布局构造函数
 * @class 表单固定布局
 */
function FixedGridLayout(parent, name, left, top, width, height, position,
		scroll, compDefaultWidth, marginTop,rowHeight, columnCount) {
	this.base = PanelComp;
	this.base(parent, name, left, top, width, height, position, scroll);
	this.parentOwner = parent;
	this.defaultwidth = getString(compDefaultWidth, FlowGridLayout.BASEWIDTH);
	this.basewidth = this.defaultwidth;
	this.gridWidth = 0; // 每一列的应有宽度
	this.colmNum = 0; // 应有的列数
	this.lastX = 0; // 上一组件的起始X坐标,以此来计算下一组件的开始坐标
	this.wordsdefault = 5; // 缺省的label中的字数
	// 保存每次传入的组件
	this.comps = new Array();
	// this.labels = new Array();
	this.marginTop = getInteger(marginTop, 6);
	this.currentRow = null;
	this.rowHeight = getInteger(rowHeight,22);
	this.columnCount = getInteger(columnCount,2);
	// 强制刷新,如果强制刷新,每次paint前要设置该值为true
	this.forceRepaint = false;
	if (FixedGridLayout.syncArray == null) {
		FixedGridLayout.syncArray = new Array;
	}
	FixedGridLayout.syncArray.push(this);
	if (FixedGridLayout.wordArray == null)
		FixedGridLayout.wordArray = new Array;
		
	FixedGridLayout.COMPHEIGHT = getCssHeight("text_form_div_COMPHEIGHT");
};

/**
 * 创建组件div
 * @private
 */
FixedGridLayout.prototype.create = function() {
	var oThis = this;
	this.Div_gen = document.createElement("DIV");
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
//	if (this.width.toString().indexOf("%") == -1)
//		this.Div_gen.style.width = this.width + "px";
//	else
//		this.Div_gen.style.width = this.width;
//	if (this.height.toString().indexOf("%") == -1)
//		this.Div_gen.style.height = this.height + "px";
//	else
//		this.Div_gen.style.height = this.height;
	this.Div_gen.style.width = "100%";
	this.Div_gen.style.height = "100%";
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.overflowY = "auto";
	this.Div_gen.style.overflowX = "hidden";
	
	//创建table
	this.table = this.createTable();
	this.Div_gen.appendChild(this.table);
	
	this.rt = null;
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * @private
 */
function resizeLayout(compId) {
	alert("resizeLayout");
};

/**
 * 调用paint函数进行绘制
 * @private
 */
FixedGridLayout.prototype.paint = function() {
	var compsCount = this.comps.length;
	if (compsCount == 0)
		return;

	var offsetWidth = this.Div_gen.offsetWidth;
	// 处于隐藏状态
	if (offsetWidth == 0)
		return;
		
	// 要隐藏的控件,要显示的控件
	var visibleComps = [], hiddenComps = [], allComps = this.comps;
	for ( var i = 0; i < compsCount; i++) {
		if (allComps[i].visible == true) {
			visibleComps.push(allComps[i]);
		} else {
			hiddenComps.push(allComps[i]);
		}
	}

	// 将comps设置为显示控件,下面只渲染显示控件
	this.comps = visibleComps;
	//重绘table
	this.Div_gen.removeChild(this.table);
	this.table = this.createTable();
	this.Div_gen.appendChild(this.table);
	
	//记录当前行索引
	var nowRow = 0;
	//记录当前行的控件个数
	var count = 0;
	//记录所有行的占用span数
	var rowInfoArray = new Array();
	var labels = new Array(this.columnCount);
	//当前行
	var currentRow = null;
	for ( var i = 0, compCount = this.comps.length; i < compCount; i++) {
		var colSpan = this.comps[i].colSpan;
		var rowSpan = this.comps[i].rowSpan;
		
		// 新行开始，加上行标
		if (count == 0) {
			currentRow = this.createRow();		
		}	

		// 加上跨行元素宽度
		if (rowInfoArray[nowRow] != null){
			count += rowInfoArray[nowRow];
			rowInfoArray[nowRow] = null;
		}
		
		// 检查越界
		if (count > this.columnCount)
			count = this.columnCount;
//			throw new Error("element out of bound in the form, please check you configuration");

		// 检查是否一个元素就超出了界限
		if (colSpan > this.columnCount)
			colSpan = this.columnCount;
//			throw new Error("element out of bound in the form, please check you configuration");
			
		// 如果加上当前控件越界,则补齐当前行td数，转到下一行
		if (count + parseInt(colSpan) > this.columnCount) {
			for (; count < this.columnCount; count++)
				this.createNullCell(currentRow);
			i--;	
			count = 0;
			nowRow++;
			continue;
		}
		//如果是强制下一行，并且当前行并不是新起的行,则根据是否已经是新行进行判断
		else if(count != 0 && this.comps[i].nextLine){
			for (; count < this.columnCount; count++)
				this.createNullCell(currentRow);
			i--;
			count = 0;
			nowRow++;
			continue;
		}
		else{
			var cell = this.createCell(currentRow,rowSpan,colSpan);
			//创建控件元素
			this.renderOneElement(cell, this.comps[i], count, labels);
			//是否绑定下一个
			if (this.comps[i].isAttachNext == true && this.comps[i + 1] != null)
				this.renderOneElement(cell, this.comps[++i], count, labels);
			count += parseInt(colSpan,10);
			//处理跨行
			if (rowSpan != 1) {
				for (var j = 1; j < rowSpan; j++) {
					rowInfoArray[nowRow + j] = getInteger(rowInfoArray[nowRow + j],0) + parseInt(colSpan,10);					
				}
			}
		}
		if (count == this.columnCount) {
			this.createNullCell(currentRow);
			count = 0;
			nowRow++;
		}
	}
	this.comps = allComps;
//	this.forceRepaint = false;
	this.afterRepaint();
};

FixedGridLayout.prototype.renderOneElement = function(cell, comp, count, labels){
	var cellWidth = 0;
	if (cell.style.width)
		cellWidth += parseInt(cell.style.width); 
	
	//创建label
	var label = comp.labelName;
	if(label != null && label != ""){
		// checkbox不设置label
		if (comp.componentType == "CHECKBOX")
			label = "";
//		FormComp form = (FormComp) getComponent();
//		if(!(form.getFrom() != null && form.getFrom().equals(Dataset.FROM_NC))){
			
		var labelDiv = $ce("DIV");	
		labelDiv.style.textAlign = "right";
		if (IS_IE)
			labelDiv.style.styleFloat = "left";
		else
			labelDiv.style.cssFloat = "left";
		labelDiv.style.paddingTop = "2px";
		labelDiv.style.paddingRight = "5px";
		labelDiv.innerHTML = label;
		//lable颜色
		if (comp.labelColor)
			labelDiv.style.color = comp.labelColor;
		//必填项，加红色 *	
		if (comp.isRequired && label != null){
			var span = $ce("span");
			span.style.color = "red";
			span.innerHTML = "*"; 
			labelDiv.appendChild(span);
			this.dealLabelWidth(labelDiv, label + "*", count, labels);
		}
		else
			this.dealLabelWidth(labelDiv, label, count, labels);
		cell.appendChild(labelDiv);
		cellWidth += parseInt(labelDiv.style.width);
		//label有paddingRight  5px
		cellWidth += 5;
	}
	var compDiv = $ce("DIV");
	if (IS_IE)
		compDiv.style.styleFloat = "left";
	else
		compDiv.style.cssFloat = "left";
		
//	compDiv.style.height = "100%";
	compDiv.appendChild(comp.Div_gen);
	cell.appendChild(compDiv);
	cellWidth += parseInt(comp.Div_gen.style.width); 
	
	//输入框外提示
	if (comp.inputAssistant != null && comp.inputAssistant != ""){
		var assDiv = $ce("DIV");
		assDiv.style.textAlign = "right";
		if (IS_IE)
			assDiv.style.styleFloat = "left";
		else
			assDiv.style.cssFloat = "left";
		assDiv.style.paddingTop = "3px";
		assDiv.style.paddingRight = "5px";
		assDiv.innerHTML = comp.inputAssistant;
		cell.appendChild(assDiv);
		cellWidth += parseInt(assDiv.offsetWidth);
	} 
	//描述
	if (comp.description != null && comp.description != ""){
		var descDiv = $ce("DIV");
		descDiv.style.textAlign = "right";
		if (IS_IE)
			descDiv.style.styleFloat = "left";
		else
			descDiv.style.cssFloat = "left";
		descDiv.style.paddingTop = "3px";
		descDiv.style.paddingRight = "5px";
		descDiv.style.color = "gray";
		descDiv.innerHTML = comp.description;
		cell.appendChild(descDiv);
		cellWidth += parseInt(descDiv.offsetWidth);
	} 
	cell.style.width = cellWidth + "px"; 
};

/**
 * 创建固定布局的Tabel
 * @private
 */
FixedGridLayout.prototype.createTable = function() {
	var table = $ce("table");
	table.setAttribute("width","100%");
//	table.setAttribute("height","100%");
	return table;
};

/**
 * 创建固定布局的TR
 * @private
 */
FixedGridLayout.prototype.createRow = function() {
	if (this.table == null)
		return;
	var row = this.table.insertRow(-1);
	row.setAttribute("height",this.rowHeight);
	return row;
};

/**
 * 创建空TD
 * @private
 */
FixedGridLayout.prototype.createNullCell = function(row) {
	if (row == null)
		return;
	var cell = row.insertCell(-1);
	return cell;
};

/**
 * 创建TD
 * @private
 */
FixedGridLayout.prototype.createCell = function(row,rowSpan,colSpan) {
	if (row == null)
		return;
	var cell = row.insertCell(-1);
	cell.setAttribute("valign","top");
	cell.setAttribute("rowSpan",rowSpan);
	cell.setAttribute("colSpan",colSpan);
//	cell.style.width = "0px";
	return cell;
};

/**XX
 * 增加td内容
 */
FixedGridLayout.prototype.addComp = function(cell,comp) {
	//标题
	var labelDiv =$ce("DIV");
	labelDiv.style.textAlign = "right";
	if (IS_IE)
		labelDiv.style.styleFloat = "left";
	else
		labelDiv.style.cssFloat = "left";
	labelDiv.style.paddingTop = "2px";
	labelDiv.style.paddingRight = "5px";
	labelDiv.style.width = "44px";
	labelDiv.value = comp.labelName;
	cell.appendChild(labelDiv);
	//comp
	var objHtml = comp.getObjHtml();
	cell.appendChild(objHtml);
	comp.parentOwner = cell;
	//Description，inputAssistant，先不处理
//	var div
};

/**
 * @private
 */
FixedGridLayout.prototype.afterRepaint = function() {
//	this.Div_gen.style.height = this.getHeight() + FixedGridLayout.SPACE_HEIGHT + "px"; 	
	this.Div_gen.style.height = "100%"; 	
};

/**
 * 获取高度
 * @private
 */

FixedGridLayout.prototype.getHeight = function() {
	if (this.table == null)
		return 0;
	var height = (this.rowHeight + 3) * this.table.rows.length;
	return height;
};

/**
 * 添加要排版的组件
 * @private
 */
FixedGridLayout.prototype.add = function(comp) {
	PanelComp.prototype.add.call(this, comp);
	this.comps.push(comp.owner);
};

/**
 * 设置是否强制重新paint
 * @private
 */
FixedGridLayout.prototype.setForceRepaint = function(isForceRepaint) {
	this.forceRepaint = getBoolean(isForceRepaint, false);
};

/**
 * 得到传入的所有panel组件
 * @private
 */
FixedGridLayout.prototype.getComps = function() {
	return this.comps;
};

/**
 * 根据id获取子项对象
 */
FixedGridLayout.prototype.getCompById = function(id) {
	if (this.comps != null) {
		for ( var i = 0, count = this.comps.length; i < count; i++) {
			if (this.comps[i].id == id)
				return this.comps[i];
		}
	}
};

/**
 * 根据id删除子项对象
 */
FixedGridLayout.prototype.removeCompById = function(id) {
	var comp = null;
	if (this.comps != null) {
		var newComps = new Array();
		for ( var i = 0, count = this.comps.length; i < count; i++) {
			if (this.comps[i].id == id){
				comp = this.comps[i];
			}else{
				newComps.push(this.comps[i])
			}
		}
		this.comps = newComps;
	}
	return comp;
};

/**
 * 处理label宽度
 * @param {} label
 */
FixedGridLayout.prototype.dealLabelWidth = function(labelDiv, label, count, labels) {
	var currentWidth = getTextWidth(label, null, true);
	if (labels[count] == null){
		labels[count] = new Array();
		labelDiv.style.width = currentWidth + "px"; 
		labels[count].push(labelDiv);
	}
	else{
		var minWidth = parseInt(labels[count][0].style.width);
		if (currentWidth > minWidth){
			for (var i = 0; i < labels[count].length; i++){
				labels[count][i].style.width = currentWidth + "px"; 
			}
			labelDiv.style.width = currentWidth + "px"; 
			labels[count].push(labelDiv);
		}
		else{
			labelDiv.style.width = minWidth + "px"; 
			labels[count].push(labelDiv);
		}
	}
};
