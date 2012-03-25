package nc.uap.lfw.core.event.conf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.data.Parameter;

/**
 * 事件后台提交规则
 * 
 * @author guoweic
 *
 */
public class EventSubmitRule implements Cloneable, Serializable {
	private static final long serialVersionUID = 1762817124593849777L;

	// Widget的规则集合，值为Dataset提交类型集合
	private Map<String, WidgetRule> widgetRules = new HashMap<String, WidgetRule>();
	// 父的提交规则
	private EventSubmitRule parentSubmitRule = null;
	
	// 参数集合
	private Map<String, Parameter> params = new HashMap<String, Parameter>();

	// Pagemeta的Tab是否提交
	private boolean tabSubmit = false;
	// Pagemeta的Card是否提交
	private boolean cardSubmit = false;
	// widget的Panel是否提交
	private boolean panelSubmit = false;
	// 所属Pagemeta的ID
	private String pagemeta;
	
	public Object clone(){
		EventSubmitRule eventSubmitRule = null;
		try {
			eventSubmitRule = (EventSubmitRule) super.clone();
			for (String widgetId : this.widgetRules.keySet()) {
				eventSubmitRule.getWidgetRules().put(widgetId, (WidgetRule) this.widgetRules.get(widgetId).clone());
			}
			for (String name : this.params.keySet()) {
				eventSubmitRule.addParam((Parameter) this.params.get(name).clone());
			}
			
			if(this.parentSubmitRule != null)
				eventSubmitRule.setParentSubmitRule((EventSubmitRule) this.parentSubmitRule.clone());
		} 
		catch (CloneNotSupportedException e) {
		}
		return eventSubmitRule;
	}
	
	public Map<String, WidgetRule> getWidgetRules() {
		return widgetRules;
	}
	
	public WidgetRule getWidgetRule(String id){
		return widgetRules.get(id);
	}

	public void setWidgetRules(Map<String, WidgetRule> widgetRules) {
		this.widgetRules = widgetRules;
	}
	
//	/**
//	 * 增加Widget
//	 * @param widgetId
//	 */
//	public void addWidget(String widgetId) {
//		if (!widgetRules.containsKey(widgetId)) {
//			WidgetRule widgetRule = new WidgetRule();
//			widgetRule.setId(widgetId);
//			widgetRules.put(widgetId, widgetRule);
//		}
//	}
	
	/**
	 * 增加WidgetRule
	 * @param widgetId
	 */
	public void addWidgetRule(WidgetRule widgetRule) {
		widgetRules.put(widgetRule.getId(), widgetRule);
	}
	
	/**
	 * 增加参数
	 * @param name
	 * @param value
	 */
	public void addParam(Parameter param) {
		params.put(param.getName(), param);
	}
	
	public Map<String, Parameter> getParamMap() {
		return params;
	}

	public Parameter[] getParams() {
		return params.values().toArray(new Parameter[0]);
	}

	public void setParams(Map<String, Parameter> params) {
		this.params = params;
	}
	
	public Parameter getParam(String id) {
		return params.get(id);
	}

	public EventSubmitRule getParentSubmitRule() {
		return parentSubmitRule;
	}

	public void setParentSubmitRule(EventSubmitRule parentSubmitRule) {
		this.parentSubmitRule = parentSubmitRule;
	}

	public boolean isTabSubmit() {
		return tabSubmit;
	}

	public void setTabSubmit(boolean tabSubmit) {
		this.tabSubmit = tabSubmit;
	}

	public boolean isCardSubmit() {
		return cardSubmit;
	}

	public void setCardSubmit(boolean cardSubmit) {
		this.cardSubmit = cardSubmit;
	}

	public boolean isPanelSubmit() {
		return panelSubmit;
	}

	public void setPanelSubmit(boolean panelSubmit) {
		this.panelSubmit = panelSubmit;
	}

	public String getPagemeta() {
		return pagemeta;
	}

	public void setPagemeta(String pagemeta) {
		this.pagemeta = pagemeta;
	}
}
