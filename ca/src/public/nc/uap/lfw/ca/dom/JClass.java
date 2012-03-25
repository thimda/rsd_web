/**
 * 
 */
package nc.uap.lfw.ca.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chouhl
 *
 */
public class JClass {
	
	private String superclassname;
	
	private String classname;
	
	private String currentFieldKey = "";
	//key-变量名称
	private Map<String, JField> fieldMap = new LinkedHashMap<String, JField>();
	
	private String currentMethodKey = "";
	
	private Map<String, JMethod> methodMap = new LinkedHashMap<String, JMethod>();
	
	//类成员变量MAP key-变量名称	value-变量类型
	private Map<String, String> fieldTypeMap = new LinkedHashMap<String, String>();
	/*************************************翻译时需要的属性*****************************************/
	private List<JField> fieldList = new ArrayList<JField>();
	private List<JMethod> methodList = new ArrayList<JMethod>();
	
	
	public List<JField> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<JField> fieldList) {
		this.fieldList = fieldList;
	}

	public List<JMethod> getMethodList() {
		return methodList;
	}

	public void setMethodList(List<JMethod> methodList) {
		this.methodList = methodList;
	}

	public JField getCurrentField(){
		return this.getFieldMap().get(currentFieldKey);
	}
	
	public JMethod getCurrentMethod(){
		return this.getMethodMap().get(currentMethodKey);
	}
	
	public List<JField> getFieldMapToList(){
		List<JField> list = new ArrayList<JField>();
		Iterator<String> keys = this.getFieldMap().keySet().iterator();
		while(keys.hasNext()){
			list.add(this.getFieldMap().get(keys.next()));
		}
		return list;
	}
	
	public List<JMethod> getMethodMapToList(){
		List<JMethod> list = new ArrayList<JMethod>();
		Iterator<String> keys = this.getMethodMap().keySet().iterator();
		while(keys.hasNext()){
			list.add(this.getMethodMap().get(keys.next()));
		}
		return list;
	}
	
	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getSuperclassname() {
		return superclassname;
	}

	public void setSuperclassname(String superclassname) {
		this.superclassname = superclassname;
	}

	public Map<String, String> getFieldTypeMap() {
		return fieldTypeMap;
	}

	public void setFieldTypeMap(Map<String, String> fieldTypeMap) {
		this.fieldTypeMap = fieldTypeMap;
	}

//	public String getCurrentFieldKey() {
//		return currentFieldKey;
//	}

	public void setCurrentFieldKey(String currentFieldKey) {
		this.currentFieldKey = currentFieldKey;
	}

	public Map<String, JField> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, JField> fieldMap) {
		this.fieldMap = fieldMap;
	}

//	public String getCurrentMethodKey() {
//		return currentMethodKey;
//	}

	public void setCurrentMethodKey(String currentMethodKey) {
		this.currentMethodKey = currentMethodKey;
	}

	public Map<String, JMethod> getMethodMap() {
		return methodMap;
	}

	public void setMethodMap(Map<String, JMethod> methodMap) {
		this.methodMap = methodMap;
	}
	
}
