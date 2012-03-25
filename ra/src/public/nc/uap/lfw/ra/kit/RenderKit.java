package nc.uap.lfw.ra.kit;

import nc.uap.lfw.ra.group.PCUIRenderGroup;
import nc.uap.lfw.ra.itf.IUIRenderGroup;

public class RenderKit {
	
	public static final String RENDER_PC = "PC";
	public static final String RENDER_IPHONE = "IPHONE";
	public static final String RENDER_IPAD = "IPAD";
	public static final String RENDER_HTML5 = "HTML5";
	
	
	/**
	 * 2011-8-2 下午07:14:40 renxh
	 * des：获得渲染器工具类，根据设备可以获取一系列渲染器
	 * @param renderType
	 * @return
	 */
	public static IUIRenderGroup getUIRenderGroup(String renderType){
		IUIRenderGroup group = null;
		if(renderType.equals(RENDER_PC)){
			group = new PCUIRenderGroup();
		}else if(renderType.equals(RENDER_IPHONE)){
			
		}
		return group;
	}
	

}
