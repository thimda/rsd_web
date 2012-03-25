package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.importer.SmartImporter;
import nc.uap.lfw.core.model.AppTypeUtil;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.util.LangResoTranf;
import nc.uap.lfw.core.util.PageNodeUtil;
import nc.uap.lfw.jsp.uimeta.UIMeta;

import org.apache.commons.lang.StringUtils;

/**
 * 客户端script引入tag。可根据不同的locale引入多语言script文件。此Tag只进行最基础的引入。各个具体模块可继承此类，
 * 实现运行时所需文件的引入
 * 
 * @author dengjt
 */
public class LfwImportTag extends LfwTagSupport {

	private static final String WEBTEMP = "/webtemp";
	private static final long serialVersionUID = -7870978632299450428L;
	protected String globalPath = null;
	protected String themeId = null;
	private String currThemePath;
	private String lfwPath;

	private boolean jsBasic = true;
	private boolean jsContainer = true;
	private boolean jsDialog = true;
	private boolean jsNormal = true;
	private boolean jsBinding = true;
	private boolean jsMvc = true;
	private boolean jsMenu = true;
	private boolean jsEditor = true;
	private boolean jsDragDrop = false;
	private boolean jsChart = false;
	private boolean jsExcel = true;
	
	private String pageId;
	private String includeCss;
	private String includeJs;

	// 是否包含JQuery代码
	private boolean jquery = false;
	private String type = AppTypeUtil.getApplicationType();

