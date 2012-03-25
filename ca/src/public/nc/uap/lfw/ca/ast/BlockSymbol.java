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
public class BlockSymbol {
	/**
	 * 方法内部原始值
	 */
	private String originalValue;
	/**
	 * 方法内部翻译值
	 */
	private String targetValue;
	/**
	 * 方法内部代码块
	 */
	private List<StatementSymbol> statementSymbolList = new ArrayList<StatementSymbol>();
	
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
	public List<StatementSymbol> getStatementSymbolList() {
		return statementSymbolList;
	}
	public void setStatementSymbolList(List<StatementSymbol> statementSymbolList) {
		this.statementSymbolList = statementSymbolList;
	}
	
	
}
