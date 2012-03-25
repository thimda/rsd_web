package nc.uap.lfw.core.processor.handler;

import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.CtxMenuMouseEvent;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * @author guoweic
 *
 */
public class ContextMenuItemEventHandler extends AbstractEventHandler<MenuItem> {

	public ContextMenuItemEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	@Override
	protected MenuItem getSource() {
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		String pSourceId = getPageCtx().getParameter(LfwPageContext.PARENT_SOURCE_ID);
		ContextMenuComp menu = (ContextMenuComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewMenus().getContextMenu(pSourceId);
		List<MenuItem> list = menu.getItemList();
		Iterator<MenuItem> it = list.iterator();
		MenuItem mItem = null;
		
		while(it.hasNext()){
			MenuItem item = it.next();
			if(item.getId().equals(sourceId)){
				mItem = item;
				break;
			}
			if (mItem == null) 
				mItem = getMenuItem(item, sourceId);
		}
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
			String pSourceId = getPageCtx().getParameter(LfwPageContext.PARENT_SOURCE_ID);
			ContextMenuComp menu = (ContextMenuComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewMenus().getContextMenu(pSourceId);
			CtxMenuMouseEvent event = new CtxMenuMouseEvent(item);
			// ¥•∑¢∂‘œÛID
			event.setTriggerId((String) menu.getExtendAttributeValue(ContextMenuComp.TRIGGER_ID));
			return event;
		}
		else
			throw new LfwRuntimeException("not implemented");
	}

}
