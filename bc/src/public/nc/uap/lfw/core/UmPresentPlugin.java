package nc.uap.lfw.core;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.parser.UIMetaConvertUtil;

public class UmPresentPlugin extends AbstractPresentPlugin {
	private boolean exist = false;
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
		targetJsp = folderPath + templateJsp;
		String targetJspCopy = "/" + WEBTEMP + targetJsp;
		// 判断UM文件是否存在
		boolean exist = ContextResourceUtil.resourceExist(targetJsp);
		if (!exist) {
			if (!exist) {
				String tempPath = getWebTempDir();
				fetchPublicFiles(pagePath, tempPath);
			}
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
		if(exist){
			PageMeta pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
			String ts = pm.getEtag();
			if(ts == null)
				ts = "unique";
			
			String targetJspCopy = "/" + WEBTEMP + targetJsp;
			targetJspCopy = targetJspCopy.replace("uimeta.um", "uimeta_" + ts + ".jsp");
			exist = ContextResourceUtil.resourceExist(targetJspCopy);
			if (!exist) {
				Writer writer = null;
				try {
					String folderPath = "/html/nodes/" + (String) LfwRuntimeEnvironment.getWebContext().getRequest().getAttribute(WebConstant.PERSONAL_PAGE_ID_KEY);
					
					String fullPath = ContextResourceUtil.getCurrentAppPath() + "/" + (LfwRuntimeEnvironment.isFromLfw() ? WEBTEMP : "") + folderPath;
					if (!LfwRuntimeEnvironment.isFromLfw()) {
						if (!ContextResourceUtil.resourceExist(targetJsp))
							return false;
					}
					UIMetaConvertUtil util = new UIMetaConvertUtil();
					util.setPagemeta(pm);
					String jsp = util.convert2Jsp(fullPath);
					File f = new File(ContextResourceUtil.getCurrentAppPath() + "/" + WEBTEMP + "/" + folderPath);
					if (!f.exists())
						f.mkdirs();
					writer = ContextResourceUtil.getOutputStream(targetJspCopy);
					writer.write(jsp);
					writer.flush();
				} catch (Exception e) {
					LfwLogger.error(e.getMessage(), e);
					throw new LfwParseException("解析UM文件出错," + e.getMessage());
				} finally {
					try {
						if (writer != null)
							writer.close();
					} catch (Exception e) {
					}
				}
				exist = ContextResourceUtil.resourceExist(targetJspCopy);
			}
			targetJsp = targetJspCopy;
		}
		return exist;
	}
}
