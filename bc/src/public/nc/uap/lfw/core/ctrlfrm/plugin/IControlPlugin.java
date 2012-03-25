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
	 * ��ȡ������
	 * @return
	 */
	public String[] getDependences();
	
	/**
	 * ��ȡȫ·����
	 * @return
	 */
	public String getFullName();
	
	public String[] getImports(boolean optimized);
	
	public String[] getCssImports(boolean optimized);
	
	public String getCssFullName();
	
	/**
	 * ��ȡ�ؼ�����
	 * @return
	 */
	public String getId();
	
	public String[] calculateDependences(boolean optimized);
	
	public String[] calculateCssDependences(boolean optimized);
	
	public String[] calculateDependenceIds();
	
	/**
	 * ��ȡ�ؼ����л���
	 * @return
	 */
	public <T>IControlSerializer<T> getControlSerializer();
	
	public <T>IUISerializer<T> getUISerializer();
	
	public IUIRender getUIRender(UIMeta uimeta, UIElement type, PageMeta pageMeta, IUIRender parentRender, DriverPhase dp);

	public IUIRender getUIRender(UIMeta uiMeta, UIElement uiEle, WebComponent comp, PageMeta pageMeta, IUIRender parentRender, DriverPhase dp);
}
