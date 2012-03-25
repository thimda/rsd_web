package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class DialogListener extends JsListenerConf {

	private static final long serialVersionUID = 9071228878797190818L;
	
	public static final String ON_CLOSE = "onclose";
	public static final String AFTER_CLOSE = "afterClose";
	public static final String BEFORE_CLOSE = "beforeClose";
	public static final String BEFORE_SHOW = "beforeShow";
	public static final String ON_CANCEL = "onCancel";
	public static final String ON_OK = "onOk";

	@Override
	public String getJsClazz() {
		return "DialogListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[6];
		descs[0] = new JsEventDesc(ON_OK, "simpleEvent");
		descs[1] = new JsEventDesc(ON_CANCEL, "simpleEvent");
		descs[2] = new JsEventDesc(BEFORE_SHOW, "simpleEvent");
		descs[3] = new JsEventDesc(BEFORE_CLOSE, "simpleEvent");
		descs[4] = new JsEventDesc(AFTER_CLOSE, "simpleEvent");
		descs[5] = new JsEventDesc(ON_CLOSE, "simpleEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnOkEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_OK);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnCancelEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CANCEL);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getBeforeShowEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_SHOW);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getBeforeCloseEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_CLOSE);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getAfterCloseEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(AFTER_CLOSE);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOncloseEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CLOSE);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
