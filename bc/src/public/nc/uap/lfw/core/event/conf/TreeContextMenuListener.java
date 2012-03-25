package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class TreeContextMenuListener extends JsListenerConf {

	private static final long serialVersionUID = -6547795076131696969L;
	
	public static final String BEFORE_CONTEXT_MENU = "beforeContextMenu";

	@Override
	public String getJsClazz() {
		return "TreeContextMenuListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(BEFORE_CONTEXT_MENU, "treeContextMenuEvent");
		return descs;
	}
	
	public static EventHandlerConf getBeforeContextMenuEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_CONTEXT_MENU);
		LfwParameter param = new LfwParameter();
		param.setName("treeContextMenuEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
