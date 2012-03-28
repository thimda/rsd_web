package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanelPanel;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh
 * 布局中的panel 渲染器
 */
public class PCPanelPanelRender extends UILayoutPanelRender<UIPanelPanel, WebElement> {

	public PCPanelPanelRender(UIPanelPanel uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<? extends UILayout, ? extends WebElement> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		this.divId = parentRender.getDivId();
	}

	public String generalHeadHtml() {
		return "";
	}

	public String generalHeadScript() {
		return "";
	}

	public String generalTailHtml() {

		return "";
	}

	public String generalTailScript() {

		return "";
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_PANELPANEL;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		return "";
	}

	@Override
	public String getNewDivId() {
		return "";
	}

	@Override
	protected String addEditableListener(String divId, String widgetId, String uiId, String subuiId, String eleId, String subEleId, String type) {
		StringBuffer buf = new StringBuffer();
		UILayoutRender parent = (UILayoutRender) this.getParentRender();
		LfwWidget lfwWidget =  this.getCurrWidget();
		String showId = parent.getVarId();
		
		buf.append("var contentDiv = "+showId+".getContentDiv();\n");
		buf.append("var params = {");
		buf.append("widgetid:'").append(widgetId).append("'");
		buf.append(",uiid:'").append(uiId).append("'");
		buf.append(",uiid:'").append(subuiId).append("'");
		buf.append(",eleid:'").append(eleId).append("'");
		buf.append(",type:'").append(type).append("'");
		buf.append("};\n");
		buf.append("new EditableListener(contentDiv, params, 'panel');\n");
		buf.append("new DragListener(contentDiv);\n;");
		return buf.toString();
	}
	
	/**
	 * Object obj 要添加的对象
	 */
	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		StringBuffer dsBuf = (StringBuffer) this.getContextAttribute(UIRender.DS_SCRIPT);
		if(dsBuf == null){
			dsBuf = new StringBuffer();
			this.setContextAttribute(UIRender.DS_SCRIPT, dsBuf);
		}
//		String divId = parent.getDivId();
		UIElement ele = (UIElement) obj;
		IControlPlugin plugin = ControlFramework.getInstance().getControlPluginByUIClass(ele.getClass());
		IUIRender render = plugin.getUIRender(uiMeta, ele, pageMeta, this, DriverPhase.PC);
		
		StringBuffer buf = new StringBuffer();
		String html = render.createRenderHtmlDynamic();
		buf.append(html);
		
		UILayoutRender parent = (UILayoutRender) this.getParentRender();
		String widgetId = ele.getWidgetId();
		if(widgetId != null && !(ele instanceof UIWidget)){
			buf.append("pageUI.getWidget('" + widgetId + "').getPanel('" + parent.getId()+ "').getContentDiv().appendChild(" + render.getNewDivId() + ");\n");
		}
		else{
			buf.append("pageUI.getPanel('" + parent.getId()+ "').getContentDiv().appendChild(" + render.getNewDivId() + ");\n");
		}
		buf.append(render.createRenderScriptDynamic());
		
		addDynamicScript(buf.toString());
	}

}
