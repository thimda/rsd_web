package nc.uap.lfw.core.model;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.model.util.DefaultUIMetaBuilder;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.IRefNode;

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

	/**
	 * 2011-8-10 下午04:28:17 renxh des：处理控件
	 * 
	 * @param webcs
	 */
	private void dealWebComponent(WebComponent[] webcs) {
		if (webcs != null) {
			for (WebComponent wc : webcs) {
				this.dealListeners(wc);// 先删除原有事件
			}
		}
	}

	/**
	 * 2011-8-10 下午04:28:32 renxh des：处理menubar
	 * 
	 * @param menubars
	 */
	private void dealMenuBars(MenubarComp[] menubars) {
		if (menubars != null) {
			for (MenubarComp menubar : menubars) {
				this.dealListeners(menubar);// 先删除原有事件

			}
		}
	}

	private void dealRefNodes(IRefNode[] refNodes) {
		if (refNodes != null) {
			for (IRefNode refNode : refNodes) {
				this.dealListeners((WebElement) refNode);// 先删除原有事件

			}
		}
	}

	private void dealDatasets(Dataset[] dss) {
		if (dss != null) {
			for (Dataset ds : dss) {
				this.dealListeners(ds);// 先删除原有事件
				//去掉此行后，设计器中数据集树为空
				ds.setLazyLoad(ds.isLazyLoad());
				ds.setCurrentKey(Dataset.MASTER_KEY);
				Row row = ds.getEmptyRow();
				ds.addRow(row);
				ds.setRowSelectIndex(0);
//				ds.setEnabled(true);
			}
		}
	}

	private void dealComboDatas(ComboData[] cbds) {
		if (cbds != null) {
			for (ComboData cbd : cbds) {
				this.dealListeners(cbd);// 先删除原有事件

			}
		}
	}

	/**
	 * 2011-8-18 下午06:48:38 renxh des：处理监听的总方法
	 * 
	 * @param element
	 */
	private void dealListeners(WebElement element) {
		this.removeListeners(element);// 先删除原有事件
		// this.addEditListeners(element);
	}

	/**
	 * 2011-8-10 下午03:32:39 renxh des：删除所有的监听
	 * 
	 * @param element
	 */
	private void removeListeners(WebElement element) {
//		Map<String, JsListenerConf> lisMap = element.getListenerMap();
//		if (lisMap != null) {
//			List<String> keys = new ArrayList<String>();
//			Iterator<String> keyIts = lisMap.keySet().iterator();
//			while (keyIts.hasNext()) {
//				keys.add(keyIts.next());
//			}
//			for (String key : keys) {
//				element.removeListener(key);
//			}
//		}
//
//		// 新版本事件删除
//		EventConf[] eventConfs = element.getEventConfs();
//		if (eventConfs != null) {
//			for (int i = 0; i < eventConfs.length; i++) {
//				EventConf event = eventConfs[i];
//				element.removeEventConf(event);
//
//			}
//		}
//
//		if (element instanceof FormComp) {
//			FormComp formc = (FormComp) element;
//			List<FormElement> feList = formc.getElementList();
//			for (FormElement fe : feList) {
//				this.removeListeners(fe);
//			}
//		}
	}
}
