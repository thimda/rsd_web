package nc.uap.lfw.file.event;

import java.io.OutputStream;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.vo.LfwFileVO;

/**
 * 文件上传事件
 * 
 * @author licza
 * 
 */
public class UploadEvent {

	LfwFileVO file;
	String user_name;

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 *上传事件
	 * 
	 * @param file
	 *            上传文件
	 * @param user_name
	 *            上传人姓名
	 */
	public UploadEvent(LfwFileVO file, String user_name) {
		super();
		this.file = file;
		this.user_name = user_name;
	}
	/**
	 * 获得文件的输出流
	 * @param out
	 */
	public void getOutputStream(OutputStream out){
		try {
			new FileManager().download(getFile().getPk_lfwfile(), out);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}

	public LfwFileVO getFile() {
		return file;
	}
}
