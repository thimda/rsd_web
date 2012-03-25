/**
 * 
 */
package nc.uap.lfw.design.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.manager.PoolObjectManager;
import nc.uap.lfw.core.uimodel.AMCServiceObj;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.util.AMCUtil;
import nc.uap.lfw.core.util.AMCWebElementToXML;
import nc.uap.lfw.core.util.ApplicationNodeUtil;
import nc.uap.lfw.core.util.PageNodeUtil;
import nc.uap.lfw.design.itf.IAMCProviderForDesign;
import nc.uap.lfw.design.itf.IDatasetProvider;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.design.itf.MdComponnetVO;
import nc.uap.lfw.design.itf.MdModuleVO;
import nc.uap.lfw.jsp.parser.UIMetaParserUtil;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;

/**
 * 
 * UAPWEB服务接口实现类
 * @author chouhl
 *
 */
public class AMCProviderForDesignImpl implements IAMCProviderForDesign {
	
	/***************操作元素XML文件*******************/
	public AMCServiceObj operateWebElementXML(AMCServiceObj amcServiceObj){
		switch(amcServiceObj.getOperateWebElementXMLType()){
			//创建application.app文件和Controller类文件
			case AMCServiceObj.CreateApplicationXml:
				createControllerClassFile(amcServiceObj);
				createApplicationToXml(amcServiceObj);
				ApplicationNodeUtil.refreshApplication(amcServiceObj.getCtxPath(), amcServiceObj.getAppConf());
				break;
			//创建model.mod文件
			case AMCServiceObj.CreateModelXml:
				createModelToXml(amcServiceObj);
				break;
			//创建pagemeta.pm文件、uimeta.um文件和Controller类文件
			case AMCServiceObj.CreateWindowXml:
				createControllerClassFile(amcServiceObj);
				createWindowToXml(amcServiceObj);
				createUIMeta(amcServiceObj);
				PageNodeUtil.refreshAMC(amcServiceObj.getCtxPath(), amcServiceObj.getPageMeta());
				break;
			//创建widget.wd文件、uimeta.um文件和Controller类文件
			case AMCServiceObj.CreateViewXml:
				createControllerClassFile(amcServiceObj);
				createViewToXml(amcServiceObj);
				createUIMeta(amcServiceObj);
				break;
			case AMCServiceObj.UpdateWindowXml:
				updateWindowToXml(amcServiceObj);
				break;
			case AMCServiceObj.UpdateViewRefIdXml:
				updateViewRefIdToXml(amcServiceObj);
				break;
			case AMCServiceObj.UpdateWidgetXml:
				updateWidgetToXml(amcServiceObj);
				break;
			case AMCServiceObj.UpdateApplicationXml:
				upateApplicationToXml(amcServiceObj);
				break;
			case AMCServiceObj.UpdateAppXmlAndController:
				operateControllerEventMethod(amcServiceObj);
				upateApplicationToXml(amcServiceObj);
				break;
			case AMCServiceObj.UpdateWinXmlAndController:
				operateControllerEventMethod(amcServiceObj);
				updateWindowToXml(amcServiceObj);
				break;
			case AMCServiceObj.UpdateViewXmlAndController:
				operateControllerEventMethod(amcServiceObj);
				updateViewSessionCache(amcServiceObj);
				updateViewToXml(amcServiceObj);
				break;
			case AMCServiceObj.SavePageMetaAndUIMetaFromSessionCache:
				savePageMetaAndUIMetaFromSessionCache(amcServiceObj);
				break;
			case AMCServiceObj.SaveWidgetAndUIMetaFromSessionCache:
				saveWidgetAndUIMetaFromSessionCache(amcServiceObj);
				break;
			case AMCServiceObj.CreateUIMeta:
				createUIMeta(amcServiceObj);
				break;
			default:
				break;
		}
		return amcServiceObj;
	}
	
	private void createApplicationToXml(AMCServiceObj amcServiceObj) {
		AMCWebElementToXML.applicationToXml(amcServiceObj.getFilePath(), amcServiceObj.getFileName(), amcServiceObj.getCurrentProjPath(), amcServiceObj.getAppConf());
	}

