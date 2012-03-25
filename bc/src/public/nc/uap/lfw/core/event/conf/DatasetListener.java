package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;



public class DatasetListener extends JsListenerConf {
	public static final String ON_DATA_LOAD = "onDataLoad";
	public static final String ON_AFTER_PAGE_CHANGE = "onAfterPageChange";
	public static final String ON_BEFORE_PAGE_CHANGE = "onBeforePageChange";
	public static final String ON_AFTER_ROW_DELETE = "onAfterRowDelete";
	public static final String ON_BEFORE_ROW_DELETE = "onBeforeRowDelete";
	public static final String ON_AFTER_DATA_CHANGE = "onAfterDataChange";
	public static final String ON_BEFORE_DATA_CHANGE = "onBeforeDataChange";
	public static final String ON_AFTER_ROW_INSERT = "onAfterRowInsert";
	public static final String ON_BEFORE_ROW_INSERT = "onBeforeRowInsert";
	public static final String ON_AFTER_ROW_UN_SELECT = "onAfterRowUnSelect";
	public static final String ON_AFTER_ROW_SELECT = "onAfterRowSelect";
	public static final String ON_BEFORE_ROW_SELECT = "onBeforeRowSelect";
	private static final long serialVersionUID = 1L;

	@Override
	public String getJsClazz() {
		return "DatasetListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[14];
		descs[0] = new JsEventDesc(ON_BEFORE_ROW_SELECT, "dsIndexEvent");
		descs[1] = new JsEventDesc(ON_AFTER_ROW_SELECT, "rowSelectEvent");
		descs[2] = new JsEventDesc(ON_AFTER_ROW_UN_SELECT, "rowUnSelectEvent");
		descs[3] = new JsEventDesc(ON_BEFORE_ROW_INSERT, "dsBeforeRowInsertEvent");
		descs[4] = new JsEventDesc(ON_AFTER_ROW_INSERT, "rowInsertEvent");
		descs[5] = new JsEventDesc(ON_BEFORE_DATA_CHANGE, "dsBeforeDataChangeEvent");
		descs[6] = new JsEventDesc(ON_AFTER_DATA_CHANGE, "dataChangeEvent");
		descs[7] = new JsEventDesc(ON_BEFORE_ROW_DELETE, "dsBeforeRowDeleteEvent");
		descs[8] = new JsEventDesc(ON_AFTER_ROW_DELETE, "rowDeleteEvent");
		descs[9] = new JsEventDesc(ON_BEFORE_PAGE_CHANGE, "dsBeforePageChangeEvent");
		descs[10] = new JsEventDesc(ON_AFTER_PAGE_CHANGE, "pageChangeEvent");
		descs[11] = new JsEventDesc(ON_DATA_LOAD, "dataLoadEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnBeforeRowSelectEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_BEFORE_ROW_SELECT);
		LfwParameter param = new LfwParameter();
		param.setName("dsIndexEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnAfterRowSelectEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_AFTER_ROW_SELECT);
		LfwParameter param = new LfwParameter();
		param.setName("rowSelectEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnAfterRowUnSelectEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_AFTER_ROW_UN_SELECT);
		LfwParameter param = new LfwParameter();
		param.setName("rowUnSelectEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnBeforeRowInsertEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_BEFORE_ROW_INSERT);
		LfwParameter param = new LfwParameter();
		param.setName("dsBeforeRowInsertEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnAfterRowInsertEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_AFTER_ROW_INSERT);
		LfwParameter param = new LfwParameter();
		param.setName("rowInsertEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnBeforeDataChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_BEFORE_DATA_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("dsBeforeDataChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnAfterDataChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_AFTER_DATA_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("dataChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnBeforeRowDeleteEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_BEFORE_ROW_DELETE);
		LfwParameter param = new LfwParameter();
		param.setName("dsBeforeRowDeleteEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnAfterRowDeleteEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_AFTER_ROW_DELETE);
		LfwParameter param = new LfwParameter();
		param.setName("rowDeleteEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnBeforePageChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_BEFORE_PAGE_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("dsBeforePageChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnAfterPageChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_AFTER_PAGE_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("pageChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnDataLoadEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_DATA_LOAD);
		LfwParameter param = new LfwParameter();
		param.setName("dataLoadEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

	@Override
	public EventHandlerConf getEventTemplate(String key) {
		if(key.equals(ON_DATA_LOAD))
			return getOnDataLoadEvent();
		if(key.equals(ON_AFTER_DATA_CHANGE))
			return getOnAfterDataChangeEvent();
		if(key.equals(ON_AFTER_ROW_SELECT)){
			return getOnAfterRowSelectEvent();
		}
		else
			return super.getEventTemplate(key);
	}
	
}
