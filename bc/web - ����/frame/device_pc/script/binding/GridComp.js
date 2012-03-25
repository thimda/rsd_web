/**
 * @fileoverview
 * 
 * @author gd, guoweic
 * @version NC6.0
 * @since NC5.5
 * 
 * 1.新增事件监听功能 . guoweic <b>added</b> 2.修改event对象的获取 . guoweic <b>modified</b>
 * 3.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b> 4.修正功能缺陷 . guoweic <b>modified</b>
 */
// 表头的默认单行高度
GridComp.HEADERROW_HEIGHT = 21;
// grid数据行的默认高度
GridComp.ROW_HEIGHT = 44;
// 固定选择列的宽度值(isMultiSelWithBox为true)
GridComp.SELECTCOLUM_WIDTH = 30;
// 单元格底边高度
GridComp.CELL_BOTTOM_BORDER_WIDTH = 1;
// grid分页条的高度
GridComp.PAGEBAR_HEIGHT = 25;
// 列的左边界宽
GridComp.COLUMN_LEFT_BORDER_WIDTH = 0;
// 合计单元格的padding
GridComp.SUMCELL_PADDING = 2;
// 行状态列宽
GridComp.ROWSTATE_COLUMN_WIDTH = 13;
// 多选列宽
GridComp.MULTISEL_COLUMN_WIDTH = 61;
// "合计" 所在Div所占列宽
GridComp.SUMROW_DIV_WIDTH = 34;
GridComp.COlUMWIDTH_DEFAULT = 70;
GridComp.SCROLLBAE_HEIGHT = 17;
GridComp.prototype = new BaseComponent;
GridComp.prototype.componentType = "GRIDCOMP";
/**
 * Grid控件构造函数
 * 
 * @class 最大限度的利用html + css实现的轻量级表格控件类<br>
 *        <b>Grid特性：</b><br>
 *        1、懒加载模式装载数据,一次装载一屏<br>
 *        2、采用MVC模式，model层和view层分离<br>
 *        3、支持锁定列，可以动态指定固定在最左侧的列<br>
 *        4、支持无限分级的多表头，表头可以动态拖动<br>
 *        5、功能强大的渲染器，grid内置了常用的渲染器，可将真实值渲染为个性化外观的显示值<br>
 *        6、支持多种编辑类型，可以利用控件库的多种编辑控件编辑cell的值<br>
 *        7、支持合计行，浮动在表格最下方，可以自动获取指定合计列的值<br>
 *        8、支持前后台分页，分页时显示分页操作栏<br>
 *        <b>组件皮肤设置说明：</b><br>
 *        grid.css文件用来控制此控件的外观<br>
 *        <br>
 *        <b>Grid控件使用方法介绍：</b><br>
 *        <b>1、使用lfw框架的用法</b><br>
 *        (1)前台jsp页面中写明 &lt;lfw:grid
 *        id="">&lt;/lfw:grid>，id为引用的后台配置文件中配置的此Grid的id。<br>
 *        (2)在后台配置文件中配置Grid的各个属性信息<br>
 *        <b>Grid属性说明：</b><br>
 *        id------------Grid控件的id，jsp文件的控件id必须和这个id相同才能引用到此控件<br>
 *        dataset-------Grid控件所绑定的dataset的id<br>
 *        editable------此Grid控件整体是否可以编辑<br>
 *        width---------Grid控件的宽度，单位可以是"px"或者"%"两种<br>
 *        height--------Grid控件的高度，单位可以是"px"或者"%"两种<br>
 *        multiSelect---是否可以选择多行，多行选择则显示第一列的固定选择列，否则不显示第一行的固定选择列<br>
 *        <b>Grid列的属性说明：</b><br>
 *        id--------------Grid列控件的id<br>
 *        field-----------所引用的dataset中的字段的id<br>
 *        columEditable---此列是否可以编辑(true|false)。要使columEditable设置为false有效，Grid的editable属性必须设置为true<br>
 *        width-----------此列的宽度<br>
 *        dataType--------此列的数据类型<br>
 *        editorType------请参阅JsExtensions.js中的数据类型定义<br>
 *        renderType------此列的渲染器类型<br>
 *        isHidden--------是否引藏此列<br>
 *        columBgColor----列的背景色<br>
 *        textAlign-------文字对齐方式<br>
 *        textColor-------文字颜色<br>
 *        isFixedHeader---是否是固定列<br>
 *        refComboData----如果editorType是CheckBox类型，refCombodata表示所引用的ComboData的id<br>
 *        refNode---------如果editorType是Reference类型，此属性表示引用的参照节点的id<br>
 *        <br>
 * 
 * <b>&lt;Grid id="" dataset="" editable="" width="" height=""
 * multiSelect=""&gt;<br>
 * &lt;Column id="" field="" columEditable="" width="" dataType="" editorType=""
 * renderType="" isHidden="" columBgColor="" textAlign="" textColor=""
 * isFixedHeader="" refComboData="" refNode=""/><br>
 * ---<br>
 * &lt;Events> &lt;Event name=""> &lt;/Event> &lt;/Events><br>
 * &lt;/Grid></b><br>
 * <br>
 * <b>2、使用js，直接通过new的用法生成grid实例</b><br>
 * var grid = new GridComp(parent, name, left, top, width, height, position,
 * editable, isMultiSelect);<br>
 * var model = new GridCompModel();<br>
 * <b>// 单表头的构建方法</b><br>
 * var header1 = new GridCompHeader(keyName, showName, width, dataType,
 * sortable, isHidden, columEditable, defaultValue, columBgColor, textAlign,
 * textColor, isFixedHeader, renderType, editorType, <b>null</b>, <b>null</b>,
 * <b>false</b>);<br>
 * model.addHeader(header1);<br>
 * <b>// 多表头的构建方法</b><br>
 * var multiHeader = new GridCompHeader(keyName, showName, width, dataType,
 * sortable, isHidden, columEditable, defaultValue, columBgColor, textAlign,
 * textColor, isFixedHeader, renderType, editorType, <b>null</b>, <b>null</b>,
 * <b>true</b>);<br>
 * model.addHeader(multiHeader);<br>
 * var subHeader1 = new GridCompHeader(keyName, showName, width, dataType,
 * sortable, isHidden, columEditable, defaultValue, columBgColor, textAlign,
 * textColor, isFixedHeader, renderType, editorType, <b>multiHeader</b>,
 * <b>multiHeader</b>, <b>true</b>);<br>
 * var subHeader2 = new GridCompHeader(keyName, showName, width, dataType,
 * sortable, isHidden, columEditable, defaultValue, columBgColor, textAlign,
 * textColor, isFixedHeader, renderType, editorType, <b>multiHeader</b>,
 * <b>multiHeader</b>, <b>true</b>);<br>
 * model.setDataSet(dataset);<br>
 * grid.setModel(model);<br>
 * grid.create();<br>
 * <br>
 * <b>constants常量池中存放的常量：(当grid隐藏时可以从此中取得需要的grid的常量)</b><br>
 * <b>3、继续开发grid和增加方法时需要注意的问题</b><br>
 * (1)如果header不是basicHeader且是顶层header，那么header.basicHeaders属性存储着basicHeaders。
 * 
 * @constructor Grid构造函数
 * @author gd, guoweic
 * @version NC6.0
 * @param parent
 *            父控件对象
 * @param name
 *            控件名称
 * @param left
 *            控件左部x坐标
 * @param top
 *            控件顶部y坐标
 * @param width
 *            控件宽度
 * @param height
 *            控件高度
 * @param position
 *            定位属性
 * @param editable
 *            整体是否可以编辑
 * @param isMultiSelWithBox
 *            多行选择模式选择；1、显示第一列的固定选择列的多行选择模式 2、支持ctrl和shift形式的多选模式
 * @param isShowNumCol
 *            是否显示数字列
 * @param isShowSumRow
 *            是否显示合计行
 * @param paginationObj
 *            分页对象，存储了分页所需的信息，包括pageSize,client(true:前台分页,false:后台分页)
 * @param className
 *            css类名
 * @param isPagenationTop
 *            翻页工具条是否在顶端
 * @param oddType
 *            判断单双行类型，“0”为单双行各一行交错排列；“1”为单行1行，双行2行交错排列
 * @param isGroupWithCheckbox
 *            分组显示时，多选框是否分组
 * @param isShowHeader
 *            是否显示表头
 */
function GridComp(parent, name, left, top, width, height, position, editable,
		isMultiSelWithBox, isShowNumCol, isShowSumRow, paginationObj,
		groupHeaderIds, sortable, className, isPagenationTop, showColInfo,
		oddType, isGroupWithCheckbox, isShowHeader) {
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parent = parent;
	this.position = getString(position, "absolute");
	this.className = getString(className, "grid_div");

	if (IS_IE7 || IS_IE9)
		GridComp.ROWSTATE_COLUMN_WIDTH = 14;
	
	// 初始化静态常量
	this.initStaticConstant();

	// 标示此grid是否可以编辑
	this.editable = getBoolean(editable, true);
	// 当前选中行的索引(数组)
	this.selectedRowIndice = null;
	// 表头数组(最上层header的集合,多表头情况指最上层的header)
	this.headers = null;
	// 表头基础数组(最下层header的集合,多表头情况指最下层包括真正有用信息的header),此数组是根据headers内部生成的
	this.basicHeaders = new Array();
	// grid行高度
	this.rowHeight = GridComp.ROW_HEIGHT;
	this.headerRowHeight = GridComp.HEADERROW_HEIGHT;
	// 数据区真正的宽度
	this.realWidth = 0;
	// 编辑控件.针对每种类型控件初始化一个控件,然后将控件动态展示到编辑单元格上,这样做主要是展现效率问题.
	this.compsMap = null;
	// 保存当前激活的cell
	this.currActivedCell = null;
	// 保存当前选中的cell
	this.selectedCell = null;
	// 调用addOneRow()第一次出现竖直滚动条的标志
	this.firstVScroll = false;
	// 保存所有的静态表头headers
	this.defaultFixedHeaders = null;
	// 保存所有的动态表头headers
	this.defaultDynamicHeaders = null;
	// 当前显示编辑控件
	this.showComp = null;
	// 当前行是否被画过的标志数组
	this.paintedSign = null;
	// 标示控件是否被初始化过
	this.compsInited = false;
	// 是否多行选择(多行选择则显示第一列的固定选择列,否则不显示第一行的固定选择列)
	this.isMultiSelWithBox = getBoolean(isMultiSelWithBox, false);
	// 是否显示数字行号
	this.isShowNumCol = getBoolean(isShowNumCol, false);
	// 是否显示合计行
	this.isShowSumRow = getBoolean(isShowSumRow, false);
	// 分组的列
	this.groupHeaderIds = getString(groupHeaderIds, "");
	// this.groupHeaderIds = "pk_branch_showName,degree,sex";
	if (this.groupHeaderIds != "")
		this.groupHeaderIds = this.groupHeaderIds.split(",");
	// 整体是否可以排序
	this.sortable = getBoolean(sortable, true);

	this.pageSize = -1;
	// 是否简易分页条,这种分页条显示时去掉了"刷新","转到","设置"等按钮
	this.simplePageBar = false;
	// 解析grid分页对象
	if (paginationObj != null) {
		this.pageSize = getInteger(paginationObj.pageSize, this.pageSize);
		// 默认后台分页
		this.simplePageBar = getBoolean(paginationObj.simplePageBar, false);
	}

	// 翻页条是否在上面
	this.isPagenationTop = getBoolean(isPagenationTop, true);

	// 是否可显示“显示列”和“锁定列”菜单
	this.showColInfo = getBoolean(showColInfo, true);

	// 判断单双行类型，“0”为单双行各一行交错排列；“1”为单行2行，双行1行交错排列
	this.oddType = getString(oddType, "1");

	// 分组显示时，多选框是否分组
	this.isGroupWithCheckbox = getBoolean(isGroupWithCheckbox, true);
	// 是否显示表头，默认为显示
	this.isShowHeader = getBoolean(isShowHeader, true);

	// 注册外部回掉函数
	window.clickHolders.push(this);

};

/**
 * 销毁控件
 * 
 * @private
 */
GridComp.prototype.destroySelf = function() {
	// 销毁模型
	if (this.model) {
		this.model.destroySelf();
		this.model = null;
	}

	// 销毁排序内容
	// if (this.sortHeaders) {
	// // for (var i = 0, n = this.sortHeaders.length; i < n; i++) {
	// // var sortHeader = this.sortHeaders[i];
	// // sortHeader.dataTable.headerOwner = null;
	// // sortHeader.dataTable = null;
	// // }
	//		
	// this.sortHeaders.clear();
	// this.sortHeaders = null;
	// }
	// if (this.sortHeader) {
	// // this.sortHeader.dataTable.headerOwner = null;
	// // this.sortHeader.dataTable = null;
	// this.sortHeader = null;
	// }
	// 销毁上层表头
	if (this.headers) {
		for (var i = 0, n = this.headers.length; i < n; i++) {
			var header = this.headers[i];
			if (header.dataTable) {
				header.dataTable.headerOwner = null;
				header.dataTable = null;
			}
		}
	}
	// 销毁下层表头
	if (this.basicHeaders) {
		for (var i = 0, n = this.basicHeaders.length; i < n; i++) {
			var basicHeader = this.basicHeaders[i];
			if (basicHeader.dataTable) {
				basicHeader.dataTable.headerOwner = null;
				basicHeader.dataTable = null;
			}
		}
	}
	// 销毁编辑控件
	if (this.compsMap) {
		var comps = this.compsMap.values();
		for (var i = 0; i < comps.length; i++) {
			var comp = comps[i];
			comp.destroySelf();
		}
		this.compsMap.clear();
		this.compsMap = null;
	}
	this.destroy();
};

/**
 * 判断是否为双数行
 * 
 * @private
 */
GridComp.prototype.isOdd = function(index) {
	if (index == null)
		return false;
	if (this.oddType == "0") {
		return index % 2 == 1;
	} else if (this.oddType == "1") {
		return index % 3 != 0;
	}

};

/**
 * 初始化静态常量
 * 
 * @private
 */
GridComp.prototype.initStaticConstant = function() {
	GridComp.HEADERROW_HEIGHT = getCssHeight(this.className
			+ "_HEADERROW_HEIGHT");
	GridComp.ROW_HEIGHT = getCssHeight(this.className + "_ROW_HEIGHT");
	GridComp.SELECTCOLUM_WIDTH = getCssHeight(this.className
			+ "_SELECTCOLUM_WIDTH");
	GridComp.CELL_BOTTOM_BORDER_WIDTH = getCssHeight(this.className
			+ "_CELL_BOTTOM_BORDER_WIDTH");
};

/**
 * 外部click事件发生时的回调函数.用来隐藏当前显示的控件
 * 
 * @private
 */
GridComp.prototype.outsideClick = function(e) {
	if (e && e.calendar)
		return;
	if (this.showComp) {
		if (window.clickHolders.trigger == this.showComp) // 显示参照DIV情况
			return;
		this.hiddenComp();
	}
	this.hideTipMessage(true);
	this.hiddenSettingMenu();
};

/**
 * 外部鼠标滚轮事件发生时的回调函数.用来隐藏当前显示的控件
 * 
 * @private
 */
