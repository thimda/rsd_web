/**
 * 
 */
package nc.uap.lfw.ca.util;

import java.util.List;


import nc.uap.lfw.ca.ast.Symbol;
import nc.uap.lfw.ca.dom.JField;
import nc.uap.lfw.ca.dom.JProgram;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;
import nc.uap.lfw.ca.jdt.core.dom.AbstractTypeDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.ArrayType;
import nc.uap.lfw.ca.jdt.core.dom.BodyDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Expression;
import nc.uap.lfw.ca.jdt.core.dom.FieldDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.IExtendedModifier;
import nc.uap.lfw.ca.jdt.core.dom.SimpleName;
import nc.uap.lfw.ca.jdt.core.dom.Type;
import nc.uap.lfw.ca.jdt.core.dom.VariableDeclaration;
import nc.uap.lfw.ca.log.CaLogger;


/**
 * @author chouhl
 *
 */
public class VisitFieldUtils {

	public static void visitFieldDeclarations(List<AbstractTypeDeclaration> atds ,JProgram jprogram){
		if(atds == null){
			return;
		}
		for(AbstractTypeDeclaration atd:atds){
			visitFieldDeclaration(atd,jprogram);
		}
	}
	public static void visitFieldDeclaration(AbstractTypeDeclaration node ,JProgram jprogram){
		if(node == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		/**
		 * 类成员变量 TypeDeclaration
		 */
		try{
			FieldDeclaration[] fds = (FieldDeclaration[])c.getMethod("getFields", null).invoke(node, null);
			switch(node.getNodeType()){
				case ASTNode.TYPE_DECLARATION:
					FieldDeclaration fd = null;
					for(int i=0;i<fds.length;i++){
						fd = fds[i];
						jprogram.getCurrentClass().getFieldMap().put("ClassField"+i, new JField());
						jprogram.getCurrentClass().setCurrentFieldKey("ClassField"+i);
						visitBodyDeclaration(fd,jprogram);
					}
					break;
				default:
					break;
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	private static void visitBodyDeclaration(BodyDeclaration bd ,JProgram jprogram){
		if(bd == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(bd.getNodeType());
		
		//FieldDeclaration MethodDeclaration
		try{
			List<IExtendedModifier> iems = (List<IExtendedModifier>)c.getMethod("modifiers", null).invoke(bd, null);
			for(IExtendedModifier iem:iems){
				if(iem.isModifier()){
					Symbol s = new Symbol();
					s.setOriginalValue(iem.toString());
					jprogram.getCurrentClass().getCurrentField().setModifier(s);
				}
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//FieldDeclaration AnnotationTypeMemberDeclaration
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(bd, null);
			Symbol s = new Symbol();
			s.setOriginalValue(CompilationJavaUtils.getJDKClassType(t, jprogram.getJimports(), jprogram.getJpackage()));
			s.setType(Symbol.TYPE_NAME);
			jprogram.getCurrentClass().getCurrentField().setTypename(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//FieldDeclaration
		try{
			List vdfs = (List)c.getMethod("fragments", null).invoke(bd, null);
			visitVariableDeclarations(vdfs,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
	}
	
	private static void visitVariableDeclarations(List<VariableDeclaration> vds ,JProgram jprogram){
		if(vds == null){
			return;
		}
		
		for(VariableDeclaration vd:vds){
			visitVariableDeclaration(vd, jprogram);
		}
	}
	private static void visitVariableDeclaration(VariableDeclaration vd ,JProgram jprogram){
		if(vd == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(vd.getNodeType());
		
		//SingleVariableDeclaration
//		try{
//			Type t = (Type)c.getMethod("getType", null).invoke(vd, null);
//			jprogram.setCurrentDomName(JProgram.DOM_TYPE_NAME);
//			VisitUtils.visitType(t,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SingleVariableDeclaration VariableDeclarationFragment
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(vd, null);
			Symbol s = new Symbol();
			s.setOriginalValue(sn.getFullyQualifiedName());
			jprogram.getCurrentClass().getCurrentField().setFieldname(s);
			jprogram.getCurrentClass().getFieldTypeMap().put(sn.getFullyQualifiedName(), CompilationJavaUtils.getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jprogram.getCurrentClass().getCurrentField().getTypename().getOriginalValue()));
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SingleVariableDeclaration VariableDeclarationFragment
		try{
			Expression exp = (Expression)c.getMethod("getInitializer", null).invoke(vd, null);
			switch(jprogram.getNodeType()){
				case ASTNode.FIELD_DECLARATION:
					jprogram.setContentType(JProgram.CONTENT_VALUE);
					break;
				default:
					break;
			}
			visitExpression(exp,jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
	
	private static void visitExpression(Expression exp ,JProgram jprogram){
		if(exp == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(exp.getNodeType());
		
		String value = "";
		
		//CharacterLiteral
		try{
			Character ch = (Character)c.getMethod("charValue", null).invoke(exp, null);
			Symbol s = new Symbol();
			s.setOriginalValue(String.valueOf(ch));
			jprogram.getCurrentClass().getCurrentField().setFieldvalue(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//BooleanLiteral
		try{
			Boolean bo = (Boolean)c.getMethod("booleanValue", null).invoke(exp, null);
			Symbol s = new Symbol();
			s.setOriginalValue(String.valueOf(bo));
			jprogram.getCurrentClass().getCurrentField().setFieldvalue(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//NumberLiteral
		try{
			String str = (String)c.getMethod("getToken", null).invoke(exp, null);
			Symbol s = new Symbol();
			s.setOriginalValue(str);
			jprogram.getCurrentClass().getCurrentField().setFieldvalue(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//StringLiteral
		try{
			String str = (String)c.getMethod("getLiteralValue", null).invoke(exp, null);
			Symbol s = new Symbol();
			s.setOriginalValue(str);
			jprogram.getCurrentClass().getCurrentField().setFieldvalue(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ArrayCreation
		try{
			ArrayType at = (ArrayType)c.getMethod("getType", null).invoke(exp, null);
			CaLogger.info(at.toString());
		} catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//TypeLiteral ClassInstanceCreation CastExpression VariableDeclarationExpression
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(exp, null);
			switch(jprogram.getNodeType()){
				case ASTNode.FIELD_DECLARATION:
					Symbol s = new Symbol();
					s.setOriginalValue(CompilationJavaUtils.getJDKClassType(t, jprogram.getJimports(), jprogram.getJpackage()));
					jprogram.getCurrentClass().getCurrentField().setTypename(s);
					break;
				default:
					break;
			}
		} catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ThisExpression SuperFieldAccess SuperMethodInvocation
//		try{
//			Name n = (Name)c.getMethod("getQualifier", null).invoke(exp, null);
//			jprogram.setCurrentDomName(JProgram.DOM_NAME);
//			visitName(n,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SuperFieldAccess FieldAccess MethodInvocation SuperMethodInvocation
//		try{
//			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(exp, null);
//			jprogram.setContentType(JProgram.CONTENT_NAME);
//			switch(jprogram.getNodeType()){
//				case ASTNode.SUPER_FIELD_ACCESS:
//					break;
//				case ASTNode.FIELD_ACCESS:
//					break;
//				case ASTNode.METHOD_INVOCATION:
//					switch(jprogram.getCurrentDomType()){
//						case JProgram.INNER_METHOD_STATEMENT:
//							jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getCurrentMethod().setMethodname(sn.getFullyQualifiedName());
//							break;
//						default:
//							break;
//						}
//					break;
//				case ASTNode.SUPER_METHOD_INVOCATION:
//					break;
//				default:
//					break;
//			}
//			visitName(sn,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//ClassInstanceCreation MethodInvocation SuperMethodInvocation
//		try{
//			List<Expression> es = (List<Expression>)c.getMethod("arguments", null).invoke(exp, null);
//			switch(exp.getNodeType()){
//				case ASTNode.CLASS_INSTANCE_CREATION:
//					break;
//				case ASTNode.METHOD_INVOCATION:
//					jprogram.setCurrentDomType(JProgram.INNER_METHOD_STATEMENT);
//					break;
//				case ASTNode.SUPER_METHOD_INVOCATION:
//					break;
//				default:
//					break;
//			}
//			visitExpressions(es,jprogram);
//		} catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//FieldAccess ParenthesizedExpression ClassInstanceCreation MethodInvocation ConditionalExpression CastExpression
//		try{
//			Expression exp_next = (Expression)c.getMethod("getExpression", null).invoke(exp, null);
//			switch(exp.getNodeType()){
//				case ASTNode.FIELD_ACCESS:
//					break;
//				case ASTNode.PARENTHESIZED_EXPRESSION:
//					break;
//				case ASTNode.CLASS_INSTANCE_CREATION:
//					break;
//				case ASTNode.METHOD_INVOCATION:
//					jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getCurrentMethod().setObjname(exp_next.toString());
//					jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().getMethods().add(new JMethod());
//					break;
//				case ASTNode.CONDITIONAL_EXPRESSION:
//					break;
//				case ASTNode.CAST_EXPRESSION:
//					break;
//				default:
//					break;
//			}
//			jprogram.setCurrentDomName(JProgram.DOM_NAME);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//Assignment
//		try{
//			Expression exp_next = (Expression)c.getMethod("getLeftHandSide", null).invoke(exp, null);
//			jprogram.setCurrentDomName(JProgram.UNKNOWN);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//Assignment
//		try{
//			Expression exp_next = (Expression)c.getMethod("getRightHandSide", null).invoke(exp, null);
//			jprogram.setCurrentDomName(JProgram.UNKNOWN);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//Assignment PostfixExpression PrefixExpression
//		try{
//			Assignment.Operator o = (Assignment.Operator)c.getMethod("getOperator", null).invoke(exp, null);
//			value = o.toString();
//			LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//InfixExpression
//		try{
//			InfixExpression.Operator o = (InfixExpression.Operator)c.getMethod("getOperator", null).invoke(exp, null);jprogram.setCurrentDomType(JProgram.UNKNOWN);
//			value = o.toString();
//			LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//PostfixExpression
//		try{
//			PostfixExpression.Operator o = (PostfixExpression.Operator)c.getMethod("getOperator", null).invoke(exp, null);
//			value = o.toString();
//			LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//PrefixExpression
//		try{
//			PrefixExpression.Operator o = (PrefixExpression.Operator)c.getMethod("getOperator", null).invoke(exp, null);
//			value = o.toString();
//			LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//ClassInstanceCreation
//		try{
//			AnonymousClassDeclaration acd = (AnonymousClassDeclaration)c.getMethod("getAnonymousClassDeclaration", null).invoke(exp, null);
//			jprogram.setCurrentDomName(JProgram.UNKNOWN);
//			visitAnonymousClassDeclaration(acd, jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//ClassInstanceCreation MethodInvocation SuperMethodInvocation
//		try{
//			List<Type> ts = (List<Type>)c.getMethod("typeArguments", null).invoke(exp, null);
//			visitTypes(ts,jprogram);
//		} catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//ArrayCreation
//		try{
//			List<Expression> es = (List<Expression>)c.getMethod("dimensions", null).invoke(exp, null);
//			visitExpressions(es,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//ArrayCreation
//		try{
//			ArrayInitializer i = (ArrayInitializer)c.getMethod("getInitializer", null).invoke(exp, null);
//			visitExpression(i,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//ArrayInitializer
//		try{
//			List<Expression> es = (List<Expression>)c.getMethod("expressions", null).invoke(exp, null);
//			visitExpressions(es,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
////		
//		//ArrayAccess
//		try{
//			Expression exp_next = (Expression)c.getMethod("getArray", null).invoke(exp, null);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
////		
//		//ArrayAccess
//		try{
//			Expression exp_next = (Expression)c.getMethod("getIndex", null).invoke(exp, null);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
////		
//		//InfixExpression
//		try{
//			List<Expression> es = (List<Expression>)c.getMethod("extendedOperands", null).invoke(exp, null);jprogram.setCurrentDomType(JProgram.UNKNOWN);//***
//			visitExpressions(es,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
////		
//		//InfixExpression InstanceofExpression
//		try{
//			Expression exp_next = (Expression)c.getMethod("getLeftOperand", null).invoke(exp, null);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
////		
//		//InfixExpression
//		try{
//			Expression exp_next = (Expression)c.getMethod("getRightOperand", null).invoke(exp, null);jprogram.setCurrentDomType(JProgram.INNER_METHOD_STATEMENT_BLOCK);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
////		
//		//ConditionalExpression
//		try{
//			Expression exp_next = (Expression)c.getMethod("getThenExpression", null).invoke(exp, null);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
////		
//		//ConditionalExpression
//		try{
//			Expression exp_next = (Expression)c.getMethod("getElseExpression", null).invoke(exp, null);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
////		
//		//PostfixExpression PrefixExpression
//		try{
//			Expression exp_next = (Expression)c.getMethod("getOperand", null).invoke(exp, null);
//			visitExpression(exp_next,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//VariableDeclarationExpression
//		try{
//			List vdfs = (List)c.getMethod("fragments", null).invoke(exp, null);
//			visitVariableDeclarations(vdfs,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
//		
//		//SimpleName
//		try{
//			value = (String)c.getMethod("getFullyQualifiedName", null).invoke(exp, null);
//			LfwLogger.debug("-"+value+"   -"+exp.getClass().getSimpleName());
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
}
