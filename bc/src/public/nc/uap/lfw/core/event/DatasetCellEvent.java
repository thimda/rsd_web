package nc.uap.lfw.core.event;

import nc.uap.lfw.core.data.Dataset;

/**
 * datasetµÄcellÊÂ¼þ
 * @author guoweic
 *
 */
public class DatasetCellEvent extends DatasetEvent {
	
	private int rowIndex;

	private int colIndex;
	
	private Object newValue;
	
	private Object oldValue;
	
	public DatasetCellEvent(Dataset ds) {
		super(ds);
	}
	
	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

}
