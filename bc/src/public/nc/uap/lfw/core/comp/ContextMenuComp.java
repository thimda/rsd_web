package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ContextMenuContext;
import nc.uap.lfw.core.comp.ctx.MenuItemContext;
import nc.uap.lfw.core.event.conf.ContextMenuListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 *
 * ContextMenu的后台控件类
 *
 * create on 2007-4-13 上午08:37:30
 *
 * @author lkp 
 */


public class ContextMenuComp extends WebComponent {

	private static final long serialVersionUID = -6485980427266955507L;

	public final static String TRIGGER_ID = "TRIGGER_ID";
	
    private List<MenuItem> itemList = null;
    
    public ContextMenuComp() {}
    
    public ContextMenuComp(String id)
    {
    	super(id);
    }
    
    public List<MenuItem> getItemList()
    {
    	
    	return this.itemList;
    }
    
    public void setItemList(List<MenuItem> list)
    {
    	this.itemList = list;
    }
    
    public void addMenuItem(MenuItem item)
    {
    	if(this.itemList == null)
    		this.itemList = new ArrayList<MenuItem>();
    	this.itemList.add(item);
    }
    
    public Object clone()
    {
    	
    	ContextMenuComp contextMenu = (ContextMenuComp)super.clone();
    	contextMenu.itemList = new ArrayList<MenuItem>();
    	if(this.itemList != null)
    	{
	    	for(MenuItem child : this.itemList)
	    	{
	    		contextMenu.itemList.add((MenuItem)child.clone());
	    	}
    	}
    	return contextMenu;
    }

	@Override
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(ContextMenuListener.class);
		return list;
	}
	
	/**
	 * 查看ContextMenuComp的Context是否发生改变
	 * @param menu
	 * @return
	 */
	private boolean checkContextMenuCtxChanged(ContextMenuComp menu, List<MenuItem> menuList) {
		if (menu != null) {
			if (menu.isMenuCtxChanged())
				return true;
			List<MenuItem> list = menu.getItemList();
			if (list != null) {
				boolean result = checkContextMenuCtxChanged(null, list);
				if (result == true)
					return true;
			}
		} else if (menuList != null) {
			for (MenuItem menuItem : menuList) {
				if (menuItem.isCtxChanged())
					return true;
				List<MenuItem> subItemList = menuItem.getChildList();
				if (subItemList != null) {
					boolean result = checkContextMenuCtxChanged(null, subItemList);
					if (result == true)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isCtxChanged() {
		boolean result = checkContextMenuCtxChanged(this, null);
		if (result == true)
			return true;
		return super.isCtxChanged();
	}
	
	private boolean isMenuCtxChanged() {
		return super.isCtxChanged();
	}

	@Override
	public BaseContext getContext() {
		ContextMenuContext ctx = new ContextMenuContext();
		ctx.setId(this.getId());
		if (this.itemList.size() > 0) {
			MenuItemContext[] childItemContexts = new MenuItemContext[this.itemList.size()];
			for (int i = 0, n = this.itemList.size(); i < n; i++) {
				childItemContexts[i] = (MenuItemContext) this.itemList.get(i).getContext();
			}
			ctx.setChildItemContexts(childItemContexts);
		}
//		if (this.childMenuList.size() > 0) {
//			ContextMenuContext[] childMenuContexts = new ContextMenuContext[this.childMenuList.size()];
//			for (int i = 0, n = this.childMenuList.size(); i < n; i++) {
//				childMenuContexts[i] = (ContextMenuContext) this.childMenuList.get(i).getContext();
//			}
//			ctx.setChildMenuContexts(childMenuContexts);
//		}
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		ContextMenuContext miCtx = (ContextMenuContext) ctx;
		
		// 触发对象ID
		if (miCtx.getTriggerId() != null)
			this.setExtendAttribute(TRIGGER_ID, miCtx.getTriggerId());
		
		MenuItemContext[] childItemContexts = miCtx.getChildItemContexts();
		if(childItemContexts != null){
			for (int i = 0, n = childItemContexts.length; i < n; i++) {
				MenuItemContext itemCtx = childItemContexts[i];
				for (int j = 0, m = this.itemList.size(); j < m; j++) {
					if (itemList.get(j).getId().equals(itemCtx.getId())) {
						itemList.get(j).setContext(itemCtx);
						break;
					}
				}
			}
		}
//		ContextMenuContext[] childMenuContexts = miCtx.getChildMenuContexts();
//		for (int i = 0, n = childMenuContexts.length; i < n; i++) {
//			ContextMenuContext menuCtx = childMenuContexts[i];
//			for (int j = 0, m = this.childMenuList.size(); j < m; j++) {
//				if (childMenuList.get(j).getId().equals(menuCtx.getId())) {
//					childMenuList.get(j).setContext(menuCtx);
//					break;
//				}
//			}
//		}
		this.setCtxChanged(false);
	}
	
}

