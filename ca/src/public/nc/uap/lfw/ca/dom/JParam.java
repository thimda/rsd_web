/**
 * 
 */
package nc.uap.lfw.ca.dom;

import nc.uap.lfw.ca.ast.Symbol;

/**
 * @author chouhl
 *
 */
public class JParam {
	/**
	 * �������������ַ���
	 */
	private Symbol typename;
	/**
	 * ���������ַ���
	 */
	private Symbol paramname;
	/**
	 * ����ֵ�ַ���
	 */
	private Symbol paramValue;
	
	public Symbol getTypename() {
		return typename;
	}
	public void setTypename(Symbol typename) {
		this.typename = typename;
	}
	public Symbol getParamname() {
		return paramname;
	}
	public void setParamname(Symbol paramname) {
		this.paramname = paramname;
	}
	public Symbol getParamValue() {
		return paramValue;
	}
	public void setParamValue(Symbol paramValue) {
		this.paramValue = paramValue;
	}

}
