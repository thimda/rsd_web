package nc.uap.lfw.core.model;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.model.util.DefaultUIMetaBuilder;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.PageMeta;

/**
 * @author dengjt 2007-1-10
 * 
 * @version 6.0
 */
public class PageModel extends BasePageModel {
	public PageModel() {
		super();
	}

	@Override
	protected IUIMeta createUIMeta(PageMeta pm) {
		IUIMeta um = null;
		if(LfwRuntimeEnvironment.isFromDB()){
			String pk_template = LfwRuntimeEnvironment.getWebContext().getParameter("pk_templateDB");
			if(pk_template != null){
				um = PageModelUtil.getUIMeta(pk_template);
			}
		}
		else
			um = getUIMetaBuilder().buildUIMeta(pm);
		return um;
	}

	protected IUIMetaBuilder getUIMetaBuilder() {
		return new DefaultUIMetaBuilder();
	}

	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
	}

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		this.initPageMetaListeners();
	}

	@Override
	protected void afterInternalInitialize() {
		boolean editMode = LfwRuntimeEnvironment.isEditMode();
		if (editMode) {
			ILfwCache session = LfwCacheManager.getSessionCache();
			// 原始的uimeta 和 pageMeta
			PageMeta pm = getPageMeta();
			if (pm != null) {
				pm.setProcessorClazz("nc.uap.lfw.core.processor.EventRequestProcessor");
			}
			String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
			session.put(pageId + WebConstant.SESSION_PAGEMETA_ORI, pm);
			session.put(pageId + WebConstant.SESSION_UIMETA_ORI, getUIMeta());
		}

		super.afterInternalInitialize();
	}

	/**
	 * 2011-8-17 上午11:42:17 renxh des：初始化 listeners ，如果是编辑态 则删除原来的监听，并加入新的监听
	 */
	private void initPageMetaListeners() {

		boolean editMode = LfwRuntimeEnvironment.isEditMode();
		if (editMode) {
			PageMeta pageMeta = getPageMeta();
			ILfwCache session = LfwCacheManager.getSessionCache();

			// 外部的uimeta 和 pageMeta
			String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
			session.put(pageId + WebConstant.SESSION_PAGEMETA_OUT, pageMeta.clone());
			session.put(pageId + WebConstant.SESSION_UIMETA_OUT, getUIMeta().doClone());
		}
	}

	public IUIMeta getUIMeta() {
		return getWebContext().getUIMeta();
	}
}
