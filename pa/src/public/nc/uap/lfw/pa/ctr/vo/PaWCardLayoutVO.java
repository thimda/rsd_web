/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;

/**
 * @author wupeng1
 * @version 6.0 2011-7-22
 * @since 1.6
 */
public class PaWCardLayoutVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_cdlayout;
	private int currentIndex;
	private String pk_parent;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_cdlayout";
	}
	@Override
	public String getTableName() {
		return "pa_cdlayout";
	}
	public String getPk_cdlayout() {
		return pk_cdlayout;
	}
	public void setPk_cdlayout(String pk_cdlayout) {
		this.pk_cdlayout = pk_cdlayout;
	}
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
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
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	
	

}
