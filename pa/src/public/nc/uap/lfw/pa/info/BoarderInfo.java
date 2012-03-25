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
public class BoarderInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();

	static{
		StringPropertyInfo color = new StringPropertyInfo();
		color.setId("color");
		color.setVisible(true);
		color.setEditable(true);
		color.setDsField("string_ext3");
		color.setLabel("color");
		color.setVoField("color");
		list.add(color);
		
		IntegerPropertyInfo showl = new IntegerPropertyInfo();
		showl.setId("showLeft");
		showl.setVisible(true);
		showl.setEditable(true);
		showl.setType(StringDataTypeConst.INT);
		showl.setDsField("integer_ext1");
		showl.setLabel("showLeft");
		showl.setVoField("showleft");
		list.add(showl);
		
		IntegerPropertyInfo showr = new IntegerPropertyInfo();
		showr.setId("showRight");
		showr.setVisible(true);
		showr.setEditable(true);
		showr.setType(StringDataTypeConst.INT);
		showr.setDsField("integer_ext2");
		showr.setLabel("showRight");
		showr.setVoField("showright");
		list.add(showr);
		
		IntegerPropertyInfo showt = new IntegerPropertyInfo();
		showt.setId("showTop");
		showt.setVisible(true);
		showt.setEditable(true);
		showt.setType(StringDataTypeConst.INT);
		showt.setDsField("integer_ext3");
		showt.setLabel("showTop");
		showt.setVoField("showtop");
		list.add(showt);
		
		IntegerPropertyInfo showb = new IntegerPropertyInfo();
		showb.setId("showBottom");
		showb.setVisible(true);
		showb.setEditable(true);
		showb.setType(StringDataTypeConst.INT);
		showb.setDsField("integer_ext4");
		showb.setLabel("showBottom");
		showb.setVoField("showbottom");
		list.add(showb);
		
		StringPropertyInfo lcolor = new StringPropertyInfo();
		lcolor.setId("leftColor");
		lcolor.setVisible(true);
		lcolor.setEditable(true);
		lcolor.setDsField("string_ext4");
		lcolor.setLabel("leftColor");
		lcolor.setVoField("leftcolor");
		list.add(lcolor);
		
		StringPropertyInfo rcolor = new StringPropertyInfo();
		rcolor.setId("rightColor");
		rcolor.setVisible(true);
		rcolor.setEditable(true);
		rcolor.setDsField("string_ext5");
		rcolor.setLabel("rightColor");
		rcolor.setVoField("rightcolor");
		list.add(rcolor);
		
		StringPropertyInfo tcolor = new StringPropertyInfo();
		tcolor.setId("topColor");
		tcolor.setVisible(true);
		tcolor.setEditable(true);
		tcolor.setDsField("string_ext6");
		tcolor.setLabel("topColor");
		tcolor.setVoField("topcolor");
		list.add(tcolor);
		
		StringPropertyInfo bcolor = new StringPropertyInfo();
		bcolor.setId("bottomColor");
		bcolor.setVisible(true);
		bcolor.setEditable(true);
		bcolor.setDsField("string_ext7");
		bcolor.setLabel("bottomColor");
		bcolor.setVoField("bottomcolor");
		list.add(bcolor);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext8");
		width.setLabel("width");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo lwidth = new StringPropertyInfo();
		lwidth.setId("leftWidth");
		lwidth.setVisible(true);
		lwidth.setEditable(true);
		lwidth.setDsField("string_ext9");
		lwidth.setLabel("leftWidth");
		lwidth.setVoField("leftwidth");
		list.add(lwidth);
		
		StringPropertyInfo rwidth = new StringPropertyInfo();
		rwidth.setId("rightWidth");
		rwidth.setVisible(true);
		rwidth.setEditable(true);
		rwidth.setDsField("string_ext10");
		rwidth.setLabel("rightWidth");
		rwidth.setVoField("rightwidth");
		list.add(rwidth);
		
		StringPropertyInfo twidth = new StringPropertyInfo();
		twidth.setId("topWidth");
		twidth.setVisible(true);
		twidth.setEditable(true);
		twidth.setDsField("string_ext11");
		twidth.setLabel("topWidth");
		twidth.setVoField("topwidth");
		list.add(twidth);
		
		StringPropertyInfo bwidth = new StringPropertyInfo();
		bwidth.setId("bottomWidth");
		bwidth.setVisible(true);
		bwidth.setEditable(true);
		bwidth.setDsField("string_ext12");
		bwidth.setLabel("bottomWidth");
		bwidth.setVoField("bottomwidth");
		list.add(bwidth);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setVisible(true);
		classname.setEditable(true);
		classname.setDsField("string_ext13");
		classname.setLabel("className");
		classname.setVoField("classname");
		list.add(classname);
		
		StringPropertyInfo roundBorder = new StringPropertyInfo();
		roundBorder.setId("roundBorder");
		roundBorder.setVisible(true);
		roundBorder.setEditable(true);
		roundBorder.setDsField("string_ext14");
		roundBorder.setLabel("roundBorder");
		roundBorder.setVoField("roundborder");
		list.add(roundBorder);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("¸¸ID");
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
