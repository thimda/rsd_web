package nc.uap.lfw.stylemgr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class UwViewVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	
	private String pk_view;
	private String viewid;
	private byte[] uimeta;
	private byte[] widget;
	private String pk_template;

	private UFBoolean dr;
	private UFDateTime ts;
	@Override
	public String getPKFieldName() {
		return "pk_view";
	}
	@Override
	public String getTableName() {
		return "uw_view";
	}
	public String getPk_view() {
		return pk_view;
	}
	public void setPk_view(String pk_view) {
		this.pk_view = pk_view;
	}
	public String getViewid() {
		return viewid;
	}
	public void setViewid(String viewid) {
		this.viewid = viewid;
	}
	public String getPk_template() {
		return pk_template;
	}
	public void setPk_template(String pk_template) {
		this.pk_template = pk_template;
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
	public byte[] getUimeta() {
		return uimeta;
	}
	public void setUimeta(byte[] uimeta) {
		this.uimeta = uimeta;
	}
	public String doGetUIMetaStr() {
		return new String(this.uimeta);
	}
	
	public void doSetUIMetaStr(String uimeta){
		this.uimeta = uimeta.getBytes();
	}

	public byte[] getWidget() {
		return widget;
	}
	public void setWidget(byte[] widget) {
		this.widget = widget;
	}
	public String doGetWidgetStr() {
		return new String(this.widget);
	}
	
	public void doSetWidgetStr(String widget){
		this.widget = widget.getBytes();
	}

}
