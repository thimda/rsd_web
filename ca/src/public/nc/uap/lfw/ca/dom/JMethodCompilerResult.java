/**
 * 
 */
package nc.uap.lfw.ca.dom;

/**
 * @author chouhl
 *
 */
public class JMethodCompilerResult {
	//方法返回的类型
	private String methodreturn;
	//包含方法的类
	private String methodinside;
	
	private String methodname;
	
	private String[]params;

	public String getMethodreturn() {
		return methodreturn;
	}

	public void setMethodreturn(String methodreturn) {
		this.methodreturn = methodreturn;
	}

	public String getMethodinside() {
		return methodinside;
	}

	public void setMethodinside(String methodinside) {
		this.methodinside = methodinside;
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
