package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.comp.ToolBarTitle;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

/**
 * Toolbar 控件Tag
 * 
 * @author guoweic
 */
public class ToolbarCompTag extends NormalComponentTag {

	public String generateBody() {
		return super.generateBody();
	}

	public String generateBodyScript() {
		
		ToolBarComp toolbar = (ToolBarComp) getComponent();
		StringBuffer buf = new StringBuffer();
		String widgetId = this.getCurrWidget().getId();
		String toolbarId = getVarShowId();
		buf.append("var ")
			.append(toolbarId)
			.append(" = new ToolBarComp(document.getElementById(\"" + getDivShowId() + "\"), \"" + toolbar.getId()
					+ "\", 0, 0, \"100%\", \"100%\", null, null, " + toolbar.isTransparent() + ");\n");
		
		buf.append("pageUI.getWidget('" + widgetId + "').addComponent(" + toolbarId + ");\n");
		
		// 加载标题
		ToolBarTitle title = toolbar.getTitle();
		if (null != title) {
			String text = translate(title.getI18nName(), title.getText(), title.getLangDir());
			if ((text != null && !"".equals(text)) || !"".equals(title.getRefImg1())) {
				String titleMenuId = title.getMenuId();
				if (titleMenuId != null && !"".equals(titleMenuId))
					createMenuTag(titleMenuId);
				buf.append(toolbarId)
					.append(".setTitle(\"")
					.append(translate(title.getI18nName(), title.getText(), title.getLangDir()))
					.append("\", \"")
					.append(title.getColor())
					.append("\", ")
					.append(title.isBold())
					.append(", \"")
//					.append(getRealImgPath(title.getRefImg1()))
					.append(title.getRealRefImg1())
					.append("\", \"")
//					.append(getRealImgPath(title.getRefImg2()))
					.append(title.getRealRefImg2())
					.append("\"");
				if (titleMenuId != null && !"".equals(titleMenuId)) {
					String titleMenuShowId = COMP_PRE + getCurrWidget().getId() + "_" + titleMenuId;
					buf.append(", ")
						.append(titleMenuShowId);
				}
				buf.append(");\n");
			}
		}
		
		ToolBarItem[] elements = toolbar.getElements();
		// 加载子项
		if (elements != null) {
			for (int i = 0, n = elements.length; i < n; i++) {
				ToolBarItem item = elements[i];
				if (item.getType() != null && ToolBarItem.BUTTON_TYPE.equals(item.getType()) && "left".equals(item.getAlign())) {  // 按钮子项（左对齐）
					// 提示信息
					String tip = translate(item.getTip(), item.getTipI18nName(), item.getLangDir());
					String displayHotKey = item.getDisplayHotKey();
					if (displayHotKey != null && !"".equals(displayHotKey)) {
						tip += "(" + displayHotKey + ")";
					}
					
					//function(id, text, tip, refImg, align, withSep, width, disabled) {
					buf.append(toolbarId)
					   .append(".addButton(\"")
					   .append(item.getId())
					   .append("\", \"")
					   .append(translate(item.getI18nName(), item.getText(), item.getLangDir()))
					   .append("\", \"")
					   .append(tip)
					   .append("\", \"")
//					   .append(getRealImgPath(item.getRefImg()))
					   .append(item.getRealRefImg())
					   .append("\", \"")
					   .append(item.getAlign()) // 此处配置的align为按钮在Toolbar中的位置
					   .append("\", ")
					   .append(item.isWithSep());
//					if (!"100%".equals(item.getWidth()))
//						buf.append(", \"" + item.getWidth() + "\"");
//					else
						buf.append(", ''");
					buf.append(", " + !item.isEnabled());
					buf.append(");\n");
					
					String itemShowId = toolbarId + ".getButton('" + item.getId() + "')";

					// 为子项设置快捷键
					String hotKey = item.getHotKey();
					String modifier = String.valueOf(item.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(itemShowId)
						   .append(".setHotKey(\"")
						   .append(hotKey + modifier)
						   .append("\");\n");
					}
					
					// 加右键菜单
					String menu = item.getContextMenu();
					if (menu != null && !"".equals(menu)) {
						buf.append(addContextMenu(menu, itemShowId));
					}
					
					buf.append(addEventSupport(item, getWidget(), itemShowId, getId()));
				}
			}
			for (int i = elements.length - 1; i >= 0; i--) {
				ToolBarItem item = elements[i];
				if (item.getType() != null && ToolBarItem.BUTTON_TYPE.equals(item.getType()) && "right".equals(item.getAlign())) {  // 按钮子项（右对齐）
					String itemShowId = "tbi_" + item.getId();
					buf.append("var ")
					   .append(itemShowId)
					   .append(" = ")
					   .append(toolbarId)
						.append(".addButton(\"")
						.append(item.getId())
						.append("\", \"")
						.append(translate(item.getI18nName(), item.getText(), item.getLangDir()))
						.append("\", \"")
						.append(translate(item.getTip(), item.getTipI18nName(), item.getLangDir()))
						.append("\", \"")
//						.append(getRealImgPath(item.getRefImg()))
						.append(item.getRealRefImg())
						.append("\", \"")
						.append(item.getAlign()) // 此处配置的align为按钮在Toolbar中的位置
						.append("\", ")
						.append(item.isWithSep());
//					if (!"100%".equals(item.getWidth()))
//						buf.append(", \"" + item.getWidth() + "\"");
					buf.append(");\n");
					
					buf.append(addEventSupport(item, getWidget(), itemShowId, getId()));
				}
			}
			for (ToolBarItem item : elements) {
				if (item.getType() != null || !ToolBarItem.BUTTON_TYPE.equals(item.getType())) {//TODO 其他子项
					
				}
			}
		}

		return buf.toString();
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_TOOLBAR_BUTTON;
	}

}
