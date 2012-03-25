/**
 * 
 */
package nc.uap.lfw.ca.util;

import java.util.List;

import nc.uap.lfw.ca.ast.Symbol;
import nc.uap.lfw.ca.dom.JClass;
import nc.uap.lfw.ca.dom.JParam;
import nc.uap.lfw.ca.dom.JProgram;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;
import nc.uap.lfw.ca.jdt.core.dom.AbstractTypeDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Annotation;
import nc.uap.lfw.ca.jdt.core.dom.AnonymousClassDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.ArrayInitializer;
import nc.uap.lfw.ca.jdt.core.dom.ArrayType;
import nc.uap.lfw.ca.jdt.core.dom.Assignment;
import nc.uap.lfw.ca.jdt.core.dom.Block;
import nc.uap.lfw.ca.jdt.core.dom.BodyDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Expression;
import nc.uap.lfw.ca.jdt.core.dom.FieldDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.IExtendedModifier;
import nc.uap.lfw.ca.jdt.core.dom.InfixExpression;
import nc.uap.lfw.ca.jdt.core.dom.MethodDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Name;
import nc.uap.lfw.ca.jdt.core.dom.PostfixExpression;
import nc.uap.lfw.ca.jdt.core.dom.PrefixExpression;
import nc.uap.lfw.ca.jdt.core.dom.PrimitiveType;
import nc.uap.lfw.ca.jdt.core.dom.SimpleName;
import nc.uap.lfw.ca.jdt.core.dom.SingleVariableDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Statement;
import nc.uap.lfw.ca.jdt.core.dom.Type;
import nc.uap.lfw.ca.jdt.core.dom.TypeDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.VariableDeclaration;
import nc.uap.lfw.ca.log.CaLogger;


/**
 * @author chouhl
 *
 */
public class VisitUtils {
	
