package nc.uap.lfw.core.model;

import java.util.Map;

import nc.uap.lfw.core.page.PageMeta;
/**
 * ҳ��Ԫ���ݹ�����
 * @author dengjt
 *
 */
public interface IPageMetaBuilder {
	/**
	 * ����ҳ�����
	 * @param paramMap  ��չ��Ϣ����
	 * @return
	 */
	public PageMeta buildPageMeta(Map<String, Object> paramMap);
}
