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
public class ReferenceFormElementInfo extends FormElementInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	static{
//		StringPropertyInfo field = new StringPropertyInfo();
//		field.setId("field");
//		field.setVisible(true);
//		field.setEditable(true);
//		field.setDsField("string_ext4");
//		field.setLabel("字段");
//		field.setVoField("field");
//		list.add(field);
//		
//		StringPropertyInfo text = new StringPropertyInfo();
//		text.setId("text");
//		text.setVisible(true);
//		text.setEditable(true);
//		text.setDsField("string_ext5");
//		text.setLabel("显示值");
//		text.setVoField("itext");
//		list.add(text);
//		
//		StringPropertyInfo i18n = new StringPropertyInfo();
//		i18n.setId("i18nName");
//		i18n.setVisible(true);
//		i18n.setEditable(true);
//		i18n.setDsField("string_ext6");
//		i18n.setLabel("多语资源");
//		i18n.setVoField("i18nname");
//		list.add(i18n);
//		
//		StringPropertyInfo description = new StringPropertyInfo();
//		description.setId("description");
//		description.setVisible(true);
//		description.setEditable(true);
//		description.setDsField("string_ext7");
//		description.setLabel("描述");
//		description.setVoField("description");
//		list.add(description);
//		
//		StringPropertyInfo langDir = new StringPropertyInfo();
//		langDir.setId("langDir");
//		langDir.setVisible(true);
//		langDir.setEditable(true);
//		langDir.setDsField("string_ext8");
//		langDir.setLabel("多语目录");
//		langDir.setVoField("langdir");
//		list.add(langDir);
//		
//		StringPropertyInfo editorType = new StringPropertyInfo();
//		editorType.setId("editorType");
//		editorType.setVisible(true);
//		editorType.setEditable(true);
//		editorType.setDsField("string_ext9");
//		editorType.setLabel("编辑类型");
//		editorType.setVoField("editortype");
//		list.add(editorType);
//		
//		StringPropertyInfo dataType = new StringPropertyInfo();
//		dataType.setId("dataType");
//		dataType.setVisible(true);
//		dataType.setEditable(true);
//		dataType.setDsField("string_ext10");
//		dataType.setLabel("数据类型");
//		dataType.setVoField("datatype");
//		list.add(dataType);
//		
		StringPropertyInfo refNode = new StringPropertyInfo();
		refNode.setId("refNode");
		refNode.setVisible(true);
		refNode.setEditable(true);
		refNode.setDsField("string_ext11");
		refNode.setLabel("引用参照");
		refNode.setVoField("refnode");
		list.add(refNode);
		
		StringPropertyInfo refComboData = new StringPropertyInfo();
		refComboData.setId("refComboData");
		refComboData.setVisible(true);
		refComboData.setEditable(true);
		refComboData.setDsField("string_ext12");
		refComboData.setLabel("引用下拉框");
		refComboData.setVoField("refcombodata");
		list.add(refComboData);
//		
//		StringPropertyInfo labelColor = new StringPropertyInfo();
//		labelColor.setId("labelColor");
//		labelColor.setVisible(true);
//		labelColor.setEditable(true);
//		labelColor.setDsField("string_ext13");
//		labelColor.setLabel("字体颜色");
//		labelColor.setVoField("labelcolor");
//		list.add(labelColor);
//		
//		StringPropertyInfo defaultValue = new StringPropertyInfo();
//		defaultValue.setId("defaultValue");
//		defaultValue.setVisible(true);
//		defaultValue.setEditable(true);
//		defaultValue.setDsField("string_ext14");
//		defaultValue.setLabel("默认值");
//		defaultValue.setVoField("defaultvalue");
//		list.add(defaultValue);
//		
		StringPropertyInfo dataDivHeight = new StringPropertyInfo();
		dataDivHeight.setId("dataDivHeight");
		dataDivHeight.setVisible(true);
		dataDivHeight.setEditable(true);
		dataDivHeight.setDsField("string_ext15");
		dataDivHeight.setLabel("下拉数据区高度");
		dataDivHeight.setVoField("datadivheight");
		list.add(dataDivHeight);
		
