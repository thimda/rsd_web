/**
 * 
 */
package nc.uap.lfw.ca.util;

import java.util.List;

import nc.uap.lfw.ca.ast.Symbol;
import nc.uap.lfw.ca.dom.JField;
import nc.uap.lfw.ca.dom.JMethod;
import nc.uap.lfw.ca.dom.JParam;
import nc.uap.lfw.ca.dom.JProgram;
import nc.uap.lfw.ca.dom.JStatement;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;
import nc.uap.lfw.ca.jdt.core.dom.Assignment;
import nc.uap.lfw.ca.jdt.core.dom.Block;
import nc.uap.lfw.ca.jdt.core.dom.Expression;
import nc.uap.lfw.ca.jdt.core.dom.MethodInvocation;
import nc.uap.lfw.ca.jdt.core.dom.SimpleName;
import nc.uap.lfw.ca.jdt.core.dom.SingleVariableDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Statement;
import nc.uap.lfw.ca.jdt.core.dom.Type;
import nc.uap.lfw.ca.jdt.core.dom.VariableDeclaration;
import nc.uap.lfw.ca.log.CaLogger;


/**
 * @author chouhl
 *
 */
public class VisitMethodBodyUtils {

	public static void visitMethodBody(Block b ,JProgram jprogram){
		List<Statement> ss = b.statements();
		for(int i=0;i<ss.size();i++){
			jprogram.getCurrentClass().getCurrentMethod().getStatementMap().put("MethodStatement"+i, new JStatement());
			jprogram.getCurrentClass().getCurrentMethod().setCurrentStatementKey("MethodStatement"+i);
			jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().setStatement(ss.get(i).toString());
			jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getFieldMap().put("MethodInnerField"+i, new JField());
			jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().setCurrentFieldMapKey("MethodInnerField"+i);
			visitStatement(ss.get(i), jprogram);
		}
	}
	
