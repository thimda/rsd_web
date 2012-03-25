package nc.uap.lfw.core.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.app.itf.IOperatorLogService;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwInteractionException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.processor.AbstractRequestProcessor;
import nc.uap.lfw.core.serializer.AppContext2XmlSerializer;
import nc.uap.lfw.core.serializer.IObject2XmlSerializer;
import nc.uap.lfw.core.serializer.IXml2ObjectSerializer;
import nc.uap.lfw.core.serializer.Xml2AppContextSerializer;

/**
 * App型应用处理器
 * @author dengjt
 *
 */
public class AppRequestProcessor extends AbstractRequestProcessor<AppLifeCycleContext> {

	protected IXml2ObjectSerializer<AppLifeCycleContext> getRequestSerializer() {
		return new Xml2AppContextSerializer();
	}

	@Override
	protected IObject2XmlSerializer<AppLifeCycleContext> getResponseSerializer() {
		return new AppContext2XmlSerializer();
	}
	public AppLifeCycleContext processEvent() {
		try{
			AppLifeCycleContext ctx = getModelObject();
			AppLifeCycleContext.current(ctx);
			doProcessEvent(ctx);
			return ctx;
		}
		finally{
			AppLifeCycleContext.reset();
		}
	}
	public void doProcessEvent(AppLifeCycleContext ctx) {
		try{
			//处理合并事件情况
			List<Map<String, String>> gpList = ctx.getGroupParamMapList();
			if(gpList != null){
				for (int i = 0; i < gpList.size(); i++) {
					Map<String, String> paramMap = gpList.get(i);
					ctx.setParamMap(paramMap);
					processOneEvent(ctx);
				}
			}
			//处理单个事件情况
			else
				processOneEvent(ctx);
		}
		catch(Exception e){
			if(e instanceof InvocationTargetException){
				Throwable t = ((InvocationTargetException)e).getCause();
				if(t instanceof LfwInteractionException)
					throw (LfwInteractionException)t;
				else if(t instanceof LfwRuntimeException)
					throw (LfwRuntimeException)t;
			}
			LfwLogger.error(e);
			
			if(e instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e;
			throw new LfwRuntimeException(e);
		}
	}

	private void processOneEvent(AppLifeCycleContext ctx)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, Exception {
		
		String currentViewId = ctx.getParamMap().get(LfwPageContext.WIDGET_ID);
		ViewContext currentViewCtx = ctx.getWindowContext().getViewContext(currentViewId);
		ctx.getWindowContext().setCurrentViewContext(currentViewCtx);
		
		String plugout = ctx.getParameter(AppLifeCycleContext.PLUGOUT_SIGN);
		String ctrlClazz = ctx.getParameter("clc");
		//先执行plugout后执行其它event
		if(plugout != null && plugout.equals("1")){
			new PlugEventHandler().doPlug();
			if (ctrlClazz != null){
				try{
					eventInvoke();
				}catch(NoSuchMethodException e){
					LfwLogger.error(e.getMessage());
				}
			}
			
		}
		//先执行其它event后执行plugout
		else if (plugout != null && !plugout.equals("1")){
			if (ctrlClazz != null)
				eventInvoke();
			new PlugEventHandler().doPlug();
		}
		else{
			eventInvoke();
		}
	}
	
	private void eventInvoke() throws Exception{
		AppEventFactory eventFactory = new AppEventFactory();
		Object eventHandler = eventFactory.getController();
		if(eventHandler != null){
			AbstractServerEvent<?> serverEvent = eventFactory.getServerEvent();
			Method m = getMethod(eventHandler, serverEvent);
			if(m != null){
				//IOperatorLogService logservice = (IOperatorLogService) Class.forName("nc.uap.cpb.org.itf.OperatorLogServiceImpl").newInstance();
				//logservice.log(serverEvent);
				m.invoke(eventHandler, serverEvent);
			}
		} 
		else{
			LfwLogger.error("没有找到对应的Event Handler");
		}
	} 

	/**
	 * 获取执行的方法，以方法名为准则进行匹配。找不到方法抛出异常
	 * @param serverEvent 
	 * @param eventHandler 
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	protected Method getMethod(Object eventHandler, AbstractServerEvent<?> serverEvent) throws SecurityException, NoSuchMethodException {
		String methodName = AppLifeCycleContext.current().getParameter(AppLifeCycleContext.METHOD_NAME);
		if(methodName != null){
			return eventHandler.getClass().getMethod(methodName, new Class[]{serverEvent.getClass()});
		}else{
			return null;
		}
	}
}
