package nc.uap.lfw.ra.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import nc.bs.framework.codesync.util.IOUtils;
import nc.uap.lfw.core.AbstractPresentPlugin;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;
/**
 * 渲染引擎展现插件
 *
 */
public class RaPresentPlugin extends AbstractPresentPlugin {
	private boolean exist = false;
	private static final String JSP_PATH = "/html/nodes/jsp/ra.jsp";
	/**
	 * 资源查找类
	 * 
	 * @param pagePath
	 * @param templateJsp
	 * @return
	 * @throws IOException
	 */
	public boolean resLookUp(String pagePath, String templateJsp) throws IOException {
		// 开发态
		String folderPath = "/html/nodes/" + pagePath;
		if(templateJsp.endsWith(".ra")){
			templateJsp = templateJsp.substring(0,templateJsp.length()-2)+"um";
		}
		targetJsp = folderPath + templateJsp;
		String targetJspCopy = "/" + WEBTEMP + targetJsp;
		// 判断UM文件是否存在
		boolean exist = ContextResourceUtil.resourceExist(targetJsp.replace(".ra", ".um"));
		if (!exist) {
			String tempPath = getWebTempDir();
			fetchPublicFiles(pagePath, tempPath);
			exist = ContextResourceUtil.resourceExist(targetJspCopy);
			if (exist){
				roteType.put(cacheKey, true);
				LfwRuntimeEnvironment.setFromLfw(true);
			}
		}
 
		if(exist){
			Properties prop = loadNodePropertie((LfwRuntimeEnvironment.isFromLfw()?targetJspCopy:targetJsp).replace(templateJsp, MODEL_PROP));
			if (prop != null)
				model = prop.getProperty(MODEL);
			
//			exist = ContextResourceUtil.resourceExist((LfwRuntimeEnvironment.isFromLfw()?targetJspCopy:targetJsp).replace(".ra", ".um"));
		
		}
		return exist;
	}

	public void prepare(HttpServletRequest req, String pagePath,
			String themeId, String templateJsp) throws IOException {
		String mode = LfwRuntimeEnvironment.getMode();
		String lng = LfwRuntimeEnvironment.getLangCode();
		Boolean isDesign = mode.equals(WebConstant.MODE_DESIGN);
		exist = resCheck(pagePath, themeId, lng, templateJsp, isDesign);
		//LfwRuntimeEnvironment.setFromLfw(roteType.containsKey(cacheKey));
	}

	public boolean translate(HttpServletRequest req, String pagePath,
			String themeId, String templateJsp) throws IOException {
		boolean  raJspExist = ContextResourceUtil.resourceExist(JSP_PATH);
		if(!raJspExist){
			InputStream jspSourceStream = null;
			File jspFile = ContextResourceUtil.getFile(JSP_PATH);
			if(!jspFile.exists()){
				File jspFolder = jspFile.getParentFile();
				if(!jspFolder.exists())
					jspFolder.mkdir();
				
				jspFile.createNewFile();
			}
			OutputStream jspStream = new FileOutputStream(jspFile);
			try {
				jspSourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JSP_PATH.substring(1));
				IOUtils.copy(jspSourceStream, jspStream);
				jspStream.flush();
			} catch (Exception e) {
				LfwLogger.error(e.getMessage(), e);
			}finally{
				if(jspSourceStream != null){
					try {
						jspSourceStream.close();
					} catch (Exception e2) {
						LfwLogger.error(e2.getMessage(), e2);
					}
					jspSourceStream = null;
				}
				
				if(jspStream != null){
					try {
						jspStream.close();
					} catch (Exception e2) {
						LfwLogger.error(e2.getMessage(), e2);
					}
					jspStream = null;
				}
			}
		}
		this.targetJsp = JSP_PATH;
		return raJspExist;
	}
	
	/**
	 * 获取此功能点对应的页面控制类路径
	 * @return
	 */
	public String getModel(){
		return super.getModel();
	}
}
