package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;



public class MouseListener extends JsListenerConf {
	
	private static final long serialVersionUID = -7905009343446995L;
	
	public static final String ON_MOUSE_OUT = "onmouseout";
	public static final String ON_MOUSE_OVER = "onmouseover";
	public static final String ON_DB_CLICK = "ondbclick";
	public static final String ON_CLICK = "onclick";

	@Override
	public String getJsClazz() {
		return "MouseListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[4];
		descs[0] = new JsEventDesc(ON_CLICK, "mouseEvent");
		descs[1] = new JsEventDesc(ON_DB_CLICK, "mouseEvent");
		descs[2] = new JsEventDesc(ON_MOUSE_OVER, "mouseEvent");
		descs[3] = new JsEventDesc(ON_MOUSE_OUT, "mouseEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnClickEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CLICK);
		LfwParameter param = new LfwParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnDbClickEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_DB_CLICK);
		LfwParameter param = new LfwParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnMouseOverEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_MOUSE_OVER);
		LfwParameter param = new LfwParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnMouseOutEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_MOUSE_OUT);
		LfwParameter param = new LfwParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
