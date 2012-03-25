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
public class PaImageCompVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String pk_image;
	private String pk_parent;
	
	private String alt;
	private String image1;
	private String image2;
	private UFBoolean image1changeds ;
	private String realimage1;
	private UFBoolean image2changeds ;
	private String realimage2;
	private String imageinact;
	private UFBoolean floatrights ;
	private UFBoolean floatlefts ;
	
	/**
	 * widget中包含的属性
	 */
	private String id;
	private String width;
	private String height;
	private String contextmenu;
	private String positions;
	private String itop;
	private String ileft;
	private UFBoolean enableds;
	private UFBoolean visibles;
	private String classname;
	
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_image";
	}
	@Override
	public String getTableName() {
		return "pa_imagecomp";
	}
	public String getPk_image() {
		return pk_image;
	}
	public void setPk_image(String pk_image) {
		this.pk_image = pk_image;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getImage1() {
		return image1;
	}
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	public UFBoolean getImage1changeds() {
		return image1changeds;
	}
	public void setImage1changeds(UFBoolean image1changeds) {
		this.image1changeds = image1changeds;
	}
	public String getRealimage1() {
		return realimage1;
	}
	public void setRealimage1(String realimage1) {
		this.realimage1 = realimage1;
	}
	public UFBoolean getImage2changeds() {
		return image2changeds;
	}
	public void setImage2changeds(UFBoolean image2changeds) {
		this.image2changeds = image2changeds;
	}
	public String getRealimage2() {
		return realimage2;
	}
	public void setRealimage2(String realimage2) {
		this.realimage2 = realimage2;
	}
	public String getImageinact() {
		return imageinact;
	}
	public void setImageinact(String imageinact) {
		this.imageinact = imageinact;
	}
	public UFBoolean getFloatrights() {
		return floatrights;
	}
	public void setFloatrights(UFBoolean floatrights) {
		this.floatrights = floatrights;
	}
	public UFBoolean getFloatlefts() {
		return floatlefts;
	}
	public void setFloatlefts(UFBoolean floatlefts) {
		this.floatlefts = floatlefts;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
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
	public UFBoolean getEnableds() {
		return enableds;
	}
	public void setEnableds(UFBoolean enableds) {
		this.enableds = enableds;
	}
	public UFBoolean getVisibles() {
		return visibles;
	}
	public void setVisibles(UFBoolean visibles) {
		this.visibles = visibles;
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
