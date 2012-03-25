package nc.uap.lfw.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ModelServerConfig;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.filesystem.DocumentCenterFileSystem;
import nc.uap.lfw.file.filesystem.ILfwFileSystem;
import nc.uap.lfw.file.itf.ILfwFileQryService;
import nc.uap.lfw.file.itf.ILfwFileService;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * LFW文件系统
 * 
 * @author licza
 * 
 * @modify lisw 增加系统预置filemanager的方法
 * 
 * @modify lisw 将 FileManager 提高至系统对外公开接口
 */
public class FileManager {
	public final static String FILE_PK = "filepk";
	private final static String FILEMANAGER = "filemanager";
	/**
	 * 是否客户机
	 * 
	 * @return
	 */
	public boolean isClient() {
		return Boolean.TRUE;
	}

	/**
	 * 获得文件存储路径
	 * 
	 * @return
	 */
	public String getFileStore() {
		String fileStore = "$/resources/lfw/attachment";
		/**
		 * 当前ctx配置
		 */
//		String fileStore = LfwRuntimeEnvironment.getModelServerConfig().getConfigValue("filestore");
//		
//		if (StringUtils.isEmpty(fileStore)) {
//			/**
//			 * Lfw配置
//			 */
//			fileStore = LfwRuntimeEnvironment.getServerConfig().get("filestore");
//		}
//		if(StringUtils.isEmpty(fileStore))
//			throw new LfwRuntimeException("未配置文件存储路径!请在system.properties中设置");
//		/**
//		 * 设置相对位置
//		 */
		if(fileStore.startsWith("$"))
			fileStore = RuntimeEnv.getInstance().getNCHome() + fileStore.substring(1);
		
		return fileStore;
	};

	/**
	 * 获得上传服务器链接
	 * 
	 * @return
	 * 
	 */
	public String getFileServerURL() {
		String fileServerURL = LfwRuntimeEnvironment.getModelServerConfig().getConfigValue("fileserver");
		if (StringUtils.isEmpty(fileServerURL)) {
			throw new LfwRuntimeException("未配置文件存储路径!");
		}
		return fileServerURL;
	};

	/**
	 * 获得LFW文件系统
	 * 
	 * @return
	 */
	public ILfwFileSystem getFileSystem() {
//		if (isClient()) {
			return new DocumentCenterFileSystem(getFileStore());
//		} else {
//			return new NetworkFileSystem(getFileServerURL(), LfwRuntimeEnvironment.getDatasource());
//		}
	}
	public ILfwFileSystem getFileSystem(LfwFileVO vo) {
		return getFileSystem();
	}
	
	/**
	 * 上传文件
	 * 
	 * @param fileName 文件名
	 * @param billType 业务类型
	 * @param billItem 业务标志
	 * @param in 入流
	 * @return
	 * @throws Exception
	 */
	public String upload(String fileName, String billType, String billItem, long size, InputStream in) throws Exception {
		return upload(fileName, billType, billItem,size, in, Boolean.FALSE.booleanValue());
	}

