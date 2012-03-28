package nc.uap.lfw.ra.render.pc.tag;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UIElement;

public class RaButtonCompTag extends RaWebComponentTag {

	@Override
	protected UIElement getUIElement() {
		WebElement comp = getWebElement();
		UIButton uibutton = new UIButton();
		uibutton.setId(comp.getId());
		uibutton.setWidgetId(getWidgetId());
		return uibutton;
	}

}
