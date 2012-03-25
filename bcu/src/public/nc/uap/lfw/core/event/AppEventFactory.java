package nc.uap.lfw.core.event;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.core.common.DatasetConstant;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.ParameterSet;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.GridCellListener;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.event.conf.TreeNodeListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.util.LfwClassUtil;

public final class AppEventFactory {
//	private WebElement source;
	private IEventSupport source;
	private String level;
	public Object getController(){
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
//		WebElement source = getSource();
//		
//		String methodName = ctx.getParameter(AppLifeCycleContext.METHOD_NAME);
//		EventConf[] confs = source.getEventConfs();
//		EventConf conf = null;
//		if(confs != null){
//			for (int i = 0; i < confs.length; i++) {
//				EventConf currConf = confs[i];
//				if(currConf.getMethodName().equals(methodName)){
//					conf = currConf;
//					break;
//				}
//			}
//		}
//		
//		if(conf == null)
//			throw new LfwRuntimeException("指定的事件不存在,method:" + methodName + ",source:" + source.getId());
		
		String ctrlClazz =ctx.getParameter("clc");// conf.getControllerClazz();
		//没有单独指定，走当前全局Controller
		if(ctrlClazz == null || ctrlClazz.trim().equals("")){
			ctrlClazz = getControllerClazz();
		}
		
		if(ctrlClazz == null){
			LfwLogger.error("没有找到Controller配置,level:" + level);
			//throw new LfwRuntimeException("没有找到Controller配置,level:" + level);
			return null;
		}else{
			return LfwClassUtil.newInstance(ctrlClazz);
		}
	}

	private String getLevel() {
		if(level == null)
			level = AppLifeCycleContext.current().getParameter(AppLifeCycleContext.EVENT_LEVEL);
		return level;
	}

	private String getControllerClazz() {
		String level = getLevel();
		if(AppLifeCycleContext.EVENT_LEVEL_VIEW.equals(level))
			return AppLifeCycleContext.current().getViewContext().getView().getControllerClazz();
		else if(AppLifeCycleContext.EVENT_LEVEL_WIN.equals(level))
			return AppLifeCycleContext.current().getWindowContext().getWindow().getControllerClazz();
		else if (AppLifeCycleContext.current().getApplicationContext().getApplication() != null)
			return AppLifeCycleContext.current().getApplicationContext().getApplication().getControllerClazz();
		else 
			return null;
	}

	private IEventSupport getSource() {
		if(source == null){
			AppLifeCycleContext ctx = AppLifeCycleContext.current();
			String sourceType = ctx.getParameter(LfwPageContext.SOURCE_TYPE);
			String sourceId = ctx.getParameter(LfwPageContext.SOURCE_ID);
			if(LfwPageContext.SOURCE_TYPE_DATASET.equals(sourceType)){
				source = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset(sourceId);
			}
			else if(LfwPageContext.SOURCE_TYPE_MENUBAR_MENUITEM.equals(sourceType)){
				String parentSourceId = ctx.getParameter(LfwPageContext.PARENT_SOURCE_ID);
				source = getMenuItem(sourceId, parentSourceId, AppLifeCycleContext.current().getViewContext().getView());//.getViewMenus().getMenuBar(parentSourceId).getItem(sourceId);
			}
			else if(LfwPageContext.SOURCE_TYPE_PAGEMETA.equals(sourceType)){
				source = AppLifeCycleContext.current().getWindowContext().getWindow();
			}
			else if(LfwPageContext.SOURCE_TYPE_WIDGT.equals(sourceType)){
				source = AppLifeCycleContext.current().getWindowContext().getWindow().getWidget(sourceId);
			}else if(LfwPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM.equals(sourceType)){
				String parentSourceId = ctx.getParameter(LfwPageContext.PARENT_SOURCE_ID);
				ContextMenuComp menubar = AppLifeCycleContext.current().getViewContext().getView().getViewMenus().getContextMenu(parentSourceId);
				List<MenuItem> list = menubar.getItemList();
				Iterator<MenuItem> it = list.iterator();
				MenuItem mItem = null;
				while (it.hasNext()) {
					MenuItem item = it.next();
					if (item.getId().equals(sourceId)) {
						mItem = item;
						break;
					}
					
					if(mItem == null){
						mItem = getMenuItem(item, sourceId);
					}
					
				}
				if(mItem == null)
					throw new LfwRuntimeException("没有找到对应的MenuItem," + sourceId);
				source = mItem;
			}
			else if("tag".equals(sourceType)){
				source = UIElementFinder.findElementById(AppLifeCycleContext.current().getViewContext().getUIMeta(), sourceId);
			}
			else {
				if(sourceId != null)
					source = AppLifeCycleContext.current().getViewContext().getView().getViewComponents().getComponent(sourceId);
				else
					source = AppLifeCycleContext.current().getWindowContext().getWindow();
			}
		}
		return source;
	}
	
