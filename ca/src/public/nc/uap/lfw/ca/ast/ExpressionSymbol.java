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
	 * 表达式原始值
	 */
	private String originalValue;
	/**
	 * 表达式翻译值
	 */
	private String targetValue;
	/**
	 * 表达式包含的符号
	 */
	private List<Symbol> symbolList = new ArrayList<Symbol>();
	/**
	 * 表达式包含的表达式
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
