package nc.uap.lfw.core.page;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.WidgetElement;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.data.Parameter;
import nc.uap.lfw.core.event.conf.AutoformListener;
import nc.uap.lfw.core.event.conf.ContextMenuListener;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.DialogListener;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.ExcelCellListener;
import nc.uap.lfw.core.event.conf.ExcelListener;
import nc.uap.lfw.core.event.conf.ExcelRowListener;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.FormRule;
import nc.uap.lfw.core.event.conf.GridCellListener;
import nc.uap.lfw.core.event.conf.GridListener;
import nc.uap.lfw.core.event.conf.GridRowListener;
import nc.uap.lfw.core.event.conf.GridRule;
import nc.uap.lfw.core.event.conf.LinkListener;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.SelfDefListener;
import nc.uap.lfw.core.event.conf.TextListener;
import nc.uap.lfw.core.event.conf.TreeContextMenuListener;
import nc.uap.lfw.core.event.conf.TreeNodeListener;
import nc.uap.lfw.core.event.conf.TreeRowListener;
import nc.uap.lfw.core.event.conf.TreeRule;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.model.plug.IPlugoutType;
import nc.uap.lfw.core.model.plug.PlugoutTypeFactory;
import nc.uap.lfw.core.model.util.LfwSubmitRuleMergeUtil;
import nc.uap.lfw.core.refnode.RefOkController;

public final class PlugEventAdjuster {
	
	public static final String CONTROL_TYPE_WIDGET = "Widget";  
	public static final String CONTROL_TYPE_MENU = "Menu";  
	public static final String CONTROL_TYPE_POPUPMENU = "PopupMenu";  
//	public static final String CONTROL_TYPE_CONINTER = "Coninter";  
	public static final String CONTROL_TYPE_DATASET = "Dataset";
	
	public static final String CONTROL_TYPE_BUTTON = "Button";  
	public static final String CONTROL_TYPE_FORM = "Form";  
	public static final String CONTROL_TYPE_GRID = "Grid";  
	public static final String CONTROL_TYPE_TREE = "Tree";  
	public static final String CONTROL_TYPE_EXCEL = "Excel";  
	public static final String CONTROL_TYPE_TEXTCOMP = "TextComp";  
	public static final String CONTROL_TYPE_IFRAMECOMP = "IFrameComp";  
	public static final String CONTROL_TYPE_LABEL = "Label";  
	public static final String CONTROL_TYPE_IMAGE = "Image";  
	public static final String CONTROL_TYPE_LINKCOMP = "LinkComp";  
	public static final String CONTROL_TYPE_CUSTOMCONIZOL = "CustomConizol";  
	

