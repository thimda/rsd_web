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
 * Grid�ؼ���̨��Ӧ��������
 *
 */
public class GridComp extends WebComponent implements IDataBinding  , IContainerComp<GridColumn>
{
	private static final long serialVersionUID = -525473184313225199L;
	private String dataset;
	//gird��headers�б�
	private List<IGridColumn> columnList = null;
	// �Ƿ�Ϊ�ɱ༭grid
	private boolean editable = true;
	// �Ƿ���ʾ�̶�ѡ����
	private boolean multiSelect = false;
	// grid���и߶�
	private String rowHeight = null;
	// grid��ͷ���и߶�
	private String headerRowHeight = null;
	// �Ƿ���ʾ������
	private boolean showNumCol = false;
	// �Ƿ���ʾ�ϼ���
	private boolean showSumRow = false;
	// ��������grid��ҳÿҳ��Ŀ
	private String pageSize = null;
	// �����headerIds���ո�����˳�����,����header�䶺�ŷָ�
	private String groupColumns = null;
	// �����Ƿ��������
	private boolean sortable = true;
	// �Ƿ���ʾ��ͷ
	private boolean showHeader = true;
	
	// ��ʾ����
	private String caption;
	
	// ��ʾ��ID�ַ������á�,���ָ�м䲻���пո������ں�̨����ǰ̨����context
	private String showColumns;
	// ����Level
	private GridTreeLevel topLevel = null;
	//row render��  ����Ⱦ
	private String rowRender; 
	//�Զ��� cellEditor����   cellEditor 
	private String extendCellEditor;
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * �жϵ�˫�����ͣ���0��Ϊ��˫�и�һ�н������У���1��Ϊ����1�У�˫��2�н�������
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

	// ��ҳ�������Ƿ��ڶ���
	private boolean pagenationTop = false;
	// �Ƿ����ʾ����ʾ�С��͡������С��˵�
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
	 * ���ñ�ͷ�б�
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
	 * ���ӱ�ͷ
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
	 * �Ƴ���ͷ��(ֻ�Ƴ���ds field��������,��GridColumn���͵���)
	 * @param fieldId ���������õ�ds��fieldId
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
	 * �ϲ����ԡ�ע��˺ϲ��㷨���������ڴ��޷ּ��ĵ���ģ�嵽�зּ������������ļ���
	 * ������֧��������ʽ�����㷨Ӧ�����ʵ��Ľ���
	 */
	public void mergeProperties(WebElement ele) {
		super.mergeProperties(ele);
		//�ϲ���������
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
		
		//�ϲ��С�
		List<IGridColumn> tmpColumnList = grid.getColumnList();
		if(tmpColumnList != null){
			Iterator<IGridColumn> it = tmpColumnList.iterator();
			while(it.hasNext()){
				IGridColumn column = it.next();
				//������Ͳ�������
				if(column instanceof GridColumnGroup){
					GridColumnGroup group = (GridColumnGroup) column;
					//�����ӵ��飬����ӣ����ݹ鴦������µ�����
					if(group.getConfType().equals(WebElement.CONF_ADD)){
						//��ȿ�¡����
						GridColumnGroup clonedGroup = (GridColumnGroup) group.clone();
						//�ݹ鴦����
						mergeGroup(clonedGroup);
						//�������������ӵ��б���
						if(clonedGroup.getConfPos() == -1)
							this.columnList.add(clonedGroup);
						else
							this.columnList.add(clonedGroup.getConfPos(), clonedGroup);
					}
					//���������ú�ɾ�����
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
							//logger.warn("û�д��б����ҵ���ɾ������:" + tmpColumn.getId());
							return;
						}
						
						this.columnList.remove(currColumn);
					}
					else if(tmpColumn.getConfType().equals(WebElement.CONF_REF)){
						GridColumn currColumn = (GridColumn) this.getColumnById(tmpColumn.getId());
						if(currColumn == null){
							//logger.warn("û�д��б����ҵ����޸ĵ���:" + tmpColumn.getId());
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
	 * ������ĵݹ鷽��
	 * @param group
	 */
	private void mergeGroup(GridColumnGroup group) {
		//��ȡ��ǰ������б�
		List<IGridColumn> childColumnList = group.getChildColumnList();
		if(childColumnList != null){
			Iterator<IGridColumn> it = childColumnList.iterator();
			//�ݴ���ȡ������
			List<GridColumn> tempList = new ArrayList<GridColumn>();
			while(it.hasNext()){
				IGridColumn column = it.next();
				//�������
				if(column instanceof GridColumnGroup){
					GridColumnGroup childGroup = (GridColumnGroup) column;
					//���¼���
					if(childGroup.getConfType().equals(WebElement.CONF_ADD)){
						mergeGroup(childGroup);
					}
					//������ɾ�����
//					else if(childGroup.getConfType().equals(WebElement.CONF_DEL)){
//						mergeGroup(childGroup);
//						boolean removed = this.columnList.remove(childGroup.getId());
//						if(!removed)
//							logger.warn("û�д��б����ҵ���ɾ�������飺" + childGroup.getId());
//					}
					//������ref���
//					else if(childGroup.getConfType().equals(WebElement.CONF_REF)){
//						mergeGroup(childGroup);
//						GridColumnGroup currGroup = (GridColumnGroup) this.getColumnById(childGroup.getId());
//						if(currGroup == null){
//							logger.warn("û�д��б����ҵ����޸ĵ����飺" + childGroup.getId());
//							return;
//						}
//						currGroup.mergeProperties(childGroup);
//					}
				}
				else{
					GridColumn childColumn = (GridColumn) column;
					//TODO ɾ����group�¶�Ӧ��column
					if(childColumn.getConfType().equals(WebElement.CONF_DEL)){
						//�ӵ�ǰ����ɾ��column
						it.remove();
						//��ԭ��������ɾ��column
						IGridColumn currColumn = this.getColumnById(childColumn.getId());
						if(currColumn != null)
							this.columnList.remove(currColumn);
					}
					else if(childColumn.getConfType().equals(WebElement.CONF_REF)){
						//�ӵ�ǰgroup��ɾ����
						it.remove();
						//��ȡԭ����Ϣ
						GridColumn sourceColumn = (GridColumn) this.getColumnById(childColumn.getId());
						//��ԭ����ɾ������
						this.columnList.remove(sourceColumn);
						//������Ϣ
						sourceColumn.mergeProperties(childColumn);
						//childColumn.mergeProperties(ele)
						tempList.add(sourceColumn);
					}
					//���Ӳ�����
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
			buffer.append("����ID����Ϊ��!\r\n");
		}
		if(this.getDataset() == null || this.getDataset().equals("")){
			buffer.append("������õ����ݼ�����Ϊ��!\r\n");
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
			//������ڲ�����õ���������޷�������־���ߡ�
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

