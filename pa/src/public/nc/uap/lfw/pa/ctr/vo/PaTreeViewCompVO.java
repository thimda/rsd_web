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
public class PaTreeViewCompVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String pk_treeview;
	private String pk_parent;
	
	private String pk_treemodel;
	private String pk_treelevel;
	// 是否可以拖拽
	private UFBoolean dragenable;
	// 是否展开根节点
	private UFBoolean rootopen ;
	// 默认展开级别
	private Integer openlevel ;
	// 树根节点默认显示值
	private String itext ;
	private String i18nname;
	private UFBoolean withroot ;
	// 是否是CheckBoxTree
	private UFBoolean withcheckbox;
	// 复选策略 0:只设置自己 1:设置自己和子 2:设置自己和子和父
	private Integer checkboxmodel ;
	
	private String langdir;

	//显示名称
	private String caption;
	
	/**
	 * widget页面中的属性
	 */
	private UFBoolean enabled;
	private String height;
	private String id;
	private String ileft;
	private String positions;
	private String itop;
	private UFBoolean visible;
	private String width;
	private String contextmenu;
	private String classname;
	private String parentid;
	private String widgetid;

	@Override
	public String getPKFieldName() {
		return "pk_treeview";
	}

	@Override
	public String getTableName() {
		return "pa_treeview";
	}

	public String getPk_treeview() {
		return pk_treeview;
	}

	public void setPk_treeview(String pk_treeview) {
		this.pk_treeview = pk_treeview;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getPk_treemodel() {
		return pk_treemodel;
	}

	public void setPk_treemodel(String pk_treemodel) {
		this.pk_treemodel = pk_treemodel;
	}

	public String getPk_treelevel() {
		return pk_treelevel;
	}

	public void setPk_treelevel(String pk_treelevel) {
		this.pk_treelevel = pk_treelevel;
	}

	public UFBoolean getDragenable() {
		return dragenable;
	}

	public void setDragenable(UFBoolean dragenable) {
		this.dragenable = dragenable;
	}

	public UFBoolean getRootopen() {
		return rootopen;
	}

	public void setRootopen(UFBoolean rootopen) {
		this.rootopen = rootopen;
	}

	public Integer getOpenlevel() {
		return openlevel;
	}

	public void setOpenlevel(Integer openlevel) {
		this.openlevel = openlevel;
	}

	public String getItext() {
		return itext;
	}

	public void setItext(String itext) {
		this.itext = itext;
	}

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}

	public UFBoolean getWithroot() {
		return withroot;
	}

	public void setWithroot(UFBoolean withroot) {
		this.withroot = withroot;
	}

	public UFBoolean getWithcheckbox() {
		return withcheckbox;
	}

	public void setWithcheckbox(UFBoolean withcheckbox) {
		this.withcheckbox = withcheckbox;
	}

	public Integer getCheckboxmodel() {
		return checkboxmodel;
	}

	public void setCheckboxmodel(Integer checkboxmodel) {
		this.checkboxmodel = checkboxmodel;
	}

	public String getLangdir() {
		return langdir;
	}

	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public UFBoolean getEnabled() {
		return enabled;
	}

	public void setEnabled(UFBoolean enabled) {
		this.enabled = enabled;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getItop() {
		return itop;
	}

	public void setItop(String itop) {
		this.itop = itop;
	}

	public UFBoolean getVisible() {
		return visible;
	}

	public void setVisible(UFBoolean visible) {
		this.visible = visible;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
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
