package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * @author guoweic
 *
 */
public class KeyListener extends JsListenerConf {

	private static final long serialVersionUID = -4253180527143374857L;
	
	public static final String ON_KEY_UP = "onKeyUp";
	public static final String ON_ENTER = "onEnter";
	public static final String ON_KEY_DOWN = "onKeyDown";

	@Override
	public String getJsClazz() {
		return "KeyListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[3];
		descs[0] = new JsEventDesc(ON_KEY_DOWN, "keyEvent");
		descs[1] = new JsEventDesc(ON_ENTER, "keyEvent");
		descs[2] = new JsEventDesc(ON_KEY_UP, "keyEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnKeyDownEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_KEY_DOWN);
		LfwParameter param = new LfwParameter();
		param.setName("keyEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnEnterEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_ENTER);
		LfwParameter param = new LfwParameter();
		param.setName("keyEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnKeyUpEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_KEY_UP);
		LfwParameter param = new LfwParameter();
		param.setName("keyEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
