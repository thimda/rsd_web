/**
 * 统计图表控件（基于FusionCharts）
 * 
 * @fileoverview 
 * 
 * @auther
 * @version NC6.0
 *  
 */
var GALLERY_RENDERER = "flash";
var tmpChart = new FusionCharts("Column2D.swf", "tmpChartId", "560", "400", "0", "0");
var NO_FLASH = tmpChart.options.renderer=="javascript";
tmpFlash = null;
if(NO_FLASH || /GALLERY_RENDERER=javascript/i.test(document.cookie) )
{
	GALLERY_RENDERER = 'javascript';
}
FusionCharts.setCurrentRenderer(GALLERY_RENDERER);

ChartComp.prototype = new BaseComponent;
ChartComp.prototype.componentType = "CHART";

/**
 * 构造方法
 * @class
 */
function ChartComp(parent, name, left, top, width, height, chartconfig, position, className) {
	if (arguments.length == 0)
		return;
	this.base = BaseComponent;
	this.base(name, left, top, width, height);
	
	this.parentOwner = parent;
	this.chartconfig = chartconfig;
	this.position = getString(position, "relative");
	this.className = getString(className, "chart_div");	
	this.create();
	this.isshow  = false;
};

/**
 * 主体创建函数
 */
ChartComp.prototype.create = function() {
	var oThis = this;
	this.Div_gen = $ce("div");
	this.Div_gen.id = this.id + "_div";
	this.Div_gen.style.position = this.position;
	this.Div_gen.style.left = this.left + "px";
	this.Div_gen.style.top = this.top + "px";
	this.Div_gen.style.width = this.width;
	this.Div_gen.style.height = this.height;
	//this.Div_gen.className = this.className;
	//this.Div_gen.style.background = "yellow";
	if (this.parentOwner)
		this.placeIn(this.parentOwner);
};

/**
 * 二级回调函数
 */
ChartComp.prototype.manageSelf = function() {
	this.createChart();
//	this.chart.onclick = function() {
//		oThis.onclick();
//	};
};
ChartComp.prototype.createChart = function(){
	this.chart = FusionCharts(this.id);
	if(!this.chart){
		this.chart = new FusionCharts(window.baseGlobalPath + "/frame/script/chart/fusionchart/swf/" + this.chartconfig.showType + ".swf", this.id, this.Div_gen.offsetWidth, this.Div_gen.offsetHeight, "0", "0");
		this.chart.addEventListener("BeforeLinkedItemOpen",ChartComp_BeforeLinkedItemOpen);
		this.chart.addEventListener("BeforeLinkedItemOpen",ChartComp_Error);
	}
}

ChartComp.prototype.onclick = function(e) {
	var mouseEvent = {
			"obj" : this,
			"event" : e
		};
	this.doEventFunc("onclick", MouseListener.listenerType, mouseEvent);
};

ChartComp.prototype.onModelChanged = function(event) {
	var g = this.owner;
	// 行选中时
	if (RowSelectEvent.prototype.isPrototypeOf(event)) {
	}
	// 行选中撤销事件
	else if (RowUnSelectEvent.prototype.isPrototypeOf(event)) {
	}
	// cell数据改变时
	else if (DataChangeEvent.prototype.isPrototypeOf(event)) {
	}
	// 违反校验规则的事件
	else if (DataCheckEvent.prototype.isPrototypeOf(event)) {
		
	}
	// 整页数据更新
	else if (PageChangeEvent.prototype.isPrototypeOf(event)) {
		this.convertData();
		this.show();
	}
	// 插入新数据行
	else if (RowInsertEvent.prototype.isPrototypeOf(event)) {
		
	}
	// 删除行
	else if (RowDeleteEvent.prototype.isPrototypeOf(event)) {
		
	}
	else if (StateClearEvent.prototype.isPrototypeOf(event)) {
	}
};
/**
 * 行钻取暂用dataset行选中实现
 */
