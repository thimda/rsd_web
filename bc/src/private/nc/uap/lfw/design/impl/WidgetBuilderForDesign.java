package nc.uap.lfw.design.impl;

import java.io.InputStream;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.model.util.DefaultWidgetBuilder;
import nc.uap.lfw.core.uimodel.WidgetConfig;

/**
 * 为插件设计的widget解析器
 * @author gd 2010-1-7
 * @version NC6.0
 * @since NC6.0
 */
public class WidgetBuilderForDesign extends DefaultWidgetBuilder {	
	protected InputStream getWidgetInput(String confPath){
		return ContextResourceUtilForDesign.getResourceAsStream(confPath);
	}
	
	protected String getLfwWidgetConfPath(WidgetConfig conf, String metaId, Map<String, Object> paramMap){
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		String projectPath = (String) paramMap.get(WebConstant.PROJECT_PATH_KEY);
		String confPath = projectPath + "/web/html/nodes/";
		if(conf.getRefId().startsWith("..")){
			String path = projectPath + "/web/pagemeta/public/widgetpool/" + conf.getRefId().substring(3);
			confPath = path;
		}
		else
			confPath += (metaId + "/" + conf.getRefId().replaceAll("\\.", "/"));
		
		confPath += "/widget.wd";
		return confPath;
	}
}