		StringPropertyInfo relationField = new StringPropertyInfo();
		relationField.setId("relationField");
		relationField.setVisible(true);
		relationField.setEditable(true);
		relationField.setDsField("string_ext16");
		relationField.setLabel("关联字段");
		relationField.setVoField("relationfield");
		list.add(relationField);
		
//		StringPropertyInfo maxLength = new StringPropertyInfo();
//		maxLength.setId("maxLength");
//		maxLength.setVisible(false);
//		maxLength.setEditable(true);
//		maxLength.setDsField("string_ext17");
//		maxLength.setLabel("最大字节长度");
//		maxLength.setVoField("maxlength");
//		list.add(maxLength);
//		
//		StringPropertyInfo input = new StringPropertyInfo();
//		input.setId("inputAssistant");
//		input.setVisible(false);
//		input.setEditable(true);
//		input.setDsField("string_ext18");
//		input.setLabel("输入辅助提示");
//		input.setVoField("inputassistant");
//		list.add(input);
//		
//		StringPropertyInfo maxValue = new StringPropertyInfo();
//		maxValue.setId("maxValue");
//		maxValue.setVisible(false);
//		maxValue.setEditable(true);
//		maxValue.setDsField("string_ext19");
//		maxValue.setLabel("最大值");
//		maxValue.setVoField("maxvalue");
//		list.add(maxValue);
//		
//		StringPropertyInfo bindId = new StringPropertyInfo();
//		bindId.setId("bindId");
//		bindId.setVisible(true);
//		bindId.setEditable(true);
//		bindId.setDsField("string_ext20");
//		bindId.setLabel("控件ID");
//		bindId.setVoField("bindid");
//		list.add(bindId);
//		
//		StringPropertyInfo minValue = new StringPropertyInfo();
//		minValue.setId("minValue");
//		minValue.setVisible(false);
//		minValue.setEditable(true);
//		minValue.setDsField("string_ext21");
//		minValue.setLabel("最小值");
//		minValue.setVoField("minvalue");
//		list.add(minValue);
//		
//		StringPropertyInfo precision = new StringPropertyInfo();
//		precision.setId("precision");
//		precision.setVisible(false);
//		precision.setEditable(true);
//		precision.setDsField("string_ext22");
//		precision.setLabel("精度");
//		precision.setVoField("precisions");
//		list.add(precision);
//		
//		StringPropertyInfo hideBarIndices = new StringPropertyInfo();
//		hideBarIndices.setId("hideBarIndices");
//		hideBarIndices.setVisible(false);
//		hideBarIndices.setEditable(true);
//		hideBarIndices.setDsField("string_ext23");
//		hideBarIndices.setLabel("hideBarIndices");
//		hideBarIndices.setVoField("hidebarindices");
//		list.add(hideBarIndices);
//		
//		StringPropertyInfo hideImageIndices = new StringPropertyInfo();
//		hideImageIndices.setId("hideImageIndices");
//		hideImageIndices.setVisible(false);
//		hideImageIndices.setEditable(true);
//		hideImageIndices.setDsField("string_ext24");
//		hideImageIndices.setLabel("hideImageIndices");
//		hideImageIndices.setVoField("hideimageindices");
//		list.add(hideImageIndices);
//		
//		StringPropertyInfo tip = new StringPropertyInfo();
//		tip.setId("tip");
//		tip.setVisible(true);
//		tip.setEditable(true);
//		tip.setDsField("string_ext25");
//		tip.setLabel("提示");
//		tip.setVoField("tip");
//		list.add(tip);
//		
//		StringPropertyInfo width = new StringPropertyInfo();
//		width.setId("width");
//		width.setVisible(false);
//		width.setEditable(true);
//		width.setDsField("string_ext26");
//		width.setLabel("宽");
//		width.setVoField("width");
//		list.add(width);
//		
//		StringPropertyInfo height = new StringPropertyInfo();
//		height.setId("height");
//		height.setVisible(false);
//		height.setEditable(true);
//		height.setDsField("string_ext27");
//		height.setLabel("高");
//		height.setVoField("height");
//		list.add(height);
//		
		StringPropertyInfo value = new StringPropertyInfo();
		value.setId("value");
		value.setVisible(true);
		value.setEditable(true);
		value.setDsField("string_ext28");
		value.setLabel("ref值");
		value.setVoField("value");
		list.add(value);
		
