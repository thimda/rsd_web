package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;

/**
 * 仪表盘
 * @author lisw
 *
 */
public class ChartConfigAngularGauge extends ChartConfig {
	//名称列
	public final static  String  Angular_Column_Type = "type";
	//开始值
	public final static String  Angular_Column_Start = "start";
	//结束值
	public final static String  Angular_Column_End = "end";
	//颜色
	public final static String  Angular_Column_Color = "color";
	//颜色区间类型
	public final static String  Angular_Type_Range = "range";
	//指针类型
	public final static String  Angular_Type_Dial = "dial";
	//数据集名称
	public final static String  Angular_Dataset_Name = "Angulards";
	
	public ChartConfigAngularGauge(){
		super();
		this.setShowType(ChartConfig.ShowType_AngularGauge);
		this.setSeriesType(ChartConfig.Series_Angular_Type);
	}
	
	public Dataset createDataset(){
		Dataset ds = new Dataset();
		ds.setId(Angular_Dataset_Name);
		ds.setLazyLoad(true);
		
		Field field = new Field();
		field.setId(Angular_Column_Type);
		field.setText("参数名称");
		ds.getFieldSet().addField(field);

		field = new Field();
		field.setId(Angular_Column_Start);
		field.setText("开始参数");
		ds.getFieldSet().addField(field);
		
		field = new Field();
		field.setId(Angular_Column_End);
		field.setText("结束参数");
		ds.getFieldSet().addField(field);
		
		field = new Field();
		field.setId(Angular_Column_Color);
		field.setText("颜色");
		ds.getFieldSet().addField(field);
		return ds;
	}
}
