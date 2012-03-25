package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

public class GridPanelInfo extends LayoutPanelInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		
		StringPropertyInfo rowspan = new StringPropertyInfo();
		rowspan.setId("rowSpan");
		rowspan.setVisible(true);
		rowspan.setEditable(true);
		rowspan.setDsField("string_ext4");
		rowspan.setLabel("rowSpan");
		rowspan.setVoField("rowspan");
		list.add(rowspan);
		
		StringPropertyInfo colspan = new StringPropertyInfo();
		colspan.setId("colSpan");
		colspan.setVisible(true);
		colspan.setEditable(true);
		colspan.setDsField("string_ext5");
		colspan.setLabel("colSpan");
		colspan.setVoField("colspan");
		list.add(colspan);
		
		StringPropertyInfo colwidth = new StringPropertyInfo();
		colwidth.setId("colWidth");
		colwidth.setVisible(true);
		colwidth.setEditable(true);
		colwidth.setDsField("string_ext6");
		colwidth.setLabel("colWidth");
		colwidth.setVoField("colwidth");
		list.add(colwidth);
		
		StringPropertyInfo colheight = new StringPropertyInfo();
		colheight.setId("colHeight");
		colheight.setVisible(true);
		colheight.setEditable(true);
		colheight.setDsField("string_ext7");
		colheight.setLabel("colHeight");
		colheight.setVoField("colheight");
		list.add(colheight);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("¸¸Id");
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
