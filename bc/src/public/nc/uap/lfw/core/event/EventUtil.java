/**
 * 
 */
package nc.uap.lfw.core.event;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.CheckBoxComp;
import nc.uap.lfw.core.comp.CheckboxGroupComp;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.EditorComp;
import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.FileUploadComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IFrameComp;
import nc.uap.lfw.core.comp.ImageComp;
import nc.uap.lfw.core.comp.LabelComp;
import nc.uap.lfw.core.comp.LinkComp;
import nc.uap.lfw.core.comp.ListComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.ModalDialogComp;
import nc.uap.lfw.core.comp.RadioComp;
import nc.uap.lfw.core.comp.RadioGroupComp;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.comp.SelfDefComp;
import nc.uap.lfw.core.comp.SelfDefElementComp;
import nc.uap.lfw.core.comp.TextAreaComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.conf.AutoformListener;
import nc.uap.lfw.core.event.conf.CardListener;
import nc.uap.lfw.core.event.conf.ContainerListener;
import nc.uap.lfw.core.event.conf.ContextMenuListener;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.DialogListener;
import nc.uap.lfw.core.event.conf.ExcelCellListener;
import nc.uap.lfw.core.event.conf.ExcelListener;
import nc.uap.lfw.core.event.conf.ExcelRowListener;
import nc.uap.lfw.core.event.conf.FileListener;
import nc.uap.lfw.core.event.conf.FocusListener;
import nc.uap.lfw.core.event.conf.GridCellListener;
import nc.uap.lfw.core.event.conf.GridListener;
import nc.uap.lfw.core.event.conf.GridRowListener;
import nc.uap.lfw.core.event.conf.JsEventDesc;
import nc.uap.lfw.core.event.conf.KeyListener;
import nc.uap.lfw.core.event.conf.LinkListener;
import nc.uap.lfw.core.event.conf.ListListener;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.OutlookBarListener;
import nc.uap.lfw.core.event.conf.PageListener;
import nc.uap.lfw.core.event.conf.ReferenceTextListener;
import nc.uap.lfw.core.event.conf.SelfDefListener;
import nc.uap.lfw.core.event.conf.TabListener;
import nc.uap.lfw.core.event.conf.TextListener;
import nc.uap.lfw.core.event.conf.TreeContextMenuListener;
import nc.uap.lfw.core.event.conf.TreeNodeListener;
import nc.uap.lfw.core.event.conf.TreeRowListener;
import nc.uap.lfw.core.event.conf.WidgetListener;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.jsp.uimeta.UIShutter;
import nc.uap.lfw.jsp.uimeta.UITabComp;

/**
 * @author chouhl
 *
 */
public class EventUtil {
	
	public final static String[] MouseListeners = new String[]{
		MouseListener.ON_MOUSE_OUT, MouseListener.ON_MOUSE_OVER, MouseListener.ON_DB_CLICK, MouseListener.ON_CLICK
	};
	
	public final static String[] CardListeners = new String[]{
		CardListener.BEFORE_PAGE_CHANGE, CardListener.AFTER_PAGE_INIT, CardListener.BEFORE_PAGE_INIT
	};
	
	public final static String[] ContextMenuListeners = new String[]{
		ContextMenuListener.ON_MOUSE_OUT, ContextMenuListener.ON_CLOSE, ContextMenuListener.BEFORE_CLOSE, ContextMenuListener.ON_SHOW, ContextMenuListener.BEFORE_SHOW
	};
	
	public final static String[] FocusListeners = new String[]{
		FocusListener.ON_BLUR, FocusListener.ON_FOCUS
	};
	
	public final static String[] ExcelListeners = new String[]{
		ExcelListener.ON_DATA_OUTER_DIV_CONTEXT_MENU
	};
	
	public final static String[] ExcelRowListeners = new String[]{
		ExcelRowListener.ON_ROW_DB_CLICK, ExcelRowListener.ON_ROW_SELECTED, ExcelRowListener.BEFORE_ROW_SELECTED
	};
	
