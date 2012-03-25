package nc.uap.lfw.core.model.file;

public interface IFileService {
	/**
	 * 跟据pk取文件
	 * 
	 * @param pk_form
	 * @return
	 */
	public String[] getFiles(String pk_form);
	
	/**
	 * 取下载路径Url
	 * @param id
	 * @return
	 */
	public String getDownUrl(String id);

	/**
	 * 删除File
	 * 
	 * @param fileNo
	 * @return
	 */
	public void deleteFile(String fileNo);
}
