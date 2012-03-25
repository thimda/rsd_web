package nc.uap.lfw.core.comp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.ctx.BaseContext;
import nc.uap.lfw.core.comp.ctx.ExcelContext;
import nc.uap.lfw.core.event.conf.ExcelCellListener;
import nc.uap.lfw.core.event.conf.ExcelListener;
import nc.uap.lfw.core.event.conf.ExcelRowListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.exception.LfwPluginException;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.jsp.uimeta.UIElement;

/**
 * Excel控件后台对应的配置类
 * 
 * @author guoweic
 * 
 */
public class ExcelComp extends WebComponent implements IDataBinding {
	private static final long serialVersionUID = -525473184313225199L;
	private String dataset;
	// gird的headers列表
	private List<IExcelColumn> columnList = null;
	// 是否为可编辑excel
	private boolean editable = true;
	// 是否显示固定选择列
	private boolean multiSelect = false;
	// excel单行高度
	private String rowHeight = null;
	// excel表头单行高度
	private String headerRowHeight = null;
	// 是否显示数字列
	private boolean showNumCol = false;
	// 是否显示合计行
	private boolean showSumRow = false;
	// 服务器端excel分页每页数目
	private String pageSize = null;
	// 是否简易分页显示栏
	private boolean simplePageBar = false;
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
	private boolean showColInfo = true;

	public ExcelComp() {
		super();
	}

	public ExcelComp(String id) {
		super(id);
	}

	public List<IExcelColumn> getColumnList() {
		return columnList;
	}

	public IExcelColumn getColumnById(String id) {
		Iterator<IExcelColumn> it = columnList.iterator();
		while (it.hasNext()) {
			IExcelColumn column = it.next();
			if (((WebElement) column).getId().equals(id))
				return column;
		}
		return null;
	}

	public IExcelColumn getColumn(int index) {
		return columnList.get(index);
	}

	public IExcelColumn getColumnByField(String field) {
		Iterator<IExcelColumn> it = columnList.iterator();
		while (it.hasNext()) {
			IExcelColumn column = it.next();
			if (((ExcelColumn) column).getField().equals(field))
				return column;
		}
		return null;
	}

	/**
	 * 设置表头列表
	 * 
	 * @param columnList
	 */
	public void setColumnList(List<IExcelColumn> columnList) {
		this.columnList = columnList;
	}

	/**
	 * 增加表头
	 * 
	 * @param col
	 *            excelCoulumn
	 */
	public void addColumn(IExcelColumn col) {
		addColumn(col,false);
	}
	public void addColumn(IExcelColumn col,boolean withnotify){
		if (this.columnList == null) {
			this.columnList = new ArrayList<IExcelColumn>();
		}
		this.columnList.add(col);
		if(withnotify){
			this.notify("addColumn", col);
		}
	}
	
	public void insertColumn(int index, IExcelColumn col) {
		if (index < 0 || index > getColumnCount())
			index = getColumnCount();
		if (this.columnList == null) {
			this.columnList = new ArrayList<IExcelColumn>();
		}
		this.columnList.add(index, col);
	}

	public int getColumnCount() {
		return this.columnList.size();
	}

	/**
	 * 移除表头列(只移除和ds field关联的列,即ExcelColumn类型的列)
	 * 
	 * @param fieldId
	 *            该列所引用的ds的fieldId
	 */
	public void removeColumnByField(String fieldId) {
		if (fieldId == null)
			return;
		if (this.columnList != null) {
			for (int i = 0; i < this.columnList.size(); i++) {
				if (this.columnList.get(i) instanceof ExcelColumn) {
					ExcelColumn colum = (ExcelColumn) this.columnList.get(i);
					if (colum.getField() != null
							&& colum.getField().equals(fieldId))
						this.columnList.remove(i);
				}
			}
		}
	}

	public void removeColumn(IExcelColumn col) {
		columnList.remove(col);
	}

	public void setDataset(String datasetId) {
		this.dataset = datasetId;
	}

	public String getDataset() {
		return dataset;
	}

