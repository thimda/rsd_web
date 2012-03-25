/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-8-26
 * @since 1.6
 */
public class TreeViewCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	private static final String[] KEYS = new String[]{"��", "��"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	static{
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(KEYS);
		vis.setValues(VALUES);
		vis.setDsField("combo_ext1");
		vis.setLabel("�Ƿ�ɼ�");
		vis.setVoField("visible");
		list.add(vis);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setKeys(KEYS);
		ena.setValues(VALUES);
		ena.setDsField("combo_ext2");
		ena.setLabel("�Ƿ����");
		ena.setVoField("enabled");
		list.add(ena);
		
		ComboPropertyInfo drag = new ComboPropertyInfo();
		drag.setId("dragEnable");
		drag.setVisible(true);
		drag.setEditable(true);
		drag.setType(StringDataTypeConst.bOOLEAN);
		drag.setKeys(KEYS);
		drag.setValues(VALUES);
		drag.setDsField("combo_ext3");
		drag.setLabel("�Ƿ����ק");
		drag.setVoField("dragenable");
		list.add(drag);
		
		ComboPropertyInfo rootopen = new ComboPropertyInfo();
		rootopen.setId("rootOpen");
		rootopen.setVisible(true);
		rootopen.setEditable(true);
		rootopen.setType(StringDataTypeConst.bOOLEAN);
		rootopen.setKeys(KEYS);
		rootopen.setValues(VALUES);
		rootopen.setDsField("combo_ext4");
		rootopen.setLabel("�Ƿ�չ�����ڵ�");
		rootopen.setVoField("rootopen");
		list.add(rootopen);
//		
//		ComboPropertyInfo pos = new ComboPropertyInfo();
//		pos.setId("position");
//		pos.setVisible(true);
//		pos.setEditable(true);
//		pos.setType(StringDataTypeConst.STRING);
//		pos.setKeys(new String[]{"��Ե�", "���Ե�"});
//		pos.setValues(new String[]{"relative", "positive"});
//		pos.setDsField("combo_ext5");
//		pos.setLabel("λ��");
//		pos.setVoField("positions");
//		list.add(pos);
		
		ComboPropertyInfo withckbox = new ComboPropertyInfo();
		withckbox.setId("withCheckBox");
		withckbox.setVisible(true);
		withckbox.setEditable(true);
		withckbox.setType(StringDataTypeConst.bOOLEAN);
		withckbox.setKeys(KEYS);
		withckbox.setValues(VALUES);
		withckbox.setDsField("combo_ext6");
		withckbox.setLabel("�ж�ѡ��");
		withckbox.setVoField("withcheckbox");
		list.add(withckbox);
		
		ComboPropertyInfo withroot = new ComboPropertyInfo();
		withroot.setId("withRoot");
		withroot.setVisible(true);
		withroot.setEditable(true);
		withroot.setType(StringDataTypeConst.bOOLEAN);
		withroot.setKeys(KEYS);
		withroot.setValues(VALUES);
		withroot.setDsField("combo_ext7");
		withroot.setLabel("��ʾ���ݽڵ�");
		withroot.setVoField("withroot");
		list.add(withroot);
		
//		StringPropertyInfo top = new StringPropertyInfo();
//		top.setId("top");
//		top.setVisible(false);
//		top.setEditable(true);
//		top.setDsField("string_ext4");
//		top.setLabel("�����");
//		top.setVoField("itop");
//		list.add(top);
//		
//		StringPropertyInfo left = new StringPropertyInfo();
//		left.setId("left");
//		left.setVisible(false);
//		left.setEditable(true);
//		left.setDsField("string_ext5");
//		left.setLabel("��߾�");
//		left.setVoField("ileft");
//		list.add(left);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext6");
		width.setLabel("��");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext7");
		height.setLabel("��");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setVisible(true);
		langdir.setEditable(true);
		langdir.setDsField("string_ext8");
		langdir.setLabel("����Ŀ¼");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo contm = new StringPropertyInfo();
		contm.setId("contextMenu");
		contm.setVisible(true);
		contm.setEditable(true);
		contm.setDsField("string_ext9");
		contm.setLabel("�����˵�");
		contm.setVoField("contextmenu");
		list.add(contm);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setVisible(true);
		classname.setEditable(true);
		classname.setDsField("string_ext10");
		classname.setLabel("�Զ�������");
		classname.setVoField("classname");
		list.add(classname);
		
		StringPropertyInfo i18nname = new StringPropertyInfo();
		i18nname.setId("i18nName");
		i18nname.setVisible(true);
		i18nname.setEditable(true);
		i18nname.setDsField("string_ext11");
		i18nname.setLabel("������Դ");
		i18nname.setVoField("i18nname");
		list.add(i18nname);
		
		StringPropertyInfo cap = new StringPropertyInfo();
		cap.setId("caption");
		cap.setVisible(true);
		cap.setEditable(true);
		cap.setDsField("string_ext12");
		cap.setLabel("����");
		cap.setVoField("caption");
		list.add(cap);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setVisible(true);
		text.setEditable(true);
		text.setDsField("string_ext13");
		text.setLabel("��ʾֵ");
		text.setVoField("itext");
		list.add(text);
		
		IntegerPropertyInfo openlevel = new IntegerPropertyInfo();
		openlevel.setId("openLevel");
		openlevel.setVisible(true);
		openlevel.setEditable(true);
		openlevel.setType(StringDataTypeConst.INT);
		openlevel.setDsField("integer_ext1");
		openlevel.setLabel("openLevel");
		openlevel.setVoField("�򿪲㼶");
		list.add(openlevel);
		
		IntegerPropertyInfo checkboxmodel = new IntegerPropertyInfo();
		checkboxmodel.setId("checkBoxModel");
		checkboxmodel.setVisible(true);
		checkboxmodel.setEditable(true);
		checkboxmodel.setType(StringDataTypeConst.INT);
		checkboxmodel.setDsField("integer_ext2");
		checkboxmodel.setLabel("CheckBoxModel");
		checkboxmodel.setVoField("ѡ��ģʽ");
		list.add(checkboxmodel);
		
	}
	@Override
	public IPropertyInfo[] getPropertyInfos() {
		IPropertyInfo[] sinfo = super.getPropertyInfos();
		if(sinfo == null)
			return list.toArray(new IPropertyInfo[0]);
		else{
			int lenth = sinfo.length;
			IPropertyInfo[] newArr = new IPropertyInfo[list.size()+lenth];
			System.arraycopy(list.toArray(), 0, newArr, 0, list.size());
			System.arraycopy(sinfo, 0, newArr, list.size(), lenth);
			return newArr;
		}
		
	}
	

}