	/**
	 * 针对window之间的plug增加plugEvent
	 * 
	 * @param app
	 */
	public void addAppPlugEventConf(ApplicationContext appCtx, PageMeta pageMeta){
		if (LfwRuntimeEnvironment.getWebContext().getAppSession() == null)
			return;
		if (appCtx.getApplication() == null)
			return;
		//对每个pageMeta都增加一个参照Plugin
		addRefPlugin(pageMeta);
		//如果页面包含公共的查询模板则给此查询模板添加一个一个plugin
		addpubSimpleQueryPlugin(pageMeta);
		//打开的为参照pageMeta时，增加参照plugout，注：参照的view的id定为"main"
		String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
		String isReference = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("isReference");
		if (isReference != null && isReference.equals("true")){
			String sourceViewId = "main";
			if (pageMeta.getWidget("main") == null && pageMeta.getWidgetIds().length > 0)
				sourceViewId = pageMeta.getWidgetIds()[0];
//			LfwWidget refWidget = pageMeta.getWidget("main");
			LfwWidget refWidget = pageMeta.getWidget(sourceViewId);
			if (refWidget != null){
				addRefPlugout(refWidget);
				//创建连接
				Connector conn = new Connector(); 
				conn.setSourceWindow(pageId);
				conn.setSource(refWidget.getId());
				conn.setPlugoutId(RefOkController.PLUGOUT_ID);
				String parentPageId = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("otherPageId");
				conn.setTargetWindow(parentPageId);
				String widgetId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("widgetId");
				conn.setTarget(widgetId);
				conn.setPluginId(RefOkController.PLUGIN_ID);
				String connId = conn.getSourceWindow() + "$" + conn.getSource() + "$" + conn.getTargetWindow() + "$" + conn.getTarget();
				boolean existConn = false;
				for (Connector c : appCtx.getApplication().getConnectorList()){
					if (c.getId() != null && c.getId().equals(connId)){
						existConn = true;
						break;
					}
				}
				if (!existConn){
					conn.setId(connId);
					appCtx.getApplication().addConnector(conn);
				}
			}
		}
		
		//是否是弹出查询模板
		String isqeury = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("isAdvanceQuery");
		if (isqeury != null && isqeury.equals("true")){
			generateQueryConnection(pageMeta, pageId);
		}

		
		//生成plugout对象上的提交规则
		for (Connector conn : appCtx.getApplication().getConnectorList()){
			String sourceWindowId = conn.getSourceWindow();
			String targetWindowId = conn.getTargetWindow();
			//处理plugout
			if (sourceWindowId.equals(pageId)){
				PageMeta inPagemeta = LfwRuntimeEnvironment.getWebContext().getCrossPageMeta(targetWindowId);
				genPlugoutSubmitRule(pageMeta, inPagemeta, conn);
			}
			//处理plugin   不做处理，对于  plugout在先打开的window中,plugin在后打开的window中的情况，不支持
//			if (targetWindowId.equals(pageMeta.getId())){
//				PageMeta outPagemeta = LfwRuntimeEnvironment.getWebContext().getCrossPageMeta(sourceWindowId);
//				if (outPagemeta == null)
//					return;
//				genPlugoutSubmitRule(pageMeta, pageMeta, conn);
//			}
		}
		
		//绑定触发器事件
		for (Connector conn : appCtx.getApplication().getConnectorList()){
			String sourceWindowId = conn.getSourceWindow();
			String targetWindowId = conn.getTargetWindow();
			
			//处理plugout
//			String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
			if (sourceWindowId.equals(pageId)){
				PageMeta inPagemeta = LfwRuntimeEnvironment.getWebContext().getCrossPageMeta(targetWindowId);
				addEvent(pageMeta, inPagemeta, conn);
			}
			//处理plugin  不做处理，对于  plugout在先打开的window中,plugin在后打开的window中的情况，不支持
//			if (targetWindowId.equals(pageMeta.getId())){
//				PageMeta outPagemeta = LfwRuntimeEnvironment.getWebContext().getCrossPageMeta(sourceWindowId);
//				if (outPagemeta == null)
//					return;
//				genPlugoutSubmitRule(pageMeta, pageMeta, conn);
//				//				addEventSubmitRule(pageMeta, outPagemeta, conn);
//			}
		}
	}
	
	
	/**
	 * 产生包含查询模板的window和查询模板间的connection
	 * @param appCtx
	 * @param pageMeta
	 * @param pageId
	 */
	private void generateQueryConnection(PageMeta pageMeta, String pageId) {
		ApplicationContext appCtx = AppLifeCycleContext.current().getApplicationContext();
		String sourceViewId = "main";
		if (pageMeta.getWidget("main") == null)
			return;
//			LfwWidget refWidget = pageMeta.getWidget("main");
		LfwWidget refWidget = pageMeta.getWidget(sourceViewId);
		if (refWidget != null){
			addAdvanceQueryPlugout(refWidget);
			//创建连接
			Connector conn = new Connector(); 
			conn.setSourceWindow(pageId);
			conn.setSource(refWidget.getId());
			conn.setPlugoutId("advanceQueryPlugOut");
			String parentPageId = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("otherPageId");
			conn.setTargetWindow(parentPageId);
			String widgetId = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("widgetId");
			conn.setTarget(widgetId);
			conn.setPluginId("advancePlugin");
			String connId = conn.getSourceWindow() + "$" + conn.getSource() + "$" + conn.getTargetWindow() + "$" + conn.getTarget();
			boolean existConn = false;
			for (Connector c : appCtx.getApplication().getConnectorList()){
				if (c.getId() != null && c.getId().equals(connId)){
					existConn = true;
					break;
				}
			}
			if (!existConn){
				conn.setId(connId);
				appCtx.getApplication().addConnector(conn);
			}
		}
	}
	
	
	/**
	 * 给包含公共简单查询模板的widget添加plugin, plugin的id
	 * @param pageMeta
	 */
	private void addpubSimpleQueryPlugin(PageMeta pageMeta) {
		boolean iscontains = false;
		LfwWidget[] widgets = pageMeta.getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			LfwWidget widget = widgets[i];
			if("simplequery".equals(widget.getId())){
				iscontains = true;
				break;
			}
		}
		if(iscontains == false)
			return;
		PluginDesc pluginDesc = new PluginDesc();
		pluginDesc.setId("advancePlugin");
		List<PluginDescItem> descItemList = new ArrayList<PluginDescItem>();
		
		PluginDescItem typeItem = new PluginDescItem();
		typeItem.setId("queryParam");
			
