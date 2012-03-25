package nc.uap.lfw.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 输出描述器，一个输出至少包含一个出发时机，可包含0到多个参数
 * @author dengjt
 *
 */
public class PluginDesc implements Serializable, Cloneable {
	private static final long serialVersionUID = 6615952170085829290L;
	
	//映射类型
	public static String PLUGINTYPEMAPPING = "mapping";
	
	//事件类型
	public static String PLUGINTYPEEVENT = "event";

	//plugin类型
	private String pluginType = PLUGINTYPEEVENT;
	
	//plugin提交规则
//	private EventSubmitRule submitRule = null;
	
	public String getPluginType() {
		return pluginType;
	}
	public void setPluginType(String pluginType) {
		this.pluginType = pluginType;
	}

	private List<PluginDescItem> descItemList;
	private String id;
	public List<PluginDescItem> getDescItemList() {
		return descItemList;
	}
	public void setDescItemList(List<PluginDescItem> descItemList) {
		this.descItemList = descItemList;
	}
	
	public void addDescItem(PluginDescItem descItem){
		if(descItemList == null)
			descItemList = new ArrayList<PluginDescItem>();
		this.descItemList.add(descItem);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

//	public EventSubmitRule getSubmitRule() {
//		return submitRule;
//	}
//
//	public void setSubmitRule(EventSubmitRule submitRule) {
//		this.submitRule = submitRule;
//	}

	
	public Object clone(){
		try {
			PluginDesc pluginDesc = (PluginDesc)super.clone();
			pluginDesc.descItemList = new ArrayList<PluginDescItem>();
			
			if (this.descItemList != null){
				for (PluginDescItem item : this.descItemList){
					pluginDesc.addDescItem(item);
				}
				
			}
			
//			if(submitRule != null)
//				pluginDesc.submitRule = (EventSubmitRule) submitRule.clone();
			
			return pluginDesc;
		} catch (CloneNotSupportedException e) {
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}
}
