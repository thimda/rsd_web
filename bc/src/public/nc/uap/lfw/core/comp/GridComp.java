package nc.uap.lfw.core.comp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.GridContext;
import nc.uap.lfw.core.event.conf.GridCellListener;
import nc.uap.lfw.core.event.conf.GridListener;
import nc.uap.lfw.core.event.conf.GridRowListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwPluginException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;

/**
 * Grid控件后台对应的配置类
 *
 */
public class GridComp extends WebComponent implements IDataBinding  , IContainerComp<GridColumn>
{
	private static final long serialVersionUID = -525473184313225199L;
	private String dataset;
	//gird的headers列表
	private List<IGridColumn> columnList = null;
	// 是否为可编辑grid
	private boolean editable = true;
	// 是否显示固定选择列
	private boolean multiSelect = false;
	// grid单行高度
	private String rowHeight = null;
	// grid表头单行高度
	private String headerRowHeight = null;
	// 是否显示数字列
	private boolean showNumCol = false;
	// 是否显示合计行
	private boolean showSumRow = false;
	// 服务器端grid分页每页数目
	private String pageSize = null;
	// 分组的headerIds按照给定的顺序分组,各个header间逗号分隔
	private String groupColumns = null;
	// 整体是否可以排序
	private boolean sortable = true;
	// 是否显示表头
	private boolean showHeader = true;
	
	// 显示名称
	private String caption;
	
	// 显示列ID字符串，用“,”分割，中间不能有空格，用于在后台中向前台传递context
	private String showColumns;
	// 树表Level
	private GridTreeLevel topLevel = null;
	//row render类  行渲染
	private String rowRender; 
	//自定义 cellEditor方法   cellEditor 
	private String extendCellEditor;
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * 判断单双行类型，“0”为单双行各一行交错排列；“1”为单行1行，双行2行交错排列
	 */
	private String oddType = "1";
	public String getOddType() {
		return oddType;
	}

	public void setOddType(String oddType) {
		this.oddType = oddType;
	}