	public final static String[] ExcelCellListeners = new String[]{
		ExcelCellListener.BEFORE_EDIT, ExcelCellListener.AFTER_EDIT, ExcelCellListener.CELL_EDIT, ExcelCellListener.ON_CELL_CLICK, ExcelCellListener.CELL_VALUE_CHANGED
	};
	
	public final static String[] FileListeners = new String[]{
		FileListener.ON_UPLOAD
	};
	
	public final static String[] AutoformListeners = new String[]{
		AutoformListener.IN_ACTIVE, AutoformListener.GET_VALUE, AutoformListener.ACTIVE, AutoformListener.SET_VALUE
	};
	
	public final static String[] GridListeners = new String[]{
		GridListener.ON_DATA_OUTER_DIV_CONTEXT_MENU
	};
	
	public final static String[] GridRowListeners = new String[]{
		GridRowListener.ON_ROW_DB_CLICK, GridRowListener.ON_ROW_SELECTED, GridRowListener.BEFORE_ROW_SELECTED
	};
	
	public final static String[] GridCellListeners = new String[]{
		GridCellListener.BEFORE_EDIT, GridCellListener.AFTER_EDIT, GridCellListener.CELL_EDIT, GridCellListener.ON_CELL_CLICK, GridCellListener.CELL_VALUE_CHANGED
	};
	
	public final static String[] PluginListeners = new String[]{
		"plugin"
	};
	
	public final static String[] ViewListeners = new String[]{
		//WidgetListener.BEFORE_INIT_DATA, WidgetListener.ON_INITIALIZING
	};
	
	public final static String[] DialogListeners = new String[]{
		DialogListener.ON_CLOSE, DialogListener.AFTER_CLOSE, DialogListener.BEFORE_CLOSE, DialogListener.BEFORE_SHOW, DialogListener.ON_CANCEL, DialogListener.ON_OK
	};
	
	public final static String[] LinkListeners = new String[]{
		LinkListener.ON_CLICK
	};

	public final static String[] ListListeners = new String[]{
		ListListener.DB_VALUE_CHANGE, ListListener.VALUE_CHANGED
	};
	
	public final static String[] ContainerListeners = new String[]{
		ContainerListener.ON_CONTAINER_CREATE
	};
	
	public final static String[] OutlookBarListeners = new String[]{
		OutlookBarListener.AFTER_ITEM_INIT, OutlookBarListener.BEFORE_ITEM_INIT, OutlookBarListener.AFTER_CLOSE_ITEM, OutlookBarListener.BEFORE_ACTIVED_CHANGE, OutlookBarListener.AFTER_ACTIVED_CHANGE
	};
	
	public final static String[] SelfDefListeners = new String[]{
		SelfDefListener.ON_SELF_DEF_EVENT, SelfDefListener.ON_CREATE_EVENT
	};
	
	public final static String[] TabListeners = new String[]{
		TabListener.AFTER_ITEM_INIT, TabListener.BEFORE_ITEM_INIT, TabListener.AFTER_CLOSE_ITEM, TabListener.BEFORE_ACTIVED_CHANGE, TabListener.AFTER_ACTIVED_CHANGE
	};
	
	public final static String[] TextListeners = new String[]{
		TextListener.ON_SELECT, TextListener.VALUE_CHANGED
	};
	
	public final static String[] KeyListeners = new String[]{
		KeyListener.ON_KEY_UP, KeyListener.ON_KEY_DOWN, KeyListener.ON_ENTER
	};
	
	public final static String[] ReferenceTextListeners = new String[]{
		ReferenceTextListener.BEFORE_OPEN_REF_PAGE
	};
	
