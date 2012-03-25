/**
 * 
 */
package nc.uap.lfw.core.uimodel.conf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.data.LfwParameter;
import nc.uap.lfw.core.event.conf.EventHandlerConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.event.conf.JsListenerConf;

/**
 * @author chouhl
 *
 */
public class AMCListenerConf extends JsListenerConf {

	private static final long serialVersionUID = -4551448503530915503L;
	
	public static final String ON_CLOSED = "onClosed";
	public static final String ON_CLOSING = "onClosing";
	public static final String BEFORE_ACTIVE = "beforeActive";
	public static final String AFTER_PAGE_INIT = "afterPageInit";

	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[4];
		descs[0] = new JsEventDesc(AFTER_PAGE_INIT, "");
		descs[1] = new JsEventDesc(BEFORE_ACTIVE, "");
		descs[2] = new JsEventDesc(ON_CLOSING, "");
		descs[3] = new JsEventDesc(ON_CLOSED, "");
		return descs;
	}

	public String getJsClazz() {
		return this.getClass().getSimpleName();
	}
	
	public static EventHandlerConf getAfterPageInitEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(AFTER_PAGE_INIT);
		LfwParameter param = new LfwParameter();
		param.setName("");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getBeforeActiveEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_ACTIVE);
		LfwParameter param = new LfwParameter();
		param.setName("");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnClosingEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CLOSING);
		LfwParameter param = new LfwParameter();
		param.setName("");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnClosedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CLOSED);
		LfwParameter param = new LfwParameter();
		param.setName("");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

	public List<EventHandlerConf> getExistEventConfList() {
		List<EventHandlerConf> eventConfList = new ArrayList<EventHandlerConf>();
		Map<String, EventHandlerConf> existEventConfMap = getEventHandlerMap();
		Iterator<String> keys = existEventConfMap.keySet().iterator();
		while(keys.hasNext()){
			eventConfList.add(existEventConfMap.get(keys.next()));
		}
		return eventConfList;
	}

	public boolean isEventConfExist(EventHandlerConf eventConf){
		if(getEventHandlerMap() == null || !getEventHandlerMap().containsKey(eventConf.getName())){
			return false;
		}
		return true;
	}
	
}
