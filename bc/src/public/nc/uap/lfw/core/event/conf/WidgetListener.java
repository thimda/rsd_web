package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;


/**
 * @author guoweic
 *
 */
public class WidgetListener extends JsListenerConf {
	
	private static final long serialVersionUID = -6607289711930021906L;
	
	public static final String BEFORE_INIT_DATA = "beforeInitData";
	public static final String ON_INITIALIZING= "onInitializing";
	@Override
	public String getJsClazz() {
		return "WidgetListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[2];
		descs[0] = new JsEventDesc(BEFORE_INIT_DATA, "loader");
		descs[1] = new JsEventDesc(ON_INITIALIZING, "event");
		return descs;
	}
	
	public static EventHandlerConf getOnInitializingEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_INITIALIZING);
		LfwParameter param = new LfwParameter();
		param.setName("event");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getBeforeInitDataEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_INIT_DATA);
		LfwParameter param = new LfwParameter();
		param.setName("loader");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