ChartComp.prototype.drillData = function(rowid){
	if(this.chartconfig.captionUrl){
		var js = this.chartconfig.captionFunc + "('" 
			+ this.chartconfig.captionUrl + "','" +  this.id+ "','" 
			+ this.chartconfig.caption +  "') ";
		eval(js);
	}
	else if(this.dataset){
		 var index = this.dataset.getRowIndexById(rowid);
		 this.dataset.setRowSelected(index);
	}
}
ChartComp.prototype.OpenTitle = function(url,chrtid){
	
}
function ChartComp_BeforeLinkedItemOpen(e,args){
	stopAll(e);
}
function FC_Error(event,arg){	
	return false;
}
function ChartComp_Error(event,arg){
	
}
/**
 * 公共钻取函数
 */
function ChartComp_DrillData(WidgetID,CmpID,rowid){
	var widget =  pageUI.getWidget(WidgetID);
	var chartcmp = widget.getComponent(CmpID);
	chartcmp.drillData(rowid);
}
/**
 * 绑定数据集
 */
ChartComp.prototype.setDataset = function(dataset) {
	this.dataset = dataset;
	dataset.bindComponent(this);
};

/**
 * 将dataset数据转换为图表所需数据
 */
ChartComp.prototype.convertData = function() {
	try
	{
		var convertor = new ChartModelConvertor(this.dataset, this.chartconfig,this);
		var chartModel = convertor.convert();
		var rows = this.dataset.getRows();
		if(rows){
			var xml = chartModel.toXml();
			this.setDataXML(xml);
		}
	}
	catch(e){
		Logger.error(e);
	}
	
};

/**
 * 设置XML数据
 */
ChartComp.prototype.setDataXML = function(xml) {
	this.chart.setDataXML(xml);
};

/**
 * 设置XML文件地址
 */
ChartComp.prototype.setDataURL = function(url) {
	this.chart.setDataURL(url);
};

/**
 * 显示控件
 */
ChartComp.prototype.show = function() {
		
		//if(this.isshow)
			//return;
		var rows = this.dataset.getRows();
		if(rows){
			try{
				this.chart.render(this.Div_gen.id);
				//this.isshow = true;
			}
			catch(e){
				
				//Logger.error(e);
			}
		}
	
};

/**
 * 获取对象信息
 */
ChartComp.prototype.getContext = function() {
	var context = new Object;
//	context.javaClass = "nc.uap.lfw.core.comp.ctx.ButtonContext";
	context.c = "ChartContext";
	context.id = this.id;
	context.enabled = !this.disabled;
	return context;
};

/**
 * 设置对象信息
 */
ChartComp.prototype.setContext = function(context) {
	if (context.enabled == this.disabled)
		this.setActive(context.enabled);
	
};


// 单系列图形
ChartConfig.SINGLE_SERIES = "single-series";
// 多系列图形
ChartConfig.MULTI_SERIES = "multi-series";
//仪表盘
ChartConfig.Angular_SERIES = "angular";
//双Y轴类型
ChartConfig.Dual_SERIES = "dual";

//仪表盘
//名称列
ChartConfig.Angular_Column_Type = "type"; 
//开始值列
ChartConfig.Angular_Column_Start = "start";
//结束值列
ChartConfig.Angular_Column_End = "end";
//颜色列
ChartConfig.Angular_Column_Color = "color";
//颜色区间
ChartConfig.Angular_Type_Range = "range";
//指针
ChartConfig.Angular_Type_Dial = "dial";

/**
 * 
 */