	public void doTag() throws JspException {
//		PageContext pageContext = getPageContext();
//		getPageId();
//		getThemeId();
//		getGlobalPath();
//		StringBuffer buf = new StringBuffer();
//		String lfwCtx = LfwRuntimeEnvironment.getLfwCtx();
//		boolean clientMode = LfwRuntimeEnvironment.isClientMode();
//		if(clientMode)
//			lfwCtx = ".." + lfwCtx;
//		lfwPath = lfwCtx + "/frame";
//		
//		String rootPath = LfwRuntimeEnvironment.getRootPath();
//		if(clientMode)
//			rootPath = ".." + rootPath;
//		LfwTheme theme = LfwThemeManager.getLfwTheme(rootPath, themeId);
//		if(theme == null){
//			throw new LfwRuntimeException("根据themeid找不到对应的theme," + themeId);
//		}
//		String themeCtxPath = theme.getCtxPath();
//		if(clientMode)
//			themeCtxPath = ".." + themeCtxPath;
//		currThemePath = themeCtxPath + "/frame/themes/" + themeId;
//
//		ServletContext ctx = pageContext.getServletContext();
//		String domain = (String) ctx.getAttribute(WebConstant.DOMAIN_KEY);
//		buf.append("<script>\n\t\t\t\t");
//		// 如果指定页面所处domain，则设置domain
//		if (domain != null) {
//			buf.append("document.domain=\"").append(domain).append("\";\n\t\t\t\t");
//		}
//		
//		buf.append("window.globalPath = \"").append(rootPath).append("\";\n\t\t\t\t");
//		buf.append("window.corePath = \"").append(LfwRuntimeEnvironment.getCorePath()).append("\";\n\t\t\t\t");
//		buf.append("window.themeId = \"").append(themeId).append("\";\n\t\t\t\t");
//		buf.append("window.baseGlobalPath = \"" + LfwRuntimeEnvironment.getLfwCtx() + "\";\n\t\t\t\t");
//		buf.append("window.themeGlobalPath = \"" + themeCtxPath + "\";\n\t\t\t\t");
//		buf.append("window.themePath = \"").append(currThemePath).append("\";\n\t\t\t\t");
//		if(clientMode){
//			String offlineCachePath = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.OFFLINE_CACHEPATH);
//			if(offlineCachePath != null)
//				buf.append("window.offlineCachePath = \"").append(offlineCachePath).append("\";\n\t\t\t\t");
//		}
//		ServletRequest request = pageContext.getRequest();
//		String nodePath = (String) request.getAttribute(WebConstant.NODE_PATH);
//		String nodeThemePath = (String) request.getAttribute(WebConstant.NODE_THEME_PATH);
//		String nodeStylePath = (String) request.getAttribute(WebConstant.NODE_STYLE_PATH);
//		String nodeImagePath = (String) request.getAttribute(WebConstant.NODE_IMAGE_PATH);
//
//		buf.append("window.nodePath = \"").append(nodePath).append("\";\n\t\t\t\t");
//		buf.append("window.nodeThemePath = \"").append(nodeThemePath).append("\";\n\t\t\t\t");
//		buf.append("window.nodeStylePath = \"").append(nodeStylePath).append("\";\n\t\t\t\t");
//		buf.append("window.nodeImagePath = \"").append(nodeImagePath).append("\";\n\t\t\t\t");
//
//		if(LfwRuntimeEnvironment.isFromLfw()){
//			buf.append("window.tempPath = 'webtemp';\n");
//		}
//		else{
//			buf.append("window.tempPath = '';\n");
//		}
//		buf.append("window.debugMode = '").append(LfwRuntimeEnvironment.getMode()).append("';\n");
//		
//		// add by renxh 是否为编辑态
//		boolean editMode = LfwRuntimeEnvironment.isEditMode();
//		if(editMode){
//			buf.append("window.editMode = true;\n");
//		}else{
//			buf.append("window.editMode = false;\n");
//		}
//		
//		boolean wMode = LfwRuntimeEnvironment.isWindowEditorMode();
//		
//		if(wMode){
//			buf.append("window.windowEditorMode = true;\n");
//		}else{
//			buf.append("window.windowEditorMode = false;\n");
//		}
//		buf.append("window.datasourceName = '").append(LfwRuntimeEnvironment.getDatasource()).append("';\n");
//
//		buf.append("window.clientMode = ").append(clientMode).append(";\n");
//
//		buf.append("\t\t</script>\n");
//		IUIMeta  ium = LfwRuntimeEnvironment.getWebContext().getUIMeta();
//		if(ium != null){
//			UIMeta um = (UIMeta)ium;
//			this.includeJs = um.getIncudejs();
//			this.includeCss = um.getIncudecss();
//			this.jsEditor = um.isJsEditor() != null && um.isJsEditor().intValue() == 1;
//			this.jquery = um.isJquery() != null && um.isJquery().intValue() == 1;
//			this.jsExcel = um.isJsExcel() != null && um.isJsExcel().intValue() == 1;
//			this.jsChart = um.isChart() != null && um.isChart().intValue() == 1;
//		}
//		// 引入css
//		addCss(buf, lfwCtx);
//		// 引入script
//		addScript(buf, lfwCtx);
//
//		try {
//			pageContext.getOut().write(buf.toString());
//		} catch (IOException e) {
//			Logger.error(e);
//		}
		
		PageMeta pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		UIMeta um = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
		String imp = new SmartImporter().genImporters(pm, um, isCustomized());
//		// 引入css
//		addCss(buf, lfwCtx);
//		// 引入script
//		addScript(buf, lfwCtx);

		try {
			getPageContext().getOut().write(imp);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private void addCss(StringBuffer buf, String lfwCtx) {
		if (!isCustomized()) {
			buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/global.css'>\n");
			
			String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
			boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
			if(editMode){
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/editable.css'>\n");
			}
			
			if (jsBasic) {
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/button.css'>\n");
			}
			if (jsContainer) {
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/spliter.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + lfwThemePath + "/styles/frame.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/panel.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/tab.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/outlookbar.css'>\n");
			}

			// dialog部分
			if (jsDialog) {
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/modaldialog.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/dialogbutton.css'>\n");
				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
				// lfwThemePath + "/styles/messagedialog.css'>\n");
				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
				// lfwThemePath + "/styles/warningdialog.css'>\n");
				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
				// lfwThemePath + "/styles/progressdialog.css'>\n");
				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
				// lfwThemePath + "/styles/errordialog.css'>\n");
				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
				// lfwThemePath + "/styles/confirmdialog.css'>\n");
			}

			if (jsMenu) {
				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
				// lfwThemePath + "/styles/menu.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/contextmenu.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/menubar.css'>\n");
			}
			// 普通控件部分
			if (jsNormal) {
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/text.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/text_form.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/checkbox.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/radiogroup.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/checkboxgroup.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/calendar.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/label.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/option.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/imagebutton.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/link.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/toolbar.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/toolbutton.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + lfwThemePath + "/styles/fileupload.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/selfdefcomp.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/message.css'>\n");
			}

			// 数据绑定型控件部分
			if (jsBinding) {
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/treeview.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/grid.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/paginationbutton.css'>\n");
			}

			if (jsEditor) {
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/editor/default/default.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/editor.css'>\n");
			}
			
			String include = includeSelfDef(false, "include.css");
			buf.append(include);

			// XHTML中必须包含的CSS样式 added by guoweic
			// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
			// lfwThemePath + "/styles/xbase.css'>\n");

		} else {
			buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/compression/cssall.css'>\n");
			String include = includeSelfDef(false, "include.css");
			buf.append(include);
			if (jsEditor) {
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/editor/default/default.css'>\n");
				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/editor.css'>\n");
			}
		}


		addExtendCss(buf);
	}

