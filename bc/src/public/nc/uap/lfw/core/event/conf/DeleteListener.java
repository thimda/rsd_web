package nc.uap.lfw.core.event.conf;

/**
 * 从其他途径的来的Listener，如果删除，将删除信息放入DeleteListener
 * 只保存其id即可
 * @author zhangxya
 *
 */
public class DeleteListener extends JsListenerConf{

	private static final long serialVersionUID = 1L;

	@Override
	protected JsEventDesc[] createJsEventDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJsClazz() {
		// TODO Auto-generated method stub
		return null;
	}

}
