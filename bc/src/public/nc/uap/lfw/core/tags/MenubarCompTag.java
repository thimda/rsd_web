package nc.uap.lfw.core.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.util.StringUtil;

/**
 * Menubar 控件Tag
 * @author dengjt
 * @author gd 2008-02-19 加入快捷键的全面支持
 */
public class MenubarCompTag extends NormalComponentTag {

	private static final String BEFORE_SCRIPT = "beforeScript";
	private static final String AFTER_SCRIPT = "afterScript";
	
	private String menubarVarId = "";
	
	/**
	 * menubar属于pageMeta,不属于任何widget
	 */
	public void doTag() throws JspException, IOException {
		String script;
//		if(!LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION)){
//			script = addBeforeJsLog();
//			addToBodyScript(script);
//		}
		script = (String) getAttribute(BEFORE_SCRIPT);
		if(script != null)
			addToBodyScript(script);
		
		super.doRender();
		
		script = (String) getAttribute(AFTER_SCRIPT);
		if(script != null)
		{	
			addToBodyScript(script);
		}
		
//		if(!LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION)){
//			script = addAfterJsLog();
//			addToBodyScript(script);
//		}
	}
	
	public String generateBody() {
		// 该处position改为absolute,解决多个menubar时第二个开始的menubar无法显示在左上角的问题
		return 	"<div id=\"" + getDivShowId() + "\" style=\"position:relative;width:100%;height:100%;overflow:hidden;\"></div>";
	}

