package nc.uap.lfw.core.event.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.data.LfwParameter;

/**
 * <Event>描述类
 */
public class EventHandlerConf extends ExtendAttributeSupport implements Cloneable, Serializable {
	private static final long serialVersionUID = -6145431199492149652L;
	
	// Dataset的列ID参数标识
	public static final String PARAM_DATASET_FIELD_ID = "dataset_field_id";
	
	public static final String SUBMIT_PRE = "SP_";
	private String name = null;
	private String script = null;
	private boolean onserver = false;
	private boolean async = true;
	private List<LfwParameter> paramList = new ArrayList<LfwParameter>();
	private List<LfwParameter> extendParamList = new ArrayList<LfwParameter>();
	private EventSubmitRule submitRule = null;
	
	public List<LfwParameter> getParamList() {
		return paramList;
	}
	
	public EventHandlerConf(){}
	
	public EventHandlerConf(String name, LfwParameter param, String script) {
		this.name = name;
		this.paramList = new ArrayList<LfwParameter>();
		paramList.add(param);
		this.script = script;
	}


	public void setParamList(List<LfwParameter> paramList) {
		this.paramList = paramList;
	}

	public void addParam(LfwParameter param) {
		paramList.add(param);
	}
	
	public LfwParameter getParam(String name) {
		for(int i = 0; i < paramList.size(); i++)
		{
			if(paramList.get(i).getName().equals(name))
				return paramList.get(i);
		}
		return null;
	}


	public void setExtendParamList(List<LfwParameter> extendParamList) {
		this.extendParamList = extendParamList;
	}

	public void addExtendParam(LfwParameter extendParam) {
		extendParamList.add(extendParam);
	}
	
	public LfwParameter getExtendParam(String name) {
		for(int i = 0; i < extendParamList.size(); i++)
		{
			if(extendParamList.get(i).getName().equals(name))
				return extendParamList.get(i);
		}
		return null;
	}

	public List<LfwParameter> getExtendParamList() {
		return extendParamList;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Object clone() {
		EventHandlerConf eventHandlerConf = (EventHandlerConf) super.clone();
		eventHandlerConf.paramList = new ArrayList<LfwParameter>();
		eventHandlerConf.extendParamList = new ArrayList<LfwParameter>();
		for(int i = 0, n = paramList.size(); i < n; i++)
			eventHandlerConf.paramList.add((LfwParameter) paramList.get(i).clone());
		for(int i = 0, n = extendParamList.size(); i < n; i++)
			eventHandlerConf.extendParamList.add((LfwParameter) extendParamList.get(i).clone());
		if(submitRule != null)
			eventHandlerConf.submitRule = (EventSubmitRule) submitRule.clone();
		
		return eventHandlerConf;
	}

	public boolean isOnserver() {
		return onserver;
	}

	public void setOnserver(boolean onserver) {
		this.onserver = onserver;
	}

	public EventSubmitRule getSubmitRule() {
		return submitRule;
	}

	public void setSubmitRule(EventSubmitRule submitRule) {
		this.submitRule = submitRule;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

}

