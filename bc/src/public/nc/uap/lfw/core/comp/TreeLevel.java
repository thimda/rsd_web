package nc.uap.lfw.core.comp;

import java.io.Serializable;
/**
 * 树Level定义。此Level分为三种：
 * 简单树Level，递归树Level，和编码树Level
 * @author dengjt
 *
 */
public abstract class TreeLevel extends WebElement implements IDataBinding,Serializable,Cloneable
{
	private static final long serialVersionUID = 6764402847156490555L;	
	/**
	 * 	TreeLevel的类型，只能是如下两种之一：
	 *	public static final String TREE_LEVEL_TYPE_RECURSIVE="Recursive";
	 *	public static final String TREE_LEVEL_TYPE_SIMPLE="Simple";
	 */
	//	递归层(本层的dataset可以递归构建成树)
	public static final String TREE_LEVEL_TYPE_RECURSIVE = "Recursive";
	//	简单层(dataset是平面)
	public static final String TREE_LEVEL_TYPE_SIMPLE = "Simple";

	private String dataset;
		
	/**
	 * 通过该属性指定显示字段,树节点显示的时候自动从该字段中取值并作为节点的标题显示,
	 * 字段可多个,中间以逗号分隔	.
	 */
	private String labelFields;
	
	/**
	 * 此字段用于指定显示的各个字段间的分割符
	 */
	private String labelDelims;
	
	/**
	 * 用于设定与上层节点间的关系，当该层节点被展开时，系统自动从上层节点绑定的record对象中
	 * 获取masterkeyFields指定的字段的值。该属性可以配置为多个字段，中间用逗号”,”分割。
	 */
	private String masterKeyField;
	
	/**
	 * 用于设定与上层节点间的关系，当该层节点被展开时，系统自动从上层节点绑定的record
	 * 对象中获取masterkeyFields指定的字段的值并作为当前层绑定的dataseet的parameters
	 * 存储起来，parameter的名字就是detailkeyParameters指定的名称，多个参数名用逗号分割。
	 */
	private String detailKeyParameter;
	
	// 本层级的父层级
	private TreeLevel parentLevel;

	// 本层级的孩子层级,孩子层级可以有并列的多级
	private TreeLevel childTreeLevel;
	
	// 通过此属性标识该Level是否有contextMenu
	private String contextMenu;
	
	//标识当前级缓加载字段
	private String loadField;
	
	//标识是否是叶子节点
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
