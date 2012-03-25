package nc.uap.lfw.design.itf;

import java.io.Serializable;

public class FuncNodeVO implements Serializable{
	private static final long serialVersionUID = -6205728228170680264L;
	/**
	 * �������� - ҵ������
	 */
	public final static int FUNC_TYPE_BUSINESS = 0;
	/**
	 * �������� - ��������
	 */
	public final static int FUNC_TYPE_ADMIN = 1;
	/**
	 * �������� - ϵͳ����
	 */
	public final static int FUNC_TYPE_SYSTEM = 2;
	/**
	 * �ڵ����� - module
	 */
	public final static int MODULE_FUNC_NODE = 0;
	/**
	 * �ڵ����� - ��ڵ�
	 */
	public final static int INEXECUTABLE_FUNC_NODE = 2;
	/**
	 * �ڵ����� - ��ִ�й��ܽڵ�
	 */
	public final static int EXECUTABLE_FUNC_NODE = 1;
	
	/**
	 * ������Web�ڵ� leijun+2008-3-10
	 */
	public final static int LFW_FUNC_NODE = 6;
	
	/**
	 * ҳ������
	 */
	
	//��������,��Ƭ����
	public final static int PAGETYPE_MANAGE_MASCHI_CARDFIRST = 0;
	//��������,�б�����
	public final static int PAGETYPE_MANAGE_MASCHI_LISTFIRST = 6;
	//������ͷ����Ƭ����
	public final static int PAGETYPE_MANAGE_SINGAL_CARDFIRST = 1;
	//������ͷ,�б�����
	public final static int PAGETYPE_MANAGE_SINGAL_LISTFIRST = 7;
	//card����
	public final static int PAGETYPE_CARD_MASCHI = 2;
	//card����ͷ
	public final static int PAGETYPE_CARD_SINGAL = 3;
	//list����
	public final static int PAGETYPE_LIST_MASCHI = 4;
	//list����ͷ
	public final static int PAGETYPE_LIST_SINGAL = 5;
	
	private String pagetype;
	public String getPagetype() {
		return pagetype;
	}
	public void setPagetype(String pagetype) {
		this.pagetype = pagetype;
	}
	private String pk_func;
	private String funcCode;
	private String funcName;
	private String pk_parent;
	private String funurl;
	private String ownmodule;
	//0 module  1��ִ�нڵ�  2��ڵ�
	private Integer nodeType;
	//���ܵ�����(ҵ������,��������,ϵͳ����)
	private Integer funType;
	//��֯����
	private String orgTypeCode;
	//��������
	private String funDesc;
	
	private String displayCode;
	
	private String subSystemid;
	
	public String getSubSystemid() {
		return subSystemid;
	}
	public void setSubSystemid(String subSystemid) {
		this.subSystemid = subSystemid;
	}
	public String getDisplayCode() {
		return displayCode;
	}
	public void setDisplayCode(String displayCode) {
		this.displayCode = displayCode;
	}
	public String getFunurl() {
		return funurl;
	}
	public void setFunurl(String funurl) {
		this.funurl = funurl;
	}
	public String getPk_func() {
		return pk_func;
	}
	public void setPk_func(String pk_func) {
		this.pk_func = pk_func;
	}
	public String getFuncCode() {
		return funcCode;
	}
	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public Integer getFunType() {
		return funType;
	}
	public void setFunType(Integer funType) {
		this.funType = funType;
	}
	public String getOrgTypeCode() {
		return orgTypeCode;
	}
	public void setOrgTypeCode(String orgTypeCode) {
		this.orgTypeCode = orgTypeCode;
	}
	public String getFunDesc() {
		return funDesc;
	}
	public void setFunDesc(String funDesc) {
		this.funDesc = funDesc;
	}
	public String getOwnmodule() {
		return ownmodule;
	}
	public void setOwnmodule(String ownmodule) {
		this.ownmodule = ownmodule;
	}
	public Integer getNodeType() {
		return nodeType;
	}
	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}
	
}
