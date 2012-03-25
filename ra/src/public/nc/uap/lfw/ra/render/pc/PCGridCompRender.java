package nc.uap.lfw.ra.render.pc;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.conf.GridCellListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.tags.GridModelUtil;
import nc.uap.lfw.jsp.uimeta.UIConstant;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh �����Ⱦ��
 * @param <T>
 * @param <K>
 */
public class PCGridCompRender extends UINormalComponentRender<UIGridComp, GridComp> {

	private static final String GRIDCOLUMN_VISIBLE_SCRIPT = "gridcolumn_visible_script";
	private static final String GRIDCOLUMN_EDITABLE_SCRIPT = "gridcolumn_editable_script";
	private static final String GRIDCOLUMN_EDITABLE_INDEX = "gridcolumn_editable_index";
	private static final String GRIDCOLUMN_PRECISION_INDEX = "gridcolumn_precision_index";
	
	public PCGridCompRender(UIGridComp uiEle, GridComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}

	public String generateBodyHtml() {

		return super.generateBodyHtml();
	}
	@Override
	public String generateBodyHtmlDynamic() {

		return super.generateBodyHtmlDynamic();
	}

	private String generateBodyScript(boolean isDynamic) {
		GridComp grid = this.getWebElement();
		UIGridComp uiComp = this.getUiElement();
		
		//���û������Ĭ����չ�У��������һ��Ϊ��չ��
		if(uiComp.getAutoExpand().equals(UIConstant.TRUE))
			setAutoExpand(grid);
		
		Dataset ds = this.getDataset();

		StringBuffer buf = new StringBuffer();
		
		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		buf.append("var ").append(widget).append(" = pageUI.getWidget('"+this.getCurrWidget().getId()+"');\n");
		
		String gridId = getVarId();
		buf.append("var ").append(gridId);
		
		buf.append(" = new GridComp(document.getElementById(\"");
		buf.append(getDivId()).append("\"),\"");
		
		buf.append(grid.getId()).append("\",\"0\",\"0\",\"100%\",\"100%\",\"relative\",");
		buf.append(grid.isEditable()).append(",");
		buf.append(grid.isMultiSelect()).append(",");
		buf.append(grid.isShowNumCol()).append(",");
		buf.append(grid.isShowSumRow()).append(",");
		buf.append("{");
		if(ds.getPageSize() != -1)
			buf.append("pageSize:").append(ds.getPageSize()).append(",");
		buf.append("flowmode:" + isFlowMode() + "},");
		if (grid.getGroupColumns() == null || grid.getGroupColumns().equals(""))
			buf.append("null");
		else {
			buf.append("'" + grid.getGroupColumns() + "'");
		}
		buf.append(",").append(grid.isSortable()).append(",'");
		
		String className = uiComp.getClassName();
		buf.append(className == null ? "" : className).append("',");
		buf.append(grid.isPagenationTop()).append(",");
		buf.append(grid.isShowColInfo()).append(",'");
		buf.append(grid.getOddType()).append("',");
		if (grid.getGroupColumns() != null && grid.getGroupColumns().length() > 0)
			buf.append(true);
		else {
			buf.append(false);
		}

		buf.append(",").append(grid.isShowHeader()).append(",");
		buf.append(grid.getExtendCellEditor()).append(",");
		buf.append(grid.getRowRender()).append(");\n");
		
		buf.append(gridId + ".widget=" + widget + ";\n");
		buf.append(widget + ".addComponent(" + gridId + ");\n");

		// �����и�
		String rowHeight = grid.getRowHeight();
		if (rowHeight != null && rowHeight != "")
			buf.append(gridId + ".setRowHeight(" + rowHeight + ");\n");
		// ���ñ�ͷ�и�
		String headerRowHeight = grid.getHeaderRowHeight();
		if (headerRowHeight != null && headerRowHeight != "")
			buf.append(gridId + ".setHeaderRowHeight(" + headerRowHeight + ");\n");

		String modelStr = GridModelUtil.generateGridModel(ds, grid, getCurrWidget());
		buf.append(modelStr);
		buf.append(gridId + ".setModel(model);\n");

		return buf.toString();
	}
	
