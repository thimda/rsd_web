package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class ListListener extends JsListenerConf {

	private static final long serialVersionUID = -4331604212969138123L;
	
	public static final String DB_VALUE_CHANGE = "dbValueChange";
	public static final String VALUE_CHANGED = "valueChanged";

	@Override
	public String getJsClazz() {
		return "ListListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[2];
		descs[0] = new JsEventDesc(VALUE_CHANGED, "listEvent");
		descs[1] = new JsEventDesc(DB_VALUE_CHANGE, "listEvent");
		return descs;
	}
	
	public static EventHandlerConf getValueChangedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(VALUE_CHANGED);
		LfwParameter param = new LfwParameter();
		param.setName("listEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getDbValueChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(DB_VALUE_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("listEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
