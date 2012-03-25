package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class AutoformListener extends JsListenerConf {

	private static final long serialVersionUID = -6386227520105335055L;
	
	public static final String IN_ACTIVE = "inActive";
	public static final String GET_VALUE = "getValue";
	public static final String ACTIVE = "active";
	public static final String SET_VALUE = "setValue";
	
	@Override
	public String getJsClazz() {
		return "AutoformListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[4];
		descs[0] = new JsEventDesc(IN_ACTIVE, "simpleEvent");
		descs[1] = new JsEventDesc(GET_VALUE, "simpleEvent");
		descs[2] = new JsEventDesc(ACTIVE, "simpleEvent");
		descs[3] = new JsEventDesc(SET_VALUE, "autoformSetValueEvent");
		return descs;
	}
	
	public static EventHandlerConf getInActiveEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(IN_ACTIVE);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getGetValueEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(GET_VALUE);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getActiveEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ACTIVE);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getSetValueEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(SET_VALUE);
		LfwParameter param = new LfwParameter();
		param.setName("autoformSetValueEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
