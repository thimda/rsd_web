package nc.uap.lfw.core.uimodel.conf;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.WidgetListener;
import nc.uap.lfw.core.page.config.LfwWidgetConf;

public class ViewConf extends LfwWidgetConf {
	
	private static final long serialVersionUID = 3427328973781650942L;
	
	public static final String TagName = "Widget";
	
	/**
	 * controllerÀàÈ«Â·¾¶
	 */
	private String controllerClazz = null;

	public String getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}
	
	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(WidgetListener.class);
		return list;
	}

}
