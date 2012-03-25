package nc.uap.lfw.file.filesystem;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * LFW�ļ������ӿ�
 * 
 * @author licza
 * 
 */
public interface ILfwFileSystem {
	/**
	 * �����ļ�
	 * 
	 * @param FileVO���ĵ�������Ϣ
	 * @param out����ȡ������д��out����
	 * @return
	 */
	public Object download(Serializable fileVO, OutputStream out) throws Exception;

	/**
	 * �����ļ�
	 * 
	 * @param fileVO
	 * @param input
	 * @throws Exception
	 */
	public void upload(Serializable fileVO, InputStream input) throws Exception;

	/**
	 * ɾ���ļ�
	 * 
	 * @param fileVO
	 * @throws Exception
	 */
	public void deleteFile(Serializable fileVO) throws Exception;
	

	/**
	 * �ļ�ϵͳ���Ƿ���ڴ��ļ�
	 * @param fileNo
	 * @return
	 * @throws Exception
	 */
	public boolean existInFs(String fileNo) throws Exception;
	
 }
