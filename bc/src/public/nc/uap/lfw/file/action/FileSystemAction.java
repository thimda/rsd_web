package nc.uap.lfw.file.action;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ModelServerConfig;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.file.ContentTypeConst;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.file.IFileUploadExtender;
import nc.uap.lfw.file.itf.ILfwFileQryService;
import nc.uap.lfw.file.pack.ZipEntry;
import nc.uap.lfw.file.pack.ZipOutputStream;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.After;
import nc.uap.lfw.servletplus.annotation.Before;
import nc.uap.lfw.servletplus.annotation.Param;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.constant.Keys;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
/**
 * 文件上传Action
 * 
 * @author licza
 * 
 */
@Servlet(path = "/file")
public class FileSystemAction extends BaseAction {
	
	/** 缓冲区大小 **/
//	private static final int BUFFER = 1024 * 256;
	private FileManager fileManager = null;
	private final String SPLITER = "||";
	private final String FILEMANAGER = "filemanager";
	private final String BILLTYPE = "billtype";
	public static final String BILLITEM = "billitem";
	private final String ISCOVER = "iscover";
	private final String ExtendClass = "extendclass"; 
	private final String FILEPK = "filepk";
	/**
	 * 用来包装ios文件上传后回调处理
	 */
	@Before
	public void before(){
		String fileManagerClazz = request.getParameter(FILEMANAGER);
		if (StringUtils.isNotBlank(fileManagerClazz))
			setFileManager(fileManagerClazz);
	}
	/**
	 * 用来包装ios文件上传后回调处理
	 */
	@After
	public void after(){
	}
	
	/**
	 * 获得文件管理
	 * 
	 * @return
	 */
	public FileManager getFileManager() {
		if (fileManager == null) {
			fileManager = FileManager.getSystemFileManager();
		}
		return fileManager;
	}
	
