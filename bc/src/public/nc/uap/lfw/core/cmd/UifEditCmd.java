package nc.uap.lfw.core.cmd;


/**
 * "编辑"菜单逻辑处理,弹出指定的ViewID
 *
 */
public class UifEditCmd extends UifOpenViewCmd {

	public UifEditCmd(String viewId){
		super(viewId);
	}
	
	public UifEditCmd(String viewId, String width, String height){
		super(viewId, width, height);
	}
	
	public UifEditCmd(String viewId, String width, String height, String title) {
		super(viewId, width, height, title);
	}
}
