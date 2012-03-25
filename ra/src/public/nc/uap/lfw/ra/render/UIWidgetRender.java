package nc.uap.lfw.ra.render;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIDialog;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.itf.IUIRender;

/**
 * @author renxh 片段渲染器
 */
public abstract class UIWidgetRender<T extends UIWidget, K extends WebElement> extends UILayoutRender<T, K> {
	public UIWidgetRender(T uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
	}

	private static final long serialVersionUID = 3828096267824128641L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.uap.lfw.ra.render.UILayoutRender#createRenderHtml()
	 */
	@Override
	public String createRenderHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtml());
		UIWidget widget = this.getUiElement();
		IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), widget.getUimeta(), getPageMeta(), this, DriverPhase.PC);
		if (render != null)
			buf.append(render.createRenderHtml());
		buf.append(this.generalTailHtml());
		return buf.toString();
	}
	
	
	
	public String createRenderHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtmlDynamic());
		
		UIWidget widget = this.getUiElement();
		IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), widget.getUimeta(), getPageMeta(), this, DriverPhase.PC);
		if (render != null){
			buf.append(render.createRenderHtmlDynamic());
			buf.append(getDivId()).append(".appendChild(").append(render.getNewDivId()).append(");\n");
		}
//		buf.append(this.generalTailHtml());
		
		return buf.toString();
	}

	@Override
	public String createRenderScript() {
		String str = super.createRenderScript();
		// 子节点
		UIWidget uiWidget = this.getUiElement();
		LfwWidget widget = getCurrWidget();
		if (widget.isDialog() || uiWidget instanceof UIDialog) {
			return wrapByRequired("modaldialog", str);
		}
		return str;
	}
	
	public String getType(){
		return null;
	}
	
	public String createRenderScriptDynamic() {
		String str = super.createRenderScriptDynamic();
		// 子节点
		UIWidget uiWidget = this.getUiElement();
		LfwWidget widget = getCurrWidget();
		if (widget.isDialog() || uiWidget instanceof UIDialog) {
			return wrapByRequired("modaldialog", str);
		}
		return str;

	}

	@Override
	public String generalHeadScript() {
		return "";
	}

	@Override
	public String generalTailScript() {
		return "";
	}

	public String generalHeadHtml() {
		return super.generalHeadHtml();
	}
	
	public String generalHeadHtmlDynamic() {
		return super.generalHeadHtmlDynamic();
	}

	public String generalTailHtml() {
		return super.generalTailHtml();
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		//super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyDestroy(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		super.notifyDestroy(uimeta, pagemeta, obj);
	}

	@Override
	public void notifyRemoveChild(UIMeta uimeta, PageMeta pagemeta, Object obj) {
		String divId = this.getDivId();
		StringBuffer buf = new StringBuffer();
		UIWidget widget = this.getUiElement();
		if (divId != null) {
			UIElement child = (UIElement)obj;
			if (child != null) {
				child.notifyChange(UIElement.DESTROY);
			}
		} else {
			buf.append("alert('删除子元素失败！未获得divId')");
		}
		
		addDynamicScript(buf.toString());
	}

	

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_WIDGT;
	}
	
	
	
	

}
