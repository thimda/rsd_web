/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public class ButtonInfo extends ControlInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>(); 
	static{
		
		
		ComboPropertyInfo cinfo = new ComboPropertyInfo();
		cinfo.setId("visible");
		cinfo.setKeys(new String[]{"是","否"});
		cinfo.setValues(new String[]{"Y", "N"});
//		cinfo.setDefaultValue("Y");
		cinfo.setDsField("combo_ext1");
		cinfo.setType(StringDataTypeConst.bOOLEAN);
		cinfo.setLabel("是否可见");
		cinfo.setVoField("visibles");
		cinfo.setVisible(true);
		cinfo.setEditable(true);
		list.add(cinfo);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setKeys(new String[]{"是", "否"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setDsField("combo_ext4");
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setLabel("是否可编辑");
		ena.setVoField("enableds");
		list.add(ena);
		
//		StringPropertyInfo top = new StringPropertyInfo();
//		top.setId("top");
//		top.setEditable(true);
//		top.setVisible(false);
//		top.setDsField("string_ext14");
//		top.setVoField("itop");
//		top.setLabel("顶层距");
//		list.add(top);
//		
//		StringPropertyInfo left = new StringPropertyInfo();
//		left.setId("left");
//		left.setVisible(false);
//		left.setEditable(true);
//		left.setDsField("string_ext15");
//		left.setLabel("左边距");
//		left.setVoField("ileft");
//		list.add(left);
		
		StringPropertyInfo contm = new StringPropertyInfo();
		contm.setId("contextMenu");
		contm.setVisible(true);
		contm.setEditable(true);
		contm.setDsField("string_ext4");
		contm.setLabel("弹出菜单");
		contm.setVoField("contextmenu");
		list.add(contm);

		StringPropertyInfo lang = new StringPropertyInfo();
		lang.setId("langDir");
		lang.setVisible(true);
		lang.setEditable(true);
		lang.setDsField("string_ext5");
		lang.setLabel("多语目录");
		lang.setVoField("langdir");
		list.add(lang);
		
		StringPropertyInfo tip = new StringPropertyInfo();
		tip.setId("tip");
		tip.setVisible(true);
		tip.setEditable(true);
		tip.setDsField("string_ext6");
		tip.setLabel("提示");
		tip.setVoField("tip");
		list.add(tip);
		
		StringPropertyInfo tin = new StringPropertyInfo();
		tin.setId("tipI18nName");
		tin.setVisible(true);
		tin.setEditable(true);
		tin.setDsField("string_ext7");
		tin.setLabel("多语提示");
		tin.setVoField("tipi18nname");
		list.add(tin);
		
		ComboPropertyInfo position = new ComboPropertyInfo();
		position.setId("position");
		position.setKeys(new String[]{"相对的", "绝对的"});
		position.setValues(new String[]{"relative", "positive"});
		position.setVisible(true);
		position.setEditable(true);
		position.setType(StringDataTypeConst.STRING);
		position.setDsField("combo_ext2");
		position.setLabel("位置");
		position.setVoField("positions");
		list.add(position);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext16");
		width.setVoField("width");
		width.setLabel("宽");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext17");
		height.setVoField("height");
		height.setLabel("高");
		list.add(height);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setVisible(true);
		text.setEditable(true);
		text.setDsField("string_ext8");
		text.setVoField("itext");
		text.setLabel("显示值");
		list.add(text);
		
		
		StringPropertyInfo hotkey = new StringPropertyInfo();
		hotkey.setId("hotKey");
		hotkey.setVisible(true);
		hotkey.setEditable(true);
		hotkey.setDsField("string_ext9");
		hotkey.setVoField("hotkey");
		hotkey.setLabel("热键");
		list.add(hotkey);
		
		StringPropertyInfo dhotkey = new StringPropertyInfo();
		dhotkey.setId("displayHotKey");
		dhotkey.setVisible(true);
		dhotkey.setEditable(true);
		dhotkey.setDsField("string_ext10");
		dhotkey.setVoField("displayhotkey");
		dhotkey.setLabel("热键显示值");
		list.add(dhotkey);
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setDsField("string_ext11");
		className.setVoField("classname");
		className.setLabel("自定义主题");
		list.add(className);
		
		StringPropertyInfo i18nName = new StringPropertyInfo();
		i18nName.setId("i18nName");
		i18nName.setVisible(true);
		i18nName.setEditable(true);
		i18nName.setDsField("string_ext12");
		i18nName.setLabel("多语显示值");
		i18nName.setVoField("i18nname");
		list.add(i18nName);
		
		StringPropertyInfo refImg = new StringPropertyInfo();
		refImg.setId("refImg");
		refImg.setVisible(true);
		refImg.setEditable(true);
		refImg.setDsField("string_ext13");
		refImg.setVoField("refimg");
		refImg.setLabel("引用图形");
		list.add(refImg);
		
//		StringPropertyInfo parentid = new StringPropertyInfo();
//		parentid.setId("");
//		parentid.setVisible(false);
//		parentid.setEditable(true);
//		parentid.setDsField("parentid");
//		parentid.setLabel("父ID");
//		parentid.setVoField("parentid");
//		list.add(parentid);
		
	}
	
	/* (non-Javadoc)
	 * @see nc.uap.lfw.pa.info.IBaseInfo#getPropertyInfos()
	 */
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
