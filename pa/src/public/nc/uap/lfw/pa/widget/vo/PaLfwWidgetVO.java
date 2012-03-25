/**
 * 
 */
package nc.uap.lfw.pa.widget.vo;

import nc.uap.lfw.pa.ctr.vo.PaComponentVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 对应的是前台LfwWidget.java类文件
 * @author wupeng1
 * @version 6.0 2011-8-1
 * @since 1.6
 */
public class PaLfwWidgetVO extends PaComponentVO {
	

	private static final long serialVersionUID = 1L;
	
	private String pk_lfwwd;

	private String editformularclazz;

	private UFBoolean dialog;
	
	private String caption;
	
	private String langdir;
		
	private String i18nname;
	
	private String pk_pagemeta;
	
	private String operatorstate;
	
	private String busistate;
	
	private String pk_parent;
	
	private String parentid;
	private String id;
	
	
	@Override
	public String getPKFieldName() {
		return "pk_lfwwd";
	}

	@Override
	public String getTableName() {
		return "pa_lfwwidget";
	}

	public String getPk_lfwwd() {
		return pk_lfwwd;
	}

	public void setPk_lfwwd(String pk_lfwwd) {
		this.pk_lfwwd = pk_lfwwd;
	}

	public String getEditformularclazz() {
		return editformularclazz;
	}

	public void setEditformularclazz(String editformularclazz) {
		this.editformularclazz = editformularclazz;
	}

	public UFBoolean getDialog() {
		return dialog;
	}

	public void setDialog(UFBoolean dialog) {
		this.dialog = dialog;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getLangdir() {
		return langdir;
	}

	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}

	public String getPk_pagemeta() {
		return pk_pagemeta;
	}

	public void setPk_pagemeta(String pk_pagemeta) {
		this.pk_pagemeta = pk_pagemeta;
	}

	public String getOperatorstate() {
		return operatorstate;
	}

	public void setOperatorstate(String operatorstate) {
		this.operatorstate = operatorstate;
	}

	public String getBusistate() {
		return busistate;
	}

	public void setBusistate(String busistate) {
		this.busistate = busistate;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
