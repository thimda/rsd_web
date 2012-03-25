package nc.uap.lfw.file.filesystem;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.file.core.FileExistOperMode;
import nc.file.core.IFileStoreView;
import nc.file.fsv.local.ILocalFileStoreViewParamKeys;
import nc.file.fsv.local.LocalFileStoreViewFactory;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.vo.LfwFileVO;

import org.apache.commons.lang.StringUtils;

/**
 * 使用文档中心的文件系统
 * @author licza
 *
 */
public class DocumentCenterFileSystem extends LocalFileSystem implements ILfwFileSystem {
	public DocumentCenterFileSystem(String filePath) {
		super(filePath);
	}

	@Override
	public void deleteFile(Serializable fileVO) throws Exception {
		LfwFileVO vo = (LfwFileVO)fileVO;
		String path = createFilePath(vo);
		IFileStoreView fsv = null;
		try {
			fsv = getFsv();
			fsv.openConnection(null);
			fsv.deleteFile(path);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			if(fsv != null){
				fsv.closeConnection();
			}
		}
	}

	@Override
	public void deleteFile(Serializable[] fileVOs) throws Exception {
		if(fileVOs == null || fileVOs.length <= 0)
		for(Serializable fileVO : fileVOs){
			deleteFile(fileVO);
		}
	}

	@Override
	public Object download(Serializable fileVO, OutputStream output, long begin)
			throws Exception {
		String path = createFilePath((LfwFileVO)fileVO);
		IFileStoreView fsv = null;
		try {
			fsv = getFsv();
			fsv.openConnection(null);
			fsv.readFile(path, output);
			return null;
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			return null;
		}finally{
			if(fsv != null){
				fsv.closeConnection();
			}
		}
	}

	@Override
	public Object download(Serializable fileVO, OutputStream out)
			throws Exception {
		return super.download(fileVO, out);
	}

	@Override
	public boolean existInFs(String fileNo) throws Exception {
		LfwFileVO fileInfo = new FileManager().getFileQryService().getFile(fileNo);
		String path = createFilePath(fileInfo);
		IFileStoreView fsv = null;
		try {
			fsv = getFsv();
			fsv.openConnection(null);
			return fsv.exists(path);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			return false;
		}finally{
			if(fsv != null){
				fsv.closeConnection();
			}
		}
	}

	@Override
	public void upload(Serializable fileVO, InputStream input) throws Exception {
		IFileStoreView fsv = null;
		try {
			fsv = getFsv();
			fsv.openConnection(null);
			fsv.writeFile(input, createFilePath((LfwFileVO)fileVO), FileExistOperMode.FILE_EXIST_OVERWIRT);
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			if(fsv != null){
				fsv.closeConnection();
			}
		}
	}

	public IFileStoreView getFsv() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ILocalFileStoreViewParamKeys.ROOT_DIR_PATH, filePath);
		return new LocalFileStoreViewFactory().createfileStoreView(params);
	}
	protected String createFilePath(LfwFileVO fileVersionVO) {
		/**
		 * 如果没有文件名.则使用默认设置
		 */
		if(fileVersionVO.getFilename() == null)
			return super.createFilePath(fileVersionVO);

		String id = fileVersionVO.getPk_lfwfile();
		if (id == null || id.length() == 0) {
			return null;
		}
		//补齐
		int idLength = id.length() + 1;
		// K为每段的长度
		final int k = 3;
		int rootPathLength = idLength % k ;
		StringBuilder sb = new StringBuilder();
		//sb.append(filePath);
		// 返回文件分隔符
		String seprator = System.getProperty("file.separator");
		if (rootPathLength != 0) {
			sb.append(seprator);
			sb.append(id.substring(0, rootPathLength));
		}
		for (int i = 0; i < idLength / k; i++) {
			sb.append(seprator);
			sb.append(StringUtils.substring(id, i * k, i * k + k));
		}
		sb.append(seprator);
		sb.append(fileVersionVO.getFilename());
		return sb.toString();
	}
}
