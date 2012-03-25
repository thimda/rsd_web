package nc.uap.lfw.core.ctrlfrm.plugin;

import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.ctrlfrm.seria.IControlSerializer;
import nc.uap.lfw.core.ctrlfrm.uiseria.IUISerializer;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;

public interface IControlPlugin{
	/**
	 * 获取依赖包
	 * @return
	 */
	public String[] getDependences();
	
	/**
	 * 获取全路径名
	 * @return
	 */
	public String getFullName();
	
	public String[] getImports(boolean optimized);
	
	public String[] getCssImports(boolean optimized);
	
	public String getCssFullName();
	
	/**
	 * 获取控件类型
	 * @return
	 */
	public String getId();
	
	public String[] calculateDependences(boolean optimized);
	
	public String[] calculateCssDependences(boolean optimized);
	
	public String[] calculateDependenceIds();
	
	/**
	 * 获取控件序列化器
	 * @return
	 */
	public <T>IControlSerializer<T> getControlSerializer();
	
	public <T>IUISerializer<T> getUISerializer();
	
	public IUIRender getUIRender(UIMeta uimeta, UIElement type, PageMeta pageMeta, IUIRender parentRender, DriverPhase dp);

	public IUIRender getUIRender(UIMeta uiMeta, UIElement uiEle, WebComponent comp, PageMeta pageMeta, IUIRender parentRender, DriverPhase dp);
}
