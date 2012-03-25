package nc.uap.lfw.login.vo;

import java.io.Serializable;

/**
 * LFW���ܽڵ�VO
 * 
 * @author licza
 * 
 */
public class LfwFunNodeVO implements Serializable {
	
	private static final long serialVersionUID = 1829403362235218466L;
	public static final String FUNC_CATE = "FUNC_CATE";
	public static final String FUNC_FUNC = "FUNC_FUNC";
	/**
	 * �ڵ�PK
	 */
	private String pk_funnode;
	/**
	 * �ڵ�����
	 */
	private String title;

	/**
	 *�ڵ����ӵ�ַ
	 */
	private String linkurl;
	
	/**
	 * �ڵ�����
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
