package nc.uap.lfw.stylemgr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class UwItemVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1708536547164106882L;
	private String pk_item;
	private String itemname;
	private UFBoolean isvisiable;
	private String pk_component;
	private String itemdesc;
	
	private UFBoolean dr;
	private UFDateTime ts;
	
	public String getPk_item() {
		return pk_item;
	}
	public void setPk_item(String pk_item) {
		this.pk_item = pk_item;
	}
	
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getPk_component() {
		return pk_component;
	}
	public void setPk_component(String pk_component) {
		this.pk_component = pk_component;
	}
	public UFBoolean getIsvisiable() {
		return isvisiable;
	}
	public void setIsvisiable(UFBoolean isvisiable) {
		this.isvisiable = isvisiable;
	}
	public String getItemdesc() {
		return itemdesc;
	}
	public void setItemdesc(String itemdesc) {
		this.itemdesc = itemdesc;
	}
	public UFBoolean getDr() {
		return dr;
	}
	public void setDr(UFBoolean dr) {
		this.dr = dr;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	@Override
	public String getPKFieldName() {
		return "pk_item";
	}
	@Override
	public String getTableName() {
		return "uw_item";
	}

	
}
