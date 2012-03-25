package nc.uap.lfw.core.refnode;

/**
 * 自定义类型参照，表明参照页面，参照逻辑都统一由开发者控制
 * @author dengjt
 *
 */
public class SelfDefRefNode extends BaseRefNode{
	private static final long serialVersionUID = 5017526449328725295L;
	public SelfDefRefNode() {
		super();
		setDialog(true);
	}
	public String getLangDir() {
		return null;
	}

}
