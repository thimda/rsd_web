/**
 * 
 */
package nc.uap.lfw.design.impl;

import java.io.InputStream;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.IWidgetBuilder;
import nc.uap.lfw.core.model.parser.PageMetaParser;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.WidgetConfig;

/**
 * @author chouhl
 *
 */
public class WindowBuilderForDesign{

	private static String BASE_PATH = "/web/html/nodes/";
	private static String PAGEMETA_NAME = "pagemeta.pm";
	
	/**
	 * 仅解析PageMeta
	 * @param paramMap
	 * @return
	 */
	public PageMeta buildWindow(Map<String, Object> paramMap) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		String projectPath = (String) paramMap.get(WebConstant.PROJECT_PATH_KEY);
		String pageId = (String) paramMap.get(WebConstant.PAGE_ID_KEY);
		
		InputStream input = null;
		try{
			input = ContextResourceUtilForDesign.getResourceAsStream(projectPath + BASE_PATH + pageId + "/" + PAGEMETA_NAME);
			if(input != null){
				PageMeta window = initWindow(input);
				if(window == null){
					throw new LfwParseException("获取Window时出错,pageid=" + pageId);
				}
				return (PageMeta)window.clone();
			}
		}catch(Exception e){
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}finally{
			try{
				if(input != null){
					input.close();
				}
			}catch(Exception e){
				LfwLogger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	/**
	 * 解析PageMeta和widget
	 * @param paramMap
	 * @return
	 */
	public PageMeta buildWindowAndWidget(Map<String, Object> paramMap) 
	{
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		String projectPath = (String) paramMap.get(WebConstant.PROJECT_PATH_KEY);
		String pageId = (String) paramMap.get(WebConstant.PAGE_ID_KEY);
		
		InputStream input = null;
		try{
			input = ContextResourceUtilForDesign.getResourceAsStream(projectPath + BASE_PATH + pageId + "/" + PAGEMETA_NAME);
			if(input != null){
				PageMeta window = initWindow(input);
				if(window == null){
					throw new LfwParseException("获取Window时出错,pageid=" + pageId);
				}
				if(window.getId() == null)
					window.setId(pageId);
				if(window.getSourcePackage() == null)
					window.setSourcePackage("src/public");
				WidgetConfig[] configs = window.getWidgetConfs();
				for (int i = 0; i < configs.length; i++) {
					WidgetConfig widgetConf = configs[i];
					LfwWidget widget = null;
					String refId = widgetConf.getRefId();
					try{
						widget = buildWidget(window, widgetConf, pageId, paramMap);
						widget.setId(widgetConf.getId());
						if(widget.getSourcePackage() == null)
							widget.setSourcePackage("src/public/");
					}catch(Exception e){
						LfwLogger.error(e.getMessage(), e);
						widget = new LfwWidget();
						widget.setId(widgetConf.getId());
					}
					widgetConf.setRefId(refId);
					widget.setRefId(refId);
					window.addWidget(widget);
				}
				return (PageMeta)window.clone();
			}
		}catch(Exception e){
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}finally{
			try{
				if(input != null){
					input.close();
				}
			}catch(Exception e){
				LfwLogger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	/**
	 * 仅解析widget
	 * @param paramMap
	 * @param widgetId
	 * @return
	 */
	public LfwWidget buildWidget(Map<String, Object> paramMap, String widgetId)
	{
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		String projectPath = (String) paramMap.get(WebConstant.PROJECT_PATH_KEY);
		String pageId = (String) paramMap.get(WebConstant.PAGE_ID_KEY);
		
		InputStream input = null;
		try{
			input = ContextResourceUtilForDesign.getResourceAsStream(projectPath + BASE_PATH + pageId + "/" + PAGEMETA_NAME);
			if(input != null){
				PageMeta window = initWindow(input);
				if(window == null){
					throw new LfwParseException("获取Window时出错,pageid=" + pageId);
				}
//				Iterator<LfwWidgetConf> it = window.getWidgetConfList().iterator();
//				while(it.hasNext()){
//					LfwWidgetConf widgetConf = it.next();
//					if(widgetId != null && widgetConf.getId().equals(widgetId))
//					{	
//						LfwWidget widget = null;
//						try{
//							widget = buildWidget(window, widgetConf, pageId, paramMap);
//						}catch(Exception e){
//							LfwLogger.error(e.getMessage(), e);
//							widget = new LfwWidget();
//						}
//						widget.setId(widgetConf.getId());
//						return widget;
//					}	
//				}
			}
		}catch(Exception e){
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}finally{
			try{
				if(input != null){
					input.close();
				}
			}catch(Exception e){
				LfwLogger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	private PageMeta initWindow(InputStream input){
		return PageMetaParser.parse(input);
	}
	
	public LfwWidget buildWidget(PageMeta window, WidgetConfig widgetConf, String metaId, Map<String, Object> paramMap) {
		return getWidgetBuilder(widgetConf).buildWidget(window, widgetConf, metaId, paramMap);
	}
	
	protected IWidgetBuilder getWidgetBuilder(WidgetConfig widgetConf) {
		return new WidgetBuilderForDesign();
	}
	
}
