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
public class CheckboxGroupCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	
	static{

		ComboPropertyInfo visible = new ComboPropertyInfo();
		visible.setId("visible");
		visible.setKeys(KEYS);
		visible.setValues(VALUES);
		visible.setType(StringDataTypeConst.bOOLEAN);
		visible.setDsField("combo_ext1");
		visible.setLabel("是否可见");
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
		enabled.setLabel("是否可用");
		enabled.setVoField("enableds");
		list.add(enabled);
		
		ComboPropertyInfo changeLine = new ComboPropertyInfo();
		changeLine.setId("changeLine");
		changeLine.setVisible(true);
		changeLine.setEditable(true);
		changeLine.setType(StringDataTypeConst.bOOLEAN);
		changeLine.setKeys(KEYS);
		changeLine.setValues(VALUES);
		changeLine.setDsField("combo_ext3");
		changeLine.setLabel("changeLine");
		changeLine.setVoField("changelines");
		list.add(changeLine);
		
		ComboPropertyInfo focus = new ComboPropertyInfo();
		focus.setId("focus");
		focus.setVisible(true);
		focus.setEditable(true);
		focus.setType(StringDataTypeConst.bOOLEAN);
		focus.setKeys(KEYS);
		focus.setValues(VALUES);
		focus.setDsField("combo_ext4");
		focus.setLabel("是否聚焦");
		focus.setVoField("focuss");
		list.add(focus);
		
		ComboPropertyInfo readOnly = new ComboPropertyInfo();
		readOnly.setId("readOnly");
		readOnly.setVisible(true);
		readOnly.setEditable(true);
		readOnly.setType(StringDataTypeConst.bOOLEAN);
		readOnly.setKeys(KEYS);
		readOnly.setValues(VALUES);
		readOnly.setDsField("combo_ext5");
		readOnly.setLabel("是否只读");
		readOnly.setVoField("readonlys");
		list.add(readOnly);
		
		ComboPropertyInfo textalign = new ComboPropertyInfo();
		textalign.setId("textAlign");
		textalign.setType(StringDataTypeConst.STRING);
		textalign.setVisible(true);
		textalign.setEditable(true);
		textalign.setKeys(new String[]{"左", "中", "右"});
		textalign.setValues(new String[]{"left", "center", "right"});
		textalign.setDsField("combo_ext6");
		textalign.setLabel("标签位置");
		textalign.setVoField("textalign");
		list.add(textalign);
		
		IntegerPropertyInfo textWidth = new IntegerPropertyInfo();
		textWidth.setId("textWidth");
		textWidth.setVisible(true);
		textWidth.setEditable(true);
		textWidth.setType(StringDataTypeConst.INT);
		textWidth.setDsField("integer_ext1");
		textWidth.setLabel("标签宽度");
		textWidth.setVoField("textwidth");
		list.add(textWidth);
		
		IntegerPropertyInfo sepWidth = new IntegerPropertyInfo();
		sepWidth.setId("sepWidth");
		sepWidth.setVisible(true);
		sepWidth.setEditable(true);
		sepWidth.setType(StringDataTypeConst.INT);
		sepWidth.setDsField("integer_ext2");
		sepWidth.setLabel("间距");
		sepWidth.setVoField("sepwidth");
		list.add(sepWidth);
		
		IntegerPropertyInfo tabIndex = new IntegerPropertyInfo();
		tabIndex.setId("tabIndex");
		tabIndex.setVisible(true);
		tabIndex.setEditable(true);
		tabIndex.setType(StringDataTypeConst.INT);
		tabIndex.setDsField("integer_ext3");
		tabIndex.setLabel("tabIndex");
		tabIndex.setVoField("tabindex");
		list.add(tabIndex);
		
		StringPropertyInfo comboDataId = new StringPropertyInfo();
		comboDataId.setId("comboDataId");
		comboDataId.setEditable(true);
		comboDataId.setVisible(true);
		comboDataId.setDsField("string_ext4");
		comboDataId.setVoField("combodataid");
		comboDataId.setLabel("引用下拉框数据");
		list.add(comboDataId);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext5");
		width.setVoField("width");
		width.setLabel("宽");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setEditable(true);
		height.setVisible(true);
		height.setDsField("string_ext6");
		height.setVoField("height");
		height.setLabel("高");
		list.add(height);
		
		StringPropertyInfo left = new StringPropertyInfo();
		left.setId("left");
		left.setEditable(true);
		left.setVisible(false);
		left.setDsField("string_ext7");
		left.setLabel("左边距");
		left.setVoField("ileft");
		list.add(left);
		
		StringPropertyInfo position = new StringPropertyInfo();
		position.setId("position");
		position.setEditable(true);
		position.setVisible(true);
		position.setDsField("string_ext8");
		position.setLabel("位置");
		position.setVoField("positions");
		list.add(position);
		
		StringPropertyInfo contextmenu = new StringPropertyInfo();
		contextmenu.setId("contextMenu");
		contextmenu.setEditable(true);
		contextmenu.setVisible(true);
		contextmenu.setDsField("string_ext9");
		contextmenu.setLabel("弹出菜单");
		contextmenu.setVoField("contextmenu");
		list.add(contextmenu);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setEditable(true);
		classname.setVisible(true);
		classname.setDsField("string_ext10");
		classname.setLabel("自定义主题");
		classname.setVoField("classname");
		list.add(classname);
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setEditable(true);
		langdir.setVisible(true);
		langdir.setDsField("string_ext11");
		langdir.setLabel("多语目录");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo i18nname = new StringPropertyInfo();
		i18nname.setId("i18nName");
		i18nname.setEditable(true);
		i18nname.setVisible(true);
		i18nname.setDsField("string_ext12");
		i18nname.setLabel("多语显示值");
		i18nname.setVoField("i18nname");
		list.add(i18nname);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setEditable(true);
		text.setVisible(true);
		text.setDsField("string_ext13");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo value = new StringPropertyInfo();
		value.setId("value");
		value.setEditable(true);
		value.setVisible(true);
		value.setDsField("string_ext14");
		value.setLabel("输入值");
		value.setVoField("value");
		list.add(value);
		
		StringPropertyInfo editorType = new StringPropertyInfo();
		editorType.setId("editorType");
		editorType.setEditable(true);
		editorType.setVisible(true);
		editorType.setDsField("string_ext15");
		editorType.setLabel("编辑类型");
		editorType.setVoField("editortype");
		list.add(editorType);
		
		StringPropertyInfo parentId = new StringPropertyInfo();
		parentId.setId("");
		parentId.setEditable(true);
		parentId.setVisible(false);
		parentId.setDsField("parentid");
		parentId.setLabel("父ID");
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
