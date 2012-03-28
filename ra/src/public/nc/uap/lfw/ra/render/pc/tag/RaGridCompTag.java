package nc.uap.lfw.ra.render.pc.tag;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIGridComp;

public class RaGridCompTag extends RaWebComponentTag {
	@Override
	protected UIElement getUIElement() {
		WebElement comp = getWebElement();
		UIGridComp uigrid = new UIGridComp();
		uigrid.setId(comp.getId());
		uigrid.setWidgetId(getWidgetId());
		return uigrid;
	}

}
