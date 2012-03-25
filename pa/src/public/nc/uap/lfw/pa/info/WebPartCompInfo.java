package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

public class WebPartCompInfo extends ControlInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		
		StringPropertyInfo contentFetcher = new StringPropertyInfo();
		contentFetcher.setId("contentFetcher");
		contentFetcher.setEditable(true);
		contentFetcher.setVisible(true);
		contentFetcher.setDsField("string_ext4");
		contentFetcher.setVoField("contentfetcher");
		contentFetcher.setLabel("内容获取（类）");
		list.add(contentFetcher);
		
//		StringPropertyInfo left = new StringPropertyInfo();
//		left.setId("left");
//		left.setEditable(true);
//		left.setVisible(true);
//		left.setDsField("string_ext5");
//		left.setVoField("ileft");
//		left.setLabel("左边距");
//		list.add(left);
//		
//		StringPropertyInfo top = new StringPropertyInfo();
//		top.setId("top");
//		top.setEditable(true);
//		top.setVisible(true);
//		top.setDsField("string_ext6");
//		top.setVoField("itop");
//		top.setLabel("顶边距");
//		list.add(top);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext7");
		width.setVoField("width");
		width.setLabel("宽");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setEditable(true);
		height.setVisible(true);
		height.setDsField("string_ext8");
		height.setVoField("height");
		height.setLabel("高");
		list.add(height);
		
//		StringPropertyInfo position = new StringPropertyInfo();
//		position.setId("position");
//		position.setEditable(true);
//		position.setVisible(true);
//		position.setDsField("string_ext9");
//		position.setVoField("positions");
//		position.setLabel("位置");
//		list.add(position);
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setEditable(true);
		className.setVisible(true);
		className.setDsField("string_ext10");
		className.setVoField("classname");
		className.setLabel("主题");
		list.add(className);
		
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