	private MenuItem getMenuItem(String menuItemId, String sourceId, LfwWidget widget) {
		MenubarComp menubar = widget.getViewMenus().getMenuBar(sourceId);
		List<MenuItem> list = menubar.getMenuList();
		Iterator<MenuItem> it = list.iterator();
		MenuItem mItem = null;
		while (it.hasNext()) {
			MenuItem item = it.next();
			if (item.getId().equals(menuItemId)) {
				mItem = item;
				break;
			}
			
			if(mItem == null){
				mItem = getMenuItem(item, menuItemId);
			}
			
		}
		if(mItem == null)
			throw new LfwRuntimeException("没有找到对应的MenuItem," + sourceId);
		return mItem;
	}
	
	private MenuItem getMenuItem(MenuItem item, String sourceId) {
		List<MenuItem> items = item.getChildList();
		if (items != null && items.size() > 0) {
			Iterator<MenuItem> cIt = items.iterator();
			while (cIt.hasNext()) {
				MenuItem cItem = cIt.next();
				if (cItem.getId().equals(sourceId)) {
					return cItem;
				}
				
				cItem = getMenuItem(cItem, sourceId);
				if(cItem != null)
					return cItem;
			}
		}
		return null;
	}

	public AbstractServerEvent<?> getServerEvent() {
		AppLifeCycleContext ctx = AppLifeCycleContext.current();
		String eventName = ctx.getParameter(LfwPageContext.EVENT_NAME);
		
		String sourceType = ctx.getParameter(LfwPageContext.SOURCE_TYPE);
//		String sourceId = ctx.getParameter(LfwPageContext.SOURCE_ID);
		
//		IEventHandler eventHandler = EventHandlerFactory.getEventHandler(sourceType, null);
//		eventHandler.execute();
		
		
		JsEventDesc desc = getEventDesc(eventName);
		if(desc == null){
			 desc = new JsEventDesc(null, null);
			desc.setEventClazz(nc.uap.lfw.core.event.ScriptEvent.class.getName());
		}
		String clazz = desc.getEventClazz();
		try {
			Class<?> c = LfwClassUtil.forName(clazz);
			IEventSupport source = getSource();
			if (LfwPageContext.SOURCE_TYPE_DATASET.equals(sourceType)) {
				Dataset ds = (Dataset)source;
				if(eventName.equals(DatasetListener.ON_DATA_LOAD)){
					DataLoadEvent serverEvent = new DataLoadEvent(ds);
					ParameterSet params = ds.getReqParameters();
					String keyValue = params.getParameterValue(DatasetConstant.QUERY_PARAM_KEYVALUE);
//					if(keyValue == null)
//						throw new LfwRuntimeException("目标KeyValue为空");
					
					String currKey = ds.getCurrentKey();
					serverEvent.setOriginalKeyValue(currKey);
					if(currKey != null && !currKey.equals(""))
						serverEvent.setOriginalPageIndex(ds.getCurrentRowSet().getPaginationInfo().getPageIndex());
					if(keyValue != null)
						ds.setCurrentKey(keyValue);
					
					String pageIndexStr = params.getParameterValue(DatasetConstant.QUERY_PARAM_PAGEINDEX);
					int pageIndex = 0;
					if(pageIndexStr != null)
						pageIndex = Integer.parseInt(pageIndexStr);
					ds.getCurrentRowSet().getPaginationInfo().setPageIndex(pageIndex);
					
					return serverEvent;
				}
				else if(eventName.equals(DatasetListener.ON_AFTER_ROW_INSERT)){
					String insertIndex = ctx.getParameter("row_insert_index");
					RowInsertEvent event = new RowInsertEvent(ds);
					event.setInsertedIndex(Integer.valueOf(insertIndex));
					return event;
				}	
				else if (eventName.equals(DatasetListener.ON_AFTER_DATA_CHANGE) || eventName.equals(DatasetListener.ON_BEFORE_DATA_CHANGE)) {
					String colSingle = ctx.getParameter("isColSingle");
					//如果不是列批量修改
					if (colSingle == null || colSingle.equalsIgnoreCase("false")){
						String rowIndex = ctx.getParameter("cellRowIndex");
						String colIndex = ctx.getParameter("cellColIndex");
						String newValue = ctx.getParameter("newValue");
						String oldValue = ctx.getParameter("oldValue");
						DatasetCellEvent event = new DatasetCellEvent(ds);
						event.setRowIndex(rowIndex == null ? -1 : Integer.parseInt(rowIndex));
						event.setColIndex(colIndex == null ? -1 : Integer.parseInt(colIndex));
						event.setNewValue(newValue);
						event.setOldValue(oldValue);
						return event;
					}
					else{
						//处理某一列批量修改
						String rowIndex = ctx.getParameter("cellRowIndices");
						String colIndex = ctx.getParameter("cellColIndex");
						String newValue = ctx.getParameter("newValues");
						String oldValue = ctx.getParameter("oldValues");
						DatasetCellEvent event = new DatasetColSingleEvent(ds);
						String[] strRowIndices = rowIndex.split(",");
						int[] rowIndices = new int[strRowIndices.length];
						for (int i = 0; i < strRowIndices.length; i++){
							rowIndices[i] = Integer.parseInt(strRowIndices[i]); 
						}
						((DatasetColSingleEvent)event).setRowIndices(rowIndices);
						event.setColIndex(colIndex == null ? -1 : Integer.parseInt(colIndex));
						//处理空值
						newValue = newValue.replaceAll(",,", ", ,");
						if (newValue.startsWith(","))
							newValue = " " + newValue;
						if (newValue.endsWith(","))
							newValue = newValue + " ";
						oldValue = oldValue.replaceAll(",,", ", ,");
						if (oldValue.startsWith(","))
							oldValue = " " + oldValue;
						if (oldValue.endsWith(","))
							oldValue = oldValue + " ";
						((DatasetColSingleEvent)event).setNewValues(newValue.split(","));
						((DatasetColSingleEvent)event).setOldValues(oldValue.split(","));
						return (DatasetCellEvent)event;
					}
				}
				
			}
			else if (LfwPageContext.SOURCE_TYPE_TREE.equals(sourceType)){
				TreeViewComp tree = (TreeViewComp)source;
				if (TreeNodeListener.ON_DRAG_START.equals(eventName)) {
					TreeNodeDragEvent serverEvent = new TreeNodeDragEvent(tree);
					String sourceNodeRowId = ctx.getParameter("sourceNodeRowId");
					((TreeNodeDragEvent)serverEvent).setSourceNodeRowId(sourceNodeRowId);
					return serverEvent;
				} 
				else if (TreeNodeListener.ON_DRAG_END.equals(eventName)) {
					TreeNodeDragEvent serverEvent = new TreeNodeDragEvent(tree);
					String sourceNodeRowId = ctx.getParameter("sourceNodeRowId");
					String targetNodeRowId = ctx.getParameter("targetNodeRowId");
					((TreeNodeDragEvent)serverEvent).setSourceNodeRowId(sourceNodeRowId);
					((TreeNodeDragEvent)serverEvent).setTargetNodeRowId(targetNodeRowId);
					return serverEvent;
				} 
				else if (TreeNodeListener.ON_DBCLICK.equals(eventName) || TreeNodeListener.ON_CLICK.equals(eventName)) {
					TreeNodeEvent serverEvent = new TreeNodeEvent(tree);
					String nodeRowId = ctx.getParameter("nodeRowId");
					((TreeNodeEvent)serverEvent).setNodeRowId(nodeRowId);
					return serverEvent;
				}
				else if (TreeNodeListener.ON_CHECKED.equals(eventName)) {
					TreeNodeEvent serverEvent = new TreeNodeEvent(tree);
					String nodeRowId = ctx.getParameter("nodeRowId");
					((TreeNodeEvent)serverEvent).setNodeRowId(nodeRowId);
					return serverEvent;
				}	
				else if(TreeNodeListener.AFTER_SEL_NODE_CHANGE.equals(eventName)){
					TreeNodeEvent serverEvent = new TreeNodeEvent(tree);
					String nodeRowId = ctx.getParameter("nodeRowId");
					((TreeNodeEvent)serverEvent).setNodeRowId(nodeRowId);
					String currentDsId = ctx.getParameter("datasetId");
					((TreeNodeEvent)serverEvent).setCurrentdsId(currentDsId);
					return serverEvent;
				}
			}
			else if (LfwPageContext.SOURCE_TYPE_GRID.equals(sourceType)){
				GridComp grid = (GridComp)source;
				if(GridCellListener.AFTER_EDIT.equals(eventName) ) {
					CellEvent serverEvent = new CellEvent(grid);
					String rowIndex = ctx.getParameter("rowIndex");
					String colIndex = ctx.getParameter("colIndex");
					String newValue = ctx.getParameter("newValue");
					String oldValue = ctx.getParameter("oldValue");
					if(rowIndex != null && !rowIndex.equals(""))
						serverEvent.setRowIndex(Integer.parseInt(rowIndex));
					if(colIndex != null && !rowIndex.equals(""))
						serverEvent.setColIndex(Integer.parseInt(colIndex));
					
					serverEvent.setNewValue(newValue);
					serverEvent.setOldValue(oldValue);
					return serverEvent;
				}else if(GridCellListener.BEFORE_EDIT.equals(eventName) || 
						GridCellListener.CELL_EDIT.equals(eventName) ||
						GridCellListener.CELL_VALUE_CHANGED.equals(eventName) ||
						GridCellListener.ON_CELL_CLICK.equals(eventName)){
					CellEvent serverEvent = new CellEvent(grid);
					String rowIndex = ctx.getParameter("rowIndex");
					String colIndex = ctx.getParameter("colIndex");
					if(rowIndex != null)
						serverEvent.setRowIndex(Integer.parseInt(rowIndex));
					if(colIndex != null)
						serverEvent.setColIndex(Integer.parseInt(colIndex));
					return serverEvent;
				}
			}
			
			Constructor<?>[] cons = c.getConstructors();
			if(cons != null){
				for(Constructor<?> con : cons){
					return (AbstractServerEvent<?>) con.newInstance(source);
				}
			}
			return null;
		} 
		catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException("构造事件参数出现问题, class:" + clazz);
		}
	}

	private JsEventDesc getEventDesc(String eventName) {
		IEventSupport source = getSource();
		List<JsEventDesc> list = source.getAcceptEventDescs();
		if(list == null)
			return null;
		Iterator<JsEventDesc> it = list.iterator();
		while(it.hasNext()){
			JsEventDesc desc = it.next();
			if(desc.getName().equals(eventName))
				return desc;
		}
		return null;
	}
	
	
}
