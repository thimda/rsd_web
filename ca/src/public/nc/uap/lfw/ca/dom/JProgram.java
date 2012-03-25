/**
 * 
 */
package nc.uap.lfw.ca.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author chouhl
 *
 */
public class JProgram{
	
	public static final int UNKNOWN = 0;
	
	public static final int IMPORT = 1;
	
	public static final int PACKAGE = 2;
	
	public static final int FIELD = 3;
	
	public static final int METHOD = 4;
	
	public static final int PARAM = 5;
	
	public static final int INNER_METHOD = 6;
	
	public static final int INNER_FIELD = 7;
	
	public static final int INNER_METHOD_PARAM = 8;
	
	public static final int INNER_METHOD_STATEMENT = 9;
	
	public static final int INNER_METHOD_STATEMENT_BLOCK = 10;
	
	public static final int DOM_NAME = 1;
	
	public static final int DOM_TYPE_NAME = 2;
	
	public static final int DOM_VALUE = 3;
	//保存内容类型
	public static final int CONTENT_TYPE_NAME = 1;
	
	public static final int CONTENT_NAME = 2;
	
	public static final int CONTENT_VALUE = 3;
	
	//包名
	private String jpackage;
	//import类集合
	private List<String> jimports;
	//类
	private String currentClassKey = "";
	
	private Map<String, JClass> classMap = new HashMap<String, JClass>();
	
	private int nodeType = UNKNOWN;
	
	private int contentType = UNKNOWN;
	/*****************************************/
	/**
	 * 指定转换方法
	 */
	private JMethod method;
	/**
	 * 需要转换的所有类方法和类属性
	 */
//	private Map<String, JClass> needClassMap = new HashMap<String, JClass>();
	
	public List<JClass> getClassMapToList(){
		List<JClass> list = new ArrayList<JClass>();
		Iterator<String> keys = this.getClassMap().keySet().iterator();
		while(keys.hasNext()){
			list.add(this.getClassMap().get(keys.next()));
		}
		return list;
	}
	
	public String getJpackage() {
		return jpackage;
	}
	public void setJpackage(String jpackage) {
		this.jpackage = jpackage;
	}
	public List<String> getJimports() {
		if(jimports==null){
			jimports = new ArrayList<String>();
		}
		return jimports;
	}
	public void setJimports(List<String> jimports) {
		this.jimports = jimports;
	}

	
	public Map<String, JClass> getClassMap() {
		return classMap;
	}

	public void setClassMap(Map<String, JClass> classMap) {
		this.classMap = classMap;
	}

	public JClass getCurrentClass(){
		return this.getClassMap().get(currentClassKey);
	}
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
//	public Map<String, JClass> getNeedClassMap() {
//		return needClassMap;
//	}
//	public void setNeedClassMap(Map<String, JClass> needClassMap) {
//		this.needClassMap = needClassMap;
//	}
	public JMethod getMethod() {
		return method;
	}
	public void setMethod(JMethod method) {
		this.method = method;
	}
//	public String getCurrentClassKey() {
//		return currentClassKey;
//	}
	public void setCurrentClassKey(String currentClassKey) {
		this.currentClassKey = currentClassKey;
	}
	
}