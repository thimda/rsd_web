package nc.uap.lfw.design.itf;

import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public interface ILfwDesignDataProvider {
	
	public Map<String, IRefNode> getRefNode(String projectPath);
	
	public Map<String, Map<String, IRefNode>> getAllRefNodesFromPool(String ctx);
	
	//String ctx =ResourcesPlugin.getWorkspace().getRoot().getc.getWorkspace().getRoot().getProjects() 
	public Map<String,Dataset> getDataset(String projectPath);
	
	public  Map<String, Map<String, Dataset>> getAllPoolDatasets(String ctx);
	
	public  Map<String, Map<String, LfwWidget>> getAllPoolWidgets(String ctx);
	
	public List getAllComponents();
	
	public List getAllClasses();
	
	public void removeDsFromPool(String rootPath, Dataset ds);
	
	public void removeWidgetFromPool(String rootPath, LfwWidget widget);
	
	public void removeRefnodeFromPool(String rootPath, IRefNode refNode);
	
	public String[][] getAllAccounts();
	
	public PageMeta getPageMeta(Map<String, Object> paramMap, Map <String, String> userInfoMap);
	
	
	public void saveDatasettoXml(String rootPath,String filePath, String fileName, Dataset ds) ;
	
	public void saveRefNodetoXml(String rootPath, String filePath, String fileName, IRefNode refnode) ;
	
	public void saveWidgettoXml(String filePath, String fileName, String projectPath, LfwWidget widget, String rootPath);
	
	public void savePagemetaToXml(String filePath, String fileName, String projectPath, PageMeta meta);
	
	public String[] getAllNcRefNode();
	
	public List getAllModulse();
	
	public List getAllComponentByModuleId(String moduleid);
	
	public List getAllClassByComponentId(String componentID);
	
	public MdDataset getMdDataset(MdDataset ds);
	
	public String getAggVO(String fullClassName);
	
	public List  getNCRefMdDataset(MdDataset mdds);
	
	public List  getNCFieldRelations(MdDataset mdds);
	
	public List  getAllNCRefNode(MdDataset mdds);
	
	public List  getAllNcComboData(MdDataset mdds);
	
	/**
	 * 生成参照类
	 * @param refType
	 * @param modelClass
	 * @param refPk
	 * @param refCode
	 * @param refName
	 * @param visibleFields
	 * @param childfield
	 * @param childfield2
	 * @return
	 */
	public String generateRefNodeClass(String refType, String modelClass, String tableName, String refPk, String refCode, String refName, String visibleFields, String childfield, String childfield2);
	
	public String generatorClass(String fullPath, String extendClass, Map<String, Object> param);
	
	public String generatorVO(String fullPath, String tableName, String primaryKey, Dataset ds);
	
	public List<FuncNodeVO> getFuncRegisterVOs();
	
	
	public List<TemplateVO> getTemplateVOs();
	/**
	 * 根据funcode获取已有模板
	 * @param funcnode
	 * @return
	 */
	public List<TemplateVO> getTemplateByFuncnode(String funcnode);
	
	public String getBillType(String funnode);
	
	public List<TemplateVO> getAllTemplateByFuncnode(String funcnode);
	
	/**
	 * 根据funcode获取已有查询模板
	 * @param funcnode
	 * @return
	 */
	public List<TemplateVO> getQeuryTemplateByFuncnode(String funcnode);
	
	//getQueryTemplateByFuncnode
	
	public TypeNodeVO[] getAllMainOrgType();
	
	public void updateSysTemplate(FuncNodeVO funnodevo, TemplateVO[] templateVOs) throws LfwBusinessException;;
	
	public void delSysTemplateRelated(String funnode);
	
	public void deleteFunNode(String funcnode);
	
	public TypeNodeVO getOrgTypeByPK(String id);
	
	public UIMeta getUIMeta(String folderPath, PageMeta pm, String widgetID);
	
	public String[] registerBillAction(String billType, String[] actions);
	
	public void registerMenuItems(List<MenuItem> menuItems, String funnode);
	
	public void saveUIMeta(UIMeta meta, String pmPath, String folderPath);
	
	public String getVersion();
	
	public Map<String, String>[] getPageNames(String[] projPaths);
	
//	public PageFlow[][] getPageFlowDef(String[] projPaths);
	
//	public void pageFlowToXMl(String projectPath, PageFlow pageFlow);
	
	public void refresh();
	
//	public void saveFormComptoXml(String filePath, String fileName, StyleFormComp form);
	
//	public StyleFormComp getFormCompFromXml(String filePath, String fileName);
	
	public BCPManifest getMenifest(String filePath);
	
	public LfwWidget getMdDsFromComponent(LfwWidget widget, String componetId);
}
