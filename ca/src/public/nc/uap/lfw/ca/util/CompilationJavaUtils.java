/**
 * 
 */
package nc.uap.lfw.ca.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.ca.ast.Symbol;
import nc.uap.lfw.ca.dom.JClass;
import nc.uap.lfw.ca.dom.JField;
import nc.uap.lfw.ca.dom.JMethod;
import nc.uap.lfw.ca.dom.JParam;
import nc.uap.lfw.ca.dom.JProgram;
import nc.uap.lfw.ca.dom.JStatement;
import nc.uap.lfw.ca.jdt.core.dom.ASTNode;
import nc.uap.lfw.ca.jdt.core.dom.ASTParser1;
import nc.uap.lfw.ca.jdt.core.dom.AbstractTypeDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.ArrayType;
import nc.uap.lfw.ca.jdt.core.dom.CompilationUnit;
import nc.uap.lfw.ca.jdt.core.dom.Expression;
import nc.uap.lfw.ca.jdt.core.dom.ImportDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.InfixExpression;
import nc.uap.lfw.ca.jdt.core.dom.MethodDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.SingleVariableDeclaration;
import nc.uap.lfw.ca.jdt.core.dom.Type;
import nc.uap.lfw.ca.log.CaLogger;

import org.apache.commons.lang.StringUtils;


/**
 * @author chouhl
 *
 */
public class CompilationJavaUtils {

	public static void visitCompilationUnit(CompilationUnit unit ,JProgram jprogram){
		if(unit == null){
			return;
		}
		
		VisitPackageUtils.visitPackage(unit, jprogram);
		
		VisitImportUtils.visitImport(unit, jprogram);
		
		VisitClassUtils.visitClass(unit, jprogram);
	}
	
	public static CompilationUnit parseJavaToTree(String[] filenames){
		CompilationUnit unit = null;
		if(filenames!=null){
			String content = null;
			for(String filename:filenames){
				try{
					content = FileUtils.readFileToString(new File(filename));
					if(content == null){
						continue;
					}
					unit = parseJavaToTree(content);
					break;
				}catch(Exception e){
					CaLogger.error(e.getMessage());
				}
			}
		}
		return unit;
	}
	
	private static CompilationUnit parseJavaToTree(String content){
		CompilationUnit unit = null;
		char[] ch = new char[content.length()];
		content.getChars(0, content.length(), ch, 0);
		ASTParser1 ast = ASTParser1.newParser();
		ast.setSource(ch);
		unit = (CompilationUnit)ast.createAST(null);
		return unit;
	}
	
	public static String getWholeClassName(List<String> imports,String packageName,String classname){
		String result = null;
		if(classname != null){
			for(String jimport:imports){
				String[] importArrays = jimport.split("\\.");
				String[] classnameArrays = classname.replaceFirst("<.{0,}>", "").split("\\.");
				if(classnameArrays.length == 1){
					if(importArrays[importArrays.length-1].equals(classnameArrays[0])){
						return jimport.substring(0, jimport.lastIndexOf(".")+1) + classname;
					}
				}else{
					if(importArrays.length >= classnameArrays.length){
						boolean temp = false;
						int pos = -1;
						leave:for(int i=importArrays.length-1;i>=0;i--){
							if(importArrays[i].equals(classnameArrays[0])){
								temp = true;
								pos = i;
								for(int j=i;j<importArrays.length;j++){
									if(j-i >= classnameArrays.length){
										temp = false;
										break leave;
									}
									if(!importArrays[j].equals(classnameArrays[j-i])){
										temp = false;
										break leave;
									}
								}
							}
						}
						if(temp){
							StringBuffer importStr = new StringBuffer();
							for(int i = 0;i<pos;i++){
								importStr.append(importArrays[i] + ".");
							}
							importStr.append(classname);
							return importStr.toString();
						}
					}
				}
			}
			if(packageName != null){
				try {
					if(classname.startsWith("java.lang")){
						Class.forName(classname.replaceFirst("<.{0,}>", ""));
						result = classname;
					}else{
						Class.forName("java.lang." + classname.replaceFirst("<.{0,}>", ""));
						if(classname.equals("Integer") || classname.equals("Float") || classname.equals("Double") || classname.equals("Long")
								|| classname.equals("Short") || classname.equals("Byte")){
							result = "java.lang.Number";
						}else{
							result = "java.lang." + classname;
						}
					}
				} catch (Exception e) {
					//当前类不属于java.lang包
					if(classname.equals("byte") || classname.equals("short") || classname.equals("char") || classname.equals("int")
							|| classname.equals("long") || classname.equals("float") || classname.equals("double")){
						result = "java.lang.Number";
					}else if(classname.equals("boolean")){
						result = "java.lang.Boolean";
					}else if(classname.equals("void")){
						result = classname;
					}else{
						if(!classname.startsWith("nc")){
							result = packageName + "." + classname;
						}else{
							result = classname;
						}
					}
				}
			}else{
				result = classname;
			}
		}
		return result;
	}
	
