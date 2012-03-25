/**
 * 
 */
package nc.uap.lfw.pa.widget.vo;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-8-1
 * @since 1.6
 */
public class PaViewComponentsVO extends SuperVO{

	private static final long serialVersionUID = 1L;
	private String pk_viewcomp;
	private String pk_parent;
	private String pk_lfwwd;
	
	private String pk_widget;
	private String id;
	private String parentid;
	
	
	@Override
	public String getPKFieldName() {
		return "pk_viewcomp";
	}
	@Override
	public String getTableName() {
		return "pa_viewcomp";
	}
	public String getPk_viewcomp() {
		return pk_viewcomp;
	}
	public void setPk_viewcomp(String pk_viewcomp) {
		this.pk_viewcomp = pk_viewcomp;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getPk_lfwwd() {
		return pk_lfwwd;
	}
	public void setPk_lfwwd(String pk_lfwwd) {
		this.pk_lfwwd = pk_lfwwd;
	}
	public String getPk_widget() {
		return pk_widget;
	}
	public void setPk_widget(String pk_widget) {
		this.pk_widget = pk_widget;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
	
}
