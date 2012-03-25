package nc.uap.lfw.core.importer;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.LfwTheme;
import nc.uap.lfw.core.LfwThemeManager;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.model.AppTypeUtil;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.core.refnode.RefNodeRelations;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.util.StringUtil;

import org.apache.commons.lang.StringUtils;


/**
 * 智能依赖引入工具类
 * @author dengjt
 *
 */
public final class SmartImporter {
	/**
	 * 所有lib的缓存字符串,非优化
	 */
	private String libArrStr;
	/**
	 * 所有lib的缓存字符串,优化
	 */
	private String optimizedLibArrStr;
	private PageMeta pm;
	public String genImporters(PageMeta pm, UIMeta um, boolean optimized){
		this.pm = pm;
		StringBuffer scriptBuf = new StringBuffer();
		StringBuffer cssBuf = new StringBuffer();
		StringBuffer headBuf = new StringBuffer();
		genCommonHeader(headBuf, scriptBuf, cssBuf);
		genCommonImporter(pm, um, scriptBuf, cssBuf, optimized);
		return headBuf.toString() + cssBuf.toString() + scriptBuf.toString();
		
	}
	
	private void genCommonImporter(PageMeta pm, UIMeta um, StringBuffer scriptBuf, StringBuffer cssBuf, boolean optimized) {
		LinkedHashSet<Importer> importList = new LinkedHashSet<Importer>();
		LinkedHashSet<Importer> cssImportList = new LinkedHashSet<Importer>();
		Set<String> idSet = new HashSet<String>();
		String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
		boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
		if(editMode)
			loadLib("coreedit", importList, cssImportList, idSet, optimized);
		else
			loadLib("core", importList, cssImportList, idSet, optimized);
		
		String appType = AppTypeUtil.getApplicationType();
		if(appType.equals(AppTypeUtil.APP_TYPE))
			loadLib("app", importList, cssImportList, idSet, optimized);
		else
			loadLib("page", importList, cssImportList, idSet, optimized);
		
		loadLib("widget", importList, cssImportList, idSet, optimized);
		
		loadLib("contextmenu", importList, cssImportList, idSet, optimized);
		
		Integer jquery = null;
		if(um == null)
			jquery = 0;
		else
			jquery = um.isJquery();
		if(jquery != null && jquery.intValue() == 1)
			loadLib("jquery", importList, cssImportList, idSet, optimized);
		
		if(um != null && um.isReference() != null && um.isReference() == 1){
//			importList.add(new Importer("ui/ctrl/ref/refscript"));
//			importList.add(new Importer("ui/ctrl/ref/reftree"));
//			importList.add(new Importer("ui/ctrl/ref/refgrid"));
//			importList.add(new Importer("ui/ctrl/ref/refgridtree"));
			loadLib("reflib", importList, cssImportList, idSet, optimized);
		}
		
		getImportList(importList, cssImportList, idSet, um, pm, optimized);
		
		String frameDevice = "/frame/device_pc";
		boolean clientMode = LfwRuntimeEnvironment.isClientMode();
		String rootPath = LfwRuntimeEnvironment.getRootPath();
		if(clientMode)
			rootPath = ".." + rootPath;
		
		String themeId = LfwRuntimeEnvironment.getThemeId();
		LfwTheme theme = LfwThemeManager.getLfwTheme(rootPath, themeId);
		if(theme == null){
			throw new LfwRuntimeException("根据themeid找不到对应的theme," + themeId);
		}
		String themeCtxPath = theme.getCtxPath();
		if(clientMode)
			themeCtxPath = ".." + themeCtxPath;
		String currThemePath = themeCtxPath + frameDevice + "/themes/" + themeId;
		
		Iterator<Importer> cssIt = cssImportList.iterator();
		while(cssIt.hasNext()){
			Importer imp = cssIt.next();
			String fullName = imp.getFullname();
			String str = currThemePath + "/" + fullName;
			cssBuf.append("<link rel='STYLESHEET' type='text/css' href='" + str + ".css'>\n");
		}
		
		Iterator<Importer> it = importList.iterator();
		while(it.hasNext()){
			Importer imp = it.next();
			String ctxStr = imp.getCtx();
			String fullName = imp.getFullname();
			String str = "/" + ctxStr + frameDevice + "/script/" + fullName;
			scriptBuf.append("<script type='text/javascript' src='" + str + ".js'></script>\n");
		}

		String includeJs = null;
		String includeCss = null;
		String lfwIncludeJs = null;
		String lfwIncludeCss = null;
		String pageCss = null;
		if(um != null){
			includeJs = um.getIncudejs();
			includeCss = um.getIncudecss();
			lfwIncludeJs = um.getLfwIncudejs();
			lfwIncludeCss = um.getLfwIncudecss();
			pageCss = um.getPagecss();
		}
		
		addExtendJs(includeJs, scriptBuf);
		addExtendCss(includeCss, cssBuf);
		
		
		addPageCss(pageCss,scriptBuf);
		String cssInclude = includeSelfDefCss("include.css");
		cssBuf.append(cssInclude);
		String include = includeSelfDefScript("include.js");
		scriptBuf.append(include);

		addLfwExtendJs(lfwIncludeJs, scriptBuf);
		addLfwExtendCss(lfwIncludeCss, cssBuf);

		scriptBuf.append("<script>\n")
			.append("window.loadedLib = ")
			.append(generateLoadedLib(idSet))
			.append(";\n")
			.append("window.libArray = ")
			.append(generateAllLibArray(false))
			.append(";\n")
			.append("window.onload = function(){\n")
			.append("")
			.append("pageBodyScript();\n")
			.append("}\n")
			.append("</script>\n");

		genUserInfo(scriptBuf);
		
	}
	private void genUserInfo(StringBuffer scriptBuf){
		//打印用户
		String userPK = "";
		String userName = "";
		String userCode = "";
		LfwSessionBean lfwsession =  nc.uap.lfw.core.LfwRuntimeEnvironment.getLfwSessionBean();
		if(null != lfwsession){
			userName = lfwsession.getUser_name();
			userCode = lfwsession.getUser_code();
			userPK = lfwsession.getPk_user();
		}
		if(userPK == null) userPK = "";
		if(userName == null) userName = "";
		if(userCode == null) userCode = "";
		
		scriptBuf.append("<script>\n")
		.append("window.userpk = \"")
		.append(userPK)
		.append("\";\n")
		.append("window.usercode = \"")
		.append(userCode)
		.append("\";\n")
		.append("window.username = \"")
		.append(userName)
		.append("\";\n")		
		.append("</script>\n");
	}

