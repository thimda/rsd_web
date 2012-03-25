/**
 * 
 */
package nc.uap.lfw.ca.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chouhl
 *
 */
public class ExpressionSymbol {
	/**
	 * ���ʽԭʼֵ
	 */
	private String originalValue;
	/**
	 * ���ʽ����ֵ
	 */
	private String targetValue;
	/**
	 * ���ʽ�����ķ���
	 */
	private List<Symbol> symbolList = new ArrayList<Symbol>();
	/**
	 * ���ʽ�����ı��ʽ
	 */
	private ExpressionSymbol expressionSymbol;
	
	public String getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}
	public List<Symbol> getSymbolList() {
		return symbolList;
	}
	public void setSymbolList(List<Symbol> symbolList) {
		this.symbolList = symbolList;
	}
	public ExpressionSymbol getExpressionSymbol() {
		return expressionSymbol;
	}
	public void setExpressionSymbol(ExpressionSymbol expressionSymbol) {
		this.expressionSymbol = expressionSymbol;
	}
	public String getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
	
	
}
