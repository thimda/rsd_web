package nc.uap.lfw.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.importer.SmartImporter;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * �ͻ���script����tag���ɸ��ݲ�ͬ��locale���������script�ļ�����Tagֻ��������������롣��������ģ��ɼ̳д��࣬
 * ʵ������ʱ�����ļ�������
 * 
 */
public class PageImportTag extends LfwTagSupport {
//	private static final String WEBTEMP = "/webtemp";
	private static final long serialVersionUID = -7870978632299450428L;
//	protected String globalPath = null;
//	protected String themeId = null;
//	private String currThemePath;
//	private String lfwPath;

//	private boolean jsBasic = true;
//	private boolean jsContainer = true;
//	private boolean jsDialog = true;
//	private boolean jsNormal = true;
//	private boolean jsBinding = true;
//	private boolean jsMvc = true;
//	private boolean jsMenu = true;
//	private boolean jsEditor = true;
//	private boolean jsDragDrop = false;
//	private boolean jsChart = false;
//	private boolean jsExcel = true;
	
//	private String pageId;
//	private String includeCss;
//	private String includeJs;

	// �Ƿ����JQuery����
//	private boolean jquery = false;
//	private String type = AppTypeUtil.getApplicationType();
//
//	private boolean editMode = false;
//	private boolean wEditMode = false;
	public PageImportTag() {
	}
	public void doTag() throws JspException {
//		getPageId();
/*		getThemeId();
		getGlobalPath();*/
		// add by renxh �Ƿ�Ϊ�༭̬
//		String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
//		editMode = (editModeStr == null) ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
//		wEditMode = LfwRuntimeEnvironment.isWindowEditorMode();
		
//		PageContext pageContext = getPageContext();
//		StringBuffer buf = new StringBuffer();
//		String lfwCtx = LfwRuntimeEnvironment.getLfwCtx();
//		boolean clientMode = LfwRuntimeEnvironment.isClientMode();
//		if(clientMode)
//			lfwCtx = ".." + lfwCtx;
//		lfwPath = lfwCtx + "/frame";
//		
		
		PageMeta pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		UIMeta um = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
		String imp = new SmartImporter().genImporters(pm, um, isCustomized());
//		// ����css
//		addCss(buf, lfwCtx);
//		// ����script
//		addScript(buf, lfwCtx);

		try {
			getPageContext().getOut().write(imp);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

//	private void addCss(StringBuffer buf, String lfwCtx) {
//		if (!isCustomized()) {
//			buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/global.css'>\n");
//			
//			String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
//			boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
//			if(editMode){
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/editable.css'>\n");
//			}
//			
//			if (jsBasic) {
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/button.css'>\n");
//			}
//			if (jsContainer) {
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/spliter.css'>\n");
////				buf.append("<link rel='STYLESHEET' type='text/css' href='" + lfwThemePath + "/styles/frame.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/panel.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/tab.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/outlookbar.css'>\n");
//			}
//
//			// dialog����
//			if (jsDialog) {
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/modaldialog.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/dialogbutton.css'>\n");
//				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
//				// lfwThemePath + "/styles/messagedialog.css'>\n");
//				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
//				// lfwThemePath + "/styles/warningdialog.css'>\n");
//				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
//				// lfwThemePath + "/styles/progressdialog.css'>\n");
//				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
//				// lfwThemePath + "/styles/errordialog.css'>\n");
//				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
//				// lfwThemePath + "/styles/confirmdialog.css'>\n");
//			}
//
//			if (jsMenu) {
//				// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
//				// lfwThemePath + "/styles/menu.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/contextmenu.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/menubar.css'>\n");
//			}
//			// ��ͨ�ؼ�����
//			if (jsNormal) {
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/text.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/list.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/text_form.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/checkbox.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/radiogroup.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/checkboxgroup.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/calendar.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/label.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/option.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/imagebutton.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/link.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/toolbar.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/toolbutton.css'>\n");
////				buf.append("<link rel='STYLESHEET' type='text/css' href='" + lfwThemePath + "/styles/fileupload.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/selfdefcomp.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/message.css'>\n");
//			}
//
//			// ���ݰ��Ϳؼ�����
//			if (jsBinding) {
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/treeview.css'>\n");
//				UIMeta um = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
//				if(editMode && !wEditMode){
//					UIWidget view = um.getFirstView();
//					um = view.getUimeta();
//				}
//				if(um.getFlowmode().booleanValue())
//					buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/grid1.css'>\n");
//				else
//					buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/grid.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/paginationbutton.css'>\n");
//			}
//
//			if (jsEditor) {
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/editor/default/default.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/editor.css'>\n");
//			}
//			
//			String include = includeSelfDefCss("include.css");
//			buf.append(include);
//
//			// XHTML�б��������CSS��ʽ added by guoweic
//			// buf.append("<link rel='STYLESHEET' type='text/css' href='" +
//			// lfwThemePath + "/styles/xbase.css'>\n");
//
//		} else {
//			buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/compression/cssall.css'>\n");
//			String include = includeSelfDefCss("include.css");
//			buf.append(include);
//			if (jsEditor) {
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/editor/default/default.css'>\n");
//				buf.append("<link rel='STYLESHEET' type='text/css' href='" + currThemePath + "/styles/editor.css'>\n");
//			}
//		}
//
//
//		addExtendCss(buf);
//	}
//
//	private void addScript(StringBuffer buf, String lfwCtx) {
//		if (!isCustomized()) {
//			String langCode = LfwRuntimeEnvironment.getLangCode();
//			if (langCode == null || langCode.isEmpty() || langCode.equals("null"))
//				langCode = "simpchn";
//			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/multilanguage/ml_" + langCode + ".js'></script>\n");
//			
//			
//			// basic����
//			if (jsBasic) {
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/compression/basic_compressed.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/BrowserSniffer.js'></script>\n");
//
//				/************** added by guoweic ************/
//
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Logger.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/EventUtil.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Listener.js'></script>\n");
//
//				/***************** END ***********************/
//
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/JsExtensions.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/ArgumentsUtil.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/HashMap.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Singleton.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Formater.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Event.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/EventClass.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Measures.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/BaseComponent.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Boot.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Ajax.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/Cache.js'></script>\n");
//				
//			}
//
//			
//
//			// container
//			if (jsContainer) {
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/compression/containers_compressed.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/SpliterComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/PanelComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/HtmlContentComp.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/FrameComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/OutLookBarComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/TabComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/FloatingPanelComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/FlowGridLayout.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/FixedGridLayout.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/containers/SilverlightWidget.js'></script>\n");
//			}
//
//			// dialog����
//			if (jsDialog) {
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/compression/dialog_compressed.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ModalDialogComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/FloatingDialogComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ConfirmDialogComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ThreeButtonsDialogComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/WarningDialogComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ProgressDialogComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ErrorDialogComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/MessageDialogComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/ExceptionDialog.js'></script>\n");
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/dialog/DebugMonitor.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/dialog/InputDialogComp.js'></script>\n");
//			}
//
//			if (jsMenu) {
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/compression/menu_compressed.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/menu/MenuBarComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/menu/ContextMenuComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/menu/MenuBarGroup.js'></script>\n");
//			}
//			// ��ͨ�ؼ�����
//			if (jsNormal) {
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/compression/normal_compressed.us'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/TextComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/StringTextComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/PswTextComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/IntegerTextComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/FloatTextComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ReferenceTextComp.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/FileUploadComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/DateTextComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/TextAreaComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ButtonComp.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/TableComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ImageComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/LabelComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ImageButtonComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/RadioComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/CheckboxComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/CalendarComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ComboComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/OptionComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ListComp.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ListToListComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/LinkComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ProgressBarComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/IFrameComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/ToolBarComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/RadioGroupComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/CheckboxGroupComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/LoadingBarComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/SelfDefComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/SelfDefElementComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/MessageComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/FileComp.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/normal/TextMarkComp.js'></script>\n");
//			}
//
//			// ���ݰ��Ϳؼ�����
//			if (jsBinding) {
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/compression/binding_compressed.js'></script>\n");
//				UIMeta um = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
//				//��windows�༭״̬�µı༭view��״̬ʱ����Ҫ����view�������Ƿ���ʽ����
//				if(editMode && !wEditMode){
//					UIWidget view = um.getFirstView();
//					um = view.getUimeta();
//				}
//					
//				if(um.getFlowmode().booleanValue()){
//					buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/GridComp1.js'></script>\n");
//					buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/GridAssistant1.js'></script>\n");
//				}
//				else{
//					buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/GridComp.js'></script>\n");
//					buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/GridAssistant.js'></script>\n");
//				}
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/TreeViewComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/TreeLevel.js'></script>\n");
//				//Masker
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/masker/MaskerMeta.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/masker/Maskers.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/masker/MaskerUtil.js'></script>\n");
//			}
//
//			// web frame mvc����
//			if (jsMvc) {
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/Widget.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PageUI.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PageUI_" + type + ".js'></script>\n");
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/compression/mvc_compressed.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PageUtil.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/CardLayout.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/AutoFormComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PaginationComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/WebDataset.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/ViewModel.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/WebDatasetEvent.js'></script>\n");
//
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/DsRelationLogic.js'></script>\n");
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/mvc/DataSetLoadLogic.js'></script>\n");
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/mvc/DatasetLoader.js'></script>\n");
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/mvc/DatasetSaver.js'></script>\n");
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/mvc/DataSetSaveLogic.js'></script>\n");
//
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/CommonCommand.js'></script>\n");
//
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/IDatasetConstant.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/ButtonManager.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/IBillOperate.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/IBillStatus.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/DatasetRuleChecker.js'></script>\n");
//			}
//
//			if (jsDragDrop) {
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/dragdrop/Coordinates.js'></script>\n");
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/dragdrop/Drag.js'></script>\n");
//				// buf.append("<script type='text/javascript' src='" + lfwPath +
//				// "/script/dragdrop/Dragdrop.js'></script>\n");
//			}
//			
//			if (jsEditor) {
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/ckeditor/ckeditor.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/kindeditor.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/zh_CN.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/EditorComp.js'></script>\n");
//			}
//			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/json/jsonrpc.js'></script>\n");
//
//			if (jquery) {
//				// ����JQuery�ļ�
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/jquery/jquery-1.4.2.js'></script>\n");
//			}
//			
//			if (jsChart) {
//				// ����ͼ��ؼ�����ļ�
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/fusionchart/FusionCharts.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/fusionchart/FusionCharts.jqueryplugin.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/ChartModel.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/ChartConvertor.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/chart/ChartComp.js'></script>\n");
//			}
//			
//			if (jsExcel) {
//				// ����Excel�ؼ�����ļ�
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/ExcelComp.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/binding/ntko/officecontrol.js'></script>\n");
//			}
//			
//			/************** added by renxh ************/
//			String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
//			boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
//			if(editMode){
//				if (!jsMenu) {//�༭̬��Ҫ�Ҽ�֧��
//					buf.append("<script type='text/javascript' src='" + lfwPath + "/script/menu/ContextMenuComp.js'></script>\n");
//				}
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/layout/LayoutSupportEdit.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/basic/editable.js'></script>\n");
//			}else{
//				/************** added by guoweic ************/
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/layout/LayoutSupport.js'></script>\n");
//				/***************** END ***********************/
//			}
//
//			/***************** END ***********************/
//			
//			// Բ��DIV��ش���
//			// buf.append("<script type='text/javascript' src='" + lfwPath +
//			// "/script/jquery/curvycorners.js'></script>\n");
//
//			addExtendJs(buf);
//			
//			String include = includeSelfDefScript("include.js");
//			buf.append(include);
//
//			// String assignedPageId = getPageId();
//			// if(assignedPageId != null){
//			// //buf.append("<script type='text/javascript' src='" +
//			// LfwRuntimeEnvironment.getRootPath() + "/code/" + assignedPageId +
//			// "'></script>\n");
//			// }
//
//			buf.append("<script>\n").append("\twindow.onload = function(){\n").append("\t\t").append("pageBodyScript();\n").append("\t}\n").append(
//					"</script>\n");
//			
//		}
//
//		else {
//			String langCode = (String) getPageContext().getSession().getAttribute(WebConstant.LANG_KEY);
//			if (langCode == null)
//				langCode = "simpchn";
//			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/multilanguage/ml_" + langCode + ".js'></script>\n");
//
//			/*
//			 * //basic���� if(jsBasic)
//			 * buf.append("<script type='text/javascript' src='/lfw/code/jsbasic"
//			 * + "'></script>\n");
//			 * 
//			 * //container if(jsContainer)buf.append(
//			 * "<script type='text/javascript' src='/lfw/code/jscontainers" +
//			 * "'></script>\n");
//			 * 
//			 * //dialog���� if(jsDialog)
//			 * buf.append("<script type='text/javascript' src='/lfw/code/jsdialog"
//			 * + "'></script>\n");
//			 * 
//			 * 
//			 * if(jsMenu)
//			 * buf.append("<script type='text/javascript' src='/lfw/code/jsmenu"
//			 * + "'></script>\n");
//			 * 
//			 * //��ͨ�ؼ����� if(jsNormal)
//			 * buf.append("<script type='text/javascript' src='/lfw/code/jsnormal"
//			 * + "'></script>\n");
//			 * 
//			 * 
//			 * //���ݰ��Ϳؼ����� if(jsBinding)
//			 * buf.append("<script type='text/javascript' src='/lfw/code/jsbinding"
//			 * + "'></script>\n");
//			 * 
//			 * //web frame mvc���� if(jsMvc)
//			 * buf.append("<script type='text/javascript' src='/lfw/code/jsmvc"
//			 * + "'></script>\n");
//			 * 
//			 * if(jsDragDrop){
//			 * //buf.append("<script type='text/javascript' src='" + lfwPath +
//			 * "/script/compression/dragdrop_compressed.js'></script>\n"); }
//			 * //if(jsEditor)
//			 * //buf.append("<script type='text/javascript' src='" + lfwPath +
//			 * "/script/editor/EditorComp.js'></script>\n");
//			 */
//
//			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/compression/jsall.js'></script>\n");
//			
//			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/mvc/PageUI_" + type + ".js'></script>\n");
//			// Բ��DIV��ش���
//			// buf.append("<script type='text/javascript' src='" + lfwPath +
//			// "/script/jquery/curvycorners.js'></script>\n");
//			buf.append("<script type='text/javascript' src='" + lfwPath + "/script/json/jsonrpc.js'></script>\n");
//
//
//			if (jsEditor) {
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/ckeditor/ckeditor.js'></script>\n");
//				
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/kindeditor.js'></script>\n");
////				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/zh_CN.js'></script>\n");
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/editor/EditorComp.js'></script>\n");
//			}
//
//			if (jquery) {
//				// ����JQuery�ļ�
//				buf.append("<script type='text/javascript' src='" + lfwPath + "/script/jquery/jquery-1.4.2.js'></script>\n");
//			}
//			
//			addExtendJs(buf);
//			
//			String include = includeSelfDefScript("include.js");
//			buf.append(include);
//			
//			buf.append("<script>\n").append("\twindow.onload = function(){\n").append("\t\t").append("pageBodyScript();\n").append("\t}\n").append(
//					"</script>\n");
//			
//		}
//		
//		ServletRequest servlet = LfwRuntimeEnvironment.getWebContext().getRequest();
//		//�Զ��������Դ
//		Properties langResources = (Properties) servlet.getAttribute("langResources");
//		if(langResources != null){
//			String pageId = getPageId();
//			LangResoTranf.tranf(langResources, pageId);
//			String langResPath = LfwRuntimeEnvironment.getRootPath() + "/webtemp/html/nodes/" + pageId + "/ml_resource.js";
//			buf.append("<script type='text/javascript' src='" + langResPath + "'></script>\n");
//		}
//	}

//	/**
//	 * 
//	 * @param isJs
//	 * @return 0 not exist, 1 exist in normal path, 2 exist in temp path
//	 */
//	private String includeSelfDefScript(String fileName) {
//		StringBuffer buf = new StringBuffer();
//		PageMeta pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
//		String folderPath = pm.getFoldPath();
//		String appPath = ContextResourceUtil.getCurrentAppPath();
//		String ctxPath = LfwRuntimeEnvironment.getRootPath();
//		String filePath = folderPath + "/" + fileName;
//		addIncludedSelfDefScript(buf, appPath, ctxPath, filePath);
//		
//		LfwWidget[] widgets = pm.getWidgets();
//		if(widgets != null){
//			for (int i = 0; i < widgets.length; i++) {
//				LfwWidget widget = widgets[i];
//				folderPath = widget.getFoldPath();
//				//public view
//				if(folderPath == null){
//					filePath = "/pagemeta/public/widgetpool/" + widget.getId() + "/" + fileName;
//				}else{
//					filePath = folderPath + "/" + fileName;
//				}
//				addIncludedSelfDefScript(buf, appPath, ctxPath, filePath);
//			}
//		}
//		return buf.toString();
//	}
//	
//	private void addIncludedSelfDefScript(StringBuffer buf, String appPath, String ctxPath, String filePath) {
//		String scriptPath = appPath + filePath;
//		File scriptFile = new File(scriptPath);
//		if(scriptFile.exists())
//			buf.append("<script type='text/javascript' src='" + ctxPath + filePath + "'></script>\n");
//	}
//	
//	/**
//	 * 
//	 * @param isJs
//	 * @return 0 not exist, 1 exist in normal path, 2 exist in temp path
//	 */
//	private String includeSelfDefCss(String fileName) {
//		StringBuffer buf = new StringBuffer();
//		PageMeta pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
//		String folderPath = pm.getFoldPath();
//		String appPath = ContextResourceUtil.getCurrentAppPath();
//		String ctxPath = LfwRuntimeEnvironment.getRootPath();
//		String filePath = folderPath + "/" + fileName;
//		addIncludedSelfDefCss(buf, appPath, ctxPath, filePath);
//		
//		LfwWidget[] widgets = pm.getWidgets();
//		if(widgets != null){
//			for (int i = 0; i < widgets.length; i++) {
//				LfwWidget widget = widgets[i];
//				folderPath = widget.getFoldPath();
//				filePath = folderPath + "/" + fileName;
//				addIncludedSelfDefCss(buf, appPath, ctxPath, filePath);
//			}
//		}
//		return buf.toString();
//	}
//
//	private void addIncludedSelfDefCss(StringBuffer buf, String appPath, String ctxPath, String filePath) {
//		String scriptPath = appPath + filePath;
//		File scriptFile = new File(scriptPath);
//		if(scriptFile.exists())
//			buf.append("<link rel='STYLESHEET' type='text/css' href='" + ctxPath + filePath + "'>\n");
//	}
//	
//	/**
//	 * ��չ����CSS�ӿڣ�������Ҫ��д�˷��� <b>ע��:ʵ�ֹ����п��������Ƿ��Ż��������������isCustomized()����������</b>
//	 * 
//	 * @param buf
//	 */
//	protected void addExtendCss(StringBuffer buf) {
//		if (StringUtils.isNotEmpty(includeCss)) {
//			String[] csss = includeCss.split(",");
//			for (String css : csss) {
//				buf.append(addPublicInclude(css));
//			}
//		}
//	}
//
//	/**
//	 * ��չ����js�ӿڣ�������Ҫ��д�˷��� <b>ע��:ʵ�ֹ����п��������Ƿ��Ż��������������isCustomized()����������</b>
//	 * 
//	 * @param buf
//	 */
//	protected void addExtendJs(StringBuffer buf) {
//		if (StringUtils.isNotEmpty(includeJs)) {
//			String[] jss = includeJs.split(",");
//			for (String js : jss) {
//				buf.append(addPublicInclude(js));
//			}
//		}
//	}
//
//	protected String addPublicInclude(String fileName) {
//		StringBuffer includeHead = new StringBuffer();
//		StringBuffer includeTrail = new StringBuffer();
//		String folder = "includejs";
//		if (fileName.toLowerCase().endsWith(".css")) {
//			folder = "includecss";
//			includeHead.append("<link rel='STYLESHEET' type='text/css' href='");
//			includeTrail.append("'>\n");
//		} else {
//			includeHead.append("<script type='text/javascript' src='");
//			includeTrail.append("'></script>\n");
//		}
//		String path = "/html/nodes/" + folder + "/" + fileName;
//		boolean exist = ContextResourceUtil.resourceExist(path);
//		if (exist) {
//			includeHead.append(LfwRuntimeEnvironment.getRootPath() + "/html/nodes/" + folder + "/" + fileName);
//			includeHead.append(includeTrail);
//			return includeHead.toString();
//		}
//		return "";
//	}

//	/**
//	 * ��������Ӧ��ģ���Լ���ThemePath
//	 * 
//	 * @return
//	 */
//	protected String getThemePath() {
//		return globalPath + "/themes/" + themeId;
//	}
//
//	public String getGlobalPath() {
//		if (globalPath == null)
//			return LfwRuntimeEnvironment.getLfwCtx() + "/frame";
//		return globalPath;
//	}
//
//	public void setGlobalPath(String globalPath) {
//		this.globalPath = globalPath;
//	}
//
//	public String getPageId() {
//		if (pageId == null)
//			pageId = (String) getPageContext().getRequest().getAttribute(WebConstant.PERSONAL_PAGE_ID_KEY);
//		if (pageId == null)
//			pageId = getPageContext().getRequest().getParameter("pageId");
//		return pageId;
//	}
//
//	public void setPageId(String pageId) {
//		this.pageId = pageId;
//	}
//
//	public String getThemeId() {
//		if (themeId == null)
//			themeId = LfwRuntimeEnvironment.getThemeId();
//		return themeId;
//	}
//
//	public void setThemeId(String theme) {
//		this.themeId = theme;
//	}
//
	/**
	 * �Ƿ�����Ż����롣��ν�Ż����룬�����뾭�����ǵĹ����Ż����ɵ�js�ļ��������С���������١��������ڲ�Ʒ���������Ż���������ڿ������Խ׶�
	 */
	private boolean isCustomized() {
		return !(LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DEBUG) || LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_DESIGN));
	}
//
//	public String getLfwPath() {
//		return lfwPath;
//	}
//
//	public void setLfwPath(String lfwPath) {
//		this.lfwPath = lfwPath;
//	}
//
//	public String getLfwThemePath() {
//		return currThemePath;
//	}
//
//	public void setLfwThemePath(String lfwThemePath) {
//		this.currThemePath = lfwThemePath;
//	}
//
//	public boolean isJsBasic() {
//		return jsBasic;
//	}
//
//	public void setJsBasic(boolean jsBasic) {
//		this.jsBasic = jsBasic;
//	}
//
//	public boolean isJsBinding() {
//		return jsBinding;
//	}
//
//	public void setJsBinding(boolean jsBinding) {
//		this.jsBinding = jsBinding;
//	}
//
//	public boolean isJsContainer() {
//		return jsContainer;
//	}
//
//	public void setJsContainer(boolean jsContainer) {
//		this.jsContainer = jsContainer;
//	}
//
//	public boolean isJsDialog() {
//		return jsDialog;
//	}
//
//	public void setJsDialog(boolean jsDialog) {
//		this.jsDialog = jsDialog;
//	}
//
//	public boolean isJsMvc() {
//		return jsMvc;
//	}
//
//	public void setJsMvc(boolean jsMvc) {
//		this.jsMvc = jsMvc;
//	}
//
//	public boolean isJsNormal() {
//		return jsNormal;
//	}
//
//	public void setJsNormal(boolean jsNormal) {
//		this.jsNormal = jsNormal;
//	}
//
//	public boolean isJsEditor() {
//		return jsEditor;
//	}
//
//	public void setJsEditor(boolean jsEditor) {
//		this.jsEditor = jsEditor;
//	}
//
//	public boolean isJsMenu() {
//		return jsMenu;
//	}
//
//	public void setJsMenu(boolean jsMenu) {
//		this.jsMenu = jsMenu;
//	}
//
//	public boolean isJquery() {
//		return jquery;
//	}
//
//	public void setJquery(boolean jquery) {
//		this.jquery = jquery;
//	}
//
//	public String getIncludeCss() {
//		return includeCss;
//	}
//
//	public void setIncludeCss(String includeCss) {
//		this.includeCss = includeCss;
//	}
//
//	public String getIncludeJs() {
//		return includeJs;
//	}
//
//	public void setIncludeJs(String includeJs) {
//		this.includeJs = includeJs;
//	}
//
//	public boolean isJsChart() {
//		return jsChart;
//	}
//
//	public void setJsChart(boolean jsChart) {
//		this.jsChart = jsChart;
//	}
//
//	public boolean isJsExcel() {
//		return jsExcel;
//	}
//
//	public void setJsExcel(boolean jsExcel) {
//		this.jsExcel = jsExcel;
//	}
//
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
////		this.type = type;
//	}

}