	private void addScript(StringBuffer buf, String lfwCtx) {
		if (!isCustomized()) {
			String langCode = LfwRuntimeEnvironment.getLangCode();
			if (langCode == null || langCode.isEmpty() || langCode.equals("null"))
				langCode = "simpchn";
			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/multilanguage/ml_" + langCode + ".js'></script>\n");
			
			
			// basic部分
			if (jsBasic) {
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/compression/basic_compressed.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/BrowserSniffer.js'></script>\n");

				/************** added by guoweic ************/

				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Logger.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/EventUtil.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Listener.js'></script>\n");

				/***************** END ***********************/

				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/JsExtensions.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/ArgumentsUtil.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/HashMap.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Singleton.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Formater.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Event.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/EventClass.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Measures.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/BaseComponent.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Boot.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Ajax.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Cache.js'></script>\n");
				
			}

			

			// container
			if (jsContainer) {
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/compression/containers_compressed.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/SpliterComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/PanelComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/HtmlContentComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/FrameComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/OutLookBarComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/TabComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/FloatingPanelComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/FlowGridLayout.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/FixedGridLayout.js'></script>\n");

			}

			// dialog部分
			if (jsDialog) {
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/compression/dialog_compressed.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ModalDialogComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/FloatingDialogComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ConfirmDialogComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/WarningDialogComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ProgressDialogComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ErrorDialogComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/MessageDialogComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ExceptionDialog.js'></script>\n");
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/dialog/DebugMonitor.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/InputDialogComp.js'></script>\n");
			}

			if (jsMenu) {
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/compression/menu_compressed.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/menu/MenuBarComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/menu/ContextMenuComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/menu/MenuBarGroup.js'></script>\n");
			}
			// 普通控件部分
			if (jsNormal) {
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/compression/normal_compressed.us'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/TextComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/StringTextComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/PswTextComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/IntegerTextComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/FloatTextComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ReferenceTextComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/FileUploadComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/DateTextComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/TextAreaComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ButtonComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/TableComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ImageComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/LabelComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ImageButtonComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/RadioComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/CheckboxComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/CalendarComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ComboComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/OptionComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ListComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ListToListComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/LinkComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ProgressBarComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/IFrameComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ToolBarComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/RadioGroupComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/CheckboxGroupComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/LoadingBarComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/SelfDefComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/SelfDefElementComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/MessageComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/FileComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/TextMarkComp.js'></script>\n");
			}

			// 数据绑定型控件部分
			if (jsBinding) {
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/compression/binding_compressed.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/GridComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/GridAssistant.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/TreeViewComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/TreeLevel.js'></script>\n");
				//Masker
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/masker/MaskerMeta.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/masker/Maskers.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/masker/MaskerUtil.js'></script>\n");
			}

			// web frame mvc部分
			if (jsMvc) {
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/Widget.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PageUI.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PageUI_" + type + ".js'></script>\n");
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/compression/mvc_compressed.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PageUtil.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/CardLayout.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/AutoFormComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PaginationComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/WebDataset.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/ViewModel.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/WebDatasetEvent.js'></script>\n");

				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/DsRelationLogic.js'></script>\n");
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/mvc/DataSetLoadLogic.js'></script>\n");
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/mvc/DatasetLoader.js'></script>\n");
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/mvc/DatasetSaver.js'></script>\n");
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/mvc/DataSetSaveLogic.js'></script>\n");

				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/CommonCommand.js'></script>\n");

				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/IDatasetConstant.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/ButtonManager.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/IBillOperate.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/IBillStatus.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/DatasetRuleChecker.js'></script>\n");
			}

			if (jsDragDrop) {
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/dragdrop/Coordinates.js'></script>\n");
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/dragdrop/Drag.js'></script>\n");
				// buf.append("<script type='text/javascript' src='" + lfwPath +
				// "/script/dragdrop/Dragdrop.js'></script>\n");
			}
			
			if (jsEditor) {
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/kindeditor.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/zh_CN.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/EditorComp.js'></script>\n");
			}
			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/json/jsonrpc.js'></script>\n");

			if (jquery) {
				// 引入JQuery文件
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/jquery/jquery-1.4.2.js'></script>\n");
			}
			
			if (jsChart) {
				// 引入图表控件相关文件
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/fusionchart/FusionCharts.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/fusionchart/FusionCharts.jqueryplugin.js'></script>\n");				
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/ChartModel.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/ChartConvertor.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/ChartComp.js'></script>\n");
			}
			
			if (jsExcel) {
				// 引入Excel控件相关文件
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/ExcelComp.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/ntko/officecontrol.js'></script>\n");
			}
			
			/************** added by renxh ************/
			String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
			boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
			if(editMode){
				if (!jsMenu) {//编辑态需要右键支持
					buf.append("<script type='text/javascript' src='" + lfwPath + "/script/menu/ContextMenuComp.js'></script>\n");
				}
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/layout/LayoutSupportEdit.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/editable.js'></script>\n");
			}else{
				/************** added by guoweic ************/
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/layout/LayoutSupport.js'></script>\n");
				/***************** END ***********************/
			}

			/***************** END ***********************/
			
			// 圆角DIV相关代码
			// buf.append("<script type='text/javascript' src='" + lfwPath +
			// "/script/jquery/curvycorners.js'></script>\n");

			addExtendJs(buf);
			
			String include = includeSelfDef(true, "include.js");
			buf.append(include);

			// String assignedPageId = getPageId();
			// if(assignedPageId != null){
			// //buf.append("<script type='text/javascript' src='" +
			// LfwRuntimeEnvironment.getRootPath() + "/code/" + assignedPageId +
			// "'></script>\n");
			// }

			buf.append("<script>\n").append("\twindow.onload = function(){\n").append("\t\t").append("pageBodyScript();\n").append("\t}\n").append(
					"</script>\n");
			
		}

		else {
			String langCode = (String) getPageContext().getSession().getAttribute(WebConstant.LANG_KEY);
			if (langCode == null)
				langCode = "simpchn";
			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/multilanguage/ml_" + langCode + ".js'></script>\n");

			/*
			 * //basic部分 if(jsBasic)
			 * buf.append("<script type='text/javascript' src='/lfw/code/jsbasic"
			 * + "'></script>\n");
			 * 
			 * //container if(jsContainer)buf.append(
			 * "<script type='text/javascript' src='/lfw/code/jscontainers" +
			 * "'></script>\n");
			 * 
			 * //dialog部分 if(jsDialog)
			 * buf.append("<script type='text/javascript' src='/lfw/code/jsdialog"
			 * + "'></script>\n");
			 * 
			 * 
			 * if(jsMenu)
			 * buf.append("<script type='text/javascript' src='/lfw/code/jsmenu"
			 * + "'></script>\n");
			 * 
			 * //普通控件部分 if(jsNormal)
			 * buf.append("<script type='text/javascript' src='/lfw/code/jsnormal"
			 * + "'></script>\n");
			 * 
			 * 
			 * //数据绑定型控件部分 if(jsBinding)
			 * buf.append("<script type='text/javascript' src='/lfw/code/jsbinding"
			 * + "'></script>\n");
			 * 
			 * //web frame mvc部分 if(jsMvc)
			 * buf.append("<script type='text/javascript' src='/lfw/code/jsmvc"
			 * + "'></script>\n");
			 * 
			 * if(jsDragDrop){
			 * //buf.append("<script type='text/javascript' src='" + lfwPath +
			 * "/script/compression/dragdrop_compressed.js'></script>\n"); }
			 * //if(jsEditor)
			 * //buf.append("<script type='text/javascript' src='" + lfwPath +
			 * "/script/editor/EditorComp.js'></script>\n");
			 */

			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/compression/jsall.js'></script>\n");
			
			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PageUI_" + type + ".js'></script>\n");
			// 圆角DIV相关代码
			// buf.append("<script type='text/javascript' src='" + lfwPath +
			// "/script/jquery/curvycorners.js'></script>\n");
			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/json/jsonrpc.js'></script>\n");


			if (jsEditor) {
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/kindeditor.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/zh_CN.js'></script>\n");
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/EditorComp.js'></script>\n");
			}

			if (jquery) {
				// 引入JQuery文件
				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/jquery/jquery-1.4.2.js'></script>\n");
			}
			
			addExtendJs(buf);
			
			String include = includeSelfDef(true, "include.js");
			buf.append(include);
			
			buf.append("<script>\n").append("\twindow.onload = function(){\n").append("\t\t").append("pageBodyScript();\n").append("\t}\n").append(
					"</script>\n");
			
		}
		
		ServletRequest servlet = LfwRuntimeEnvironment.getWebContext().getRequest();
		//自定义多语资源
		Properties langResources = (Properties) servlet.getAttribute("langResources");
		if(langResources != null){
			String pageId = getPageId();
			LangResoTranf.tranf(langResources, pageId);
			String langResPath = LfwRuntimeEnvironment.getRootPath() + "/webtemp/html/nodes/" + pageId + "/ml_resource.js";
			buf.append("<script type='text/javascript' src='" + langResPath + "'></script>\n");
		}
	}

