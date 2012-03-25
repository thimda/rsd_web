package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class TextListener extends JsListenerConf {
	

	private static final long serialVersionUID = 549435257965508153L;

	public static final String ON_SELECT = "onselect";
	public static final String VALUE_CHANGED = "valueChanged";

	@Override
	public String getJsClazz() {
		return "TextListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[2];
		descs[0] = new JsEventDesc(ON_SELECT, "simpleEvent");
		descs[1] = new JsEventDesc(VALUE_CHANGED, "valueChangeEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnSelectEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_SELECT);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getValueChangedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(VALUE_CHANGED);
		LfwParameter param = new LfwParameter();
		param.setName("valueChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
