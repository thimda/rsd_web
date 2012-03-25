package nc.uap.lfw.core.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.vo.ml.NCLangRes4VoTransl;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Lfw文件上传下载Servlet。此Servlet会调用每个页面指定的上传处理器进行实际文件的保存与插入。
 * 注意：此处已经确保了单点调用。所以上传处理器必定是在master上运行。
 * @author dengjt
 *
 */
public class LfwFileUploadServlet extends HttpServlet 
{     
	
	private static final long serialVersionUID = -5347929490268322875L;

	// 服务器上传文件夹路径
	public static final String SERVER_FILE_FOLDER = "d:\\uploadfiles\\";
	
//	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		if(!ServletFileUpload.isMultipartContent(req)) {
			throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "LfwFileUploadServlet-000000")/*请求不是一个multipart请求!*/);
		}
		try {
			Object result = doSaveFiles(req, res);
			if(result != null){
				LfwJsonSerializer serializer = LfwJsonSerializer.getInstance();
				String strResult = serializer.toJsObject(result);
				req.setAttribute("result", strResult);
				//TODO 是否返回成功页面??????????
//				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/html/nodes/uploadsuccess.jsp");
//				dispatcher.forward(req, res);
			}
		} 
		catch (Exception e) {
			LfwLogger.error(e);
		}
	}
	
	private Object doSaveFiles(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		res.setContentType(CONTENT_TYPE);
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置最多只允许在内存中存储的数据,单位:字节
			factory.setSizeThreshold(4096);
			// 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
			File tempPathFile = new File("c:\\temp");
			if (!tempPathFile.exists())
				tempPathFile.mkdirs();
			factory.setRepository(tempPathFile);
			ServletFileUpload upload = new ServletFileUpload(factory);
			//TODO 设置允许用户上传文件大小,单位:字节，暂时设为10m
			upload.setSizeMax(10 * 1024 * 1024);
			// 设置读取的编码格式
			upload.setHeaderEncoding("UTF-8");
			// 开始读取上传信息
			List fileItems = upload.parseRequest(req);
			// 依次处理每个上传的文件
			Iterator it = fileItems.iterator();

			// 正则匹配，过滤路径取文件名
			String regExp = ".+\\\\(.+)$";

			// 过滤掉的文件类型
			String[] errorType = { ".exe", ".com", ".cgi", ".asp" };
			Pattern p = Pattern.compile(regExp);
			
			// 从参数中获取处理类
			String fileUploadHandler = (String) req.getParameter("handler");
//			if(fileUploadHandler == null || "".equals(fileUploadHandler)){
//				fileUploadHandler = DefaultFileUploadHandler.class.getName();
//			}
//			IFileUploadHandler fileHandler = (IFileUploadHandler) LfwClassUtil.newInstance(fileUploadHandler);
			Map parameterMap = req.getParameterMap();
			List<File> fileList = new ArrayList<File>();

			// 创建服务器目录
			File folder = new File(SERVER_FILE_FOLDER);
			if (!folder.exists())
				folder.mkdirs();
			
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				// 忽略其他不是文件域的所有表单信息
				if (!item.isFormField()) {
					// 文件全路径
					String name = item.getName();
					// 文件名
					String fileName = name;
					
					long size = item.getSize();
					
					if ((name == null || name.equals("")) && size == 0)
						continue;
					
					Matcher m = p.matcher(name);
					boolean result = m.find();
					if (result) {  // name为文件全路径（IE）
						fileName = m.group(1);
					} else {  // name为文件名（Firefox）
//						throw new IOException("fail to upload");
					}
					
					// 校验文件类型
					for (int temp = 0; temp < errorType.length; temp++) {
						if (fileName.endsWith(errorType[temp])) {
							throw new IOException(name + ": wrong type");
						}
					}
					
					// 为文件名增加时间戳
					String time = getTime();
					fileName = fileName.substring(0, fileName.lastIndexOf(".") - 1) + "_" + time + fileName.substring(fileName.lastIndexOf("."));
					
					// 获取文件
					LfwLogger.debug("get file:" + name);
					// 将文件写入服务器指定目录
					File file = new File(SERVER_FILE_FOLDER + fileName);
					if (!file.exists())
						file.createNewFile();
					// 写入文件
					item.write(file);
					
					fileList.add(file);
					
				}
			}
//			try {
//				// 保存文件
//				return null;//fileHandler.saveFiles(fileList, parameterMap);
//			}
//			//TODO 异常包装
//			catch(LfwBusinessException e) {
//				LfwLogger.error(e);
//			}
		} catch (IOException e) {
			LfwLogger.error(e);
		} catch (SizeLimitExceededException e) {
			//TODO 文件大小超出限制的处理
			LfwLogger.error(e);
		} catch (FileUploadException e) {
			LfwLogger.error(e);
		}
		
		return null;
	}

	/**
	 * 获取时间戳
	 * @return
	 */
	private String getTime() {
		Calendar c = Calendar.getInstance();
		String y = String.valueOf(c.get(Calendar.YEAR));
		String m = getRealStringValue(c.get(Calendar.MONTH));
		String d = getRealStringValue(c.get(Calendar.DATE));
		String h = getRealStringValue(c.get(Calendar.HOUR));
		String mi = getRealStringValue(c.get(Calendar.MINUTE));
		String s = getRealStringValue(c.get(Calendar.SECOND));
		String ms = getRealStringValue(Calendar.MILLISECOND);
		String time = y + m + d + h + mi + s + ms;
		return time;
	}
	
	/**
	 * 获取数字的两位字符串
	 * @param value
	 * @return
	 */
	private String getRealStringValue(int value) {
		String realValue = String.valueOf(value);
		realValue = realValue.length() == 1 ? "0" + realValue : realValue;
		return realValue;
	}

}