	/**
	 * 
	 * @param isJs
	 * @return 0 not exist, 1 exist in normal path, 2 exist in temp path
	 */
	private String includeSelfDef(boolean isJs, String fileName) {
		if (pageId == null)
			return "";
		String path;
		String pagePath = PageNodeUtil.getPageNodeDir(pageId);
		if (pagePath == null)
			pagePath = pageId;
		String staticPath = null;
		boolean clientMode = LfwRuntimeEnvironment.isClientMode();
		if(clientMode){
			int pos = pageId.indexOf("/");
			if(pos > -1){
				staticPath = pageId.substring(pos+1) + fileName;
			}else{
				staticPath = pageId + fileName;
			}
		}
		if (isJs) {
			path = "/html/nodes/" + pagePath + "/" + fileName;
			boolean exist = ContextResourceUtil.resourceExist(path);
			if (exist) {
				String scriptPath = LfwRuntimeEnvironment.getRootPath() + "/html/nodes/" + pagePath + "/" + fileName;
				if(clientMode){
					scriptPath = staticPath;
				}
				String str = "<script type='text/javascript' src='" + scriptPath + "'></script>\n";
				return str;
			}
			if (LfwRuntimeEnvironment.isFromLfw()) {
				exist = ContextResourceUtil.resourceExist(WEBTEMP + path);
				if (exist) {
					String scriptPath = LfwRuntimeEnvironment.getRootPath() + WEBTEMP + "/html/nodes/" + pagePath + "/" + fileName;
					if(clientMode){
						scriptPath = staticPath;
					}
					String str = "<script type='text/javascript' src='" + scriptPath + "'></script>\n";
					return str;
				}
			}
		} else {
			path = "/html/nodes/" + pagePath + "/" + fileName + "";
			boolean exist = ContextResourceUtil.resourceExist(path);
			if (exist) {
				String stylePath = LfwRuntimeEnvironment.getRootPath() + "/html/nodes/" + pagePath + "/" + fileName;
				if(clientMode){
					stylePath = staticPath;
				}
				String str = "<link rel='STYLESHEET' type='text/css' href='" + stylePath + "'>\n";
				return str;
			}
			if (LfwRuntimeEnvironment.isFromLfw()) {
				exist = ContextResourceUtil.resourceExist(WEBTEMP + path);
				if (exist) {
					String stylePath = LfwRuntimeEnvironment.getRootPath() + WEBTEMP + "/html/nodes/" + pagePath + "/" + fileName;
					if(clientMode){
						stylePath = staticPath;
					}
					String str = "<link rel='STYLESHEET' type='text/css' href='" + stylePath + "'>\n";
					return str;
				}
			}

		}
		return "";
	}

