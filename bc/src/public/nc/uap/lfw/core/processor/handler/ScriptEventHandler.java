package nc.uap.lfw.core.processor.handler;

import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.ScriptServerListener;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.processor.AbstractEventHandler;
import nc.uap.lfw.util.LfwClassUtil;

public class ScriptEventHandler extends AbstractEventHandler<WebElement> {

	private static final String LISTENER_CLAZZ = "listener_class";
	private static final String HANDLER_EVENT = "handlerEvent";

	public ScriptEventHandler(LfwPageContext pageCtx) {
		super(pageCtx);
	}

	@Override
	protected WebElement getSource() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<ScriptServerListener> getListenerClazz() {
		String clazz = getPageCtx().getParameter(LISTENER_CLAZZ);
		if(clazz == null)
			throw new LfwRuntimeException("后台请求必须指定处理类");
		return (Class<ScriptServerListener>) LfwClassUtil.forName(clazz);
	}

	@Override
	protected String getEventName() {
		return HANDLER_EVENT;
	}

	@Override
	protected AbstractServerEvent<WebElement> getServerEvent(String eventName,
			WebElement source) {
		return new ScriptEvent(source);
	}
}