	public boolean isShowHeader() {
		return showHeader;
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	// 分页工具条是否在顶端
	private boolean pagenationTop = false;
	// 是否可显示“显示列”和“锁定列”菜单
	private boolean showColInfo = false;
	
	public GridComp() {
		super();
	}

	public GridComp(String id) {
		super(id);
	}

	public List<IGridColumn> getColumnList() 
	{
		return columnList;
	}
	
	public IGridColumn getColumnById(String id){
		Iterator<IGridColumn> it = columnList.iterator();
		while(it.hasNext()){
			IGridColumn column = it.next();
			if(((WebElement)column).getId().equals(id))
				return column;
			else if(column instanceof GridColumnGroup){
				GridColumnGroup gridColGroup = (GridColumnGroup) column;
				List<IGridColumn> childList = gridColGroup.getChildColumnList();
				for (int i = 0; i < childList.size(); i++) {
					IGridColumn col = (IGridColumn) childList.get(i);
					if(((GridColumn)col).getId().equals(id))
						return col;
				}
			}
		}
		return null;
	}
	
	public IGridColumn getColumn(int index){
		return columnList.get(index);
	}
	
	public IGridColumn getColumnByField(String field){
		Iterator<IGridColumn> it = columnList.iterator();
		while(it.hasNext()){
			IGridColumn column = it.next();
			if(column instanceof GridColumnGroup){
				GridColumnGroup gridColGroup = (GridColumnGroup) column;
				List<IGridColumn> childList = gridColGroup.getChildColumnList();
				for (int i = 0; i < childList.size(); i++) {
					IGridColumn col = (IGridColumn) childList.get(i);
					if(((GridColumn)col).getField().equals(field))
						return col;
				}
			}
			else if(((GridColumn)column).getField().equals(field))
				return column;
		}
		return null;
	}
	
	/**
	 * 设置表头列表
	 * @param columnList
	 */
	public void setColumnList(List<IGridColumn> columnList) 
	{
		this.columnList = columnList;
		if(columnList != null && columnList.size() > 0){
			Iterator<IGridColumn> it = columnList.iterator();
			while(it.hasNext()){
				IGridColumn col = it.next();
				col.setGridComp(this);
			}
		}
	}
	
	/**
	 * 增加表头
	 * @param col gridCoulumn
	 */
	public void addColumn(IGridColumn col){
		if(this.columnList == null){
			this.columnList = new ArrayList<IGridColumn>();
		}
		this.columnList.add(col);
		col.setGridComp(this);
	}
	
	public void addColumn(IGridColumn col, boolean withnotify){
		addColumn(col);
		if (withnotify)
			notify("addColumn", col);
	}
	
	public void addColumns(List<IGridColumn> columns, boolean withnotify){
		for (IGridColumn col : columns)
			addColumn(col);
		if (withnotify)
			notify("addColumns", columns);
	}	
	
	public void insertColumn(int index, IGridColumn col)
	{
		if(index < 0 || index > getColumnCount())
			index = getColumnCount();
		if(this.columnList == null)
		{
			this.columnList = new ArrayList<IGridColumn>();
		}
		col.setGridComp(this);
		this.columnList.add(index, col);
	}
	
	public int getColumnCount() {
		return this.columnList.size();
	}
	
	/**
	 * 移除表头列(只移除和ds field关联的列,即GridColumn类型的列)
	 * @param fieldId 该列所引用的ds的fieldId
	 */
	public void removeColumnByField(String fieldId){
		if(fieldId == null)
			return;
		if(this.columnList != null){
			for(int i = 0; i < this.columnList.size(); i++){
				if(this.columnList.get(i) instanceof GridColumn){
					GridColumn colum = (GridColumn)this.columnList.get(i);
					if(colum.getField() != null && colum.getField().equals(fieldId))
						this.columnList.remove(i);
				}
			}
		}
	}
	
	public void removeColumnByField(String fieldId, boolean withnotify){
		if(fieldId == null)
			return;
		IGridColumn col = getColumnById(fieldId);
		if (col == null)
			return;
		if (withnotify) 
			notify("delColumn", col);
		removeColumn(col);
	}
	
	public void removeColumn(IGridColumn col){
		columnList.remove(col);
	}

	public void removeColumn(IGridColumn col, boolean withnotify){
		columnList.remove(col);
		if (withnotify)
			notify("delColumn", col);
	}
	
	public void removeColumns(List<IGridColumn> columns, boolean withnotify){
		for (IGridColumn col : columns)
			columnList.remove(col);
		if (withnotify)
			notify("delColumns", columns);
	}
	
	public void setDataset(String datasetId) {
		this.dataset = datasetId;
	}
	
	public String getDataset() 
	{
		return dataset;
	}
//	
//	public String getLevelDataset() {
//		if (topLevel == null)
//			return null;
//		return topLevel.getDataset();
//	}
	
	public Object clone()
	{
		GridComp grid = (GridComp) super.clone();
		if(this.columnList != null)
		{
			grid.columnList = new ArrayList<IGridColumn>();
			Iterator<IGridColumn> it = this.columnList.iterator();
			while(it.hasNext())
				grid.addColumn((IGridColumn) it.next().clone());
		}
		if (this.topLevel != null){
			grid.setTopLevel((GridTreeLevel) this.topLevel.clone());
		}
		return grid;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		if (editable != this.editable) {
			this.editable = editable;
			setCtxChanged(true);
		}
	}

	public boolean isMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	public String getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(String rowHeight) {
		this.rowHeight = rowHeight;
	}

	public String getHeaderRowHeight() {
		return headerRowHeight;
	}

	public void setHeaderRowHeight(String headerRowHeight) {
		this.headerRowHeight = headerRowHeight;
	}

	public boolean isShowNumCol() {
		return showNumCol;
	}

	public void setShowNumCol(boolean isShowNumCol) {
		this.showNumCol = isShowNumCol;
	}

	public boolean isShowSumRow() {
		return showSumRow;
	}

	public void setShowSumRow(boolean showSumRow) {
		this.showSumRow = showSumRow;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
//
//	public String getPageClientSize() {
//		return pageClientSize;
//	}
//
//	public void setPageClientSize(String pageClientSize) {
//		this.pageClientSize = pageClientSize;
//	}

	public String getShowColumns() {
		return showColumns;
	}

	public void setShowColumns(String showColumns) {
		this.showColumns = showColumns;
		setCtxChanged(true);
		addCtxChangedProperty("showColumns");
	}
	
	
	public List<GridColumn> filterColumns(String name) {
		List<GridColumn> eleList = new ArrayList<GridColumn>();
		Iterator<IGridColumn> it = columnList.iterator();
		while(it.hasNext()){
			IGridColumn ele = it.next();
			if(ele instanceof GridColumn){
				GridColumn col = (GridColumn) ele;
				if(col.getId().startsWith(name))
					eleList.add(col);
			}
		}
		return eleList;
	}
	
	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(GridListener.class);
		list.add(GridRowListener.class);
		list.add(GridCellListener.class);
		return list;
	}
	
	/**
	 * 合并属性。注意此合并算法仅仅适用于从无分级的单据模板到有分级的增量配置文件。
	 * 如果今后支持其它方式，此算法应该做适当改进。
	 */
	public void mergeProperties(WebElement ele) {
		super.mergeProperties(ele);
		//合并基本属性
		GridComp grid = (GridComp) ele;

		String dataset = grid.getDataset();
		if(dataset != null)
			this.setDataset(dataset);
		
		String rowHeight = grid.getHeaderRowHeight();
		if(rowHeight != null)
			this.setRowHeight(rowHeight);
		
//		String pageClientSize = grid.getPageClientSize();
//		if(pageClientSize != null)
//			this.setPageClientSize(pageClientSize);
		
		String pageSize = grid.getPageSize();
		if(pageSize != null)
			this.setPageSize(pageSize);
		
		String groupColumns = grid.getGroupColumns();
		if(groupColumns != null)
			this.setGroupColumns(groupColumns);
		
		boolean sortable = grid.isSortable();
		if(sortable == false)
			this.setSortable(sortable);
		
		//合并列。
		List<IGridColumn> tmpColumnList = grid.getColumnList();
		if(tmpColumnList != null){
			Iterator<IGridColumn> it = tmpColumnList.iterator();
			while(it.hasNext()){
				IGridColumn column = it.next();
				//如果待和并列是组
				if(column instanceof GridColumnGroup){
					GridColumnGroup group = (GridColumnGroup) column;
					//是增加的组，则添加，并递归处理此组下的子列
					if(group.getConfType().equals(WebElement.CONF_ADD)){
						//深度克隆此组
						GridColumnGroup clonedGroup = (GridColumnGroup) group.clone();
						//递归处理组
						mergeGroup(clonedGroup);
						//将处理过的组添加到列表中
						if(clonedGroup.getConfPos() == -1)
							this.columnList.add(clonedGroup);
						else
							this.columnList.add(clonedGroup.getConfPos(), clonedGroup);
					}
					//不会有引用和删除情况
//					else if(group.getConfType().equals(WebElement.CONF_REF)){
//						//GridColumnGroup currGroup = this.columnList.get
//						//TODO
//					}
				}
				else
				{
					GridColumn tmpColumn = (GridColumn) column;
					if(tmpColumn.getConfType().equals(WebElement.CONF_ADD)){
						this.columnList.add((IGridColumn) tmpColumn.clone());
					}
					else if(tmpColumn.getConfType().equals(WebElement.CONF_DEL)){
						IGridColumn currColumn = this.getColumnById(tmpColumn.getId());
						if(currColumn == null){
							//logger.warn("没有从列表中找到待删除的列:" + tmpColumn.getId());
							return;
						}
						
						this.columnList.remove(currColumn);
					}
					else if(tmpColumn.getConfType().equals(WebElement.CONF_REF)){
						GridColumn currColumn = (GridColumn) this.getColumnById(tmpColumn.getId());
						if(currColumn == null){
							//logger.warn("没有从列表中找到待修改的列:" + tmpColumn.getId());
							return;
						}
						currColumn.mergeProperties(tmpColumn);
						if(tmpColumn.getConfPos() != -1){
							this.columnList.remove(currColumn);
							this.columnList.add(tmpColumn.getConfPos(), currColumn);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 处理组的递归方法
	 * @param group
	 */
	private void mergeGroup(GridColumnGroup group) {
		//获取当前组的子列表
		List<IGridColumn> childColumnList = group.getChildColumnList();
		if(childColumnList != null){
			Iterator<IGridColumn> it = childColumnList.iterator();
			//暂存提取出的列
			List<GridColumn> tempList = new ArrayList<GridColumn>();
			while(it.hasNext()){
				IGridColumn column = it.next();
				//如果是组
				if(column instanceof GridColumnGroup){
					GridColumnGroup childGroup = (GridColumnGroup) column;
					//是新加组
					if(childGroup.getConfType().equals(WebElement.CONF_ADD)){
						mergeGroup(childGroup);
					}
					//不会有删除情况
//					else if(childGroup.getConfType().equals(WebElement.CONF_DEL)){
//						mergeGroup(childGroup);
//						boolean removed = this.columnList.remove(childGroup.getId());
//						if(!removed)
//							logger.warn("没有从列表中找到待删除的列组：" + childGroup.getId());
//					}
					//不会有ref情况
//					else if(childGroup.getConfType().equals(WebElement.CONF_REF)){
//						mergeGroup(childGroup);
//						GridColumnGroup currGroup = (GridColumnGroup) this.getColumnById(childGroup.getId());
//						if(currGroup == null){
//							logger.warn("没有从列表中找到待修改的列组：" + childGroup.getId());
//							return;
//						}
//						currGroup.mergeProperties(childGroup);
//					}
				}
				else{
					GridColumn childColumn = (GridColumn) column;
					//TODO 删除此group下对应的column
					if(childColumn.getConfType().equals(WebElement.CONF_DEL)){
						//从当前组下删除column
						it.remove();
						//从原有配置中删除column
						IGridColumn currColumn = this.getColumnById(childColumn.getId());
						if(currColumn != null)
							this.columnList.remove(currColumn);
					}
					else if(childColumn.getConfType().equals(WebElement.CONF_REF)){
						//从当前group中删除列
						it.remove();
						//获取原列信息
						GridColumn sourceColumn = (GridColumn) this.getColumnById(childColumn.getId());
						//从原列中删除此列
						this.columnList.remove(sourceColumn);
						//补充信息
						sourceColumn.mergeProperties(childColumn);
						//childColumn.mergeProperties(ele)
						tempList.add(sourceColumn);
					}
					//增加不做事
				}
			}
			Iterator<GridColumn> colIt = tempList.iterator();
		
			while(colIt.hasNext()){
				GridColumn column = colIt.next();
				if(column.getConfPos() != -1){
					if(column.getConfPos() >= group.getChildColumnList().size())
						group.getChildColumnList().add(column);
					else
						group.getChildColumnList().add(column.getConfPos(), column);
				}
				else
					group.getChildColumnList().add(column);
			}
		}
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public String getGroupColumns() {
		return groupColumns;
	}

	public void setGroupColumns(String groupColumns) {
		this.groupColumns = groupColumns;
	}

	public boolean isPagenationTop() {
		return pagenationTop;
	}

	public void setPagenationTop(boolean pagenationTop) {
		this.pagenationTop = pagenationTop;
	}
	
	public void validate(){
		StringBuffer buffer = new StringBuffer();
		if(this.getId() == null || this.getId().equals("")){
			buffer.append("表格的ID不能为空!\r\n");
		}
		if(this.getDataset() == null || this.getDataset().equals("")){
			buffer.append("表格引用的数据集不能为空!\r\n");
		}
		if(buffer.length() > 0)
			throw new  LfwPluginException(buffer.toString());
	}

	@Override
	public BaseContext getContext() {
		GridContext ctx = new GridContext();
		ctx.setEnabled(this.enabled);
		ctx.setEditable(this.editable);
		if (checkCtxPropertyChanged("showColumns"))
			ctx.setShowColumns(this.showColumns);
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		GridContext gridctx = (GridContext) ctx;
		if (gridctx.getId() != null && !"".equals(gridctx.getId()))
			this.setId(gridctx.getId());
		this.setEditable(gridctx.isEditable());
		this.setEnabled(gridctx.isEnabled());
		this.setShowColumns(gridctx.getShowColumns());
		this.setCtxChanged(false);
	}

	public boolean isShowColInfo() {
		return showColInfo;
	}

	public void setShowColInfo(boolean showColInfo) {
		this.showColInfo = showColInfo;
	}

	public GridTreeLevel getTopLevel() {
		return topLevel;
	}

	public void setTopLevel(GridTreeLevel topLevel) {
		this.topLevel = topLevel;
	}

	public String getRowRender() {
		return rowRender;
	}

	public void setRowRender(String rowRender) {
		this.rowRender = rowRender;
	}
	
	public String getExtendCellEditor() {
		return extendCellEditor;
	}

	public void setExtendCellEditor(String extendCellEditor) {
		this.extendCellEditor = extendCellEditor;
	}

	public void notify(String type, Object obj){
		if(LifeCyclePhase.ajax.equals(getPhase())){			
//			try{
			Map<String,Object> map = new HashMap<String,Object>();
			String widgetId = this.getWidget().getId();
			map.put("widgetId", widgetId);
			map.put("gridId", this.getId());
			map.put("type", type);
			if (type.equals("addColumn") || type.equals("delColumn"))
				map.put("column", obj);
			else if (type.equals("addColumns") || type.equals("delColumns"))
				map.put("columns", obj);
			
			this.notifyChange(UIElement.UPDATE, map);
//				UIMeta uiMeta = (UIMeta) LfwRuntimeEnvironment.getWebContext().getUIMeta();
//				UIElement uiGridComp = null;//uiMeta.findChildById(this.getId());
//				Class c = Class.forName("nc.uap.lfw.ra.render.pc.PCGridCompRender");
//				Object instance = c.newInstance();
//				Method m = c.getMethod("notifyUpdate", new Class[]{UIMeta.class, PageMeta .class, Object.class});
//				m.invoke(instance, new Object[]{null, null, map});
//			}
			//因此类在插件中用到，插件中无法包含日志工具。
//			catch(Throwable e){
//				throw new LfwRuntimeException(e.getMessage());
//			}
		}
//		return null;
	}

	@Override
	public GridColumn getElementById(String id) {
		IGridColumn gc = getColumnByField(id);
		if(gc instanceof GridColumn){
			return (GridColumn) gc;
		}
		return null;
	}
	
}

