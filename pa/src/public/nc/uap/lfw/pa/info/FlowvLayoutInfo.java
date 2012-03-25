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
public class FlowvLayoutInfo extends LayoutInfo{
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();

	static{
		
		IntegerPropertyInfo autofill = new IntegerPropertyInfo();
		autofill.setId("autoFill");
		autofill.setVisible(true);
		autofill.setEditable(true);
		autofill.setType(StringDataTypeConst.INTEGER);
		autofill.setDsField("integer_ext1");
		autofill.setLabel(" «∑Ò◊‘  ”¶");
		autofill.setVoField("autofill");
		list.add(autofill);
		
//		StringPropertyInfo parentid = new StringPropertyInfo();
//		parentid.setId("");
//		parentid.setVisible(false);
//		parentid.setEditable(true);
//		parentid.setDsField("parentid");
//		parentid.setLabel("∏∏ID");
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
