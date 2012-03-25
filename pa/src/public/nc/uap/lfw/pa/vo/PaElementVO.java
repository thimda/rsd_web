/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-7-20
 * @since 1.6
 */
public class PaElementVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_element;
	private String id;
	private String widget;
	private String classname;
	
	private String pk_parent;
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_element";
	}
	@Override
	public String getTableName() {
		return "pa_element";
	}
	public String getPk_element() {
		return pk_element;
	}
	public void setPk_element(String pk_element) {
		this.pk_element = pk_element;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWidget() {
		return widget;
	}
	public void setWidget(String widget) {
		this.widget = widget;
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
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}

}
