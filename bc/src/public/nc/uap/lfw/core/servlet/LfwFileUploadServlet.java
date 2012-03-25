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
 * Lfw�ļ��ϴ�����Servlet����Servlet�����ÿ��ҳ��ָ�����ϴ�����������ʵ���ļ��ı�������롣
 * ע�⣺�˴��Ѿ�ȷ���˵�����á������ϴ��������ض�����master�����С�
 * @author dengjt
 *
 */
public class LfwFileUploadServlet extends HttpServlet 
{     
	
	private static final long serialVersionUID = -5347929490268322875L;

	// �������ϴ��ļ���·��
	public static final String SERVER_FILE_FOLDER = "d:\\uploadfiles\\";
	
//	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		if(!ServletFileUpload.isMultipartContent(req)) {
			throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "LfwFileUploadServlet-000000")/*������һ��multipart����!*/);
		}
		try {
			Object result = doSaveFiles(req, res);
			if(result != null){
				LfwJsonSerializer serializer = LfwJsonSerializer.getInstance();
				String strResult = serializer.toJsObject(result);
				req.setAttribute("result", strResult);
				//TODO �Ƿ񷵻سɹ�ҳ��??????????
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
			// �������ֻ�������ڴ��д洢������,��λ:�ֽ�
			factory.setSizeThreshold(4096);
			// ����һ���ļ���С����getSizeThreshold()��ֵʱ���ݴ����Ӳ�̵�Ŀ¼
			File tempPathFile = new File("c:\\temp");
			if (!tempPathFile.exists())
				tempPathFile.mkdirs();
			factory.setRepository(tempPathFile);
			ServletFileUpload upload = new ServletFileUpload(factory);
			//TODO ���������û��ϴ��ļ���С,��λ:�ֽڣ���ʱ��Ϊ10m
			upload.setSizeMax(10 * 1024 * 1024);
			// ���ö�ȡ�ı����ʽ
			upload.setHeaderEncoding("UTF-8");
			// ��ʼ��ȡ�ϴ���Ϣ
			List fileItems = upload.parseRequest(req);
			// ���δ���ÿ���ϴ����ļ�
			Iterator it = fileItems.iterator();

			// ����ƥ�䣬����·��ȡ�ļ���
			String regExp = ".+\\\\(.+)$";

			// ���˵����ļ�����
			String[] errorType = { ".exe", ".com", ".cgi", ".asp" };
			Pattern p = Pattern.compile(regExp);
			
			// �Ӳ����л�ȡ������
			String fileUploadHandler = (String) req.getParameter("handler");
//			if(fileUploadHandler == null || "".equals(fileUploadHandler)){
//				fileUploadHandler = DefaultFileUploadHandler.class.getName();
//			}
//			IFileUploadHandler fileHandler = (IFileUploadHandler) LfwClassUtil.newInstance(fileUploadHandler);
			Map parameterMap = req.getParameterMap();
			List<File> fileList = new ArrayList<File>();

			// ����������Ŀ¼
			File folder = new File(SERVER_FILE_FOLDER);
			if (!folder.exists())
				folder.mkdirs();
			
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				// �������������ļ�������б���Ϣ
				if (!item.isFormField()) {
					// �ļ�ȫ·��
					String name = item.getName();
					// �ļ���
					String fileName = name;
					
					long size = item.getSize();
					
					if ((name == null || name.equals("")) && size == 0)
						continue;
					
					Matcher m = p.matcher(name);
					boolean result = m.find();
					if (result) {  // nameΪ�ļ�ȫ·����IE��
						fileName = m.group(1);
					} else {  // nameΪ�ļ�����Firefox��
//						throw new IOException("fail to upload");
					}
					
					// У���ļ�����
					for (int temp = 0; temp < errorType.length; temp++) {
						if (fileName.endsWith(errorType[temp])) {
							throw new IOException(name + ": wrong type");
						}
					}
					
					// Ϊ�ļ�������ʱ���
					String time = getTime();
					fileName = fileName.substring(0, fileName.lastIndexOf(".") - 1) + "_" + time + fileName.substring(fileName.lastIndexOf("."));
					
					// ��ȡ�ļ�
					LfwLogger.debug("get file:" + name);
					// ���ļ�д�������ָ��Ŀ¼
					File file = new File(SERVER_FILE_FOLDER + fileName);
					if (!file.exists())
						file.createNewFile();
					// д���ļ�
					item.write(file);
					
					fileList.add(file);
					
				}
			}
//			try {
//				// �����ļ�
//				return null;//fileHandler.saveFiles(fileList, parameterMap);
//			}
//			//TODO �쳣��װ
//			catch(LfwBusinessException e) {
//				LfwLogger.error(e);
//			}
		} catch (IOException e) {
			LfwLogger.error(e);
		} catch (SizeLimitExceededException e) {
			//TODO �ļ���С�������ƵĴ���
			LfwLogger.error(e);
		} catch (FileUploadException e) {
			LfwLogger.error(e);
		}
		
		return null;
	}

	/**
	 * ��ȡʱ���
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
	 * ��ȡ���ֵ���λ�ַ���
	 * @param value
	 * @return
	 */
	private String getRealStringValue(int value) {
		String realValue = String.valueOf(value);
		realValue = realValue.length() == 1 ? "0" + realValue : realValue;
		return realValue;
	}

}