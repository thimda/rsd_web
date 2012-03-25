package nc.uap.lfw.core.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.AbstractServerEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.event.listener.AbstractServerListener;
import nc.uap.lfw.core.event.listener.IServerListener;
import nc.uap.lfw.core.exception.LfwInteractionException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.util.LfwClassUtil;

public abstract class AbstractEventHandler<T extends WebElement> implements IEventHandler{
	private LfwPageContext pageCtx;
	private JsListenerConf listenerConf;
	protected abstract T getSource();
	protected AbstractServerEvent<T> getServerEvent(String eventName, T source){
		throw new LfwRuntimeException("不支持Listener类型:" + getListenerConf().getClass().getSimpleName() + ",event:" + eventName + ",source:" + source.getClass().getSimpleName());
	}
	public AbstractEventHandler(LfwPageContext pageCtx) {
		this.pageCtx = pageCtx;
	}
	
	protected String getWidgetId() {
		String widgetId = getPageCtx().getParameter(LfwPageContext.WIDGET_ID);
		return widgetId;
	}
	
	protected JsListenerConf getListenerConf(){
		if(listenerConf == null){
			String listenerId = pageCtx.getParameter(LfwPageContext.LISTENER_ID);
			T source = getSource();
			listenerConf = source.getListenerMap().get(listenerId);
		}
		return listenerConf;
	}
	
	@SuppressWarnings("unchecked")
	protected Class<? extends AbstractServerListener> getListenerClazz(){
		String clazz = getListenerConf().getServerClazz();
		return (Class<AbstractServerListener>) LfwClassUtil.forName(clazz);
	}
	
	protected String getEventName() {
		String eventName = getPageCtx().getParameter(LfwPageContext.EVENT_NAME);
		return eventName;
	}
	
	public void execute(){
		try{
			Class<? extends AbstractServerListener> c = getListenerClazz();
			String eventName = getEventName();
			T source = getSource();
			
			IServerListener listener = (IServerListener) LfwClassUtil.newInstance(c, new Class[]{LfwPageContext.class, String.class}, new Object[]{pageCtx, getWidgetId()});
			AbstractServerEvent<T> serverEvent = getServerEvent(eventName, source);
			
			Method m = getExecuteMethod(c, serverEvent);
			m.invoke(listener, new Object[]{serverEvent});
		}
		catch (Throwable e) {
			Logger.error(e.getMessage(), e);
			if(e instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e;
			else if(e.getCause() instanceof LfwInteractionException)
				throw (LfwInteractionException)e.getCause();
			else if(e.getCause() instanceof LfwRuntimeException){
				throw (LfwRuntimeException)e.getCause();
			}
			else if(e instanceof InvocationTargetException){
				e = ((InvocationTargetException)e).getTargetException();
			}
			String msg = e.getMessage();
			if(msg != null)
				msg = "," + msg;
			else 
				msg = "";
			throw new LfwRuntimeException("处理事件发生异常" + msg, e);
		}
	}
	
	protected Method getExecuteMethod(Class<? extends AbstractServerListener> c, AbstractServerEvent<T> serverEvent) throws NoSuchMethodException {
		Method m = null;
		if(serverEvent instanceof MouseEvent)
			m = c.getMethod(getEventName(), new Class[]{MouseEvent.class});
		else
			m = c.getMethod(getEventName(), new Class[]{serverEvent.getClass()});
		return m;
	}
	
	public LfwPageContext getPageCtx() {
		return pageCtx;
	}
	
}
