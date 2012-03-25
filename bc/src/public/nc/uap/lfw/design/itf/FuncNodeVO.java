package nc.uap.lfw.design.itf;

import java.io.Serializable;

public class FuncNodeVO implements Serializable{
	private static final long serialVersionUID = -6205728228170680264L;
	/**
	 * 功能类型 - 业务类型
	 */
	public final static int FUNC_TYPE_BUSINESS = 0;
	/**
	 * 功能类型 - 管理类型
	 */
	public final static int FUNC_TYPE_ADMIN = 1;
	/**
	 * 功能类型 - 系统类型
	 */
	public final static int FUNC_TYPE_SYSTEM = 2;
	/**
	 * 节点类型 - module
	 */
	public final static int MODULE_FUNC_NODE = 0;
	/**
	 * 节点类型 - 虚节点
	 */
	public final static int INEXECUTABLE_FUNC_NODE = 2;
	/**
	 * 节点类型 - 可执行功能节点
	 */
	public final static int EXECUTABLE_FUNC_NODE = 1;
	
	/**
	 * 轻量级Web节点 leijun+2008-3-10
	 */
	public final static int LFW_FUNC_NODE = 6;
	
	/**
	 * 页面类型
	 */
	
	//管理主子,卡片优先
	public final static int PAGETYPE_MANAGE_MASCHI_CARDFIRST = 0;
	//管理主子,列表优先
	public final static int PAGETYPE_MANAGE_MASCHI_LISTFIRST = 6;
	//管理单表头，卡片优先
	public final static int PAGETYPE_MANAGE_SINGAL_CARDFIRST = 1;
	//管理单表头,列表优先
	public final static int PAGETYPE_MANAGE_SINGAL_LISTFIRST = 7;
	//card主子
	public final static int PAGETYPE_CARD_MASCHI = 2;
	//card单表头
	public final static int PAGETYPE_CARD_SINGAL = 3;
	//list主子
	public final static int PAGETYPE_LIST_MASCHI = 4;
	//list单表头
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
	//0 module  1可执行节点  2虚节点
	private Integer nodeType;
	//功能点类型(业务类型,管理类型,系统类型)
	private Integer funType;
	//组织类型
	private String orgTypeCode;
	//功能描述
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
