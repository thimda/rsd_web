package nc.uap.lfw.core;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import nc.uap.lfw.core.common.WebConstant;

public class JspPresentPlugin extends AbstractPresentPlugin {
	private boolean exist = false;;
	public void prepare(HttpServletRequest req, String pagePath, String themeId, String templateJsp) throws IOException {
		String mode = LfwRuntimeEnvironment.getMode();
		String lng = LfwRuntimeEnvironment.getLangCode();
		Boolean isDesign = mode.equals(WebConstant.MODE_DESIGN);
		exist = resCheck(pagePath, themeId, lng, templateJsp, isDesign);
	}

	public boolean resLookUp(String pagePath, String templateJsp) throws IOException {

		targetJsp = "/html/nodes/" + pagePath + templateJsp;
		boolean exist = ContextResourceUtil.resourceExist(targetJsp);

		// 从Lfw jar包中获取资源
		if (!exist) {
			String tempPath = getWebTempDir();
			targetJsp = "/" + WEBTEMP + "/html/nodes/" + pagePath + templateJsp;
			exist = ContextResourceUtil.resourceExist(targetJsp);

			// 路径中不存在，从公共服务中获取
			if (!exist) {
				fetchPublicFiles(pagePath, tempPath);
				exist = ContextResourceUtil.resourceExist(targetJsp);
			}
			
			if (exist) {
				roteType.put(cacheKey, true);
				LfwRuntimeEnvironment.setFromLfw(true);
			}
		}
		if (exist) {
			rote.put(pagePath, targetJsp);
			Properties prop = loadNodePropertie(targetJsp.replace(templateJsp, MODEL_PROP));
			if (prop != null)
				model = prop.getProperty(MODEL);
		}
		return exist;
	}

	public boolean translate(HttpServletRequest req, String pagePath,
			String themeId, String templateJsp) throws IOException {
		return exist;
	}

}
