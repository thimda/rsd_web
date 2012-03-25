package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public interface IWebPartContentFetcher {
	/**
	 * ���ݷ����ȡҳ��Ԫ��
	 * @param um
	 * @param pm
	 * @param view
	 * @param hroup
	 * @return
	 */
	public String fetchHtml(UIMeta um, PageMeta pm, LfwWidget view);
	/**
	 * ��ȡbodyScript�ű�
	 * @param um
	 * @param pm
	 * @param view
	 * @param hroup
	 * @return
	 */
	public String fetchBodyScript(UIMeta um, PageMeta pm, LfwWidget view);
}
