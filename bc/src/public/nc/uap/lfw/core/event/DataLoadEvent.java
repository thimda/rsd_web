package nc.uap.lfw.core.event;

import nc.uap.lfw.core.data.Dataset;

public class DataLoadEvent extends DatasetEvent {

	private String oldKeyValue;
	private int oldPageIndex;
	public DataLoadEvent(Dataset dataset) {
		super(dataset);
	}
	public String getOriginalKeyValue() {
		return oldKeyValue;
	}
	public void setOriginalKeyValue(String keyValue) {
		this.oldKeyValue = keyValue;
	}
	public int getOriginalPageIndex() {
		return oldPageIndex;
	}
	public void setOriginalPageIndex(int pageIndex) {
		this.oldPageIndex = pageIndex;
	}
}
