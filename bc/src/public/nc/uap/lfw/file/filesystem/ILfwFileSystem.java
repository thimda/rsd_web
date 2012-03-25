package nc.uap.lfw.file.filesystem;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * LFW文件操作接口
 * 
 * @author licza
 * 
 */
public interface ILfwFileSystem {
	/**
	 * 下载文件
	 * 
	 * @param FileVO：文档对象信息
	 * @param out：读取的数据写入out流中
	 * @return
	 */
	public Object download(Serializable fileVO, OutputStream out) throws Exception;

	/**
	 * 上载文件
	 * 
	 * @param fileVO
	 * @param input
	 * @throws Exception
	 */
	public void upload(Serializable fileVO, InputStream input) throws Exception;

	/**
	 * 删除文件
	 * 
	 * @param fileVO
	 * @throws Exception
	 */
	public void deleteFile(Serializable fileVO) throws Exception;
	

	/**
	 * 文件系统中是否存在此文件
	 * @param fileNo
	 * @return
	 * @throws Exception
	 */
	public boolean existInFs(String fileNo) throws Exception;
	
 }
