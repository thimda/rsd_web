package nc.uap.lfw.core.serializer.impl;

/**
 * 为了实现Handler中vo-->ds转换外部控制力度的加强，增加了
 * 该vo，用于描述vo到ds中的row转换时每个属性对应关系的描述。
 * 
 * @author lkp
 *
 */
public class CellInfoVO {

	/*vo对应的属性名称，可能为null,表示该Field不来自于该vo*/
	private String voAttributeName;
	
	/*vo对应的属性值，可能为null,意义与上属性相同*/
	private Object voAttributeValue;
	
	/*对应的ds中的FieldSet中所在的位置*/
	private int index;
	
	/*数据类型*/
	private String dataType;

	
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getVoAttributeName() {
		return voAttributeName;
	}

	public void setVoAttributeName(String voAttributeName) {
		this.voAttributeName = voAttributeName;
	}

	public Object getVoAttributeValue() {
		return voAttributeValue;
	}

	public void setVoAttributeValue(Object voAttributeValue) {
		this.voAttributeValue = voAttributeValue;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
