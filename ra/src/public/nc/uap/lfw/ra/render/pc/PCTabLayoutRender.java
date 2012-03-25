package nc.uap.lfw.ra.render.pc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.lfw.jsp.uimeta.UITabRightPanel;
import nc.uap.lfw.jsp.uimeta.UpdatePair;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh 页签布局渲染器
 * 
 */
public class PCTabLayoutRender extends UILayoutRender<UITabComp, WebElement> {
	public static final String TAB_LAYOUT_BASE = "tab_layout_";
	private String className;
	private int currentIndex = 0;
	private boolean oneTabHide = false;
	private String tabType = "top";
	private List<String> itemList = new ArrayList<String>();
	
	public PCTabLayoutRender(UITabComp uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UITabComp tabLayout = this.getUiElement();

		if (this.widget == null) {
			this.widget = tabLayout.getWidgetId() == null ? "" : tabLayout.getWidgetId();
		}

		divId = DIV_PRE + (this.widget.equals("") ? TAB_LAYOUT_BASE : this.widget + "_") + this.id;

		varId = COMP_PRE + (this.widget.equals("") ? TAB_LAYOUT_BASE : this.widget + "_") + getId();

		this.oneTabHide = tabLayout.getOneTabHide() == null ? false : (tabLayout.getOneTabHide() == UITabComp.ONETAB_HIDE_HIDE ? true : false);
		this.tabType = (tabLayout.getTabType() == null || tabLayout.getTabType().equals("")) ? "top" : tabLayout.getTabType();
		try {
			this.currentIndex = tabLayout.getCurrentItem() == null ? 0 : Integer.parseInt(tabLayout.getCurrentItem());
		} catch (Exception e) {
			this.currentIndex = 0;
		}

	}

