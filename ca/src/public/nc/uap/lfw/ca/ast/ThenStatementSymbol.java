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
public class ThenStatementSymbol extends StatementSymbol{

	private List<StatementSymbol> statementSymbolList = new ArrayList<StatementSymbol>();

	public List<StatementSymbol> getStatementSymbolList() {
		return statementSymbolList;
	}

	public void setStatementSymbolList(List<StatementSymbol> statementSymbolList) {
		this.statementSymbolList = statementSymbolList;
	}
	
	
}
