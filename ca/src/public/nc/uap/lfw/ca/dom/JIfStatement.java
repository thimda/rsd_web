/**
 * 
 */
package nc.uap.lfw.ca.dom;

import java.util.List;

import nc.uap.lfw.ca.ast.Symbol;

/**
 * @author chouhl
 *
 */
public class JIfStatement extends JStatement{

	private JStatement ifStatement;
	
	private JStatement elseStatement;
	
	private List<Symbol> symbolList;

	public JStatement getIfStatement() {
		return ifStatement;
	}

	public void setIfStatement(JStatement ifStatement) {
		this.ifStatement = ifStatement;
	}

	public JStatement getElseStatement() {
		return elseStatement;
	}

	public void setElseStatement(JStatement elseStatement) {
		this.elseStatement = elseStatement;
	}

	public List<Symbol> getSymbolList() {
		return symbolList;
	}

	public void setSymbolList(List<Symbol> symbolList) {
		this.symbolList = symbolList;
	}
	
}
