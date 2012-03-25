package nc.uap.lfw.core.event.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.data.LfwParameter;
/**
 * 事件配置
 * @author dengjt
 *
 */
public class EventConf extends ExtendAttributeSupport implements Serializable, Cloneable {
	//事件正常状态
	public static final int NORMAL_STATUS = 1;
	//事件增加状态
	public static final int ADD_STATUS = 2;
	//事件删除状态
	public static final int DEL_STATUS = 3;
	
	private static final long serialVersionUID = 5332995004954913304L;
	private String name = null;
	private String script = null;
	private String methodName = null;
	private boolean onserver = true;
	private boolean async = true;
	private List<LfwParameter> paramList = null;
	private List<LfwParameter> extendParamList = null;
	private EventSubmitRule submitRule = null;
	private String controllerClazz = null;
	private String jsEventClaszz = null;
	//事件状态
	private int eventStatus = NORMAL_STATUS;
	//类文件路径
	private String classFilePath = null;
	//类文件名称
	private String classFileName = null;
	public EventConf(){}
	
	public String getJsEventClaszz() {
		return jsEventClaszz;
	}

	public void setJsEventClaszz(String jsEventClaszz) {
		this.jsEventClaszz = jsEventClaszz;
	}

	public EventConf(String name, LfwParameter param, String script) {
		this.name = name;
		this.paramList = new ArrayList<LfwParameter>();
		paramList.add(param);
		this.script = script;
	}
	
	public List<LfwParameter> getParamList() {
		if(this.paramList == null)
			this.paramList = new ArrayList<LfwParameter>();
		return this.paramList;
	}

	public void setParamList(List<LfwParameter> paramList) {
		this.paramList = paramList;
	}

	public void addParam(LfwParameter param) {
		getParamList().add(param);
	}
	
	public LfwParameter getParam(String name) {
		for(int i = 0; i < getParamList().size(); i++)
		{
			if(getParamList().get(i).getName().equals(name))
				return getParamList().get(i);
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
		for(int i = 0; i < getExtendParamList().size(); i++)
		{
			if(extendParamList.get(i).getName().equals(name))
				return extendParamList.get(i);
		}
		return null;
	}

	public List<LfwParameter> getExtendParamList() {
		if(this.extendParamList == null)
			this.extendParamList = new ArrayList<LfwParameter>();
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
		EventConf eventConf = (EventConf) super.clone();
		eventConf.paramList = new ArrayList<LfwParameter>();
		eventConf.extendParamList = new ArrayList<LfwParameter>();
		for(int i = 0, n =  getParamList().size(); i < n; i++)
			eventConf.paramList.add((LfwParameter) getParamList().get(i).clone());
		for(int i = 0, n = getExtendParamList().size(); i < n; i++)
			eventConf.extendParamList.add((LfwParameter) getExtendParamList().get(i).clone());
		if(submitRule != null)
			eventConf.submitRule = (EventSubmitRule) submitRule.clone();
		
		return eventConf;
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

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getControllerClazz() {
		return controllerClazz == null ? "" : controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}

	public int getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(int eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getClassFilePath() {
		return classFilePath;
	}

	public void setClassFilePath(String classFilePath) {
		this.classFilePath = classFilePath;
	}

	public String getClassFileName() {
		return classFileName;
	}

	public void setClassFileName(String classFileName) {
		this.classFileName = classFileName;
	}
	
}
