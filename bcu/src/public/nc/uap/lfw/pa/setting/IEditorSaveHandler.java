/**
 * 
 */
package nc.uap.lfw.pa.setting;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * @author wupeng1
 * @version 6.0 2011-10-11
 * @since 1.6
 */
public interface IEditorSaveHandler {
	public void save(PageMeta pm, UIMeta meta, LfwWidget lwidget, String pagemetaPath);
}
