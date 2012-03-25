package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * ����Level����
 * ����Level����Level��Ӧ��dataset�����ݼ�û�и��ӹ�ϵ
 * @author dengjt
 *
 */
public class SimpleTreeLevel extends TreeLevel {

	private static final long serialVersionUID = -8998177771506215135L;

	public String getType() {
		return TREE_LEVEL_TYPE_SIMPLE;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
}
