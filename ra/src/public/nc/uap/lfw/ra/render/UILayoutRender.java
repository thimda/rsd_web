package nc.uap.lfw.ra.render;

import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.itf.IUIRender;

/**
 * @author renxh 布局渲染器
 * @param <T>
 * @param <K>
 */
public abstract class UILayoutRender<T extends UILayout, K extends WebElement> extends UIRender<T, K> {

	private static final long serialVersionUID = 8375401324830733396L;

	private int childCount = 0; // 子节点数量，默认为0

	public UILayoutRender(T uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, null);
		this.setPageMeta(pageMeta);
		this.setUiMeta(uimeta);
		this.setParentRender(parentRender);

		UILayout layout = this.getUiElement();
		this.id = (String) layout.getId();
		if (this.id == null) {
			throw new LfwRuntimeException(this.getClass().getName() + ":id不能为空！");
		}
		if (layout instanceof UIWidget) { // 如果是uiwidget，则id 则为 widgetId
			this.widget = this.id;
		} else {
			this.widget = (String) layout.getAttribute(UILayout.WIDGET_ID);
		}
		
		if (this.widget != null) {
			uiEle.setAttribute(UILayout.WIDGET_ID, this.widget);
		}
		
		this.divId = (widget == null || widget.equals("")) ? (DIV_PRE + id) : (DIV_PRE + widget + "_" + id);
		this.varId = (widget == null || widget.equals("")) ? (COMP_PRE + id) : (COMP_PRE + widget + "_" + id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.uap.lfw.ra.render.UIRender#createRenderHtml()
	 */
	public String createRenderHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtml());

