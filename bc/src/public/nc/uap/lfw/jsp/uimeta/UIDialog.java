package nc.uap.lfw.jsp.uimeta;

import java.util.ArrayList;
import java.util.List;

public class UIDialog extends UIWidget {
	private static final long serialVersionUID = -7898259918810962213L;
	private boolean visible = false;
	private String width;
	private String height;
	private boolean refresh = false;
	private String title;
	
	public UIDialog() {
		setWidth("80%");
		setHeight("80%");
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
		this.notifyChange(UPDATE, this);
	}
	private List<String> eventHandler = null;
	
	/**
	 * 增加事件监听
	 * @param eventType
	 */
	public void addEvent(String eventType){
		if(eventHandler == null)
			eventHandler = new ArrayList<String>();
		if(!eventHandler.contains(eventType))
			eventHandler.add(eventType);
	}
	public void removeEvent(String eventType){
		if(eventHandler == null)
			eventHandler = new ArrayList<String>();
		if(eventHandler.contains(eventType))
			eventHandler.remove(eventType);
	}
	public void clearEvent(){
		 eventHandler = null;
	}
	public List<String> getEventHandlerList(){
		return eventHandler;
	}
	public boolean isVisible() {
		return visible;
	}
	
	protected String getObserverName(String type, Object obj){
		return "UIWidget";
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public boolean isRefresh() {
		return refresh;
	}
	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
