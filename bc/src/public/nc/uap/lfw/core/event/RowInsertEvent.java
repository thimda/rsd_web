package nc.uap.lfw.core.event;

import nc.uap.lfw.core.data.Dataset;

public class RowInsertEvent extends DatasetEvent {
	// 插入行序号
	private Integer insertedIndex;
	
	public RowInsertEvent(Dataset ds) {
		super(ds);
	}
	
	public Integer getInsertedIndex() {
		return insertedIndex;
	}
	
	public void setInsertedIndex(Integer insertedIndex) {
		this.insertedIndex = insertedIndex;
	}

}
