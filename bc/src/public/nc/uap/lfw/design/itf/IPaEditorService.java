package nc.uap.lfw.design.itf;

import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * Eclipse�༭���޸ķ���
 * 
 * @author licza
 * 
 */
public interface IPaEditorService {
	/**
	 * ��ȡԭʼ��uimeta
	 * 
	 * @return
	 */
	public UIMeta getOriUIMeta(String pageId,String sessionId);

	/**
	 * ��ȡ�ⲿ��uimeta
	 * 
	 * @return
	 */
	public UIMeta getOutUIMeta(String pageId,String sessionId);

	/**
	 * ԭʼpagemeta
	 * 
	 * @return
	 */
	public PageMeta getOriPageMeta(String pageId,String sessionId);

	/**
	 * �ⲿpageMeta
	 *  
	 * @return
	 */
	public PageMeta getOutPageMeta(String pageId,String sessionId);
	
	public void clearSessionCache(String sessionId);
}
