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
public class PaLinkCompVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_link;
	private String pk_parent;
	
	private String href;
	private String i18nname;
	private UFBoolean hasimgs;
	private String image;
	private UFBoolean imagechangeds;
	private String realimage;

	private String target ;
	private UFBoolean visibles ;
	
	
	/**
	 * widget÷–µƒ Ù–‘
	 */
	private String id;
	private String height;
	private String width;
	private String itop;
	private String ileft;
	private String positions;
	private String contextmenu;
	private String classname;
	
	private String parentid;
	private String widgetid;
	@Override
	public String getPKFieldName() {
		return "pk_link";
	}
	@Override
	public String getTableName() {
		return "pa_linkcomp";
	}
	public String getPk_link() {
		return pk_link;
	}
	public void setPk_link(String pk_link) {
		this.pk_link = pk_link;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getI18nname() {
		return i18nname;
	}
	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}
	public UFBoolean getHasimgs() {
		return hasimgs;
	}
	public void setHasimgs(UFBoolean hasimgs) {
		this.hasimgs = hasimgs;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public UFBoolean getImagechangeds() {
		return imagechangeds;
	}
	public void setImagechangeds(UFBoolean imagechangeds) {
		this.imagechangeds = imagechangeds;
	}
	public String getRealimage() {
		return realimage;
	}
	public void setRealimage(String realimage) {
		this.realimage = realimage;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public UFBoolean getVisibles() {
		return visibles;
	}
	public void setVisibles(UFBoolean visibles) {
		this.visibles = visibles;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getItop() {
		return itop;
	}
	public void setItop(String itop) {
		this.itop = itop;
	}
	public String getIleft() {
		return ileft;
	}
	public void setIleft(String ileft) {
		this.ileft = ileft;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
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
