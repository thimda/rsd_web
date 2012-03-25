package nc.uap.lfw.ra.render;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.design.itf.IPaEditorService;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UpdatePair;
import nc.uap.lfw.ra.itf.IUIRender;

/**
 * @author renxh
 * ��������ؼ���Ⱦ��
 * @param <T>
 * @param <K>
 */
public abstract class UINormalComponentRender<T extends UIComponent, K extends WebComponent> extends UIComponentRender<T, K> {

	private static final long serialVersionUID = 3493771869083674715L;
	private String align;

	// ���ڵ�Ϊpanel �������������
	public UINormalComponentRender(T uiEle, K webEle, UIMeta uimeta, PageMeta pageMeta, IUIRender parentRender) {
		super(uiEle, webEle);
		if(webEle == null){
			throw new LfwRuntimeException(this.getClass().getName() +": webEle����Ϊ�գ�");
		}
		this.setPageMeta(pageMeta);
		this.setUiMeta(uimeta);
		this.setParentRender(parentRender);
		WebComponent comp = this.getWebElement();
		this.id = comp.getId();
		if(this.id == null){
			throw new LfwRuntimeException(this.getClass().getName() +": id����Ϊ�գ�");
		}
		if (comp.getWidget() == null) {
			comp.setWidget(this.getCurrWidget());
		}
		if (comp.getWidget() != null) {
			this.widget = comp.getWidget().getId();
		}
		if (this.widget == null){
			this.divId = DIV_PRE + this.id;
			varId = COMP_PRE + id;
		}
		else{
			varId = COMP_PRE + this.widget + "_" + getId();
			this.divId = DIV_PRE + this.widget + "_" + this.id;
		}
		if(uiEle != null){
			this.align = uiEle.getAlign();
		}
		if(this.align == null)
			this.align = UIComponent.ALIGN_LEFT;
	}

