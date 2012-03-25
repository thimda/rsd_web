package nc.uap.lfw.core.comp;

import java.io.Serializable;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;

/**
 * 图表控件的配置对象
 * 
 * @author guoweic
 *
 */
public class ChartConfig implements Serializable {
	/**
	 * 单列条形图二维
	 */
	public static final String  ShowType_Bar_2D = "Bar2D";
	/**
	 * 多序列条形图二维
	 */
	public static final String  ShowType_MSBar_2D = "MSBar2D";
	/**
	 * 多序列条形图三维
	 */
	public static final String  ShowType_MSBar_3D = "MSBar3D";
	/**
	 * 堆积条形图二维
	 */
	public static final String  ShowType_StackedBar_2D = "StackedBar2D";
	/**
	 * 堆积条形图三维
	 */
	public static final String  ShowType_StackedBar_3D = "StackedBar3D";
	
	/**
	 * 单序列柱状图2D
	 */
	public static final String  ShowType_Column_2D = "Column2D";	
	/**
	 * 多列柱状图2D
	 */
	public static final String  ShowType_MSColumn_2D = "MSColumn2D";
	
	/**
	 * 多列柱状图3D
	 */
	public static final String  ShowType_MSColumn_3D = "MSColumn3D";
	
	/**
	 * 单序列饼图2D
	 */
	public static final String  ShowType_Pie_2D = "Pie2D";
	
	/**
	 * 单序列饼图3D
	 */
	public static final String  ShowType_Pie_3D = "Pie3D";
	
	/**
	 * 单序列饼图2D
	 */
	public static final String  ShowType_Doughnut_2D = "Doughnut2D";
	
	/**
	 * 单序列饼图3D
	 */
	public static final String  ShowType_Doughnut_3D = "Doughnut3D";
	
	//
	//Doughnut3D
	/**
	 * 单序列面积图2D
	 */
	public static final String ShowType_Area_2D = "Area2D";
	
	/**
	 * 多序列面积图
	 */
	public static final String ShowType_MSArea_2D = "MSArea";
	/**
	 * 雷达图
	 */
	public static final String ShowType_Radar = "Radar";
	
	/**
	 * 堆积二维图
	 */
	public static final String ShowType_Stacked_2D = "StackedColumn2D";
	
	/**
	 * 堆积三维图
	 */
	public static final String ShowType_Stacked_3D = "StackedColumn3D";
	/**
	 * 面积堆积图
	 */
	public static final String ShowType_StackedArea_2D = "StackedArea2D";
	/**
	 * 三维线、列、面积 复合图
	 */
	public static final String ShowType_MSCombi_3D = "MSCombi3D";
	/**
	 * 二维双Y轴线、列、面积 复合图
	 */	
	public static final String ShowType_MSCombiDY_3D = "MSCombiDY2D";
	/**
	 * 金字塔图
	 */
	public static final String ShowType_Pyramid = "Pyramid";
	/**
	 * 折线图
	 */
	public static final String ShowType_Line_2D = "Line";
	/**
	 * 多序列折线图
	 */
	public static final String ShowType_MSLine = "MSLine";
	
	/**
	 * 仪表盘
	 */
	public static final String ShowType_AngularGauge = "AngularGauge";
	
	/**
	 * 双Y三维列线复合图
	 */
	public static final String ShowType_MSColumnLineDY_3D = "MSColumn3DLineDY";
	/**
	 * 双Y二维堆积图复合图	
	 */
	public static final String ShowType_MSStackedColumnLineDY_2D = "MSStackedColumn2DLineDY";
	
	/**
	 * 趋势图
	 * 曲线图
	 */
	public static final String ShowType_MSSpline = "MSSpline";
	/**
	 * 单序列类型
	 */
	public static final String  Series_Single_Type = "single-series";
	/**
	 * 多序列类型
	 */
	public static final String  Series_Mutil_Type = "multi-series";
	
	/**
	 * 仪表盘类型
	 */
	public static final String  Series_Angular_Type = "angular";
	
	/**
	 * 双Y轴类型
	 */
	public static final String  Series_DualY_Type = "dual";
	/**
	 * 分类列
	 */
	public static final String Dataset_Column_Group = "group";
	/**
	 * 数据列
	 */
	public static final String Dataset_Column_Data = "data";
	/**
	 * 指标列
	 */
	public static final String Dataset_Column_Series = "series";
	
	
	private static final long serialVersionUID = 2191769841906034318L;

