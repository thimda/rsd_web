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
 * PageMetaĬ�Ϲ�����
 * @author dengjt
 * @since 6.0
 */
public class DefaultPageMetaBuilder implements IPageMetaBuilder {
	private static final String CONF_POSTFIX = "_$conf";
	private static String PAGEMETA_NAME = "pagemeta.pm";
	private static String BASE_PATH = "/html/nodes/";
	
	/**
	 * �ӱ���˽�������л�ȡPageMeta
	 * @param metaId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageMeta buildPageMeta(Map<String, Object> paramMap){
		String basePath = BASE_PATH;
		if(LfwRuntimeEnvironment.isFromLfw())
			basePath = WebConstant.TEMP_FROM_LFW_PATH + BASE_PATH;
		
		//��ȡPageID
		String metaId = (String)LfwRuntimeEnvironment.getWebContext().getRequest().getAttribute(WebConstant.PERSONAL_PAGE_ID_KEY);
		if(metaId == null)
			metaId = (String) paramMap.get(WebConstant.PAGE_ID_KEY);
		
		//����������Դ�Ļ���
		ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
		Map<String, PageMeta> pageMetaPool = (Map<String, PageMeta>) cache.get(WebConstant.PAGEMETA_POOL_KEY);
		
		if(pageMetaPool == null){
			pageMetaPool = new HashMap<String, PageMeta>();
			cache.put(WebConstant.PAGEMETA_POOL_KEY, pageMetaPool);
		}
		
		//��ȡ��Ӧ������洢Ŀ¼
		String pagePath = PageNodeUtil.getPageNodeDir(metaId);
		//��.ת��
		if(pagePath == null)
			pagePath = metaId.replaceAll("\\.", "/");
		
		String folderPath = basePath + pagePath ;
		//��ȡ�����PageMeta�����Ƚϰ汾
		String filePath = folderPath + "/" + PAGEMETA_NAME;
		PageMeta pageMeta = (PageMeta) pageMetaPool.get(metaId + CONF_POSTFIX);
		
		//��ȡ�ϴ��޸�ʱ��
		String lastModified = getLastModified(filePath);
		if(lastModified.equals("-1") || pageMeta == null || !lastModified.equals(pageMeta.getExtendAttributeValue(PageMeta.MODIFY_TS))){
			//��ȡ���µ�PageMeta
			pageMeta = fetchPageMeta(metaId, pageMetaPool, folderPath, lastModified);
		}
		else{
			//��ȡ�����PageMeta�ĸ��ư�
			pageMeta = (PageMeta) pageMeta.clone();
		}
		
		//����Ƭ�����°�
		try{
			WidgetConfig[] widgetConfs = pageMeta.getWidgetConfs();
			for (int i = 0; i < widgetConfs.length; i++) {
				WidgetConfig widgetConfig = widgetConfs[i];
				//������Դ������ʵ��Ƭ��
				LfwWidget widget = buildWidget(pageMeta, widgetConfig, metaId, paramMap);
				widget.setId(widgetConfig.getId());
				pageMeta.addWidget(widget);
			}
			//���������Ƭ�η������
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
	 * ��ȡҳ��������
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
					throw new LfwParseException("��ȡPageMetaʱ����pageid=" + metaId);
//				pageMetaConf.setExtendAttribute(PageMeta.FOLD_PATH, folderPath);
				pageMetaConf.setExtendAttribute(PageMeta.MODIFY_TS, lastModified);
				pageMetaConf.setFoldPath(folderPath);
				pageMetaPool.put(metaId + CONF_POSTFIX, (PageMeta) pageMetaConf.clone());
			}
			else
				throw new LfwParseException("��·��:" + filePath + ",û���ҵ�PageMeta");
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
	 * ��ȡ�����ļ�������޸�ʱ�䣬��������̬����ʼ�շ��� -1
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
	 * ��ȡƬ�ι�����
	 * @param widgetConf
	 * @return
	 */
	protected IWidgetBuilder getWidgetBuilder(WidgetConfig widgetConf) {
		return new DefaultWidgetBuilder();
	}
	
	/**
	 * �����������ȡPageMeta
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
			throw new LfwParseException("����ҳ��ʱ���ִ���");
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
