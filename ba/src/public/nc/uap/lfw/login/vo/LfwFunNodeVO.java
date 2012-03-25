package nc.uap.lfw.login.vo;

import java.io.Serializable;

/**
 * LFW功能节点VO
 * 
 * @author licza
 * 
 */
public class LfwFunNodeVO implements Serializable {
	
	private static final long serialVersionUID = 1829403362235218466L;
	public static final String FUNC_CATE = "FUNC_CATE";
	public static final String FUNC_FUNC = "FUNC_FUNC";
	/**
	 * 节点PK
	 */
	private String pk_funnode;
	/**
	 * 节点名称
	 */
	private String title;

	/**
	 *节点链接地址
	 */
	private String linkurl;
	
	/**
	 * 节点类型
	 */
	private String type = FUNC_FUNC;

	public String getPk_funnode() {
		return pk_funnode;
	}

	public void setPk_funnode(String pk_funnode) {
		this.pk_funnode = pk_funnode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
