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
 * LFW�ļ�ϵͳ
 * 
 * @author licza
 * 
 * @modify lisw ����ϵͳԤ��filemanager�ķ���
 * 
 * @modify lisw �� FileManager �����ϵͳ���⹫���ӿ�
 */
public class FileManager {
	public final static String FILE_PK = "filepk";
	private final static String FILEMANAGER = "filemanager";
	/**
	 * �Ƿ�ͻ���
	 * 
	 * @return
	 */
	public boolean isClient() {
		return Boolean.TRUE;
	}

	/**
	 * ����ļ��洢·��
	 * 
	 * @return
	 */
	public String getFileStore() {
		String fileStore = "$/resources/lfw/attachment";
		/**
		 * ��ǰctx����
		 */
//		String fileStore = LfwRuntimeEnvironment.getModelServerConfig().getConfigValue("filestore");
//		
//		if (StringUtils.isEmpty(fileStore)) {
//			/**
//			 * Lfw����
//			 */
//			fileStore = LfwRuntimeEnvironment.getServerConfig().get("filestore");
//		}
//		if(StringUtils.isEmpty(fileStore))
//			throw new LfwRuntimeException("δ�����ļ��洢·��!����system.properties������");
//		/**
//		 * �������λ��
//		 */
		if(fileStore.startsWith("$"))
			fileStore = RuntimeEnv.getInstance().getNCHome() + fileStore.substring(1);
		
		return fileStore;
	};

	/**
	 * ����ϴ�����������
	 * 
	 * @return
	 * 
	 */
	public String getFileServerURL() {
		String fileServerURL = LfwRuntimeEnvironment.getModelServerConfig().getConfigValue("fileserver");
		if (StringUtils.isEmpty(fileServerURL)) {
			throw new LfwRuntimeException("δ�����ļ��洢·��!");
		}
		return fileServerURL;
	};

	/**
	 * ���LFW�ļ�ϵͳ
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
	 * �ϴ��ļ�
	 * 
	 * @param fileName �ļ���
	 * @param billType ҵ������
	 * @param billItem ҵ���־
	 * @param in ����
	 * @return
	 * @throws Exception
	 */
	public String upload(String fileName, String billType, String billItem, long size, InputStream in) throws Exception {
		return upload(fileName, billType, billItem,size, in, Boolean.FALSE.booleanValue());
	}

	/**
	 * 
	 * �ϴ��ļ�
	 * 
	 * @param fileName �ļ���
	 * @param billType ҵ������
	 * @param billItem ҵ���־
	 * @param in ����
	 * @param override�Ƿ񸲸�
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
	 * �����ļ�
	 * @param filepk �ļ�pk
	 * @param size �ļ���С
	 * @param in ������
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
	 * �����ļ�
	 * @param vo �ļ�vo
	 * @param in ������
	 * @throws Exception
	 */
	public void ReUpload(LfwFileVO vo, InputStream in) throws Exception{
		ILfwFileService fileService = NCLocator.getInstance().lookup(ILfwFileService.class);
		fileService.updataVos(new LfwFileVO[]{ vo});
		getFileSystem(vo).upload(vo, in);
	}
	/**
	 * �ļ�����
	 * @param fileName �ļ�����
	 * @param billType ��������	 * 
	 * @param billItem ����PK
	 * @param category �ļ�����
	 * @param oldFilePK Դ�ļ�PK
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
	 * ��ȡfileVO
	 * @param filePK
	 * @return
	 * @throws LfwBusinessException 
	 */
	public LfwFileVO getFileVO(String filePK) throws LfwBusinessException{
		return getFileQryService().getFile(filePK);
	}	
	/**
	 * �����ļ�����
	 * @param vo
	 * @return
	 * @throws LfwBusinessException
	 */
	public String insertFileVO(LfwFileVO vo) throws LfwBusinessException{
		ILfwFileService service = this.getFileService();
		return service.add(vo);
	}
	
	/**
	 * ɾ���ļ�
	 * 
	 * @param fileNo �ļ�����
	 * @throws Exception
	 */
	public void delete(String fileNo) throws Exception {
		LfwFileVO vo = getFileQryService().getFile(fileNo);
		getFileService().delete(vo);
		getFileSystem(vo).deleteFile(vo);
	}
	
	/**
	 * ����VOɾ���ļ���
	 * @param vo
	 * @throws LfwBusinessException
	 */
	public void delete(LfwFileVO vo) throws Exception{
		getFileService().delete(vo);
		getFileSystem(vo).deleteFile(vo);
	}

	/**
	 * �༭vo
	 * @param vo
	 * @throws LfwBusinessException
	 */
	public void updateVo(LfwFileVO vo) throws LfwBusinessException{
		getFileService().edit(vo);
	}
	/**
	 * ���¶��Vo 
	 * @param vos
	 * @throws LfwBusinessException
	 */
	void updataVos(LfwFileVO[] vos) throws LfwBusinessException{
		getFileService().updataVos(vos);
	}
	

	/**
	 * �����ļ�
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
	 * �ļ��Ƿ����
	 * @param fileNo
	 * @return
	 * @throws Exception
	 */
	public boolean exist(String fileNo) throws Exception{
		return true;
	}

	/**
	 * �ļ�ϵͳ���Ƿ���ڴ��ļ�
	 * @param fileNo
	 * @return
	 * @throws Exception
	 */
	public boolean existInFs(String fileNo) throws Exception{
		LfwFileVO vo = getFileQryService().getFile(fileNo);
		return getFileSystem(vo).existInFs(fileNo);
	}
	/**
	 * �޸��ϴ��ļ�״̬
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
	 * �����ļ���׺������ļ�����
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
	 * ���ݵ���pk�������еĸ����б�
	 * @param itempk
	 * @return
	 * @throws LfwBusinessException
	 */
	public LfwFileVO[] getAttachFileByItemID(String itempk)  throws LfwBusinessException{
		return getFileByItemID(itempk);
	}
	/**
	 * ���ݵ���PK����ȫ���ļ�
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
	 * ����ļ���ѯ����
	 * @return
	 */
	public ILfwFileQryService getFileQryService(){
		return NCLocator.getInstance().lookup(ILfwFileQryService.class);
	}
	
	
	/**
	 * ����ļ�����
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
		 * ��ǰctx������Ĭ�ϵ��ļ�������
		 */
		if(StringUtils.isEmpty(fileManagerClazz)){
			//Ĭ�ϲ����Ż���portalFileManager
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
