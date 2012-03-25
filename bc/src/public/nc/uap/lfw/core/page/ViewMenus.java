package nc.uap.lfw.core.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.exception.LfwRuntimeException;

public class ViewMenus implements Serializable, Cloneable{
	private static final long serialVersionUID = -7761705359086487592L;
	private Map<String, MenubarComp> menuBarMap = new HashMap<String, MenubarComp>();
	private Map<String, ContextMenuComp> contextMenuMap = new HashMap<String, ContextMenuComp>();
	private LfwWidget widget;
	private PageMeta pagemeta;
	public ViewMenus(LfwWidget lfwWidget) {
		this.widget = lfwWidget;
	}
	
	public ViewMenus(){
	}
	
	public ViewMenus(PageMeta pm) {
		this.pagemeta = pm;
	}
	
	public void addMenuBar(MenubarComp menu) {
		menu.setPageMeta(this.pagemeta);
		menuBarMap.put(menu.getId(), menu);
	}

	public MenubarComp getMenuBar(String id) {
		return menuBarMap.get(id);
	}
	
	public MenubarComp[] getMenuBars() {
		return menuBarMap.values().toArray(new MenubarComp[0]);
	}
	public void removeMenuBar(String id) {
		if (menuBarMap.containsKey(id))
			menuBarMap.remove(id);
	}
	public void addContextMenu(ContextMenuComp menu) {
		menu.setWidget(widget);
		contextMenuMap.put(menu.getId(), menu);
	}
	public ContextMenuComp[] getContextMenus() {
		return contextMenuMap.values().toArray(new ContextMenuComp[0]);
	}
	public ContextMenuComp getContextMenu(String id) {
		return contextMenuMap.get(id);
	}
	
	public void removeContextMenu(String id){
		contextMenuMap.remove(id);
	}
	
	public Object clone()
	{
		try {
			ViewMenus components = (ViewMenus)super.clone();
			components.menuBarMap = new HashMap<String, MenubarComp>();
			components.contextMenuMap = new HashMap<String, ContextMenuComp>();
			if(menuBarMap != null)
			{
				Iterator<String> menubarIt = menuBarMap.keySet().iterator();
				while(menubarIt.hasNext())
				{
					String menubarId = menubarIt.next();
					components.menuBarMap.put(menubarId, (MenubarComp) menuBarMap.get(menubarId).clone());
				}
			}
			
			if(contextMenuMap != null)
			{
				Iterator<String> contextMenuIt = contextMenuMap.keySet().iterator();
				while(contextMenuIt.hasNext())
				{
					String contextMenuId = contextMenuIt.next();
					components.contextMenuMap.put(contextMenuId, (ContextMenuComp) contextMenuMap.get(contextMenuId).clone());
				}
			}
			return components;
		} catch (CloneNotSupportedException e) {
			Logger.error(e, e);
			throw new LfwRuntimeException(e.getMessage(), e);
		}
	}

	public LfwWidget getWidget() {
		return widget;
	}

	public void setWidget(LfwWidget widget) {
		this.widget = widget;
		ContextMenuComp[] ctxMenus = getContextMenus();
		for (int i = 0; i < ctxMenus.length; i++) {
			ctxMenus[i].setWidget(widget);
		}
	}

	public PageMeta getPagemeta() {
		return pagemeta;
	}

	public void setPagemeta(PageMeta pagemeta) {
		this.pagemeta = pagemeta;
	}
}