	public String generateBodyScript() { 
		MenubarComp menubar = (MenubarComp) getComponent();
		StringBuffer buf = new StringBuffer();
		
//		//parent, name, left, top, width, height, position, className
//		buf.append("var ")
//		   .append(getVarShowId())
//		   .append(" = ButtonManager.getInstance().createMenuBar($ge('" + DIV_PRE + menubar.getId() + "'), '" + menubar.getId() + "', 0, 0, '100%', '100%', 'relative','" + (menubar.getClassName() == null || menubar.getClassName().equals("") ? "":menubar.getClassName()) + "', '');\n");
//		if(menubar.getMenuList() != null)
//		{
//			Iterator<MenuItem> it = menubar.getMenuList().iterator();
//			while(it.hasNext())
//			{
//				MenuItem item = it.next();
//				buf.append("var ")
//				   .append(COMP_PRE)
//				   .append(item.getId())
//				   .append(" = ButtonManager.getInstance().createMenu(")
//				   .append(getVarShowId())
//				   .append(",null,\"")
//				   .append(item.getId())
//				   .append("\", \"")
//				   .append(translate(item.getI18nName(), item.getText(), item.getLangDir()))
//				   .append("\",'")
//				   .append(item.getTip() == null?translate(item.getI18nName(), item.getText(), item.getLangDir()): translate(item.getTip(), item.getTip(), item.getLangDir()))
//				   .append("',");
//				if(item.getImgIcon() != null){
//					buf.append("'")
//					   .append(getRealImgPath(item.getImgIcon()))
//					   .append("',");
//				}
//				else
//					buf.append("null,");
//				buf.append("{opStatusArray:")
//				   .append(StringUtil.mergeScriptArrayForInteger(item.getOperatorStatusArray()))
//				   .append(",busiStatusArray:")
//				   .append(StringUtil.mergeScriptArrayForInteger(item.getBusinessStatusArray()))
//				   .append(",btnCode:'")
//				   .append(item.getBtnCode() == null? "": item.getBtnCode())
//				   .append("',btnNo:'")
//				   .append(item.getBtnNo())
//				   .append("'},")
//				   .append(item.isCheckBoxGroup());
//				   if(item.getHotKey() != null)
//					   buf.append(",[" + (item.getModifiers() == 0 ? "": item.getModifiers())+ ",'" + item.getHotKey() + "'],");
//				   else
//					   buf.append(",null,");
//				   buf.append("{imgIconOn:")
//				      .append(item.getImgIconOn() == null ? "''" : "'" + getRealImgPath(item.getImgIconOn()) + "'")
//				      .append(",imgIconDisable:")
//				      .append(item.getImgIconDisable() == null ? "''" : "'" + getRealImgPath(item.getImgIconDisable()) + "'")
//				      .append("}")
//				      .append(");\n");
//				   
//				buf.append(addEventSupport(item, getCurrWidget().getId(), COMP_PRE + item.getId(), getId()));
//				buf.append(genItemScriptForButtonManager(item));
//			}
//		}
//		//buf.append("ButtonManager.getInstance().setOperateStatus(IBillOperate.OP_INIT);\n");
		
		menubarVarId = getVarShowId();
		buf.append("var ")
		   .append(menubarVarId)
		   .append(" = new MenuBarComp(document.getElementById(\"" + getDivShowId() + "\"), \"" + menubar.getId() + "\", 0, 0, \"100%\", \"100%\");\n");
	
		if(menubar.getMenuList() != null)
		{
			Iterator<MenuItem> it = menubar.getMenuList().iterator();
			while(it.hasNext())
			{	//parent, menuId, menuCaption, menuTip, menuSrcImg, isCheckBoxGroup
				MenuItem item = it.next();
				if (item.isSep()) {
					buf.append(menubarVarId)
		    		   .append(".addSep();\n");
				} else {
					String menuId = menubarVarId + item.getId();
					// 提示信息
					String tip = item.getTip() == null?translate(item.getI18nName(),item.getText(), item.getLangDir()): translate(item.getTip(), item.getTip(), item.getLangDir());
					String displayHotKey = item.getDisplayHotKey();
					if (displayHotKey != null && !"".equals(displayHotKey)) {
						tip += "(" + displayHotKey + ")";
					}
					buf.append("var ")
					   .append(menuId)
					   .append(" = ")
					   .append(menubarVarId)
					   .append(".addMenu(\"")
					   .append(item.getId())
					   .append("\", \"")
					   .append(translate(item.getI18nName(),item.getText(), item.getLangDir()))
					   .append("\",'")
					   .append(tip)
					   .append("', ");
					if(item.getImgIcon() != null){
						buf.append("'")
//						   .append(getRealImgPath(item.getImgIcon()))
						   .append(item.getRealImgIcon())
						   .append("', ");
					} else
						buf.append("null, ");
					buf.append(item.isCheckBoxGroup())
					   .append(", ")
					   .append(item.isSelected())
					   .append(", ")
					   .append("{imgIconOn:")
//				       .append(item.getImgIconOn() == null ? "''" : "'" + getRealImgPath(item.getImgIconOn()) + "'")
				       .append(item.getImgIconOn() == null ? "''" : "'" + item.getRealImgIconOn() + "'")
					   .append(",imgIconDisable:")
//				       .append(item.getImgIconDisable() == null ? "''" : "'" + getRealImgPath(item.getImgIconDisable()) + "'")
					   .append(item.getImgIconDisable() == null ? "''" : "'" + item.getRealImgIconDisable() + "'")
				       .append("}")
				       .append(");\n");
					  
//					buf.append(menuId + ".operateStateArray=" + StringUtil.mergeScriptArrayStrForInteger(item.getOperatorStatusArray()))
//					   .append(";\n")
//					   .append(menuId + ".businessStateArray=" + StringUtil.mergeScriptArrayStrForInteger(item.getBusinessStatusArray()))
//					   .append(";\n")
//					   .append(menuId + ".userStateArray=" + StringUtil.mergeScriptArrayStrForInteger(item.getUserStatusArray()))
//					   .append(";\n");
//	
//					//按钮可视状态数组
//					buf.append(menuId + ".operateVisibleStateArray=" + StringUtil.mergeScriptArrayStrForInteger(item.getOperatorVisibleStatusArray()))
//					.append(";\n")
//					.append(menuId + ".businessVisibleStateArray=" + StringUtil.mergeScriptArrayStrForInteger(item.getBusinessVisibleStatusArray()))
//					.append(";\n")
//					.append(menuId + ".userVisibleStateArray=" + StringUtil.mergeScriptArrayStrForInteger(item.getUserVisibleStatusArray()))
//					.append(";\n");
					
					// 为子项设置快捷键
					String hotKey = item.getHotKey();
					String modifier = String.valueOf(item.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(menuId)
						   .append(".setHotKey(\"")
						   .append(hotKey+modifier)
						   .append("\");\n");
					}
					
					if (!item.isEnabled()) {
						buf.append(menuId + ".setActive(false);\n");
					}
					
					String widgetId = null;
					if(this.getCurrWidget() != null)
						widgetId = this.getCurrWidget().getId();
					 
					//跟据页面状态，更新按钮
					if (widgetId != null)
						buf.append("if ((pageUI.getWidget('" + widgetId + "') != null) && (pageUI.getWidget('" + widgetId + "').operateState != null || pageUI.operateState != null))")
						.append(";\n")
						.append("{" + menuId + ".updateState(pageUI.getWidget('" + widgetId + "').operateState,pageUI.getWidget('" + widgetId + "').operateState);}\n");
					else
						buf.append("if (pageUI.operateState != null || pageUI.operateState != null)")
						.append(";\n")
						.append("{" + menuId + ".updateState(pageUI.operateState,pageUI.operateState);}\n");
						
					
					buf.append(addEventSupport(item, widgetId, menuId, getId()));
					buf.append(genItemScript(item));
				}
			}
		}
		
		MenubarGroupItemTag groupItem = (MenubarGroupItemTag) findAncestorWithClass(this, MenubarGroupItemTag.class);
		if(groupItem != null)
			buf.append(groupItem.addMenuBar(menubarVarId));
		else{
			if(this.getCurrWidget() != null){
				String widget = WIDGET_PRE + this.getCurrWidget().getId();
				buf.append(widget + ".addMenu(" + menubarVarId + ");\n");
			}
			else{
				buf.append("pageUI.addMenubar(" + menubarVarId + ");\n");
			}
		}
		
		return buf.toString();
	}
	
