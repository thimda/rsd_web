package nc.uap.lfw.ra.render;

import java.util.Iterator;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.itf.IUIRender;

/**
 * @author renxh UIMeta��Ⱦ��
 * 
 */
public class UIMetaRender extends UILayoutPanelRender<UIMeta, WebElement> {

	public UIMetaRender(UIMeta uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		this.id = uiEle.getId();
		this.widget = uiEle.getWidgetId() == null ? "" : (String) uiEle.getWidgetId();
		if(this.widget == null || this.widget.equals("")){
			if(this.id == null)
				this.id = "_win_um";
			this.divId = DIV_PRE + this.id;
		}
		else{
			if(this.id == null)
				this.id = this.widget + "_um";
			this.divId = DIV_PRE + this.id;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.uap.lfw.ra.render.UILayoutPanelRender#createRenderHtml()
	 */
	public String createRenderHtml() {
		StringBuffer buf = new StringBuffer("");
		buf.append(this.generalHeadHtml());
		UIMeta uimeta = this.getUiMeta();
		UIMeta wuimeta = this.getUiElement();
		IUIRender render = ControlFramework.getInstance().getUIRender(uimeta, wuimeta.getElement(), this.getPageMeta(), this.getParentRender(), DriverPhase.PC);
		if (render != null){
//			render.setUiMeta(wuimeta);
			buf.append(render.createRenderHtml());
		}
		Map<String, UIWidget> dialogMap = wuimeta.getDialogMap(); // ����dialog
		if (dialogMap != null) {
			Iterator<String> keys = dialogMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				IUIRender dialogRender = ControlFramework.getInstance().getUIRender(wuimeta, dialogMap.get(key), this.getPageMeta(), null, DriverPhase.PC);
				buf.append(dialogRender.createRenderHtml());
			}
		}
		buf.append(this.generalTailHtml());
		return buf.toString();
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.uap.lfw.ra.render.UILayoutPanelRender#createRenderScript()
	 */
	public String createRenderScript() {
		StringBuffer buf = new StringBuffer("");
		buf.append(this.generalEditableHeadScript());
		buf.append(this.generalHeadScript());
		UIMeta uimeta = this.getUiMeta();
		UIMeta wuimeta = this.getUiElement();
		IUIRender render = ControlFramework.getInstance().getUIRender(uimeta, wuimeta.getElement(), this.getPageMeta(), this.getParentRender(), DriverPhase.PC);
		if (render != null)
			buf.append(render.createRenderScript());

		Map<String, UIWidget> dialogMap = wuimeta.getDialogMap(); // ����dialog
		if (dialogMap != null) {
			Iterator<String> keys = dialogMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				IUIRender dialogRender = ControlFramework.getInstance().getUIRender(uimeta, dialogMap.get(key), this.getPageMeta(), null, DriverPhase.PC);
				buf.append(dialogRender.createRenderScript());
			}
		}
		buf.append(this.generalTailScript());
		buf.append(this.generalEditableTailScript());
		return buf.toString();
	}
	
	public String createRenderScriptDynamic() {
		return super.createRenderScriptDynamic();
	}

	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer("");
		UIMeta wuimeta = this.getUiElement();
		String height = "height:100%;";
//		if(wuimeta.getFlowmode().booleanValue())
//			height = "min-height:100%;";
		buf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;" + height + "top:0px;left:0px;position:relative;\">");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append("=$ce('DIV');\n");
		buf.append(newDivId).append(".style.width='100%';");
		buf.append(newDivId).append(".style.height='100%';\n");
		buf.append(newDivId).append(".style.top='0px';\n");
		buf.append(newDivId).append(".style.left='0px';\n");
		buf.append(newDivId).append(".style.position='relative';\n");
		buf.append(newDivId).append(".id='"+newDivId+"';\n");
		if(this.isEditMode()){
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

	public String generalHeadScript() {
		return "";
	}
	
	public String generalTailHtml() {
		StringBuffer buf = new StringBuffer("");
		buf.append(this.generalEditableTailHtml());
		buf.append("</div>\n");
		return buf.toString();
	}

	public String generalTailScript() {
		return "";
	}

	@Override
	public String generalEditableHeadHtml() {
		if (isEditMode()) {
			StringBuffer buf = new StringBuffer("");
			buf.append("<div ");
			buf.append(" id='").append(getDivId()).append("'");
			buf.append(" style='height:100%;min-height:" + MIN_HEIGHT + ";margin:0px;padding:0px'");
			buf.append(">");
			return buf.toString();
		}
		return "";
	}
	@Override
	public String generalEditableHeadHtmlDynamic() {
		if (isEditMode()) {
			StringBuffer buf = new StringBuffer("");
			buf.append("var ").append(getDivId()).append(" = $ce('DIV');\n");
			buf.append(getDivId()).append(".style.height = '100%';\n");
//			buf.append(getDivId()).append(".style.borderWidth = '0px';\n");
			buf.append(getDivId()).append(".style.margin = '0px';\n");
			buf.append(getDivId()).append(".style.padding = '0px';\n");
			buf.append(getDivId()).append(".id='"+getDivId()+"';\n");
			return buf.toString();
		}
		return "";
	}

	@Override
	public String generalEditableTailHtml() {
		if (isEditMode()) {
			String tail = "</div>\n";
			return tail;
		}
		return "";
	}

	/**
	 * 2011-8-4 ����06:52:05 renxh des����Ⱦ�༭̬ͷ �ű�
	 * 
	 * @return
	 */
	@Override
	public String generalEditableHeadScript() {
		return "";
	}
	
	@Override
	public String generalEditableHeadScriptDynamic() {
		return "";
	}

	@Override
	public String generalEditableTailScript() {
		if (isEditMode()) {
			String widgetId = this.getWidget() == null ? "" : this.getWidget();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getAttribute(UIElement.ID);
			String eleid = "";
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			StringBuffer buf = new StringBuffer();
			if (getDivId() == null) {
				LfwLogger.error("div id is null!" + this.getClass().getName());
			} else {
				boolean isWinEditmode = LfwRuntimeEnvironment.isWindowEditorMode();
				boolean isWidget = this.widget != null && !"".equals(this.widget);
				if(!(isWinEditmode && isWidget)){
					buf.append(this.addEditableListener(getDivId(), widgetId, uiid, null, eleid, null, type));
				}
			}

			return buf.toString();
		}
		return "";
	}

	/**
	 * 2011-9-14 ����10:18:37 renxh des��Ϊ������ӿɱ༭����ʽ������Ժ��ɿɱ༭��ʽ
	 * 
	 * @param divId
	 * @param widgetId
	 * @param uiId
	 * @param eleId
	 * @param type
	 * @return
	 */
	
	@Override
	public String generalEditableTailScriptDynamic() {
		return super.generalEditableTailScriptDynamic();
	}

	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyDestroy(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyUpdate(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyRemoveChild(uiMeta, pageMeta, obj);
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_UIMETA;
	}
	
	/**
	 * 2011-9-14 ����10:18:37 renxh des��Ϊ������ӿɱ༭����ʽ������Ժ��ɿɱ༭��ʽ
	 * 
	 * @param divId
	 * @param widgetId
	 * @param uiId
	 * @param eleId
	 * @param type
	 * @return
	 */
//	protected String addEditableListener(String divId, String widgetId, String uiId, String subuiId, String eleId, String subEleId, String type) {
//		UIMeta um = getUiElement();
//		if(um.getLayout() == null)
//			return "";
//		return super.addEditableListener(divId, widgetId, uiId, subuiId, eleId, subEleId, type);
//	}
	
}
