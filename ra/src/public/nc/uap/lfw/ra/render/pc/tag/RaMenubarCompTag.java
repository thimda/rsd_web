package nc.uap.lfw.ra.render.pc.tag;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMenubarComp;

public class RaMenubarCompTag extends RaWebComponentTag {

	@Override
	protected UIElement getUIElement() {
		WebElement comp = getPageMeta().getWidget(getWidgetId()).getViewMenus().getMenuBar(getId());
		UIMenubarComp uimenubar = new UIMenubarComp();
		uimenubar.setId(comp.getId());
		uimenubar.setWidgetId(getWidgetId());
		return uimenubar;
	}

}
