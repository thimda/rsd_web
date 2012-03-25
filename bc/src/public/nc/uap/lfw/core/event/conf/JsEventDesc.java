package nc.uap.lfw.core.event.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsEventDesc implements Serializable {
	
	private static final long serialVersionUID = 2941761855770856333L;
	
	/**
	 * 事件名称
	 */
	private String name;
	
	/**
	 * 事件方法参数列表
	 */
	private List<String> paramList;
	
	private boolean async = true;
	private boolean onlyClient = false;
	private String eventClazz;
	private String jsEventClazz;
	
	public JsEventDesc() {
		
	}
	
	public JsEventDesc(String name, String param) {
		this(name, param, true);
	}
	
	public JsEventDesc(String name, String param, boolean asyn) {
		this.name = name;
		this.paramList = new ArrayList<String>();
		if(param != null && param.trim().length() > 0){
			paramList.add(param);
		}
		this.async = asyn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getParamList() {
		return paramList;
	}

	public void setParamList(List<String> paramList) {
		this.paramList = paramList;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public boolean isOnlyClient() {
		return onlyClient;
	}

	public void setOnlyClient(boolean onlyClient) {
		this.onlyClient = onlyClient;
	}

	public String getEventClazz() {
		return eventClazz;
	}

	public void setEventClazz(String eventClazz) {
		this.eventClazz = eventClazz;
	}

	public String getJsEventClazz() {
		return jsEventClazz;
	}

	public void setJsEventClazz(String jsEventClazz) {
		this.jsEventClazz = jsEventClazz;
	}

}
