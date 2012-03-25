package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class LinkListener extends JsListenerConf {

	private static final long serialVersionUID = 7593116890067884230L;
	
	public static final String ON_CLICK = "onclick";

	@Override
	public String getJsClazz() {
		return "LinkListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(ON_CLICK, "linkEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnClickEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CLICK);
		LfwParameter param = new LfwParameter();
		param.setName("linkEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
