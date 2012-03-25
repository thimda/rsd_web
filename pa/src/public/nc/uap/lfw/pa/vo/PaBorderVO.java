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
public class PaBorderVO extends PaLayoutVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_border;
	private String pk_parent;
	
	private String id;
	private String widgetid;
	private String color;
	private Integer showleft;
	private Integer showright;
	private Integer showtop;
	private Integer showbottom;
	private String leftcolor;
	private String rightcolor;
	private String topcolor;
	private String bottomcolor;
	private String width;
	private String leftwidth;
	private String rightwidth;
	private String topwidth;
	private String bottomwidth;
	private String classname;
	private String roundborder;
	
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_border";
	}
	@Override
	public String getTableName() {
		return "pa_layout" + PaConstant.LAYOUT_BORDER;
	}
	public String getPk_border() {
		return pk_border;
	}
	public void setPk_border(String pk_border) {
		this.pk_border = pk_border;
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public String getLeftcolor() {
		return leftcolor;
	}
	public void setLeftcolor(String leftcolor) {
		this.leftcolor = leftcolor;
	}
	public String getRightcolor() {
		return rightcolor;
	}
	public void setRightcolor(String rightcolor) {
		this.rightcolor = rightcolor;
	}
	public String getTopcolor() {
		return topcolor;
	}
	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}
	public String getBottomcolor() {
		return bottomcolor;
	}
	public void setBottomcolor(String bottomcolor) {
		this.bottomcolor = bottomcolor;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getLeftwidth() {
		return leftwidth;
	}
	public void setLeftwidth(String leftwidth) {
		this.leftwidth = leftwidth;
	}
	public String getRightwidth() {
		return rightwidth;
	}
	public void setRightwidth(String rightwidth) {
		this.rightwidth = rightwidth;
	}
	public String getTopwidth() {
		return topwidth;
	}
	public void setTopwidth(String topwidth) {
		this.topwidth = topwidth;
	}
	public String getBottomwidth() {
		return bottomwidth;
	}
	public void setBottomwidth(String bottomwidth) {
		this.bottomwidth = bottomwidth;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	public Integer getShowleft() {
		return showleft;
	}
	public void setShowleft(Integer showleft) {
		this.showleft = showleft;
	}
	public Integer getShowright() {
		return showright;
	}
	public void setShowright(Integer showright) {
		this.showright = showright;
	}
	public Integer getShowtop() {
		return showtop;
	}
	public void setShowtop(Integer showtop) {
		this.showtop = showtop;
	}
	public Integer getShowbottom() {
		return showbottom;
	}
	public void setShowbottom(Integer showbottom) {
		this.showbottom = showbottom;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getRoundborder() {
		return roundborder;
	}
	public void setRoundborder(String roundborder) {
		this.roundborder = roundborder;
	}
	
	
	

}
