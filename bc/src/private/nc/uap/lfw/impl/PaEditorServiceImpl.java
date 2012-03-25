package nc.uap.lfw.impl;

import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class PaEditorServiceImpl implements IPaEditorService {

	@Override
	public PageMeta getOriPageMeta(String pageId,String sessionId) {		
		ILfwCache sessionCache = LfwCacheManager.getSessionCache(sessionId);
		PageMeta pageMeta = (PageMeta) sessionCache.get(pageId + WebConstant.SESSION_PAGEMETA_ORI);
		return pageMeta;

	}

	@Override
	public UIMeta getOriUIMeta(String pageId,String sessionId) {
		ILfwCache sessionCache = LfwCacheManager.getSessionCache(sessionId);
		UIMeta uiMeta = (UIMeta) sessionCache.get(pageId + WebConstant.SESSION_UIMETA_ORI);
		return uiMeta;
	}

	@Override
	public PageMeta getOutPageMeta(String pageId,String sessionId) {
		ILfwCache sessionCache = LfwCacheManager.getSessionCache(sessionId);
		PageMeta pageMeta = (PageMeta) sessionCache.get(pageId + WebConstant.SESSION_PAGEMETA_OUT);
		return pageMeta;
	}

	@Override
	public UIMeta getOutUIMeta(String pageId,String sessionId) {
		ILfwCache sessionCache = LfwCacheManager.getSessionCache(sessionId);
		UIMeta uiMeta = (UIMeta) sessionCache.get(pageId + WebConstant.SESSION_UIMETA_OUT);
		return uiMeta;
	}

	@Override
	public void clearSessionCache(String sessionId) {
		try{
			LfwCacheManager.removeSessionCache(sessionId);
		}
		catch(Exception e){
			LfwLogger.error(e);
		}
		
	}

}
