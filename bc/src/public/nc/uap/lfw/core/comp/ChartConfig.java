package nc.uap.lfw.core.comp;

import java.io.Serializable;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;

/**
 * ͼ��ؼ������ö���
 * 
 * @author guoweic
 *
 */
public class ChartConfig implements Serializable {
	/**
	 * ��������ͼ��ά
	 */
	public static final String  ShowType_Bar_2D = "Bar2D";
	/**
	 * ����������ͼ��ά
	 */
	public static final String  ShowType_MSBar_2D = "MSBar2D";
	/**
	 * ����������ͼ��ά
	 */
	public static final String  ShowType_MSBar_3D = "MSBar3D";
	/**
	 * �ѻ�����ͼ��ά
	 */
	public static final String  ShowType_StackedBar_2D = "StackedBar2D";
	/**
	 * �ѻ�����ͼ��ά
	 */
	public static final String  ShowType_StackedBar_3D = "StackedBar3D";
	
	/**
	 * ��������״ͼ2D
	 */
	public static final String  ShowType_Column_2D = "Column2D";	
	/**
	 * ������״ͼ2D
	 */
	public static final String  ShowType_MSColumn_2D = "MSColumn2D";
	
	/**
	 * ������״ͼ3D
	 */
	public static final String  ShowType_MSColumn_3D = "MSColumn3D";
	
	/**
	 * �����б�ͼ2D
	 */
	public static final String  ShowType_Pie_2D = "Pie2D";
	
	/**
	 * �����б�ͼ3D
	 */
	public static final String  ShowType_Pie_3D = "Pie3D";
	
	/**
	 * �����б�ͼ2D
	 */
	public static final String  ShowType_Doughnut_2D = "Doughnut2D";
	
	/**
	 * �����б�ͼ3D
	 */
	public static final String  ShowType_Doughnut_3D = "Doughnut3D";
	
	//
	//Doughnut3D
	/**
	 * ���������ͼ2D
	 */
	public static final String ShowType_Area_2D = "Area2D";
	
	/**
	 * ���������ͼ
	 */
	public static final String ShowType_MSArea_2D = "MSArea";
	/**
	 * �״�ͼ
	 */
	public static final String ShowType_Radar = "Radar";
	
	/**
	 * �ѻ���άͼ
	 */
	public static final String ShowType_Stacked_2D = "StackedColumn2D";
	
	/**
	 * �ѻ���άͼ
	 */
	public static final String ShowType_Stacked_3D = "StackedColumn3D";
	/**
	 * ����ѻ�ͼ
	 */
	public static final String ShowType_StackedArea_2D = "StackedArea2D";
	/**
	 * ��ά�ߡ��С���� ����ͼ
	 */
	public static final String ShowType_MSCombi_3D = "MSCombi3D";
	/**
	 * ��ά˫Y���ߡ��С���� ����ͼ
	 */	
	public static final String ShowType_MSCombiDY_3D = "MSCombiDY2D";
	/**
	 * ������ͼ
	 */
	public static final String ShowType_Pyramid = "Pyramid";
	/**
	 * ����ͼ
	 */
	public static final String ShowType_Line_2D = "Line";
	/**
	 * ����������ͼ
	 */
	public static final String ShowType_MSLine = "MSLine";
	
	/**
	 * �Ǳ���
	 */
	public static final String ShowType_AngularGauge = "AngularGauge";
	
	/**
	 * ˫Y��ά���߸���ͼ
	 */
	public static final String ShowType_MSColumnLineDY_3D = "MSColumn3DLineDY";
	/**
	 * ˫Y��ά�ѻ�ͼ����ͼ	
	 */
	public static final String ShowType_MSStackedColumnLineDY_2D = "MSStackedColumn2DLineDY";
	
	/**
	 * ����ͼ
	 * ����ͼ
	 */
	public static final String ShowType_MSSpline = "MSSpline";
	/**
	 * ����������
	 */
	public static final String  Series_Single_Type = "single-series";
	/**
	 * ����������
	 */
	public static final String  Series_Mutil_Type = "multi-series";
	
	/**
	 * �Ǳ�������
	 */
	public static final String  Series_Angular_Type = "angular";
	
	/**
	 * ˫Y������
	 */
	public static final String  Series_DualY_Type = "dual";
	/**
	 * ������
	 */
	public static final String Dataset_Column_Group = "group";
	/**
	 * ������
	 */
	public static final String Dataset_Column_Data = "data";
	/**
	 * ָ����
	 */
	public static final String Dataset_Column_Series = "series";
	
	
	private static final long serialVersionUID = 2191769841906034318L;

	// ��ʾͼ������
	private String showType = "";
	// ͼ����ʾϵ�����ͣ�ChartConfig.SINGLE_SERIES��ChartConfig.MULTI_SERIES��
	private String seriesType = "";
	// ͼ�����
	private String caption = "";
	//��������
	private String captionurl = "";
	//���⺯��
	private String captionFunction = "";
	// ͳ�ƽ������ǰ׺
	private String numberPrefix = "";
	// ������
	private String groupColumn =Dataset_Column_Group;
	// ������ͼ����ʾ����
	private String groupName = "";
	// ͳ���У��á�,���ָ���ַ�����
	private String seriesColumns = Dataset_Column_Series;
	// ͳ����ͼ����ʾ���ƣ��á�,���ָ���ַ�����
	private String seriesNames = null;
	/**
	 * ������
	 */
	private String dataColumn = Dataset_Column_Data;
	
	// ������ʾ����
	private String xAxisName = "";
	// ������ʾ����
	private String yAxisName = "";
	//��ȡ
	private boolean isdrill = false;
	//��������
	private ChartFont captionFont = null;
	
	//X������
	private ChartFont XAxisFont = null;
	//X��̶�
	private ChartFont XLabelFont = null;	
	//X��̶���б�� �ݲ�֧��
	//Y������
	private ChartFont YAxisFont = null;
	//Y��̶�
	private ChartFont YLabelFont = null;
	
	//ͼ��
	private ChartFont legendFont = null;
	//����ɫ
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
		field.setText("����");
		ds.getFieldSet().addField(field);

		field = new Field();
		field.setId(this.Dataset_Column_Data);
		field.setText("����");
		ds.getFieldSet().addField(field);
		
		field = new Field();
		field.setId(this.Dataset_Column_Series);
		field.setText("ָ��");
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



