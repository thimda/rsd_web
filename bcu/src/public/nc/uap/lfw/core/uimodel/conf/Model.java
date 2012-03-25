/**
 * 
 */
package nc.uap.lfw.core.uimodel.conf;

import nc.uap.lfw.core.uimodel.AMCWebElement;


/**
 * 
 * Model节点元素类
 * @author chouhl
 *
 */
public class Model extends AMCWebElement{

	private static final long serialVersionUID = 3680832058677636799L;
	
	public static final String TagName = "Model";
	
	/**
	 * 元数据ID
	 */
	private String refId = null;
	
	private String ts = null;
	
	private String version = null;

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
