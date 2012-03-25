package nc.uap.lfw.core.page.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.bcmanage.bs.IBusiCenterManageService;
import nc.bcmanage.vo.BusiCenterVO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.IWidgetContentProvider;
import nc.uap.lfw.core.model.parser.DatasetPoolParser;
import nc.uap.lfw.core.model.parser.RefNodePoolParser;
import nc.uap.lfw.core.model.parser.WidgetParser;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.vo.pub.BusinessException;

/**
 * 从公共池中获取相应类型对象
 * 
 * @author gd 2010-1-4
 * @version NC6.0
 * @since NC6.0
 */
public class PoolObjectManager {
	public static final String MAIN_PATH = "/pagemeta/public/";
	public static final String CMD_PATH = "commands";
	public static final String REF_PATH = "refnodes";
	public static final String DS_PATH = "dspool";
	public static final String PUB_WIDGET_PATH = "widgetpool";
	public static final String PAGE_PATH = "pagepool";
	// ds的缓存key值
	private static final String DATASET_CACHE_KEY = "DATASET_CACHE_KEY";
	private static final String REFNODE_CACHE_KEY = "REFNODE_CACHE_KEY";
	private static final String PUB_WIDGET_CACHE_KEY = "PUB_WIDGET_CACHE_KEY";

	/**
	 * 根据id获取ds
	 * 
	 * @param id
	 * @return
	 */
	public static Dataset getDataset(String id) {
		Map<String, Map<String, Dataset>> dssMap = getDatasetsFromPool(null);
		Iterator<Map<String, Dataset>> it = dssMap.values().iterator();
		while (it.hasNext()) {
			Map<String, Dataset> dsmap = it.next();
			Dataset ds = dsmap.get(id);
			if (ds != null)
				return ds;
		}
		return null;
	}

	public static Dataset getDataset(String id, String ctx) {
		Dataset ds = getDatasetsFromPool(null).get(ctx).get(id);
		if (ds != null)
			return (Dataset) ds.clone();
		return null;
	}

	/**
	 * 根据id获取参照
	 * 
	 * @param id
	 * @return
	 */
	public static IRefNode getRefNode(String id) {
		String ctx = LfwRuntimeEnvironment.getRootPath();
		ctx = ctx.substring(1);
		IRefNode refNode = getRefNodes(ctx).get(ctx).get(id);
		if (refNode != null)
			return (IRefNode) refNode.clone();
		return null;
	}

