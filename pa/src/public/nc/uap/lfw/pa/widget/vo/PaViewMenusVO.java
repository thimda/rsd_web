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
public class PaViewMenusVO extends SuperVO{
	
	private static final long serialVersionUID = 1L;
	private String pk_viewmenus;
	private String pk_widget;
	private String pk_pagemeta;
	private String pk_parent;
	private String parentid;
	private String id;
	
	@Override
	public String getPKFieldName() {
		return "pk_viewmenus";
	}
	@Override
	public String getTableName() {
		return "pa_viewmenus";
	}
	public String getPk_viewmenus() {
		return pk_viewmenus;
	}
	public void setPk_viewmenus(String pk_viewmenus) {
		this.pk_viewmenus = pk_viewmenus;
	}
	public String getPk_widget() {
		return pk_widget;
	}
	public void setPk_widget(String pk_widget) {
		this.pk_widget = pk_widget;
	}
	public String getPk_pagemeta() {
		return pk_pagemeta;
	}
	public void setPk_pagemeta(String pk_pagemeta) {
		this.pk_pagemeta = pk_pagemeta;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
