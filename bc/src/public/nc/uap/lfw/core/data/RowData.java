package nc.uap.lfw.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RowData implements Serializable,Cloneable{
	private static final long serialVersionUID = -989226679182394744L;
	private List<Row> rowList = new ArrayList<Row>();
	private boolean rowDataChanged = true;
	private boolean rowDataSelfChanged = true;
	private RowSet rowSet = null;
	private int pageindex;
	private List<Integer> selectIndices;
	private List<Row> deleteRowList;
	public RowData(int pageindex) {
		this.pageindex = pageindex;
	}
	/**
	 * ���һ����¼��Ϣ
	 * @param row
	 */
	public void addRow(Row row) {
		rowList.add(row);
		rowDataChanged = true;
	}
	
	/**
	 * ��ָ��λ����Ӽ�¼��Ϣ
	 * @param index
	 * @param row
	 */
	public void insertRow(int index, Row row)
	{
		if(index < 0 || index > rowList.size())
			throw new IllegalArgumentException("index�����쳣!");
		//�����ds�л�ȡԭ�����ٽ���insert������Ҫclone���޸�rowid;
		if ((this.deleteRowList  != null && this.deleteRowList.contains(row)) 
				|| rowList.contains(row)){
			Row newRow = (Row) row.clone();
			newRow.setRowId(row.getRowId() + "c");
			rowList.add(index, newRow);
		}else{
			rowList.add(index, row);
		}
		if (null != selectIndices) {
			int size = selectIndices.size();
			if(size > 0){
				for (int i = 0; i < size; i++) {
					Integer id = selectIndices.get(i);
					if(id.intValue() >= index){
						selectIndices.set(i, (id + 1));
					}
				}
			}
		}
		rowDataChanged = true;
	}

	/**
	 * ɾ��һ����¼��Ϣ
	 * @param row
	 */
	public void removeRow(Row row) {
		int index = getRowIndex(row);
		removeRow(index);
	}

	/**
	 * ɾ��ָ��λ�õļ�¼��Ϣ
	 * @param rindex
	 */
	public void removeRow(int rindex) {
		Row row = rowList.remove(rindex);
		if(deleteRowList == null){
			deleteRowList = new ArrayList<Row>();
		}
		deleteRowList.add(row);
		if (null != selectIndices) {
			int index = selectIndices.indexOf(rindex);
			if(index != -1)
				selectIndices.remove(index);
			int size = selectIndices.size();
			if(size > 0){
				for (int i = 0; i < size; i++) {
					Integer id = selectIndices.get(i);
					if(id.intValue() > rindex){
						selectIndices.set(i, (id - 1));
					}
				}
			}
		}
		rowDataChanged = true;
	}
	
	/**
	 * ɾ��һ����¼��Ϣ,��ɾ�����ɾ��
	 * @param row
	 * @param isTrueRemove
	 */
	public void removeRow(Row row, boolean isTrueRemove){
		if (isTrueRemove)
			removeRow(row);
		else{
			row.setState(Row.STATE_FALSE_DELETED);
		}
	}
	
	/**
	 * ɾ��һ����¼��Ϣ,��ɾ�����ɾ��
	 * 
	 * @param rindex
	 * @param isTrueRemove
	 */
	public void removeRow(int rindex, boolean isTrueRemove){
		if (isTrueRemove)
			removeRow(rindex);
		else{
			Row row = rowList.get(rindex);
			row.setState(Row.STATE_FALSE_DELETED);
		}
	}
	/**
	 * ��ȡ�ƶ�λ�õļ�¼����
	 * @param index
	 * @return
	 */
	public Row getRow(int index)
	{
		return rowList.get(index);
	}
	
	public Row[] getRows() {
		return rowList.toArray(new Row[0]);
	}
	
	public boolean isRowDataSelfChanged() {
		return rowDataSelfChanged;
	}

	public boolean isRowDataChanged() {
		Iterator<Row> it = rowList.iterator();
		while(it.hasNext()){
			if(it.next().isRowChanged())
				return true;
		}
		if(rowDataChanged)
			return true;
		return rowDataSelfChanged;
	}

	public void setRowDataChanged(boolean rowDataChanged) {
		this.rowDataChanged = rowDataChanged;
	}
	public RowSet getRowSet() {
		return rowSet;
	}
	public void setRowSet(RowSet rowSet) {
		this.rowSet = rowSet;
	}
	public int getPageindex() {
		return pageindex;
	}
	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}
	public Row getSelectedRow() {
		Row[] rows = getSelectedRows();
		if(rows != null && rows.length > 0)
			return rows[rows.length - 1];
		return null;
	}
	public Row[] getSelectedRows() {
		if(selectIndices == null || selectIndices.size() == 0)
			return null;
		Row[] selRows = new Row[selectIndices.size()];
		Row[] rows = this.getRows();
		for (int i = 0; i < selRows.length; i++) {
			selRows[i] = rows[selectIndices.get(i)];
		}
		return selRows;
	}
	
	public Row[] getFalseDeleteRows(){
		List<Row> delRowList = new ArrayList<Row>();
		for (Row r : rowList){
			if (r.getState() == Row.STATE_FALSE_DELETED){
				delRowList.add(r);
			}
		}
		return delRowList.toArray(new Row[0]);
	}
	
	public Integer[] getSelectIndices() {
		if(selectIndices == null)
			return null;
		return selectIndices.toArray(new Integer[0]);
	}
	public void setRowSelectIndices(Integer[] selectIndices) {
		if(this.selectIndices == null)
			this.selectIndices = new ArrayList<Integer>();
		else
			this.selectIndices.clear();
		if(selectIndices != null)
			this.selectIndices.addAll(Arrays.asList(selectIndices));
		rowDataChanged = true;
	}
	
	public int getRowIndex(Row row){
		int size = rowList.size();
		for (int i = 0; i < size; i++) {
			if(rowList.get(i).getRowId().equals(row.getRowId()))
				return i;
		}
		return -1;
	}
	
	protected Row[] getDeleteRows() {
		return deleteRowList == null ? null : deleteRowList.toArray(new Row[0]);
	}
	

	public int getRowCount() {
		return rowList.size();
	}
	public void setRowSelectIndex(int index) {
		setRowSelectIndices(new Integer[]{index});
	}
	public void setRowDataSelfChanged(boolean rowDataSelfChanged) {
		this.rowDataSelfChanged = rowDataSelfChanged;
	}
	public void clear() {
//		this.pageindex = 0;
		this.rowList.clear();
		this.rowDataChanged = true;
		this.rowDataSelfChanged = true;
		this.selectIndices = null;
		this.deleteRowList = null;
	}
	public Row getRowById(String id) {
		Iterator<Row> it = this.rowList.iterator();
		while(it.hasNext()){
			Row row = it.next();
			if(row.getRowId().equals(id))
				return row;
		}
		return null;
	}
	
	/**
	 * �ƶ���λ��
	 * 
	 * @param index ԭʼindex
	 * @param targetIndex Ŀ��index
	 * @return
	 */
	public void moveRow(int index, int targetIndex) {
		if (index < 0 || index > this.rowList.size() - 1 || targetIndex < 0 || targetIndex > this.rowList.size() - 1)
			return;
		else if (index == targetIndex)
			return;
		else {
			Row row = this.rowList.get(index);
			this.rowList.remove(index);
			this.rowList.add(targetIndex, row);
			this.setRowDataSelfChanged(true);
		}
	}
	 
	/**
	 * ���н���λ��
	 * @param row1
	 * @param row2
	 */
	public void swapRow(Row row1, Row row2) {
		int index1 = getRowIndex(row1), index2 = getRowIndex(row2);
		if (index1 == -1 || index2 == -1) {
		return;
		}
		rowList.set(index1, row2);
		rowList.set(index2, row1);
		this.setRowDataSelfChanged(true);
	}

	
}
