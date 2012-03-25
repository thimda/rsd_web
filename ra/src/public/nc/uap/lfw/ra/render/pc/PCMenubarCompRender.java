package nc.uap.lfw.ra.render.pc;

import java.util.Iterator;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMenubarComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh
 * 菜单栏控件渲染器
 */
public class PCMenubarCompRender extends UINormalComponentRender<UIMenubarComp, MenubarComp> {

	public PCMenubarCompRender(UIMenubarComp uiEle,MenubarComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}

	private String menubarVarId = "";

	@Override
	public String generateBodyHtml() {
		// 该处position改为absolute,解决多个menubar时第二个开始的menubar无法显示在左上角的问题
		StringBuffer buf = new StringBuffer();
		buf.append("<div id=\"" + getDivId() + "\" style=\"position:relative;width:100%;height:100%;\"></div>");
		return buf.toString();
	}
	
	
	@Override
	public String generateBodyHtmlDynamic() {
		// 该处position改为absolute,解决多个menubar时第二个开始的menubar无法显示在左上角的问题
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getDivId()).append(" = $ce('DIV');\n");
		buf.append(getDivId()).append(".style.position = 'relative';\n");
		buf.append(getDivId()).append(".style.width = '100%';\n");
		buf.append(getDivId()).append(".style.height = '100%';\n");
//		buf.append(getDivId()).append(".style.overflow = 'hidden';\n");
		buf.append(getDivId()).append(".id = '"+getDivId()+"';\n");
		return buf.toString();
	}
	
	/**
	 * 
	 * @param isDynamic 动态脚本 或 非动态脚本
	 * @return
	 */
	public String generateBodyScript() {
		MenubarComp menubar = this.getWebElement();
		StringBuffer buf = new StringBuffer();

		menubarVarId = getVarId();
		buf.append("var ").append(menubarVarId);
		buf.append(" = new MenuBarComp(document.getElementById(\"" + getDivId() + "\"), \"" );
		
		buf.append(menubar.getId() + "\", 0, 0, \"100%\", \"100%\");\n");

		if (menubar.getMenuList() != null) {
			Iterator<MenuItem> it = menubar.getMenuList().iterator();
			while (it.hasNext()) {
				MenuItem item = it.next();
				item.setWidget(menubar.getWidget());
				if (item.isSep()) {
					buf.append(menubarVarId).append(".addSep();\n");
				} else {
					String menuId = menubarVarId + item.getId();
					// 提示信息
					String tip = item.getTip() == null ? translate(item.getI18nName(), item.getText(), item.getLangDir()) : translate(item.getTip(),
							item.getTip(), item.getLangDir());
					String displayHotKey = item.getDisplayHotKey();
					if (displayHotKey != null && !"".equals(displayHotKey)) {
						tip += "(" + displayHotKey + ")";
					}
					buf.append("var ").append(menuId).append(" = ");
					buf.append(menubarVarId).append(".addMenu(\"").append(item.getId()).append("\", \"");
					buf.append(translate(item.getI18nName(), item.getText(), item.getLangDir())).append("\",'").append(tip).append("', ");
					if (item.getImgIcon() != null) {
						buf.append("'").append(item.getRealImgIcon()).append("', ");
					} else
						buf.append("null, ");
					buf.append(item.isCheckBoxGroup()).append(", ");
					buf.append(item.isSelected()).append(", ");
					buf.append("{imgIconOn:").append(
							item.getImgIconOn() == null ? "''" : "'" + item.getRealImgIconOn() + "'");
					buf.append(",imgIconDisable:").append(
							item.getImgIconDisable() == null ? "''" : "'" + item.getRealImgIconDisable() + "'");
					buf.append("}").append(");\n");

					// 为子项设置快捷键
					String hotKey = item.getHotKey();
					String modifier = String.valueOf(item.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(menuId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
					}

					if (!item.isEnabled()) {
						buf.append(menuId + ".setActive(false);\n");
					}
					
					String widgetId = this.getWidget();
					 
					//跟据页面状态，更新按钮
					if (widgetId != null)
						buf.append("if ((pageUI.getWidget('" + widgetId + "') != null) && (pageUI.getWidget('" + widgetId + "').operateState != null || pageUI.operateState != null))")
						.append(";\n")
						.append("{" + menuId + ".updateState(pageUI.getWidget('" + widgetId + "').operateState,pageUI.getWidget('" + widgetId + "').operateState);}\n");
					else
						buf.append("if (pageUI.operateState != null || pageUI.operateState != null)").append(";\n");
						buf.append("{" + menuId + ".updateState(pageUI.operateState,pageUI.operateState);}\n");

					buf.append(addEventSupport(item, this.getWidget(), menuId, getId()));
					buf.append(genItemScript(item));
				}
			}
		}

		if (this.getParentRender() instanceof PCMenuGroupItemRender) {
			PCMenuGroupItemRender groupItem = (PCMenuGroupItemRender) this.getParentRender();
			buf.append(groupItem.addMenuBar(menubarVarId));
		} else {
			if (this.getCurrWidget() != null) {
				String widget = WIDGET_PRE + this.getWidget();
				buf.append("var ").append(widget).append(" = pageUI.getWidget('"+this.getCurrWidget().getId()+"');\n");
				buf.append(widget + ".addMenu(" + menubarVarId + ");\n");
			} else
				buf.append("pageUI.addMenubar(" + menubarVarId + ");\n");
		}

		return buf.toString();
	}
	
	

	/**
	 * 生成子项菜单的JS代码
	 * 
	 * @param item
	 * @return
	 */
	private String genItemSubMenuScript(MenuItem item) {
		StringBuffer buf = new StringBuffer();

		if (item.getChildList() != null && item.getChildList().size() > 0) {
			// 其本身的右键菜单
			String theContextMenu = this.getWebElement().getContextMenu();
			for (MenuItem cItem : item.getChildList()) {
				if (cItem.isSep()) {
					buf.append(menubarVarId).append(item.getId()).append(".addSep();\n");
				} else {
					buf.append("var ").append(menubarVarId).append(cItem.getId()).append(" = ").append(menubarVarId).append(item.getId()).append(
							".addMenu(\"").append(cItem.getId()).append("\", \"").append(
							translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir())).append("\", \"").append(
							cItem.getTip() == null ? translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir()) : translate(cItem.getTip(),
									cItem.getTip(), cItem.getLangDir())).append("\", ");
					if (cItem.getImgIcon() != null) {
						buf.append("'").append(cItem.getRealImgIcon()).append("', ");
					} else
						buf.append("null, ");
					buf.append(cItem.isCheckBoxGroup()).append(", ").append(cItem.isSelected()).append(", ").append("{imgIconOn:").append(
							cItem.getImgIconOn() == null ? "''" : "'" + cItem.getRealImgIconOn() + "'").append(",imgIconDisable:").append(
							cItem.getImgIconDisable() == null ? "''" : "'" + cItem.getRealImgIconDisable() + "'").append("}").append(");\n");

//					buf.append(
//							menubarVarId + cItem.getId() + ".operateStateArray="
//									+ StringUtil.mergeScriptArrayStrForInteger(cItem.getOperatorStatusArray())).append(";\n").append(
//							menubarVarId + cItem.getId() + ".businessStateArray="
//									+ StringUtil.mergeScriptArrayStrForInteger(cItem.getBusinessStatusArray())).append(";\n");

					// 加右键菜单
					if (theContextMenu != null && !"".equals(theContextMenu)) {
						buf.append(addContextMenu(theContextMenu, menubarVarId + cItem.getId()));
					}

					buf.append(addEventSupport(cItem, null, menubarVarId + cItem.getId(), getId()));

					buf.append(genItemSubMenuScript(cItem));
				}
			}
		}
		return buf.toString();
	}

	/**
	 * 产生每一个菜单项的script脚本
	 * 
	 * @param item
	 * @return
	 */
	private String genItemScript(MenuItem item) {
		String parentId = menubarVarId + item.getId();
		StringBuffer buf = new StringBuffer();
		if (item.getChildList() != null && item.getChildList().size() > 0) {
			// 其本身的右键菜单
			String theContextMenu = this.getWebElement().getContextMenu();
			Iterator<MenuItem> it = item.getChildList().iterator();
			while (it.hasNext()) {
				MenuItem cItem = it.next();

				if (cItem.isSep()) {
					buf.append(parentId).append(".addSep();\n");
				} else {
					String menuId = menubarVarId + cItem.getId();
					// 提示信息
					String tip = cItem.getTip() == null ? translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir()) : translate(cItem
							.getTip(), cItem.getTip(), cItem.getLangDir());
					String displayHotKey = cItem.getDisplayHotKey();
					if (displayHotKey != null && !"".equals(displayHotKey)) {
						tip += "(" + displayHotKey + ")";
					}
					buf.append("var ").append(menuId).append(" = ").append(parentId).append(".addMenu(\"").append(cItem.getId()).append("\", \"")
							.append(translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir())).append("\",'").append(tip).append("', ");
					if (cItem.getImgIcon() != null) {
						buf.append("'").append(cItem.getRealImgIcon()).append("', ");
					} else
						buf.append("null, ");
					buf.append(cItem.isCheckBoxGroup()).append(", ").append(cItem.isSelected()).append(", ").append("{imgIconOn:").append(
							cItem.getImgIconOn() == null ? "''" : "'" + cItem.getRealImgIconOn() + "'").append(",imgIconDisable:").append(
							cItem.getImgIconDisable() == null ? "''" : "'" + cItem.getRealImgIconDisable() + "'").append("}").append(");\n");

//					buf.append(menuId + ".operateStateArray=" + StringUtil.mergeScriptArrayStrForInteger(cItem.getOperatorStatusArray())).append(";\n")
//							.append(menuId + ".businessStateArray=" + StringUtil.mergeScriptArrayStrForInteger(cItem.getBusinessStatusArray())).append(
//									";\n");

					// 为子项设置快捷键
					String hotKey = cItem.getHotKey();
					String modifier = String.valueOf(cItem.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(menuId).append(".setHotKey(\"").append(hotKey + modifier).append("\");\n");
					}

					// 加右键菜单
					if (theContextMenu != null && !"".equals(theContextMenu)) {
						buf.append(addContextMenu(theContextMenu, menubarVarId + cItem.getId()));
					}
					
					LfwWidget currentWidget = getCurrWidget();
					cItem.setWidget(currentWidget);

					buf.append(addEventSupport(cItem, this.getWidget(), menubarVarId + cItem.getId(), getId()));

					buf.append(genItemSubMenuScript(cItem));
				}
			}
		}

		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_MENUBAR_MENUITEM;
	}
	
	@Override
	protected String getRenderType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_MENUBAR;
	}
	//删除menuitem的操作
	@Override
	public void notifyRemoveChild(UIMeta uiMeta,PageMeta pageMeta,Object obj) {
		throw new LfwRuntimeException("未实现");
	}
	
	// 销毁menubar自身
	@Override
	public void notifyDestroy(UIMeta uiMeta,PageMeta pageMeta,Object obj) {
		String divId = this.getDivId();
		UIComponent uiEle = this.getUiElement();
		if (pageMeta != null && this.getUiElement()!=null) {
			LfwWidget widget = pageMeta.getWidget(this.getUiElement().getWidgetId());
			if (widget != null) {
				WebComponent webButton = (WebComponent) widget.getViewMenus().getMenuBar(uiEle.getId());
				StringBuffer buf = new StringBuffer();
				if(webButton != null){
					if (divId != null) {
						buf.append("window.execDynamicScript2RemoveComponent('" + divId + "','" + uiEle.getWidgetId() + "','" + webButton.getId() + "');");
						widget.getViewMenus().removeMenuBar(uiEle.getId());
					} else {
						buf.append("alert('删除控件失败！未获得divId')");
					}
				}
				addDynamicScript(buf.toString());

			}else{
				
			}
		}
	}


	
	
	

}
