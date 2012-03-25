package nc.uap.lfw.core.tags;

import java.util.List;

import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * ContextMenuComp�Ľ���Tag 
 */

public class ContextMenuCompTag extends NormalComponentTag {

 	// �䱾����Ҽ��˵�
	private String theContextMenu = null;
	
	public String generateBodyScript() {
		ContextMenuComp contextMenu = (ContextMenuComp)this.getComponent();
	    // �䱾����Ҽ��˵�
    	theContextMenu = contextMenu.getContextMenu();
		StringBuffer buf = new StringBuffer();
	    buf.append("window.")
	       .append(getVarShowId())
	       .append(" = new ContextMenuComp(\"")
           .append(this.getId())
           .append("\" ,0 ,0 ,false);\n");
	    
	    if(this.getCurrWidget() != null){
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append(widget + ".addMenu(" + getVarShowId() + ");\n");
		}
	    
//	    if (!contextMenu.getWidth().equals("100%")) {
	    	buf.append("window.")
		       .append(getVarShowId())
		       .append(".setWidth(")
		       .append("120")
		       .append(");\n");
//	    }

	    List<MenuItem> itemList = contextMenu.getItemList();
 	    if(itemList != null){
	    	for(MenuItem item : itemList)
	    	{
	    		if (item.isSep()) {
	    			buf.append(getVarShowId())
		    		   .append(".addSep();\n");
	    		} else {
		    		String itemShowId = "$MI_" + item.getId();
		    		buf.append("var ")
		    		   .append(itemShowId)
		    		   .append(" = ")
		    		   .append("window.")
		    		   .append(getVarShowId())
		    		   .append(".addMenu(\"")
		    		   .append(item.getId())
		    		   .append("\",\"")
		    		   .append(translate(item.getI18nName(), item.getText(), item.getLangDir())) 
		    		   .append("\");\n");
		    		
		    		// Ϊ�������ӿ�ݼ�
		    		String hotKey = item.getHotKey();
					String modifier = String.valueOf(item.getModifiers());
		    		if (hotKey != null && !"".equals(hotKey)) {
		    			buf.append(itemShowId)
		    			   .append(".setHotKey(\"")
		    			   .append(hotKey+modifier)
		    			   .append("\");\n");
		    		}
		    		
		    		genScriptForItem(buf, item);
	    		
		    		// ���Ҽ��˵�
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
	 * Ϊ���������Ӳ˵��Ľű�
	 * @param buf
	 * @param item
	 */
	private void genScriptForItem(StringBuffer buf, MenuItem item) {
		List<MenuItem> itemList = item.getChildList();
		if (itemList != null && itemList.size() > 0) {
			String itemShowId = "$MI_" + item.getId();
			for(MenuItem subItem : itemList) {
				if (subItem.isSep()) {
	    			buf.append(itemShowId)
		    		   .append(".addSep();\n");
	    		} else {
		    		String subItemShowId = "$MI_" + subItem.getId();
					buf.append("var ")
		    		   .append(subItemShowId)
		    		   .append(" = ")
		    		   .append(itemShowId)
		    		   .append(".addMenu(\"")
		    		   .append(subItem.getId())
		    		   .append("\",\"")
		    		   .append(translate(subItem.getI18nName(), subItem.getText(), subItem.getLangDir())) 
		    		   .append("\");\n");
		    		
		    		// Ϊ�������ӿ�ݼ�
		    		String hotKey = subItem.getHotKey();
					String modifier = String.valueOf(subItem.getModifiers());
		    		if (hotKey != null && !"".equals(hotKey)) {
		    			buf.append(subItemShowId)
		    			   .append(".setHotKey(\"")
		    			   .append(hotKey+modifier)
		    			   .append("\");\n");
		    		}
		    		
		    		// ���Ҽ��˵�
					if (theContextMenu != null && !"".equals(theContextMenu)) {
						buf.append(addContextMenu(theContextMenu, subItemShowId));
					}
					
					buf.append(this.addEventSupport(subItem, getCurrWidget().getId(), subItemShowId, getId()));
					
					genScriptForItem(buf, subItem);
	    		}
			}
		}
	}
	
	/**
	 * ��õ�ǰ�ؼ�����ӦComponent 
	 * @return
	 */
	protected WebComponent getComponent()
	{
		LfwWidget widget = getCurrWidget();
		WebComponent comp = widget.getViewMenus().getContextMenu(getId());
		if(comp == null)
			throw new LfwRuntimeException(this.getClass().getName() + ":can not find the component by id:" + getId());
		return comp;
	}
	
//	/**
//	 * �ݹ����ɸ�menuItem�������е��Ӳ˵���
//	 * @param item
//	 * @return
//	 */
//	private String genScriptForItem(String parentId, MenuItem item)
//	{
//		StringBuffer buf = new StringBuffer();
//		
//		
//		
//		return buf.toString();
//	}
	
	/**
	 * ContextMenu����Ҫ������ռλ��
	 */
	public String generateBody() 
	{
		return "";
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM;
	}

}

