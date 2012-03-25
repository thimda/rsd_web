package nc.uap.lfw.core.refnode;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * NC参照封装
 *
 */
public class NCRefNode extends RefNode {
	private static final long serialVersionUID = 1L;
	/**
	 * NC参照Code
	 */
	private String refcode;
	/**
	 * 是否使用数据权限
	 */
	private boolean usePower = false;
	/**
	 * 是否多组织
	 */
	private boolean orgs = false;

	
	public boolean isOrgs() {
		return orgs;
	}
	public void setOrgs(boolean orgs) {
		this.orgs = orgs;
	}
	public String getRefcode() {
		return refcode;
	}
	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}
	public boolean isUsePower() {
		return usePower;
	}
	public void setUsePower(boolean usePower) {
		this.usePower = usePower;
	}
	
	public void validate(){
		super.validate();
		StringBuffer buffer = new StringBuffer();
		if(this.getRefcode() == null || this.getRefcode().equals("")){
			buffer.append("NC参照编码不能为空!\r\n");
		}
		if(buffer.length() > 0)
			throw new  LfwRuntimeException(buffer.toString());
	}
}
