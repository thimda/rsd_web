package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class DateListener extends JsListenerConf {

	private static final long serialVersionUID = -182461023148665918L;
	
	private static final String VALUE_CHANGED = "valueChanged";

	@Override
	public String getJsClazz() {
		return "DateListener";
	}
	
	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(VALUE_CHANGED, "dateEvent");
		return descs;
	}
	
	public static EventHandlerConf getValueChangedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(VALUE_CHANGED);
		LfwParameter param = new LfwParameter();
		param.setName("dateEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