	/**
	 * 
	 * 上传文件
	 * 
	 * @param fileName 文件名
	 * @param billType 业务类型
	 * @param billItem 业务标志
	 * @param in 入流
	 * @param override是否覆盖
	 * @return
	 * @throws Exception
	 */
	public String upload(String fileName, String billType, String billItem,long size, InputStream in, boolean override) throws Exception {
		ILfwFileService fileService = NCLocator.getInstance().lookup(ILfwFileService.class);
		ILfwFileQryService fileQry = NCLocator.getInstance().lookup(ILfwFileQryService.class);
		LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
		String pk_user = null;
		if(ses != null)
			pk_user = ses.getPk_user();
		LfwFileVO vo = new LfwFileVO();
		vo.setFilename(fileName);
		vo.setPk_billitem(billItem);
		vo.setPk_billtype(billType);
		vo.setCreator(pk_user);
		vo.setCreattime(new UFDateTime());
		vo.setLastmodifyer(pk_user);
		vo.setLastmodifytime(new UFDateTime());
		vo.setCreatestatus("0");
		try {
			/*
			if (override) {
				LfwFileVO[] fvo = fileQry.getFile(billType, billItem);
				if (!ArrayUtils.isEmpty(fvo)) {
					vo = fvo[0];
					vo.setFilename(fileName);
					vo.setFilesize(size);
					fileService.updataVos(new LfwFileVO[]{vo});
					return vo.getPk_lfwfile();
				}
			}*/
			vo.setFilesize(size);
			vo.setFiletypo(getFileType(fileName));
			return fileService.add(vo);
		} catch (LfwBusinessException e) {
			throw e;
		} finally {
			if (StringUtils.isNotBlank(vo.getPk_lfwfile()) && in != null)
				getFileSystem(vo).upload(vo, in);
 		}
	}
	/**
	 * 覆盖文件
	 * @param filepk 文件pk
	 * @param size 文件大小
	 * @param in 输入流
	 * @throws Exception
	 */
	public void ReUpload(String filepk,long size, InputStream in) throws Exception{
		
		ILfwFileQryService fileQry = NCLocator.getInstance().lookup(ILfwFileQryService.class);
		LfwFileVO vo = fileQry.getFile(filepk);
		if(vo == null)
				throw new LfwBusinessException("can not find files");
		vo.setFilesize(size);
		ReUpload(vo,in);
	}
	/**
	 * 覆盖文件
	 * @param vo 文件vo
	 * @param in 输入流
	 * @throws Exception
	 */
	public void ReUpload(LfwFileVO vo, InputStream in) throws Exception{
		ILfwFileService fileService = NCLocator.getInstance().lookup(ILfwFileService.class);
		fileService.updataVos(new LfwFileVO[]{ vo});
		getFileSystem(vo).upload(vo, in);
	}
	/**
	 * 文件复制
	 * @param fileName 文件名称
	 * @param billType 单据类型	 * 
	 * @param billItem 单据PK
	 * @param category 文件分组
	 * @param oldFilePK 源文件PK
	 * @return
	 * @throws Exception
	 */
	public String copyFile(String fileName, String billType, String billItem,String category, String oldFilePK) throws Exception{
		
		ILfwFileQryService fileQry = NCLocator.getInstance().lookup(ILfwFileQryService.class);
		LfwFileVO filevo = fileQry.getFile(oldFilePK);
		if(null == filevo)
			throw new LfwBusinessException("file is not exists");
		CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
		OutputStream out = cbb.getOutputStream();
		
		this.download(oldFilePK, out);
		out.close();
		InputStream in =  cbb.getInputStream();
		try{
			return this.upload(fileName, billType, billItem, filevo.getFilesize(), in);
		}
		finally{
			in.close();
		}
	}
	/**
	 * 获取fileVO
	 * @param filePK
	 * @return
	 * @throws LfwBusinessException 
	 */
	public LfwFileVO getFileVO(String filePK) throws LfwBusinessException{
		return getFileQryService().getFile(filePK);
	}	
	/**
	 * 插入文件对象
	 * @param vo
	 * @return
	 * @throws LfwBusinessException
	 */
	public String insertFileVO(LfwFileVO vo) throws LfwBusinessException{
		ILfwFileService service = this.getFileService();
		return service.add(vo);
	}
	
	/**
	 * 删除文件
	 * 
	 * @param fileNo 文件主键
	 * @throws Exception
	 */
	public void delete(String fileNo) throws Exception {
		LfwFileVO vo = getFileQryService().getFile(fileNo);
		getFileService().delete(vo);
		getFileSystem(vo).deleteFile(vo);
	}
	
	/**
	 * 根据VO删除文件件
	 * @param vo
	 * @throws LfwBusinessException
	 */
	public void delete(LfwFileVO vo) throws Exception{
		getFileService().delete(vo);
		getFileSystem(vo).deleteFile(vo);
	}

