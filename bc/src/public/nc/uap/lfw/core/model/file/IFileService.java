package nc.uap.lfw.core.model.file;

public interface IFileService {
	/**
	 * ����pkȡ�ļ�
	 * 
	 * @param pk_form
	 * @return
	 */
	public String[] getFiles(String pk_form);
	
	/**
	 * ȡ����·��Url
	 * @param id
	 * @return
	 */
	public String getDownUrl(String id);

	/**
	 * ɾ��File
	 * 
	 * @param fileNo
	 * @return
	 */
	public void deleteFile(String fileNo);
}
