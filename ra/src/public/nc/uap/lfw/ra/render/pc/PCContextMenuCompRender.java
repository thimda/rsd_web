package nc.uap.lfw.ra.render.pc;

import java.util.List;

import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IUIRender;
import nc.uap.lfw.ra.render.UIContextMenuCompRender;

/**
 * @author renxh 
 * 右键弹出菜单render
 * @param <T>
 * @param <K>
 */
public class PCContextMenuCompRender extends UIContextMenuCompRender{

	// 其本身的右键菜单
	private String theContextMenu = null;

	private static final String CONTEXTMENU_BASE = "pc_contextmenu_";

	public PCContextMenuCompRender(UIComponent uiEle, ContextMenuComp webEle, UIMeta uimeta, PageMeta pageMeta, IUIRender parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
		ContextMenuComp contextMenu = (ContextMenuComp) this.getWebElement();
		this.id = contextMenu.getId();
		if (id == null) {
			this.id = this.getUniqueId(CONTEXTMENU_BASE);
		}
		if(contextMenu.getWidget()!=null)
			this.widget = contextMenu.getWidget().getId();
		divId = DIV_PRE + ((this.getWidget()==null || this.getWidget().equals(""))?CONTEXTMENU_BASE: this.getWidget()+"_")+ id;
		varId = COMP_PRE + ((this.getWidget()==null || this.getWidget().equals(""))?CONTEXTMENU_BASE: this.getWidget()+"_") + id;
	}

	public String generateBodyHtml() {

		return "";
	}

	public String generateBodyScript() {

		ContextMenuComp contextMenu = (ContextMenuComp) this.getWebElement();
		// 其本身的右键菜单
		theContextMenu = contextMenu.getContextMenu();

		StringBuffer buf = new StringBuffer();
		buf.append("window.").append(getVarId()).append(" = new ContextMenuComp(\"").append(this.getId()).append("\" ,0 ,0 ,false);\n");

		if (this.getCurrWidget() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append(widget + ".addMenu(" + getVarId() + ");\n");
		}

		//if (!contextMenu.getWidth().equals("100%")) {
		buf.append("window.").append(getVarId()).append(".setWidth(60);\n");
		//}

		List<MenuItem> itemList = contextMenu.getItemList();
		if (itemList != null) {
			for (MenuItem item : itemList) {
				if (item.isSep()) {
					buf.append(getVarId()).append(".addSep();\n");
				} else {
					String itemShowId = "$MI_" + item.getId();
					buf.append("var ").append(itemShowId).append(" = ").append("window.").append(getVarId()).append(".addMenu(\"").append(
							item.getId()).append("\",\"").append(translate(item.getI18nName(), item.getText(), item.getLangDir())).append("\");\n");

					// 为子项增加快捷键
					String hotKey = item.getHotKey();
					String modifier = String.valueOf(item.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(itemShowId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
					}

					genScriptForItem(buf, item);

					// 加右键菜单
					if (theContextMenu != null && !"".equals(theContextMenu)) {
						buf.append(addContextMenu(theContextMenu, itemShowId));
					}

					buf.append(this.addEventSupport(item, contextMenu.getWidget().getId(), itemShowId, getId()));
				}
			}
		}

		return buf.toString();
	}

	/**
	 * 为子项增加子菜单的脚本
	 * 
	 * @param buf
	 * @param item
	 */
	private void genScriptForItem(StringBuffer buf, MenuItem item) {
		List<MenuItem> itemList = item.getChildList();
		if (itemList != null && itemList.size() > 0) {
			String itemShowId = "$MI_" + item.getId();
			for (MenuItem subItem : itemList) {
				if (subItem.isSep()) {
					buf.append(itemShowId).append(".addSep();\n");
				} else {
					String subItemShowId = "$MI_" + subItem.getId();
					buf.append("var ").append(subItemShowId).append(" = ").append(itemShowId).append(".addMenu(\"").append(subItem.getId()).append(
							"\",\"").append(translate(subItem.getI18nName(), subItem.getText(), subItem.getLangDir())).append("\");\n");

					// 为子项增加快捷键
					String hotKey = subItem.getHotKey();
					String modifier = String.valueOf(subItem.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(subItemShowId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
					}

					// 加右键菜单
					if (theContextMenu != null && !"".equals(theContextMenu)) {
						buf.append(addContextMenu(theContextMenu, subItemShowId));
					}

					buf.append(this.addEventSupport(subItem, getCurrWidget().getId(), subItemShowId, getId()));

					genScriptForItem(buf, subItem);
				}
			}
		}
	}


	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM;
	}

}
