/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-9-27
 * @since 1.6
 */
public class GridColumnInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	
	private static final String[] KEYS = new String[]{"��", "��"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	
	static{
		StringPropertyInfo field = new StringPropertyInfo();
		field.setId("field");
		field.setEditable(true);
		field.setVisible(true);
		field.setDsField("string_ext4");
		field.setVoField("field");
		field.setLabel("�ֶ�");
		list.add(field);
		
		StringPropertyInfo langDir = new StringPropertyInfo();
		langDir.setId("langDir");
		langDir.setEditable(true);
		langDir.setVisible(true);
		langDir.setDsField("string_ext5");
		langDir.setVoField("langdir");
		langDir.setLabel("����Ŀ¼");
		list.add(langDir);
		
		StringPropertyInfo i18n = new StringPropertyInfo();
		i18n.setId("i18nName");
		i18n.setVisible(true);
		i18n.setEditable(true);
		i18n.setDsField("string_ext6");
		i18n.setLabel("������Դ");
		i18n.setVoField("i18nname");
		list.add(i18n);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setVisible(true);
		text.setEditable(true);
		text.setDsField("string_ext7");
		text.setLabel("��ʾֵ");
		text.setVoField("itext");
		list.add(text);
		
		IntegerPropertyInfo width = new IntegerPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setType(StringDataTypeConst.INT);
		width.setDsField("integer_ext1");
		width.setLabel("���");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo datatype = new StringPropertyInfo();
		datatype.setId("dataType");
		datatype.setVisible(true);
		datatype.setEditable(true);
		datatype.setDsField("string_ext8");
		datatype.setLabel("��������");
		datatype.setVoField("datatype");
		list.add(datatype);
		
		ComboPropertyInfo sortable = new ComboPropertyInfo();
		sortable.setId("sortable");
		sortable.setVisible(true);
		sortable.setEditable(true);
		sortable.setKeys(KEYS);
		sortable.setValues(VALUES);
		sortable.setDsField("combo_ext1");
		sortable.setLabel("�Ƿ������");
		sortable.setVoField("sortables");
		list.add(sortable);
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(KEYS);
		vis.setValues(VALUES);
		vis.setDsField("combo_ext2");
		vis.setLabel("�Ƿ�ɼ�");
		vis.setVoField("visibles");
		list.add(vis);
		
		ComboPropertyInfo editable = new ComboPropertyInfo();
		editable.setId("editable");
		editable.setVisible(true);
		editable.setEditable(true);
		editable.setType(StringDataTypeConst.bOOLEAN);
		editable.setKeys(KEYS);
		editable.setValues(VALUES);
		editable.setDsField("combo_ext3");
		editable.setLabel("�Ƿ�ɱ༭");
		editable.setVoField("editables");
		list.add(editable);
		
		ComboPropertyInfo autoExpand = new ComboPropertyInfo();
		autoExpand.setId("autoExpand");
		autoExpand.setVisible(true);
		autoExpand.setEditable(true);
		autoExpand.setType(StringDataTypeConst.bOOLEAN);
		autoExpand.setKeys(KEYS);
		autoExpand.setValues(VALUES);
		autoExpand.setDsField("combo_ext4");
		autoExpand.setLabel("�Զ���չ");
		autoExpand.setVoField("autoexpands");
		list.add(autoExpand);
		
		ComboPropertyInfo fixedHeader = new ComboPropertyInfo();
		fixedHeader.setId("fixedHeader");
		fixedHeader.setVisible(true);
		fixedHeader.setEditable(true);
		fixedHeader.setType(StringDataTypeConst.bOOLEAN);
		fixedHeader.setKeys(KEYS);
		fixedHeader.setValues(VALUES);
		fixedHeader.setDsField("combo_ext5");
		fixedHeader.setLabel("�̶���ͷ");
		fixedHeader.setVoField("fixedheaders");
		list.add(fixedHeader);
		
		ComboPropertyInfo imageOnly = new ComboPropertyInfo();
		imageOnly.setId("imageOnly");
		imageOnly.setVisible(true);
		imageOnly.setEditable(true);
		imageOnly.setType(StringDataTypeConst.bOOLEAN);
		imageOnly.setKeys(KEYS);
		imageOnly.setValues(VALUES);
		imageOnly.setDsField("combo_ext6");
		imageOnly.setLabel("ֻ��ʾͼƬ");
		imageOnly.setVoField("imageonlys");
		list.add(imageOnly);
		
		ComboPropertyInfo sumCol = new ComboPropertyInfo();
		sumCol.setId("sumCol");
		sumCol.setVisible(true);
		sumCol.setEditable(true);
		sumCol.setType(StringDataTypeConst.bOOLEAN);
		sumCol.setKeys(KEYS);
		sumCol.setValues(VALUES);
		sumCol.setDsField("combo_ext7");
		sumCol.setLabel("�Ƿ�ϼ���");
		sumCol.setVoField("sumcols");
		list.add(sumCol);
		
		ComboPropertyInfo nullAble = new ComboPropertyInfo();
		nullAble.setId("nullAble");
		nullAble.setVisible(true);
		nullAble.setEditable(true);
		nullAble.setType(StringDataTypeConst.bOOLEAN);
		nullAble.setKeys(KEYS);
		nullAble.setValues(VALUES);
		nullAble.setDsField("combo_ext8");
		nullAble.setLabel("�Ƿ����Ϊ��");
		nullAble.setVoField("nullables");
		list.add(nullAble);
		
		StringPropertyInfo columbgcolor = new StringPropertyInfo();
		columbgcolor.setId("columBgColor");
		columbgcolor.setVisible(true);
		columbgcolor.setEditable(true);
		columbgcolor.setDsField("string_ext9");
		columbgcolor.setLabel("�б���ɫ");
		columbgcolor.setVoField("columbgcolor");
		list.add(columbgcolor);
		
		StringPropertyInfo textalign = new StringPropertyInfo();
		textalign.setId("textAlign");
		textalign.setVisible(true);
		textalign.setEditable(true);
		textalign.setDsField("string_ext10");
		textalign.setLabel("����λ��");
		textalign.setVoField("textalign");
		list.add(textalign);
		
		StringPropertyInfo textcolor = new StringPropertyInfo();
		textcolor.setId("textColor");
		textcolor.setVisible(true);
		textcolor.setEditable(true);
		textcolor.setDsField("string_ext11");
		textcolor.setLabel("������ɫ");
		textcolor.setVoField("textColor");
		list.add(textcolor);
		
		StringPropertyInfo editorType = new StringPropertyInfo();
		editorType.setId("editorType");
		editorType.setVisible(true);
		editorType.setEditable(true);
		editorType.setDsField("string_ext12");
		editorType.setLabel("�༭����");
		editorType.setVoField("editortype");
		list.add(editorType);
		
		StringPropertyInfo renderType = new StringPropertyInfo();
		renderType.setId("renderType");
		renderType.setVisible(true);
		renderType.setEditable(true);
		renderType.setDsField("string_ext13");
		renderType.setLabel("��Ⱦ����");
		renderType.setVoField("rendertype");
		list.add(renderType);
		
		StringPropertyInfo refcombodata = new StringPropertyInfo();
		refcombodata.setId("refComboData");
		refcombodata.setVisible(true);
		refcombodata.setEditable(true);
		refcombodata.setDsField("string_ext14");
		refcombodata.setLabel("����������");
		refcombodata.setVoField("refcombodata");
		list.add(refcombodata);
		
		StringPropertyInfo refnode = new StringPropertyInfo();
		refnode.setId("refNode");
		refnode.setVisible(true);
		refnode.setEditable(true);
		refnode.setDsField("string_ext15");
		refnode.setLabel("���ò���");
		refnode.setVoField("refnode");
		list.add(refnode);
		
		StringPropertyInfo maxValue = new StringPropertyInfo();
		maxValue.setId("maxValue");
		maxValue.setVisible(true);
		maxValue.setEditable(true);
		maxValue.setDsField("string_ext16");
		maxValue.setLabel("���ֵ");
		maxValue.setVoField("maxvalue");
		list.add(maxValue);
		
		StringPropertyInfo minValue = new StringPropertyInfo();
		minValue.setId("minValue");
		minValue.setVisible(true);
		minValue.setEditable(true);
		minValue.setDsField("string_ext17");
		minValue.setLabel("��Сֵ");
		minValue.setVoField("minvalue");
		list.add(minValue);
		
		StringPropertyInfo precisions = new StringPropertyInfo();
		precisions.setId("precision");
		precisions.setVisible(true);
		precisions.setEditable(true);
		precisions.setDsField("string_ext18");
		precisions.setLabel("����");
		precisions.setVoField("precisions");
		list.add(precisions);
		
		StringPropertyInfo maxlength = new StringPropertyInfo();
		maxlength.setId("maxLength");
		maxlength.setVisible(true);
		maxlength.setEditable(true);
		maxlength.setDsField("string_ext19");
		maxlength.setLabel("maxlength");
		maxlength.setVoField("maxlength");
		list.add(maxlength);
		
		StringPropertyInfo columngroup = new StringPropertyInfo();
		columngroup.setId("colmngroup");
		columngroup.setVisible(true);
		columngroup.setEditable(true);
		columngroup.setDsField("string_ext20");
		columngroup.setLabel("������");
		columngroup.setVoField("columngroup");
		list.add(columngroup);
		
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