	/**
	 * 刷新ds缓存
	 * 
	 * @param ctx
	 * @return
	 */
	public static Map<String, Map<String, Dataset>> refreshDatasetPool(
			String ctx, Dataset ds) {
		ILfwCache cache = LfwCacheManager.getStrongCache(
				WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = DATASET_CACHE_KEY;
		HashMap<String, Map<String, Dataset>> dss = (HashMap<String, Map<String, Dataset>>) cache
				.get(cacheKey);
		if (dss == null) {
			dss = new HashMap<String, Map<String, Dataset>>();
			cache.put(cacheKey, dss);
		}
		Map<String, Dataset> dsMap = dss.get(ctx);
		if (dsMap == null) {
			dsMap = new HashMap<String, Dataset>();
			dss.put(ctx, dsMap);
		}
		dsMap.put(ds.getId(), ds);
		return dss;
	}

	/**
	 * 刷新widget缓存
	 * 
	 * @param ctx
	 * @param ds
	 * @return
	 */
	public static Map<String, Map<String, LfwWidget>> refreshWidgetPool(
			String ctx, LfwWidget widget) {
		ILfwCache cache = LfwCacheManager.getStrongCache(
				WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = PUB_WIDGET_CACHE_KEY;
		HashMap<String, Map<String, LfwWidget>> widgets = (HashMap<String, Map<String, LfwWidget>>) cache
				.get(cacheKey);
		if (widgets == null) {
			widgets = new HashMap<String, Map<String, LfwWidget>>();
			cache.put(cacheKey, widgets);
		}
		Map<String, LfwWidget> widgetMap = widgets.get(ctx);
		if (widgetMap == null) {
			widgetMap = new HashMap<String, LfwWidget>();
			widgets.put(ctx, widgetMap);
		}
		widgetMap.put(widget.getId(), widget);
		return widgets;
	}

	public static Map<String, Map<String, Dataset>> removeDatasetFromPool(
			String ctx, Dataset ds) {
		ILfwCache cache = LfwCacheManager.getStrongCache(
				WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = DATASET_CACHE_KEY;
		HashMap<String, Map<String, Dataset>> dss = (HashMap<String, Map<String, Dataset>>) cache
				.get(cacheKey);
		if (dss != null) {
			Map<String, Dataset> dsMap = dss.get(ctx);
			if (dsMap != null) {
				dsMap.remove(ds.getId());
			}
		}
		return dss;
	}

	/**
	 * 从缓存中删除widget
	 * 
	 * @param ctx
	 * @param widget
	 * @return
	 */
	public static Map<String, Map<String, LfwWidget>> removeWidgetFromPool(
			String ctx, LfwWidget widget) {
		ILfwCache cache = LfwCacheManager.getStrongCache(
				WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = PUB_WIDGET_CACHE_KEY;
		HashMap<String, Map<String, LfwWidget>> widgets = (HashMap<String, Map<String, LfwWidget>>) cache
				.get(cacheKey);
		if (widgets != null) {
			Map<String, LfwWidget> widgetMap = widgets.get(ctx);
			if (widgetMap != null) {
				widgetMap.remove(widget.getId());
			}
		}
		return widgets;
	}

	public static Map<String, Map<String, IRefNode>> removeRefNodeFromPool(
			String ctx, IRefNode refnode) {
		ILfwCache cache = LfwCacheManager.getStrongCache(
				WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = REFNODE_CACHE_KEY;
		HashMap<String, Map<String, IRefNode>> refNodesMap = (HashMap<String, Map<String, IRefNode>>) cache
				.get(cacheKey);
		if (refNodesMap != null) {
			Map<String, IRefNode> refNodeMap = refNodesMap.get(ctx);
			if (refNodeMap != null) {
				refNodeMap.remove(refnode.getId());
			}
		}
		return refNodesMap;
	}

	/**
	 * 刷新refnode缓存
	 * 
	 * @param ctx
	 * @return
	 */
	public static Map<String, Map<String, IRefNode>> refreshRefNodePool(
			String ctx, IRefNode refNode) {
		ILfwCache cache = LfwCacheManager.getStrongCache(
				WebConstant.LFW_CORE_CACHE, null);
		// cache.remove(REFNODE_CACHE_KEY);
		String cacheKey = REFNODE_CACHE_KEY;
		HashMap<String, Map<String, IRefNode>> refNodesMap = (HashMap<String, Map<String, IRefNode>>) cache
				.get(cacheKey);
		if (refNodesMap == null) {
			refNodesMap = new HashMap<String, Map<String, IRefNode>>();
			cache.put(cacheKey, refNodesMap);
		}
		Map<String, IRefNode> ctxNodeMap = refNodesMap.get(ctx);
		if (ctxNodeMap == null) {
			ctxNodeMap = new HashMap<String, IRefNode>();
			refNodesMap.put(ctx, ctxNodeMap);
		}
		ctxNodeMap.put(refNode.getId(), refNode);
		return refNodesMap;
	}

	public static Map<String, Map<String, Dataset>> getDatasetsFromPool(
			String ctx) {
		ILfwCache cache = LfwCacheManager.getStrongCache(
				WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = DATASET_CACHE_KEY;
		HashMap<String, Map<String, Dataset>> dss = (HashMap<String, Map<String, Dataset>>) cache
				.get(cacheKey);
		if (dss == null) {
			dss = new HashMap<String, Map<String, Dataset>>();
			cache.put(cacheKey, dss);
		}
		if (ctx != null) {
			Map<String, Dataset> ctxdsss = dss.get(ctx);
			if (ctxdsss == null) {
				ctxdsss = new HashMap<String, Dataset>();
				dss.put(ctx, ctxdsss);
				String[] confFiles = PoolObjectUtil
						.getConfigFilePaths(MAIN_PATH + DS_PATH);
				if (confFiles != null) {
					for (int i = 0; i < confFiles.length; i++) {
						String filePath = confFiles[i];
						try {
							Dataset ds = DatasetPoolParser
									.parse(ContextResourceUtil
											.getResourceAsStream(filePath));
							String dsId = PoolObjectUtil.parsePathId(filePath,
									MAIN_PATH + DS_PATH);
							ds.setId(dsId);
							ctxdsss.put(dsId, ds);
						} catch (Exception e) {
							Logger.error(
									"===PoolObjectManager类getDatasetFromPool方法:初始化文件"
											+ filePath + "出错", e);
						}
					}
				}
			}
		}
		return dss;
	}

	/**
	 * 从公共池中获得widget池
	 * 
	 * @param ctx
	 * @return
	 */
	public static Map<String, Map<String, LfwWidget>> getWidgetsFromPool(
			String ctx) {
		if (LfwRuntimeEnvironment.getDatasource() == null) {
			IBusiCenterManageService busiCender = NCLocator.getInstance()
					.lookup(IBusiCenterManageService.class);
			BusiCenterVO[] busicenters = null;
			try {
				busicenters = busiCender.getBusiCenterVOs();
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				LfwLogger.error(e1.getMessage(), e1);
			}
			for (int i = 0; i < busicenters.length; i++) {
				// 设置默认数据源
				BusiCenterVO busi = busicenters[i];
				if (busi.getCode() != null
						&& ("develop".equals(busi.getCode()))
						|| "0000".equals(busi.getCode())) {
					continue;
				} else {
					LfwRuntimeEnvironment.setDatasource(busi
							.getDataSourceName());
					break;
				}
			}
			if (LfwRuntimeEnvironment.getDatasource() == null
					|| "".equals(LfwRuntimeEnvironment.getDatasource()))
				LfwRuntimeEnvironment.setDatasource(busicenters[0]
						.getDataSourceName());
		} else
			LfwRuntimeEnvironment.setDatasource(LfwRuntimeEnvironment
					.getDatasource());
		if (LfwRuntimeEnvironment.getRootPath() == null) {
			LfwRuntimeEnvironment.setRootPath(ctx);
		}
		ILfwCache cache = LfwCacheManager.getStrongCache(
				WebConstant.LFW_CORE_CACHE, null);
		String cacheKey = PUB_WIDGET_CACHE_KEY;
		HashMap<String, Map<String, LfwWidget>> widgets = (HashMap<String, Map<String, LfwWidget>>) cache
				.get(cacheKey);
		if (widgets == null) {
			widgets = new HashMap<String, Map<String, LfwWidget>>();
			cache.put(cacheKey, widgets);
		}
		if (ctx != null) {
			Map<String, LfwWidget> ctxwidets = widgets.get(ctx);
			ctxwidets = null;
			if (ctxwidets == null) {
				ctxwidets = new HashMap<String, LfwWidget>();
				widgets.put(ctx, ctxwidets);
				String[] confFiles = PoolObjectUtil
						.getConfigFilePaths(MAIN_PATH + PUB_WIDGET_PATH);
				if (confFiles != null) {
					for (int i = 0; i < confFiles.length; i++) {
						String filePath = confFiles[i];
						try {
							LfwWidget widget = WidgetParser
									.parse(ContextResourceUtil
											.getResourceAsStream(filePath));
							String widgetId = PoolObjectUtil.parseWidgetId(
									filePath, MAIN_PATH + PUB_WIDGET_PATH);
//							 if(widget.getProvider()!=null){
//							 String providerStr = widget.getProvider();
//							 Class providerClass =
//							 (Class)Class.forName(providerStr);
//							 Object d = providerClass.newInstance();
//							 Method m = providerClass.getMethod("buildWidget",
//							 PageMeta.class,LfwWidget.class,Map.class,String.class);
//							 widget = (LfwWidget)m.invoke(d,
//							 null,widget,null,widget.getId());
//							 }
							widget.setId(widgetId);
							ctxwidets.put(widgetId, widget);
						} catch (Throwable e) {
							Logger.error(
									"===PoolObjectManager类getWidgetsFromPool方法:初始化文件"
											+ filePath + "出错", e);
						}
					}
				}
			}
		}
		return widgets;
	}

	/**
	 * 根据ID从公共池中取得widget
	 * 
	 * @param id
	 * @return
	 */
	public static LfwWidget getWidget(String id) {
		Map<String, Map<String, LfwWidget>> widgetMap = getWidgetsFromPool(null);
		Iterator<Map<String, LfwWidget>> it = widgetMap.values().iterator();
		while (it.hasNext()) {
			Map<String, LfwWidget> widgetmap = it.next();
			LfwWidget widget = widgetmap.get(id);
			if (widget != null)
				return widget;
		}
		return null;
	}

	public static Map<String, Map<String, IRefNode>> getRefNodes(String ctx) {
		ILfwCache cache = LfwCacheManager.getStrongCache(
				WebConstant.LFW_CORE_CACHE, null);
		HashMap<String, Map<String, IRefNode>> refNodes = (HashMap<String, Map<String, IRefNode>>) cache
				.get(REFNODE_CACHE_KEY);
		if (refNodes == null || refNodes.get(ctx) == null) {
			String[] confFiles = PoolObjectUtil.getConfigFilePaths(MAIN_PATH
					+ REF_PATH);
			if (refNodes == null)
				refNodes = new HashMap<String, Map<String, IRefNode>>();
			HashMap<String, IRefNode> ctxrefNodes = new HashMap<String, IRefNode>();
			if (confFiles != null) {
				for (int i = 0; i < confFiles.length; i++) {
					String filePath = confFiles[i];
					try {
						IRefNode pool = RefNodePoolParser
								.parse(ContextResourceUtil
										.getResourceAsStream(filePath));
						if (pool == null)
							continue;
						String refPoolId = PoolObjectUtil.parsePathId(filePath,
								MAIN_PATH + REF_PATH);
						pool.setId(refPoolId);
						ctxrefNodes.put(pool.getId(), pool);
						refNodes.put(ctx, ctxrefNodes);
					} catch (Exception e) {
						Logger.error("===PoolObjectManager类getRefNode方法:初始化文件"
								+ filePath + "出错", e);
					}
				}
			}
			cache.put(REFNODE_CACHE_KEY, refNodes);
		}
		return refNodes;
	}

	public static LfwWidget rebuiltWidget(LfwWidget widget, String providerStr) {
		try {
			widget = (LfwWidget) widget.clone();
			Class<IWidgetContentProvider> providerClass = (Class<IWidgetContentProvider>) Class.forName(providerStr);
			IWidgetContentProvider d = providerClass.newInstance();			
//			Method m = providerClass.getMethod("buildWidget", PageMeta.class,
//					LfwWidget.class, Map.class, String.class);
//			widget = (LfwWidget) m
//					.invoke(d, null, widget, null, widget.getId());
			widget = d.buildWidget(null, widget, null, widget.getId());
		} catch (Exception e) {
			LfwLogger.error(e);
		}
		return widget;
	}
}
