package nc.uap.lfw.ra.render;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;

/**
 * @author renxh layout panel ��Ⱦ��
 * @param <T>
 * @param <K>
 */
public abstract class UILayoutPanelRender<T extends UILayoutPanel, K extends WebElement> extends UIRender<T, K> {
	private static final long serialVersionUID = -5166629121473789042L;
	
	public UILayoutPanelRender(T uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<?, ?> parentRender) {
		super(uiEle, null);
		this.setPageMeta(pageMeta);
		this.setUiMeta(uimeta);
		this.setParentRender(parentRender);
		UILayoutPanel panel = this.getUiElement();
		this.id = (String) panel.getAttribute(UILayoutPanel.ID);
		this.widget = (String) panel.getAttribute(UILayoutPanel.WIDGET_ID);
		if (this.widget == null && parentRender != null) {
			this.widget = parentRender.getWidget();
		}
		
		if(parentRender == null)
			divId = DIV_PRE + id;
		else
			divId = DIV_PRE + this.getParentRender().getDivId() + "_" + id;
//		divId = this.getParentRender().getDivId() + "_" + id;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.uap.lfw.ra.render.UIRender#createRenderHtml()
	 */
	public String createRenderHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtml());
		UILayoutPanel panel = this.getUiElement();

		// ��Ⱦ�ӽڵ�
		IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel.getElement(), getPageMeta(), this, DriverPhase.PC);
		if (render != null) {
			buf.append(render.createRenderHtml());
		}
		// ��Ⱦ�ӽڵ�

		buf.append(this.generalTailHtml());
		return buf.toString();
	}

	public String createRenderHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtmlDynamic());
		UILayoutPanel panel = this.getUiElement();

		// ��Ⱦ�ӽڵ�
		IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel.getElement(), getPageMeta(), this, DriverPhase.PC);
		if (render != null) {
			buf.append(render.createRenderHtmlDynamic());
			buf.append(getDivId()).append(".appendChild(").append(render.getNewDivId()).append(");\n");
		}

		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.uap.lfw.ra.render.UIRender#createRenderScript()
	 */
	public String createRenderScript() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadScript());
		buf.append(this.generalHeadScript());

		// ��Ⱦ�ӽڵ�
		UILayoutPanel panel = this.getUiElement();
		IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel.getElement(), getPageMeta(), this, DriverPhase.PC);
		if (render != null) {
			buf.append(render.createRenderScript());
		}
		// ��Ⱦ�ӽڵ�

		buf.append(this.generalTailScript());
		buf.append(this.generalEditableTailScript());
		return buf.toString();
	}

	public String createRenderScriptDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadScriptDynamic());
		buf.append(this.generalHeadScript());
		
		// ��Ⱦ�ӽڵ�
		UILayoutPanel panel = this.getUiElement();
		IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel.getElement(), getPageMeta(), this, DriverPhase.PC);
		if (render != null) {
			buf.append(render.createRenderScriptDynamic());
		}
		
		buf.append(this.generalTailScript());
		buf.append(this.generalEditableTailScriptDynamic());
		return buf.toString();
	}
	
	

	@Override
	public String generalEditableHeadHtml() {
		if (isEditMode()) {
			StringBuffer buf = new StringBuffer("");
			buf.append("<div ");
			buf.append(" id='").append(getDivId()).append("'");
			buf.append(" style='height:100%;min-height:" + MIN_HEIGHT + ";overflow:auto;'");
			buf.append(">");
			return buf.toString();
		}
		return "";
	}

	public String generalHeadHtml() {

		return "";
	}

	/**
	 * 2011-10-13 ����02:31:16 renxh des������dom����ռλ��DIV
	 * 
	 * @return
	 */
	public abstract String generalHeadHtmlDynamic();


	/**
	 * 2011-10-13 ����02:59:59 renxh des���ɱ༭̬ͷ�ű�
	 * 
	 * @return
	 */
	public String generalEditableHeadScriptDynamic() {
		return super.generalEditableHeadScriptDynamic();
	}

	/**
	 * 2011-10-13 ����03:00:12 renxh des���ɱ༭̬β�ű�
	 * 
	 * @return
	 */
	public String generalEditableTailScriptDynamic() {
		return super.generalEditableTailScriptDynamic();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.uap.lfw.ra.render.UIRender#generalHeadScript() ����ͷ����DIV
	 */
	public String generalHeadScript() {

		return "";
	}

	public String generalTailHtml() {

		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableTailHtml());
		buf.append("</div>\n");
		return buf.toString();
	}

	public String generalTailScript() {

		return "";
	}

	protected String getSourceType(WebElement ele) {
		return null;
	}

	/**
	 * Object obj Ҫɾ���Ķ���
	 */
	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		String divId = this.getDivId();
		StringBuffer buf = new StringBuffer();
