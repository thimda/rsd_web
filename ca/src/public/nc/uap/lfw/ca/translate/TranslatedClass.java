/**
 * 
 */
package nc.uap.lfw.ca.translate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chouhl
 *
 */
public class TranslatedClass {

	private String filename;
	
	private Map<String, String> fieldMap;
	
	private Map<String, String> methodMap;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Map<String, String> getFieldMap() {
		if(fieldMap == null){
			fieldMap = new LinkedHashMap<String, String>();
		}
		return fieldMap;
	}

	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<String, String> getMethodMap() {
		if(methodMap == null){
			methodMap = new LinkedHashMap<String, String>();
		}
		return methodMap;
	}

	public void setMethodMap(Map<String, String> methodMap) {
		this.methodMap = methodMap;
	}
	
}
