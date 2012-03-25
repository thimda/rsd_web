/**
 * 
 */
package nc.uap.lfw.core.uimodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.conf.IListenerSupport;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.uimodel.conf.AMCListenerConf;

/**
 * 
 * 节点元素基类
 * @author chouhl
 * 
 */
public class AMCWebElement extends WebElement implements IListenerSupport, Cloneable, Serializable {

	private static final long serialVersionUID = -8967743531123048621L;
	/**
	 * ID
	 */
	private String id = null;
	/**
	 * 标题
	 */
	private String caption = null;
	/**
	 * 国际化名称
	 */
	private String i18nName = null;
	/**
	 * 处理类全路径
	 */
	private String processorClazz = null;
	/**
	 * controller类全路径
	 */
	private String controllerClazz = null;
	
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(AMCListenerConf.class);
		return list;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public String getProcessorClazz() {
		return processorClazz;
	}

	public void setProcessorClazz(String processorClazz) {
		this.processorClazz = processorClazz;
	}

	public String getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
