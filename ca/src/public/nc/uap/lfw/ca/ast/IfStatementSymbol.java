/**
 * 
 */
package nc.uap.lfw.ca.ast;

/**
 * @author chouhl
 *
 */
public class IfStatementSymbol extends StatementSymbol{
	/**
	 * if�������ʽ
	 */
	private ExpressionSymbol ifExpressionSymbol;
	/**
	 * if�����
	 */
	private StatementSymbol thenStatementSymbol;
	/**
	 * else�����
	 */
	private StatementSymbol elseStatementSymbol;
	
	public ExpressionSymbol getIfExpressionSymbol() {
		return ifExpressionSymbol;
	}
	public void setIfExpressionSymbol(ExpressionSymbol ifExpressionSymbol) {
		this.ifExpressionSymbol = ifExpressionSymbol;
	}
	public StatementSymbol getThenStatementSymbol() {
		return thenStatementSymbol;
	}
	public void setThenStatementSymbol(StatementSymbol thenStatementSymbol) {
		this.thenStatementSymbol = thenStatementSymbol;
	}
	public StatementSymbol getElseStatementSymbol() {
		return elseStatementSymbol;
	}
	public void setElseStatementSymbol(StatementSymbol elseStatementSymbol) {
		this.elseStatementSymbol = elseStatementSymbol;
	}
	
	
}
