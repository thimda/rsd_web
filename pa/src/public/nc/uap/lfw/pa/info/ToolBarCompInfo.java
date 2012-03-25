/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-9-7
 * @since 1.6
 */
public class ToolBarCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(new String[]{"��", "��"});
		vis.setValues(new String[]{"Y", "N"});
		vis.setDsField("combo_ext1");
		vis.setLabel("�Ƿ�ɼ�");
		vis.setVoField("visible");
		list.add(vis);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setKeys(new String[]{"��", "��"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setDsField("combo_ext2");
		ena.setLabel("�Ƿ����");
		ena.setVoField("enabled");
		list.add(ena);
		
		ComboPropertyInfo trans = new ComboPropertyInfo();
		trans.setId("transparent");
		trans.setVisible(true);
		trans.setEditable(true);
		trans.setType(StringDataTypeConst.bOOLEAN);
		trans.setKeys(new String[]{"��", "��"});
		trans.setValues(new String[]{"Y", "N"});
		trans.setDsField("combo_ext3");
		trans.setLabel("�Ƿ�͸��");
		trans.setVoField("transparent");
		list.add(trans);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext4");
		width.setLabel("��");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setEditable(true);
		height.setVisible(true);
		height.setDsField("string_ext5");
		height.setLabel("��");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo top = new StringPropertyInfo();
		top.setId("top");
		top.setEditable(true);
		top.setVisible(false);
		top.setDsField("string_ext6");
		top.setLabel("�����");
		top.setVoField("itop");
		list.add(top);
		
		StringPropertyInfo left = new StringPropertyInfo();
		left.setId("left");
		left.setEditable(true);
		left.setVisible(false);
		left.setDsField("string_ext7");
		left.setLabel("�����");
		left.setVoField("ileft");
		list.add(left);
		
		StringPropertyInfo position = new StringPropertyInfo();
		position.setId("position");
		position.setEditable(true);
		position.setVisible(true);
		position.setDsField("string_ext8");
		position.setLabel("λ��");
		position.setVoField("positions");
		list.add(position);
		
		StringPropertyInfo contextmenu = new StringPropertyInfo();
		contextmenu.setId("contextMenu");
		contextmenu.setEditable(true);
		contextmenu.setVisible(true);
		contextmenu.setDsField("string_ext9");
		contextmenu.setLabel("�����˵�");
		contextmenu.setVoField("contextmenu");
		list.add(contextmenu);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setEditable(true);
		classname.setVisible(true);
		classname.setDsField("string_ext10");
		classname.setLabel("�Զ�������");
		classname.setVoField("classname");
		list.add(classname);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("��ID");
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
