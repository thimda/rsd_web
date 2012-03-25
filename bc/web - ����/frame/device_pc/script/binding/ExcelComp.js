/**
 * Excel控件
 * 
 * @fileoverview
 * 
 * @auther guoweic
 * @version NC6.0
 * 
 */

ExcelComp.prototype = new BaseComponent;
ExcelComp.prototype.componentType = "Excel";

ExcelComp.COlUMWIDTH_DEFAULT = 5;

/**
 * 构造方法
 * 
 * @class
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
function ExcelComp(parent, name, left, top, width, height, position, editable,
		isMultiSelWithBox, isShowNumCol, isShowSumRow, paginationObj,
		groupHeaderIds, sortable, className, isPagenationTop, showColInfo,
		oddType, isGroupWithCheckbox, isShowHeader) {
	if (arguments.length == 0)
		return;
	this.unableEventCount = 0;
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	this.parentOwner = parent;
	this.position = getString(position, "relative");

	// 放置Excel的frame对象
	this.frame = null;
	//OfficeControl
	this.officeControl = null;
	// 表头对象
	this.header = null;
	// 表头高度（默认为1）
	this.headerHeight = 1;
	// 分组的列
	this.groupHeaderIds = getString(groupHeaderIds, "");
	// 列索引映射（Excel列和Dataset真实列）
	this.colIndexMap = new HashMap();

	// TODO 当前sheet名称
	this.currentSheet = "Sheet1";
	
	this.activesheet = null;
	
	window.excelComp = this;
	this.create();
};

/**
 * 主体创建函数
 * 
 * @private
 */
ExcelComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("div");
	this.Div_gen.id = this.id + "_div";
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = this.height;
	
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 二级回调函数
 * 
 * @private
 */
ExcelComp.prototype.manageSelf = function() {
	if (IS_IE) {
		this.Div_gen.style.border = "1px solid red";
		this.officeControl = new OfficeControl({
			TargetElement:this.Div_gen.id,
			rootpath:window.globalPath,
			trackRevisions:true,
			autoSave:false,
			readonly:false,
			canopen:false,
			showRevisions:false,
			Titlebar:false,
			Statusbar:false,
			Menubar:false,
			Toolbars:false,
			FileSave:false,
			IsNoCopy:false,
			FileNew:false,
			width:this.width,
			height:this.height,
			OnSheetChanged:ExcelComp.valueChange,
			OnSheetBeforeDoubleClick:ExcelComp.beforeDoubleClick,
			OnSheetSelectionChange:ExcelComp.SheetSelectionChanged,
			OnSheetFollowHyperlink:ExcelComp.SheetFollowHyperlink
		});
		// 初始化控件
		this.initExcelComp();
	} else {
		this.Div_gen.innerHTML = "Excel控件目前只支持IE浏览器！";
	}
};

/**
 * 销毁控件（子类中如果必要须重写该方法）
 * 
 * @private
 */
ExcelComp.prototype.destroySelf = function() {
	if(this.officeControl != null)
		this.officeControl.Exit();
	this.officeControl = null;
	window.excelComp = null;
	
	this.destroy();
};

/**
 * 设置此Excel的model，重画表头和所有行数据
 * 
 * @param model
 *            excel数据集
 */
ExcelComp.prototype.setModel = function(model) {
	this.unableEvents();
	
	// 初始化设置model
	if (this.model == null) {
		this.model = model;
		// 将excel对象绑定在model的owner属性上
		this.model.owner = this;
		// 将model的basicHeaders保存到excel的basicHeaders中
		this.basicHeaders = this.model.basicHeaders;
	} else {
		this.model.dataset.unbindComponent(this.model);
		delete this.model;
		this.model = model;
		this.model.owner = this;
		this.basicHeaders = this.model.basicHeaders;
	}
	this.paintData();
	// if (this.pageSize > 0)
	// this.setPaginationInfo();

	// 计算所有合计列的值
	if (this.isShowSumRow) {
		this.model.setSumColValue(null, null);
	}
	this.enableEvents();
		// // 根据model编辑属性设置excel的编辑属性
		// if (this.model.dataset != null && this.model.dataset.isEditable() ==
		// false)
		// this.setEditable(false);
		// else if (this.model.dataset != null && this.model.dataset.editable
		// && this.model.dataset.editableChanged)
		// this.setEditable(true);
};

/**
 * 绑定数据集 ExcelComp.prototype.setDataset = function(dataset) { this.dataset =
 * dataset; dataset.bindComponent(this);
 * ExcelComp.execExcelOperate("ExcelComp.paintRows"); };
 */

/**
 * 设置配置信息
 */
ExcelComp.prototype.setConfig = function(config) {
	this.config = config;
};

/**
 * 执行Excel操作（方法中若包含对window.excelComp.officeControl的操作，则需调用该方法调用）
 * 
 * @param funcName
 *            执行操作的方法名称，后面的参数为执行方法参数，且必须为简单类型
 */
/* 去除对excel的间接调用
ExcelComp.execExcelOperate = function(funcName) {
	// 拼接执行方法的参数字符串
	var paramStr = "";
	for (var i = 1, n = arguments.length; i < n; i++) {
		paramStr += ",";
		if (typeof(arguments[i]) == "string") // 字符串类型参数
			paramStr += "\"" + arguments[i] + "\"";
		else if (typeof(arguments[i]) == "date") { // 日期类型
			// TODO ??????????????????????????????????
			var date = new Date(arguments[i]);
			paramStr += "\"" + date.getFomatDateTime() + "\"";
		} else
			// 非字符串类型参数
			paramStr += arguments[i];
	}
	if (paramStr.length > 0)
		paramStr = paramStr.substring(1);
	// 等待window.excelComp.excelObj对象加载
	if (ExcelComp.getExcelObjTimeout)
		clearTimeout(ExcelComp.getExcelObjTimeOut);
	if (!window.excelComp.officeControl) {
		if (paramStr.length > 0)
			ExcelComp.getExcelObjTimeout = setTimeout(
					"ExcelComp.execExcelOperate(\"" + funcName + "\","
							+ paramStr + ");", 100);
		else
			ExcelComp.getExcelObjTimeout = setTimeout(
					"ExcelComp.execExcelOperate(\"" + funcName + "\");", 100);
	} else {
		// 调用执行方法
		if (funcName != null) {
			eval(funcName + "(" + paramStr + ");");
		}
	}
};
*/
/**
 * 值改变事件
 */
ExcelComp.valueChange = function(sheetName, rowIndex, colIndex) {
	var oThis = window.excelComp;	
	oThis.unableEvents();// 禁止excel事件
	if (rowIndex <= oThis.headerHeight){ // 表头
		oThis.enableEvents();// 启用excel事件
		return;
	}
	var ischangecur = false;
	var currange = oThis.officeControl.curobj.ActiveDocument.Application.Selection;
	if(currange && currange.Rows && currange.Columns){
		var rowCount = currange.Rows.Count;
		var columnCount = currange.Columns.Count;
		if(rowCount > 0 && columnCount>0){
			for(var ri=1;ri<= rowCount && rowCount < 50;ri++){//最多处理50行
				for(var ci=1;ci<= columnCount;ci++){
					var currow = currange.Cells(ri,ci).Row;
					var curcolumn = currange.Cells(ri,ci).Column;
					var header = oThis.getHeaderByColIndex(curcolumn);
					if(!header)
						break;
					if(currow == rowIndex && curcolumn == colIndex)
						ischangecur = true;
					oThis.ChangeValuetoDS(sheetName,currow,curcolumn);
				}
			}
		}
	}
	if(!ischangecur)
		oThis.ChangeValuetoDS(sheetName, rowIndex, colIndex);
	oThis.enableEvents();// 启用excel事件
}
/**
 * 
 * @param {} sheetName
 * @param {} rowIndex
 * @param {} colIndex
 */
ExcelComp.prototype.ChangeValuetoDS = function(sheetName, rowIndex, colIndex) {
	//this.model.dataset.silent = true;
	// 获取在dataset中真实的rowIndex和colIndex
	var dsRowIndex = this.getDsRowIndexByExcelRowIndex(rowIndex);
	var dsColIndex = this.getDsColIndexByExcelColIndex(colIndex);
	if(!dsColIndex) return;		
	var basicHeaderIndex = this.getBasicHeaderIndexByExcelColIndex(colIndex);
	// 改变model的值
	var newValue = this.officeControl.GetRangeValue(sheetName, this.getRangeNameByIndex(rowIndex, colIndex)); // 获得修改的新值
	if (dsRowIndex >= this.getRowsNum()) { // 编辑行超出dataset范围,只改变字体的颜色
		
		// modify by renxh 2011-06-27
		if(dsRowIndex == this.getRowsNum()){			
			//var newRow = this.model.addRow();
			var newRow = this.model.getEmptyRow();//生成空行，但不插入dataset
			
			for(var i=0;i<this.basicHeaders.length;i++){
				/*
				if(this.basicHeaders[i].required){
					newRow.setCellValue(i,'默认值');	
				}
				*/
				if( i == basicHeaderIndex){
					newRow.setCellValue(basicHeaderIndex,newValue);
				}
				else{
					var curcolindex = this.getBasicHeaderIndexByExcelColIndex(i);
					if(curcolindex){
						var cellnewValue = this.officeControl.GetRangeValue(sheetName, this.getRangeNameByIndex(rowIndex, curcolindex));
						if(cellnewValue && cellnewValue != ""){
							newRow.setCellValue(curcolindex,cellnewValue);	
						}
					}
					/*
					else if(this.basicHeaders[i].required){
						newRow.setCellValue(i,'默认值');	
					}
					*/
				}
			}
			newRow = this.model.addRow(newRow);
			//this.paintRows();
			this.model.rows = null;
			this.model.initUIRows();
			this.addOneRow(newRow,this.getRowsNum()-1);
			return;
		}else{
			this.activesheet.Cells(rowIndex,colIndex).Font.ColorIndex = 4; // 改变字体的颜色
			alert("请在表格区域填写");
			
			return;
		}
	}
	var header = this.getHeaderByColIndex(colIndex);
	// 格式化值
	newValue = this.formatValue(header,newValue);	
	
	this.setCellValueFromFormatValue(sheetName,rowIndex, colIndex,newValue);
	//this.model.dataset.silent = false;
	
	
	// 下拉框类型，获取真正的value值
// if (header.editorType == EditorType.COMBOBOX)
// newValue = header.comboData.getValueByName(newValue);
		

	// cell编辑后事件
// var oldValue = oThis.model.getCellValueByIndex(row, col);
// if (newValue != oldValue) {
// if (oldValue != null && typeof(oldValue) != typeof(newValue)) // TODO
// // 类型不同，不进行插入（Date类型有错误）
// return;
// oThis.model.setValueAt(row, col, newValue);
// oThis.onAfterEdit(row, col, oldValue, newValue);
// }
};

