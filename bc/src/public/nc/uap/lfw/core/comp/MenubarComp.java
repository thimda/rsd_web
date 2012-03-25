package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.MenuItemContext;
import nc.uap.lfw.core.comp.ctx.MenubarContext;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.PageMeta;
/**
 * 
 *
 */
public class MenubarComp extends WebComponent {
	
	private static final long serialVersionUID = 2754368999187161913L;
	
//	//标识此Menubar是在哪个位置，因为menubar管理器会自动使处于同一个位置的menubar同时只显示一个，例如管理类型卡片。这个值没有特定常量，只需要有区别即可
//	private String posIdentity = "head";
	
	private List<MenuItem> menuList = null;
	private PageMeta pagemeta;

	public MenubarComp() {
		super();
	}

	public MenubarComp(String id) {
		super(id);
	}
	
	public List<MenuItem> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuItem> menuList) {
		this.menuList = menuList;
	}

	public void addMenuItem(MenuItem item){
		if(menuList == null)
			menuList = new ArrayList<MenuItem>();
		item.setWidget(getWidget());
		menuList.add(item);
	}
	public Object clone()
	{
		MenubarComp menubar = new MenubarComp(this.getId());
		menubar.setEnabled(this.isEnabled());
//		menubar.setHeight(this.getHeight());
//		menubar.setLeft(this.getLeft());
		menubar.setListenerMap(this.getListenerMap());
		if(this.getMenuList() != null){
			List<MenuItem> list = new ArrayList<MenuItem>();
			List<MenuItem> sourceList = this.getMenuList();
			
			MenuItem item = null;
			for(Iterator<MenuItem> it = sourceList.iterator(); it.hasNext(); )
			{
				item = it.next();
				if(item != null)
					list.add((MenuItem)(item.clone()));
			}
			menubar.setMenuList(list);
		}
		//menubar.setMenuList((List<MenuItem>) ((ArrayList<MenuItem>)this.getMenuList()).clone());
		menubar.setRendered(this.isRendered());
//		menubar.setTop(this.getTop());
		menubar.setVisible(this.isVisible());
//		menubar.setWidth(this.getWidth());
//		menubar.setClassName(this.getClassName());
		//menubar.setPosIdentity(this.getPosIdentity());
		menubar.setContextMenu(this.getContextMenu());
		//Events
		EventConf[] eventConfs = this.getEventConfs();
		if(eventConfs != null && eventConfs.length > 0){
			for(EventConf eventConf : eventConfs){
				menubar.addEventConf(eventConf);
			}
		}
		return menubar;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}

	@Override
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		return list;
	}

	public void setPageMeta(PageMeta pagemeta) {
		this.pagemeta = pagemeta;
	}
	
	public PageMeta getPageMeta() {
		return this.pagemeta;
	}

//	public String getPosIdentity() {
//		return posIdentity;
//	}
//
//	public void setPosIdentity(String posIdentity) {
//		this.posIdentity = posIdentity;
//	}
	
	/**
	 * 查看MenubarComp的Context是否发生改变
	 * @param menubar
	 * @return
	 */
	private boolean checkMenubarCtxChanged(MenubarComp menubar, List<MenuItem> menuList) {
		if (menubar != null) {
			if (menubar.isMenubarCtxChanged())
				return true;
			List<MenuItem> list = menubar.getMenuList();
			if (list != null) {
				boolean result = checkMenubarCtxChanged(null, list);
				if (result == true)
					return true;
			}
		} else if (menuList != null) {
			for (MenuItem menuItem : menuList) {
				if (menuItem.isCtxChanged())
					return true;
				List<MenuItem> subItemList = menuItem.getChildList();
				if (subItemList != null) {
					boolean result = checkMenubarCtxChanged(null, subItemList);
					if (result == true)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isCtxChanged() {
		boolean result = checkMenubarCtxChanged(this, null);
		if (result == true)
			return true;
		return super.isCtxChanged();
	}
	
	private boolean isMenubarCtxChanged() {
		return super.isCtxChanged();
	}
	
	@Override
	public BaseContext getContext() {
		MenubarContext ctx = new MenubarContext();
		ctx.setId(this.getId());
		if (this.menuList.size() > 0) {
			MenuItemContext[] childItemContexts = new MenuItemContext[this.menuList.size()];
			for (int i = 0, n = this.menuList.size(); i < n; i++) {
				childItemContexts[i] = (MenuItemContext) this.menuList.get(i).getContext();
			}
			ctx.setChildItemContexts(childItemContexts);
		}
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		MenubarContext miCtx = (MenubarContext) ctx;
		MenuItemContext[] childItemContexts = miCtx.getChildItemContexts();
		for (int i = 0, n = childItemContexts.length; i < n; i++) {
			MenuItemContext itemCtx = childItemContexts[i];
			for (int j = 0, m = this.menuList.size(); j < m; j++) {
				if (menuList.get(j).getId().equals(itemCtx.getId())) {
					menuList.get(j).setContext(itemCtx);
					break;
				}
			}
		}
		this.setCtxChanged(false);
	}
	
	
	public MenuItem getItem(String id)
	{
		for (int i = 0; i < menuList.size(); i++) {
			if(menuList.get(i).getId().equals(id))
				return menuList.get(i);
		}
		return null;
	}
	
	public void removeItem(MenuItem menuItem)
	{
		menuList.remove(menuItem);
	}
}

