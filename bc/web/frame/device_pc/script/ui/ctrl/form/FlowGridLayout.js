/**
 * 
 * 传入组件的宽度必须为FlowGridLayout.BASEWIDTH的整数倍 模拟的一个布局管理器,基类为panelcomp
 * 
 * @auther gd, guoweic
 * @version NC6.0
 * 
 * 1.修正显示混乱问题（以适应多浏览器）. guoweic <b>modified</b>
 * 
 */

/**
 * @private
 */
FlowGridLayout.prototype.className = "FlowGridLayout";
FlowGridLayout.prototype = new PanelComp;

// 常量设置
FlowGridLayout.BASEWIDTH = 120;
FlowGridLayout.COLSPACE = 21;
FlowGridLayout.ROWSPACE = 6;
FlowGridLayout.COMPHEIGHT = 22;

/**
 * 表单流式布局构造函数
 * 
 * @class 表单流式布局
 */
function FlowGridLayout(parent, name, left, top, width, height, position,
		scroll, compDefaultWidth, marginTop) {
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
	// 记录当前绘制时画布大小
	this.nowPaintWidth = 0;
	this.nowPaintHeight = 0;
	this.marginTop = getInteger(marginTop, 6);
	// 强制刷新,如果强制刷新,每次paint前要设置该值为true
	this.forceRepaint = false;
	if (FlowGridLayout.syncArray == null) {
		FlowGridLayout.syncArray = new Array;
	}
	FlowGridLayout.syncArray.push(this);
	if (FlowGridLayout.wordArray == null)
		FlowGridLayout.wordArray = new Array;

	FlowGridLayout.COMPHEIGHT = getCssHeight("text_form_div_COMPHEIGHT");
	
	this.labelPosition = "left";
};

/**
 * 创建组件div
 * 
 * @private
 */
