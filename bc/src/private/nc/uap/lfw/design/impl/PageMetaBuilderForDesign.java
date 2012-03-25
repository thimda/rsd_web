package nc.uap.lfw.design.impl;

import java.io.InputStream;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.IWidgetBuilder;
import nc.uap.lfw.core.model.parser.PageMetaParser;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.WidgetConfig;
import nc.uap.lfw.design.noexport.IPageMetaBuidlerForDesign;

/**
 * 为插件解析pageMeta提供的解析类
 * @author gd 2010-1-7
 * @version NC6.0
 * @since NC6.0
 */
public class PageMetaBuilderForDesign implements IPageMetaBuidlerForDesign
{	
	private static String BASE_PATH = "/web/html/nodes/";
	private static String PAGEMETA_NAME = "pagemeta.pm";
	/**
	 * 本方法仅解析pagemeta，不包含widget
	 * @param paramMap
	 * @return
	 */
	public PageMeta buildPageMeta(Map<String, Object> paramMap) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		
		String filePath = (String) paramMap.get("FILE_PATH");
		String pageId = (String) paramMap.get(WebConstant.PAGE_ID_KEY);
		if(filePath != null){
			filePath = filePath + "/" + PAGEMETA_NAME;
		}
		else{
			String projectPath = (String) paramMap.get(WebConstant.PROJECT_PATH_KEY);
			filePath = projectPath + BASE_PATH + pageId + "/" + PAGEMETA_NAME;
		}
		InputStream input = null;
		try{
			input = ContextResourceUtilForDesign.getResourceAsStream(filePath);
			if(input != null){
				PageMeta pageMetaConf = initPageMeta(input);
				if(pageMetaConf == null)
					throw new LfwParseException("获取PageMeta时出错,pageid=" + pageId);
				return (PageMeta) pageMetaConf.clone();
			}
		}
		catch(Exception e){
			Logger.error(e.getMessage(), e);
		}
		finally{
			try{
				if(input != null)
					input.close();
			}
			catch(Exception e){
				Logger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	/**
	 * 解析pagemeta和widget
	 * @param paramMap
	 * @return
	 */
	public PageMeta buildPageMetaAndWidget(Map<String, Object> paramMap) 
	{
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		
		String projectPath = (String) paramMap.get(WebConstant.PROJECT_PATH_KEY);
		String pageId = (String) paramMap.get(WebConstant.PAGE_ID_KEY);
		
		InputStream input = null;
		try{
			input = ContextResourceUtilForDesign.getResourceAsStream(projectPath + BASE_PATH + pageId + "/" + PAGEMETA_NAME);
			if(input != null){
				PageMeta pageMetaConf = initPageMeta(input);
				if(pageMetaConf == null)
					throw new LfwParseException("获取PageMeta时出错,pageid=" + pageId);
				WidgetConfig[] widgetConfigs = pageMetaConf.getWidgetConfs();
				for (int i = 0; i < widgetConfigs.length; i++) {
					WidgetConfig widgetConf = widgetConfigs[i];
					LfwWidget widget = null;
					String refId = widgetConf.getRefId();
					try{
						widget = buildWidget(pageMetaConf, widgetConf, pageId, paramMap);
						widget.setId(widgetConf.getId());
					}catch(Exception e){
						Logger.error(e.getMessage(), e);
						widget = new LfwWidget();
						widget.setId(widgetConf.getId());
					}
					widgetConf.setRefId(refId);
					widget.setRefId(refId);
					pageMetaConf.addWidget(widget);
				}
				pageMetaConf.setId(pageId);
				return (PageMeta) pageMetaConf.clone();
			}
		}
		catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		finally{
			try{
				if(input != null)
					input.close();
			}
			catch(Exception e){
				Logger.error(e.getMessage(), e);
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
				PageMeta pageMetaConf = initPageMeta(input);
				if(pageMetaConf == null)
					throw new LfwParseException("获取PageMeta时出错,pageid=" + pageId);
				WidgetConfig[] widgetConfs = pageMetaConf.getWidgetConfs();
				for (int i = 0; i < widgetConfs.length; i++) {
					WidgetConfig widgetConf = widgetConfs[i];
					if(widgetId != null && widgetConf.getId().equals(widgetId))
					{	
						LfwWidget widget = null;
						try{
							widget = buildWidget(pageMetaConf, widgetConf, pageId, paramMap);
						}
						catch(Exception e){
							Logger.error(e.getMessage(), e);
							widget = new LfwWidget();
						}
						widget.setId(widgetConf.getId());
						return widget;
					}	
				}
			}
		}
		catch(Exception e){
			Logger.error(e.getMessage(), e);
		}
		finally{
			try{
				if(input != null)
					input.close();
			}
			catch(Exception e){
				Logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	private PageMeta initPageMeta(InputStream input){
		PageMeta conf = PageMetaParser.parse(input);
		return conf;
	}
	
	public LfwWidget buildWidget(PageMeta pageMetaConf, WidgetConfig widgetConf, String metaId, Map<String, Object> paramMap) {
		return getWidgetBuilder(widgetConf).buildWidget(pageMetaConf, widgetConf, metaId, paramMap);
	}
	
	protected IWidgetBuilder getWidgetBuilder(WidgetConfig widgetConf) {
		return new WidgetBuilderForDesign();
	}
}
