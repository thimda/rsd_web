package nc.uap.lfw.ra.itf;

import java.io.IOException;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;


/**
 * @author renxh
 * 渲染接口
 *
 */
public interface IUIRender{
	
	/**
	 * 2011-8-2 下午08:01:50 renxh des：渲染 html
	 * 
	 * @return
	 */
	public String createRenderHtml();

	/**
	 * 2011-8-2 下午08:02:06 renxh des：渲染script
	 * 
	 * @return
	 */
	public String createRenderScript();
	
	/**
	 * 动态创建html占位符
	 * @return
	 */
	public String createRenderHtmlDynamic();
	
	/**
	 * 2011-7-29 下午04:42:34 renxh
	 * des： 渲染js代码
	 * @param writer
	 * @throws IOException
	 */
	public String createRenderScriptDynamic();
	
	public String getType();

	public String getDivId();
	
	public String getNewDivId();

	public void setDivId(String divId);
	
	public IControlPlugin getControlPlugin();

	public void setControlPlugin(IControlPlugin controlPlugin);
	
	/**
	 * 2011-10-8 上午09:06:03 renxh des：删除UIMeta中的元素时，执行删除脚本操作,remove为删除子元素
	 */

	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta, Object obj);

	/**
	 * 2011-10-8 上午09:06:40 renxh des：添加UIMeta时，执行此操作 // 添加子元素
	 */
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj);

	/**
	 * 销毁自身
	 * 
	 * @param uiMeta
	 * @param pageMeta
	 */
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj);

	/**
	 * 2011-10-8 上午09:07:30 renxh des：更新UIMeta中的元素时，执行此操作，更新自身的属性
	 */
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj);
	
	public IUIRender getParentRender();
	
	public String getId();
	
	public <T extends UIElement> T getUiElement();

	public <K extends WebElement> K getWebElement();

}
