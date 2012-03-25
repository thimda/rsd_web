package nc.uap.lfw.core.model;

import java.util.Map;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.WidgetConfig;

public interface IWidgetBuilder {
	/**
	 * 
	 * @param pm
	 * @param conf
	 * @param projectPath 工程的绝对路径
	 * @param metaId
	 * @return
	 */
	public LfwWidget buildWidget(PageMeta pm, WidgetConfig conf, String metaId, Map<String, Object> paramMap);
}