	/**
	 * 生成子项菜单的JS代码
	 * @param item
	 * @return
	 */
	private String genItemSubMenuScript(MenuItem item) {
		StringBuffer buf = new StringBuffer();
		
		if (item.getChildList() != null && item.getChildList().size() > 0) {
			// 其本身的右键菜单
	    	String theContextMenu = ((MenubarComp) getComponent()).getContextMenu();
			for (MenuItem cItem : item.getChildList()) {
				if (cItem.isSep()) {
					buf.append(menubarVarId)
					   .append(item.getId())
		    		   .append(".addSep();\n");
				} else {
					buf.append("var ")
					   .append(menubarVarId)
					   .append(cItem.getId())
					   .append(" = ")
					   .append(menubarVarId)
					   .append(item.getId())
					   .append(".addMenu(\"")
					   .append(cItem.getId())
					   .append("\", \"")
					   .append(translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir()))
					   .append("\", \"")
					   .append(cItem.getTip() == null ? translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir()): translate(cItem.getTip(), cItem.getTip(), cItem.getLangDir()))
					   .append("\", ");
					if(cItem.getImgIcon() != null){
						buf.append("'")
//						   .append(getRealImgPath(cItem.getImgIcon()))
						   .append(cItem.getRealImgIcon())
						   .append("', ");
					} else
						buf.append("null, ");
					buf.append(cItem.isCheckBoxGroup())
					   .append(", ")
					   .append(cItem.isSelected())
					   .append(", ")
					   .append("{imgIconOn:")
//				       .append(cItem.getImgIconOn() == null ? "''" : "'" + getRealImgPath(cItem.getImgIconOn()) + "'")
				       .append(cItem.getImgIconOn() == null ? "''" : "'" + cItem.getRealImgIconOn() + "'")
				       .append(",imgIconDisable:")
//				       .append(cItem.getImgIconDisable() == null ? "''" : "'" + getRealImgPath(cItem.getImgIconDisable()) + "'")
				       .append(cItem.getImgIconDisable() == null ? "''" : "'" + cItem.getRealImgIconDisable() + "'")
				       .append("}")
				       .append(");\n");
	
//					buf.append(menubarVarId + cItem.getId() + ".operateStateArray=" + StringUtil.mergeScriptArrayStrForInteger(cItem.getOperatorStatusArray()))
//					   .append(";\n")
//					   .append(menubarVarId + cItem.getId() + ".businessStateArray=" + StringUtil.mergeScriptArrayStrForInteger(cItem.getBusinessStatusArray()))
//					   .append(";\n");
		    		
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
	 * @param item
	 * @return
	 */
	private String genItemScript(MenuItem item)
	{
		String parentId = menubarVarId + item.getId();
		StringBuffer buf = new StringBuffer();
		if(item.getChildList() != null && item.getChildList().size() > 0)
		{
			// 其本身的右键菜单
	    	String theContextMenu = ((MenubarComp) getComponent()).getContextMenu();
			Iterator<MenuItem> it = item.getChildList().iterator();
			while(it.hasNext())
			{
				MenuItem cItem = it.next();
				
				if (cItem.isSep()) {
					buf.append(parentId)
		    		   .append(".addSep();\n");
				} else {
					String menuId = menubarVarId + cItem.getId();
					// 提示信息
					String tip = cItem.getTip() == null ? translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir()): translate(cItem.getTip(), cItem.getTip(), cItem.getLangDir());
					String displayHotKey = cItem.getDisplayHotKey();
					if (displayHotKey != null && !"".equals(displayHotKey)) {
						tip += "(" + displayHotKey + ")";
					}
					buf.append("var ")
					   .append(menuId)
					   .append(" = ")
					   .append(parentId)
					   .append(".addMenu(\"")
					   .append(cItem.getId())
					   .append("\", \"")
					   .append(translate(cItem.getI18nName(),cItem.getText(), cItem.getLangDir()))
					   .append("\",'")
					   .append(tip)
					   .append("', ");
					if(cItem.getImgIcon() != null){
						buf.append("'")
//						   .append(getRealImgPath(cItem.getImgIcon()))
						   .append(cItem.getRealImgIcon())
						   .append("', ");
					} else
						buf.append("null, ");
					buf.append(cItem.isCheckBoxGroup())
					   .append(", ")
					   .append(cItem.isSelected())
					   .append(", ")
					   .append("{imgIconOn:")
//				       .append(cItem.getImgIconOn() == null ? "''" : "'" + getRealImgPath(cItem.getImgIconOn()) + "'")
				       .append(cItem.getImgIconOn() == null ? "''" : "'" + cItem.getRealImgIconOn() + "'")
				       .append(",imgIconDisable:")
//				       .append(cItem.getImgIconDisable() == null ? "''" : "'" + getRealImgPath(cItem.getImgIconDisable()) + "'")
				       .append(cItem.getImgIconDisable() == null ? "''" : "'" + cItem.getRealImgIconDisable() + "'")
				       .append("}")
				       .append(");\n");
//					
//					buf.append(menuId + ".operateStateArray=" + StringUtil.mergeScriptArrayStrForInteger(cItem.getOperatorStatusArray()))
//					   .append(";\n")
//					   .append(menuId + ".businessStateArray=" + StringUtil.mergeScriptArrayStrForInteger(cItem.getBusinessStatusArray()))
//					   .append(";\n");
	
					// 为子项设置快捷键
					String hotKey = cItem.getHotKey();
					String modifier = String.valueOf(cItem.getModifiers());
					if (hotKey != null && !"".equals(hotKey)) {
						buf.append(menuId)
						   .append(".setHotKey(\"")
						   .append(hotKey+modifier)
						   .append("\");\n");
					}
		    		
		    		// 加右键菜单
					if (theContextMenu != null && !"".equals(theContextMenu)) {
						buf.append(addContextMenu(theContextMenu, menubarVarId + cItem.getId()));
					}
					
					//buf.append(menuId).append(".widget = " + WIDGET_PRE + getCurrWidget().getId()).append(";\n");
					//buf.append(addCommandScript(cItem));
					
					String widgetId = null;
					if(this.getCurrWidget() != null)
						widgetId = this.getCurrWidget().getId();
					buf.append(addEventSupport(cItem, widgetId, menubarVarId + cItem.getId(), getId()));
					
					buf.append(genItemSubMenuScript(cItem));
				}
			}
		}
		
		return buf.toString();
	}
	
//	private String genItemScriptForButtonManager(MenuItem item)
//	{
//		StringBuffer buf = new StringBuffer();
//		if(item.getChildList() != null)
//		{
//			Iterator<MenuItem> it = item.getChildList().iterator();
//			while(it.hasNext())
//			{
//				MenuItem cItem = it.next();
//				buf.append("var ")
//				   .append(COMP_PRE)
//				   .append(cItem.getId())
//				   .append(" = ButtonManager.getInstance().createMenuItem(")
//				   .append(getVarShowId())
//				   .append(",")
//				   .append(COMP_PRE)
//				   .append(item.getId())
//				   .append(", \"")
//				   .append(cItem.getId())
//				   .append("\", \"")
//				   .append(translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir()))
//				   .append("\",'")
//				   .append(cItem.getTip() == null ? translate(cItem.getI18nName(), cItem.getText(), cItem.getLangDir()) : translate(cItem.getTip(), cItem.getTip(), cItem.getLangDir()))
//				   .append("',null, {opStatusArray:")
//				   .append(StringUtil.mergeScriptArrayForInteger(cItem.getOperatorStatusArray()))
//				   .append(",busiStatusArray:")
//				   .append(StringUtil.mergeScriptArrayForInteger(cItem.getBusinessStatusArray()))
//				   .append(",btnCode:'")
//				   .append(cItem.getBtnCode() == null? "": cItem.getBtnCode())
//				   .append("',btnNo:'")
//				   .append(cItem.getBtnNo())
//				   .append("'},")
//				   .append(cItem.isCheckBoxGroup())
//				   .append(",")
//				   .append(cItem.isSelected());
//					
//				   // 注册热键
//				   if(cItem.getHotKey() != null)
//					   buf.append(",[" + (cItem.getModifiers() == 0 ? "": cItem.getModifiers())+ ",'" + cItem.getHotKey() + "']);\n");
//				   else
//					   buf.append(");\n");
//				buf.append(addEventSupport(cItem, getWidget(), COMP_PRE + cItem.getId(), getId()));
//			}
//		}
//		
//		return buf.toString();
//	}
	
	protected String getVarShowId(){
		return COMP_PRE + getId();
	}

	/**
	 * 获得当前控件所对应Component 
	 * @return
	 */
	protected WebComponent getComponent()
	{
		String widget = getWidget();
		WebComponent comp = null;
		if(widget == null){
			comp = getPageModel().getPageMeta().getViewMenus().getMenuBar(getId());
			if(comp == null){
				LfwWidget currWidget = getCurrWidget();
				if(currWidget != null)
					comp = currWidget.getViewMenus().getMenuBar(getId());
			}
		}
		else{
			LfwWidget currWidget = getCurrWidget();
			comp = currWidget.getViewMenus().getMenuBar(getId());
		}
		if(comp == null)
			throw new LfwRuntimeException(this.getClass().getName() + ":can not find the menubar by id:" + getId());
		return comp;
	}
	
	protected LfwWidget getCurrWidget()
	{
		String w = getWidget();
		if(w != null && !w.equals(""))
			return super.getCurrWidget();
		return null;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_MENUBAR_MENUITEM;
	}
	
//	private String addCommandScript(MenuItem item) {
//		StringBuffer funcBuf = new StringBuffer();
//		if(item.getCommand() != null)
//		{
//			String commandFunc = COMMAND_PRE + getCurrWidget().getId() + "_" + item.getCommand();
//			funcBuf.append(COMP_PRE)
//			       .append(item.getId())
//			       .append(".onclick = ")
//			       .append(commandFunc)
//			       .append(";\n");
//		}
//		return funcBuf.toString();
//	}
}
