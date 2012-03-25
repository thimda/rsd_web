package nc.uap.lfw.ca.test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.ca.dom.JClass;
import nc.uap.lfw.ca.dom.JField;
import nc.uap.lfw.ca.dom.JMethod;
import nc.uap.lfw.ca.dom.JProgram;
import nc.uap.lfw.ca.dom.JStatement;
import nc.uap.lfw.ca.jdt.core.dom.CompilationUnit;
import nc.uap.lfw.ca.log.CaLogger;
import nc.uap.lfw.ca.translate.TranslateUtils;
import nc.uap.lfw.ca.util.CompilationJavaUtils;

public class GWT_UAP{
	
	public static void showMapMsg(Map<String, String> map){
		if(map!=null){
			Iterator<String> ptkeys = map.keySet().iterator();
			String ptkey = null;
			while(ptkeys.hasNext()){
				ptkey = ptkeys.next();
				CaLogger.debug(ptkey+" "+map.get(ptkey));
			}
		}
	}
	
	public static void main(String[] args){
		String classname = "nc.portal.basapp.photocate.PhotoUploadMouseListener";
		String methodname = "onclick";
		
		GWT_UAP gwt = new GWT_UAP();
		gwt.compilerJavaToJavascript(classname, methodname);
	}
	
	private Map<String, JClass> jclassMap = new LinkedHashMap<String, JClass>();
	
	public void compilerJavaToJavascript(String wholeClassName,String methodName){
		
		if(wholeClassName != null && wholeClassName.startsWith("java")){
			return;
		}
		
		wholeClassName = wholeClassName.replaceFirst("<.{0,}>", "");
		
		String[] filename = CompilationJavaUtils.getFilename(wholeClassName);
		
		CompilationUnit unit = CompilationJavaUtils.parseJavaToTree(filename);

		JProgram jp = new JProgram();
		JMethod method = new JMethod();
		method.setWholeClassName(wholeClassName);
		method.setMethodname(methodName);
		jp.setMethod(method);
		
		CompilationJavaUtils.visitCompilationUnit(unit,jp);
		
		CompilationJavaUtils.dealWithJProgram(jp);
		
//		showJProgramMsg(jp);
		
		/******/
		JClass jclass = this.jclassMap.get(wholeClassName);
		if(jclass == null){
			jclass = new JClass();
		}
		jclass.setFieldList(jp.getClassMap().get(wholeClassName).getFieldMapToList());
		//jclass.getMethodList().add(method);
		this.jclassMap.put(wholeClassName, jclass);
		/******/
		
		List<JClass> jcs = jp.getClassMapToList();
		for(JClass jc:jcs){
			List<JMethod> jms = jc.getMethodMapToList();
			for(JMethod jm:jms){
				if(jm.getMethodname().equals(methodName)){
					jclass.getMethodList().add(jm);
//					method = jm;
//					method.setParamTypeNames(jm.getParamTypeNames());
//					method.setStatementMap(jm.getStatementMap());
				}
				List<JStatement> jss = jm.getStatementMapToList();
				for(JStatement js:jss){
					List<JMethod> jmes = js.getMethodMapToList();
					for(JMethod jme:jmes){
						compilerJavaToJavascript(jme.getWholeClassName(), jme.getMethodname(), jme.getParamTypeNames());
					}
				}
			}
		}
		
		CaLogger.debug("��Ҫ����������෽����������");
		Iterator<String> keys = this.jclassMap.keySet().iterator();
		String key = null;
		while(keys.hasNext()){
			key = keys.next();
			JClass jc = this.jclassMap.get(key);
			CaLogger.debug("---�ࣺ"+key);
			if(jc != null){
				
				TranslateUtils.translate(key, jc.getFieldList(), jc.getMethodList());
				
				List<JField> jfs = jc.getFieldList();
				for(JField jf:jfs){
					CaLogger.debug("---�����ԣ�"+jf.getTypename().getOriginalValue() + " --- " + jf.getFieldname().getOriginalValue());
				}
				List<JMethod> jms = jc.getMethodList();
				for(JMethod jm:jms){
					String[] paramTypeNames = jm.getParamTypeNames();
					String param = "";
					if(paramTypeNames != null){
						StringBuffer s = new StringBuffer();
						for(String p:paramTypeNames){
							s.append(p + ",");
						}
						if(s.length() > 0){
							param = s.substring(0, s.length()-1);
						}
					}
					CaLogger.debug("---�෽����"+jm.getMethodname() + "("+param+")");
				}
			}
		}
	}
	