GridComp.prototype.outsideMouseWheelClick = function(e) {
	e = EventUtil.getEvent();
	if (e && e.calendar)
		return;
	if (this.showComp) {
		if (window.clickHolders.trigger == this.showComp) // 显示参照DIV情况
			return;
		this.hiddenComp();
	}
	this.hiddenSettingMenu();
	this.hideTipMessage(true);
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 创建gird控件的整体外层div
 * 
 * @private
 */
GridComp.prototype.create = function() {
	var oThis = this;
	// 创建最外层包容div
	if (this.outerDiv == null)
		this.initOuterDiv();
	if (this.parent)
		this.placeIn(this.parent);
};

/**
 * 返回grid的显示对象
 */
GridComp.prototype.getObjHtml = function() {
	return this.outerDiv;
};

/**
 * 创建Grid框架各个组成部分
 * 
 * @private
 */
GridComp.prototype.manageSelf = function() {
	var oThis = this;

	// 在此之前必须完成model的初始化工作,否则下面的框架初始化没法完成
	if (this.model == null)
		return;

	if (this.parent.offsetHeight < 10) {
		this.width = 200 + "";
		this.outerDiv.style.height = 200 + "px";
	}
	if (this.parent.offsetWidth < 10) {
		this.width = 450 + "";
		this.outerDiv.style.width = 450 + "px";
	}

	this.paintData();
	// 如果控件可编辑,则创建编辑控件
	if (this.editable) {
		// 缓初始化编辑控件
		setTimeout("GridComp.initEditCompsForGrid('" + this.id + "')", 100);
	}

	// 重新设置自动表头的宽度
	setTimeout("GridComp.processAutoExpandHeadersWidth('" + this.id + "')", 350);
};

/**
 * 数据区点击后执行方法
 * 
 * @private
 */
GridComp.prototype.click = function(e) {
	document.onclick(e);
	// 隐藏已经显示出来的设置按钮
	this.hiddenSettingMenu();
	// grid整体禁用直接返回
	if (this.isGridActive == false)
		return;

	// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
	var cell = this.getRealCell(e);
	var columDiv = cell.parentNode, rowIndex = cell.rowIndex, colIndex = cell.colIndex;

	// 只处理点击动态数据区和静态数据区cell的情况,点击数据区的其他地方不处理
	if (columDiv == null
			|| (columDiv.parentNode != null && columDiv.parentNode.id != "dynamicDataDiv"))
		return;

	// 首先隐藏掉上一个显示出的控件
	if (this.showComp != null) {
		this.hiddenComp();
	}
	
	this.setFocusIndex(rowIndex);

	// 行选中之前调用用户的方法,返回false将不允许点击下一行
	if (this.onBeforeRowSelected(rowIndex, this.getRow(rowIndex)) == false)
		return;

	if (this.isMultiSelWithBox) {
		// // 获取当前所有选中行
		// var selectedRowIndices = this.model.dataset.getSelectedIndices();
		// // 将当前所有选中行变为非选中状态
		// if (selectedRowIndices != null && selectedRowIndices.length > 0) {
		// for ( var i = selectedRowIndices.length - 1; i >= 0; i--)
		// this.model.dataset.setRowUnSelected(selectedRowIndices[i]);
		// }
		// this.processCtrlSel(false, rowIndex);
		
		this.rowSelected(rowIndex);
	} else {
		this.processCtrlSel(false, rowIndex);
	}


	// 不管整体能否编辑都要将点击的cell传给用户,征求用户处理意见(参数:cellItem, rowId,
	// columId)(比如用户想显示一些提示信息)
	if (this.onCellClick(cell, rowIndex, colIndex) == false) {
		stopDefault(e);
		return;
	}

	// 如果当前列可编辑，单元格编辑前调用的方法
	if (this.model.dataset.editable == true
			&& this.basicHeaders[colIndex].columEditable == true) {
		if (this.onBeforeEdit(rowIndex, colIndex) == false) {
			// this.rowSelected(rowIndex);
			return;
		}
	}

	// 调用公有方法激活真正相应cell的控件
	this.setCellSelected(cell, e.ctrlKey);
	// 改变当前点击的cell的外观
	this.changeSelectedCellStyle(rowIndex);

};

/**
 * 数据区的事件处理
 * 
 * @private
 */
GridComp.prototype.attachEvents = function() {
	var oThis = this;

	// 监测整体数据区的双击事件
	this.dataOuterDiv.ondblclick = function(e) {
		e = EventUtil.getEvent();
		// grid整体禁用直接返回
		if (oThis.isGridActive == false)
			return;
		// 整体能编辑不支持双击事件
		if (oThis.editable)
			return;

		// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
		var cell = oThis.getRealCell(e);
		if (cell == null || cell == this) // 点击的是空白区域
			return;
		oThis.onRowDblClick(cell.rowIndex, oThis.getRow(cell.rowIndex));
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	// 阻止事件上传,避免点击鼠标右键显示设置menu
	this.dataOuterDiv.oncontextmenu = function(e) {
		e = EventUtil.getEvent();
		// 先执行点击方法，选中该行
		oThis.click(e);
		var result = oThis.onDataOuterDivContextMenu(e);
		if (result == false)
			stopAll(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	// this.fixedColumDiv.oncontextmenu = function(e) {
	// e = EventUtil.getEvent();
	// stopAll(e);
	// // 删除事件对象（用于清除依赖关系）
	// clearEventSimply(e);
	// };

	// 监测整体数据区的点击事件
	this.dataOuterDiv.onclick = function(e) {
		e = EventUtil.getEvent();
		// 点击执行方法
		oThis.click(e);
		oThis.hideTipMessage(true);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	// 增加属性，以便使firefox响应onkeydown事件
	this.dataOuterDiv.setAttribute("tabindex", 0);
	// 监测整体数据区键盘事件
	this.dataOuterDiv.onkeydown = function(e) {
		e = EventUtil.getEvent();
		// grid整体禁用直接返回
		if (oThis.isGridActive == false) {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}

		// 首先隐藏掉上一个显示出的控件
		if (oThis.showComp != null)
			oThis.hiddenComp();

		var cell = oThis.selectedCell;
		if (cell == null)
			cell = getTarget(e);

		var ch = e.key;
		// 当前没有选中的单元格，直接返回
		if (cell.tagName.toLowerCase() != "div") {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}

		var rowIndex = cell.rowIndex;
		var colIndex = cell.colIndex;

		// 下移
		if (ch == 40) {
			if (!this.isMultiSelWithBox) { // 单选表格
				if (rowIndex == null) { // 当前没有选中单元格
					var selIndexs = oThis.getSelectedRowIndice();
					if (selIndexs == null || selIndexs.length == 0)
						rowIndex = -1;
					else
						rowIndex = selIndexs[0];
					// 选中下一行
					oThis.model.setRowSelected(rowIndex + 1);
				} else if (rowIndex + 1 <= oThis.getRowsNum() - 1) {
					// 选中下一行单元格
					if (oThis.paintedSign[rowIndex + 1] == 0)
						oThis.dataOuterDiv.scrollTop += oThis.rowHeight;
					cell = oThis.getCell(rowIndex + 1, colIndex);
					oThis.setCellSelected(cell);
					// 选中下一行
					oThis.model.setRowSelected(rowIndex + 1);
				}
			}
		}
		// 上移
		else if (ch == 38) {
			if (!this.isMultiSelWithBox) { // 单选表格
				if (rowIndex == null) { // 当前没有选中单元格
					var selIndexs = oThis.getSelectedRowIndice();
					if (selIndexs == null || selIndexs.length == 0) {
						// 删除事件对象（用于清除依赖关系）
						clearEventSimply(e);
						return;
					} else
						rowIndex = selIndexs[0];
					// 选中下一行
					if (rowIndex > 0)
						oThis.model.setRowSelected(rowIndex - 1);
				} else if (rowIndex > 0) {
					// 选中上一行单元格
					cell = oThis.getCell(rowIndex - 1, colIndex);
					oThis.setCellSelected(cell);
					// 选中上一行
					oThis.model.setRowSelected(rowIndex - 1);
				}
			}
		}
		// 左移
		else if (ch == 37) {
			if (!this.isMultiSelWithBox && rowIndex != null) { // 单选表格
				var tmpCell = oThis.getCell(rowIndex, colIndex - 1);
				if (tmpCell == null)
					tmpCell = oThis.getVisibleCellByDirection(cell, -1);
				if (tmpCell != null) {
					oThis.setCellSelected(tmpCell);
					if (cell.rowIndex != tmpCell.rowIndex) // 选中新行
						oThis.model.setRowSelected(tmpCell.rowIndex);
					// 改变当前点击的cell的外观
					oThis.changeSelectedCellStyle(tmpCell.rowIndex);
				}
			}
		}
		// 右移
		else if (ch == 39) {
			if (!this.isMultiSelWithBox && rowIndex != null) { // 单选表格
				var tmpCell = oThis.getCell(rowIndex, colIndex + 1);
				if (tmpCell == null) {
					if (rowIndex + 1 <= oThis.getRowsNum() - 1) {
						if (oThis.paintedSign[rowIndex + 1] == 0)
							oThis.dataOuterDiv.scrollTop += oThis.rowHeight;
					}
					tmpCell = oThis.getVisibleCellByDirection(cell, 1);
				}
				if (tmpCell != null) {
					oThis.setCellSelected(tmpCell);
					if (cell.rowIndex != tmpCell.rowIndex) // 选中新行
						oThis.model.setRowSelected(tmpCell.rowIndex);
					// 改变当前点击的cell的外观
					oThis.changeSelectedCellStyle(tmpCell.rowIndex);
				}
			}
		}
		// 开放Ctrl C
		if (!e.ctrlKey) {
			stopDefault(e);
		}
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	// 固定列的双击事件(改变整体的行的外观)
	this.fixedColumDiv.ondblclick = function(e) {
		// grid整体禁用直接返回
		if (oThis.isGridActive == false)
			return;
		// 整体能编辑不支持双击事件
		if (oThis.editable)
			return;

		e = EventUtil.getEvent();

		// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
		var cell = oThis.getRealCell(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		if (cell == null || cell == this) { // 点击的是空白区域
			return;
		}
		oThis.onRowDblClick(cell.rowIndex, oThis.getRow(cell.rowIndex));
	};

	// 固定列的点击事件(改变整体的行的外观)
	this.fixedColumDiv.onclick = function(e) {

		// 隐藏已经显示出来的设置按钮
		oThis.hiddenSettingMenu();

		// grid整体禁用直接返回
		if (oThis.isGridActive == false)
			return;

		e = EventUtil.getEvent();

		// 得到真正的cell对象(render的时候用户可能在cell中加上自己的div,所以点击的不一定是cell)
		var cell = oThis.getRealCell(e);
		// 点击行标区直接返回
		if (cell.id == "rowNumDiv" || cell.id == "fixedColum") {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}

		// 点击行标div,行状态div直接返回
		if (cell.parentNode.id.startWith("numline")
				|| cell.id.startWith("numline")
				|| cell.parentNode.id.startWith("sumRowDiv")
				|| cell.id.startWith("sumRowDiv")
				|| cell.parentNode.id == "lineStateColumDiv") {
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
			return;
		}
		// 改变当前选中行的外观
		var columDiv = cell.parentNode;
		var rowIndex = cell.rowIndex;
		var colIndex = cell.colIndex;

		// 点击的是固定选择列
		if (columDiv.id == "fixedSelectColum") {

		} else {
			// 首先隐藏掉上一个显示出的控件
			if (oThis.showComp != null)
				oThis.hiddenComp();

			// 行选中之前调用用户的方法
			if (oThis.onBeforeRowSelected(rowIndex, oThis.getRow(rowIndex)) == false) {
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return;
			}

			// 不管整体能否编辑都要将点击的cell传给用户,征求用户处理意见(参数:cellItem, rowId,
			// columId)(比如用户想显示一些提示信息)
			if (oThis.onCellClick(cell, rowIndex, colIndex) == false) {
				// 多选模式下只改变选中行的外观
				if (oThis.isMultiSelWithBox) {
					// oThis.rowSelected(rowIndex);

				} else {
					oThis.processCtrlSel(false, rowIndex);
				}
				stopDefault(e);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return;
			}

			// 单元格编辑前调用的方法
			if (oThis.onBeforeEdit(rowIndex, colIndex) == false) {
				oThis.rowSelected(rowIndex);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
				return;
			}

			// 调用公有方法激活真正相应cell的控件
			oThis.setCellSelected(cell, e.ctrlKey);

			if (columDiv.parentNode.id == "fixedDataDiv") {
				// 多选模式下只改变选中行的外观
				if (oThis.isMultiSelWithBox)
					oThis.rowSelected(rowIndex);
				else {
					oThis.processCtrlSel(false, rowIndex);
				}
			}
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
			clearEventSimply(e);
		}
	};

	// 滚动事件,使固定列固定
	this.dataOuterDiv.onscroll = function(e) {
		e = EventUtil.getEvent();

		// 滚动时的缓加载数据
		e.triggerObj = oThis;
		handleScrollEvent(e);

		// 滚动时隐藏掉当前显示的控件
		if (oThis.showComp != null) {
			if (IS_IE || oThis.autoScroll != true)
				oThis.hiddenComp();
			else
				// firefox的程序自动设置scroll时，不隐藏编辑控件
				oThis.autoScroll = false;
		}

		var src = getTarget(e);
		var iScrollTop = src.scrollTop;
		var iScrollLeft = src.scrollLeft;
		var headerDivStl = oThis.headerDiv.style;
		var fixedColDivStl = oThis.fixedColumDiv.style;
		headerDivStl.width = (oThis.headerDiv.defaultWidth + iScrollLeft)
				+ "px";
		headerDivStl.left = iScrollLeft * (-1) + "px";
		// 固定列div的移动
		// fixedColDivStl.height = oThis.fixedColumDiv.defaultHeight +
		// iScrollTop;
		fixedColDivStl.height = (oThis.fixedColumDiv.defaultHeight + iScrollTop)
				+ "px";
		if (oThis.pageSize > 0 && oThis.isPagenationTop == true)
			fixedColDivStl.top = (oThis.constant.headerHeight
					+ GridComp.PAGEBAR_HEIGHT + iScrollTop * (-1))
					+ "px";
		else
			fixedColDivStl.top = (oThis.constant.headerHeight + iScrollTop
					* (-1))
					+ "px";
		// 行状态列的高度调整
		if (oThis.isShowSumRow)
			oThis.lineStateColumDiv.style.height = (oThis.lineStateColumDiv.defaultHeight + iScrollTop)
					+ "px";
		// 动态列headerDiv
		if (oThis.dynamicHeaderDiv.defaultWidth + iScrollLeft > 0)
			oThis.dynamicHeaderDiv.style.width = (oThis.dynamicHeaderDiv.defaultWidth + iScrollLeft)
					+ "px";
		// 合计行的div,"合计"
		if (oThis.sumRowDiv) {
			// gd
			// oThis.sumRowDiv.style.top = oThis.sumRowDiv.defaultTop +
			// iScrollTop;
			oThis.dynSumRowDataDiv.style.left = (parseFloat(oThis.dynSumRowDataDiv.defaultLeft) + iScrollLeft
					* (-1))
					+ "px";
		}

		// 固定头div的移动
		oThis.fixedHeaderDiv.style.left = iScrollLeft + "px";
		// 删除事件对象（用于清除依赖关系）
		// clearEvent(e);
		e.triggerObj = null;
		clearEventSimply(e);
	};

	// 增加统一的resize事件处理必须给对象一个id
	this.outerDiv.id = oThis.id + "_outerdiv";
	// 给outerDiv增加onresize方法
	addResizeEvent(this.outerDiv, function() {
		GridComp.gridResize(oThis.id);
	})
};

/**
 * 设置dataOuterDiv的横向滚动条位置
 * 
 * @private
 */
GridComp.prototype.setScrollLeft = function(scrollLeft) {
	// 系统自动调整标志（非手工拖动）
	if (!IS_IE)
		this.autoScroll = true;
	this.dataOuterDiv.scrollLeft = scrollLeft;
};

/**
 * 设置dataOuterDiv的纵向滚动条位置
 * 
 * @private
 */
GridComp.prototype.setScrollTop = function(scrollTop) {
	// 系统自动调整标志（非手工拖动）
	if (!IS_IE)
		this.autoScroll = true;
	this.dataOuterDiv.scrollTop = scrollTop;
};

/**
 * grid自动调整大小逻辑
 * 
 * @param gridId
 *            当前调整大小grid的id
 * @private
 */
GridComp.gridResize = function(gridId) {
	var grid = window.objects[gridId];
	
	if (grid == null)
		return;
	var outerDiv = grid.outerDiv, barHeight = GridComp.SCROLLBAE_HEIGHT;
	if(outerDiv.style.height != '100%'){
		outerDiv.style.height = "100%";
	}
	grid.height = "100%";
	if(outerDiv.style.width != '100%'){
		outerDiv.style.width = "100%";
	}	
	grid.width = '100%';
		
	try {
		// 如果此时grid大小和上次不发生变化,则不会真正的重新调整grid大小
		var cond1 = (grid.constant.outerDivWidth != null && grid.constant.outerDivWidth == outerDiv.offsetWidth);
		var cond2 = (grid.constant.outerDivHeight != null && grid.constant.outerDivHeight == outerDiv.offsetHeight);
		if (cond1 && cond2)
			return;

		grid.constant.outerDivWidth = outerDiv.offsetWidth;
		grid.constant.outerDivHeight = outerDiv.offsetHeight;
		grid.constant.fixedHeaderDivWidth = grid.fixedHeaderDiv.offsetWidth;

		if (grid.width.indexOf("%") != -1) {
			var fixedHeaderWidth = grid.constant.fixedHeaderDivWidth;
			var currWidth = grid.constant.outerDivWidth;
			grid.dataOuterDiv.style.width = currWidth + "px";// - 1;

			// 垂直滚动
			if (grid.isVScroll()) {
				grid.headerDiv.style.width = (currWidth - barHeight) + "px";// - 1;
				grid.headerDiv.defaultWidth = currWidth - barHeight;// - 1;

				var dynHeaderWidth = currWidth - fixedHeaderWidth - barHeight;// - 2;
				if (dynHeaderWidth > 0)
					grid.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
				grid.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
			} else {
				grid.headerDiv.style.width = currWidth + "px";// - 2;
				grid.headerDiv.defaultWidth = currWidth;// - 2;

				var dynHeaderWidth = currWidth - fixedHeaderWidth;// - 3;
				if (dynHeaderWidth > 0)
					grid.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
				grid.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
			}
		}
		if (grid.height.indexOf("%") != -1) {
			// 减掉分页栏的高度
			if (grid.pageSize > 0)
				grid.dataOuterDiv.style.height = (grid.outerDiv.offsetHeight
						- 1 - GridComp.PAGEBAR_HEIGHT)
						+ "px";
			else
				grid.dataOuterDiv.style.height = (grid.outerDiv.offsetHeight - 1)
						+ "px";

			// 减掉合计行的高度
			if (grid.isShowSumRow)
				grid.dataOuterDiv.style.height = (parseInt(grid.dataOuterDiv.style.height) - GridComp.ROW_HEIGHT)
						+ "px";

			grid.adjustFixedColumDivHeight();			
//			var currheight = grid.constant.outerDivHeight;
//			if (grid.isDataDivScroll()) {
//				var temp = currheight - barHeight - grid.constant.headerHeight;
//				if (grid.pageSize > 0)
//					temp -= GridComp.PAGEBAR_HEIGHT;
//				grid.fixedColumDiv.style.height = temp + "px";
//				grid.fixedColumDiv.defaultHeight = temp;
//			} else {
//				var temp = currheight - grid.constant.headerHeight;
//				if (grid.pageSize > 0)
//					temp -= GridComp.PAGEBAR_HEIGHT;
//				grid.fixedColumDiv.style.height = temp + "px";
//				grid.fixedColumDiv.defaultHeight = temp;
//			}
		}
		grid.setScrollLeft(0);
		// 重画区域
		grid.paintZone();
		// 调整合计行的top
		if (grid.isShowSumRow) {
			if (grid.pageSize > 0 && grid.isPagenationTop == false) // 有分页条并且分页条在下方
				grid.sumRowDiv.style.top = (grid.constant.outerDivHeight
						- GridComp.SCROLLBAE_HEIGHT - GridComp.PAGEBAR_HEIGHT - 20)
						+ "px";
			else
				grid.sumRowDiv.style.top = (grid.constant.outerDivHeight
						- GridComp.SCROLLBAE_HEIGHT - 12)
						+ "px";

			if (grid.pageSize > 0 && grid.isPagenationTop == false) // 有分页条并且分页条在下方
				grid.dynSumRowContentDiv.style.top = (grid.constant.outerDivHeight
						- barHeight - GridComp.PAGEBAR_HEIGHT - 20)
						+ "px";
			else
				grid.dynSumRowContentDiv.style.top = (grid.constant.outerDivHeight
						- barHeight - 12)
						+ "px";

			// 调整放置合计行的div的宽度
			grid.dynSumRowContentDiv.style.width = (grid.dynamicHeaderDiv.offsetWidth + 4)
					+ "px";
		}

		// 调整自动扩展表头的宽度,采用缓画,只有gridResize结束后才调整表头自动扩展列的宽度
		if (grid.stForAutoExpand != null)
			clearTimeout(grid.stForAutoExpand);
		grid.stForAutoExpand = setTimeout(
				"GridComp.processAutoExpandHeadersWidth('" + gridId + "')", 100);

		// 重新调整翻页工具条位置
		GridComp.adjustPaginationBar(grid);
	} catch (e) {

	}
};

/**
 * grid重新调整翻页工具条位置
 * 
 * @private
 */
GridComp.adjustPaginationBar = function(grid) {
	if (grid.pageSize > 0 && !grid.isPagenationTop) {
		var tempH = grid.dataOuterDiv.style.height;
		if (tempH != "") {
			tempH = tempH.replace("px", "");
			grid.dataOuterDiv.style.height = tempH - 15 + "px";
		}
		grid.paginationBar.style.top = grid.outerDiv.offsetHeight
				- GridComp.PAGEBAR_HEIGHT - 15 + "px";
	}
};

/**
 * 得到真正的cell对象(render的时候用户可能在cell中加上自己的div，所以点击的不一定是cell)
 * 
 * @private
 */
GridComp.prototype.getRealCell = function(e) {
	var cell = getTarget(e);
	if (cell.editorType == null) {
		var pNode = cell.parentNode;
		while (pNode != null) {
			if (pNode.editorType != null) {
				cell = pNode;
				break;
			}
			pNode = pNode.parentNode;
		}
	}
	return cell;
};

/**
 * 设置grid数据行单行高度
 * 
 * @param{Integer} height
 * @private
 */
GridComp.prototype.setRowHeight = function(height) {
	height = parseInt(height);
	if (height < 10)
		height = 10;
	this.rowHeight = height;
};

/**
 * 设置grid表头单行高度
 * 
 * @param{Integer} height
 * @private
 */
GridComp.prototype.setHeaderRowHeight = function(height) {
	height = parseInt(height);
	if (height < 10)
		height = 10;
	this.headerRowHeight = height;
};

/**
 * 根据行,列索引得到cell 注意:如果此列是隐藏列则继续向下获取不隐藏的最近的一个 cell,此cell可能位于下一行中
 * 
 * @param rowIndex
 *            行索引值
 * @param colIndex
 *            列索引值
 */
GridComp.prototype.getCell = function(rowIndex, colIndex) {
	rowIndex = parseInt(rowIndex);
	colIndex = parseInt(colIndex);

	if (rowIndex < 0 || rowIndex > this.getRowsNum() - 1)
		return null;
	if (colIndex < 0 || colIndex > this.basicHeaders.length - 1)
		return null;
	if (this.basicHeaders[colIndex] != null
			&& this.basicHeaders[colIndex].isHidden == false)
		return this.basicHeaders[colIndex].dataDiv.cells[rowIndex];
	return null;
};

/**
 * 获取该cell的前一个或者上一个可见cell
 * 
 * @param direction
 *            -1,向左;1,向右
 * @private
 */
GridComp.prototype.getVisibleCellByDirection = function(cell, direction) {
	var rowIndex = cell.rowIndex;
	var colIndex = cell.colIndex;
	// 尝试获取下一个可见cell
	if (direction == 1) {
		// 首先获取本行内的下一个可见cell
		for (var j = colIndex + 1, count = this.basicHeaders.length; j < count; j++) {
			if (this.basicHeaders[j].isHidden == false)
				return this.basicHeaders[j].dataDiv.cells[rowIndex];
		}

		// 如果本行内没有了可见cell,则继续向下面的行搜寻可见cell
		for (var i = rowIndex + 1, rowNum = this.getRowsNum(); i < rowNum; i++) {
			for (var j = 0, count = this.basicHeaders.length; j < count; j++) {
				if (this.basicHeaders[j].isHidden == false)
					return this.basicHeaders[j].dataDiv.cells[i];
			}
		}
	}
	// 尝试获取上一个可见cell
	else if (direction == -1) {
		for (var j = colIndex - 1; j >= 0; j--) {
			if (this.basicHeaders[j].isHidden == false)
				return this.basicHeaders[j].dataDiv.cells[rowIndex];
		}

		for (var i = rowIndex - 1; i >= 0; i--) {
			for (var j = this.basicHeaders.length - 1; j >= 0; j--) {
				if (this.basicHeaders[j].isHidden == false)
					return this.basicHeaders[j].dataDiv.cells[i];
			}
		}
	}
	return null;
};

/**
 * 获取该cell的前一个或者上一个可编辑cell
 * 
 * @param direction
 *            -1,向左;1,向右
 * @private
 */
GridComp.prototype.getEditableCellByDirection = function(cell, direction) {
	if (this.editable == false)
		return null;
	var rowIndex = cell.rowIndex;
	var colIndex = cell.colIndex;
	// 尝试获取下一个可见cell
	if (direction == 1) {
		// 首先获取本行内的下一个可见cell
		for (var j = colIndex + 1, count = this.basicHeaders.length; j < count; j++) {
			if (this.basicHeaders[j].isHidden == false
					&& this.basicHeaders[j].columEditable == true
					&& this.onBeforeEdit(rowIndex, j) != false)
				return this.basicHeaders[j].dataDiv.cells[rowIndex];
		}

		// 如果本行内没有了可见cell,则继续向下面的行搜寻可见cell
		for (var i = rowIndex + 1, rowNum = this.getRowsNum(); i < rowNum; i++) {
			for (var j = 0, count = this.basicHeaders.length; j < count; j++) {
				if (this.basicHeaders[j].isHidden == false
						&& this.basicHeaders[j].columEditable == true
						&& this.onBeforeEdit(i, j) != false)
					return this.basicHeaders[j].dataDiv.cells[i];
			}
		}
	}
	// 尝试获取上一个可见cell
	else if (direction == -1) {
		for (var j = colIndex - 1; j >= 0; j--) {
			if (this.basicHeaders[j].isHidden == false
					&& this.basicHeaders[j].columEditable == true
					&& this.onBeforeEdit(rowIndex, j) != false)
				return this.basicHeaders[j].dataDiv.cells[rowIndex];
		}

		for (var i = rowIndex - 1; i >= 0; i--) {
			for (var j = this.basicHeaders.length - 1; j >= 0; j--) {
				if (this.basicHeaders[j].isHidden == false
						&& this.basicHeaders[j].columEditable == true
						&& this.onBeforeEdit(i, j) != false)
					return this.basicHeaders[j].dataDiv.cells[i];
			}
		}
	}
	return null;
};

/**
 * 设置cell选中，同时让隐藏的cell显示出来，如果该列能够编辑则激活相应的编辑控件
 * 若选择模式为单选模式则同时通知model行选中，多行选择模式下如果是checkbox多选仅改变行的选中外观，如果是ctrl多行则改变选中的多行
 * 
 * @private
 */
GridComp.prototype.setCellSelected = function(cell, ctrl, shift) {
	if (cell == null)
		return;

	var cellClassName = cell.className;
	// 第一次没有选中的cell
	if (this.oldCell == null) {
		if (this.basicHeaders[cell.colIndex] != null
				&& this.basicHeaders[cell.colIndex].isFixedHeader)
			cell.className += " fixedcell_select";
		else
			cell.className += " cell_select";
		this.oldCell = cell;
		this.oldClassName = cellClassName;
	}
	// 有选中的cell了
	else {
		// 选中cell和当前cell不是同一个cell
		if (this.oldCell != cell) {
			this.oldCell.className = this.oldClassName;
			var isOdd = this.isOdd(this.oldCell.rowIndex);
			var curHeader = this.basicHeaders[this.oldCell.colIndex];

			if (!this.isMultiSelWithBox) {
				if (this.selectedRowIndice != null
						&& this.selectedRowIndice
								.indexOf(this.oldCell.rowIndex) != -1) {
					if (curHeader != null && curHeader.isFixedHeader)
						this.oldCell.className = isOdd
								? "fixed_gridcell_odd fixedcell_select"
								: "fixed_gridcell_even fixedcell_select";
					else
						this.oldCell.className = isOdd
								? "gridcell_odd cell_select"
								: "gridcell_even cell_select";
				} else {
					if (curHeader != null && curHeader.isFixedHeader)
						this.oldCell.className = isOdd
								? "fixed_gridcell_odd"
								: "fixed_gridcell_even";
					else
						this.oldCell.className = isOdd
								? "gridcell_odd"
								: "gridcell_even";
				}
			} else {
				if (curHeader != null && curHeader.isFixedHeader)
					this.oldCell.className = isOdd
							? "fixed_gridcell_odd"
							: "fixed_gridcell_even";
				else
					this.oldCell.className = isOdd
							? "gridcell_odd"
							: "gridcell_even";
			}

			this.oldCell = cell;
			this.oldClassName = cellClassName;
		}
	}

	// 记录当前选中的cell
	this.selectedCell = cell;
	// 让隐藏的cell显示出来
	this.letCellVisible(cell);
	// 如果该列可以编辑设置cell编辑控件激活
	if (this.editable == true
			&& this.basicHeaders[cell.colIndex].columEditable == true)
		this.setCellActive(cell);
	// 如果列不能编辑设置cell的焦点,注意该处必须设置焦点,避免cell失去焦点而导致不能接收键盘事件
	else
		cell.focus();

		// // 设置行选中
		// if (this.isMultiSelWithBox == false)
		// this.processCtrlSel(ctrl, cell.rowIndex);
		// else {
		// // 获取当前所有选中行
		// var selectedRowIndices = this.model.dataset.getSelectedIndices();
		// // 将当前所有选中行变为非选中状态
		// if (selectedRowIndices != null && selectedRowIndices.length > 0) {
		// for ( var i = selectedRowIndices.length - 1; i >= 0; i--)
		// this.model.dataset.setRowUnSelected(selectedRowIndices[i]);
		// }
		// this.processCtrlSel(false, cell.rowIndex);
		// // this.rowSelected(cell.rowIndex);
		// }
};

/**
 * 激活某个cell的编辑控件
 * 
 * @param cell
 *            当前选中的表格单元格
 * @param ctrl
 *            当前是否按着ctrl键操作,按着ctrl键不激活相应控件
 * @private
 */
GridComp.prototype.setCellActive = function(cell, ctrl) {
	if (this.editable == false)
		return;

	if (ctrl)
		return;

	// 记录选中cell为当前cell
	this.selectedCell = cell;
	var rowIndex = cell.rowIndex;
	var colIndex = cell.colIndex;
	// 用户自定义editor
	if (this.getCellEditor(cell, rowIndex, colIndex) != null) {
		var comp = this.getCellEditor(cell, rowIndex, colIndex);
		comp
				.setBounds(compOffsetLeft(cell, document.body) + 1,
						compOffsetTop(cell, document.body), cell.offsetWidth
								- 1, GridComp.ROW_HEIGHT);
		comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
		comp.showV();
		comp.setFocus();
		this.currActivedCell = cell;
		this.showComp = comp;
	} else {
		// 如果comp为null,得到stringtext
		if (cell.editorType == null || cell.editorType == "") {
			var comp = this.compsMap.get(EditorType.STRINGTEXT);
			comp.setBounds(compOffsetLeft(cell, document.body) + 1,
					compOffsetTop(cell, document.body), cell.offsetWidth - 1,
					cell.offsetHeight);
			// 将真实值设置到编辑控件中
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.setFocus();
			this.currActivedCell = cell;
			this.showComp = comp;
		} else if (cell.editorType != EditorType.CHECKBOX
				&& cell.editorType != EditorType.COMBOBOX
				&& cell.editorType != EditorType.REFERENCE
				&& cell.editorType != EditorType.TEXTAREA) {
			var comp = this.compsMap.get(cell.editorType);
			var header = cell.parentNode.header;

			if (cell.editorType == EditorType.STRINGTEXT)
				comp.setMaxSize(header.maxLength);
			// 若为数字类型根据header的precision属性设置精度,如果有最大最小值则进行设置
			if (cell.editorType == EditorType.DECIMALTEXT) {
				comp.setPrecision(header.precision);
				if (header.floatMinValue != null)
					comp.setMinValue(header.floatMinValue);
				if (header.floatMaxValue != null)
					comp.setMaxValue(header.floatMaxValue);
			}
			// 若为整数类型根据header的maxValue和minValue设置最大最小值
			if (cell.editorType == EditorType.INTEGERTEXT) {
				comp.setIntegerMinValue(header.integerMinValue);
				comp.setIntegerMaxValue(header.integerMaxValue);
			}
			comp.setBounds(compOffsetLeft(cell, document.body) + 1,
					compOffsetTop(cell, document.body), cell.offsetWidth - 1,
					cell.offsetHeight);
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.setFocus();
			this.currActivedCell = cell;
			this.showComp = comp;
		}
		// 处理下拉框类型
		else if (cell.editorType == EditorType.COMBOBOX) {
			var comp = this.compsMap.get(cell.editorType + colIndex);
			var header = this.basicHeaders[colIndex];
			comp.setBounds(compOffsetLeft(cell, document.body) + 1,
					compOffsetTop(cell, document.body), cell.offsetWidth - 1,
					cell.offsetHeight);
			// 将记录的上次的旧值清空
			if (comp.oldValue)
				comp.oldValue = null;
			comp.showV();
			comp.setFocus();
			comp.nowCell = cell;

			var selInd = -1;
			if (header.comboData != null) {
				var keyValues = header.comboData.getValueArray();
				selInd = keyValues.indexOf(this.model.getCellValueByIndex(
						rowIndex, colIndex));
			}
			comp.setSelectedItem(selInd);
			this.currActivedCell = cell;
			this.showComp = comp;

		}
		// 处理参照
		else if (cell.editorType == EditorType.REFERENCE) {
			var comp = this.compsMap.get(cell.editorType + cell.colIndex);
			comp.setBounds(compOffsetLeft(cell, document.body) + 1,
					compOffsetTop(cell, document.body), cell.offsetWidth - 1,
					cell.offsetHeight);
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.setFocus();
			this.currActivedCell = cell;
			this.showComp = comp;
		}
		// 文本域类型
		else if (cell.editorType == EditorType.TEXTAREA) {
			var comp = this.compsMap.get(cell.editorType);
			comp.setBounds(compOffsetLeft(cell, document.body) + 1,
					compOffsetTop(cell, document.body) - 1, cell.offsetWidth
							- 1, cell.offsetHeight);
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.setFocus();
			this.currActivedCell = cell;
			this.showComp = comp;
		}
		// 处理复选框(checkbox)类型
		else if (cell.editorType == EditorType.CHECKBOX) {
			var comp = this.compsMap.get(cell.editorType);
			comp.setBounds(compOffsetLeft(cell, document.body)
					+ parseInt(cell.offsetWidth / 2) - 10, compOffsetTop(cell,
					document.body)
					+ 1, cell.offsetWidth - 1, cell.offsetHeight);
			var header = this.basicHeaders[cell.colIndex];
			if (header.valuePair != null)
				comp.setValuePair(header.valuePair);
			comp.setValue(this.model.getCellValueByIndex(rowIndex, colIndex));
			comp.showV();
			comp.nowCell = cell;
			this.currActivedCell = cell;
			this.showComp = comp;
		}
		// 在弹出窗口中，激活某个cell的编辑控件，弹出窗口会遮住comp
		if (this.showComp)
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。		
//			this.showComp.Div_gen.style.zIndex = 100000;
			this.showComp.Div_gen.style.zIndex = getZIndex();
	}
};

/**
 * 若cell处于部分隐藏状态，则将此cell全部显示出来
 * 
 * @param cell
 *            要全部显示出来的cell
 * @private
 */
GridComp.prototype.letCellVisible = function(cell) {
	if (cell == null)
		return;
	// 处理左右隐藏的情况
	// 该cell之前所有显示的表头的宽度
	var preHeadersWidth = this.getPreHeadersWidth(cell);
	var columDiv = cell.parentNode;
	var rowIndex = cell.rowIndex;
	var iScrollLeft = this.dataOuterDiv.scrollLeft;
	var flag = true;

	if (preHeadersWidth == 0) {
		this.setScrollLeft(0); // 1px为修正cell边框显示重合问题
		flag = false;
	} else if (iScrollLeft > preHeadersWidth) {
		var deltX = iScrollLeft - preHeadersWidth;
		this.setScrollLeft(iScrollLeft - deltX - 1); // 1px为修正cell边框显示重合问题
		flag = false;
	}

	var gridWidth = this.constant.outerDivWidth;
	var gridHeight = this.constant.outerDivHeight;
	if (this.pageSize > 0) // 有分页条，要将其高度减去
		gridHeight -= GridComp.PAGEBAR_HEIGHT;
	if (flag) {
		var realWidth = columDiv.offsetLeft + cell.offsetWidth;
		var currWidth = gridWidth - this.constant.fixedColumDivWidth
				+ iScrollLeft;
		if (realWidth > currWidth) {
			var deltX = realWidth - currWidth;
			if (this.isVScroll()) {
				this.setScrollLeft(iScrollLeft + deltX + 1
						+ GridComp.SCROLLBAE_HEIGHT);
			} else {
				this.setScrollLeft(iScrollLeft + deltX + 1);
			}
		}
	}

	// 处理上下隐藏的情况
	var preRowsHeight = rowIndex * this.rowHeight;
	var iScrollTop = this.dataOuterDiv.scrollTop;
	if (iScrollTop > preRowsHeight) {
		var deltY = iScrollTop - preRowsHeight;
		this.setScrollTop(iScrollTop - deltY);
	}
	var realHeight = (rowIndex + 1) * this.rowHeight;
	if (this.isScroll())
		currHeight = gridHeight - this.constant.headerHeight + iScrollTop
				- GridComp.SCROLLBAE_HEIGHT;
	else
		currHeight = gridHeight - this.constant.headerHeight + iScrollTop;
	if (realHeight > currHeight) {
		var deltY = realHeight - currHeight;
		this.setScrollTop(iScrollTop + deltY);
	}
};

/**
 * 处理按住ctrl并且选行时的逻辑
 * 
 * @ctrl 是否按住ctrl键点击当前行
 * @private
 */
GridComp.prototype.processCtrlSel = function(ctrl, rowIndex) {

	// 如果此时有多行选中,则清除所有选中行,然后选中当前行
	if (this.selectedRowIndice != null && this.selectedRowIndice.length > 1) {
		// this.clearAllUISelRows();
		// 选中当前行
		this.model.setRowSelected(rowIndex);
	} else {
		// 如果点击的cell处在选中行上,并且该行已经选中则不再通知model行选中 gd 2007-12-05
		if (this.selectedRowIndice == null
				|| (this.selectedRowIndice.length > 0 && this.selectedRowIndice[0] != rowIndex))
			this.model.setRowSelected(rowIndex);
		else
			this.rowSelected(rowIndex);
	}
};

/**
 * 设置当前聚焦行（点中行）
 * 
 * @private
 */
GridComp.prototype.setFocusIndex = function(rowIndex) {
	this.model.setFocusIndex(rowIndex);
};

/**
 * 获取当前聚焦行（点中行）
 * 
 * @private
 */
GridComp.prototype.getFocusIndex = function() {
	this.model.getFocusIndex();
};

/**
 * 清除所有选中行外观
 * 
 * @private
 */
GridComp.prototype.clearAllUISelRows = function() {
	var selRowsIndice = this.selectedRowIndice;
	if (selRowsIndice != null && selRowsIndice.length > 0) {
		// 所有选中行
		var selIndice = [];
		for (var i = 0, count = selRowsIndice.length; i < count; i++)
			selIndice.push(this.selectedRowIndice[i]);

		// 清除所有选中行的外观
		for (var i = 0, count = selIndice.length; i < count; i++) {
			if (this.isMultiSelWithBox == false) {
				var index = selIndice[i];
				for (var j = 0, headerLength = this.basicHeaders.length; j < headerLength; j++) {
					var header = this.basicHeaders[j];
					if (header.isHidden == false) {
						var cell = header.dataDiv.cells[index];
						if (cell != null) {
							var isOdd = this.isOdd(index);
							// 校验不通过的的字段颜色不清除掉
							if (cell.isErrorCell) {
								if (header.isFixedHeader)
									cell.className = isOdd
											? "fixed_gridcell_odd cell_error"
											: "fixed_gridcell_even cell_error";
								else
									cell.className = isOdd
											? "gridcell_odd cell_error"
											: "gridcell_even cell_error";
							} else {
								if (header.isFixedHeader)
									cell.className = isOdd
											? "fixed_gridcell_odd"
											: "fixed_gridcell_even";
								else
									cell.className = isOdd
											? "gridcell_odd"
											: "gridcell_even";
							}
						}
					}
				}

				// 设置此行状态div的背景
				var node = this.lineStateColumDiv.cells[index];
				if (node != null && node.className != "row_state_div row_update_state"
						&& node.className != "row_state_div row_add_state")
					node.className = "row_state_div";

				// 从选择数组中将此选择行删除
				this.selectedRowIndice.splice(i, 1);
				// 没有选中行将选中数组置空
				if (this.selectedRowIndice.length == 0)
					this.selectedRowIndice = null;
			}
		}
	}
};

/**
 * 初始化grid中的各个框架结构
 * 
 * @private
 */
GridComp.prototype.initDivs = function() {
	if (this.pageSize > 0)
		this.initPaginationBar();
	// 创建表头区整体div
	this.initHeaderDiv();
	// 创建固定表头区整体div
	this.initFixedHeaderDiv();
	if (this.isShowNumCol) {
		// 创建行号列表头div
		this.initRowNumHeaderDiv();
	} else
		this.constant.rowNumHeaderDivWidth = 0;

	// 创建表格行状态显示列header
	this.initLineStateHeaderDiv();
	// 创建固定表头区固定选择列
	if (this.isMultiSelWithBox)
		this.initSelectColumHeaderDiv();
	// 创建固定表头区header数据区div
	this.initFixedHeaderTableDiv();
	// 创建动态表头区整体div
	this.initDynamicHeaderDiv();
	// 创建动态表头区header数据区div
	this.initDynamicHeaderTableDiv();
	// 创建数据区整体div
	this.initDataOuterDiv();
	// 创建固定列整体div
	this.initFixedColumDiv();
	if (this.isShowNumCol) {
		// 画行数字行标列div
		this.initRowNumDiv();
	}
	// 创建固定选择列
	if (this.isMultiSelWithBox)
		this.initSelectColumDiv();
	// 创建行状态显示列
	this.initLineStateColumDiv();

	// 创建浮动在最下面的合计行
	if (this.isShowSumRow) {
		this.initSumRowDiv();
		this.initSumRowDataDiv();
	}

	// 重新调整翻页工具条位置
	GridComp.adjustPaginationBar(this);
};

/**
 * 根据model中的数据初始化界面数据
 * 
 * @private
 */
GridComp.prototype.initDatas = function() {
	// 根据headers model画headers内的table(数据)
	this.initHeaderTables();
	// 画固定列数据区的所有列div
	this.initFixedColumDataDiv();
	// 画动态列数据区的数据外层包装div及每列数据div
	this.initDynamicColumDataDiv();
	// 初始化合计行中的每个cell单元
	if (this.isShowSumRow)
		this.initSumRowCells();
};

/**
 * 重新处理设置为自动扩展表头的宽度
 * 
 * @private
 */
GridComp.processAutoExpandHeadersWidth = function(gridId) {
	var grid = window.objects[gridId];
	var autoHeaders = grid.getAutoExpandHeaders();
	// 此Header是自动扩展表头
	if (autoHeaders != null && autoHeaders.length > 0) {
		if (!grid.isScroll()) {
			var expandTotalWidth = 0;
			// TODO:减1是为了修正在portal中自动调整后仍有横向滚动条的问题
			if (grid.isVScroll())
				expandTotalWidth = grid.outerDiv.offsetWidth
						- grid.getNoAutoExpandHeadersWidth()
						- GridComp.ROWSTATE_COLUMN_WIDTH
						- GridComp.COLUMN_LEFT_BORDER_WIDTH - 17;// - 1;
			else
				expandTotalWidth = grid.outerDiv.offsetWidth
						- grid.getNoAutoExpandHeadersWidth()
						- GridComp.ROWSTATE_COLUMN_WIDTH
						- GridComp.COLUMN_LEFT_BORDER_WIDTH;// - 1;

			// 60px为多选列的宽度
			if (grid.isMultiSelWithBox)
				expandTotalWidth = expandTotalWidth
						- GridComp.MULTISEL_COLUMN_WIDTH
						- GridComp.COLUMN_LEFT_BORDER_WIDTH;// - 1;

			// 如果显示数字列,自动扩展宽度要减去数字列
			if (grid.isShowNumCol)
				expandTotalWidth = expandTotalWidth
						- grid.constant.rowNumHeaderDivWidth;

			// 每一份的宽度
			var oneWidth = Math.floor(expandTotalWidth / autoHeaders.length)
					- GridComp.COLUMN_LEFT_BORDER_WIDTH;
			for (var i = 0, count = autoHeaders.length; i < count; i++) {
				if (i == count - 1) {
					autoHeaders[i].width = expandTotalWidth - i
							* (oneWidth + GridComp.COLUMN_LEFT_BORDER_WIDTH)
							- GridComp.COLUMN_LEFT_BORDER_WIDTH;
					// 改变dynamicHeaderTableDiv的宽度
					var dynTableDivRealWidth = grid
							.getDynamicTableDivRealWidth(true);
					// + (count + 1);
					if (IS_IE)
						dynTableDivRealWidth = dynTableDivRealWidth + 1;
					grid.dynamicHeaderTableDiv.style.width = dynTableDivRealWidth
							+ "px";

					autoHeaders[i].dataTable.style.width = autoHeaders[i].width
							+ "px";
					autoHeaders[i].cell.width = autoHeaders[i].width;
					autoHeaders[i].contentDiv.style.width = (autoHeaders[i].width - 1)
							+ "px";
					autoHeaders[i].dataDiv.style.width = autoHeaders[i].width
							+ "px";
					// grid.dynamicColumDataDiv.style.width =
					// (dynTableDivRealWidth - count - 2) + "px";
					grid.dynamicColumDataDiv.style.width = dynTableDivRealWidth
							+ "px";
					// 处理合计行
					if (autoHeaders[i].sumCell) {
						if (autoHeaders[i].keyName == grid.basicHeaders[0].keyName)
							autoHeaders[i].sumCell.style.width = autoHeaders[i].width
									+ GridComp.ROWSTATE_COLUMN_WIDTH
									- GridComp.SUMROW_DIV_WIDTH
									- (GridComp.SUMCELL_PADDING * 2) + "px";
						// autoHeaders[i].sumCell.style.width =
						// autoHeaders[i].width - 3 - 20 + "px";
						else
							autoHeaders[i].sumCell.style.width = autoHeaders[i].width
									- (GridComp.SUMCELL_PADDING * 2) + "px";
					}
					if (grid.dynSumRowDataDiv)
						grid.dynSumRowDataDiv.style.width = dynTableDivRealWidth
								+ "px";
				} else {
					autoHeaders[i].width = oneWidth;
					autoHeaders[i].cell.width = oneWidth;
					autoHeaders[i].contentDiv.style.width = (oneWidth - 1)
							+ "px";
					autoHeaders[i].dataTable.style.width = oneWidth + "px";
					autoHeaders[i].dataDiv.style.width = oneWidth + "px";
					// 处理合计行
					if (autoHeaders[i].sumCell) {
						if (autoHeaders[i].keyName == grid.basicHeaders[0].keyName)
							autoHeaders[i].sumCell.style.width = autoHeaders[i].width
									+ GridComp.ROWSTATE_COLUMN_WIDTH
									- GridComp.SUMROW_DIV_WIDTH
									- (GridComp.SUMCELL_PADDING * 2) + "px";
						// autoHeaders[i].sumCell.style.width = oneWidth - 3 -
						// 20 + "px";
						else
							autoHeaders[i].sumCell.style.width = autoHeaders[i].width
									- (GridComp.SUMCELL_PADDING * 2) + "px";
						// autoHeaders[i].sumCell.style.width = oneWidth -3 +
						// "px";
					}
				}
				// guoweic: add 修正当列设置为autoExpand="true"时，该元素宽度的显示问题 start
				// 2009-11-20
				if (autoHeaders[i].isHidden == false) {
					for (var j = 0, rowLength = autoHeaders[i].dataDiv.cells.length; j < rowLength; j++) {
						var tempCell = autoHeaders[i].dataDiv.cells[j];
						if (tempCell) {
							// 修正当有列设置为autoExpand="true"时，该元素宽度的显示问题
							tempCell.style.width = autoHeaders[i].width - 5
									+ "px"; // 5为左右padding加上border的宽
						}
					}
				}
				// guoweic: add end

			}
		}
	}
};

/**
 * 获取非自动扩展表头的宽度
 * 
 * @private
 */
GridComp.prototype.getNoAutoExpandHeadersWidth = function() {
	if (this.basicHeaders == null)
		return -1;
	var width = 0;
	for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
		if (this.basicHeaders[i].isHidden == false
				&& this.basicHeaders[i].isAutoExpand == false)
			width += this.basicHeaders[i].width
					+ GridComp.COLUMN_LEFT_BORDER_WIDTH;
	}
	return width;
};

/**
 * 获取自动扩展宽度的表头
 * 
 * @private
 */
GridComp.prototype.getAutoExpandHeaders = function() {
	if (this.basicHeaders == null)
		return null;
	var autoHeaders = [];
	for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
		if (this.basicHeaders[i].isHidden == false
				&& this.basicHeaders[i].isAutoExpand == true)
			autoHeaders.push(this.basicHeaders[i]);
	}
	return autoHeaders;
};

/**
 * 初始化所有常量
 * 
 * @private
 */
GridComp.prototype.initConstant = function() {
	if (this.constant == null)
		this.constant = new Object();
	if (this.outerDiv.offsetWidth != 0) {
		this.constant.outerDivHeight = this.outerDiv.offsetHeight;
		this.constant.outerDivWidth = this.outerDiv.offsetWidth;
	}
};

/**
 * 获得基本头信息。此grid支持多表头，只有最下层的表头包含有用信息。
 * 
 * @param headers
 *            最顶层的headers
 * @private
 */
GridComp.prototype.initBasicHeaders = function() {
	// 已经调用model.setDataset初始化了basicHeaders
	var basicHeaders = this.model.getBasicHeaders();
	// 对需要分组的header设上标示
	if (this.groupHeaderIds != "") {
		// 记录分组列的列索引
		this.groupHeaderColIndice = [];
		var j = 0;
		for (var i = 0, count = basicHeaders.length; i < count; i++) {
			if (j == this.groupHeaderIds.length)
				break;
			if (basicHeaders[i].keyName == this.groupHeaderIds[j]) {
				basicHeaders[i].isGroupBy = true;
				this.groupHeaderColIndice.push(i);
				j++;
			}
		}
	}

	if (basicHeaders != null && basicHeaders.length > 0) {
		// 得到model中设置的headers数据
		this.headers = this.model.getHeaders();
		// 保存header的总体高度
		if (this.isShowHeader)
			this.constant.headerHeight = this.getHeaderDepth()
					* (this.headerRowHeight + 1); // +1为边框的高度
		else
			this.constant.headerHeight = 0;
	} else {
		if (this.headers == null)
			throw new Error("grid must be initialized with headers!");
	}
};

/**
 * 根据key获取header
 * 
 * @private
 */
GridComp.prototype.getHeader = function(keyName) {
	var basicHeaders = this.model.getBasicHeaders();
	if (basicHeaders == null)
		return null;
	else {
		for (var i = basicHeaders.length - 1; i >= 0; i--) {
			if (basicHeaders[i].keyName == keyName)
				return basicHeaders[i];
		}
	}
};

GridComp.prototype.removeHeader = function(keyName) {
	var header = this.model.removeHeader(keyName);
	return header;
};

/**
 * 判断初始界面是否会出现横向、纵向滚动条
 * 
 * @private
 */
GridComp.prototype.adjustScroll = function() {
	var gridWidth = this.constant.outerDivWidth;
	var gridHeight = this.constant.outerDivHeight;
	// 判断初始界面时是否会出现横向滚动条
	this.scroll = false;
	var dataRealWidth = this.getDynamicTableDivRealWidth(true)
			+ this.getDynamicTableDivRealWidth(false);
	if (dataRealWidth > gridWidth)
		this.scroll = true;

	// 判断初始界面时是否会出现纵向滚动条(修正表头宽度)
	this.vScroll = false;
	var dataRealHeight = gridHeight - this.getRowsNum() * this.rowHeight
			- this.constant.headerHeight;
	if (this.pageSize > 0)
		dataRealHeight -= GridComp.PAGEBAR_HEIGHT;
	if (dataRealHeight < 0)
		this.vScroll = true;
};

/**
 * 创建最外层包容div
 * 
 * @private
 */
GridComp.prototype.initOuterDiv = function() {
	var oThis = this;
	this.outerDiv = $ce("div");
	this.outerDiv.className = this.className;
	this.outerDiv.style.left = this.left;
	this.outerDiv.style.top = this.top;
	this.outerDiv.style.width = this.width;
	this.outerDiv.style.height = this.height;
};

/**
 * 创建分页操作条
 * 
 * @private
 */
GridComp.prototype.initPaginationBar = function() {
	var oThis = this;
	this.paginationBar = $ce("div");
	this.paginationBar.className = "grid_paginationbar";
	this.paginationBar.style.width = "100%";
	this.paginationBar.style.height = GridComp.PAGEBAR_HEIGHT;
	this.outerDiv.appendChild(this.paginationBar);

	this.paginationContent = $ce("div");
	this.paginationContent.id = "grid_paginationcontent";
	this.paginationContent.style.position = "absolute";
	this.paginationContent.style[ATTRFLOAT] = "right";
	this.paginationContent.style.width = "420px";
	this.paginationContent.style.right = "0px";
	this.paginationBar.appendChild(this.paginationContent);

	// 创建分页栏中的各个控件
	var width = 50;
	var elePadding = 5;
	// 第一页
	// this.firstPage = new ButtonComp(this.paginationBar, "$firstPage",
	// elePadding, "4",
	// width, "16", "", trans("ml_pagefirst"), window.themePath
	// + "/images/pagination/firstpage.gif", "absolute", "center",
	// false, "pagination_button_div");

	this.firstPage = new LinkComp(this.paginationContent, "$firstPage",
			elePadding + 10, 4, "javascript:void(0)", "首页", false, "_self",
			"absolute", "");
	this.firstPage.onclick = function() {
		if (this.disabled)
			return;
		// 前台分页
		if (oThis.model.dataset != null)
			oThis.processServerPagination(0);
	};

	this.prePage = new LinkComp(this.paginationContent, "$prePage", 1 * width
			+ 2 * elePadding, 4, "javascript:void(0)", "上一页", false, "_self",
			"absolute", "");
	this.prePage.onclick = function() {
		if (this.disabled)
			return;
		if (oThis.model.dataset != null) {
			var currPageIndex = oThis.model.dataset.getPageIndex(null);
			oThis.processServerPagination(currPageIndex - 1);
		}
	};

	// 下一页
	this.nextPage = new LinkComp(this.paginationContent, "$nextPage", 2 * width
			+ 3 * elePadding, 4, "javascript:void(0)", "下一页", false, "_self",
			"absolute", "");

	this.nextPage.onclick = function() {
		if (this.disabled)
			return;

		var currPageIndex = oThis.model.dataset.getPageIndex();
		oThis.processServerPagination(currPageIndex + 1);
	};

	// 最后一页
	this.lastPage = new LinkComp(this.paginationContent, "$lastPage", 3 * width
			+ 4 * elePadding, 4, "javascript:void(0)", "末页", false, "_self",
			"absolute", "");

	this.lastPage.onclick = function() {
		if (this.disabled)
			return;
		// 前台分页

		if (oThis.model.dataset != null) {
			var pageCount = oThis.model.getPageCount();
			oThis.processServerPagination(pageCount - 1);
		}
	};

	if (!this.simplePageBar) {
		// // "刷新"按钮
		// this.refreshPage = new ButtonComp(this.paginationBar, "$refreshPage",
		// 4
		// * width + 5 * elePadding, "4", width + 5, "16",
		// trans("ml_refresh"), trans("ml_refresh"), null, "absolute",
		// "center", false, "pagination_button_div");
		// this.refreshPage.onclick = function() {
		// if (this.disabled)
		// return;
		//
		// if (oThis.model.dataset != null)
		// oThis.processServerPagination(oThis.model.dataset
		// .getPageIndex(null));
		// };

		this.pageJump = $ce("div");
		this.pageJump.className = "label_normal";
		this.pageJump.id = "$pageJump";
		this.pageJump.style.position = "absolute";
		this.pageJump.style.left = 5 * width + 6 * elePadding + 35 + "px";
		this.pageJump.style.top = "4px";
		this.pageJump.innerHTML = trans("ml_goto");
		this.paginationContent.appendChild(this.pageJump);
		// 页码输入框
		this.pageIndex = new IntegerTextComp(this.paginationContent,
				"$pageIndex", 6 * width + 7 * elePadding + 10, "4", "30",
				"absolute", null, 0, null);
		this.pageIndex.setBounds(6 * width + 7 * elePadding + 10, "4", "30",
				"16");
		this.pageIndex.Div_gen.style.height = "16px";
		this.pageIndex.Div_text.style.border = "none";
		this.pageIndex.Div_text.style.borderBottomStyle = "solid";
		this.pageIndex.Div_text.style.borderBottomWidth = "1px";
		this.pageIndex.input.style.textAlign = "center";
		this.pageIndex.input.style.color = "#09c";

		this.pageNumbic = $ce("div");
		this.pageNumbic.className = "label_normal";
		this.pageNumbic.id = "pageNumbic";
		this.pageNumbic.style.position = "absolute";
		this.pageNumbic.style.left = 6 * width + 7 * elePadding + 40 + "px";
		this.pageNumbic.style.top = "4px";
		this.pageNumbic.innerHTML = trans("ml_page");
		this.paginationContent.appendChild(this.pageNumbic);

		this.pageIndex.onenter = function() {
			if (this.disabled)
				return;
			var count = oThis.pageIndex.getValue();
			if (count != null && !isNaN(parseInt(count, 10))) {
				count = parseInt(count, 10);
				var pageCount = -1;

				pageCount = oThis.model.getPageCount();
				if (pageCount == -1)
					return;
				if (count < 1 || count > pageCount)
					return;
				else {

					oThis.processServerPagination(count - 1);
				}
			}
		};

		this.toPage = new ImageComp(this.paginationContent, "$goToPage",
				window.themePath + "/images/grid/go.png", 7 * width + 8
						* elePadding, 4, "16", "16", "", "", null);
		this.toPage.onclick = function() {
			if (this.disabled)
				return;
			var count = oThis.pageIndex.getValue();
			if (count != null && !isNaN(parseInt(count, 10))) {
				count = parseInt(count, 10);
				var pageCount = -1;

				pageCount = oThis.model.getPageCount();
				if (pageCount == -1)
					return;
				if (count < 1 || count > pageCount)
					return;
				else {

					oThis.processServerPagination(count - 1);
				}
			}
		};
	}

	// 分页文字描述
	this.pageNum = $ce("div");
	this.pageNum.className = "label_normal";
	this.pageNum.id = "$pageNum";
	this.pageNum.style.position = "absolute";

	var baseWidth = 4;
	if (this.simplePageBar)
		baseWidth = 4;

	this.pageNum.style.left = (baseWidth * width + baseWidth * elePadding - 15)
			+ "px";
	this.pageNum.style.top = "4px";
	this.pageNum.style.textOverflow = "ellipsis";
	this.pageNum.style.whiteSpace = "nowrap";
	if (this.simplePageBar)
		this.pageNum.innerHTML = trans("ml_pageCount") + "  "
				+ trans("ml_rowCount");
	else
		this.pageNum.innerHTML = trans("ml_pageCount") + "  "
				+ trans("ml_rowCount") + "  " + trans("ml_pageRowCount");
	this.paginationContent.appendChild(this.pageNum);
};

/**
 * 处理后台分页
 * 
 * @param pageIndex
 *            当前页码
 * @private
 */
GridComp.prototype.processServerPagination = function(pageIndex) {
	var pageCount = this.model.getPageCount();
	if (pageIndex < 0 || pageIndex > pageCount - 1)
		return;
	this.processPaginationInfo(pageIndex, this.model.dataset.getAllRowCount(),
			pageCount, this.pageSize);
	this.model.dataset.setCurrentPage(null, pageIndex);
};

/**
 * 处理分页信息
 * 
 * @private
 */
GridComp.prototype.setPaginationInfo = function() {
	if (this.pageSize > 0) {
		var pageIndex = this.model.dataset.getPageIndex(null);
		// 显示分页信息(后台分页)
		this.processPaginationInfo(pageIndex, this.model.dataset
				.getAllRowCount(), this.model.getPageCount(), this.pageSize);
	}
};

/**
 * 根据传入的参数,控制分页按钮状态,显示分页信息
 * 
 * @param pageIndex
 *            页码
 * @param rowCount
 *            当前页行数,对于前台分页指当前分页的总行数
 * @param pageCount
 *            页数
 * @param pageRowCount
 *            每页行数
 * @param isClient
 *            是否前台分页
 * @private
 */
GridComp.prototype.processPaginationInfo = function(pageIndex, rowCount,
		pageCount, pageRowCount) {
	pageIndex = parseInt(pageIndex, 10);
	if (pageIndex < 0)
		return;

	// 设置按钮状态
	if (pageIndex == 0 || pageIndex == pageCount - 1) {
		if (pageIndex == 0) {
			if (this.prePage)
				this.prePage.setActive(false);
			if (this.firstPage)
				this.firstPage.setActive(false);
			if (pageCount > 1) {
				if (this.nextPage)
					this.nextPage.setActive(true);
				if (this.lastPage)
					this.lastPage.setActive(true);
			} else {
				if (this.nextPage)
					this.nextPage.setActive(false);
				if (this.lastPage)
					this.lastPage.setActive(false);
			}
		}
		if (pageIndex == pageCount - 1) {
			if (pageCount > 1) {
				this.prePage.setActive(true);
				this.firstPage.setActive(true);
			}
			if (this.nextPage)
				this.nextPage.setActive(false);
			if (this.lastPage)
				this.lastPage.setActive(false);
		}
	} else {
		if (this.prePage)
			this.prePage.setActive(true);
		if (this.firstPage)
			this.firstPage.setActive(true);
		if (this.nextPage)
			this.nextPage.setActive(true);
		if (this.lastPage)
			this.lastPage.setActive(true);
	}

	if (this.refreshPage)
		this.refreshPage.setActive(true);
	if (this.toPage)
		this.toPage.setActive(true);
	if (this.pageSetting)
		this.pageSetting.setActive(true);

	this.pageClient = false;

	// 设置页码
	if (!this.simplePageBar) {
		if (this.pageIndex)
			this.pageIndex.setValue(pageIndex + 1);
	}
	// 设置显示的分页信息
	if (this.simplePageBar) {
		if (this.pageNum)
			this.pageNum.innerHTML = trans("ml_pageCount") + " "
					+ (pageIndex + 1) + " " + trans("ml_rowCount") + " "
					+ rowCount;
	} else {
		if (this.pageNum)
			this.pageNum.innerHTML = trans("ml_the") + " " + (pageIndex + 1)
					+ "/" + pageCount + trans("ml_page") + " "
					+ trans("ml_all") + "<span id='" + this.id + "_rowCount'>"
					+ rowCount + "</span>" + trans("ml_line") + " ";
	}
};

/**
 * 创建表头区整体div
 * 
 * @private
 */
GridComp.prototype.initHeaderDiv = function() {
	var gridWidth = this.constant.outerDivWidth;
	var gridHeight = this.constant.outerDivHeight;
	this.headerDiv = $ce("div");
	this.headerDiv.className = "headerbar_div";
	// this.headerDiv.style.border = "solid red 1px";
	// 根据是否显示分页条调整top
	if (this.pageSize > 0 && this.isPagenationTop == true)
		this.headerDiv.style.top = GridComp.PAGEBAR_HEIGHT + "px";
	else
		this.headerDiv.style.top = "0px";
	this.outerDiv.appendChild(this.headerDiv);

	// 表头不显示菜单
	this.headerDiv.oncontextmenu = function(e) {
		e = EventUtil.getEvent();
		stopAll(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	if (this.vScroll) {
		this.headerDiv.style.width = (gridWidth - GridComp.SCROLLBAE_HEIGHT)
				+ "px";// - 2;
		// 记录headerDiv的原始宽度
		this.headerDiv.defaultWidth = gridWidth - GridComp.SCROLLBAE_HEIGHT;// - 2;
	} else {
		this.headerDiv.style.width = gridWidth + "px";// - 2;
		// 记录headerDiv的原始宽度
		this.headerDiv.defaultWidth = gridWidth;// - 2;
	}
	this.constant.headerDivWidth = this.headerDiv.defaultWidth;
	this.headerDiv.style.height = this.constant.headerHeight + "px";
	if (!this.isShowHeader)
		this.headerDiv.style.visibility = "hidden";
};

/**
 * 创建固定表头区整体div
 * 
 * @private
 */
GridComp.prototype.initFixedHeaderDiv = function() {
	this.fixedHeaderDivWidth = 0;
	// 得到固定表头的宽度
	for (var i = this.headers.length - 1; i >= 0; i--) {
		if (this.headers[i].isFixedHeader)
			this.fixedHeaderDivWidth += this
					.getHeaderDefaultWidth(this.headers[i]);
	}

	// 加上固定选择列的宽度
	if (this.isMultiSelWithBox)
		this.fixedHeaderDivWidth += (GridComp.SELECTCOLUM_WIDTH + 1); // 有1px的border宽度

	this.fixedHeaderDiv = $ce("div");
	this.headerDiv.appendChild(this.fixedHeaderDiv);
	// this.fixedHeaderDiv.className = "headerdiv fixedheaderdiv";
	this.fixedHeaderDiv.style.height = this.constant.headerHeight + "px";
	this.fixedHeaderDiv.style.width = this.fixedHeaderDivWidth + "px";
	this.fixedHeaderDiv.style.left = "0px";
	this.fixedHeaderDiv.style.top = "0px";
	this.fixedHeaderDiv.style.position = "absolute";
	// 放在最上层
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。	
//	this.fixedHeaderDiv.style.zIndex = "10000";
	this.fixedHeaderDiv.style.zIndex = getZIndex();
	// this.fixedHeaderDiv.style[ATTRFLOAT] = "left";
	// 将固定表头值放入常量对象
	this.constant.fixedHeaderDivWidth = this.fixedHeaderDiv.offsetWidth;
};

/**
 * 创建静态表头区行标区表头div
 * 
 * @private
 */
GridComp.prototype.initRowNumHeaderDiv = function() {
	this.rowNumHeaderDiv = $ce("div");
	this.fixedHeaderDiv.appendChild(this.rowNumHeaderDiv);
	this.rowNumHeaderDiv.className = "row_num_header_div";
	this.rowNumHeaderDiv.style.height = this.constant.headerHeight + "px";

	var width = this.rowNumHeaderDiv.offsetWidth;
	// 将行号表头区的宽度放入constant常量中,供后续使用
	this.constant.rowNumHeaderDivWidth = width;
	// 如果启用了fixedHeaderDiv则需要改变fixedHeaderDiv的宽度
	this.fixedHeaderDiv.style.width = (width + this.fixedHeaderDivWidth) + "px";
	this.constant.fixedHeaderDivWidth = width + this.fixedHeaderDivWidth;
};

/**
 * 创建表格最左侧功能设置触发区
 * 
 * @private
 */
GridComp.prototype.initLineStateHeaderDiv = function() {
	var oThis = this;
	this.tableSettingDiv = $ce("div");
	this.fixedHeaderDiv.appendChild(this.tableSettingDiv);
	this.tableSettingDiv.className = "row_state_header_div";
	this.tableSettingDiv.style.left = this.constant.rowNumHeaderDivWidth + "px";
	this.tableSettingDiv.style.height = this.constant.headerHeight + "px";
	var settingWidth = this.tableSettingDiv.offsetWidth;
	// 将功能设置区的宽度放入constant常量中,供后续使用
	this.constant.lineStateHeaderDivWidth = settingWidth;
	// 如果启用了settingDiv则需要改变fixedHeaderDiv的宽度
	this.fixedHeaderDiv.style.width = (settingWidth + this.constant.fixedHeaderDivWidth)
			+ "px";
	this.constant.fixedHeaderDivWidth = settingWidth
			+ this.constant.fixedHeaderDivWidth;
	this.tableSettingDiv.onclick = function(e) {
		// guoweic:多表头不允许进行列设置操作
		if (oThis.headerDepth > 1)
			return;

		if (!oThis.showColInfo)
			return;
		// 禁用
		if (true)
			return;
		e = EventUtil.getEvent();

		if (oThis.settingMenu == null) {
			oThis.settingMenu = new ContextMenuComp("settingMenu", "", "", true);
			oThis.settingMenu.Div_gen.style.zIndex = oThis.fixedHeaderDiv.style.zIndex + 1;
			var menu = oThis.settingMenu;
			// 保存是谁触发的此settingMenu对象
			menu.triggerObj = this;

			// 显示列子菜单,默认所有列显示
			var showColumsMenu = new ContextMenuComp("showColumsMenu", "", "",
					true);
			showColumsMenu.Div_gen.style.zIndex = oThis.fixedHeaderDiv.style.zIndex + 1;
			for (var i = 0, count = oThis.basicHeaders.length; i < count; i++) {
				if (oThis.basicHeaders[i].isHidden == false)
					showColumsMenu.addMenu(oThis.basicHeaders[i].keyName,
							oThis.basicHeaders[i].showName,
							oThis.basicHeaders[i].showName, "", true, true);
			}

			// 锁定列子菜单
			var lockColumsMenu = new ContextMenuComp("lockColumsMenu", "", "",
					true);
			lockColumsMenu.Div_gen.style.zIndex = oThis.fixedHeaderDiv.style.zIndex + 1;
			for (var i = 0, count = oThis.basicHeaders.length; i < count; i++) {
				if (oThis.basicHeaders[i].isHidden == false)
					lockColumsMenu.addMenu(oThis.basicHeaders[i].keyName,
							oThis.basicHeaders[i].showName,
							oThis.basicHeaders[i].showName, "", true, false);
			}

			// 创建grid设置主菜单
			var showColumsItem = menu.addMenu("showColums",
					trans("ml_showcolums"), trans("ml_showcolums"),
					window.themePath + "/images/grid/colums.gif", false, false);
			menu.addItemChildMenu(showColumsMenu, showColumsItem);
			var lockColumsItem = menu.addMenu("lockColums",
					trans("ml_lockcolums"), trans("ml_lockcolums"),
					window.themePath + "/images/grid/lock.gif", false, false);
			menu.addItemChildMenu(lockColumsMenu, lockColumsItem);

			/**
			 * 锁定列菜单打开之前根据当前grid显示的列,隐藏相应的menuItem
			 */
			lockColumsMenu.onBeforeShow = function() {
				// 当前选中的所有显示列
				var showColumsItems = showColumsMenu.getSelectedItems();
				var showColumsItemKey = new Array();
				if (showColumsItems != null && showColumsItems.length > 0) {
					for (var i = 0, count = showColumsItems.length; i < count; i++)
						showColumsItemKey.push(showColumsItems[i].name);

					var lockColumsItems = this.childItems;
					for (var i = 0, count = lockColumsItems.length; i < count; i++) {
						if (showColumsItemKey.indexOf(lockColumsItems[i].name) == -1)
							lockColumsItems[i].hide();
						else
							lockColumsItems[i].show();
					}
				}
				// 没有选中任何显示列,那么锁定列均不显示
				else {
					var lockColumsItems = this.childItems;
					for (var i = 0, count = lockColumsItems.length; i < count; i++)
						lockColumsItems[i].hide();
				}
			};

			/**
			 * 锁定列菜单关闭时的重载函数,只能锁定当前grid显示的列
			 */
			lockColumsMenu.onBeforeClose = function() {
				var selItems = this.getSelectedItems();
				var lockColIds = new Array(selItems.length);

				for (var i = 0, count = selItems.length; i < count; i++)
					lockColIds[i] = selItems[i].name;

				// 锁定列在显示列中不能选择,也就是"锁定列"不能选择"隐藏"
				// 获取当前的显示列
				var showItems = showColumsMenu.getVisibleItems();
				var showColIds = new Array(showItems.length);
				for (var i = 0, count = showItems.length; i < count; i++)
					showColIds[i] = showItems[i].name;

				if (lockColIds.length > 0) {
					for (var i = 0, count = showColIds.length; i < count; i++) {
						if (lockColIds.indexOf(showColIds[i]) != -1)
							showItems[i].checkbox.disabled = true;
						else
							showItems[i].checkbox.disabled = false;
					}
				} else {
					for (var i = 0, count = showItems.length; i < count; i++) {
						if (showItems[i].checkbox.disabled == true)
							showItems[i].checkbox.disabled = false;
					}
				}

				oThis.setFixedColumns(lockColIds);
			};

			/**
			 * 显示列菜单关闭时的重载函数
			 */
			showColumsMenu.onBeforeClose = function() {
				var selItems = this.getSelectedItems();
				var colIds = new Array(selItems.length);

				for (var i = 0, count = selItems.length; i < count; i++) {
					colIds[i] = selItems[i].name;
				}
				oThis.setShowColumns(colIds);
			};
		}

		// 首先隐藏当前显示的设置按钮
		oThis.settingMenu.setPosLeft(compOffsetLeft(this, document.body));
		oThis.settingMenu.setPosTop(compOffsetTop(this, document.body)
				+ oThis.headerRowHeight);
		oThis.settingMenu.show(e);
		stopAll(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
};

/**
 * 创建固定表头区固定选择列header
 * 
 * @private
 */
GridComp.prototype.initSelectColumHeaderDiv = function() {
	var oThis = this;
	this.selectColumHeaderDiv = $ce("div");
	var div = this.selectColumHeaderDiv;
	this.fixedHeaderDiv.appendChild(div);
	div.id = "fixedSelectColumHeader";
	div.className = "select_headerdiv";
	div.style.left = (this.constant.rowNumHeaderDivWidth + this.constant.lineStateHeaderDivWidth)
			+ "px";
	div.style.height = this.constant.headerHeight + "px";
	// 如果多表头,表头高度会超过两倍表头高,这时要计算paddingTop让checkbox聚中显示
	if (this.constant.headerHeight > GridComp.HEADERROW_HEIGHT)
		div.style.paddingTop = parseInt((this.constant.headerHeight - 20) / 2)
				+ "px";
	// 多选模式下的全选checkbox
	this.selectAllBox = $ce("INPUT");
	this.selectAllBox.type = "checkbox";
	div.appendChild(this.selectAllBox);
	this.selectAllBox.defaultChecked = false;
	this.selectAllBox.checked = false;
	this.selectAllBox.onclick = function(e) {
		e = EventUtil.getEvent();
		// 置空当前选择单元格
		oThis.selectedCell = null;
		oThis.oldCell = null;
		// 隐藏编辑框
		oThis.hiddenComp();
		// 取消全选
		if (this.checked == false) {
			var indices = new Array;
			for (var i = 0, count = oThis.model.getRowsCount(); i < count; i++) {
				indices.push(i);
				// oThis.model.setRowUnSelected(i);
			}
			oThis.model.setRowUnSelected(indices);
		}
		// 全选
		else {
			var indices = new Array;
			for (var i = 0, count = oThis.model.getRowsCount(); i < count; i++) {
				indices.push(i);
			}
			oThis.model.addRowSelected(indices);
		}
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};
};

/**
 * 创建固定表头区header数据区div
 * 
 * @private
 */
GridComp.prototype.initFixedHeaderTableDiv = function() {
	var currLeft = this.constant.rowNumHeaderDivWidth
			+ this.constant.lineStateHeaderDivWidth;
	if (this.isMultiSelWithBox)
		currLeft = currLeft + this.selectColumHeaderDiv.offsetWidth;
	this.fixedHeaderTableDiv = $ce("div");
	this.fixedHeaderDiv.appendChild(this.fixedHeaderTableDiv);
	// this.fixedHeaderTableDiv.className = "fixed_headertable_div";
	this.fixedHeaderTableDiv.style.position = "absolute";
	this.fixedHeaderTableDiv.style.overflow = "hidden";
	this.fixedHeaderTableDiv.style.top = "0px";
	this.fixedHeaderTableDiv.style.left = currLeft + "px";
	this.fixedHeaderTableDiv.style.height = this.constant.headerHeight + "px";
	this.fixedHeaderTableDiv.style.width = (this.constant.fixedHeaderDivWidth - currLeft)
			+ "px";
	// 将固定表头区header数据区的宽度放入常量对象
	this.constant.fixedHeaderTableDivWidth = this.constant.fixedHeaderDivWidth
			- currLeft;
};

/**
 * 创建动态表头区整体div
 * 
 * @private
 */
GridComp.prototype.initDynamicHeaderDiv = function() {
	var gridWidth = this.constant.outerDivWidth;
	var gridHeight = this.constant.outerDivHeight;
	var fixedHeaderWidth = this.constant.fixedHeaderDivWidth;
	this.dynamicHeaderDiv = $ce("div");
	this.headerDiv.appendChild(this.dynamicHeaderDiv);
	this.dynamicHeaderDiv.className = "headerbar_div";
	// this.dynamicHeaderDiv.style.border = "solid red 1px";
	this.dynamicHeaderDiv.style.left = fixedHeaderWidth + "px";
	this.dynamicHeaderDiv.style.height = this.constant.headerHeight + "px";
	if (this.vScroll) {
		var dynHeaderWidth = gridWidth - fixedHeaderWidth
				- GridComp.SCROLLBAE_HEIGHT;// - 5;
		if (dynHeaderWidth > 0) {
			this.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
			this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
		} else {
			this.dynamicHeaderDiv.style.width = "0px";
			this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
		}
	} else {
		var dynHeaderWidth = gridWidth - fixedHeaderWidth;// - 3;
		if (dynHeaderWidth > 0) {
			this.dynamicHeaderDiv.style.width = (gridWidth - fixedHeaderWidth)
					+ "px";// - 3;
			this.dynamicHeaderDiv.defaultWidth = gridWidth - fixedHeaderWidth;// - 3;
		} else {
			this.dynamicHeaderDiv.style.width = "0px";
			this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
		}
	}
};

/**
 * 创建动态表头区header数据区div
 * 
 * @private
 */
GridComp.prototype.initDynamicHeaderTableDiv = function() {
	// 包装tables的包容div
	this.dynamicHeaderTableDiv = $ce("div");
	this.dynamicHeaderDiv.appendChild(this.dynamicHeaderTableDiv);
	// this.dynamicHeaderTableDiv.className = "dynamic_headertable_div";
	// this.dynamicHeaderTableDiv.style.border = "solid blue 1px";
	this.dynamicHeaderTableDiv.style.left = "0px";
	this.dynamicHeaderTableDiv.style.position = "absolute";
	this.dynamicHeaderTableDiv.style.height = this.constant.headerHeight + "px";
	this.dynamicHeaderTableDiv.style.width = this
			.getDynamicTableDivRealWidth(true)
			+ "px";
};

/**
 * 根据生成的headers model画固定和动态header内的table
 * 
 * @private
 */
GridComp.prototype.initHeaderTables = function() {
	// 表头的总深度
	this.headerDepth = this.getHeaderDepth();
	// 将动态表头和固定表头放在以下两个数组中
	this.defaultFixedHeaders = new Array();
	this.defaultDynamicHeaders = new Array();
	for (var i = 0, count = this.headers.length; i < count; i++) {
		if (this.headers[i].isFixedHeader)
			this.defaultFixedHeaders.push(this.headers[i]);
		else if (this.headers[i].isFixedHeader == false)
			this.defaultDynamicHeaders.push(this.headers[i]);
	}

	// TODO:目前对于多表头先不允许拖动
	for (var i = 0, count = this.headers.length; i < count; i++)
		this.initHeaderTable(this.headers[i]);
};

/**
 * @private
 */
GridComp.prototype.getFirstVisibleHeader = function() {
	for (var i = 0; i < this.headers.length; i++) {
		if (this.headers[i].isHidden == false)
			return this.headers[i];
	}
	return null;
};

/**
 * @private
 */
GridComp.prototype.getLastVisibleHeader = function() {
	for (var i = this.headers.length - 1; i >= 0; i--) {
		if (this.headers[i].isHidden == false)
			return this.headers[i];
	}
	return null;
};

/**
 * 得到动态表头的第一个不隐藏的header
 * 
 * @private
 */
GridComp.prototype.getFirstDynamicVisibleHeader = function() {
	if (this.defaultDynamicHeaders == null
			|| this.defaultDynamicHeaders.length == 0)
		return null;
	for (var i = 0, count = this.defaultDynamicHeaders.length; i < count; i++) {
		if (this.defaultDynamicHeaders[i].isHidden == false)
			return this.defaultDynamicHeaders[i];
	}
};

/**
 * 得到动态表头的最后一个不隐藏的header
 * 
 * @private
 */
GridComp.prototype.getLastDynamicVisibleHeader = function() {
	if (this.defaultDynamicHeaders == null
			|| this.defaultDynamicHeaders.length == 0)
		return null;
	for (var i = this.defaultDynamicHeaders.length - 1; i >= 0; i--) {
		if (this.defaultDynamicHeaders[i].isHidden == false)
			return this.defaultDynamicHeaders[i];
	}
};

/**
 * 得到固定表头的第一个不隐藏的header
 * 
 * @private
 */
GridComp.prototype.getFirstFixedVisibleHeader = function() {
	if (this.defaultFixedHeaders == null
			|| this.defaultFixedHeaders.length == 0)
		return null;
	for (var i = 0, count = this.defaultFixedHeaders.length; i < count; i++) {
		if (this.defaultFixedHeaders[i].isHidden == false)
			return this.defaultFixedHeaders[i];
	}
};

/**
 * 得到固定表头的最后一个不隐藏的header
 * 
 * @private
 */
GridComp.prototype.getLastFixedVisibleHeader = function() {
	if (this.defaultFixedHeaders == null
			|| this.defaultFixedHeaders.length == 0)
		return null;
	for (var i = this.defaultFixedHeaders.length - 1; i >= 0; i--) {
		if (this.defaultFixedHeaders[i].isHidden == false)
			return this.defaultFixedHeaders[i];
	}
};

/**
 * 得到指定header的下一个显示的basic header
 * 
 * @private
 */
GridComp.prototype.getNextVisibleBasicHeader = function(header) {
	if (this.basicHeaders == null)
		throw new Error("basicHeaders为null!");
	if (header == null)
		return null;
	for (var i = 0; i < this.basicHeaders.length; i++) {
		if (this.basicHeaders[i] == header && this.basicHeaders[i + 1] != null) {
			for (var j = i + 1; j < this.basicHeaders.length; j++) {
				if (this.basicHeaders[j].isHidden == false)
					return this.basicHeaders[j];
			}
		}
	}
	return null;
};

/**
 * 得到指定header的上一个显示的basic header
 * 
 * @private
 */
GridComp.prototype.getLastVisibleBasicHeader = function(header) {
	if (this.basicHeaders == null)
		throw new Error("basicHeaders为null!");
	if (header == null)
		return null;
	for (var i = 0; i < this.basicHeaders.length; i++) {
		if (this.basicHeaders[i] == header) {
			for (var j = i - 1; j >= 0; j--) {
				if (this.basicHeaders[j].isHidden == false)
					return this.basicHeaders[j];
			}
		}
	}
	return null;
};

/**
 * 为boolean列创建全选checkbox(提出此方法主要为了代码复用)
 * 
 * @private
 */
GridComp.prototype.createCheckBoxForSelAll = function(header) {
	var oThis = this;
	var selectBox = $ce("INPUT");
	selectBox.type = "checkbox";
	selectBox.style.verticalAlign = "middle";
	selectBox.style.marginTop = "0";
	selectBox.defaultChecked = false;
	selectBox.checked = false;
	// 设置checkbox的禁用状态
	if (this.editable == false || header.columEditable == false)
		selectBox.disabled = true;

	selectBox.onmousedown = function(e) {
		// 置空当前选择单元格
		oThis.selectedCell = null;
		oThis.oldCell = null;
		// 隐藏编辑框
		oThis.hiddenComp();
		var ds = oThis.model.dataset;
		var colIndex = ds.nameToIndex(header.keyName);
		var dsRows = ds.getRows(null);
		if (header.valuePair == null || header.valuePair[1] == null)
			return;

		// 取消全选
		if (this.checked) {
			if (dsRows != null) {
				var rowIndices = new Array();
				var values = new Array();
				for (var i = 0; i < dsRows.length; i++) {
					rowIndices.push(i);
					values.push(header.valuePair[1]);
					// ds.setValueAt(i, colIndex, header.valuePair[1]);
				}
				ds.setValuesAt(rowIndices, colIndex, values);

			}
			this.checked = false;
		}
		// 全选
		else {
			if (dsRows != null) {
				var rowIndices = new Array();
				var values = new Array();
				for (var i = 0; i < dsRows.length; i++) {
					rowIndices.push(i);
					values.push(header.valuePair[0]);
					// ds.setValueAt(i, colIndex, header.valuePair[0]);
				}
				ds.setValuesAt(rowIndices, colIndex, values);
			}
			this.checked = true;
		}
	};

	// 并阻止事件的进一步向上传播
	selectBox.onclick = function(e) {
		e = EventUtil.getEvent();
		// 阻止默认选中事件和事件上传,放在onmousedown中不会起作用,但通知model行选中必须放在onmousedown中!
		stopDefault(e);
		stopEvent(e);
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
	};

	return selectBox;
};

/**
 * 绘制每个header内的table
 * 
 * @param header
 *            this.headers数组中的header
 * @param isLastHeader
 *            是否最后一个header
 * @private
 */
GridComp.prototype.initHeaderTable = function(header) {
	// 隐藏列不创建
	if (header.isHidden)
		return;
	var oThis = this;
	var tempDiv = null;
	// 得到header的总深度
	var totalDepth = header.getDepth();
	// 如果为多表头，且 header总深度小于2，说明 多表头的子全部隐藏了，不创建此多表头
	if (header.children != null && totalDepth < 2)
		return;
	// 得到此header的总宽度作为table的宽度
	var tableWidth = this.getHeaderDefaultWidth(header);
	// 创建table
	header.dataTable = $ce("table");
	header.dataTable.headerOwner = header;
	var headerTable = header.dataTable;

	if (header.isGroupHeader != true) {
		// 处理表头拖动
		header.dataTable.refGrid = this;

		header.dataTable.onmousedown = GRID_INIT;
		if (!IS_IE)
			this.headerDiv.onmouseup = GRID_END;
		header.dataTable.onmouseup = GRID_END;
		// 存储这两个引用直接供GRID_DRAG内部判断鼠标的位置区域时使用
		header.dataTable.refHeader = header;
		if (!IS_IE)
			this.headerDiv.onmousemove = GRID_DRAG;
		header.dataTable.onmousemove = GRID_DRAG;
	}

	// 根据header是固定还是动态,将header添加到不同的div中
	if (header.isFixedHeader == false)
		this.dynamicHeaderTableDiv.appendChild(header.dataTable);
	else
		this.fixedHeaderTableDiv.appendChild(header.dataTable);
	header.dataTable.style.height = this.constant.headerHeight + "px";
	header.dataTable.style.width = tableWidth + "px";
	header.dataTable.cellPadding = "0px";
	header.dataTable.cellSpacing = "0px";
	// 创建tbody
	oTBody = $ce("tbody");
	header.dataTable.appendChild(oTBody);

	// 单表头情况
	if (header.children == null) {
		headerTable.className = "headerdiv";
		headerTable.onmouseover = function(e) {
			// if(!e)
			// e = window.event;
			// if(window.dragStart)
			// return;
			// if(e.srcElement.nodeName != 'DIV')
			// return;
			// this.oldClassName = this.className;
			// this.className += " headerdiv_mouseover";
		};

		headerTable.onmouseout = function(e) {
			// if(!e)
			// e = window.event;
			// if(window.dragStart)
			// return;
			// if(e.srcElement.nodeName != 'DIV')
			// return;
			// this.className = this.oldClassName;
		};

		// 设置表头颜色
		if (header.required)
			headerTable.className += " header_required";
		else {
			// 根据header中的textcolor属性设置文字颜色
			if (header.textColor != null && header.textColor != "")
				headerTable.style.color = header.textColor;
		}

		var row = this.addTableRow(oTBody, null);
		var cell = row.insertCell(-1);
		cell.width = header.width;
		// 保存cell的引用在
		header.cell = cell;

		var selectBox = null;
		// 如果是默认的boolean渲染类型,或者编辑类型为EditorType.CHECKBOX,则创建全选checkbox
		if (header.renderType == BooleanRender
				|| header.editorType == EditorType.CHECKBOX
				|| (header.dataType == DataType.UFBOOLEAN && header.editorType != EditorType.STRINGTEXT)) {
			if (header.isShowCheckBox)		
				selectBox = this.createCheckBoxForSelAll(header);
		}

		// 将header的引用绑定在cell上
		cell.owner = header;
		tempDiv = $ce("div");
		// tempDiv.style.height = GridComp.HEADERROW_HEIGHT + "px";
		cell.appendChild(tempDiv);
		// 将放置内容的div引用绑定header上
		header.contentDiv = tempDiv;
		// 在header上保存grid的引用
		header.owner = this;

		// var padding = Math.floor((cell.offsetHeight - getTextHeight(
		// header.showName, headerTable.className)) / 2);
		// if (padding < 0)
		// padding = 0;

		header.textNode = $ce("DIV");
		header.textNode.id = header.keyName;
		// add by renxh 为表头添加编辑态
		var widgetid;
		try{
			widgetid = ((header.owner.widget) ? header.owner.widget.id : "");
		}catch(e){
			widgetid = "";
		}
		
		var params = {
			uiid : "",
			eleid : this.id,
			type : "grid_header",
			widgetid : widgetid,
			subeleid : header.keyName
		};
		if (window.editMode) {
			new EditableListener(header.textNode, params,
					EditableListener.COMPOMENT_TYPE);
		}
		tempDiv.appendChild(header.textNode);
		if (selectBox != null) {
			// 只有header的高度为2倍表头高度以上才设置此时padding
			// if (cell.offsetHeight > GridComp.HEADERROW_HEIGHT * 2)
			// tempDiv.style.paddingTop = (padding - 4) + "px";
			// tempDiv.appendChild(selectBox);
			header.selectBox = selectBox;
			header.textNode.appendChild(selectBox);
			header.textNode.appendChild(document
					.createTextNode(header.showName));
		} else {
			// tempDiv.style.paddingTop = padding + "px";
			// tempDiv.appendChild(document.createTextNode(header.showName));
			header.textNode.appendChild(document
					.createTextNode(header.showName));
		}

		// tempDiv.onmousedown = function(e)
		// {
		// if(!e)
		// e = window.event;
		// stopAll(e);
		// }

		// 显示"排序箭头",并排序,按着ctrl点击表头支持多列排序
		if (this.sortable && header.sortable) {
			tempDiv.onclick = function(e) {
				e = EventUtil.getEvent();

				// tempdiv相对body的left
				var offsetLeft = compOffsetLeft(this, document.body);
				// 当前鼠标的位置
				var currX = e.clientX + document.body.scrollLeft;
				if (currX > offsetLeft + 15
						&& currX < offsetLeft + this.offsetWidth - 15) {
					// 未按ctrl键
					if (!e.ctrlKey) {
						if (oThis.sortHeaders != null) {
							var headerDiv = null;
							for (var i = 0, count = oThis.sortHeaders.length; i < count; i++) {
								headerDiv = oThis.sortHeaders[i].contentDiv;
								if (headerDiv.sortImg.parentNode)
									headerDiv.sortImg.parentNode
											.removeChild(headerDiv.sortImg);
							}

							// 清除多列排序记录数组
							while (oThis.sortHeaders.length != 0) {
								oThis.sortHeaders.shift().contentDiv.sortImg = null;
							}
						}
						// 如果当前排序header不为空,且当前排序header不为现在点击的header,则先将当前排序header的图标隐藏
						if (oThis.sortHeader != null
								&& oThis.sortHeader != header) {
							var lastHeaderDiv = oThis.sortHeader.contentDiv;
							lastHeaderDiv.sortImg.parentNode
									.removeChild(lastHeaderDiv.sortImg);
							lastHeaderDiv.sortImg = null;
						}
					}
					// 按着ctrl键
					else {
						// 如果有上次单击的排序列,首先清除上次的单击排序列图标
						if (oThis.sortHeader != null
								&& oThis.sortHeader != header) {
							var lastHeaderDiv = oThis.sortHeader.contentDiv;
							lastHeaderDiv.sortImg.parentNode
									.removeChild(lastHeaderDiv.sortImg);
							lastHeaderDiv.sortImg = null;
							oThis.sortHeader = null;
						}
					}

					if (this.sortImg == null) {
						this.sortImg = $ce("img");
						this.sortImg.className = "sort_img";
						this.sortImg.src = window.themePath
								+ "/images/grid/sort_up.gif";
						tempDiv.appendChild(this.sortImg);
						// 记录当前的排序方向
						header.ascending = -1;
					} else {
						if (header.ascending == -1) {
							this.sortImg.src = window.themePath
									+ "/images/grid/sort_down.gif";
							header.ascending = 1;
						} else if (header.ascending == 1) {
							this.sortImg.src = window.themePath
									+ "/images/grid/sort_up.gif";
							header.ascending = -1;
						}
					}

					// 未按ctrl键
					if (!e.ctrlKey) {
						oThis.model.sortable([header], null, null);
						// 记录当前排序列
						oThis.sortHeader = header;
					}
					// 按着ctrl键
					else {
						// 记录需要排序的多列表头,按点击顺序记录
						if (oThis.sortHeaders == null)
							oThis.sortHeaders = new Array();
						oThis.sortHeaders.push(header);
					}
				}
				stopAll(e);
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
			};

			// ctrl键弹起时排序多列
			tempDiv.onkeyup = function(e) {
				e = EventUtil.getEvent();

				if (e.key == 17) {
					// if(oThis.sortHeaders != null)
					// oThis.sortHeaders.clear();
				}
				// 删除事件对象（用于清除依赖关系）
				clearEventSimply(e);
			};
		}
	}
	// 多表头情况
	else {
		headerTable.className = "multiheaderdiv";
		/* colspan,rowspan计算公式 */
		// colspan: td跨几列
		// rowspan: td跨几行 总体深度 - header所在层数(从0开始) - 孩子层数
		var tempHeaders = new Array();
		// totalDepth即为行数
		for (var i = 0; i < totalDepth; i++) {
			var row = header.dataTable.insertRow(i);
			// 得到这一行内的所有列(不包括隐藏列)
			tempHeaders = header.getVisibleHeadersByLevel(i);
			for (var j = 0; j < tempHeaders.length; j++) {
				// TODO 判断是否隐藏
				var tempHeader = tempHeaders[j];
				var cell = row.insertCell(-1);
				cell.className = "multiheadercell";
				// 将header的引用绑定在cell上
				cell.owner = tempHeader;
				cell.rowSpan = tempHeader.getRowspan(totalDepth);
				cell.colSpan = tempHeader.getColspan();
				var selectBox = null;
				var tempDiv = $ce("div");
				if (tempHeader.children == null) {
					var headerLevel = tempHeader.getHeaderLevel();
					// 不是多表头中最底层的header
					if (headerLevel != totalDepth - 1)
						cell.height = (this.headerDepth - tempHeader
								.getHeaderLevel())
								* this.headerRowHeight;
					else
						cell.height = this.headerRowHeight;

					// 如果是默认的boolean渲染类型,则创建全选checkbox(注:对于用户自己写的boolean渲染器不会创建全选checkbox)
					if (tempHeader.renderType == BooleanRender
							|| header.editorType == EditorType.CHECKBOX
							|| (header.dataType == DataType.UFBOOLEAN && header.editorType != EditorType.STRINGTEXT))
						selectBox = this.createCheckBoxForSelAll(tempHeader);
				} else {
					// cell.className = "multiheaderdiv";
					// 第0层header应有高度(header总深-孩子层数)*this.headerRowHeight
					if (tempHeader == header)
						cell.height = (this.headerDepth - header
								.getHeaderChildrenLevel())
								* this.headerRowHeight;
					else
						cell.height = this.headerRowHeight;
				}

				if (j != 0)
					cell.width = tempHeader.width - 1;
				else
					cell.width = tempHeader.width;
				// 将放置内容的div引用绑定header上
				tempHeader.contentDiv = tempDiv;
				// 在header上保存grid的引用
				tempHeader.owner = this;

				tempHeader.textNode = $ce("DIV");
				tempDiv.appendChild(tempHeader.textNode);

				/*
				 * div上不设置width if (tempHeader.children == null)
				 * tempDiv.style.width = tempHeader.width + "px"; else {
				 * //得到此header的真实宽度 // 得到此header下的所有basicHeaders var ownerBasics =
				 * tempHeader.getBasicHeadersBySpecify(); var realWidth = 0; for (
				 * var k = ownerBasics.length - 1; k >= 0; k--) realWidth +=
				 * ownerBasics[k].width; tempDiv.style.width = realWidth + "px"; }
				 */
				tempDiv.title = tempHeader.showName;

				if (selectBox != null) {
					// tempDiv.appendChild(selectBox);
					header.selectBox = selectBox;
					tempHeader.textNode.appendChild(selectBox);
					// tempDiv.appendChild(document.createTextNode(tempHeader.showName));
					tempHeader.textNode.appendChild(document
							.createTextNode(tempHeader.showName));
				} else {
					// tempDiv.appendChild(document.createTextNode(tempHeader.showName));
					tempHeader.textNode.appendChild(document
							.createTextNode(tempHeader.showName));
				}
				cell.appendChild(tempDiv);

			}
		}
		tempHeaders = null;
	}

	// 固定列的设置top,right边界
	if (header.isFixedHeader) {
		headerTable.className += " fixedheaderdiv";
	}

	// 列的第一个header不设置left边界(包括fixed和dynamic) TODO 这里需要判断未隐藏的固定列是否>0
	if (header == this.getFirstFixedVisibleHeader()
			|| header == this.getFirstDynamicVisibleHeader()) {
		headerTable.style.borderLeftWidth = "0px";
	}
	// headers最后一个加右边界
	if (header == this.getLastFixedVisibleHeader()
			|| header == this.getLastDynamicVisibleHeader()) {
		headerTable.style.borderRightWidth = getStyleAttribute(headerTable,
				"borderBottomWidth");
		headerTable.style.borderRightStyle = getStyleAttribute(headerTable,
				"borderBottomStyle");
		headerTable.style.borderRightColor = getStyleAttribute(headerTable,
				"borderBottomColor");
	}
};

/**
 * @private
 */
function GRID_INIT(e) {
	e = EventUtil.getEvent();

	var grid = this.refGrid;

	// 首先隐藏掉上一个显示出的控件
	if (grid.showComp != null) {
		grid.hiddenComp();
	}

	// 得到触发源(header.contentDiv)
	var src = getTarget(e);
	if (src.tagName != null
			&& (src.tagName.toLowerCase() == "input" || src.tagName
					.toLowerCase() == "img")) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}

	// 触发源div相对body的left
	var offsetLeft = compOffsetLeft(src, document.body);
	var outerDivScrollLeft = grid.dataOuterDiv.scrollLeft;
	// 当前鼠标的位置
	var currX = e.clientX + document.body.scrollLeft;
	// td
	// var dragSrc = src.parentNode;
	var dragSrc = null;
	if (src.nodeName == "TD") {
		dragSrc = src;
	} else if (src.nodeName == "TABLE") {
		// TABLE/TBODY/TR/TD
		dragSrc = src.childNodes[0].childNodes[0].childNodes[0]
	} else {
		var tempSrc = src.parentNode;
		while (tempSrc != null && tempSrc.nodeName != "TD") {
			tempSrc = tempSrc.parentNode;
		}
		if (tempSrc == null)
			dragSrc = src;
		else
			dragSrc = tempSrc;
	}

	// 获得触发源所在的header
	var curHeader = dragSrc.owner;
	if (curHeader == null) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}
	// 获得grid的引用
	window.gridOwner = curHeader.owner;

	// 鼠标在以下这两个区域内才能拖拽
	if (currX > offsetLeft + parseInt(dragSrc.width) - 5
			&& currX < offsetLeft + parseInt(dragSrc.width)) {
		// 保存真正处理拖动的header
		window.dragHeader = curHeader;
		window.src = src;
		window.dragStart = true;
		window.src.style.cursor = 'col-resize';
	}

	// cell(td)
	window.dragSrc = dragSrc;
	// 开始拖动时的X坐标
	window.dragSrcX = e.clientX + document.body.scrollLeft;
	// 拖动表头的原始宽度
	window.defaultHeaderWidth = parseInt(window.dragSrc.width);
	// 动态数据区的原始宽度
	window.dynamicColumDataDivWidth = window.gridOwner.dynamicColumDataDiv.offsetWidth;// + 2;
	// 动态表头的初始宽度
	window.defaultDynamicHeaderWidth = window.gridOwner
			.getDynamicTableDivRealWidth(true)
			+ 2;
	window.defaultDynHeaderTableWidth = window.gridOwner.dynamicHeaderTableDiv.offsetWidth;// + 2;
	// 保存此表头的允许最小宽度
	if (window.src != null) {
		if (window.src.firstChild.tagName != null
				&& window.src.firstChild.tagName.toLowerCase() == "input")
			window.minWidth = getTextWidth(window.src.childNodes[1].nodeValue,
					window.src.className)
					+ 25;
		else
			// window.minWidth = getTextWidth(window.src.firstChild.nodeValue,
			// window.src.className) + 10;
			window.minWidth = getTextWidth(window.src.innerHTML,
					window.src.nodeName)
					+ 10;
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * @private
 */
function GRID_END(e) {
	// e = EventUtil.getEvent();

	if (window.dragStart != null && window.dragStart) {
		window.dragStart = false;
		window.src.style.cursor = 'default';
		// guoweic: add start 2009-10-27
		if (IS_IE) // 仅IE中有此方法
			// guoweic: add end
			window.src.releaseCapture();

		var header = window.dragHeader;
		// 改变拖动表头的宽度
		if (header.isFixedHeader == false) {
			/* 改变动态表头的宽度、改变数据区的宽度、改变相应列的宽度 */
			// 改变的宽度
			var changedWidth = window.headerChangedWidth;
			// 没有drag则changedWidth为null,直接返回不进行下面的处理
			if (changedWidth == null)
				return;
			// 当前表头的宽度
			var currWidth = window.defaultHeaderWidth + changedWidth;
			if (currWidth > 0 && currWidth > window.minWidth) {
				/* 改变动态数据区、动态表头宽度 */
				// 动态列headerDiv
				var grid = window.gridOwner;
				// 改变动态表头区宽度
				grid.dynamicHeaderTableDiv.style.width = (window.defaultDynHeaderTableWidth + changedWidth)
						+ "px";
				// 改变拖动表头列宽度
				header.dataDiv.style.width = currWidth + "px";
				grid.dynamicColumDataDiv.style.width = (window.dynamicColumDataDivWidth + changedWidth)
						+ "px";
				// 改变拖动表头div的宽度
				if (header == grid.getLastDynamicVisibleHeader())
					header.contentDiv.style.width = (currWidth - 2) + "px";
				else
					header.contentDiv.style.width = (currWidth - 1) + "px";
				// 改变拖动表头td的宽度
				window.dragSrc.width = currWidth;
				// 改变拖动表头table的宽度
				header.dataTable.style.width = currWidth + "px";
				// 改变拖动表头的背景色为非选中背景
				// header.dataTable.className = header.dataTable.oldClassName;
				// 如果该列为合计列,设置合计列的宽度
				if (header.sumCell) {
					grid.dynSumRowContentDiv.style.width = (grid.dynamicHeaderDiv.offsetWidth + 4)
							+ "px";
					// grid.dynSumRowContentDiv.style.width =
					// (window.defaultDynHeaderTableWidth
					// + changedWidth) + "px";
					grid.dynSumRowDataDiv.style.width = (window.defaultDynHeaderTableWidth + changedWidth)
							+ "px";
					if (header.keyName == grid.basicHeaders[0].keyName)
						// header.sumCell.style.width = currWidth -
						// (GridComp.SUMCELL_PADDING * 2) + "px";
						header.sumCell.style.width = currWidth
								+ GridComp.ROWSTATE_COLUMN_WIDTH
								- GridComp.SUMROW_DIV_WIDTH
								- (GridComp.SUMCELL_PADDING * 2) + "px";
					else
						header.sumCell.style.width = currWidth
								- (GridComp.SUMCELL_PADDING * 2) + "px";
				}

				// 记录拖动表头的width属性
				header.width = currWidth;
				grid.adjustFixedColumDivHeight();
			}

			// 增加此段代码是为了避免表头和数据区div在拖动之后不重合的问题,关键所在! gd 2008-02-21
			var grid = window.gridOwner;
			if (isDivScroll(grid.dataOuterDiv)) {
				if (grid.dataOuterDiv.scrollLeft > 0) {
					grid.setScrollLeft(grid.dataOuterDiv.scrollLeft - 1);
				}
			}

			// guoweic: add start 2009-10-29
			// 循环设置该列每个cell的宽度，以适应XHTML中的宽度计算问题
			for (var i = 0, n = header.dataDiv.childNodes.length; i < n; i++) {
				// 获取cell元素
				if (header.dataDiv.childNodes[i].className.indexOf("gridcell_") != -1) {
					header.dataDiv.childNodes[i].style.width = (header.dataDiv.offsetWidth - 5)
							+ "px"; // 5为左右padding加上border的宽
				}
			}
			// guoweic: add end
		}
	}
};

/**
 * @private
 */
function GRID_DRAG(e) {
	e = EventUtil.getEvent();
	// 得到触发源(header.contentDiv)
	var src = getTarget(e);
	if (src.tagName != null
			&& (src.tagName.toLowerCase() == "input" || src.tagName
					.toLowerCase() == "img")) {
		// 删除事件对象（用于清除依赖关系）
		clearEventSimply(e);
		return;
	}

	// 触发源div相对body的left
	var offsetLeft = compOffsetLeft(src, document.body);
	// 当前鼠标的位置
	var currX = e.clientX + document.body.scrollLeft;

	if (window.dragStart == null || window.dragStart == false) {
		// td
		var dragSrc = null;
		if (src.nodeName == "TD") {
			dragSrc = src;
		} else if (src.nodeName == "TABLE") {
			// TABLE/TBODY/TR/TD
			dragSrc = src.childNodes[0].childNodes[0].childNodes[0]
		} else {
			var tempSrc = src.parentNode;
			while (tempSrc != null && tempSrc.nodeName != "TD") {
				tempSrc = tempSrc.parentNode;
			}
			if (tempSrc == null)
				dragSrc = src;
			else
				dragSrc = tempSrc;
		}

		var flag = 0;
		if (currX > offsetLeft + parseInt(dragSrc.width) - 5
				&& currX < offsetLeft + parseInt(dragSrc.width)) {
			// 多表头暂时不允许拖动
			if (this.refHeader && this.refHeader.isGroupHeader)
				return;
			flag = 1;
		}
		// if(currX > offsetLeft && currX < offsetLeft + 7)
		// {
		// // 第一个header不允许拖动
		// if(this.refGrid.getLastVisibleBasicHeader(this.refHeader) == null)
		// return;
		// // 多表头暂时不允许拖动
		// if(this.refGrid.getLastVisibleBasicHeader(this.refHeader) != null &&
		// this.refGrid.getLastVisibleBasicHeader(this.refHeader).isGroupHeader)
		// return;
		// flag = 2;
		// }

		if (flag == 1 || flag == 2)
			src.style.cursor = 'e-resize';
		else
			src.style.cursor = 'default';
	}
	// guoweic: modify start 2009-10-27
	// if (e.button == 1 && window.dragStart) {
	// IE中0表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
	if ((e.button == 1 || e.button == 0) && window.dragStart) {
		// guoweic: modify end
		window.src.style.cursor = 'col-resize';
		// guoweic: add start 2009-10-27
		if (IS_IE) // 仅IE中有此方法
			// guoweic: add end
			window.src.setCapture();
		window.headerChangedWidth = currX - window.dragSrcX;
	}
	// 删除事件对象（用于清除依赖关系）
	clearEventSimply(e);
};

/**
 * 创建数据区整体div
 * 
 * @private
 */
GridComp.prototype.initDataOuterDiv = function() {
	this.dataOuterDiv = $ce("div");
	this.outerDiv.appendChild(this.dataOuterDiv);
	this.dataOuterDiv.className = "data_outer_div";
	if (this.pageSize > 0 && this.isPagenationTop == true)
		this.dataOuterDiv.style.top = GridComp.PAGEBAR_HEIGHT + "px";
	else
		this.dataOuterDiv.style.top = "0px";

	this.dataOuterDiv.style.width = this.constant.outerDivWidth + "px";// - 2;

	// 分页则减去分页条的高度
	if (this.pageSize > 0) {
		var h = this.constant.outerDivHeight - GridComp.PAGEBAR_HEIGHT;
		if (h >= 0)
			this.dataOuterDiv.style.height = h + "px";
	} else
		this.dataOuterDiv.style.height = (this.constant.outerDivHeight - 1)
				+ "px";

	// 显示合计行则减去合计行的高度
	if (this.isShowSumRow)
		this.dataOuterDiv.style.height = (parseInt(this.dataOuterDiv.style.height) - GridComp.ROW_HEIGHT)
				+ "px";
};

/**
 * 创建固定列整体div
 * 
 * @private
 */
GridComp.prototype.initFixedColumDiv = function() {
	var h = this.constant.headerHeight;
	this.fixedColumDiv = $ce("div");
	this.fixedColumDiv.id = "fixedColum";
	this.outerDiv.appendChild(this.fixedColumDiv);
//	this.dataOuterDiv.appendChild(this.fixedColumDiv);
	this.fixedColumDiv.className = "fixedcolum_div";
	// this.fixedColumDiv.style.border = "solid blue 1px";
	// if(this.pageSize > 0)
	// this.fixedColumDiv.style.top = h + GridComp.PAGEBAR_HEIGHT;
	// else
	// this.fixedColumDiv.style.top = h;

	// 调整高度
	this.adjustFixedColumDivHeight();
	// 宽度和表头固定区一样
	this.fixedColumDiv.style.width = this.constant.fixedHeaderDivWidth + "px";
	// 将固定列的宽度放入常量对象
	this.constant.fixedColumDivWidth = this.constant.fixedHeaderDivWidth;
};

/**
 * 调整fixedColumDiv的高度,当滚动条从有到无或者从无到有时均需要调整此高度
 * 
 * @private
 */
GridComp.prototype.adjustFixedColumDivHeight = function() {
	var oH = this.constant.outerDivHeight, h = this.constant.headerHeight;
	// 调整top值
	if (this.pageSize > 0 && this.isPagenationTop == true)
		this.fixedColumDiv.style.top = (h + GridComp.PAGEBAR_HEIGHT) + "px";
	else
		this.fixedColumDiv.style.top = h + "px";

	// 调整高度
	if (this.isScroll()) {
		var height = oH - h - GridComp.SCROLLBAE_HEIGHT;
		if (height < 0)
			height = 0;
		this.fixedColumDiv.style.height = height + "px";// - 1;
		this.fixedColumDiv.defaultHeight = height;// - 1;
		if (this.pageSize > 0) {
			var curHeight = this.fixedColumDiv.defaultHeight
					- GridComp.PAGEBAR_HEIGHT;
			if (curHeight >= 0)
				this.fixedColumDiv.style.height = curHeight + "px";
			this.fixedColumDiv.defaultHeight = curHeight;
		}
	} else {
		this.fixedColumDiv.style.height = (oH > h ? (oH - h) : 0) + "px";
		this.fixedColumDiv.defaultHeight = oH > h ? (oH - h) : 0;
		if (this.pageSize > 0) {
			var curHeight = this.fixedColumDiv.defaultHeight
					- GridComp.PAGEBAR_HEIGHT;
			if (curHeight >= 0)
				this.fixedColumDiv.style.height = curHeight + "px";
			this.fixedColumDiv.defaultHeight = curHeight;
		}
	}
	if (this.pageSize > 0 && !this.isPagenationTop) {
		var tempH = this.fixedColumDiv.style.height;
		if (tempH != "") {
			tempH = tempH.replace("px", "");
			this.fixedColumDiv.style.height = tempH - 15 + "px";
			this.fixedColumDiv.defaultHeight = tempH - 15;
		}
	}
	
};

/**
 * 画行数字行标div
 * 
 * @private
 */
GridComp.prototype.initRowNumDiv = function() {
	this.rowNumDiv = $ce("div");
	this.fixedColumDiv.appendChild(this.rowNumDiv);
	this.rowNumDiv.id = "rowNumDiv";
	this.rowNumDiv.className = "num_div";
	this.rowNumDiv.style.width = this.constant.rowNumHeaderDivWidth + "px";
	// 保存此div所有子div的引用
	this.rowNumDiv.cells = new Array(this.getRowsNum());
};

/**
 * 创建行状态显示列
 * 
 * @private
 */
GridComp.prototype.initLineStateColumDiv = function() {
	this.lineStateColumDiv = $ce("div");
	var line = this.lineStateColumDiv;
	this.fixedColumDiv.appendChild(line);
	line.id = "lineStateColumDiv";
	line.className = "state_div";
	line.style.top = "0px";
	// line.style.border = "solid blue 1px";

	// 显示合计行,要减小行状态列的高度
	if (this.isShowSumRow) {
		line.style.height = (this.fixedColumDiv.defaultHeight - GridComp.ROW_HEIGHT)
				+ "px";
		line.defaultHeight = this.fixedColumDiv.defaultHeight
				- GridComp.ROW_HEIGHT;
	}

	// 设置left
	if (this.isShowNumCol)
		line.style.left = this.constant.rowNumHeaderDivWidth + "px";
	else
		line.style.left = "0px";
	line.style.width = this.constant.lineStateHeaderDivWidth + "px";
	// 保存此div所有子div的引用
	this.lineStateColumDiv.cells = new Array(this.getRowsNum());
};

/**
 * 画合计行在固定列区域的部分
 * 
 * @private
 */
GridComp.prototype.initSumRowDiv = function() {
	this.sumRowDiv = $ce("div");
	this.sumRowDiv.id = "sumRowDiv";
	this.sumRowDiv.className = "sum_div";
	this.outerDiv.appendChild(this.sumRowDiv);
	this.sumRowDiv.style.left = "0px";
	if (this.pageSize > 0 && this.isPagenationTop == false) // 有分页条并且分页条在下方
		this.sumRowDiv.style.top = (this.constant.outerDivHeight
				- GridComp.SCROLLBAE_HEIGHT - GridComp.PAGEBAR_HEIGHT - 12)
				+ "px";
	else
		this.sumRowDiv.style.top = (this.constant.outerDivHeight
				- GridComp.SCROLLBAE_HEIGHT - 4)
				+ "px";
	this.sumRowDiv.style.height = GridComp.ROW_HEIGHT + "px";
	this.sumRowDiv.style.lineHeight = GridComp.ROW_HEIGHT + "px";
	this.sumRowDiv.style.width = (this.tableSettingDiv.offsetWidth
			+ this.constant.rowNumHeaderDivWidth + 20)
			+ "px";
	this.sumRowDiv.innerHTML = "<center>" + trans("ml_total") + "</ceter>";
};
/**
 * 画合计行div,此div内放置合计行的每个cell
 * 
 * @private
 */
GridComp.prototype.initSumRowDataDiv = function() {
	// 创建放置动态数据区"合计"行的div,该div的overflow设置为hidden
	this.dynSumRowContentDiv = $ce("div");
	var cont = this.dynSumRowContentDiv;
	cont.className = "dynsumcontainer_div";
	this.outerDiv.appendChild(cont);
	// this.dataOuterDiv.appendChild(cont);
	if (this.dynamicHeaderDiv.defaultWidth + 4 > 0)
		cont.style.width = (this.dynamicHeaderDiv.defaultWidth + 4) + "px";
	else
		cont.style.width = "0px";
	cont.style.height = GridComp.ROW_HEIGHT + "px";

	if (this.pageSize > 0 && this.isPagenationTop == false) // 有分页条并且分页条在下方
		cont.style.top = (this.constant.outerDivHeight
				- GridComp.SCROLLBAE_HEIGHT - GridComp.PAGEBAR_HEIGHT - 12)
				+ "px";
	else
		cont.style.top = (this.constant.outerDivHeight
				- GridComp.SCROLLBAE_HEIGHT - 4)
				+ "px";

	cont.style.left = this.fixedColumDiv.offsetWidth + 20 + "px";
	cont.style.defaultLeft = this.fixedColumDiv.offsetWidth + 20 + "px";

	// 创建动态数据区"合计"行
	this.dynSumRowDataDiv = $ce("div");
	var d = this.dynSumRowDataDiv;
	d.className = "dynsumrow_div";
	d.id = "dynSumRowDataDiv";
	cont.appendChild(d);
	// d.style.top = 0;
	d.style.top = "0px";
	// d.style.left = 0;
	d.style.left = "0px";
	// d.defaultLeft = 0;
	d.defaultLeft = "0px";
	d.style.height = "100%";
	d.style.width = this.dynamicHeaderTableDiv.offsetWidth + "px";
		// guoweic: modify end
};

/**
 * 画合计行中的每个单元格
 * 
 * @private
 */
GridComp.prototype.initSumRowCells = function() {
	var num = 0;
	for (var i = 0, count = this.defaultDynamicHeaders.length; i < count; i++) {
		if (this.defaultDynamicHeaders[i].isHidden == false) {
			var cell = $ce("div");
			cell.headKey = this.defaultDynamicHeaders[i].keyName;
			// 表头保存sumcell的引用
			this.defaultDynamicHeaders[i].sumCell = cell;
			cell.className = "dynsumcell_div";
			this.dynSumRowDataDiv.appendChild(cell);
			cell.style.height = GridComp.ROW_HEIGHT + "px";
			cell.style.lineHeight = GridComp.ROW_HEIGHT + "px";
			if (i == 0)
				cell.style.width = this.defaultDynamicHeaders[i].width - 5 - 20
						+ "px";
			else
				cell.style.width = this.defaultDynamicHeaders[i].width - 5
						+ "px";
			cell.style.paddingLeft = "2px";
			cell.style.paddingRight = "2px";
		}
	}
};

/**
 * 创建固定选择列
 * 
 * @private
 */
GridComp.prototype.initSelectColumDiv = function() {
	this.selectColumDiv = $ce("div");
	this.fixedColumDiv.appendChild(this.selectColumDiv);
	this.selectColumDiv.id = "fixedSelectColum";
	this.selectColumDiv.className = "fixed_select_colum";
	// 将固定表头引用保存在固定选择列的header属性中
	this.selectColumDiv.header = this.selectColumHeaderDiv;
	// this.selectColumDiv.style.position = "absolute";
	// this.selectColumDiv.style.overflow = "hidden";
	this.selectColumDiv.style.left = (this.constant.rowNumHeaderDivWidth + this.constant.lineStateHeaderDivWidth)
			+ "px";
	this.selectColumDiv.style.width = GridComp.SELECTCOLUM_WIDTH + "px";
	// 当前model中的行数
	var rowNum = this.getRowsNum();
	// 存放所有cells索引的数组
	this.selectColumDiv.cells = new Array(rowNum);
	// 实际高度
	var realH = rowNum * this.rowHeight;
	this.selectColumDiv.style.height = realH + "px";
};

/**
 * 画固定列数据区div
 * 
 * @private
 */
GridComp.prototype.initFixedColumDataDiv = function() {
	var gridWidth = this.constant.outerDivWidth;
	var gridHeight = this.constant.outerDivHeight;
	this.fixedColumDataDiv = $ce("div");
	var $f = this.fixedColumDataDiv;
	this.fixedColumDiv.appendChild($f);
	$f.id = "fixedDataDiv";
	$f.style.position = "absolute";

	$f.style.left = (this.constant.rowNumHeaderDivWidth + this.constant.lineStateHeaderDivWidth)
			+ "px";
	// checkbox多选列模式
	if (this.isMultiSelWithBox)
		$f.style.left = (this.constant.rowNumHeaderDivWidth
				+ this.constant.lineStateHeaderDivWidth + GridComp.SELECTCOLUM_WIDTH)
				+ "px";
	$f.style.width = this.constant.fixedHeaderTableDivWidth + "px";

	// 当前model中的行数
	var rowNum = this.getRowsNum();
	// 固定列区域的高度
	var areaH = 0;
	if (this.scroll)
		areaH = gridHeight - this.constant.headerHeight
				- GridComp.SCROLLBAE_HEIGHT;
	else
		areaH = gridHeight - this.constant.headerHeight;
	if (areaH < 0)
		areaH = 0;
	// 实际高度
	var realH = rowNum * this.rowHeight;
	if (realH > areaH)
		$f.style.height = areaH + "px";
	else
		$f.style.height = realH + "px";

	$f.style.fontSize = "12px";
	if (this.defaultFixedHeaders != null) {
		var tempLen = this.defaultFixedHeaders.length;
		for (var i = 0; i < tempLen; i++) {
			var tempHeader = this.defaultFixedHeaders[i];
			// 单表头情况
			if (tempHeader.basicHeaders == null && tempHeader.isHidden == false) {
				tempHeader.dataDiv = $ce("div");
				var tempDiv = tempHeader.dataDiv;
				// 将header的引用保存在dataDiv上
				tempDiv.header = tempHeader;
				// 列的数据存放数组,为了快速检索数据
				tempDiv.cells = new Array(rowNum);
				$f.appendChild(tempDiv);
				tempDiv.style.width = tempHeader.width + "px";
				tempDiv.style.height = rowNum * this.rowHeight + "px";
				tempDiv.style.position = "relative";
				tempDiv.style[ATTRFLOAT] = "left";
				tempDiv.style.overflow = "hidden";
			}
			// 多表头情况
			else if (tempHeader.basicHeaders != null
					&& tempHeader.isHidden == false) {
				var tempHeaders = tempHeader.basicHeaders;
				for (var j = 0; j < tempHeaders.length; j++) {
					tempHeaders[j].dataDiv = $ce("div");
					var tempDiv = tempHeaders[j].dataDiv;
					// 将header的引用保存在dataDiv上
					tempDiv.header = tempHeaders[j];
					// 列的数据存放数组,为了快速检索数据
					tempDiv.cells = new Array(rowNum);
					$f.appendChild(tempDiv);
					tempDiv.style.width = tempHeaders[j].width + "px";
					tempDiv.style.height = rowNum * this.rowHeight + "px";
					tempDiv.style.position = "relative";
					tempDiv.style[ATTRFLOAT] = "left";
					tempDiv.style.overflow = "hidden";
				}
			}
		}
	}
};

/**
 * 画动态列数据区div
 * 
 * @private
 */
GridComp.prototype.initDynamicColumDataDiv = function() {
	var iOffsetWidth = this.constant.fixedColumDivWidth;
	var rowsNum = this.getRowsNum();
	this.dynamicColumDataDiv = $ce("div");
	this.dataOuterDiv.appendChild(this.dynamicColumDataDiv);
	this.dynamicColumDataDiv.style.position = "absolute";
	this.dynamicColumDataDiv.className = "dynamic_data_div";
	this.dynamicColumDataDiv.id = "dynamicDataDiv";
	// this.dynamicColumDataDiv.style.border = "solid black 1px";

	this.dynamicColumDataDiv.style.top = this.constant.headerHeight + "px";
	this.dynamicColumDataDiv.style.left = iOffsetWidth + "px";
	// 保存数据区真正的宽度值,动态表格区真正的宽度值
	var dynamicDataDivRealWidth = this.getDynamicTableDivRealWidth(true);
	this.realWidth = dynamicDataDivRealWidth + iOffsetWidth;
	// this.dynamicColumDataDiv.style.width = dynamicDataDivRealWidth - 2 +
	// "px";
	this.dynamicColumDataDiv.style.width = dynamicDataDivRealWidth + "px";
	this.dynamicColumDataDiv.style.height = rowsNum * this.rowHeight + "px";

	if (this.defaultDynamicHeaders != null) {
		var len = this.defaultDynamicHeaders.length;
		var rowNum = this.getRowsNum();
		for (var i = 0; i < len; i++) {
			var tempH = this.defaultDynamicHeaders[i];
			// 单表头情况
			if (tempH.basicHeaders == null && tempH.isHidden == false) {
				tempH.dataDiv = $ce("div");
				var tempDiv = tempH.dataDiv;
				// 将header的引用保存在dataDiv上
				tempDiv.header = tempH;
				// 列的数据存放数组,为了快速检索数据
				tempDiv.cells = new Array(rowsNum);
				this.dynamicColumDataDiv.appendChild(tempDiv);
				tempDiv.style.width = tempH.width + "px";
				tempDiv.style.height = rowsNum * this.rowHeight + "px";
				tempDiv.style.position = "relative";
				tempDiv.style[ATTRFLOAT] = "left";
				tempDiv.style.overflow = "hidden";
			}
			// 多表头情况
			else if (tempH.basicHeaders != null && tempH.isHidden == false) {
				var tempHeaders = tempH.basicHeaders;
				for (var j = 0; j < tempHeaders.length; j++) {
					if (tempHeaders[j].isHidden == false) {
						tempHeaders[j].dataDiv = $ce("div");
						var tempDiv = tempHeaders[j].dataDiv;
						// 将header的引用保存在dataDiv上
						tempDiv.header = tempHeaders[j];
						// 列的数据存放数组,为了快速检索数据
						tempDiv.cells = new Array(rowsNum);
						this.dynamicColumDataDiv.appendChild(tempDiv);
						tempDiv.style.width = tempHeaders[j].width + "px";
						tempDiv.style.height = rowsNum * this.rowHeight + "px";
						tempDiv.style.position = "relative";
						tempDiv.style[ATTRFLOAT] = "left";
						tempDiv.style.overflow = "hidden";
					}
				}
			}
		}
	}

	if (isDivVScroll(this.dataOuterDiv)) {
		this.setScrollTop(0);
	}
};

/**
 * 返回model中的行数量
 * 
 * @private
 */
GridComp.prototype.getRowsNum = function() {
	if (this.model == null)
		return 0;
	else
		return this.model.getRowsCount();
};

/**
 * 得到动态列中给定header之前的所有显示的动态列header的宽度和
 * 
 * @private
 */
GridComp.prototype.getPreHeadersWidth = function(cell) {
	var totalWidth = 0;
	// 得到此cell的header
	var len = this.basicHeaders.length;
	for (var i = 0; i < len; i++) {
		var header = this.basicHeaders[i];
		if (!header.isFixedHeader && !header.isHidden) {
			if (header != this.basicHeaders[cell.colIndex])
				totalWidth += header.width;
			else
				break;
		}
	}
	return totalWidth;
};

/**
 * 得到动态表头区或者固定表头区真正的宽度
 * 
 * @param isDynamic
 *            true:动态表区 false:静态表区
 * @private
 */
GridComp.prototype.getDynamicTableDivRealWidth = function(isDynamic) {
	if (this.headers == null)
		return;

	var realWidth = 0;
	if (isDynamic) {
		// 得到动态表头的宽度
		for (var i = 0; i < this.headers.length; i++) {
			var header = this.headers[i];
			if (header.isFixedHeader == false && header.isHidden == false)
				realWidth += this.getHeaderDefaultWidth(header)
						+ GridComp.COLUMN_LEFT_BORDER_WIDTH;
		}
	} else if (isDynamic == false) {
		// 得到固定表头的宽度
		for (var i = 0; i < this.headers.length; i++) {
			var header = this.headers[i];
			if (header.isFixedHeader && header.isHidden == false)
				realWidth += this.getHeaderDefaultWidth(header)
						+ GridComp.COLUMN_LEFT_BORDER_WIDTH;
		}
	}
	return realWidth;
};

/**
 * 设置grid的编辑属性,为true则点击cell时会激活相应的编辑控件
 * 
 * @param isEditable
 *            设置grid可否编辑
 * @private
 */
GridComp.prototype.setEditable = function(isEditable) {
	this.editable = getBoolean(isEditable, true);
	if (this.editable && this.compsInited == false)
		GridComp.initEditCompsForGrid(this.id);

	for (var i = 0; i < this.basicHeaders.length; i++) {
		var header = this.basicHeaders[i];
		if (header.renderType == BooleanRender) {
			if (isEditable) {
				if (header.selectBox != null) {
					header.selectBox.disabled = false;
					for (var j = 0; j < header.dataDiv.cells.length; j++) {
						if (this.paintedSign[j] == 1 && header.dataDiv.cells[j])
							header.dataDiv.cells[j].firstChild.disabled = false;
					}
				}
			} else {
				if (header.selectBox != null) {
					header.selectBox.disabled = true;
					for (var j = 0; j < header.dataDiv.cells.length; j++) {
						if (this.paintedSign[j] == 1 && header.dataDiv.cells[j])
							header.dataDiv.cells[j].firstChild.disabled = true;
					}
				}
			}
		}
	}
};

/**
 * 设置此grid是否激活
 * 
 * @param isActive
 *            true表示处于激活,否则表示禁用状态
 */
GridComp.prototype.setActive = function(isActive) {
	this.isGridActive = getBoolean(isActive, true);
};

/**
 * 初始化grid的编辑控件
 * 
 * @param gridId
 *            要初始化控件的grid id
 * @private
 */
GridComp.initEditCompsForGrid = function(gridId) {
	var oThis = window.objects[gridId];
	if (oThis == null)
		return;
	if (oThis.compsInited)
		return;
	// 标示控件已经被初始化过
	oThis.compsInited = true;
	// 标示各个控件是否已经初始化
	var stringInited = false;
	var textAreaInited = false;
	var integerInited = false;
	var decimalInited = false;
	var dateInited = false;
	var dateTimeInited = false;
	var boolInited = false;

	var basicHeaders = oThis.basicHeaders;
	if (oThis.compsMap == null)
		oThis.compsMap = new HashMap();

	for (var i = 0; i < basicHeaders.length; i++) {
		var comp = null;
		// textArea控件
		if (basicHeaders[i].editorType == EditorType.TEXTAREA
				&& textAreaInited == false) {
			comp = new TextAreaComp(document.body, "textArea", 0, 0, "", "",
					"absolute", false, "", "", "");
			oThis.compsMap.put(EditorType.TEXTAREA, comp);
			textAreaInited = true;
		} else if (basicHeaders[i].editorType == EditorType.INTEGERTEXT
				&& integerInited == false) {
			// 整型输入框
			comp = new IntegerTextComp(document.body, "integerText", 0, 0,
					"100%", "absolute", "", "", "");
			oThis.compsMap.put(EditorType.INTEGERTEXT, comp);
			integerInited = true;
		} else if (basicHeaders[i].editorType == EditorType.DECIMALTEXT
				&& decimalInited == false) {
			// 浮点数输入框
			// parent, name, left, top, width, position, precision, attrArr
			comp = new FloatTextComp(document.body, "floatText", 0, 0, "100%",
					"absolute", "", "");
			oThis.compsMap.put(EditorType.DECIMALTEXT, comp);
			decimalInited = true;
		} else if (basicHeaders[i].editorType == EditorType.CHECKBOX
				&& boolInited == false) {
			// 选择框
			comp = new CheckboxComp(document.body, "checkBox", 0, 0, "100%",
					"", false, "absolute");
			oThis.compsMap.put(EditorType.CHECKBOX, comp);
			boolInited = true;
		} else if (basicHeaders[i].editorType == EditorType.DATETEXT
				&& dateInited == false) {
			// 日期类型输入框
			comp = new DateTextComp(document.body, "dateText", 0, 0, "100%",
					"absolute", {
						readOnly : false
					});
			oThis.compsMap.put(EditorType.DATETEXT, comp);
			dateInited = true;
		} else if (basicHeaders[i].editorType == EditorType.DATETIMETEXT
				&& dateTimeInited == false) {
			// 日期时间类型输入框
			comp = new DateTextComp(document.body, "dateText", 0, 0, "100%",
					"absolute", {
						readOnly : false
					});
			comp.setShowTimeBar(true);
			oThis.compsMap.put(EditorType.DATETIMETEXT, comp);
			dateTimeInited = true;
		} else if (basicHeaders[i].editorType == EditorType.COMBOBOX) {
			// ComboComp(parent, name, left, top, width, position, selectOnly,
			// attrArr, className)
			// 下拉框类型(暂时设置下拉控件为仅选择的)
			comp = new ComboComp(document.body, "COMBOBOX" + i, 0, 0, "100%",
					"absolute", true, null, "");
			basicHeaders[i].comboComp = comp;
			// 设置输入控件的高度
			comp.Div_gen.style.height = GridComp.ROW_HEIGHT + "px";
			// 仅显示图片的combo
			if (basicHeaders[i].comboData != null) {
				// var keyValues = basicHeaders[i].comboData.getValueArray();
				// var captionValues = basicHeaders[i].comboData.getNameArray();
				// if (basicHeaders[i].showImgOnly) {
				// for ( var j = 0, count = keyValues.length; j < count; j++)
				// comp.createOption(captionValues[j], keyValues[j], null,
				// false, -1, true);
				// }
				// // 显示文字的combo
				// else {
				// for ( var j = 0, count = keyValues.length; j < count; j++)
				// comp.createOption(captionValues[j], keyValues[j], null,
				// false, -1, false);
				// }
				comp.setComboData(basicHeaders[i].comboData);
			}
			var key = EditorType.COMBOBOX + i;
			oThis.compsMap.put(key, comp);
		} else if (basicHeaders[i].editorType == EditorType.REFERENCE) {
			comp = new ReferenceTextComp(document.body, "Reference" + i, 0, 0,
					"100%", "absolute", "", basicHeaders[i].nodeInfo);
			comp.setBindInfo(oThis.model.dataset.id, basicHeaders[i].keyName);
			comp.widget = oThis.widget;
			var key = EditorType.REFERENCE + i;
			oThis.compsMap.put(key, comp);
		} else if (stringInited == false) {
			// 字符串输入框
			comp = new StringTextComp(document.body, "text", 0, 0, "100%",
					"absolute", "", "");
			oThis.compsMap.put(EditorType.STRINGTEXT, comp);
			stringInited = true;
		}
		if (comp != null) {
			comp.ingrid = true;
			comp.colIndex = i;
			var compKeyListener = new KeyListener();
			// compKeyListener.onEnter = function(keyEvent) {
			// GridComp.compEnterFun(oThis);
			// stopAll(keyEvent.event);
			// };
			compKeyListener.onKeyDown = function(keyEvent) {
				GridComp.compKeyDownFun(oThis, keyEvent.event);
			};
			comp.addListener(compKeyListener);
			comp.hideV();
			// // 下拉框和参照的值改变事件，修改DS内容
			// if (comp.componentType == "COMBOBOX" || comp.componentType ==
			// "REFERENCETEXT") {
			// 数据改变事件
			var textListener = new TextListener();
			textListener.valueChanged = function(valueChangeEvent) {
				GridComp.compValueChangeFun(oThis, valueChangeEvent);
			};
			comp.addListener(textListener);
			// }
			// 焦点移出事件
			var compFocusListener = new FocusListener();
			compFocusListener.onBlur = function(keyEvent) {
				GridComp.compBlurFun(oThis, keyEvent);
			};
			comp.addListener(compFocusListener);
		}
	}

		// 标示控件已经被初始化过
		// oThis.compsInited = true;
};

/**
 * 失去焦点时事件处理
 * 
 * @private
 */
GridComp.compBlurFun = function(oThis, keyEvent) {
	var currCell = oThis.selectedCell;
	if (currCell) {
		var comp = keyEvent.obj;
		// // 除了下拉框和参照以外，修改DS内容
		// if (comp.componentType != "COMBOBOX" && comp.componentType !=
		// "REFERENCETEXT") {
		// // 如果输入控件的旧值和改变后的值不一样才改变model的值
		// if (comp.oldValue != comp.getValue()
		// || comp.componentType == "COMBOBOX") { //TODO 下拉框的valuechange事件有问题
		// oThis.model.setValueAt(currCell.rowIndex, currCell.colIndex,
		// comp.getValue());
		// }
		// }

		// 激活将要激活的控件,因为控件隐藏触发失去焦点是个异步的过程,必须在上一个控件失去焦点后才能触发下一个控件激活
		if (oThis.nextNeedActiveCell) {
			oThis.setCellSelected(oThis.nextNeedActiveCell);
			oThis.nextNeedActiveCell = null;
		}
	}
};

/**
 * 值改变时事件处理
 * 
 * @private
 */
GridComp.compValueChangeFun = function(oThis, valueChangeEvent) {
	var currCell = oThis.selectedCell;
	if (currCell) {
		var comp = valueChangeEvent.obj;
		// 如果输入控件的旧值和改变后的值不一样才改变model的值
		var newValue = comp.getValue();
		if (comp.oldValue != newValue || comp.componentType == "COMBOBOX") { // TODO
			// 下拉框的valuechange事件有问题
			var colIndex = currCell.colIndex;
			// 可能出现修改的comp不是当前选中的Cell对应的comp
			// TODO 这里逻辑不完善，先只处理 COMBOBOX的情况
			if (comp.componentType == "COMBOBOX" && comp.colIndex != null
					&& comp.colIndex != currCell.colIndex)
				colIndex = comp.colIndex;
			// cell编辑后事件
			oThis.onAfterEdit(currCell.rowIndex, colIndex, comp.oldValue,
					newValue);
			oThis.model.setValueAt(currCell.rowIndex, colIndex, newValue);
		}
	}
};

/**
 * onkeydown事件处理
 * 
 * @private
 */
GridComp.compKeyDownFun = function(oThis, e) {
	// 当前激活的cell
	// "tab"键和"shift+tab"键和回车键处理
	if ((e.keyCode == 9 && e.shiftKey) || e.keyCode == 9 || e.keyCode == 13) {
		var activeCell = oThis.selectedCell;
		var nextActiveCell;
		if (oThis.editable) {
			nextActiveCell = oThis.getEditableCellByDirection(activeCell, 1);
			if (e.keyCode == 9 && e.shiftKey)
				nextActiveCell = oThis.getEditableCellByDirection(activeCell,
						-1);
		}
		// // guoweic: 不可编辑状态下屏蔽tab和shift tab功能
		// else {
		// nextActiveCell = oThis.getVisibleCellByDirection(activeCell, 1);
		// if (e.keyCode == 9 && e.shiftKey)
		// nextActiveCell = oThis.getVisibleCellByDirection(activeCell, -1);
		// }
		oThis.showComp.input.onblur();
		if (nextActiveCell != null
				&& activeCell.rowIndex == nextActiveCell.rowIndex) {
			// if (activeCell.rowIndex != nextActiveCell.rowIndex) // 选中新行
			// 不支持回车换行
			// oThis.model.setRowSelected(nextActiveCell.rowIndex);

			oThis.hiddenComp();
			oThis.setCellSelected(nextActiveCell);
		}
		stopAll(e);
	}

};

/**
 * 回车事件处理
 * 
 * @private
 */
GridComp.compEnterFun = function(oThis) {
	var activeCell = oThis.selectedCell;
	// 在最后一个可编辑单元格点回车，新增一行
	// if (activeCell != null) {
	// if ((activeCell.rowIndex == oThis.getRowsNum() - 1)
	// && (oThis.basicHeaders[activeCell.colIndex].keyName == oThis
	// .getLastVisibleHeader().keyName))
	// oThis.addRow();
	// }

	// 获取下一个将要激活的cell
	var nextActiveCell = oThis.getEditableCellByDirection(activeCell, 1);
	if (nextActiveCell != null) {
		if (activeCell.rowIndex != nextActiveCell.rowIndex) // 选中新行
			oThis.model.setRowSelected(nextActiveCell.rowIndex);
		oThis.nextNeedActiveCell = nextActiveCell;
		oThis.hiddenComp();
	}
};

/**
 * 隐藏当前显示的控件
 * 
 * @private
 */
GridComp.prototype.hiddenComp = function() {
	if (this.showComp != null) {
		var tempComp = this.showComp;
		this.showComp.hideV();
		this.showComp = null;
		// 将激活控件的记录变量设为null(只有编辑控件真正的隐藏才会设置当前的激活cell为null)
		this.currActivedCell = null;
		// if (!IS_IE)
		// tempComp.input.blur();
	}
};

/**
 * 隐藏所有已经显示出来的设置menu
 * 
 * @private
 */
GridComp.prototype.hiddenSettingMenu = function() {
	if (this.contextMenu && this.contextMenu.visible)
		this.contextMenu.hide();
};

/**
 * 得到最顶层header的默认初始宽度
 * 
 * @param header
 *            最顶层header
 * @private
 */
GridComp.prototype.getHeaderDefaultWidth = function(header) {
	if (header.parent != null)
		return null;

	var headerWidth = 0;
	// 单表头
	if (header.children == null)
		headerWidth = header.width;
	// 多表头
	else {
		var basicHeaders = header.basicHeaders;
		for (var j = 0, count = basicHeaders.length; j < count; j++) {
			if (basicHeaders[j].isHidden == false)
				headerWidth += basicHeaders[j].width;
		}
	}
	return headerWidth;
};

/**
 * 创建表格行
 * 
 * @private
 * @param table
 *            要增加的行的table的引用
 * @param posi
 *            要增加行的位置
 */
GridComp.prototype.addTableRow = function(tbody, posi) {
	if (posi == null || isNaN(posi))
		posi = tbody.rows.length;
	tbody.insertRow(posi);
	return tbody.rows[posi];
};

/**
 * 得到dataOuterDiv是否出了纵向滚动条
 * 
 * @private
 */
GridComp.prototype.isDataDivVScroll = function() {
	return isDivVScroll(this.dataOuterDiv);
};

/**
 * 得到dataOuterDiv是否出了横向滚动条
 * 
 * @private
 */
GridComp.prototype.isDataDivScroll = function() {
	return isDivScroll(this.dataOuterDiv);
};

/**
 * 根据当前grid中的行数判断是否出纵向滚动条
 * 
 * @return} true|false
 * @private
 */
GridComp.prototype.isVScroll = function() {
	var num = this.getRowsNum();
	if (num > 0) {
		// 是否竖直滚动
		if (num * this.rowHeight > this.constant.outerDivHeight
				- this.constant.headerHeight + 2)
			return true;
		else
			return false;
	} else if (num == 0)
		return false;
};

/**
 * 根据数据区(数字列+静态表头+动态表头)真正的宽度判断当前是否出横向滚动条
 * 
 * @return boolean 是否出横向滚动条
 * @private
 */
GridComp.prototype.isScroll = function() {
	var gridRealWidth = this.getDynamicTableDivRealWidth(true)
			+ this.getDynamicTableDivRealWidth(false)
			+ this.constant.rowNumHeaderDivWidth + 5;
	if (this.isMultiSelWithBox)
		gridRealWidth += GridComp.SELECTCOLUM_WIDTH;
	if (gridRealWidth > this.constant.outerDivWidth)
		return true;
	else
		return false;
};

/**
 * 插入一行 用户调用此方法插入一行数据
 * 
 * @param row
 *            GridCompRow
 * @param index
 *            插入的位置
 */
GridComp.prototype.insertRow = function(row, index) {
	if (this.model == null)
		this.model = new GridCompModel();
	if (row == null || GridCompRow.prototype.isPrototypeOf(row))
		return this.model.insertRow(row, index);
	else
		throw new Error("Row must be the instance of GridCompRow or null!");
};

/**
 * 增加一行 用户调用此方法增加一行数据
 * 
 * @param row
 *            GridCompRow
 */
GridComp.prototype.addRow = function(row) {
	if (this.model == null)
		this.model = new GridCompModel();
	if (row == null || GridCompRow.prototype.isPrototypeOf(row))
		return this.model.addRow(row);
	else
		throw new Error("Row must be the instance of GridCompRow or null!");
};

/**
 * 增加一组行
 * 
 * @param rows
 *            GridCompRow数组
 */
GridComp.prototype.addRows = function(rows) {
	if (rows != null) {
		for (var i = 0; i < rows.length; i++)
			this.addRow(rows[i]);
	}
};

/**
 * Model中插入行时触发此方法.
 * 
 * @param index
 *            插入行的位置
 * @private
 */
GridComp.prototype.fireRowInserted = function(index) {
	var gridHeight = this.constant.outerDivHeight;
	// 当前数据区的实际高度
	var currHeight = this.getRowsNum() * this.rowHeight;
	// 动态列数据区高度
	this.dynamicColumDataDiv.style.height = currHeight + "px";
	// 可见区域高度
	var areaHeight = 0;
	if (this.scroll)
		areaHeight = gridHeight - this.constant.headerHeight
				- GridComp.SCROLLBAE_HEIGHT;
	else
		areaHeight = gridHeight - this.constant.headerHeight;

	if (areaHeight < 0)
		areaHeight = 0;

	if (currHeight > areaHeight)
		this.fixedColumDataDiv.style.height = areaHeight + "px";
	else
		this.fixedColumDataDiv.style.height = currHeight + "px";

	// 每一个header.dataDiv的高度增加一个行高
	var basicHeaders = this.basicHeaders;
	for (var i = 0; i < basicHeaders.length; i++) {
		if (basicHeaders[i].isHidden == false)
			basicHeaders[i].dataDiv.style.height = currHeight + "px";
	}
	// 固定选择列的高度
	if (this.isMultiSelWithBox)
		this.selectColumDiv.style.height = currHeight + "px";

	var displayCont = this.calcuDisplayRowNum();
	var displayContTop = displayCont[0];
	var displayContBottom = displayCont[1];
	var modelLen = this.getRowsNum();
	// 插入行在当前显示区域内要画此行
	if (index <= displayContBottom && index >= displayContTop) {
		// 标示是插入行调用的addOneRow
		this.insertFlag = true;
		// 插入到最后一行
		if (index == modelLen - 1) {
			this.paintedSign.splice(index, 0, 1);
			// 向grid中增加一行
			this.addOneRow(this.model.getRow(index), index,
					this.dataOuterDiv.scrollLeft, this.headers.length,
					this.rowHeight, this.model.getRowsCount());

			// this.scrollToSelectedRow(index);
			// // 滚动的时候隐藏当前显示的控件
			// this.hiddenComp();
			// // 得到显示区域的top,bottom值
			// var displayCont = this.calcuDisplayRowNum();
			// var displayContTop = displayCont[0];
			// // 因为显示的特殊需要此displayContBottom的值要大于实际的显示区域的底部值,故减去2
			// var displayContBottom = displayCont[1] - 2;
			// var sRowH = index * this.rowHeight;
			//						
			// if(sRowH < displayContTop * this.rowHeight)
			// this.setScrollTop(displayContTop * this.rowHeight - sRowH);
			// else if(sRowH > displayContBottom * this.rowHeight)
			// this.setScrollTop(sRowH - displayContTop * this.rowHeight);
		}
		// 插入到中间一行
		else {
			// 将插入行位置下的所有行向下移一个单位,并改变所有移动行的id
			var signLen = this.paintedSign.length;
			for (var i = signLen - 1; i >= index && this.paintedSign[i] == 1; i--) {
				for (var j = 0; j < basicHeaders.length; j++) {
					if (basicHeaders[j].isHidden == false) {
						var cell = this.basicHeaders[j].dataDiv.cells[i];
						// 改变行号
						cell.rowIndex = cell.rowIndex + 1;
						// 改变top值
						cell.style.top = (cell.offsetTop + this.rowHeight)
								+ "px";
					}
				}
			}
			this.paintedSign.splice(index, 0, 1);
			this.addOneRow(this.model.getRow(index), index,
					this.dataOuterDiv.scrollLeft, this.headers.length,
					this.rowHeight, this.model.getRowsCount(), true);
		}
	}
	// 插入行不在显示区域内,只需将行状态数组插入当前行的状态即可
	else {
		if (index <= modelLen - 1) {
			this.paintedSign.splice(index, 0, 0);
		}
	}
};

/**
 * 创建行状态和行标div
 * 
 * @private
 */
GridComp.prototype.andLineStateAndColNum = function(rowCount, index, rowHeight,
		row) {
	var realIndex = null;
	if (this.insertFlag != null && this.insertFlag)
		realIndex = rowCount - 1;
	else
		realIndex = index;

	// 增加数字行号
	if (this.isShowNumCol) {
		var $n = $ce("div");
		$n.className = "row_num_div";
		var style = $n.style;
		$n.id = "numline" + realIndex;
		$n.rowIndex = realIndex;
		style.top = rowHeight * realIndex + "px";
		style.height = rowHeight + "px";
		$n.innerHTML = "<center>" + (realIndex + 1) + "</ceter>";
		this.rowNumDiv.appendChild($n);
	}

	// 创建行状态列内的div
	var $l = $ce("div");
	$l.className = "row_state_div";
	var stl = $l.style;
	$l.id = "linestate" + realIndex;
	$l.rowIndex = realIndex;
	stl.top = rowHeight * realIndex + "px";
	stl.height = rowHeight + "px";
	this.lineStateColumDiv.appendChild($l);

	if (this.insertFlag != null && this.insertFlag) {
		if (this.isShowNumCol)
			this.rowNumDiv.cells.splice(realIndex + 1, 0, $n);
		this.lineStateColumDiv.cells.splice(realIndex + 1, 0, $l);
		// 改变index位置的行状态图标
		this.lineStateColumDiv.cells[index].className = "row_state_div row_add_state";
	} else {
		if (this.isShowNumCol)
			this.rowNumDiv.cells[realIndex] = $n;
		this.lineStateColumDiv.cells[realIndex] = $l;
		if (row.rowData.state == DatasetRow.STATE_NEW)
			this.lineStateColumDiv.cells[index].className = "row_state_div row_add_state";
		else
			this.lineStateColumDiv.cells[index].className = "row_state_div";
	}
};

/**
 * 设置复选框选中状态
 * 
 * @param checked
 *            是否选中
 * @param rowIndex
 *            行号
 * @private
 */
GridComp.prototype.setCheckBoxChecked = function(checked, rowIndex) {
	if (this.groupHeaderIds.length > 0 && this.isMultiSelWithBox == true
			&& this.isGroupWithCheckbox == true) { // 分组复选框
		var groupRowIds = this.model.rows[rowIndex].groupRowIds;
		if (groupRowIds != null) {
			for (var i = groupRowIds.length - 1; i >= 0; i--) {
				var index = this.model.getRowIndexById(groupRowIds[i]);
				if (index != -1) {
					if (checked == true) {
						this.model.addRowSelected(index);
					} else {
						this.model.setRowUnSelected(index);
					}
				}
			}
		}
	} else {
		if (checked == true) {
			this.setFocusIndex(rowIndex);
			this.model.addRowSelected(rowIndex);
		} else {
			this.setFocusIndex(-1);
			this.model.setRowUnSelected(rowIndex);
		}
	}
};

/**
 * 调整新增多选div后面每条记录多选div的显示位置和rowIndex值
 * 
 * @param newCheckDiv
 *            新增的多选div
 * @private
 */
GridComp.prototype.adjustMultiSelBoxs = function(newCheckDiv) {
	if (this.isMultiSelWithBox) {
		startIndex = newCheckDiv.rowIndex;
		var checkDivs = this.selectColumDiv.childNodes;
		for (var i = 0, n = checkDivs.length; i < n; i++) {
			if (checkDivs[i].nodeName.toLowerCase() == "div") {
				if (checkDivs[i].rowIndex >= startIndex
						&& checkDivs[i] != newCheckDiv) {
					checkDivs[i].rowIndex += 1;
					checkDivs[i].style.top = this.rowHeight
							* checkDivs[i].rowIndex + "px";
				}
			}
		}

	}

};

/**
 * 给表格增加一行
 * 
 * @param row(model数据中的一行)
 * @param len
 *            即headers.length
 * @param isInsertInMiddle
 *            向中间插入行
 * @private
 */
GridComp.prototype.addOneRow = function(row, index, scrollLeft, len, rowHeight,
		rowCount, isInsertInMiddle) {
	var isOdd = this.isOdd(index);
	var k = 0;
	var tempHeaders = [], fixedheaderWidth = this.constant.fixedHeaderDivWidth, scrollTop = 0, oThis = this;
	// 注意:此句话非常耗费时间,在js代码的循环中避免重复调用,该处优化为传入该参数,大大节省了渲染时间
	// var iScrollLeft = this.dataOuterDiv.scrollLeft;

	// 判断是否出竖直滚动条,将headerDiv的宽度缩小17px
	
	var dsIndex = this.model.dataset.getRowIndex(row.rowData); 
	if (this.firstVScroll == false) {
		if (this.isVScroll()) {
			var barWidth = GridComp.SCROLLBAE_HEIGHT;
			var dynHeaderWidth = this.constant.outerDivWidth - fixedheaderWidth
					- barWidth - 1 + scrollLeft;
			if (dynHeaderWidth > 0)
				this.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
			this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
			this.headerDiv.defaultWidth = this.constant.outerDivWidth
					- barWidth - 1;
			this.firstVScroll = true;
		}
	}
	this.andLineStateAndColNum(rowCount, index, rowHeight, row);

	var checkDiv = null;
	var checkBox = null;
	if (this.isMultiSelWithBox) {
		// 向固定选择列增加选择框
		checkDiv = $ce("div");
		checkDiv.className = isOdd
				? "fixed_selectcolum_checkbox_div_odd"
				: "fixed_selectcolum_checkbox_div_even";
		checkDiv.rowIndex = index;
		checkDiv.editorType = "CheckBox";
		checkDiv.style.top = rowHeight * index + "px";
		checkDiv.style.left = "0px";
		checkDiv.style.width = GridComp.SELECTCOLUM_WIDTH + "px";
		checkDiv.style.height = rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH
				+ "px";
		checkDiv.style.lineHeight = rowHeight
				- GridComp.CELL_BOTTOM_BORDER_WIDTH + "px";
		checkBox = $ce("INPUT");
		checkBox.type = "checkbox";
		checkDiv.appendChild(checkBox);
		this.selectColumDiv.appendChild(checkDiv);

		// 设置选中状态
		// checkBox.checked = this.model.isRowSelected(index) ? true :
		// this.selectAllBox.checked;
		checkBox.checked = this.model.isRowSelected(index);

		// 通知model选中该行
		checkBox.onmousedown = function(e) {
			// 置空当前选择单元格
			oThis.selectedCell = null;
			oThis.oldCell = null;
			var rowIndex = this.parentNode.rowIndex;
			// 设置选中状态
			oThis.setCheckBoxChecked(!this.checked, rowIndex);
			// 隐藏编辑框
			oThis.hiddenComp();
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

		// 表示是插入行调用addOneRow
		if (this.insertFlag != null && this.insertFlag) {
			this.selectColumDiv.cells.splice(index, 0, checkDiv);
		} else
			this.selectColumDiv.cells[index] = checkDiv;

		// 如果是向中间插入行，则调整新增多选div后面每条记录多选div的显示位置和rowIndex值
		if (isInsertInMiddle == true
				&& index < this.selectColumDiv.childNodes.length - 1)
			this.adjustMultiSelBoxs(checkDiv);
	}

	// cell的top值
	var cellTop = rowHeight * index;
	// 动态表头长度
	var dynHeaderLen = this.defaultDynamicHeaders.length, lastHeader = this
			.getLastDynamicVisibleHeader(), firstHeader = this
			.getFirstDynamicVisibleHeader();
	var rowsCount = this.model.getRowsCount();
	this.setHeadersOffsetWidth();

	for (var i = 0; i < this.model.basicHeaders.length; i++) {
		var header = this.model.basicHeaders[i];
		if (header.isHidden == true)
			continue;

		var cell = $ce("div");
		cell.rowIndex = index;
		// 记录cell对应ds中的第几列
		cell.colIndex = i;
		cell.editorType = header.editorType;
		cell.className = isOdd ? "gridcell_odd" : "gridcell_even";

		cell.onmouseover = function() {
			oThis.showTipMessage(this);
			oThis.gridRowMouseOver(this);
		};
		cell.onmouseout = function() {
			oThis.hideTipMessage();
			oThis.gridRowMouseOut(this);
		};
		var cellStyle = cell.style;
		cellStyle.top = cellTop + "px";
		cellStyle.width = "100%";// header.width;
		// （guoweic：有问题，实际宽度会加上padding和border的宽）
		cellStyle.height = rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH + "px";
		// 在div中只有一行的情况下设置行距等于div高度,可以使div中的文字距中,从而不需要设置paddingTop
		cellStyle.lineHeight = rowHeight - GridComp.CELL_BOTTOM_BORDER_WIDTH
				+ "px";
		cellStyle.paddingLeft = "2px";
		cellStyle.paddingRight = "2px";
		// 根据header中textAlign属性设置文字在cell中的位置
		cellStyle.textAlign = header.textAlign;
		// 根据header中的textcolor属性设置文字颜色
		// if(header.textColor != null && header.textColor != "")
		// cellStyle.color = header.textColor;
		// 先将cell放入dataDiv,以便于子可以获取各种定位属性
		header.dataDiv.appendChild(cell);
		// guoweic: add start 2009-10-29
		if (header.dataDivWidth != null)
			cellStyle.width = (header.dataDivWidth - 5) + "px"; // 5为左右padding加上border的宽
		else
			cellStyle.width = (header.dataDiv.offsetWidth - 5) + "px"; // 5为左右padding加上border的宽

		// guoweic: add end

		// 如果分组显示,新分组开始的标示
		var newGroupBegin = false;
		// 如果分组显示,新分组结束的标示
		var newGroupEnd = false;
		if (header.isGroupBy == true) {
			// 第一个分组的header
			if (header.keyName == this.groupHeaderIds[0]) {
				if ((index > 0 && row.getCellValue(i) != this.model.rows[index
						- 1].getCellValue(i))
						|| index == 0) {
					newGroupBegin = true;
					// 为Model设置临时的新分组第一行
					this.model.newGroupRow = this.model.rows[index];
					// 第一行的相关分组行数组，用于级联选中和反选
					this.model.rows[index].groupRowIds = new Array();
					var rowId = this.model.rows[index].rowData.rowId;
					this.model.rows[index].groupRowIds.push(rowId);
				}
			}
			// 后面分组的header
			// 开始新组的条件:1.第一行肯定要分组 2.此cell数据和上一行该列的cell数据不一样
			// 3.上一个分组header列的该行的cell数据和此列上一行的不一致则也要分组
			else {
				if (index == 0
						|| (row.getCellValue(i) != this.model.rows[index - 1]
								.getCellValue(i))
						|| this.isCurrCellNeedNewGroup(row, index, i))
					newGroupBegin = true;
			}
			// 判断是否为新分组的最后一个cell
			if ((index <= (this.model.rows.length - 2) && row.getCellValue(i) != this.model.rows[index
					+ 1].getCellValue(i))
					|| index == this.model.rows.length - 1)
				newGroupEnd = true;
		}

		// 动态列
		if (header.isFixedHeader == false) {
			// 单表头
			if (header.children == null) {
				if (dynHeaderLen > 0 && header != firstHeader
						&& header != lastHeader) {
				} else {
					// 动态列最后一列为单表头的情况
					if (dynHeaderLen > 0 && header == lastHeader) {
						if (header.isGroupBy) {
						} else {
							var bottomWidth = getStyleAttribute(cell,
									"border-bottom-width");
							var bottomStyle = getStyleAttribute(cell,
									"border-bottom-style");
							var bottomColor = getStyleAttribute(cell,
									"border-bottom-color");
							if (bottomWidth)
								cell.style.borderRightWidth = bottomWidth;
							if (bottomStyle)
								cell.style.borderRightStyle = bottomStyle;
							if (bottomColor)
								cell.style.borderRightColor = bottomColor;
						}
					}
				}
			} else {
				if (dynHeaderLen > 0) {
					if (header != firstHeader && header != lastHeader) {
						cell.className = isOdd
								? "gridcell_odd"
								: "gridcell_even";
					}
					// 动态列第一列为多表头的情况
					else if (header == firstHeader) {
						cell.className = isOdd
								? "gridcell_odd"
								: "gridcell_even";
						if (j == 0) {
							// 多表头最后一列不显示右边线
							cell.style.borderLeft = "0";
						}
					}
					// 动态列最后一列为多表头的情况
					else if (header == lastHeader) {
						cell.className = isOdd
								? "gridcell_odd"
								: "gridcell_even";
					}
				}

			}
		}
		// 固定列
		else {
			// 单表头
			if (header.children == null) {
				cell.className = isOdd
						? "fixed_gridcell_odd"
						: "fixed_gridcell_even";
			} else {
				cell.className = "fixed_colum_grid_cell";
			}
		}

		// 渲染cell
		var childNode = null;
		if (!header.isGroupBy) {
			childNode = header.renderType.render(index, i, row.getCellValue(i),
					header, cell, dsIndex);
		}
		// 分组显示的列
		else if (header.isGroupBy == true) {
			// 新组开始才画该cell
			var realValue = null;
			if (newGroupBegin)
				realValue = row.getCellValue(i);
			childNode = header.renderType.render(index, i, realValue, header,
					cell, dsIndex);
			if (checkDiv != null && checkBox != null
					&& this.isGroupWithCheckbox) {
				if (!newGroupBegin) {
					// 第一个分组的header
					if (header.keyName == this.groupHeaderIds[0]) {
						// 去掉除新组第一个cell以外的复选框
						checkDiv.removeChild(checkBox);
						// 向第一个分组行增加相关分组行数据，用于级联选中和反选
						var rowId = this.model.rows[index].rowData.rowId;
						this.model.newGroupRow.groupRowIds.push(rowId);
					}
				}
			}
			// 新组最后一个cell去掉底边
			if (!newGroupEnd) {
				// cell.style.borderBottomWidth = "0px";
				// cell.style.height = GridComp.ROW_HEIGHT +
				// GridComp.CELL_BOTTOM_BORDER_WIDTH + "px";
				var bottomColor = getStyleAttribute(cell, "background-color");
				cell.style.borderBottomColor = bottomColor;
			}
		}

		// 根据header中的bgcolor属性设置列背景色
		if (header.columBgColor != null && header.columBgColor != "")
			cellStyle.backgroundColor = header.columBgColor;

		if (this.insertFlag != null && this.insertFlag)
			header.dataDiv.cells.splice(index, 0, cell);
		else
			header.dataDiv.cells[index] = cell;

	}

	this.clearHeadersOffsetWidth();
	this.insertFlag = false;
};

GridComp.prototype.setTipSticky = function() {
	this.tipSticky = true;
};
GridComp.prototype.hideTipMessage = function(force) {
	// this.tipSticky = true;
	if (!this.tipSticky || force) {
		if (GridComp.tipDiv)
			GridComp.tipDiv.style.display = "none";
	}
	if (this.tipRt)
		clearTimeout(this.tipRt);
};
GridComp.prototype.showTipMessage = function(cell) {
	if (this.tipRt != null)
		clearTimeout(this.tipRt);
	var title = cell.tip;
	if (title == null || title == "")
		return;
	var left = compOffsetLeft(cell, document.body) + 1;
	var top = compOffsetTop(cell, document.body) + GridComp.ROW_HEIGHT;
	var width = (cell.offsetWidth - 2) + "px";
	this.tipRt = setTimeout("GridComp.doShowTipMessage(" + left + "," + top
			+ ",'" + width + "','" + encodeURIComponent(title) + "')", 500);
};

GridComp.doShowTipMessage = function(left, top, width, title) {
	if (title.length > 10)
		width = "220px";
	var div = GridComp.tipDiv;
	if (GridComp.tipDiv == null) {
		div = $ce("DIV");
		div.style.position = "absolute";
//dingrf 111121 改用 Measures.js中的 getZIndex()方法。		
//		div.style.zIndex = 1000;
		div.style.zIndex = getZIndex();
		div.style.backgroundColor = "yellow";
		div.style.border = "1px solid red";
		GridComp.tipDiv = div;
		document.body.appendChild(div);
	}
	div.style.display = "";
	div.style.left = left + "px";
	div.style.top = top + "px";
	div.style.width = width;
	// div.style.height = "10px";
	// div.style.background = "";
	div.innerHTML = decodeURIComponent(title);
};

/**
 * 设置每个Header的OffsetWidth属性，避免增行时反复计算
 * 
 * @private
 */
GridComp.prototype.setHeadersOffsetWidth = function() {
	for (var i = 0, n = this.headers.length; i < n; i++) {
		var header = this.headers[i];
		if (header.dataDiv != null) {
			header.dataDivWidth = header.dataDiv.offsetWidth;
			var basicHeaders = header.basicHeaders;
			if (basicHeaders != null) {
				for (var j = 0, m = basicHeaders.length; j < m; j++) {
					var basicHeader = basicHeaders[j];
					basicHeader.dataDivWidth = basicHeader.dataDiv.offsetWidth;
				}
			}
		}
	}
};

/**
 * 清空每个Header的OffsetWidth属性
 * 
 * @private
 */
GridComp.prototype.clearHeadersOffsetWidth = function() {
	for (var i = 0, n = this.headers.length; i < n; i++) {
		var header = this.headers[i];
		header.dataDivWidth = null;
		var basicHeaders = header.basicHeaders;
		if (basicHeaders != null) {
			for (var j = 0, m = basicHeaders.length; j < m; j++) {
				basicHeaders[j].dataDivWidth = null;
			}
		}
	}
};

/**
 * 鼠标移入后改变行样式
 * 
 * @private
 */
GridComp.prototype.gridRowMouseOver = function(cell) {
	var rowIndex = cell.rowIndex;
	// 改变当前行的外观
	for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
		var header = this.basicHeaders[i];
		if (header.isHidden == false) {
			var tempCell = header.dataDiv.cells[rowIndex];
			if (tempCell != null) {
				if (!tempCell.isErrorCell) {
					var isOdd = this.isOdd(rowIndex);
					tempCell.oldClassName = tempCell.className;
					tempCell.className = isOdd
							? "gridcell_odd gridcell_mouseover"
							: "gridcell_even gridcell_mouseover";
				}
			}
		}
	}
};

/**
 * 鼠标移出后改变行样式
 * 
 * @private
 */
GridComp.prototype.gridRowMouseOut = function(cell) {
	var rowIndex = cell.rowIndex;
	var selectedRowIndice = this.getSelectedRowIndice();
	if (selectedRowIndice && selectedRowIndice.indexOf(rowIndex) != -1) {
		this.rowSelected(rowIndex);
		return;
	}
	for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
		var header = this.basicHeaders[i];
		if (header.isHidden == false) {
			var tempCell = header.dataDiv.cells[rowIndex];
			if (tempCell != null) {
				if (!tempCell.isErrorCell) {
					tempCell.className = cell.oldClassName;
				}
			}
		}
	}
};

/**
 * 判断该cell是否应该启动新的分组
 * 
 * @private
 */
GridComp.prototype.isCurrCellNeedNewGroup = function(row, rowIndex,
		curGroupColIndex) {
	// 分组列列号数组
	var indice = this.groupHeaderColIndice;
	if (indice != null && indice.length > 0) {
		// 获取上一个最近的分组
		var startIndex = indice.indexOf(curGroupColIndex) - 1;
		for (var i = startIndex; i >= 0; i--) {
			if ((row.getCellValue(indice[i]) != this.model.rows[rowIndex - 1]
					.getCellValue(indice[i])))
				return true;
		}
		return false;
	} else
		return false;
};

/**
 * 若选中行不在选中区域,滚动行到选中的行
 * 
 * @param index
 *            行位置
 * @private
 */
GridComp.prototype.scrollToSelectedRow = function(index) {
	// 滚动时将当前选中的cell置空,则在index行选中时会清除当前行的选中cell的外观
	this.selectedCell = null;
	// 滚动的时候隐藏当前显示的控件
	this.hiddenComp();

	// 得到显示区域的top,bottom值
	var displayCont = this.calcuDisplayRowNum();
	var displayContTop = displayCont[0] + 2;
	// 因为显示的特殊需要此displayContBottom的值要大于实际的显示区域的底部值,故减去2
	var displayContBottom = displayCont[1] - 2;
	var sRowH = index * this.rowHeight;

	// guoweic: modify start 2009-10-22
	if (sRowH < displayContTop * this.rowHeight)
		// this.dataOuterDiv.scrollTop -= (displayContTop * this.rowHeight -
		// sRowH);
		this.setScrollTop(parseFloat(this.dataOuterDiv.scrollTop)
				- (displayContTop * this.rowHeight - sRowH));
	else if (sRowH > displayContBottom * this.rowHeight)
		// this.dataOuterDiv.scrollTop += sRowH - displayContTop *
		// this.rowHeight;
		this.setScrollTop(parseFloat(this.dataOuterDiv.scrollTop)
				+ (sRowH - displayContTop * this.rowHeight));
	// guoweic: modify end

	displayCont = this.calcuDisplayRowNum();
	displayContTop = displayCont[0];
	displayContBottom = displayCont[1];

	var rows = this.model.getRows();
	var modelLen = this.model.getRowsCount();
	var scrollLeft = this.dataOuterDiv.scrollLeft;
	var len = this.headers.length;
	var rowHeihgt = this.rowHeight, rowCount = this.model.getRowsCount();
	for (var i = displayContTop; i <= displayContBottom && i < modelLen; i++) {
		// 如果原来没有显示过,将此行数据添加到界面上。
		if (this.paintedSign[i] == 0)
			this.addOneRow(rows[i], i, scrollLeft, len, rowHeihgt, rowCount);
		this.paintedSign[i] = 1;
	}
};

/**
 * 画区域
 * 
 * @private
 */
GridComp.prototype.paintZone = function() {
	if (this.model == null)
		return;
	if (this.needPaintRows != null && this.needPaintRows == true) {
		this.needPaintRows = false;
		this.paintRows();
	} else {
		var num = this.calcuDisplayRowNum();
		// 对每行进行处理
		var rows = this.model.getRows();
		if (rows == null || rows.length == 0)
			return;
		var modelLen = rows.length;
		var scrollLeft = this.dataOuterDiv.scrollLeft;
		var len = this.headers.length;
		var rowHeihgt = this.rowHeight, rowCount = this.model.getRowsCount();
		for (var i = num[0]; i < num[1] && i < modelLen; i++) {
			// 如果原来没有显示过,将此行数据添加到界面上
			if (this.paintedSign[i] == 0)
				this
						.addOneRow(rows[i], i, scrollLeft, len, rowHeihgt,
								rowCount);
			this.paintedSign[i] = 1;
		}
	}
};

/**
 * 计算显示区域,返回最小和最大的二元素整型数组
 * 
 * @private
 */
GridComp.prototype.calcuDisplayRowNum = function() {
	var topPos = this.dataOuterDiv.scrollTop;
	var bottomPos;
	if (this.isScroll())
		bottomPos = topPos + this.constant.outerDivHeight
				- this.constant.headerHeight - GridComp.SCROLLBAE_HEIGHT;
	else
		bottomPos = topPos + this.constant.outerDivHeight
				- this.constant.headerHeight;
	var num = new Array();
	num[0] = Math.floor(topPos / this.rowHeight);
	num[1] = Math.ceil(bottomPos / this.rowHeight);
	return num;
};

/**
 * 得到key在header中的位置
 * 
 * @return key在headers中的索引
 * @private
 */
GridComp.prototype.nameToIndex = function(key) {
	for (var i = this.basicHeaders.length - 1; i >= 0; i--) {
		if (this.basicHeaders[i].keyName == key)
			return i;
	}
	return -1;
};

/**
 * 得到当前选中行的索引数组
 * 
 * @return 当前选中行的索引数组
 * @private
 */
GridComp.prototype.getSelectedRowIndice = function() {
	if (this.isMultiSelWithBox == false)
		return this.selectedRowIndice;
	else {
		if (this.model.dataset != null)
			return this.model.getSelectedIndices();
	}
};

/**
 * 得到选中的所有行
 * 
 * @return GridCompRow的数组
 * @private
 */
GridComp.prototype.getSelectedRows = function() {
	if (this.isMultiSelWithBox == false) {
		if (this.selectedRowIndice != null && this.selectedRowIndice.length > 0)
			return [this.getRow(this.selectedRowIndice[0])];
	} else
		return this.model.getSelectedRows();
};

/**
 * 改变点击的cell所在一行的所有cell的外观
 * 
 * @param rowIndex
 *            行号
 * @param isAddSel
 *            是否追加显示选中行
 * @private
 */
GridComp.prototype.rowSelected = function(rowIndex, isAddSel) {
	// 要改变外观的行没有显示出来要首先显示出来
	// if(this.paintedSign[rowIndex] == 0)
	// if (this.pageSize > 0) { // 有分页条，要将其高度减去
	// if ((rowIndex + 1) * GridComp.ROW_HEIGHT - GridComp.PAGEBAR_HEIGHT <
	// this.dataOuterDiv.scrollTop
	// || (rowIndex + 2) * GridComp.ROW_HEIGHT + GridComp.PAGEBAR_HEIGHT >
	// (this.dataOuterDiv.scrollTop + this.dataOuterDiv.offsetHeight))
	// this.scrollToSelectedRow(rowIndex);
	// } else {
	// if ((rowIndex + 1) * GridComp.ROW_HEIGHT < this.dataOuterDiv.scrollTop
	// || (rowIndex + 2) * GridComp.ROW_HEIGHT > (this.dataOuterDiv.scrollTop +
	// this.dataOuterDiv.offsetHeight))
	// this.scrollToSelectedRow(rowIndex);
	// }

	// 此模式下只能有一行的外观为选中行样式
	if (isAddSel == null) {
		// 改变上次选中行外观效果
		if (this.selectedRowIndice != null && this.selectedRowIndice.length > 0) {
			if (this.selectedRowIndice[0] != -1) {
				for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
					var header = this.basicHeaders[i];
					if (header.isHidden == false) {
						var selIndex = this.selectedRowIndice[0];
						var isOdd = this.isOdd(selIndex);
						var seleCell = header.dataDiv.cells[selIndex];
						// 将上次选中行的背景色全部变为白色,需要校验的field不变色
						if (seleCell != null) {
							if (seleCell.isErrorCell) {
								if (header.isFixedHeader)
									seleCell.className = isOdd
											? "fixed_gridcell_odd cell_error"
											: "fixed_gridcell_even cell_error";
								else
									seleCell.className = isOdd
											? "gridcell_odd cell_error"
											: "gridcell_even cell_error";
							} else {
								if (header.isFixedHeader)
									seleCell.className = isOdd
											? "fixed_gridcell_odd"
											: "fixed_gridcell_even";
								else
									seleCell.className = isOdd
											? "gridcell_odd"
											: "gridcell_even";
							}
						}
					}
				}
			}
		}
		// 没有记录的选中行,说明是第一次点击
		else
			this.selectedRowIndice = new Array();

		// 改变当前行的外观
		this.changeCurrSelectedRowStyle(rowIndex);
		// 设置行状态
		if (this.isMultiSelWithBox) {
			if (this.selectedRowIndice.length > 0) {
				// 恢复上次选中行的状态图标
				var node = this.lineStateColumDiv.cells[this.selectedRowIndice[0]];
				if (node != null
						&& node.className == "row_state_div row_normal_state")
					node.className = "row_state_div";

				var curNode = this.lineStateColumDiv.cells[rowIndex];
				if (curNode != null && curNode.className != "row_state_div row_delete_state")
					curNode.className = "row_state_div row_normal_state";
			}
		} else {
			var node = this.lineStateColumDiv.cells[rowIndex];
			if (node != null
					&& node.className != "row_state_div row_update_state"
					&& node.className != "row_state_div row_add_state"
					&& node.className != "row_state_div row_delete_state")
				node.className = "row_state_div row_normal_state";
		}

		// 改变当前点击的cell的外观
		this.changeSelectedCellStyle(rowIndex);

		if (this.selectedCell != null && this.currActivedCell != null
				&& (this.currActivedCell != this.selectedCell)) {
			if (this.selectedCell.rowIndex == rowIndex)
				this.selectedCell.className = "cell_select";
			else {
				var isOdd = this.isOdd(rowIndex);
				var header = this.basicHeaders[this.selectedCell.colIndex];
				if (header.isFixedHeader)
					this.selectedCell.className = isOdd
							? "fixed_gridcell_odd"
							: "fixed_gridcell_even";
				else
					this.selectedCell.className = isOdd
							? "gridcell_odd"
							: "gridcell_even";
			}
		}
		// 保存当前选中行
		this.selectedRowIndice[0] = rowIndex;
	}
	// 追加行选中
	else if (isAddSel) {
		if (this.selectedRowIndice == null)
			this.selectedRowIndice = [];

		// 将当前点击行的外观改变
		this.changeCurrSelectedRowStyle(rowIndex);
		// 改变当前点击的cell的外观
		this.changeSelectedCellStyle(rowIndex);
		// 记录当前选中行
		this.selectedRowIndice.push(rowIndex);
	}
	// 调用用户的方法
	if (this.isMultiSelWithBox == false)
		this.onRowSelected(rowIndex);

		// guoweic: 让隐藏的列显示出来，默认为第一个元素
		// this.letCellVisible(this.getCell(rowIndex, 0));
};

/**
 * @private
 */
GridComp.prototype.changeCurrSelectedRowStyle = function(rowIndex) {
	for (var i = 0, headerLength = this.basicHeaders.length; i < headerLength; i++) {
		var header = this.basicHeaders[i];
		if (header.isHidden == false) {
			var tempCell = header.dataDiv.cells[rowIndex];
			if (tempCell != null) {
				if (!tempCell.isErrorCell) {
					var isOdd = this.isOdd(rowIndex);
					if (header.isFixedHeader)
						tempCell.className = isOdd
								? "fixed_gridcell_odd cell_select"
								: "fixed_gridcell_even cell_select";
					else
						tempCell.className = isOdd
								? "gridcell_odd cell_select"
								: "gridcell_even cell_select";
				}
			}
		}
	}
};

/**
 * @private
 */
GridComp.prototype.changeSelectedCellStyle = function(rowIndex) {
	if (!this.isMultiSelWithBox && this.selectedCell != null
			&& !this.selectedCell.isErrorCell) {
		var isOdd = this.isOdd(rowIndex);
		var header = this.basicHeaders[this.selectedCell.colIndex];
		if (header == null)
			return;
		if (header.isFixedHeader)
			this.selectedCell.className = isOdd
					? "fixed_gridcell_odd cell_focus"
					: "fixed_gridcell_even cell_focus";
		else
			this.selectedCell.className = isOdd
					? "gridcell_odd cell_focus"
					: "gridcell_even cell_focus";
	}
};

/**
 * 得到给定索引数组的行
 * 
 * @return GridCompRow数组
 * @private
 */
GridComp.prototype.getRows = function(indice) {
	if (indice == null || !(indice instanceof Array))
		return null;

	var rows = new Array();
	for (var i = 0; i < indice.length; i++)
		rows.push(this.model.getRow(indice[i]));
	return rows;
};

/**
 * 得到给定索引数组的行
 * 
 * @return GridCompRow数组
 */
GridComp.prototype.getRow = function(index) {
	if (index == null)
		return null;
	var tempIndex = parseInt(index);
	return this.getRows([tempIndex])[0];
};

/**
 * model中行删除时的回调方法
 * 
 * @private
 */
GridComp.prototype.fireRowDeleted = function(indice) {
	// 如果outerDiv没有显示出来，不进行处理
	if (this.outerDiv.offsetWidth == 0) {
		this.needPaintRows = true;
		return;
	};
	var gridWidth = this.constant.outerDivWidth;
	for (var i = 0, count = indice.length; i < count; i++) {
		// 若显示控件在当前选中行则隐藏当前控件
		if (this.currActivedCell != null
				&& this.currActivedCell.id == indice[i])
			this.hiddenComp();
		// 删除整个选中行
		this.deleteRows([indice[i]]);
		var displayCont = this.calcuDisplayRowNum();
		var displayContTop = displayCont[0];
		var displayContBottom = displayCont[1];
		// 若删除行在显示区内并且现有行数大于显示区的行数则paintZone
		if (displayContBottom - displayContTop - 2 < this.getRowsNum()
				&& indice[i] > displayContTop && indice[i] < displayContBottom)
			this.paintZone();

		if (this.isVScroll() == false) {
			// 当没有竖直滚动条从有到无时要设置固定列的top,height
			var iOffWidth = this.constant.fixedHeaderDivWidth;
			var iScrollLeft = this.dataOuterDiv.scrollLeft;
			// this.fixedColumDiv.style.height =
			// this.fixedColumDiv.defaultHeight;
			// if(this.pageSize > 0)
			// this.fixedColumDiv.style.top = this.constant.headerHeight +
			// GridComp.PAGEBAR_HEIGHT;
			// else
			// this.fixedColumDiv.style.top = this.constant.headerHeight;

			var dynHeaderWidth = gridWidth - iOffWidth + iScrollLeft;
			if (dynHeaderWidth > 0)
				this.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
			this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;

			this.headerDiv.defaultWidth = gridWidth;
			if (this.firstVScroll) {
				this.setScrollLeft(iScrollLeft - GridComp.SCROLLBAE_HEIGHT);
				this.firstVScroll = false;
			}
		}
	}
};

/**
 * 删除选中行
 * 
 */
GridComp.prototype.deleteSeletedRow = function() {
	var selectedRowIndice = this.getSelectedRowIndice();
	if (selectedRowIndice != null && selectedRowIndice.length > 0) {
		// 删除model中的此行数据
		this.model.deleteRows(selectedRowIndice);
		this.selectedRowIndice = null;
	}
};

/**
 * 删除指定行
 * 
 * @param indice
 *            指定的索引值数组
 * @private
 */
GridComp.prototype.deleteRows = function(indice) {
	if (indice == null || indice.length <= 0)
		return;

	// 要删除的行数
	var deleCount = indice.length;
	// 将数组的值按升序排列
	indice.sort(ascendRule_int);
	var len = this.basicHeaders.length;

	// 如果显示数字行标,则从最后行删除deleCount个numdiv
	if (this.isShowNumCol) {
		for (var i = 0; i < deleCount; i++) {
			if (this.rowNumDiv.childNodes.length - 1 >= 0) {
				var node = this.rowNumDiv.cells[indice[i]];
				if (node != null) {
					// 从界面上删除
					this.rowNumDiv.removeChild(node);
				}
				// 从该删除行的下一行开始向下扫描,改变每一行的cell的id,并将cell上移一个单位
				var signLen = this.paintedSign.length;
				var seleCheck = null;
				for (var j = indice[0] + 1; j < signLen; j++) {
					if (this.paintedSign[j] == 1) {
						var cell = this.rowNumDiv.cells[j];
						// 改变行号
						cell.rowIndex = j;
						cell.innerHTML = "<center>" + j + "</ceter>";
						// 改变top值
						cell.style.top = (cell.offsetTop - this.rowHeight)
								+ "px";
					}
				}
				// 从数组中移除,必须在最后移除,否则数据会错位
				this.rowNumDiv.cells.splice(indice[i], 1);
			}
		}
	}

	// 删除行状态列,deleCount个numdiv
	for (var i = 0; i < deleCount; i++) {
		if (this.lineStateColumDiv.childNodes.length - 1 >= 0) {
			var node = this.lineStateColumDiv.cells[indice[i]];
			if (node != null) {
				this.lineStateColumDiv.removeChild(node);
			}
			// 从该删除行的下一行开始向下扫描,改变每一行的cell的id,并将cell上移一个单位
			var signLen = this.paintedSign.length;
			var seleCheck = null;
			for (var j = indice[0] + 1; j < signLen; j++) {
				if (this.paintedSign[j] == 1) {
					var cell = this.lineStateColumDiv.cells[j];
					// 改变top值
					cell.style.top = (cell.offsetTop - this.rowHeight) + "px";
				}
			}

			this.lineStateColumDiv.cells.splice(indice[i], 1);
		}
	}

	// guoweic: modify start 2009-10-28
	if (deleCount == 1) {
		var rowIndex = indice[0];
		// 如果要删除的行还没有画过,删除此行只需改变动态数据区,选择列区的高度
		if (this.paintedSign[rowIndex] == 0) {
			var dynHeight = this.dynamicColumDataDiv.offsetHeight
					- this.rowHeight;
			if (dynHeight > 0)
				// this.dynamicColumDataDiv.style.height = dynHeight;
				this.dynamicColumDataDiv.style.height = dynHeight + "px";

			if (this.isMultiSelWithBox) {
				selColumHeight = this.selectColumDiv.offsetHeight
						- this.rowHeight;
				if (selColumHeight > 0)
					// this.selectColumDiv.style.height = selColumHeight;
					this.selectColumDiv.style.height = selColumHeight + "px";
			}

			for (var i = 0; i < this.basicHeaders.length; i++) {
				if (this.basicHeaders[i].isHidden == false) {
					// 得到列div
					var dataDiv = this.basicHeaders[i].dataDiv;
					// 改变列高
					var dataDivHeight = dataDiv.offsetHeight;
					if (dataDivHeight == 0)
						dataDivHeight = this.getRowsNum() * this.rowHeight;
					if (dataDivHeight - this.rowHeight > 0)
						// dataDiv.style.height = dataDivHeight -
						// this.rowHeight;
						dataDiv.style.height = (dataDivHeight - this.rowHeight)
								+ "px";
				}
			}
			this.paintedSign.splice(rowIndex, 1);
			return;
		}

		// 选中的是最后一行直接删除
		if (rowIndex == this.paintedSign.length - 1) {
			// 如果有固定列删除固定列中相应行
			if (this.isMultiSelWithBox) {
				// 如果删除行在显示区域则删除
				this.selectColumDiv
						.removeChild(this.selectColumDiv.cells[rowIndex]);
				this.selectColumDiv.cells.splice(rowIndex, 1);
				// 改变多选列高度
				if (this.selectColumDiv.offsetHeight - this.rowHeight > 0)
					// this.selectColumDiv.style.height =
					// this.selectColumDiv.offsetHeight
					// - this.rowHeight;
					this.selectColumDiv.style.height = (this.selectColumDiv.offsetHeight - this.rowHeight)
							+ "px";
			}

			for (var i = 0; i < len; i++) {
				if (this.basicHeaders[i].isHidden == false) {
					// 得到列div
					var dataDiv = this.basicHeaders[i].dataDiv;
					dataDiv.removeChild(dataDiv.cells[rowIndex]);
					// 删除索引数组中的cell
					dataDiv.cells.splice(rowIndex, 1);

					// 改变列高
					var dataDivHeight = dataDiv.offsetHeight;
					if (dataDivHeight == 0)
						dataDivHeight = this.getRowsNum() * this.rowHeight;
					if (dataDivHeight - this.rowHeight > 0)
						// dataDiv.style.height = dataDivHeight -
						// this.rowHeight;
						dataDiv.style.height = (dataDivHeight - this.rowHeight)
								+ "px";
				}
			}
			// 当前行是否被画过的标志数组中移除掉此项
			this.paintedSign.splice(rowIndex, 1);
		}
		// 选中的不是最后一行
		else {
			// 如果多选,删除多选列中的checkbox
			if (this.isMultiSelWithBox) {
				this.selectColumDiv
						.removeChild(this.selectColumDiv.cells[rowIndex]);
				this.selectColumDiv.cells.splice(rowIndex, 1);
			}

			// 删除选中行中cells
			for (var i = 0; i < len; i++) {
				if (this.basicHeaders[i].isHidden == false) {
					// 得到列div
					var dataDiv = this.basicHeaders[i].dataDiv;
					dataDiv.removeChild(dataDiv.cells[rowIndex]);
					dataDiv.cells.splice(rowIndex, 1);
				}
			}

			// 当前行是否被画过的标志数组中移除掉此项
			this.paintedSign.splice(rowIndex, 1);
			// 从该删除行的下一行开始向下扫描,改变每一行的cell的id,并将cell上移一个单位
			var signLen = this.paintedSign.length;
			var cell = null;
			var seleCheck = null;
			for (var i = indice[0]; i < signLen; i++) {
				if (this.paintedSign[i] == 1) {
					if (this.isMultiSelWithBox) {
						seleCheck = this.selectColumDiv.cells[i];
						seleCheck.rowIndex = seleCheck.rowIndex - 1;
						// seleCheck.style.top = seleCheck.offsetTop -
						// this.rowHeight;
						seleCheck.style.top = (seleCheck.offsetTop - this.rowHeight)
								+ "px";
					}

					for (var j = 0; j < len; j++) {
						if (this.basicHeaders[j].isHidden == false) {
							cell = this.basicHeaders[j].dataDiv.cells[i];
							// 改变行号
							cell.rowIndex = cell.rowIndex - 1;
							// 改变top值
							// cell.style.top = cell.offsetTop - this.rowHeight;
							cell.style.top = (cell.offsetTop - this.rowHeight)
									+ "px";
						}
					}
				}
			}

			// 改变列高
			for (var i = 0; i < len; i++) {
				if (this.basicHeaders[i].isHidden == false) {
					// 得到列div
					var dataDiv = this.basicHeaders[i].dataDiv;
					// 改变列高
					var dataDivHeight = dataDiv.offsetHeight;
					if (dataDivHeight == 0)
						dataDivHeight = this.getRowsNum() * this.rowHeight;
					if (dataDivHeight - this.rowHeight > 0)
						dataDiv.style.height = (dataDivHeight - this.rowHeight)
								+ "px";
				}
			}

			if (this.isMultiSelWithBox) {
				var selectColumDivHeight = this.selectColumDiv.offsetHeight;
				if (this.selectColumDiv.offsetHeight == 0)
					selectColumDivHeight = this.getRowsNum() * this.rowHeight;
				if (selectColumDivHeight - this.rowHeight > 0)
					this.selectColumDiv.style.height = (selectColumDivHeight - this.rowHeight)
							+ "px";
			}
		}

		if (this.selectedRowIndice != null) {
			// 调整选中行的索引值
			for (var i = 0, count = this.selectedRowIndice.length; i < count; i++) {
				if (this.selectedRowIndice[i] > rowIndex)
					this.selectedRowIndice[i]--;
			}
		}
	}
	// guoweic: modify end

	// 改变动态数据区高度
	var dynHeight = this.dynamicColumDataDiv.offsetHeight - this.rowHeight;
	if (dynHeight > 0)
		this.dynamicColumDataDiv.style.height = dynHeight + "px";
	// 改变固定区fixedColumDiv的top
	var iScrollTop = this.dataOuterDiv.scrollTop;
	this.fixedColumDiv.style.height = (this.fixedColumDiv.defaultHeight + iScrollTop)
			+ "px";
	if (this.pageSize > 0 && this.isPagenationTop == true)
		this.fixedColumDiv.style.top = (this.constant.headerHeight
				+ GridComp.PAGEBAR_HEIGHT + iScrollTop * (-1))
				+ "px";
	else
		this.fixedColumDiv.style.top = (this.constant.headerHeight + iScrollTop
				* (-1))
				+ "px";

	if (this.selectedRowIndice != null && this.selectedRowIndice.length > 0) {
		if (this.selectedRowIndice[0] == rowIndex)
			this.selectedRowIndice = null;
	}
};

/**
 * 获得Header宽度
 * 
 * @private
 */
GridComp.prototype.getHeaderWidth = function() {
	if (this.headers == null)
		return 0;
	var width = 0;
	for (var i = 0; i < this.headers.length; i++) {
		if (!this.headers[i].isHidden)
			width += this.headers[i].width;
	}
	return width;
};

/**
 * 获得Header最大深度
 * 
 * @private
 */
GridComp.prototype.getHeaderDepth = function() {
	var maxDepth = 1;
	for (var i = 0; i < this.headers.length; i++) {
		var depth = this.headers[i].getDepth();
		if (maxDepth <= depth)
			maxDepth = depth;
	}
	return maxDepth;
};

/**
 * model改变后会调用此方法通知grid
 * 
 * @param rowIndex
 *            cell所在行号
 * @param colIndex
 *            cell所在列号
 * @param newValue
 *            新值
 * @param oldValue
 *            旧值
 * @private
 */
GridComp.prototype.cellValueChangedFunc = function(rowIndex, colIndex,
		newValue, oldValue) {
	if (this.paintedSign[rowIndex] == 1) {
		// 此列不隐藏
		if (this.basicHeaders[colIndex].isHidden == false) {
			var cell = this.basicHeaders[colIndex].dataDiv.cells[rowIndex];
			var header = this.basicHeaders[colIndex];
			if (cell.firstChild != null)
				cell.removeChild(cell.firstChild);
			this.basicHeaders[colIndex].renderType.render(rowIndex, colIndex,
					newValue, header, cell);

			// 如果该cell原先处于选中行要将此cell的背景重新设置为选中行样式
			var isOdd = this.isOdd(rowIndex);
			if (header.isFixedHeader) {
				if (this.selectedRowIndice != null
						&& this.selectedRowIndice.indexOf(rowIndex) != -1)
					cell.className = isOdd
							? "fixed_gridcell_odd cell_select"
							: "fixed_gridcell_even cell_select";
				else
					cell.className = isOdd
							? "fixed_gridcell_odd"
							: "fixed_gridcell_even";
			} else {
				if (this.selectedRowIndice != null
						&& this.selectedRowIndice.indexOf(rowIndex) != -1)
					cell.className = isOdd
							? "gridcell_odd cell_select"
							: "gridcell_even cell_select";
				else
					cell.className = isOdd ? "gridcell_odd" : "gridcell_even";
			}
			cell.isErrorCell = false;
			// TODO:
			if (header.editorType == EditorType.REFERENCE) {
				if (this.compsMap != null) {
					var comp = this.compsMap.get(EditorType.REFERENCE
							+ colIndex);
					if (comp != null && comp.visible) {
						if (this.currActivedCell != null
								&& this.currActivedCell == cell)
							comp.setValue(newValue);
					}
				}
			}

			// 设置行状态(行状态为更新行才设置行状态图标为更新状态,新增行cell数据的改变不变化行状态图标)
			if (this.model != null
					&& this.model.getRow(rowIndex).rowData.state == DatasetRow.STATE_UPD)
				this.lineStateColumDiv.cells[rowIndex].className = "row_state_div row_update_state";
		}
	}
	// cell值改变后通知用户
	this.onCellValueChanged(rowIndex, colIndex, oldValue, newValue);
};

/**
 * 设置要隐藏的列
 * 
 * @param columnIds
 *            要隐藏的列
 */
GridComp.prototype.setHiddenColumns = function(columnIds) {

};

/**
 * 获取grid当前的显示列
 * 
 * @return 显示列id的数组
 */
GridComp.prototype.getVisibleColumnIds = function() {
	if (!this.model)
		return null;
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return null;

	var visibleColumns = [];
	for (var i = 0, count = headers.length; i < count; i++) {
		if (headers[i].isHidden == false)
			visibleColumns.push(headers[i].keyName);
	}
	return visibleColumns;
};

/**
 * 获取grid当前的隐藏列
 * 
 * @return 隐藏列id的数组
 */
GridComp.prototype.getHiddenColumnIds = function() {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return null;

	var hiddenColumns = [];
	for (var i = 0, count = headers.length; i < count; i++) {
		if (headers[i].isHidden == true)
			hiddenColumns.push(headers[i].keyName);
		return hiddenColumns;
	}
	return hiddenColumns;
};

/**
 * 设置要显示的列
 * 
 * @param columnIds
 *            要显示的列的id数组
 */
GridComp.prototype.setShowColumns = function(columnIds) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;

	// 全部隐藏
	if (columnIds == null || columnIds.length == 0) {
		for (var i = 0, count = headers.length; i < count; i++)
			headers[i].isHidden = true;
	}
	// 按指定列隐藏
	else {
		for (var i = 0, count = headers.length; i < count; i++) {
			if (columnIds.indexOf(headers[i].keyName) != -1)
				headers[i].isHidden = false;
			else
				headers[i].isHidden = true;
		}
	}
	this.setModel(this.model);

	// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的界面显示的区域.必须设置重新画一下
	// 设置行标志为未画
	for (var i = 0, count = this.getRowsNum(); i < count; i++)
		this.paintedSign[i] = 0;
	this.paintZone();
};

/**
 * 根据ID取列
 * 
 * @param columnId
 *            要取的列ID
 */
GridComp.prototype.getBasicHeaderById = function(columnId) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0 || columnId == null)
		return;
	for (var i = 0, count = headers.length; i < count; i++) {
		if (headers[i].keyName == columnId)
			return headers[i];
	}
	return;
};

/**
 * 设置要锁定的列
 * 
 * @param columnIds
 *            要锁定的列的id数组
 */
GridComp.prototype.setFixedColumns = function(columnIds) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;

	if (columnIds == null || columnIds.length == 0) {
		for (var i = 0, count = headers.length; i < count; i++)
			headers[i].isFixedHeader = false;
	} else {
		for (var i = 0, count = headers.length; i < count; i++) {
			if (columnIds.indexOf(headers[i].keyName) != -1)
				headers[i].isFixedHeader = true;
			else
				headers[i].isFixedHeader = false;
		}
	}
	this.setModel(this.model);

	// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的绘制了界面显示的区域.必须设置重新画一下
	// 设置行标志为未画
	for (var i = 0, count = this.getRowsNum(); i < count; i++)
		this.paintedSign[i] = 0;
	this.paintZone();
};

/**
 * 设置参数指定列的显示隐藏属性
 * 
 * @param{Array} columns 列显示隐藏数组
 */
GridComp.prototype.setColumnVisible = function(columns) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;
	var hasChanged = false;
	for (var i = 0; i < headers.length; i++) {
		var header = headers[i];
		// columns: [columName:visible]
		if (columns.indexOf(header.keyName + ":" + String(header.isHidden)) != -1) {
			header.isHidden = (!header.isHidden);

			// 修改group的显示属性(如果group中的所有子都隐藏，group也设为隐藏)
			if (header.isGroupHeader == true) {
				if (header.isHidden == true) {
					var groupHeader = header.topHeader;
					if (groupHeader != null && groupHeader.isHidden == false) {
						var childrenHeaders = groupHeader.allChildrenHeader;
						var allHidden = true;
						for (var j = 0; j < childrenHeaders.length; j++) {
							if (childrenHeaders[j].isHidden == false) {
								allHidden = false;
								break;
							}
						}
						if (allHidden == true) {
							groupHeader.isHidden = true;
						}
					}
				} else {
					var groupHeader = header.topHeader;
					if (groupHeader != null && groupHeader.isHidden == true) {
						groupHeader.isHidden = false;
					}
				}
			}

			hasChanged = true;
		}
	}

	if (hasChanged) {
		this.model.rows = null;
		this.model.initUIRows();
		this.setModel(this.model);
		// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的界面显示的区域.必须设置重新画一下
		// 设置行标志为未画
//		for (var i = 0, count = this.getRowsNum(); i < count; i++)
//			this.paintedSign[i] = 0;
//		this.paintZone();
	}
};

/**
 * 设置参数指定列的editable属性
 * 
 * @param{Array} columns 列editable数组
 */
GridComp.prototype.setColumnEditable = function(columns) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;
	for (var i = 0; i < headers.length; i++) {
		var header = headers[i];
		// columns: [columName:editable]
		if (columns.indexOf(header.keyName + ":"
				+ String(!header.columEditable)) != -1) {
			header.columEditable = (!header.columEditable);
			if (header.columEditable == true && this.editable == false) {
				// 根据model编辑属性设置grid的编辑属性
				if (this.model.dataset != null && this.model.dataset.editable)
					this.setEditable(true);
			}
		}
	}
};

/**
 * 设置列头全选框隐藏/显示
 * 
 * @param keyName
 *            列值名称
 * @param visible
 *            显示隐藏属性 true/false
 */
GridComp.prototype.setHeaderCheckBoxVisible = function(keyName, visible) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;
	for (var i = 0; i < headers.length; i++) {
		if (headers[i].keyName == keyName) {
			// 判断是否存在全选框
			if (headers[i].selectBox) {
				visible = getBoolean(visible, true);
				if (visible == true)
					headers[i].selectBox.style.display = "";
				else
					headers[i].selectBox.style.display = "none";
			}
			break;
		}
	}
};