	private void createModelToXml(AMCServiceObj amcServiceObj) {
		AMCWebElementToXML.modelToXml(amcServiceObj.getFilePath(), amcServiceObj.getFileName(), amcServiceObj.getCurrentProjPath(), amcServiceObj.getModel());
	}

	private void createWindowToXml(AMCServiceObj amcServiceObj){
		AMCWebElementToXML.windowToXml(amcServiceObj.getFilePath(), amcServiceObj.getFileName(), amcServiceObj.getCurrentProjPath(), amcServiceObj.getPageMeta());
	}
	
	private void createViewToXml(AMCServiceObj amcServiceObj){
		LfwRuntimeEnvironment.setRootPath(amcServiceObj.getRootPath());
		AMCWebElementToXML.widgetToXml(amcServiceObj.getFilePath(), amcServiceObj.getFileName(), amcServiceObj.getCurrentProjPath(), amcServiceObj.getLfwWidget());
	}
	
	private void updateViewRefIdToXml(AMCServiceObj amcServiceObj){
		LfwRuntimeEnvironment.setRootPath(amcServiceObj.getRootPath());
		AMCWebElementToXML.viewToXml(amcServiceObj.getFilePath(), amcServiceObj.getFileName(), amcServiceObj.getCurrentProjPath(), amcServiceObj.getRefId());		
	}
	
	private void updateViewToXml(AMCServiceObj amcServiceObj){
		LfwRuntimeEnvironment.setRootPath(amcServiceObj.getRootPath());
		AMCWebElementToXML.widgetToXml(amcServiceObj.getFilePath(), amcServiceObj.getFileName(), amcServiceObj.getCurrentProjPath(), amcServiceObj.getLfwWidget());
	}
	
	private void updateWidgetToXml(AMCServiceObj amcServiceObj){
		LfwRuntimeEnvironment.setRootPath(amcServiceObj.getRootPath());
		AMCWebElementToXML.widgetToXml(amcServiceObj.getFilePath(), amcServiceObj.getFileName(), amcServiceObj.getCurrentProjPath(), amcServiceObj.getLfwWidget());
	}
	
	private void upateApplicationToXml(AMCServiceObj amcServiceObj){
		AMCWebElementToXML.applicationToXml(amcServiceObj.getFilePath(), amcServiceObj.getFileName(), amcServiceObj.getCurrentProjPath(), amcServiceObj.getAppConf());
	}
	
	private void updateWindowToXml(AMCServiceObj amcServiceObj){
		AMCWebElementToXML.windowToXml(amcServiceObj.getFilePath(), amcServiceObj.getFileName(), amcServiceObj.getCurrentProjPath(), amcServiceObj.getPageMeta());
	}
	
	private void savePageMetaAndUIMetaFromSessionCache(AMCServiceObj amcServiceObj){
		String sessionId = amcServiceObj.getSessionId();
		String pageMetaId = amcServiceObj.getPageMeta().getId();
		String nodePath = amcServiceObj.getAmcNodePath();
		PageMeta pageMeta = getInterfaceService(IPaEditorService.class).getOriPageMeta(pageMetaId, sessionId);
		UIMeta uimeta = getInterfaceService(IPaEditorService.class).getOriUIMeta(pageMetaId, sessionId);
		save(pageMeta, uimeta, null, nodePath);
	}
	
	private void saveWidgetAndUIMetaFromSessionCache(AMCServiceObj amcServiceObj){
		String sessionId = amcServiceObj.getSessionId();
		String pageMetaId = amcServiceObj.getPageMeta().getId();
		String widgetId = amcServiceObj.getLfwWidget().getId();
		String nodePath = amcServiceObj.getAmcNodePath();
		PageMeta pageMeta = getInterfaceService(IPaEditorService.class).getOriPageMeta(pageMetaId, sessionId);
		UIMeta uimeta = getInterfaceService(IPaEditorService.class).getOriUIMeta(pageMetaId, sessionId);
		if(widgetId != null){
			UIElement uiEle = uimeta.getElement();
			UIWidget uiWidget = null;
			UIMeta widgetMeta = null;
			if(uiEle != null && uiEle instanceof UIWidget){
				uiWidget = (UIWidget) uiEle;
				widgetMeta = uiWidget.getUimeta();
			}
			LfwWidget widget = pageMeta.getWidget(widgetId);
			if(widget != null){
				LfwRuntimeEnvironment.setRootPath(amcServiceObj.getRootPath());
				save(pageMeta, widgetMeta, widget, nodePath);
			}
		}else{
			throw new LfwRuntimeException(AMCProviderForDesignImpl.class.getName() + ":WidgetID为空");
		}
	}
	
