package nc.uap.lfw.stylemgr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class UwFuncNodeVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1670804324351171978L;
	private String pk_funcnode;
	private String funcnodename;
	private String pk_parent;
	private String url;
	private String pageid;
	private String funcnodedesc;

	private UFBoolean dr;
	private UFDateTime ts;
	public String getPk_funcnode() {
		return pk_funcnode;
	}
	public void setPk_funcnode(String pk_funcnode) {
		this.pk_funcnode = pk_funcnode;
	}
	public String getFuncnodename() {
		return funcnodename;
	}
	public void setFuncnodename(String funcnodename) {
		this.funcnodename = funcnodename;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPageid() {
		return pageid;
	}
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	public String getFuncnodedesc() {
		return funcnodedesc;
	}
	public void setFuncnodedesc(String funcnodedesc) {
		this.funcnodedesc = funcnodedesc;
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
		return "pk_funcnode";
	}
	@Override
	public String getTableName() {
		return "uw_funcnode";
	}

	

}
