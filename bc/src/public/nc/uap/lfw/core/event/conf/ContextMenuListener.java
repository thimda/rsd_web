package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

public class ContextMenuListener extends JsListenerConf {
	
	private static final long serialVersionUID = -30999543786919696L;

	public static final String ON_MOUSE_OUT = "onmouseout";
	public static final String ON_CLOSE = "onclose";
	public static final String BEFORE_CLOSE = "beforeClose";
	public static final String ON_SHOW = "onshow";
	public static final String BEFORE_SHOW = "beforeShow";

	@Override
	public String getJsClazz() {
		return "ContextMenuListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[5];
		descs[0] = new JsEventDesc(BEFORE_SHOW, "simpleEvent");
		descs[1] = new JsEventDesc(ON_SHOW, "simpleEvent");
		descs[2] = new JsEventDesc(BEFORE_CLOSE, "simpleEvent");
		descs[3] = new JsEventDesc(ON_CLOSE, "simpleEvent");
		descs[4] = new JsEventDesc(ON_MOUSE_OUT, "menuItemEvent");
		return descs;
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
	
	public static EventHandlerConf getOnShowEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_SHOW);
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
	
	public static EventHandlerConf getOnCloseEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CLOSE);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnMouseOutEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_MOUSE_OUT);
		LfwParameter param = new LfwParameter();
		param.setName("menuItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
