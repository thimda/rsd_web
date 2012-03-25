package nc.uap.lfw.file;


import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.file.helper.Scene;

/**
 * LFW上传辅助类
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
	 * 上传
	 */
	public void upload(){
		String uploader = LfwRuntimeEnvironment.getRootPath() + "/core/file.jsp?pageId=file";
		ctx.showModalDialog(uploader + scene.toString(), "上传文件", "450", "425", "file_uploader", false, false);
	}
	/**
	 * 下载
	 * @param id 文件PK
	 */
	public void down(String id){
		String url = getDownUrl(id);
		ctx.openNewWindow(url, "下载", "100", "100");
	}
	/**
	 * 获得下载路径
	 * @param id 文件PK
	 * @return
	 */
	public String getDownUrl(String id){
		return LfwRuntimeEnvironment.getRootPath() + "/pt/file/down?id=" + id;
	}
	/**
	 * 获得查看路径
	 * @param id 文件PK
	 * @return
	 */
	public String getViewUrl(String id){
		return LfwRuntimeEnvironment.getRootPath() + "/pt/file/view?id=" + id;
	}
}
