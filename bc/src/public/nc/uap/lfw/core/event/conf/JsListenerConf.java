package nc.uap.lfw.core.event.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.exception.LfwValidateException;

public abstract class JsListenerConf implements Cloneable, Serializable {
	
	public static final String TYPE = "type";
	
	private String confType;
	
	private static final long serialVersionUID = 5038555390835120183L;
	
	private String from;
	
	/**
	 * ��ǰListener�������¼����󼯺�
	 */
	private Map<String, EventHandlerConf> eventMap = new HashMap<String, EventHandlerConf>();
	
	/**
	 * ��ǰListener���԰����������¼���Ϣ
	 */
	private JsEventDesc[] jsEventDesc;
	
	/**
	 * ��ǰListener���԰����������¼�����
	 */
	private List<EventHandlerConf> JsEventHandlerList;
	
	private String id;
	
	private String serverClazz;
	
	public String getConfType() {
		return confType;
	}

	public void setConfType(String confType) {
		this.confType = confType;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}


	/**
	 * �鿴��ǰListener�Ƿ���԰����������¼�
	 * @param eventName �¼�����
	 * @return
	 */
	private boolean checkEvent(String eventName) {
		JsEventDesc[] desc = getJsEventDesc();
		int size = desc.length;
		for (int i = 0; i < size; i++) {
			if (eventName.equals(desc[i].getName()))
				return true;
		}
		return false;
	}
	
	public void addEventHandler(EventHandlerConf event) {
		if (checkEvent(event.getName()))
			eventMap.put(event.getName(), event);
		else
			throw new LfwValidateException("�����Event���ͣ�" + event.getName());
	}

	public Map<String, EventHandlerConf> getEventHandlerMap() {
		return eventMap;
	}

	public EventHandlerConf getEventHandler(String s) {
		return eventMap.get(s);
	}

	public void removeEventHandler(String s) {
		eventMap.remove(s);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object clone() {
		JsListenerConf ele;
		try {
			ele = (JsListenerConf) super.clone();
			if (this.eventMap != null) {
				ele.eventMap = new HashMap<String, EventHandlerConf>();
				Iterator<EventHandlerConf> it = this.eventMap.values().iterator();
				while (it.hasNext()) {
					ele.addEventHandler((EventHandlerConf) it.next().clone());
				}
			}
			return ele;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public abstract String getJsClazz();

	/**
	 * ��ȡListener��ӦEvent������Ϣ
	 * @return JsEventDesc[]
	 */
	public JsEventDesc[] getJsEventDesc() {
		if (jsEventDesc == null)
			jsEventDesc = createJsEventDesc();
		return jsEventDesc;
	}

	
	/**
	 * ����Listener��ӦEvent������Ϣ
	 * @return JsEventDesc[]
	 */
	protected abstract JsEventDesc[] createJsEventDesc();
	
	public String getServerClazz() {
		return serverClazz;
	}

	public void setServerClazz(String serverClazz) {
		this.serverClazz = serverClazz;
	}

	public List<EventHandlerConf> getJsEventHandlerList() {
		if (JsEventHandlerList == null) {
			JsEventHandlerList = new ArrayList<EventHandlerConf>();
			jsEventDesc = getJsEventDesc();
			for (JsEventDesc eventDesc : jsEventDesc) {
				if (null != eventDesc) {
					EventHandlerConf event = new EventHandlerConf();
					event.setName(eventDesc.getName());
					event.setAsync(eventDesc.isAsync());
					List<String> paramList = eventDesc.getParamList();
					for (String paramName : paramList) {
						LfwParameter param = new LfwParameter();
						param.setName(paramName);
						event.addParam(param);
					}
					JsEventHandlerList.add(event);
				}
			}
		}
		
		return JsEventHandlerList;
	}
	
	public EventHandlerConf getEventTemplate(String key) {
		return new EventHandlerConf();
	}
	
}