	/**
	 * 扩展引入CSS接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	protected void addExtendCss(StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeCss)) {
			String[] csss = includeCss.split(",");
			for (String css : csss) {
				buf.append(addPublicInclude(css));
			}
		}
	}

	/**
	 * 扩展引入js接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	protected void addExtendJs(StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeJs)) {
			String[] jss = includeJs.split(",");
			for (String js : jss) {
				buf.append(addPublicInclude(js));
			}
		}
	}

	protected String addPublicInclude(String fileName) {
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
		String path = "/html/nodes/" + folder + "/" + fileName;
		boolean exist = ContextResourceUtil.resourceExist(path);
		if (exist) {
			includeHead.append(LfwRuntimeEnvironment.getRootPath() + "/html/nodes/" + folder + "/" + fileName);
			includeHead.append(includeTrail);
			return includeHead.toString();
		}
		return "";
	}

	/**
	 * 产生具体应用模块自己的ThemePath
	 * 
	 * @return
	 */
	protected String getThemePath() {
		return globalPath + "/themes/" + themeId;
	}

	public String getGlobalPath() {
		if (globalPath == null)
			return LfwRuntimeEnvironment.getLfwCtx() + "/frame";
		return globalPath;
	}

	public void setGlobalPath(String globalPath) {
		this.globalPath = globalPath;
	}

