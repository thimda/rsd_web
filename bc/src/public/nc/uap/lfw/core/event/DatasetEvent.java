package nc.uap.lfw.core.event;

import nc.uap.lfw.core.data.Dataset;

public class DatasetEvent extends AbstractServerEvent<Dataset> {
	public DatasetEvent(Dataset webElement) {
		super(webElement);
	}
}
