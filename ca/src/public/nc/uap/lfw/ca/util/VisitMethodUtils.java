/**
 * 
 */
package nc.uap.lfw.ca.util;

import java.util.Iterator;
import java.util.List;


import nc.uap.lfw.ca.ast.BlockSymbol;
import nc.uap.lfw.ca.ast.Symbol;
import nc.uap.lfw.ca.dom.JClass;
import nc.uap.lfw.ca.dom.JMethod;
import nc.uap.lfw.ca.dom.JParam;
import nc.uap.lfw.ca.dom.JProgram;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;
import nc.uap.lfw.ca.jdt.core.dom.AbstractTypeDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Block;
import nc.uap.lfw.ca.jdt.core.dom.BodyDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Expression;
import nc.uap.lfw.ca.jdt.core.dom.IExtendedModifier;
import nc.uap.lfw.ca.jdt.core.dom.MethodDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Name;
import nc.uap.lfw.ca.jdt.core.dom.SimpleName;
import nc.uap.lfw.ca.jdt.core.dom.SingleVariableDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Type;
import nc.uap.lfw.ca.jdt.core.dom.VariableDeclaration;
import nc.uap.lfw.ca.log.CaLogger;


/**
 * @author chouhl
 *
 */
public class VisitMethodUtils {

	public static void visitMethodDeclarations(List<AbstractTypeDeclaration> atds ,JProgram jprogram){
		if(atds == null){
			return;
		}
		for(AbstractTypeDeclaration atd:atds){
			visitMethodDeclaration(atd,jprogram);
		}
	}
	public static void visitMethodDeclaration(AbstractTypeDeclaration node ,JProgram jprogram){
		if(node == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		/**
		 * 类成员方法 TypeDeclaration
		 */
		try{
			MethodDeclaration[] mds = (MethodDeclaration[])c.getMethod("getMethods", null).invoke(node, null);
			MethodDeclaration md = null;
			for(int i=0;i<mds.length;i++){
				md = mds[i];
				if(md.getName().getFullyQualifiedName().equals(jprogram.getMethod().getMethodname())){
					if(jprogram.getMethod().getParamMap().size() == 0){
//						JClass jc = jprogram.getNeedClassMap().get(jprogram.getJpackage()+"."+jprogram.getCurrentClass().getClassname());
//						if(jc == null){
//							jc = new JClass();
//						}
						JMethod jm = new JMethod();
						jm.setMethodname(md.getName().getFullyQualifiedName());
						List<SingleVariableDeclaration> vds = md.parameters();
						String[] paramTypeNames = new String[vds.size()];
						for(int n=0;n<vds.size();n++){
							paramTypeNames[n] = CompilationJavaUtils.getJDKClassType(vds.get(n).getType(), jprogram.getJimports(), jprogram.getJpackage());
						}
						jm.setParamTypeNames(paramTypeNames);
//						jc.getMethodList().add(jm);//去掉重复方法
						jm.setMd(md);
						jprogram.getCurrentClass().getMethodMap().put("ClassMethod"+i, jm);
						jprogram.getCurrentClass().setCurrentMethodKey("ClassMethod"+i);
						visitBodyDeclaration(md,jprogram);
					}else if(jprogram.getMethod().getParamMap().size() > 0){
						List<SingleVariableDeclaration> vds = md.parameters();
						if(vds.size() == jprogram.getMethod().getParamMap().size()){
							boolean temp = true;
							String[] paramTypeNames = new String[vds.size()];
							for(int n=0;n<vds.size();n++){
								paramTypeNames[n] = CompilationJavaUtils.getJDKClassType(vds.get(n).getType(), jprogram.getJimports(), jprogram.getJpackage());
								if(!paramTypeNames[n].equals(jprogram.getMethod().getParamMap().get("MethodParam"+n))){
									temp = false;
								}
							}
							if(temp){//方法名相同，参数相同
//								JClass jc = jprogram.getNeedClassMap().get(jprogram.getJpackage()+"."+jprogram.getCurrentClass().getClassname());
//								if(jc == null){
//									jc = new JClass();
//								}
								JMethod jm = new JMethod();
								jm.setMethodname(md.getName().getFullyQualifiedName());
								jm.setParamTypeNames(paramTypeNames);
//								jc.getMethodList().add(jm);//去掉重复方法
								jm.setMd(md);
								jprogram.getCurrentClass().getMethodMap().put("ClassMethod"+i, jm);
								jprogram.getCurrentClass().setCurrentMethodKey("ClassMethod"+i);
								visitBodyDeclaration(md,jprogram);
							}
						}
					}
				}
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
				Symbol s = new Symbol();
				s.setOriginalValue(iem.toString());
				if(iem.isModifier()){
					jprogram.getCurrentClass().getCurrentMethod().setModifier(s);
				}
				if(iem.isAnnotation()){
					jprogram.getCurrentClass().getCurrentMethod().setAnnotation(s);
				}
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration EnumConstantDeclaration AnnotationTypeMemberDeclaration
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(bd, null);
			CaLogger.info("VisitMethodUtils:"+bd.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration
		try{
			Type t = (Type)c.getMethod("getReturnType2", null).invoke(bd, null);
			Symbol s = new Symbol();
			s.setOriginalValue(CompilationJavaUtils.getJDKClassType(t, jprogram.getJimports(), jprogram.getJpackage()));
			jprogram.getCurrentClass().getCurrentMethod().setReturnTypeName(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration
		try{
			List<VariableDeclaration> vds = (List<VariableDeclaration>)c.getMethod("parameters", null).invoke(bd, null);
			VariableDeclaration vd = null;
			for(int i=0;i<vds.size();i++){
				vd = vds.get(i);
				jprogram.getCurrentClass().getCurrentMethod().getParamMap().put("MethodParam"+i, new JParam());
				jprogram.getCurrentClass().getCurrentMethod().setCurrentParamKey("MethodParam"+i);
				visitVariableDeclaration(vd,jprogram);
			}
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration Initializer
		try{
			Block b = (Block)c.getMethod("getBody", null).invoke(bd, null);
			VisitMethodBodyUtils.visitMethodBody(b, jprogram);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration
		try{
			List<Name> ns = (List<Name>)c.getMethod("thrownExceptions", null).invoke(bd, null);
			CaLogger.info("VisitMethodUtils:thrownExceptions:"+bd.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//MethodDeclaration TypeDeclaration
		try{
			List tps = (List)c.getMethod("typeParameters", null).invoke(bd, null);
			CaLogger.info("VisitMethodUtils:typeParameters:"+bd.getClass().getSimpleName());
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
		
//		LfwLogger.debug("------------------------------VariableDeclaration------------------------------");
//		LfwLogger.debug(vd.toString());
		
		//SingleVariableDeclaration
		try{
			Type t = (Type)c.getMethod("getType", null).invoke(vd, null);
			Symbol s = new Symbol();
			s.setOriginalValue(CompilationJavaUtils.getJDKClassType(t, jprogram.getJimports(), jprogram.getJpackage()));
			s.setType(Symbol.TYPE_NAME);
			jprogram.getCurrentClass().getCurrentMethod().getCurrentParam().setTypename(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SingleVariableDeclaration VariableDeclarationFragment
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(vd, null);
			Symbol s = new Symbol();
			s.setOriginalValue(sn.getFullyQualifiedName());
			jprogram.getCurrentClass().getCurrentMethod().getCurrentParam().setParamname(s);
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		//SingleVariableDeclaration VariableDeclarationFragment
		try{
			Expression exp = (Expression)c.getMethod("getInitializer", null).invoke(vd, null);
			CaLogger.info("VisitMethodUtils:"+exp.toString()+":"+vd.getClass().getSimpleName());
		}catch(Exception e1){CaLogger.info(e1.getMessage());}
	}
}
