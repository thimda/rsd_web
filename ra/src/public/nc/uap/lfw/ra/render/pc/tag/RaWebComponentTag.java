package nc.uap.lfw.ra.render.pc.tag;

import nc.uap.lfw.core.comp.WebElement;


public abstract class RaWebComponentTag extends RaWebElementTag {
	private String widgetId;

	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}
	
	@Override
	protected WebElement getWebElement() {
		return getPageMeta().getWidget(widgetId).getViewComponents().getComponent(getId());
	}
}