	public static String[] srcpath = {"D:/views/NC_UAP_WEB6.0_dev/NC6_UAP_VOB/NC_UAP_WEB/uapweb/ca/src/test/",
		"D:/views/NC_UAP_WEB6.0_dev/NC6_UAP_VOB/NC_UAP_WEB/uapweb/bc/src/public/",
		"D:/views/NC_UAP_WEB6.0_dev/NC6_UAP_VOB/NC_UAP_WEB/uapweb/ca/src/private/",
		"D:/views/NC_UAP_PORTAL6.0_dev/NC6_UAP_VOB/NC_UAP_PORTAL6.0/pserver/src/public/",
		"D:/views/NC_UAP_PORTAL_POA6.0_dev/NC6_UAP_VOB/NC_UAP_PORTAL_POA/poa/src/public/",
		"D:/views/NC_UAP_WEB6.1_dev/NC6_UAP_VOB/NC_UAP_WEB/uapweb/bcu/src/test/"};
	
	public static String[] getFilename(String classname){
		if(StringUtils.isNotBlank(classname)){
			classname = classname.replaceAll("\\.", "/") + ".java";
			String[] filepath = new String[srcpath.length];
			for(int i=0;i<srcpath.length;i++){
				filepath[i] = srcpath[i] + classname;
			}
			return filepath;
		}
		return null;
	}
	
