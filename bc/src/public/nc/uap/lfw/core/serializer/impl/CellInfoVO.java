package nc.uap.lfw.core.serializer.impl;

/**
 * Ϊ��ʵ��Handler��vo-->dsת���ⲿ�������ȵļ�ǿ��������
 * ��vo����������vo��ds�е�rowת��ʱÿ�����Զ�Ӧ��ϵ��������
 * 
 * @author lkp
 *
 */
public class CellInfoVO {

	/*vo��Ӧ���������ƣ�����Ϊnull,��ʾ��Field�������ڸ�vo*/
	private String voAttributeName;
	
	/*vo��Ӧ������ֵ������Ϊnull,��������������ͬ*/
	private Object voAttributeValue;
	
	/*��Ӧ��ds�е�FieldSet�����ڵ�λ��*/
	private int index;
	
	/*��������*/
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
