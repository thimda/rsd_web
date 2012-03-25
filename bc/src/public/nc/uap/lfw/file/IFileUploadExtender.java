package nc.uap.lfw.file;

import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.file.vo.LfwFileVO;

import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 
 * @author lisw
 * �ϴ��ļ���չ����
 * ������ϴ��ļ���ִ���������������γ������ļ��ļ���Ϊ����������,��������չ����
 */
public interface IFileUploadExtender {
	/**
	 * ��չִ��
	 * @param req �������
	 * @param filevo �ļ�����
	 */
	public void extend(MultipartHttpServletRequest req,String filepk) throws LfwRuntimeException;
	
	public String[] getRetValues();
}
