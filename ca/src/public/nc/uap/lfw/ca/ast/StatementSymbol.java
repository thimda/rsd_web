/**
 * 
 */
package nc.uap.lfw.ca.ast;

import java.util.List;

/**
 * @author chouhl
 *
 */
public class StatementSymbol {
	/**
	 * ���������
	 */
	private int statementType;
	/**
	 * �����ԭʼֵ
	 */
	private String originalValue;
	/**
	 * ����鷭��ֵ
	 */
	private String targetValue;
	/**
	 * ����
	 */
	private List<Symbol> symbolList;
	/**
	 * ���ʽ����
	 */
	private ExpressionSymbol expressionSymbol;
	
	public int getStatementType() {
		return statementType;
	}
	public void setStatementType(int statementType) {
		this.statementType = statementType;
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
	
}