/**
 * 根据header中设置的显示状态和当前状态判断是否应该显示
 * 
 * @private
 */
GridComp.prototype.showByState = function(state) {
	var headers = this.model.basicHeaders;
	if (headers == null || headers.length == 0)
		return;
	var hasChanged = false;
	for (var i = 0, count = headers.length; i < count; i++) {
		if (headers[i].showState == null)
			continue;
		if (headers[i].showState != state) {
			if (headers[i].isHidden == false) {
				headers[i].isHidden = true;
				hasChanged = true;
			}
		} else {
			if (headers[i].isHidden == true) {
				headers[i].isHidden = false;
				hasChanged = true;
			}
		}
	}
	if (!hasChanged)
		return;
	this.setModel(this.model);
	// setModel时,往outerDiv里添加div会导致gridResize,从而导致不正确的界面显示的区域.必须设置重新画一下
	// 设置行标志为未画
	for (var i = 0, count = this.getRowsNum(); i < count; i++)
		this.paintedSign[i] = 0;
	this.paintZone();
};

/**
 * 设置此Grid的model，重画表头和所有行数据
 * 
 * @param model
 *            grid数据集
 */
GridComp.prototype.setModel = function(model) {
	// 初始化设置model
	if (this.model == null) {
		this.model = model;
		// 将grid对象绑定在model的owner属性上
		this.model.owner = this;
		// 将model的basicHeaders保存到grid的basicHeaders中
		this.basicHeaders = this.model.basicHeaders;
		this.create();
	} else {
		if (this.model != model){
			this.model.dataset.unbindComponent(this.model);
			delete this.model;
			this.model = model;
			this.model.owner = this;
		}
		this.basicHeaders = this.model.basicHeaders;
		this.paintData();
		// 将model的basicHeaders保存到grid的basicHeaders中
		// this.paintZone();
		// this.attachEvents();
	}
	if (this.pageSize > 0)
		this.setPaginationInfo();

	// 计算所有合计列的值
	if (this.isShowSumRow) {
		this.model.setSumColValue(null, null);
		// 如果此时出了滚动条,则调整合计行的top
		// if(isDivScroll(this.dataOuterDiv))
		// this.dynSumRowContentDiv.style.top =
		// this.dynSumRowContentDiv.offsetTop - (GridComp.SCROLLBAE_HEIGHT - 4)
		// - 5;
	}

	// 根据model编辑属性设置grid的编辑属性
	if (this.model.dataset != null && this.model.dataset.isEditable() == false)
		this.setEditable(false);
	else if (this.model.dataset != null && this.model.dataset.editable
			&& this.model.dataset.editableChanged)
		this.setEditable(true);
};

