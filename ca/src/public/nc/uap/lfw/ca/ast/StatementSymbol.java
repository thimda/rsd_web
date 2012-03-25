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
	 * 代码块类型
	 */
	private int statementType;
	/**
	 * 代码块原始值
	 */
	private String originalValue;
	/**
	 * 代码块翻译值
	 */
	private String targetValue;
	/**
	 * 符号
	 */
	private List<Symbol> symbolList;
	/**
	 * 表达式符号
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
