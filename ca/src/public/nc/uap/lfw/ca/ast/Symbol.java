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
	 * 当前符号所属类型：1-普通字符	2-方法名称	3-类型名称
	 */
	private int type = 1;
	/**
	 * 符号原始值
	 */
	private String originalValue;
	/**
	 * 符号翻译值
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
