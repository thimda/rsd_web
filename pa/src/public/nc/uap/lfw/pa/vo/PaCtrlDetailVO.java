/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-7-19
 * @since 1.6
 */
public class PaCtrlDetailVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	private String pk_detail;
	private String pk_parent;
	public String getPk_detail() {
		return pk_detail;
	}
	public void setPk_detail(String pk_detail) {
		this.pk_detail = pk_detail;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	
}
