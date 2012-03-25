package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

public class ExcelListener extends JsListenerConf {

	private static final long serialVersionUID = 5167669090828251715L;

	public static final String ON_DATA_OUTER_DIV_CONTEXT_MENU = "onDataOuterDivContextMenu";

	@Override
	public String getJsClazz() {
		return "ExcelListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(ON_DATA_OUTER_DIV_CONTEXT_MENU, "mouseEvent");
		return descs;
	}

	public static EventHandlerConf getOnDataOuterDivContextMenuEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_DATA_OUTER_DIV_CONTEXT_MENU);
		LfwParameter param = new LfwParameter();
		param.setName("mouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