	private void loadLib(String id, LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, Set<String> idSet, boolean optimized) {
		IControlPlugin cplugin = ControlFramework.getInstance().getControlPluginByType(id);
		doGetPluginImportList(importList, cssImportList, idSet, cplugin, optimized);
	}

	private void genCommonHeader(StringBuffer headBuf, StringBuffer scriptBuf, StringBuffer cssBuf) {
		String frameDevice = "/frame/device_pc";
		String langCode = LfwRuntimeEnvironment.getLangCode();
		if (langCode == null || langCode.isEmpty() || langCode.equals("null"))
			langCode = "simpchn";
		scriptBuf.append("<script type='text/javascript' src='/lfw" + frameDevice + "/script/ui/lang/ml_" + langCode + ".js'></script>\n");
		
		String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
		boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
		
		String lfwCtx = LfwRuntimeEnvironment.getLfwCtx();
		boolean clientMode = LfwRuntimeEnvironment.isClientMode();
		if(clientMode)
			lfwCtx = ".." + lfwCtx;
		String rootPath = LfwRuntimeEnvironment.getRootPath();
		if(clientMode)
			rootPath = ".." + rootPath;
		
		String themeId = LfwRuntimeEnvironment.getThemeId();
		LfwTheme theme = LfwThemeManager.getLfwTheme(rootPath, themeId);
		if(theme == null){
			throw new LfwRuntimeException("根据themeid找不到对应的theme," + themeId);
		}
		String themeCtxPath = theme.getCtxPath();
		if(clientMode)
			themeCtxPath = ".." + themeCtxPath;
		String currThemePath = themeCtxPath + frameDevice + "/themes/" + themeId;
		
		
		ServletContext ctx = LfwRuntimeEnvironment.getServletContext();
		String domain = (String) ctx.getAttribute(WebConstant.DOMAIN_KEY);
		headBuf.append("<script>\n\t\t\t\t");
		// 如果指定页面所处domain，则设置domain
		if (domain != null) {
			headBuf.append("document.domain=\"").append(domain).append("\";\n\t\t\t\t");
		}
		
		headBuf.append("window.globalPath = \"").append(rootPath).append("\";\n\t\t\t\t");
		headBuf.append("window.corePath = \"").append(LfwRuntimeEnvironment.getCorePath()).append("\";\n\t\t\t\t");
		headBuf.append("window.themeId = \"").append(themeId).append("\";\n\t\t\t\t");
		headBuf.append("window.baseGlobalPath = \"" + LfwRuntimeEnvironment.getLfwCtx() + "\";\n\t\t\t\t");
		headBuf.append("window.frameGlobalPath = window.baseGlobalPath + \"" + frameDevice + "/script\";\n\t\t\t\t");
		headBuf.append("window.themeGlobalPath = \"" + themeCtxPath + "\";\n\t\t\t\t");
		headBuf.append("window.themePath = \"").append(currThemePath).append("\";\n\t\t\t\t");
		headBuf.append("window.JSessionID = \"" + LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId() + "\";\n\t\t\t\t");

		if(clientMode){
			String offlineCachePath = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.OFFLINE_CACHEPATH);
			if(offlineCachePath != null)
				headBuf.append("window.offlineCachePath = \"").append(offlineCachePath).append("\";\n\t\t\t\t");
		}
		ServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		String nodePath = (String) request.getAttribute(WebConstant.NODE_PATH);
		String nodeThemePath = (String) request.getAttribute(WebConstant.NODE_THEME_PATH);
		String nodeStylePath = (String) request.getAttribute(WebConstant.NODE_STYLE_PATH);
		String nodeImagePath = (String) request.getAttribute(WebConstant.NODE_IMAGE_PATH);

