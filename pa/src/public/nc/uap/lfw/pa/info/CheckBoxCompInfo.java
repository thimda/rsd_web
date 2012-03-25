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
public class CheckBoxCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	private static final String[] KEYS = new String[]{"��", "��"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	
	static{

		ComboPropertyInfo visible = new ComboPropertyInfo();
		visible.setId("visible");
		visible.setKeys(KEYS);
		visible.setValues(VALUES);
		visible.setType(StringDataTypeConst.bOOLEAN);
		visible.setDsField("combo_ext1");
		visible.setLabel("�Ƿ�ɼ�");
		visible.setVoField("visibles");
		visible.setVisible(true);
		visible.setEditable(true);
		list.add(visible);
		
		ComboPropertyInfo enabled = new ComboPropertyInfo();
		enabled.setId("enabled");
		enabled.setVisible(true);
		enabled.setEditable(true);
		enabled.setType(StringDataTypeConst.bOOLEAN);
		enabled.setKeys(KEYS);
		enabled.setValues(VALUES);
		enabled.setDsField("combo_ext2");
		enabled.setLabel("�Ƿ����");
		enabled.setVoField("enableds");
		list.add(enabled);
		
		ComboPropertyInfo checked = new ComboPropertyInfo();
		checked.setId("checked");
		checked.setVisible(true);
		checked.setEditable(true);
		checked.setKeys(KEYS);
		checked.setValues(VALUES);
		checked.setType(StringDataTypeConst.bOOLEAN);
		checked.setDsField("combo_ext3");
		checked.setLabel("�Ƿ�ѡ��");
		checked.setVoField("checkeds");
		list.add(checked);
		
		ComboPropertyInfo focus = new ComboPropertyInfo();
		focus.setId("focus");
		focus.setVisible(true);
		focus.setEditable(true);
		focus.setKeys(KEYS);
		focus.setValues(VALUES);
		focus.setType(StringDataTypeConst.bOOLEAN);
		focus.setDsField("combo_ext4");
		focus.setLabel("�Ƿ�۽�");
		focus.setVoField("focuss");
		list.add(focus);
		
		ComboPropertyInfo textalign = new ComboPropertyInfo();
		textalign.setId("textAlign");
		textalign.setType(StringDataTypeConst.STRING);
		textalign.setVisible(true);
		textalign.setEditable(true);
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
		
		StringPropertyInfo dataType = new StringPropertyInfo();
		dataType.setId("dataType");
		dataType.setEditable(true);
		dataType.setVisible(true);
		dataType.setDsField("string_ext4");
		dataType.setVoField("datatype");
		dataType.setLabel("��ǩ����");
		list.add(dataType);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext5");
		width.setVoField("width");
		width.setLabel("��");
		list.add(width);
		
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
		i18nname.setId("������ʾֵ");
		i18nname.setEditable(true);
		i18nname.setVisible(true);
		i18nname.setDsField("string_ext12");
		i18nname.setLabel("i18nName");
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
		
		StringPropertyInfo parentId = new StringPropertyInfo();
		parentId.setId("");
		parentId.setEditable(true);
		parentId.setVisible(false);
		parentId.setDsField("parentid");
		parentId.setLabel("��ID");
		parentId.setVoField("parentid");
		list.add(parentId);

		StringPropertyInfo imgSrc = new StringPropertyInfo();
		imgSrc.setId("imgsrc");
		imgSrc.setEditable(true);
		imgSrc.setVisible(true);
		imgSrc.setDsField("string_ext16");
		imgSrc.setLabel("��ʾͼƬ·��");
		imgSrc.setVoField("imgsrc");
		list.add(imgSrc);
		
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
