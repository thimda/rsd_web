package nc.uap.lfw.core.model.util;

import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.refnode.RefNodeRelations;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 片段组合工具，用来将不同来源的片段按照规则组合一起
 */
public class LfwWidgetMergeUtil {
	public static void merge(LfwWidget targetWidget, LfwWidget sourceWidget) {
		targetWidget.setFrom(sourceWidget.getFrom());
		
		//不以本地TS为准
		sourceWidget.getExtendMap().remove(LfwWidget.MODIFY_TS);
		sourceWidget.getExtendMap().remove(LfwWidget.UNIQUE_TS);
		sourceWidget.getExtendMap().remove(LfwWidget.UNIQUE_ID);
		targetWidget.getExtendMap().putAll(sourceWidget.getExtendMap());
	
		ViewComponents viewComponents = sourceWidget.getViewComponents();
		mergeViewComponents(targetWidget.getViewComponents(), viewComponents);
		
		ViewModels viewModel = sourceWidget.getViewModels();
		mergeViewModel(targetWidget.getViewModels(), viewModel);
		
		ViewMenus viewMenus = sourceWidget.getViewMenus();
		mergeViewMenus(targetWidget.getViewMenus(), viewMenus);
	}
	
	private static void mergeViewMenus(ViewMenus targetMenus, ViewMenus sourceMenus) {
		ContextMenuComp[] menus = sourceMenus.getContextMenus();
		for (ContextMenuComp contextMenuComp : menus) {
			targetMenus.addContextMenu(contextMenuComp);
		}
	}

	private static void mergeViewComponents(ViewComponents targetComps, ViewComponents sourceComps){
		
		WebComponent[] components = sourceComps.getComponents();
		if(components != null && components.length > 0){
			for(WebComponent component: components) {
				if(component.getConfType().equals(WebElement.CONF_ADD))
					targetComps.addComponent(component);
				else if(component.getConfType().equals(WebElement.CONF_DEL)){
					if(targetComps.getComponentsMap() != null)
						targetComps.getComponentsMap().remove(component.getId());
				}
				else if(component.getConfType().equals(WebElement.CONF_REF)){
					WebComponent sourceComp = targetComps.getComponent(component.getId());
					if(sourceComp == null)
						throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "LfwPMMergeUtil-000001", null, new String[]{component.getId()})/*merge component对象时出错，找不到{0}*/);
					sourceComp.mergeProperties(component);
				}
			}
		}
	}
	
	private static void mergeViewModel(ViewModels targetModels, ViewModels sourceModels){
		Dataset[] dss = sourceModels.getDatasets();
		if(dss.length > 0){
			for (int i = 0; i < dss.length; i++) {
				Dataset ds = dss[i];
				if(ds.getConfType().equals(WebElement.CONF_ADD))
					targetModels.addDataset(ds);
				else if(ds.getConfType().equals(WebElement.CONF_DEL)){
					targetModels.removeDataset(ds.getId());
				}
				else if(ds.getConfType().equals(WebElement.CONF_REF)){
					Dataset sourceDs = targetModels.getDataset(ds.getId());
					if(sourceDs == null)
						throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "LfwPMMergeUtil-000002", null, new String[]{ds.getId()})/*merge dataset对象时出错，找不到{0}*/);
					sourceDs.mergeProperties(ds);
				}
			}
		}
		
		DatasetRelations dsRelations = sourceModels.getDsrelations();
		if(dsRelations != null){
			DatasetRelations sourceDsRelations = targetModels.getDsrelations();
			if(sourceDsRelations == null)
				targetModels.setDsrelations(dsRelations);
			else
				targetModels.getDsrelations().addDsRelations(dsRelations);
		}
		
		RefNodeRelations refnodeRels = sourceModels.getRefNodeRelations();
		if(refnodeRels != null){
			//RefNodeRelations sourcedsRefNodeRels = targetModels.getRefNodeRelations();
			targetModels.setRefnodeRelations(refnodeRels);
		}
//		Map<String, PageData> pdMap = sourceModels.getPageDataMap();
//		if(pdMap.size() > 0){
//			Iterator<PageData> it = pdMap.values().iterator();
//			while(it.hasNext()){
//				PageData pd = it.next();
//				if(pd.getConfType().equals(WebElement.CONF_ADD))
//					targetModels.addPageData(pd);
//				else if(pd.getConfType().equals(WebElement.CONF_DEL)){
//					targetModels.getPageDataMap().remove(pd.getId());
//				}
//				else if(pd.getConfType().equals(WebElement.CONF_REF)){
//					PageData sourcePd = targetModels.getPageDataMap().get(pd.getId());
//					if(sourcePd == null)
//						throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "LfwPMMergeUtil-000003", null, new String[]{pd.getId()})/*merge pagedata对象时出错，找不到{0}*/);
//					sourcePd.mergeProperties(pd);
//				}
//			}
//		}
		
		IRefNode[] refnodes = sourceModels.getRefNodes();
		if(refnodes != null && refnodes.length > 0){
			for(IRefNode rn: refnodes) {
				RefNode refnode = (RefNode) rn;
				if(refnode.getConfType().equals(WebElement.CONF_ADD))
					targetModels.addRefNode(refnode);
				else if(refnode.getConfType().equals(WebElement.CONF_DEL)){
					targetModels.removeRefNode(refnode.getId());
				}
				else if(refnode.getConfType().equals(WebElement.CONF_REF)){
					RefNode sourceComp = (RefNode) targetModels.getRefNode(refnode.getId());
					if(sourceComp == null)
						throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lfw", "LfwPMMergeUtil-000004", null, new String[]{refnode.getId()})/*merge refnode对象时出错，找不到{0}*/);
					sourceComp.mergeProperties(refnode);
				}
			}
		}
	}
}
