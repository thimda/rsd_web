package nc.uap.lfw.core.comp;

import java.io.Serializable;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * 递归Level定义
 * 递归树Level指此level对应的dataset行数据间有父子关系
 *
 */
public class GridTreeLevel extends WebElement implements Serializable,Cloneable {

	private static final long serialVersionUID = 5754554948104868625L;
	/**
	 * 递归主键字段(level层内的参数)
	 */
	protected String recursiveKeyField;
	
	/**
	 * 递归父主键字段(level层内的参数)
	 */
	protected String recursivePKeyField;
	
	//标识当前级缓加载字段
	private String loadField;
	
	//标识是否是叶子节点
	private String leafField;
	/**
	 * 通过该属性指定显示字段,树节点显示的时候自动从该字段中取值并作为节点的标题显示,
	 * 字段可多个,中间以逗号分隔	.
	 */
	private String labelFields;
	
	private String dataset;
	
	// 本层级的父层级
	private GridTreeLevel parentLevel;

	// 本层级的孩子层级,孩子层级可以有并列的多级
	private GridTreeLevel childTreeLevel;

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
	

	public String getLoadField() {
		return loadField;
	}

	public void setLoadField(String loadField) {
		this.loadField = loadField;
	}

	public String getLeafField() {
		return leafField;
	}

	public void setLeafField(String leafField) {
		this.leafField = leafField;
	}

	public String getLabelFields() {
		return labelFields;
	}

	public void setLabelFields(String labelFields) {
		this.labelFields = labelFields;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public GridTreeLevel getParentLevel() {
		return parentLevel;
	}

	public void setParentLevel(GridTreeLevel parentLevel) {
		this.parentLevel = parentLevel;
	}

	public GridTreeLevel getChildTreeLevel() {
		return childTreeLevel;
	}

	public void setChildTreeLevel(GridTreeLevel childTreeLevel) {
		this.childTreeLevel = childTreeLevel;
	}
	
}
