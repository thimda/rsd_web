package nc.uap.lfw.core.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.plug.IPlugoutType;
import nc.uap.lfw.core.model.plug.PlugoutTypeFactory;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDescItem;
import nc.uap.lfw.core.refnode.RefOkController;
import nc.uap.lfw.util.LfwClassUtil;

import org.json.JSONObject;

public class PlugEventHandler {
	public void doPlug() throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String plugoutId = ctx.getParameter(AppLifeCycleContext.PLUGOUT_ID);
		String plugoutSource = ctx.getParameter(AppLifeCycleContext.PLUGOUT_SOURCE);
//		String plugoutSourceWindow = ctx.getParameter(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW);
		String params = ctx.getParameter(AppLifeCycleContext.PLUGOUT_PARAMS);
		JSONObject paramsJson = null;
		if (params != null){
			try {
				paramsJson = new JSONObject(params);
			} catch (ParseException e) {
				LfwLogger.error(e);
			}
		}
//		String fromServer = ctx.getParameter(AppLifeCycleContext.PLUGOUT_FROMSERVER);
//		Map<String, Object> resultMap = null;
//		//后台请求
//		if (fromServer != null && fromServer.equals("1")){
//			resultMap = ctx.getWindowContext().getPlug(plugoutSource + "_" + plugoutId);
//		}
		
		WindowContext winCtx = null;
//		if (plugoutSourceWindow == null || plugoutSourceWindow.equals(""))
			winCtx = ctx.getWindowContext();
//		else
//			winCtx = ctx.getApplicationContext().getWindowContext(plugoutSourceWindow);
		ViewContext viewCtx = winCtx.getViewContext(plugoutSource);
		LfwWidget widget = winCtx.getWindow().getWidget(plugoutSource);
		PlugoutDesc plugout = widget.getPlugoutDesc(plugoutId);
		
		
		Map<String, Object> paramMap = ctx.getApplicationContext().getPlug(winCtx.getId() + "_" + plugoutSource + "_" + plugoutId);
		//Map<String, Object> paramMap = winCtx.getPlug(plugoutSource + "_" + plugoutId);
		//window内plug调用 
		Connector[] connectors = winCtx.getWindow().getConnectors();
		if(connectors != null){
			Map<String, Object> resultMap = buildPlugContent(plugout, viewCtx, paramsJson, paramMap);
//			ctx.getWindowContext().addPlug(plugoutSource + "_" + plugoutId, resultMap);
			for (int i = 0; i < connectors.length; i++) {
				Connector conn = connectors[i];
				if (conn.getSource().equals(plugoutSource) && conn.getPlugoutId().equals(plugoutId)){
					eventInvoke(ctx, conn, resultMap);
				}
			}
		}
		
		//app中window间plug调用 
		List<Connector> connectorList = ctx.getApplicationContext().getApplication().getConnectorList();
		if (connectorList != null){
				Map<String, Object> resultMap = buildPlugContent(plugout, viewCtx, paramsJson, paramMap);
//				if (fromServer != null && fromServer.equals("1"))
//					ctx.getWindowContext().addPlug(plugoutSource + "_" + plugoutId, resultMap);
			String sourceWindowId = winCtx.getWindow().getId();
			for (Connector conn : connectorList){
				if (conn.getSourceWindow().equals(sourceWindowId) && conn.getSource().equals(plugoutSource) && conn.getPlugoutId().equals(plugoutId)){
					eventInvoke(ctx, conn, resultMap);
				}
			}
		}
	}

	private void eventInvoke(AppLifeCycleContext ctx, Connector conn, Map<String, Object> resultMap) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		LfwWidget targetWidget = null;
		WindowContext oriWinCtx = ctx.getApplicationContext().getCurrentWindowContext();
		ViewContext oriViewCtx = ctx.getViewContext();
		if (conn.getTargetWindow() == null || conn.getTargetWindow().equals(""))
			targetWidget = ctx.getWindowContext().getWindow().getWidget(conn.getTarget());
		else{
			WindowContext targetWinCtex = ctx.getApplicationContext().getWindowContext(conn.getTargetWindow());
//			PageMeta inPagemeta = LfwRuntimeEnvironment.getWebContext().getCrossPageMeta(conn.getTargetWindow());
			if (targetWinCtex == null)
				return;
			targetWidget = targetWinCtex.getWindow().getWidget(conn.getTarget());
			ctx.getApplicationContext().setCurrentWindowContext(targetWinCtex);
		}

		if (targetWidget == null)
			return;
		
		PluginDesc plugin = targetWidget.getPluginDesc(conn.getPluginId());
		String controllerClazz = targetWidget.getControllerClazz();
		String methodName = "plugin" + plugin.getId();
		Object controller = null;
		Method m = null;
		if (plugin.getId().equals(RefOkController.PLUGIN_ID)){
//			m = getMethod(controllerClazz, methodName, new Class[]{Map.class});
			if(controllerClazz != null){
				controller = LfwClassUtil.newInstance(controllerClazz);
				try{
					m = controller.getClass().getMethod(methodName, new Class[]{Map.class});
				} 
				catch (NoSuchMethodException e){
				}
			}
			
			if(m == null){
				controller = LfwClassUtil.newInstance(RefOkController.class);
				try{
					m = controller.getClass().getMethod(methodName, new Class[]{Map.class});
				} 
				catch (NoSuchMethodException e){
					LfwLogger.error(e);
					throw new LfwRuntimeException("没有找到方法:" + methodName);
				}
			}
		}
		else{
			controller = LfwClassUtil.newInstance(controllerClazz);
			m = controller.getClass().getMethod(methodName, new Class[]{Map.class});
		}
		
		ViewContext pluginViewCtx = ctx.getWindowContext().getViewContext(conn.getTarget());
		ctx.getWindowContext().setCurrentViewContext(pluginViewCtx);
		Map<String, Object> mappingObj = mapping(conn, resultMap);
