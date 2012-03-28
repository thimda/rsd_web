package nc.uap.lfw.ra.render.pc.tag;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DriverPhase;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;
import nc.uap.lfw.ra.render.UIMetaRender;
import nc.uap.lfw.ra.render.pc.PCWidgetRender;

public class RaWidgetTag extends RaLayoutTag {

	@Override
	protected UIElement getUIElement() {
		WebElement comp = getWebElement();
		UIWidget uiwidget = new UIWidget();
		uiwidget.setId(comp.getId());
		
		UIMeta uimeta = new UIMeta();
		uiwidget.addPanel(uimeta);
		uimeta.setId(comp.getId() + "_um");
		uimeta.setWidgetId(uiwidget.getId());
		return uiwidget;
	}

	@Override
	protected WebElement getWebElement() {
		return getPageMeta().getWidget(getId());
	}
	
	@Override
	protected void doRender() {
		PCWidgetRender render = (PCWidgetRender) getRender();
		String html = render.generalHeadHtml();
		if(html != null)
			addToBodyHtml(html);
		String script = render.generalHeadScript();
		if(script != null)
			addToBodyScript(script);
		
		
		UIWidget uiwidget = render.getUiElement();
//		uiwidget.get
		UIMetaRender uimetaRender = (UIMetaRender) ControlFramework.getInstance().getUIRender((UIMeta) getUIMeta(), uiwidget.getUimeta(), getPageMeta(), render, DriverPhase.PC);
		html = uimetaRender.createRenderHtmlHead();
		if(html != null)
			addToBodyHtml(html);
		script = uimetaRender.createRenderScriptHead();
		if(script != null)
			addToBodyScript(script);
		
		
		try {
			if(getJspBody() != null)
				getJspBody().invoke(getJspContext().getOut());
		} 
		catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
		
		String umTailHtml = render.generalTailHtml();
		if(umTailHtml != null)
			addToBodyHtml(umTailHtml);
		
		StringBuffer umTailScriptBuf = new StringBuffer();
		umTailScriptBuf.append(render.generalTailScript());
		umTailScriptBuf.append(render.generalEditableTailScript());
		addToBodyScript(umTailScriptBuf.toString());
		
		
		String tailHtml = render.generalTailHtml();
		if(tailHtml != null)
			addToBodyHtml(tailHtml);
		String tailScript = render.generalTailScript();
		if(tailScript != null)
			addToBodyScript(tailScript);
	}

}
