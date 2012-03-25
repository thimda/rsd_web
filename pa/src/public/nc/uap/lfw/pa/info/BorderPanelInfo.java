/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class BorderPanelInfo extends LayoutPanelInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	static{
		
		StringPropertyInfo top = new StringPropertyInfo();
		top.setId("top");
		top.setVisible(true);
		top.setEditable(true);
		top.setDsField("string_ext3");
		top.setLabel("top");
		top.setVoField("itop");
		list.add(top);
		
		StringPropertyInfo center = new StringPropertyInfo();
		center.setId("center");
		center.setVisible(true);
		center.setEditable(true);
		center.setDsField("string_ext4");
		center.setLabel("center");
		center.setVoField("center");
		list.add(center);
		
		StringPropertyInfo bottom = new StringPropertyInfo();
		bottom.setId("bottom");
		bottom.setVisible(true);
		bottom.setEditable(true);
		bottom.setDsField("string_ext5");
		bottom.setLabel("bottom");
		bottom.setVoField("bottom");
		list.add(bottom);
		
		StringPropertyInfo left = new StringPropertyInfo();
		left.setId("left");
		left.setVisible(true);
		left.setEditable(true);
		left.setDsField("string_ext6");
		left.setLabel("left");
		left.setVoField("ileft");
		list.add(left);
		
		StringPropertyInfo right = new StringPropertyInfo();
		right.setId("right");
		right.setVisible(true);
		right.setEditable(true);
		right.setDsField("string_ext7");
		right.setLabel("right");
		right.setVoField("iright");
		list.add(right);
		
		StringPropertyInfo posi = new StringPropertyInfo();
		posi.setId("position");
		posi.setVisible(true);
		posi.setEditable(true);
		posi.setDsField("string_ext8");
		posi.setLabel("Î»ÖÃ");
		posi.setVoField("positions");
		list.add(posi);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext9");
		height.setLabel("¸ß");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext10");
		width.setLabel("¿í");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("¸¸ID");
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
