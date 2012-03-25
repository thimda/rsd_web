package nc.uap.lfw.core.comp;

import java.io.Serializable;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * �ݹ�Level����
 * �ݹ���Levelָ��level��Ӧ��dataset�����ݼ��и��ӹ�ϵ
 *
 */
public class GridTreeLevel extends WebElement implements Serializable,Cloneable {

	private static final long serialVersionUID = 5754554948104868625L;
	/**
	 * �ݹ������ֶ�(level���ڵĲ���)
	 */
	protected String recursiveKeyField;
	
	/**
	 * �ݹ鸸�����ֶ�(level���ڵĲ���)
	 */
	protected String recursivePKeyField;
	
	//��ʶ��ǰ���������ֶ�
	private String loadField;
	
	//��ʶ�Ƿ���Ҷ�ӽڵ�
	private String leafField;
	/**
	 * ͨ��������ָ����ʾ�ֶ�,���ڵ���ʾ��ʱ���Զ��Ӹ��ֶ���ȡֵ����Ϊ�ڵ�ı�����ʾ,
	 * �ֶοɶ��,�м��Զ��ŷָ�	.
	 */
	private String labelFields;
	
	private String dataset;
	
	// ���㼶�ĸ��㼶
	private GridTreeLevel parentLevel;

	// ���㼶�ĺ��Ӳ㼶,���Ӳ㼶�����в��еĶ༶
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
