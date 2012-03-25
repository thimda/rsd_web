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
public class PanelLayoutInfo extends LayoutInfo {
	
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();

	static{
		StringPropertyInfo left = new StringPropertyInfo();
		left.setId("left");
		left.setVisible(false);
		left.setEditable(true);
		left.setDsField("string_ext3");
		left.setLabel("左边距");
		left.setVoField("ileft");
		list.add(left);
		
		StringPropertyInfo top = new StringPropertyInfo();
		top.setId("top");
		top.setVisible(false);
		top.setEditable(true);
		top.setDsField("string_ext4");
		top.setLabel("顶层距");
		top.setVoField("itop");
		list.add(top);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext5");
		width.setLabel("宽");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext6");
		height.setLabel("高");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo position = new StringPropertyInfo();
		position.setId("position");
		position.setVisible(true);
		position.setEditable(true);
		position.setDsField("string_ext7");
		position.setLabel("位置");
		position.setVoField("positions");
		list.add(position);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setVisible(true);
		classname.setEditable(true);
		classname.setDsField("string_ext8");
		classname.setLabel("自定义主题");
		classname.setVoField("classname");
		list.add(classname);
		
		IntegerPropertyInfo transp = new IntegerPropertyInfo();
		transp.setId("transparent");
		transp.setVisible(true);
		transp.setEditable(true);
		transp.setType(StringDataTypeConst.INTEGER);
		transp.setDsField("integer_ext1");
		transp.setLabel("transparent");
		transp.setVoField("transparent");
		list.add(transp);
		
		
		StringPropertyInfo backgroundColor = new StringPropertyInfo();
		backgroundColor.setId("backgroundColor");
		backgroundColor.setVisible(true);
		backgroundColor.setEditable(true);
		backgroundColor.setDsField("string_ext9");
		backgroundColor.setLabel("背景颜色");
		backgroundColor.setVoField("bkgrdcolor");
		list.add(backgroundColor);
		
		IntegerPropertyInfo scroll = new IntegerPropertyInfo();
		scroll.setId("scroll");
		scroll.setVisible(true);
		scroll.setEditable(true);
		scroll.setType(StringDataTypeConst.INT);
		scroll.setDsField("integer_ext2");
		scroll.setLabel("scroll");
		scroll.setVoField("scroll");
		list.add(scroll);
		
		StringPropertyInfo paddingtop = new StringPropertyInfo();
		paddingtop.setId("paddingTop");
		paddingtop.setVisible(false);
		paddingtop.setEditable(true);
		paddingtop.setDsField("string_ext10");
		paddingtop.setLabel("paddingTop");
		paddingtop.setVoField("paddingtop");
		list.add(paddingtop);
		
		StringPropertyInfo paddingbottom = new StringPropertyInfo();
		paddingbottom.setId("paddingBottom");
		paddingbottom.setVisible(false);
		paddingbottom.setEditable(true);
		paddingbottom.setDsField("string_ext11");
		paddingbottom.setLabel("paddingBottom");
		paddingbottom.setVoField("paddingbottom");
		list.add(paddingbottom);
		
		StringPropertyInfo paddingleft = new StringPropertyInfo();
		paddingleft.setId("paddingLeft");
		paddingleft.setVisible(false);
		paddingleft.setEditable(true);
		paddingleft.setDsField("string_ext12");
		paddingleft.setLabel("paddingLeft");
		paddingleft.setVoField("paddingleft");
		list.add(paddingleft);
		
		StringPropertyInfo paddingright = new StringPropertyInfo();
		paddingright.setId("paddingRight");
		paddingright.setVisible(false);
		paddingright.setEditable(true);
		paddingright.setDsField("string_ext13");
		paddingright.setLabel("paddingRight");
		paddingright.setVoField("paddingright");
		list.add(paddingright);
		
		IntegerPropertyInfo roundrect = new IntegerPropertyInfo();
		roundrect.setId("roundRect");
		roundrect.setVisible(true);
		roundrect.setEditable(true);
		roundrect.setType(StringDataTypeConst.INTEGER);
		roundrect.setDsField("integer_ext3");
		roundrect.setLabel("roundRect");
		roundrect.setVoField("roundrect");
		list.add(roundrect);
		
		StringPropertyInfo radius = new StringPropertyInfo();
		radius.setId("radius");
		radius.setVisible(true);
		radius.setEditable(true);
		radius.setDsField("string_ext14");
		radius.setLabel("radius");
		radius.setVoField("radius");
		list.add(radius);
		
		IntegerPropertyInfo withborder = new IntegerPropertyInfo();
		withborder.setId("withBorder");
		withborder.setVisible(true);
		withborder.setEditable(true);
		withborder.setType(StringDataTypeConst.INTEGER);
		withborder.setDsField("integer_ext4");
		withborder.setLabel("withBorder");
		withborder.setVoField("withborder");
		list.add(withborder);
		
		StringPropertyInfo borderwidth = new StringPropertyInfo();
		borderwidth.setId("borderWidth");
		borderwidth.setVisible(true);
		borderwidth.setEditable(true);
		borderwidth.setDsField("string_ext15");
		borderwidth.setLabel("borderWidth");
		borderwidth.setVoField("borderwidth");
		list.add(borderwidth);
		
		StringPropertyInfo bordercolor = new StringPropertyInfo();
		bordercolor.setId("borderColor");
		bordercolor.setVisible(true);
		bordercolor.setEditable(true);
		bordercolor.setDsField("string_ext16");
		bordercolor.setLabel("borderColor");
		bordercolor.setVoField("bordercolor");
		list.add(bordercolor);
		
		StringPropertyInfo title = new StringPropertyInfo();
		title.setId("title");
		title.setVisible(true);
		title.setEditable(true);
		title.setDsField("string_ext17");
		title.setLabel("title");
		title.setVoField("title");
		list.add(title);
		
		IntegerPropertyInfo display = new IntegerPropertyInfo();
		display.setId("display");
		display.setVisible(true);
		display.setEditable(true);
		display.setType(StringDataTypeConst.INTEGER);
		display.setDsField("integer_ext5");
		display.setLabel("display");
		display.setVoField("display");
		list.add(display);
		
		IntegerPropertyInfo visibility = new IntegerPropertyInfo();
		visibility.setId("visibility");
		visibility.setVisible(true);
		visibility.setEditable(true);
		visibility.setDsField("integer_ext6");
		visibility.setLabel("visibility");
		visibility.setVoField("visibility");
		list.add(visibility);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
		parentid.setVoField("parentid");
		list.add(parentid);
		
		
	}
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