	// 显示图像类型
	private String showType = "";
	// 图表显示系列类型（ChartConfig.SINGLE_SERIES、ChartConfig.MULTI_SERIES）
	private String seriesType = "";
	// 图表标题
	private String caption = "";
	//标题连接
	private String captionurl = "";
	//标题函数
	private String captionFunction = "";
	// 统计结果数字前缀
	private String numberPrefix = "";
	// 分组列
	private String groupColumn =Dataset_Column_Group;
	// 分组列图表显示名称
	private String groupName = "";
	// 统计列（用“,”分割的字符串）
	private String seriesColumns = Dataset_Column_Series;
	// 统计列图表显示名称（用“,”分割的字符串）
	private String seriesNames = null;
	/**
	 * 数据列
	 */
	private String dataColumn = Dataset_Column_Data;
	
	// 横轴显示文字
	private String xAxisName = "";
	// 纵轴显示文字
	private String yAxisName = "";
	//钻取
	private boolean isdrill = false;
	//标题字体
	private ChartFont captionFont = null;
	
	//X轴字体
	private ChartFont XAxisFont = null;
	//X轴刻度
	private ChartFont XLabelFont = null;	
	//X轴刻度倾斜度 暂不支持
	//Y轴字体
	private ChartFont YAxisFont = null;
	//Y轴刻度
	private ChartFont YLabelFont = null;
	
	//图例
	private ChartFont legendFont = null;
	//背景色
	private String bgcolor = "FFFFFF"; 
	
	public String getShowType() {
		return showType;
	}
	
	public void setShowType(String showType) {
		this.showType = showType;
	}
	
	public String getSeriesType() {
		return seriesType;
	}
	
	public void setSeriesType(String seriesType) {
		this.seriesType = seriesType;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String getNumberPrefix() {
		return numberPrefix;
	}
	
	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}
	
	public String getGroupColumn() {
		return groupColumn;
	}
	
	public void setGroupColumn(String groupColumn) {
		this.groupColumn = groupColumn;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getSeriesColumns() {
		return seriesColumns;
	}
	
	public void setSeriesColumns(String seriesColumns) {
		this.seriesColumns = seriesColumns;
	}
	
	public String getSeriesNames() {
		return seriesNames;
	}
	
	public void setSeriesNames(String seriesNames) {
		this.seriesNames = seriesNames;
	}
	
	public String getXAxisName() {
		return xAxisName;
	}
	
	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}
	