	public final static String[] TreeNodeListeners_1 = new String[]{
		TreeNodeListener.ON_CLICK, TreeNodeListener.ON_DBCLICK,
		TreeNodeListener.ON_CHECKED, TreeNodeListener.ON_NODE_LOAD,TreeNodeListener.ON_NODE_DELETE,
		TreeNodeListener.BEFORE_SEL_NODE_CHANGE, TreeNodeListener.AFTER_SEL_NODE_CHANGE, TreeNodeListener.ROOT_NODE_CREATED, TreeNodeListener.NODE_CREATED, TreeNodeListener.BEFORE_NODE_CAPTION_CHANGE
	};
	
	public final static String[] TreeNodeListeners_2 = new String[]{
		TreeNodeListener.ON_DRAG_END, TreeNodeListener.ON_DRAG_START
	};
	
	public final static String[] TreeRowListeners = new String[]{
		TreeRowListener.BEFORE_NODE_CREATE
	};
	
	public final static String[] TreeContextMenuListeners = new String[]{
		TreeContextMenuListener.BEFORE_CONTEXT_MENU
	};
	
	public final static String[] DatasetListeners_1 = new String[]{
		DatasetListener.ON_AFTER_PAGE_CHANGE, DatasetListener.ON_AFTER_ROW_DELETE, DatasetListener.ON_AFTER_ROW_SELECT,
		DatasetListener.ON_AFTER_ROW_UN_SELECT, DatasetListener.ON_BEFORE_PAGE_CHANGE, DatasetListener.ON_BEFORE_ROW_DELETE, DatasetListener.ON_BEFORE_ROW_INSERT,
		DatasetListener.ON_BEFORE_ROW_SELECT
	};
	
	public final static String[] DatasetListeners_2 = new String[]{
		DatasetListener.ON_AFTER_ROW_INSERT
	};
	
	public final static String[] DatasetListeners_3 = new String[]{
		DatasetListener.ON_BEFORE_DATA_CHANGE, DatasetListener.ON_AFTER_DATA_CHANGE
	};
	
	public final static String[] DatasetListeners_4 = new String[]{
		DatasetListener.ON_DATA_LOAD
	};
	
	public final static String[] PageListeners = new String[]{
		PageListener.AFTER_PAGE_INIT, PageListener.BEFORE_ACTIVE, PageListener.ON_CLOSING, PageListener.ON_CLOSED
	};
	
	public static List<JsEventDesc> createAcceptEventDescs(IEventSupport element){
		List<JsEventDesc> descList = null;
		EventEntity[] eventEntities = null;
		if(element instanceof MenubarComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
		}else if(element instanceof ButtonComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
		}else if(element instanceof UICardLayout){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					CardListeners, CardEvent.class, "cardEvent", CardListener.class);
		}else if(element instanceof ContextMenuComp){
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					ContextMenuListeners, ContextMenuEvent.class, "contextMenuEvent", ContextMenuListener.class);
		}else if(element instanceof EditorComp){
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					FocusListeners, FocusEvent.class, "focusEvent", FocusListener.class);
		}else if(element instanceof ExcelComp){
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					ExcelListeners, ExcelEvent.class, "excelEvent", ExcelListener.class);
			eventEntities[2] = new EventEntity(
					ExcelRowListeners, ExcelRowEvent.class, "excelRowEvent", ExcelRowListener.class);
			eventEntities[3] = new EventEntity(
					ExcelCellListeners, ExcelCellEvent.class, "excelCellEvent", ExcelCellListener.class);
		}else if(element instanceof FileUploadComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					FileListeners, FileUploadEvent.class, "fileUploadEvent", FileListener.class);
		}else if(element instanceof FormComp){
			eventEntities = new EventEntity[3];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					AutoformListeners, AutoformEvent.class, "autoformEvent", AutoformListener.class);
			eventEntities[2] = new EventEntity(
					FocusListeners, FocusEvent.class, "focusEvent", FocusListener.class);
		}else if(element instanceof GridComp){
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					GridListeners, GridEvent.class, "gridEvent", GridListener.class);
			eventEntities[2] = new EventEntity(
					GridRowListeners, GridRowEvent.class, "gridRowEvent", GridRowListener.class);
			eventEntities[3] = new EventEntity(
					GridCellListeners, CellEvent.class, "gridCellEvent", GridCellListener.class);
		}else if(element instanceof IFrameComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
		}else if(element instanceof ImageComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
		}else if(element instanceof LabelComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
		}
		else if(element instanceof LfwWidget){
			eventEntities = new EventEntity[3];
			eventEntities[0] = new EventEntity(
					PluginListeners, null, "", null);
			eventEntities[1] = new EventEntity(
					DialogListeners, DialogEvent.class, "dialogEvent", DialogListener.class);
			eventEntities[2] = new EventEntity(
					ViewListeners, WidgetEvent.class, "widgetEvent", WidgetListener.class);
		}
		else if(element instanceof LinkComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					LinkListeners, LinkEvent.class, "linkEvent", LinkListener.class);
		}else if(element instanceof ListComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					ListListeners, ListEvent.class, "listEvent", ListListener.class);
		}else if(element instanceof MenubarComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
		}else if(element instanceof MenuItem){
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					ContainerListeners, ContainerEvent.class, "containerEvent", ContainerListener.class);
		}else if(element instanceof ModalDialogComp){
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					DialogListeners, DialogEvent.class, "simpleEvent", DialogListener.class);
		}
