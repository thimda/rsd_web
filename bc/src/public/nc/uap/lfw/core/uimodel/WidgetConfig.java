package nc.uap.lfw.core.uimodel;

import java.io.Serializable;

import nc.uap.lfw.core.comp.WebElement;

public class WidgetConfig extends WebElement implements Serializable {
	private static final long serialVersionUID = -2311286196923157648L;
	private String id;
	private String refId;
	private String sourcePackage;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public void setSourcePackage(String refId2) {
		// TODO Auto-generated method stub
		
	}
	public String getSourcePackage() {
		return sourcePackage;
	}
}
