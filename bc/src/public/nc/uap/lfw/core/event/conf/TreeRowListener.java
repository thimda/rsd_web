package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

public class TreeRowListener extends JsListenerConf {

	private static final long serialVersionUID = 2322522905341760932L;
	
	public static final String BEFORE_NODE_CREATE = "beforeNodeCreate";

	@Override
	public String getJsClazz() {
		return "TreeRowListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(BEFORE_NODE_CREATE, "treeRowEvent");
		return descs;
	}
	
	public static EventHandlerConf getBeforeNodeCreateEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_NODE_CREATE);
		LfwParameter param = new LfwParameter();
		param.setName("treeRowEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
