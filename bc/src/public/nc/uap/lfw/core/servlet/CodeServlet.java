package nc.uap.lfw.core.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.util.StringUtil;

/**
 * 动态生成初始化脚本下载servlet
 * 
 * @author dengjt
 * 
 */
public class CodeServlet extends LfwServletBase {

	private static final long serialVersionUID = 7714861659342364014L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
//		res.setCharacterEncoding("UTF-8");
//		String type = req.getParameter("type");
//		if(type.equals("requirelib")){
//			res.setContentType("text/plain");
//			String libid = req.getParameter("libid");
//			IControlPlugin plugin = ControlFramework.getInstance().getControlPluginByType(libid);
//			List<String> cssList = new ArrayList<String>();
//			List<String> jsList = new ArrayList<String>();
//			String[] cssDependences = plugin.calculateCssDependences();
//			if(cssDependences != null)
//				cssList.addAll(Arrays.asList(cssDependences));
//			String[] cssImports = plugin.getCssImports();
//			if(cssImports != null)
//				cssList.addAll(Arrays.asList(cssImports));
//			
//			String[] jsDependences = plugin.calculateDependences();
//			if(jsDependences != null)
//				jsList.addAll(Arrays.asList(jsDependences));
//			String[] jsImports = plugin.getImports();
//			if(jsImports != null)
//				jsList.addAll(Arrays.asList(jsImports));
//			
//			String result = "{jslib:" + StringUtil.mergeScriptArray(jsList) + ",csslib:" + StringUtil.mergeScriptArray(cssList) + "}";
//			res.getWriter().write(result);
//		}
//		String accept = req.getHeader(HttpHeaders.ACCEPT_ENCODING);
//		if(accept != null){
//			String[] types = accept.split(",");
//		}
//		boolean gzip = true;
//		if(gzip)
//			res.addHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
//		
//		String requestURI = req.getRequestURI();
//		if(requestURI.startsWith(LfwRuntimeEnvironment.getLfwCtx() + "/images")){
//			// 生产模式,压缩js代码
//			long lastModified = req.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
//			if(lastModified == -1L || lastModified != JsCodeUtil.getCacheTime().getMillis())
//			{
//				String themeId = (String) req.getSession().getAttribute("themeId");
//				String imagePath = requestURI.substring(LfwRuntimeEnvironment.getLfwCtx().length());
//				byte[] bytes = JsCodeUtil.getImage(themeId, imagePath);
//				if(bytes != null){
//					res.addDateHeader(HttpHeaders.LAST_MODIFIED, JsCodeUtil.getCacheTime().getMillis());
//					// 10小时内不过期
//					res.addHeader("Cache-Control", "max-age=36000");
//					res.getOutputStream().write(bytes);
//				}
//				else
//					res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			}
//			else
//				res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
////			getImage
////			JsCodeUtil.getCssCode(themeId, cssId)
//		}
//		else{
//			String pageId = requestURI.substring((req.getContextPath() + "/code").length() + 1);
//			// 请求js压缩代码
//			if(pageId.startsWith("js"))
//			{
//				res.setContentType("text/javascript");
//				String groupName = pageId.substring(2);
//				// 生产模式,压缩js代码
//				long lastModified = req.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
//				if(lastModified == -1L || lastModified != JsCodeUtil.getCacheTime().getMillis())
//				{
//					res.addDateHeader(HttpHeaders.LAST_MODIFIED, JsCodeUtil.getCacheTime().getMillis());
//					// 10小时内不过期
//					res.addHeader("Cache-Control", "max-age=36000");
//					if(gzip)
//						res.getOutputStream().write(JsCodeUtil.getCompressedJsCode(groupName));
//					else
//						res.getOutputStream().write(JsCodeUtil.getJsCode(groupName));
//				}
//				else
//					res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
//			}
//			else if(pageId.startsWith("css")){
//				res.setContentType("text/css"); 
//				String themeId = req.getParameter("themeId");
//				req.getSession().setAttribute("themeId", themeId);
//				String groupName = pageId.substring(3);
//				// 生产模式,压缩js代码
//				long lastModified = req.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
//				res.setContentType("text/css"); 
//				if(lastModified == -1L || lastModified != JsCodeUtil.getCacheTime().getMillis())
//				{
//					res.addDateHeader(HttpHeaders.LAST_MODIFIED, JsCodeUtil.getCacheTime().getMillis());
//					// 10小时内不过期
//					res.addHeader("Cache-Control", "max-age=36000");
//					if(gzip)
//						res.getOutputStream().write(JsCodeUtil.getCompressedCssCode(themeId, groupName));
//					else
//						res.getOutputStream().write(JsCodeUtil.getCssCode(themeId, groupName));
//				}
//				else
//					res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
//			}
//			// 刷新代码缓存,重新压缩js代码
//			else if(pageId.equals("refresh"))
//			{
//				JsCodeUtil.refresh();
//			}
//		}
	}
}
