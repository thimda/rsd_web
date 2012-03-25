package nc.uap.lfw.ra.render;

import java.util.List;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.ra.itf.IUIRender;

/**
 * @author renxh
 * 渲染panel 布局的渲染器
 * @param <T>
 * @param <K>
 */
public abstract class UIPanelRender<T extends UIPanel, K extends WebElement> extends UILayoutRender<T, K> {

	public UIPanelRender(T uiEle, UIMeta uimeta, PageMeta pageMeta, UIRender<?, ?> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
	}

	public String createRenderHtml() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalHeadHtml());
		UIPanel panel = this.getUiElement();
		List<UILayoutPanel> panelList = panel.getPanelList();
		for(int i=0;i< panelList.size();i++){
			UILayoutPanel uiLayoutPanel = panelList.get(i);
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), uiLayoutPanel, getPageMeta(), this,DriverPhase.PC);
			if(render!=null)
				buf.append(render.createRenderHtml());
		}		
		buf.append(this.generalTailHtml());
		return buf.toString();
	}

	public String createRenderScript() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.generalEditableHeadScript());
		buf.append(this.generalHeadScript());		
		
		// 子节点
		UIPanel panel = this.getUiElement();
		List<UILayoutPanel> panelList = panel.getPanelList();
		for(int i=0;i< panelList.size();i++){
			UILayoutPanel uiLayoutPanel = panelList.get(i);
			IUIRender render = ControlFramework.getInstance().getUIRender(getUiMeta(), uiLayoutPanel,getPageMeta(), this, DriverPhase.PC);
			if(render!=null)
				buf.append(render.createRenderScript());
		}	
		buf.append(this.generalTailScript());
		buf.append(this.generalEditableTailScript());
		return buf.toString();
	}
	
	


}