ExcelComp.prototype.formatValue = function(header,data){
	var newValue = data;
	if(!header) return newValue;
	//lisw add;参照不做处理，逻辑值crud时再处理
	if(header.editorType == EditorType.REFERENCE){ // 引用类型		
		//if(newValue != '' && newValue != 'abc') // 此处可以写修改逻辑
			//newValue = this.getRefData(header.nodeInfo);
	}else if( header.editorType == EditorType.COMBOBOX){ // 下拉框类型
		newValue = header.comboData.getValueByName(newValue); // 根据名称获得对应的值
	}else{
		newValue = this.parseData(header,newValue);
	}
	if(newValue == null || newValue=='undefined') newValue = '';
	if(header.required && newValue == ''){ // 不允许为空的选取默认值
		//alert( header.showName + " 值不能为空，修改为默认值!");
		newValue = "";
	}
	return newValue;
}
/**
 * renxh 启用excel事件
 * */ 
ExcelComp.prototype.enableEvents = function(){
	if(this.unableEventCount>0)
		this.unableEventCount--;
	if(this.unableEventCount == 0){
		this.officeControl.enableEvents();
		this.protectSheet();
	}
}
/**
 * renxh 禁止excel事件
 * */ 
ExcelComp.prototype.unableEvents = function(){
	if(this.unableEventCount <=1){
		this.officeControl.unableEvents();
		this.unProtectSheet();
	}
	this.unableEventCount++;
}

/**
 * 前台excel驱动，更新模型和excel的值
 */
ExcelComp.prototype.setCellValueFromFormatValue = function(sheetName, rowIndex, colIndex, newValue) {
	
	try{
		var rangeName = this.getRangeNameByIndex(rowIndex, colIndex);
		var excelValue = '';
		var range = this.officeControl.GetRange(this.activesheet, rowIndex, colIndex);
		this.setRangeBgNormalColor(range);
		// 下拉框类型，获取真正的value值
		if (rowIndex > this.headerHeight && this.model.dataset != null) { // rowIndex <= headerHeight说明该单元格是表头
			
			//var header = oThis.headers[oThis.colIndexMap.get(colIndex)];
			var header = this.getHeaderByColIndex(colIndex);
			
			if (header && (header.editorType == EditorType.COMBOBOX)){
				excelValue = header.comboData.getNameByValue(newValue);
			}else{
				excelValue = newValue;
			}
			if(header && header.required){
				if(!newValue || newValue == "")
					this.setRangeBgRequireColor(range)
			}
		}
		var curvalue = range.Value;;
		if(curvalue != excelValue)
			range.Value = excelValue;
			//this.officeControl.SetRangeValue(sheetName, rangeName, excelValue);
		//this.testRelationChange(sheetName, rowIndex, colIndex, newValue);//测试联动值，正式时将此注释
		if (rowIndex > this.headerHeight && this.model.dataset != null) { // rowIndex <= headerHeight 说明该单元格是表头，不对ds进行修改
			// 获取dataset的旧值
			var dsRowIndex = this.getDsRowIndexByExcelRowIndex(rowIndex);
			var dsColIndex = this.getDsColIndexByExcelColIndex(colIndex);
			var oldValue = this.model.dataset.getValueAt(dsRowIndex, dsColIndex);
			if (oldValue != newValue)
					this.model.dataset.setValueAt(dsRowIndex, dsColIndex , newValue);
		}
	}catch(e){
		showErrorDialog(e.description);
	}
	
};
/** 测试联动情况  renxh*/
ExcelComp.prototype.testRelationChange = function(sheetName, rowIndex, colIndex, newValue){	
	var rangeName = this.getRangeNameByIndex(rowIndex+20, colIndex);
	this.officeControl.SetRangeValue(sheetName, rangeName, "联动值："+newValue);
}

/**
 * 单元格双击事件
 */
ExcelComp.beforeDoubleClick = function(sheetName, rowIndex, colIndex) {
	// TODO ?????????????????????????????????????????????????

	// if (rowIndex == 1 && colIndex == 1) {
	// window.excelComp.excelObj.CancelSheetDoubleClick = true;
	//		
	// var ds = pageUI.getWidget("main").getDataset("pwdsecds");
	// // window.excelComp.setDataset(ds);
	//		
	// var model = new ExcelCompModel();
	// var pk_passwordsecurity = new
	// ExcelCompHeader('pk_passwordsecurity','密码安全级别主键','15','String',false,true,true,'','','left','',false,DefaultRender,'StringText',null,null,null,false,false);
	// model.addHeader(pk_passwordsecurity);
	// var name = new
	// ExcelCompHeader('name','名称','10','String',false,false,true,'','','left','',false,DefaultRender,'StringText',null,null,null,false,false);
	// model.addHeader(name);
	// var defaultpassword = new
	// ExcelCompHeader('defaultpassword','缺省密码','12','String',false,false,true,'','','left','',false,DefaultRender,'StringText',null,null,null,false,false);
	// model.addHeader(defaultpassword);
	// var minimumlength = new
	// ExcelCompHeader('minimumlength','最低长度','12','Integer',false,false,true,'','','right','',false,IntegerRender,'IntegerText',null,null,null,false,false);
	// model.addHeader(minimumlength);
	// var errorloginthreshold = new
	// ExcelCompHeader('errorloginthreshold','错误允许数','13','Integer',false,false,true,'','','right','',false,IntegerRender,'IntegerText',null,null,null,false,false);
	// model.addHeader(errorloginthreshold);
	// var validatedays = new
	// ExcelCompHeader('validatedays','有效天数','12','Integer',false,false,true,'','','right','',false,IntegerRender,'IntegerText',null,null,null,false,false);
	// model.addHeader(validatedays);
	// var invalidatelock = new
	// ExcelCompHeader('invalidatelock','密码过期策略','13','Integer',false,false,true,'','','right','',false,passworddirtyRender,'ComboBox',null,null,null,false,false);
	// model.addHeader(invalidatelock);
	// invalidatelock.setHeaderComboBoxComboData($cb_main_pwdsecds);
	// var modpassword = new
	// ExcelCompHeader('modpassword','是否强制修改','13','UFBoolean',false,false,true,'','','center','',false,BooleanRender,'CheckBox',null,null,null,false,false);
	// model.addHeader(modpassword);
	// modpassword.setValuePair(["Y","N"]);
	// var alertdays = new
	// ExcelCompHeader('alertdays','有效提示天数','13','Integer',false,false,true,'','','right','',false,IntegerRender,'IntegerText',null,null,null,false,false);
	// model.addHeader(alertdays);
	// var complexity = new
	// ExcelCompHeader('complexity','验证类','50','String',false,false,true,'','','left','',false,DefaultRender,'StringText',null,null,null,false,false);
	// model.addHeader(complexity);
	// model.setDataSet(ds);
	//		
	// window.excelComp.setModel(model);
	//		
	//		
	// return;
	// }
	var oThis = window.excelComp;
	// 获取在dataset中真实的rowIndex和colIndex
	var row = rowIndex - oThis.headerHeight - 1;
	// 设置该行选中
	oThis.model.setRowSelected(row);
	//var header = oThis.headers[oThis.colIndexMap.get(colIndex)];
	
	var header = oThis.getHeaderByColIndex(colIndex);
	if(header){
		if (header.editorType == EditorType.REFERENCE) { // 参照类型
			var nodeInfo = header.nodeInfo;
			if (nodeInfo) {
				oThis.openRefDialog(nodeInfo);
				oThis.officeControl.EnableSheetDoubleClick();
			}
		}
	}
};


ExcelComp.SheetFollowHyperlink = function(sheet,LinkTarget){
	var name = LinkTarget.name;
	var match = /(refIndexpic)_(\d+)_(\d+)/;
	if(name.match(match)){
		alter("hello world!");
	}
}


/**
 * 单元格选择变更
 * @param {} SheetName
 * @param {} row
 * @param {} col
 */
ExcelComp.SheetSelectionChanged = function(SheetName, row, col){
	var oThis = window.excelComp;
	
	if (row <= oThis.headerHeight){ // 表头
		return;
	}
	var header = oThis.getHeaderByColIndex(col);
	if(!header) return;
	if (header.editorType == EditorType.REFERENCE) { // 参照类型
		oThis.ShowRefPic(SheetName, row, col);
	}
};
/**
 * 显示参照图片
 * @param {} SheetName
 * @param {} row
 * @param {} col
 */
