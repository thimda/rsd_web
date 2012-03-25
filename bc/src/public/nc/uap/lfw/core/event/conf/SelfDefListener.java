package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 自定义组件事件
 * 
 * @author guoweic
 *
 */
public class SelfDefListener extends JsListenerConf {
	
	private static final long serialVersionUID = 5755817543115686744L;
	
	public static final String ON_SELF_DEF_EVENT = "onSelfDefEvent";
	public static final String ON_CREATE_EVENT = "oncreate";
	@Override
	public String getJsClazz() {
		return "SelfDefListener";
	}

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[2];
		descs[0] = new JsEventDesc(ON_SELF_DEF_EVENT, "selfDefEvent");
		descs[1] = new JsEventDesc(ON_CREATE_EVENT, "oncreate", true);
		return descs;
	}
	
	public static EventHandlerConf getOnClickEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_SELF_DEF_EVENT);
		LfwParameter param = new LfwParameter();
		param.setName("selfDefEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

}
