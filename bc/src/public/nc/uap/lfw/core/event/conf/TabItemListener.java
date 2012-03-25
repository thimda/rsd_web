package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class TabItemListener extends JsListenerConf {

	private static final long serialVersionUID = 4568675012030112688L;
	
	public static final String ON_CLOSE = "onclose";

	@Override
	public String getJsClazz() {
		return "TabItemListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(ON_CLOSE, "tabItemEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnCloseEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CLOSE);
		LfwParameter param = new LfwParameter();
		param.setName("tabItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
