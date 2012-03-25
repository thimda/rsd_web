package nc.uap.lfw.core.ses;

import nc.vo.pub.SuperVO;
/**
 * »á»°VO
 * @author dengjt
 *
 */
public class WebSessionVO extends SuperVO {
	private static final long serialVersionUID = -5268618858003159141L;
	private String pk_webses;
	private String sesid;
	private String pageid;
	private Object obj;
	public String getPk_webses() {
		return pk_webses;
	}
	public void setPk_webses(String pk_webses) {
		this.pk_webses = pk_webses;
	}
	public String getPageid() {
		return pageid;
	}
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object content) {
		this.obj = content;
	}
	@Override
	public String getPKFieldName() {
		return "pk_webses";
	}
	@Override
	public String getTableName() {
		return "uw_webses";
	}
	public String getSesid() {
		return sesid;
	}
	public void setSesid(String sesid) {
		this.sesid = sesid;
	}
}
