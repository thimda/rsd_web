package nc.uap.lfw.core.refnode;

import nc.uap.lfw.core.exception.LfwRuntimeException;

/**
 * NC���շ�װ
 *
 */
public class NCRefNode extends RefNode {
	private static final long serialVersionUID = 1L;
	/**
	 * NC����Code
	 */
	private String refcode;
	/**
	 * �Ƿ�ʹ������Ȩ��
	 */
	private boolean usePower = false;
	/**
	 * �Ƿ����֯
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
			buffer.append("NC���ձ��벻��Ϊ��!\r\n");
		}
		if(buffer.length() > 0)
			throw new  LfwRuntimeException(buffer.toString());
	}
}
