package nc.uap.lfw.core.processor.handler;

import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * @author guoweic
 * 
 */
public class MenubarItemEventHandler extends AbstractEventHandler<MenuItem> {

	public MenubarItemEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	@Override
	protected MenuItem getSource() {
		LfwPageContext pageCtx = getPageCtx();
		String sourceId = pageCtx.getParameter(LfwPageContext.SOURCE_ID);
		String psourceId = pageCtx.getParameter(LfwPageContext.PARENT_SOURCE_ID);
		MenubarComp menubar = null;
		if(getWidgetId() != null)
			menubar = (MenubarComp) pageCtx.getWidgetContext(getWidgetId()).getWidget().getViewMenus().getMenuBar(psourceId);
		else
			menubar = pageCtx.getPageMeta().getViewMenus().getMenuBar(psourceId);
		List<MenuItem> list = menubar.getMenuList();
		Iterator<MenuItem> it = list.iterator();
		MenuItem mItem = null;
		while (it.hasNext()) {
			MenuItem item = it.next();
			if (item.getId().equals(sourceId)) {
				mItem = item;
				break;
			}
			
			if(mItem == null){
				mItem = getMenuItem(item, sourceId);
			}
			
		}
		if(mItem == null)
			throw new LfwRuntimeException("没有找到对应的MenuItem," + sourceId);
		return mItem;
	}
	
	private MenuItem getMenuItem(MenuItem item, String sourceId) {
		List<MenuItem> items = item.getChildList();
		if (items != null && items.size() > 0) {
			Iterator<MenuItem> cIt = items.iterator();
			while (cIt.hasNext()) {
				MenuItem cItem = cIt.next();
				if (cItem.getId().equals(sourceId)) {
					return cItem;
				}
				
				cItem = getMenuItem(cItem, sourceId);
				if(cItem != null)
					return cItem;
			}
		}
		return null;
	}

	@Override
	protected AbstractServerEvent<MenuItem> getServerEvent(String eventName, MenuItem item) {
		JsListenerConf listener = getListenerConf();
		if (listener instanceof MouseListener) {
			MouseEvent<MenuItem> event = new MouseEvent<MenuItem>(item);
			return event;
		}
		else
			throw new LfwRuntimeException("not implemented");
	}

}
