package nc.uap.lfw.core.util;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import nc.bs.ml.NCLangResOnserver;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;

import org.apache.commons.io.FileUtils;
public class LangResoTranf {
	public static void tranf(Properties langResources, String nodeId) {
		String importJsFileName = "ml_resource.js";
		String basePath = "html/nodes/";
		String importJsFilePath = ContextResourceUtil.getCurrentAppPath()+ WebConstant.TEMP_FROM_LFW_PATH+"/" + basePath +nodeId+"/"+ importJsFileName;
		File file = new File(importJsFilePath);
			
		StringBuffer buffer = new StringBuffer("");
		buffer.append("function transs(key){"+"\r\n");
		buffer.append("  if(window.transsMap == null){\r\n");
		buffer.append("		window.transsMap = new Object;\r\n");
		String module = LfwRuntimeEnvironment.getRootPath().substring(1);
		for (Enumeration<Object> iter = langResources.keys(); iter.hasMoreElements();) {
			String key  = (String) iter.nextElement();
			String value = langResources.getProperty(key);
			String resID = NCLangResOnserver.getInstance().getString(module, value, key);
			buffer.append("		window.transsMap[\"" + key + "\"]=\"" + resID + "\";\r\n");
		}
		buffer.append("}\r\n");
		buffer.append("return window.transsMap[key];");
		buffer.append("\r\n}");
		try {
			FileUtils.writeStringToFile(file, buffer.toString(), "UTF-8");
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
}