/**
 * model改变后调用此方法重画所有行数据
 * 
 * @param sort
 *            表示是排序时重画，不需要重新initUIRows
 * @param
 * @private
 */
GridComp.prototype.paintRows = function(sort, startIndex, count) {
	// 如果outerDiv没有显示出来，不进行paint
	if (this.outerDiv.offsetWidth == 0) {
		this.needPaintRows = true;
		return;
	};
	if (this.isMultiSelWithBox) {
		// this.removeAllChildren(this.selectColumDiv);
		if (this.selectColumDiv != null)
			this.selectColumDiv.parentNode.removeChild(this.selectColumDiv);
	}
	// 移除掉固定列数据区div和动态列数据区div
	if (this.fixedColumDataDiv != null)
		this.fixedColumDataDiv.parentNode.removeChild(this.fixedColumDataDiv);
	if (this.dynamicColumDataDiv != null)
		this.dynamicColumDataDiv.parentNode
				.removeChild(this.dynamicColumDataDiv);
	// 移除行标列
	if (this.isShowSumRow && this.rowNumDiv != null) {
		this.rowNumDiv.parentNode.removeChild(this.rowNumDiv);
		this.rowNumDiv.cells = null;
	}
	// 移除行状态列
	if (this.lineStateColumDiv != null){
		this.lineStateColumDiv.parentNode.removeChild(this.lineStateColumDiv);
		this.lineStateColumDiv.cells = null;
	}
	// 将model的rows置为null,需要重新取新数据
	if (!sort) {
		this.model.rows = null;
		if (startIndex != null && count != null)
			this.model.initUIRows(startIndex, count);
		else
			this.model.initUIRows(startIndex);
	}

	var rowCount = this.getRowsNum();
	this.paintedSign = null;
	// 记录行标志的数组.0表示未画,1表示画了行
	this.paintedSign = new Array(rowCount);
	// 设置行标志为未画
	for (var i = 0; i < rowCount; i++)
		this.paintedSign[i] = 0;

	// 当前选中行索引
	this.selectedRowIndice = null;
	this.currActivedCell = null;
	// 重画行时隐藏掉当前显示的控件
	if (this.showComp)
		this.hiddenComp();

	var gridWidth = this.constant.outerDivWidth;
	var fixedHeaderDivWidth = this.constant.fixedHeaderDivWidth;
	// 调整表头的宽度
	if (this.isVScroll()) {
		this.headerDiv.style.width = (gridWidth - GridComp.SCROLLBAE_HEIGHT)
				+ "px";// - 1;
		this.headerDiv.defaultWidth = gridWidth - GridComp.SCROLLBAE_HEIGHT;// - 1;
		this.headerDiv.style.left = "0px";
		this.fixedHeaderDiv.style.left = "0px";

		var dynHeaderWidth = gridWidth - fixedHeaderDivWidth
				- GridComp.SCROLLBAE_HEIGHT;// - 1;
		if (dynHeaderWidth > 0)
			this.dynamicHeaderDiv.style.width = dynHeaderWidth + "px";
		this.dynamicHeaderDiv.defaultWidth = dynHeaderWidth;
	} else {
		this.headerDiv.style.width = gridWidth + "px";// - 2;
		this.headerDiv.defaultWidth = gridWidth;// - 2;
		// this.headerDiv.style.left = 0;
		this.headerDiv.style.left = "0px";
		this.fixedHeaderDiv.style.left = "0px";
		this.dynamicHeaderDiv.style.width = (gridWidth - fixedHeaderDivWidth)
				+ "px";// - 3;
		this.dynamicHeaderDiv.defaultWidth = gridWidth - fixedHeaderDivWidth;// - 3;
	}
	if (this.isScroll())
		this.setScrollLeft(0);
	// guoweic: modify end

	// 画固定列数据区的所有列div
	this.initFixedColumDataDiv();
	// 画动态列数据区的数据外层包装div及每列数据div
	this.initDynamicColumDataDiv();
	// 画数字行标区
	if (this.isShowNumCol)
		this.initRowNumDiv();
	// 画行状态区
	this.initLineStateColumDiv();
	// 画固定选择列
	if (this.isMultiSelWithBox)
		this.initSelectColumDiv();
	// 调整固定列高度
	this.adjustFixedColumDivHeight();
	this.paintZone();
	
	if (this.stForAutoExpand != null)
		clearTimeout(this.stForAutoExpand);
	this.stForAutoExpand = setTimeout(
			"GridComp.processAutoExpandHeadersWidth('" + this.id + "')", 100);
	
	
};

