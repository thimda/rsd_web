package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class ExcelCellListener extends JsListenerConf {

	private static final long serialVersionUID = -3695845890413486543L;
	
	public static final String BEFORE_EDIT = "beforeEdit";
	public static final String AFTER_EDIT = "afterEdit";
	public static final String CELL_EDIT = "cellEdit";
	public static final String ON_CELL_CLICK = "onCellClick";
	public static final String CELL_VALUE_CHANGED = "cellValueChanged";
	
	@Override
	public String getJsClazz() {
		return "ExcelCellListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[5];
		descs[0] = new JsEventDesc(ON_CELL_CLICK, "cellEvent");
		descs[1] = new JsEventDesc(CELL_EDIT, "cellEvent");
		descs[2] = new JsEventDesc(AFTER_EDIT, "cellEvent");
		descs[3] = new JsEventDesc(BEFORE_EDIT, "cellEvent", false);
		descs[4] = new JsEventDesc(CELL_VALUE_CHANGED, "cellEvent", false);
		return descs;
	}
	
	public static EventHandlerConf getOnCellClickEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CELL_CLICK);
		LfwParameter param = new LfwParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getCellEditEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(CELL_EDIT);
		LfwParameter param = new LfwParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getAfterEditEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(AFTER_EDIT);
		LfwParameter param = new LfwParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getBeforeEditEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_EDIT);
		LfwParameter param = new LfwParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getCellValueChangedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(CELL_VALUE_CHANGED);
		LfwParameter param = new LfwParameter();
		param.setName("cellEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
