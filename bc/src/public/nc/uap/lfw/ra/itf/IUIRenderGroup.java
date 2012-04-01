package nc.uap.lfw.ra.itf;

import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * @author renxh
 * ��Ⱦ��ӿڣ����Ի��һ�����豸��ص���Ⱦ��
 *
 */
public interface IUIRenderGroup {
	/**
	 * 2011-7-19 ����01:20:05 renxh des���������ͻ����Ӧ��uirender
	 * 
	 * @param uiEle
	 * @param pageMeta
	 * @return
	 */
	public IUIRender getUIRender(UIMeta uimeta, UIElement uiEle, PageMeta pageMeta, IUIRender parentRender);
	
	/**
	 * 2011-8-2 ����07:15:42 renxh
	 * des������webElement���ͻ�ö�Ӧ��render
	 * @param webEle
	 * @param pageMeta
	 * @param parentRender
	 * @return
	 */
	public IUIRender getUIRender(UIMeta uimeta, UIElement uiEle,WebElement webEle, PageMeta pageMeta, IUIRender parentRender);

	/**
	 * 2011-7-19 ����01:36:32 renxh des���������ڵ�render
	 * 
	 * @param webEle
	 * @param pageMeta
	 * @return
	 */
	public IUIRender getContextMenuUIRender(UIMeta uimeta, UIElement uiEle,ContextMenuComp webEle, PageMeta pageMeta, IUIRender parentRender);
}
