/**
 * 
 */
package nc.uap.lfw.design.impl;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IAMCProviderForDesign;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.pa.setting.IEditorSaveHandler;

/**
 * @author chouhl
 *
 */
public class DevelopeEditorSaveHandlerImpl implements IEditorSaveHandler {

	public static <T> T getInterfaceService(Class<T> clazz){
//		Properties props = new  Properties();
//		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		return NCLocator.getInstance().lookup(clazz);
	}
	
	public void save(PageMeta pm, UIMeta meta, LfwWidget widget, String nodePath) {
		getInterfaceService(IAMCProviderForDesign.class).save(pm, meta, widget, nodePath);
	}

}
