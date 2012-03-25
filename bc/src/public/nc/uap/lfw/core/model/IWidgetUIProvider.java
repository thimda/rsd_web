package nc.uap.lfw.core.model;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * Widget UI������
 *
 */
public interface IWidgetUIProvider {
	public UIMeta getDefaultUIMeta(LfwWidget widget);
}
