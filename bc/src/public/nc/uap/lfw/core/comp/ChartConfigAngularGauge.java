package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;

/**
 * �Ǳ���
 * @author lisw
 *
 */
public class ChartConfigAngularGauge extends ChartConfig {
	//������
	public final static  String  Angular_Column_Type = "type";
	//��ʼֵ
	public final static String  Angular_Column_Start = "start";
	//����ֵ
	public final static String  Angular_Column_End = "end";
	//��ɫ
	public final static String  Angular_Column_Color = "color";
	//��ɫ��������
	public final static String  Angular_Type_Range = "range";
	//ָ������
	public final static String  Angular_Type_Dial = "dial";
	//���ݼ�����
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
		field.setText("��������");
		ds.getFieldSet().addField(field);

		field = new Field();
		field.setId(Angular_Column_Start);
		field.setText("��ʼ����");
		ds.getFieldSet().addField(field);
		
		field = new Field();
		field.setId(Angular_Column_End);
		field.setText("��������");
		ds.getFieldSet().addField(field);
		
		field = new Field();
		field.setId(Angular_Column_Color);
		field.setText("��ɫ");
		ds.getFieldSet().addField(field);
		return ds;
	}
}
