/**
 * 
 */
package nc.uap.lfw.core.uimodel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.IListenerSupport;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.conf.Model;
import nc.uap.lfw.design.itf.MdComponnetVO;
import nc.uap.lfw.design.itf.MdModuleVO;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * 
 * UAPWEB服务传输对象
 * @author chouhl
 *
 */
public class AMCServiceObj extends WebElement implements IListenerSupport, Cloneable, Serializable{

	private static final long serialVersionUID = 5332817671938821265L;
	
	public static final int DefaultServiceListenerTemplate = 0;
	public static final int ApplicationServiceListenerTemplate = 1;
	public static final int WindowServiceListenerTemplate = 2;
	public static final int ViewServiceListenerTemplate = 3;
	
	
	public static final int CreateApplicationXml = 1;
	public static final int CreateModelXml = 2;
	public static final int CreateWindowXml = 3;
	public static final int CreateViewXml = 4;
	public static final int UpdateApplicationXml = 11;
	public static final int UpdateWindowXml = 12;
	public static final int UpdateViewRefIdXml = 13;
	public static final int UpdateWidgetXml = 14;
	public static final int UpdateAppXmlAndController = 21;
	public static final int UpdateWinXmlAndController = 22;
	public static final int UpdateViewXmlAndController = 23;
	public static final int SavePageMetaAndUIMetaFromSessionCache = 31;
	public static final int SaveWidgetAndUIMetaFromSessionCache = 32;
	public static final int CreateUIMeta = 99;
	/**
	 * 操作元素XML文件类型
	 */
	private int operateWebElementXMLType;
	
	
	public static final int GetWindowWithWidget = 1;
	public static final int GetAppWindowList = 2;
	public static final int GetApplication = 3;
	public static final int GetAMCNames = 4;
	public static final int BuildPageMeta = 5;
	public static final int BuildWidget = 6;
	public static final int BuildPageMetaAndWidget = 7;
	public static final int GetWindow = 8;
	public static final int GetView = 9;
	public static final int GetUIMeta = 10;
	public static final int GetCacheWindows = 11;
	public static final int UpdateSessionCacheForView = 21;
	public static final int GetPageMetaFromSessionCache = 31;
	public static final int SetPageMetaToSessionCache = 32;
	public static final int GetElementFromSessionCache = 33;
	public static final int ClearSession = 99;
	/**
	 * 操作元素类型
	 */
	private int operateWebElement;
	
	
	public static final int GetExistModels = 1;
	public static final int GetExistComponentIds = 2;
	/**
	 * 操作VO类型
	 */
	private int operateVO;
	/******************元素************************/
	
	/**
	 * Application元素
	 */
	private Application appConf;
	/**
	 * Model元素
	 */
	private Model model;
	/**
	 * Window元素
	 */
	private PageMeta pageMeta;
//	/**
//	 * Window元素
//	 */
//	private PageMeta pmConfig;
//	/**
//	 * Window元素
//	 */
//	private PageMeta window;
	/**
	 * PageMeta元素集合
	 */
	private List<PageMeta> pmList;
//	/**
//	 * PageMetaConfig元素集合
//	 */
//	private List<PageMeta> pmcList;
//	/**
//	 * Window元素集合
//	 */
//	private List<WindowConfig> winConfList;
	/**
	 * View元素
	 */
	private LfwWidget lfwWidget;
//	/**
//	 * View元素
//	 */
//	private LfwWidget viewConf;
	/**
	 * UIMeta元素
	 */
	private UIMeta uimeta;
	/*******************VO***********************/
	
	private List<MdModuleVO> moduleVOList;
	
	private List<MdComponnetVO> componentVOList;
	
