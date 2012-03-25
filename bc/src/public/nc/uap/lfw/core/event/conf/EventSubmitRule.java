package nc.uap.lfw.core.event.conf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.data.Parameter;

/**
 * �¼���̨�ύ����
 * 
 * @author guoweic
 *
 */
public class EventSubmitRule implements Cloneable, Serializable {
	private static final long serialVersionUID = 1762817124593849777L;

	// Widget�Ĺ��򼯺ϣ�ֵΪDataset�ύ���ͼ���
	private Map<String, WidgetRule> widgetRules = new HashMap<String, WidgetRule>();
	// �����ύ����
	private EventSubmitRule parentSubmitRule = null;
	
	// ��������
	private Map<String, Parameter> params = new HashMap<String, Parameter>();

	// Pagemeta��Tab�Ƿ��ύ
	private boolean tabSubmit = false;
	// Pagemeta��Card�Ƿ��ύ
	private boolean cardSubmit = false;
	// widget��Panel�Ƿ��ύ
	private boolean panelSubmit = false;
	// ����Pagemeta��ID
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
//	 * ����Widget
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
	 * ����WidgetRule
	 * @param widgetId
	 */
	public void addWidgetRule(WidgetRule widgetRule) {
		widgetRules.put(widgetRule.getId(), widgetRule);
	}
	
	/**
	 * ���Ӳ���
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
