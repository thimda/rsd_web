/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.uap.lfw.pa.PaConstant;
import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-7-20
 * @since 1.6
 */
public class PaSplitterVO extends SuperVO{
	/**
	 * °üº¬Á½¸ösplitterPanel(SplitterOne, SplitterTwo)
	 */
	private static final long serialVersionUID = 1L;

	
	private String pk_spliter;
	private String pk_parent;
	
	private String dividesize;
	private Integer orientation;
	private Integer boundmode;
	private Integer onetouch;
	private String spliterwidth;
	private Integer inverse;
	private Integer hidebar;
	private Integer hidedirection;
	private String id;
	private String widgetid;
	private String classname;
	
	private String pk_splitterone;
	private String pk_splittertwo;
	
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_spliter";
	}
	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_SPLIT;
	}
	public String getPk_spliter() {
		return pk_spliter;
	}
	public void setPk_spliter(String pk_spliter) {
		this.pk_spliter = pk_spliter;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getDividesize() {
		return dividesize;
	}
	public void setDividesize(String dividesize) {
		this.dividesize = dividesize;
	}
	public Integer getOrientation() {
		return orientation;
	}
	public void setOrientation(Integer orientation) {
		this.orientation = orientation;
	}
	public Integer getBoundmode() {
		return boundmode;
	}
	public void setBoundmode(Integer boundmode) {
		this.boundmode = boundmode;
	}
	public Integer getOnetouch() {
		return onetouch;
	}
	public void setOnetouch(Integer onetouch) {
		this.onetouch = onetouch;
	}
	public String getSpliterwidth() {
		return spliterwidth;
	}
	public void setSpliterwidth(String spliterwidth) {
		this.spliterwidth = spliterwidth;
	}
	public Integer getInverse() {
		return inverse;
	}
	public void setInverse(Integer inverse) {
		this.inverse = inverse;
	}
	public Integer getHidebar() {
		return hidebar;
	}
	public void setHidebar(Integer hidebar) {
		this.hidebar = hidebar;
	}
	public Integer getHidedirection() {
		return hidedirection;
	}
	public void setHidedirection(Integer hidedirection) {
		this.hidedirection = hidedirection;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	public String getPk_splitterone() {
		return pk_splitterone;
	}
	public void setPk_splitterone(String pk_splitterone) {
		this.pk_splitterone = pk_splitterone;
	}
	public String getPk_splittertwo() {
		return pk_splittertwo;
	}
	public void setPk_splittertwo(String pk_splittertwo) {
		this.pk_splittertwo = pk_splittertwo;
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
