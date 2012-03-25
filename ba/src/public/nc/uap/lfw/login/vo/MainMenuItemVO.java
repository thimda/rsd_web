package nc.uap.lfw.login.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class MainMenuItemVO extends SuperVO {

	public static final String FUNC_CODE = "func_code";
	public static final String FUNC_SRC = "func_src";
	
	private static final long serialVersionUID = 1L;
	private String funcode;
	private java.lang.String menuitemcode;
	private java.lang.String menuitemname;
	private String menuitemdes; 
	private String funurl;
	private UFBoolean isLeafMenu; //是否叶子节点
	private String parentcode;
	private String resid;
	
	public String getResid() {
		return resid;
	}
	public void setResid(String resid) {
		this.resid = resid;
	}
	public String getFuncode() {
		return funcode;
	}
	public void setFuncode(String funcode) {
		this.funcode = funcode;
	}
	public java.lang.String getMenuitemcode() {
		return menuitemcode;
	}
	public void setMenuitemcode(java.lang.String menuitemcode) {
		this.menuitemcode = menuitemcode;
	}
	public java.lang.String getMenuitemname() {
		return menuitemname;
	}
	public void setMenuitemname(java.lang.String menuitemname) {
		this.menuitemname = menuitemname;
	}
	public String getMenuitemdes() {
		return menuitemdes;
	}
	public void setMenuitemdes(String menuitemdes) {
		this.menuitemdes = menuitemdes;
	}
	public UFBoolean getIsLeafMenu() {
		return isLeafMenu;
	}
	public void setIsLeafMenu(UFBoolean isLeafMenu) {
		this.isLeafMenu = isLeafMenu;
	}
	public String getFunurl() {
		return funurl;
	}
	public void setFunurl(String funurl) {
		this.funurl = funurl;
	}
	public String getParentcode() {
		return parentcode;
	}
	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}
	@Override
	public String getPKFieldName() {
		return null;
	}
	@Override
	public String getParentPKFieldName() {
		return null;
	}
	@Override
	public String getTableName() {
		return null;
	}
	 
}
