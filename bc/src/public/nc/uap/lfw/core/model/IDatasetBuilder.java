package nc.uap.lfw.core.model;

import java.util.Map;

import nc.uap.lfw.core.data.Dataset;

public interface IDatasetBuilder {
	public Dataset buildDataset(Dataset ds, Map<String, Object> paramMap);
}
