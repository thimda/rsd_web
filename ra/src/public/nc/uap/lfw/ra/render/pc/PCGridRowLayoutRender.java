package nc.uap.lfw.ra.render.pc;

import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIGridRowLayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

public class PCGridRowLayoutRender extends UILayoutRender<UIGridRowLayout, WebElement> {

	public PCGridRowLayoutRender(UIGridRowLayout uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UIGridRowLayout gridRow = this.getUiElement();
		this.id = uiEle.getId();
		if (this.id == null || this.id.equals("")) {
			this.id = this.getUniqueId(GRID_ID_ROW);
		}
		if (parentRender != null && parentRender.getDivId() != null) {
			this.divId = parentRender.getDivId() + "_" + this.id;
		} else {
			this.divId = DIV_PRE + this.id;
		}
		this.rowHeight = gridRow.getRowHeight();
		parentRender.setDivId(getDivId());

	}

	protected static final String GRID_ID_ROW = "grid_row_";
	private String rowHeight;

	@Override
	public String createRenderHtml() {

		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtml());

		// 子节点
		UIGridRowLayout gridRow = this.getUiElement();
		List<UILayoutPanel> panels = gridRow.getPanelList();
		for (int i = 0; i < panels.size(); i++) {
			UILayoutPanel panel = panels.get(i);
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel, this.getPageMeta(), this, DriverPhase.PC);
			if (render != null) {
				buf.append(render.createRenderHtml());
			}
		}
		// 子节点

		buf.append(this.generalTailHtml());
		return buf.toString();
	}

	/**
	 * 创建tr的脚本，同时会创建td
	 * 
	 * @return
	 */
	public String createRenderHtmlDynamic() {
		return super.createRenderHtmlDynamic();
	}

	@Override
	public String createRenderScript() {

//		StringBuffer buf = new StringBuffer();
//		buf.append(this.generalHeadScript());
//
//		// 子节点
//		UIGridRowLayout gridRow = this.getUiElement();
//		List<UILayoutPanel> panels = gridRow.getPanelList();
//		for (int i = 0; i < panels.size(); i++) {
//			UILayoutPanel panel = panels.get(i);
//			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel, this.getPageMeta(), this, DriverPhase.PC);
//			if (render != null) {
//				buf.append(render.createRenderScript());
//			}
//		}
//		// 子节点
//
//		buf.append(this.generalTailScript());
//		super.createRenderScript()
//		return buf.toString();
		return super.createRenderScript();
	}

	public String createRenderScriptDynamic() {
//		StringBuffer buf = new StringBuffer();
//		buf.append(this.generalHeadScriptDynamic());
//		buf.append(this.generalTailScriptDynamic());
//		return buf.toString();
		return super.createRenderScriptDynamic();
	}

	@Override
	public String generalHeadHtml() {

		StringBuffer buf = new StringBuffer();
		if (this.getRowHeight() != null) {
			buf.append("<tr height=\"" + this.getRowHeight() + "\" id='" + this.getDivId() + "' isHeight='1'>");
		} else {
			buf.append("<tr id='" + this.getDivId() + "' isHeight='0'>");
		}

		return buf.toString();
	}

	@Override
	public String generalTailHtml() {

		StringBuffer buf = new StringBuffer();
		buf.append("</tr>");
		return buf.toString();
	}

	public String getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(String rowHeight) {
		this.rowHeight = rowHeight;
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_GRIDROW;
	}

	@Override
	public String generalHeadHtmlDynamic() {
//		PageMeta pageMeta = LfwRuntimeEnvironment.getWebContext().getPageMeta();
//		UIMeta uiMeta = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getDivId()).append(" = $ce('TR');\n");
		buf.append(getDivId()).append(".id = '" + getDivId() + "';\n");
		if (this.getRowHeight() != null) {
			buf.append(getDivId()).append(".style.height='" + this.rowHeight + "';\n");
			buf.append(getDivId()).append(".isHeight='0';\n");
		} else {
			buf.append(getDivId()).append(".isHeight='0';\n");
		}
		return buf.toString();
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyDestroy(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		UIGridRowLayout row = this.getUiElement();
		Iterator<UILayoutPanel> cells = row.getPanelList().iterator();
		while (cells.hasNext()) {
			UILayoutPanel cell = cells.next();
			cell.notifyChange(UILayoutPanel.DESTROY, this);
		}
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getDivId()).append(" = $ge('" + getDivId() + "');\n");
		buf.append("var parent = ").append(getDivId()).append(".parentNode;\n");
		buf.append("if(parent)parent.removeChild(" + getDivId() + ");\n");
		addDynamicScript(buf.toString());
	}

	@Override
	public void notifyRemoveChild(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		if (obj != null) {
			UILayoutPanel cell = (UILayoutPanel) obj;
			cell.notifyChange(UILayoutPanel.DESTROY, this);
//			this.removeUIElement(this.getUiElement(), cell);
		}

	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {

		super.notifyUpdate(uiMeta, pageMeta, obj);
	}

	@Override
	public String generalHeadScript() {

		return "";
	}

	@Override
	public String generalTailScript() {

		return "";
	}

	@Override
	public String generalEditableHeadHtml() {

		return "";
	}

	@Override
	public String generalEditableHeadScript() {

		return "";
	}

	@Override
	public String generalEditableTailHtml() {

		return "";
	}

	@Override
	public String generalEditableTailScript() {

		return "";
	}

	@Override
	public String generalEditableHeadScriptDynamic() {
		return "";
	}

	@Override
	public String generalEditableTailScriptDynamic() {

		return "";
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNewDivId() {
		return this.getDivId();
	}
}
