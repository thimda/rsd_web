/**
 * 
 */
package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 */
public class SpliterLayoutInfo extends LayoutInfo {

	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();

	static{
		StringPropertyInfo divs = new StringPropertyInfo();
		divs.setId("divideSize");
		divs.setVisible(true);
		divs.setEditable(true);
		divs.setDsField("string_ext3");
		divs.setLabel("divideSize");
		divs.setVoField("dividesize");
		list.add(divs);
		
		StringPropertyInfo splw = new StringPropertyInfo();
		splw.setId("spliterWidth");
		splw.setVisible(true);
		splw.setEditable(true);
		splw.setDsField("string_ext4");
		splw.setLabel("spliterWidth");
		splw.setVoField("spliterwidth");
//		list.add(splw);
		
		IntegerPropertyInfo ori = new IntegerPropertyInfo();
		ori.setId("orientation");
		ori.setVisible(true);
		ori.setEditable(true);
		ori.setType(StringDataTypeConst.INTEGER);
		ori.setDsField("integer_ext1");
		ori.setLabel("orientation");
		ori.setVoField("orientation");
		list.add(ori);
		
		IntegerPropertyInfo bou = new IntegerPropertyInfo();
		bou.setId("boundMode");
		bou.setVisible(true);
		bou.setEditable(true);
		bou.setType(StringDataTypeConst.INTEGER);
		bou.setDsField("integer_ext2");
		bou.setLabel("boundMode");
		bou.setVoField("boundmode");
		list.add(bou);
		
		IntegerPropertyInfo oneTouch = new IntegerPropertyInfo();
		oneTouch.setId("oneTouch");
		oneTouch.setVisible(true);
		oneTouch.setEditable(true);
		oneTouch.setType(StringDataTypeConst.INTEGER);
		oneTouch.setDsField("integer_ext3");
		oneTouch.setLabel("oneTouch");
		oneTouch.setVoField("onetouch");
		list.add(oneTouch);
		
		IntegerPropertyInfo inverse = new IntegerPropertyInfo();
		inverse.setId("inverse");
		inverse.setVisible(true);
		inverse.setEditable(true);
		inverse.setType(StringDataTypeConst.INTEGER);
		inverse.setDsField("integer_ext4");
		inverse.setLabel("inverse");
		inverse.setVoField("inverse");
		list.add(inverse);
		
		IntegerPropertyInfo hidebar = new IntegerPropertyInfo();
		hidebar.setId("hideBar");
		hidebar.setVisible(true);
		hidebar.setEditable(true);
		hidebar.setType(StringDataTypeConst.INTEGER);
		hidebar.setDsField("integer_ext5");
		hidebar.setLabel("hideBar");
		hidebar.setVoField("hidebar");
		list.add(hidebar);
		
		IntegerPropertyInfo hidedirection = new IntegerPropertyInfo();
		hidedirection.setId("hideDirection");
		hidedirection.setVisible(true);
		hidedirection.setEditable(true);
		hidedirection.setType(StringDataTypeConst.INTEGER);
		hidedirection.setDsField("integer_ext6");
		hidedirection.setLabel("Òþ²Ø·½Ïò");
		hidedirection.setVoField("hidedirection");
		list.add(hidedirection);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("¸¸ID");
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
