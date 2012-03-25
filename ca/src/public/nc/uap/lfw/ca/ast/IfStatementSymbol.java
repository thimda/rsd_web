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
	 * if条件表达式
	 */
	private ExpressionSymbol ifExpressionSymbol;
	/**
	 * if代码块
	 */
	private StatementSymbol thenStatementSymbol;
	/**
	 * else代码块
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