	/* (non-Javadoc)
	 * @see nc.uap.lfw.ra.render.UIRender#createRenderHtml()
	 * ��Ⱦռλ��
	 */
	public final String createRenderHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generateBodyHtml());
		return buf.toString();
	}
	
	public final String createRenderHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generateBodyHtmlDynamic());
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see nc.uap.lfw.ra.render.UIRender#createRenderScript()
	 * ��Ⱦ�ű�
	 */
	public String createRenderScript() {
		StringBuffer buf = new StringBuffer();
		String script = (String) getAttribute(BEFORE_SCRIPT);// ��ʱ����֪����δ���
		if (script != null)
			buf.append(script);
		buf.append(this.generalEditableHeadScript());
		
		buf.append(this.generateBodyScript());
 
		WebComponent comp = (WebComponent) this.getWebElement();
		
		String varId = getVarId();
		
		String compStr = "pageUI.getWidget('" + this.getWidget() + "').getComponent('" + getId() + "')";
		
		buf.append(this.addEventSupport(comp, getWidget(), compStr, null));
		
		if (comp.getContextMenu() != null && !comp.getContextMenu().equals("")) {
			script = addContextMenu(comp.getContextMenu(), varId);
			buf.append(script);
		}

		script = this.generateDsBindingScript();
		if (script != null){
			StringBuffer dsScript = (StringBuffer) this.getContextAttribute(DS_SCRIPT);
			if(dsScript!=null){
				dsScript.append(script);
			}else{
				dsScript = new StringBuffer();
				dsScript.append(script);
				this.setContextAttribute(DS_SCRIPT, dsScript);
			}
		}
		
		comp.setRendered(true);
		
		String wstr = setWidgetToComponent();
		if(wstr != null)
			buf.toString();
//		buf.append(getVarId()).append(".flowMode = ").append(isFlowMode() + ";\n");
		
		if(this.getWebElement() instanceof SelfDefComp){// �Զ���������⴦��
			String id = getVarId();
			script = id + ".oncreate();\n";
			buf.append(script);
		}
		
		buf.append(this.generalEditableTailScript());
		script = (String) getAttribute(AFTER_SCRIPT);
		if (script != null)
			buf.append(script);

		return wrapByRequired(getType(), buf.toString());
	}
	
	/**
	 * ��Widget���԰󶨵��ؼ���
	 */
	protected String setWidgetToComponent() {
		if (this.getWidget() != null){
			StringBuffer buf = new StringBuffer();
			buf.append("pageUI.getWidget('").append(this.getWidget()).append("').getComponent('").append(getId()).append("').widget = " + WIDGET_PRE + this.getWidget()).append("\n");
			return buf.toString();
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see nc.uap.lfw.ra.render.UIRender#generalEditableHeadScript()
	 * ������Ⱦ�ű�
	 */
	public String generalEditableHeadScript() {
		return "";
	}
	
//	public String generalEditableHeadScriptDynamic() {
//		return "";
//	}
//	
	

	/** (non-Javadoc)
	 * @see nc.uap.lfw.ra.render.UIRender#generalEditableTailScript()
	 * ���ڿؼ���div����padding
	 */
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
				buf.append("var params = {widgetid:'"+widgetId+"',uiid:'"+uiid+"',eleid:'"+eleid+"',type:'"+type+"'};\n");
				buf.append("new EditableListener(document.getElementById('" + getDivId() + "'),params,'component');\n");
			}
			if(this.getDivId() != null){
				buf.append("if(document.getElementById('").append(this.getDivId()).append("'))");
				buf.append("  document.getElementById('").append(this.getDivId()).append("').style.padding = '0px';\n");
			}
		}
		return buf.toString();
	}


	final public String generalHeadHtml() {

		return "";
	}

	final public String generalHeadScript() {

		return ""; 
	}

	final public String generalTailHtml() {

		return "";
	}

	final public String generalTailScript() {

		return "";
	}

	protected abstract String getSourceType(IEventSupport ele);

	public abstract String generateBodyScript();

	/**
	 * �ṩ�����า�ǵķ���
	 */
	public String generateDsBindingScript() {
		return "";
	}
	
	public String generateDsBindingScriptDynamic() {
		return "";
	}

	/**
	 * 2011-8-2 ����07:34:13 renxh
	 * des������ռλ��
	 * @return
	 */
	public String generateBodyHtml() {
		StringBuffer buf = new StringBuffer();
		UIComponent comp = this.getUiElement();
		String width = comp.getWidth();
		if (width == null)
			buf.append("<div style=\"");
		else
			buf.append("<div style=\"width:").append(width.indexOf("%") != -1 ? width : width + "px;");
		String height = comp.getHeight();
		if (height != null)
			buf.append(";height:").append(height.indexOf("%") != -1 ? height : height + "px;");
		buf.append(";top:").append(comp.getTop() + "px");
		buf.append(";left:").append(comp.getLeft() + "px");
		buf.append(";position:").append(comp.getPosition());
//		buf.append(";overflow:hidden");
		if(this.align.equals(UIComponent.ALIGN_RIGHT))
			buf.append(";float:right;");
		buf.append("\" id=\"").append(getDivId()).append("\">\n");
		buf.append("</div>\n");
		return buf.toString();
	}
	
	
	/**
	 * 2011-10-10 ����01:28:18 renxh
	 * des������js�ű�����DIV
	 * @return
	 */
	public String generateBodyHtmlDynamic(){
		StringBuffer buf = new StringBuffer();
		UIComponent comp = this.getUiElement();
		buf.append("var ").append(getDivId()).append(" = $ce('DIV');");
		String width = comp.getWidth();
		if(width != null){
			width = width.indexOf("%") != -1 ? width : width + "px";
			buf.append(getDivId()).append(".style.width = '").append(width).append("';");
		}
		String height = comp.getHeight();
		if(height != null){
			height = height.indexOf("%") != -1 ? height : height + "px";
			buf.append(getDivId()).append(".style.height = '").append(height).append("';");;
		}
		buf.append(getDivId()).append(".style.top = '").append(comp.getTop() + "px';");
		buf.append(getDivId()).append(".style.left = '").append(comp.getLeft() + "px';");
		buf.append(getDivId()).append(".style.position = '").append(comp.getPosition()).append("';");
//		buf.append(getDivId()).append(".style.overflow = 'hidden';");
		if(this.align.equals(UIComponent.ALIGN_RIGHT))
			buf.append(getDivId()).append(".style[ATTRFLOAT] = 'right';");
//		buf.append(getDivId()).append(".style.margin = '0 auto';");
		buf.append(getDivId()).append(".id = '").append(getDivId()).append("';");
		return buf.toString();
	}


	
	/* (non-Javadoc)
	 * @see nc.uap.lfw.ra.render.UIRender#notifyRemove(nc.uap.lfw.jsp.uimeta.UIMeta, nc.uap.lfw.core.page.PageMeta)
	 * ��ɾ���������ɾ��UIMeta�����ɾ���ű�
	 */
	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta,Object obj) {
		throw new LfwRuntimeException("����ķ�������! component û���ӽڵ�ɾ��");
		
	}
	

	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta,Object obj){
		String divId = this.getDivId();
		UIComponent uiEle = this.getUiElement();
		if (this.getUiElement()!=null) {
			String sessionId = LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId();
			String pageId =  LfwRuntimeEnvironment.getWebContext().getWebSession().getPageId();
			IPaEditorService ipaService = NCLocator.getInstance().lookup(IPaEditorService.class);
			PageMeta outPageMeta = ipaService.getOutPageMeta(pageId,sessionId);// ����ⲿ��pageMeta
			LfwWidget widget = outPageMeta.getWidget(this.getUiElement().getWidgetId());
			if (widget != null) {
				WebComponent webComp = null;
				if(isMenu(uiEle)){
					webComp = widget.getViewMenus().getMenuBar(uiEle.getId());
				}
				else
					webComp = (WebComponent) widget.getViewComponents().getComponent(uiEle.getId());
				StringBuffer buf = new StringBuffer();
				if (divId != null) {
					buf.append("window.execDynamicScript2RemoveComponent('" + divId + "','" + uiEle.getWidgetId() + "','" + webComp.getId() + "');");
					this.removeComponent(widget.getId(), uiEle.getId(),isMenu(uiEle));
				} else {
					buf.append("alert('ɾ���ؼ�ʧ�ܣ�δ���divId')");
				}
				addDynamicScript(buf.toString());

			}
		}
	}
	
	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		throw new LfwRuntimeException("δʵ�֣�");
		
	}

	/**
	 * Ŀǰֻ֧��align ����
	 */
	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		if(UIElement.VISIBLE.equals(obj)){
			boolean visble = getUiElement().getVisible();
			addDynamicScript("document.getElementById('"+ getNewDivId() +"').style.display=\""+ (visble? "" : "none") +"\";\n");
		}else if(obj instanceof UpdatePair){
			UpdatePair pair = (UpdatePair) obj;
			if(pair.getKey().equals(UIComponent.ALIGN)){
				UIComponent uiObj = this.getUiElement();
				String align = uiObj.getAlign();
				if(align == null){
					return;
				}
				String divId = getDivId();
				StringBuffer buf = new StringBuffer();
				buf.append("var ").append(divId).append(" = $ge('"+divId+"');\n");
				buf.append(divId).append(".style[ATTRFLOAT] = '").append(align).append("';");
				addDynamicScript(buf.toString());
			}
			else if(pair.getKey().equals(UIComponent.WIDTH)){
				UIComponent uiObj = this.getUiElement();
				String width = uiObj.getWidth();
				if(width == null){
					return;
				}
				
				width = RenderHelper.formatMeasureStr(width);
				String divId = getDivId();
				StringBuffer buf = new StringBuffer();
				buf.append("var ").append(divId).append(" = $ge('"+divId+"');\n");
				buf.append(divId).append(".style.width = '").append(width).append("';");
				addDynamicScript(buf.toString());
			}
			
			else if(pair.getKey().equals(UIComponent.HEIGHT)){
				UIComponent uiObj = this.getUiElement();
				String height = uiObj.getHeight();
				if(height == null){
					return;
				}
				
				height = RenderHelper.formatMeasureStr(height);
				String divId = getDivId();
				StringBuffer buf = new StringBuffer();
				buf.append("var ").append(divId).append(" = $ge('"+divId+"');\n");
				buf.append(divId).append(".style.height = '").append(height).append("';");
				addDynamicScript(buf.toString());
			}
			else if(pair.getKey().equals(UIComponent.ID)){
				notifyUpdateId();
			}
			else if(pair.getKey().equals(UIComponent.CLASSNAME)){
				String className = (String) pair.getValue();
				String widget = WIDGET_PRE + this.getCurrWidget().getId();
				StringBuffer buf = new StringBuffer();
				buf.append("var ").append(widget).append(" = pageUI.getWidget('"+this.getCurrWidget().getId()+"');\n");
				buf.append("var comp = ").append(widget).append(".getComponent('" + id + "');\n");
				buf.append("comp.changeClass('").append(className).append("');\n");
				addDynamicScript(buf.toString());
			}
		}
		
	}

	/**
	 * ID���²���
	 */
	protected void notifyUpdateId() {
		
	}

	/**
	 * 2011-10-10 ����01:31:02 renxh
	 * des����̬����ִ�нű�
	 * @return
	 */
	public String createRenderScriptDynamic(){
		StringBuffer buf = new StringBuffer();
		String script = (String) getAttribute(BEFORE_SCRIPT);// ��ʱ����֪����δ���
		if (script != null)
			buf.append(script);
		buf.append(this.generalEditableHeadScript());
		
		buf.append(this.generateBodyScript());

		WebComponent comp = (WebComponent) this.getWebElement();
		
		String varId = getVarId();
		
		String compStr = "pageUI.getWidget('" + this.getWidget() + "').getComponent('" + getId() + "')";
		buf.append(this.addEventSupport(comp, getWidget(), compStr, null));
		
		if (comp.getContextMenu() != null && !comp.getContextMenu().equals("")) {
			script = addContextMenu(comp.getContextMenu(), varId);
			buf.append(script);
		}

		script = this.generateDsBindingScript();
		if (script != null){
			StringBuffer dsScript = (StringBuffer) this.getContextAttribute(DS_SCRIPT);
			if(dsScript!=null){
				dsScript.append(script);
			}else{
				dsScript = new StringBuffer();
				dsScript.append(script);
				this.setContextAttribute(DS_SCRIPT, dsScript);
			}
		}
		
		comp.setRendered(true);
		
		String wstr = setWidgetToComponent();
		if(wstr != null)
			buf.toString();
		
		if(this.getWebElement() instanceof SelfDefComp){// �Զ���������⴦��
			String id = getVarId();
			script = id + ".oncreate();\n";
			buf.append(script);
		}
		
		buf.append(this.generalEditableTailScriptDynamic());
		script = (String) getAttribute(AFTER_SCRIPT);
		if (script != null)
			buf.append(script);

		return wrapByRequired(getType(), buf.toString());
	}
	
	/**
	 * 2011-8-4 ����03:52:07 renxh des������µ�divId����Ϊ�Ա༭̬ʱ����Ҫ��������div�ı�id
	 * 
	 * @return
	 */
	public String getNewDivId() {
		return this.getDivId();
	}
}
