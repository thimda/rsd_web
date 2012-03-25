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
	 * ִ�й�ʽ�����ݿͻ��˲��������ԣ�ÿ�ν�ִ��һ����ʽ��
	 * @param valueMap ������ǰ�����е�����ֵ�ԡ�
	 * @param exp ���ʽ
	 * @return ����ִ�н��ֵ�ԡ���ʽ��key:key, value:value)
	 */
	public Map executeFormular(HashMap valueMap, String exp, HashMap dtMap);
	public String[] matchRefPk(String value, String pageUniqueId, String widgetId, String refNodeId);
}
