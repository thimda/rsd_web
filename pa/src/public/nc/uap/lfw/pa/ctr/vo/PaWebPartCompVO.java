package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;

public class PaWebPartCompVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String ileft;
	private String itop;
	private String width;
	private String height;
	private String positions;
	private String classname;
	private String contentfetcher;
	
	private String widgetid;
	private String parentid;
	private String pk_parent;
	
	
	@Override
	public String getPKFieldName() {
		return "pk_webpartcomp";
	}

	@Override
	public String getTableName() {
		return "pa_webpartcomp";
	}
	
	public String getContentfetcher() {
		return contentfetcher;
	}

	public void setContentfetcher(String contentfetcher) {
		this.contentfetcher = contentfetcher;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWidgetid() {
		return widgetid;
	}

	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getPk_parent() {
		return pk_parent;
	}

	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}

	public String getIleft() {
		return ileft;
	}

	public void setIleft(String ileft) {
		this.ileft = ileft;
	}

	public String getItop() {
		return itop;
	}

	public void setItop(String itop) {
		this.itop = itop;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}
}