//		Object controller = LfwClassUtil.newInstance(controllerClazz);
//		Method m = controller.getClass().getMethod(methodName, new Class[]{Map.class});
		m.invoke(controller, mappingObj);
		
		ctx.getApplicationContext().setCurrentWindowContext(oriWinCtx);
		ctx.getWindowContext().setCurrentViewContext(oriViewCtx);
	}
	
	private Method getMethod(String clazz, String methodName, Class[] paramClasses) {
		if(clazz == null)
			return null;
		Object controller = LfwClassUtil.newInstance(clazz);
		try{
			Method m = controller.getClass().getMethod(methodName, new Class[]{Map.class});
			return m;
		} 
		catch (NoSuchMethodException e){
			return null;
		}
	}

	private Map<String, Object> mapping(Connector conn, Map<String, Object> resultMap) {
		Map<String, Object> mappingObj = new HashMap<String, Object>();
		if (conn.getMapping() != null){
			Iterator<Entry<String, String>> entryIt = conn.getMapping().entrySet().iterator();
			while(entryIt.hasNext()){
				Entry<String, String> entry = entryIt.next();
				mappingObj.put(entry.getValue(), resultMap.get(entry.getKey()));
				resultMap.remove(entry.getKey());
			}
		}
		
		Iterator<String> resultIt = resultMap.keySet().iterator();
		while(resultIt.hasNext()){
			String key = resultIt.next();
			mappingObj.put(key, resultMap.get(key));
		}
		
		return mappingObj;
	}

	private Map<String, Object> buildPlugContent(PlugoutDesc plugout, ViewContext viewCtx, JSONObject paramsJson, Map<String, Object> paramMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<PlugoutDescItem> itemList = plugout.getDescItemList();
		//如果有传递参数，从传递中的参数中取值
		if (paramsJson != null && paramsJson.length() > 0){
			Iterator<String> paramIt = paramsJson.keys();
			while (paramIt.hasNext()){
				String key = paramIt.next();
				result.put(key, paramsJson.get(key));
			}
		}
		
		if(paramMap != null)
			result.putAll(paramMap);
		
		Iterator<PlugoutDescItem> itemIt = itemList.iterator();
		while(itemIt.hasNext()){
			PlugoutDescItem item = itemIt.next();
			String key = item.getName();
			//如果有传递参数，从传递中的参数中取值
//			if (paramsJson != null && key != null && paramsJson.length() > 0 && paramsJson.opt(key) != null ){
//				result.put(key, paramsJson.get(key));
//			}
			if (!result.containsKey(key)){
				String type = item.getType();
				IPlugoutType plugoutType = PlugoutTypeFactory.getPlugoutType(type);
				if (plugoutType != null){
					Object obj = plugoutType.fetchContent(item, viewCtx);
					result.put(item.getName(), obj);
				}
			}
		}
		return result;
	}
}