		// 渲染子节点
		UILayout layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel, getPageMeta(), this, DriverPhase.PC);
			if (render != null)
				buf.append(render.createRenderHtml());
		}
		// 渲染子节点

		buf.append(this.generalTailHtml());
		return buf.toString();
	}
	
	
	/**
	 * @return
	 * 渲染占位符
	 */
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
				String cDivId = render.getNewDivId();
				if(cDivId != null && !cDivId.equals(""))
					buf.append(getDivId()).append(".appendChild(").append(cDivId).append(");\n");
			}
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

		// 渲染子节点
		UILayout layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel, getPageMeta(), this, DriverPhase.PC);
			if (render != null)
				buf.append(render.createRenderScript());
		}
		// 渲染子节点

		buf.append(this.generalTailScript());
		buf.append(this.generalEditableTailScript());
		return wrapByRequired(getType(), buf.toString());
	}
	
	/**
	 * @return
	 * 动态创建动态脚本
	 */
	public String createRenderScriptDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadScriptDynamic());
		buf.append(this.generalHeadScript());
		
		// 渲染子节点
		UILayout layout = getUiElement();
		List<UILayoutPanel> pList = layout.getPanelList();
		Iterator<UILayoutPanel> it = pList.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel, getPageMeta(), this, DriverPhase.PC);
			if (render != null){
				buf.append(render.createRenderScriptDynamic());
			}
		}
		buf.append(this.generalTailScript());
		buf.append(this.generalEditableTailScriptDynamic());
		return wrapByRequired(getType(), buf.toString());
	}
	
	public String generalHeadHtml() {
		StringBuffer buf = new StringBuffer();
		String height = "height:100%;";
		boolean isFlowmode = isFlowMode();
		if(isFlowmode)
			height = "";
		buf.append("<div id=\"" + getNewDivId() + "\" style=\"" + height + "position:relative;\" flowmode=\"" + isFlowmode + "\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}
	
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

	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}

	// obj为子元素
	@Override
	public void notifyRemoveChild(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		StringBuffer buf = new StringBuffer();
		UILayout uilayout = this.getUiElement();
		if (this.getDivId() != null) {
			List<UILayoutPanel> children = uilayout.getPanelList();
			for (int i = 0; i < children.size(); i++) {
				UILayoutPanel panel = children.get(i);
				if (obj == panel) {
					panel.notifyChange(UILayoutPanel.DESTROY,this);
//					this.removeUIElement(uilayout, panel);
					break;
				}
			}
		} else {
			buf.append("alert('删除layout失败！未获得divId')");
		}
		addDynamicScript(buf.toString());

	}
	
	/**
	 * Object obj 为父节点
	 */
	@Override
	public void notifyDestroy(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		
		StringBuffer buf = new StringBuffer();
		UILayout uilayout = this.getUiElement();
		if (this.getDivId() != null) {
			List<UILayoutPanel> children = uilayout.getPanelList();
			for (int i = 0; i < children.size(); i++) {
				UILayoutPanel panel = children.get(i);
				panel.notifyChange(UILayoutPanel.DESTROY,this);
			}
			buf.append("window.execDynamicScript2RemoveLayout('" + this.getDivId() + "');");
		} else {
			buf.append("alert('删除layout失败！未获得divId')");
		}
		addDynamicScript(buf.toString());

	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		UILayout layout = getUiElement();
		int size = layout.getPanelList().size();
		UILayoutPanel targetPanel = (UILayoutPanel) obj;
		int index = 0;
		for (int i = 0; i < size; i++) {
			UILayoutPanel panel = layout.getPanelList().get(i);
			if(panel.getId().equals(targetPanel.getId())){
				index = i;
				break;
			}
		}
		
		IControlPlugin plugin = ControlFramework.getInstance().getControlPluginByUIClass(obj.getClass());
		IUIRender render = plugin.getUIRender(uiMeta, targetPanel, pageMeta, this, DriverPhase.PC);
		
		StringBuffer buf = new StringBuffer();
		String html = render.createRenderHtmlDynamic();
		buf.append(html);
		
		buf.append("var div = $ge('" + divId + "');\n");
		buf.append("div.insertBefore(" + render.getNewDivId() + ", div.childNodes[" + (index) + "]);\n");
		buf.append(render.createRenderScriptDynamic());
		
		addDynamicScript(buf.toString());
//		Iterator<UILayoutPanel> it = layout.getPanelList().iterator();
//		
//		while(it.hasNext()){
//			UILayoutPanel panel = it.next();
//			
//		}
		
	}
	

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		throw new LfwRuntimeException("未实现！");
	}

	/**
	 * 2011-10-13 下午01:42:13 renxh des：利用dom创建DIV
	 * 
	 * @return
	 */
	public String generalHeadHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append(" = $ce('DIV');\n");
		buf.append(newDivId).append(".style.width = '100%';\n");
		if(!isFlowMode())
			buf.append(newDivId).append(".style.height = '100%';\n");
		else
			buf.append(newDivId).append(".flowmode=" + isFlowMode() + ";\n");
//		buf.append(newDivId).append(".style.overlflow = 'hidden';\n");
		buf.append(newDivId).append(".id = '"+newDivId+"';\n");		
		if (this.isEditMode()) {
			buf.append(this.generalEditableHeadHtmlDynamic());
			buf.append(newDivId).append(".appendChild(").append(getDivId()).append(");\n");
		}
		return buf.toString();
	}
//
//	/**
//	 * 2011-10-13 下午02:59:10 renxh des：头脚本
//	 * 
//	 * @return
//	 */
//	public abstract String generalHeadScriptDynamic();
//
//	/**
//	 * 2011-10-13 下午02:59:24 renxh des：尾脚本
//	 * 
//	 * @return
//	 */
//	public abstract String generalTailScriptDynamic();

	/**
	 * 2011-10-13 下午02:59:59 renxh des：可编辑态头脚本
	 * 
	 * @return
	 */
	public String generalEditableHeadScriptDynamic() {
		return super.generalEditableHeadScriptDynamic();
	}

	/**
	 * 2011-10-13 下午03:00:12 renxh des：可编辑态尾脚本
	 * 
	 * @return
	 */
	public String generalEditableTailScriptDynamic() {
		return super.generalEditableTailScriptDynamic();
	}

}
