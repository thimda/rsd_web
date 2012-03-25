package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

public class TreeNodeListener extends JsListenerConf {

	private static final long serialVersionUID = 3497937000050545357L;
	
	public static final String ON_DRAG_END = "onDragEnd";
	public static final String ON_DRAG_START = "onDragStart";
	public static final String ON_DBCLICK = "ondbclick";
	public static final String ON_CLICK = "onclick";
	public static final String ON_NODE_LOAD = "onNodeLoad";
	public static final String ON_CHECKED = "onChecked";
	public static final String ON_NODE_DELETE = "onNodeDelete";
	public static final String BEFORE_SEL_NODE_CHANGE = "beforeSelNodeChange";
	public static final String AFTER_SEL_NODE_CHANGE = "afterSelNodeChange";
	public static final String ROOT_NODE_CREATED = "rootNodeCreated";
	public static final String NODE_CREATED = "nodeCreated";
	public static final String BEFORE_NODE_CAPTION_CHANGE = "beforeNodeCaptionChange";

	@Override
	public String getJsClazz() {
		return "TreeNodeListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[12];
		descs[0] = new JsEventDesc(NODE_CREATED, "treeNodeEvent");
		descs[1] = new JsEventDesc(ROOT_NODE_CREATED, "treeNodeEvent");
		descs[2] = new JsEventDesc(AFTER_SEL_NODE_CHANGE, "treeNodeEvent");
		descs[3] = new JsEventDesc(BEFORE_SEL_NODE_CHANGE, "treeNodeEvent");
		descs[4] = new JsEventDesc(ON_NODE_LOAD, "treeNodeEvent");
		descs[5] = new JsEventDesc(ON_CHECKED, "treeNodeEvent");
		descs[6] = new JsEventDesc(ON_NODE_DELETE, "treeNodeEvent");

		descs[7] = new JsEventDesc(ON_CLICK, "treeNodeMouseEvent");
		descs[8] = new JsEventDesc(ON_DBCLICK, "treeNodeMouseEvent");

		descs[9] = new JsEventDesc(ON_DRAG_START, "treeNodeDragEvent");
		descs[10] = new JsEventDesc(ON_DRAG_END, "treeNodeDragEvent");

		descs[11] = new JsEventDesc(BEFORE_NODE_CAPTION_CHANGE, "treeNodeEvent");
		
		return descs;
	}
	
	public static EventHandlerConf getNodeCreatedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(NODE_CREATED);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getRootNodeCreatedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ROOT_NODE_CREATED);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getAfterSelNodeChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(AFTER_SEL_NODE_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getBeforeSelNodeChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_SEL_NODE_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnNodeLoadEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_NODE_LOAD);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnCheckedEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CHECKED);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnNodeDeleteEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_NODE_DELETE);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnClickEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CLICK);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeMouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnDbclickEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_DBCLICK);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeMouseEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnDragStartEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_DRAG_START);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeDragEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getOnDragEndEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_DRAG_END);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeDragEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getBeforeNodeCaptionChangeEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(BEFORE_NODE_CAPTION_CHANGE);
		LfwParameter param = new LfwParameter();
		param.setName("treeNodeEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
