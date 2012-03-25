package nc.uap.lfw.design.itf;

import java.util.Map;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public interface IPageMetaProviderForDesign {
	public PageMeta getPageMeta(Map<String, Object> paramMap);
	
	public PageMeta getPageMetaWithWidget(Map<String, Object> paramMap, Map<String, String> userInfo);
	
	public LfwWidget getWidget(Map<String, Object> paramMap, String widgetId);
	
	public void saveWidgetToXml(String filePath, String fileName, String projectPath, LfwWidget widget, String rootPath);
	
	public void savePageMetaToXml(String filePath, String fileName, String projectPath, PageMeta meta);
	
	public void saveDatasetToXml(String rootPath, String filePath, String fileName, Dataset ds);
	
	public void removeDsFromPool(String rootPath, Dataset ds);
	
	public void removeWidgetFromPool(String rootPath, LfwWidget widget);
	
	public void removeRefNodeFromPool(String rootPath, IRefNode refnode);
	
	public void saveRefNodeToXml(String rootPath, String filePath, String fileName, IRefNode refnode);
	
	public  Map<String, Dataset> getDatasetsFromPool(String projectPath);
	
	public  Map<String, Map<String, Dataset>> getAllPoolDatasets(String ctx);
	
	public  Map<String, Map<String, LfwWidget>> getAllPoolWidgets(String ctx);
	
	public Map<String, IRefNode> getRefNodesFromPool(String projectPath);
	
	public Map<String, Map<String, IRefNode>> getAllRefNodesFromPool(String ctx);
	
	public UIMeta getUIMeta(String folderPath, PageMeta pm, String widgetId);
	
	public void saveUIMeta(UIMeta meta, String pmPath, String folderPath);
	
//	public void saveFormCompToXml(String filePath, String fileName,StyleFormComp form);
	
//	public StyleFormComp getFormCompFromXml(String filePath, String fileName);
	
	public BCPManifest getMenifest(String filePath);
}