	public String getYAxisName() {
		return yAxisName;
	}
	
	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}

	public String getDataColumn() {
		return dataColumn;
	}

	public void setDataColumn(String dataColumn) {
		this.dataColumn = dataColumn;
	}

	public boolean isIsdrill() {
		return isdrill;
	}

	public void setIsdrill(boolean isdrill) {
		this.isdrill = isdrill;
	}
	
	/**
	 * 
	 * @param paraname
	 * @param parastring
	 */
	public void putNcModelParameters(String paraname,String parastring){
		
	}
	/**
	 * 
	 * @param paraname
	 * @param parastring
	 */
	public void putNcModelSerialsParameters(String SerialName,String paraname,String parastring){
		
	}

	public String GenCreateScript(String chartId){
		StringBuffer buf = new StringBuffer();
		buf.append("var config_").append(chartId).append(" = ");
		buf.append(" new ChartConfig(");
		if (this.getShowType() != null)
			buf.append("'").append(this.getShowType()).append("',");
		else
			buf.append("null,");
		if (this.getSeriesType() != null)
			buf.append("'").append(this.getSeriesType()).append("',");
		else
			buf.append("null,");
		if (this.getCaption() != null)
			buf.append("'").append(this.getCaption()).append("',");
		else
			buf.append("null,");
		if (this.getNumberPrefix() != null)
			buf.append("'").append(this.getNumberPrefix()).append("',");
		else
			buf.append("null,");
		if (this.getGroupColumn() != null)
			buf.append("'").append(this.getGroupColumn()).append("',");
		else
			buf.append("null,");
		if (this.getGroupName() != null)
			buf.append("'").append(this.getGroupName()).append("',");
		else
			buf.append("null,");
		if (this.getSeriesColumns() != null)
			buf.append("'").append(this.getSeriesColumns()).append("',");
		else
			buf.append("null,");
		if (this.getSeriesNames() != null)
			buf.append("'").append(this.getSeriesNames()).append("',");
		else
			buf.append("null,");
		if (this.getXAxisName() != null)
			buf.append("'").append(this.getXAxisName()).append("',");
		else
			buf.append("null,");
		if (this.getYAxisName() != null)
			buf.append("'").append(this.getYAxisName()).append("',");
		else
			buf.append("null,");

		if (this.getDataColumn() != null)
			buf.append("'").append(this.getDataColumn()).append("',");
		else
			buf.append("null,");
		buf.append("").append(this.isIsdrill()).append(")\n");
		
		if(this.captionFont != null)
			buf.append(" config_").append(chartId).append(".setCaptionFont(").append(this.captionFont.GenNewScriptstr()).append("); \n");
		if(this.XAxisFont != null)
			buf.append(" config_").append(chartId).append(".setxAxisFont(").append(this.XAxisFont.GenNewScriptstr()).append("); \n");
		if(this.XLabelFont != null)
			buf.append(" config_").append(chartId).append(".setxLabelFont(").append(this.XLabelFont.GenNewScriptstr()).append("); \n");
		if(this.YAxisFont != null)
			buf.append(" config_").append(chartId).append(".setyAxisFont(").append(this.YAxisFont.GenNewScriptstr()).append("); \n");
		if(this.YLabelFont != null)
			buf.append(" config_").append(chartId).append(".setyLabelFont(").append(this.YLabelFont.GenNewScriptstr()).append("); \n");
		if(this.legendFont != null)
			buf.append(" config_").append(chartId).append(".setylegendFont(").append(this.legendFont.GenNewScriptstr()).append("); \n");
		if(this.bgcolor != null && !this.bgcolor.equals("")){
			buf.append(" config_").append(chartId).append(".setbgcolor('").append(this.bgcolor).append("'); \n");
		}
		if(this.captionurl != null && !this.captionurl.equals("")){
			buf.append(" config_").append(chartId).append(".setCaptionUrl('").append(this.captionurl).append("'); \n");
		}
		if(this.captionFunction != null && !this.captionFunction.equals("")){
			buf.append(" config_").append(chartId).append(".setCaptionFunc('").append(this.captionFunction).append("'); \n");
		}		
		
		return buf.toString();
	}

	public ChartFont getCaptionFont() {
		return captionFont;
	}

	public void setCaptionFont(ChartFont captionFont) {
		this.captionFont = captionFont;
	}

	public ChartFont getXAxisFont() {
		return XAxisFont;
	}

	public void setXAxisFont(ChartFont xAxisFont) {
		XAxisFont = xAxisFont;
	}

	public ChartFont getXLabelFont() {
		return XLabelFont;
	}

	public void setXLabelFont(ChartFont xLabelFont) {
		XLabelFont = xLabelFont;
	}

	public ChartFont getYAxisFont() {
		return YAxisFont;
	}

	public void setYAxisFont(ChartFont yAxisFont) {
		YAxisFont = yAxisFont;
	}

	public ChartFont getYLabelFont() {
		return YLabelFont;
	}

	public void setYLabelFont(ChartFont yLabelFont) {
		YLabelFont = yLabelFont;
	}

	public ChartFont getLegendFont() {
		return legendFont;
	}

	public void setLegendFont(ChartFont legendFont) {
		this.legendFont = legendFont;
	}
	
	public void setBgColor(String color){
		this.bgcolor = color;
	}
	public String getBgColor(){
		return this.bgcolor;
	}
	public String getCaptionurl() {
		return captionurl;
	}
	
	public void setCaptionurl(String captionurl) {
		this.captionurl = captionurl;
	}
	public Dataset createDataset(){
		Dataset ds = new Dataset();
		ds.setId("dataset_chart");
		ds.setLazyLoad(true);
		
		Field field = new Field();
		field.setId(this.Dataset_Column_Group);
		field.setText("分组");
		ds.getFieldSet().addField(field);

		field = new Field();
		field.setId(this.Dataset_Column_Data);
		field.setText("数据");
		ds.getFieldSet().addField(field);
		
		field = new Field();
		field.setId(this.Dataset_Column_Series);
		field.setText("指标");
		ds.getFieldSet().addField(field);
		
		return ds;
	}

	public String getCaptionFunction() {
		return captionFunction;
	}

	public void setCaptionFunction(String captionFunction) {
		this.captionFunction = captionFunction;
	}

}



