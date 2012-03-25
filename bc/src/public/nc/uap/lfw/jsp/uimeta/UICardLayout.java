package nc.uap.lfw.jsp.uimeta;



public class UICardLayout extends UILayout {
	private static final long serialVersionUID = -7255360718452324802L;
	public static final String CURRENT_ITEM = "currentItem";

	public String getId() {
		return (String) getAttribute(ID);
	}

	public void setId(String value) {
		setAttribute(ID, value);
	}

	public String getCurrentItem() {
		return (String) getAttribute(CURRENT_ITEM);
	}

	public void setCurrentItem(String value) {
		setAttribute(CURRENT_ITEM, value);
		UpdatePair pair = new UpdatePair(CURRENT_ITEM, value);
		notifyChange(UPDATE,pair);
	}

	public String getWidgetId() {
		return (String) getAttribute(WIDGET_ID);
	}

	public void setWidgetId(String widgetId) {
		setAttribute(WIDGET_ID, widgetId);
	}
}
