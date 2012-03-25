package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * ±‡º≠∆˜øÿº˛≈‰÷√¿‡
 * 
 * @author dengjt
 * 
 */
public class EditorComp extends WebComponent {

	private static final long serialVersionUID = 2731851377588527207L;
	private String hideBarIndices;
	private String hideImageIndices;
	private String value = "";
	private boolean readOnly = false;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null && !value.equals(this.value)) {
			this.value = value;
			setCtxChanged(true);
		}
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		setCtxChanged(true);
	}

	public String getHideBarIndices() {
		return hideBarIndices;
	}

	public void setHideBarIndices(String hideBarIndices) {
		this.hideBarIndices = hideBarIndices;
	}

	public String getHideImageIndices() {
		return hideImageIndices;
	}

	public void setHideImageIndices(String hideImageIndices) {
		this.hideImageIndices = hideImageIndices;
	}

	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}

	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(FocusListener.class);
		return list;
	}
}
