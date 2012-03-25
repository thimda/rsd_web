package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.PageEvent;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.PageListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.processor.AbstractEventHandler;

public class PagemetaEventHandler extends AbstractEventHandler<PageMeta> {

	public PagemetaEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	@Override
	protected AbstractServerEvent<PageMeta> getServerEvent(String eventName, PageMeta pm) {
		JsListenerConf listener = getListenerConf();
		if(listener instanceof PageListener){
			PageEvent pageEvent = new PageEvent(pm);
			return pageEvent;
		}
		else
			return super.getServerEvent(eventName, pm);
	}

	@Override
	protected PageMeta getSource() {
		return getPageCtx().getPageMeta();
	}

}
