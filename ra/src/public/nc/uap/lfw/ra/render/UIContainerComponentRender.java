package nc.uap.lfw.ra.render;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * @author renxh 容器类控件渲染器
 * @param <T>
 * @param <K>
 */
public abstract class UIContainerComponentRender<T extends UIComponent, K extends WebComponent> extends UIComponentRender<T, K> {

	public UIContainerComponentRender(T uiEle, K webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, webEle);
		this.setPageMeta(pageMeta);
		this.setUiMeta(uimeta);
		this.setParentRender(parentRender);
		WebComponent comp = this.getWebElement();
		this.id = comp.getId();
		if(this.id == null){
			throw new LfwRuntimeException(this.getClass().getName() +":id不能为空！");
		}
		if (comp.getWidget() == null) {
			comp.setWidget(this.getCurrWidget());
		}
		if (comp.getWidget() != null) {
			this.widget = comp.getWidget().getId();
		}
		if (this.widget == null){
			varId = COMP_PRE + id;
			this.divId = DIV_PRE + this.id;
		}
		else{
			varId = COMP_PRE + this.widget + "_" + getId();
			this.divId = DIV_PRE + this.widget + "_" + this.id;
		}
	}

	public final String createRenderHtml() {
		WebComponent comp = this.getWebElement();
		if (comp.isRendered())
			return "";
		StringBuffer buf = new StringBuffer();

		// 将控件实例设入pageContext中
		// getJspContext().setAttribute(getId(), comp); // 被下面的代码替换
		this.setContextAttribute(getId(), comp);

		String headStr = this.generalHeadHtml();
		if (headStr != null)
			buf.append(headStr);

		// 此处加入处理子节点的代码

		String tailStr = this.generalTailHtml();
		if (tailStr != null)
			buf.append(tailStr);

		return buf.toString();
	}
	
	public final String createRenderHtmlDynamic() {
		WebComponent comp = this.getWebElement();
		StringBuffer buf = new StringBuffer();

		// 将控件实例设入pageContext中
		// getJspContext().setAttribute(getId(), comp); // 被下面的代码替换
		this.setContextAttribute(getId(), comp);

		String headStr = this.generalHeadHtmlDynamic();
		if (headStr != null)
			buf.append(headStr);
		return buf.toString();
	}

	public String createRenderScript() {
		WebComponent comp = this.getWebElement();
		if (comp.isRendered())
			return "";
		StringBuffer buf = new StringBuffer();
		buf.append(this.getAttribute(BEFORE_SCRIPT));
		buf.append(this.generalEditableHeadScript());
		String script = this.generalHeadScript();
		buf.append(script);

		// 此处加入处理子节点的代码

		script = this.generalTailScript();
		buf.append(script);
		if (comp.getContextMenu() != null && !comp.getContextMenu().equals("")) {
			script = addContextMenu(comp.getContextMenu(), COMP_PRE + getId());
			buf.append(script);
		}

		// 添加控件事件绑定
		script = addEventSupport(comp, getCurrWidget() == null ? null : getCurrWidget().getId(), getVarId(), null);
		buf.append(script);
		comp.setRendered(true);
		String wstr = setWidgetToComponent();
		if(wstr != null)
			buf.toString();
		buf.append(this.generalEditableTailScript());
		buf.append(this.getAttribute(AFTER_SCRIPT));
		return wrapByRequired(getType(), buf.toString());
//		return buf.toString();
	}

	/**
	 * 将Widget属性绑定到控件上
	 */
	protected String setWidgetToComponent() {
		if (this.getWidget() != null){
			StringBuffer buf = new StringBuffer();
			buf.append(getVarId() + ".widget = " + WIDGET_PRE + this.getWidget()).append("\n");
			return buf.toString();
		}
		return "";
	}
	
	
	/**
	 * @return
	 * 动态创建脚本
	 */
	public final String createRenderScriptDynamic() {
		WebComponent comp = this.getWebElement();
		StringBuffer buf = new StringBuffer();
		buf.append(this.getAttribute(BEFORE_SCRIPT));
		buf.append(this.generalEditableHeadScriptDynamic());
		String script = this.generalHeadScriptDynamic();
		buf.append(script);

		// 此处加入处理子节点的代码

		script = this.generalTailScriptDynamic();
		buf.append(script);
		if (comp.getContextMenu() != null && !comp.getContextMenu().equals("")) {
			script = addContextMenu(comp.getContextMenu(), COMP_PRE + getId());
			buf.append(script);
		}

		// 添加控件事件绑定
		script = addEventSupport(comp, getCurrWidget() == null ? null : getCurrWidget().getId(), getVarId(), null);
		buf.append(script);
		String wstr = setWidgetToComponent();
		if(wstr != null)
			buf.toString();
		buf.append(this.generalEditableTailScriptDynamic());
		buf.append(this.getAttribute(AFTER_SCRIPT));
//		return buf.toString();
		return wrapByRequired(getType(), buf.toString());
	}
	
	@Override
	public String generalHeadHtml() {
//		WebComponent tab = this.getWebElement();
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"").append(getNewDivId());
		buf.append("\" style=\"top:0px;left:0px;width:100%;height:100%;overflow:hidden;\">\n");
		buf.append(this.generalEditableHeadHtml());
		return buf.toString();
	}
	
	public String generalHeadHtmlDynamic() {
//		WebComponent tab = this.getWebElement();
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getNewDivId()).append(" = $ce('DIV');\n");
		buf.append(getNewDivId()).append(".id = '"+getNewDivId()+"';\n");
		buf.append(getNewDivId()).append(".style.top = '0px';\n");
		buf.append(getNewDivId()).append(".style.left = '0px';\n");
		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append(getNewDivId()).append(".style.height = '100%';\n");
		buf.append(getNewDivId()).append(".style.overflow = 'hidden';\n");
		if(this.isEditMode()){
			buf.append(this.generalEditableHeadHtmlDynamic());
			if(!getNewDivId().equals(getDivId()))
				buf.append(getNewDivId()).append(".appendChild("+getDivId()+");\n");
		}
		
		
		return buf.toString();
	}
	
	@Override
	public String generalHeadScript() {

		return "";
	}
	public String generalHeadScriptDynamic() {

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
	
	public String generalTailScriptDynamic() {

		return "";
	}

	protected String getSourceType(WebElement ele) {
		return null;
	}

	
	public String generalEditableHeadHtml() {
		
		return "";
	}
	
	public String generalEditableHeadHtmlDynamic() {
		
		return "";
	}

	@Override
	public String generalEditableHeadScript() {
		
		return "";
	}
	@Override
	public String generalEditableHeadScriptDynamic() {
		
		return "";
	}

	
	public String generalEditableTailHtml() {
		
		return "";
	}

	
	public String generalEditableTailScript() {
		
		StringBuffer buf = new StringBuffer();
		if(this.isEditMode()){
			if(this.getWidget() != null && LfwRuntimeEnvironment.isWindowEditorMode()){
				return "";
			}
			String widgetId = this.getWidget() == null ? "" : this.getWidget();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			
			if (getDivId() == null) {
				LfwLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = new Object();\n");
				buf.append("params.widgetid ='").append(widgetId).append("';\n");
				buf.append("params.uiid ='").append(uiid).append("';\n");
				buf.append("params.eleid ='").append(eleid).append("';\n");
				buf.append("params.type ='").append(type).append("';\n");
				buf.append("new EditableListener(document.getElementById('" + getDivId() + "'),params,'component');\n");
			}
			if(this.getDivId() != null){
				buf.append("if(document.getElementById('").append(this.getDivId()).append("'))");
				buf.append("  document.getElementById('").append(this.getDivId()).append("').style.padding = '0px';\n");
			}
		}
		return buf.toString();
	}
	
	public String generalEditableTailScriptDynamic() {
		
		StringBuffer buf = new StringBuffer();
		if(this.isEditMode()){
			if(this.getWidget() != null && LfwRuntimeEnvironment.isWindowEditorMode()){
				return "";
			}
			String widgetId = this.getWidget() == null ? "" : this.getWidget();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			
			if (getDivId() == null) {
				LfwLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = new Object();\n");
				buf.append("params.widgetid ='").append(widgetId).append("';\n");
				buf.append("params.uiid ='").append(uiid).append("';\n");
				buf.append("params.eleid ='").append(eleid).append("';\n");
				buf.append("params.type ='").append(type).append("';\n");
				buf.append("new EditableListener(" + getDivId() + ",params,'component');\n");
			}
			if(this.getDivId() != null){
				buf.append("if(").append(this.getDivId()).append(")");
				buf.append(this.getDivId()).append(".style.padding = '0px';\n");
			}
		}
		return buf.toString();
	}
	
	
	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta,Object obj) {
		throw new LfwRuntimeException("该方法未实现！");
	}
	
	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		String divId = this.getDivId();
		UIComponent uiEle = this.getUiElement();
		if (this.getUiElement()!=null) {
			String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
			String pageId =  LfwRuntimeEnvironment.getWebContext().getWebSession().getPageId();
			IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
			PageMeta outPageMeta = ipaService.getOutPageMeta(pageId,sessionId);// 获得外部的pageMeta
			LfwWidget widget = outPageMeta.getWidget(this.getUiElement().getWidgetId());
			if (widget != null) {
				WebComponent webButton = (WebComponent) widget.getViewComponents().getComponent(uiEle.getId());
				StringBuffer buf = new StringBuffer();
				if (divId != null) {
					buf.append("window.execDynamicScript2RemoveComponent('" + divId + "','" + uiEle.getWidgetId() + "','" + webButton.getId() + "');");
					this.removeComponent(widget.getId(), uiEle.getId(), isMenu(uiEle));
				} else {
					buf.append("alert('删除控件失败！未获得divId')");
				}
				addDynamicScript(buf.toString());

			}
		}
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		throw new LfwRuntimeException("未实现！");
	}
	

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		throw new LfwRuntimeException("未实现！");
	}

}