	/******************************************/
	/**
	 * 工程路径集合
	 */
	private String[] projPaths;
	/**
	 * 节点类型
	 */
	private String itemType;
	/**
	 * 节点路径
	 */
	private String amcNodePath;
	/**
	 * 文件后缀名
	 */
	private String suffix;
	/**
	 * 标签名称
	 */
	private String tagName;
	/**
	 * 当前工程路径
	 */
	private String currentProjPath;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * ViewID
	 */
	private String viewId;
	/**
	 * 引用ID
	 */
	private String refId;
	/**
	 * rootPath
	 */
	private String rootPath;
	/**
	 * 
	 */
	private Map<String, Object> paramMap;
	/**
	 * 
	 */
	private Map<String, String> userInfoMap;
	/**
	 * 
	 */
	private Map<String, String>[] webElementNames;
	/**
	 * 上下文
	 */
	private String ctxPath;
//	/**
//	 * 是否流式布局
//	 */
//	private boolean isFlowlayout = true; 
	/**************ListenerClassFile*******************/
	public final static int CreateListenerClassFile = 0;
	public final static int AddEventMethod = 1;
	public final static int DeleteEventMethod = 2;
	/**
	 * 操作ListenerClassFile类型(0-创建ListenerClass文件，1-添加事件方法，2-删除事件方法)
	 */
	private int operateListenerType;
	/**
	 * ListenerClass模板类型
	 */
	private int listenerTemplateType;
	/**
	 * 基本事件方法名称
	 */
	private String baseEventMethodName;
	/**
	 * 事件方法名称
	 */
	private String eventMethodName;
	/**
	 * 源文件夹
	 */
	private String sourcePackage;
	/**
	 * 包名
	 */
	private String packageName;
	/**
	 * 类名
	 */
	private String className;
	/**
	 * 类文件路径
	 */
	private String classFilePath;
	/**
	 * 类文件名称
	 */
	private String classFileName;
	/**
	 * 事件方法描述
	 */
	private List<JsEventDesc> descList;
	/**
	 * 事件
	 */
	private Map<String,List<EventConf>> eventsMap;
	/**
	 * 导入类集合
	 */
	private List<String> importNames;
	/**
	 * UIElement
	 */
	private Map<String, UIElement> elementMap;
	
	/*********************************/
	/**
	 * SESSIONID
	 */
	private String sessionId;
	/**
	 * 操作是否成功
	 */
	private boolean isSuccess = true;
	/**
	 *错误信息 
	 */
	private String errorMessage;

	private boolean createUIMeta = true;
	
