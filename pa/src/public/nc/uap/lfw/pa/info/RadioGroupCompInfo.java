/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-10-10
 * @since 1.6
 */
public class RadioGroupCompInfo extends ControlInfo {


	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	private static final String[] KEYS = new String[]{"��", "��"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	
	static{

		ComboPropertyInfo visi = new ComboPropertyInfo();
		visi.setId("visible");
		visi.setKeys(KEYS);
		visi.setValues(VALUES);
		visi.setType(StringDataTypeConst.bOOLEAN);
		visi.setDsField("combo_ext1");
		visi.setLabel("�Ƿ�ɼ�");
		visi.setVoField("visible");
		visi.setVisible(true);
		visi.setEditable(true);
		list.add(visi);
		
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
		
		ComboPropertyInfo readOnly = new ComboPropertyInfo();
		readOnly.setId("readOnly");
		readOnly.setVisible(true);
		readOnly.setEditable(true);
		readOnly.setType(StringDataTypeConst.bOOLEAN);
		readOnly.setKeys(KEYS);
		readOnly.setValues(VALUES);
		readOnly.setDsField("combo_ext3");
		readOnly.setLabel("readOnly");
		readOnly.setVoField("readonly");
		list.add(readOnly);
		
		ComboPropertyInfo focus = new ComboPropertyInfo();
		focus.setId("focus");
		focus.setVisible(true);
		focus.setEditable(true);
		focus.setType(StringDataTypeConst.bOOLEAN);
		focus.setKeys(KEYS);
		focus.setValues(VALUES);
		focus.setDsField("combo_ext4");
		focus.setLabel("�Ƿ�۽�");
		focus.setVoField("focus");
		list.add(focus);
		
		ComboPropertyInfo changeLine = new ComboPropertyInfo();
		changeLine.setId("changeLine");
		changeLine.setVisible(true);
		changeLine.setEditable(true);
		changeLine.setType(StringDataTypeConst.bOOLEAN);
		changeLine.setKeys(KEYS);
		changeLine.setValues(VALUES);
		changeLine.setDsField("combo_ext5");
		changeLine.setLabel("changeLine");
		changeLine.setVoField("changeline");
		list.add(changeLine);
		
		ComboPropertyInfo textalign = new ComboPropertyInfo();
		textalign.setId("textAlign");
		textalign.setVisible(true);
		textalign.setEditable(true);
		textalign.setType(StringDataTypeConst.STRING);
		textalign.setKeys(new String[]{"��", "��", "��"});
		textalign.setValues(new String[]{"left", "center", "right"});
		textalign.setDsField("combo_ext6");
		textalign.setLabel("��ǩλ��");
		textalign.setVoField("textalign");
		list.add(textalign);
		
		IntegerPropertyInfo textWidth = new IntegerPropertyInfo();
		textWidth.setId("textWidth");
		textWidth.setVisible(true);
		textWidth.setEditable(true);
		textWidth.setType(StringDataTypeConst.INT);
		textWidth.setDsField("integer_ext1");
		textWidth.setLabel("��ǩ���");
		textWidth.setVoField("textwidth");
		list.add(textWidth);
		
		IntegerPropertyInfo index = new IntegerPropertyInfo();
		index.setId("index");
		index.setVisible(true);
		index.setEditable(true);
		index.setType(StringDataTypeConst.INT);
		index.setDsField("integer_ext2");
		index.setLabel("index");
		index.setVoField("indexs");
		list.add(index);
		
		IntegerPropertyInfo tabIndex = new IntegerPropertyInfo();
		tabIndex.setId("tabIndex");
		tabIndex.setVisible(true);
		tabIndex.setEditable(true);
		tabIndex.setType(StringDataTypeConst.INT);
		tabIndex.setDsField("integer_ext3");
		tabIndex.setLabel("tabIndex");
		tabIndex.setVoField("tabindex");
		list.add(tabIndex);
		
		IntegerPropertyInfo sepWidth = new IntegerPropertyInfo();
		sepWidth.setId("sepWidth");
		sepWidth.setVisible(true);
		sepWidth.setEditable(true);
		sepWidth.setType(StringDataTypeConst.INT);
		sepWidth.setDsField("integer_ext4");
		sepWidth.setLabel("ÿ������ļ��");
		sepWidth.setVoField("sepwidth");
		list.add(sepWidth);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setEditable(true);
		height.setVisible(true);
		height.setDsField("string_ext4");
		height.setVoField("height");
		height.setLabel("��");
		list.add(height);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext5");
		width.setVoField("width");
		width.setLabel("��");
		list.add(width);
		
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
		left.setLabel("��߾�");
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
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setEditable(true);
		langdir.setVisible(true);
		langdir.setDsField("string_ext11");
		langdir.setLabel("����Ŀ¼");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo i18nname = new StringPropertyInfo();
		i18nname.setId("i18nName");
		i18nname.setEditable(true);
		i18nname.setVisible(true);
		i18nname.setDsField("string_ext12");
		i18nname.setLabel("������ʾֵ");
		i18nname.setVoField("i18nname");
		list.add(i18nname);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setEditable(true);
		text.setVisible(true);
		text.setDsField("string_ext13");
		text.setLabel("��ʾֵ");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo value = new StringPropertyInfo();
		value.setId("value");
		value.setEditable(true);
		value.setVisible(true);
		value.setDsField("string_ext14");
		value.setLabel("����ֵ");
		value.setVoField("value");
		list.add(value);
		
		StringPropertyInfo editorType = new StringPropertyInfo();
		editorType.setId("editorType");
		editorType.setEditable(true);
		editorType.setVisible(true);
		editorType.setDsField("string_ext15");
		editorType.setLabel("�༭����");
		editorType.setVoField("editortype");
		list.add(editorType);
		
		
		StringPropertyInfo comboDataId = new StringPropertyInfo();
		comboDataId.setId("comboDataId");
		comboDataId.setEditable(true);
		comboDataId.setVisible(true);
		comboDataId.setDsField("string_ext16");
		comboDataId.setLabel("��������������");
		comboDataId.setVoField("combodataid");
		list.add(comboDataId);
		
		
		StringPropertyInfo parentId = new StringPropertyInfo();
		parentId.setId("");
		parentId.setEditable(true);
		parentId.setVisible(false);
		parentId.setDsField("parentid");
		parentId.setLabel("��ID");
		parentId.setVoField("parentid");
		list.add(parentId);

	
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
