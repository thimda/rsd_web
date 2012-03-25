/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 */
public class GridLayoutInfo extends LayoutInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();

	static{

		StringPropertyInfo border = new StringPropertyInfo();
		border.setId("border");
		border.setVisible(true);
		border.setEditable(true);
		border.setDsField("string_ext6");
		border.setLabel("border");
		border.setVoField("border");
		list.add(border);
		
		IntegerPropertyInfo rowcount = new IntegerPropertyInfo();
		rowcount.setId("rowcount");
		rowcount.setVisible(false);
		rowcount.setEditable(true);
		rowcount.setType(StringDataTypeConst.INTEGER);
		rowcount.setDsField("integer_ext1");
		rowcount.setLabel("行数");
		rowcount.setVoField("rowcounts");
		list.add(rowcount);
		
		IntegerPropertyInfo colcount = new IntegerPropertyInfo();
		colcount.setId("colcount");
		colcount.setVisible(false);
		colcount.setEditable(true);
		colcount.setType(StringDataTypeConst.INTEGER);
		colcount.setDsField("integer_ext2");
		colcount.setLabel("列数");
		colcount.setVoField("colcount");
		list.add(colcount);
		
//		StringPropertyInfo gridtype = new StringPropertyInfo();
//		gridtype.setId("className");
//		gridtype.setVisible(true);
//		gridtype.setEditable(true);
//		gridtype.setDsField("string_ext4");
//		gridtype.setLabel("自定义主题");
//		gridtype.setVoField("classname");
//		list.add(gridtype);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
		parentid.setVoField("parentid");
		list.add(parentid);
	}
	
	@Override
	public IPropertyInfo[] getPropertyInfos() {
		IPropertyInfo[] pinfos = super.getPropertyInfos();
		if(pinfos == null)
			return list.toArray(new IPropertyInfo[0]);
		else{
			int length = pinfos.length;
			IPropertyInfo[] newArr = new IPropertyInfo[list.size() + length];
			System.arraycopy(list.toArray(), 0, newArr, 0, list.size());
			System.arraycopy(pinfos, 0, newArr, list.size(), length);
			return newArr;
		}
	}
}
