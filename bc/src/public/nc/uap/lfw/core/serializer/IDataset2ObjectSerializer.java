package nc.uap.lfw.core.serializer;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;

/**
 * 数据集到对象模型的序列化器。用于从已有数据模型抽取业务模型
 * @author dengjt
 *
 * @param <T>
 */
public interface IDataset2ObjectSerializer<T> {
	public T serialize(Dataset dataset, Row[] selRows);
	public T serialize(Dataset dataset, Row selRow);
	public T serialize(Dataset dataset);
}
