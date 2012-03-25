package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.AbstractEventHandler;

public class ButtonEventHandler extends AbstractEventHandler<ButtonComp> {

	public ButtonEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	@Override
	protected AbstractServerEvent<ButtonComp> getServerEvent(String eventName, ButtonComp bt) {
		JsListenerConf listener = getListenerConf();
		if (listener instanceof MouseListener) {
			MouseEvent<ButtonComp> event = new MouseEvent<ButtonComp>(bt);
			return event;
		}
		else
			throw new LfwRuntimeException("not implemented");
	}

	@Override
	protected ButtonComp getSource() {
		String sourceId = getPageCtx().getParameter(LfwPageContext.SOURCE_ID);
		ButtonComp button = (ButtonComp) getPageCtx().getPageMeta().getWidget(getWidgetId()).getViewComponents().getComponent(sourceId);
		return button;
	}

}