	public static void dealWithJProgram(JProgram jprogram){
		if(jprogram != null){
			List<JClass> jcs = jprogram.getClassMapToList();
			for(JClass jc:jcs){
				List<JField> jfs = jc.getFieldMapToList();
				for(JField jf:jfs){
					if(jf != null && jf.getFieldname() != null && jf.getFieldname().getOriginalValue() != null && jf.getFieldname().getOriginalValue().trim().length() > 0){
						jc.getFieldTypeMap().put(jf.getFieldname().getOriginalValue(), getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jf.getTypename().getOriginalValue()));
					}
				}
				
//				CaLogger.debug("\n*****类方法*****");
				List<JMethod> jms = jc.getMethodMapToList();
				for(JMethod jm:jms){
//					CaLogger.debug("*****方法参数*****");
					List<JParam> jps = jm.getParamMapToList();
					for(JParam jp:jps){
						jm.getParamTypeMap().put(jp.getParamname().getOriginalValue(), getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jp.getTypename().getOriginalValue().replaceFirst("<.{0,}>", "")));
					}

//					CaLogger.debug("*****方法内部Statement*****");
					List<JStatement> jss = jm.getStatementMapToList();
					for(JStatement js:jss){
//						CaLogger.debug("*****Statement内部变量*****");
						CaLogger.debug("statement:"+js.getStatement());
						List<JField> jfields = js.getFieldMapToList();
						for(JField jf:jfields){
							if(jf != null && jf.getFieldname() != null && jf.getFieldname().getOriginalValue() != null && jf.getFieldname().getOriginalValue().trim().length() > 0 && jf.getTypename() != null){
								jm.getFieldTypeMap().put(jf.getFieldname().getOriginalValue(), getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jf.getTypename().getOriginalValue()));
							}
						}
//						CaLogger.debug("*****Statement内部方法*****");
						String returnTypeName = null;
						List<JMethod> jmes = js.getMethodMapToList();
						JMethod jme = null;
						for(int i=jmes.size()-1;i>=0;i--){
							jme = jmes.get(i);
							String[] ptns = jme.getParamTypeNames();
							for(int j=0;j<ptns.length;j++){
								if(ptns[j] != null && ptns[j].equals("this")){
									ptns[j] = getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jc.getClassname());
								}else if(jm.getMethodTypeMap().get(ptns[j]) != null){
									ptns[j] = jm.getMethodTypeMap().get(ptns[j]);
								}else if(jm.getFieldTypeMap().get(ptns[j]) != null){
									ptns[j] = jm.getFieldTypeMap().get(ptns[j]);
								}else if(jm.getParamTypeMap().get(ptns[j]) != null){
									ptns[j] = jm.getParamTypeMap().get(ptns[j]);
								}else if(jc.getFieldTypeMap().get(ptns[j]) != null){
									ptns[j] = jc.getFieldTypeMap().get(ptns[j]);
								}
							}
							jme.setParamTypeNames(ptns);
							
							String wholeClassName = null;
							if(jme.getObjname() != null && jme.getObjname().getOriginalValue().replaceFirst("this", "").trim().length() > 1){//被其他对象调用
								jme.getObjname().setOriginalValue(jme.getObjname().getOriginalValue().replaceFirst("this.", ""));
								wholeClassName = jm.getMethodTypeMap().get(jme.getObjname().getOriginalValue());
								if(wholeClassName == null){
									wholeClassName = jm.getFieldTypeMap().get(jme.getObjname().getOriginalValue());
								}
								if(wholeClassName == null){
									wholeClassName = jm.getParamTypeMap().get(jme.getObjname().getOriginalValue());
								}
								if(wholeClassName == null){
									wholeClassName = jc.getFieldTypeMap().get(jme.getObjname().getOriginalValue());
								}
								if(wholeClassName == null){
									wholeClassName = jm.getClassTypeMap().get(jme.getObjname().getOriginalValue());
								}
							}else{
								wholeClassName = getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jc.getClassname());
							}
							wholeClassName = getRealWholeClassName(wholeClassName, jme.getMethodname(), jme.getParamTypeNames());
							if(jm.getMethodTypeMap().get(jme.getExpression()) == null){	
								if(wholeClassName != null){
									returnTypeName = getMethodReturnType(wholeClassName, jme.getMethodname(), jme.getParamTypeNames());
									returnTypeName = getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), returnTypeName);
									jme.setWholeClassName(wholeClassName);
								}else{
									CaLogger.error("当前方法"+jme.getMethodname()+"未找到或不存在");
								}
							}else{
								Symbol s = new Symbol();
								s.setOriginalValue(jm.getMethodTypeMap().get(jme.getExpression()));
								s.setType(Symbol.TYPE_NAME);
								jme.setReturnTypeName(s);
								if(wholeClassName != null){
									jme.setWholeClassName(wholeClassName);
								}else{
									CaLogger.error("当前方法"+jme.getMethodname()+"未找到或不存在");
								}
							}
							if(returnTypeName != null){
								jm.getMethodTypeMap().put(jme.getExpression(), returnTypeName);
							}
						}
					}
				}
			}
		}
	}
	
	public static String getRealWholeClassName(String wholeClassName,String methodName,String[] paramTypeNames){
		String result = null;
		if(wholeClassName == null){
			return result;
		}
		String wcn = wholeClassName.replaceFirst("<.{0,}>", "");
			
		if(wcn.startsWith("java")){//当前调用对象为JDK提供。*******暂时规则*******
			return wholeClassName;
		}else{
			String[] filename = CompilationJavaUtils.getFilename(wcn);
			CompilationUnit unit = CompilationJavaUtils.parseJavaToTree(filename);
			if(unit != null){
				String packageName = unit.getPackage().getName().getFullyQualifiedName();
				
				List<ImportDeclaration> ids = unit.imports();
				List<String> imports = new ArrayList<String>();
				for(int i=0;i<ids.size();i++){
					imports.add(ids.get(i).getName().getFullyQualifiedName());
				}
				
				List<AbstractTypeDeclaration> atds = unit.types();
				for(AbstractTypeDeclaration atd:atds){
					Class c = ASTNode.nodeClassForType(atd.getNodeType());
					String superClassName = null;
					try{
						Type t = (Type)c.getMethod("getSuperclassType", null).invoke(atd, null);
						if(t != null){
							superClassName = t.toString().replaceFirst("[<].{1,}[>]", "");
						}
					}catch(Exception e1){
						CaLogger.info(e1.getMessage());
					}
					
					try{
						MethodDeclaration[] mds = (MethodDeclaration[])c.getMethod("getMethods", null).invoke(atd, null);
						MethodDeclaration md = null;
						for(int i=0;i<mds.length;i++){
							md = mds[i];
							if(md.getName().getFullyQualifiedName().equals(methodName)){//方法名称相同
								List<SingleVariableDeclaration> svds = md.parameters();
								if(compareParamType(svds, paramTypeNames,imports,packageName)){
									result = wholeClassName;
									break;
								}
							}
						}
					}catch(Exception e1){CaLogger.info(e1.getMessage());}
					
					if(result == null){//当前类不包含此方法
						if(superClassName != null){//当前类存在父类
							wholeClassName = getWholeClassName(imports, packageName, superClassName);
							result = getRealWholeClassName(wholeClassName, methodName, paramTypeNames);
						}else{
							CaLogger.error("方法"+methodName+"未在相关类"+wholeClassName+"中找到");
						}
					}
				}
			}else{
				CaLogger.error(wholeClassName+"类文件未找到或不存在");
			}
		}
		
		return result;
	}
	
	public static String getMethodReturnType(String wholeClassName,String methodName,String[] paramTypeNames){
		String result = null;
		if(wholeClassName == null){
			return result;
		}
		String wcn = wholeClassName.replaceFirst("<.{0,}>", "");
			
		if(wcn.startsWith("java")){//当前调用对象为JDK提供。*******暂时规则*******
			if(wcn.endsWith("Map")){
				if(methodName.equalsIgnoreCase("get")){
					int begin = wholeClassName.lastIndexOf(",")+1;
					int end = wholeClassName.lastIndexOf(">");
					if(begin != -1 && begin < end){
						result = wholeClassName.substring(begin, end);
					}else{
						result = "java.lang.Object";
					}
				}else if(methodName.equalsIgnoreCase("put")){
					result = "void";
				}
			}else{
				result = "java.lang.Object";
			}
		}else{
			String[] filename = CompilationJavaUtils.getFilename(wcn);
			CompilationUnit unit = CompilationJavaUtils.parseJavaToTree(filename);
			if(unit != null){
				String packageName = unit.getPackage().getName().getFullyQualifiedName();
				
				List<ImportDeclaration> ids = unit.imports();
				List<String> imports = new ArrayList<String>();
				for(int i=0;i<ids.size();i++){
					imports.add(ids.get(i).getName().getFullyQualifiedName());
				}
				
				List<AbstractTypeDeclaration> atds = unit.types();
				for(AbstractTypeDeclaration atd:atds){
					Class c = ASTNode.nodeClassForType(atd.getNodeType());
					String superClassName = null;
					try{
						Type t = (Type)c.getMethod("getSuperclassType", null).invoke(atd, null);
						if(t != null){
							superClassName = t.toString().replaceFirst("[<].{1,}[>]", "");
						}
					}catch(Exception e1){
						CaLogger.info(e1.getMessage());
					}
					
					try{
						MethodDeclaration[] mds = (MethodDeclaration[])c.getMethod("getMethods", null).invoke(atd, null);
						MethodDeclaration md = null;
						for(int i=0;i<mds.length;i++){
							md = mds[i];
							if(md.getName().getFullyQualifiedName().equals(methodName)){//方法名称相同
								List<SingleVariableDeclaration> svds = md.parameters();
								if(compareParamType(svds, paramTypeNames,imports,packageName)){
									result = getJDKClassType(md.getReturnType2(), imports, packageName);
									break;
								}
							}
						}
					}catch(Exception e1){CaLogger.info(e1.getMessage());}
					
					if(result == null){//当前类不包含此方法
						if(superClassName != null){//当前类存在父类
							wholeClassName = getWholeClassName(imports, packageName, superClassName);
							result = getMethodReturnType(wholeClassName, methodName, paramTypeNames);
						}else{
							CaLogger.error("方法"+methodName+"未在相关类"+wholeClassName+"中找到");
						}
					}
				}
			}else{
				CaLogger.error(wholeClassName+"类文件未找到或不存在");
			}
		}
		
		return result;
	}
	
	public static String getExpressionReturnType(Expression exp){
		String result = null;
		if(exp != null){
			switch(exp.getNodeType()){
				case ASTNode.CHARACTER_LITERAL:
				case ASTNode.BOOLEAN_LITERAL:
				case ASTNode.NUMBER_LITERAL:
				case ASTNode.STRING_LITERAL:
				case ASTNode.SIMPLE_NAME:
					result = getJDKClassType(exp);
					break;
				case ASTNode.INFIX_EXPRESSION:
					InfixExpression ie = (InfixExpression)exp;
					Expression leftExp = ie.getLeftOperand();
					String left = getExpressionReturnType(leftExp);
					Expression rightExp = ie.getRightOperand();
					String right = getExpressionReturnType(rightExp);
					//****************暂时逻辑处理*****************
					if("java.lang.String".equals(left) || "java.lang.String".equals(right) ){
						result = "java.lang.String";
					}else{
						result = left + "_" + right;
					}
					break;
				default:
					result = exp.toString();
					break;
			}
		}
		return result;
	}
	
	private static boolean compareParamType(List<SingleVariableDeclaration> svds,String[] paramTypeNames,List<String> imports,String packageName){
		boolean result = false;
		if((svds == null || svds.size() == 0) && (paramTypeNames == null || paramTypeNames.length == 0)){//方法无入参
			result = true;
		}else if(svds != null && paramTypeNames != null && svds.size() == paramTypeNames.length){//方法入参个数相同
			result = true;
			for(int n=0;n<paramTypeNames.length;n++){
				if(paramTypeNames[n].equals("java.lang.Integer") || paramTypeNames[n].equals("java.lang.Short") || paramTypeNames[n].equals("java.lang.Long")
						|| paramTypeNames[n].equals("java.lang.Float") || paramTypeNames[n].equals("java.lang.Double") || paramTypeNames[n].equals("java.lang.Number")){
					if(!getJDKClassType(svds.get(n).getType(), imports, packageName).equals("java.lang.Number")){
						result = false;
						break;
					}
				}else if(!(getJDKClassType(svds.get(n).getType(), imports, packageName).equals(paramTypeNames[n]))){
					result = false;
					break;
				}
			}
		}
		return result;
	}
	
	public static String getJDKClassType(Expression exp){
		String result = null;
		if(exp != null){
			switch(exp.getNodeType()){
				/**************Expression*******************/
				case ASTNode.CHARACTER_LITERAL:
					result = "java.lang.Character";
					break;
				case ASTNode.BOOLEAN_LITERAL:
					result = "java.lang.Boolean";
					break;
				case ASTNode.NUMBER_LITERAL:
					result = "java.lang.Number";
					break;
				case ASTNode.STRING_LITERAL:
					result = "java.lang.String";
					break;
				case ASTNode.SIMPLE_NAME:
					result = exp.toString();
					break;
				default:
					//result = astType.toString();
					break;
			}
		}
		return result;
	}
	
	public static String getJDKClassType(Type type,List<String> imports,String packageName){
		String result = null;
		if(type != null){
			switch(type.getNodeType()){
				/************Type**************/	
				case ASTNode.ARRAY_TYPE:
					ArrayType at = (ArrayType)type;
					result = getWholeClassName(imports, packageName, at.getElementType().toString().replaceFirst("<.{0,}>", "")).replaceFirst(at.getElementType().toString(), at.toString());
					break;
				case ASTNode.SIMPLE_TYPE:
					result = getWholeClassName(imports, packageName, type.toString());
					break;
				case ASTNode.PRIMITIVE_TYPE:
					if(type.toString().equals("byte")){
						result = "java.lang.Number";
					}else if(type.toString().equals("short")){
						result = "java.lang.Number";
					}else if(type.toString().equals("char")){
						result = "java.lang.Character";
					}else if(type.toString().equals("int")){
						result = "java.lang.Number";
					}else if(type.toString().equals("long")){
						result = "java.lang.Number";
					}else if(type.toString().equals("float")){
						result = "java.lang.Number";
					}else if(type.toString().equals("double")){
						result = "java.lang.Number";
					}else if(type.toString().equals("boolean")){
						result = "java.lang.Boolean";
					}else if(type.toString().equals("void")){
						result = "void";
					}else{
						result = type.toString();
					}
					break;
				case ASTNode.QUALIFIED_TYPE:
				case ASTNode.PARAMETERIZED_TYPE:
				case ASTNode.WILDCARD_TYPE:
				default:
					result = type.toString();
					break;
			}
		}
		return result;
	}
}