//		else if(element instanceof OutlookbarComp){
//			eventEntities = new EventEntity[1];
//			eventEntities[0] = new EventEntity(
//					OutlookBarListeners, SimpleEvent.class, "simpleEvent", OutlookBarListener.class);
//		}
		else if(element instanceof SelfDefComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					SelfDefListeners, SelfDefEvent.class, "selfDefEvent", SelfDefListener.class);
		}else if(element instanceof UITabComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					TabListeners, TabEvent.class, "tabEvent", TabListener.class);
		}else if(element instanceof CheckBoxComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					TextListeners, TextEvent.class, "textEvent", TextListener.class);
		}else if(element instanceof CheckboxGroupComp){
			eventEntities = new EventEntity[3];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(
					FocusListeners, FocusEvent.class, "focusEvent", FocusListener.class);
		}else if(element instanceof RadioComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
		}else if(element instanceof RadioGroupComp){
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(
					FocusListeners, FocusEvent.class, "focusEvent", FocusListener.class);
			eventEntities[3] = new EventEntity(
					TextListeners, TextEvent.class, "textEvent", TextListener.class);
		}else if(element instanceof ReferenceComp){
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(
					TextListeners, TextEvent.class, "textEvent", TextListener.class);
			eventEntities[3] = new EventEntity(
					ReferenceTextListeners, RefTextEvent.class, "refTextEvent", ReferenceTextListener.class);
		}else if(element instanceof SelfDefElementComp){
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(
					FocusListeners, FocusEvent.class, "focusEvent", FocusListener.class);
			eventEntities[1] = new EventEntity(
					KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
		}else if(element instanceof TextAreaComp){
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(
					FocusListeners, FocusEvent.class, "focusEvent", FocusListener.class);
			eventEntities[1] = new EventEntity(
					KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
		}else if(element instanceof TextComp){
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(
					FocusListeners, FocusEvent.class, "focusEvent", FocusListener.class);
			eventEntities[3] = new EventEntity(
					TextListeners, TextEvent.class, "textEvent", TextListener.class);
		}else if(element instanceof ToolBarComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
		}else if(element instanceof ToolBarItem){
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					ContextMenuListeners, ContextMenuEvent.class, "contextMenuEvent", ContextMenuListener.class);
		}else if(element instanceof TreeViewComp){
			eventEntities = new EventEntity[5];
			eventEntities[0] = new EventEntity(
					MouseListeners, MouseEvent.class, "mouseEvent", MouseListener.class);
			eventEntities[1] = new EventEntity(
					TreeNodeListeners_1, TreeNodeEvent.class, "treeNodeEvent", TreeNodeListener.class);
			eventEntities[2] = new EventEntity(
					TreeNodeListeners_2, TreeNodeDragEvent.class, "treeNodeDragEvent", TreeNodeListener.class);
			eventEntities[3] = new EventEntity(
					TreeRowListeners, TreeRowEvent.class, "treeRowEvent", TreeRowListener.class);
			eventEntities[4] = new EventEntity(
					TreeContextMenuListeners, TreeCtxMenuEvent.class, "treeCtxMenuEvent", TreeContextMenuListener.class);
		}else if(element instanceof Dataset){
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(
					DatasetListeners_1, DatasetEvent.class, "datasetEvent", DatasetListener.class);
			eventEntities[1] = new EventEntity(
					DatasetListeners_2, RowInsertEvent.class, "rowInsertEvent", DatasetListener.class);
			eventEntities[2] = new EventEntity(
					DatasetListeners_3, DatasetCellEvent.class, "datasetCellEvent", DatasetListener.class);
			eventEntities[3] = new EventEntity(
					DatasetListeners_4, DataLoadEvent.class, "dataLoadEvent", DatasetListener.class);
		}else if(element instanceof PageMeta){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					PageListeners, PageEvent.class, "pageEvent", PageListener.class);
		}else if(element instanceof Application){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					PageListeners, PageEvent.class, "pageEvent", PageListener.class);
		}
		else if(element instanceof UITabComp){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					TabListeners, TabEvent.class, "tabEvent", TabListener.class);
		}else if(element instanceof UIShutter){
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(
					OutlookBarListeners, SimpleEvent.class, "simpleEvent", OutlookBarListener.class);
		}
		
		if(eventEntities != null){
			descList = new ArrayList<JsEventDesc>();
			for(EventEntity ee : eventEntities){
				descList.addAll(ee.getEventDescs());
			}
		}
		return descList;
	}
	
