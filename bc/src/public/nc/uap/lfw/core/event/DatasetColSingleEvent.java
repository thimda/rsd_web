package nc.uap.lfw.core.event;

import nc.uap.lfw.core.data.Dataset;

/**
 * dataset的某一列批量修改事件
 * @author dingrf
 *
 */

public class DatasetColSingleEvent extends DatasetCellEvent {

	private int[] rowIndices;

	private int colIndex;
	
	private Object[] newValues;
	
	private Object[] oldValues;
	
	public DatasetColSingleEvent(Dataset ds) {
		super(ds);
	}

	public int[] getRowIndices() {
		return rowIndices;
	}

	public void setRowIndices(int[] rowIndices) {
		this.rowIndices = rowIndices;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public Object[] getNewValues() {
		return newValues;
	}

	public void setNewValues(Object[] newValues) {
		this.newValues = newValues;
	}

	public Object[] getOldValues() {
		return oldValues;
	}

	public void setOldValues(Object[] oldValues) {
		this.oldValues = oldValues;
	}
	
}
