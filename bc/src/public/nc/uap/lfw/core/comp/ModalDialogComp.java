package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.event.conf.DialogListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 模态对话框配置类
 * @author dengjt
 *
 */
public class ModalDialogComp extends WebComponent {
	
	private static final long serialVersionUID = 1L;

	private List<WebComponent> list = new ArrayList<WebComponent>();
	
	private String i18nName = "Dialog";
	
	public void addComponent(WebComponent comp){
		list.add(comp);
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(DialogListener.class);
		return list;
	}
	
}