//	public static List<JsEventDesc> getAcceptEventDescs(UIElement uiElement){
//		List<JsEventDesc> descList = null;
//		EventEntity[] eventEntities = null;
//		if(uiElement instanceof UITabComp){
//			eventEntities = new EventEntity[1];
//			eventEntities[0] = new EventEntity(
//					TabListeners, TabEvent.class, "tabEvent", TabListener.class);
//		}else if(uiElement instanceof UIShutter){
//			eventEntities = new EventEntity[1];
//			eventEntities[0] = new EventEntity(
//					OutlookBarListeners, SimpleEvent.class, "simpleEvent", OutlookBarListener.class);
//		}
//		if(eventEntities != null){
//			descList = new ArrayList<JsEventDesc>();
//			for(EventEntity ee : eventEntities){
//				descList.addAll(ee.getEventDescs());
//			}
//		}
//		return descList;
//	}
	
}

class EventEntity{
	
	private String[] events = null;
	private Class<?> eventClazz = null;
	private String param = null;
	private Class<?> jsEventClaszz = null;
	
	public EventEntity(String[] events, Class<?> eventClazz, String param, Class<?> jsEventClaszz){
		this.events = events;
		this.eventClazz = eventClazz;
		this.param = param;
		this.jsEventClaszz = jsEventClaszz;
	}
	
	public List<JsEventDesc> getEventDescs(){
		List<JsEventDesc> list = new ArrayList<JsEventDesc>();
		for(String event : events){
			JsEventDesc desc = new JsEventDesc(event, param);
			if(eventClazz != null){
				desc.setEventClazz(eventClazz.getName());
			}else{
				desc.setEventClazz("");
			}
			if(jsEventClaszz != null){
				desc.setJsEventClazz(jsEventClaszz.getName());
			}else{
				desc.setJsEventClazz("");
			}
			list.add(desc);
		}
		return list;
	}
}
