package nc.uap.lfw.core.model;

import java.util.Map;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;

/**
 * Widget ���ݹ�����
 *
 */
public interface IWidgetContentProvider {
	public LfwWidget buildWidget(PageMeta pm, LfwWidget conf, Map<String, Object> paramMap, String currWidgetId);
}
