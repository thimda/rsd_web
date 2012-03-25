/**
 * 
 */
package nc.uap.lfw.ca.dom;

import nc.uap.lfw.ca.ast.Symbol;

/**
 * @author chouhl
 *
 */
public class JField {

	private Symbol modifier;
	
	private Symbol typename;
	
	private Symbol fieldname;
	
	private Symbol fieldvalue;
	
	private String originalValue;
	
	private String targetValue;
	
	/****************翻译时需要的属性*********************/
	private String fieldName;

	public Symbol getModifier() {
		return modifier;
	}

	public void setModifier(Symbol modifier) {
		this.modifier = modifier;
	}

	public Symbol getTypename() {
		return typename;
	}

	public void setTypename(Symbol typename) {
		this.typename = typename;
	}

	public Symbol getFieldname() {
		return fieldname;
	}

	public void setFieldname(Symbol fieldname) {
		this.fieldname = fieldname;
	}

	public Symbol getFieldvalue() {
		return fieldvalue;
	}

	public void setFieldvalue(Symbol fieldvalue) {
		this.fieldvalue = fieldvalue;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
}
