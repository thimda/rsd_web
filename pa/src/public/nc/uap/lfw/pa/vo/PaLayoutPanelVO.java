/**
 * 
 */
package nc.uap.lfw.pa.vo;

/**
 * @author wupeng1
 * @version 6.0 2011-7-19
 * @since 1.6
 */
public class PaLayoutPanelVO extends PaElementVO {
	private static final long serialVersionUID = 1L;
	private String pk_lp;
	
	private String id;
	private String classname;
	private String name;
	private String i18nname;
	private String pk_parent;
	private String parentid;
	
	private String leftpadding;
	private String rightpadding;
	private String toppadding;
	private String bottompadding;
	private String leftborder;
	private String rightborder;
	private String topborder;
	private String bottomborder;
	private String border;
	
	@Override
	public String getPKFieldName() {
		return "pk_lp";
	}
	
	@Override
	public String getTableName() {
		return "pa_panel";
	}

	public String getPk_lp() {
		return pk_lp;
	}

	public void setPk_lp(String pk_lp) {
		this.pk_lp = pk_lp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
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

	public String getLeftpadding() {
		return leftpadding;
	}

	public void setLeftpadding(String leftpadding) {
		this.leftpadding = leftpadding;
	}

	public String getRightpadding() {
		return rightpadding;
	}

	public void setRightpadding(String rightpadding) {
		this.rightpadding = rightpadding;
	}

	public String getLeftborder() {
		return leftborder;
	}

	public void setLeftborder(String leftborder) {
		this.leftborder = leftborder;
	}

	public String getRightborder() {
		return rightborder;
	}

	public void setRightborder(String rightborder) {
		this.rightborder = rightborder;
	}

	public String getTopborder() {
		return topborder;
	}

	public void setTopborder(String topborder) {
		this.topborder = topborder;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	public String getBottomborder() {
		return bottomborder;
	}

	public void setBottomborder(String bottomborder) {
		this.bottomborder = bottomborder;
	}

	public String getToppadding() {
		return toppadding;
	}

	public void setToppadding(String toppadding) {
		this.toppadding = toppadding;
	}

	public String getBottompadding() {
		return bottompadding;
	}

	public void setBottompadding(String bottompadding) {
		this.bottompadding = bottompadding;
	}

}
