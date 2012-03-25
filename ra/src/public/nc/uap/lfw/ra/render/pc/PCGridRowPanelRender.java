package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIGridRowLayout;
import nc.uap.lfw.jsp.uimeta.UIGridRowPanel;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;

/**
 * @author renxh
 * 布局中的panel 渲染器
 */
public class PCGridRowPanelRender extends UILayoutPanelRender<UIGridRowPanel, WebElement> {

	public PCGridRowPanelRender(UIGridRowPanel uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<? extends UILayout, ? extends WebElement> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
	}

	@Override
	public String generalHeadHtmlDynamic() {
		return "";
	}


	@Override
	protected String getSourceType(IEventSupport ele) {
		return "";
	}
	
	
	public String createRenderHtmlDynamic() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtmlDynamic());
		UILayoutPanel panel = this.getUiElement();

		// 渲染子节点
		IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), panel.getElement(), getPageMeta(), this, DriverPhase.PC);
		if (render != null) {
			buf.append(render.createRenderHtmlDynamic());
		}

		return buf.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNewDivId() {
		return this.getDivId();
	}
	
	/**
	 * Object obj 父节点
	 */
	@Override
	public void notifyDestroy(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		UIGridRowLayout row = (UIGridRowLayout) this.getUiElement().getElement();
		row.notifyChange(UIElement.DESTROY, this);
	}
}
