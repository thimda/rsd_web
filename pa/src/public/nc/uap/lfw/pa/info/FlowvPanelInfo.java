/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 */
public class FlowvPanelInfo extends LayoutPanelInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext4");
		height.setLabel("¸ß");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo anchor = new StringPropertyInfo();
		anchor.setId("anchor");
		anchor.setVisible(true);
		anchor.setEditable(true);
		anchor.setDsField("string_ext5");
		anchor.setLabel("Ãªµã");
		anchor.setVoField("anchor");
		list.add(anchor);
	
//		StringPropertyInfo parentid = new StringPropertyInfo();
//		parentid.setId("");
//		parentid.setVisible(false);
//		parentid.setEditable(true);
//		parentid.setDsField("parentid");
//		parentid.setLabel("¸¸ID");
//		parentid.setVoField("parentid");
//		list.add(parentid);
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
