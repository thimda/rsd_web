package nc.uap.lfw.core.uimodel;

import java.io.Serializable;

public class WindowConfig implements Serializable {
	private static final long serialVersionUID = 4690714919957013783L;
	private String id;
	private String caption;
	private String i18nName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
}
