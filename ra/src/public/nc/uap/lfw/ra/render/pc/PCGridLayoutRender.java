package nc.uap.lfw.ra.render.pc;

import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIGridLayout;
import nc.uap.lfw.jsp.uimeta.UIGridRowLayout;
import nc.uap.lfw.jsp.uimeta.UIGridRowPanel;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh 表格布局渲染器
 * @param <T>
 * @param <K>
 */
public class PCGridLayoutRender extends UILayoutRender<UIGridLayout, WebElement> {
	// Grid 布局ID基础字符串
	protected static final String GRID_ID_BASE = "grid_layout_";
	
	// 行数
	private int rowcount = 0;
	
	// 列数
	private int colcount = 0;
	
	private String border;
	
	private String className;
	
	// 用来完成GridPanel的计数
	protected int childCount = 0;
	

	public PCGridLayoutRender(UIGridLayout uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UIGridLayout layout = this.getUiElement();
		this.id = layout.getId();
		if (id == null || this.id.equals("")) {
			throw new LfwRuntimeException(this.getClass().getName() + ":id不能为空！");
		}
		divId = DIV_PRE + id;
		this.rowcount = layout.getRowcount();
		this.colcount = layout.getColcount();
		this.border = this.getFormatSize(layout.getBorder());
		this.className = layout.getClassName();
	}


	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}


	public String createRenderHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtml());
		UIGridLayout layout = this.getUiElement();
		List<UILayoutPanel> gridRows = layout.getPanelList();
		if (gridRows != null) {
			this.rowcount = gridRows.size();
			for (int i = 0; i < gridRows.size(); i++) {
				UIGridRowPanel rowPanel = (UIGridRowPanel) gridRows.get(i);
				IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), rowPanel, this.getPageMeta(), this, DriverPhase.PC);
				if (render != null) {
					buf.append(render.createRenderHtml());
				}
			}
		}
		buf.append(this.generalTailHtml());
		return buf.toString();
	}

	public String createRenderScript() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadScript());
		buf.append(this.generalHeadScript());

		UIGridLayout layout = this.getUiElement();
		List<UILayoutPanel> gridRows = layout.getPanelList();
		if (gridRows != null) {
			this.rowcount = gridRows.size();
			for (int i = 0; i < gridRows.size(); i++) {
				UIGridRowPanel rowPanel = (UIGridRowPanel) gridRows.get(i);
				UIGridRowLayout gridRow = rowPanel.getRow();
				IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), gridRow, this.getPageMeta(), this, DriverPhase.PC);
				if (render != null) {
					buf.append(render.createRenderScript());
				}
			}
		}
		// 渲染子节点

		buf.append(this.generalTailScript());
		buf.append(this.generalEditableTailScript());
		return buf.toString();
	}

	public String createRenderScriptDynamic() {
		return super.createRenderScriptDynamic();
	}

	public String generalHeadHtml() {

		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadHtml());
		buf.append("<table id='").append(this.getDivId()).append("' cellpadding='0' cellspacing='0' ");
		if(null == className){
			className = "";
		}			
		buf.append("class='"+className+"' ");
		buf.append(" style='empty-cells :show; ");
		if (border != null && !border.equals("")) {
			buf.append("border-width:"+border+";' ");
		}
		else{
			buf.append("'");
		}
		buf.append(" >");
		return buf.toString();
	}

	public String generalHeadScript() {
		return "";
	}

	public String generalTailHtml() {

		StringBuffer buf = new StringBuffer();
		buf.append("</table>\n");
		buf.append(this.generalEditableTailHtml());
		return buf.toString();
	}

	@Override
	public String generalEditableTailScript() {
		if (isEditMode()) {
			String widgetId = this.getWidget() == null ? "" : this.getWidget();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			StringBuffer buf = new StringBuffer();
			if (getDivId() == null) {
				LfwLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {");
				buf.append("widgetid : '").append(widgetId).append("'");
				buf.append(",uiid : '").append(uiid).append("'");
				buf.append(",eleid : '").append(eleid).append("'");
				buf.append(",type : '").append(type).append("'");
				buf.append("};\n");
				buf.append("new EditableListener(document.getElementById('" + getDivId() + "'),params,'grid_layout');\n");
				return buf.toString();
			}

			return buf.toString();
		}
		return "";
	}

	@Override
	public String generalEditableTailScriptDynamic() {
		if (isEditMode()) {

			String widgetId = this.getWidget() == null ? "" : this.getWidget();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			StringBuffer buf = new StringBuffer();
			if (getDivId() == null) {
				LfwLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {");
				buf.append("widgetid : '").append(widgetId).append("'");
				buf.append(",uiid : '").append(uiid).append("'");
				buf.append(",eleid : '").append(eleid).append("'");
				buf.append(",type : '").append(type).append("'");
				buf.append("};\n");
				buf.append("new EditableListener(" + getDivId() + ",params,'grid_layout');\n");
				return buf.toString();
			}

			return buf.toString();
		}
		return "";
	}

	public String generalTailScript() {
		return "";
	}

	public int getRowcount() {
		return rowcount;
	}

	public void setRowcount(int rowcount) {
		this.rowcount = rowcount;
	}

	public int getColcount() {
		return colcount;
	}

	public void setColcount(int colcount) {
		this.colcount = colcount;
	}

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
	}


	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_GRIDLAYOUT;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadHtml());
		String divId = getDivId();
		buf.append("var ").append(divId).append(" = $ce('TABLE');\n");
//		buf.append("var tbody = $ce('TBODY');\n");
//		buf.append(divId).append(".appendChild(tbody);\n");
		if(null != this.className){
			buf.append(divId).append(".className = '"+className+"';\n");
		}
		
		buf.append(divId).append(".id = '" + divId + "';\n");
		if (border != null && !border.equals("")) {
			buf.append(divId).append(".style.borderWidth ='" + border + "';\n");
		}
		
		buf.append(divId).append(".setAttribute('cellspacing', '0');\n");
		buf.append(divId).append(".setAttribute('cellpadding', '0');\n");
		return buf.toString();
	}

	// 添加行
	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public String createRenderHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtmlDynamic());
		
		// 渲染子节点
		UILayout layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel, getPageMeta(), this, DriverPhase.PC);
			if (render != null){
				buf.append(render.createRenderHtmlDynamic());
				buf.append(getDivId()).append(".appendChild(").append(render.getNewDivId()).append(");\n");
			}
		}
		return buf.toString();
	}


	@Override
	public void notifyDestroy(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		UIGridLayout uiEle = this.getUiElement();
		Iterator<UILayoutPanel> rows = uiEle.getPanelList().iterator();
		while (rows.hasNext()) {
			UIGridRowPanel row = (UIGridRowPanel) rows.next();
			row.notifyChange(UIGridLayout.DESTROY, this);
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
			UIGridRowLayout row = (UIGridRowLayout) obj;
			row.notifyChange(UILayoutPanel.DESTROY, this);
		}
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadHtml());
		buf.append("var ta = $ge('" + getDivId() + "');\n");
		buf.append("var ").append(getDivId()).append(" = ta;\n");
		if (border != null && !border.equals("")) {
			buf.append(getDivId()).append(".style.borderWidth ='" + border + "';\n");
		}
		
		if (className != null) {
			buf.append(getDivId()).append(".className='" + className + "';\n");
		}
		
		buf.append("layoutInitFunc();");
		addDynamicScript(buf.toString());
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
	public String generalEditableHeadScriptDynamic() {
		return "";
	}
	
	@Override
	public String getNewDivId() {
		return this.getDivId();
	}
}
