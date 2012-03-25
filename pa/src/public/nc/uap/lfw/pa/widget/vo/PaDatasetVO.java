/**
 * 
 */
package nc.uap.lfw.pa.widget.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-8-1
 * @since 1.6
 */
public class PaDatasetVO extends SuperVO{


	private static final long serialVersionUID = 1L;
	
	private String pk_dataset;

	private UFBoolean lazyload;
	
	private String currentkey;
	
	private int pagesize;
	
	private String vometa;
	
	private UFBoolean enabled;
	
	private String operatorStatusarray;
	
	private boolean controloperatorstatus;
	
	private boolean controlwidgetopestatus;
	
	private int randomrowindex;
	
	private boolean stickpage;
	
	private String caption;
	
	private int focusindex;
	
	private String pk_viewmodels;
	
	private String pk_parent;
	
	private String parentid;

	@Override
	public String getPKFieldName() {
		return "pk_dataset";
	}

	@Override
	public String getTableName() {
		return "pa_dataset";
	}

	public String getPk_dataset() {
		return pk_dataset;
	}

	public void setPk_dataset(String pk_dataset) {
		this.pk_dataset = pk_dataset;
	}

	public UFBoolean getLazyload() {
		return lazyload;
	}

	public void setLazyload(UFBoolean lazyload) {
		this.lazyload = lazyload;
	}

	public String getCurrentkey() {
		return currentkey;
	}

	public void setCurrentkey(String currentkey) {
		this.currentkey = currentkey;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getVometa() {
		return vometa;
	}

	public void setVometa(String vometa) {
		this.vometa = vometa;
	}

	public UFBoolean getEnabled() {
		return enabled;
	}

	public void setEnabled(UFBoolean enabled) {
		this.enabled = enabled;
	}

	public String getOperatorStatusarray() {
		return operatorStatusarray;
	}

	public void setOperatorStatusarray(String operatorStatusarray) {
		this.operatorStatusarray = operatorStatusarray;
	}

	public boolean isControloperatorstatus() {
		return controloperatorstatus;
	}

	public void setControloperatorstatus(boolean controloperatorstatus) {
		this.controloperatorstatus = controloperatorstatus;
	}

	public boolean isControlwidgetopestatus() {
		return controlwidgetopestatus;
	}

	public void setControlwidgetopestatus(boolean controlwidgetopestatus) {
		this.controlwidgetopestatus = controlwidgetopestatus;
	}

	public int getRandomrowindex() {
		return randomrowindex;
	}

	public void setRandomrowindex(int randomrowindex) {
		this.randomrowindex = randomrowindex;
	}

	public boolean isStickpage() {
		return stickpage;
	}

	public void setStickpage(boolean stickpage) {
		this.stickpage = stickpage;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getFocusindex() {
		return focusindex;
	}

	public void setFocusindex(int focusindex) {
		this.focusindex = focusindex;
	}

	public String getPk_viewmodels() {
		return pk_viewmodels;
	}

	public void setPk_viewmodels(String pk_viewmodels) {
		this.pk_viewmodels = pk_viewmodels;
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

}
