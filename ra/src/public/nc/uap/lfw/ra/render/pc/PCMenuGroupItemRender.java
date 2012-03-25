package nc.uap.lfw.ra.render.pc;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UIMenuGroupItem;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UILayoutPanelRender;
import nc.uap.lfw.ra.render.UILayoutRender;

/**
 * @author renxh
 * 菜单组控件元素渲染器
 */
public class PCMenuGroupItemRender extends UILayoutPanelRender<UIMenuGroupItem, WebElement> {

	public PCMenuGroupItemRender(UIMenuGroupItem uiEle, UIMeta uimeta, PageMeta pageMeta, UILayoutRender<? extends UILayout, ? extends WebElement> parentRender) {
		super(uiEle, uimeta, pageMeta, parentRender);
		UIMenuGroupItem item = this.getUiElement();
		try {
			this.state = item.getState().toString();
		} catch (Exception e) {
			LfwLogger.error(e);
		}

	}

	private String state = null;

	public String addMenuBar(String menubarId) {
		StringBuffer sb = new StringBuffer();
		UILayoutRender<?, ?> tab = (UILayoutRender<?, ?>) this.getParentRender();
		String groupId = COMP_PRE + tab.getId();
		sb.append(groupId).append(".addItem('").append(getState()).append("', ").append(menubarId).append(");\n");
		return sb.toString();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String generalHeadHtml() {

		return "";
	}

	public String generalTailHtml() {

		return "";
	}

	public String generalTailScript() {

		return "";
	}

	public String generalHeadScript() {

		return "";
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_MENU_GROUP_ITEM;
	}

	@Override
	public String generalHeadHtmlDynamic() {
		return "";
	}

}
