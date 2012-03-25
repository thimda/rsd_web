package nc.uap.lfw.core.uimodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.PageMeta;

/**
 * 应用级别UI模型
 * @author dengjt
 *
 */
public class Application extends AMCWebElement {

	private static final long serialVersionUID = 2528764331431629310L;
	
	public static final String TagName = "Application";
	
	/**
	 * Window节点元素集合
	 */
	private List<PageMeta> windowList = new ArrayList<PageMeta>();
	
	/**
	 * 默认WindowID
	 */
	private String defaultWindowId = null;
	
	/**
	 * window间plugout plugin
	 */
	private List<Connector> connectorList = new ArrayList<Connector>();
	
	public List<PageMeta> getWindowList() {
		return windowList;
	}

	public void setWindowList(List<PageMeta> windowList) {
		this.windowList = windowList;
	}
	
	public PageMeta getWindowConf(String id){
		Iterator<PageMeta> it = windowList.iterator();
		while(it.hasNext()){
			PageMeta win = it.next();
			if(win.getId().equals(id))
				return win;
		}
		return null;
	}
	
	public void addWindow(PageMeta window){
		this.windowList.add(window);
	}
	
	public String getDefaultWindowId() {
		return defaultWindowId;
	}

	public void setDefaultWindowId(String defaultWindowId) {
		this.defaultWindowId = defaultWindowId;
	}

	public List<Connector> getConnectorList() {
		return connectorList;
	}

	public void setConnectorList(List<Connector> connectorList) {
		this.connectorList = connectorList;
	}
	
	public void addConnector(Connector conn){
		this.connectorList.add(conn);
	}
	
}
