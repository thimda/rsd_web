package nc.uap.lfw.core.uif.listener;

import java.util.HashMap;
import java.util.Map;

public class MultiBodyInfo implements IBodyInfo {

	private static final long serialVersionUID = -1452833246272800150L;
	private String bodyTabId;
	private Map<String, String> itemDsMap;

	public Map<String, String> getItemDsMap() {
		return itemDsMap;
	}
	public void setItemDsMap(Map<String, String> itemDsMap) {
		this.itemDsMap = itemDsMap;
	}
	public String getBodyTabId() {
		return bodyTabId;
	}
	public void setBodyTabId(String bodyTabId) {
		this.bodyTabId = bodyTabId;
	}
	
	/**
	 * ����ҳǩ��ID��Dataset Id ��
	 * @param tabItem
	 * @param ds
	 */
	public void addTabItemDatasetPair(String tabItem, String ds){
		if(itemDsMap == null)
			itemDsMap = new HashMap<String, String>();
		itemDsMap.put(tabItem, ds);
	}
	
	/**
	 * ͨ��ҳǩ��ID���Dataset ID
	 * @param tabItem
	 * @return
	 */
	public String getDatasetByTabItem(String tabItem){
		if(itemDsMap == null)
			return null;
		return itemDsMap.get(tabItem);
	}
}
