package nc.uap.lfw.core.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.IPageMetaBuilder;
import nc.uap.lfw.core.model.IWidgetBuilder;
import nc.uap.lfw.core.model.parser.PageMetaParser;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.WidgetConfig;
import nc.uap.lfw.core.util.PageNodeUtil;

import org.granite.hash.FNV164Hash;

/**
 * PageMeta默认构造器
 * @author dengjt
 * @since 6.0
 */
public class DefaultPageMetaBuilder implements IPageMetaBuilder {
	private static final String CONF_POSTFIX = "_$conf";
	private static String PAGEMETA_NAME = "pagemeta.pm";
	private static String BASE_PATH = "/html/nodes/";
	
	/**
	 * 从本地私有配置中获取PageMeta
	 * @param metaId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageMeta buildPageMeta(Map<String, Object> paramMap){
		String basePath = BASE_PATH;
		if(LfwRuntimeEnvironment.isFromLfw())
			basePath = WebConstant.TEMP_FROM_LFW_PATH + BASE_PATH;
		
		//获取PageID
		String metaId = (String)LfwRuntimeEnvironment.getWebContext().getRequest().getAttribute(WebConstant.PERSONAL_PAGE_ID_KEY);
		if(metaId == null)
			metaId = (String) paramMap.get(WebConstant.PAGE_ID_KEY);
		
		//不区分数据源的缓存
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		Map<String, PageMeta> pageMetaPool = (Map<String, PageMeta>) cache.get(WebConstant.PAGEMETA_POOL_KEY);
		
		if(pageMetaPool == null){
			pageMetaPool = new HashMap<String, PageMeta>();
			cache.put(WebConstant.PAGEMETA_POOL_KEY, pageMetaPool);
		}
		
		//获取对应的物理存储目录
		String pagePath = PageNodeUtil.getPageNodeDir(metaId);
		//将.转换
		if(pagePath == null)
			pagePath = metaId.replaceAll("\\.", "/");
		
		String folderPath = basePath + pagePath ;
		//获取缓存的PageMeta，并比较版本
		String filePath = folderPath + "/" + PAGEMETA_NAME;
		PageMeta pageMeta = (PageMeta) pageMetaPool.get(metaId + CONF_POSTFIX);
		
		//获取上次修改时间
		String lastModified = getLastModified(filePath);
		if(lastModified.equals("-1") || pageMeta == null || !lastModified.equals(pageMeta.getExtendAttributeValue(PageMeta.MODIFY_TS))){
			//获取最新的PageMeta
			pageMeta = fetchPageMeta(metaId, pageMetaPool, folderPath, lastModified);
		}
		else{
			//获取缓存的PageMeta的复制版
			pageMeta = (PageMeta) pageMeta.clone();
		}
		
		//处理片段最新版
		try{
			WidgetConfig[] widgetConfs = pageMeta.getWidgetConfs();
			for (int i = 0; i < widgetConfs.length; i++) {
				WidgetConfig widgetConfig = widgetConfs[i];
				//根据来源构建真实的片段
				LfwWidget widget = buildWidget(pageMeta, widgetConfig, metaId, paramMap);
				widget.setId(widgetConfig.getId());
				pageMeta.addWidget(widget);
			}
			//将处理完的片段放入池中
			pageMetaPool.put(metaId, pageMeta);
		}
		catch(Exception e){
			Logger.error(e.getMessage(), e);
			if(e instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e;
			throw new LfwRuntimeException(e.getMessage());
		}
		
		PageMeta pm = (PageMeta) pageMeta.clone();
		String[] wds = pm.getWidgetIds();
		if(wds != null && wds.length > 0){
			Arrays.sort(wds);
			String etag = "";
			for (int i = 0; i < wds.length; i++) {
				LfwWidget wd = pm.getWidget(wds[i]);
				String uniId = (String) wd.getExtendAttributeValue(LfwWidget.UNIQUE_ID);
				String uniTs = (String) wd.getExtendAttributeValue(LfwWidget.UNIQUE_TS);
				etag += uniId + uniTs;
			}
			
			String hashCode = "" + new FNV164Hash().hash(etag);
			pm.setEtag(hashCode);
		}
		return pm;
	}

	/**
	 * 获取页面描述类
	 * @param metaId
	 * @param pageMetaPool
	 * @param filePath
	 * @param lastModified
	 * @return
	 */
	private PageMeta fetchPageMeta(String metaId, Map<String, PageMeta> pageMetaPool, String folderPath, String lastModified) {
		InputStream input = null;
		PageMeta pageMetaConf = null;
		String filePath = folderPath + "/" + PAGEMETA_NAME;
		try{
			input = ContextResourceUtil.getResourceAsStream(filePath);
			if(input != null){
				pageMetaConf = initPageMeta(input, null);
				if(pageMetaConf == null)
					throw new LfwParseException("获取PageMeta时出错，pageid=" + metaId);
//				pageMetaConf.setExtendAttribute(PageMeta.FOLD_PATH, folderPath);
				pageMetaConf.setExtendAttribute(PageMeta.MODIFY_TS, lastModified);
				pageMetaConf.setFoldPath(folderPath);
				pageMetaPool.put(metaId + CONF_POSTFIX, (PageMeta) pageMetaConf.clone());
			}
			else
				throw new LfwParseException("从路径:" + filePath + ",没有找到PageMeta");
		}
		catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
		finally{
			try{
				if(input != null)
					input.close();
			}
			catch(Exception e){
				LfwLogger.error(e.getMessage(), e);
			}
		}
		return pageMetaConf;
	}
	
	/**
	 * 获取配置文件的最后修改时间，如果是设计态，则始终返回 -1
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
	
	public LfwWidget buildWidget(PageMeta pageMetaConf, WidgetConfig widgetConf, String metaId, Map<String, Object> paramMap) {
		return getWidgetBuilder(widgetConf).buildWidget(pageMetaConf, widgetConf, metaId, paramMap);
	}
	
	/**
	 * 获取片段构建器
	 * @param widgetConf
	 * @return
	 */
	protected IWidgetBuilder getWidgetBuilder(WidgetConfig widgetConf) {
		return new DefaultWidgetBuilder();
	}
	
	/**
	 * 从输入流里读取PageMeta
	 * @param input
	 * @param dirPath
	 * @return
	 */
	private PageMeta initPageMeta(InputStream input, String dirPath){
		try{
			PageMeta conf = PageMetaParser.parse(input);
			return conf;
		}
		catch(Exception e){
			Logger.error(e.getMessage(), e);
			throw new LfwParseException("解析页面时出现错误");
		}
		finally{
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
			}
		}
	}

}