		StringPropertyInfo showValue = new StringPropertyInfo();
		showValue.setId("showValue");
		showValue.setVisible(true);
		showValue.setEditable(true);
		showValue.setDsField("string_ext29");
		showValue.setLabel("参照显示值");
		showValue.setVoField("showvalue");
		list.add(showValue);
		
//		ComboPropertyInfo editable = new ComboPropertyInfo();
//		editable.setId("editable");
//		editable.setVisible(true);
//		editable.setEditable(true);
//		editable.setKeys(KEYS);
//		editable.setValues(VALUES);
//		editable.setDsField("combo_ext1");
//		editable.setLabel("是否可编辑");
//		editable.setVoField("editables");
//		list.add(editable);
//		
//		ComboPropertyInfo enabled = new ComboPropertyInfo();
//		enabled.setId("enabled");
//		enabled.setVisible(true);
//		enabled.setEditable(true);
//		enabled.setType(StringDataTypeConst.bOOLEAN);
//		enabled.setKeys(KEYS);
//		enabled.setValues(VALUES);
//		enabled.setDsField("combo_ext2");
//		enabled.setLabel("是否可用");
//		enabled.setVoField("enableds");
//		list.add(enabled);
//		
//		ComboPropertyInfo visible = new ComboPropertyInfo();
//		visible.setId("visible");
//		visible.setVisible(true);
//		visible.setEditable(true);
//		visible.setType(StringDataTypeConst.bOOLEAN);
//		visible.setKeys(KEYS);
//		visible.setValues(VALUES);
//		visible.setDsField("combo_ext3");
//		visible.setLabel("是否可见");
//		visible.setVoField("visibles");
//		list.add(visible);
//		
//		ComboPropertyInfo nextLine = new ComboPropertyInfo();
//		nextLine.setId("nextLine");
//		nextLine.setVisible(false);
//		nextLine.setEditable(true);
//		nextLine.setType(StringDataTypeConst.bOOLEAN);
//		nextLine.setKeys(KEYS);
//		nextLine.setValues(VALUES);
//		nextLine.setDsField("combo_ext4");
//		nextLine.setLabel("nextLine");
//		nextLine.setVoField("nextline");
//		list.add(nextLine);
//		
//		ComboPropertyInfo imageOnly = new ComboPropertyInfo();
//		imageOnly.setId("imageOnly");
//		imageOnly.setVisible(false);
//		imageOnly.setEditable(true);
//		imageOnly.setType(StringDataTypeConst.bOOLEAN);
//		imageOnly.setKeys(KEYS);
//		imageOnly.setValues(VALUES);
//		imageOnly.setDsField("combo_ext5");
//		imageOnly.setLabel("只显示图片");
//		imageOnly.setVoField("imageonlys");
//		list.add(imageOnly);
//		
		ComboPropertyInfo selectOnly = new ComboPropertyInfo();
		selectOnly.setId("selectOnly");
		selectOnly.setVisible(true);
		selectOnly.setEditable(true);
		selectOnly.setType(StringDataTypeConst.bOOLEAN);
		selectOnly.setKeys(KEYS);
		selectOnly.setValues(VALUES);
		selectOnly.setDsField("combo_ext6");
		selectOnly.setLabel("下拉框使用");
		selectOnly.setVoField("selectonlys");
		list.add(selectOnly);
		
//		ComboPropertyInfo nullAble = new ComboPropertyInfo();
//		nullAble.setId("nullAble");
//		nullAble.setVisible(true);
//		nullAble.setEditable(true);
//		nullAble.setType(StringDataTypeConst.bOOLEAN);
//		nullAble.setKeys(KEYS);
//		nullAble.setValues(VALUES);
//		nullAble.setDsField("combo_ext7");
//		nullAble.setLabel("是否可以为空");
//		nullAble.setVoField("nullables");
//		list.add(nullAble);
//		
//		ComboPropertyInfo attachNext = new ComboPropertyInfo();
//		attachNext.setId("attachNext");
//		attachNext.setVisible(true);
//		attachNext.setEditable(true);
//		attachNext.setType(StringDataTypeConst.bOOLEAN);
//		attachNext.setKeys(KEYS);
//		attachNext.setValues(VALUES);
//		attachNext.setDsField("combo_ext8");
//		attachNext.setLabel("attachNext");
//		attachNext.setVoField("attachnexts");
//		list.add(attachNext);
//		
//		ComboPropertyInfo focus = new ComboPropertyInfo();
//		focus.setId("focus");
//		focus.setVisible(true);
//		focus.setEditable(true);
//		focus.setType(StringDataTypeConst.bOOLEAN);
//		focus.setKeys(KEYS);
//		focus.setValues(VALUES);
//		focus.setDsField("combo_ext9");
//		focus.setLabel("是否聚焦");
//		focus.setVoField("focuss");
//		list.add(focus);
//		
//		IntegerPropertyInfo rowSpan = new IntegerPropertyInfo();
//		rowSpan.setId("rowSpan");
//		rowSpan.setEditable(true);
//		rowSpan.setVisible(true);
//		rowSpan.setType(StringDataTypeConst.INTEGER);
//		rowSpan.setDsField("integer_ext1");
//		rowSpan.setLabel("rowSpan");
//		rowSpan.setVoField("rowspan");
//		list.add(rowSpan);
//		
//		IntegerPropertyInfo colSpan = new IntegerPropertyInfo();
//		colSpan.setId("colSpan");
//		colSpan.setEditable(true);
//		colSpan.setVisible(true);
//		colSpan.setType(StringDataTypeConst.INTEGER);
//		colSpan.setDsField("integer_ext2");
//		colSpan.setLabel("colSpan");
//		colSpan.setVoField("colspan");
//		list.add(colSpan);
//		
//		IntegerPropertyInfo index = new IntegerPropertyInfo();
//		index.setId("index");
//		index.setEditable(true);
//		index.setVisible(true);
//		index.setType(StringDataTypeConst.INT);
//		index.setDsField("integer_ext3");
//		index.setLabel("索引");
//		index.setVoField("indexs");
//		list.add(index);
//		
//		IntegerPropertyInfo inputWidth = new IntegerPropertyInfo();
//		inputWidth.setId("inputWidth");
//		inputWidth.setEditable(true);
//		inputWidth.setVisible(true);
//		inputWidth.setType(StringDataTypeConst.INTEGER);
//		inputWidth.setDsField("integer_ext4");
//		inputWidth.setLabel("元素宽度");
//		inputWidth.setVoField("inputwidth");
//		list.add(inputWidth);
//		
//		StringPropertyInfo parentid = new StringPropertyInfo();
//		parentid.setId("");
//		parentid.setVisible(false);
//		parentid.setEditable(true);
//		parentid.setDsField("parentid");
//		parentid.setLabel("父ID");
//		parentid.setVoField("parentid");
//		list.add(parentid);
//		
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
