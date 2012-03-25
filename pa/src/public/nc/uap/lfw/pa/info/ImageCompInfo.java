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
public class ImageCompInfo extends ControlInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>(); 
	
	static{
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setKeys(new String[]{"是","否"});
		vis.setValues(new String[]{"Y", "N"});
		vis.setDsField("combo_ext1");
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setLabel("是否可见");
		vis.setVoField("visibles");
		vis.setVisible(true);
		vis.setEditable(true);
		list.add(vis);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setKeys(new String[]{"是","否"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setDsField("combo_ext2");
		ena.setLabel("s是否可用");
		ena.setVoField("enableds");
		ena.setVisible(true);
		ena.setEditable(true);
		list.add(ena);
		
		ComboPropertyInfo image1changed = new ComboPropertyInfo();
		image1changed.setId("image1Changed");
		image1changed.setKeys(new String[]{"是","否"});
		image1changed.setValues(new String[]{"Y", "N"});
		image1changed.setType(StringDataTypeConst.bOOLEAN);
		image1changed.setDsField("combo_ext3");
		image1changed.setLabel("image1Changed");
		image1changed.setVoField("image1changeds");
		image1changed.setVisible(true);
		image1changed.setEditable(true);
		list.add(image1changed);
		
		ComboPropertyInfo image2changed = new ComboPropertyInfo();
		image2changed.setId("image2Changed");
		image2changed.setKeys(new String[]{"是","否"});
		image2changed.setValues(new String[]{"Y", "N"});
		image2changed.setType(StringDataTypeConst.bOOLEAN);
		image2changed.setDsField("combo_ext4");
		image2changed.setLabel("image2Changed");
		image2changed.setVoField("image2changeds");
		image2changed.setVisible(true);
		image2changed.setEditable(true);
		list.add(image2changed);
		
		ComboPropertyInfo floatLeft = new ComboPropertyInfo();
		floatLeft.setId("floatLeft");
		floatLeft.setKeys(new String[]{"是","否"});
		floatLeft.setValues(new String[]{"Y", "N"});
		floatLeft.setType(StringDataTypeConst.bOOLEAN);
		floatLeft.setDsField("combo_ext5");
		floatLeft.setLabel("floatLeft");
		floatLeft.setVoField("floatlefts");
		floatLeft.setVisible(true);
		floatLeft.setEditable(true);
		list.add(floatLeft);
		
		ComboPropertyInfo floatRight = new ComboPropertyInfo();
		floatRight.setId("floatRight");
		floatRight.setKeys(new String[]{"是","否"});
		floatRight.setValues(new String[]{"Y", "N"});
		floatRight.setType(StringDataTypeConst.bOOLEAN);
		floatRight.setDsField("combo_ext6");
		floatRight.setLabel("floatRight");
		floatRight.setVoField("floatrights");
		floatRight.setVisible(true);
		floatRight.setEditable(true);
		list.add(floatRight);
		
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext4");
		width.setLabel("宽");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext5");
		height.setLabel("高");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo contextmenu = new StringPropertyInfo();
		contextmenu.setId("contextMenu");
		contextmenu.setVisible(true);
		contextmenu.setEditable(true);
		contextmenu.setDsField("string_ext6");
		contextmenu.setLabel("弹出菜单");
		contextmenu.setVoField("contextmenu");
		list.add(contextmenu);
		
		StringPropertyInfo position = new StringPropertyInfo();
		position.setId("position");
		position.setVisible(true);
		position.setEditable(true);
		position.setDsField("string_ext7");
		position.setLabel("位置");
		position.setVoField("positions");
		list.add(position);
		
		StringPropertyInfo top = new StringPropertyInfo();
		top.setId("top");
		top.setVisible(false);
		top.setEditable(true);
		top.setDsField("string_ext8");
		top.setLabel("顶层距");
		top.setVoField("itop");
		list.add(top);
		
		StringPropertyInfo left = new StringPropertyInfo();
		left.setId("left");
		left.setVisible(false);
		left.setEditable(true);
		left.setDsField("string_ext9");
		left.setLabel("左边距");
		left.setVoField("ileft");
		list.add(left);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setVisible(true);
		classname.setEditable(true);
		classname.setDsField("string_ext10");
		classname.setLabel("自定义主题");
		classname.setVoField("classname");
		list.add(classname);
		
		StringPropertyInfo alt = new StringPropertyInfo();
		alt.setId("alt");
		alt.setVisible(true);
		alt.setEditable(true);
		alt.setDsField("string_ext11");
		alt.setLabel("alt");
		alt.setVoField("alt");
		list.add(alt);
		
		StringPropertyInfo image1 = new StringPropertyInfo();
		image1.setId("image1");
		image1.setVisible(true);
		image1.setEditable(true);
		image1.setDsField("string_ext12");
		image1.setLabel("image1");
		image1.setVoField("image1");
		list.add(image1);
		
		StringPropertyInfo image2 = new StringPropertyInfo();
		image2.setId("image2");
		image2.setVisible(true);
		image2.setEditable(true);
		image2.setDsField("string_ext13");
		image2.setLabel("image2");
		image2.setVoField("image2");
		list.add(image2);
		
		StringPropertyInfo realImage1 = new StringPropertyInfo();
		realImage1.setId("realImage1");
		realImage1.setVisible(true);
		realImage1.setEditable(true);
		realImage1.setDsField("string_ext14");
		realImage1.setLabel("realImage1");
		realImage1.setVoField("realimage1");
		list.add(realImage1);
		
		StringPropertyInfo realImage2 = new StringPropertyInfo();
		realImage2.setId("realImage2");
		realImage2.setVisible(true);
		realImage2.setEditable(true);
		realImage2.setDsField("string_ext15");
		realImage2.setLabel("realImage2");
		realImage2.setVoField("realimage2");
		list.add(realImage2);
		
		StringPropertyInfo imageInact = new StringPropertyInfo();
		imageInact.setId("imageInact");
		imageInact.setVisible(true);
		imageInact.setEditable(true);
		imageInact.setDsField("string_ext16");
		imageInact.setLabel("imageInact");
		imageInact.setVoField("imageinact");
		list.add(imageInact);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
		parentid.setVoField("parentid");
		list.add(parentid);
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