	private void setAutoExpand(GridComp grid) {
		Iterator<IGridColumn> it = grid.getColumnList().iterator();
		GridColumn lastColumn = null;
		while(it.hasNext()){
			IGridColumn col = it.next();
			if(col instanceof GridColumn){
				if(((GridColumn) col).isVisible()){
					lastColumn = (GridColumn)col;
					if(lastColumn.isAutoExpand()){
						break;
					}
				}
			}
			//���һ��������û��autoexpand
			if(!it.hasNext())
				lastColumn.setAutoExpand(true);
		}
	}

	public String generateBodyScript(){
		return this.generateBodyScript(false);
	}
	

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_GRID;
	}

	protected void addProxyParam(StringBuffer buf, String eventName) {
		if (GridCellListener.AFTER_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', cellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', cellEvent.colIndex);\n");
			buf.append("proxy.addParam('newValue', cellEvent.newValue);\n");
			buf.append("proxy.addParam('oldValue', cellEvent.oldValue);\n");
		} else if (GridCellListener.BEFORE_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', gridCellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', gridCellEvent.colIndex);\n");
		} else if (GridCellListener.ON_CELL_CLICK.equals(eventName) || GridCellListener.CELL_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', cellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', cellEvent.colIndex);\n");
		} else if (GridCellListener.CELL_VALUE_CHANGED.equals(eventName)){
				buf.append("proxy.addParam('rowIndex', gridCellEvent.rowIndex);\n");
				buf.append("proxy.addParam('colIndex', gridCellEvent.colIndex);\n");
		}
	}
	
	// ɾ�������У���ͷ����
	@Override
	public void notifyRemoveChild(UIMeta uimeta, PageMeta pagemeta,Object obj) {
		if (obj instanceof GridColumn) { // ���������
			GridColumn column = (GridColumn) obj;		
			GridComp grid = column.getGridComp();
			StringBuffer buf = new StringBuffer();
			String widget = grid.getWidget() != null ? grid.getWidget().getId() : this.getWidget();			
			buf.append("window.execDynamicScript2RemoveGridColumn('" + widget + "','" + grid.getId() + "','"+column.getField()+"');");
			grid.removeColumn(column);
			addDynamicScript(buf.toString());
		}
	}
	
	//ɾ�� grid�ؼ�
	@Override
	public void notifyDestroy(UIMeta uimeta, PageMeta pagemeta,Object obj) {
		super.notifyDestroy(uimeta, pagemeta, obj);
	}

	@Override
	public void notifyAddChild(UIMeta uiMeta, PageMeta pageMeta, Object obj) {
		super.notifyAddChild(uiMeta, pageMeta, obj);
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj){
		if (obj instanceof Map){
			Map<String,Object> map = (Map<String,Object>)obj;
			String widgetId = (String)map.get("widgetId");
			LfwWidget widget = getPageMeta().getWidget(widgetId);
			String gridId = (String)map.get("gridId");
			GridComp grid = (GridComp)widget.getViewComponents().getComponent(gridId);
			String type = (String)map.get("type");
			if (type != null && type.equals("addColumn")){
				IGridColumn column = (IGridColumn)map.get("column");
				String script = this.addGridColumnScript(widget, grid, column);			
				this.addDynamicScript(script);
			}
			else if (type != null && type.equals("addColumns")){
				List<IGridColumn> columns = (List<IGridColumn>)map.get("columns");
				String script = this.addGridColumnsScript(widget, grid, columns);
				this.addDynamicScript(script);
			}
			else if (type != null && type.equals("delColumn")){
				IGridColumn column = (IGridColumn)map.get("column");
				String script = this.delGridColumnScript(widget, grid, column);
				this.addDynamicScript(script);
			}
			else if (type != null && type.equals("delColumns")){
				List<IGridColumn> columns = (List<IGridColumn>)map.get("columns");
				String script = this.delGridColumnsScript(widget, grid, columns);
				this.addDynamicScript(script);
			}
			else if (type != null && type.equals(GridColumn.COLUMN_EDITABLE)){
				GridColumn column = (GridColumn)map.get("column");
				this.columnEditableScript(widget, grid, column);
			}
			else if (type != null && type.equals(GridColumn.COLUMN_VISIBLE)){
				GridColumn column = (GridColumn)map.get("column");
				this.columnVisibleScript(widget, grid, column);
			}
			else if (type != null && type.equals(GridColumn.COLUMN_PRECISION)){
				GridColumn column = (GridColumn)map.get("column");
				this.columnPrecisionScript(widget, grid, column);
			}
		}
	}
	