	/**
	 * 设置文件管理类
	 * @param fileManagerClazz
	 */
	public void setFileManager(String fileManagerClazz) {
		try {
			fileManager = (FileManager) Thread.currentThread().getContextClassLoader().loadClass(fileManagerClazz).newInstance();
		} catch (InstantiationException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("不能为FileManager类" + fileManagerClazz + "创建实例", e);
		} catch (ClassNotFoundException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("FileManager类" + fileManagerClazz + "不存在", e);
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e);
		}
	}
	private static MultipartResolver multipartResolver = null;
	/**
	 * 获得MultipartHttpServletRequest
	 * 
	 * @return
	 * @throws MultipartException
	 */
	private static MultipartHttpServletRequest getMultipartResolver(HttpServletRequest request) throws MultipartException {
		if (multipartResolver == null) {
			multipartResolver = new CommonsMultipartResolver();
			((CommonsMultipartResolver) multipartResolver).setDefaultEncoding("UTF-8");
		}
		return multipartResolver.resolveMultipart(request);
	}
	/**
	 * 在业务方法进行前设置FileManager
	 */
	@Before
	public void setFileManager(){
		String fileManagerClazz = request.getParameter(FILEMANAGER);
		if (StringUtils.isNotBlank(fileManagerClazz))
			setFileManager(fileManagerClazz);
	}
	
	/**
	 * 上传文件
	 */
	@Action(method = Keys.POST)
	public void upload() {
		InputStream input = null;
		try {
			MultipartHttpServletRequest req = getMultipartResolver(request);
			Map<String, MultipartFile> fileMap = req.getFileMap();
			List<MultipartFile> files = new ArrayList<MultipartFile>();
			if (MapUtils.isNotEmpty(fileMap))
				files.addAll(fileMap.values());
			else
				throw new NullPointerException("上传文件为空");
			String billType = req.getParameter(BILLTYPE);
			String billItem = req.getParameter(BILLITEM);
			
			String fileManagerClazz = req.getParameter(FILEMANAGER);
			String iscover = req.getParameter(ISCOVER);
			String filepk = req.getParameter(FILEPK);
			LfwFileVO filevo = null;
			if(filepk != null){
				filevo =  getFileManager().getFileVO(filepk);				
			}
			
			boolean override = iscover != null && Boolean.TRUE.toString().equals(iscover);
			String pk_user = "";
			String userName = "";
			LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
			if(ses != null){
				pk_user = ses.getPk_user();
				userName = ses.getUser_name();
			}

			if (StringUtils.isNotBlank(fileManagerClazz))
				setFileManager(fileManagerClazz);
			MultipartFile file = (MultipartFile) files.get(0);
			String fileName = file.getOriginalFilename();
			input = file.getInputStream();
			
			String pk = "";
			if(filevo != null){
				pk = filepk;
				filevo.setFilesize(file.getSize());
				getFileManager().ReUpload(filevo, input);
			}
			else
				pk = getFileManager().upload(fileName, billType, billItem, file.getSize(), input, override);
			
			IFileUploadExtender extender = getUploadExtender(req);
			if(extender != null){
				try{
					extender.extend(req, pk);
				}
				catch(LfwRuntimeException ex){
					getFileManager().delete(pk);
					LfwLogger.error("执行上传文件扩展类失败。");
					LfwLogger.error(ex);
					throw ex;
				}
			}
			String [] rtn = new String[]{pk,
											fileName,
											Long.valueOf(file.getSize()).toString(),
											getFileManager().getFileType(fileName),
											new UFDateTime().toString(),
											pk_user,
											userName
										};
			print(StringUtils.join(rtn,SPLITER));
			
			if(extender != null){
				String[] extrtn = extender.getRetValues();
				if(null != extrtn && extrtn.length > 0){
					print(SPLITER);
					print(StringUtils.join(extrtn, SPLITER));
				}
			}
		} catch (LfwRuntimeException ex) {
			print(ex.getMessage());
		} catch (Exception e) {
			LfwLogger.error("文件上传失败", e);
			print("文件上传失败:");
			print(e.getMessage());
		} finally {
			IOUtils.closeQuietly(input);
		}
	}	
	private IFileUploadExtender getUploadExtender(MultipartHttpServletRequest req){
		IFileUploadExtender extender = null;
		String extendclass = req.getParameter(ExtendClass);
		if(extendclass != null && !extendclass.equals("")){
			try {
				Object obj =  Class.forName(extendclass).newInstance();
				extender = (IFileUploadExtender)obj;
			} catch (InstantiationException e) {
				LfwLogger.error(e);
			} catch (IllegalAccessException e) {
				LfwLogger.error(e);
			} catch (ClassNotFoundException e) {
				LfwLogger.error(e);
			}
		}
		return extender;
	}
	/**
	 * 通过正则表达式判断pk是否为空 
	 */
	static Pattern pkPattern = Pattern.compile("^\\w{20}$");
	/**
	 * 上传公文
	 */
	@Action(method = Keys.POST)
	public void doc() {
		InputStream input = null;
		try {
			LfwSessionBean ses = LfwRuntimeEnvironment.getLfwSessionBean();
			if (ses == null)
				throw new LfwRuntimeException("会话超时!请重新登陆!");
			MultipartHttpServletRequest req = getMultipartResolver(request);
			Map<String, MultipartFile> fileMap = req.getFileMap();
			List<MultipartFile> files = new ArrayList<MultipartFile>();
			if (MapUtils.isNotEmpty(fileMap))
				files.addAll(fileMap.values());
			else
				throw new NullPointerException("上传文件为空");
			MultipartFile file = (MultipartFile) files.get(0);
			String pk = file.getName();
			if(!pkPattern.matcher(pk).matches())
				pk = null;
//			if (pk.length() != 20)
//				pk = null;
			input = file.getInputStream();
			if (StringUtils.isNotBlank(pk)) {
				FileManager filemanager =getFileManager(); 
				LfwFileVO fileVO = filemanager.getFileQryService().getFile(pk);
				if (fileVO.getCreator() == null)
					fileVO.setCreator(ses.getPk_user());
				if (fileVO.getCreattime() == null)
					fileVO.setCreattime(new UFDateTime());
				fileVO.setLastmodifyer(ses.getPk_user());
				fileVO.setLastmodifytime(new UFDateTime());
				fileVO.setFilesize(Long.valueOf(file.getSize()));
				filemanager.getFileSystem(fileVO).upload(fileVO, input);
				filemanager.updateVo(fileVO);
			}
			print(pk);
		} catch (Exception e) {
			print(e.getMessage());
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
	/**
	 * 根据业务下载文件
	 * 
	 * @param id 业务主键
	 * @throws Exception
	 * @throws IOException
	 */
	@Action(url = "/down/bill")
	public void bill(@Param(name = "id") String id) throws IOException {
		if(id == null || id.length() <= 0)
			return ;
		try {
			ILfwFileQryService fileQry = getFileManager().getFileQryService();
			LfwFileVO[] fvo = fileQry.getFile(null, id);
			if (!ArrayUtils.isEmpty(fvo)) {
				String pk_file = fvo[0].getPk_lfwfile();
				down(pk_file);
			} else {
				throw new FileNotFoundException("请求的文件不存在");
			}
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
			throw new LfwRuntimeException("操作异常:" + e.getMessage());
		}
	}
	/**
	 * 下载文件
	 * 
	 * @param id 文件主键
	 * @throws IOException
	 */
	@Action
	public void down(@Param(name = "id") String id) throws IOException {
		if(id == null || id.length() <= 0)
			return ;
		OutputStream out = null;
		try {
			ILfwFileQryService fileQry = getFileManager().getFileQryService();
			LfwFileVO fvo = fileQry.getFile(id);
			
			if(fvo.getFiletypo() == null){
				String fileType = getFileManager().getFileType(fvo.getFilename());
				fvo.setFiletypo(fileType);
			}
			if(request.getParameter("prv") == null){
				String modifiedSince = request.getHeader("If-Modified-Since");
				if(modifiedSince != null){
					if(modifiedSince.equals(fvo.getLastmodifytime().toString())){
						response.setStatus(304);
						return;
					}
				}
			}
			if(fvo != null){
				addFileName(fvo,false);
			}
			out = response.getOutputStream();
			UFDateTime lastModify =  fvo.getLastmodifytime();
			if(lastModify == null)
				lastModify = new UFDateTime();
			response.setHeader("Last-Modified", lastModify.toString());
			getFileManager().download(id, out);
			response.flushBuffer();
		} catch (Exception e) {
			throw new LfwRuntimeException("文件下载失败", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	@Action
	public void view(@Param(name = "id") String id) throws IOException {
		if(id == null || id.length() <= 0)
			return ;
		OutputStream out = null;
		try {
			ILfwFileQryService fileQry = getFileManager().getFileQryService();
			LfwFileVO fvo = fileQry.getFile(id);
			addFileName(fvo,true);
			String contentType = ContentTypeConst.getContentType(fvo.getFiletypo());
			response.setContentType(contentType);
			out = response.getOutputStream();
			getFileManager().download(id, out);
			out.flush();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (Exception e) {
			throw new LfwRuntimeException("文件下载失败", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	
	
	/**
	 * 压缩模式下载一组文件
	 * @param pks
	 * @throws IOException
	 */
	@Action(url="/down/pack.zip")
	public void pack(@Param(name="pks") String pks)throws IOException{
		if(pks == null || pks.length() <= 0)
			return;
		
		ZipOutputStream zo = new ZipOutputStream(response.getOutputStream());
		zo.setEncoding("gbk");
		ILfwFileQryService fileQry = getFileManager().getFileQryService();
		String[] pkArr = pks.split(",");
		try {
			if(pkArr.length > 0){
				ZipEntry entry = null;
				for(String pk : pkArr){
					/**
					 * 查出文件名及文件的时间信息
					 */
					LfwFileVO  vo = fileQry.getFile(pk);
					entry = new ZipEntry(vo.getFilename());
					entry.setTime(vo.getLastmodifytime().getMillis());
					zo.putNextEntry(entry);
					/**
					 * 下载文件到ZIP输出流
					 */
					getFileManager().download(pk, zo);
				}
			}
			/**
			 * 冲到流中
			 */
			zo.flush();
		} catch (Exception e) {
			LfwLogger.error(e.getMessage(),e);
		}finally{
			/**
			 * 安全关闭输出流
			 */
			IOUtils.closeQuietly(zo);
		}
	}
	
	/**
	 * 加入文件名
	 * <pre>
	 * 如果字符数超过最大限制,取最后15个字符
	 * </pre>
	 * @param fvo
	 * @throws UnsupportedEncodingException
	 */
	public void addFileName(LfwFileVO fvo,boolean isView) throws UnsupportedEncodingException {
		if (StringUtils.isBlank(request.getHeader("Content-Disposition"))) {
			String contentType = ContentTypeConst.getContentType(fvo.getFiletypo());
			if(StringUtils.isEmpty(contentType))
				return;
			String userAgent = request.getHeader("user-agent");
			boolean isIe = userAgent != null && userAgent.length() > 0 && userAgent.indexOf("MSIE") != -1;
			String fileName = null;
			if(isIe){
				fileName = URLEncoder.encode(fvo.getFilename(), "UTF-8");
				if (fileName.length() > 150) {
					fileName = URLEncoder.encode(fvo.getFilename().substring(fvo.getFilename().length() - 15) + "...", "UTF-8");
				}
			}else{
				fileName = MimeUtility.encodeText(fvo.getFilename(), "utf-8", "B");
			}
			response.setContentType(contentType);
			response.addHeader("Content-Disposition", (isView ? contentType:"attachment") + ";filename=" + fileName);
		}
	}
}
