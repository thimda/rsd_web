/**
 * 
 */
package nc.uap.lfw.ca.dom;

/**
 * @author chouhl
 *
 */
public class JClassQuery {

	private String classname;
	
	private String methodname;
	
	private String[]params;
	
	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}
}