/**
 * paint所有重新设定的model数据
 * 
 * @private
 */
GridComp.prototype.paintData = function() {
	// 清空原来数据显示页
	this.clearDivs();
	if (this.model == null)
		return;

	// 初始化构建框架所需的各个常量
	this.initConstant();
	// 根据model初始化header
	this.initBasicHeaders();
	// 判断初始化界面时是否会出现横向、纵向滚动条
	this.adjustScroll();
	// 初始化基础框架
	this.initDivs();
	// 根据model初始化数据
	this.initDatas();
	var rowCount = this.getRowsNum();
	// 记录行标志的数组.0表示未画,1表示画了行
	this.paintedSign = new Array(rowCount);
	// 设置行标志为未画
	for (var i = 0; i < rowCount; i++)
		this.paintedSign[i] = 0;
	// 真正的画界面
	this.paintZone();
	// 各区域的事件处理
	this.attachEvents();
	// 当前选中行索引
	this.selectedRowIndice = null;
	this.currActivedCell = null;
	if (this.showComp)
		this.hiddenComp();
};

/**
 * 清除grid所有框架div
 * 
 * @private
 */
GridComp.prototype.clearDivs = function() {
	// 移除所有div 2008-04-07 gd
	this.removeAllChildren(this.outerDiv);
	if (this.dataOuterDiv != null) {
		this.dataOuterDiv.style.width = "0px";
	}
};

