package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public interface IWebPartContentFetcher {
	/**
	 * 根据分组获取页面元素
	 * @param um
	 * @param pm
	 * @param view
	 * @param hroup
	 * @return
	 */
	public String fetchHtml(UIMeta um, PageMeta pm, LfwWidget view);
	/**
	 * 获取bodyScript脚本
	 * @param um
	 * @param pm
	 * @param view
	 * @param hroup
	 * @return
	 */
	public String fetchBodyScript(UIMeta um, PageMeta pm, LfwWidget view);
}
