package nc.uap.lfw.core.event;

import nc.uap.lfw.core.comp.GridComp;

/**
 * @author guoweic
 *
 */
public class CellEvent extends AbstractServerEvent<GridComp> {
	private int rowIndex;
	private int colIndex;
	private Object newValue;
	private Object oldValue;
	
	public CellEvent(GridComp webElement) {
		super(webElement);
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

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

}
