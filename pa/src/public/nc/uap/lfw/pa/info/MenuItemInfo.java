/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-8-29
 * @since 1.6
 */
public class MenuItemInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();

	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	static{
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setVisible(true);
		text.setEditable(true);
		text.setDsField("string_ext4");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo i18n = new StringPropertyInfo();
		i18n.setId("i18nName");
		i18n.setVisible(true);
		i18n.setEditable(true);
		i18n.setDsField("string_ext5");
		i18n.setLabel("多语显示值");
		i18n.setVoField("i18nname");
		list.add(i18n);
		
		StringPropertyInfo oper = new StringPropertyInfo();
		oper.setId("operatorStatusArray");
		oper.setVisible(true);
		oper.setEditable(true);
		oper.setDsField("string_ext6");
		oper.setLabel("操作状态");
		oper.setVoField("operatorstatusarray");
		
		StringPropertyInfo busi = new StringPropertyInfo();
		busi.setId("businessStatusArray");
		busi.setVisible(true);
		busi.setEditable(true);
		busi.setDsField("string_ext7");
		busi.setLabel("业务状态");
		busi.setVoField("businessstatusArray");
		list.add(busi);
		
//		StringPropertyInfo op = new StringPropertyInfo();
//		op.setId("operatorVisibleStatusArray");
//		op.setVisible(true);
//		op.setEditable(true);
//		op.setDsField("string_ext8");
//		op.setLabel("operatorVisibleStatusArray");
//		op.setVoField("operatorvisiblestatusarray");
//		list.add(op);
//		
//		StringPropertyInfo bus = new StringPropertyInfo();
//		bus.setId("businessVisibleStatusArray");
//		bus.setVisible(true);
//		bus.setEditable(true);
//		bus.setDsField("string_ext9");
//		bus.setLabel("businessVisibleStatusArray");
//		bus.setVoField("businessvisiblestatusarray");
//		list.add(bus);
//		
//		StringPropertyInfo ext= new StringPropertyInfo();
//		ext.setId("extendStatusArray");
//		ext.setVisible(true);
//		ext.setEditable(true);
//		ext.setDsField("string_ext10");
//		ext.setLabel("extendStatusArray");
//		ext.setVoField("extendstatusarray");
//		list.add(ext);
		
		StringPropertyInfo tip = new StringPropertyInfo();
		tip.setId("tip");
		tip.setVisible(true);
		tip.setEditable(true);
		tip.setDsField("string_ext11");
		tip.setLabel("提示");
		tip.setVoField("tip");
		list.add(tip);
		
		StringPropertyInfo tipi18nname = new StringPropertyInfo();
		tipi18nname.setId("tipI18nName");
		tipi18nname.setVisible(true);
		tipi18nname.setEditable(true);
		tipi18nname.setDsField("string_ext12");
		tipi18nname.setLabel("提示多语");
		tipi18nname.setVoField("tipi18nname");
		list.add(tipi18nname);
		
//		ComboPropertyInfo sel = new ComboPropertyInfo();
//		sel.setId("selected");
//		sel.setVisible(true);
//		sel.setEditable(true);
//		sel.setType(StringDataTypeConst.bOOLEAN);
//		sel.setKeys(KEYS);
//		sel.setValues(VALUES);
//		sel.setDsField("combo_ext1");
//		sel.setLabel("Selected");
//		sel.setVoField("selected");
//		list.add(sel);
		
//		ComboPropertyInfo chkboxgrp = new ComboPropertyInfo();
//		chkboxgrp.setId("checkBoxGroup");
//		chkboxgrp.setVisible(true);
//		chkboxgrp.setEditable(true);
//		chkboxgrp.setType(StringDataTypeConst.bOOLEAN);
//		chkboxgrp.setKeys(KEYS);
//		chkboxgrp.setValues(VALUES);
//		chkboxgrp.setDsField("combo_ext2");
//		chkboxgrp.setLabel("checkBoxGroup");
//		chkboxgrp.setVoField("checkboxgroup");
//		list.add(chkboxgrp);
		
//		StringPropertyInfo tag = new StringPropertyInfo();
//		tag.setId("tag");
//		tag.setVisible(true);
//		tag.setEditable(true);
//		tag.setDsField("string_ext13");
//		tag.setLabel("tag");
//		tag.setVoField("tag");
//		list.add(tag);
		
		StringPropertyInfo hotkey = new StringPropertyInfo();
		hotkey.setId("hotKey");
		hotkey.setVisible(true);
		hotkey.setEditable(true);
		hotkey.setDsField("string_ext14");
		hotkey.setLabel("热键");
		hotkey.setVoField("hotkey");
		list.add(hotkey);
		
		StringPropertyInfo dsphotkey = new StringPropertyInfo();
		dsphotkey.setId("displayHotKey");
		dsphotkey.setVisible(true);
		dsphotkey.setEditable(true);
		dsphotkey.setDsField("string_ext22");
		dsphotkey.setLabel("显示热键");
		dsphotkey.setVoField("displayhotkey");
		list.add(dsphotkey);
		
		IntegerPropertyInfo modifiers = new IntegerPropertyInfo();
		modifiers.setId("modifiers");
		modifiers.setVisible(true);
		modifiers.setEditable(true);
		modifiers.setType(StringDataTypeConst.INT);
		modifiers.setDsField("integer_ext1");
		modifiers.setLabel("modifiers");
		modifiers.setVoField("modifiers");
		list.add(modifiers);
		
		StringPropertyInfo imgicon = new StringPropertyInfo();
		imgicon.setId("imgIcon");
		imgicon.setVisible(true);
		imgicon.setEditable(true);
		imgicon.setDsField("string_ext15");
		imgicon.setLabel("imgIcon");
		imgicon.setVoField("imgicon");
		list.add(imgicon);
		
		StringPropertyInfo imgiconon = new StringPropertyInfo();
		imgiconon.setId("imgIconOn");
		imgiconon.setVisible(true);
		imgiconon.setEditable(true);
		imgiconon.setDsField("string_ext16");
		imgiconon.setLabel("imgIconOn");
		imgiconon.setVoField("imgiconon");
		list.add(imgiconon);
		
		StringPropertyInfo imgicondisable = new StringPropertyInfo();
		imgicondisable.setId("imgIconDisable");
		imgicondisable.setVisible(true);
		imgicondisable.setEditable(true);
		imgicondisable.setDsField("string_ext17");
		imgicondisable.setLabel("imgIconDisable");
		imgicondisable.setVoField("imgicondisable");
		list.add(imgicondisable);
		
		ComboPropertyInfo imgiconchanged = new ComboPropertyInfo();
		imgiconchanged.setId("imgIconChanged");
		imgiconchanged.setVisible(true);
		imgiconchanged.setEditable(true);
		imgiconchanged.setKeys(KEYS);
		imgiconchanged.setValues(VALUES);
		imgiconchanged.setDsField("combo_ext3");
		imgiconchanged.setLabel("imgIconChanged");
		imgiconchanged.setVoField("imgiconchanged");
		list.add(imgiconchanged);
		
		StringPropertyInfo realimgicon = new StringPropertyInfo();
		realimgicon.setId("realImgIcon");
		realimgicon.setVisible(true);
		realimgicon.setEditable(true);
		realimgicon.setDsField("string_ext18");
		realimgicon.setLabel("realImgIcon");
		realimgicon.setVoField("realimgicon");
		list.add(realimgicon);
		
		ComboPropertyInfo imgonchg = new ComboPropertyInfo();
		imgonchg.setId("imgIconOnChanged");
		imgonchg.setVisible(true);
		imgonchg.setEditable(true);
		imgonchg.setType(StringDataTypeConst.bOOLEAN);
		imgonchg.setKeys(KEYS);
		imgonchg.setValues(VALUES);
		imgonchg.setDsField("combo_ext4");
		imgonchg.setLabel("imgIconOnChanged");
		imgonchg.setVoField("imgicononchanged");
		list.add(imgonchg);
		
		StringPropertyInfo realimgiconon = new StringPropertyInfo();
		realimgiconon.setId("realImgIconOn");
		realimgiconon.setVisible(true);
		realimgiconon.setEditable(true);
		realimgiconon.setDsField("string_ext19");
		realimgiconon.setLabel("realImgIconOn");
		realimgiconon.setVoField("realimgiconon");
		list.add(realimgiconon);
		
		ComboPropertyInfo imgdischg = new ComboPropertyInfo();
		imgdischg.setId("imgIconDisableChanged");
		imgdischg.setVisible(true);
		imgdischg.setEditable(true);
		imgdischg.setType(StringDataTypeConst.bOOLEAN);
		imgdischg.setKeys(KEYS);
		imgdischg.setValues(VALUES);
		imgdischg.setDsField("combo_ext5");
		imgdischg.setLabel("imgIconDisableChanged");
		imgdischg.setVoField("imgicondisablechanged");
		list.add(imgdischg);
		
		StringPropertyInfo realimgicondisable = new StringPropertyInfo();
		realimgicondisable.setId("realImgIconDisable");
		realimgicondisable.setVisible(true);
		realimgicondisable.setEditable(true);
		realimgicondisable.setDsField("string_ext20");
		realimgicondisable.setLabel("realImgIconDisable");
		realimgicondisable.setVoField("realimgicondisable");
		list.add(realimgicondisable);
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setVisible(true);
		langdir.setEditable(true);
		langdir.setDsField("string_ext21");
		langdir.setLabel("多语目录");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setDsField("string_ext22");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(KEYS);
		vis.setValues(VALUES);
		vis.setDsField("combo_ext6");
		vis.setLabel("是否可见");
		vis.setVoField("visible");
		list.add(vis);
		
		ComboPropertyInfo sep = new ComboPropertyInfo();
		sep.setId("sep");
		sep.setVisible(true);
		sep.setEditable(true);
		sep.setType(StringDataTypeConst.bOOLEAN);
		sep.setKeys(KEYS);
		sep.setValues(VALUES);
		sep.setDsField("combo_ext7");
		sep.setLabel("sep");
		sep.setVoField("sep");
		list.add(sep);
		
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
