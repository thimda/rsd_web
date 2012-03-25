package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * 记录集信息，它作为<code>DataSet</code>的数据信息存在，Record与
 * Field之间没有直接关联而是通过所依赖的DataSet进行关联。
 *
 */

public class RowSet implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;
	private PaginationInfo paginationInfo = new PaginationInfo();
	private Map<Integer, RowData> pagedRowListMap = new HashMap<Integer, RowData>();
	private boolean rowSetChanged = false;
	private Dataset dataset;
	private String keyvalue;
	private String oldKeyValue;
	public RowSet(String keyvalue){
		this.keyvalue = keyvalue;
	}
	/**
	 * 清空记录信息
	 */
	public void clear() {
		pagedRowListMap.clear();
	}

	public RowData getRowData(Integer pageIndex) {
		return pagedRowListMap.get(pageIndex);
	}
	public RowData getCurrentRowData() {
		return pagedRowListMap.get(paginationInfo.getPageIndex());
	}
	
	public void addRowData(Integer pageIndex, RowData rowData) {
		pagedRowListMap.put(pageIndex, rowData);
	}
	
	public RowData[] getRowDatas() {
		return pagedRowListMap.values().toArray(new RowData[0]);
	}
	
//	/**
//	 * 添加一条记录信息
//	 * @param row
//	 */
//	public void addRow(Row row) {
//
//		rowList.add(row);
//	}
//	
//	/**
//	 * 在指定位置添加记录信息
//	 * @param index
//	 * @param row
//	 */
//	public void insertRow(int index, Row row)
//	{
//		if(index < 0 || index > this.size())
//			throw new IllegalArgumentException("index参数异常!");
//		rowList.add(index, row);
//	}
//
//	/**
//	 * 删除一条记录信息
//	 * @param row
//	 */
//	public void removeRow(Row row) {
//
//		rowList.remove(row);
//	}
//
//	/**
//	 * 删除指定位置的记录信息
//	 * @param index
//	 */
//	public void removeRow(int index) {
//
//		rowList.remove(index);
//	}
//	
//	/**
//	 * 获取制定位置的纪录对象
//	 * @param index
//	 * @return
//	 */
//	public Row getRow(int index)
//	{
//		return rowList.get(index);
//	}
//	
//	public Row getRow(String rowId)
//	{
//		for(int i = 0;i < rowList.size(); i++)
//			if(rowList.get(i).getRowId().equals(rowId))
//				return rowList.get(i);
//		return null;		
//	}

//	/**
//	 * 获取当前记录集中的需要更新的记录，
//	 * 即状态为：更新的记录。
//	 * 
//	 * @return
//	 */
//	public Row[] getUpdateRow() {
//
//		return this.getRowByState(Row.STATE_UPDATE);
//	}
//
//	/**
//	 * 获取当前记录集中的需要删除的记录，
//	 * 即状态为：删除的记录。
//	 * 
//	 * @return
//	 */
//	public Row[] getDeleteRow() {
//
//		return this.getRowByState(Row.STATE_DELETED);
//	}
//
//	/**
//	 * 获取当前记录集中的需要在数据库中
//	 * 新增的记录，即状态为：增加的记录。
//	 * @return
//	 */
//	public Row[] getAddRow() {
//
//		return this.getRowByState(Row.STATE_ADD);
//	}
//
//	/**
//	 * 获取无状态记录信息，即一般状态信息。后台
//	 * 向前台返回时，只有nrm状态。
//	 * @return
//	 */
//	public Row[] getNormalRow() {
//
//		return this.getRowByState(Row.STATE_NORMAL);
//	}
//
//	/**
//	 * 获取指定状态的记录信息
//	 * @param state
//	 * @return
//	 */
//	public Row[] getRowByState(int state) {
//
//		RowData rows = new ArrayRowData();
//		for (int i = 0; i < rowList.size(); i++) {
//
//			if (rowList.get(i).getState() == state)
//				rows.add(rowList.get(i));
//		}
//		return (Row[]) rows.toArray(new Row[rows.size()]);
//	}
//
//	/**
//	 * 获取当前记录集的条数
//	 * @return
//	 */
//	public int size() {
//
//		return rowList.size();
//	}
//
//	/**
//	 * 获取当前记录集中的所有记录
//	 * @return
//	 */
//	public Row[] getRows() {
//
//		return (Row[]) rowList.toArray(new Row[rowList.size()]);
//	}

	/**
	 * 实现克隆方法
	 */
	public Object clone()
	{
		try {
			RowSet rs = (RowSet)super.clone();
//			rs.pagedRowListMap = new ArrayRowData();
//			for(int i = 0;i < this.rowList.size(); i++)
//			{
//				rs.rowList.add((Row)this.rowList.get(i).clone());
//			}
			return rs;
		} catch (CloneNotSupportedException e) {
			LfwLogger.error(e.getMessage(), e);;
		}		
		return null;
	}

//	public void adjustRowSize() {
//		Iterator<Row> it = rowList.iterator();
//		while(it.hasNext()){
//			Row row = it.next();
//			row.addColumn();
//		}
//	}

	public PaginationInfo getPaginationInfo() {
		return paginationInfo;
	}

	public void setPaginationInfo(PaginationInfo paginationInfo) {
		this.paginationInfo = paginationInfo;
	}

	public boolean isRowSetChanged() {
		RowData[] rds = getRowDatas();
		for (int i = 0; i < rds.length; i++) {
			if(rds[i].isRowDataChanged())
				return true;
		}
		return rowSetChanged;
	}

	public void setRowSetChanged(boolean rowSetChanged) {
		this.rowSetChanged = rowSetChanged;
	}

	public RowData getCurrentRowData(boolean create) {
		RowData rowData = getCurrentRowData();
		if(rowData == null){
			int index = paginationInfo.getPageIndex();
			rowData = new RowData(index);
			addRowData(index, rowData);
		}
		return rowData;
	}
	public Dataset getDataset() {
		return dataset;
	}
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
		paginationInfo.setPageSize(dataset.getPageSize());
	}
	public String getKeyvalue() {
		return keyvalue;
	}
	public void setKeyvalue(String keyvalue) {
		this.keyvalue = keyvalue;
	}
	public void setOldKeyValue(String newKey) {
		oldKeyValue = newKey;
	}
	public String getOldKeyValue() {
		return oldKeyValue;
	}
	
	
}
