package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * focus 
 * @author zhangxya
 *
 */
public class FocusListener extends JsListenerConf {

	private static final long serialVersionUID = -4253180527143374857L;
	
	public static final String ON_BLUR = "onBlur";
	public static final String ON_FOCUS = "onFocus";

	@Override
	public String getJsClazz() {
		return "FocusListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[2];
		descs[0] = new JsEventDesc(ON_FOCUS, "focusEvent");
		descs[1] = new JsEventDesc(ON_BLUR, "focusEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnFocusEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_FOCUS);
		LfwParameter param = new LfwParameter();
		param.setName("focusEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnBlurEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_BLUR);
		LfwParameter param = new LfwParameter();
		param.setName("focusEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
