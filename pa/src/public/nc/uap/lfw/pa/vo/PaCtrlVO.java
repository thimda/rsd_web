/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.vo.pub.SuperVO;

/**
 * 控件类型的表
 * @author wupeng1
 * @version 6.0 2011-7-19
 * @since 1.6
 */
public class PaCtrlVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pk_ctrl;
	private String type;
	private String name;
	
	private String pk_panel;

	public String getPk_ctrl() {
		return pk_ctrl;
	}

	public void setPk_ctrl(String pk_ctrl) {
		this.pk_ctrl = pk_ctrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk_panel() {
		return pk_panel;
	}

	public void setPk_panel(String pk_panel) {
		this.pk_panel = pk_panel;
	}
	
	
	
}
