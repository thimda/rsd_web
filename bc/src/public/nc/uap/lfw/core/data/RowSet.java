package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.log.LfwLogger;

/**
 * ��¼����Ϣ������Ϊ<code>DataSet</code>��������Ϣ���ڣ�Record��
 * Field֮��û��ֱ�ӹ�������ͨ����������DataSet���й�����
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
	 * ��ռ�¼��Ϣ
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
//	 * ���һ����¼��Ϣ
//	 * @param row
//	 */
//	public void addRow(Row row) {
//
//		rowList.add(row);
//	}
//	
//	/**
//	 * ��ָ��λ����Ӽ�¼��Ϣ
//	 * @param index
//	 * @param row
//	 */
//	public void insertRow(int index, Row row)
//	{
//		if(index < 0 || index > this.size())
//			throw new IllegalArgumentException("index�����쳣!");
//		rowList.add(index, row);
//	}
//
//	/**
//	 * ɾ��һ����¼��Ϣ
//	 * @param row
//	 */
//	public void removeRow(Row row) {
//
//		rowList.remove(row);
//	}
//
//	/**
//	 * ɾ��ָ��λ�õļ�¼��Ϣ
//	 * @param index
//	 */
//	public void removeRow(int index) {
//
//		rowList.remove(index);
//	}
//	
//	/**
//	 * ��ȡ�ƶ�λ�õļ�¼����
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
//	 * ��ȡ��ǰ��¼���е���Ҫ���µļ�¼��
//	 * ��״̬Ϊ�����µļ�¼��
//	 * 
//	 * @return
//	 */
//	public Row[] getUpdateRow() {
//
//		return this.getRowByState(Row.STATE_UPDATE);
//	}
//
//	/**
//	 * ��ȡ��ǰ��¼���е���Ҫɾ���ļ�¼��
//	 * ��״̬Ϊ��ɾ���ļ�¼��
//	 * 
//	 * @return
//	 */
//	public Row[] getDeleteRow() {
//
//		return this.getRowByState(Row.STATE_DELETED);
//	}
//
//	/**
//	 * ��ȡ��ǰ��¼���е���Ҫ�����ݿ���
//	 * �����ļ�¼����״̬Ϊ�����ӵļ�¼��
//	 * @return
//	 */
//	public Row[] getAddRow() {
//
//		return this.getRowByState(Row.STATE_ADD);
//	}
//
//	/**
//	 * ��ȡ��״̬��¼��Ϣ����һ��״̬��Ϣ����̨
//	 * ��ǰ̨����ʱ��ֻ��nrm״̬��
//	 * @return
//	 */
//	public Row[] getNormalRow() {
//
//		return this.getRowByState(Row.STATE_NORMAL);
//	}
//
//	/**
//	 * ��ȡָ��״̬�ļ�¼��Ϣ
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
//	 * ��ȡ��ǰ��¼��������
//	 * @return
//	 */
//	public int size() {
//
//		return rowList.size();
//	}
//
//	/**
//	 * ��ȡ��ǰ��¼���е����м�¼
//	 * @return
//	 */
//	public Row[] getRows() {
//
//		return (Row[]) rowList.toArray(new Row[rowList.size()]);
//	}

	/**
	 * ʵ�ֿ�¡����
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
