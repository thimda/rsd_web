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
public class TextAreaCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(new String[]{"��","��"});
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
		ena.setKeys(new String[]{"��","��"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setDsField("combo_ext2");
		ena.setLabel("�Ƿ����");
		ena.setVoField("enabled");
		list.add(ena);
		
		ComboPropertyInfo focus = new ComboPropertyInfo();
		focus.setId("focus");
		focus.setVisible(true);
		focus.setEditable(true);
		focus.setType(StringDataTypeConst.bOOLEAN);
		focus.setKeys(new String[]{"��","��"});
		focus.setValues(new String[]{"Y", "N"});
		focus.setDsField("combo_ext2");
		focus.setLabel("�Ƿ�۽�");
		focus.setVoField("focus");
		list.add(focus);
		
		IntegerPropertyInfo textWidth = new IntegerPropertyInfo();
		textWidth.setId("textWidth");
		textWidth.setVisible(true);
		textWidth.setEditable(true);
		textWidth.setType(StringDataTypeConst.INT);
		textWidth.setDsField("integer_ext1");
		textWidth.setLabel("��ǩ���");
		textWidth.setVoField("textwidth");
		list.add(textWidth);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext4");
		width.setVoField("width");
		width.setLabel("��");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setEditable(true);
		height.setVisible(true);
		height.setDsField("string_ext5");
		width.setLabel("��");
		height.setVoField("height");
		list.add(height);
		
//		StringPropertyInfo top = new StringPropertyInfo();
//		top.setId("top");
//		top.setEditable(true);
//		top.setVisible(false);
//		top.setDsField("string_ext6");
//		width.setLabel("�����");
//		top.setVoField("itop");
//		list.add(top);
//		
//		StringPropertyInfo left = new StringPropertyInfo();
//		left.setId("left");
//		left.setEditable(true);
//		left.setVisible(false);
//		left.setDsField("string_ext7");
//		width.setLabel("��߾�");
//		left.setVoField("ileft");
//		list.add(left);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setEditable(true);
		text.setVisible(true);
		text.setDsField("string_ext8");
		width.setLabel("��ʾֵ");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo editorType = new StringPropertyInfo();
		editorType.setId("editorType");
		editorType.setEditable(true);
		editorType.setVisible(true);
		editorType.setDsField("string_ext9");
		width.setLabel("�༭����");
		editorType.setVoField("editortype");
		list.add(editorType);
		
		StringPropertyInfo i18n = new StringPropertyInfo();
		i18n.setId("i18nName");
		i18n.setEditable(true);
		i18n.setVisible(true);
		i18n.setDsField("string_ext10");
		width.setLabel("������ʾֵ");
		i18n.setVoField("i18nname");
		list.add(i18n);
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setEditable(true);
		langdir.setVisible(true);
		langdir.setDsField("string_ext12");
		width.setLabel("����Ŀ¼");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo textAlign = new StringPropertyInfo();
		textAlign.setId("textAlign");
		textAlign.setEditable(true);
		textAlign.setVisible(true);
		textAlign.setDsField("string_ext13");
		width.setLabel("��ǩλ��");
		textAlign.setVoField("textalign");
		list.add(textAlign);
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setEditable(true);
		className.setVisible(true);
		className.setDsField("string_ext14");
		width.setLabel("�Զ�������");
		className.setVoField("classname");
		list.add(className);
		
//		StringPropertyInfo position = new StringPropertyInfo();
//		position.setId("position");
//		position.setEditable(true);
//		position.setVisible(true);
//		position.setDsField("string_ext15");
//		width.setLabel("λ��");
//		position.setVoField("positions");
//		list.add(position);
		
		StringPropertyInfo contextmenu = new StringPropertyInfo();
		contextmenu.setId("contextMenu");
		contextmenu.setEditable(true);
		contextmenu.setVisible(true);
		contextmenu.setDsField("string_ext16");
		width.setLabel("�����˵�");
		contextmenu.setVoField("contextmenu");
		list.add(contextmenu);
		
		StringPropertyInfo tip = new StringPropertyInfo();
		tip.setId("tip");
		tip.setEditable(true);
		tip.setVisible(true);
		tip.setDsField("string_ext17");
		width.setLabel("��ʾ");
		tip.setVoField("tip");
		list.add(tip);
		
		StringPropertyInfo rows = new StringPropertyInfo();
		rows.setId("rows");
		rows.setEditable(true);
		rows.setVisible(true);
		rows.setDsField("string_ext18");
		width.setLabel("�п�");
		rows.setVoField("rows");
		list.add(rows);
		
		StringPropertyInfo cols = new StringPropertyInfo();
		cols.setId("cols");
		cols.setEditable(true);
		cols.setVisible(true);
		cols.setDsField("string_ext19");
		width.setLabel("�п�");
		cols.setVoField("cols");
		list.add(cols);
		
//		
//		StringPropertyInfo parentId = new StringPropertyInfo();
//		parentId.setId("");
//		parentId.setEditable(true);
//		parentId.setVisible(false);
//		parentId.setDsField("parentid");
//		parentId.setLabel("��ID");
//		parentId.setVoField("parentid");
//		list.add(parentId);

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
