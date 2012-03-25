package nc.uap.lfw.core.model;

import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;
/**
 * ҳ�沼��Ԫ���ݹ�����
 * @author dengjt
 *
 */
public interface IUIMetaBuilder {
	/**
	 * ����ҳ�沼�ֶ���
	 * @param paramMap  ��չ��Ϣ����
	 * @return
	 */
	public UIMeta buildUIMeta(PageMeta pagemeta);
	
	public UIMeta buildUIMeta(PageMeta pagemeta,String pageId,String widgetId);
}