		descItemList.add(typeItem);
		pluginDesc.setDescItemList(descItemList);
		for (LfwWidget widget :  pageMeta.getWidgets()){
			if (widget.getPluginDesc("advancePlugin") == null)
				widget.addPluginDescs(pluginDesc);
		}
		
	}

	/**
	 * 对前台事件追加plugout提交规则 
	 * 
	 * @param inPageMeta
	 * @param outPagemeta
	 * @param conn
	 */
	@Deprecated
	private void addEventSubmitRule(PageMeta inPageMeta, PageMeta outPageMeta, Connector conn){
		//		String sourceWindowId = conn.getSourceWindow();
		String soruceWidgetId = conn.getSource();
		String plugoutId = conn.getPlugoutId();
		String targetWidgetId = conn.getTarget();
		String pluginId = conn.getPluginId();
		LfwWidget outView = outPageMeta.getWidget(soruceWidgetId);
		if (outView == null)
			return;
		PlugoutDesc plugoutDesc = outView.getPlugoutDesc(plugoutId);
		if (plugoutDesc == null)
			return;
		List<PlugoutEmitItem> descEmitList = plugoutDesc.getEmitList();
		if (descEmitList == null)
			return;
		LfwWidget targetWidget = inPageMeta.getWidget(targetWidgetId);
		
		if (targetWidget != null){
			
			//设置提交规则
			EventSubmitRule sr = new EventSubmitRule();
			
			WidgetRule sourceWr = new WidgetRule();				
			sourceWr.setId(soruceWidgetId);
			
			List<PlugoutDescItem> descDescList = plugoutDesc.getDescItemList();
			for (PlugoutDescItem item : descDescList){
				String type = item.getType();
				String source = item.getSource();
				IPlugoutType plugoutType = PlugoutTypeFactory.getPlugoutType(type);
				if (plugoutType != null)
					plugoutType.buildSourceWidgetRule(sourceWr, source);
			}
			sr.addWidgetRule(sourceWr);
			
			EventSubmitRule targetsubmitRule = getTargetWidgetSubmitRule(targetWidget, pluginId);
			if (targetsubmitRule != null){
				sr.addWidgetRule(targetsubmitRule.getWidgetRule(targetWidgetId));
			}
			sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SIGN,"1"));
