package nc.uap.lfw.jsp.uimeta;




public class UIShutter extends UILayout {
	private static final long serialVersionUID = -4232393027776208793L;
	public static final String CURRENT_ITEM = "currentItem";
	
	public static final String CLASSNAME = "className";
	
	public String getClassName() {
		return (String)getAttribute(CLASSNAME);
	}

	public void setClassName(String className) {
		setAttribute(CLASSNAME, className);
	}

	
	public String getCurrentItem() {
		return (String) getAttribute(CURRENT_ITEM);
	}
	
	public void setCurrentItem(String value){
		setAttribute(CURRENT_ITEM, value);
		UpdatePair pair = new UpdatePair(CURRENT_ITEM, value);
		notifyChange(UPDATE,pair);
	}
	
	public String getId() {
		return (String) getAttribute(ID);
	}
	public void setId(String id) {
		setAttribute(ID, id);
	}

	
	public String getWidgetId() {
		return (String) getAttribute(WIDGET_ID);
	}

	public void setWidgetId(String widgetId) {
		setAttribute(WIDGET_ID, widgetId);
	}
}
