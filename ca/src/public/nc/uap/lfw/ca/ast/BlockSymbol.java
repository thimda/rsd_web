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
	 * �����ڲ�ԭʼֵ
	 */
	private String originalValue;
	/**
	 * �����ڲ�����ֵ
	 */
	private String targetValue;
	/**
	 * �����ڲ������
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
