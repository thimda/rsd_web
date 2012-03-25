/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-7-22
 * @since 1.6
 */
public class PaEditorCompVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private String pk_editor;
	
	private String hidebarindices;
	private String hideimageindices;
	private String value ;
	private UFBoolean readonlys ;
	
	private String pk_parent;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_editor";
	}

	@Override
	public String getTableName() {
		return "pa_editorcomp";
	}

	public String getPk_editor() {
		return pk_editor;
	}

	public void setPk_editor(String pk_editor) {
		this.pk_editor = pk_editor;
	}

	public String getHidebarindices() {
		return hidebarindices;
	}

	public void setHidebarindices(String hidebarindices) {
		this.hidebarindices = hidebarindices;
	}

	public String getHideimageindices() {
		return hideimageindices;
	}

	public void setHideimageindices(String hideimageindices) {
		this.hideimageindices = hideimageindices;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public UFBoolean getReadonlys() {
		return readonlys;
	}

	public void setReadonlys(UFBoolean readonlys) {
		this.readonlys = readonlys;
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

	public String getWidgetid() {
		return widgetid;
	}

	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}


	

}
