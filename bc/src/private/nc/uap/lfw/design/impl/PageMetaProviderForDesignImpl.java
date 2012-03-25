package nc.uap.lfw.design.impl;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.uap.lfw.conf.persist.WidgetToXml;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.manager.PoolObjectManager;
import nc.uap.lfw.core.persistence.DatasetToXml;
import nc.uap.lfw.core.persistence.PageMetaToXml;
import nc.uap.lfw.core.persistence.RefnodeToXml;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.design.itf.BCPManifest;
import nc.uap.lfw.design.itf.IPageMetaProviderForDesign;
import nc.uap.lfw.design.itf.MenifestParser;
import nc.uap.lfw.jsp.parser.UIMetaParserUtil;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * PageMeta和Widget的解析类,为插件使用定制
 * 
 * @author gd 2010-1-14
 * @version NC6.0
 * @since NC6.0
 */
public class PageMetaProviderForDesignImpl implements
		IPageMetaProviderForDesign {

	private static PageMetaBuilderForDesign builder = new PageMetaBuilderForDesign();

	public PageMeta getPageMeta(Map<String, Object> paramMap) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		return builder.buildPageMeta(paramMap);
	}

	public LfwWidget getWidget(Map<String, Object> paramMap, String widgetId) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		return builder.buildWidget(paramMap, widgetId);
	}

	public void removeDsFromPool(String rootPath, Dataset ds) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		setRootPath(rootPath);
		DatasetToXml.deletePoolDs(ds);
	}

	public void removeWidgetFromPool(String rootPath, LfwWidget widget) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		setRootPath(rootPath);
		PoolObjectManager.removeWidgetFromPool(rootPath, widget);
	}

	public void removeRefNodeFromPool(String rootPath, IRefNode refnode) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		setRootPath(rootPath);
		RefnodeToXml.deletePoolRefNode(refnode);
	}

	public void savePageMetaToXml(String filePath, String fileName,
			String projectPath, PageMeta meta) {
		PageMetaToXml.toXml(filePath, fileName, projectPath, meta);
	}

	public void saveWidgetToXml(String filePath, String fileName,
			String projectPath, LfwWidget widget, String rootPath) {
		setRootPath(rootPath);
		WidgetToXml.toXml(filePath, fileName, projectPath, widget);
	}

	public void saveDatasetToXml(String rootPath, String filePath,
			String fileName, Dataset ds) {
		// 设为design模式
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		setRootPath(rootPath);
		DatasetToXml.toXml(filePath, fileName, ds);
	}

	public Map<String, Map<String, IRefNode>> getAllRefNodesFromPool(String ctx) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		return PoolObjectManager.getRefNodes(ctx);
	}

	public void setRootPath(String rootPath) {
		LfwRuntimeEnvironment.setRootPath(rootPath);
	}

	public Map<String, Dataset> getDatasetsFromPool(String projectPath) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		// return PoolObjectManager.getDatasetsFromPool(projectPath);
		return DatasetParser.getDatasetsFromPool(projectPath);
	}

	public Map<String, Map<String, Dataset>> getAllPoolDatasets(String ctx) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		return PoolObjectManager.getDatasetsFromPool(ctx);
	}

	public Map<String, IRefNode> getRefNodesFromPool(String projectPath) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		return RefNodeParser.getRefnodesFromPool(projectPath);
	}

	public UIMeta getUIMeta(String folderPath, PageMeta pm, String widgetId) {
		UIMetaParserUtil util = new UIMetaParserUtil();
		util.setPagemeta(pm);
		return util.parseUIMeta(folderPath, widgetId);
	}

	public void saveUIMeta(UIMeta meta, String pmPath, String folderPath) {
		folderPath = folderPath.replaceAll("\\\\", "/");
		UIMetaToXml.toXml(meta, folderPath);
		if (pmPath == null)
			return;
		int index = folderPath.indexOf("/html/nodes");
		String webPath = pmPath.substring(0, index);
		String nodePath = pmPath.substring(index);
		webPath += "/webtemp" + nodePath;
		File f = new File(webPath);
		if (f.exists()) {
			File[] fs = f.listFiles();
			if (fs != null) {
				for (int i = 0; i < fs.length; i++) {
					fs[i].delete();
				}
			}
			f.delete();
		}
	}

	public PageMeta getPageMetaWithWidget(Map<String, Object> paramMap,
			Map<String, String> userInfoMap) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		try {
			Class c = Class.forName("nc.uap.lfw.compatible.NCLoginUtil");
			Method m = c.getMethod("doNCLogin", new Class[] { Map.class });
			m.invoke(null, new Object[] { userInfoMap });
			// NCLoginUtil.doNCLogin(userInfoMap);
		} catch (Exception e) {
			LfwLogger.error(e);
		}
		return builder.buildPageMetaAndWidget(paramMap);
	}

	public void saveRefNodeToXml(String rootPath, String filePath,
			String fileName, IRefNode refnode) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		setRootPath(rootPath);
		RefnodeToXml.toXml(filePath, fileName, refnode);
	}

	// public void saveFormCompToXml(String filePath, String fileName,
	// StyleFormComp form) {
	// FormCompToXml.saveFormComptoXml(filePath, fileName, form);
	// }
	//	
	//	
	// public StyleFormComp getFormCompFromXml(String filePath, String
	// fileName){
	// return FormCompToXml.getFormCompFromXml(filePath, fileName);
	// }

	public BCPManifest getMenifest(String filePath) {
		return MenifestParser.getMenifest(filePath);
	}

	public Map<String, Map<String, LfwWidget>> getAllPoolWidgets(String ctx) {
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		Map<String, Map<String, LfwWidget>> poolMap = PoolObjectManager
				.getWidgetsFromPool(ctx);
		Map<String, Map<String, LfwWidget>> newPoolMap = new HashMap<String, Map<String, LfwWidget>>();
		Map<String, LfwWidget> newWidgetMaps = new HashMap<String, LfwWidget>();
		try {
			Iterator<Map.Entry<String, Map<String, LfwWidget>>> widgetMaps = poolMap
					.entrySet().iterator();

			while (widgetMaps.hasNext()) {
				Map.Entry<String, Map<String, LfwWidget>> widgetSet = widgetMaps
						.next();
				Map<String, LfwWidget> widgetMap = widgetSet.getValue();
				String key = widgetSet.getKey();
				Iterator<LfwWidget> widgets = widgetMap.values().iterator();
				while (widgets.hasNext()) {
					LfwWidget widget = (LfwWidget) widgets.next().clone();
					if (widget.getProvider() != null) {
						String providerStr = widget.getProvider();
						widget = PoolObjectManager.rebuiltWidget(widget,
								providerStr);
					}
					newWidgetMaps.put(widget.getId(), widget);
				}
				newPoolMap.put(key, newWidgetMaps);
			}
		} catch (Exception e) {
			LfwLogger.error(e);
		}
		return newPoolMap;
	}

}