	public Object clone() {
		ExcelComp excel = (ExcelComp) super.clone();
		if (this.columnList != null) {
			excel.columnList = new ArrayList<IExcelColumn>();
			Iterator<IExcelColumn> it = this.columnList.iterator();
			while (it.hasNext())
				excel.addColumn((IExcelColumn) it.next().clone());
		}
		return excel;
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
	// public String getPageClientSize() {
	// return pageClientSize;
	// }
	//
	// public void setPageClientSize(String pageClientSize) {
	// this.pageClientSize = pageClientSize;
	// }

	public String getShowColumns() {
		return showColumns;
	}

	public void setShowColumns(String showColumns) {
		this.showColumns = showColumns;
		setCtxChanged(true);
		addCtxChangedProperty("showColumns");
	}

	public List<ExcelColumn> filterColumns(String name) {
		List<ExcelColumn> eleList = new ArrayList<ExcelColumn>();
		Iterator<IExcelColumn> it = columnList.iterator();
		while (it.hasNext()) {
			IExcelColumn ele = it.next();
			if (ele instanceof ExcelColumn) {
				ExcelColumn col = (ExcelColumn) ele;
				if (col.getId().startsWith(name))
					eleList.add(col);
			}
		}
		return eleList;
	}

	public boolean isSimplePageBar() {
		return simplePageBar;
	}

	public void setSimplePageBar(boolean simplePageBar) {
		this.simplePageBar = simplePageBar;
	}

	public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(MouseListener.class);
		list.add(ExcelListener.class);
		list.add(ExcelRowListener.class);
		list.add(ExcelCellListener.class);
		return list;
	}

	/**
	 * 合并属性。注意此合并算法仅仅适用于从无分级的单据模板到有分级的增量配置文件。 如果今后支持其它方式，此算法应该做适当改进。
	 */
	public void mergeProperties(WebElement ele) {
		super.mergeProperties(ele);
		// 合并基本属性
		ExcelComp excel = (ExcelComp) ele;

		String dataset = excel.getDataset();
		if (dataset != null)
			this.setDataset(dataset);

		String rowHeight = excel.getHeaderRowHeight();
		if (rowHeight != null)
			this.setRowHeight(rowHeight);

		// String pageClientSize = excel.getPageClientSize();
		// if(pageClientSize != null)
		// this.setPageClientSize(pageClientSize);

		String pageSize = excel.getPageSize();
		if (pageSize != null)
			this.setPageSize(pageSize);

		String groupColumns = excel.getGroupColumns();
		if (groupColumns != null)
			this.setGroupColumns(groupColumns);

		boolean sortable = excel.isSortable();
		if (sortable == false)
			this.setSortable(sortable);

		// 合并列。
		List<IExcelColumn> tmpColumnList = excel.getColumnList();
		if (tmpColumnList != null) {
			Iterator<IExcelColumn> it = tmpColumnList.iterator();
			while (it.hasNext()) {
				IExcelColumn column = it.next();
				// 如果待和并列是组
				if (column instanceof ExcelColumnGroup) {
					ExcelColumnGroup group = (ExcelColumnGroup) column;
					// 是增加的组，则添加，并递归处理此组下的子列
					if (group.getConfType().equals(WebElement.CONF_ADD)) {
						// 深度克隆此组
						ExcelColumnGroup clonedGroup = (ExcelColumnGroup) group
								.clone();
						// 递归处理组
						mergeGroup(clonedGroup);
						// 将处理过的组添加到列表中
						if (clonedGroup.getConfPos() == -1)
							this.columnList.add(clonedGroup);
						else
							this.columnList.add(clonedGroup.getConfPos(),
									clonedGroup);
					}
					// 不会有引用和删除情况
					// else if(group.getConfType().equals(WebElement.CONF_REF)){
					// //ExcelColumnGroup currGroup = this.columnList.get
					// //TODO
					// }
				} else {
					ExcelColumn tmpColumn = (ExcelColumn) column;
					if (tmpColumn.getConfType().equals(WebElement.CONF_ADD)) {
						this.columnList.add((IExcelColumn) tmpColumn.clone());
					} else if (tmpColumn.getConfType().equals(
							WebElement.CONF_DEL)) {
						IExcelColumn currColumn = this.getColumnById(tmpColumn
								.getId());
						if (currColumn == null) {
							// logger.warn("没有从列表中找到待删除的列:" +
							// tmpColumn.getId());
							return;
						}

						this.columnList.remove(currColumn);
					} else if (tmpColumn.getConfType().equals(
							WebElement.CONF_REF)) {
						ExcelColumn currColumn = (ExcelColumn) this
								.getColumnById(tmpColumn.getId());
						if (currColumn == null) {
							// logger.warn("没有从列表中找到待修改的列:" +
							// tmpColumn.getId());
							return;
						}
						currColumn.mergeProperties(tmpColumn);
						if (tmpColumn.getConfPos() != -1) {
							this.columnList.remove(currColumn);
							this.columnList.add(tmpColumn.getConfPos(),
									currColumn);
						}
					}
				}
			}
		}
	}