ExcelComp.prototype.ShowRefPic = function(SheetName, row, col){	
	
	this.unProtectSheet();
	var activesheet = this.activesheet;
	if(activesheet){
		var match = /(refIndexpic)_(\d+)_(\d+)/;
		var range = activesheet.Cells(row,col);						
		var top = 0;
		var left = 0;
		var width = 15;
		var height = 15;
		if(range != null){
			top = range.Top;
			left = range.Left + range.Width -15;
			height = range.Height;
			if(left < 0) left = 0;
		}
		
		var imageName = "RefIamge";
		var image =  null;
		var ojeobjects =  activesheet.OLEObjects;
		if(ojeobjects){
			try{
				image = ojeobjects.Item(imageName);
			}catch(error){}
		}
		if(image == null){
			var location = window.location;
			var pro = location.protocol;
			var serverip = location.host;
			var port = location.port;
			if(port != "")
				port = ":"+port;
			var lfwpath = window.baseGlobalPath;
			
			//var url="http://20.1.85.63/lfw/frame/themes/rigo/images/calendar/close_off.gif";
			var url = pro + "//" + serverip + port + lfwpath + "/frame/themes/rigo/images/calendar/close_off.gif";			
			//image = activesheet.OLEObjects.Add("Forms.Image.1","",false,true,"","",left,top,width,height);
			
			image = activesheet.OLEObjects.Add("Forms.Image.1","",false,true,"");
			//image.SourceName = url;
			image.Name = imageName;
			image.Left = left;
			image.Top = top;
			image.Height = height;
			image.Width = 15;
			//image.LinkCell = range;
		}
		else{
			//image.LinkCell = range;
			image.Left = left;
			image.Top = top;
			image.Height = height;
			image.Width = 15;
		}
		
		/*
		var shape = null;
		var shapes = this.officeControl.GetActiveShapes();
		if(shapes){
			for(var i=0;i<shapes.Count;i++){
				if(shapes.Item(i).Name.match(match)){
					shape = shapes.Item(i);
					break;
				}
			}
		}
		if(shape == null){
			var location = window.location;
			var pro = location.protocol;
			var serverip = location.host;
			var port = location.port;
			if(port != "")
				port = ":"+port;
			var lfwpath = window.baseGlobalPath;
			
			//var url="http://20.1.85.63/lfw/frame/themes/rigo/images/calendar/close_off.gif";
			var url = pro + "//" + serverip + port + lfwpath + "/frame/themes/rigo/images/calendar/close_off.gif";
			shape =this.officeControl.AddPicByApplication(url,true,true,left,top,15,height);
			shape.Name = "refIndexpic_" + range.Row + "_" + range.Column;
			shape.Locked = true;
			//var addr = "http://ufida/portal/Ref";
			//activesheet.Hyperlinks.Add(shape,addr,"","请选择相应参照","参照");
		}
		else{
			shape.Left = left;
			shape.Top = top;
			shape.Height = height;
			shape.Width = 15;
			shape.Name = "refIndexpic_" + range.Row + "_" + range.Column;
			
			
			var shapelink = shape.Hyperlink;
			var addr = "http://ufida/portal/Ref";
			if(shapelink == null){
				shapelink = activesheet.Hyperlinks.Add(shape,addr,"","请选择相应参照","参照");
			}
			else{
				shapelink.Address = addr;
				shapelink.ScreenTip = "请选择相应参照";
			}
		}*/
		
	}
	this.protectSheet();
};

// 获得参照的引用数据列表
ExcelComp.prototype.getRefData = function(nodeInfo){
// var ajax = new Ajax();
// ajax.setPath(window.corePath + "/" + nodeInfo.path);
// ajax.addParam("pageId",encodeURIComponent(nodeInfo.pageMeta));
// ajax.addParam("widgetId",this.widget.id);
// ajax.addParam("otherPageUniqueId",getPageUniqueId());
// ajax.addParam("nodeId",nodeInfo.id);
// ajax.addParam("owner",nodeInfo.id);
// ajax.addParam("showType","type_div");
// ajax.post(false)
	return '修正后的值';
}

/**根据列获得对应的header，取得基本的表头结点 */
ExcelComp.prototype.getHeaderByColIndex = function(colIndex){
	var headerIndex = this.colIndexMap.get(colIndex);
	if(typeof(headerIndex)  == 'undefined') return null;
	if(typeof(headerIndex) == 'number'){
		return this.headers[headerIndex];
	}else{
		var indexArray = headerIndex.split('-');
		var header = this.headers[indexArray[0]];
		return header.children[indexArray[1]];
	}
}

ExcelComp.prototype.refReturnFunc = function(xmlHttpReq, returnArgs, tip, ajax){
	var resp = xmlHttpReq.responseText;
	alert(resp);
	var map = new HashMap();
	
}



/**
 * 打开参照对话框
 */
ExcelComp.prototype.openRefDialog = function(nodeInfo) {
	// //记录当前参照refnode的id
	// if (nodeInfo.id != null)
	// window.$nowRefNodeId = nodeInfo.id;

	// 关闭之前打开的参照对话框
	this.closeRefDialog();

	var url = null;
	url = window.corePath + "/" + nodeInfo.path + "?pageId="
			+ encodeURIComponent(nodeInfo.pageMeta) + "&widgetId="
			+ this.widget.id + "&otherPageUniqueId=" + getPageUniqueId()
			+ "&nodeId=" + nodeInfo.id + "&owner=" + nodeInfo.id
			+ "&showType=type_div";
	this.referenceDialog = showDialog(url, "参照选择", 600 ,400, "reference select", true ,false, null) ;
	
	var reframe = document.getElementById(Excel_Ref_frame_ID);
	if(!reframe){
		var refdiv = this.referenceDialog.Div_gen;
		var Excel_Ref_frame_ID = "Excel_Ref_frame";
		
		var iframe = $ce("iframe");
		iframe.name = Excel_Ref_frame_ID;
		iframe.id = Excel_Ref_frame_ID;
		iframe.style.top = 0;
		iframe.style.left = 0;
		iframe.style.width = "100%";
		iframe.style.height = "100%";		
		iframe.style.visibility = "inherit";
		iframe.style.position = "absolute";
		iframe.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)";
		iframe.style.zIndex = -1;
		refdiv.appendChild(iframe);
	}

	/*
	this.referenceDialog = window.open(url, null, "height=400,width=600");
	this.referenceDialog.parentPage = window;
	this.referenceDialog.moveTo((screen.availWidth-600)/2,(screen.availHeight-400)/2);
	this.referenceDialog.focus();
		// // Dialog中的Iframe的ID（该格式在PageUtil.js中的showDialog()方法里定义）
		// var iframeId = "$modalDialogFrame" + this.refIndex;
		// // 调用方法加载数据
		// ReferenceTextComp.doFilter("", iframeId, true);
		 
		*/ 
};

/**
 * 关闭参照对话框
 */
ExcelComp.prototype.closeRefDialog = function() {
	if (this.referenceDialog != null) {
		this.referenceDialog.close();
	}
};

/**
 * 初始化Excel控件
 */
ExcelComp.prototype.initExcelComp = function() {	
	if(this.officeControl){
		var sheets = this.officeControl.GetSheets()
		if(sheets == null || sheets.Count<1){
			this.officeControl.CreateSheet();
			sheets = this.officeControl.GetSheets();
		}
		this.activesheet = sheets(this.currentSheet); 
		this.setRangeBgColor(this.activesheet.Cells,12632256);
		this.setRangeAlign(this.activesheet.Cells,2);
		this.setBorderNormalColor(this.activesheet.Cells);
		//this.setCellBorder(this.activesheet.Cells,1);
		this.setCellBorderLineStyle(this.activesheet.Cells,0);
		this.officeControl.SetRangeLocked(this.activesheet.Cells,false);		
		this.officeControl.curobj.ActiveDocument.CommandBars("Cell").Enabled = false;
		this.protectSheet();
	}
};

/**
 * paint所有重新设定的model数据
 * 
 * @private
 */
ExcelComp.prototype.paintData = function() {
	if (this.model == null)
		return;
	// 清空原来数据显示页
	this.clearSheetData( this.currentSheet);
	// // 初始化构建框架所需的各个常量
	// this.initConstant();
	// 根据model初始化header
	this.initBasicHeaders();
	// 画表头
	this.paintHeaders();
	var rowCount = this.getRowsNum();
	// 真正的画界面
	this.paintZone();
	// // 各区域的事件处理
	// this.attachEvents();
	// 当前选中行索引
	this.selectedRowIndice = null;
	this.currActivedCell = null;
	if (this.showComp)
		this.hiddenComp();
};

/**
 * 清空sheet内所有内容
 * 
 * @private
 */
ExcelComp.prototype.clearSheetData = function(sheetName) {
	// alert(typeof(window.excelComp.excelObj.ActiveDocument.Sheets("Sheet1").Range("C5").Validation));
	// var oThis = this;
	// window.excelComp.excelObj.ActiveDocument.Sheets(oThis.currentSheet).Cells(3,13).Validation.Modify([1111,22],
	// true, "$C$1:$C$13");
	// TODO
	// window.excelComp.excelObj.ActiveDocument.Sheets(oThis.currentSheet).Cells.Clear
	// = true;
};

/**
 * 返回model中的行数量
 * 
 * @private
 */
ExcelComp.prototype.getRowsNum = function() {
	if (this.model == null)
		return 0;
	else
		return this.model.getRowsCount();
};

/**
 * 获得基本头信息。支持多表头，只有最下层的表头包含有用信息。
 * 
 * @param headers
 *            最顶层的headers
 * @private
 */
 ExcelComp.prototype.initBasicHeaders = function() {
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
	} else {
		if (this.headers == null)
			throw new Error("excel must be initialized with headers!");
	}
};

/**
 * 根据key获取header
 * 
 * @private
 */
