package nc.uap.lfw.core.model;

import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;
/**
 * 页面布局元数据构造器
 * @author dengjt
 *
 */
public interface IUIMetaBuilder {
	/**
	 * 构造页面布局对象
	 * @param paramMap  扩展信息参数
	 * @return
	 */
	public UIMeta buildUIMeta(PageMeta pagemeta);
	
	public UIMeta buildUIMeta(PageMeta pagemeta,String pageId,String widgetId);
}
