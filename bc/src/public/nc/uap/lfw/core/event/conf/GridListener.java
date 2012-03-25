package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

public class GridListener extends JsListenerConf {

	private static final long serialVersionUID = 5167669090828251715L;
	
//	public static final String PROCESS_PAGE_COUNT = "processPageCount";
	public static final String ON_DATA_OUTER_DIV_CONTEXT_MENU = "onDataOuterDivContextMenu";

	@Override
	public String getJsClazz() {
		return "GridListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(ON_DATA_OUTER_DIV_CONTEXT_MENU, "mouseEvent");
//		descs[1] = new JsEventDesc(PROCESS_PAGE_COUNT, "processPageCountEvent");
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
	
//	public static EventHandlerConf getProcessPageCountEvent() {
//		EventHandlerConf event = new EventHandlerConf();
//		event.setName(PROCESS_PAGE_COUNT);
//		LfwParameter param = new LfwParameter();
//		param.setName("processPageCountEvent");
//		event.addParam(param);
//		EventSubmitRule esb = new EventSubmitRule();
//		event.setSubmitRule(esb);
//		return event;
//	}

}
