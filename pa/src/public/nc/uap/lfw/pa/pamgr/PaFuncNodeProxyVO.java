package nc.uap.lfw.pa.pamgr;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

@SuppressWarnings("serial")
public class PaFuncNodeProxyVO extends SuperVO {
	/**
	 * 该VO是UAPCP下CpAppsNodeVO的代理VO
	 */
	private nc.vo.pub.lang.UFBoolean activeflag;
	private java.lang.String i18nname;
	private java.lang.String id;
	private java.lang.String pk_appsnode;
	private java.lang.String pk_group;
	private java.lang.String title;
	private java.lang.String url;
	private UFBoolean specialflag;
	private java.lang.String pk_appscategory;
	private java.lang.Integer dr;
	private nc.vo.pub.lang.UFDateTime ts;

	public static final String ACTIVEFLAG = "activeflag";
	public static final String I18NNAME = "i18nname";
	public static final String ID = "id";
	public static final String PK_APPSNODE = "pk_appsnode";
	public static final String PK_GROUP = "pk_group";
	public static final String TITLE = "title";
	public static final String URL = "url";
	public static final String PK_APPSCATEGORY = "pk_appscategory";
			
	/** 
	 * 属性activeflag的Getter方法.
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getActiveflag () {
		return activeflag;
	}   
	/**
	 * 属性activeflag的Setter方法.
	 * 创建日期:
	 * @param newActiveflag nc.vo.pub.lang.UFBoolean
	 */
	public void setActiveflag (nc.vo.pub.lang.UFBoolean newActiveflag ) {
	 	this.activeflag = newActiveflag;
	} 	  
	/**
	 * 属性i18nname的Getter方法.
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getI18nname () {
		return i18nname;
	}   
	/**
	 * 属性i18nname的Setter方法.
	 * 创建日期:
	 * @param newI18nname java.lang.String
	 */
	public void setI18nname (java.lang.String newI18nname ) {
	 	this.i18nname = newI18nname;
	} 	  
	/**
	 * 属性id的Getter方法.
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getId () {
		return id;
	}   
	/**
	 * 属性id的Setter方法.
	 * 创建日期:
	 * @param newId java.lang.String
	 */
	public void setId (java.lang.String newId ) {
	 	this.id = newId;
	} 	  
	/**
	 * 属性pk_appsnode的Getter方法.
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_appsnode () {
		return pk_appsnode;
	}   
	/**
	 * 属性pk_appsnode的Setter方法.
	 * 创建日期:
	 * @param newPk_appsnode java.lang.String
	 */
	public void setPk_appsnode (java.lang.String newPk_appsnode ) {
	 	this.pk_appsnode = newPk_appsnode;
	} 	  
	/**
	 * 属性pk_group的Getter方法.
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * 属性pk_group的Setter方法.
	 * 创建日期:
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
	} 	  
	/**
	 * 属性title的Getter方法.
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTitle () {
		return title;
	}   
	/**
	 * 属性title的Setter方法.
	 * 创建日期:
	 * @param newTitle java.lang.String
	 */
	public void setTitle (java.lang.String newTitle ) {
	 	this.title = newTitle;
	} 	  
	/**
	 * 属性url的Getter方法.
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getUrl () {
		return url;
	}   
	/**
	 * 属性url的Setter方法.
	 * 创建日期:
	 * @param newUrl java.lang.String
	 */
	public void setUrl (java.lang.String newUrl ) {
	 	this.url = newUrl;
	} 	  
	/**
	 * 属性pk_appscategory的Getter方法.
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_appscategory () {
		return pk_appscategory;
	}   
	/**
	 * 属性pk_appscategory的Setter方法.
	 * 创建日期:
	 * @param newPk_appscategory java.lang.String
	 */
	public void setPk_appscategory (java.lang.String newPk_appscategory ) {
	 	this.pk_appscategory = newPk_appscategory;
	} 	  
	/**
	 * 属性dr的Getter方法.
	 * 创建日期:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.
	 * 创建日期:
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * 属性ts的Getter方法.
	 * 创建日期:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.
	 * 创建日期:
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
 
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
	  return "pk_appsnode";
	}
    
	public UFBoolean getSpecialflag() {
		return specialflag;
	}
	public void setSpecialflag(UFBoolean specialflag) {
		this.specialflag = specialflag;
	}
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "cp_appsnode";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "cp_appsnode";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:
	  */
     public PaFuncNodeProxyVO() {
		super();	
	}    
}