/**
 * 删除参数节点下所有子节点
 * 
 * @private
 */
GridComp.prototype.removeAllChildren = function(p) {
	if (p) {
		while (p.childNodes.length > 0)
			p.removeChild(p.childNodes[0]);
	}
};

/**
 * grid滚动处理函数.设置30毫秒延迟
 * 
 * @private
 */
function handleScrollEvent(e) {
	handleScrollEvent.triggerObj = e.triggerObj;
	if (handleScrollEvent.timer != null)
		clearTimeout(handleScrollEvent.timer);
	handleScrollEvent.timer = setTimeout("doScroll()", 30);
};

/**
 * @private
 */
function doScroll() {
	handleScrollEvent.triggerObj.paintZone();
};

/*******************************************************************************
 * 
 * 用户可以重载的方法如下
 * 
 ******************************************************************************/
/**
 * cell单击时用户重载方法
 * 
 * @param cell
 * @param rowIndex
 *            行id
 * @param colIndex
 *            列id
 * @private
 */
GridComp.prototype.onCellClick = function(cell, rowIndex, colIndex) {
	var cellEvent = {
		"obj" : this,
		"cell" : cell,
		"rowIndex" : rowIndex,
		"colIndex" : colIndex
	};
	this.doEventFunc("onCellClick", GridCellListener.listenerType, cellEvent);
};

