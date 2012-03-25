package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.LinkEvent;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.LinkListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * @author guoweic
 *
 */
public class LinkEventHandler extends AbstractEventHandler<LinkComp> {

	public LinkEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	@Override
	protected LinkComp getSource() {
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		LinkComp link = (LinkComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewComponents().getComponent(sourceId);
		return link;
	}
	
	protected AbstractServerEvent<LinkComp> getServerEvent(String eventName, LinkComp link) {
		JsListenerConf listener = getListenerConf();
		if (listener instanceof LinkListener) {
			AbstractServerEvent<LinkComp> event = new LinkEvent(link);
			return event;
		} else
			return super.getServerEvent(eventName, link);
	}

}