	private void createUIMeta(AMCServiceObj amcServiceObj){
//		boolean create = amcServiceObj.isCreateUIMeta();
//		if(!create)
//			return;
//		String fp = amcServiceObj.getFilePath().replaceAll("\\\\", "/");
//		String id = fp.substring(fp.lastIndexOf("/") + 1) + "_um";
//		UIMeta meta = new UIMeta();
////		meta.setAttribute(UIMeta.ISCHART, 0);
////		meta.setAttribute(UIMeta.ISJQUERY, 0);
////		meta.setAttribute(UIMeta.ISEXCEL, 0);
////		meta.setAttribute(UIMeta.JSEDITOR, 0);
//		meta.setAttribute(UIMeta.ID, id);
//		meta.setFlowmode(amcServiceObj.isFlowlayout());
		UIMeta meta = amcServiceObj.getUimeta();
		if(meta == null)
			return;
		AMCWebElementToXML.createUIMeta(amcServiceObj.getFilePath(), meta);
	}
	/***************操作元素*******************/
	public AMCServiceObj operateWebElement(AMCServiceObj amcServiceObj){
		switch(amcServiceObj.getOperateWebElement()){
			case AMCServiceObj.GetWindowWithWidget:
				amcServiceObj.setPageMeta(getWindowWithWidget(amcServiceObj));
				break;
			case AMCServiceObj.GetAppWindowList:
				amcServiceObj.setPmList(getAppWindowList(amcServiceObj));
				break;
			case AMCServiceObj.GetApplication:
				amcServiceObj.setAppConf(getApplication(amcServiceObj));
				break;
			case AMCServiceObj.GetWindow:
				amcServiceObj.setPageMeta(getWindow(amcServiceObj));
				break;
			case AMCServiceObj.GetView:
				amcServiceObj.setLfwWidget(getView(amcServiceObj));
				break;
			case AMCServiceObj.GetAMCNames:
				amcServiceObj.setWebElementNames(getAMCNames(amcServiceObj));
				break;
			case AMCServiceObj.BuildPageMeta:
				amcServiceObj.setPageMeta(buildPageMeta(amcServiceObj));
				break;
			case AMCServiceObj.BuildPageMetaAndWidget:
				amcServiceObj.setPageMeta(buildPageMetaAndWidget(amcServiceObj));
				break;
			case AMCServiceObj.BuildWidget:
				amcServiceObj.setLfwWidget(buildWidget(amcServiceObj));
				break;
			case AMCServiceObj.GetCacheWindows:
				amcServiceObj.setPmList(getCacheWindows(amcServiceObj));
				break;
			case AMCServiceObj.UpdateSessionCacheForView:
				updateViewSessionCache(amcServiceObj);
				break;
			case AMCServiceObj.ClearSession:
				clearSessionCache(amcServiceObj);
				break;
			case AMCServiceObj.GetPageMetaFromSessionCache:
				amcServiceObj.setPageMeta(getPageMetaFromSessionCache(amcServiceObj));
				break;
			case AMCServiceObj.SetPageMetaToSessionCache:
				setPageMetaToSessionCache(amcServiceObj);
				break;
			case AMCServiceObj.GetUIMeta:
				amcServiceObj.setUimeta(getUIMeta(amcServiceObj));
				break;
			case AMCServiceObj.GetElementFromSessionCache:
				amcServiceObj.setPageMeta(getPageMetaFromSessionCache(amcServiceObj));
				amcServiceObj.setUimeta(getUIMetaFromSessionCache(amcServiceObj));
				break;
			default:
				amcServiceObj.setErrorMessage("此操作类型不存在:" + amcServiceObj.getOperateWebElement());
				amcServiceObj.setSuccess(false);
				throw new LfwRuntimeException("此操作类型不存在:" + amcServiceObj.getOperateWebElement());
		}
		return amcServiceObj;
	}
	
