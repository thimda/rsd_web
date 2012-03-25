package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class CheckboxListener extends JsListenerConf {

	private static final long serialVersionUID = -6115578985256536433L;
	
	private static final String ON_CHANGE = "onChange";

	@Override
	public String getJsClazz() {
		return "CheckboxListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(ON_CHANGE, "checkboxValueChangeEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("checkboxValueChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
