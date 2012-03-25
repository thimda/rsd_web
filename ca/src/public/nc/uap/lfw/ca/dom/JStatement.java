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

/**
 * @author chouhl
 *
 */
public class JStatement {
	
	private String statement;
	
	private String currentFieldMapKey = "";
	
	private Map<String, JField> fieldMap = new LinkedHashMap<String, JField>();
	
	private String currentMethodMapKey = "";
	
	private Map<String, JMethod> methodMap = new LinkedHashMap<String, JMethod>();;
	
	/******************翻译时使用的属性*********************/
	//符号集合
	private List<Symbol> symbolList;
	
	public List<Symbol> getSymbolList() {
		return symbolList;
	}

	public void setSymbolList(List<Symbol> symbolList) {
		this.symbolList = symbolList;
	}
	
	public JField getCurrentField(){
		return this.getFieldMap().get(currentFieldMapKey);
	}
	
	public List<JField> getFieldMapToList(){
		List<JField> list = new ArrayList<JField>();
		Iterator<String> keys = this.getFieldMap().keySet().iterator();
		while(keys.hasNext()){
			list.add(this.getFieldMap().get(keys.next()));
		}
		return list;
	}
	
	public JMethod getCurrentMethod(){
		return this.getMethodMap().get(currentMethodMapKey);
	}
	
	public List<JMethod> getMethodMapToList(){
		List<JMethod> list = new ArrayList<JMethod>();
		Iterator<String> keys = this.getMethodMap().keySet().iterator();
		while(keys.hasNext()){
			list.add(this.getMethodMap().get(keys.next()));
		}
		return list;
	}

	public Map<String, JMethod> getMethodMap() {
		return methodMap;
	}

	public void setMethodMap(Map<String, JMethod> methodMap) {
		this.methodMap = methodMap;
	}

	public void setCurrentMethodMapKey(String currentMethodMapKey) {
		this.currentMethodMapKey = currentMethodMapKey;
	}

	public String getCurrentMethodMapKey() {
		return currentMethodMapKey;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getCurrentFieldMapKey() {
		return currentFieldMapKey;
	}

	public void setCurrentFieldMapKey(String currentFieldMapKey) {
		this.currentFieldMapKey = currentFieldMapKey;
	}

	public Map<String, JField> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, JField> fieldMap) {
		this.fieldMap = fieldMap;
	}
	
}
