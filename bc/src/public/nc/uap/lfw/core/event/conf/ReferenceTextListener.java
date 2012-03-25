package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class ReferenceTextListener extends JsListenerConf {

	private static final long serialVersionUID = -2466758758675812584L;
	
	public static final String BEFORE_OPEN_REF_PAGE = "beforeOpenReference";

	@Override
	public String getJsClazz() {
		return "ReferenceTextListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(BEFORE_OPEN_REF_PAGE, "simpleEvent");
		return descs;
	}
	
	public static EventHandlerConf getBeforeOpenRefDialogEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_OPEN_REF_PAGE);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
