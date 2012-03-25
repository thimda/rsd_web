package nc.uap.lfw.core.comp;

import java.io.Serializable;
/**
 * ��Level���塣��Level��Ϊ���֣�
 * ����Level���ݹ���Level���ͱ�����Level
 * @author dengjt
 *
 */
public abstract class TreeLevel extends WebElement implements IDataBinding,Serializable,Cloneable
{
	private static final long serialVersionUID = 6764402847156490555L;	
	/**
	 * 	TreeLevel�����ͣ�ֻ������������֮һ��
	 *	public static final String TREE_LEVEL_TYPE_RECURSIVE="Recursive";
	 *	public static final String TREE_LEVEL_TYPE_SIMPLE="Simple";
	 */
	//	�ݹ��(�����dataset���Եݹ鹹������)
	public static final String TREE_LEVEL_TYPE_RECURSIVE = "Recursive";
	//	�򵥲�(dataset��ƽ��)
	public static final String TREE_LEVEL_TYPE_SIMPLE = "Simple";

	private String dataset;
		
	/**
	 * ͨ��������ָ����ʾ�ֶ�,���ڵ���ʾ��ʱ���Զ��Ӹ��ֶ���ȡֵ����Ϊ�ڵ�ı�����ʾ,
	 * �ֶοɶ��,�м��Զ��ŷָ�	.
	 */
	private String labelFields;
	
	/**
	 * ���ֶ�����ָ����ʾ�ĸ����ֶμ�ķָ��
	 */
	private String labelDelims;
	
	/**
	 * �����趨���ϲ�ڵ��Ĺ�ϵ�����ò�ڵ㱻չ��ʱ��ϵͳ�Զ����ϲ�ڵ�󶨵�record������
	 * ��ȡmasterkeyFieldsָ�����ֶε�ֵ�������Կ�������Ϊ����ֶΣ��м��ö��š�,���ָ
	 */
	private String masterKeyField;
	
	/**
	 * �����趨���ϲ�ڵ��Ĺ�ϵ�����ò�ڵ㱻չ��ʱ��ϵͳ�Զ����ϲ�ڵ�󶨵�record
	 * �����л�ȡmasterkeyFieldsָ�����ֶε�ֵ����Ϊ��ǰ��󶨵�dataseet��parameters
	 * �洢������parameter�����־���detailkeyParametersָ�������ƣ�����������ö��ŷָ
	 */
	private String detailKeyParameter;
	
	// ���㼶�ĸ��㼶
	private TreeLevel parentLevel;

	// ���㼶�ĺ��Ӳ㼶,���Ӳ㼶�����в��еĶ༶
	private TreeLevel childTreeLevel;
	
	// ͨ�������Ա�ʶ��Level�Ƿ���contextMenu
	private String contextMenu;
	
	//��ʶ��ǰ���������ֶ�
	private String loadField;
	
	//��ʶ�Ƿ���Ҷ�ӽڵ�
	private String leafField;
	
	public String getDataset() {
		return dataset;
	}
	
	public void setDataset(String datasetId) {
		this.dataset = datasetId;
	}

	
	public abstract String getType();
	
	
	public String getDetailKeyParameter() {
		return detailKeyParameter;
	}
	
	public void setDetailKeyParameter(String detailKeyParameters) {
		this.detailKeyParameter = detailKeyParameters;
	}
	
	public String getLabelFields() {
		return labelFields;
	}
	
	public void setLabelFields(String labelField) {
		this.labelFields = labelField;
	}
	
	public String getMasterKeyField() {
		return masterKeyField;
	}
	
	public void setMasterKeyField(String masterKeyFields) {
		this.masterKeyField = masterKeyFields;
	}
	
	public TreeLevel getParentLevel() {
		return parentLevel;
	}
	
	public String getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(String contextMenu) {
		this.contextMenu = contextMenu;
	}

	public void setParentLevel(TreeLevel parentLevel) {
		this.parentLevel = parentLevel;
	}
	
	public String getLabelDelims() {
		return labelDelims;
	}

	public void setLabelDelims(String labelDelims) {
		this.labelDelims = labelDelims;
	}

	public Object clone()
	{
		TreeLevel level = (TreeLevel) super.clone();
		if(this.childTreeLevel != null){
			level.childTreeLevel = (TreeLevel) this.childTreeLevel.clone();
			level.childTreeLevel.parentLevel = level;
		}
		return level;
		
	}

	public TreeLevel getChildTreeLevel() {
		return childTreeLevel;
	}

	public void setChildTreeLevel(TreeLevel childTreeLevel) {
		this.childTreeLevel = childTreeLevel;
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
}
