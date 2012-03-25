package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class CardListener extends JsListenerConf {
	
	private static final long serialVersionUID = -6287064492955727387L;
	
	public static final String BEFORE_PAGE_CHANGE = "beforePageChange";
	public static final String AFTER_PAGE_INIT = "afterPageInit";
	public static final String BEFORE_PAGE_INIT = "beforePageInit";

	@Override
	public String getJsClazz() {
		return "CardListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[3];
		descs[0] = new JsEventDesc(BEFORE_PAGE_INIT, "cardEvent");
		descs[1] = new JsEventDesc(AFTER_PAGE_INIT, "cardEvent");
		descs[2] = new JsEventDesc(BEFORE_PAGE_CHANGE, "cardEvent");
		return descs;
	}
	
	public static EventHandlerConf getBeforePageInitEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_PAGE_INIT);
		LfwParameter param = new LfwParameter();
		param.setName("cardEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getAfterPageInitEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(AFTER_PAGE_INIT);
		LfwParameter param = new LfwParameter();
		param.setName("cardEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getBeforePageChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_PAGE_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("cardEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
