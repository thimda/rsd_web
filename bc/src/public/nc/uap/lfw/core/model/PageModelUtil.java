package nc.uap.lfw.core.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebSession;
import nc.uap.lfw.core.exception.LfwParseException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.parser.PageMetaParser;
import nc.uap.lfw.core.model.parser.WidgetParser;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.RequestLifeCycleContext;
import nc.uap.lfw.core.uimodel.WidgetConfig;
import nc.uap.lfw.jsp.parser.UIMetaParserUtil;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.pa.PaBusinessException;
import nc.uap.lfw.stylemgr.itf.IUwTemplateService;
import nc.uap.lfw.stylemgr.vo.UwTemplateVO;
import nc.uap.lfw.stylemgr.vo.UwViewVO;

public final class PageModelUtil {
	//通过模板PK获得对应的PageMeta
	public static PageMeta getPageMeta(String pk_template){
		WebSession ws =	LfwRuntimeEnvironment.getWebContext().getWebSession();
		PageMeta pm = (PageMeta)ws.getAttribute("PAGEMETA_CACHE_" + pk_template);
		if(pm == null){
			LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);	
			if(pk_template.isEmpty() ||pk_template == null)
				throw new LfwRuntimeException("pk_template为空，需要有效的个性化模板");
			UwTemplateVO vo = getTemplateByPK(pk_template);
			if(vo == null){
				return null;
			}
			String metaStr = vo.doGetPageMetaStr();
			java.io.InputStream input = getInput(metaStr);
			pm = initPageMeta((java.io.InputStream) input, null);
			
			if(pm == null)
				return null;
			
			String[] viewIds = getViewIdsByPageMeta(pm);
			if(viewIds != null && viewIds.length > 0){
				for(int i = 0; i < viewIds.length; i++){
					String viewId = viewIds[i];
					UwViewVO ivo = getViewByIdAndPK(pk_template, viewId);
					
					if(ivo == null){
						continue;
					}
					String widgetStr = ivo.doGetWidgetStr();
					java.io.InputStream in = getInput(widgetStr);
					
					LfwWidget widget = WidgetParser.parse(in);
					if(widget != null)
						pm.addWidget(widget);
				}
			}	
			RequestLifeCycleContext.get().setPhase(oriPhase);
			ws.setAttribute("PAGEMETA_CACHE_" + pk_template, pm);
		}
		
		return pm;
	}

	private static String[] getViewIdsByPageMeta(PageMeta pm) {
		WidgetConfig[] widgets = pm.getWidgetConfs();
		
		String[] viewIds = null;
		
		if(widgets != null && widgets.length > 0){
			viewIds = new String[widgets.length];
			for(int x = 0; x < widgets.length; x ++){
				viewIds[x] =widgets[x].getId();
			}
			
		}
		return viewIds;
	}

	private static UwViewVO getViewByIdAndPK(String pk_template, String viewId) {
		IUwTemplateService service = NCLocator.getInstance().lookup(IUwTemplateService.class);
		UwViewVO ivo = null;
		try{
			ivo = service.getViewVO(viewId, pk_template); 
		}
		catch(PaBusinessException e){
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		return ivo;
	}

	//将String流的转换成input流
	private static InputStream getInput(String metaStr) {
		java.io.InputStream input = null;
		try {
			input = new java.io.ByteArrayInputStream(metaStr.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		return input;
	}

	public static UwTemplateVO getTemplateByPK(String pk_template) {
		IUwTemplateService service = NCLocator.getInstance().lookup(IUwTemplateService.class);
		UwTemplateVO vo = null;
		if(pk_template == null)
			return null;
		try {
			vo = service.getTemplateVOByPK(pk_template);
		} catch (PaBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
		return vo;
	}
	
	/**
	 * 从输入流里读取PageMeta
	 * @param input
	 * @param dirPath
	 * @return
	 */
	public static PageMeta initPageMeta(java.io.InputStream input, String dirPath){
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
	
	public static UIMeta getUIMeta(String pk_template){
		
		WebSession ws =	LfwRuntimeEnvironment.getWebContext().getWebSession();
		UIMeta meta = (UIMeta) ws.getAttribute("UIMETA_CACHE_" + pk_template);
		if(meta == null){
			LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);	
			
			UwTemplateVO template = getTemplateByPK(pk_template);
			if(template == null)
				return null;
			String uiMetaStr = template.doGetUIMetaStr();
			java.io.InputStream input = getInput(uiMetaStr);
			
			PageMeta pm = getPageMeta(pk_template);
			
			UIMetaParserUtil parserUtil = new UIMetaParserUtil();
			parserUtil.setPagemeta(pm);
			
			meta = parserUtil.parseUIMeta(input, null, pk_template);
			RequestLifeCycleContext.get().setPhase(oriPhase);
			ws.setAttribute("UIMETA_CACHE_" + pk_template, meta);
		}
		
		return meta;
	}
	

}