	/**
	 * 处理组的递归方法
	 * 
	 * @param group
	 */
	private void mergeGroup(ExcelColumnGroup group) {
		// 获取当前组的子列表
		List<IExcelColumn> childColumnList = group.getChildColumnList();
		if (childColumnList != null) {
			Iterator<IExcelColumn> it = childColumnList.iterator();
			// 暂存提取出的列
			List<ExcelColumn> tempList = new ArrayList<ExcelColumn>();
			while (it.hasNext()) {
				IExcelColumn column = it.next();
				// 如果是组
				if (column instanceof ExcelColumnGroup) {
					ExcelColumnGroup childGroup = (ExcelColumnGroup) column;
					// 是新加组
					if (childGroup.getConfType().equals(WebElement.CONF_ADD)) {
						mergeGroup(childGroup);
					}
					// 不会有删除情况
					// else
					// if(childGroup.getConfType().equals(WebElement.CONF_DEL)){
					// mergeGroup(childGroup);
					// boolean removed =
					// this.columnList.remove(childGroup.getId());
					// if(!removed)
					// logger.warn("没有从列表中找到待删除的列组：" + childGroup.getId());
					// }
					// 不会有ref情况
					// else
					// if(childGroup.getConfType().equals(WebElement.CONF_REF)){
					// mergeGroup(childGroup);
					// ExcelColumnGroup currGroup = (ExcelColumnGroup)
					// this.getColumnById(childGroup.getId());
					// if(currGroup == null){
					// logger.warn("没有从列表中找到待修改的列组：" + childGroup.getId());
					// return;
					// }
					// currGroup.mergeProperties(childGroup);
					// }
				} else {
					ExcelColumn childColumn = (ExcelColumn) column;
					// TODO 删除此group下对应的column
					if (childColumn.getConfType().equals(WebElement.CONF_DEL)) {
						// 从当前组下删除column
						it.remove();
						// 从原有配置中删除column
						IExcelColumn currColumn = this.getColumnById(childColumn
								.getId());
						if (currColumn != null)
							this.columnList.remove(currColumn);
					} else if (childColumn.getConfType().equals(
							WebElement.CONF_REF)) {
						// 从当前group中删除列
						it.remove();
						// 获取原列信息
						ExcelColumn sourceColumn = (ExcelColumn) this
								.getColumnById(childColumn.getId());
						// 从原列中删除此列
						this.columnList.remove(sourceColumn);
						// 补充信息
						sourceColumn.mergeProperties(childColumn);
						// childColumn.mergeProperties(ele)
						tempList.add(sourceColumn);
					}
					// 增加不做事
				}
			}
			Iterator<ExcelColumn> colIt = tempList.iterator();

			while (colIt.hasNext()) {
				ExcelColumn column = colIt.next();
				if (column.getConfPos() != -1) {
					if (column.getConfPos() >= group.getChildColumnList()
							.size())
						group.getChildColumnList().add(column);
					else
						group.getChildColumnList().add(column.getConfPos(),
								column);
				} else
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

	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			buffer.append("Excel的ID不能为空!\r\n");
		}
		if (this.getDataset() == null || this.getDataset().equals("")) {
			buffer.append("Excel引用的数据集不能为空!\r\n");
		}
		if (buffer.length() > 0)
			throw new LfwPluginException(buffer.toString());
	}

	@Override
	public BaseContext getContext() {
		ExcelContext ctx = new ExcelContext();
		ctx.setEnabled(this.enabled);
		ctx.setEditable(this.editable);
		if (checkCtxPropertyChanged("showColumns"))
			ctx.setShowColumns(this.showColumns);
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		ExcelContext excelctx = (ExcelContext) ctx;
		if (excelctx.getId() != null && !"".equals(excelctx.getId()))
			this.setId(excelctx.getId());
		this.setEditable(excelctx.isEditable());
		this.setEnabled(excelctx.isEnabled());
		this.setCtxChanged(false);
	}

	public boolean isShowColInfo() {
		return showColInfo;
	}

	public void setShowColInfo(boolean showColInfo) {
		this.showColInfo = showColInfo;
	}
	
	public void notify(String type, Object obj){
		if(LifeCyclePhase.ajax.equals(getPhase())){			
			Map<String,Object> map = new HashMap<String,Object>();
			String widgetId = this.getWidget().getId();
			map.put("widgetId", widgetId);
			map.put("excelId", this.getId());
			map.put("type", type);
			if (type.equals("addColumn"))
				map.put("column", obj);
			
			this.notifyChange(UIElement.UPDATE, map);
		}
	}
	
}
