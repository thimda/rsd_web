package nc.uap.lfw.core.model.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.AbstractPresentPlugin;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.PubDataset;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.IDatasetBuilder;
import nc.uap.lfw.core.model.IWidgetBuilder;
import nc.uap.lfw.core.model.IWidgetContentProvider;
import nc.uap.lfw.core.model.parser.WidgetParser;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.page.manager.PoolObjectManager;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.uimodel.WidgetConfig;
import nc.uap.lfw.core.util.PageNodeUtil;
import nc.uap.lfw.util.LfwClassUtil;

public class DefaultWidgetBuilder implements IWidgetBuilder {
	private static final String CONF_POSTFIX = "_$conf";
	private static final String WIDGET_PRE = "WIDGET_";
	
	/**
	 * 构建片段
	 */
	public LfwWidget buildWidget(PageMeta pm, WidgetConfig conf, String metaId, Map<String, Object> paramMap) {
		LfwWidget widget = parseWidget(pm, conf, metaId, paramMap);
		widget.setId(conf.getId());
		return widget;
	}
	
	/**
	 * 获取配置文件的路径
	 * @param widgetConfig
	 * @param projectPath
	 * @param metaId
	 * @return
	 */
	protected String getLfwWidgetConfPath(WidgetConfig widgetConfig, String metaId, Map<String, Object> paramMap)
	{
		String confPath = "/html/nodes/";
		if(LfwRuntimeEnvironment.isFromLfw())
			confPath = WebConstant.TEMP_FROM_LFW_PATH + confPath;
		
		String pagePath = PageNodeUtil.getPageNodeDir(metaId);
		if(pagePath == null)
			pagePath = metaId.replaceAll("\\.", "/");
		confPath += (pagePath + "/" + widgetConfig.getRefId().replaceAll("\\.", "/"));
		confPath += "/widget.wd";
		return confPath;
	}
	
	@SuppressWarnings("unchecked")
	protected LfwWidget parseWidget(PageMeta pm, WidgetConfig conf, String metaId, Map<String, Object> paramMap) {
		//不区分数据源的缓存
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		Map<String, LfwWidget> widgetPool = (Map<String, LfwWidget>) cache.get(WebConstant.WIDGET_POOL_KEY);
		
		//创建缓存池
		if(widgetPool == null){
			widgetPool = new HashMap<String, LfwWidget>();
			cache.put(WebConstant.WIDGET_POOL_KEY, widgetPool);
		}
		
		//获得配置缓存key
		String cacheId = metaId + "_$_" + conf.getId();
		
		LfwWidget widget = null;
		//处理来自公共池中片段
		if(conf.getRefId() != null && conf.getRefId().startsWith("..")){
			//如果片段来自公共池需要合并。
			String refWidgetID = conf.getRefId().substring(3);
			conf.setSourcePackage(conf.getRefId());
			LfwLogger.error("DefaultWidgetBuilder类中LFWwidget引用的refWidgetId= " + refWidgetID);
			widget = fetchWidgetFromPool(refWidgetID);
			widget.setRefId(conf.getRefId());
		}
		else
			//从配置文件中获取最新片段
			widget = parseConfigWidget(pm, conf, metaId, cacheId, widgetPool, paramMap);
		//从真正来源获取片段
		return parseSourceWidget(pm, paramMap, widgetPool, cacheId, widget, conf.getId());
	}

