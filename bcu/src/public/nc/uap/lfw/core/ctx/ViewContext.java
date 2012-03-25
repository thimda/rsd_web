package nc.uap.lfw.core.ctx;

import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class ViewContext{
	private static final long serialVersionUID = -3742250714460813845L;
	private String id;
	private LfwWidget widget;
	private UIMeta uiMeta;

	public UIMeta getUIMeta() {
		return uiMeta;
	}
	
	public void setUIMeta(UIMeta um){
		this.uiMeta = um;
	}

	public void setId(String widgetId) {
		this.id = widgetId;
	}

	public String getId() {
		return id;
	}

	public void setView(LfwWidget widget) {
		this.widget = widget;
	}
	
	public LfwWidget getView(){
		return this.widget;
	}

	public void reset() {
	}
}