	private void compilerJavaToJavascript(String wholeClassName,String methodName,String[] paramTypeNames){
		
		if(wholeClassName != null && wholeClassName.startsWith("java")){
			return;
		}
		wholeClassName = CompilationJavaUtils.getRealWholeClassName(wholeClassName, methodName, paramTypeNames);
		
		wholeClassName = wholeClassName.replaceFirst("<.{0,}>", "");
		/********/
		JClass jclass = this.jclassMap.get(wholeClassName);
		if(jclass == null){
			jclass = new JClass();
		}else{
			List<JMethod> methods = jclass.getMethodList();
			for(JMethod method:methods){
				if(method.getMethodname() != null && method.getMethodname().equals(methodName)){//��������ͬ
					String[] ptns = method.getParamTypeNames();
					if((ptns == null || ptns.length == 0) && (paramTypeNames == null || paramTypeNames.length == 0)){//���������
						return;
					}else if((ptns != null && paramTypeNames != null && ptns.length == paramTypeNames.length)){//��θ�����ͬ
						boolean temp = true;
						for(int i=0;i<ptns.length;i++){
							if(!(ptns[i] != null && ptns[i].equals(paramTypeNames[i]))){//������Ͳ�ͬ
								temp = false;
								break;
							}
						}
						if(temp){//���������ͬ
							return;
						}
					}
				}
			}
		}
		/******/
		String[] filename = CompilationJavaUtils.getFilename(wholeClassName);
		
		CompilationUnit unit = CompilationJavaUtils.parseJavaToTree(filename);
		
		JProgram jp = new JProgram();
		JMethod method = new JMethod();
		method.setWholeClassName(wholeClassName);
		method.setMethodname(methodName);
		method.setParamTypeNames(paramTypeNames);
		jp.setMethod(method);
		
		CompilationJavaUtils.visitCompilationUnit(unit,jp);
		
		CompilationJavaUtils.dealWithJProgram(jp);
		
//		showJProgramMsg(jp);
		
		/******/
		jclass.setFieldList(jp.getClassMap().get(wholeClassName).getFieldMapToList());
		//jclass.getMethodList().add(method);
		this.jclassMap.put(wholeClassName, jclass);
		/******/
		
		List<JClass> jcs = jp.getClassMapToList();
		for(JClass jc:jcs){
			List<JMethod> jms = jc.getMethodMapToList();
			for(JMethod jm:jms){
				if(jm.getMethodname().equals(methodName)){
					jclass.getMethodList().add(jm);
//					method.setParamMap(jm.getParamMap());
//					method.setStatementMap(jm.getStatementMap());
				}
				List<JStatement> jss = jm.getStatementMapToList();
				for(JStatement js:jss){
					List<JMethod> jmes = js.getMethodMapToList();
					for(JMethod jme:jmes){
						compilerJavaToJavascript(jme.getWholeClassName(), jme.getMethodname(), jme.getParamTypeNames());
					}
				}
			}
		}
	}
	
	private void showJProgramMsg(JProgram jp){
		CaLogger.debug("*********************************************");
		List<JClass> jcs = jp.getClassMapToList();
		for(JClass jc:jcs){
			CaLogger.debug("*****������*****");
			List<JField> jfs = jc.getFieldMapToList();
			for(JField jf:jfs){
				CaLogger.debug(jf.getTypename().getOriginalValue() + " " + jf.getFieldname().getOriginalValue());
			}
			
			CaLogger.debug("*****�෽��*****");
			List<JMethod> jms = jc.getMethodMapToList();
			for(JMethod jm:jms){
				CaLogger.debug("*****��������*****");
				showMapMsg(jm.getParamTypeMap());
				CaLogger.debug("*****�����ڲ�����*****");
				showMapMsg(jm.getFieldTypeMap());
//				CaLogger.debug("*****�����ڲ�����*****");
//				showMapMsg(jm.getMethodTypeMap());
//				CaLogger.debug("*****�����ڲ���*****");
//				showMapMsg(jm.getClassTypeMap());
				
				CaLogger.debug("*****�����ڲ�Statement*****");
				List<JStatement> jss = jm.getStatementMapToList();
				for(JStatement js:jss){
					CaLogger.debug("Դ��"+js.getStatement());
					CaLogger.debug("*****Statement�ڲ�����*****");
					List<JMethod> jmes = js.getMethodMapToList();
					for(JMethod jme:jmes){
						CaLogger.debug("Դ��"+jme.getExpression());
						if(jme.getObjname() != null){
							CaLogger.debug(jme.getObjname().getOriginalValue() + " ���� " + jme.getMethodname());
						}else{
							CaLogger.debug("this ���� " + jme.getMethodname());
						}
						String[] ptns = jme.getParamTypeNames();
						for(String ptn:ptns){
							CaLogger.debug(ptn);
						}
					}
				}
			}
		}
	}
}