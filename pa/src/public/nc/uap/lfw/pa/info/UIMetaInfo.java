package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

public class UIMetaInfo extends BaseInfo {

	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		StringPropertyInfo widgetId = new StringPropertyInfo();
		widgetId.setId("");
		widgetId.setVisible(true);
		widgetId.setEditable(false);
		widgetId.setDsField("string_ext2");
		widgetId.setVoField("widgetid");
		widgetId.setLabel("VIEW ID");
		list.add(widgetId);
		
		ComboPropertyInfo flowmode = new ComboPropertyInfo();
		flowmode.setId("flowmode");
		flowmode.setVisible(true);
		flowmode.setEditable(true);
		flowmode.setType(StringDataTypeConst.BOOLEAN);
		flowmode.setKeys(new String[]{"是","否"});
		flowmode.setValues(new String[]{"Y", "N"});
		flowmode.setDsField("combo_ext1");
		flowmode.setVoField("flowmode");
		flowmode.setLabel("流式布局");
		list.add(flowmode);
		
		IntegerPropertyInfo jquery = new IntegerPropertyInfo();
		jquery.setId("jquery");
		jquery.setVisible(true);
		jquery.setEditable(true);
		jquery.setType(StringDataTypeConst.INTEGER);
		jquery.setDsField("integer_ext1");
		jquery.setVoField("jquery");
		jquery.setLabel("jquery");
		list.add(jquery);
		
//		IntegerPropertyInfo chart = new IntegerPropertyInfo();
//		chart.setId("chart");
//		chart.setVisible(true);
//		chart.setEditable(true);
//		chart.setType(StringDataTypeConst.INTEGER);
//		chart.setDsField("integer_ext2");
//		chart.setVoField("chart");
//		chart.setLabel("chart");
//		list.add(chart);
		
		StringPropertyInfo includejs = new StringPropertyInfo();
		includejs.setId("includejs");
		includejs.setVisible(true);
		includejs.setEditable(true);
		includejs.setDsField("string_ext6");
		includejs.setVoField("includejs");
		includejs.setLabel("引入JS");
		list.add(includejs);
		
		StringPropertyInfo includecss = new StringPropertyInfo();
		includecss.setId("includecss");
		includecss.setVisible(true);
		includecss.setEditable(true);
		includecss.setDsField("string_ext7");
		includecss.setVoField("includecss");
		includecss.setLabel("引入CSS");
		list.add(includecss);
		
//		IntegerPropertyInfo jsEditor = new IntegerPropertyInfo();
//		jsEditor.setId("jsEditor");
//		jsEditor.setVisible(true);
//		jsEditor.setEditable(true);
//		jsEditor.setType(StringDataTypeConst.INTEGER);
//		jsEditor.setDsField("integer_ext3");
//		jsEditor.setVoField("jseditor");
//		jsEditor.setLabel("jsEditor");
//		list.add(jsEditor);
		
//		IntegerPropertyInfo jsExcel = new IntegerPropertyInfo();
//		jsExcel.setId("jsExcel");
//		jsExcel.setVisible(true);
//		jsExcel.setEditable(true);
//		jsExcel.setType(StringDataTypeConst.INTEGER);
//		jsExcel.setDsField("string_ext9");
//		jsExcel.setVoField("jsexcel");
//		jsExcel.setLabel("jsExcel");
//		list.add(jsExcel);
		
//		StringPropertyInfo generateclass = new StringPropertyInfo();
//		generateclass.setId("generateClass");
//		generateclass.setVisible(true);
//		generateclass.setEditable(true);
//		generateclass.setDsField("string_ext10");
//		generateclass.setVoField("generateclass");
//		generateclass.setLabel("generateclass");
//		list.add(generateclass);
		
//		StringPropertyInfo tabBody = new StringPropertyInfo();
//		tabBody.setId("tabBody");
//		tabBody.setVisible(true);
//		tabBody.setEditable(true);
//		tabBody.setDsField("string_ext11");
//		tabBody.setVoField("tabbody");
//		tabBody.setLabel("tabBody");
//		list.add(tabBody);
		
		StringPropertyInfo childtype = new StringPropertyInfo();
		childtype.setId("");
		childtype.setVisible(false);
		childtype.setEditable(true);
		childtype.setDsField("childtype");
		childtype.setLabel("子类型");
		childtype.setVoField("childtype");
		list.add(childtype);
		
		
		StringPropertyInfo childid = new StringPropertyInfo();
		childid.setId("");
		childid.setVisible(false);
		childid.setEditable(true);
		childid.setDsField("childid");
		childid.setLabel("子ID");
		childid.setVoField("childid");
		list.add(childid);
		
	}
	@Override
	public IPropertyInfo[] getPropertyInfos() {
		

		IPropertyInfo[] sinfo = super.getPropertyInfos();
		if(sinfo == null)
			return list.toArray(new IPropertyInfo[0]);
		else{
			int lenth = sinfo.length;
			IPropertyInfo[] newArr = new IPropertyInfo[list.size()+lenth];
			System.arraycopy(list.toArray(), 0, newArr, 0, list.size());
			System.arraycopy(sinfo, 0, newArr, list.size(), lenth);
			return newArr;
		}
	
	}
	

}
