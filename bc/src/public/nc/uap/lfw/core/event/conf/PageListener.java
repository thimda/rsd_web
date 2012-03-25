package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;



public class PageListener extends JsListenerConf {
	
	private static final long serialVersionUID = 7025182395167702809L;
	
	public static final String ON_CLOSED = "onClosed";
	public static final String ON_CLOSING = "onClosing";
	public static final String BEFORE_ACTIVE = "beforeActive";
	public static final String AFTER_PAGE_INIT = "afterPageInit";

	@Override
	public String getJsClazz() {
		return "PageListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[4];
//		descs[0] = new JsEventDesc("beforePageInit", "");
		descs[0] = new JsEventDesc(AFTER_PAGE_INIT, "");
//		descs[2] = new JsEventDesc("beforeInitData", "loader");
//		descs[3] = new JsEventDesc("externalInit", "");
		descs[1] = new JsEventDesc(BEFORE_ACTIVE, "");
		descs[2] = new JsEventDesc(ON_CLOSING, "");
		descs[3] = new JsEventDesc(ON_CLOSED, "");
		return descs;
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
	
}
