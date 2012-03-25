package nc.uap.lfw.file.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

/**
 * Lfw文件实体
 * 
 * @author licza
 * 
 */
public class LfwFileVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4495583322946622666L;
	/** 主键 **/
	private String pk_lfwfile;
	/** 文件名 **/
	private String filename;
	/** 显示名称 **/
	private String displayname;
	/** 创建者 **/
	private String creator;
	private String pk_billtype;
	private String pk_billitem;
	private String pk_group;
	/** 文件类型 **/
	private String filetypo;
	/** 文件大小 **/
	private Long   filesize;
	
	private UFBoolean dr;
	private UFDate ts;
	/** 创建时间 **/
	private UFDateTime creattime;
	/** 最后修改时间 **/
	private UFDateTime lastmodifytime;
	/** 最后修改者 **/
	private String lastmodifyer;
	/** 单据新增状态,默认是1；即正常状态；如果是新增初始单据在新增时需将该状态值为0**/
	private String createstatus;
	/**
	 * 扩展项
	 */
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	
	@Override
	public String getPKFieldName() {
		return "pk_lfwfile";
	}

	@Override
	public String getTableName() {
		return "uw_lfwfile";
	}

	public String getPk_lfwfile() {
		return pk_lfwfile;
	}

	public void setPk_lfwfile(String pk_lfwfile) {
		this.pk_lfwfile = pk_lfwfile;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPk_billtype() {
		return pk_billtype;
	}

	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	public String getPk_billitem() {
		return pk_billitem;
	}

	public void setPk_billitem(String pk_billitem) {
		this.pk_billitem = pk_billitem;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public UFBoolean getDr() {
		return dr;
	}

	public void setDr(UFBoolean dr) {
		this.dr = dr;
	}

	public UFDate getTs() {
		return ts;
	}

	public void setTs(UFDate ts) {
		this.ts = ts;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFiletypo() {
		return filetypo;
	}

	public void setFiletypo(String filetypo) {
		this.filetypo = filetypo;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public UFDateTime getCreattime() {
		return creattime;
	}

	public void setCreattime(UFDateTime creattime) {
		this.creattime = creattime;
	}

	public UFDateTime getLastmodifytime() {
		return lastmodifytime;
	}

	public void setLastmodifytime(UFDateTime lastmodifytime) {
		this.lastmodifytime = lastmodifytime;
	}

	public String getLastmodifyer() {
		return lastmodifyer;
	}

	public void setLastmodifyer(String lastmodifyer) {
		this.lastmodifyer = lastmodifyer;
	}
	
	public String getDisplayname() {
		return (displayname != null && displayname.length() > 0) ? displayname : filename;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public void setCreatestatus(String createstatus) {
		this.createstatus = createstatus;
	}

	public String getCreatestatus() {
		return createstatus;
	}
	
}
