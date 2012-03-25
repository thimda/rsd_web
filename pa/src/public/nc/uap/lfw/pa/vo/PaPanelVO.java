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
public class PaPanelVO extends PaLayoutVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_panel;
	private String pk_parent;
	
	
	private String id;
	private String widgetid;
	private String ileft;
	private String itop;
	private String width;
	
	private String height;
	private String positions;
	private String classname;
	
	private Integer transparent;
	private String bkgrdcolor;
	
	private Integer scroll;
	
	private String paddingleft;
	private String paddingtop;
	private String paddingright;
	private String paddingbottom;
	
	private Integer roundrect;
	private String radius;
	private Integer withborder;
	private String borderwidth;
	private String bordercolor;
	
	private Integer display;
	private Integer visibility;
	
	private String title;
	
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_panel";
	}
	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_PANEL;
	}
	public String getPk_panel() {
		return pk_panel;
	}
	public void setPk_panel(String pk_panel) {
		this.pk_panel = pk_panel;
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
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	public String getIleft() {
		return ileft;
	}
	public void setIleft(String ileft) {
		this.ileft = ileft;
	}
	public String getItop() {
		return itop;
	}
	public void setItop(String itop) {
		this.itop = itop;
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
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public Integer getTransparent() {
		return transparent;
	}
	public void setTransparent(Integer transparent) {
		this.transparent = transparent;
	}
	public String getBkgrdcolor() {
		return bkgrdcolor;
	}
	public void setBkgrdcolor(String bkgrdcolor) {
		this.bkgrdcolor = bkgrdcolor;
	}

	public String getPaddingleft() {
		return paddingleft;
	}
	public void setPaddingleft(String paddingleft) {
		this.paddingleft = paddingleft;
	}
	public String getPaddingtop() {
		return paddingtop;
	}
	public void setPaddingtop(String paddingtop) {
		this.paddingtop = paddingtop;
	}
	public String getPaddingright() {
		return paddingright;
	}
	public void setPaddingright(String paddingright) {
		this.paddingright = paddingright;
	}
	public String getPaddingbottom() {
		return paddingbottom;
	}
	public void setPaddingbottom(String paddingbottom) {
		this.paddingbottom = paddingbottom;
	}
	public Integer getRoundrect() {
		return roundrect;
	}
	public void setRoundrect(Integer roundrect) {
		this.roundrect = roundrect;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public Integer getWithborder() {
		return withborder;
	}
	public void setWithborder(Integer withborder) {
		this.withborder = withborder;
	}
	public String getBorderwidth() {
		return borderwidth;
	}
	public void setBorderwidth(String borderwidth) {
		this.borderwidth = borderwidth;
	}
	public String getBordercolor() {
		return bordercolor;
	}
	public void setBordercolor(String bordercolor) {
		this.bordercolor = bordercolor;
	}
	public Integer getDisplay() {
		return display;
	}
	public void setDisplay(Integer display) {
		this.display = display;
	}
	public Integer getVisibility() {
		return visibility;
	}
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public Integer getScroll() {
		return scroll;
	}
	public void setScroll(Integer scroll) {
		this.scroll = scroll;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
