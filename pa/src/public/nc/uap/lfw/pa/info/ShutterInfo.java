/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wupeng1
 * @version 6.0 2011-9-7
 * @since 1.6
 */
public class ShutterInfo extends LayoutInfo {

	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();

	static{
		StringPropertyInfo curr = new StringPropertyInfo();
		curr.setId("currentItem");
		curr.setVisible(true);
		curr.setEditable(true);
		curr.setDsField("string_ext3");
		curr.setLabel("currentItem");
		curr.setVoField("currentitem");
		list.add(curr);
		
//		StringPropertyInfo className = new StringPropertyInfo();
//		className.setId("className");
//		className.setVisible(true);
//		className.setEditable(true);
//		className.setDsField("string_ext4");
//		className.setLabel("自定义主题");
//		className.setVoField("classname");
//		list.add(className);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
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
