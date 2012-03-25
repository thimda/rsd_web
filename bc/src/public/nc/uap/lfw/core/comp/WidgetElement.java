package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.page.LfwWidget;

public class WidgetElement extends WebElement {
	private static final long serialVersionUID = 6347664694295884243L;
	private LfwWidget widget;
	public WidgetElement(String id) {
		super(id);
	}
	public WidgetElement() {
		super();
	}
	public LfwWidget getWidget() {
		return widget;
	}
	public void setWidget(LfwWidget widget) {
		this.widget = widget;
	}
}
