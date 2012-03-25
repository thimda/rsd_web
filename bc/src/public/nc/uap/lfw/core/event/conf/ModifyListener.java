package nc.uap.lfw.core.event.conf;


/**
 * combo À˘”√Listener
 * @author zhangxya
 *
 */
public class ModifyListener extends JsListenerConf {

	private static final long serialVersionUID = -4253180527143374857L;
	
	public static final String MODIFY_TEXT = "modifyText";


	@Override
	protected JsEventDesc[] createJsEventDesc() {
		JsEventDesc[] descs = new JsEventDesc[1];
		descs[0] = new JsEventDesc(MODIFY_TEXT, "ModifyEvent");
		return descs;
	}

	@Override
	public String getJsClazz() {
		return "ModifyListener";
	}	

}