//			if (sourceWindowId != null)
//				sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW,sourceWindowId));
			sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE,soruceWidgetId));
			sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_ID,plugoutId));
		}
		
		for (PlugoutEmitItem item : descEmitList){
			//TODO  增加异常处理
			String type = item.getType();
			String source = item.getSource();
			
			String controlType = type.split("\\.")[0]; 
			String eventType = type.split("\\.")[1];
			
			String sourceItem = null;
			if (source.indexOf(".") != -1){
				sourceItem = source.split("\\.")[1];	
				source = source.split("\\.")[0];
			}
			
			EventConf event = new EventConf();
			event.setJsEventClaszz(getJsEventClaszz(controlType, eventType));
//			event.setOnserver(false);
			event.setOnserver(true);
			event.setName(eventType);
//			StringBuffer scriptBuf = new StringBuffer();
//			scriptBuf.append(generateSubmitRule(sr));
//			scriptBuf.append("triggerPlugout(submitRule);\n");
//			event.setScript(scriptBuf.toString());
			
			
			if (sourceItem == null)
				event.setMethodName(controlType + "_" + source + "_plugevent");
			else 
				event.setMethodName(controlType + "_" + source + "_" + sourceItem + "_plugevent");
			List<EventConf> eventList = null;
			if (controlType.equals(CONTROL_TYPE_WIDGET)){
				eventList = getEventList(outPageMeta.getWidget(soruceWidgetId));
			}
			else if (controlType.equals(CONTROL_TYPE_MENU) || controlType.equals(CONTROL_TYPE_POPUPMENU)){
				eventList = getEventList(outPageMeta.getWidget(soruceWidgetId).getViewMenus().getMenuBar(source).getItem(sourceItem));
			}
//			else if (controlType.equals(CONTROL_TYPE_CONINTER)){
//				eventList = getEventList(outPageMeta.getWidget(soruceWidgetId).getViewConinters().getContainer(source));
//			}
			else if (isComponentType(controlType)){
				eventList = getEventList(outPageMeta.getWidget(soruceWidgetId).getViewComponents().getComponent(source));
			}
			else if (controlType.equals(CONTROL_TYPE_DATASET)){
				eventList = getEventList(outPageMeta.getWidget(soruceWidgetId).getViewModels().getDataset(source));
			}
			mergeEvent(eventList, event);
		}
	}
	
	/**
	 * 针对window内plug增加plugEvent
	 * @param pageMeta
	 */
	public void addPlugEventConf(PageMeta pageMeta){
		//生成plugout对象上的提交规则
		Iterator<Connector> it = pageMeta.getConnectorMap().values().iterator();
		while(it.hasNext()){
			Connector conn = it.next();
			genPlugoutSubmitRule(pageMeta, pageMeta, conn);
		}
		//绑定触发器事件
		Iterator<Connector> it2 = pageMeta.getConnectorMap().values().iterator();
		while(it2.hasNext()){
			Connector conn = it2.next();
			addEvent(pageMeta, pageMeta, conn);
		}	
	}

	/**
	 * 根据Connector对plugout对象生成或合并提交规则
	 * @param outPageMeta
	 * @param inPageMeta
	 * @param conn
	 */
	private void genPlugoutSubmitRule(PageMeta outPageMeta,PageMeta inPageMeta, Connector conn) {
//		String sourceWindowId = conn.getSourceWindow();
		String sourceWidgetId = conn.getSource();
		String plugoutId = conn.getPlugoutId();
		String targetWidgetId = conn.getTarget();
		String pluginId = conn.getPluginId();
		PlugoutDesc plugoutDesc = outPageMeta.getWidget(sourceWidgetId).getPlugoutDesc(plugoutId);
		if (plugoutDesc == null) return;
		EventSubmitRule sr = null;
		//设置提交规则
		sr = new EventSubmitRule();
		
		WidgetRule sourceWr = new WidgetRule();				
		sourceWr.setId(sourceWidgetId);
		
		List<PlugoutDescItem> descDescList = plugoutDesc.getDescItemList();
		if(descDescList != null && descDescList.size() > 0){
			for (PlugoutDescItem item : descDescList){
			String type = item.getType();
			//			type = type.split("\\.")[1];
			String source = item.getSource();
			IPlugoutType plugoutType = PlugoutTypeFactory.getPlugoutType(type);
			if (plugoutType != null)
				plugoutType.buildSourceWidgetRule(sourceWr, source);
			}
		}
		sr.addWidgetRule(sourceWr);
//		if (sourceWindowId != null)
//			sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW,sourceWindowId));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SIGN,"1"));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE,sourceWidgetId));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_ID,plugoutId));
		
		//增加plugin的提交规则
		if(inPageMeta != null){
			//不同window
			if (!inPageMeta.getId().equals(outPageMeta.getId())){
				LfwWidget targetWidget = inPageMeta.getWidget(targetWidgetId);
				if (targetWidget != null){
					EventSubmitRule targetsubmitRule = getTargetWidgetSubmitRule(targetWidget, pluginId);
					if (targetsubmitRule == null){
						targetsubmitRule = new EventSubmitRule();
						WidgetRule pwr = new WidgetRule();
						pwr.setId(targetWidgetId);
						targetsubmitRule.addWidgetRule(pwr);
					}
					sr.setParentSubmitRule(targetsubmitRule);
				}
			}
			//同window内
			else{
				LfwWidget targetWidget = inPageMeta.getWidget(targetWidgetId);
				if (targetWidget != null){
					EventSubmitRule targetsubmitRule = getTargetWidgetSubmitRule(targetWidget, pluginId);
					if (targetsubmitRule != null){
						sr.addWidgetRule(targetsubmitRule.getWidgetRule(targetWidgetId));
					}
				}
			}
		}

		if (plugoutDesc.getSubmitRule() == null)  
			plugoutDesc.setSubmitRule(sr);
		else 
			LfwSubmitRuleMergeUtil.mergeSubmitRule(plugoutDesc.getSubmitRule(), sr);
	}

	/**
	 * 对于有触发器的plugout，往触发器上绑定plug事件
	 * 
	 * @param outPageMeta
	 * @param inPageMeta
	 * @param conn
	 */
	private void addEvent(PageMeta outPageMeta,PageMeta inPageMeta, Connector conn){
//		String sourceWindowId = conn.getSourceWindow();
		String sourceWidgetId = conn.getSource();
		String plugoutId = conn.getPlugoutId();
//		String targetWidgetId = conn.getTarget();
//		String pluginId = conn.getPluginId();
		PlugoutDesc plugoutDesc = outPageMeta.getWidget(sourceWidgetId).getPlugoutDesc(plugoutId);
		if (plugoutDesc == null)
			return;
		List<PlugoutEmitItem> descEmitList = plugoutDesc.getEmitList();
		if (descEmitList == null)
			return;

		EventSubmitRule sr = plugoutDesc.getSubmitRule();
		
		//设置提交规则
//		sr = new EventSubmitRule();
//		
//		WidgetRule sourceWr = new WidgetRule();				
//		sourceWr.setId(sourceWidgetId);
//		
//		List<PlugoutDescItem> descDescList = plugoutDesc.getDescItemList();
//		if(descDescList != null && descDescList.size() > 0){
//			for (PlugoutDescItem item : descDescList){
//			String type = item.getType();
//			//			type = type.split("\\.")[1];
//			String source = item.getSource();
//			IPlugoutType plugoutType = PlugoutTypeFactory.getPlugoutType(type);
//			if (plugoutType != null)
//				plugoutType.buildSourceWidgetRule(sourceWr, source);
//			}
//		}
//		sr.addWidgetRule(sourceWr);
////		if (sourceWindowId != null)
////			sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW,sourceWindowId));
//		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SIGN,"1"));
//		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE,sourceWidgetId));
//		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_ID,plugoutId));
//		
//		//增加plugin的提交规则
//		if(inPageMeta != null){
//			//TODO 
//			//不同window
//			if (!inPageMeta.getId().equals(outPageMeta.getId())){
//				LfwWidget targetWidget = inPageMeta.getWidget(targetWidgetId);
//				if (targetWidget != null){
//					EventSubmitRule targetsubmitRule = getTargetWidgetSubmitRule(targetWidget, pluginId);
//					if (targetsubmitRule == null){
//						targetsubmitRule = new EventSubmitRule();
//						WidgetRule pwr = new WidgetRule();
//						pwr.setId(targetWidgetId);
//						targetsubmitRule.addWidgetRule(pwr);
//					}
//					sr.setParentSubmitRule(targetsubmitRule);
//				}
//			}
//			//同window内
//			else{
//				LfwWidget targetWidget = inPageMeta.getWidget(targetWidgetId);
//				if (targetWidget != null){
//					EventSubmitRule targetsubmitRule = getTargetWidgetSubmitRule(targetWidget, pluginId);
//					if (targetsubmitRule != null){
//						sr.addWidgetRule(targetsubmitRule.getWidgetRule(targetWidgetId));
//					}
//				}
//			}
//		}
		
		for (PlugoutEmitItem item : descEmitList){
			//TODO  增加异常处理
			String type = item.getType();
			String source = item.getSource();
			
			String controlType = type.split("\\.")[0]; 
			String eventType = type.split("\\.")[1];
			
			String sourceItem = null;
			if (source.indexOf(".") != -1){
				sourceItem = source.split("\\.")[1];	
				source = source.split("\\.")[0];
			}
			
			EventConf event = new EventConf();
			event.setJsEventClaszz(getJsEventClaszz(controlType, eventType));
			event.setOnserver(true);
			event.setSubmitRule(sr);
			event.setName(eventType);
//			StringBuffer scriptBuf = new StringBuffer();
//			scriptBuf.append("debugger;\n");
//			scriptBuf.append(generateSubmitRule(sr));
//			scriptBuf.append("triggerPlugout(submitRule);\n");
//			event.setScript(scriptBuf.toString());
			
			
			if (sourceItem == null)
				event.setMethodName(controlType + "_" + source + "_plugevent");
			else 
				event.setMethodName(controlType + "_" + source + "_" + sourceItem + "_plugevent");
			List<EventConf> eventList = null;
			if (controlType.equals(CONTROL_TYPE_WIDGET)){
				eventList = getEventList(outPageMeta.getWidget(sourceWidgetId));
			}
			else if (controlType.equals(CONTROL_TYPE_MENU) || controlType.equals(CONTROL_TYPE_POPUPMENU)){
				eventList = getEventList(outPageMeta.getWidget(sourceWidgetId).getViewMenus().getMenuBar(source).getItem(sourceItem));
			}
//			else if (controlType.equals(CONTROL_TYPE_CONINTER)){
//				eventList = getEventList(outPageMeta.getWidget(sourceWidgetId).getViewConinters().getContainer(source));
//			}
			else if (isComponentType(controlType)){
				eventList = getEventList(outPageMeta.getWidget(sourceWidgetId).getViewComponents().getComponent(source));
			}
			else if (controlType.equals(CONTROL_TYPE_DATASET)){
				eventList = getEventList(outPageMeta.getWidget(sourceWidgetId).getViewModels().getDataset(source));
			}
			mergeEvent(eventList, event);
		}
	}
	
	/**
	 *合并event 
	 * */
	private void mergeEvent(List<EventConf> eventList, EventConf event) {
		boolean merged = false;
		for (EventConf e : eventList){
			if (e.isOnserver() && e.getName().equals(event.getName())){
//				String jsEventClaszz = event.getJsEventClaszz() + "," + e.getJsEventClaszz(); 
//				e.setJsEventClaszz(jsEventClaszz);
				mergeSubmitRules(e, event);	
				merged = true;
				break;
			}
		}
//		for (EventConf e : eventList){
//			if (e.getMethodName().equals(event.getMethodName())){
//				mergeSubmitRules(e, event);	
//				merged = true;
//				break;
//			}
//		}
		if (!merged)
			eventList.add(event);
	}

	/**
	 * 合并提交规则
	 * @param e
	 * @param event
	 */
	private void mergeSubmitRules(EventConf e, EventConf event) {
		if (event.getSubmitRule() == null || event.getSubmitRule().getWidgetRules().size() == 0)
			return;
		if (e.getSubmitRule() == null)
			e.setSubmitRule(event.getSubmitRule());
		LfwSubmitRuleMergeUtil.mergeSubmitRule(e.getSubmitRule(), event.getSubmitRule());
//		Iterator<WidgetRule> it = event.getSubmitRule().getWidgetRules().values().iterator();
//		while (it.hasNext()){
//			e.getSubmitRule().addWidgetRule(it.next());
//		}
	}

	public EventSubmitRule getTargetWidgetSubmitRule(LfwWidget targetWidget, String pluginId) {
		if (targetWidget == null || targetWidget.getEventConfs() == null)
			return null;
		if (targetWidget.getEventConfs().length < 1)
			return null;
		for (EventConf event : targetWidget.getEventConfs()){
			if (event.getMethodName().equals("plugin" + pluginId)){
				return event.getSubmitRule(); 
			}
		}
		return null;
	}

	/**
	 * 获取控件事件对应监听类
	 * 
	 * @param controlType
	 * @param eventType
	 * @return
	 */
	private String getJsEventClaszz(String controlType, String eventType) {
		//widget
		if (controlType.equals(CONTROL_TYPE_WIDGET)){
			return DialogListener.class.getName();
		}
		//只有mouseListener
		else if (controlType.equals(CONTROL_TYPE_MENU) || controlType.equals(CONTROL_TYPE_BUTTON)
				|| controlType.equals(CONTROL_TYPE_IFRAMECOMP) || controlType.equals(CONTROL_TYPE_LABEL)){
			return MouseListener.class.getName();
		}
		//右键菜单
		else if (controlType.equals(CONTROL_TYPE_POPUPMENU)){
			if (isMouseEvent(eventType))
				return MouseListener.class.getName();
			else
				return ContextMenuListener.class.getName();
		}
		//数据集
		else if (controlType.equals(CONTROL_TYPE_DATASET))
			return DatasetListener.class.getName();
		//grid
		else if (controlType.equals(CONTROL_TYPE_GRID)){
			if (isMouseEvent(eventType))
				return MouseListener.class.getName();
			else if (eventType.equals(GridListener.ON_DATA_OUTER_DIV_CONTEXT_MENU))
				return GridListener.class.getName();
			else if (eventType.equals(GridRowListener.BEFORE_ROW_SELECTED)
					|| eventType.equals(GridRowListener.ON_ROW_DB_CLICK)
					|| eventType.equals(GridRowListener.ON_ROW_SELECTED))
				return GridRowListener.class.getName();
//			else if (eventType.equals(GridCellListener.ON_CELL_CLICK) || eventType.equals(GridCellListener.BEFORE_EDIT)
//					|| eventType.equals(GridCellListener.AFTER_EDIT) || eventType.equals(GridCellListener.CELL_EDIT)
//					|| eventType.equals(GridCellListener.CELL_VALUE_CHANGED))
			else
				return GridCellListener.class.getName();	
		}
		//excel
		else if (controlType.equals(CONTROL_TYPE_EXCEL)){
			if (isMouseEvent(eventType))
				return MouseListener.class.getName();
			else if (eventType.equals(ExcelListener.ON_DATA_OUTER_DIV_CONTEXT_MENU))
				return ExcelListener.class.getName();
			else if (eventType.equals(ExcelRowListener.ON_ROW_DB_CLICK) || eventType.equals(ExcelRowListener.ON_ROW_SELECTED)
					|| eventType.equals(ExcelRowListener.BEFORE_ROW_SELECTED))
				return ExcelRowListener.class.getName();
//			else if (eventType.equals(ExcelCellListener.BEFORE_EDIT) || eventType.equals(ExcelCellListener.AFTER_EDIT)
//					|| eventType.equals(ExcelCellListener.ON_CELL_CLICK) || eventType.equals(ExcelCellListener.CELL_EDIT)
//					|| eventType.equals(ExcelCellListener.CELL_VALUE_CHANGED))
			else
				return ExcelCellListener.class.getName();
		}	
		//tree
		else if (controlType.equals(CONTROL_TYPE_TREE)){
			if (isMouseEvent(eventType))
				return MouseListener.class.getName();
			else if (eventType.equals(TreeNodeListener.ON_DRAG_END) || eventType.equals(TreeNodeListener.ON_DRAG_START)
					|| eventType.equals(TreeNodeListener.ON_CLICK) || eventType.equals(TreeNodeListener.ON_DBCLICK)
					|| eventType.equals(TreeNodeListener.ON_NODE_LOAD) || eventType.equals(TreeNodeListener.ON_CHECKED)
					|| eventType.equals(TreeNodeListener.BEFORE_SEL_NODE_CHANGE) || eventType.equals(TreeNodeListener.AFTER_SEL_NODE_CHANGE)
					|| eventType.equals(TreeNodeListener.ROOT_NODE_CREATED) || eventType.equals(TreeNodeListener.NODE_CREATED)
					|| eventType.equals(TreeNodeListener.BEFORE_NODE_CAPTION_CHANGE))
				return TreeNodeListener.class.getName();
			else if (eventType.equals(TreeRowListener.BEFORE_NODE_CREATE))
				return TreeRowListener.class.getName();
			else 
				return TreeContextMenuListener.class.getName();
		}
		//form
		else if (controlType.equals(CONTROL_TYPE_FORM)){
			if (isMouseEvent(eventType))
				return MouseListener.class.getName();
			else if (eventType.equals(FocusListener.ON_BLUR) || eventType.equals(FocusListener.ON_FOCUS))
				return FocusListener.class.getName();
			else 
				return AutoformListener.class.getName();
		}
		//文本框
		else if (controlType.equals(CONTROL_TYPE_TEXTCOMP)){
			if (isMouseEvent(eventType))
				return MouseListener.class.getName();
			else if (eventType.equals(FocusListener.ON_BLUR) || eventType.equals(FocusListener.ON_FOCUS))
				return FocusListener.class.getName();
			else if (eventType.equals(TextListener.ON_SELECT) || eventType.equals(TextListener.ON_SELECT))
				return TextListener.class.getName();
			else 
				return KeyListener.class.getName();
		}
		//超链接
		else if (controlType.equals(CONTROL_TYPE_LINKCOMP))
			return LinkListener.class.getName();
		//自定义控件
		else if (controlType.equals(CONTROL_TYPE_CUSTOMCONIZOL))
			return SelfDefListener.class.getName();
		else
			return MouseListener.class.getName();
	}

	/**
	 * 判断是否为鼠标事件
	 * 
	 * @param eventType
	 * @return
	 */
	private boolean isMouseEvent(String eventType) {
		if (eventType.equals(MouseListener.ON_CLICK) || eventType.equals(MouseListener.ON_DB_CLICK) 
				|| eventType.equals(MouseListener.ON_MOUSE_OUT) || eventType.equals(MouseListener.ON_MOUSE_OVER))
			return true;
		else
			return false;
	}

	/**
	 * 触发器是否为Component
	 * 
	 * @param controlType
	 * @return
	 */
	private boolean isComponentType(String controlType) {
		if (controlType.equals(CONTROL_TYPE_BUTTON) || controlType.equals(CONTROL_TYPE_FORM) || 
				controlType.equals(CONTROL_TYPE_GRID) || controlType.equals(CONTROL_TYPE_TREE)||
				controlType.equals(CONTROL_TYPE_EXCEL) || controlType.equals(CONTROL_TYPE_TEXTCOMP)||
				controlType.equals(CONTROL_TYPE_IFRAMECOMP) || controlType.equals(CONTROL_TYPE_LABEL)||
				controlType.equals(CONTROL_TYPE_IMAGE) || controlType.equals(CONTROL_TYPE_LINKCOMP) || controlType.equals(CONTROL_TYPE_CUSTOMCONIZOL))
			return true;
		else 
			return false;
		}
	
	/**
	 * 生成提交规则
	 * 
	 */
	public String generateSubmitRuleScript(EventSubmitRule submitRule) {
		if (submitRule != null) {
//			String ruleId = "sr_" + jsEvent.getName() + "_" + listener.getId();
			String ruleId = "submitRule";
			StringBuffer buf = new StringBuffer();
			buf.append("var ").append(ruleId).append(" = new SubmitRule();\n");
			if (submitRule.getParamMap() != null && submitRule.getParamMap().size() > 0) {
				Iterator<Entry<String, Parameter>> it = submitRule.getParamMap().entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Parameter> entry = it.next();
					buf.append(ruleId).append(".addParam('").append(entry.getKey()).append("', '").append(entry.getValue().getValue()).append("');\n");
				}
			}
			if (submitRule.isCardSubmit()) {
				buf.append(ruleId).append(".setCardSubmit(true);\n");
			}
			if (submitRule.isTabSubmit()) {
				buf.append(ruleId).append(".setTabSubmit(true);\n");
			}
			if (submitRule.isPanelSubmit()) {
				buf.append(ruleId).append(".setPanelSubmit(true);\n");
			}

			String jsScript = generateWidgetRulesScript(submitRule, ruleId);
			buf.append(jsScript);

			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if (pSubmitRule != null) {
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = new SubmitRule();\n");
				String pJsScript = generateWidgetRulesScript(pSubmitRule, pRuleId);
				buf.append(pJsScript);
				buf.append(ruleId + ".setParentSubmitRule(" + pRuleId + ");\n");
			}
			return buf.toString();
//			buf.append(listenerShowId).append(".").append(jsEvent.getName()).append(".submitRule = (").append(ruleId).append(");\n");
		}
		else 
			return "";

	}

	/**
	 * 创建js的提交规则
	 * 
	 * @param submitRule
	 * @param ruleId
	 * @return
	 */
	private String generateWidgetRulesScript(EventSubmitRule submitRule, String ruleId) {
		StringBuffer sb = new StringBuffer();
		Map<String, WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
		if (widgetRuleMap != null && !widgetRuleMap.isEmpty()) {
			Iterator<Entry<String, WidgetRule>> it = widgetRuleMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, WidgetRule> entry = it.next();
				WidgetRule widgetRule = entry.getValue();
				String wstr = generateWidgetRuleScript(widgetRule);
				sb.append(wstr);

				String widgetId = widgetRule.getId();
				sb.append(ruleId).append(".addWidgetRule('").append(widgetId).append("', wdr_").append(widgetId).append(");\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 得到片段的规则
	 * 
	 * @param widgetRule
	 * @return
	 */
	private String generateWidgetRuleScript(WidgetRule widgetRule) {
		StringBuffer buf = new StringBuffer();
		String wid = "wdr_" + widgetRule.getId();
		buf.append("var ").append(wid).append(" = new WidgetRule('").append(widgetRule.getId()).append("');\n");
		if (widgetRule.isCardSubmit()) {
			buf.append(wid).append(".setCardSubmit(true);\n");
		}
		if (widgetRule.isTabSubmit()) {
			buf.append(wid).append(".setTabSubmit(true);\n");
		}
		if (widgetRule.isPanelSubmit()) {
			buf.append(wid).append(".setPanelSubmit(true);\n");
		}

		DatasetRule[] dsRules = widgetRule.getDatasetRules();
		if (dsRules != null) {
			for (int i = 0; i < dsRules.length; i++) {
				DatasetRule dsRule = dsRules[i];
				String id = "dsr_" + dsRule.getId();
				buf.append("var ").append(id).append(" = new DatasetRule('").append(dsRule.getId()).append("','").append(dsRule.getType()).append("');\n");
				buf.append(wid).append(".addDsRule('").append(dsRule.getId()).append("',").append(id).append(");\n");
			}
		}

		TreeRule[] treeRules = widgetRule.getTreeRules();
		if (treeRules != null) {
			for (int i = 0; i < treeRules.length; i++) {
				TreeRule treeRule = treeRules[i];
				String id = "treer_" + treeRule.getId();
				buf.append("var ").append(id).append(" = new TreeRule('").append(treeRule.getId()).append("','").append(treeRule.getType()).append("');\n");
				buf.append(wid).append(".addTreeRule('").append(treeRule.getId()).append("',").append(id).append(");\n");
			}
		}

		GridRule[] gridRules = widgetRule.getGridRules();
		if (gridRules != null) {
			for (int i = 0; i < gridRules.length; i++) {
				GridRule gridRule = gridRules[i];
				String id = "gridr_" + gridRule.getId();
				buf.append("var ").append(id).append(" = new GridRule('").append(gridRule.getId()).append("','").append(gridRule.getType()).append("');\n");
				buf.append(wid).append(".addGridRule('").append(gridRule.getId() + "',").append(id).append(");\n");
			}
		}

		FormRule[] formRules = widgetRule.getFormRules();
		if (formRules != null) {
			for (int i = 0; i < formRules.length; i++) {
				FormRule formRule = formRules[i];
				String id = "formr_" + formRule.getId();
				buf.append("var ").append(id).append(" = new FormRule('").append(formRule.getId()).append("','").append(formRule.getType()).append("');\n");
				buf.append(wid).append(".addFormRule('").append(formRule.getId() + "',").append(id).append(");\n");
			}
		}
		return buf.toString();
	}
	
	/**
	 * 对每个pageMeta都增加一个参照Plugin
	 * @param pageMeta
	 */
	
	private void addRefPlugin(PageMeta pageMeta) {
		PluginDesc pluginDesc = new PluginDesc();
		pluginDesc.setId(RefOkController.PLUGIN_ID);
		List<PluginDescItem> descItemList = new ArrayList<PluginDescItem>();
		
		PluginDescItem typeItem = new PluginDescItem();
		typeItem.setId(RefOkController.TYPE);
		PluginDescItem idItem = new PluginDescItem();
		idItem.setId(RefOkController.ID);
		PluginDescItem writefieldsItem = new PluginDescItem();
		writefieldsItem.setId(RefOkController.WRITEFIELDS);
		
		descItemList.add(typeItem);
		descItemList.add(idItem);
		descItemList.add(writefieldsItem);
		pluginDesc.setDescItemList(descItemList);
		for (LfwWidget widget :  pageMeta.getWidgets()){
			if (widget.getPluginDesc(RefOkController.PLUGIN_ID) == null)
				widget.addPluginDescs(pluginDesc);
		}
	}

	
	
	/**
	 * 对每个pageMeta都增加一个参照Plugout
	 * 
	 * @param refWidget
	 */
	private void addAdvanceQueryPlugout(LfwWidget refWidget) {
		PlugoutDesc plugoutDesc = new PlugoutDesc(); 
		plugoutDesc.setId("advanceQueryPlugOut");
		List<PlugoutDescItem>  plugoutDescItemList = new ArrayList<PlugoutDescItem>();
		
		PlugoutDescItem typeItem = new PlugoutDescItem();
		typeItem.setName(RefOkController.TYPE);
		plugoutDescItemList.add(typeItem);
		plugoutDesc.setDescItemList(plugoutDescItemList);
		if (refWidget.getPlugoutDesc("advanceQueryPlugOut") == null)
			refWidget.addPlugoutDescs(plugoutDesc);
	}
	
	
	/**
	 * 对每个pageMeta都增加一个参照Plugout
	 * 
	 * @param refWidget
	 */
	private void addRefPlugout(LfwWidget refWidget) {
		PlugoutDesc plugoutDesc = new PlugoutDesc(); 
		plugoutDesc.setId(RefOkController.PLUGOUT_ID);
		List<PlugoutDescItem>  plugoutDescItemList = new ArrayList<PlugoutDescItem>();
		
		PlugoutDescItem typeItem = new PlugoutDescItem();
		typeItem.setName(RefOkController.TYPE);
		PlugoutDescItem idItem = new PlugoutDescItem();
		idItem.setName(RefOkController.ID);
		PlugoutDescItem writefieldsItem = new PlugoutDescItem();
		writefieldsItem.setName(RefOkController.WRITEFIELDS);
		PlugoutDescItem valuesItem = new PlugoutDescItem();
		plugoutDescItemList.add(typeItem);
		plugoutDescItemList.add(idItem);
		plugoutDescItemList.add(writefieldsItem);
		plugoutDescItemList.add(valuesItem);
		plugoutDesc.setDescItemList(plugoutDescItemList);
		if (refWidget.getPlugoutDesc(RefOkController.PLUGOUT_ID) == null)
			refWidget.addPlugoutDescs(plugoutDesc);
	}
	
	/**
	 * 获取WidgetElement上的事件列表
	 * 
	 * @param ele
	 * @return
	 */
	private List<EventConf> getEventList(WidgetElement ele){
		List<EventConf> eventList = null;
		eventList = ele.getEventConfList();
		if (eventList == null){
			eventList = new ArrayList<EventConf>();
			ele.setEventConfList(eventList);
		}
		return eventList;
	}
	

}
