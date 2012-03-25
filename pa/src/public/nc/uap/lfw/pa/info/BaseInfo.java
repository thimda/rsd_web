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
public abstract class BaseInfo implements IBaseInfo {
	private static final long serialVersionUID = -3659134536127052351L;
	
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		StringPropertyInfo sinfo = new StringPropertyInfo();
		sinfo.setId("id");
		sinfo.setVisible(true);
		sinfo.setEditable(false);
		sinfo.setDsField("string_ext1");
		sinfo.setVoField("id");
		sinfo.setLabel("ID");
		list.add(sinfo);
	}
	
	public IPropertyInfo[] getPropertyInfos(){
		return list.toArray(new IPropertyInfo[0]);
		
	}
}
