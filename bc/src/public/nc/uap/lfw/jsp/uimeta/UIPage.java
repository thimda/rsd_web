package nc.uap.lfw.jsp.uimeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class UIPage extends UILayoutPanel {
	private static final long serialVersionUID = 5927522368179835547L;
	private String folderPath;
	private Map<String, UIPortalLayout> dialogMap = new HashMap<String, UIPortalLayout>();
	public static String TEMPLATE = "template";
	public static String TITLE = "title";
	public static String VERSION = "version";
	public static String SKIN = "skin";
	public static String ISDEFAULT = "isdefault";
	public static String PAGENAME = "pagename";

	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	public Map<String, UIPortalLayout> getDialogMap() {
		return dialogMap;
	}
	public void setDialogMap(Map<String, UIPortalLayout> dialogMap) {
		this.dialogMap = dialogMap;
	}
	
	public UIPortalLayout getDialog(String id){
		return dialogMap.get(id);
	}
	
	public void setDialog(String id, UIPortalLayout dialog){
		dialogMap.put(id, dialog);
	}
	
	
	public Integer getIsdefault() {
		return (Integer) getAttribute(ISDEFAULT);
	}

	public void setIsdefault(Integer isdefault) {
		setAttribute(ISDEFAULT, isdefault);
	}

	public String getPagename() {
		return (String) getAttribute(PAGENAME);
	}

	public void setPagename(String pagename) {
		setAttribute(PAGENAME, pagename);
	}

	public String getVersion() {
		return (String) getAttribute(VERSION);
	}

	public void setVersion(String version) {
		setAttribute(VERSION, version);
	}

	public String getSkin() {
		return (String) getAttribute(SKIN);
	}

	public void setSkin(String skin) {
		setAttribute(SKIN, skin);
	}

	

	public String getTemplate() {
		return (String) getAttribute(TEMPLATE);
	}

	public void setTemplate(String template) {
		setAttribute(TEMPLATE, template);
	}

	public String getTitle() {
		return (String) getAttribute(TITLE);
	}

	public void setTitle(String title) {
		setAttribute(TITLE, title);
	}
	
	
	protected Map<String, Serializable> createAttrMap() {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put(ISDEFAULT, 0);
		return map;
	}
	
	@Override
	public UIPage doClone() {
		UIPage page = (UIPage)super.doClone();		
		if(this.dialogMap != null){
			page.dialogMap = new HashMap<String, UIPortalLayout>();
			Iterator<String> keys = this.dialogMap.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				UIPortalLayout layout = this.dialogMap.get(key);
				page.dialogMap.put(key,(UIPortalLayout)layout.doClone());
			}
		}
		
		return page;		
	}
	
}
