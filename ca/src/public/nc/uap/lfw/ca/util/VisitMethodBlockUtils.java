/**
 * 
 */
package nc.uap.lfw.ca.util;

import java.util.ArrayList;
import java.util.List;


import nc.uap.lfw.ca.ast.BlockSymbol;
import nc.uap.lfw.ca.ast.ExpressionSymbol;
import nc.uap.lfw.ca.ast.IfStatementSymbol;
import nc.uap.lfw.ca.ast.StatementSymbol;
import nc.uap.lfw.ca.ast.Symbol;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;
import nc.uap.lfw.ca.jdt.core.dom.Block;
import nc.uap.lfw.ca.jdt.core.dom.Expression;
import nc.uap.lfw.ca.jdt.core.dom.IfStatement;
import nc.uap.lfw.ca.jdt.core.dom.SimpleName;
import nc.uap.lfw.ca.jdt.core.dom.Statement;


/**
 * @author chouhl
 *
 */
public class VisitMethodBlockUtils {

	public static List<Symbol> list = new ArrayList<Symbol>();
	
	public static void visitMethodBody(Block b,BlockSymbol bs){
		Symbol s = new Symbol();
		s.setOriginalValue("{");
		list.add(s);
		
		bs.setOriginalValue(b.toString());
		List<Statement> stList = b.statements();
		for(Statement st:stList){
			bs.getStatementSymbolList().add(visitStatement(st));
		}
		
		Symbol s1 = new Symbol();
		s1.setOriginalValue("}");
		list.add(s1);
	}
	
	private static StatementSymbol visitStatement(Statement st){
		StatementSymbol ss = null;
		switch(st.getNodeType()){
			case ASTNode.BLOCK:
				Block b = (Block)st;
				
				ss = new StatementSymbol();
				
				
				break;
			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
				break;
			case ASTNode.IF_STATEMENT:
				IfStatement ifst = (IfStatement)st;
				
				Symbol s = new Symbol();
				s.setOriginalValue("if");
				list.add(s);
				
				ss = new IfStatementSymbol();

				ss.setStatementType(st.getNodeType());
				ss.setOriginalValue(ifst.toString());
				
				Symbol s1 = new Symbol();
				s1.setOriginalValue("(");
				list.add(s1);
				ss.setExpressionSymbol(visitExpression(ifst.getExpression()));
				Symbol s2 = new Symbol();
				s1.setOriginalValue(")");
				list.add(s2);
				
				visitMethodBody((Block)ifst.getThenStatement(),new BlockSymbol());
				
				Symbol s5 = new Symbol();
				s1.setOriginalValue("else");
				list.add(s5);
				
				visitMethodBody((Block)ifst.getElseStatement(),new BlockSymbol());
				
				//ifst.getExpression()
				break;
			default:
				break;
		}
		return ss;
	}
	
	private static ExpressionSymbol visitExpression(Expression en){
		ExpressionSymbol es = null;
		switch(en.getNodeType()){
			case ASTNode.SIMPLE_NAME:
				SimpleName sn = (SimpleName)en;
				
				Symbol s1 = new Symbol();
				s1.setOriginalValue(sn.getFullyQualifiedName());
				list.add(s1);
				
				es = new ExpressionSymbol();
				Symbol sl = new Symbol();
				sl.setOriginalValue(sn.getFullyQualifiedName());
				sl.setType(1);
				es.getSymbolList().add(sl);
				break;
			default:
				break;
		}
		return es;
	}
}
