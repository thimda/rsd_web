/**
 * 
 */
package nc.uap.lfw.pa.vo;

/**
 * @author wupeng1
 * @version 6.0 2011-7-20
 * @since 1.6
 */
public class PaWidgetVO extends PaLayoutVO {
	
	private static final long serialVersionUID = 1L;
	
	private String pk_widget;
	private String id;
	private String pk_uimeta;
	private String pk_parent;
	
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_widget";
	}
	@Override
	public String getTableName() {
		return "pa_widget";
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

	public String getPk_uimeta() {
		return pk_uimeta;
	}
	public void setPk_uimeta(String pk_uimeta) {
		this.pk_uimeta = pk_uimeta;
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

	

}
