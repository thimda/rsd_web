/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;


/**
 * @author wupeng1
 * @version 6.0 2011-7-25
 * @since 1.6
 */
public class PaPageMetaVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	
	private String pk_pagemeta;
	private String pk_parent;
	
	private String processorclazz;

	private String caption;
	
	private String i18nname;
	
	private String editformularclazz;
	
	private String pagestates; 
	
	private String operatorstate;
	
	private String busistate;
	
	private String etag;
	
	private String nodeimagepath;
	
	private String parentid;
	
	private String id;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_pagemeta";
	}

	@Override
	public String getTableName() {
		return "pa_pagemeta";
	}

	public String getPk_pagemeta() {
		return pk_pagemeta;
	}

	public void setPk_pagemeta(String pk_pagemeta) {
		this.pk_pagemeta = pk_pagemeta;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getProcessorclazz() {
		return processorclazz;
	}

	public void setProcessorclazz(String processorclazz) {
		this.processorclazz = processorclazz;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}

	public String getEditformularclazz() {
		return editformularclazz;
	}

	public void setEditformularclazz(String editformularclazz) {
		this.editformularclazz = editformularclazz;
	}

	public String getPagestates() {
		return pagestates;
	}

	public void setPagestates(String pagestates) {
		this.pagestates = pagestates;
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

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public String getNodeimagepath() {
		return nodeimagepath;
	}

	public void setNodeimagepath(String nodeimagepath) {
		this.nodeimagepath = nodeimagepath;
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

	public String getWidgetid() {
		return widgetid;
	}

	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}

}
