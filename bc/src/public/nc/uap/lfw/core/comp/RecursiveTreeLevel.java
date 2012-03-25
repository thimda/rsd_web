package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * �ݹ�Level����
 * �ݹ���Levelָ��level��Ӧ��dataset�����ݼ��и��ӹ�ϵ
 * @author dengjt
 *
 */
public class RecursiveTreeLevel extends TreeLevel {

	private static final long serialVersionUID = 5754554948104868625L;
	/**
	 * �ݹ������ֶ�(level���ڵĲ���)
	 */
	protected String recursiveKeyField;
	
	/**
	 * �ݹ鸸�����ֶ�(level���ڵĲ���)
	 */
	protected String recursivePKeyField;

	
	public String getType() {
		return TREE_LEVEL_TYPE_RECURSIVE;
	}

	public String getRecursiveKeyField() {
		return recursiveKeyField;
	}
	
	public void setRecursiveKeyField(String recursiveKeyField) {
		this.recursiveKeyField = recursiveKeyField;
	}
	
	public String getRecursivePKeyField() {
		return recursivePKeyField;
	}
	
	public void setRecursivePKeyField(String recursiveKeyParameters) {
		this.recursivePKeyField = recursiveKeyParameters;
	}
	
	public void mergeProperties(WebElement ele) {
		throw new LfwRuntimeException("not implemented");
	}
	
}
