package nc.uap.lfw.core.comp;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 递归Level定义
 * 递归树Level指此level对应的dataset行数据间有父子关系
 * @author dengjt
 *
 */
public class RecursiveTreeLevel extends TreeLevel {

	private static final long serialVersionUID = 5754554948104868625L;
	/**
	 * 递归主键字段(level层内的参数)
	 */
	protected String recursiveKeyField;
	
	/**
	 * 递归父主键字段(level层内的参数)
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