FlowGridLayout.prototype.create = function() { 
	var oThis = this;
	this.Div_gen = document.createElement("DIV");
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	if (this.width.toString().indexOf("%") == -1)
		this.Div_gen.style.width = this.width + "px";
	else
		this.Div_gen.style.width = this.width;
	if (this.height.toString().indexOf("%") == -1)
		this.Div_gen.style.height = this.height + "px";
	else
		this.Div_gen.style.height = this.height;
	this.Div_gen.style.position = this.position;
	if(this.overflow != null)
		this.Div_gen.style.overflowY = this.overflow;
	this.Div_gen.style.overflowX = "auto";

	this.rt = null;
	// guoweic: modify start 2009-11-25
	/**
	 * this.Div_gen.onresize = function() { if (oThis.rt != null)
	 * clearTimeout(oThis.rt); oThis.rt = setTimeout("resizeLayout('" + oThis.id +
	 * "')", 100); };
	 */
	// guoweic: modify end
	addResizeEvent(this.Div_gen, function() {
		FlowGridLayout.resize(oThis.id);
	});
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

FlowGridLayout.resize = function(id){
	var layout = window.objects[id];
	try{
		layout.paint(true);
	}catch(e){}
};

/**
 * @private
 */
function resizeLayout(compId) {
	var layout = objects[compId];
	layout.paint();
};

/**
 * 调用paint函数进行绘制
 * 
 * @private
 */
FlowGridLayout.prototype.paint = function(isForce) {
	var compsCount = this.comps.length;
	if (compsCount == 0)
		return;

	var offsetWidth = this.Div_gen.offsetWidth;
	// 处于隐藏状态
	if (offsetWidth == 0)
		return;
	// 控制只有width变化时进行绘制,减少浏览器压力
	if (this.forceRepaint == false && (isForce == null || isForce == false)) {
		if (offsetWidth == this.nowPaintWidth)
			return;
	}
	this.nowPaintWidth = offsetWidth;

	// 要隐藏的控件,要显示的控件
	var visibleComps = [], hiddenComps = [], allComps = this.comps;

	for (var i = 0; i < compsCount; i++) {
		if (allComps[i].visible == true) {
			visibleComps.push(allComps[i]);
			allComps[i].show();
		} else {
			hiddenComps.push(allComps[i]);
			var label = allComps[i].attachedLabel;
			if (label != null)
				label.style.display = "none";
			allComps[i].hide();
		}
	}

	// 将comps设置为显示控件,下面只渲染显示控件
	this.comps = visibleComps;

	// 得到组件的高度
	this.compHeight = this.getCompsHeight();

	this.gridPanel();
	this.lastCompTotalWidth = 0;

	for (var i = 0, compCount = this.comps.length; i < compCount; i++) {
		var colNum = this.comps[i].colNum; // 列号
		var rowNum = this.comps[i].rowNum; // 行号
		
		// 得到组件所占用的列数
		var compColNum = this.getColmNum(this.comps[i]);
		// 文字的宽度,组件的宽度
		var labelWidth = FlowGridLayout.wordArray[colNum];

		var compWidth = 0;
		for (var k = colNum, count1 = colNum + compColNum; k < count1; k++)
			compWidth = compWidth + this.realColmWidth[k];

		if(this.labelPosition != 'top'){
			compWidth = compWidth - labelWidth;
		}
		// 新行开始直接往面版上加组件,无需判断.但要获得组件所占的列数
		var usedWidth = 0;
		if (colNum != 0) {
			// 能放下,计算已经被占用的宽度
			for (var j = 0; j < colNum; j++)
				usedWidth += this.realColmWidth[j];
				
			if(this.labelPosition == 'top'){
				usedWidth -= 50 * colNum;
			}
		}
		var top = 0;
		if(this.labelPosition == 'top'){
			top = rowNum * 2 * (FlowGridLayout.COMPHEIGHT) + (rowNum * 10);//10文字和输入框间距
		}else{
			top = this.marginTop + rowNum * (FlowGridLayout.COMPHEIGHT + FlowGridLayout.ROWSPACE);
		}
		var left = 0;
		if(this.labelPosition == 'top'){
			left = usedWidth;
		}else{
			left = usedWidth + FlowGridLayout.COLSPACE - 6;
		}
		if (this.comps[i].attachedLabel != null) {
			this.comps[i].attachedLabel.style.left = left + "px";
			this.comps[i].attachedLabel.style.top = top + "px";
			this.comps[i].attachedLabel.style.display = "block";

			// 调整Label宽度
			this.comps[i].attachedLabel.style.width = FlowGridLayout
					.getLabelDivWidth(labelWidth)
					+ "px";
			var htmlStr = null;
			if (this.comps[i].isRequired)
				htmlStr = "<div style='position:relative;'>"
						+ (this.comps[i].componentType == "CHECKBOX" ? "" : this.comps[i].labelName)
						+ "<span style='color:red;margin-left:3px;margin-right:3px;'>*</span></div>";
			else {
				htmlStr = "<div style='position:relative;margin-right:10px;'>"
						+ (this.comps[i].componentType == "CHECKBOX" ? "" : this.comps[i].labelName)
						+ "</div>";		
			}
			this.comps[i].attachedLabel.innerHTML = htmlStr; 
				
		} else {
			var attachedLabel = this.createLabel(this.comps[i],	usedWidth, top, labelWidth);
			this.comps[i].attachedLabel = attachedLabel;
			this.Div_gen.appendChild(attachedLabel);
		}
		if(this.labelPosition == 'top'){
			this.comps[i].attachedLabel.style.textAlign = "left";
		}

		// 编辑态下的测试
		if (window.editMode) {
			if (this.comps[i].attachedLabel != null) {
//				this.comps[i].params.type = this.comps[i].params.type+"_label";
				new EditableListener(this.comps[i].attachedLabel,this.comps[i].params, EditableListener.COMPOMENT_TYPE);
			}
		}

		var tmpCompHeight = FlowGridLayout.COMPHEIGHT * this.comps[i].rowSpan
				+ FlowGridLayout.ROWSPACE * (this.comps[i].rowSpan - 1);
		// 设置组件的位置
		//如果是textarea，控件占满布局，否则控件大小不变
//		if (this.comps[i].componentType == "TEXTAREA"){
//			this.comps[i].setBounds(compLeft + 5, this.marginTop + rowNum
//					* (FlowGridLayout.COMPHEIGHT + FlowGridLayout.ROWSPACE),
//					compWidth, tmpCompHeight);
//		}
//		else{
		var compLeft = 0;
		if(this.labelPosition == 'top'){
			compLeft = usedWidth;
			top = rowNum * 2 * (FlowGridLayout.COMPHEIGHT) + (rowNum * 10) + FlowGridLayout.COMPHEIGHT;
			compWidth = this.nowPaintWidth;
		}else{
			compLeft = usedWidth + labelWidth + FlowGridLayout.COLSPACE - 6;
			top = this.marginTop + rowNum * (FlowGridLayout.COMPHEIGHT + FlowGridLayout.ROWSPACE);
			if (this.comps[i].basewidth.indexOf("%") != -1)
				compWidth = compWidth * parseFloat(this.comps[i].basewidth) / 100 ;
			else
				compWidth = this.comps[i].basewidth > compWidth ? compWidth : this.comps[i].basewidth;			
		}
		this.comps[i].setBounds(compLeft, top, compWidth, tmpCompHeight);
		
		//this.comps[i].setBounds(compLeft + 5, this.marginTop + rowNum
		//		* (FlowGridLayout.COMPHEIGHT + FlowGridLayout.ROWSPACE),
		//		compWidth, tmpCompHeight);
//		}
				
		// this.comps[i].setBounds(compLeft + 5, this.marginTop + rowNum
		// * (FlowGridLayout.COMPHEIGHT + FlowGridLayout.ROWSPACE),
		// compWidth, tmpCompHeight);
	}
	// wt3.stop();
	// 记录上次创建的labels
	this.oldLabels = this.labels;
	// 将这次显示的labels清空
	this.labels = [];
	this.comps = allComps;
	this.forceRepaint = false;
	this.afterRepaint();
};

/**
 * @private
 */
FlowGridLayout.prototype.afterRepaint = function() {
};

/**
 * 获取高度
 * 
 * @private
 */

FlowGridLayout.prototype.getHeight = function() {
	if (this.comps == null || this.comps.length == 0)
		return 0;
	// var lastComp;
	var comp;
	var maxHeight = 0;
	for (var i = this.comps.length - 1; i >= 0; i--) {
		if (this.comps[i].visible != false) {
			comp = this.comps[i];
			var height = comp.getObjHtml().offsetHeight
					+ comp.getObjHtml().offsetTop + 5;
			if (height > maxHeight)
				maxHeight = height;
		}
	}
	return maxHeight;
};

/**
 * 添加要排版的组件
 * 
 * @private
 */
FlowGridLayout.prototype.add = function(comp) {
	PanelComp.prototype.add.call(this, comp);
	this.comps.push(comp.owner);
};

/**
 * 设置是否强制重新paint
 * 
 * @private
 */
FlowGridLayout.prototype.setForceRepaint = function(isForceRepaint) {
	this.forceRepaint = getBoolean(isForceRepaint, false);
};

/**
 * 得到传入的所有panel组件
 * 
 * @private
 */
FlowGridLayout.prototype.getComps = function() {
	/*
	 * var pnl = new Array(); pnl[0] = new PanelComp(null, "单据编号", "", "",
	 * FlowGridLayout.BASEWIDTH, 20, "", ""); pnl[1] = new PanelComp(null, "部门",
	 * "", "", 2*FlowGridLayout.BASEWIDTH, 20, "", ""); pnl[2] = new
	 * PanelComp(null, "单据状态", "", "", FlowGridLayout.BASEWIDTH, 20, "", "");
	 * pnl[3] = new PanelComp(null, "单据日期", "", "", FlowGridLayout.BASEWIDTH,
	 * 20, "", ""); pnl[4] = new PanelComp(null, "工艺类型", "", "",
	 * FlowGridLayout.BASEWIDTH, 20, "", ""); pnl[5] = new PanelComp(null,
	 * "分摊标准", "", "", FlowGridLayout.BASEWIDTH, 20, "", ""); pnl[6] = new
	 * PanelComp(null, "费用类型", "", "", FlowGridLayout.BASEWIDTH, 20, "", "");
	 * pnl[7] = new PanelComp(null, "材料", "", "", 2*FlowGridLayout.BASEWIDTH,
	 * 20, "", ""); pnl[8] = new PanelComp(null, "总耗量", "", "",
	 * FlowGridLayout.BASEWIDTH, 20, "", ""); pnl[9] = new PanelComp(null,
	 * "总金额", "", "", FlowGridLayout.BASEWIDTH, 20, "", ""); pnl[10] = new
	 * PanelComp(null, "产量合计", "", "", FlowGridLayout.BASEWIDTH, 20, "", "");
	 * pnl[11] = new PanelComp(null, "折合后产量合计", "", "",
	 * FlowGridLayout.BASEWIDTH, 20, "", ""); pnl[12] = new PanelComp(null,
	 * "平均单耗", "", "", FlowGridLayout.BASEWIDTH, 20, "", ""); pnl[13] = new
	 * PanelComp(null, "平均单位成本", "", "", FlowGridLayout.BASEWIDTH, 20, "", "");
	 * pnl[14] = new PanelComp(null, "单据类型", "", "", FlowGridLayout.BASEWIDTH,
	 * 20, "", ""); pnl[15] = new PanelComp(null, "备注", "", "",
	 * 3*FlowGridLayout.BASEWIDTH, 20, "", ""); for(var i = 0; i < 16; i ++) {
	 * pnl[i].create(); } return pnl;
	 */
	return this.comps;
};

/**
 * 根据id获取子项对象
 */
FlowGridLayout.prototype.getCompById = function(id) {
	if (this.comps != null) {
		for (var i = 0, count = this.comps.length; i < count; i++) {
			if (this.comps[i].id == id)
				return this.comps[i];
		}
	}
};
/**
 * 根据id删除子项对象
 */
FlowGridLayout.prototype.removeCompById = function(id) {
	var comp = null;
	if (this.comps != null) {
		var newComps = new Array();
		for (var i = 0, count = this.comps.length; i < count; i++) {
			if (this.comps[i].id == id){
				comp = this.comps[i];
			}else{
				newComps.push(this.comps[i]);
			}
			
		}
		this.comps = newComps;
	}
	return comp;
};



/**
 * 得到组件中最长名字的长度
 * 
 * @private
 */
FlowGridLayout.prototype.getMaxNameWidth = function() {
	var comps = this.comps;
	var maxLength = 0;
	for (var i = 0; i < comps.length; i++) {
		var nowLength = getTextWidth(comps[i].labelName, this.className);
		if (nowLength > maxLength)
			maxLength = nowLength;
	}
	return maxLength;
};

/**
 * 得到指定组件的名字的长度
 * 
 * @private
 */
FlowGridLayout.prototype.getCompNameWidth = function(comp) {
	if (comp.componentType == "CHECKBOX") {
		return 0;
	} else {
		return getTextWidth(comp.labelName, this.className);
	}
};

/**
 * 得到传入组件的高度
 * 
 * @private
 */
FlowGridLayout.prototype.getCompsHeight = function() {
	// TODO
	// var comps = this.getComps();
	// return comps[0].getCompHeight();
	return 22;
};

/**
 * 得到所有组件的宽度 return allWidth数组
 * 
 * @private
 */
FlowGridLayout.prototype.getAllCompsWidth = function(comps) {
	var allWidth = new Array(comps.length);
	for (var i = 0; i < comps.length; i++)
		allWidth[i] = comps[i].getCompWidth();
	return allWidth;
};

/**
 * 得到组件所占的列数
 * 
 * @private
 */
FlowGridLayout.prototype.getColmNum = function(comp) {
	if (comp.colSpan == -1)
		return this.colmNum;
	// var cols = Math.ceil((parseFloat(comp.basewidth) + this.wordsdefault * 12
	// + FlowGridLayout.COLSPACE) / (this.basewidth + this.wordsdefault * 12 +
	// FlowGridLayout.COLSPACE));
		
//	var cols = Math.ceil(comp.basewidth / this.basewidth);
	var cols = parseInt(comp.colSpan);
	return cols > this.colmNum ? this.colmNum : cols;
};

/**
 * 划分面版为网格,得到列数,计算每一条数线的横坐标,组件只能放置在以这些横坐标开始的位置
 * 
 * @private
 */
FlowGridLayout.prototype.gridPanel = function(sNum) {
	// 应划分的列数(但每一列的宽度并不是相等的,需要根据各列中的文字最长宽度加上BASEWIDTH)
	// this.colmNum = Math.floor(this.Div_gen.style.pixelWidth/(this.BASEWIDTH +
	// this.wordsdefault * 12));
	// 这个列数实际上是预估的
	this.basewidth = this.defaultwidth;
	if (sNum)
		this.colmNum = sNum;
	else {
		// 如果有过此布局，则统一使用同一分组列，使外观看起来整齐
		// if (FlowGridLayout.colmNum != null)
		// this.colmNum = FlowGridLayout.colmNum;
		// else
		this.colmNum = Math
				.floor(this.Div_gen.offsetWidth
						/ (this.basewidth + this.wordsdefault * 12 + FlowGridLayout.COLSPACE));
		if (this.colmNum < 1 )
			this.colmNum = 1;
	}
	FlowGridLayout.colmNum = this.colmNum;
	// 实际每一列的宽度
	this.realColmWidth = new Array(this.colmNum);
	// 保存每一列文字的最大宽度
	// TODO guwoeic
	if (!this.colmWordMaxWidth)
		this.colmWordMaxWidth = new Array(this.colmNum);

	// 声明数组：存放对每个列长度有贡献的组件,以次计算各列的实际宽度
	var colm = new Array(this.colmNum);
	for (var i = 0, count = this.colmNum; i < count; i++)
		colm[i] = new Array();

	// 记录列号的
	var temp = 0;
	// 当前计算到的行号
	var rowNum = 0;
	// 通过初次扫描,获得每个组件中的文字对哪个列做贡献,进行记录,然后由此计算各列的实际宽度,取得各列的开始x坐标
	for (var i = 0, count = this.comps.length; i < count; i++) {
		var comp = this.comps[i];
		if (comp.nextLine && temp != 0) {
			temp = 0;
			rowNum++;
		}
		var colSpan = this.getColmNum(comp);
		var rowSpan = comp.rowSpan;

		// 计算摆放位置
		if (colSpan == 1) { // 只占一列
			while (colm[temp] && colm[temp][rowNum] != null) { // 当前位置已被占用
				if (comp.nextLine){//换行显示时，只要位置被占用，就换一行
					temp = 0;
					rowNum++;
				}
				else{
					if (temp < this.colmNum - 1) {
						temp++;
					} else {
						temp = 0;
						rowNum++;
					}
				}
			}
		} else if (colSpan > 1) { // 占多列
			while (FlowGridLayout.checkPosition(colSpan, colm, temp, rowNum) == false) { // 所需位置已被占用
				if (temp + colSpan <= this.colmNum)
					temp++;
				else {
					temp = 0;
					rowNum++;
				}
			}
		}

		// 计算此组件的文字宽度,存储到相应列的数组中
		var labelWidth = this.getCompNameWidth(comp);
		// 添加间距
		if (colm[temp] != null) {
			colm[temp][rowNum] = labelWidth + FlowGridLayout.COLSPACE;
			if (colSpan > 1) { // 所占列数大于1
				for (var j = 1; j < colSpan; j++) {
					colm[temp + j][rowNum] = 0;
				}
			}
			if (rowSpan > 1) { // 所占行数大于1
				for (var j = 1; j < rowSpan; j++) {
					colm[temp][rowNum + j] = labelWidth
							+ FlowGridLayout.COLSPACE;
					if (colSpan > 1) { // 所占列数大于1
						for (var k = 1; k < colSpan; k++) {
							colm[temp + k][rowNum + j] = 0;
						}
					}
				}
			}
			// 设置元素的位置信息
			comp.rowNum = rowNum;
			comp.colNum = temp;

		}

		temp = temp + parseInt(colSpan);
		if (temp > this.colmNum - 1) {
			temp = 0;
			rowNum++;
		}
	}

	// 计算每一列文字的实际最大宽度
	for (var i = 0, count = this.colmNum; i < count; i++) {
		this.colmWordMaxWidth[i] = 0;
		for (var j = 0, count1 = colm[i].length; j < count1; j++) {
			if (colm[i][j] > this.colmWordMaxWidth[i])
				this.colmWordMaxWidth[i] = colm[i][j];
		}
		if (FlowGridLayout.wordArray[i] == null
				|| FlowGridLayout.wordArray[i] < this.colmWordMaxWidth[i]) {
			FlowGridLayout.wordArray[i] = this.colmWordMaxWidth[i];
		}
		this.colmWordMaxWidth[i] = FlowGridLayout.wordArray[i];
	}

	// 去掉多余的列宽度
	if (FlowGridLayout.wordArray.length > this.colmNum) {
		for (var i = FlowGridLayout.wordArray.length - 1; i >= this.colmNum; i--) {
			FlowGridLayout.wordArray.remove(i);
		}
	}

	// 进一步调整，预估可能不正确，需要减一列来调整
	var totalCalc = 0;
	for (var i = 0, count = this.colmNum; i < count; i++) {
		totalCalc += this.colmWordMaxWidth[i];
	}

	var offsetWidth = this.Div_gen.offsetWidth;
	// 出现了滚动条
	if (this.Div_gen.scrollHeight > this.Div_gen.offsetHeight) {
		offsetWidth -= 17;
	}
	var remainSpace = offsetWidth - (this.basewidth + FlowGridLayout.COLSPACE)
			* this.colmNum + FlowGridLayout.COLSPACE - totalCalc;
	if (remainSpace < 0 && this.colmNum > 1) {
		this.gridPanel(this.colmNum - 1);
		return;
	}

	this.basewidth = this.defaultwidth + Math.floor(remainSpace / this.colmNum);

	// 每一列的实际宽度
	for (var i = 0, count = this.colmNum; i < count; i++){
		this.realColmWidth[i] = this.colmWordMaxWidth[i] + this.basewidth;
	}
	// FlowGridLayout.syncAllLayouts();
};

/**
 * 检查元素所需占用的位置是否已被其他元素占用
 * 
 * @private
 */
FlowGridLayout.checkPosition = function(colSpan, colm, colNum, rowNum) {
	for (var i = 0; i < colSpan; i++) {
		try {
			if (colm[colNum + i][rowNum] != null)
				return false;
		} catch (error) {
			return false;
		}
	}
	return true;
};

/**
 * @private
 */
FlowGridLayout.syncAllLayouts = function() {
	var syncArray = FlowGridLayout.syncArray;
	for (var i = 0; i < syncArray.length; i++) {
		syncArray[i].sync();
	}
};

/**
 * @private
 */
FlowGridLayout.prototype.sync = function() {
	var wordArray = FlowGridLayout.wordArray;
	var needSync = false;
	if (this.colmWordMaxWidth == null)
		return;
	for (var i = 0; i < this.colmNum; i++) {
		if (wordArray[i] > this.colmWordMaxWidth[i]) {
			needSync = true;
			break;
		}
	}
	if (needSync) {
		this.paint(true);
	}
};

/**
 * 获取标签的DIV宽度
 * 
 * @private
 */
FlowGridLayout.getLabelDivWidth = function(width) {
	return parseInt(width, 10) - 0;
};

/**
 * 根据给定参数生成label,用div + textNode实现
 * 
 * @param comp
 *            组件,从此组件获得文字描述创建label
 * @param left
 *            label的左坐标值
 * 
 * @private
 */
FlowGridLayout.prototype.createLabel = function(comp, left, top, width) {
	var divLabel = document.createElement("div");
	divLabel.style.textAlign = "right";
	divLabel.style.position = "absolute";
	if (width <= 0)
		width = 20;

	divLabel.style.width = FlowGridLayout.getLabelDivWidth(width) + "px";
	divLabel.style.height = this.compHeight + "px";
	divLabel.style.left = (left + FlowGridLayout.COLSPACE - 6) + "px";
	divLabel.style.top = top + "px";
	divLabel.style.lineHeight = this.compHeight + "px";
	//divLabel.style.paddingTop = "4px";
	//divLabel.style.paddingRight = "4px";

	divLabel.style.textOverflow = "ellipsis";
	divLabel.style.whiteSpace = "nowrap";
	var htmlStr = null;
	if (comp.isRequired)
		htmlStr = "<div style='position:relative;'>"
				+ (comp.componentType == "CHECKBOX" ? "" : comp.labelName)
				+ "<span style='color:red;margin-left:3px;margin-right:3px;'>*</span></div>";
	else {
		htmlStr = "<div style='position:relative;margin-right:10px;'>"
				+ (comp.componentType == "CHECKBOX" ? "" : comp.labelName)
				+ "</div>";
	}
	if (comp.labelColor)
		divLabel.style.color = comp.labelColor;
	// 将文字(label名字)放到label中
	// divLabel.appendChild(document.createTextNode(comp.labelName));
	divLabel.innerHTML = htmlStr;
	// TODO 需放入css中
	// if (comp.isRequired)
	// divLabel.style.color = "#346699";
	// else {
	// if (comp.labelColor)
	// divLabel.style.color = comp.labelColor;
	// }
	// this.labels.push(divLabel);
	return divLabel;
};