	public static void visitNames(List<Name> ns ,JProgram jprogram){
		if(ns==null){
			return;
		}
		
		for(Name n:ns){
			visitName(n, jprogram);
		}
	}
	public static void visitName(Name n ,JProgram jprogram){
		if(n==null){
			return;
		}
		
		String value = "";
		
		Class c = ASTNode.nodeClassForType(n.getNodeType());
		
		try{
			Boolean isDeclaration = (Boolean)c.getMethod("isDeclaration", null).invoke(n, null);
			value += "-isDeclaration:"+isDeclaration;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isQualifiedName = (Boolean)c.getMethod("isQualifiedName", null).invoke(n, null);
			value += "-isQualifiedName:"+isQualifiedName;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isSimpleName = (Boolean)c.getMethod("isSimpleName", null).invoke(n, null);
			value += "-isSimpleName:"+isSimpleName;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		//LfwLogger.debug("------------------------------Name------------------------------");
		//LfwLogger.debug(n.toString());
		//LfwLogger.debug("-"+value+"   ---"+n.getClass().getSimpleName());
		
		//SimpleName QualifiedName
		try{
			value = (String)c.getMethod("getFullyQualifiedName", null).invoke(n, null);
			CaseUtils.putNameToJProgram(value, jprogram);
			//LfwLogger.debug("-"+value+"   ---"+n.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//QualifiedName
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(n, null);
			visitName(sn,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//QualifiedName
		try{
			Name name = (Name)c.getMethod("getQualifier", null).invoke(n, null);
			visitName(name,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitTypes(List<Type> ts ,JProgram jprogram){
		if(ts==null){
			return;
		}
		
		for(Type t:ts){
			visitType(t, jprogram);
		}
	}
	public static void visitType(Type t ,JProgram jprogram){
		if(t==null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(t.getNodeType());
		
		String value = "";
		
		try{
			Boolean isArrayType = (Boolean)c.getMethod("isArrayType", null).invoke(t, null);
			value += "-isArrayType:"+isArrayType;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isParameterizedType = (Boolean)c.getMethod("isParameterizedType", null).invoke(t, null);
			value += "-isParameterizedType:"+isParameterizedType;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isPrimitiveType = (Boolean)c.getMethod("isPrimitiveType", null).invoke(t, null);
			value += "-isPrimitiveType:"+isPrimitiveType;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isQualifiedType = (Boolean)c.getMethod("isQualifiedType", null).invoke(t, null);
			value += "-isQualifiedType:"+isQualifiedType;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isSimpleType = (Boolean)c.getMethod("isSimpleType", null).invoke(t, null);
			value += "-isSimpleType:"+isSimpleType;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isWildcardType = (Boolean)c.getMethod("isWildcardType", null).invoke(t, null);
			value += "-isWildcardType:"+isWildcardType;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isUpperBound = (Boolean)c.getMethod("isUpperBound", null).invoke(t, null);
			value += "-isUpperBound:"+isUpperBound;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//LfwLogger.debug("------------------------------Type------------------------------");
		//LfwLogger.debug(t.toString());
		//LfwLogger.debug("-"+value+"   ---"+t.getClass().getSimpleName());
		
		//PrimitiveType
		try{
			PrimitiveType.Code code = (PrimitiveType.Code)c.getMethod("getPrimitiveTypeCode", null).invoke(t, null);
			value += "-Code:"+code.toString();
			CaseUtils.putNameToJProgram(code.toString(), jprogram);
			//LfwLogger.debug("-"+value+"   ---"+t.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ArrayType
		try{
			Type type = (Type)c.getMethod("getComponentType", null).invoke(t, null);
			visitType(type,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ArrayType
		try{
			Type type = (Type)c.getMethod("getElementType", null).invoke(t, null);
			visitType(type,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//QualifiedType
		try{
			SimpleName n = (SimpleName)c.getMethod("getName", null).invoke(t, null);
			visitName(n,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SimpleType
		try{
			Name n = (Name)c.getMethod("getName", null).invoke(t, null);
			visitName(n,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//QualifiedType
		try{
			Type type = (Type)c.getMethod("getQualifier", null).invoke(t, null);
			visitType(type,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ParameterizedType
		try{
			Type type = (Type)c.getMethod("getType", null).invoke(t, null);
			visitType(type,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ParameterizedType
		try{
			List<Type> ts = (List<Type>)c.getMethod("typeArguments", null).invoke(t, null);
			visitTypes(ts,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//WildcardType
		try{
			Type type = (Type)c.getMethod("getBound", null).invoke(t, null);
			visitType(type,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitVariableDeclarations(List<VariableDeclaration> vds ,JProgram jprogram){
		if(vds==null){
			return;
		}
		
		for(VariableDeclaration vd:vds){
			visitVariableDeclaration(vd, jprogram);
		}
	}
	public static void visitVariableDeclaration(VariableDeclaration vd ,JProgram jprogram){
		if(vd==null){
			return;
		}
		
		String value = "";
		
		Class c = ASTNode.nodeClassForType(vd.getNodeType());
		
		try{
			Boolean isVarargs = (Boolean)c.getMethod("isVarargs", null).invoke(vd, null);
			value += "-isVarargs:"+isVarargs;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		///LfwLogger.debug("------------------------------VariableDeclaration------------------------------");
		//LfwLogger.debug(vd.toString());
		//LfwLogger.debug("-"+value+"   ---"+vd.getClass().getSimpleName());
		
		//SingleVariableDeclaration VariableDeclarationFragment
		try{
			Expression exp = (Expression)c.getMethod("getInitializer", null).invoke(vd, null);
			visitExpression(exp,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SingleVariableDeclaration VariableDeclarationFragment
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(vd, null);
			visitName(sn,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SingleVariableDeclaration
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(vd, null);
			visitType(t,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitExpressions(List<Expression> exps ,JProgram jprogram){
		if(exps==null){
			return;
		}
		
		for(Expression exp:exps){
			visitExpression(exp, jprogram);
		}
	}
	public static void visitExpression(Expression exp ,JProgram jprogram){
		if(exp==null){
			return;
		}
		
		String value = "";
		
		Class c = ASTNode.nodeClassForType(exp.getNodeType());
		
		
		try{
			Boolean isResolvedTypeInferredFromExpectedType = (Boolean)c.getMethod("isResolvedTypeInferredFromExpectedType", null).invoke(exp, null);
			value += "-isResolvedTypeInferredFromExpectedType:"+isResolvedTypeInferredFromExpectedType;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//LfwLogger.debug("------------------------------Expression------------------------------");
		//LfwLogger.debug(exp.toString());
		//LfwLogger.debug("-"+value+"   ---"+exp.getClass().getSimpleName());
		//CharacterLiteral StringLiteral
//		try{
//			value = (String)c.getMethod("getEscapedValue", null).invoke(exp, null);
//			LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}

		//CharacterLiteral
		try{
			Character ch = (Character)c.getMethod("charValue", null).invoke(exp, null);
			CaseUtils.putNameToJProgram(String.valueOf(ch), jprogram);
			//LfwLogger.debug("-"+ch+"   -"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//BooleanLiteral
		try{
			Boolean bo = (Boolean)c.getMethod("booleanValue", null).invoke(exp, null);
			CaseUtils.putNameToJProgram(String.valueOf(bo), jprogram);
			//LfwLogger.debug("-"+bo+"   -"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//StringLiteral
		try{
			value = (String)c.getMethod("getLiteralValue", null).invoke(exp, null);
			CaseUtils.putNameToJProgram(value, jprogram);
			//LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ArrayCreation
		try{
			ArrayType at = (ArrayType)c.getMethod("getType", null).invoke(exp, null);
			visitType(at,jprogram);
		} catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeLiteral ClassInstanceCreation CastExpression VariableDeclarationExpression
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(exp, null);
			visitType(t,jprogram);
		} catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ThisExpression SuperFieldAccess SuperMethodInvocation
		try{
			Name n = (Name)c.getMethod("getQualifier", null).invoke(exp, null);
			visitName(n,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SuperFieldAccess FieldAccess MethodInvocation SuperMethodInvocation
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(exp, null);
			visitName(sn,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ClassInstanceCreation MethodInvocation SuperMethodInvocation
		try{
			List<Expression> es = (List<Expression>)c.getMethod("arguments", null).invoke(exp, null);
			switch(exp.getNodeType()){
				case ASTNode.CLASS_INSTANCE_CREATION:
					break;
				case ASTNode.METHOD_INVOCATION:
					break;
				case ASTNode.SUPER_METHOD_INVOCATION:
					break;
				default:
					break;
			}
			visitExpressions(es,jprogram);
		} catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//FieldAccess ParenthesizedExpression ClassInstanceCreation MethodInvocation ConditionalExpression CastExpression
		try{
			Expression exp_next = (Expression)c.getMethod("getExpression", null).invoke(exp, null);
			switch(exp.getNodeType()){
				case ASTNode.FIELD_ACCESS:
					break;
				case ASTNode.PARENTHESIZED_EXPRESSION:
					break;
				case ASTNode.CLASS_INSTANCE_CREATION:
					break;
				case ASTNode.METHOD_INVOCATION:
					Symbol s = new Symbol();
					s.setOriginalValue(exp_next.toString());
//					jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getCurrentMethod().setObjname(s);
//					jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethods().add(new JMethod());
					break;
				case ASTNode.CONDITIONAL_EXPRESSION:
					break;
				case ASTNode.CAST_EXPRESSION:
					break;
				default:
					break;
			}
			visitExpression(exp_next,jprogram);
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
		
		//InfixExpression
		try{
			InfixExpression.Operator o = (InfixExpression.Operator)c.getMethod("getOperator", null).invoke(exp, null);
			value = o.toString();
			//LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//PostfixExpression
		try{
			PostfixExpression.Operator o = (PostfixExpression.Operator)c.getMethod("getOperator", null).invoke(exp, null);
			value = o.toString();
			//LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//PrefixExpression
		try{
			PrefixExpression.Operator o = (PrefixExpression.Operator)c.getMethod("getOperator", null).invoke(exp, null);
			value = o.toString();
			//LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ClassInstanceCreation
		try{
			AnonymousClassDeclaration acd = (AnonymousClassDeclaration)c.getMethod("getAnonymousClassDeclaration", null).invoke(exp, null);
			visitAnonymousClassDeclaration(acd, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ClassInstanceCreation MethodInvocation SuperMethodInvocation
		try{
			List<Type> ts = (List<Type>)c.getMethod("typeArguments", null).invoke(exp, null);
			visitTypes(ts,jprogram);
		} catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ArrayCreation
		try{
			List<Expression> es = (List<Expression>)c.getMethod("dimensions", null).invoke(exp, null);
			visitExpressions(es,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ArrayCreation
		try{
			ArrayInitializer i = (ArrayInitializer)c.getMethod("getInitializer", null).invoke(exp, null);
			visitExpression(i,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ArrayInitializer
		try{
			List<Expression> es = (List<Expression>)c.getMethod("expressions", null).invoke(exp, null);
			visitExpressions(es,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ArrayAccess
		try{
			Expression exp_next = (Expression)c.getMethod("getArray", null).invoke(exp, null);
			visitExpression(exp_next,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ArrayAccess
		try{
			Expression exp_next = (Expression)c.getMethod("getIndex", null).invoke(exp, null);
			visitExpression(exp_next,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//InfixExpression
		try{
			List<Expression> es = (List<Expression>)c.getMethod("extendedOperands", null).invoke(exp, null);
			visitExpressions(es,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//InfixExpression InstanceofExpression
		try{
			Expression exp_next = (Expression)c.getMethod("getLeftOperand", null).invoke(exp, null);
			visitExpression(exp_next,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//InfixExpression
		try{
			Expression exp_next = (Expression)c.getMethod("getRightOperand", null).invoke(exp, null);
			visitExpression(exp_next,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ConditionalExpression
		try{
			Expression exp_next = (Expression)c.getMethod("getThenExpression", null).invoke(exp, null);
			visitExpression(exp_next,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ConditionalExpression
		try{
			Expression exp_next = (Expression)c.getMethod("getElseExpression", null).invoke(exp, null);
			visitExpression(exp_next,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//PostfixExpression PrefixExpression
		try{
			Expression exp_next = (Expression)c.getMethod("getOperand", null).invoke(exp, null);
			visitExpression(exp_next,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//VariableDeclarationExpression
		try{
			List vdfs = (List)c.getMethod("fragments", null).invoke(exp, null);
			visitVariableDeclarations(vdfs,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//NumberLiteral
		try{
			value = (String)c.getMethod("getToken", null).invoke(exp, null);
			//LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SimpleName
		try{
			value = (String)c.getMethod("getFullyQualifiedName", null).invoke(exp, null);
			//LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitStatements(List<Statement> states ,JProgram jprogram){
		if(states==null){
			return;
		}
		
		for(Statement state:states){
			visitStatement(state, jprogram);
		}
	}
	public static void visitStatement(Statement state ,JProgram jprogram){
		if(state==null){
			return;
		}
		
		String value = "";
		
		Class c = ASTNode.nodeClassForType(state.getNodeType());
		
		//LfwLogger.debug("------------------------------Statement------------------------------");
		///LfwLogger.debug(state.toString());
		//LfwLogger.debug("-"+value+"   ---"+state.getClass().getSimpleName());
		
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
			visitExpressions(es,jprogram);
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
			visitName(sn,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//AssertStatement
		try{
			Expression exp = (Expression)c.getMethod("getMessage", null).invoke(state, null);
			visitExpression(exp,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//VariableDeclarationStatement
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(state, null);
			visitType(t,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//VariableDeclarationStatement
		try{
			List vdfs = (List)c.getMethod("fragments", null).invoke(state, null);
			visitVariableDeclarations(vdfs,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeDeclarationStatement
		try{
			AbstractTypeDeclaration atd = (AbstractTypeDeclaration)c.getMethod("getDeclaration", null).invoke(state, null);
			visitBodyDeclaration(atd,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		
		//ConstructorInvocation SuperConstructorInvocation
		try{
			List<Expression> es = (List<Expression>)c.getMethod("arguments", null).invoke(state, null);
			visitExpressions(es,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ConstructorInvocation SuperConstructorInvocation
		try{
			List<Type> ts = (List<Type>)c.getMethod("typeArguments", null).invoke(state, null);
			visitTypes(ts,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
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
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitAbstractTypeDeclarations(List<AbstractTypeDeclaration> atds ,JProgram jprogram){
		if(atds == null){
			return;
		}
		for(AbstractTypeDeclaration atd:atds){
			visitAbstractTypeDeclaration(atd,jprogram);
		}
	}
	public static void visitAbstractTypeDeclaration(AbstractTypeDeclaration atd ,JProgram jprogram){
		if(atd == null){
			return;
		}
		
		String value = "";
		
		Class c = ASTNode.nodeClassForType(atd.getNodeType());
		
		try{
			Boolean isInterface = (Boolean)c.getMethod("isInterface", null).invoke(atd);
			value += "-isInterface:"+isInterface;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isLocalTypeDeclaration = (Boolean)c.getMethod("isLocalTypeDeclaration", null).invoke(atd, null);
			value += "-isLocalTypeDeclaration:"+isLocalTypeDeclaration;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isMemberTypeDeclaration = (Boolean)c.getMethod("isMemberTypeDeclaration", null).invoke(atd, null);
			value += "-isMemberTypeDeclaration:"+isMemberTypeDeclaration;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isPackageMemberTypeDeclaration = (Boolean)c.getMethod("isPackageMemberTypeDeclaration", null).invoke(atd, null);
			value += "-isPackageMemberTypeDeclaration:"+isPackageMemberTypeDeclaration;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		//LfwLogger.debug("------------------------------AbstractTypeDeclaration------------------------------");
		//LfwLogger.debug(atd.toString());
		//LfwLogger.debug("-"+value+"   ---"+atd.getClass().getSimpleName());
		
		//TypeDeclaration EnumDeclaration AnnotationTypeDeclaration
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(atd, null);
			switch(atd.getNodeType()){
				case ASTNode.TYPE_DECLARATION:
					JClass jc = new JClass();
					jc.setClassname(sn.getFullyQualifiedName());
					jprogram.getClassMap().put(CompilationJavaUtils.getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jc.getClassname()), jc);
					jprogram.setCurrentClassKey(CompilationJavaUtils.getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jc.getClassname()));
					break;
				default:
					break;
			}
			visitName(sn,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeDeclaration
		try{
			FieldDeclaration[] fds = (FieldDeclaration[])c.getMethod("getFields", null).invoke(atd, null);
			switch(atd.getNodeType()){
				case ASTNode.TYPE_DECLARATION:
					for(FieldDeclaration fd:fds){
						visitBodyDeclaration(fd,jprogram);
					}
					break;
				default:
					for(FieldDeclaration fd:fds){
						visitBodyDeclaration(fd,jprogram);
					}
					break;
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeDeclaration
		try{
			MethodDeclaration[] mds = (MethodDeclaration[])c.getMethod("getMethods", null).invoke(atd, null);
			switch(atd.getNodeType()){
				case ASTNode.TYPE_DECLARATION:
					for(MethodDeclaration md:mds){
						visitBodyDeclaration(md,jprogram);
					}
					break;
				default:
					for(MethodDeclaration md:mds){
						visitBodyDeclaration(md,jprogram);
					}
					break;
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeDeclaration EnumDeclaration AnnotationTypeDeclaration
		try{
			List<BodyDeclaration> bds = (List<BodyDeclaration>)c.getMethod("bodyDeclarations", null).invoke(atd, null);
			visitBodyDeclarations(bds,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}

		//TypeDeclaration
		try{
			Type t = (Type)c.getMethod("getSuperclassType", null).invoke(atd, null);
			visitType(t,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeDeclaration
		try{
			TypeDeclaration[] tds = (TypeDeclaration[])c.getMethod("getTypes", null).invoke(atd, null);
			for(TypeDeclaration td:tds){
				visitAbstractTypeDeclaration(td,jprogram);
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeDeclaration EnumDeclaration AnnotationTypeDeclaration
		try{
			List<IExtendedModifier> iems = (List<IExtendedModifier>)c.getMethod("modifiers", null).invoke(atd, null);
			for(IExtendedModifier iem:iems){
				//LfwLogger.debug("-"+iem.toString()+"-isAnnotation:"+iem.isAnnotation()+"-isModifier:"+iem.isModifier()+"   ---"+iem.getClass().getSimpleName());
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeDeclaration EnumDeclaration
		try{
			List<Type> ts = (List<Type>)c.getMethod("superInterfaceTypes", null).invoke(atd);
			visitTypes(ts, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeDeclaration
		try{
			List tps = (List)c.getMethod("typeParameters", null).invoke(atd, null);
			visitTypeParameters(tps, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//EnumDeclaration
		try{
			List ecds = (List)c.getMethod("enumConstants", null).invoke(atd);
			visitBodyDeclarations(ecds,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitBodyDeclarations(List<BodyDeclaration> bds ,JProgram jprogram){
		if(bds==null){
			return;
		}
		
		for(BodyDeclaration bd:bds){
			visitBodyDeclaration(bd, jprogram);
		}
	}
	public static void visitBodyDeclaration(BodyDeclaration bd ,JProgram jprogram){
		if(bd==null){
			return;
		}
		
		String value = "";
		
		Class c = ASTNode.nodeClassForType(bd.getNodeType());
		
		try{
			Boolean isConstructor = (Boolean)c.getMethod("isConstructor", null).invoke(bd, null);
			value += "-isConstructor:"+isConstructor;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isVarargs = (Boolean)c.getMethod("isVarargs", null).invoke(bd, null);
			value += "-isVarargs:"+isVarargs;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isLocalTypeDeclaration = (Boolean)c.getMethod("isLocalTypeDeclaration", null).invoke(bd, null);
			value += "-isLocalTypeDeclaration:"+isLocalTypeDeclaration;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isMemberTypeDeclaration = (Boolean)c.getMethod("isMemberTypeDeclaration", null).invoke(bd, null);
			value += "-isMemberTypeDeclaration:"+isMemberTypeDeclaration;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isPackageMemberTypeDeclaration = (Boolean)c.getMethod("isPackageMemberTypeDeclaration", null).invoke(bd, null);
			value += "-isPackageMemberTypeDeclaration:"+isPackageMemberTypeDeclaration;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		//LfwLogger.debug("------------------------------BodyDeclaration------------------------------");
		//LfwLogger.debug(bd.toString());
		//LfwLogger.debug("-"+value+"   ---"+bd.getClass().getSimpleName());
		//FieldDeclaration MethodDeclaration
		try{
			List<IExtendedModifier> iems = (List<IExtendedModifier>)c.getMethod("modifiers", null).invoke(bd, null);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration EnumConstantDeclaration AnnotationTypeMemberDeclaration
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(bd, null);
			switch(bd.getNodeType()){
			case ASTNode.METHOD_DECLARATION:
				break;
			default:
				break;
			}
			visitName(sn,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration
		try{
			Type t = (Type)c.getMethod("getReturnType2", null).invoke(bd, null);
			visitType(t,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration
		try{
			List<VariableDeclaration> vds = (List<VariableDeclaration>)c.getMethod("parameters", null).invoke(bd, null);
			for(VariableDeclaration vd:vds){
				jprogram.getCurrentClass().getCurrentMethod().getParamMap().put(vd.toString(), new JParam());
				jprogram.getCurrentClass().getCurrentMethod().setCurrentParamKey(vd.toString());
				visitVariableDeclaration(vd,jprogram);
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration Initializer
		try{
			Block b = (Block)c.getMethod("getBody", null).invoke(bd, null);
			visitStatement(b,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration
		try{
			List<Name> ns = (List<Name>)c.getMethod("thrownExceptions", null).invoke(bd, null);
			visitNames(ns,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration TypeDeclaration
		try{
			List tps = (List)c.getMethod("typeParameters", null).invoke(bd, null);
			visitTypeParameters(tps, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//FieldDeclaration
		try{
			List vdfs = (List)c.getMethod("fragments", null).invoke(bd, null);
			visitVariableDeclarations(vdfs,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//EnumConstantDeclaration
		try{
			List<Expression> es = (List<Expression>)c.getMethod("arguments", null).invoke(bd, null);
			visitExpressions(es,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//EnumConstantDeclaration
		try{
			AnonymousClassDeclaration acd = (AnonymousClassDeclaration)c.getMethod("getAnonymousClassDeclaration", null).invoke(bd, null);
			visitAnonymousClassDeclaration(acd, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//AnnotationTypeMemberDeclaration
		try{
			Expression exp = (Expression)c.getMethod("getDefault", null).invoke(bd, null);
			visitExpression(exp,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//FieldDeclaration AnnotationTypeMemberDeclaration
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(bd, null);
			switch(bd.getNodeType()){
				case ASTNode.FIELD_DECLARATION:
					break;
				default:
					break;
			}
			visitType(t,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		try {
			visitAbstractTypeDeclaration((AbstractTypeDeclaration)bd, jprogram);
		} catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitCatchClauses(List<ASTNode> nodes ,JProgram jprogram){
		if(nodes==null){
			return;
		}
		
		for(ASTNode node:nodes){
			visitCatchClause(node, jprogram);
		}
	}
	public static void visitCatchClause(ASTNode node ,JProgram jprogram){
		if(node==null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		String value = "";
		
		//LfwLogger.debug("------------------------------CatchClause------------------------------");
		//LfwLogger.debug(node.toString());
		//LfwLogger.debug("-"+value+"   ---"+node.getClass().getSimpleName());
		
		try{
			SingleVariableDeclaration svd = (SingleVariableDeclaration)c.getMethod("getException", null).invoke(node, null);
			visitVariableDeclaration(svd, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		try{
			Block b = (Block)c.getMethod("getBody", null).invoke(node, null);
			visitStatement(b, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitAnonymousClassDeclaration(ASTNode node ,JProgram jprogram){
		if(node==null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		String value = "";
		
		//LfwLogger.debug("------------------------------AnonymousClassDeclaration------------------------------");
		//LfwLogger.debug(node.toString());
		//LfwLogger.debug("-"+value+"   ---"+node.getClass().getSimpleName());
		
		try{
			List<BodyDeclaration> bds = (List<BodyDeclaration>)c.getMethod("bodyDeclarations", null).invoke(node, null);
			visitBodyDeclarations(bds, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitAnnotations(List<Annotation> anns ,JProgram jprogram){
		if(anns==null){
			return;
		}
		
		for(Annotation ann:anns){
			visitAnnotation(ann, jprogram);
		}
	}
	public static void visitAnnotation(Annotation ann ,JProgram jprogram){
		if(ann==null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(ann.getNodeType());
		
		String value = "";
		try{
			Boolean isAnnotation = (Boolean)c.getMethod("isAnnotation", null).invoke(ann, null);
			value += "-isAnnotation:"+isAnnotation;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isMarkerAnnotation = (Boolean)c.getMethod("isMarkerAnnotation", null).invoke(ann, null);
			value += "-isMarkerAnnotation:"+isMarkerAnnotation;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isModifier = (Boolean)c.getMethod("isModifier", null).invoke(ann, null);
			value += "-isModifier:"+isModifier;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isNormalAnnotation = (Boolean)c.getMethod("isNormalAnnotation", null).invoke(ann, null);
			value += "-isNormalAnnotation:"+isNormalAnnotation;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isSingleMemberAnnotation = (Boolean)c.getMethod("isSingleMemberAnnotation", null).invoke(ann, null);
			value += "-isSingleMemberAnnotation:"+isSingleMemberAnnotation;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		//LfwLogger.debug("------------------------------Annotation------------------------------");
		//LfwLogger.debug(ann.toString());
		//LfwLogger.debug("-"+value+"   ---"+ann.getClass().getSimpleName());
		
		//NormalAnnotation MarkerAnnotation SingleMemberAnnotation
		try{
			Name n = (Name)c.getMethod("getTypeName", null).invoke(ann, null);
			visitName(n,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SingleMemberAnnotation
		try{
			Expression e = (Expression)c.getMethod("getValue", null).invoke(ann, null);
			visitExpression(e,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitTypeParameters(List<ASTNode> nodes ,JProgram jprogram){
		if(nodes==null){
			return;
		}
		
		for(ASTNode node:nodes){
			visitTypeParameter(node, jprogram);
		}
	}
	public static void visitTypeParameter(ASTNode node ,JProgram jprogram){
		if(node==null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		String value = "";
		
		//LfwLogger.debug("------------------------------TypeParameter------------------------------");
		//LfwLogger.debug(node.toString());
		//LfwLogger.debug("-"+value+"   ---"+node.getClass().getSimpleName());
		
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(node, null);
			visitName(sn, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		try{
			List<Type> ts = (List<Type>)c.getMethod("typeBounds", null).invoke(node, null);
			visitTypes(ts, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitPackageDeclaration(ASTNode node ,JProgram jprogram){
		if(node==null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		String value = "";
		//LfwLogger.debug("------------------------------PackageDeclaration------------------------------");
		//LfwLogger.debug(node.toString());
		//LfwLogger.debug("-"+value+"   ---"+node.getClass().getSimpleName());
		try{
			List<Annotation> anns = (List<Annotation>)c.getMethod("annotations", null).invoke(node, null);
			visitAnnotations(anns,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		try{
			Name n = (Name)c.getMethod("getName", null).invoke(node, null);
			jprogram.setJpackage(n.getFullyQualifiedName());
			visitName(n, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	public static void visitImportDeclarations(List<ASTNode> nodes ,JProgram jprogram){
		if(nodes==null){
			return;
		}
		
		for(ASTNode node:nodes){
			visitImportDeclaration(node,jprogram);
		}
	}
	public static void visitImportDeclaration(ASTNode node ,JProgram jprogram){
		if(node==null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		String value = "";
		try{
			Boolean isOnDemand = (Boolean)c.getMethod("isOnDemand", null).invoke(node, null);
			value += "-isOnDemand:"+isOnDemand;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		try{
			Boolean isStatic = (Boolean)c.getMethod("isStatic", null).invoke(node, null);
			value += "-isStatic:"+isStatic;
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		//LfwLogger.debug("------------------------------ImportDeclaration------------------------------");
		//LfwLogger.debug(node.toString());
		//LfwLogger.debug("-"+value+"   ---"+node.getClass().getSimpleName());
		
		try{
			Name n = (Name)c.getMethod("getName", null).invoke(node, null);
			jprogram.getJimports().add(n.getFullyQualifiedName());
			visitName(n,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
}