	/**
	 * 编辑vo
	 * @param vo
	 * @throws LfwBusinessException
	 */
	public void updateVo(LfwFileVO vo) throws LfwBusinessException{
		getFileService().edit(vo);
	}
	/**
	 * 更新多个Vo 
	 * @param vos
	 * @throws LfwBusinessException
	 */
	void updataVos(LfwFileVO[] vos) throws LfwBusinessException{
		getFileService().updataVos(vos);
	}
	

	/**
	 * 下载文件
	 * 
	 * @param fileNo
	 * @param out
	 * @throws Exception
	 */
	public void download(String fileNo, OutputStream out) throws Exception {
		LfwFileVO vo = getFileQryService().getFile(fileNo);
		getFileSystem(vo).download(vo, out);
	}
	
	/**
	 * 文件是否存在
	 * @param fileNo
	 * @return
	 * @throws Exception
	 */
	public boolean exist(String fileNo) throws Exception{
		return true;
	}

	/**
	 * 文件系统中是否存在此文件
	 * @param fileNo
	 * @return
	 * @throws Exception
	 */
	public boolean existInFs(String fileNo) throws Exception{
		LfwFileVO vo = getFileQryService().getFile(fileNo);
		return getFileSystem(vo).existInFs(fileNo);
	}
	/**
	 * 修改上传文件状态
	 * @param billitem
	 * @throws LfwRuntimeException
	 */
	public void billSaveComplete(String billitem) throws LfwRuntimeException{
		try{
			LfwFileVO[] files = getFileQryService().getFile("", billitem);
			if(files != null && files.length > 0){
				for(LfwFileVO file : files){
					file.setCreatestatus("1");					
				}
				getFileService().updataVos(files);
			}
		}
		catch(LfwBusinessException exp){
			LfwLogger.error(exp);
			throw new LfwRuntimeException(exp);
		}
	}
	/**
	 * 根据文件后缀名获得文件类型
	 * @param fileName
	 * @return
	 */
	public String getFileType(String fileName){
		String filetypo = "NaN";
		int beginIndex = fileName.lastIndexOf(".");
		if( beginIndex > 0){
			filetypo = fileName.substring(beginIndex + 1);
		}
		return filetypo;
	}
	/***
	 * 根据单据pk返回所有的附件列表
	 * @param itempk
	 * @return
	 * @throws LfwBusinessException
	 */
	public LfwFileVO[] getAttachFileByItemID(String itempk)  throws LfwBusinessException{
		return getFileByItemID(itempk);
	}
	/**
	 * 根据单据PK返回全部文件
	 * @param itempk
	 * @return
	 * @throws LfwBusinessException
	 */
	public LfwFileVO[] getFileByItemID(String itempk) throws LfwBusinessException{
		ILfwFileQryService fileQry  = getFileQryService();
		LfwFileVO[] fvos = fileQry.getFile(null, itempk);
		return fvos;
	}
	/**
	 * 获得文件查询服务
	 * @return
	 */
	public ILfwFileQryService getFileQryService(){
		return NCLocator.getInstance().lookup(ILfwFileQryService.class);
	}
	
	
	/**
	 * 获得文件服务
	 * @return
	 */
	private ILfwFileService getFileService(){
		return NCLocator.getInstance().lookup(ILfwFileService.class);
	}
	
	public static FileManager getSystemFileManager(){
		FileManager fileManager = null;
		String fileManagerClazz = "";
		
		ModelServerConfig  MsCfg = LfwRuntimeEnvironment.getModelServerConfig();
		fileManagerClazz = MsCfg.getConfigValue(FILEMANAGER);
		ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
		/**
		 * 当前ctx配置了默认的文件管理类
		 */
		if(StringUtils.isEmpty(fileManagerClazz)){
			//默认采用门户的portalFileManager
			fileManagerClazz = "nc.uap.portal.comm.file.PortalFileManager";
		}
		try {
			fileManager = (FileManager) ctxLoader.loadClass(fileManagerClazz).newInstance();
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}
		
		return fileManager == null ?  new FileManager() : fileManager ;
	}
	
}