/**
 * cell编辑时调用该方法,用户可以重载该方法返回自定义的cell编辑器
 * 
 * @private
 */
GridComp.prototype.getCellEditor = function(cell, rowIndex, colIndex) {
	var cellEvent = {
		"obj" : this,
		"cell" : cell,
		"rowIndex" : rowIndex,
		"colIndex" : colIndex
	};
	this.doEventFunc("cellEdit", GridCellListener.listenerType, cellEvent);
};

/**
 * cell编辑完成后调用此方法
 * 
 * @param rowIndex
 *            行号
 * @param colIndex
 *            列号
 * @param oldValue
 *            旧值
 * @param newValue
 *            新值
 * @private
 */
GridComp.prototype.onAfterEdit = function(rowIndex, colIndex, oldValue,
		newValue) {
	var afterCellEditEvent = {
		"obj" : this,
		"rowIndex" : rowIndex,
		"colIndex" : colIndex,
		"oldValue" : oldValue,
		"newValue" : newValue
	};
	this.doEventFunc("afterEdit", GridCellListener.listenerType,
			afterCellEditEvent);
};

/**
 * cell编辑前调用的方法,返回false可以阻止编辑cell
 * 
 * @param rowIndex
 *            行索引
 * @param colIndex
 *            列索引
 * @private
 */
GridComp.prototype.onBeforeEdit = function(rowIndex, colIndex) {
	var beforeCellEditEvent = {
		"obj" : this,
		"rowIndex" : rowIndex,
		"colIndex" : colIndex
	};
	var result = this.doEventFunc("beforeEdit", GridCellListener.listenerType,
			beforeCellEditEvent);
	if (result != null)
		return result;
};

/**
 * cell值改变后调用此方法
 * 
 * @param rowIndex
 *            行号
 * @param colIndex
 *            列号
 * @param oldValue
 *            旧值
 * @param newValue
 *            新值
 * @private
 */
GridComp.prototype.onCellValueChanged = function(rowIndex, colIndex, oldValue,
		newValue) {
	var cellValueChangedEvent = {
		"obj" : this,
		"rowIndex" : rowIndex,
		"colIndex" : colIndex,
		"oldValue" : oldValue,
		"newValue" : newValue
	};
	this.doEventFunc("cellValueChanged", GridCellListener.listenerType,
			cellValueChangedEvent);
};

/**
 * 行选中之前调用的方法
 * 
 * @param rowIndex
 *            选中行的索引
 * @param{GridCompRow} row
 * @private
 */
GridComp.prototype.onBeforeRowSelected = function(rowIndex, row) {
	var rowEvent = {
		"obj" : this,
		"rowIndex" : rowIndex,
		"row" : row
	};
	return this.doEventFunc("beforeRowSelected", GridRowListener.listenerType,
			rowEvent);
};

/**
 * 行双击时调用的方法(只有在grid整体不能编辑时该方法才有用)
 * 
 * @param rowIndex
 *            选中行的索引
 * @param{GridCompRow} row
 * @private
 */
GridComp.prototype.onRowDblClick = function(rowIndex, row) {
	var rowEvent = {
		"obj" : this,
		"rowIndex" : rowIndex,
		"row" : row
	};
	this.doEventFunc("onRowDbClick", GridRowListener.listenerType, rowEvent);
};

/**
 * 行选中时调用的方法
 * 
 * @param rowIndex
 *            选中行的索引
 * @private
 */
GridComp.prototype.onRowSelected = function(rowIndex) {
	var rowSelectedEvent = {
		"obj" : this,
		"rowIndex" : rowIndex
	};
	this.doEventFunc("onRowSelected", GridRowListener.listenerType,
			rowSelectedEvent);
};

/**
 * 数据区上点击鼠标右键时调用这个方法
 * 
 * @private
 */
GridComp.prototype.onDataOuterDivContextMenu = function(e) {
	var mouseEvent = {
		"obj" : this,
		"event" : e
	};
	return this.doEventFunc("onDataOuterDivContextMenu",
			GridListener.listenerType, mouseEvent);
};

/**
 * @private
 */
GridComp.prototype.processPageCount = function(pageInfo) {
	var processPageCountEvent = {
		"obj" : this,
		"pageInfo" : pageInfo
	};
	this.doEventFunc("processPageCount", GridListener.listenerType,
			processPageCountEvent);
	return pageInfo;
};

/*******************************************************************************
 * 
 * 创建表头及表头操作方法
 * 
 ******************************************************************************/
/**
 * 表头类构造函数
 * 
 * @class 表头类，用于构件grid的表头。
 * @constructor 表头类构造函数
 * @param keyName
 *            表头的真实值
 * @param showName
 *            表头的显示值
 * @param width
 *            表头宽度
 * @param dataType
 *            此列的数据类型
 * @param sortable
 *            此列是否可排序
 * @param isHidden
 *            初始化时是否隐藏
 * @param columEditable
 *            此列是否可以编辑
 * @param columBgColor
 *            此列的背景色
 * @param textAlign
 *            此列的文字居中方式
 * @param textColor
 *            此列的文字颜色
 * @param isFixedHeader
 *            此列是否是固定列
 * @param renderType
 *            渲染器类型
 * @param editorType
 *            编辑器类型
 * @param topHeader
 *            isGroupHeader为true时有用,若此header是多表头中的一个header,此参数表示所属多表头的最顶层header(null说明此header是最顶层header,否则必须传入此header的顶层header)
 * @param groupHeader
 *            isGroupHeader为true时有用,如果此header是属于一个特定groupHeader组,则此参数表示此header所属于的groupHeader
 * @param isGroupHeader
 *            true表示此header是多表头中的header
 * @param isSumCol
 *            是否是合计列
 * @param isAutoExpand
 *            自动扩展列,设置为自动扩展列的header会自动占满表格的剩余空间
 */
function GridCompHeader(keyName, showName, width, dataType, sortable, isHidden,
		columEditable, defaultValue, columBgColor, textAlign, textColor,
		isFixedHeader, renderType, editorType, topHeader, groupHeader,
		isGroupHeader, isSumCol, isAutoExpand, isShowCheckBox) {
	this.width = getInteger(width, GridComp.COlUMWIDTH_DEFAULT);
	if (this.width < GridComp.COlUMWIDTH_DEFAULT)
		this.width = 50;
	// var showNameWidth = getTextWidth(showName);
	// if(showNameWidth > this.width)
	// this.width = showNameWidth;

	this.children = null;
	this.keyName = keyName;
	this.showName = getString(showName, "");
	// 此列是否默认隐藏
	this.isHidden = getBoolean(isHidden, false);
	this.parent = null;
	// 初始是否是固定表头(默认为动态表头)
	this.isFixedHeader = getBoolean(isFixedHeader, false);
	// 此列的数据类型
	this.dataType = getString(dataType, DataType.STRING);
	// 此列是否可排序
	this.sortable = getBoolean(sortable, true);
	// cell中元素的对齐方式
	this.textAlign = getString(textAlign, "left");
	this.columBgColor = getString(columBgColor, "");
	this.textColor = getString(textColor, "black");
	// 此列的默认值
	this.defaultValue = getString(defaultValue, "");
	this.columEditable = getBoolean(columEditable, true);
	// 值解析器.如果没有设置,则将根据数值类型调用默认解析器.
	this.parser = null;
	// 渲染器对象的引用
	this.renderType = renderType;
	// 此列的编辑类型(即是什么控件)
	this.editorType = getString(editorType, EditorType.STRINGTEXT);
	// 表示该列是否是合计列
	this.isSumCol = getBoolean(isSumCol, false);
	// 该表头是否自动扩展
	this.isAutoExpand = getBoolean(isAutoExpand, false);
	// 是否分组显示,如果分组显示,相同的一组值将只会显示第一个值
	this.isGroupBy = false;
	// 是否显示表头checkbox
	this.isShowCheckBox = getBoolean(isShowCheckBox, true);
	
	// 标示此header是否是多表头中的header
	this.isGroupHeader = getBoolean(isGroupHeader, false);
	if (this.isGroupHeader) {
		// 多表头最顶层表头
		this.topHeader = topHeader;
		if (this.topHeader == null || this.topHeader == "")
			this.allChildrenHeader = new Array();
		else
			this.topHeader.allChildrenHeader.push(this);

		if (groupHeader != null && groupHeader != "") {
			// 如果 groupHeader隐藏，子改设置为隐藏
			if (groupHeader.isHidden == true)
				this.isHidden == true;
			groupHeader.addChildHeader(this);
		}

	}
};

/**
 * 修改表头显示文字
 */
GridCompHeader.prototype.replaceText = function(text) {
	if (this.textNode && text != null) {
		this.textNode.innerHTML = "";
		// this.textNode.appendChild(document.createTextNode(header.showName));
		this.textNode.appendChild(document.createTextNode(text));
	}
};

/**
 * 设置显示状态
 */
GridCompHeader.prototype.setShowState = function(state) {
	this.showState = state;
};

/**
 * 增加子header,多表头情况的处理
 */
GridCompHeader.prototype.addChildHeader = function(header) {
	if (this.children == null)
		this.children = new Array();
	this.children.push(header);
	// 保存此header的父header
	header.parent = this;
};

/**
 * 根据传入的最父级header,返回指定层数的所有header
 * 
 * @param level
 *            层数从0开始
 * @return header数组(包括隐藏列) 注:调用此方法的只能是最顶层header
 * @private
 */
GridCompHeader.prototype.getHeadersByLevel = function(level) {
	var headers = this.getAllChildrenHeaderByLevel(level);
	return headers;
};

/**
 * 得到多表头列的每一层的最左边的header(用于改变边界style)
 * 
 * @return headers数组
 * @private
 */
GridCompHeader.prototype.getAllLeftHeaders = function() {
	if (this.children == null)
		return;
	var depth = this.getDepth();
	var headers = new Array();
	var temp = null;
	for (var i = 0; i < depth; i++) {
		temp = this.getVisibleHeadersByLevel(i);
		if (temp != null && temp.length > 0)
			headers.push(temp[0]);
	}
	return headers;
};

/**
 * 得到指定层的可见header,不包括隐藏列
 * 
 * @param level
 *            层数从0开始
 * @return header数组 注:调用此方法的只能是最顶层header
 * @private
 */
GridCompHeader.prototype.getVisibleHeadersByLevel = function(level) {
	// 隐藏列直接返回
	if (this.isHidden)
		return null;
	var headers = this.getAllChildrenHeaderByLevel(level);
	var temp = new Array();
	for (var i = 0; i < headers.length; i++) {
		if (headers[i].isHidden == false)
			temp.push(headers[i]);
	}
	headers = null;
	return temp;
};

/**
 * 得到指定层的所有子header,不考虑子header是隐藏列的情况 注:任何header均可调用此方法
 * 
 * @private
 */
GridCompHeader.prototype.getAllChildrenHeaderByLevel = function(level) {
	var temp = new Array();
	// 顶层header调用情况
	if (this.parent == null && this.children != null) {
		// 取第0级直接返回this
		if (level == 0) {
			temp.push(this);
			return temp;
		}
		// 取其他级
		else {
			if (this.allChildrenHeader != null
					&& this.allChildrenHeader.length > 0) {
				for (var i = 0; i < this.allChildrenHeader.length; i++) {
					if (this.allChildrenHeader[i].getHeaderLevel() == level)
						return this.allChildrenHeader[i].parent.children;
				}
			}
		}
	}
	// basicHeader调用情况
	else if (this.parent != null && this.children == null) {
		temp.push(this);
		return temp;
	} else {
		// 得到此header的所在层数
		var currHeaderLevel = this.getHeaderLevel();
		var header = this;
		// 得到最顶层header
		while (header.parent != null)
			header = header.parent;
		for (var i = 0; i < header.allChildrenHeader.length; i++) {
			if (header.allChildrenHeader[i].getHeaderLevel() == currHeaderLevel
					+ 1 + level)
				return header.allChildrenHeader[i].parent.children;
		}
	}
	// 返回空数组
	return temp;
};

/**
 * 得到header深度
 * 
 * @private
 */
GridCompHeader.prototype.getDepth = function() {
	return 1 + this.getMaxDepth(this);
};

/**
 * 递归得到表头最大深度
 * 
 * @private
 */
GridCompHeader.prototype.getMaxDepth = function() {
	var maxDepth = 0;
	if (this.children != null) {
		var childs = this.children;
		for (var i = 0; i < childs.length; i++) {
			if (!childs[i].isHidden) {
				var depth = 1 + childs[i].getMaxDepth();
				if (depth > maxDepth)
					maxDepth = depth;
			}
		}
	}
	return maxDepth;
};

/**
 * 得到header所在的层值 注:层数从0开始
 * 
 * @private
 */
GridCompHeader.prototype.getHeaderLevel = function() {
	var level = 0;
	if (this.parent != null)
		level = 1 + this.parent.getHeaderLevel();
	return level == 0 ? 0 : level;
};

/**
 * 递归得到给定header的colspan值
 * 
 * @private
 */
GridCompHeader.prototype.getColspan = function() {
	var w = 0;
	if (this.children != null) {
		for (var i = 0; i < this.children.length; i++) {
			if (!this.children[i].isHidden) {
				var ret = this.children[i].getColspan();
				w += ret;
			}
		}
	}
	return w == 0 ? 1 : w;
};

/**
 * 得到header列最底层的全部basicHeaders(顺序为从左到右)
 * 
 * @return basicHeaders数组 注:调用此方法的只能是顶层header
 * @private
 */
GridCompHeader.prototype.getBasicHeaders = function() {
	var basicHeaders = new Array();
	if (this.children == null) {
		basicHeaders.push(this);
	} else {

		// // header的孩子层数
		// var level = this.getHeaderChildrenLevel();
		// // 得到从左到右的按顺序的basicHeaders
		// for ( var i = level; i >= 0; i--) {
		// var headers = this.getHeadersByLevel(i);
		// for ( var j = 0; j < headers.length; j++) {
		// if (headers[j].children == null)
		// basicHeaders.push(headers[j]);
		// }
		// }
		this.getBasicHeader(this, basicHeaders);

	}
	return basicHeaders;
};

/**
 * 递归取header的basicheaders
 * 
 * @param {GridCompHeader}
 *            header
 * @param {array}
 *            basicHeaders
 */
GridCompHeader.prototype.getBasicHeader = function(header, basicHeaders) {
	for (var i = 0; i < header.children.length; i++) {
		var childrenHeader = header.children[i];
		if (childrenHeader.children == null)
			basicHeaders.push(childrenHeader);
		else
			this.getBasicHeader(childrenHeader, basicHeaders);
	}
};

/**
 * 得到任意给定header的所有下挂的basicHeaders
 * 
 * @private
 */
GridCompHeader.prototype.getBasicHeadersBySpecify = function() {
	var headers = new Array();
	if (this.children == null) {
		headers.push(this);
		return headers;
	} else {
		if (this.parent == null)
			return this.getBasicHeaders();
		else {
			// 得到此header的孩子层数
			var childLevel = this.getHeaderChildrenLevel();
			for (var i = 0; i <= childLevel; i++) {
				var currLevelHeaders = this.getAllChildrenHeaderByLevel(i);
				for (var j = 0; j < currLevelHeaders.length; j++) {
					if (currLevelHeaders[j].children == null)
						headers.push(currLevelHeaders[j]);
				}
			}
			return headers;
		}
	}
};

/**
 * 此header列最底层有多少个子header 根据传入的最父级header
 * 
 * @private
 */
GridCompHeader.prototype.getDepthestHeadersNum = function() {
	if (this.parent != null)
		return null;
	// 最顶层header的colspan的值就是最底层的header数
	return this.getColspan();
};

/**
 * 得到header的rowspan值
 * 
 * @totalDepth 此header所在table的总深度
 * @private
 */
GridCompHeader.prototype.getRowspan = function(totalDepth) {
	var childLevel = 0;
	if (this.children != null)
		childLevel = this.getHeaderChildrenLevel();
	var rowspan = totalDepth - this.getHeaderLevel() - childLevel;
	return rowspan;
};

/**
 * 对于多表头列求得给定一个header的孩子层数
 * 
 * @private
 */
GridCompHeader.prototype.getHeaderChildrenLevel = function() {
	return this.getMaxDepth();
};

/**
 * 如果header是符点数，则调用此方法设置最小值
 */
GridCompHeader.prototype.setFloatMinValue = function(minValue) {
	if (!isNaN(parseFloat(minValue))) {
		this.floatMinValue = parseFloat(minValue);
	} else
		this.floatMinValue = null;
};

/**
 * 如果header是浮点数，则调用此方法设置最大值
 */
GridCompHeader.prototype.setFloatMaxValue = function(maxValue) {
	if (!isNaN(parseFloat(maxValue))) {
		this.floatMaxValue = parseFloat(maxValue);
	} else
		this.floatMaxValue = null;
};

/**
 * 如果header类型是inttext，则调用此方法设置最小值
 * 
 * @param minValue
 *            最小值，不能小于javascript允许的整数最小值，小于则采用默认最小值
 */
GridCompHeader.prototype.setIntegerMinValue = function(minValue) {
	if (minValue != null) {
		// 判断minValue是否是数字
		if (isNumber(minValue)) {
			if ((parseInt(minValue) >= -9007199254740992)
					&& (parseInt(minValue) <= 9007199254740992))
				this.integerMinValue = minValue;
			else
				this.integerMinValue = "";
		} else
			this.integerMinValue = -9007199254740992;
	}
};

/**
 * 如果header类型是inttext，则调用此方法设置最大值
 * 
 * @param maxValue
 *            最大值，不能大于javascript允许的整数最大值，大于则采用默认最大值
 */
GridCompHeader.prototype.setIntegerMaxValue = function(maxValue) {
	if (maxValue != null) {
		// 判断maxValue是否是数字
		if (isNumber(maxValue)) {
			if (parseInt(maxValue) >= -9007199254740992
					&& parseInt(maxValue) <= 9007199254740992)
				this.integerMaxValue = maxValue;
			else
				this.integerMaxValue = "";
		} else
			this.integerMaxValue = 9007199254740992;
	}
};

/**
 * 如果header类型是numtext，则调用此方法设置精度
 * 
 * @param precision
 *            int型，小数点后几位
 */
GridCompHeader.prototype.setPrecision = function(precision,fromDs) {
	fromDs = (fromDs == null) ? false : fromDs;
	if (fromDs == true){
		this.precisionFromDs = true;
	}
	//以ds设置的精度为准
	if (this.precisionFromDs != null && this.precisionFromDs == true && fromDs == false)
		return;
	if (this.precision == null || this.precision != precision) {
		this.precision = precision;
		if (this.dataDiv && this.dataDiv.cells) {
			for (var i = 0; i < this.dataDiv.cells.length; i++) {
				var cell = this.dataDiv.cells[i];
				var value = this.owner.model.getCellValueByIndex(i,cell.colIndex);
				if (cell.firstChild != null)
					cell.removeChild(cell.firstChild);
				this.renderType.render(i, this.dataDiv.cells[i].colIndex, value, this, cell);
			}
		}
	}
};

/**
 * 如果header类型是stringtext，则调用此方法设置最大长度
 * 
 * @param{int} maxLength 最大字符允许长度
 */
GridCompHeader.prototype.setMaxLength = function(maxLength) {
	this.maxLength = parseInt(maxLength);
};

/**
 * 设置此列是否必须填写，设置为true会改变表头的颜色
 * 
 * @param isRequired
 *            此列是否为必输项
 */
GridCompHeader.prototype.setRequired = function(isRequired) {
	this.required = getBoolean(isRequired, false);
};

/**
 * 参照类型设置nodeinfo
 */
GridCompHeader.prototype.setNodeInfo = function(nodeInfo) {
	this.nodeInfo = nodeInfo;
};

/**
 * 设置checkbox的真实值和显示值
 */
GridCompHeader.prototype.setValuePair = function(valuePair) {
	this.valuePair = valuePair;
};

/**
 * 如果header类型是combox，则调用此方法把showImgOnly的值传入，来标明此列 的combox是什么类型的combox
 * 
 * @param
 */
GridCompHeader.prototype.setShowImgOnly = function(showImgOnly) {
	this.showImgOnly = showImgOnly;
};

/**
 * 如果header类型是combox，则调用此方法设置combox的ComboData
 */
GridCompHeader.prototype.setHeaderComboBoxComboData = function(comboData) {
	this.comboData = comboData;
};

/*
 * 如果header类型是combox，则调用此方法把combox的所有options键值传入
 * 
 * @param keys 键值一维数组 GridCompHeader.prototype.setHeaderComboBoxKeyValues =
 * function(keys) { this.keyValues = keys; };
 */

/*
 * 如果header类型是combox，则调用此方法把combox的和每个option键值对应的显示值传入
 * 
 * @param captions 显示值一维数组
 * GridCompHeader.prototype.setHeaderComboBoxCaptionValues = function(captions) {
 * this.captionValues = captions; };
 */

/*
 * 如果header类型是combox，则调用此方法得到combox的所有options键值
 * 
 * @return keys 键值一维数组 GridCompHeader.prototype.getHeaderComboBoxKeyValues =
 * function() { return this.keyValues; };
 */

/*
 * 如果header类型是combox，则调用此方法得到和combox的每个option键值对应的显示值
 * 
 * @return captions 显示值一维数组
 * GridCompHeader.prototype.getHeaderComboBoxCaptionValues = function() { return
 * this.captionValues; };
 */

/**
 * Grid模型（数组类型）
 * 
 * @class GridCompModel定义 GridCompModel是grid的数据模型，此模型独立于gird。
 *        model适配dataset，无需了解dataset的实现细节，对dataset是
 *        透明化的，所有的数据插入，更新,修改均是对GridCompModel进行操作。
 * @author gd
 * @version NC5.5
 * @since NC5.02
 */
GridCompModel = Array;

/**
 * 设置headers
 */
GridCompModel.prototype.setHeaders = function(headers) {
	this.headers = headers;
};

/**
 * 得到model中的headers数据
 */
GridCompModel.prototype.getHeaders = function() {
	return this.headers;
};

GridCompModel.prototype.removeHeader = function(keyName) {
	var temp = new Array();
	var header = null;
	var headers = this.headers;
	for(var i=0;i<headers.length;i++){
		if(headers[i].keyName != keyName){
			temp.push(headers[i]);
		}else{
			header = headers[i]
		}
	}	
	this.headers = temp;
	return header;
};

/**
 * 得到指定数据行
 */
GridCompModel.prototype.getRow = function(index) {
	var tempIndex = parseInt(index);
	if (tempIndex < 0 || tempIndex >= this.dataset.getRowCount())
		return null;
	if (this.rows == null || this.rows.length == 0)
		return null;
	return this.rows[index];
};

/**
 * 得到指定数据行
 */
GridCompModel.prototype.getRowIndexById = function(id) {
	for (var i = 0, n = this.rows.length; i < n; i++) {
		if (this.rows[i].rowData.rowId == id)
			return i;
	}
	return -1;
};

/**
 * 得到grid指定行列的cell值
 * 
 * @param rowIndex
 *            行索引
 * @param colIndex
 *            列索引
 */
GridCompModel.prototype.getCellValueByIndex = function(rowIndex, colIndex) {
	var tempRowIndex = parseInt(rowIndex);
	var tempColIndex = parseInt(colIndex);
	if (tempRowIndex < 0 || tempRowIndex >= this.dataset.getRowCount())
		return null;
	if (colIndex > this.basicHeaders.length - 1)
		return null;
	return this.rows[rowIndex].getCellValue(colIndex);
};

/**
 * 得到所有的数据行
 * 
 * @return{GridCompRow[]} GridCompRow的数组,若没有任何数据返回null
 */
GridCompModel.prototype.getRows = function() {
	if (this.rows == null || this.rows.length == 0)
		return null;
	return this.rows;
};

/**
 * 对model中的gridrow进行排序，如果没有传入sortFuncs数组，则按照每列的数据类型调用默认的排序函数
 * 如果没有传入sortDirecs数组则默认按照升序排序
 * 
 * @param sortHeaders{Array}
 *            要排序的表头
 * @param sortFuncs{Array}
 *            每一列排序的排序算法(指定的排序算法都按升序)
 * @param ascending{Array}
 *            每一列排序的方向(升序-1, 降序1)
 */
GridCompModel.prototype.sortable = function(sortHeaders, sortFuncs, ascendings) {
	// 没有数据不进行排序
	if (this.rows == null || this.rows.length == 0)
		return;

	if (sortHeaders == null || sortHeaders.length == 0)
		return;
	if (sortFuncs != null && sortHeaders.length != sortFunsc.length)
		return;

	// 设置默认排序函数
	if (sortFuncs == null || ascendings.length == 0) {
		sortFuncs = new Array(sortHeaders.length);
		var dataType = null;
		for (var i = 0, count = sortHeaders.length; i < count; i++) {
			dataType = sortHeaders[i].dataType;
			if (dataType == DataType.INTEGER || dataType == DataType.INT)
				sortFuncs[i] = sortRowsByIntergerColum;
			else if (dataType == DataType.dOUBLE
					|| dataType == DataType.UFDOUBLE
					|| dataType == DataType.DOUBLE)
				sortFuncs[i] = sortRowsByDecimalColum;
			else
				sortFuncs[i] = defaultSortRows;
		}
	}

	// 设置默认排序方向(默认升序)
	if (ascendings == null || ascendings.length == 0) {
		ascendings = new Array(sortHeaders.length);
		for (var i = 0, count = ascendings.length; i < count; i++)
			ascendings[i] = -1;
	}

	if (sortHeaders.length == 1) {
		// 存在sortHeader,说明已经排序过,则此时只需要reverse即可
		if (this.owner.sortHeader == sortHeaders[0]) {
			this.rows.reverse();
		} else {
			sortFuncs[0].index = this.basicHeaders.indexOf(sortHeaders[0]);
			sortFuncs[0].ascending = ascendings[0];
			this.rows.sort(sortFuncs[0]);
		}
	} else {
		// 按照指定的多列排序
		for (var i = 0, count = sortHeaders.length; i < count; i++) {
			sortFuncs[i].index = this.basicHeaders.indexOf(sortHeaders[i]);
			sortFuncs[i].ascending = ascendings[i];
			this.rows.sort(sortFuncs[i]);
		}
	}

	this.owner.paintRows(true);
};

/**
 * 默认排序
 * 
 * @private
 */
function defaultSortRows(row1, row2) {
	var index = defaultSortRows.index;
	var ascending = defaultSortRows.ascending;
	if (row1.getCellValue(index) < row2.getCellValue(index))
		return (ascending == -1) ? -1 : 1;
	else if (row1.getCellValue(index) > row2.getCellValue(index))
		return (ascending == -1) ? 1 : -1;
	else
		return 0;
};

/**
 * 按整数列排序行(升序)
 * 
 * @private
 */
function sortRowsByIntergerColum(row1, row2) {
	var index = sortRowsByIntergerColum.index;
	var ascending = sortRowsByIntergerColum.ascending;
	if (parseInt(row1.getCellValue(index)) < parseInt(row2.getCellValue(index)))
		return ascending == -1 ? -1 : 1;
	else if (parseInt(row1.getCellValue(index)) > parseInt(row2
			.getCellValue(index)))
		return ascending == -1 ? 1 : -1;
	else
		return 0;
};

/**
 * 按浮点数列排序行(升序)
 * 
 * @private
 */
function sortRowsByDecimalColum(row1, row2) {
	var index = sortRowsByDecimalColum.index;
	var ascending = sortRowsByDecimalColum.ascending;
	if (parseFloat(row1.getCellValue(index)) < parseFloat(row2
			.getCellValue(index)))
		return (ascending == -1) ? -1 : 1;
	else if (parseFloat(row1.getCellValue(index)) > parseFloat(row2
			.getCellValue(index)))
		return (ascending == -1) ? 1 : -1;
	else
		return 0;
};

/**
 * 该参数是为了前后台分页准备的，initUIRows时需要去获取ds相应的行初始化grid rows
 * 
 * @private
 */
