package nc.uap.lfw.design.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.design.itf.BCPManifest;
import nc.uap.lfw.design.itf.FuncNodeVO;
import nc.uap.lfw.design.itf.IDatasetProvider;
import nc.uap.lfw.design.itf.IGeneratorCode;
import nc.uap.lfw.design.itf.ILfwDesignDataProvider;
import nc.uap.lfw.design.itf.IPageMetaProviderForDesign;
import nc.uap.lfw.design.itf.TemplateVO;
import nc.uap.lfw.design.itf.TypeNodeVO;
import nc.uap.lfw.design.noexport.IFuncRegisterService;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class LfwDesignDataProvider implements ILfwDesignDataProvider{
	
	private IPageMetaProviderForDesign getPageMetaProvider(){
		Properties props = new  Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		//props.setProperty(NCLocator.TARGET_MODULE, "uapweb");
		IPageMetaProviderForDesign pmProvider = NCLocator.getInstance(props).lookup(IPageMetaProviderForDesign.class);
		return pmProvider;
	}
	
	private IFuncRegisterService getFuncRegisterService(){
		Properties props = new  Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		//props.setProperty(NCLocator.TARGET_MODULE, "uapweb");
		IFuncRegisterService service = NCLocator.getInstance(props).lookup(IFuncRegisterService.class);
		return service;
	}
	
	private IDatasetProvider getDataProvider(){
		Properties props = new  Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		IDatasetProvider dataProvider = NCLocator.getInstance(props).lookup(IDatasetProvider.class);
		return dataProvider;
	}
	
	private IGeneratorCode getCodeGenerator(){
		Properties props = new  Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		IGeneratorCode generator = NCLocator.getInstance(props).lookup(IGeneratorCode.class);
		return generator;
	}
	
	public Map<String, IRefNode> getRefNode(String projectPath){
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		return pmProvider.getRefNodesFromPool(projectPath);
	}
	
	public void removeDsFromPool(String rootPath, Dataset ds) {
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		pmProvider.removeDsFromPool(rootPath, ds);
	}
	
	public void removeRefnodeFromPool(String rootPath, IRefNode refNode) {
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		pmProvider.removeRefNodeFromPool(rootPath, refNode);
	}
	
	public Map<String, Map<String, IRefNode>> getAllRefNodesFromPool(String ctx){
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		return pmProvider.getAllRefNodesFromPool(ctx);
	}
	
	
	//String ctx =ResourcesPlugin.getWorkspace().getRoot().getc.getWorkspace().getRoot().getProjects() 
	public Map<String,Dataset> getDataset(String projectPath){
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		return pmProvider.getDatasetsFromPool(projectPath);
	}
	
	
	public Map<String, Map<String, Dataset>> getAllPoolDatasets(String projectPath){
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		return pmProvider.getAllPoolDatasets(projectPath);
	}
	
	public Map<String, Map<String, LfwWidget>> getAllPoolWidgets(String ctx){
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		return pmProvider.getAllPoolWidgets(ctx);
	}
	
	public String getBillType(String funcnode){
		IFuncRegisterService service = getFuncRegisterService();
		try {
			return service.getBillType(funcnode);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	public PageMeta getPageMeta(Map<String, Object> paramMap, Map <String, String> userInfoMap) {
		Properties props = new  Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		//props.setProperty(NCLocator.TARGET_MODULE, "uapweb");
//		LogUtility.logErrorMessage("NCLOcator================");
//		LogUtility.logErrorMessage("NCLOcator:" + NCLocator.class.getName());
		IPageMetaProviderForDesign pmProvider = NCLocator.getInstance(props).lookup(IPageMetaProviderForDesign.class);
		return (PageMeta) pmProvider.getPageMetaWithWidget(paramMap, userInfoMap);
	}

	public String[][] getAllAccounts(){
		String[][] account = null;
		IFuncRegisterService service = getFuncRegisterService();
		try {
			account =  service.getAllAccounts();
		} 
		catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return account;
	}
	
	public void saveDatasettoXml(String rootPath, String filePath, String fileName, Dataset ds) {
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		pmProvider.saveDatasetToXml(rootPath, filePath, fileName, ds);
	}

	
	public void savePagemetaToXml(String filePath, String fileName, String projectPath, PageMeta meta) {
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		pmProvider.savePageMetaToXml(filePath, fileName, projectPath, meta);
	}
	
	public String[] getAllNcRefNode() {
		String[] names = null;
		IFuncRegisterService service = getFuncRegisterService();
		try {
			names =  service.getAllRefNode();
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return names;
	}
	
	public List getAllModulse() {
		List modules = new ArrayList();
		IDatasetProvider dataProvider = getDataProvider();
		try {
			modules =  dataProvider.getALlModuels();
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return modules;
	}
	
	public List getAllComponents() {
		List components = new ArrayList();
		IDatasetProvider dataProvider = getDataProvider();
		try {
			components =  dataProvider.getAllComponents();
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return components;
	}
	
	public List getAllClasses(){
		List classes = new ArrayList();
		IDatasetProvider dataProvider = getDataProvider();
		try {
			classes =  dataProvider.getAllClasses();
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return classes;
		
	}
	
	public List getAllComponentByModuleId(String moduleid) {
		List components = new ArrayList();
		IDatasetProvider dataProvider = getDataProvider();
		try {
			components =  dataProvider.getAllComponentByModuleId(moduleid);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return components;
	}
	
	public List getAllClassByComponentId(String componentID) {
		List classes = new ArrayList();
		IDatasetProvider dataProvider = getDataProvider();
		try {
			classes =  dataProvider.getAllClassByComId(componentID);
		} catch (LfwBusinessException e) {
			LfwLogger.error(e.getMessage(), e);
		}
		return classes;
	}
	
	public MdDataset getMdDataset(MdDataset ds) {
		IDatasetProvider dataProvider = getDataProvider();
		ds =  dataProvider.getMdDataset(ds);
		return ds;
	}
	
	public String getAggVO(String fullClassName) {
		IDatasetProvider dataProvider = getDataProvider();
		String aggvo = null;
		try {
			aggvo =  dataProvider.getAggVo(fullClassName);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return aggvo;
	}
	
	public List  getNCRefMdDataset(MdDataset mdds) {
		List refdsList = new ArrayList();
		IDatasetProvider dataProvider = getDataProvider();
		try {
			refdsList =  dataProvider.getRefMdDatasetList(mdds);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return refdsList;
	}
	
	public List  getNCFieldRelations(MdDataset mdds) {
		List refdsList = new ArrayList();
		IDatasetProvider dataProvider = getDataProvider();
		try {
			refdsList =  dataProvider.getFieldRelations(mdds);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return refdsList;
	}
	
	public List  getAllNCRefNode(MdDataset mdds) {
		List refnodeList = new ArrayList();
		IDatasetProvider dataProvider = getDataProvider();
		try {
			refnodeList =  dataProvider.getNcRefNodeList(mdds);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return refnodeList;
	}
	
	public List  getAllNcComboData(MdDataset mdds) {
		List combodataList = new ArrayList();
		IDatasetProvider dataProvider = getDataProvider();
		try {
			combodataList = dataProvider.getNcComoboDataList(mdds);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return combodataList;
	}
	
	public String generatorClass(String fullPath, String extendClass,Map<String, Object> param){
		IGeneratorCode generator = getCodeGenerator();
		try {
			  return generator.generatorCode(fullPath, extendClass, param);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
			return "";
		}
	}
	
	public String generatorVO(String fullPath, String tableName, String primaryKey, Dataset ds){
		IGeneratorCode generator = getCodeGenerator();
		try {
			  return generator.generatorVO(fullPath, tableName, primaryKey, ds);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
			return "";
		}
	}
		
	public List<FuncNodeVO> getFuncRegisterVOs() {
		IFuncRegisterService service = getFuncRegisterService();
		try {
			return service.getFuncNodeVOs();
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	public List<TemplateVO> getTemplateVOs() {
		IFuncRegisterService service = getFuncRegisterService();
		try {
			return service.getTemplateVOs();
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 根据funcode获取已有模板
	 * @param funcnode
	 * @return
	 */
	public List<TemplateVO> getTemplateByFuncnode(String funcnode) {
		IFuncRegisterService service = getFuncRegisterService();
		try {
			return service.getTemplateByFuncnode(funcnode);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public List<TemplateVO> getAllTemplateByFuncnode(String funcnode) {
		IFuncRegisterService service = getFuncRegisterService();
		try {
			return service.getAllTemplateByFuncnode(funcnode);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 根据funcode获取已有查询模板
	 * @param funcnode
	 * @return
	 */
	public List<TemplateVO> getQeuryTemplateByFuncnode(String funcnode) {
		IFuncRegisterService service = getFuncRegisterService();
		try {
			return service.getQueryTemplateByFuncnode(funcnode);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	//getQueryTemplateByFuncnode
	
	public TypeNodeVO[] getAllMainOrgType(){
		IFuncRegisterService service = getFuncRegisterService();
		try {
			return  service.getAllMainOrgType();
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public void updateSysTemplate(FuncNodeVO funnodevo, TemplateVO[] templateVOs) throws LfwBusinessException{
		IFuncRegisterService service = getFuncRegisterService();
		service.updateSysTemplate(funnodevo, templateVOs);
	}
	
	
	public void delSysTemplateRelated(String funnode){
		IFuncRegisterService service = getFuncRegisterService();
		try {
			 service.delSysTemplateRelated(funnode);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}
	
	public void deleteFunNode(String funcnode){
		IFuncRegisterService service = getFuncRegisterService();
		try {
			 service.deleteFunNode(funcnode);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}
	
	
	public TypeNodeVO getOrgTypeByPK(String id) {
		IFuncRegisterService service = getFuncRegisterService();
		try {
			return service.getOrgTypeById(id);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public UIMeta getUIMeta(String folderPath, PageMeta pm, String widgetId) {
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
//		try {
			return pmProvider.getUIMeta(folderPath, pm, widgetId);
//		} catch (LfwBusinessException e) {
//			e.printStackTrace();
//		}
//		return null;
	}
	
	public void saveUIMeta(UIMeta meta, String pmPath, String folderPath){
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
//		try {
			pmProvider.saveUIMeta(meta, pmPath, folderPath);
//		} catch (LfwBusinessException e) {
//			e.printStackTrace();
//		}
//		return null;
	}

	public void saveRefNodetoXml(String rootPath, String filePath,
			String fileName, IRefNode refnode) {
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		pmProvider.saveRefNodeToXml(rootPath, filePath, fileName, refnode);
	}

	
	public void registerMenuItems(List<MenuItem> menuItems, String funnode){
		IFuncRegisterService service = getFuncRegisterService();
		try {
			service.registerMenuItems(menuItems, funnode);
		} 
		catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	public String[] registerBillAction(String billType, String[] actions) {
		IFuncRegisterService service = getFuncRegisterService();
		try {
			return service.registerBillAction(billType, actions);
		} 
		catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}


	public String getVersion() {
		IFuncRegisterService service = getFuncRegisterService();
		return service.getVersion();
	}
	
	public Map<String, String>[] getPageNames(String[] projPaths){
		IFuncRegisterService service = getFuncRegisterService();
		return service.getPageNames(projPaths);
	}
	
//	public PageFlow[][] getPageFlowDef(String[] projPaths){
//		IFuncRegisterService service = getFuncRegisterService();
//		return service.getPageFlowDef(projPaths);
//	}
//	
//	public void pageFlowToXMl(String projectPath, PageFlow pageFlow){
//		IFuncRegisterService service = getFuncRegisterService();
//		service.pageFlowToXMl(projectPath, pageFlow);
//	}
	
	public void refresh(){
		IFuncRegisterService service = getFuncRegisterService();
		service.refresh();
	}
	
//	public void saveFormComptoXml(String filePath, String fileName, StyleFormComp form){
//		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
//		pmProvider.saveFormCompToXml(filePath, fileName, form);
//	}
//	
//	public StyleFormComp getFormCompFromXml(String filePath, String fileName){
//		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
//		return pmProvider.getFormCompFromXml(filePath, fileName);
//	}

	public String generateRefNodeClass(String refType, String modelClass, String tableName,
			String refPk, String refCode, String refName, String visibleFields,
			String childfield, String childfield2) {
		IGeneratorCode generator = getCodeGenerator();
		try {
			  return generator.generateRefNodeClass(refType, modelClass, tableName, refPk, refCode, refName, visibleFields, childfield, childfield2);
		} 
		catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
			return "";
		}
	}

	public BCPManifest getMenifest(String filePath) {
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		return pmProvider.getMenifest(filePath);
	}

	public LfwWidget getMdDsFromComponent(LfwWidget widget, String componetId) {
		Properties props = new  Properties();
		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		IDatasetProvider dataProvider = NCLocator.getInstance(props).lookup(IDatasetProvider.class);
		try {
			return dataProvider.getMdDsFromComponent(widget, componetId);
		} catch (LfwBusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return widget;
	}

	public void removeWidgetFromPool(String rootPath, LfwWidget widget) {
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		pmProvider.removeWidgetFromPool(rootPath, widget);
	}

	public void saveWidgettoXml(String filePath, String fileName,
		String projectPath, LfwWidget widget, String rootPath) {
		IPageMetaProviderForDesign pmProvider = getPageMetaProvider();
		pmProvider.saveWidgetToXml(filePath, fileName, projectPath, widget, rootPath);
	}

}
