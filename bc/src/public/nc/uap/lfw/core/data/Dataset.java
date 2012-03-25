package nc.uap.lfw.core.data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import nc.uap.lfw.core.common.DataTypeTranslator;
import nc.uap.lfw.core.common.IntDataTypeConst;
import nc.uap.lfw.core.comp.IDetachable;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.comp.WidgetElement;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.JsListenerConf;
import nc.uap.lfw.core.exception.LfwPluginException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LifeCyclePhase;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.vo.pub.lang.UFBoolean;
/**
 * 数据载体，包含结构信息（元数据信息）即FieldSet,FieldRelation等，另外 包含数据部分，即RowSet部分。
 * 
 */
public class Dataset extends WidgetElement implements IDetachable {
	public static final String MASTER_KEY = "MASTER_KEY";
	public static int ALL_NOT_SAVE = 2;
	public static int ALL_SAVE = 1;
	public static int UPDATE_SAVE = 0;
	public static final String FROM_VO = "VO";
	public static final String FROM_NC = "NC";
	public static final String CODE_LEVEL_CLAZZ = "$codelevelclazz";
	public static final String CODE_LEVEL_PPK = "$codelevelppk";
	public static final String CODE_LEVEL_PK = "$codelevelpk";
	public static final String CODE_LEVEL_RULE = "$coderule";
	public static final String CODE_LEVEL_CODEFIELD = "$codefield";
	// 编码树自动增加父列名称
	public static final String PARENT_PK_COLUMN = "$PARENT_PK";
	private static final long serialVersionUID = 71816449687525005L;
	/* 字段集信息 */
	private FieldSet fieldSet = new FieldSet();
	/* 记录集信息 */
	private Map<String, RowSet> rowSetMap = new HashMap<String, RowSet>();
	/* 请求参数集合 */
	private ParameterSet reqParameters = new ParameterSet();
	/* 应答参数集合 */
	private ParameterSet resParameters = new ParameterSet();
	/* 是否缓加载 */
	private boolean lazyLoad = true;
	private String currentKey;
	private FieldRelations fieldRelations = new FieldRelations();
	private int pageSize = -1;
	/* VO类名 */
	private String voMeta;
	private boolean enabled = false;
	/* 在这些操作状态下数据集可编辑 */
	private boolean controlwidgetopeStatus = false;
	// 行数量计数器，用于产生行随机ID
	private int randomRowIndex = 0;
	// 客户端是否维持分页,由业务判断,维持分页将增加客户端的内存使用量，不维持则无法获取之前页面的选中情况等信息
	private boolean stickPage = true;
	private String refId;
	private String from;
	//作为子表时是否可以为空
	private boolean notNullBody;
	public boolean isNotNullBody() {
		return notNullBody;
	}
	public void setNotNullBody(boolean notNullBody) {
		this.notNullBody = notNullBody;
	}
	public int getRandomRowIndex() {
		return randomRowIndex;
	}
	public void setRandomRowIndex(int randomRowIndex) {
		this.randomRowIndex = randomRowIndex;
	}
	// 显示名称
	private String caption;
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	// 当前聚焦行
	private int focusIndex = -1;
	public boolean isControlwidgetopeStatus() {
		return controlwidgetopeStatus;
	}
	public void setControlwidgetopeStatus(boolean controlwidgetopeStatus) {
		this.controlwidgetopeStatus = controlwidgetopeStatus;
	}
	/**
	 * 默认构造方法
	 */
	public Dataset() {
		this(null);
	}
	/**
	 * 通过DataSet Id构造DataSet
	 * 
	 * @param id
	 */
	public Dataset(String id) {
		super(id);
	}
	public Row getEmptyRow() {
		return getEmptyRow(Row.STATE_ADD);
	}
	/**
	 * 此方法能够通过DataSet实例获取一个空的记录信息。 该记录信息是根据当前dataSet所拥有的字段数进行初始化的。
	 * 
	 */
	public Row getEmptyRow(int state) {
		this.randomRowIndex++;
		FieldSet fs = getFieldSet();
		int fieldCount = fs.getFieldCount();
		String id = getRandomRowId();
		Row row = new Row(id, fieldCount);
		row.setState(state);
		for (int i = 0; i < fieldCount; i++) {
			Field f = fs.getField(i);
			if (f.getDefaultValue() != null)
				row.setValue(i, f.getDefaultValue());
			else {
				int dataType = DataTypeTranslator.translateString2Int(f.getDataType());
				if (IntDataTypeConst.UFBoolean_TYPE == dataType || IntDataTypeConst.boolean_TYPE == dataType || IntDataTypeConst.Boolean_TYPE == dataType)
					row.setValue(i, new UFBoolean(false));
			}
		}
		return row;
	}
	public int getFieldCount() {
		return getFieldSet().getFieldCount();
	}
	/**
	 * 获取字段集对象
	 * 
	 * @return
	 */
	public FieldSet getFieldSet() {
		return this.fieldSet;
	}
	/**
	 * 设置字段集对象。 该方法可以不用，可以通过获取方法对FieldSet的操作实现Field的
	 * 增加和删除，但是为了zhouhai解析需要，故增加此方法。
	 * 
	 * @param fieldSet
	 */
	public void setFieldSet(FieldSet fieldSet) {
		this.fieldSet = fieldSet;
		if (this.fieldSet != null) {
			this.fieldSet.setDataSet(this);
		}
	}
	public RowSet getCurrentRowSet() {
		if (currentKey == null)
			return null;
		RowSet rowset = getRowSet(currentKey);
		if (rowset == null) {
			rowset = new RowSet(currentKey);
			addRowSet(currentKey, rowset);
		}
		return rowset;
	}
	public RowData getCurrentRowData() {
		if (this.currentKey == null)
			return null;
		return getCurrentRowSet().getCurrentRowData();
	}
	/**
	 * 获取当前RowData行数
	 * 
	 * @return
	 */
	public int getCurrentRowCount() {
		RowData rd = getCurrentRowData();
		if (rd == null)
			return 0;
		return rd.getRowCount();
	}
	/**
	 * 获取总行数
	 * 
	 * @return
	 */
	public int getRowCount() {
		int count = 0;
		RowSet[] rowSets = getRowSets();
		for (RowSet rowSet : rowSets) {
			RowData[] rowDatas = rowSet.getRowDatas();
			for (RowData rowData : rowDatas) {
				count += rowData.getRowCount();
			}
		}
		return count;
	}
	public Row getSelectedRow() {
		RowData rowData = getCurrentRowSet().getCurrentRowData();
		if (null != rowData)
			return getCurrentRowSet().getCurrentRowData().getSelectedRow();
		else
			return null;
	}
	public int getFocusIndex() {
		return focusIndex;
	}
	public void setFocusIndex(int focusIndex) {
		this.focusIndex = focusIndex;
		setCtxChanged(true);
	}
	/**
	 * 获取当前聚焦行
	 * 
	 * @return
	 */
	public Row getFocusRow() {
		RowData rowData = getCurrentRowSet().getCurrentRowData();
		if (null != rowData && focusIndex != -1 && rowData.getRowCount() >= focusIndex + 1)
			return rowData.getRow(focusIndex);
		else
			return null;
	}
	/**
	 * 获取新增或修改的当前RowData中的Row
	 * 
	 * @return
	 */
	public Row[] getChangedRows() {
		RowData rowData = getCurrentRowSet().getCurrentRowData();
		if (null == rowData)
			return null;
		List<Row> rowList = new ArrayList<Row>();
		for (Row r : rowData.getRows()) {
			if (Row.STATE_ADD == r.getState() || Row.STATE_UPDATE == r.getState()) {
				rowList.add(r);
			}
		}
		return rowList.toArray(new Row[0]);
	}
	public Row[] getSelectedRows() {
		RowData rowData = getCurrentRowSet().getCurrentRowData();
		if (null != rowData)
			return getCurrentRowSet().getCurrentRowData().getSelectedRows();
		else
			return null;
	}
	public Row[] getAllSelectedRows() {
		List<Row> allRowList = new ArrayList<Row>();
		RowSet[] rowSets = getRowSets();
		for (int i = 0; i < rowSets.length; i++) {
			RowSet rowSet = rowSets[i];
			RowData[] rowDatas = rowSet.getRowDatas();
			for (int j = 0; j < rowDatas.length; j++) {
				RowData rowData = rowDatas[j];
				Row[] selRows = rowData.getSelectedRows();
				if (selRows != null)
					allRowList.addAll(Arrays.asList(selRows));
			}
		}
		return allRowList.toArray(new Row[0]);
	}
	public Integer getSelectedIndex() {
		Integer[] indices = getSelectedIndices();
		if (indices == null || indices.length == 0)
			return -1;
		return indices[0];
	}
	public Integer[] getSelectedIndices() {
		RowData rowData = getCurrentRowSet().getCurrentRowData();
		if (null != rowData) {
			Integer[] indices = getCurrentRowSet().getCurrentRowData().getSelectIndices();
			return indices;
		} else
			return null;
	}
	public void setRowSelectIndex(Integer index) {
		getCurrentRowData().setRowSelectIndex(index);
		setFocusIndex(index);
	}
	public void setRowSelectIndices(Integer[] indices) {
		getCurrentRowData().setRowSelectIndices(indices);
	}
	public Object getValue(String key) {
		Row r = getSelectedRow();
		if (r == null)
			return null;
		int index = fieldSet.nameToIndex(key);
		if (index == -1)
			throw new IllegalArgumentException("不存在key:" + key);
		return r.getValue(index);
	}
	public Object getValue(int index) {
		Row r = getSelectedRow();
		if (r == null)
			return null;
		if (index < 0 || index > fieldSet.getFieldCount() - 1)
			throw new IllegalArgumentException("index 越界:" + index);
		return r.getValue(index);
	}
	public void setValue(String key, String value) {
		Row r = getSelectedRow();
		if (r == null) {
			throw new LfwRuntimeException("没有选中行");
		}
		int index = fieldSet.nameToIndex(key);
		if (index == -1)
			throw new IllegalArgumentException("不存在key:" + key);
		r.setValue(index, value);
	}
	public void setValue(int index, String value) {
		Row r = getSelectedRow();
		if (r == null) {
			throw new LfwRuntimeException("没有选中行");
		}
		if (index < 0 || index > fieldSet.getFieldCount() - 1)
			throw new IllegalArgumentException("index 越界:" + index);
		r.setValue(index, value);
	}
	/**
	 * 获取记录集信息
	 * 
	 * @return
	 */
	public RowSet getRowSet(String key) {
		return this.rowSetMap.get(key);
	}
	public void replaceKeyValue(String oldKey, String newKey) {
		RowSet rowSet = this.rowSetMap.remove(oldKey);
		rowSet.setOldKeyValue(oldKey);
		rowSet.setKeyvalue(newKey);
		this.rowSetMap.put(newKey, rowSet);
		if (oldKey.equals(this.currentKey))
			this.currentKey = newKey;
		setCtxChanged(true);
	}
	public void removeRowSet(String key) {
		this.rowSetMap.remove(key);
		if (key.equals(this.currentKey))
			this.currentKey = "-1";
		setCtxChanged(true);
	}
	public void removeRow(Row row) {
		getCurrentRowSet().getCurrentRowData().removeRow(row);
	}
	public void removeRow(int index) {
		getCurrentRowSet().getCurrentRowData().removeRow(index);
	}
	public void removeRow(Row row, boolean isTrueRemove) {
		getCurrentRowSet().getCurrentRowData().removeRow(row, isTrueRemove);
	}
	public void removeRow(int index, boolean isTrueRemove) {
		getCurrentRowSet().getCurrentRowData().removeRow(index, isTrueRemove);
	}
	public int nameToIndex(String field) {
		return getFieldSet().nameToIndex(field);
	}
	public int getRowIndex(Row row) {
		RowSet rowSet = getCurrentRowSet();
		if (rowSet == null)
			return -1;
		RowData rowData = rowSet.getCurrentRowData();
		if (rowData == null)
			return -1;
		return rowData.getRowIndex(row);
	}
	/**
	 * 从所有的Rowsets中取到row所在的行
	 * 
	 * @param row
	 * @return
	 */
	public int getRowIndexInAllRowSets(Row row) {
		RowSet[] rowSets = getRowSets();
		if (rowSets == null)
			return -1;
		int rowIndex = -1;
		for (int i = 0; i < rowSets.length; i++) {
			RowSet rowSet = rowSets[i];
			RowData[] rowDatas = rowSet.getRowDatas();
			for (int j = 0; j < rowDatas.length; j++) {
				RowData rowData = rowDatas[j];
				if (rowData == null)
					continue;
				rowIndex = rowData.getRowIndex(row);
				if (rowIndex != -1)
					return rowIndex;
			}
		}
		return rowIndex;
	}
	public void removeRowInAllRowSets(Row row) {
		RowSet[] rowSets = getRowSets();
		for (int i = 0; i < rowSets.length; i++) {
			RowSet rowSet = rowSets[i];
			RowData[] rowDatas = rowSet.getRowDatas();
			for (int j = 0; j < rowDatas.length; j++) {
				RowData rowData = rowDatas[j];
				int index = rowData.getRowIndex(row);
				if (index != -1) {
					rowData.removeRow(row);
					return;
				}
			}
		}
	}
	/**
	 * 获取记录集信息
	 * 
	 * @return
	 */
	public RowSet getRowSet(String key, boolean create) {
		if (key == null || key.equals(""))
			throw new LfwRuntimeException("key不能为空");
		RowSet rowset = this.rowSetMap.get(key);
		if (rowset == null) {
			rowset = new RowSet(key);
			addRowSet(key, rowset);
			RowData rd = new RowData(0);
			rowset.addRowData(0, rd);
		}
		return rowset;
	}
	public void addRowSet(String key, RowSet rowSet) {
		rowSet.setDataset(this);
		this.rowSetMap.put(key, rowSet);
	}
	/**
	 * 获取请求参数集合
	 * 
	 * @return
	 */
	public ParameterSet getReqParameters() {
		return this.reqParameters;
	}
	public Parameter getReqParameter(String key) {
		return this.reqParameters.getParameter(key);
	}
	public void addRow(Row row) {
		if (this.currentKey == null || this.currentKey.equals("")) {
			setCurrentKey(Dataset.MASTER_KEY);
		}
		RowSet rowSet = getCurrentRowSet();
		RowData rd = rowSet.getCurrentRowData(true);
		rd.addRow(row);
	}
	public void insertRow(int index, Row row) {
		if (this.currentKey == null || this.currentKey.equals(""))
			throw new LfwRuntimeException("请先指定当前数据集的Key!");
		RowSet rowSet = getCurrentRowSet();
		RowData rd = rowSet.getCurrentRowData(true);
		rd.insertRow(index, row);
	}
	/**
	 * 获取应答参数集合
	 * 
	 * @return
	 */
	public ParameterSet getResParameters() {
		return this.resParameters;
	}
	/**
	 * 设置请求参数集 为解析方法而增加
	 * 
	 * @param reqParams
	 */
	public void setReqParameters(ParameterSet reqParams) {
		this.reqParameters = reqParams;
	}
	/**
	 * 设置应答参数集 为解析方便而增加
	 * 
	 * @param resParams
	 */
	public void setResParameters(ParameterSet resParams) {
		this.resParameters = resParams;
	}
	/**
	 * 对DataSet的数据相关信息进行清除，仅保留结构信息。
	 * 
	 */
	public void clear() {
		this.setCtxChanged(true);
		this.currentKey = null;
		this.rowSetMap.clear();
		this.reqParameters.clear();
		this.resParameters.clear();
		this.focusIndex = -1;
	}
	/**
	 * 对外提供的拷贝方法，仅进行结构拷贝，不进行数据拷贝
	 */
	public Object clone() {
		Dataset ds = (Dataset) super.clone();
		ds.fieldSet = (FieldSet) this.fieldSet.clone();
		ds.fieldSet.setDataSet(ds);// add by renxh
		if (this.rowSetMap != null) {
			ds.rowSetMap = new HashMap<String, RowSet>();
			Iterator<Entry<String, RowSet>> it = this.rowSetMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, RowSet> entry = it.next();
				ds.rowSetMap.put(entry.getKey(), entry.getValue());
			}
		}
		ds.reqParameters = (ParameterSet) this.reqParameters.clone();
		ds.resParameters = (ParameterSet) this.resParameters.clone();
		return ds;
	}
	/**
	 * 该方法用于生成前后台交互所用的recordId，前台在交互过程中通过它来 建立记录的对应关系。产生规则： 0/1 + datasetId +
	 * random(10位) 其中由后台产生的record id，第一位是 0 由前台产生的record id，第一位是 1
	 * 
	 * @return public static String getRandomRowId() { // String id = "0" +
	 *         this.getId(); // Random random = new Random(); // id +=
	 *         Math.abs(random.nextInt()); // return id; UUID rowId =
	 *         UUID.randomUUID(); return "0" + rowId.toString(); }
	 */
	/**
	 * 该方法用于生成前后台交互所用的recordId，前台在交互过程中通过它来 建立记录的对应关系。 产生规则： 0/1 + random
	 * 其中由后台产生的record id，第一位是 0 由前台产生的record id，第一位是 1
	 * 
	 * @return
	 */
	public String getRandomRowId() {
		return "0" + this.getId() + this.randomRowIndex;
	}
	/**
	 * 是否采用缓加载策略
	 * 
	 * @return
	 */
	public boolean isLazyLoad() {
		return lazyLoad;
	}
	/**
	 * 设置是否采用缓加载策略
	 * 
	 * @param lazyLoad
	 */
	public void setLazyLoad(boolean lazyLoad) {
		this.lazyLoad = lazyLoad;
	}
	/**
	 * 
	 * @param f
	 */
	public void adjustField(Field f) {
		this.getFieldSet().addField(f);
		this.notify("adjustField", f);
	}
	@Override public void mergeProperties(WebElement ele) {
		// super.mergeProperties(ele);
		Dataset ds = (Dataset) ele;
		Map<String, JsListenerConf> sourceHanderMap = this.getListenerMap();
		Map<String, JsListenerConf> handlerMap = ds.getListenerMap();
		if (handlerMap != null && handlerMap.size() > 0) {
			for (Iterator<String> it = handlerMap.keySet().iterator(); it.hasNext();) {
				String listenerId = it.next();
				JsListenerConf listener = handlerMap.get(listenerId);
				JsListenerConf originalListener = sourceHanderMap.get(listenerId);
				// 如果模板ds有此Listener
				if (originalListener != null) {
					// 删除的Listener
					if (listener.getConfType().equals(WebElement.CONF_DEL))
						this.removeListener(listenerId);
					else
						this.addListener(listener);
				} else {
					// 现在存在，以前不存在，是新增的Listener
					this.addListener(listener);
				}
			}
		}
		String confType = ds.getConfType();
		if (confType != null)
			this.setConfType(confType);
	}
	@Override public List<Class<? extends JsListenerConf>> createAcceptListeners() {
		List<Class<? extends JsListenerConf>> list = new ArrayList<Class<? extends JsListenerConf>>();
		list.add(DatasetListener.class);
		return list;
	}
	public FieldRelations getFieldRelations() {
		return fieldRelations;
	}
	public void setFieldRelations(FieldRelations fieldRelations) {
		this.fieldRelations = fieldRelations;
	}
	public String getCurrentKey() {
		return currentKey;
	}
	public void setCurrentKey(String currentKey) {
		if (this.currentKey == null || !this.currentKey.equals(currentKey)) {
			this.currentKey = currentKey;
			this.setCtxChanged(true);
			if (this.currentKey != null && !this.currentKey.equals(""))
				getRowSet(this.currentKey, true);
		}
	}
	public RowSet[] getRowSets() {
		return rowSetMap.values().toArray(new RowSet[0]);
	}
	public Row[] getFalseDeleteRows() {
		return getCurrentRowData().getFalseDeleteRows();
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getVoMeta() {
		return voMeta;
	}
	public void setVoMeta(String voMeta) {
		this.voMeta = voMeta;
	}
	@Override public boolean isCtxChanged() {
		RowSet[] rowsets = getRowSets();
		if (rowsets != null) {
			for (int i = 0; i < rowsets.length; i++) {
				if (rowsets[i].isRowSetChanged())
					return true;
			}
		}
		return super.isCtxChanged();
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			setCtxChanged(true);
		}
	}
	public Row getRowById(String id) {
		RowData rd = getCurrentRowData();
		Row row = rd.getRowById(id);
		if (row != null)
			return row;
		Iterator<RowSet> rsIt = rowSetMap.values().iterator();
		while (rsIt.hasNext()) {
			RowSet rs = rsIt.next();
			RowData[] rds = rs.getRowDatas();
			for (int i = 0; i < rds.length; i++) {
				row = rds[i].getRowById(id);
				if (row != null)
					return row;
			}
		}
		return null;
	}
	public String getLastCondition() {
		return (String) this.getExtendAttributeValue("LAST_CONDITION");
	}
	public void setLastCondition(String condition) {
		setLastCondition(false, condition);
	}
	public void setLastCondition(boolean parent, String condition) {
		this.setExtendAttribute("LAST_CONDITION", condition);
	}
	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			buffer.append("数据集的ID不能为空!\r\n");
		}
		if (buffer.length() > 0)
			throw new LfwPluginException(buffer.toString());
	}
	public boolean isStickPage() {
		return stickPage;
	}
	public void setStickPage(boolean stickPage) {
		this.stickPage = stickPage;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	@Override public void detach() {
		currentKey = null;
		rowSetMap.clear();
		reqParameters.clear();
		resParameters.clear();
	}
	/**
	 * @deprecated use setRowSelectIndex
	 * @param rowIndex
	 */
	public void setSelectedIndex(int rowIndex) {
		setRowSelectIndex(rowIndex);
	}

	private void notify(String type, Object obj){
		if(LifeCyclePhase.ajax.equals(getPhase())){			
			Map<String,Object> map = new HashMap<String,Object>();
			String widgetId = this.getWidget().getId();
			map.put("widgetId", widgetId);
			map.put("datasetId", this.getId());
			map.put("type", type);
			if (type.equals("adjustField"))
				map.put("field", obj);
			this.getWidget().notifyChange(UIElement.UPDATE, map);
		}
	}
	
}
