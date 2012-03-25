/**
 * 
 */
package nc.uap.lfw.pa.vo;

/**
 * @author wupeng1
 * @version 6.0 2011-7-19
 * @since 1.6
 */
public class PaLayoutVO extends PaElementVO {
	private static final long serialVersionUID = 1L;
	
	private String pk_layout;
	
	private String id;
	private String classname;
	private String name;
	private String i18nname;
	private String pk_parent;
	
	private String parentid;
	
	@Override
	public String getPKFieldName() {
		return "pk_layout";
	}
	@Override
	public String getTableName() {
		return "pa_layout";
	}
	public String getPk_layout() {
		return pk_layout;
	}
	public void setPk_layout(String pk_layout) {
		this.pk_layout = pk_layout;
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
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getI18nname() {
		return i18nname;
	}
	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
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
