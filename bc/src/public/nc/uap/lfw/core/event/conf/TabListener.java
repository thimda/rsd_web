package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class TabListener extends JsListenerConf {

	private static final long serialVersionUID = 8183794076873548178L;
	public static final String AFTER_ITEM_INIT = "afterItemInit";
	public static final String BEFORE_ITEM_INIT = "beforeItemInit";
	public static final String AFTER_CLOSE_ITEM = "afterCloseItem";
	public static final String BEFORE_ACTIVED_CHANGE = "beforeActivedTabItemChange";
	public static final String AFTER_ACTIVED_CHANGE = "afterActivedTabItemChange";
	
	
	@Override
	public String getJsClazz() {
		return "TabListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[5];
		descs[0] = new JsEventDesc(AFTER_ITEM_INIT, "tabItemEvent");
		descs[1] = new JsEventDesc(BEFORE_ITEM_INIT, "tabItemEvent");
		descs[2] = new JsEventDesc(AFTER_CLOSE_ITEM, "tabItemEvent");
		descs[3] = new JsEventDesc(BEFORE_ACTIVED_CHANGE, "tabItemChangeEvent");
		descs[4] = new JsEventDesc(AFTER_ACTIVED_CHANGE, "tabItemChangeEvent");
		return descs;
	}
	
	public static EventHandlerConf getBeforeCloseItemEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_ITEM_INIT);
		LfwParameter param = new LfwParameter();
		param.setName("tabItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getAfterCloseItemEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(AFTER_CLOSE_ITEM);
		LfwParameter param = new LfwParameter();
		param.setName("tabItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getAfterItemInitEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(AFTER_ITEM_INIT);
		LfwParameter param = new LfwParameter();
		param.setName("tabItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getBeforeActivedTabItemChange() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_ACTIVED_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("tabItemChangeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getAfterActivedTabItemChange() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(AFTER_ACTIVED_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("tabItemEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