		headBuf.append("window.nodePath = \"").append(nodePath).append("\";\n\t\t\t\t");
		headBuf.append("window.nodeThemePath = \"").append(nodeThemePath).append("\";\n\t\t\t\t");
		headBuf.append("window.nodeStylePath = \"").append(nodeStylePath).append("\";\n\t\t\t\t");
		headBuf.append("window.nodeImagePath = \"").append(nodeImagePath).append("\";\n\t\t\t\t");

		String type = AppTypeUtil.getApplicationType();
		if (type.equals(AppTypeUtil.APP_TYPE)){
			String appId = (String) LfwRuntimeEnvironment.getWebContext().getAppSession().getAttribute("appId");
			headBuf.append("window.appId = '" + appId + "';\n");
			headBuf.append("window.appType = 'true';\n");
		}
		
			
		if(LfwRuntimeEnvironment.isFromLfw()){
			headBuf.append("window.tempPath = 'webtemp';\n");
		}
		else{
			headBuf.append("window.tempPath = '';\n");
		}
		headBuf.append("window.debugMode = '").append(LfwRuntimeEnvironment.getMode()).append("';\n");
		
		if(editMode){
			headBuf.append("window.editMode = true;\n");
		}
		else{
			headBuf.append("window.editMode = false;\n");
		}
		
		boolean wEditMode = LfwRuntimeEnvironment.isWindowEditorMode();
		if(wEditMode){
			headBuf.append("window.windowEditorMode = true;\n");
		}
		else{
			headBuf.append("window.windowEditorMode = false;\n");
		}
		headBuf.append("window.datasourceName = '").append(LfwRuntimeEnvironment.getDatasource()).append("';\n");

