/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;

/**
 * @author wupeng1
 * @version 6.0 2011-7-20
 * @since 1.6
 */
public class PaShutterVO extends PaLayoutVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_shutter;
	private String pk_parent;
	
	private String id;
	private String wigetid;
	private String currentitem;
	private String classname;
	
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_shutter";
	}
	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_SHUTTER;
	}
	public String getPk_shutter() {
		return pk_shutter;
	}
	public void setPk_shutter(String pk_shutter) {
		this.pk_shutter = pk_shutter;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCurrentitem() {
		return currentitem;
	}
	public void setCurrentitem(String currentitem) {
		this.currentitem = currentitem;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getWigetid() {
		return wigetid;
	}
	public void setWigetid(String wigetid) {
		this.wigetid = wigetid;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
}