//	public void notifyAddColumn(UIMeta uiMeta, PageMeta pageMeta, Object obj){
//		if(obj instanceof Map){
//			Map<String,Object> map = (Map<String,Object>)obj;
//			String widgetId = (String)map.get("widgetId");
//			LfwWidget widget = getPageMeta().getWidget(widgetId);
//			String gridId = (String)map.get("gridId");
//			IGridColumn column = (IGridColumn)map.get("column");
//			GridComp grid = (GridComp)widget.getViewComponents().getComponent(gridId);
//			String script = this.addGridColumnScript(widget, grid, column);			
//			this.addDynamicScript(script);
//		}
//		
//	}
	/**
	 * ��̬����column
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 * @return
	 */
	private String addGridColumnScript(LfwWidget widget, GridComp grid,
			IGridColumn column) {
		StringBuffer buf = new StringBuffer();
		buf.append("var gridcomp = pageUI.getWidget('"+ widget.getId() +"').getComponent('"+ grid.getId() + "');\n");
		buf.append("var model = gridcomp.model;\n");
		Dataset ds = widget.getViewModels().getDataset(grid.getDataset());
		buf.append(GridModelUtil.generateGridColumn(column, ds, widget, "model"));
		buf.append("model.initBasicHeaders();\n");
		buf.append("gridcomp.setModel(model);\n");
		return buf.toString();
	}

//	public void notifyAddColumns(UIMeta uiMeta, PageMeta pageMeta, Object obj){
//		if(obj instanceof Map){
//			Map<String,Object> map = (Map<String,Object>)obj;
//			String widgetId = (String)map.get("widgetId");
//			LfwWidget widget = getPageMeta().getWidget(widgetId);
//			String gridId = (String)map.get("gridId");
//			List<IGridColumn> columns = (List<IGridColumn>)map.get("columns");
//			GridComp grid = (GridComp)widget.getViewComponents().getComponent(gridId);
//			String script = this.addGridColumnsScript(widget, grid, columns);			
//			this.addDynamicScript(script);
//		}
//		
//	}
	/**
	 * ��̬����columns
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 * @return
	 */
	private String addGridColumnsScript(LfwWidget widget, GridComp grid,
			List<IGridColumn> columns) {
		StringBuffer buf = new StringBuffer();
		buf.append("var gridcomp = pageUI.getWidget('"+ widget.getId() +"').getComponent('"+ grid.getId() + "');\n");
		buf.append("var model = gridcomp.model;\n");
		Dataset ds = widget.getViewModels().getDataset(grid.getDataset());
		for (IGridColumn col : columns)
			buf.append(GridModelUtil.generateGridColumn(col, ds, widget, "model"));
		buf.append("model.initBasicHeaders();\n");
		buf.append("gridcomp.setModel(model);\n");
		return buf.toString();
	}