		headBuf.append("window.clientMode = ").append(clientMode).append(";\n");

		headBuf.append("\t\t</script>\n");
	}
	
	
	
	private String generateLoadedLib(Set<String> idSet) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		Iterator<String> it = idSet.iterator();
		while(it.hasNext()){
			String str = it.next();
			buf.append(str)
			   .append(":1");
			if(it.hasNext()){
				buf.append(",");
			}
		}
		buf.append("}");
		return buf.toString();
	}

	private String generateAllLibArray(boolean optimized) {
		if(optimized){
			if(optimizedLibArrStr == null){
				StringBuffer buf = new StringBuffer();
				buf.append("{");
				IControlPlugin[] ctrlPlugins = ControlFramework.getInstance().getAllControlPlugins();
				for (int i = 0; i < ctrlPlugins.length; i++) {
					IControlPlugin ctrlPlugin = ctrlPlugins[i];
					buf.append(ctrlPlugin.getId())
					   .append(":")
					   .append("{dp:")
					   .append(StringUtil.mergeScriptArrayStr(ctrlPlugin.getDependences()))
					   .append("}");
					if( i != ctrlPlugins.length - 1)
						buf.append(",");
				}
				buf.append("}");
				optimizedLibArrStr = buf.toString();
			}
			return optimizedLibArrStr;
		}
		else{
			if(libArrStr == null){
				StringBuffer buf = new StringBuffer();
				buf.append("{");
				IControlPlugin[] ctrlPlugins = ControlFramework.getInstance().getAllControlPlugins();
				for (int i = 0; i < ctrlPlugins.length; i++) {
					IControlPlugin ctrlPlugin = ctrlPlugins[i];
					buf.append("'")
					   .append(ctrlPlugin.getId())
					   .append("'")
					   .append(":{")
					   .append("'dp':")
					   .append(StringUtil.mergeScriptArrayStr(ctrlPlugin.getDependences()))
					   .append(",'jslib':")
					   .append(StringUtil.mergeScriptArrayStr(ctrlPlugin.getImports(optimized)))
					   .append(",'csslib':")
					   .append(StringUtil.mergeScriptArrayStr(ctrlPlugin.getCssImports(optimized)))
					   .append("}");
					if( i != ctrlPlugins.length - 1)
						buf.append(",");
				}
				buf.append("}");
				libArrStr = buf.toString();
			}
			return libArrStr;
		}
	}

	/**
	 * 扩展引入CSS接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	private void addExtendCss(String includeCss, StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeCss)) {
			String[] csss = includeCss.split(",");
			for (String css : csss) {
				buf.append(addPublicInclude(css));
			}
		}
	}
	
	/**
	 * 扩展引入CSS接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	private void addLfwExtendCss(String includeCss, StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeCss)) {
			String[] csss = includeCss.split(",");
			for (String css : csss) {
				buf.append(addLfwPublicInclude(css));
			}
		}
	}
	
	/**
	 * 添加页面级样式
	 * @param pagecss
	 * @param buf
	 */
	private void addPageCss(String pagecss,StringBuffer buf){
		if(StringUtils.isNotEmpty(pagecss)){
			buf.append("<style type='text/css'> \n");
			buf.append(pagecss);
			buf.append("</style> \n");
		}
	}
	
	/**
	 * 扩展引入js接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	private void addExtendJs(String includeJs, StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeJs)) {
			String[] jss = includeJs.split(",");
			for (String js : jss) {
				buf.append(addPublicInclude(js));
			}
		}
	}
	
	/**
	 * 扩展引入js接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	private void addLfwExtendJs(String includeJs, StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeJs)) {
			String[] jss = includeJs.split(",");
			for (String js : jss) {
				buf.append(addLfwPublicInclude(js));
			}
		}
	}

	/**
	 * 添加外部引入。不在进行文件是否存在判断，业务指定，由业务自己保证
	 * @param fileName
	 * @return
	 */
	private String addPublicInclude(String fileName) {
		StringBuffer includeHead = new StringBuffer();
		StringBuffer includeTrail = new StringBuffer();
		String folder = "includejs";
		if (fileName.toLowerCase().endsWith(".css")) {
			folder = "includecss";
			includeHead.append("<link rel='STYLESHEET' type='text/css' href='");
			includeTrail.append("'>\n");
		} else {
			includeHead.append("<script type='text/javascript' src='");
			includeTrail.append("'></script>\n");
		}
		includeHead.append(LfwRuntimeEnvironment.getRootPath() + "/html/nodes/" + folder + "/" + fileName);
		includeHead.append(includeTrail);
		return includeHead.toString();
	}
	
	/**
	 * 添加Lfw外部引入。不在进行文件是否存在判断，业务指定，由业务自己保证
	 * @param fileName
	 * @return
	 */
	private String addLfwPublicInclude(String fileName) {
		StringBuffer includeHead = new StringBuffer();
		StringBuffer includeTrail = new StringBuffer();
		String folder = "includejs";
		if (fileName.toLowerCase().endsWith(".css")) {
			folder = "includecss";
			includeHead.append("<link rel='STYLESHEET' type='text/css' href='");
			includeTrail.append("'>\n");
		} else {
			includeHead.append("<script type='text/javascript' src='");
			includeTrail.append("'></script>\n");
		}
		includeHead.append(LfwRuntimeEnvironment.getLfwCtx() + "/html/nodes/" + folder + "/" + fileName);
		includeHead.append(includeTrail);
		return includeHead.toString();
	}
	
	private void getImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, Set<String> idSet, UIMeta um, PageMeta pm, boolean optimized) {
		LfwWidget[] widgets = pm.getWidgets();
		if(um != null)
			getImportList(importList, cssImportList, idSet, um, optimized);
		for (int i = 0; i < widgets.length; i++) {
			LfwWidget widget = widgets[i];
			ViewModels vms = widget.getViewModels();
			Dataset[] dss = vms.getDatasets();
			if(dss != null && dss.length > 0)
				doGetImportList(importList, cssImportList, idSet, dss[0], optimized);
			
			ComboData[] combos = vms.getComboDatas();
			if(combos != null && combos.length > 0)
				doGetImportList(importList, cssImportList, idSet, combos[0], optimized);
			
			DatasetRelations dsRels = vms.getDsrelations();
			if(dsRels != null)
				doGetImportList(importList, cssImportList, idSet, new DatasetRelation(), optimized);
			
			IRefNode[] rfs = vms.getRefNodes();
			if(rfs != null && rfs.length > 0)
				doGetImportList(importList, cssImportList, idSet, rfs[0], optimized);
			
			RefNodeRelations rfrels = vms.getRefNodeRelations();
			if(rfrels != null)
				doGetImportList(importList, cssImportList, idSet, new RefNodeRelation(), optimized);
			
			if(um == null){
				ViewComponents vcs = widget.getViewComponents();
				WebComponent[] coms = vcs.getComponents();
				if(coms != null && coms.length > 0){
					for (int j = 0; j < coms.length; j++) {
						doGetImportList(importList, cssImportList, idSet, coms[j], optimized);
					}
				}
			}
		}
	}
	
	private void getImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, Set<String> idSet, UIMeta um, boolean optimized) {
		UIElement ele = um.getElement();
		if(ele instanceof UIWidget){
			UIWidget widget = (UIWidget) ele;
			getWidgetImportList(importList, cssImportList, idSet, widget, optimized);
		}
		else if(ele instanceof UILayout){
			getLayoutImportList(importList, cssImportList, idSet, (UILayout) ele, optimized);
		}
		else if(ele instanceof UIComponent){
			doGetUIEleImportList(importList, cssImportList, idSet, ele, optimized);
		}
	}

	private void getLayoutImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, Set<String> idSet, UILayout layout, boolean optimized) {
		doGetUIEleImportList(importList, cssImportList, idSet, layout, optimized);
		List<UILayoutPanel> panelList = layout.getPanelList();
		Iterator<UILayoutPanel> it = panelList.iterator();
		while(it.hasNext()){
			UILayoutPanel panel = it.next();
			UIElement ele = panel.getElement();
			if(ele == null)
				continue;
			doGetUIEleImportList(importList, cssImportList, idSet, ele, optimized);
			if(ele instanceof UIWidget){
				getWidgetImportList(importList, cssImportList, idSet, (UIWidget) ele, optimized);
			}
			if(ele instanceof UILayout){
				getLayoutImportList(importList, cssImportList, idSet, (UILayout) ele, optimized);
			}
		}
	}
	
	private void getWidgetImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, Set<String> idSet, UIWidget widget, boolean optimized){
		UIMeta um = widget.getUimeta();
		getImportList(importList, cssImportList, idSet, um, optimized);
	}

	private void doGetUIEleImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, Set<String> idSet, UIElement ele, boolean optimized) {
		IControlPlugin plugin = null;
		if(ele instanceof UITextField){
			LfwWidget widget = pm.getWidget(ele.getWidgetId());
			TextComp text = (TextComp) widget.getViewComponents().getComponent(ele.getId());
			if(text instanceof TextComp){
				String editorType = text.getEditorType();
				String type = null;
				if(editorType.equals(EditorTypeConst.STRINGTEXT))
					type = "stringtext";
				else if(editorType.equals(EditorTypeConst.INTEGERTEXT))
					type = "integertext";
				else if(editorType.equals(EditorTypeConst.DECIMALTEXT))
					type = "floattext";
				else if(editorType.equals(EditorTypeConst.DATETEXT))
					type = "datetext";
				else if(editorType.equals(EditorTypeConst.REFERENCE))
					type = "reftext";
				else if(editorType.equals(EditorTypeConst.PWDTEXT))
					type = "pswtext";
				else if(editorType.equals(EditorTypeConst.COMBODATA))
					type = "combotext";
				else if(editorType.equals(EditorTypeConst.TEXTAREA))
					type = "textarea";
				plugin = ControlFramework.getInstance().getControlPluginByType(type);
			}
		}
		else
			plugin = ControlFramework.getInstance().getControlPluginByUIClass(ele.getClass());
		doGetPluginImportList(importList, cssImportList, idSet, plugin, optimized);
	}
	
	private void doGetImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, Set<String> idSet, Object ele, boolean optimized) {
		IControlPlugin plugin = null;
		if(ele instanceof TextComp){
			String editorType = ((TextComp)ele).getEditorType();
			String type = null;
			if(editorType.equals(EditorTypeConst.STRINGTEXT))
				type = "stringtext";
			else if(editorType.equals(EditorTypeConst.INTEGERTEXT))
				type = "integertext";
			else if(editorType.equals(EditorTypeConst.DECIMALTEXT))
				type = "floattext";
			else if(editorType.equals(EditorTypeConst.DATETEXT))
				type = "datetext";
			else if(editorType.equals(EditorTypeConst.REFERENCE))
				type = "reftext";
			else if(editorType.equals(EditorTypeConst.PWDTEXT))
				type = "pswtext";
			else if(editorType.equals(EditorTypeConst.COMBODATA))
				type = "combotext";
			else if(editorType.equals(EditorTypeConst.TEXTAREA))
				type = "textarea";
			plugin = ControlFramework.getInstance().getControlPluginByType(type);
		}
		else{
			Class<?> c = ele.getClass();
			if(ele instanceof ComboData)
				c = ComboData.class;
			else if(ele instanceof IRefNode)
				c = IRefNode.class;
			else if(ele instanceof Dataset)
				c = Dataset.class;
			plugin = ControlFramework.getInstance().getControlPluginByClass(c);
		}
			
		doGetPluginImportList(importList, cssImportList, idSet, plugin, optimized);
	}

	private void doGetPluginImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, Set<String> idSet, IControlPlugin plugin, boolean optimized) {
		if(plugin != null){
			if(idSet.contains(plugin.getId()))
				return;
			idSet.add(plugin.getId());
			String[] dependences = plugin.calculateDependences(optimized);
			if(dependences != null){
				for (int i = 0; i < dependences.length; i++) {
					importList.add(new Importer(dependences[i]));
				}
			}

			String[] cssDependences = plugin.calculateCssDependences(optimized);
			if(cssDependences != null){
				for (int i = 0; i < cssDependences.length; i++) {
					cssImportList.add(new Importer(cssDependences[i]));
				}
			}
			
			String[] idDependences = plugin.calculateDependenceIds();
			if(idDependences != null){
				for (int i = 0; i < idDependences.length; i++) {
					idSet.add(idDependences[i]);
				}
			}
			
			String[] imps = plugin.getImports(optimized);
			if(imps != null){
				for (int i = 0; i < imps.length; i++) {
					importList.add(new Importer(imps[i]));
				}
			}
			
			String[] cssimps = plugin.getCssImports(optimized);
			if(cssimps != null){
				for (int i = 0; i < cssimps.length; i++) {
					cssImportList.add(new Importer(cssimps[i]));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param isJs
	 * @return 0 not exist, 1 exist in normal path, 2 exist in temp path
	 */
	private String includeSelfDefScript(String fileName) {
		StringBuffer buf = new StringBuffer();
		PageMeta pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		String folderPath = pm.getFoldPath();
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String ctxPath = LfwRuntimeEnvironment.getRootPath();
		String filePath = folderPath + "/" + fileName;
		addIncludedSelfDefScript(buf, appPath, ctxPath, filePath);
		
		LfwWidget[] widgets = pm.getWidgets();
		if(widgets != null){
			for (int i = 0; i < widgets.length; i++) {
				LfwWidget widget = widgets[i];
				folderPath = widget.getFoldPath();
				//public view
				if(folderPath == null){
					filePath = "/pagemeta/public/widgetpool/" + widget.getId() + "/" + fileName;
				}else{
					filePath = folderPath + "/" + fileName;
				}
				addIncludedSelfDefScript(buf, appPath, ctxPath, filePath);
			}
		}
		return buf.toString();
	}
	
	private void addIncludedSelfDefScript(StringBuffer buf, String appPath, String ctxPath, String filePath) {
		String scriptPath = appPath + filePath;
		File scriptFile = new File(scriptPath);
		if(scriptFile.exists())
			buf.append("<script type='text/javascript' src='" + ctxPath + filePath + "'></script>\n");
	}
	
	/**
	 * 
	 * @param isJs
	 * @return 0 not exist, 1 exist in normal path, 2 exist in temp path
	 */
	private String includeSelfDefCss(String fileName) {
		StringBuffer buf = new StringBuffer();
		PageMeta pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		String folderPath = pm.getFoldPath();
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String ctxPath = LfwRuntimeEnvironment.getRootPath();
		String filePath = folderPath + "/" + fileName;
		addIncludedSelfDefCss(buf, appPath, ctxPath, filePath);
		
		LfwWidget[] widgets = pm.getWidgets();
		if(widgets != null){
			for (int i = 0; i < widgets.length; i++) {
				LfwWidget widget = widgets[i];
				folderPath = widget.getFoldPath();
				filePath = folderPath + "/" + fileName;
				addIncludedSelfDefCss(buf, appPath, ctxPath, filePath);
			}
		}
		return buf.toString();
	}

	private void addIncludedSelfDefCss(StringBuffer buf, String appPath, String ctxPath, String filePath) {
		String scriptPath = appPath + filePath;
		File scriptFile = new File(scriptPath);
		if(scriptFile.exists())
			buf.append("<link rel='STYLESHEET' type='text/css' href='" + ctxPath + filePath + "'>\n");
	}
}
