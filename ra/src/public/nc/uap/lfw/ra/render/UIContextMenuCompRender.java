package nc.uap.lfw.ra.render;

import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;

/**
 * @author renxh
 * ÓÒ¼ü²Ëµ¥äÖÈ¾Æ÷
 * @param <T>
 * @param <K>
 */
public class UIContextMenuCompRender extends UINormalComponentRender<UIComponent, ContextMenuComp> {
	public UIContextMenuCompRender(UIComponent uiEle, ContextMenuComp webEle, UIMeta uimeta, PageMeta pageMeta, IUIRender parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}

	public String generateBodyHtml() {
		
		return "";
	}
	
	public String generateBodyScript() {
		
		return "";
	}

	
	protected String getSourceType(IEventSupport ele) {
		
		return LfwPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM;
	}


	@Override
	public void notifyRemoveChild(UIMeta uiMeta, PageMeta pageMeta,Object obj) {
		super.notifyRemoveChild(uiMeta, pageMeta, obj);
	}
}