//		UILayoutPanel panel = this.getUiElement();
		if (divId != null) {
			UIElement child = (UIElement) obj;
			if (child != null) {
				child.notifyChange(UIElement.DESTROY, this);
			}
			buf.append("layoutInitFunc();\n");
		} else {
			buf.append("alert('ɾ��panelʧ�ܣ�δ���divId')");
		}
		addDynamicScript(buf.toString());
	}

	/**
	 * Object obj ���ڵ�
	 */
	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		String divId = this.getDivId();
		StringBuffer buf = new StringBuffer();
		UILayoutPanel panel = this.getUiElement();
		if (divId != null) {
			UIElement child = panel.getElement();
			if (child != null) {
				child.notifyChange(UIElement.DESTROY, this);
			}
			buf.append("window.execDynamicScript2RemovePanel('" + divId + "');\n");
			buf.append("layoutInitFunc();\n");
		} else {
			buf.append("alert('ɾ��panelʧ�ܣ�δ���divId')");
		}

		addDynamicScript(buf.toString());
	}

	/**
	 * Object obj Ҫ��ӵĶ���
	 */
	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		StringBuffer dsBuf = (StringBuffer) this.getContextAttribute(UIRender.DS_SCRIPT);
		if(dsBuf == null){
			dsBuf = new StringBuffer();
			this.setContextAttribute(UIRender.DS_SCRIPT, dsBuf);
		}
		String divId = this.getDivId();
		UIElement ele = (UIElement) obj;
		IControlPlugin plugin = ControlFramework.getInstance().getControlPluginByUIClass(ele.getClass());
		IUIRender render = plugin.getUIRender(uiMeta, ele, pageMeta, this, DriverPhase.PC);
		
		StringBuffer buf = new StringBuffer();
		String html = render.createRenderHtmlDynamic();
		buf.append(html);
		
		
		buf.append("$ge('" + divId + "').appendChild(" + render.getNewDivId() + ");\n");
		buf.append(render.createRenderScriptDynamic());
		
		addDynamicScript(buf.toString());
	}


	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		if(UIElement.VISIBLE.equals(obj)){
			boolean visble = getUiElement().getVisible();
			addDynamicScript("document.getElementById('"+ getNewDivId() +"').style.display=\""+ (visble? "" : "none") +"\";\n");
		}
	}
	

	protected String getBorderString(boolean script) {
		UILayoutPanel panel = getUiElement();
		StringBuffer buf = new StringBuffer();
		String border = panel.getBorder();
		if(border != null && !border.equals("")){
			buf.append("border:").append(border).append(";");
		}
		
		String leftBorder = panel.getLeftBorder();
		if(leftBorder != null && !leftBorder.equals("")){
			buf.append("border-left:").append(getThemeBorder(leftBorder)).append(";");
		}
		String rightBorder = panel.getRightBorder();
		if(rightBorder != null && !rightBorder.equals("")){
			buf.append("border-right:").append(getThemeBorder(rightBorder)).append(";");
		}
		String topBorder = panel.getTopBorder();
		if(topBorder != null && !topBorder.equals("")){
			buf.append("border-top:").append(getThemeBorder(topBorder)).append(";");
		}
		String bottomBorder = panel.getBottomBorder();
		if(bottomBorder != null && !bottomBorder.equals("")){
			buf.append("border-bottom:").append(getThemeBorder(bottomBorder)).append(";");
		}
		if(buf.length() != 0){
			return buf.toString() + ";";
		}
		return "";
	}
	
	private String getThemeBorder(String border){
		if(border.equals("#"))
			border = "1px solid #d1e2d8";
		return border;
	}
	
	protected String getMarginLeftString(boolean script) {
		UILayoutPanel panel = getUiElement();
		String leftPadding = panel.getLeftPadding();
		if(leftPadding == null || leftPadding.equals(""))
			return "";
		return "padding-left:" + leftPadding + "px;";
	}

	protected String getMarginRightString(boolean script) {
		UILayoutPanel panel = getUiElement();
		String rightPadding = panel.getRightPadding();
		if(rightPadding == null || rightPadding.equals(""))
			return "";
		return "padding-right:" + rightPadding + "px;";
	}
	
	protected String getMarginBottomString(boolean script) {
		UILayoutPanel panel = getUiElement();
		String bottomPadding = panel.getBottomPadding();
		if(bottomPadding == null || bottomPadding.equals(""))
			return "";
		return "padding-bottom:" + bottomPadding + "px;";
	}
	
	protected String getMarginTopString(boolean script) {
		UILayoutPanel panel = getUiElement();
		String topPadding = panel.getTopPadding();
		if(topPadding == null || topPadding.equals(""))
			return "";
		return "padding-top:" + topPadding + "px;";
	}

}