function ChartConfig(showType, seriesType, caption, numberPrefix, groupColumn, groupName, seriesColumns, seriesNames, xAxisName, yAxisName,datacolumn,isdrill) {
	// 显示图像类型
	this.showType = showType;
	// 图表显示系列类型（ChartConfig.SINGLE_SERIES、ChartConfig.MULTI_SERIES）
	this.seriesType = seriesType;
	// 图表标题
	this.caption = caption;
	// 统计结果数字前缀
	this.numberPrefix = numberPrefix;
	// 分组列
	this.groupColumn = groupColumn;
	// 分组列图表显示名称
	this.groupName = groupName;
	// 统计列
	if (seriesColumns != null)
		this.seriesColumns = seriesColumns.split(",");
	// 统计列图表显示名称
	if (seriesNames != null)
		this.seriesNames = seriesNames.split(",");
	// 横轴显示文字
	this.xAxisName = xAxisName;
	// 纵轴显示文字
	this.yAxisName = yAxisName;
	//数据列
	this.dataColumn =  datacolumn;
	//是否钻取
	this.isdrill = isdrill;
	
	this.sizescale = null;  
	
	//仪表盘属性
	//仪表盘外半径
	this.outradius = null;
	//仪表盘内半径
	this.innerradius = null;
	//指针半径
	this.dialradius = null;
	//最大值
	this.maxvalue = null;
	//最小值
	this.minvalue = null;
	//将控件的宽度的20分之一
	this.saclevalue = null;
	//位置调节
	this.gaugeOriginX = null;
	this.gaugeOriginY = null;
	this.dialValue = null;
	this.dialColor = null;
	this.datasetrender = null;
	
	this.seriesRenders = null;
	this.seriesParentYAxis = null;
	
	this.SYAxisName = null;
	//标题字体
	this.captionFont =null;
	//X轴字体
	this.xAxisFont = null;
	//X轴刻度
	this.xLabelFont = null;
	//y轴字体
	this.yAxisFont = null;
	//y轴刻度
	this.yLabelFont = null;
	//图例
	this.legendFont = null;
	//bgcolor
	this.bgcolor = null;
	//标题连接
	this.captionUrl = null; 
	this.captionFunc = null;
	this.clickUrl = null;
	this.colorlist = new Array("45B3D4","FFD000","A6D04B",
		"E086DE","F99D61","6484FD","BF8403","4BD09C","FA91BB",
		"D4B3FD","FDD493","F36062","056C8B","CFAA02","3DA01E",
		"C52E30","874CE0","FE8900","2143C4","117C73");
};
/**
 * 设置render
 * @param {} render
 */
ChartConfig.prototype.SetRender = function(render){
	this.datasetrender = render;
}
/**
 * 增加序列级render
 */
ChartConfig.prototype.addSeriesRender= function(seriesName,renderType){
	if(this.seriesRenders == null){
		this.seriesRenders = new HashMap();		
	}
	this.seriesRenders.put(seriesName,renderType);
}

/**
 * 获取序列级render
 * @param {} seriesName
 * @return {}
 */
ChartConfig.prototype.getSeriesRender = function(seriesName){
	var value = null
	if(this.seriesRenders)
		if(this.seriesRenders.containsKey(seriesName))
			value  = this.seriesRenders.get(seriesName);
	return value;
}
/**
 * 增加序列级所属Y轴
 */
ChartConfig.prototype.addSeriesParentYAxis= function(seriesName,parentYAxis){
	if(this.seriesParentYAxis == null){
		this.seriesParentYAxis = new HashMap();		
	}
	this.seriesParentYAxis.put(seriesName,parentYAxis);
}

/**
 * 获取序列级所属Y轴
 * @param {} seriesName
 * @return {}
 */
ChartConfig.prototype.getSeriesParentYAxis = function(seriesName){
	var value = null
	if(this.seriesParentYAxis)
		if(this.seriesParentYAxis.containsKey(seriesName))
			value  = this.seriesParentYAxis.get(seriesName);
	return value;
}
//
/**
 * 设置SY轴名称
 * @param {} sYAxisName
 */
ChartConfig.prototype.setSYAxisName = function(sYAxisName){
	this.SYAxisName = sYAxisName;
}
//标题字体
ChartConfig.prototype.setCaptionFont = function(font){
	this.captionFont = font
}
//X轴字体
ChartConfig.prototype.setxAxisFont = function(font){
	this.xAxisFont = font
}
//X轴刻度
ChartConfig.prototype.setxLabelFont = function(font){
	this.xLabelFont = font
}
//Y轴字体
ChartConfig.prototype.setyAxisFont = function(font){
	this.yAxisFont = font
}
//y轴刻度
ChartConfig.prototype.setyLabelFont = function(font){
	this.yLabelFont = font
}
//图例
ChartConfig.prototype.setylegendFont = function(font){
	this.legendFont = font
}
//bgcolor
ChartConfig.prototype.setbgcolor = function(bgcolor){
	this.bgcolor = bgcolor
}
ChartConfig.prototype.setCaptionUrl = function(captionurl){
	this.captionUrl = captionurl
}
ChartConfig.prototype.setCaptionFunc = function(captionFunc){
	this.captionFunc = captionFunc
}
