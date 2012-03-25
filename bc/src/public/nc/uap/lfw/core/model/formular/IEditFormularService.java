package nc.uap.lfw.core.model.formular;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * lfw bill service via json
 * @author dengjt
 *
 */
public interface IEditFormularService extends Serializable{
	/**
	 * 执行公式，根据客户端操作的特性，每次仅执行一个公式。
	 * @param valueMap 包含当前操作行的所有值对。
	 * @param exp 表达式
	 * @return 返回执行结果值对。格式（key:key, value:value)
	 */
	public Map executeFormular(HashMap valueMap, String exp, HashMap dtMap);
	public String[] matchRefPk(String value, String pageUniqueId, String widgetId, String refNodeId);
}
