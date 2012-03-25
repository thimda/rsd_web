package nc.uap.lfw.core.model.file;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.vo.LfwFileVO;

public class FileServiceImpl implements IFileService {
	@Override
	public String[] getFiles(String pk_form) {
		String[] pks = pk_form.split(",");
		List<String> fileList = new ArrayList<String>(); 
		try {
			for (int i = 0; i< pks.length; i++){
				String pk = pks[i];
				if (pk.startsWith(FileManager.FILE_PK)){
					pk = pk.replace(FileManager.FILE_PK, "");
					LfwFileVO fileVo = FileManager.getSystemFileManager().getFileVO(pk);
					if (fileVo != null){
						String file = fileToString(fileVo);
						fileList.add(file);
					} 
				}else{
					LfwFileVO[] files;
					files = FileManager.getSystemFileManager().getFileByItemID(pk);
					if (files != null){
						//				String[] fileArray = new String[files.length];
						for (int j = 0; j < files.length; j++){
							String file = fileToString(files[j]);
							fileList.add(file);
						}
						//				return fileArray;
					}
				}
			}
		} catch (LfwBusinessException e) {
			LfwLogger.error(e);
			return new String[]{""};
		}
		return fileList.toArray(new String[0]);
	}
	
	private String fileToString(LfwFileVO file){
		String fileString = "{id:'" + file.getPk_lfwfile() + "',name:'" +  file.getDisplayname() + "',type:'" + file.getFiletypo() + "',filesize:'" + file.getFilesize() + "'}";
		return fileString;
	}

	@Override
	public String getDownUrl(String id) {
		return "\"" + LfwRuntimeEnvironment.getRootPath() + "/pt/file/down?id=" + id + "\"";
	}

	@Override
	public void deleteFile(String fileNo) {
		try {
			FileManager.getSystemFileManager().delete(fileNo);
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e);
		}
	}
}