ExcelComp.prototype.getHeader = function(keyName) {
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

/**
 * 获得Header最大深度
 * 
 * @private
 */
ExcelComp.prototype.getHeaderDepth = function() {
	var maxDepth = 1;
	for (var i = 0; i < this.headers.length; i++) {
		var depth = this.headers[i].getDepth();
		if (maxDepth <= depth)
			maxDepth = depth;
	}
	return maxDepth;
};

/**
 * 根据生成的headers model画表头
 * 
 * @private
 */
ExcelComp.prototype.paintHeaders = function() {
	this.colIndexMap.clear();
	// 要显示的表头在Excel上的列号
	var colIndex = 0;
	this.headerHeight = this.getHeaderDepth();
	var childTotalCount = 0;
	// 绘制每个header
	for (var headerIndex = 0, count = this.headers.length; headerIndex < count; headerIndex++) {
		// 隐藏列不创建
		if (this.headers[headerIndex].isHidden)
			continue;
		var header = this.headers[headerIndex];
		if (header.children == null){ // 单表
			colIndex++;			
			if(this.headerHeight>1){
				this.mergeCellsByIndex(this.currentSheet,1,colIndex,this.headerHeight,colIndex);
			}
			this.paintHeader(header, colIndex,header.getDepth());
			this.colIndexMap.put(colIndex, headerIndex);
		}else{
			colIndex++;
			this.mergeCellsByIndex(this.currentSheet,1,colIndex,1,colIndex+header.children.length-1);
			this.paintHeader(header,colIndex,1);
			childTotalCount += header.children.length
			for(var j=0;j<header.children.length;j++){
				if(j>0)colIndex++;				
				this.paintHeader(header.children[j], colIndex,2);
				this.colIndexMap.put(colIndex, headerIndex+'-'+j);
			}
		}
	}
	
};

/**
 * 绘制每个header
 * 
 * @param header
 *            this.headers数组中的header
 * @private
 */
ExcelComp.prototype.paintHeader = function(header, headerIndex,rowIndex) {
	var oThis = this;
	// TODO 得到header的总深度 ????????????????????????????
	var totalDepth = rowIndex;// header.getDepth();
	// 得到此header的总宽度
	var headerWidth = this.getHeaderDefaultWidth(header);
	// 单表头情况
		// TODO 设置表头背景颜色 （必须将颜色对应成Excel所需的数字类型，此处默认为灰色，不可单独修改）
		// if (header.columBgColor != null && header.columBgColor != "")
		

		// 设置表头边框（默认为实线）
		var borderWeight = 2;
		var range = this.officeControl.GetRange(this.activesheet, totalDepth, headerIndex);
		
		this.setCellBorder(range, borderWeight);
		this.setCellBgColor(range, new VBAColor(150,150,150).getNumber());
		// // 根据header中的textcolor属性设置文字颜色 （必须将颜色对应成Excel所需的数字类型，此处默认为黑色，不可单独修改）
		// // if (header.textColor != null && header.textColor != "")
		// ExcelComp.execExcelOperate("ExcelComp.setCellFontColor", "Sheet1",
		// totalDepth, headerIndex, 1);

		// 设置宽度
		if (header.width)
			headerWidth = header.width;
		this.setColumnWidth(this.activesheet, headerIndex, headerWidth);

		// 对齐方式
		this.setCellAlign( range, "center");
		// 设值
		this.setCellValue( range, header.showName,totalDepth, headerIndex);
		this.officeControl.SetRangeLocked(range,true);
		// this.showName = getString(showName, "");
		// // cell中元素的对齐方式
		// this.textAlign = getString(textAlign, "left");
		// this.columBgColor = getString(columBgColor, "");
		// this.textColor = getString(textColor, "black");

};

/**
 * 得到最顶层header的默认初始宽度
 * 
 * @param header
 *            最顶层header
 * @private
 */
ExcelComp.prototype.getHeaderDefaultWidth = function(header) {
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
 * model改变后调用此方法重画所有行数据
 * 
 * @private
 */
ExcelComp.prototype.paintRows = function(startIndex, count) {
	// 将model的rows置为null,需要重新取新数据
	this.model.rows = null;
	if (startIndex != null && count != null)
		this.model.initUIRows(startIndex, count);
	else
		this.model.initUIRows(startIndex);

	var rows = this.model.getRows();
	if (rows == null || rows.length == 0)
		return;
	for (var i = 0; i < rows.length; i++) {
		this.addOneRow(rows[i], i);
	}
};
/** 为排序重新画界面*/
ExcelComp.prototype.paintRowsForSort = function() {
	// 将model的rows置为null,需要重新取新数据
	var rows = this.model.getRows();
	if (rows == null || rows.length == 0)
		return;
	for (var i = 0; i < rows.length; i++) {
		this.addOneRow(rows[i], i);
	}
};

/**
 * model改变后调用此方法重画所有行数据
 * 
 * @private ExcelComp.prototype.paintRows = function(startIndex, count) { //
 *          将model的rows置为null,需要重新取新数据 this.model.rows = null; if (startIndex !=
 *          null && count != null) this.model.initUIRows(startIndex, count);
 *          else this.model.initUIRows(startIndex);
 * 
 * var rowCount = this.getRowsNum(); this.paintedSign = null; //
 * 记录行标志的数组.0表示未画,1表示画了行 this.paintedSign = new Array(rowCount); // 设置行标志为未画 for (
 * var i = 0; i < rowCount; i++) this.paintedSign[i] = 0;
 * 
 * this.paintZone(); };
 */

/**
 * 画区域
 * 
 * @private
 */
ExcelComp.prototype.paintZone = function() {
	this.paintRows();
};

/**
 * 给表格增加一行
 * 
 * @param row(model数据中的一行)
 * @private
 */
ExcelComp.prototype.addOneRow = function(row, rowIndex) {	
	var excelRowIndex = rowIndex + this.headerHeight + 1;
	
	for (var i = 0; i < row.rowData.dataArr.length; i++) {
		var coindex = this.getExcelColIndexByDsColIndex(i);
		var headerindex = this.getBasicHeaderIndexByExcelColIndex(coindex);
		if(!coindex)
			continue;
		if (this.basicHeaders[headerindex] && this.basicHeaders[headerindex].isHidden)
			continue;
		
		var value = row.getCellValue(headerindex);
		var range = this.officeControl.GetRange(this.activesheet,excelRowIndex, coindex);
		
		// 下拉框类型
		if (this.basicHeaders[headerindex] && this.basicHeaders[headerindex].editorType == EditorType.COMBOBOX) { // 下拉框类型
			var captionValuesStr = "";
			var comboData = this.basicHeaders[headerindex].comboData;
			if (comboData != null) {
				var caption = comboData.getNameByValue(value);
				/*
				if (caption != null)
					value = caption;
				else
					value = "";
					*/
				var captions = comboData.getNameArray();
				if (captions.length > 0)
					captionStr = captions.join(",");
			}
			this.setCellComboData(range, captionStr);
		}
		//指定背景颜色，可以对不同的列指定不同的背景颜色 renxh
		var columBgColor = 0;
		if(this.basicHeaders[headerindex]){
			columBgColor = this.basicHeaders[headerindex].columBgColor;
		}		
				

		// 设置边框（默认为实线）
		var borderWeight = 1;
		
		this.setCellBorder(range, borderWeight);
		//设置单元格背景色 renxh
		//this.setCellBgColor(range, "red");
		this.setRangeBgNormalColor(range);
		
		//this.setCellValue( range, value,excelRowIndex, coindex);
		this.setCellValue( range, value,excelRowIndex, coindex);
	
		if(this.basicHeaders[headerindex]){ //锁定单元格 renxh
			
			var rangeName = this.getRangeNameByIndex(excelRowIndex, coindex);
			
			if (this.basicHeaders[headerindex].editorType == EditorType.REFERENCE){
				//this.officeControl.SetRangeLocked(this.currentSheet,rangeName,true); // 锁定单元格
			}
			else{
			
				if(this.basicHeaders[headerindex].columEditable){
					this.officeControl.SetRangeLocked(range,false); // 解锁单元格
				}else{
					this.officeControl.SetRangeLocked(range,true); // 锁定单元格
				}
			}
		}
	}
};

/**
 * 后台设置某单元格的值,此处同时更新excel和dataset，但是此处的应用不能是前台excel驱动，
 * 因为此处赋值时需要拿到excel中的值进行比较，如果再调用此方法，新值和旧值相等的现象，而不执行赋值操作
 */
ExcelComp.prototype.setCellValue = function(range, newValue,rowIndex,colIndex) {
	var header = null;
	// 下拉框类型，获取真正的value值
	
	if (rowIndex > this.headerHeight && this.model.dataset != null) { // headerHeight说明该单元格是表头高度
		header = this.getHeaderByColIndex(colIndex);
		//var header = oThis.headers[oThis.colIndexMap.get(colIndex)];
		if (header && (header.editorType == EditorType.COMBOBOX)){				
			newValue = header.comboData.getNameByValue(newValue);
		}
	}
	if(header && header.required){
		if(!newValue || newValue == "")
			this.setRangeBgRequireColor(range);
	}
	// 获取Excel单元格的旧值
	var rangeOldValue = range.Value;
	if (newValue != rangeOldValue) {
		range.Value = newValue;
	}
};

/**
 * 设置某单元格的对齐方式
 */
ExcelComp.prototype.setCellAlign = function(range, align) {
	// 默认左对齐
	var alignType = 3;
	if (align == "right") // 右对齐
		alignType = 4;
	else if (align == "left") // 左对齐
		alignType = 2;
	
	
	if(range != null)
		range.HorizontalAlignment = alignType;
};
ExcelComp.prototype.setRangeAlign = function(range,align){
	if(range != null)
		range.HorizontalAlignment = align;
}

/**
 * 设置某单元格的背景颜色
 */
ExcelComp.prototype.setCellBgColor = function(range, color) {
	if(range != null)
		range.Interior.Color = color;
};
/**
 * 设置正常背景色
 * @param {} range
 */
ExcelComp.prototype.setRangeBgNormalColor=function(range){
	this.setCellBgColor(range,new VBAColor(255,255,255).getNumber());
}
/**
 * 设置必输项背景色
 * @param {} range
 */
ExcelComp.prototype.setRangeBgRequireColor=function(range){
	this.setCellBgColor(range,new VBAColor(255,245,245).getNumber());
}
/**
 * 设置某单元格的背景颜色
 */
ExcelComp.prototype.setRangeBgColor = function(range, color) {
	try{
		if(range != null)
			range.Interior.Color = color;
	}
	catch(ex){
		Logger.error("设置单元格背景出错",ex);
	}
}
/**
 * 设置某单元格的文字颜色
 */
ExcelComp.prototype.setCellFontColor = function(sheet, rowIndex, colIndex, color) {	
	var range = this.officeControl.GetRange(sheet,rowIndex, colIndex);
	if(range != null)
		range.Interior.Pattern = color;
};

/**
 * 设置某单元格的边框
 */
ExcelComp.prototype.setCellBorder = function(range, weight) {
	try{
		if(range != null)
			range.Borders.Weight = weight;
		}
	catch(ex){
		Logger.error("设置单元格边框出错",ex);
	}
};
/**
 * 设置边框线样式
 * @param {} range
 * @param {} linestyle
 */
ExcelComp.prototype.setCellBorderLineStyle = function(range, linestyle) {
	try{
		if(range != null)
			range.Borders.LineStyle = linestyle;
		}
	catch(ex){
		Logger.error("设置单元格边框出错",ex);
	}
};
/**
 * 设置表格边框颜色
 * @param {} range
 * @param {} color
 */
ExcelComp.prototype.setBorderColor = function(range,color){
	try{
		if(range != null)
			range.Borders.Color = color;
		}
	catch(ex){
		Logger.error("设置单元格边框出错",ex);
	}
}
/**
 * 设置正常表格边框颜色
 * @param {} range
 */
ExcelComp.prototype.setBorderNormalColor = function(range){
	this.setBorderColor(range,new VBAColor(0,0,0).getNumber());
}
/**
 * 设置必输项表格边框
 * @param {} range
 */
ExcelComp.prototype.setBorderRequireColor = function(range){
	this.setBorderColor(range,new VBAColor(50,0,0).getNumber());
	//this.setRangeBgColor(range,new VBAColor(50,0,0).getNumber());
	
}

/**
 * 设置某单元格序列（下拉选项）
 * 
 * @param dataStr
 *            下拉选项字符串（以,分割）
 */
ExcelComp.prototype.setCellComboData = function(range, dataStr) {
	try{
		range.Validation.Delete(); // renxh 先删除数据有效性 再添加，否则会报错
		range.Validation.Add(3, 1, 1, dataStr);
	}catch(e){		
		alert(e.description);
	}
	
};

/**
 * 设置某列宽度
 */
ExcelComp.prototype.setColumnWidth = function(sheet, colIndex, width) {
	sheet.Columns(colIndex).ColumnWidth = width;
};

/**
 * 根据行、列索引号生成Excel单元格标识 注意：Excel中行号、列号是从1开始的！！！
 */
ExcelComp.prototype.getRangeNameByIndex = function(rowIndex, colIndex) {
	if(colIndex <= 26){
		var letter = String.fromCharCode(colIndex + 64);
		return letter + rowIndex;
	}else{
		// renxh 对于 AA AB 等情况的支持
		if(colIndex>26){
			var index = Math.floor(colIndex/26);
			var left = colIndex%26;
			var letter1 = String.fromCharCode(index + 64);
			var letter2 = String.fromCharCode(left + 64);
			return letter1 + letter2 + rowIndex;
		}
	}
	return '';
	
};
/**renxh 合并单元格的操作*/
ExcelComp.prototype.mergeCellsByIndex = function(sheetName,startRowIndex,startColIndex,endRowIndex,endColIndex){
	var startRangeName = this.getRangeNameByIndex(startRowIndex, startColIndex);
	var endRangeName = this.getRangeNameByIndex(endRowIndex, endColIndex);
	try{
		var range = this.activesheet.Range(startRangeName+":"+endRangeName);
		range.MergeCells = true;
	}catch(e){
		showErrorDialog("合并单元格失败！"+sheetName+",["+startRowIndex+","+startColIndex+"],["+endRowIndex+","+endColIndex+"]");
	}
}
/**保护工作薄 renxh*/ 
ExcelComp.prototype.protectSheet = function(){
	/**
	 * Password   Variant 类型，可选。为一个字符串，该字符串为工作表或工作簿指定区分大小写的密码。如果省略本参数，不用密码就可以取消对该工作表或工作簿的保护。否则，必须指定密码，通过密码来取消对该工作表或工作簿的保护。如果忘记了密码，就无法取消对该工作表或工作簿的保护。最好在安全的地方保存一份密码及其对应文档名的列表。
	DrawingObjects   Variant 类型，可选。如果为 True，则保护图形。默认值为 False。
	Contents   Variant 类型，可选。如果为 True，则保护内容。对于图表，这样将保护整个图表。对于工作表，这样将保护锁定的单元格。默认值为 True。
	Scenarios   Variant 类型，可选。如果为 True，则保护方案。本参数仅对工作表有效。默认值为 True。
	UserInterfaceOnly   Variant 类型，可选。如果为 True，则保护用户界面，但不保护宏。如果省略本参数，则保护既应用于宏也应用于用户界面。
	AllowFormattingCells   Variant 类型，可选。如果为 True，则允许用户为受保护的工作表上的任意单元格设置格式。默认值为 False。
	AllowFormattingColumns   Variant 类型，可选。如果为 True，则允许用户为受保护的工作表上的任意列设置格式。默认值为 False。
	AllowFormattingRows   Variant 类型，可选。如果为 True，则允许用户为受保护的工作表上的任意行设置格式。默认值为 False。
	AllowInsertingColumns   Variant 类型，可选。如果为 True，则允许用户在受保护的工作表上插入列。默认值为 False。
	AllowInsertingRows   Variant 类型，可选。如果为 True，则允许用户在受保护的工作表上插入行。默认值为 False。
	AllowInsertingHyperlinks   Variant 类型，可选。如果为 True，则允许用户在受保护的工作表中插入超链接。默认值为 False。
	AllowDeletingColumns   Variant 类型，可选。如果为 True，则允许用户在受保护的工作表上删除列，要删除的列中的每个单元格都是解除锁定的。默认值为 False。
	AllowDeletingRows   Variant 类型，可选。如果为 True，则允许用户在受保护的工作表上删除行，要删除的行中的每个单元格都是解除锁定的。默认值为 False。
	AllowSorting   Variant 类型，可选。如果为 True，则允许用户在受保护的工作表上进行排序。排序区域中的每个单元格必须是解除锁定的或取消保护的。默认值为 False。
	AllowFiltering   Variant 类型，可选。如果为 True，则允许用户在受保护的工作表上设置筛选。用户可以更改筛选条件，但是不能启用或禁用自动筛选功能。用户也可以在已有的自动筛选功能上设置筛选。默认值为 False。
	AllowUsingPivotTables   Variant 类型，可选。如果为 True，则允许用户在受保护的工作表上使用数据透视表。默认值为 False。
	*/
	this.activesheet.Protect(/*Password*/"1234",
		/*DrawingObjects*/true,
		/*Contents*/true,
		/*Scenarios*/true,
		/*UserInterfaceOnly*/true,
		/*AllowFormattingCells*/false,
		/*AllowFormattingColumns*/false,
		/*AllowFormattingRows*/false,
		/*AllowInsertingColumns*/false,
		/*AllowInsertingRows*/true,
		/*AllowInsertingHyperlinks*/false,
		/*AllowDeletingColumns*/false,
		/*AllowDeletingRows*/true);
		
}

ExcelComp.prototype.unProtectSheet = function(){
	this.activesheet.Unprotect("1234");
}

/**
 * 根据excel中真实列号，获得dataset中的列号
 * */
ExcelComp.prototype.getDsColIndexByExcelColIndex = function(excelColIndex){
	var header = this.getHeaderByColIndex(excelColIndex);
	if(header){
		for(var i=0;i<this.basicHeaders.length;i++){
			if(this.basicHeaders[i].keyName == header.keyName){
				return this.model.bindRelation[i];
			}
		}
	}
	return null;
}
/** renxh 获得基本表头的索引*/
ExcelComp.prototype.getBasicHeaderIndexByExcelColIndex = function(excelColIndex){
	var header = this.getHeaderByColIndex(excelColIndex);
	if(header){
		for(var i=0;i<this.basicHeaders.length;i++){
			if(this.basicHeaders[i].keyName == header.keyName){
				return i;
			}
		}
	}
	return null;
}

ExcelComp.prototype.getDsRowIndexByExcelRowIndex = function(excelRowIndex){
	return excelRowIndex - this.headerHeight - 1;
}

/**
 * 根据dataset的列号取得excel中的真实列号
 */
ExcelComp.prototype.getExcelColIndexByDsColIndex = function(dsColIndex) {
	var basicHeaderIndex = -1;
	for(var i = 0; i < this.model.bindRelation.length;i++){
		if(this.model.bindRelation[i] == dsColIndex){
			basicHeaderIndex = i;
			break;
		}
	}
	if(basicHeaderIndex == -1) return null;
	var headerIndex = this.getHeaderIndexByBasicHeaderIndex(basicHeaderIndex);
	
	var values = this.colIndexMap.values();
	for (var i = 0, n = values.length; i < n; i++) {
		if (headerIndex == values[i]) {
			var keys = this.colIndexMap.keySet();
			return parseInt(keys[i]);
		}
	}
};
/**
 * 
 *  renxh 根据basicheader的index获得 header的Index 
 * 
 */
ExcelComp.prototype.getHeaderIndexByBasicHeaderIndex = function(basicHeaderIndex){
	var basicHeader = this.basicHeaders[basicHeaderIndex];
	for(var i=0;i<this.headers.length;i++){
		if(!this.headers[i]) continue;
		if(this.headers[i].children == null){
			if(this.headers[i].keyName == basicHeader.keyName){
				return i;
			}			
		}else{
			var childrenHeaders = this.headers[i].children;
			for(var j=0;j<childrenHeaders.length;j++){
				if(childrenHeaders[j].keyName == basicHeader.keyName){
					return i+'-'+j;
			}	
			}
		}
	}
}

/**
 * 将dataset数据转换为Excel所需数据
 */
ExcelComp.prototype.convertData = function() {

};

/**
 * 设置XML数据
 */
ExcelComp.prototype.setDataXML = function(xml) {

};

/**
 * 保存方法
 */
ExcelComp.prototype.save = function() {

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
ExcelComp.prototype.onAfterEdit = function(rowIndex, colIndex, oldValue,
		newValue) {
	var afterCellEditEvent = {
		"obj" : this,
		"rowIndex" : rowIndex,
		"colIndex" : colIndex,
		"oldValue" : oldValue,
		"newValue" : newValue
	};
	alert("rowIndex:" + rowIndex + ",colIndex:" + colIndex + ",oldValue:"
			+ oldValue + ",newValue:" + newValue);
	this.doEventFunc("afterEdit", ExcelCellListener.listenerType,
			afterCellEditEvent);
};

/**
 * 获取对象信息
 * 
 * @private
 */
ExcelComp.prototype.getContext = function() {
	var context = new Object;
	// context.javaClass = "nc.uap.lfw.core.comp.ctx.ExcelContext";
	context.c = "ExcelContext";
	context.id = this.id;
	// context.enabled = !this.disabled;
	return context;
};

/**
 * 设置对象信息
 * 
 * @private
 */
ExcelComp.prototype.setContext = function(context) {
	// if (context.enabled == this.disabled)
	// this.setActive(context.enabled);

};

ExcelComp.prototype.sortByHeader = function(sheetName,rowIndex,colIndex){
	
	if(rowIndex <= this.headerHeight){
		var header = this.getHeaderByColIndex(colIndex);
		if(header.sortable){
			this.model.sortable([header],null,null);
		}
	}
}

/** --------------------------------------------------------------------------------------------------------------------------------------- */

/**
 * 表头类构造函数
 * 
 * @class 表头类，用于构件excel的表头。
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
 * @param  isAutoExpand
 * 
 * @param isrequired
 * 			  是否必输项
 */
function ExcelCompHeader(keyName, showName, width, dataType, sortable,
		isHidden, columEditable, defaultValue, columBgColor, textAlign,
		textColor, isFixedHeader, renderType, editorType, topHeader,
		groupHeader, isGroupHeader, isSumCol,isAutoExpand,isrequired) {
	// TODO 简化

	this.width = getInteger(width, ExcelComp.COlUMWIDTH_DEFAULT);
	if (this.width < ExcelComp.COlUMWIDTH_DEFAULT)
		this.width = 10;

	this.children = null;
	this.keyName = keyName;
	this.showName = getString(showName, "");
	// 此列是否默认隐藏
	this.isHidden = getBoolean(isHidden, false);
	this.parent = null;
	// // 初始是否是固定表头(默认为动态表头)
	// this.isFixedHeader = getBoolean(isFixedHeader, false);
	// 此列的数据类型
	this.dataType = getString(dataType, DataType.STRING);
	// 此列是否可排序
	this.sortable = getBoolean(sortable, true);
	// cell中元素的对齐方式
	this.textAlign = getString(textAlign, "left");
	this.columBgColor = getInteger(columBgColor, 0);
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
	// // 该表头是否自动扩展
	// this.isAutoExpand = getBoolean(isAutoExpand, false);
	// 是否分组显示,如果分组显示,相同的一组值将只会显示第一个值
	this.isGroupBy = false;
	
	this.required = getBoolean(isrequired, false);
	// 标示此header是否是多表头中的header
	this.isGroupHeader = getBoolean(isGroupHeader, false);
	if (this.isGroupHeader) {
		// 多表头最顶层表头
		this.topHeader = topHeader;
		if (this.topHeader == null || this.topHeader == "")
			this.allChildrenHeader = new Array();
		else
			this.topHeader.allChildrenHeader.push(this);

		if (groupHeader != null && groupHeader != "")
			groupHeader.addChildHeader(this);
	}	
};

/**
 * 设置显示状态
 */
ExcelCompHeader.prototype.setShowState = function(state) {
	this.showState = state;
};

/**
 * 增加子header,多表头情况的处理
 */
ExcelCompHeader.prototype.addChildHeader = function(header) {
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
ExcelCompHeader.prototype.getHeadersByLevel = function(level) {
	var headers = this.getAllChildrenHeaderByLevel(level);
	return headers;
};

/**
 * 得到多表头列的每一层的最左边的header(用于改变边界style)
 * 
 * @return headers数组
 * @private
 */
ExcelCompHeader.prototype.getAllLeftHeaders = function() {
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
ExcelCompHeader.prototype.getVisibleHeadersByLevel = function(level) {
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
ExcelCompHeader.prototype.getAllChildrenHeaderByLevel = function(level) {
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
ExcelCompHeader.prototype.getDepth = function() {
	return 1 + this.getMaxDepth(this);
};

/**
 * 递归得到表头最大深度
 * 
 * @private
 */
ExcelCompHeader.prototype.getMaxDepth = function() {
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
ExcelCompHeader.prototype.getHeaderLevel = function() {
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
ExcelCompHeader.prototype.getColspan = function() {
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
ExcelCompHeader.prototype.getBasicHeaders = function() {
	var basicHeaders = new Array();
	if (this.children == null) {
		basicHeaders.push(this);
	} else {
		// header的孩子层数
		var level = this.getHeaderChildrenLevel();
		// 得到从左到右的按顺序的basicHeaders
		for (var i = level; i >= 0; i--) {
			var headers = this.getHeadersByLevel(i);
			for (var j = 0; j < headers.length; j++) {
				if (headers[j].children == null)
					basicHeaders.push(headers[j]);
			}
		}
	}
	return basicHeaders;
};

/**
 * 得到任意给定header的所有下挂的basicHeaders
 * 
 * @private
 */
ExcelCompHeader.prototype.getBasicHeadersBySpecify = function() {
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
ExcelCompHeader.prototype.getDepthestHeadersNum = function() {
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
ExcelCompHeader.prototype.getRowspan = function(totalDepth) {
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
ExcelCompHeader.prototype.getHeaderChildrenLevel = function() {
	return this.getMaxDepth();
};

/**
 * 如果header是符点数，则调用此方法设置最小值
 */
ExcelCompHeader.prototype.setFloatMinValue = function(minValue) {
	if (!isNaN(parseFloat(minValue))) {
		this.floatMinValue = parseFloat(minValue);
	} else
		this.floatMinValue = null;
};

/**
 * 如果header是浮点数，则调用此方法设置最大值
 */
ExcelCompHeader.prototype.setFloatMaxValue = function(maxValue) {
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
ExcelCompHeader.prototype.setIntegerMinValue = function(minValue) {
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
ExcelCompHeader.prototype.setIntegerMaxValue = function(maxValue) {
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
ExcelCompHeader.prototype.setPrecision = function(precision) {
	this.precision = parseInt(precision);
};

/**
 * 如果header类型是stringtext，则调用此方法设置最大长度
 * 
 * @param{int} maxLength 最大字符允许长度
 */
ExcelCompHeader.prototype.setMaxLength = function(maxLength) {
	this.maxLength = parseInt(maxLength);
};

/**
 * 设置此列是否必须填写，设置为true会改变表头的颜色
 * 
 * @param isRequired
 *            此列是否为必输项
 */
ExcelCompHeader.prototype.setRequired = function(isRequired) {
	this.required = getBoolean(isRequired, false);
};

/**
 * 参照类型设置nodeinfo
 */
ExcelCompHeader.prototype.setNodeInfo = function(nodeInfo) {
	this.nodeInfo = nodeInfo;
};

/**
 * 设置checkbox的真实值和显示值
 */
ExcelCompHeader.prototype.setValuePair = function(valuePair) {
	this.valuePair = valuePair;
};

/**
 * 如果header类型是combox，则调用此方法把showImgOnly的值传入，来标明此列 的combox是什么类型的combox
 * 
 * @param
 */
ExcelCompHeader.prototype.setShowImgOnly = function(showImgOnly) {
	this.showImgOnly = showImgOnly;
};

/**
 * 如果header类型是combox，则调用此方法设置combox的ComboData
 */
ExcelCompHeader.prototype.setHeaderComboBoxComboData = function(comboData) {
	this.comboData = comboData;
};

/**
 * Excel配置对象
 */
function ExcelConfig() {

};

/**
 * Excel行定义
 * 
 * @class ExcelComp行定义，这个row是对dataset row的适配，根据已经建立好的
 *        匹配规则，在执行期调用row.getCellValue(0)会根据匹配规则调用dataset相应行的getCell(i)来真正得到数据。
 * @version NC6.0
 */
ExcelCompRow = Array;

/**
 * 设置此行实际的数据
 * 
 * @param row
 *            一维数组或者dsRow
 */
ExcelCompRow.prototype.setRowData = function(row) {
	this.rowData = row;
};

/**
 * 得到此行实际的数据
 * 
 * @return 一维数组或者dsRow
 */
ExcelCompRow.prototype.getRowData = function() {
	return this.rowData;
};

/**
 * 设定ExcelCompRow和DsRow列的对应关系
 */
ExcelCompRow.prototype.setBindRelation = function(relation) {
	this.relation = relation;
};

/**
 * 根据指定的excel中的index返回dataset中实际列位置的值
 */
ExcelCompRow.prototype.getCellValue = function(index) {
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
 * @param basicHeaderIndex 基本表头的下标
 *            列号
 */
ExcelCompRow.prototype.setCellValue = function(basicHeaderIndex, value) {
	// dataset数据(根据帮定关系返回真实数据)
	if (this.relation != null) {
		if (basicHeaderIndex >= this.relation.length || basicHeaderIndex < 0)
			showErrorDialog("Index out of bounds exception!");
		else {
			// 得到实际的dataSet列号
			var relPosi = this.relation[basicHeaderIndex];
			this.rowData.setCellValue(relPosi, value);
		}
	}
};

/** --------------------------------------------------------------------------------------------------------------------------------------- */

/**
 * Excel模型（数组类型）
 * 
 * @class ExcelCompModel定义 ExcelCompModel是excel的数据模型，此模型独立于excel。
 *        model适配dataset，无需了解dataset的实现细节，对dataset是
 *        透明化的，所有的数据插入，更新,修改均是对ExcelCompModel进行操作。
 * @author guoweic
 * @version NC6.0
 */
ExcelCompModel = function(){

};

/**
 * 设置headers
 */
ExcelCompModel.prototype.setHeaders = function(headers) {
	this.headers = headers;
};

/**
 * 得到model中的headers数据
 */
ExcelCompModel.prototype.getHeaders = function() {
	return this.headers;
};

/**
 * 得到指定数据行
 */
ExcelCompModel.prototype.getRow = function(index) {
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
ExcelCompModel.prototype.getRowIndexById = function(id) {
	for (var i = 0, n = this.rows.length; i < n; i++) {
		if (this.rows[i].rowData.rowId == id)
			return i;
	}
	return -1;
};

/**
 * 得到excel指定行列的cell值
 * 
 * @param rowIndex
 *            行索引
 * @param colIndex
 *            列索引
 */
ExcelCompModel.prototype.getCellValueByIndex = function(rowIndex, colIndex) {
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
 * @return{ExcelCompRow[]} ExcelCompRow的数组,若没有任何数据返回null
 */
ExcelCompModel.prototype.getRows = function() {
	if (this.rows == null || this.rows.length == 0)
		return null;
	return this.rows;
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
ExcelCompModel.prototype.setValueAt = function(rowIndex, colIndex, newValue) {
	if (this.dataset == null)
		showErrorDialog("dataset is null!");
	else {
		var dsRow = this.rows[rowIndex].rowData;
		var oldValue = dsRow.getCellValue(this.bindRelation[colIndex]);
		if (oldValue != newValue)
			this.dataset.setValueAt(this.dataset.getRowIndex(dsRow),
					this.bindRelation[colIndex], newValue);
	}
};


/**
 * 对model中的excelrow进行排序，如果没有传入sortFuncs数组，则按照每列的数据类型调用默认的排序函数
 * 如果没有传入sortDirecs数组则默认按照升序排序
 * 
 * @param sortHeaders{Array} 要排序的表头
 * @param sortFuncs{Array} 每一列排序的排序算法(指定的排序算法都按升序)
 * @param ascending{Array} 每一列排序的方向(升序-1, 降序1)
 */
ExcelCompModel.prototype.sortable = function(sortHeaders, sortFuncs, ascendings) {
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
		for ( var i = 0, count = sortHeaders.length; i < count; i++) {
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
		for ( var i = 0, count = ascendings.length; i < count; i++)
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
		for ( var i = 0, count = sortHeaders.length; i < count; i++) {
			sortFuncs[i].index = this.basicHeaders.indexOf(sortHeaders[i]);
			sortFuncs[i].ascending = ascendings[i];
			this.rows.sort(sortFuncs[i]);
		}
	}
	
	this.owner.paintRowsForSort();
};

/**
 * 默认排序 renxh
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
 * 按整数列排序行(升序) renxh
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
 * 该参数是为了前后台分页准备的，initUIRows时需要去获取ds相应的行初始化excel rows
 * 
 * @private
 */
ExcelCompModel.prototype.initUIRows = function(startIndex, count) {
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
			// 将dataset的rows放入excel的UI rows中
			var bindRelation = this.bindRelation;
			for (var i = 0, count = dsRows.length; i < count; i++) {
				// 对dataset中的行进行适配包装(内部使用尽量采用属性设置,避免方法调用)
				var row = new ExcelCompRow();
				row.rowData = dsRows[i];
				row.relation = bindRelation;
				this.rows[i] = row;
			}
		} else
			this.rows = new Array;
	}
};

/**
 * 得到dataset row在excel上的rowIndex
 * 
 * @param dsRow
 *            dateset行数据
 * @return 没有找到返回-1
 */
ExcelCompModel.prototype.getUIRowIndex = function(dsRow) {
	var rows = this.rows;
	for (var i = 0, count = rows.length; i < count; i++) {
		if (rows[i].rowData.rowId == dsRow.rowId)
			return i;
	}
	return -1;
};

/**
 * 计算合计列的值,如果excelColIndex为null,dsColIndex也为null,则计算所有合计列的值
 * 
 * @param excelColIndex
 *            excel中列索引
 * @param dsColName
 *            ds列名
 * @private
 */
ExcelCompModel.prototype.setSumColValue = function(excelColIndex, dsColName) {
	var textAlign = "center";
	if (excelColIndex == null && dsColName == null) {
		for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
			var header = this.basicHeaders[i];
			if (header.isHidden == false && header.isSumCol) {
				var sum = this.dataset.totalSum([header.keyName], null, null,
						null);
				var sumCells = this.owner.dynSumRowDataDiv.childNodes;
				// 格式化结果数据
				sum = this.owner.parseData(header, sum);
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
		if (this.basicHeaders[excelColIndex].isSumCol) {
			var header = this.basicHeaders[excelColIndex];
			var sum = this.dataset.totalSum([dsColName], null, null, null);
			var sumCells = this.owner.dynSumRowDataDiv.childNodes;
			// 格式化结果数据
			sum = this.owner.parseData(header, sum);
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
 *            为ExcelHeader的实例
 */
ExcelCompModel.prototype.addHeader = function(header) {
	if (this.headers == null)
		this.headers = new Array();
	this.headers.push(header);
};

/**
 * 设置model中的数据 不通过dataSet设置数据时使用
 */
ExcelCompModel.prototype.setRows = function(rows) {
	this.initBasicHeaders();
	this.rows = rows;
};

/**
 * 设置dataSet，此model适配dataset 注意：setDataSet之前必须保证this.headers已经初始化
 */
ExcelCompModel.prototype.setDataSet = function(dataset) {
	if (this.headers == null) {
		showErrorDialog("You must init headers before setDataSet!");
		return;
	}
	// 此model所适配的dataset
	this.dataset = dataset;
	// 解析出excel列和dataSet的绑定关系
	this.bindRelation = new Array();
	// 初始化basicHeaders
	this.initBasicHeaders();
	for (var i = 0, count = this.basicHeaders.length; i < count; i++)
		this.bindRelation.push(dataset.nameToIndex(this.basicHeaders[i].keyName));
	dataset.bindComponent(this);

	// 将model的rows置为null,需要重新取新数据
	this.rows = null;
	this.initUIRows();
};

/**
 * 根据headers初始化basicHeaders
 * 
 * @private
 */
ExcelCompModel.prototype.initBasicHeaders = function() {
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
ExcelCompModel.prototype.getBasicHeaders = function() {
	return this.basicHeaders;
};

/**
 * 得到模型中数据行数，如果没有数据返回0
 */
ExcelCompModel.prototype.getRowsCount = function() {
	if (this.dataset == null)
		return 0;
	else
		return this.rows == null ? 0 : this.rows.length;
};

/**
 * 得到新增行数据
 * 
 * @return rows ExcelRow数组
 */
ExcelCompModel.prototype.getNewAddedRows = function() {
	if (this.dataset != null) {
		var newAdded = this.dataset.getNewAddedRows();
		if (newAdded != null && newAdded.length > 0) {
			var rows = new Array(newAdded.length);
			for (var i = 0, count = newAdded.length; i < count; i++) {
				// 对dataset中的行进行适配包装
				var row = new ExcelCompRow();
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
ExcelCompModel.prototype.getUpdatedRows = function() {
	if (this.dataset != null) {
		var updateRows = this.dataset.getUpdatedRows();
		if (updateRows != null && updateRows.length > 0) {
			var rows = new Array(updateRows.length);
			for (var i = 0, count = updateRows.length; i < count; i++) {
				// 对dataset中的行进行适配包装
				var row = new ExcelCompRow();
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
ExcelCompModel.prototype.deleteRow = function(index) {
	if (this.dataset != null)
		this.dataset.deleteRows([index]);
};

/**
 * 删除model中的多行
 * 
 * @param indice
 *            删除行索引数组
 */
ExcelCompModel.prototype.deleteRows = function(indice) {
	if (this.dataset != null)
		this.dataset.deleteRows(indice);
};

/**
 * 得到model中删除的行
 */
ExcelCompModel.prototype.getDeletedRows = function() {
	if (this.dataset != null)
		return this.dataset.getDeletedRows();
};

/**
 * 得到选中行
 */
ExcelCompModel.prototype.getSelectedRows = function() {
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
				var row = new ExcelCompRow();
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
ExcelCompModel.prototype.isRowSelected = function(index) {
	var selectedRowsIndice = this.owner.selectedRowIndice;
	if (selectedRowsIndice != null && selectedRowsIndice.length > 0) {
		for (var i = 0; i < selectedRowsIndice.length; i++) {
			if (index == selectedRowsIndice[i])
				return true;
		}
	}
	return false;
};

/**
 * 设置行选中
 * 
 * @param index
 *            UI上的选中行索引
 */
ExcelCompModel.prototype.setRowSelected = function(index) {
	if (this.dataset != null) {
		var excelRow = this.getRow(index);
		if (excelRow != null) {
			var realIndex = this.dataset.getRowIndex(excelRow.rowData);
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
ExcelCompModel.prototype.addRowSelected = function(indices) {
	if (this.dataset != null) {
		if (indices instanceof Array) {
			var realIndices = new Array;
			for (var i = 0; i < indices.length; i++) {
				var index = indices[i];
				var excelRow = this.getRow(index);
				var realIndex = this.dataset.getRowIndex(excelRow.rowData);
				realIndices.push(realIndex);
			}
			this.dataset.addRowSelected(realIndices);
		} else {
			var excelRow = this.getRow(indices);
			var realIndex = this.dataset.getRowIndex(excelRow.rowData);
			this.dataset.addRowSelected(realIndex);
		}
	}
};

/**
 * 设置行反选
 */
ExcelCompModel.prototype.setRowUnSelected = function(index) {
	if (this.dataset != null) {
		var excelRow = this.getRow(index);
		var realIndex = this.dataset.getRowIndex(excelRow.rowData);
		this.dataset.setRowUnSelected(realIndex);
	}
};

/**
 * 在model指定位置插入数据行。model插入数据后会通知excel控件。
 * 
 * @param row
 *            所需插入行的数据
 * @param index
 *            当前插入的位置.此位置将被参数的行代替,此行后的所有行将移动.
 */
ExcelCompModel.prototype.insertRow = function(row, index) {
	if (this.dataset != null) {
		var newRow = new ExcelCompRow();
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
 * 在model最后插入数据行。model插入数据后会通知excel控件。
 * 
 * @param row
 *            所需插入行的数据
 */
ExcelCompModel.prototype.addRow = function(row) {
	var ds = this.dataset;
	if (this.basicHeaders == null || this.basicHeaders.length == 0) {
		showErrorDialog("basicHeaders为null!");
		return;
	}
	var headers = this.basicHeaders;
	// 调用dataset的方法插入数据
	if (this.dataset != null) {
		var newRow = new ExcelCompRow();
		if (row == null) {
			// 在dataset中插入新行
			var dsRow = this.dataset.addEmptyRow();
			// 用ExcelCompRow包装dsRow返回(默认值的设置已由webdataset完成)
			newRow.rowData = dsRow;
			newRow.relation = this.bindRelation;
		} else {
			if (!ExcelCompRow.prototype.isPrototypeOf(row)) {
				showErrorDialog("the parameter 'row' must be the instance of ExcelCompRow");
				return;
			}
			var dsRow = new DatasetRow();
			for (var i = 0, count = headers.length; i < count; i++)
				dsRow.setCellValue(this.bindRelation[i], row.getCellValue(i));
			newRow.rowData = dsRow;
			newRow.relation = this.bindRelation;
			this.dataset.addRow(dsRow);
		}
		
		return newRow;
	}
};
/**
 * 生成新的空行
 */
ExcelCompModel.prototype.getEmptyRow = function(){
	var ds = this.dataset;
	if (this.basicHeaders == null || this.basicHeaders.length == 0) {
		showErrorDialog("basicHeaders为null!");
		return;
	}
	var headers = this.basicHeaders;
	// 调用dataset的方法插入数据
	if (this.dataset != null) {
		var newRow = new ExcelCompRow();
		
		// 在dataset中插入新行
		var dsRow = this.dataset.getEmptyRow();
		// 用ExcelCompRow包装dsRow返回(默认值的设置已由webdataset完成)
		newRow.rowData = dsRow;
		newRow.relation = this.bindRelation;
		
		return newRow;
	}
}
/**
 * 设置excel的编辑属性
 * 
 * @param isEditable
 *            设置excel可否编辑
 */
ExcelCompModel.prototype.setEditable = function(isEditable) {
	this.owner.setEditable(isEditable);
};

/**
 * 销毁model
 */
ExcelCompModel.prototype.destroySelf = function() {
	this.owner = null;
};

/**
 * 获取选中所有行索引
 */
ExcelCompModel.prototype.getSelectedIndices = function() {
	if (this.owner != null)
		return this.owner.selectedRowIndice;
};

/**
 * 获取选中行索引
 */
ExcelCompModel.prototype.getSelectedIndex = function() {
	var indices = this.getSelectedIndices();
	if (indices == null || indices.length == 0)
		return -1;
	return indices[0];
};

ExcelCompModel.prototype.onModelChanged = function(event) {	
	var excel = this.owner;	
	if (!excel)
		return;
	
	excel.unableEvents();
	// 行选中时
	if (RowSelectEvent.prototype.isPrototypeOf(event)) {
	}
	// 行选中撤销事件
	else if (RowUnSelectEvent.prototype.isPrototypeOf(event)) {
	}
	// cell数据改变时
	else if (DataChangeEvent.prototype.isPrototypeOf(event)) {
		var row = event.cellRowIndex + excel.headerHeight + 1;
		var colIndex = event.cellColIndex;
		var col = excel.getExcelColIndexByDsColIndex(colIndex);
		var value = event.currentValue;
		if (row != null && col != null)
		{
			var range = excel.officeControl.GetRange(excel.activesheet,row, col);;
			excel.setCellValue(range, value,row,col);
		}
	}
	// 违反校验规则的事件
	else if (DataCheckEvent.prototype.isPrototypeOf(event)) {

	}
	// 整页数据更新
	else if (PageChangeEvent.prototype.isPrototypeOf(event)) {
		// TODO 清除原有数据

		excel.paintRows();
	}
	// 插入新数据行
	else if (RowInsertEvent.prototype.isPrototypeOf(event)) {
		excel.paintRows();
	}
	// 删除行
	else if (RowDeleteEvent.prototype.isPrototypeOf(event)) {
		excel.paintRows();
	} else if (StateClearEvent.prototype.isPrototypeOf(event)) {
	}
	excel.enableEvents();
};

/** --------------------------------------------------------------------------------------------------------------------------------------- */

/**
 * Excel行定义
 * 
 * @class ExcelComp行定义，这个row是对dataset row的适配，根据已经建立好的
 *        匹配规则，在执行期调用row.getCellValue(0)会根据匹配规则调用dataset相应行的getCell(i)来真正得到数据。
 * @version NC5.5
 * @since NC5.02
 */
ExcelCompRow = Array;

/**
 * 设置此行实际的数据
 * 
 * @param row
 *            一维数组或者dsRow
 */
ExcelCompRow.prototype.setRowData = function(row) {
	this.rowData = row;
};

/**
 * 得到此行实际的数据
 * 
 * @return 一维数组或者dsRow
 */
ExcelCompRow.prototype.getRowData = function() {
	return this.rowData;
};

/**
 * 设定ExcelCompRow和DsRow列的对应关系
 */
ExcelCompRow.prototype.setBindRelation = function(relation) {
	this.relation = relation;
};

/**
 * 根据指定的excel中的index返回dataset中实际列位置的值
 */
ExcelCompRow.prototype.getCellValue = function(index) {
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
ExcelCompRow.prototype.setCellValue = function(index, value) {
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
ExcelComp.prototype.parseData = function(header, data) {
	if (header.dataType == DataType.BOOLEAN) {
		var formater;
		if ((formater = FormaterMap.get(header.keyName)) == null) {
			formater = new BooleanFormater();
			FormaterMap.put(header.keyName, formater);
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
		if ((formater = FormaterMap.get(header.keyName)) == null) {
			formater = new DateFormater();
			FormaterMap.put(header.keyName, formater);
		}
		return formater.format(data);
	} else if (header.dataType == DataType.INTEGER) {
		var formater;
		if ((formater = FormaterMap.get(header.keyName)) == null) {
			formater = new IntegerFormater(header.integerMinValue,
					header.integerMaxValue);
			FormaterMap.put(header.keyName, formater);
		}
		return formater.format(data);
	} else if (header.dataType == DataType.DECIMAL
			|| header.dataType == DataType.UFDOUBLE
			|| header.dataType == DataType.dOUBLE) {
		var formater;
		if ((formater = FormaterMap.get(header.keyName)) == null) {
			formater = new DicimalFormater(header.precision,
					header.floatMinValue, header.floatMaxValue);
			FormaterMap.put(header.keyName, formater);
		}
		// 如果decimal精度变化则重新设置formatter精度
		if (formater.precision != header.precision)
			formater.precision = header.precision;
		return formater.format(data);
	} else {
		var formater;
		if ((formater = FormaterMap.get(header.keyName)) == null) {
			formater = new Formater();
			FormaterMap.put(header.keyName, formater);
		}
		return formater.format(data);
	}
};
/**
 * VBA color,hex number color ,rgb color ,convert
 * @param {} red
 * @param {} green
 * @param {} blue
 */
function VBAColor (red,green,blue){
	this.red = red;
	this.green = green;
	this.blue = blue;
};
VBAColor.prototype.getNumber=function(){
	var number = 0;
	number = this.red;
	number += (this.green << 8);
	number += (this.blue << 16);
	return number ;
}
VBAColor.prototype.setnumber=function(number){
	var n =  number & 0xFFFFFF;
	this.red = n & 0xFF;
	this.green = (n & 0xFF00) >> 8;
	this.blue = (n&0xFF0000) >> 16;	
}