	private static void visitStatement(Statement state ,JProgram jprogram){
		if(state == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(state.getNodeType());
		
		//IfStatement ForStatement EnhancedForStatement DoStatement SwitchStatement SynchronizedStatement ReturnStatement ThrowStatement
		//ExpressionStatement AssertStatement SuperConstructorInvocation
		try{
			Expression exp = (Expression)c.getMethod("getExpression", null).invoke(state, null);
			visitExpression(exp,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//IfStatement
		try{
			Statement s = (Statement)c.getMethod("getThenStatement", null).invoke(state, null);
			visitStatement(s,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		//IfStatement
		try{
			Statement s = (Statement)c.getMethod("getElseStatement", null).invoke(state, null);
			visitStatement(s,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}

		//ForStatement
		try{
			List<Expression> es = (List<Expression>)c.getMethod("initializers", null).invoke(state, null);
			for(Expression e:es){
				visitExpression(e, jprogram);
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//EnhancedForStatement
		try{
			SingleVariableDeclaration svd = (SingleVariableDeclaration)c.getMethod("getParameter", null).invoke(state, null);
			visitVariableDeclaration(svd,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TryStatement
		try{
			Block b = (Block)c.getMethod("getFinally", null).invoke(state, null);
			visitStatement(b,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TryStatement
		try{
			List ccs = (List)c.getMethod("catchClauses", null).invoke(state, null);
			visitCatchClauses(ccs, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//BreakStatement ContinueStatement LabeledStatement
		try{
			SimpleName sn = (SimpleName)c.getMethod("getLabel", null).invoke(state, null);
//			visitName(sn,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//AssertStatement
		try{
			Expression exp = (Expression)c.getMethod("getMessage", null).invoke(state, null);
			visitExpression(exp,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//VariableDeclarationStatement
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(state, null);
			Symbol s = new Symbol();
			s.setOriginalValue(CompilationJavaUtils.getJDKClassType(t, jprogram.getJimports(), jprogram.getJpackage()));
			s.setType(Symbol.TYPE_NAME);
			jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getCurrentField().setTypename(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//VariableDeclarationStatement
		try{
			List<VariableDeclaration> vds = (List<VariableDeclaration>)c.getMethod("fragments", null).invoke(state, null);
			for(VariableDeclaration vd:vds){
				visitVariableDeclaration(vd, jprogram);
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeDeclarationStatement
//		try{
//			AbstractTypeDeclaration atd = (AbstractTypeDeclaration)c.getMethod("getDeclaration", null).invoke(state, null);
//			visitBodyDeclaration(atd,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		
		//ConstructorInvocation SuperConstructorInvocation
		try{
			List<Expression> es = (List<Expression>)c.getMethod("arguments", null).invoke(state, null);//****
			for(Expression e:es){
				visitExpression(e, jprogram);
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ConstructorInvocation SuperConstructorInvocation
//		try{
//			List<Type> ts = (List<Type>)c.getMethod("typeArguments", null).invoke(state, null);
//			visitTypes(ts,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ForStatement WhileStatement EnhancedForStatement DoStatement LabeledStatement
		try{
			Statement s = (Statement)c.getMethod("getBody", null).invoke(state, null);
			visitStatement(s,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TryStatement SynchronizedStatement
		try{
			Block b = (Block)c.getMethod("getBody", null).invoke(state, null);
			visitStatement(b,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//Block SwitchStatement
		try{
			List<Statement> ss = (List<Statement>)c.getMethod("statements", null).invoke(state, null);
			for(Statement s:ss){
				visitStatement(s, jprogram);
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	private static void visitVariableDeclaration(VariableDeclaration vd ,JProgram jprogram){
		if(vd == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(vd.getNodeType());
		
		//SingleVariableDeclaration VariableDeclarationFragment
		try{
			Expression exp = (Expression)c.getMethod("getInitializer", null).invoke(vd, null);
			switch(exp.getNodeType()){
				case ASTNode.CLASS_INSTANCE_CREATION:
					jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getCurrentField().setFieldvalue(new Symbol(exp.toString()));
					break;
				default:
					break;
			}
			visitExpression(exp,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SingleVariableDeclaration VariableDeclarationFragment
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(vd, null);
			Symbol s = new Symbol();
			s.setOriginalValue(sn.getFullyQualifiedName());
			jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getCurrentField().setFieldname(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SingleVariableDeclaration
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(vd, null);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	private static void visitExpression(Expression exp ,JProgram jprogram){
		if(exp == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(exp.getNodeType());
		
		String value = "";

		switch(exp.getNodeType()){
			case ASTNode.METHOD_INVOCATION:
				MethodInvocation mi = (MethodInvocation)exp;
				
				String key = "StatementMethod"+jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethodMap().size();
				jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethodMap().put(key, new JMethod());
				jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().setCurrentMethodMapKey(key);
				
				jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethodMap().get(key).setExpression(exp.toString());
				
				jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethodMap().get(key).setMethodname(mi.getName().getFullyQualifiedName());
				List<Expression> argus = mi.arguments();
				String[] paramTypeNames = new String[argus.size()];
				for(int n=0;n<argus.size();n++){//对特殊类型参数进行处理
					switch(argus.get(n).getNodeType()){
						case ASTNode.CHARACTER_LITERAL:
						case ASTNode.BOOLEAN_LITERAL:
						case ASTNode.NUMBER_LITERAL:
						case ASTNode.STRING_LITERAL:
						case ASTNode.SIMPLE_NAME:
							paramTypeNames[n] = CompilationJavaUtils.getJDKClassType(argus.get(n));
							JParam jparam = new JParam();
							jparam.setTypename(new Symbol(paramTypeNames[n],Symbol.TYPE_NAME));
							jparam.setParamValue(new Symbol(argus.get(n).toString()));
							jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethodMap().get(key).getParamMap().put("StatementMethodParam"+n, jparam);
							break;
//						case ASTNode.INFIX_EXPRESSION:
//							paramTypeNames[n] = ((InfixExpression)argus.get(n)).getLeftOperand().getClass().getSimpleName();
//							visitExpression(argus.get(n), jprogram);
//							break;
						default:
							paramTypeNames[n] = CompilationJavaUtils.getExpressionReturnType(argus.get(n));
							JParam jparam1 = new JParam();
							jparam1.setTypename(new Symbol(paramTypeNames[n],Symbol.TYPE_NAME));
							jparam1.setParamValue(new Symbol(argus.get(n).toString()));
							jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethodMap().get(key).getParamMap().put("StatementMethodParam"+n, jparam1);
							visitExpression(argus.get(n), jprogram);
							break;
					}
				}
				jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethodMap().get(key).setParamTypeNames(paramTypeNames);
				if(mi.getExpression() != null){
					Symbol s = new Symbol();
					s.setOriginalValue(mi.getExpression().toString());
					jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethodMap().get(key).setObjname(s);
				}
				visitExpression(mi.getExpression() ,jprogram);
				break;
			default:
				break;
		}
		
		//CharacterLiteral
		try{
			Character ch = (Character)c.getMethod("charValue", null).invoke(exp, null);
			CaLogger.info("VisitMethodBodyUtils:"+ch+":"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//BooleanLiteral
		try{
			Boolean bo = (Boolean)c.getMethod("booleanValue", null).invoke(exp, null);
			CaLogger.info("VisitMethodBodyUtils:"+bo+":"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//NumberLiteral
		try{
			value = (String)c.getMethod("getToken", null).invoke(exp, null);
			CaLogger.info("VisitMethodBodyUtils:"+value+":"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//StringLiteral
		try{
			value = (String)c.getMethod("getLiteralValue", null).invoke(exp, null);
			CaLogger.info("VisitMethodBodyUtils:"+value+":"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
	//	//ArrayCreation
	//	try{
	//		ArrayType at = (ArrayType)c.getMethod("getType", null).invoke(exp, null);
	//		jprogram.setCurrentDomName(JProgram.DOM_TYPE_NAME);
	//		visitType(at,jprogram);
	//	} catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeLiteral ClassInstanceCreation CastExpression VariableDeclarationExpression
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(exp, null);
			CaLogger.info("VisitMethodBodyUtils:"+t.toString()+":"+exp.getClass().getSimpleName());
		} catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ThisExpression SuperFieldAccess SuperMethodInvocation
	//	try
	//		Name n = (Name)c.getMethod("getQualifier", null).invoke(exp, null);
	//		jprogram.setCurrentDomName(JProgram.DOM_NAME);
	//		visitName(n,jprogram);
	//	}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SuperFieldAccess FieldAccess MethodInvocation SuperMethodInvocation
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(exp, null);
			CaLogger.info("VisitMethodBodyUtils:"+sn.getFullyQualifiedName()+":"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ClassInstanceCreation MethodInvocation SuperMethodInvocation
		try{
			List<Expression> es = (List<Expression>)c.getMethod("arguments", null).invoke(exp, null);
			switch(exp.getNodeType()){
				case ASTNode.METHOD_INVOCATION:
					break;
				default:
					for(Expression e:es){
						visitExpression(e, jprogram);
					}
					break;
			}
		} catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//FieldAccess ParenthesizedExpression ClassInstanceCreation MethodInvocation ConditionalExpression CastExpression
		try{
			Expression exp_next = (Expression)c.getMethod("getExpression", null).invoke(exp, null);
			switch(exp.getNodeType()){
				case ASTNode.METHOD_INVOCATION:
					break;
				default:
					visitExpression(exp_next, jprogram);
					break;
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	
		//VariableDeclarationExpression
		try{
			List<VariableDeclaration> vds = (List<VariableDeclaration>)c.getMethod("fragments", null).invoke(exp, null);
			for(VariableDeclaration vd:vds){
				visitVariableDeclaration(vd, jprogram);
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SimpleName
		try{
			value = (String)c.getMethod("getFullyQualifiedName", null).invoke(exp, null);
			jprogram.getCurrentClass().getCurrentMethod().getClassTypeMap().put(value, CompilationJavaUtils.getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), value));
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		visitSpecialExpression(exp ,jprogram);
	}
	
	private static void visitSpecialExpression(Expression exp ,JProgram jprogram){
		if(exp == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(exp.getNodeType());
		
		String value = "";
		
		//InfixExpression InstanceofExpression
		try{
			Expression exp_next = (Expression)c.getMethod("getLeftOperand", null).invoke(exp, null);
			visitExpression(exp_next, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		try{
			Expression exp_next = (Expression)c.getMethod("getRightOperand", null).invoke(exp, null);
			visitExpression(exp_next, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//Assignment
		try{
			Expression exp_next = (Expression)c.getMethod("getLeftHandSide", null).invoke(exp, null);
			visitExpression(exp_next,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//Assignment
		try{
			Expression exp_next = (Expression)c.getMethod("getRightHandSide", null).invoke(exp, null);
			visitExpression(exp_next,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//Assignment PostfixExpression PrefixExpression
		try{
			Assignment.Operator o = (Assignment.Operator)c.getMethod("getOperator", null).invoke(exp, null);
			value = o.toString();
			//LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	private static void visitCatchClauses(List<ASTNode> nodes ,JProgram jprogram){
		if(nodes==null){
			return;
		}
		
		for(ASTNode node:nodes){
			visitCatchClause(node, jprogram);
		}
	}
	private static void visitCatchClause(ASTNode node ,JProgram jprogram){
		if(node==null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		//LfwLogger.debug("------------------------------CatchClause------------------------------");
		//LfwLogger.debug(node.toString());
		
		try{
			SingleVariableDeclaration svd = (SingleVariableDeclaration)c.getMethod("getException", null).invoke(node, null);
			visitVariableDeclaration(svd, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		try{
			Block b = (Block)c.getMethod("getBody", null).invoke(node, null);
			visitStatement(b, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
}