	/**
	 * 从真正的来源获取片段信息，比如NC模板
	 * @param pm
	 * @param paramMap
	 * @param widgetPool
	 * @param cacheId
	 * @param confWidget
	 * @return
	 */
	private LfwWidget parseSourceWidget(PageMeta pm, Map<String, Object> paramMap, Map<String, LfwWidget> widgetPool, String cacheId, LfwWidget confWidget, String currWidgetId) {
		try{
			LfwWidget resultWidget = null;
			IWidgetContentProvider builder = getWidgetContentProvider(confWidget, paramMap);
			if(builder != null){
				//获取真正来源构建器
				//构建真正的片段内容
				LfwWidget otherWidget = builder.buildWidget(pm, confWidget, paramMap, currWidgetId);
				if(!LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN)){
					//从缓存中获取上次已经融合的配置片段及模板片段
					LfwWidget widgetSaved = widgetPool.get(cacheId);
					//检查缓存的片段的最终TS与最终
					if(widgetSaved != null){
						//获取缓存文件最新时间
						String ts = (String) widgetSaved.getExtendAttributeValue(LfwWidget.UNIQUE_TS);
						//配置文件TS
						String confTs = (String) confWidget.getExtendAttributeValue(LfwWidget.MODIFY_TS);
						//动态提供内容TS
						String sourceTs = (String) otherWidget.getExtendAttributeValue(LfwWidget.MODIFY_TS);
						//获取二者最新时间
						String newestTs = getNewestTs(confTs, sourceTs);
						//比较缓存与最新时间是否一致
						if(compareTs(ts, newestTs)){
							String uniId = (String) otherWidget.getExtendAttributeValue(LfwWidget.UNIQUE_ID);
							String sUniId = (String) widgetSaved.getExtendAttributeValue(LfwWidget.UNIQUE_ID);
							//如果UNIQUE ID一致，则表示缓存文件最新，直接返回
							if(uniId != null && sUniId != null &&  uniId.equals(sUniId))
								return widgetSaved;
						}
						//设置获取内容时间戳为最新
						otherWidget.setExtendAttribute(LfwWidget.UNIQUE_TS, newestTs);
					}
				}
				
				//合并读取的本地内容与获取的内容
				LfwWidgetMergeUtil.merge(otherWidget, confWidget);
				resultWidget = otherWidget;
				//confWidget.setExtendAttribute(LfwWidget.MODIFY_TS, otherWidget.getExtendAttributeValue(LfwWidget.MODIFY_TS));
			}
			else
				resultWidget = confWidget;
			if(confWidget.getViewModels() != null){
				processViewModels(resultWidget, paramMap);
			}
			if(confWidget.getViewComponents() != null){
				processViewComponents(resultWidget, paramMap);
			}
			widgetPool.put(cacheId, resultWidget);
			return (LfwWidget) resultWidget.clone();
		}
		catch(Exception e){
			LfwLogger.error(e.getMessage(), e);
			if(e instanceof LfwRuntimeException)
				throw new LfwRuntimeException(e);
			throw new LfwParseException("解析Widget出现问题!" + e.getMessage(), e);
		}
	}

	/**
	 * 从公共池中获取片段
	 * @param confWidget
	 * @return
	 */
	public LfwWidget fetchWidgetFromPool(String refWidgetID) {
		LfwWidget resultWidget =  PoolObjectManager.getWidget(refWidgetID);
		return resultWidget;
	}

	private boolean compareTs(String ts, String newestTs) {
		if(ts == null)
			return false;
		return ts.compareTo(newestTs) == 0;
	}

	private String getNewestTs(String confTs, String sourceTs) {
		if(confTs == null)
			return sourceTs;
		if(sourceTs == null)
			return confTs;
		return confTs.compareTo(sourceTs) > 0 ? confTs : sourceTs;
	}
	
	
	/**
	 * 获取配置文件中的片段
	 * @param pm
	 * @param widgetConfig
	 * @param metaId
	 * @param cacheId
	 * @param widgetPool
	 * @param paramMap
	 * @return
	 * @throws LfwParseException
	 */
	private LfwWidget parseConfigWidget(PageMeta pm, WidgetConfig widgetConfig,
			String metaId, String cacheId, Map<String, LfwWidget> widgetPool, Map<String, Object> paramMap) throws LfwParseException{
		
		//从缓存中获取片段
		LfwWidget widget = (LfwWidget) widgetPool.get(cacheId + CONF_POSTFIX);
		
		//获取片段配置路径
		String configPath = getLfwWidgetConfPath(widgetConfig, metaId, paramMap);
		
		//获取上次修改时间
		String lastModified = getLastModified(configPath);
		
		//如果片段第一次初始化或者已经更新
		if(lastModified.equals("-1") || widget == null || !lastModified.equals(widget.getExtendAttributeValue(LfwWidget.MODIFY_TS))){
			widget = fetchWidget(widgetConfig, metaId, cacheId, widgetPool, configPath, lastModified);
			widgetPool.put(cacheId + CONF_POSTFIX, widget);
		}
		else
			widget = (LfwWidget) widget.clone();
		return widget;
	}

	/**
	 * 获取最新版本片段
	 * @param widgetConfig
	 * @param metaId
	 * @param cacheId
	 * @param widgetPool
	 * @param configPath
	 * @param lastModified
	 * @return
	 */
	private LfwWidget fetchWidget(WidgetConfig widgetConfig, String metaId,
			String cacheId, Map<String, LfwWidget> widgetPool, String configPath, String lastModified) {
		InputStream input = null;
		LfwWidget widget = null;
		try{
			input = getWidgetInput(configPath);
			if(input == null){
				if(configPath.contains("/web/html/nodes")){
					configPath = configPath.substring(0, configPath.indexOf("/web/html/nodes"));
					configPath = RuntimeEnv.getInstance().getNCHome() + "/hotwebs/" + configPath.substring(configPath.lastIndexOf("/") + 1);
					configPath = configPath + "/pagemeta/public/widgetpool/" + widgetConfig.getSourcePackage().substring(3) + "/" + "widget.wd";
				}else{
					configPath = "/pagemeta/public/widgetpool/" + widgetConfig.getSourcePackage().substring(3) + "/" + "widget.wd";
				}
				input = getWidgetInput(configPath);
			}
			if(input == null){
				throw new LfwParseException("没有找到Widget配置，metaId=" + metaId + ",widgetId=" + widgetConfig.getId());
			}
			widget = WidgetParser.parse(input);
			widget.setId(widgetConfig.getId());
			if(!lastModified.equals("-1")){
				widget.setExtendAttribute(LfwWidget.MODIFY_TS, lastModified);
				widget.setExtendAttribute(LfwWidget.UNIQUE_TS, lastModified);
			}
			
			int endIndex = configPath.length() - "/widget.wd".length();
			widget.setFoldPath(configPath.substring(0, endIndex));
			widgetPool.put(cacheId + CONF_POSTFIX, (LfwWidget) widget.clone());
		}
		catch(Exception e){
			LfwLogger.error(e.getMessage(), e);
			if(e instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e;
			throw new LfwParseException("解析Widget出现问题!" + e.getMessage(), e);
		}
		finally {
			try{
				if(input != null)
					input.close();
			}
			catch(Exception e){
				LfwLogger.error(e.getMessage(), e);
			}
		}
		return widget;
	}

	/**
	 * 获取配置文件的最后修改时间，如果是设计态，则始终返回 -1
	 * @param widget
	 * @param configPath
	 * @return
	 */
	protected String getLastModified(String configPath) {
		String lastModified = null;
		if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN))
			lastModified = "-1";
		else
			lastModified = ContextResourceUtil.getFile(configPath).lastModified() + "";
		
		return lastModified;
	}
	
	private void processViewComponents(LfwWidget widget, Map<String, Object> paramMap) {
	}

	/**
	 * 获取其它片段构建器
	 * @param refId
	 * @return
	 */
	private IWidgetContentProvider getWidgetContentProvider(LfwWidget confWidget, Map<String, Object> paramMap) {
		String providerClass = confWidget.getProvider();
		String from = confWidget.getFrom();
		IWidgetContentProvider provider = null;
		if(providerClass != null && !providerClass.equals("")){
			provider = (IWidgetContentProvider) LfwClassUtil.newInstance(providerClass);
		}
		else if(from != null && !from.equals("")){
		
			String clazz = null;
			String id = WIDGET_PRE + from;
			if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN))
				clazz = LfwRuntimeEnvironment.getServerConfig().get(id);
			else{
				//取本页面的加载类
				String pageId = (String) paramMap.get(WebConstant.PAGE_ID_KEY);
				String pagePath = PageNodeUtil.getPageNodeDir(pageId);
				String nodePath = "html/nodes/" + pagePath + "/node.properties";
				Properties props = AbstractPresentPlugin.loadNodePropertie(nodePath);
				if(props != null){
					clazz = props.getProperty(id);
				}
				if(clazz == null)
					clazz = LfwRuntimeEnvironment.getModelServerConfig().getConfigValue(id);
			}
			if(clazz == null)
				clazz = LfwRuntimeEnvironment.getServerConfig().get(id);
			if(clazz == null || clazz.equals(""))
				throw new LfwParseException("没有找到对应的配置，Widget refid:" + from);
			provider = (IWidgetContentProvider) LfwClassUtil.newInstance(clazz);
		}
		return provider;
	}

	/**
	 * 获取widget文件的输入流
	 * @param confPath
	 * @return
	 */
	protected InputStream getWidgetInput(String confPath){
		return ContextResourceUtil.getResourceAsStream(confPath);
	}
	
	private void processViewModels(LfwWidget widget, Map<String, Object> paramMap) {
		ViewModels viewModel = widget.getViewModels();
		Dataset[] dss = viewModel.getDatasets();
		Map<String, Dataset> refDsMap = new HashMap<String, Dataset>();
		for (int i = 0; i < dss.length; i++) {
			Dataset ds = dss[i];
			
			// 引用来自公共池的ds
			if(ds instanceof PubDataset)
			{
				PubDataset pubDs = (PubDataset)ds;
				PubDataset genDs = null;
				if(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN)){
					//设计态，解除依赖
					IDatasetBuilder builder = (IDatasetBuilder) LfwClassUtil.newInstance("nc.uap.lfw.design.impl.RefDatasetBuilderForDesign");
					genDs = (PubDataset) builder.buildDataset(pubDs, paramMap);
				}
				else
					genDs = (PubDataset) new DefaultRefDatasetBuilder().buildDataset(pubDs, null);
				
				if(genDs != null)
					refDsMap.put(genDs.getId(), genDs);
			}
			else if(ds instanceof MdDataset)
			{
				//new DefaultMdDatasetBuilder().buildDataset(ds, null);
				refDsMap.put(ds.getId(), ds);
			}
			else{
				Dataset conf = ds;
				Dataset genDs = null;
				if(conf.getFrom() != null && !conf.getFrom().equals("")){
					//IDatasetBuilder builder = getOtherDatasetBuidler(conf.getFrom());
				}
				else
					genDs = conf;
				
				if(genDs != null)
					refDsMap.put(genDs.getId(), genDs);
			}
		}
		
		//非设计态
		if(!LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN)){
			//如果是离线模式，强制参照为使用客户端数据
			if(LfwRuntimeEnvironment.isClientMode()){
				RefNode[] rfs = (RefNode[]) widget.getViewModels().getRefNodes();
				for (int i = 0; i < rfs.length; i++) {
					rfs[i].setClientCache(true);
				}
			}
		}
		viewModel.addDatasets(refDsMap);	
	}


	/**
	 * 获取引用的公共ds
	 * @param dsId
	 * @return
	 */
	protected Dataset getRefDataset(String dsId, String projectPath)
	{
		return PoolObjectManager.getDataset(dsId);
	}


}