	public String createRenderHtml() {

		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtml());
		UITabComp layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel, getPageMeta(), this, DriverPhase.PC);
			if (render != null)
				buf.append(render.createRenderHtml());
		}
		UITabRightPanel rightPanel = layout.getRightPanel();
		if (rightPanel != null) {
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), rightPanel, getPageMeta(), this, DriverPhase.PC);
			if (render != null)
				buf.append(render.createRenderHtml());
		}

		buf.append(this.generalTailHtml());
		return buf.toString();

	}

	public String createRenderHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtmlDynamic());
		
		UITabComp layout = getUiElement();
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
		UITabRightPanel rightPanel = layout.getRightPanel();
		if (rightPanel != null) {
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), rightPanel, getPageMeta(), this, DriverPhase.PC);
			if (render != null)
				buf.append(render.createRenderHtmlDynamic());
		}
		return buf.toString();
	}

	public String createRenderScript() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadScript());
		buf.append(this.generalHeadScript());
		// 子节点
		UITabComp layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel, getPageMeta(), this, DriverPhase.PC);
			if (render != null)
				buf.append(render.createRenderScript());
		}
		buf.append(this.generalTailScript());
		UITabRightPanel rightPanel = layout.getRightPanel();
		if (rightPanel != null) {
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), rightPanel, getPageMeta(), this, DriverPhase.PC);
			if (render != null)
				buf.append(render.createRenderScript());
		}
		buf.append(this.generalEditableTailScript());
		return wrapByRequired(getType(), buf.toString());
	}
	
	@Override
	public String createRenderScriptDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadScript());
		buf.append(this.generalHeadScript());
		// 子节点
		UITabComp layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel, getPageMeta(), this, DriverPhase.PC);
			if (render != null)
				buf.append(render.createRenderScriptDynamic());
		}
		UITabRightPanel rightPanel = layout.getRightPanel();
		if (rightPanel != null) {
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), rightPanel, getPageMeta(), this, DriverPhase.PC);
			if (render != null)
				buf.append(render.createRenderScriptDynamic());
		}
		buf.append(this.generalTailScript());
		buf.append(this.generalEditableTailScript());
		return wrapByRequired(getType(), buf.toString());
	}

	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"").append(getNewDivId());
		buf.append("\" style=\"top:").append(0);
		buf.append(";left:").append(0);
		buf.append(";width:").append("100%");
		buf.append(";height:").append("100%");
		buf.append(";padding-left:").append(0);
		buf.append(";padding-top:").append(0);
		buf.append(";padding-right:").append(0);
		buf.append(";padding-bottom:").append(0);
		buf.append(";overflow:hidden;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}

	public String generalHeadScript() {

		StringBuffer buf = new StringBuffer();
		String showId = getVarId();
		buf.append("var ").append(showId).append(" = new TabComp(document.getElementById(\"");
		buf.append(getDivId()).append("\"), \"");
		buf.append(getId()).append("\", 0, 0, \"100%\", \"100%\", '-1',");
		buf.append("{tabType:'").append(this.tabType).append("'")
		   .append(",flowmode:").append(isFlowMode())
		   .append("},");
		if (className != null && !className.equals(""))
			buf.append("\"").append(className).append("\");\n");
		else
			buf.append("'');\n");

		if (getWidget() != null && getWidget().length() > 0) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getWidget('").append(this.getCurrWidget().getId()).append("');\n");
			buf.append(widget + ".addTab(" + getVarId() + ");\n");
		} else {
			buf.append("pageUI.addTab(" + getVarId() + ");\n");
		}
		
		UITabComp tab = this.getUiElement();
		if (tab != null) {
			buf.append(addEventSupport(tab, getWidget(), showId, null));
		}
//		TabLayout tab =getTab();
//		if (tab != null) {
//			buf.append(addEventSupport(tab, getWidget(), showId, null));
//		}
		return buf.toString();
	}

	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	public String generalTailScript() {

		StringBuffer buf = new StringBuffer();
		UITabComp tab = getUiElement();
//			List<TabItem> items = getTab().getItemList();
		List<UILayoutPanel> items = tab.getPanelList();
		int size = items.size();
		for (int i = 0; i < size; i++) {
			UITabItem tabItem = (UITabItem) items.get(i);
			if (tabItem.getActive() != null && tabItem.getActive().equals("1")) {
				buf.append(getVarId()).append(".activeTab(").append(i).append(");\n");
			}
		}
		buf.append(getVarId()).append(".activeTab(").append(currentIndex).append(");\n");

		// 如果仅仅有一个Tab，则隐藏
		if (itemList.size() == 1 && oneTabHide) { //
			buf.append(getVarId()).append(".hideTabHead();\n");
		}

		return buf.toString();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public boolean isOneTabHide() {
		return oneTabHide;
	}

	public void setOneTabHide(boolean oneTabHide) {
		this.oneTabHide = oneTabHide;
	}

	public List<String> getItemList() {
		return itemList;
	}

	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_TAG;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		//buf.append("debugger;\n");
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.height = '100%';\n");
		buf.append(getNewDivId()).append(".style.overflow = 'hidden';\n");
		buf.append(getNewDivId()).append(".style.top = '0';\n");
		buf.append(getNewDivId()).append(".style.left = '0';\n");
		buf.append(getNewDivId()).append(".style.paddingLeft = '0';\n");
		buf.append(getNewDivId()).append(".style.paddingTop = '0';\n");
		buf.append(getNewDivId()).append(".style.paddingRight = '0';\n");
		buf.append(getNewDivId()).append(".style.paddingBottom = '0';\n");
		buf.append(getNewDivId()).append(".id = '" + getNewDivId() + "';\n");
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(getNewDivId()).append(".appendChild(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		UpdatePair pair = (UpdatePair) obj;
		if(pair == null)
			return;
		if(pair.getKey() != null){
			if(pair.getKey().equals(UITabComp.CURRENT_ITEM)){
				String widgetId = getWidget();
				StringBuffer buf = new StringBuffer();
				buf.append("pageUI");
				if(widgetId != null && widgetId.length() > 0)
					buf.append(".getWidget(\""+ getWidget() +"\")");
				buf.append(".getTab(\""+ getId() +"\")").append(".activeTab(").append(pair.getValue()).append(");\n");
				addBeforeExeScript(buf.toString());
			}
			else if(pair.getKey().equals(UITabComp.ONETAB_HIDE)){
				String widgetId = getWidget();
				StringBuffer buf = new StringBuffer();
				buf.append("pageUI");
				if(widgetId != null && widgetId.length() > 0)
					buf.append(".getWidget(\""+ getWidget() +"\")");
				buf.append(".getTab(\""+ getId() +"\")").append(".setOnTabHide(").append(pair.getValue()).append(");\n");
				addBeforeExeScript(buf.toString());
			}
		}
		if(UITabComp.CURRENT_ITEM.equals(obj)){
			String widgetId = getWidget();
			StringBuffer buf = new StringBuffer();
			buf.append("pageUI");
			if(widgetId != null && widgetId.length() > 0)
				buf.append(".getWidget(\""+ getWidget() +"\")");
			buf.append(".getTab(\""+ getId() +"\")").append(".activeTab(").append(currentIndex).append(");\n");
			addBeforeExeScript(buf.toString());
		}
		else if (UITabComp.ONETAB_HIDE.equals(obj)){
			String widgetId = getWidget();
			StringBuffer buf = new StringBuffer();
			buf.append("pageUI");
			if(widgetId != null && widgetId.length() > 0)
				buf.append(".getWidget(\""+ getWidget() +"\")");
			buf.append(".getTab(\""+ getId() +"\")").append(".setOnTabHide(").append(oneTabHide).append(");\n");
			addBeforeExeScript(buf.toString());
		}
	}

}
