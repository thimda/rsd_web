package nc.uap.lfw.core.serializer;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;

/**
 * ���ݼ�������ģ�͵����л��������ڴ���������ģ�ͳ�ȡҵ��ģ��
 * @author dengjt
 *
 * @param <T>
 */
public interface IDataset2ObjectSerializer<T> {
	public T serialize(Dataset dataset, Row[] selRows);
	public T serialize(Dataset dataset, Row selRow);
	public T serialize(Dataset dataset);
}
