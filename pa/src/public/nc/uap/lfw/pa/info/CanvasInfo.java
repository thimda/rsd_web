package nc.uap.lfw.pa.info;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.jsp.uimeta.UICanvas;

public class CanvasInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;
	private static final List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	static{
		
		ComboPropertyInfo className = new ComboPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setKeys(new String[]{"无","左","右","全"});
		className.setValues(new String[]{UICanvas.NULLCANVAS, UICanvas.LEFTCANVAS, UICanvas.RIGHTCANVAS, UICanvas.FULLCANVAS});
		className.setType(StringDataTypeConst.STRING);
		className.setDsField("combo_ext1");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);
/*		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setDsField("string_ext25");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);*/

	}
	
	public IPropertyInfo[] getPropertyInfos() {
		IPropertyInfo[] pinfos = super.getPropertyInfos();
		if(pinfos == null)
			return list.toArray(new IPropertyInfo[0]);
		else{
			int length = pinfos.length;
			
			List<IPropertyInfo> oldList = new ArrayList<IPropertyInfo>();
			
			
			for(int i = 0; i < pinfos.length; i ++){
				IPropertyInfo pinfo = pinfos[i];
				if(pinfo.getDsField().equals("string_ext25"))
					continue;
				oldList.add(pinfo);
				
			}
			IPropertyInfo[] newArr = new IPropertyInfo[list.size() + length - 1];
		
			System.arraycopy(list.toArray(), 0, newArr, 0, list.size());
			System.arraycopy(oldList.toArray(), 0, newArr, list.size(), length-1);
			return newArr;
		}
	}
}
