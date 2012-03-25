package nc.uap.lfw.core.event.conf;

import nc.uap.lfw.core.data.LfwParameter;

/**
 * 创建子项容器事件（目前用于菜单中）
 *  
 * @author guoweic
 *
 */
public class ContainerListener extends JsListenerConf {

	private static final long serialVersionUID = -638312509634452557L;

	public static final String ON_CONTAINER_CREATE = "onContainerCreate";

	public String getJsClazz() {
		return "ContainerListener";
	}
	
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(ON_CONTAINER_CREATE, "simpleEvent");
		return descs;
	}
	
	public static EventHandlerConf getOnContainerCreateEvent() {
		EventHandlerConf event = new EventHandlerConf();
		event.setName(ON_CONTAINER_CREATE);
		LfwParameter param = new LfwParameter();
		param.setName("simpleEvent");
		event.addParam(param);
		return event;
	}

}
