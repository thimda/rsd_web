package nc.uap.lfw.ra.kit;

import nc.uap.lfw.ra.group.PCUIRenderGroup;
import nc.uap.lfw.ra.itf.IUIRenderGroup;

public class RenderKit {
	
	public static final String RENDER_PC = "PC";
	public static final String RENDER_IPHONE = "IPHONE";
	public static final String RENDER_IPAD = "IPAD";
	public static final String RENDER_HTML5 = "HTML5";
	
	
	/**
	 * 2011-8-2 ����07:14:40 renxh
	 * des�������Ⱦ�������࣬�����豸���Ի�ȡһϵ����Ⱦ��
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
