package nc.uap.lfw.file;


import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.file.helper.Scene;

/**
 * LFW�ϴ�������
 * @author licza
 *
 */
public class FileUploadHelper {
	private Scene scene  = null;
	private LfwPageContext ctx;
	public FileUploadHelper(Scene scene,LfwPageContext ctx){
		this.scene = scene;
		this.ctx = ctx;
	}
	
	public FileUploadHelper(LfwPageContext ctx){
		this.ctx = ctx;
	}
	/**
	 * �ϴ�
	 */
	public void upload(){
		String uploader = LfwRuntimeEnvironment.getRootPath() + "/core/file.jsp?pageId=file";
		ctx.showModalDialog(uploader + scene.toString(), "�ϴ��ļ�", "450", "425", "file_uploader", false, false);
	}
	/**
	 * ����
	 * @param id �ļ�PK
	 */
	public void down(String id){
		String url = getDownUrl(id);
		ctx.openNewWindow(url, "����", "100", "100");
	}
	/**
	 * �������·��
	 * @param id �ļ�PK
	 * @return
	 */
	public String getDownUrl(String id){
		return LfwRuntimeEnvironment.getRootPath() + "/pt/file/down?id=" + id;
	}
	/**
	 * ��ò鿴·��
	 * @param id �ļ�PK
	 * @return
	 */
	public String getViewUrl(String id){
		return LfwRuntimeEnvironment.getRootPath() + "/pt/file/view?id=" + id;
	}
}
