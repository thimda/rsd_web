/**
 * 
 * html table的简单封装。table在排版方面有一定的便利性 注意，此table并非用来装载数据的table。装载数据请用grid
 * 
 * @author dengjt, gd, guoweic
 * @version 6.0
 * 
 * 1.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 
 */

TableComp.prototype = new BaseComponent;
TableComp.prototype.componentType = "TABLE";

/**
 * Table控件构造函数
 * @class Table控件
 */
function TableComp(parent, name, left, top, width, height, position, attrArr) {
	BaseComponent.call(this, name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "absolute");
	this.cellspacing = 0;
	this.cellpadding = 0;
	this.align = "center";
	this.valign = "middle";
	this.border = 0;

	if (attrArr != null) {
		this.cellspacing = getInteger(attrArr.cellspacing, 0);
		this.cellpadding = getInteger(attrArr.cellpadding, 0);
		this.align = getString(attrArr.align, "center");
		this.valign = getString(attrArr.valign, "middle");
		this.border = getInteger(attrArr.border, 0);
		this.background = attrArr.background;
		this.bgcolor = attrArr.bgcolor;
		this.bordercolor = attrArr.bordercolor;
	}
};

/**
 * TableComp的主体创建函数
 * @private
 */
TableComp.prototype.create = function() {
	this.Div_gen = $ce("DIV");
	this.Div_gen.id = this.name;
	this.Div_gen.className = "table_div";
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	if (this.height.toString().indexOf("%") == -1)
		this.Div_gen.style.height = this.height + "px";
	else
		this.Div_gen.style.height = this.height;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.position = this.position;

	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * TableComp的二级回掉函数
 * @private
 */
TableComp.prototype.manageSelf = function() {
	this.table = $ce("table");
	this.table.className = "table_table";
	this.table.style.width = "100%";
	this.table.style.height = "100%";
	this.table.style.left = "0px";
	this.table.style.top = "0px";
	this.table.border = this.border;
	this.table.cellspacing = this.cellspacing;
	this.table.cellpadding = this.cellpadding;
	this.table.align = this.align;
	this.table.valign = this.valign;
	if (this.background)
		this.table.backGround = this.background;
	if (this.bgcolor)
		this.table.bgColor = this.bgcolor;
	if (this.bordercolor)
		this.table.borderColor = this.bordercolor;

	this.tBody = $ce("TBODY");
	this.table.appendChild(this.tBody);
	this.Div_gen.appendChild(this.table);
};

/**
 * 创建一行
 */
TableComp.prototype.createTr = function(align, valign, bgcolor, bordercolor,
		bordercolorlight, bordercolordark) {
	var tr = new TableRow(align, valign, bgcolor, bordercolor,
			bordercolorlight, bordercolordark);
	this.addTr(tr);
};

/**
 * 由createTr调用此方法
 * 
 */
TableComp.prototype.addTr = function(tr) {
	this.tBody.appendChild(tr.getObjHtml());
};

/**
 * 返回html table组件
 */
TableComp.prototype.getTable = function() {
	return this.table;
};

/**
 * 在table项写完之后必须调用，因为td本身是无法定位自身的组件，所以写在它里面的子控件需要在td真正定位下来后才添加进去。
 */
TableComp.prototype.initTable = function() {
	for ( var i = 0; i < this.table.rows.length; i++) {
		for ( var j = 0; j < this.table.rows[i].cells.length; j++) {
			this.table.rows[i].cells[j].owner.trueAdd();
		}
	}
};

/**
 * 重新摆放组件
 */
TableComp.prototype.reinitTable = function() {
	for ( var i = 0; i < this.table.rows.length; i++) {
		for ( var j = 0; j < this.table.rows[i].cells.length; j++) {
			this.table.rows[i].cells[j].owner.resetPos();
		}
	}
};

/**
 * 销毁控件自身的方法
 * @private
 */
TableComp.prototype.destroySelf = function() {
	for ( var i = 0; i < this.table.rows.length; i++) {
		this.table.rows[i].owner.destroySelf();
	}
	BaseComponent.prototype.destroySelf.call(this);
};

/**
 * Table行构造函数
 * @class Table行定义。严格按照html tr属性定义，取常用参数,可在css中配置。
 */
function TableRow(align, valign, bgcolor) {
	this.align = getString(align, "center");
	this.valign = getString(valign, "middle");
	this.bgcolor = getString(bgcolor, "#0000FF");
};

/**
 * TableRow的主体创建函数
 * @private
 */
TableRow.prototype.create = function() {
	this.tr = $ce("tr");
	this.tr.align = this.align;
	this.tr.valign = this.valign;
	this.tr.bgcolor = this.bgcolor;
	this.tr.className = "table_tr";
	this.tr.owner = this;
};

/**
 * 得到tr的显示对象
 */
TableRow.prototype.getObjHtml = function() {
	return this.tr;
};

/**
 * 创建一列
 */
TableRow.prototype.createTd = function(width, height, colspan, rowspan, align,
		valign, bgcolor, background, id) {
	var td = new TableCell(width, height, colspan, rowspan, align, valign,
			bgcolor, background, id);
	this.addTd(td);
};

/**
 * 由createTr调用此方法
 */
TableRow.prototype.addTd = function(td) {
	this.tr.appendChild(td.getObjHtml());
};

/**
 * 销毁控件自身的方法
 * @private
 */
TableRow.prototype.destroySelf = function() {
	for ( var i = 0; i < this.tr.childNodes.length; i++) {
		this.tr.childNodes[i].owner.destroySelf();
	}
};

/**
 * TableCell构造函数
 * @class Table Cell定义。取常用参数。其它参数可使用css配置。
 */
function TableCell(width, height, colspan, rowspan, align, valign, bgcolor,
		background, id) {
	this.id = id;
	this.width = getString(width, "100%");
	this.height = getString(height, "100%");
	this.colspan = getInteger(colspan, 1);
	this.rowspan = getInteger(rowspan, 1);
	this.align = getString(align, "center");
	this.valign = getString(valign, "middle");
	this.bgcolor = bgcolor;
	this.background = background;
	// 记录添加到此td中的子节点
	this.childList = new Array();
	this.oldLeft = -1;
	this.oldTop = -1;
};

/**
 * TableCell的主体创建函数
 * @private
 */
TableCell.prototype.create = function() {
	this.td = $ce("td");
	this.td.id = this.id;
	if (this.td.width.toString().indexOf("%") == -1)
		this.td.width = this.width + "px";
	else
		this.td.width = this.width;
	if (this.td.height.toString().indexOf("%") == -1)
		this.td.height = this.height + "px";
	else
		this.td.height = this.height;

	this.td.align = this.align;
	this.td.valign = this.valign;
	if (this.bgcolor)
		this.td.bgColor = this.bgcolor;
	if (this.background)
		this.td.backGround = this.background;
	this.td.colSpan = this.colspan;
	this.td.rowSpan = this.rowspan;
	this.td.className = "table_td";
	this.td.owner = this;
};

/**
 * 得到TableCell的显示对象
 */
TableCell.prototype.getObjHtml = function() {
	return this.td;
};

/**
 * 此方法只是临时记录下子组件
 */
TableCell.prototype.add = function(obj) {
	this.childList.push(obj);
};

/**
 * 真正添加子组件
 */
TableCell.prototype.trueAdd = function() {
	if (this.oldLeft == -1)
		this.oldLeft = this.td.offsetLeft;
	if (this.oldTop == -1)
		this.oldTop = this.td.offsetTop;

	for ( var i = 0; i < this.childList.length; i++) {
		this.childList[i].style.left = this.childList[i].style.pixelLeft
				+ this.td.offsetLeft + "px";
		this.childList[i].style.top = this.childList[i].style.pixelTop
				+ this.td.offsetTop + "px";
		this.td.appendChild(this.childList[i]);
	}
};

/**
 * 调整宽度
 * @private
 */
TableCell.prototype.adjustWidth = function(width) {
	this.td.width = width;
};

/**
 * @private
 */
TableCell.prototype.resetPos = function() {
	var deltaLeft = this.td.offsetLeft - this.oldLeft;
	var deltaTop = this.td.offsetTop - this.oldTop;
	for ( var i = 0; i < this.childList.length; i++) {
		this.childList[i].style.left = this.childList[i].style.pixelLeft
				+ deltaLeft + "px";
		this.childList[i].style.top = this.childList[i].style.pixelTop
				- deltaTop + "px";
	}
	this.oldLeft = this.td.offsetLeft;
	this.oldTop = this.td.offsetTop;
};

/**
 * 销毁控件自身的方法
 * @private
 */
TableCell.prototype.destroySelf = function() {
	this.childList = null;
};