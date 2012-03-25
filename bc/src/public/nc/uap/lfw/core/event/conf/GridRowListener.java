package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class GridRowListener extends JsListenerConf {

	private static final long serialVersionUID = 830115133428826916L;
	
	public static final String ON_ROW_SELECTED = "onRowSelected";
	public static final String ON_ROW_DB_CLICK = "onRowDbClick";
	public static final String BEFORE_ROW_SELECTED = "beforeRowSelected";

	@Override
	public String getJsClazz() {
		return "GridRowListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[3];
		descs[0] = new JsEventDesc(BEFORE_ROW_SELECTED, "rowEvent");
		descs[1] = new JsEventDesc(ON_ROW_DB_CLICK, "rowEvent");
		descs[2] = new JsEventDesc(ON_ROW_SELECTED, "rowSelectedEvent");
		return descs;
	}
	
	public static EventHandlerConf getBeforeRowSelectedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_ROW_SELECTED);
		LfwParameter param = new LfwParameter();
		param.setName("rowEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnRowDbClickEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_ROW_DB_CLICK);
		LfwParameter param = new LfwParameter();
		param.setName("rowEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnRowSelectedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_ROW_SELECTED);
		LfwParameter param = new LfwParameter();
		param.setName("rowSelectedEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
