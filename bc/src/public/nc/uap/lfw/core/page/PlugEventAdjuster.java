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
	 * ���window֮���plug����plugEvent
	 * 
	 * @param app
	 */
	public void addAppPlugEventConf(ApplicationContext appCtx, PageMeta pageMeta){
		if (LfwRuntimeEnvironment.getWebContext().getAppSession() == null)
			return;
		if (appCtx.getApplication() == null)
			return;
		//��ÿ��pageMeta������һ������Plugin
		addRefPlugin(pageMeta);
		//�򿪵�Ϊ����pageMetaʱ�����Ӳ���plugout��ע�����յ�view��id��Ϊ"main"
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
				//��������
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
		
		for (Connector conn : appCtx.getApplication().getConnectorList()){
			String sourceWindowId = conn.getSourceWindow();
			String targetWindowId = conn.getTargetWindow();
			
			//����plugout
//			String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
			if (sourceWindowId.equals(pageId)){
				PageMeta inPagemeta = LfwRuntimeEnvironment.getWebContext().getCrossPageMeta(targetWindowId);
				addEvent(pageMeta, inPagemeta, conn);
			}
			//����plugin
			if (targetWindowId.equals(pageMeta.getId())){
//				PageMeta outPagemeta = appCtx.getWindowContext(sourceWindowId).getWindow();
				PageMeta outPagemeta = LfwRuntimeEnvironment.getWebContext().getCrossPageMeta(sourceWindowId);
				if (outPagemeta == null)
					return;
				addEventSubmitRule(pageMeta, outPagemeta, conn);
			}
		}
	}
	
	/**
	 * ��ÿ��pageMeta������һ������Plugin
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
	 * ��ÿ��pageMeta������һ������Plugout
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
	 * ��ǰ̨�¼�׷��plugout�ύ���� 
	 * 
	 * @param inPageMeta
	 * @param outPagemeta
	 * @param conn
	 */
	private void addEventSubmitRule(PageMeta inPageMeta, PageMeta outPageMeta, Connector conn){
		String sourceWindowId = conn.getSourceWindow();
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
			
			//�����ύ����
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
			if (sourceWindowId != null)
				sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW,sourceWindowId));
			sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE,soruceWidgetId));
			sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_ID,plugoutId));
		}
		
		for (PlugoutEmitItem item : descEmitList){
			//TODO  �����쳣����
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
	
	private List<EventConf> getEventList(WidgetElement ele){
		List<EventConf> eventList = null;
		eventList = ele.getEventConfList();
		if (eventList == null){
			eventList = new ArrayList<EventConf>();
			ele.setEventConfList(eventList);
		}
		return eventList;
	}
	
	/**
	 * ���window��plug����plugEvent
	 * @param pageMeta
	 */
	public void addPlugEventConf(PageMeta pageMeta){
		Iterator<Connector> it = pageMeta.getConnectorMap().values().iterator();
		while(it.hasNext()){
			Connector conn = it.next();
			addEvent(pageMeta, pageMeta, conn);
		}	
	}

	
	private void addEvent(PageMeta outPageMeta,PageMeta inPageMeta, Connector conn){
		String sourceWindowId = conn.getSourceWindow();
		String sourceWidgetId = conn.getSource();
		String plugoutId = conn.getPlugoutId();
		String targetWidgetId = conn.getTarget();
		String pluginId = conn.getPluginId();
		PlugoutDesc plugoutDesc = outPageMeta.getWidget(sourceWidgetId).getPlugoutDesc(plugoutId);
		if (plugoutDesc == null)
			return;
		List<PlugoutEmitItem> descEmitList = plugoutDesc.getEmitList();
		if (descEmitList == null)
			return;

		EventSubmitRule sr = null;
		//�����ύ����
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
		if (sourceWindowId != null)
			sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW,sourceWindowId));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SIGN,"1"));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE,sourceWidgetId));
		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_ID,plugoutId));
		
		//����plugin���ύ����
		if(inPageMeta != null){
			//TODO 
			//��ͬwindow
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
			//ͬwindow��
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
		
		for (PlugoutEmitItem item : descEmitList){
			//TODO  �����쳣����
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
	 *�ϲ�event 
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
	 * �ϲ��ύ����
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
	 * ��ȡ�ؼ��¼���Ӧ������
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
		//ֻ��mouseListener
		else if (controlType.equals(CONTROL_TYPE_MENU) || controlType.equals(CONTROL_TYPE_BUTTON)
				|| controlType.equals(CONTROL_TYPE_IFRAMECOMP) || controlType.equals(CONTROL_TYPE_LABEL)){
			return MouseListener.class.getName();
		}
		//�Ҽ��˵�
		else if (controlType.equals(CONTROL_TYPE_POPUPMENU)){
			if (isMouseEvent(eventType))
				return MouseListener.class.getName();
			else
				return ContextMenuListener.class.getName();
		}
		//���ݼ�
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
		//�ı���
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
		//������
		else if (controlType.equals(CONTROL_TYPE_LINKCOMP))
			return LinkListener.class.getName();
		//�Զ���ؼ�
		else if (controlType.equals(CONTROL_TYPE_CUSTOMCONIZOL))
			return SelfDefListener.class.getName();
		else
			return MouseListener.class.getName();
	}

	/**
	 * �ж��Ƿ�Ϊ����¼�
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
	 * �������Ƿ�ΪComponent
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
	 * �����ύ����
	 * 
	 */
	public String generateSubmitRule(EventSubmitRule submitRule) {
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

			String jsScript = createJsSubmitRule(submitRule, ruleId);
			buf.append(jsScript);

			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if (pSubmitRule != null) {
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = new SubmitRule();\n");
				String pJsScript = createJsSubmitRule(pSubmitRule, pRuleId);
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
	 * ����js���ύ����
	 * 
	 * @param submitRule
	 * @param ruleId
	 * @return
	 */
	private String createJsSubmitRule(EventSubmitRule submitRule, String ruleId) {
		StringBuffer sb = new StringBuffer();
		Map<String, WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
		if (widgetRuleMap != null && !widgetRuleMap.isEmpty()) {
			Iterator<Entry<String, WidgetRule>> it = widgetRuleMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, WidgetRule> entry = it.next();
				WidgetRule widgetRule = entry.getValue();
				String wstr = getWidgetRule(widgetRule);
				sb.append(wstr);

				String widgetId = widgetRule.getId();
				sb.append(ruleId).append(".addWidgetRule('").append(widgetId).append("', wdr_").append(widgetId).append(");\n");
			}
		}
		return sb.toString();
	}

	/**
	 * �õ�Ƭ�εĹ���
	 * 
	 * @param widgetRule
	 * @return
	 */
	private String getWidgetRule(WidgetRule widgetRule) {
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
	

}
