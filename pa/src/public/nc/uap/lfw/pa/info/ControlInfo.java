/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public class ControlInfo extends BaseInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		StringPropertyInfo widgetId = new StringPropertyInfo();
		widgetId.setId("");
		widgetId.setEditable(true);
		widgetId.setVisible(false);
		widgetId.setDsField("string_ext2");
		widgetId.setVoField("widgetid");
		widgetId.setLabel("View ID");
		list.add(widgetId);
		
		StringPropertyInfo sinfo2 = new StringPropertyInfo();
		sinfo2.setId("name");
		sinfo2.setEditable(false);
		sinfo2.setVisible(true);
		sinfo2.setDsField("string_ext3");
		sinfo2.setVoField("name");
		sinfo2.setLabel("Ãû³Æ");
		
		ComboPropertyInfo align = new ComboPropertyInfo();
		align.setId("align");
		align.setKeys(new String[]{"×ó", "ÓÒ"});
		align.setValues(new String[]{"left", "right"});
		align.setVisible(true);
		align.setEditable(true);
		align.setDsField("combo_ext15");
		align.setType(StringDataTypeConst.STRING);
		align.setVoField("align");
		align.setLabel("Î»ÖÃ");
		list.add(align);
		
	//	list.add(sinfo2);
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