	public String getPageId() {
		if (pageId == null)
			pageId = (String) getPageContext().getRequest().getAttribute(WebConstant.PERSONAL_PAGE_ID_KEY);
		if (pageId == null)
			pageId = getPageContext().getRequest().getParameter("pageId");
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getThemeId() {
		if (themeId == null)
			themeId = LfwRuntimeEnvironment.getThemeId();
		return themeId;
	}

	public void setThemeId(String theme) {
		this.themeId = theme;
	}

	/**
	 * 是否进行优化载入。所谓优化载入，即载入经过我们的工具优化生成的js文件。体积更小，数量更少。建议用于产品环境。非优化载入可用于开发调试阶段
	 */
	public boolean isCustomized() {
		return !(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DEBUG) || LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN));
	}

	public String getLfwPath() {
		return lfwPath;
	}

	public void setLfwPath(String lfwPath) {
		this.lfwPath = lfwPath;
	}

	public String getLfwThemePath() {
		return currThemePath;
	}

	public void setLfwThemePath(String lfwThemePath) {
		this.currThemePath = lfwThemePath;
	}

	public boolean isJsBasic() {
		return jsBasic;
	}

	public void setJsBasic(boolean jsBasic) {
		this.jsBasic = jsBasic;
	}

	public boolean isJsBinding() {
		return jsBinding;
	}

	public void setJsBinding(boolean jsBinding) {
		this.jsBinding = jsBinding;
	}

	public boolean isJsContainer() {
		return jsContainer;
	}

	public void setJsContainer(boolean jsContainer) {
		this.jsContainer = jsContainer;
	}

	public boolean isJsDialog() {
		return jsDialog;
	}

	public void setJsDialog(boolean jsDialog) {
		this.jsDialog = jsDialog;
	}

	public boolean isJsMvc() {
		return jsMvc;
	}

	public void setJsMvc(boolean jsMvc) {
		this.jsMvc = jsMvc;
	}

	public boolean isJsNormal() {
		return jsNormal;
	}

	public void setJsNormal(boolean jsNormal) {
		this.jsNormal = jsNormal;
	}

	public boolean isJsEditor() {
		return jsEditor;
	}

	public void setJsEditor(boolean jsEditor) {
		this.jsEditor = jsEditor;
	}

	public boolean isJsMenu() {
		return jsMenu;
	}

	public void setJsMenu(boolean jsMenu) {
		this.jsMenu = jsMenu;
	}

	public boolean isJquery() {
		return jquery;
	}

	public void setJquery(boolean jquery) {
		this.jquery = jquery;
	}

	public String getIncludeCss() {
		return includeCss;
	}

	public void setIncludeCss(String includeCss) {
		this.includeCss = includeCss;
	}

	public String getIncludeJs() {
		return includeJs;
	}

	public void setIncludeJs(String includeJs) {
		this.includeJs = includeJs;
	}

	public boolean isJsChart() {
		return jsChart;
	}

	public void setJsChart(boolean jsChart) {
		this.jsChart = jsChart;
	}

	public boolean isJsExcel() {
		return jsExcel;
	}

	public void setJsExcel(boolean jsExcel) {
		this.jsExcel = jsExcel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
//		this.type = type;
	}

}
