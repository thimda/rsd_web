/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public class LayoutInfo extends BaseInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();

	static{
		StringPropertyInfo widgetId = new StringPropertyInfo();
		widgetId.setId("widgetId");
		widgetId.setVisible(false);
		widgetId.setEditable(true);
		widgetId.setDsField("string_ext2");
		widgetId.setLabel("widgetId");
		widgetId.setVoField("widgetid");
		list.add(widgetId);
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setDsField("string_ext25");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);
		
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
