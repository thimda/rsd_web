package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.SelfDefEvent;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.SelfDefListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.AbstractEventHandler;

/**
 * @author guoweic
 *
 */
public class SelfDefCompEventHandler extends AbstractEventHandler<SelfDefComp> {

	public SelfDefCompEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	@Override
	protected SelfDefComp getSource() {
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		String pSourceId = getPageCtx().getParameter(LfwPageContext.PARENT_SOURCE_ID);
		SelfDefComp comp = (SelfDefComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewComponents().getComponent(pSourceId);
		return comp;
	}

	@Override
	protected AbstractServerEvent<SelfDefComp> getServerEvent(String eventName, SelfDefComp comp) {
		JsListenerConf listener = getListenerConf();
		if (listener instanceof SelfDefListener) {
			String pSourceId = getPageCtx().getParameter(LfwPageContext.PARENT_SOURCE_ID);
			SelfDefEvent event = new SelfDefEvent(comp);
			// ¥•∑¢∂‘œÛID
			event.setTriggerId((String) comp.getExtendAttributeValue(SelfDefComp.TRIGGER_ID));
			return event;
		}
		else
			throw new LfwRuntimeException("not implemented");
	}

}
