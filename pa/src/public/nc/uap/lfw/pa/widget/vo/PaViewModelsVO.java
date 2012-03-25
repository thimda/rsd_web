/**
 * 
 */
package nc.uap.lfw.pa.widget.vo;

import nc.vo.pub.SuperVO;

/**
 * ≤Œ’’ViewModels.java¿‡±Ì
 * @author wupeng1
 * @version 6.0 2011-8-1
 * @since 1.6
 */
public class PaViewModelsVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String pk_viewmodels;
	private String pk_parent;
	private String pk_lfwwd;
	private String id;
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_viewmodels";
	}
	@Override
	public String getTableName() {
		return "pa_viewmodels";
	}
	public String getPk_viewmodels() {
		return pk_viewmodels;
	}
	public void setPk_viewmodels(String pk_viewmodels) {
		this.pk_viewmodels = pk_viewmodels;
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