//	
//	/**
//	 * ɾ��column
//	 * @param uiMeta
//	 * @param pageMeta
//	 * @param obj
//	 */
//	public void notifyDelColumn(UIMeta uiMeta, PageMeta pageMeta, Object obj){
//		if(obj instanceof Map){
//			Map<String,Object> map = (Map<String,Object>)obj;
//			String widgetId = (String)map.get("widgetId");
//			LfwWidget widget = getPageMeta().getWidget(widgetId);
//			String gridId = (String)map.get("gridId");
//			IGridColumn column = (IGridColumn)map.get("columns");
//			GridComp grid = (GridComp)widget.getViewComponents().getComponent(gridId);
//			String script = this.delGridColumnScript(widget, grid, column);			
//			this.addDynamicScript(script);
//		}
//		
//	}
	/**
	 * ��̬ɾ��column
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 * @return
	 */
	private String delGridColumnScript(LfwWidget widget, GridComp grid,
			IGridColumn column) {
		StringBuffer buf = new StringBuffer();
		buf.append("var gridcomp = pageUI.getWidget('"+ widget.getId() +"').getComponent('"+ grid.getId() + "');\n");
		buf.append("var model = gridcomp.model;\n");
		if (column instanceof GridColumn){
			buf.append("model.removeHeader('" + ((GridColumn)column).getId() + "');\n");
		}
		else if (column instanceof GridColumnGroup){
			buf.append("model.removeHeader('" + ((GridColumnGroup) column).getId() + "');\n");
//			delGridColumnGroup((GridColumnGroup)column, buf);
		}
		buf.append("model.initBasicHeaders();\n");
		buf.append("gridcomp.setModel(model);\n");
		return buf.toString();
	}
	
//	/**
//	 * ɾ��columns
//	 * @param uiMeta
//	 * @param pageMeta
//	 * @param obj
//	 */
//	public void notifyDelColumns(UIMeta uiMeta, PageMeta pageMeta, Object obj){
//		if(obj instanceof Map){
//			Map<String,Object> map = (Map<String,Object>)obj;
//			String widgetId = (String)map.get("widgetId");
//			LfwWidget widget = getPageMeta().getWidget(widgetId);
//			String gridId = (String)map.get("gridId");
//			List<IGridColumn> columns = (List<IGridColumn>)map.get("columns");
//			GridComp grid = (GridComp)widget.getViewComponents().getComponent(gridId);
//			String script = this.delGridColumnsScript(widget, grid, columns);			
//			this.addDynamicScript(script);
//		}
//		
//	}
	/**
	 * ��̬ɾ��columns
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 * @return
	 */
	private String delGridColumnsScript(LfwWidget widget, GridComp grid,
			List<IGridColumn> columns) {
		StringBuffer buf = new StringBuffer();
		buf.append("var gridcomp = pageUI.getWidget('"+ widget.getId() +"').getComponent('"+ grid.getId() + "');\n");
		buf.append("var model = gridcomp.model;\n");
		for (IGridColumn col : columns){
			if (col instanceof GridColumn){
				buf.append("model.removeHeader('" + ((GridColumn)col).getId() + "');\n");
			}
			else if (col instanceof GridColumnGroup){
				buf.append("model.removeHeader('" + ((GridColumnGroup) col).getId() + "');\n");
//			delGridColumnGroup((GridColumnGroup)column, buf);
			}
		}
		buf.append("model.initBasicHeaders();\n");
		buf.append("gridcomp.setModel(model);\n");
		return buf.toString();
	}
	
