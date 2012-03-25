package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 
 * @author guoweic
 *
 */
public class SpliterListener extends JsListenerConf {

	private static final long serialVersionUID = -8719525347648390711L;
	
	public static final String RESIZE_DIV2 = "resizeDiv2";
	public static final String RESIZE_DIV1 = "resizeDiv1";

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[2];
		descs[0] = new JsEventDesc(RESIZE_DIV1, "spliterEvent");
		descs[1] = new JsEventDesc(RESIZE_DIV2, "spliterEvent");
		return descs;
	}
	
	public static EventHandlerConf getResizeDiv1Event() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(RESIZE_DIV1);
		LfwParameter param = new LfwParameter();
		param.setName("spliterEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	
	public static EventHandlerConf getResizeDiv2Event() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(RESIZE_DIV2);
		LfwParameter param = new LfwParameter();
		param.setName("spliterEvent");
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}

	@Override
	public String getJsClazz() {
		return "SpliterListener";
	}

}
