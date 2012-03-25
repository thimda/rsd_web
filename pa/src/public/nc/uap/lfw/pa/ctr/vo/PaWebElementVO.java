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
public class PaWebElementVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_webelement;
	private String pk_parent;
	
	private String id;
	private boolean rendered;
	
	private String conftype;
	private int confpos;
	private boolean ctxChanged;
	private String froms;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_webelement";
	}
	@Override
	public String getTableName() {
		return "pa_webelement";
	}
	public String getPk_webelement() {
		return pk_webelement;
	}
	public void setPk_webelement(String pk_webelement) {
		this.pk_webelement = pk_webelement;
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
	public boolean isRendered() {
		return rendered;
	}
	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public boolean isCtxChanged() {
		return ctxChanged;
	}
	public void setCtxChanged(boolean ctxChanged) {
		this.ctxChanged = ctxChanged;
	}
	public String getFroms() {
		return froms;
	}
	public void setFroms(String froms) {
		this.froms = froms;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getConftype() {
		return conftype;
	}
	public void setConftype(String conftype) {
		this.conftype = conftype;
	}
	public int getConfpos() {
		return confpos;
	}
	public void setConfpos(int confpos) {
		this.confpos = confpos;
	}
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	
	

}