//	private void delGridColumnGroup(GridColumnGroup colGroup, StringBuffer buf){
//		List<IGridColumn> childColumnList = colGroup.getChildColumnList();
//		for (IGridColumn column : childColumnList){
//			if (column instanceof GridColumn){
//				buf.append("gridcomp.removeHeader('" + ((GridColumn)column).getId() + "');\n");
//			}
//			else if (column instanceof GridColumnGroup){
//				delGridColumnGroup((GridColumnGroup)column, buf);
//			}	
//		}
//	}
	
	/**
	 * ����column��ʾ����
	 * 
	 */
	private void columnVisibleScript(LfwWidget widget, GridComp grid, GridColumn column){
		ApplicationContext appCtx = AppLifeCycleContext.current().getApplicationContext();
		
		String scriptId = GRIDCOLUMN_VISIBLE_SCRIPT + grid.getId();
		
		if (appCtx.getExecScript() != null){
			for (int i = 0; i < appCtx.getExecScript().size(); i++){
				if (appCtx.getExecScript().get(i).startsWith("/*" + scriptId + "*/")){
					appCtx.removeExecScript(i);
					break;
				}
			}
		}
		String script = null;
		String visibleScript = (String) appCtx.getAppAttribute(scriptId);
		if (visibleScript == null || visibleScript.equals("")){
			visibleScript = "[]";
		}
		//���֮ǰ�ĶԸ��е����� 
		visibleScript = visibleScript.replace(",\"" + column.getId() + ":" +"true\"", "");
		visibleScript = visibleScript.replace(",\"" + column.getId() + ":" +"false\"", "");
		visibleScript = visibleScript.replace("\"" + column.getId() + ":" +"true\"", "");
		visibleScript = visibleScript.replace("\"" + column.getId() + ":" +"false\"", "");
		
		if(column.isVisible())
			visibleScript = visibleScript.replace("]", ",\"" + column.getId() + ":" +"true\"]"); 
		else
			visibleScript = visibleScript.replace("]", ",\"" + column.getId() + ":" +"false\"]");
		visibleScript = visibleScript.replace("[,", "[");
		script = "/*" + scriptId + "*/pageUI.getWidget('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnVisible(" + visibleScript + ")";
		appCtx.addAppAttribute(scriptId, visibleScript);
		appCtx.addExecScript(script);
	}
	
	/**
	 * ����columnֻ��
	 * 
	 */
	private void columnEditableScript(LfwWidget widget, GridComp grid, GridColumn column){
		ApplicationContext appCtx = AppLifeCycleContext.current().getApplicationContext();
		
		String scriptId = GRIDCOLUMN_EDITABLE_SCRIPT + grid.getId();
		if (appCtx.getExecScript() != null){
			for (int i = 0; i < appCtx.getExecScript().size(); i++){
				if (appCtx.getExecScript().get(i).startsWith("/*" + scriptId + "*/")){
					appCtx.removeExecScript(i);
					break;
				}
			}
		}	
		String script = null;
		String editableScript = (String) appCtx.getAppAttribute(scriptId);
		if (editableScript == null || editableScript.equals("")){
			editableScript = "[]";
		}
		//���֮ǰ�ĶԸ��е����� 
		editableScript = editableScript.replace(",\"" + column.getId() + ":" +"true\"", "");
		editableScript = editableScript.replace(",\"" + column.getId() + ":" +"false\"", "");
		editableScript = editableScript.replace("\"" + column.getId() + ":" +"true\"", "");
		editableScript = editableScript.replace("\"" + column.getId() + ":" +"false\"", "");
		
		if(column.isEditable()){
			editableScript = editableScript.replace("]", ",\"" + column.getId() + ":" +"true\"]"); 
		}
		else{
			editableScript = editableScript.replace("]", ",\"" + column.getId() + ":" +"false\"]");
		}
		editableScript = editableScript.replace("[,", "[");
		script = "/*" + scriptId + "*/pageUI.getWidget('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnEditable(" + editableScript + ")";
		appCtx.addAppAttribute(GRIDCOLUMN_EDITABLE_SCRIPT + grid.getId(), editableScript);
		appCtx.addAppAttribute(scriptId, editableScript);
		appCtx.addExecScript(script);
	}
	
	/**
	 * ����column����
	 * 
	 */
	private void columnPrecisionScript(LfwWidget widget, GridComp grid, GridColumn column){
		ApplicationContext appCtx = AppLifeCycleContext.current().getApplicationContext();
		Integer index = (Integer) appCtx.getAppAttribute(GRIDCOLUMN_PRECISION_INDEX + grid.getId() + "_" + column.getId());
		
		if(index != null){
			appCtx.removeExecScript(index);
		}
		String script = "try{ pageUI.getWidget('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').getBasicHeaderById('" + column.getId() + "').setPrecision(" + column.getPrecision() + ") }catch(e){};";
		index = appCtx.addExecScript(script);
		appCtx.addAppAttribute(GRIDCOLUMN_PRECISION_INDEX + grid.getId() + "_"  + column.getId(), index);
	}
}
