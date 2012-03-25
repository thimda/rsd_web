package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

public class GridRowLayoutInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();

	static{
		StringPropertyInfo rowHeight = new StringPropertyInfo();
		rowHeight.setId("rowHeight");
		rowHeight.setVisible(true);
		rowHeight.setEditable(true);
		rowHeight.setDsField("string_ext4");
		rowHeight.setLabel("ÐÐ¸ß");
		rowHeight.setVoField("rowheight");
		list.add(rowHeight);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("¸¸Id");
		parentid.setVoField("parentid");
		list.add(parentid);
		
	}
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
