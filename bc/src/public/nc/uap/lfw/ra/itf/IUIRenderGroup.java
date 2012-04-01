package nc.uap.lfw.ra.itf;

import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * @author renxh
 * 渲染组接口，可以获得一组与设备相关的渲染器
 *
 */
public interface IUIRenderGroup {
	/**
	 * 2011-7-19 下午01:20:05 renxh des：根据类型获得相应的uirender
	 * 
	 * @param uiEle
	 * @param pageMeta
	 * @return
	 */
	public IUIRender getUIRender(UIMeta uimeta, UIElement uiEle, PageMeta pageMeta, IUIRender parentRender);
	
	/**
	 * 2011-8-2 下午07:15:42 renxh
	 * des：根据webElement类型获得对应的render
	 * @param webEle
	 * @param pageMeta
	 * @param parentRender
	 * @return
	 */
	public IUIRender getUIRender(UIMeta uimeta, UIElement uiEle,WebElement webEle, PageMeta pageMeta, IUIRender parentRender);

	/**
	 * 2011-7-19 下午01:36:32 renxh des：弹出窗口的render
	 * 
	 * @param webEle
	 * @param pageMeta
	 * @return
	 */
	public IUIRender getContextMenuUIRender(UIMeta uimeta, UIElement uiEle,ContextMenuComp webEle, PageMeta pageMeta, IUIRender parentRender);
}