	public String[] getProjPaths() {
		return projPaths;
	}
	public void setProjPaths(String[] projPaths) {
		this.projPaths = projPaths;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getAmcNodePath() {
		return amcNodePath;
	}
	public void setAmcNodePath(String amcNodePath) {
		this.amcNodePath = amcNodePath;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getCurrentProjPath() {
		return currentProjPath;
	}
	public void setCurrentProjPath(String currentProjPath) {
		this.currentProjPath = currentProjPath;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getListenerTemplateType() {
		return listenerTemplateType;
	}
	public void setListenerTemplateType(int listenerTemplateType) {
		this.listenerTemplateType = listenerTemplateType;
	}
	public int getOperateListenerType() {
		return operateListenerType;
	}
	public void setOperateListenerType(int operateListenerType) {
		this.operateListenerType = operateListenerType;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Application getAppConf() {
		return appConf;
	}
	public void setAppConf(Application appConf) {
		this.appConf = appConf;
	}
	public String getClassFilePath() {
		return classFilePath;
	}
	public void setClassFilePath(String classFilePath) {
		this.classFilePath = classFilePath;
	}
	public String getClassFileName() {
		return classFileName;
	}
	public void setClassFileName(String classFileName) {
		this.classFileName = classFileName;
	}
	public int getOperateWebElementXMLType() {
		return operateWebElementXMLType;
	}
	public void setOperateWebElementXMLType(int operateWebElementXMLType) {
		this.operateWebElementXMLType = operateWebElementXMLType;
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public PageMeta getPageMeta() {
		return pageMeta;
	}
	public void setPageMeta(PageMeta pageMeta) {
		this.pageMeta = pageMeta;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public int getOperateWebElement() {
		return operateWebElement;
	}
	public void setOperateWebElement(int operateWebElement) {
		this.operateWebElement = operateWebElement;
	}
	public Map<String, Object> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	public Map<String, String> getUserInfoMap() {
		return userInfoMap;
	}
	public void setUserInfoMap(Map<String, String> userInfoMap) {
		this.userInfoMap = userInfoMap;
	}
//	public PageMeta getWindow() {
//		return window;
//	}
//	public void setWindow(PageMeta winConf) {
//		this.window = winConf;
//	}
//	public List<WindowConfig> getWinConfList() {
//		return winConfList;
//	}
//	public void setWinConfList(List<WindowConfig> winConfList) {
//		this.winConfList = winConfList;
//	}
	public Map<String, String>[] getWebElementNames() {
		return webElementNames;
	}
	public void setWebElementNames(Map<String, String>[] webElementNames) {
		this.webElementNames = webElementNames;
	}
	public String getViewId() {
		return viewId;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
//	public PageMeta getPmConfig() {
//		return pmConfig;
//	}
//	public void setPmConfig(PageMeta pmConfig) {
//		this.pmConfig = pmConfig;
//	}
	public LfwWidget getLfwWidget() {
		return lfwWidget;
	}
	public void setLfwWidget(LfwWidget lfwWidget) {
		this.lfwWidget = lfwWidget;
	}
	public int getOperateVO() {
		return operateVO;
	}
	public void setOperateVO(int operateVO) {
		this.operateVO = operateVO;
	}
	public List<MdModuleVO> getModuleVOList() {
		return moduleVOList;
	}
	public void setModuleVOList(List<MdModuleVO> moduleVOList) {
		this.moduleVOList = moduleVOList;
	}
	public List<MdComponnetVO> getComponentVOList() {
		return componentVOList;
	}
	public void setComponentVOList(List<MdComponnetVO> componentVOList) {
		this.componentVOList = componentVOList;
	}
	public String getEventMethodName() {
		return eventMethodName;
	}
	public void setEventMethodName(String eventMethodName) {
		this.eventMethodName = eventMethodName;
	}
//	public LfwWidget getViewConf() {
//		return viewConf;
//	}
//	public void setViewConf(LfwWidget viewConf) {
//		this.viewConf = viewConf;
//	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String getBaseEventMethodName() {
		return baseEventMethodName;
	}
	public void setBaseEventMethodName(String baseEventMethodName) {
		this.baseEventMethodName = baseEventMethodName;
	}
	public List<JsEventDesc> getDescList() {
		return descList;
	}
	public void setDescList(List<JsEventDesc> descList) {
		this.descList = descList;
	}
	public List<String> getImportNames() {
		return importNames;
	}
	public void setImportNames(List<String> importNames) {
		this.importNames = importNames;
	}
	public String getSourcePackage() {
		return sourcePackage;
	}
	public void setSourcePackage(String sourcePackage) {
		this.sourcePackage = sourcePackage;
	}
	public String getCtxPath() {
		return ctxPath;
	}
	public void setCtxPath(String ctxPath) {
		this.ctxPath = ctxPath;
	}
	public Map<String, List<EventConf>> getEventsMap() {
		return eventsMap;
	}
	public void setEventsMap(Map<String, List<EventConf>> eventsMap) {
		this.eventsMap = eventsMap;
	}
	public List<PageMeta> getPmList() {
		return pmList;
	}
	public void setPmList(List<PageMeta> pmList) {
		this.pmList = pmList;
	}
//	public List<PageMeta> getPmcList() {
//		return pmcList;
//	}
//	public void setPmcList(List<PageMeta> pmcList) {
//		this.pmcList = pmcList;
//	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
//	public boolean isFlowlayout() {
//		return isFlowlayout;
//	}
//	public void setFlowlayout(boolean isFlowlayout) {
//		this.isFlowlayout = isFlowlayout;
//	}
	public UIMeta getUimeta() {
		return uimeta;
	}
	public void setUimeta(UIMeta uimeta) {
		this.uimeta = uimeta;
	}
	public Map<String, UIElement> getElementMap() {
		return elementMap;
	}
	public void setElementMap(Map<String, UIElement> elementMap) {
		this.elementMap = elementMap;
	}
	public boolean isCreateUIMeta() {
		return createUIMeta ;
	}
	public void setCreateUIMeta(boolean createUIMeta) {
		this.createUIMeta = createUIMeta;
	}
	
}
