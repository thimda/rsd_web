package nc.uap.lfw.core.uif.delegator;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.servlet.LfwServletBase;



/**
 * 实现打印模版生成的Excel文件下载的Servlet
 * 生成的excel文件放在web/tempexcel文件夹下
 * 
 */
public class PrintTemplateFileServlet extends LfwServletBase {

	private static final long serialVersionUID = 471873455845740514L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		//res.setCharacterEncoding("UTF-8");
		String filePath = req.getParameter("filePath");
		String realPath = null;
		if(true)
			realPath = RuntimeEnv.getInstance().getNCHome() + "/hotwebs" + req.getContextPath() + "/";
		else
			realPath = LfwRuntimeEnvironment.getRealPath();
		String fileAllPath = realPath + filePath;
		File file = new File(fileAllPath);
		
		FileInputStream finput = null;
		OutputStream out = null;
		try {
			finput = new FileInputStream(file);
			res.setContentType("APPLICATION/OCTET-STREAM");
			//res.setContentType("application/pdf"); 
			//res.setHeader("ETag", "W/\"518783-1222053336068\"");
//			res.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode("打印文件.pdf", "UTF-8") + "\"");
			//res.setContentLength((long)file.length());
			out = res.getOutputStream();
			
			byte[] content = new byte[1024*4];
			int count = -1;
			while((count = finput.read(content)) != -1)
				out.write(content, 0, count);
			out.flush();
		}
		finally {
			if(finput != null)
				finput.close();
		}
		if(file.exists())
			file.delete();
	}
}
