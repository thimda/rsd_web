package nc.uap.lfw.pa.pamgr;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

@SuppressWarnings("serial")
public class PaFuncNodeProxyVO extends SuperVO {
	/**
	 * ��VO��UAPCP��CpAppsNodeVO�Ĵ���VO
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
	 * ����activeflag��Getter����.
	 * ��������:
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean getActiveflag () {
		return activeflag;
	}   
	/**
	 * ����activeflag��Setter����.
	 * ��������:
	 * @param newActiveflag nc.vo.pub.lang.UFBoolean
	 */
	public void setActiveflag (nc.vo.pub.lang.UFBoolean newActiveflag ) {
	 	this.activeflag = newActiveflag;
	} 	  
	/**
	 * ����i18nname��Getter����.
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getI18nname () {
		return i18nname;
	}   
	/**
	 * ����i18nname��Setter����.
	 * ��������:
	 * @param newI18nname java.lang.String
	 */
	public void setI18nname (java.lang.String newI18nname ) {
	 	this.i18nname = newI18nname;
	} 	  
	/**
	 * ����id��Getter����.
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getId () {
		return id;
	}   
	/**
	 * ����id��Setter����.
	 * ��������:
	 * @param newId java.lang.String
	 */
	public void setId (java.lang.String newId ) {
	 	this.id = newId;
	} 	  
	/**
	 * ����pk_appsnode��Getter����.
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_appsnode () {
		return pk_appsnode;
	}   
	/**
	 * ����pk_appsnode��Setter����.
	 * ��������:
	 * @param newPk_appsnode java.lang.String
	 */
	public void setPk_appsnode (java.lang.String newPk_appsnode ) {
	 	this.pk_appsnode = newPk_appsnode;
	} 	  
	/**
	 * ����pk_group��Getter����.
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * ����pk_group��Setter����.
	 * ��������:
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
	} 	  
	/**
	 * ����title��Getter����.
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getTitle () {
		return title;
	}   
	/**
	 * ����title��Setter����.
	 * ��������:
	 * @param newTitle java.lang.String
	 */
	public void setTitle (java.lang.String newTitle ) {
	 	this.title = newTitle;
	} 	  
	/**
	 * ����url��Getter����.
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getUrl () {
		return url;
	}   
	/**
	 * ����url��Setter����.
	 * ��������:
	 * @param newUrl java.lang.String
	 */
	public void setUrl (java.lang.String newUrl ) {
	 	this.url = newUrl;
	} 	  
	/**
	 * ����pk_appscategory��Getter����.
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getPk_appscategory () {
		return pk_appscategory;
	}   
	/**
	 * ����pk_appscategory��Setter����.
	 * ��������:
	 * @param newPk_appscategory java.lang.String
	 */
	public void setPk_appscategory (java.lang.String newPk_appscategory ) {
	 	this.pk_appscategory = newPk_appscategory;
	} 	  
	/**
	 * ����dr��Getter����.
	 * ��������:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.
	 * ��������:
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	/**
	 * ����ts��Getter����.
	 * ��������:
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.
	 * ��������:
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	  
 
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:
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
	 * <p>���ر�����.
	 * <p>
	 * ��������:
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "cp_appsnode";
	}    
	
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "cp_appsnode";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:
	  */
     public PaFuncNodeProxyVO() {
		super();	
	}    
}
