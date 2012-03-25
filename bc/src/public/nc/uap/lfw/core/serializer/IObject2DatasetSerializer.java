package nc.uap.lfw.core.serializer;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;

public interface IObject2DatasetSerializer<T> {
	/**
	 * 本方法默认为对象自身包含状态定义
	 * @param obj
	 * @param ds
	 */
	public void serialize(T obj, Dataset ds);
	public void serialize(T obj, Dataset ds, int state);
	public void serialize(String keyValue, PaginationInfo paginationInfo, T obj, Dataset ds, int state);
}
