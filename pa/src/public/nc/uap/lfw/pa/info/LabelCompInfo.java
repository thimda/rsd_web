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
public class LabelCompInfo extends ControlInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setKeys(new String[]{"是","否"});
		vis.setValues(new String[]{"Y", "N"});
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setDsField("combo_ext1");
		vis.setLabel("是否可见");
		vis.setVoField("visibles");
		vis.setVisible(true);
		vis.setEditable(true);
		list.add(vis);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setKeys(new String[]{"是", "否"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setDsField("combo_ext4");
		ena.setLabel("是否可编辑");
		ena.setVoField("enableds");
		list.add(ena);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext4");
		width.setLabel("宽");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setEditable(true);
		height.setVisible(true);
		height.setDsField("string_ext5");
		width.setLabel("高");
		height.setVoField("height");
		list.add(height);
		
//		StringPropertyInfo top = new StringPropertyInfo();
//		top.setId("top");
//		top.setEditable(true);
//		top.setVisible(false);
//		top.setDsField("string_ext6");
//		width.setLabel("顶层距");
//		top.setVoField("itop");
//		list.add(top);
//		
//		StringPropertyInfo left = new StringPropertyInfo();
//		left.setId("left");
//		left.setEditable(true);
//		left.setVisible(false);
//		left.setDsField("string_ext7");
//		left.setLabel("左边距");
//		left.setVoField("ileft");
//		list.add(left);
		
//		StringPropertyInfo position = new StringPropertyInfo();
//		position.setId("position");
//		position.setEditable(true);
//		position.setVisible(true);
//		position.setDsField("string_ext8");
//		position.setLabel("位置");
//		position.setVoField("positions");
//		list.add(position);
		
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
		
		StringPropertyInfo i18nname = new StringPropertyInfo();
		i18nname.setId("i18nName");
		i18nname.setEditable(true);
		i18nname.setVisible(true);
		i18nname.setDsField("string_ext11");
		i18nname.setLabel("多语显示值");
		i18nname.setVoField("i18nname");
		list.add(i18nname);
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setEditable(true);
		langdir.setVisible(true);
		langdir.setDsField("string_ext12");
		langdir.setLabel("多语目录");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setEditable(true);
		text.setVisible(true);
		text.setDsField("string_ext13");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo innerhtml = new StringPropertyInfo();
		innerhtml.setId("innerHTML");
		innerhtml.setEditable(true);
		innerhtml.setVisible(true);
		innerhtml.setDsField("string_ext14");
		innerhtml.setLabel("HTML内容");
		innerhtml.setVoField("innerhtml");
		list.add(innerhtml);
		
		StringPropertyInfo color = new StringPropertyInfo();
		color.setId("color");
		color.setEditable(true);
		color.setVisible(true);
		color.setDsField("string_ext15");
		color.setLabel("字体颜色");
		color.setVoField("color");
		list.add(color);
		
		StringPropertyInfo style = new StringPropertyInfo();
		style.setId("style");
		style.setEditable(true);
		style.setVisible(true);
		style.setDsField("string_ext16");
		style.setLabel("字体样式");
		style.setVoField("style");
		//list.add(style);
		
		StringPropertyInfo weight = new StringPropertyInfo();
		weight.setId("weight");
		weight.setEditable(true);
		weight.setVisible(true);
		weight.setDsField("string_ext17");
		weight.setLabel("字体宽度");
		weight.setVoField("weight");
		//list.add(weight);
		
		StringPropertyInfo size = new StringPropertyInfo();
		size.setId("size");
		size.setEditable(true);
		size.setVisible(true);
		size.setDsField("string_ext18");
		size.setLabel("字体大小");
		size.setVoField("size");
		//list.add(size);
		
		StringPropertyInfo family = new StringPropertyInfo();
		family.setId("family");
		family.setEditable(true);
		family.setVisible(true);
		family.setDsField("string_ext19");
		family.setLabel("字体");
		family.setVoField("family");
		//list.add(family);
		
		ComboPropertyInfo textAlign = new ComboPropertyInfo();
		textAlign.setId("textAlign");
		textAlign.setKeys(new String[]{"左", "中", "右"});
		textAlign.setValues(new String[]{"left", "center", "right"});
		textAlign.setType(StringDataTypeConst.STRING);
		textAlign.setVisible(true);
		textAlign.setEditable(true);
		textAlign.setDsField("combo_ext5");
		textAlign.setLabel("文字位置");
		textAlign.setVoField("textalign");
		list.add(textAlign);
		
//		StringPropertyInfo parentid = new StringPropertyInfo();
//		parentid.setId("");
//		parentid.setVisible(false);
//		parentid.setEditable(true);
//		parentid.setDsField("parentid");
//		parentid.setLabel("父ID");
//		parentid.setVoField("parentid");
//		list.add(parentid);
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
