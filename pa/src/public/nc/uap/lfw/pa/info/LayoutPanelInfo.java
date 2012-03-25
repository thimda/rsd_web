/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public class LayoutPanelInfo extends BaseInfo {

	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		StringPropertyInfo widgetid = new StringPropertyInfo();
		widgetid.setId("widgetId");
		widgetid.setVisible(false);
		widgetid.setEditable(true);
		widgetid.setDsField("string_ext2");
		widgetid.setLabel("widgetId");
		widgetid.setVoField("widgetid");
		list.add(widgetid);
		
		StringPropertyInfo topPadding = new StringPropertyInfo();
		topPadding.setId("topPadding");
		topPadding.setVisible(true);
		topPadding.setEditable(true);
		topPadding.setDsField("string_ext16");
		topPadding.setLabel("ÉÏ±ß¾à");
		topPadding.setVoField("toppadding");
		list.add(topPadding);
		
		StringPropertyInfo bottomPadding = new StringPropertyInfo();
		bottomPadding.setId("bottomPadding");
		bottomPadding.setVisible(true);
		bottomPadding.setEditable(true);
		bottomPadding.setDsField("string_ext17");
		bottomPadding.setLabel("ÏÂ±ß¾à");
		bottomPadding.setVoField("bottompadding");
		list.add(bottomPadding);
		
		StringPropertyInfo leftPadding = new StringPropertyInfo();
		leftPadding.setId("leftPadding");
		leftPadding.setVisible(true);
		leftPadding.setEditable(true);
		leftPadding.setDsField("string_ext18");
		leftPadding.setLabel("×ó±ß¾à");
		leftPadding.setVoField("leftpadding");
		list.add(leftPadding);
		
		StringPropertyInfo rightPadding = new StringPropertyInfo();
		rightPadding.setId("rightPadding");
		rightPadding.setVisible(true);
		rightPadding.setEditable(true);
		rightPadding.setDsField("string_ext19");
		rightPadding.setLabel("ÓÒ±ß¾à");
		rightPadding.setVoField("rightpadding");
		list.add(rightPadding);
		
		StringPropertyInfo leftBorder = new StringPropertyInfo();
		leftBorder.setId("leftBorder");
		leftBorder.setVisible(true);
		leftBorder.setEditable(true);
		leftBorder.setDsField("string_ext20");
		leftBorder.setLabel("×ó±ß¿ò");
		leftBorder.setVoField("leftborder");
		list.add(leftBorder);
		
		StringPropertyInfo rightBorder = new StringPropertyInfo();
		rightBorder.setId("rightBorder");
		rightBorder.setVisible(true);
		rightBorder.setEditable(true);
		rightBorder.setDsField("string_ext21");
		rightBorder.setLabel("ÓÒ±ß¿ò");
		rightBorder.setVoField("rightborder");
		list.add(rightBorder);
		
		StringPropertyInfo topBorder = new StringPropertyInfo();
		topBorder.setId("topBorder");
		topBorder.setVisible(true);
		topBorder.setEditable(true);
		topBorder.setDsField("string_ext22");
		topBorder.setLabel("ÉÏ±ß¿ò");
		topBorder.setVoField("topborder");
		list.add(topBorder);
		
		StringPropertyInfo bottomBorder = new StringPropertyInfo();
		bottomBorder.setId("bottomBorder");
		bottomBorder.setVisible(true);
		bottomBorder.setEditable(true);
		bottomBorder.setDsField("string_ext23");
		bottomBorder.setLabel("ÏÂ±ß¿ò");
		bottomBorder.setVoField("bottomborder");
		list.add(bottomBorder);
		
		StringPropertyInfo border = new StringPropertyInfo();
		border.setId("border");
		border.setVisible(true);
		border.setEditable(true);
		border.setDsField("string_ext24");
		border.setLabel("±ß¿ò");
		border.setVoField("border");
		list.add(border);
		
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setDsField("string_ext25");
		className.setLabel("×Ô¶¨ÒåÖ÷Ìâ");
		className.setVoField("classname");
		list.add(className);
		
		StringPropertyInfo childtype = new StringPropertyInfo();
		childtype.setId("");
		childtype.setVisible(false);
		childtype.setEditable(true);
		childtype.setDsField("childtype");
		childtype.setLabel("×ÓÀàÐÍ");
		childtype.setVoField("childtype");
		list.add(childtype);
		
		
		StringPropertyInfo childid = new StringPropertyInfo();
		childid.setId("");
		childid.setVisible(false);
		childid.setEditable(true);
		childid.setDsField("childid");
		childid.setLabel("×ÓID");
		childid.setVoField("childid");
		list.add(childid);
		
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