	private static WindowBuilderForDesign builder = new WindowBuilderForDesign();
	
	private PageMeta getWindowWithWidget(AMCServiceObj amcServiceObj){
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		try{
			Class<?> c = Class.forName("nc.uap.lfw.compatible.NCLoginUtil");
			Method m = c.getMethod("doNCLogin", new Class[]{Map.class});
			m.invoke(null, new Object[]{amcServiceObj.getUserInfoMap()});
		}catch(Exception e){
			LfwLogger.error(e.getMessage(), e);
		}
		return builder.buildWindowAndWidget(amcServiceObj.getParamMap());
	}
	
	private List<PageMeta> getAppWindowList(AMCServiceObj amcServiceObj){
		return AMCUtil.getAppWindowList(amcServiceObj.getFilePath(), amcServiceObj.getFileName());
	}
	
	private Application getApplication(AMCServiceObj amcServiceObj){
		return AMCUtil.getApplication(amcServiceObj.getFilePath(), amcServiceObj.getFileName());
	}
	
	private PageMeta getWindow(AMCServiceObj amcServiceObj){
		return AMCUtil.getWindow(amcServiceObj.getFilePath(), amcServiceObj.getFileName());
	}
	
	private LfwWidget getView(AMCServiceObj amcServiceObj){
		return AMCUtil.getView(amcServiceObj.getFilePath(), amcServiceObj.getFileName());
	}
	
	private Map<String, String>[] getAMCNames(AMCServiceObj amcServiceObj){
		return AMCUtil.getAMCNames(amcServiceObj.getProjPaths(), amcServiceObj.getAmcNodePath(), amcServiceObj.getSuffix(), amcServiceObj.getTagName());
	}
	
	private static PageMetaBuilderForDesign pmbuilder = new PageMetaBuilderForDesign();
	
	private PageMeta buildPageMeta(AMCServiceObj amcServiceObj){
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		return pmbuilder.buildPageMeta(amcServiceObj.getParamMap());
	}
	
	private LfwWidget buildWidget(AMCServiceObj amcServiceObj){
		LfwRuntimeEnvironment.setMode(WebConstant.MODE_DESIGN);
		return pmbuilder.buildWidget(amcServiceObj.getParamMap(), amcServiceObj.getViewId());
	}
	
	private PageMeta buildPageMetaAndWidget(AMCServiceObj amcServiceObj){
		return pmbuilder.buildPageMetaAndWidget(amcServiceObj.getParamMap());
	}
	
	private List<PageMeta> getCacheWindows(AMCServiceObj amcServiceObj){
		List<PageMeta> list = new ArrayList<PageMeta>();
		Map<String, PageMeta> cacheMap = PageNodeUtil.getCacheMetaMap(amcServiceObj.getCtxPath());
		if(cacheMap != null){
			Iterator<String> ids  = cacheMap.keySet().iterator();
			while(ids.hasNext()){
				PageMeta pmConf = cacheMap.get(ids.next()); 
				if(pmConf.getWindowType() != null &&pmConf.getWindowType().equals("win")){
					PageMeta winConf = new PageMeta();
					winConf.setId(pmConf.getId());
					winConf.setCaption(pmConf.getCaption());
					list.add(winConf);
				}
			}
		}
		return list;
	}
	
