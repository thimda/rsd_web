/**
 * 
 */
package nc.uap.lfw.ca.ast;

/**
 * @author chouhl
 *
 */
public class Symbol {
	
	public static final int NORMAL = 1;
	
	public static final int METHOD_NAME = 2;
	
	public static final int TYPE_NAME = 3;
	
	/**
	 * ��ǰ�����������ͣ�1-��ͨ�ַ�	2-��������	3-��������
	 */
	private int type = 1;
	/**
	 * ����ԭʼֵ
	 */
	private String originalValue;
	/**
	 * ���ŷ���ֵ
	 */
	private String targetValue;

	public Symbol(){}
	
	public Symbol(String originalValue){
		this.originalValue = originalValue;
	}
	
	public Symbol(String originalValue,int type){
		this.originalValue = originalValue;
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public String getTargetValue() {
		if(targetValue == null){
			targetValue = this.getOriginalValue();
		}
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	
}