GridCompModel.prototype.initUIRows = function(startIndex, count) {
	// 保存UI rows的数组
	if (this.rows == null) {
		var dsRows = null;
		// startIndex, count同时不为空,说明是前台分页
		if (startIndex != null && count != null)
			dsRows = this.dataset.getRowsByScale(startIndex, count);
		// 否则只有startIndex,说明是后台分页,获取相应页码的数据集
		else {
			dsRows = this.dataset.getRows();
		}
		if (dsRows != null) {
			this.rows = new Array(dsRows.length);
			// 将dataset的rows放入grid的UI rows中
			var bindRelation = this.bindRelation;
			for (var i = 0, count = dsRows.length; i < count; i++) {
				// 对dataset中的行进行适配包装(内部使用尽量采用属性设置,避免方法调用)
				var row = new GridCompRow();
				row.rowData = dsRows[i];
				row.relation = bindRelation;
				this.rows[i] = row;
			}
		} else
			this.rows = new Array;
	}
};

/**
 * 得到dataset row在grid上的rowIndex
 * 
 * @param dsRow
 *            dateset行数据
 * @return 没有找到返回-1
 */
GridCompModel.prototype.getUIRowIndex = function(dsRow) {
	var rows = this.rows;
	for (var i = 0, count = rows.length; i < count; i++) {
		if (rows[i].rowData.rowId == dsRow.rowId)
			return i;
	}
	return -1;
};

/**
 * 设置row、col所对应元素的值
 * 
 * @param rowIndex
 *            行索引
 * @param colIndex
 *            列索引
 * @param value
 *            新值
 */
GridCompModel.prototype.setValueAt = function(rowIndex, colIndex, newValue) {
	if (this.dataset == null)
		showErrorDialog("dataset is null!");
	else {
		var dsRow = this.rows[rowIndex].rowData;
		var oldValue = dsRow.getCellValue(this.bindRelation[colIndex]);
		if (oldValue != newValue)
			this.dataset.setValueAt(this.dataset.getRowIndex(dsRow),
					this.bindRelation[colIndex], newValue);

		/*
		 * var oldValue = this.dataset.getValueAt(rowIndex,
		 * this.bindRelation[colIndex]); // 由绑定关系得到应设置的dataset中的列索引 if(oldValue !=
		 * newValue) this.dataset.setValueAt(rowIndex,
		 * this.bindRelation[colIndex], newValue);
		 */
	}
};

/**
 * 设置row、col所对应元素的值,不派发事件
 * 
 * @param rowIndex
 *            行索引
 * @param colIndex
 *            列索引
 * @param value
 *            新值
 */
GridCompModel.prototype.setCellValueAt = function(rowIndex, colIndex, newValue,
		oldValue) {
	if (this.dataset == null)
		showErrorDialog("dataset is null!");
	else {
		var dsRow = this.rows[rowIndex].rowData;
		var index = this.dataset.getRowIndex(dsRow);
		var row = this.dataset.getRow(index);
		row.setCellValue(this.bindRelation[colIndex], newValue);
		this.owner.cellValueChangedFunc(rowIndex, colIndex, newValue, oldValue);
	}
};

/**
 * Model发生变化时的回调函数
 * 
 * @private
 */
GridCompModel.prototype.onModelChanged = function(event) {
	var g = this.owner;
	if (!g)
		return;
	// 行选中时
	if (RowSelectEvent.prototype.isPrototypeOf(event)) {
		if (event.currentRow == null)
			return;
		var index = this.getUIRowIndex(event.currentRow);
		if (index == -1) {
			// 如果发现该dsRow不属于该grid,并且该grid的选中数组中有记录,则清除该记录. gd 2008-11-20
			// 只处理单选的情况,对于多选暂不考虑
			if (g.selectedRowIndice != null && g.selectedRowIndice.length == 1)
				g.clearAllUISelRows();
			return;
		}

		// 如果该行未画则直接返回
		if (g.paintedSign[index] == 0 || g.paintedSign[index] == null)
			return;

		// 单选的情况
		if (g.isMultiSelWithBox == false) {
			// 如果处于多选状态
			if (event.isAdd) {
				// 追加行选中
				g.rowSelected(index, true);
			} else {
				// 如果选中行不唯一则清除所有选中行(清除外观,从选中行数组中去除)
				if (g.selectedRowIndice != null
						&& g.selectedRowIndice.length > 0)
					g.clearAllUISelRows();

				g.rowSelected(index);

				// 如果已经选中的cell和当前选中行不在同一行,则把已经选中的cell的边框去掉
				if (g.oldCell != null && g.oldCell.rowIndex != index) {

					g.oldCell.className = g.oldClassName;
					// TODO:设定className并不能让cell的背景色改变,需要主动改变
					// if(g.oldCell.style.backgroundColor !=
					// GridComp.needCheckField)
					// g.oldCell.style.backgroundColor = GridComp.defaultBg;
				}
			}
			// 单选模式下，如果当前聚焦行不是选中行，则设置当前聚焦行为该选中行
			if (g.getFocusIndex() != index)
				g.setFocusIndex(index);
		}
		// 多选模式为checkbox的情况
		else if (g.isMultiSelWithBox) {
			if (event.isMultiSelect) {
				// 得到所有选中行
				var selRows = this.dataset.getSelectedRows();
				if (selRows != null && selRows.length > 0) {
					for (var i = 0, count = selRows.length; i < count; i++) {
						var rowIndex = this.getUIRowIndex(selRows[i]);
						if (rowIndex != -1) {
							var cell = g.selectColumDiv.cells[rowIndex];
							if (cell != null && cell.firstChild != null
									&& cell.firstChild.checked != true) {
								cell.firstChild.checked = true;
								g.setCheckBoxChecked(true, rowIndex);
							}
						}
					}
				}
			} else {
				var cell = g.selectColumDiv.cells[index];
				if (cell != null && cell.firstChild != null
						&& cell.firstChild.checked != true) {
					cell.firstChild.checked = true;
					g.setCheckBoxChecked(true, index);
				}
			}

			g.rowSelected(index);
			if (g.oldCell != null && g.oldCell.rowIndex != index)
				g.oldCell.className = g.oldClassName;
		}

	}
	// 行选中撤销事件
	else if (RowUnSelectEvent.prototype.isPrototypeOf(event)) {
		var index = this.getUIRowIndex(this.dataset
				.getRow(event.currentRowIndex));
		if (index == -1)
			return;

		// 如果该行未画则直接返回
		if (g.paintedSign[index] == 0 || g.paintedSign[index] == null)
			return;

		// ctrl模式多选
		if (g.isMultiSelWithBox) {
			var cell = this.owner.selectColumDiv.cells[index];
			if (cell != null && cell.firstChild != null
					&& cell.firstChild.checked != false) {
				cell.firstChild.checked = false;
				g.setCheckBoxChecked(false, index);
			}
			// 取消全选按钮的选中勾
			g.selectAllBox.checked = false;
		}
		var headerLength = g.basicHeaders.length, selIndice = g.selectedRowIndice;
		if (selIndice != null && selIndice.length > 0) {
			var header = null, cell = null;
			for (var i = 0; i < selIndice.length; i++) {
				if (index == selIndice[i]) {
					for (var j = 0; j < headerLength; j++) {
						header = this.basicHeaders[j];
						if (header.isHidden == false) {
							cell = header.dataDiv.cells[index];
							if (cell != null) {
								var isOdd = g.isOdd(index);
								if (header.isFixedHeader) {
									if (cell.isErrorCell)
										cell.className = isOdd
												? "fixed_gridcell_odd cell_error"
												: "fixed_gridcell_even cell_error";
									else
										cell.className = isOdd
												? "fixed_gridcell_odd"
												: "fixed_gridcell_even";
								} else {
									if (cell.isErrorCell)
										cell.className = isOdd
												? "gridcell_odd cell_error"
												: "gridcell_even cell_error";
									else
										cell.className = isOdd
												? "gridcell_odd"
												: "gridcell_even";
								}
							}
						}
					}

					// 设置此行状态div的背景
					var node = g.lineStateColumDiv.cells[selIndice[i]];
					if (node.className != "row_state_div row_update_state"
							&& node.className != "row_state_div row_add_state") {
						node.className = "row_state_div";
					}
					// 从选择数组中将此选择行删除
					g.selectedRowIndice.splice(i, 1);
					// 没有选中行将选中数组置空
					if (g.selectedRowIndice.length == 0)
						g.selectedRowIndice = null;
					break;
				}
			}
		}

		// 单选的情况，清除当前聚焦行
		if (g.isMultiSelWithBox == false) {
			g.setFocusIndex(-1);
		}
	}
	// cell数据改变时
	else if (DataChangeEvent.prototype.isPrototypeOf(event)) {
		var rowIndex = this.getUIRowIndex(event.currentRow);
		if (rowIndex == -1)
			return;
		// 由绑定关系得到dataset中的列索引对应的grid中的列索引
		var colIndex = this.bindRelation.indexOf(event.cellColIndex);
		if (colIndex != -1) {
			g.cellValueChangedFunc(rowIndex, colIndex, event.currentValue,
					event.oldValue);
			// 如果改变的是合计列,则计算合计列的值
			if (g.isShowSumRow && this.basicHeaders[colIndex].isSumCol) {
				this.setSumColValue(colIndex,
						this.basicHeaders[colIndex].keyName);
			}
		}
	}
	// 某列的多个cell数据改变时
	else if (DataColSingleChangeEvent.prototype.isPrototypeOf(event)) {
		for (var i = 0; i < event.cellRowIndices.length; i++) {
			var rowIndex = this.getUIRowIndex(event.currentRows[i]);
			if (rowIndex == -1)
				continue;
			var currentValue = event.currentValues[i];
			var oldValue = event.oldValues[i];
			// 由绑定关系得到dataset中的列索引对应的grid中的列索引
			var colIndex = this.bindRelation.indexOf(event.cellColIndex);
			if (colIndex != -1) {
				g.cellValueChangedFunc(rowIndex, colIndex, currentValue,
						oldValue);
				// 如果改变的是合计列,则计算合计列的值
				if (g.isShowSumRow && this.basicHeaders[colIndex].isSumCol) {
					this.setSumColValue(colIndex,
							this.basicHeaders[colIndex].keyName);
				}
			}
		}
	}
	// 违反校验规则的事件
	else if (DataCheckEvent.prototype.isPrototypeOf(event)) {
		var row = event.currentRow;
		var colIndice = event.cellColIndices;
		var rowIndex = this.getUIRowIndex(row);
		if (rowIndex == -1)
			return;
		// grid中需要纠正输入的列
		var colIndices = [];
		// 由绑定关系得到dataset中的列索引对应的grid中的列索引
		for (var i = 0, count = event.cellColIndices.length; i < count; i++) {
			// 对于grid没有绑定的列为空了grid不负责显示
			var index = this.bindRelation.indexOf(event.cellColIndices[i]);
			if (index != -1)
				colIndices.push(this.bindRelation
						.indexOf(event.cellColIndices[i]));
		}
		for (var i = 0, count = colIndices.length; i < count; i++) {
			var header = g.basicHeaders[colIndices[i]];
			if (header.isHidden)
				return;
			var cell = header.dataDiv.cells[rowIndex];
			if (cell != null) {
				if (event.rulesDescribe[i] != "") { // 校验失败
					cell.isErrorCell = true;
					var isOdd = g.isOdd(rowIndex);
					if (header.isFixedHeader)
						cell.className = isOdd
								? "fixed_gridcell_odd cell_error"
								: "fixed_gridcell_even cell_error";
					else
						cell.className = isOdd
								? "gridcell_odd cell_error"
								: "gridcell_even cell_error";
					cell.tip = event.rulesDescribe[i];
				} else { // 校验成功
					cell.isErrorCell = false;
					var isOdd = g.isOdd(rowIndex);
					if (header.isFixedHeader)
						cell.className = isOdd
								? "fixed_gridcell_odd"
								: "fixed_gridcell_even";
					else
						cell.className = isOdd
								? "gridcell_odd"
								: "gridcell_even";
					// 设置提示信息
					var title = row.getCellValue(colIndice);
					cell.tip = title;
				}
			}
		}
	}
	// 整页数据更新
	else if (PageChangeEvent.prototype.isPrototypeOf(event)) {
		// 对于分页,如果多选先将多选按钮变为非选中状态
		if (g.isMultiSelWithBox && g.selectAllBox.checked == true) {
			// g.selectAllBox.click();
			g.selectAllBox.checked = false;
		}
		g.paintRows(false, event.pageIndex); // this.owner.setModel(this);
		// check:08-14 当同一个keyValue的页切换的时候要重画选中行
		// if(this.dataset != null)
		// {
		// var selectedRows = this.dataset.getSelectedRows(null,
		// this.rowFilter);
		// if(selectedRows != null && selectedRows.length > 0)
		// {
		// if(g.isMultiSelWithBox == false)
		// {
		// var index = this.getUIRowIndex(selectedRows[0]);
		// if(index != -1)
		// {
		// // 单行选中情况处理
		// g.rowSelected(index);
		// // 如果已经选中的cell和当前选中行不在同一行,则把已经选中的cell的边框去掉
		// if(g.oldCell != null && g.oldCell.rowIndex != index) {
		// g.oldCell.className = g.oldClassName;
		// }
		// }
		// }
		// else if(g.isMultiSelWithBox)
		// {
		// var cell = null;
		// var index = null;
		// for(var i = 0, count = selectedRows.length; i < count; i ++)
		// {
		// index = this.getUIRowIndex(selectedRows[i]);
		// if(index != -1)
		// {
		// cell = g.selectColumDiv.cells[index];
		// if(!cell.firstChild.checked) {
		// cell.firstChild.checked = !cell.firstChild.checked;
		// }
		// }
		// }
		// }
		// }
		// }
		if (g.isShowSumRow)
			this.setSumColValue(null, null);
		// 设置分页信息
		g.setPaginationInfo();
	}
	// 插入新数据行
	else if (RowInsertEvent.prototype.isPrototypeOf(event)) {
		if (g.isMultiSelWithBox && g.selectAllBox.checked == true) {
			g.selectAllBox.click();
		}

		var rows = event.insertedRows;
		if (rows == null || rows.length == 0)
			return;

		var rowIndices = this.dataset.getRowIndices(rows);
		if (rowIndices != null) {
			var insertIndex = event.insertedIndex;
			var gridIndex = insertIndex;
			// if (this.rowFilter) {
			// var allRows = this.dataset.getRows();
			// for ( var i = 0, len = allRows.length; i < len
			// && i < insertIndex; i++) {
			// if (!this.rowFilter(allRows[i]))
			// gridIndex--;
			// }
			// }
			for (var i = 0, count = rows.length; i < count; i++) {
				var rowData = rows[i];
				// if (this.rowFilter) {
				// if (!this.rowFilter(rowData))
				// continue;
				// }

				var row = new GridCompRow();
				row.setRowData(rowData);
				row.setBindRelation(this.bindRelation);
				this.rows.splice(gridIndex, 0, row);
				// TODO guoweic
				g.fireRowInserted(gridIndex);
				gridIndex++;
			}
			if (g.isShowSumRow)
				this.setSumColValue(null, null);
		}

		// 增行后增加分页条上的记录数
		var rowCountSpan = $ge(g.id + "_rowCount");
		if (rowCountSpan != null) {
			var count = parseInt(rowCountSpan.innerHTML) + rowIndices.length;
			rowCountSpan.innerHTML = count;
		}

	}
	// 删除行
	else if (RowDeleteEvent.prototype.isPrototypeOf(event)) {
		if (event.deleteAll == false) {
			var indice = new Array();
			for (var i = 0, count = event.deletedIndices.length; i < count; i++) {
				var index = this.getUIRowIndex(event.deletedRows[i]);
				if (index != -1) {
					this.rows.splice(index, 1);
					indice.push(index);
				}
			}
			g.fireRowDeleted(indice);

			// 删行后减少分页条上的记录数
			var rowCountSpan = $ge(g.id + "_rowCount");
			if (rowCountSpan != null) {
				var count = parseInt(rowCountSpan.innerHTML) - indice.length;
				rowCountSpan.innerHTML = count;
			}

		}
		// 删除所有行数据
		else if (event.deleteAll) {
			// TODO:不要一行一行删除,改为列的整体删除
			var indice = new Array();
			for (var i = 0, count = event.deletedIndices.length; i < count; i++) {
				var index = this.getUIRowIndex(event.deletedRows[i]);
				if (index != -1) {
					this.rows.splice(index, 1);
					indice.push(index);
				}
			}
			g.fireRowDeleted(indice);

			// 删行后减少分页条上的记录数
			var rowCountSpan = $ge(g.id + "_rowCount");
			if (rowCountSpan != null) {
				rowCountSpan.innerHTML = 0;
			}
		}
		if (g.isShowSumRow)
			this.setSumColValue(null, null);
	}
	// undo操作
	else if (DatasetUndoEvent.prototype.isPrototypeOf(event)) {
		g.paintRows();
		var indice = new Array();
		var selectedRows = this.dataset.getSelectedRows();
		var UIIndex = -1;
		if (selectedRows != null && selectedRows.length > 0) {
			for (var i = 0, count = selectedRows.length; i < count; i++) {
				UIIndex = this.getUIRowIndex(selectedRows[i]);
				if (UIIndex != -1)
					indice.push(UIIndex);
			}
			g.selectedRowIndice = indice;
			g.rowSelected(indice[0]);
		} else if (selectedRows != null && selectedRows.length == 0) {
			// 没有选中行将选中数组置空
			if (g.selectedRowIndice != null && g.selectedRowIndice.length == 0)
				g.selectedRowIndice = null;
		}
		if (g.isShowSumRow)
			this.setSumColValue(null, null);
	}
	// 接收此事件清除行状态图标
	else if (StateClearEvent.prototype.isPrototypeOf(event)) {
		for (var i = 0, count = g.lineStateColumDiv.childNodes.length; i < count; i++) {
			g.lineStateColumDiv.childNodes[i].className = "row_state_div";
		}
	}
	// metadata changeg 事件
	else if (MetaChangeEvent.prototype.isPrototypeOf(event)) {
		//处理精度
		if  (event.precision != null){
			var index = this.bindRelation.indexOf(event.colIndex);
			if (index != -1){
				this.basicHeaders[index].setPrecision(event.precision,true);						
			}
		}
	}
	// focusIndex changeg 事件
	else if (FocusChangeEvent.prototype.isPrototypeOf(event)) {
		var index = event.focusIndex;
		g.rowSelected(index);
	}
	// 数据假删除 事件
	else if (DataFalseDelEvent.prototype.isPrototypeOf(event)) {
		var index = event.delRowIndex;
		var delRow = event.delRow;
		var UIIndex = this.getUIRowIndex(delRow);
		g.lineStateColumDiv.cells[UIIndex].className = "row_state_div row_delete_state";
	}
};

/**
 * 计算合计列的值,如果gridColIndex为null,dsColIndex也为null,则计算所有合计列的值
 * 
 * @param gridColIndex
 *            grid中列索引
 * @param dsColName
 *            ds列名
 * @private
 */
GridCompModel.prototype.setSumColValue = function(gridColIndex, dsColName) {
	var textAlign = "center";
	if (gridColIndex == null && dsColName == null) {
		for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
			var header = this.basicHeaders[i];
			if (header.isHidden == false && header.isSumCol) {
				var sum = this.dataset.totalSum([header.keyName], null, null,
						null);
				var sumCells = this.owner.dynSumRowDataDiv.childNodes;
				// 格式化结果数据
				sum = GridComp.parseData(header, sum);
				for (var j = 0, count1 = sumCells.length; j < count1; j++) {
					if (sumCells[j].headKey == header.keyName) {
						if (header.textAlign != null)
							textAlign = header.textAlign;
						sumCells[j].innerHTML = "<div style='text-align:"
								+ textAlign + "'>" + sum + "</div>";
					}
				}
			}
		}
	} else {
		// 如果此列是合计行,则向ds取合计列数据
		if (this.basicHeaders[gridColIndex].isSumCol) {
			var header = this.basicHeaders[gridColIndex];
			var sum = this.dataset.totalSum([dsColName], null, null, null);
			var sumCells = this.owner.dynSumRowDataDiv.childNodes;
			// 格式化结果数据
			sum = GridComp.parseData(header, sum);
			for (var i = 0, count = sumCells.length; i < count; i++) {
				if (sumCells[i].headKey == header.keyName) {
					if (header.textAlign != null)
						textAlign = header.textAlign;
					sumCells[i].innerHTML = "<div style='text-align:"
							+ textAlign + "'>" + sum + "</div>";
				}
			}
		}
	}
};

/**
 * 将指定header加入Model的headers数组
 * 
 * @param header
 *            为GridHeader的实例
 */
GridCompModel.prototype.addHeader = function(header) {
	if (this.headers == null)
		this.headers = new Array();
	this.headers.push(header);
};

/**
 * 设置model中的数据 不通过dataSet设置数据时使用
 */
GridCompModel.prototype.setRows = function(rows) {
	this.initBasicHeaders();
	this.rows = rows;
};

/**
 * 设置dataSet，此model适配dataset 注意：setDataSet之前必须保证this.headers已经初始化
 */
GridCompModel.prototype.setDataSet = function(dataset) {
	if (this.headers == null) {
		showErrorDialog("You must init headers before setDataSet!");
		return;
	}
	// 此model所适配的dataset
	this.dataset = dataset;
	// 解析出grid列和dataSet的绑定关系
	this.bindRelation = new Array();
	// 初始化basicHeaders
	this.initBasicHeaders();
	for (var i = 0, count = this.basicHeaders.length; i < count; i++){
		var index = dataset.nameToIndex(this.basicHeaders[i].keyName);		
		this.bindRelation.push(index);
		//设置精度
		var metadata = dataset.metadata[index];
		if (metadata && metadata.precision != null )
			this.basicHeaders[i].setPrecision(metadata.precision,true);						
	}
	dataset.bindComponent(this);

	// 将model的rows置为null,需要重新取新数据
	this.rows = null;
	this.initUIRows();
};

/**
 * 获取页数
 */
GridCompModel.prototype.getPageCount = function() {
	var pageInfo = this.dataset.getPageCount();
	// if (this.rowFilter)
	// return this.owner.processPageCount(pageInfo);
	return pageInfo;
};

/**
 * 根据headers初始化basicHeaders
 * 
 * @private
 */
GridCompModel.prototype.initBasicHeaders = function() {
	var header = null;
	var basics = null;
	this.basicHeaders = new Array();
	for (var i = 0, count = this.headers.length; i < count; i++) {
		header = this.headers[i];
		if (header.children == null)
			this.basicHeaders.push(header);
		else {
			// 得到basicHeader
			basics = header.getBasicHeaders();
			// 将包含真正有用信息的header放入顶层header的basicHeaders数组中
			header.basicHeaders = basics;
			for (var j = 0; j < basics.length; j++) {
				// 将header下包含真正有用信息的header放入basicHeaders中
				this.basicHeaders.push(basics[j]);
				// 保存此header的顶层header
				basics[j].topHeader = header;
			}
		}
	}
};

/**
 * 得到basicHeaders
 * 
 * @private
 */
GridCompModel.prototype.getBasicHeaders = function() {
	return this.basicHeaders;
};

/**
 * 得到模型中数据行数，如果没有数据返回0
 */
GridCompModel.prototype.getRowsCount = function() {
	if (this.dataset == null)
		return 0;
	else
		return this.rows == null ? 0 : this.rows.length;
};

/**
 * 得到新增行数据
 * 
 * @return rows GridRow数组
 */
GridCompModel.prototype.getNewAddedRows = function() {
	if (this.dataset != null) {
		var newAdded = this.dataset.getNewAddedRows();
		if (newAdded != null && newAdded.length > 0) {
			var rows = new Array(newAdded.length);
			for (var i = 0, count = newAdded.length; i < count; i++) {
				// 对dataset中的行进行适配包装
				var row = new GridCompRow();
				row.rowData = newAdded[i];
				rows[i] = row;
			}
			return rows;
		} else
			return null;
	}
	return null;
};

/**
 * 得到更新行数据
 * 
 * @return 更新行数组
 */
GridCompModel.prototype.getUpdatedRows = function() {
	if (this.dataset != null) {
		var updateRows = this.dataset.getUpdatedRows();
		if (updateRows != null && updateRows.length > 0) {
			var rows = new Array(updateRows.length);
			for (var i = 0, count = updateRows.length; i < count; i++) {
				// 对dataset中的行进行适配包装
				var row = new GridCompRow();
				row.rowData = updateRows[i];
				rows.push(row);
			}
			return rows;
		} else
			return null;
	}
	return null;
};

/**
 * 删除model一行
 * 
 * @param index
 *            删除行索引
 */
GridCompModel.prototype.deleteRow = function(index) {
	if (this.dataset != null)
		this.dataset.deleteRows([index]);
};

/**
 * 删除model中的多行
 * 
 * @param indice
 *            删除行索引数组
 */
GridCompModel.prototype.deleteRows = function(indice) {
	if (this.dataset != null)
		this.dataset.deleteRows(indice);
};

/**
 * 得到model中删除的行
 */
GridCompModel.prototype.getDeletedRows = function() {
	if (this.dataset != null)
		return this.dataset.getDeletedRows();
};

/**
 * 得到选中行
 */
GridCompModel.prototype.getSelectedRows = function() {
	if (this.dataset != null) {
		// var selectedRows = this.dataset.getSelectedRows(null,
		// this.rowFilter);
		var selectedRowsIndice = this.owner.selectedRowIndice, selectedRows = [];
		if (selectedRowsIndice != null && selectedRowsIndice.length > 0) {
			for (var i = 0; i < selectedRowsIndice.length; i++) {
				selectedRows.push(this.rows[selectedRowsIndice[i]]);
			}
		}

		if (selectedRows != null && selectedRows.length > 0) {
			var rows = new Array(selectedRows.length);
			for (var i = 0, count = selectedRows.length; i < count; i++) {
				// 对dataset中的行进行适配包装
				var row = new GridCompRow();
				row.rowData = selectedRows[i];
				rows.push(row);
			}
			return rows;
		} else
			return null;
	}
	return null;
};

/**
 * 判断行是否选中
 */
GridCompModel.prototype.isRowSelected = function(index) {
	// 根据dataset上的选中状态来判断
	var selRows = this.dataset.getSelectedRows();
	if (selRows != null && selRows.length > 0) {
		for (var i = 0, count = selRows.length; i < count; i++) {
			var rowIndex = this.getUIRowIndex(selRows[i]);
			if (rowIndex == index)
				return true;
		}
	}
	return false;
		// var selectedRowsIndice = this.owner.selectedRowIndice;
		// if (selectedRowsIndice != null && selectedRowsIndice.length > 0) {
		// for (var i = 0; i < selectedRowsIndice.length; i++) {
		// if (index == selectedRowsIndice[i])
		// return true;
		// }
		// }
		// return false;
};

/**
 * 设置当前聚焦行（鼠标点中行）
 * 
 */
GridCompModel.prototype.setFocusIndex = function(index) {
	if (this.dataset != null) {
		if (index == -1){
			this.dataset.setFocusRowIndex(-1);
		}
		else{
			var gridRow = this.getRow(index);
			if (gridRow != null) {
				var realIndex = this.dataset.getRowIndex(gridRow.rowData);
				this.dataset.setFocusRowIndex(realIndex);
			}
		}
	}
};

/**
 * 设置行选中
 * 
 * @param index
 *            UI上的选中行索引
 */
GridCompModel.prototype.setRowSelected = function(index) {
	if (this.dataset != null) {
		var gridRow = this.getRow(index);
		if (gridRow != null) {
			var realIndex = this.dataset.getRowIndex(gridRow.rowData);
			this.dataset.setRowSelected(realIndex);
		}
	}
};

/**
 * 设置多行选中
 * 
 * @param index
 *            UI上的选中行索引
 */
GridCompModel.prototype.addRowSelected = function(indices) {
	if (this.dataset != null) {
		if (indices instanceof Array) {
			var realIndices = new Array;
			for (var i = 0; i < indices.length; i++) {
				var index = indices[i];
				var gridRow = this.getRow(index);
				var realIndex = this.dataset.getRowIndex(gridRow.rowData);
				realIndices.push(realIndex);
			}
			this.dataset.addRowSelected(realIndices);
		} else {
			var gridRow = this.getRow(indices);
			var realIndex = this.dataset.getRowIndex(gridRow.rowData);
			this.dataset.addRowSelected(realIndex);
		}
	}
};

/**
 * 设置行反选
 */
GridCompModel.prototype.setRowUnSelected = function(indices) {
	if (this.dataset != null) {
		if (indices instanceof Array) {
			var realIndices = new Array;
			for (var i = 0; i < indices.length; i++) {
				var gridRow = this.getRow(indices[i]);
				var realIndex = this.dataset.getRowIndex(gridRow.rowData);
				realIndices.push(realIndex);
			}
			this.dataset.setRowUnSelected(realIndices);
		} else {
			var gridRow = this.getRow(indices);
			var realIndex = this.dataset.getRowIndex(gridRow.rowData);
			this.dataset.setRowUnSelected(realIndex);
		}
	}
};

/**
 * 在model指定位置插入数据行。model插入数据后会通知grid控件。
 * 
 * @param row
 *            所需插入行的数据
 * @param index
 *            当前插入的位置.此位置将被参数的行代替,此行后的所有行将移动.
 */
GridCompModel.prototype.insertRow = function(row, index) {
	if (this.dataset != null) {
		var newRow = new GridCompRow();
		var headers = this.basicHeaders;
		if (row == null) {
			var dsRow = this.dataset.insertEmptyRow(index);
			newRow.rowData = dsRow;
			newRow.relation = this.bindRelation;
		} else {
			var dsRow = new DatasetRow();
			for (var i = 0, count = headers.length; i < count; i++)
				dsRow.setCellValue(this.bindRelation[i], row.getCellValue(i));
			this.dataset.insertRow(index, dsRow);
			newRow.rowData = dsRow;
			newRow.relation = this.bindRelation;
		}
		return newRow;
	}
};

/**
 * 在model最后插入数据行。model插入数据后会通知grid控件。
 * 
 * @param row
 *            所需插入行的数据
 */
GridCompModel.prototype.addRow = function(row) {
	var ds = this.dataset;
	if (this.basicHeaders == null || this.basicHeaders.length == 0) {
		showErrorDialog("basicHeaders为null!");
		return;
	}
	var headers = this.basicHeaders;
	// 调用dataset的方法插入数据
	if (this.dataset != null) {
		var newRow = new GridCompRow();
		if (row == null) {
			// 在dataset中插入新行
			var dsRow = this.dataset.addEmptyRow();
			// 用GridCompRow包装dsRow返回(默认值的设置已由webdataset完成)
			newRow.rowData = dsRow;
			newRow.relation = this.bindRelation;
		} else {
			if (!GridCompRow.prototype.isPrototypeOf(row)) {
				showErrorDialog("the parameter 'row' must be the instance of GridCompRow");
				return;
			}
			var dsRow = new DatasetRow();
			for (var i = 0, count = headers.length; i < count; i++)
				dsRow.setCellValue(this.bindRelation[i], row.getCellValue(i));
			newRow.rowData = dsRow;
			newRow.relation = this.bindRelation;
		}
		return newRow;
	}
};

/**
 * 设置grid的编辑属性
 * 
 * @param isEditable
 *            设置grid可否编辑
 */
GridCompModel.prototype.setEditable = function(isEditable) {
	this.owner.setEditable(isEditable);
};

/**
 * 销毁model
 */
GridCompModel.prototype.destroySelf = function() {
	this.owner = null;
	this.splice(0, this.length);
};

/**
 * 获取选中所有行索引
 */
GridCompModel.prototype.getSelectedIndices = function() {
	if (this.owner != null)
		return this.owner.selectedRowIndice;
};

/**
 * 获取选中行索引
 */
GridCompModel.prototype.getSelectedIndex = function() {
	var indices = this.getSelectedIndices();
	if (indices == null || indices.length == 0)
		return -1;
	return indices[0];
};

/**
 * 获取当前聚焦行（鼠标点中行）
 */
GridCompModel.prototype.getFocusIndex = function() {
	if (this.dataset != null)
		return this.dataset.getFocusRowIndex();
};

/**
 * Grid行定义
 * 
 * @class GridComp行定义，这个row是对dataset row的适配，根据已经建立好的
 *        匹配规则，在执行期调用row.getCellValue(0)会根据匹配规则调用dataset相应行的getCell(i)来真正得到数据。
 * @version NC5.5
 * @since NC5.02
 */
GridCompRow = Array;

/**
 * 设置此行实际的数据
 * 
 * @param row
 *            一维数组或者dsRow
 */
GridCompRow.prototype.setRowData = function(row) {
	this.rowData = row;
};

/**
 * 得到此行实际的数据
 * 
 * @return 一维数组或者dsRow
 */
GridCompRow.prototype.getRowData = function() {
	return this.rowData;
};

/**
 * 设定GridCompRow和DsRow列的对应关系
 */
GridCompRow.prototype.setBindRelation = function(relation) {
	this.relation = relation;
};

/**
 * 根据指定的grid中的index返回dataset中实际列位置的值
 */
GridCompRow.prototype.getCellValue = function(index) {
	// dataset数据(根据帮定关系返回真实数据)
	if (this.relation != null) {
		if (index >= this.rowData.length || index < 0)
			showErrorDialog("Index out of bounds exception!");
		else {
			// 得到dataset中实际的列号
			var relPosi = this.relation[index];
			return this.rowData.getCellValue(relPosi);
		}
	}
};

/**
 * 根据索引设置row中相应列的值
 * 
 * @param index
 *            列号
 */
GridCompRow.prototype.setCellValue = function(index, value) {
	// dataset数据(根据帮定关系返回真实数据)
	if (this.relation != null) {
		if (index >= this.rowData.length || index < 0)
			showErrorDialog("Index out of bounds exception!");
		else {
			// 得到实际的dataSet列号
			var relPosi = this.relation[index];
			this.rowData.setCellValue(relPosi, value);
		}
	}
};

/**
 * 根据header的数据类型解析data
 * 
 * @private
 */
GridComp.parseData = function(header, data) {
	if (header.dataType == DataType.BOOLEAN) {
		var formater;
		if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new BooleanFormater();
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		}
		return formater.format(data);
	} else if (header.dataType == DataType.CHOOSE) {
		if (header.comboData == null)
			return data;
		var keyValues = header.comboData.getValueArray();
		var captionValues = header.comboData.getNameArray();
		var index = keyValues.indexOf(data);
		if (index != -1)
			return captionValues[index];
		return "";
	} else if (header.dataType == DataType.DATE) {
		var formater;
		if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new DateFormater();
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		}
		return formater.format(data);
	} else if (header.dataType == DataType.INTEGER) {
		var formater;
		if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new IntegerFormater(header.integerMinValue,
					header.integerMaxValue);
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		}
		return formater.format(data);
	} else if (header.dataType == DataType.Decimal
			|| header.dataType == DataType.FLOAT
			|| header.dataType == DataType.fLOAT
			|| header.dataType == DataType.UFDOUBLE
			|| header.dataType == DataType.dOUBLE) {
		var formater;
		if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new DicimalFormater(header.precision,
					header.floatMinValue, header.floatMaxValue);
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		}
		// 如果decimal精度变化则重新设置formatter精度
		if (formater.precision != header.precision)
			formater.precision = header.precision;
		return formater.format(data);
	} else {
		var formater;
		if ((formater = FormaterMap.get(header.owner.id + "$" + header.keyName)) == null) {
			formater = new Formater();
			FormaterMap.put(header.owner.id + "$" + header.keyName, formater);
		}
		return formater.format(data);
	}
};

/**
 * 获取对象信息
 * 
 * @private
 */
GridComp.prototype.getContext = function() {
	var context = new Object;
	context.c = "GridContext";
	context.id = this.id;
	context.enabled = (this.isGridActive == null || this.isGridActive == true)
			? true
			: false;
	context.editable = this.editable;
	var showColumns = this.getVisibleColumnIds();
	if (showColumns)
		context.showColumns = showColumns.join(",");
	return context;
};

/**
 * 设置对象信息
 * 
 * @private
 */
GridComp.prototype.setContext = function(context) {
	if (context.enabled != null)
		this.setActive(context.enabled);
	if (context.editable != null
			&& (this.editable == null || (this.editable != null && this.editable != context.editable)))
		this.setEditable(context.editable);
	if (context.showColumns != null && context.showColumns != "") // 显示列（用“,”分割的字符串）
		this.setShowColumns(context.showColumns.split(","));
		// TODO
		// if (context.insertRow && context.insertRowIndex)
		// this.insertRow(context.insertRow, context.insertRowIndex);
		// if (context.addRow)
		// this.addRow(context.addRow);
		// if (context.addRows)
		// this.addRows(context.addRows);
		// if (context.delRowIndexs)
		// this.deleteRows(context.delRowIndexs);

};
