/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class TabCompInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	private static final String[] KEYS = new String[]{"顶部", "底部"};
	private static final String[] VALUES = new String[]{"top", "bottom"};

	

	static{
		StringPropertyInfo curr = new StringPropertyInfo();
		curr.setId("currentItem");
		curr.setVisible(true);
		curr.setEditable(true);
		curr.setDsField("string_ext3");
		curr.setLabel("当前页签");
		curr.setVoField("currentitem");
		list.add(curr);

		ComboPropertyInfo tabType = new ComboPropertyInfo();
		tabType.setId("tabType");
		tabType.setVisible(true);
		tabType.setEditable(true);
		tabType.setType(StringDataTypeConst.STRING);
		tabType.setKeys(KEYS);
		tabType.setValues(VALUES);
		tabType.setDsField("combo_ext1");
		tabType.setLabel("标签位置");
		tabType.setDefaultValue("顶部");
		tabType.setVoField("tabtype");
		list.add(tabType);
		
		
		ComboPropertyInfo oneTabHide = new ComboPropertyInfo();
		oneTabHide.setId("oneTabHide");
		oneTabHide.setVisible(true);
		oneTabHide.setEditable(true);
		oneTabHide.setType(StringDataTypeConst.INTEGER);
		oneTabHide.setKeys(new String[]{"是","否"});
		oneTabHide.setValues(new String[]{"1","0"});
		oneTabHide.setDsField("combo_ext2");
		oneTabHide.setLabel("是否隐藏oneTab");
		oneTabHide.setVoField("onetabhide");
		list.add(oneTabHide);
		
		
//		
//		StringPropertyInfo parentid = new StringPropertyInfo();
//		parentid.setId("");
//		parentid.setVisible(false);
//		parentid.setEditable(true);
//		parentid.setDsField("parentid");
//		parentid.setLabel("父ID");
//		parentid.setVoField("parentid");
//		list.add(parentid);
		
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
