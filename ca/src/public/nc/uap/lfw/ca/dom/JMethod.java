/**
 * 
 */
package nc.uap.lfw.ca.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import nc.uap.lfw.ca.ast.Symbol;
import nc.uap.lfw.ca.jdt.core.dom.MethodDeclaration;

/**
 * @author chouhl
 *
 */
public class JMethod {
	//包含当前方法的类
	private String wholeClassName;
	//表达式
	private String expression;
	//访问范围	public protected default private
	private Symbol modifier;
	//Annotation
	private Symbol annotation;
	//方法名称
	private Symbol methodName;
	//方法参数集合
	private String currentParamKey = "";
	
	private Map<String,JParam> paramMap = new LinkedHashMap<String, JParam>();
	//方法内部变量集合
//	private String currentInnerFieldKey = "";
//	
//	private Map<String, JField> innerFieldMap = new LinkedHashMap<String, JField>();
	//方法内部方法集合
//	private String currentInnerMethodKey = "";
//	
//	private Map<String, JMethod> innerMethodMap = new LinkedHashMap<String, JMethod>();
	//方法内部代码
	private String currentStatementKey = "";
	
	private Map<String, JStatement> statementMap = new LinkedHashMap<String, JStatement>();
	//方法返回值类型名称
	private Symbol returnTypeName;
	//调用当前方法的对象名称
	private Symbol objname;
	//方法参数变量MAP key-参数名	value-参数类型
	private Map<String, String> paramTypeMap = new LinkedHashMap<String, String>();
	//方法内部变量MAP key-变量名 value-变量类型
	private Map<String, String> fieldTypeMap = new LinkedHashMap<String, String>();
	//方法内部方法MAP key-方法名+参数 value-方法返回值类型
	private Map<String, String> methodTypeMap = new LinkedHashMap<String, String>();
	//方法内部类MAP key-类名 value-类完整名称
	private Map<String, String> classTypeMap = new LinkedHashMap<String, String>();
	
	/*************翻译时需要的属性****************/
	private String methodname;
	private String[] paramTypeNames;
	/*******************EclipseDom*************************/
	private MethodDeclaration md;
	
	public List<JParam> getParamMapToList(){
		List<JParam> list = new ArrayList<JParam>();
		Iterator<String> keys = this.getParamMap().keySet().iterator();
		while(keys.hasNext()){
			list.add(this.getParamMap().get(keys.next()));
		}
		return list;
	}
	
//	public List<JField> getFieldMapToList(){
//		List<JField> list = new ArrayList<JField>();
//		Iterator<String> keys = this.getInnerFieldMap().keySet().iterator();
//		while(keys.hasNext()){
//			list.add(this.getInnerFieldMap().get(keys.next()));
//		}
//		return list;
//	}
	
	public List<JStatement> getStatementMapToList(){
		List<JStatement> list = new ArrayList<JStatement>();
		Iterator<String> keys = this.getStatementMap().keySet().iterator();
		while(keys.hasNext()){
			list.add(this.getStatementMap().get(keys.next()));
		}
		return list;
	}
	
	
	public JParam getCurrentParam(){
		return this.getParamMap().get(currentParamKey);
	}
	
//	public JField getCurrentInnerField(){
//		return this.getInnerFieldMap().get(currentInnerFieldKey);
//	}
	
//	public JMethod getCurrentInnerMethod(){
//		return this.getInnerMethodMap().get(currentInnerMethodKey);
//	}
	
	public JStatement getCurrentStatement(){
		return this.getStatementMap().get(currentStatementKey);
	}

	public Symbol getModifier() {
		return modifier;
	}

	public void setModifier(Symbol modifier) {
		this.modifier = modifier;
	}

	public Symbol getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Symbol annotation) {
		this.annotation = annotation;
	}

	public Symbol getMethodName() {
		if(methodName == null){
			methodName = new Symbol();
			methodName.setOriginalValue(this.methodname);
			methodName.setType(Symbol.METHOD_NAME);
		}
		return methodName;
	}

	public void setMethodName(Symbol methodName) {
		this.methodName = methodName;
	}

	public Map<String, JParam> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, JParam> paramMap) {
		this.paramMap = paramMap;
	}

//	public Map<String, JField> getInnerFieldMap() {
//		return innerFieldMap;
//	}
//
//	public void setInnerFieldMap(Map<String, JField> innerFieldMap) {
//		this.innerFieldMap = innerFieldMap;
//	}

//	public Map<String, JMethod> getInnerMethodMap() {
//		return innerMethodMap;
//	}
//
//	public void setInnerMethodMap(Map<String, JMethod> innerMethodMap) {
//		this.innerMethodMap = innerMethodMap;
//	}

	public Map<String, JStatement> getStatementMap() {
		return statementMap;
	}

	public void setStatementMap(Map<String, JStatement> statementMap) {
		this.statementMap = statementMap;
	}

	public Symbol getReturnTypeName() {
		return returnTypeName;
	}

	public void setReturnTypeName(Symbol returnTypeName) {
		this.returnTypeName = returnTypeName;
	}

	public Symbol getObjname() {
		return objname;
	}

	public void setObjname(Symbol objname) {
		this.objname = objname;
	}

	public Map<String, String> getFieldTypeMap() {
		return fieldTypeMap;
	}

	public void setFieldTypeMap(Map<String, String> fieldTypeMap) {
		this.fieldTypeMap = fieldTypeMap;
	}

	public String getCurrentParamKey() {
		return currentParamKey;
	}

	public void setCurrentParamKey(String currentParamKey) {
		this.currentParamKey = currentParamKey;
	}

//	public String getCurrentInnerFieldKey() {
//		return currentInnerFieldKey;
//	}
//
//	public void setCurrentInnerFieldKey(String currentInnerFieldKey) {
//		this.currentInnerFieldKey = currentInnerFieldKey;
//	}

//	public String getCurrentInnerMethodKey() {
//		return currentInnerMethodKey;
//	}
//
//	public void setCurrentInnerMethodKey(String currentInnerMethodKey) {
//		this.currentInnerMethodKey = currentInnerMethodKey;
//	}

	public String getCurrentStatementKey() {
		return currentStatementKey;
	}

	public void setCurrentStatementKey(String currentStatementKey) {
		this.currentStatementKey = currentStatementKey;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public String[] getParamTypeNames() {
		return paramTypeNames;
	}

	public void setParamTypeNames(String[] paramTypeNames) {
		this.paramTypeNames = paramTypeNames;
	}

	public Map<String, String> getParamTypeMap() {
		return paramTypeMap;
	}

	public void setParamTypeMap(Map<String, String> paramTypeMap) {
		this.paramTypeMap = paramTypeMap;
	}


	public Map<String, String> getMethodTypeMap() {
		return methodTypeMap;
	}


	public void setMethodTypeMap(Map<String, String> methodTypeMap) {
		this.methodTypeMap = methodTypeMap;
	}


	public Map<String, String> getClassTypeMap() {
		return classTypeMap;
	}


	public void setClassTypeMap(Map<String, String> classTypeMap) {
		this.classTypeMap = classTypeMap;
	}


	public String getExpression() {
		return expression;
	}


	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getWholeClassName() {
		return wholeClassName;
	}

	public void setWholeClassName(String wholeClassName) {
		this.wholeClassName = wholeClassName;
	}

	public MethodDeclaration getMd() {
		return md;
	}

	public void setMd(MethodDeclaration md) {
		this.md = md;
	}
	
}
