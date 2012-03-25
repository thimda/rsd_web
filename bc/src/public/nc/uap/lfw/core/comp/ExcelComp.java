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
 * Excel�ؼ���̨��Ӧ��������
 * 
 * @author guoweic
 * 
 */
public class ExcelComp extends WebComponent implements IDataBinding {
	private static final long serialVersionUID = -525473184313225199L;
	private String dataset;
	// gird��headers�б�
	private List<IExcelColumn> columnList = null;
	// �Ƿ�Ϊ�ɱ༭excel
	private boolean editable = true;
	// �Ƿ���ʾ�̶�ѡ����
	private boolean multiSelect = false;
	// excel���и߶�
	private String rowHeight = null;
	// excel��ͷ���и߶�
	private String headerRowHeight = null;
	// �Ƿ���ʾ������
	private boolean showNumCol = false;
	// �Ƿ���ʾ�ϼ���
	private boolean showSumRow = false;
	// ��������excel��ҳÿҳ��Ŀ
	private String pageSize = null;
	// �Ƿ���׷�ҳ��ʾ��
	private boolean simplePageBar = false;
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
	 * ���ñ�ͷ�б�
	 * 
	 * @param columnList
	 */
	public void setColumnList(List<IExcelColumn> columnList) {
		this.columnList = columnList;
	}

	/**
	 * ���ӱ�ͷ
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
	 * �Ƴ���ͷ��(ֻ�Ƴ���ds field��������,��ExcelColumn���͵���)
	 * 
	 * @param fieldId
	 *            ���������õ�ds��fieldId
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
	 * �ϲ����ԡ�ע��˺ϲ��㷨���������ڴ��޷ּ��ĵ���ģ�嵽�зּ������������ļ��� ������֧��������ʽ�����㷨Ӧ�����ʵ��Ľ���
	 */
	public void mergeProperties(WebElement ele) {
		super.mergeProperties(ele);
		// �ϲ���������
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

		// �ϲ��С�
		List<IExcelColumn> tmpColumnList = excel.getColumnList();
		if (tmpColumnList != null) {
			Iterator<IExcelColumn> it = tmpColumnList.iterator();
			while (it.hasNext()) {
				IExcelColumn column = it.next();
				// ������Ͳ�������
				if (column instanceof ExcelColumnGroup) {
					ExcelColumnGroup group = (ExcelColumnGroup) column;
					// �����ӵ��飬����ӣ����ݹ鴦������µ�����
					if (group.getConfType().equals(WebElement.CONF_ADD)) {
						// ��ȿ�¡����
						ExcelColumnGroup clonedGroup = (ExcelColumnGroup) group
								.clone();
						// �ݹ鴦����
						mergeGroup(clonedGroup);
						// �������������ӵ��б���
						if (clonedGroup.getConfPos() == -1)
							this.columnList.add(clonedGroup);
						else
							this.columnList.add(clonedGroup.getConfPos(),
									clonedGroup);
					}
					// ���������ú�ɾ�����
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
							// logger.warn("û�д��б����ҵ���ɾ������:" +
							// tmpColumn.getId());
							return;
						}

						this.columnList.remove(currColumn);
					} else if (tmpColumn.getConfType().equals(
							WebElement.CONF_REF)) {
						ExcelColumn currColumn = (ExcelColumn) this
								.getColumnById(tmpColumn.getId());
						if (currColumn == null) {
							// logger.warn("û�д��б����ҵ����޸ĵ���:" +
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
	 * ������ĵݹ鷽��
	 * 
	 * @param group
	 */
	private void mergeGroup(ExcelColumnGroup group) {
		// ��ȡ��ǰ������б�
		List<IExcelColumn> childColumnList = group.getChildColumnList();
		if (childColumnList != null) {
			Iterator<IExcelColumn> it = childColumnList.iterator();
			// �ݴ���ȡ������
			List<ExcelColumn> tempList = new ArrayList<ExcelColumn>();
			while (it.hasNext()) {
				IExcelColumn column = it.next();
				// �������
				if (column instanceof ExcelColumnGroup) {
					ExcelColumnGroup childGroup = (ExcelColumnGroup) column;
					// ���¼���
					if (childGroup.getConfType().equals(WebElement.CONF_ADD)) {
						mergeGroup(childGroup);
					}
					// ������ɾ�����
					// else
					// if(childGroup.getConfType().equals(WebElement.CONF_DEL)){
					// mergeGroup(childGroup);
					// boolean removed =
					// this.columnList.remove(childGroup.getId());
					// if(!removed)
					// logger.warn("û�д��б����ҵ���ɾ�������飺" + childGroup.getId());
					// }
					// ������ref���
					// else
					// if(childGroup.getConfType().equals(WebElement.CONF_REF)){
					// mergeGroup(childGroup);
					// ExcelColumnGroup currGroup = (ExcelColumnGroup)
					// this.getColumnById(childGroup.getId());
					// if(currGroup == null){
					// logger.warn("û�д��б����ҵ����޸ĵ����飺" + childGroup.getId());
					// return;
					// }
					// currGroup.mergeProperties(childGroup);
					// }
				} else {
					ExcelColumn childColumn = (ExcelColumn) column;
					// TODO ɾ����group�¶�Ӧ��column
					if (childColumn.getConfType().equals(WebElement.CONF_DEL)) {
						// �ӵ�ǰ����ɾ��column
						it.remove();
						// ��ԭ��������ɾ��column
						IExcelColumn currColumn = this.getColumnById(childColumn
								.getId());
						if (currColumn != null)
							this.columnList.remove(currColumn);
					} else if (childColumn.getConfType().equals(
							WebElement.CONF_REF)) {
						// �ӵ�ǰgroup��ɾ����
						it.remove();
						// ��ȡԭ����Ϣ
						ExcelColumn sourceColumn = (ExcelColumn) this
								.getColumnById(childColumn.getId());
						// ��ԭ����ɾ������
						this.columnList.remove(sourceColumn);
						// ������Ϣ
						sourceColumn.mergeProperties(childColumn);
						// childColumn.mergeProperties(ele)
						tempList.add(sourceColumn);
					}
					// ���Ӳ�����
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
			buffer.append("Excel��ID����Ϊ��!\r\n");
		}
		if (this.getDataset() == null || this.getDataset().equals("")) {
			buffer.append("Excel���õ����ݼ�����Ϊ��!\r\n");
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
