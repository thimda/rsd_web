/**
 * 
 */
package nc.uap.lfw.ca.util;


import nc.uap.lfw.ca.dom.JClass;
import nc.uap.lfw.ca.dom.JProgram;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;

/**
 * @author chouhl
 *
 */
public class CaseUtils {
	
	public static void putContentToJProgram(String content,JProgram jprogram){
		switch(jprogram.getNodeType()){
			case ASTNode.PACKAGE_DECLARATION:
				jprogram.setJpackage(content);
				break;
			case ASTNode.IMPORT_DECLARATION:
				jprogram.getJimports().add(content);
				break;
			case ASTNode.TYPE_DECLARATION:
				JClass jc = new JClass();
				jc.setClassname(content);
				jprogram.getClassMap().put(CompilationJavaUtils.getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jc.getClassname()), jc);
				jprogram.setCurrentClassKey(CompilationJavaUtils.getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jc.getClassname()));
				break;
			case ASTNode.ENUM_DECLARATION:
			case ASTNode.ANNOTATION_TYPE_DECLARATION:
				break;
			default:
				break;
		}
//		jprogram.setNodeType(JProgram.UNKNOWN);
		jprogram.setContentType(JProgram.UNKNOWN);
	}

	public static void putNameToJProgram(String name,JProgram jprogram){
//		Symbol s = new Symbol();
//		s.setOriginalValue(name);
//		switch(jprogram.getCurrentDomName()){
//			case JProgram.DOM_TYPE_NAME:
//				switch(jprogram.getCurrentDomType()){
//					case JProgram.FIELD:
//						s.setType(3);
//						jprogram.getCurrentClass().getCurrentField().setTypename(s);
//						break;
//					case JProgram.INNER_FIELD:
//						s.setType(3);
////						jprogram.getCurrentClass().getCurrentMethod().getCurrentInnerField().setTypename(s);
//						break;
//					case JProgram.INNER_METHOD:
//						break;
//					case JProgram.METHOD:
////						jprogram.getCurrentClass().getCurrentMethod().setReturntypename(name);
//						break;
//					case JProgram.PARAM:
////						jprogram.getCurrentClass().getCurrentMethod().getCurrentParam().setTypename(name);
//						break;
//					case JProgram.INNER_METHOD_PARAM:
////						jprogram.getCurrentClass().getCurrentMethod().getCurrentInnerMethod().getCurrentParam().setTypename(name);
//						break;
//					case JProgram.INNER_METHOD_STATEMENT:
////						jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().setTypename(name);
//						break;
//					default:
//						break;
//				}
//				break;
//			case JProgram.DOM_NAME:
//				switch(jprogram.getCurrentDomType()){
//					case JProgram.FIELD:
//						jprogram.getCurrentClass().getCurrentField().setFieldname(s);
//						break;
//					case JProgram.INNER_FIELD:
//						jprogram.getCurrentClass().getCurrentMethod().getCurrentInnerField().setFieldname(s);
//						break;
//					case JProgram.INNER_METHOD:
//						jprogram.getCurrentClass().getCurrentMethod().getCurrentInnerMethod().setMethodname(name);
//						break;
//					case JProgram.METHOD:
//						jprogram.getCurrentClass().getCurrentMethod().setMethodname(name);
//						break;
//					case JProgram.PARAM:
//						jprogram.getCurrentClass().getCurrentMethod().getCurrentParam().setParamname(name);
//						break;
//					case JProgram.INNER_METHOD_PARAM:
//						jprogram.getCurrentClass().getCurrentMethod().getCurrentInnerMethod().getCurrentParam().setParamname(name);
//						break;
//					case JProgram.IMPORT:
//						jprogram.getJimports().add(name);
//						break;
//					case JProgram.PACKAGE:
//						jprogram.setJpackage(name);
//						break;
//					case JProgram.INNER_METHOD_STATEMENT:
//						jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().setVarname(name);
//						break;
//					default:
//						break;
//				}
//				break;
//			case JProgram.DOM_VALUE:
//				switch(jprogram.getCurrentDomType()){
//					case JProgram.FIELD:
//						jprogram.getCurrentClass().getCurrentField().setFieldvalue(s);
//						break;
//					case JProgram.INNER_FIELD:
//						jprogram.getCurrentClass().getCurrentMethod().getCurrentInnerField().setFieldvalue(s);
//						break;
//					case JProgram.INNER_METHOD:
//						break;
//					case JProgram.METHOD:
//						break;
//					case JProgram.PARAM:
//						break;
//					case JProgram.INNER_METHOD_STATEMENT:
//						jprogram.getCurrentClass().getCurrentMethod().getCurrentStatement().setValue(name);
//						break;
//					default:
//						break;
//				}
//				break;
//			default:
//				break;
//		}
	}
}