	private void updateViewSessionCache(AMCServiceObj amcServiceObj){
		if(amcServiceObj.getSessionId() != null){
			PageMeta pm = amcServiceObj.getLfwWidget().getPagemeta();
			String pageMetaId = null;
			if(pm != null){
				pageMetaId = pm.getId();
			}else{
				pageMetaId = "defaultWindowId";
			}
			IPaEditorService pa = getInterfaceService(IPaEditorService.class);
			PageMeta oriPm = pa.getOriPageMeta(pageMetaId, amcServiceObj.getSessionId());
			if(oriPm != null){
				LfwWidget oriWidget = oriPm.getWidget(amcServiceObj.getLfwWidget().getId());
				AMCUtil.mergeViewEvents(amcServiceObj.getLfwWidget(), oriWidget);
				amcServiceObj.setLfwWidget(oriWidget);
			}
			UIMeta oriUIMeta = pa.getOriUIMeta(pageMetaId, amcServiceObj.getSessionId());
			if(oriUIMeta.getElement() instanceof UIWidget){
				UIMeta viewOriMeta = ((UIWidget)oriUIMeta.getElement()).getUimeta();
				AMCUtil.mergeViewUIMetaEvents(amcServiceObj.getUimeta(), viewOriMeta, amcServiceObj.getElementMap());
				amcServiceObj.setUimeta(viewOriMeta);
			}
		}
	}
	/**
	 * 创建Controller类文件
	 * @param amcServiceObj
	 */
	private void createControllerClassFile(AMCServiceObj amcServiceObj){
		String classFilePath = amcServiceObj.getClassFilePath();
		if(classFilePath == null || classFilePath.equals(""))
			return;
		File javaDir = new File(amcServiceObj.getClassFilePath());
		if(!javaDir.exists()){
			javaDir.mkdirs();
		}
		File javaFile = new File(amcServiceObj.getClassFilePath() + File.separator + amcServiceObj.getClassFileName());
		LfwLogger.error("创建类文件路径：" + javaFile.getAbsolutePath());
		if(javaFile.exists()){
			LfwLogger.error("创建类文件路径：" + javaFile.getAbsolutePath() + "文件已存在");
			return;
		}else{
			try {
				javaFile.createNewFile();
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
				amcServiceObj.setSuccess(false);
				amcServiceObj.setErrorMessage("创建类文件路径：" + javaFile.getAbsolutePath() + "文件创建失败");
				return;
			}
		}
		FileWriter fileWriter = null;
		try {
			String clazzContent = AMCUtil.getControllerClazz(amcServiceObj.getOperateWebElementXMLType(), amcServiceObj.getPackageName(), amcServiceObj.getClassName());
			if(clazzContent != null && clazzContent.trim().length() > 0){
				fileWriter = new FileWriter(javaFile);
				fileWriter.write(clazzContent);
				fileWriter.flush();
			}
		} catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
			amcServiceObj.setSuccess(false);
			amcServiceObj.setErrorMessage("创建类文件路径：" + javaFile.getAbsolutePath() + "文件写入失败");
		} finally{
			if(fileWriter != null){
				try {
					fileWriter.close();
				} catch (IOException e) {
					LfwLogger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	private void operateControllerEventMethod(AMCServiceObj amcServiceObj){
		Map<String, List<EventConf>> eventsMap = amcServiceObj.getEventsMap();
		if(eventsMap == null){
			return;
		}
		String path = null;
		Iterator<String> keys = eventsMap.keySet().iterator();
		while(keys.hasNext()){
			path = keys.next();
			List<EventConf> eventList = eventsMap.get(path);
			String clazzContent = AMCUtil.operateMethod(path, eventList);
			File javaFile = new File(path);
			LfwLogger.error("类文件路径：" + javaFile.getAbsolutePath());
			FileWriter fileWriter = null;
			try {
				if(clazzContent != null && clazzContent.trim().length() > 0){
					fileWriter = new FileWriter(javaFile);
					fileWriter.write(clazzContent);
					fileWriter.flush();
				}
			} catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
				amcServiceObj.setSuccess(false);
				amcServiceObj.setErrorMessage(javaFile.getAbsolutePath() + "文件修改失败");
			}finally{
				if(fileWriter != null){
					try {
						fileWriter.close();
					} catch (IOException e) {
						LfwLogger.error(e.getMessage(), e);
					}
				}
			}
		}
	}
	private void clearSessionCache(AMCServiceObj amcServiceObj){
		getInterfaceService(IPaEditorService.class).clearSessionCache(amcServiceObj.getSessionId());
	}
	/**
	 * 从session获取PageMeta
	 * @param pageMetaId
	 * @param sessionId
	 */
	private PageMeta getPageMetaFromSessionCache(AMCServiceObj amcServiceObj){
		return getInterfaceService(IPaEditorService.class).getOriPageMeta(amcServiceObj.getPageMeta().getId(), amcServiceObj.getSessionId());
	}
	/**
	 * 保存PageMeta到session
	 * @param amcServiceObj
	 */
	private void setPageMetaToSessionCache(AMCServiceObj amcServiceObj){
		PageMeta pageMeta = getInterfaceService(IPaEditorService.class).getOriPageMeta(amcServiceObj.getPageMeta().getId(), amcServiceObj.getSessionId());
		pageMeta = amcServiceObj.getPageMeta();
		amcServiceObj.setPageMeta(pageMeta);
	}
	/**
	 * 获取UIMeta
	 * @param amcServiceObj
	 * @return
	 */
	private UIMeta getUIMeta(AMCServiceObj amcServiceObj){
		UIMetaParserUtil uimetaParser = new UIMetaParserUtil();
		uimetaParser.setPagemeta(amcServiceObj.getPageMeta());
		return uimetaParser.parseUIMeta(amcServiceObj.getFilePath(), null);
	}
	
	/**
	 * 从session中获取UIMeta
	 * @param amcServiceObj
	 * @return
	 */
	private UIMeta getUIMetaFromSessionCache(AMCServiceObj amcServiceObj){
		return getInterfaceService(IPaEditorService.class).getOriUIMeta(amcServiceObj.getPageMeta().getId(), amcServiceObj.getSessionId());
	}
	
	/***************操作VO*******************/
	public AMCServiceObj operateVO(AMCServiceObj amcServiceObj){
		switch(amcServiceObj.getOperateVO()){
			case AMCServiceObj.GetExistComponentIds:
				amcServiceObj.setComponentVOList(getExistComponentIds(amcServiceObj));
				break;
			case AMCServiceObj.GetExistModels:
				amcServiceObj.setModuleVOList(getExistModels(amcServiceObj));
				break;
			default:
				break;
		}
		return amcServiceObj;
	}
	
	private List<MdModuleVO> getExistModels(AMCServiceObj amcServiceObj){
		List<MdModuleVO> existList = new ArrayList<MdModuleVO>();
		IDatasetProvider dataProvider = getInterfaceService(IDatasetProvider.class);
		try {
			List<?> componentList = dataProvider.getAllComponents();
			Map<String, String> map = AMCUtil.getExistComponentIds(amcServiceObj.getCurrentProjPath(), amcServiceObj.getAmcNodePath(), amcServiceObj.getSuffix(), amcServiceObj.getTagName());
			if(map != null){
				for(int i=0;i<componentList.size();i++){
					if(map.containsKey(((MdComponnetVO)componentList.get(i)).getId())){
						map.put(((MdComponnetVO)componentList.get(i)).getOwnmodule(), ((MdComponnetVO)componentList.get(i)).getOwnmodule());
					}
				}
				List<?> moduleList = dataProvider.getALlModuels();
				for(int i=0;i<moduleList.size();i++){
					if(map.containsKey(((MdModuleVO)moduleList.get(i)).getId())){
						existList.add((MdModuleVO)moduleList.get(i));
					}
				}
			}
		} catch (LfwBusinessException e) {
			LfwLogger.error(e);
		}
		return existList;
	}
	
	public List<MdComponnetVO> getExistComponentIds(AMCServiceObj amcServiceObj){
		List<MdComponnetVO> existList = new ArrayList<MdComponnetVO>();
		try {
			List<?> componentList = getInterfaceService(IDatasetProvider.class).getAllComponents();
			Map<String, String> map = AMCUtil.getExistComponentIds(amcServiceObj.getCurrentProjPath(), amcServiceObj.getAmcNodePath(), amcServiceObj.getSuffix(), amcServiceObj.getTagName());
			if(map != null){
				for(int i=0;i<componentList.size();i++){
					if(map.containsKey(((MdComponnetVO)componentList.get(i)).getId())){
						((MdComponnetVO)componentList.get(i)).setDisplayId(map.get(((MdComponnetVO)componentList.get(i)).getId()));
						existList.add((MdComponnetVO)componentList.get(i));
					}
				}
			}
		} catch (LfwBusinessException e) {
			LfwLogger.error(e);
		}
		return existList;
	}
	
	public void save(PageMeta pm, UIMeta meta, LfwWidget widget, String nodePath){
		if(widget == null){//保存Window
			PageMeta clone = (PageMeta)pm.clone();
			clone.setProcessorClazz("nc.uap.lfw.core.event.AppRequestProcessor");
			AMCWebElementToXML.windowToXml(nodePath, "pagemeta.pm", null, clone);
			AMCWebElementToXML.createUIMeta(nodePath, meta);
			return;
		}
		else{//保存View
			AMCWebElementToXML.widgetToXml(nodePath, "widget.wd", null, widget);
			AMCWebElementToXML.createUIMeta(nodePath, meta);
			//事件
			EventConf[] events = AMCUtil.getAllEvents(widget);
			Map<String, List<EventConf>> eventsMap = null;
			if(events != null && events.length > 0){
				eventsMap = new HashMap<String, List<EventConf>>();
				for(EventConf event : events){
					if(event.getClassFilePath() != null && event.getClassFileName() != null){
						List<EventConf> list = eventsMap.get(event.getClassFilePath() + File.separator + event.getClassFileName());
						if(list == null){
							list = new ArrayList<EventConf>();
						}
						list.add(event);
						eventsMap.put(event.getClassFilePath() + File.separator + event.getClassFileName(), list);
					}
				}
			}
			EventConf[] events1 = meta.getEventConfs();
			if(events1 != null && events1.length > 0){
				if(eventsMap == null){
					eventsMap = new HashMap<String, List<EventConf>>();
				}
				for(EventConf event : events1){
					if(event.getClassFilePath() != null && event.getClassFileName() != null){
						List<EventConf> list = eventsMap.get(event.getClassFilePath() + File.separator + event.getClassFileName());
						if(list == null){
							list = new ArrayList<EventConf>();
						}
						list.add(event);
						eventsMap.put(event.getClassFilePath() + File.separator + event.getClassFileName(), list);
					}
				}
			}
			AMCServiceObj amcServiceObj = new AMCServiceObj();
			amcServiceObj.setEventsMap(eventsMap);
			operateControllerEventMethod(amcServiceObj);
			if(events != null && events.length > 0){
				for(EventConf event : events){
					if(event.getEventStatus() == EventConf.ADD_STATUS){
						event.setEventStatus(EventConf.NORMAL_STATUS);
					}else if(event.getEventStatus() == EventConf.DEL_STATUS){
						AMCUtil.removeEvent(widget, event.getName(), event.getMethodName());
					}
				}
			}
			if(events1 != null && events1.length > 0){
				List<EventConf> list = new ArrayList<EventConf>();
				for(EventConf event : events1){
					if(event.getEventStatus() == EventConf.ADD_STATUS){
						event.setEventStatus(EventConf.NORMAL_STATUS);
						list.add(event);
					}else if(event.getEventStatus() == EventConf.NORMAL_STATUS){
						list.add(event);
					}
				}
				meta.removeAllEventConf();
				for(EventConf event : list){
					meta.addEventConf(event);
				}
			}
			if(widget.getExtendAttribute(LfwWidget.POOLWIDGET) != null){			
				PoolObjectManager.refreshWidgetPool(LfwRuntimeEnvironment.getRootPath(), widget);
			}
		}
	}
	
	/**
	 * 获取接口服务 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	private static <T> T getInterfaceService(Class<T> clazz){
//		Properties props = new  Properties();
//		props.setProperty(NCLocator.SERVICEDISPATCH_URL, "http://127.0.0.1:80/ServiceDispatcherServlet");
		return NCLocator.getInstance().lookup(clazz);
	}
	
}
