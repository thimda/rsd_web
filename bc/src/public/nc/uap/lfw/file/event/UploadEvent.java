package nc.uap.lfw.file.event;

import java.io.OutputStream;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.vo.LfwFileVO;

/**
 * �ļ��ϴ��¼�
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
	 *�ϴ��¼�
	 * 
	 * @param file
	 *            �ϴ��ļ�
	 * @param user_name
	 *            �ϴ�������
	 */
	public UploadEvent(LfwFileVO file, String user_name) {
		super();
		this.file = file;
		this.user_name = user_name;
	}
	/**
	 * ����ļ��������
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
