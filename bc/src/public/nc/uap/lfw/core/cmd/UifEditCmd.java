package nc.uap.lfw.core.cmd;


/**
 * "�༭"�˵��߼�����,����ָ����ViewID
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
