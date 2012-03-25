package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * @author guoweic
 *
 */
public class ToolbarButtonEventHandler extends AbstractEventHandler<ToolBarItem> {

	public ToolbarButtonEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	@Override
	protected ToolBarItem getSource() {
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		String pSourceId = getPageCtx().getParameter(LfwPageContext.PARENT_SOURCE_ID);
		ToolBarComp toolbar = (ToolBarComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewComponents().getComponent(pSourceId);
		ToolBarItem[] elements = toolbar.getElements();
		ToolBarItem mItem = null;
		
		for (ToolBarItem item : elements) {
			if(item.getType().equals(ToolBarItem.BUTTON_TYPE) && item.getId().equals(sourceId)){
				mItem = item;
				break;
			}
		}
		return mItem;
	}
	
	@Override
	protected AbstractServerEvent<ToolBarItem> getServerEvent(String eventName, ToolBarItem item) {
		JsListenerConf listener = getListenerConf();
		if (listener instanceof MouseListener) {
			AbstractServerEvent<ToolBarItem> event = new MouseEvent<ToolBarItem>(item);
			return event;
		}
		else
			return super.getServerEvent(eventName, item);
	}

}
