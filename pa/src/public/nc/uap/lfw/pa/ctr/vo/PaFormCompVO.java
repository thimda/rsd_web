/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-7-25
 * @since 1.6
 * 
 * 参考FormComp.java类和WidgetToXml.java类
 */
public class PaFormCompVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;
	private String pk_formcomp;
	private String pk_parent;
	
	private Integer columncount;
	private String dataset;
	private Integer rowheight;
	private Integer elewidth;
	
	private String caption;
	private Integer labelminwidth;
	private UFBoolean withforms;
	private String backgroundcolor;
	private Integer rendertype;
	private UFBoolean renderhiddeneles;
	private UFBoolean readonlys;
	private String labelposition;
	
	
	/**
	 * widget页面中的属性
	 */
	
	private String id;
	private String width;
	private String positions;
	private String itop;
	private String ileft;
	private String contextmenu;
	private String classname;
	private UFBoolean enableds;
	private UFBoolean visibles;
	
	private String parentid;
	private String widgetid;
	@Override
	public String getPKFieldName() {
		return "pk_formcomp";
	}
	@Override
	public String getTableName() {
		return "pa_formcomp";
	}
	public String getPk_formcomp() {
		return pk_formcomp;
	}
	public void setPk_formcomp(String pk_formcomp) {
		this.pk_formcomp = pk_formcomp;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public Integer getColumncount() {
		return columncount;
	}
	public void setColumncount(Integer columncount) {
		this.columncount = columncount;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public Integer getRowheight() {
		return rowheight;
	}
	public void setRowheight(Integer rowheight) {
		this.rowheight = rowheight;
	}
	public Integer getElewidth() {
		return elewidth;
	}
	public void setElewidth(Integer elewidth) {
		this.elewidth = elewidth;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public Integer getLabelminwidth() {
		return labelminwidth;
	}
	public void setLabelminwidth(Integer labelminwidth) {
		this.labelminwidth = labelminwidth;
	}
	public UFBoolean getWithforms() {
		return withforms;
	}
	public void setWithforms(UFBoolean withforms) {
		this.withforms = withforms;
	}
	public String getBackgroundcolor() {
		return backgroundcolor;
	}
	public void setBackgroundcolor(String backgroundcolor) {
		this.backgroundcolor = backgroundcolor;
	}
	public Integer getRendertype() {
		return rendertype;
	}
	public void setRendertype(Integer rendertype) {
		this.rendertype = rendertype;
	}
	public UFBoolean getRenderhiddeneles() {
		return renderhiddeneles;
	}
	public void setRenderhiddeneles(UFBoolean renderhiddeneles) {
		this.renderhiddeneles = renderhiddeneles;
	}
	public UFBoolean getReadonlys() {
		return readonlys;
	}
	public void setReadonlys(UFBoolean readonlys) {
		this.readonlys = readonlys;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	public String getItop() {
		return itop;
	}
	public void setItop(String itop) {
		this.itop = itop;
	}
	public String getIleft() {
		return ileft;
	}
	public void setIleft(String ileft) {
		this.ileft = ileft;
	}
	public String getContextmenu() {
		return contextmenu;
	}
	public void setContextmenu(String contextmenu) {
		this.contextmenu = contextmenu;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public UFBoolean getEnableds() {
		return enableds;
	}
	public void setEnableds(UFBoolean enableds) {
		this.enableds = enableds;
	}
	public UFBoolean getVisibles() {
		return visibles;
	}
	public void setVisibles(UFBoolean visibles) {
		this.visibles = visibles;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getWidgetid() {
		return widgetid;
	}
	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}
	public String getLabelposition() {
		return labelposition;
	}
	public void setLabelposition(String labelposition) {
		this.labelposition = labelposition;
	}
	
}
