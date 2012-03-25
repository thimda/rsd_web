package nc.uap.lfw.design.itf;

import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * Eclipse编辑器修改服务
 * 
 * @author licza
 * 
 */
public interface IPaEditorService {
	/**
	 * 获取原始的uimeta
	 * 
	 * @return
	 */
	public UIMeta getOriUIMeta(String pageId,String sessionId);

	/**
	 * 获取外部的uimeta
	 * 
	 * @return
	 */
	public UIMeta getOutUIMeta(String pageId,String sessionId);

	/**
	 * 原始pagemeta
	 * 
	 * @return
	 */
	public PageMeta getOriPageMeta(String pageId,String sessionId);

	/**
	 * 外部pageMeta
	 *  
	 * @return
	 */
	public PageMeta getOutPageMeta(String pageId,String sessionId);
	
	public void clearSessionCache(String sessionId);
}
