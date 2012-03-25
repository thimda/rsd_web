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
 */
public class PaGridCompVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private String pk_gridcomp;
	private String pk_parent;
	
	private String dataset;
	private UFBoolean editables;
	private UFBoolean multiselects;
	private String rowheight;
	private String headerrowheight;
	private UFBoolean shownumcols;
	private UFBoolean showsumrows;
	private String pagesize;
	private UFBoolean simplepagebars;
	private String groupcolumns;
	private UFBoolean sortables;
	private UFBoolean showheaders;
	private String caption;
	private String showcolumns;
	private String oddtype;
	private UFBoolean pagenationtops;
	private UFBoolean showcolinfos;

	/**
	 * widget页面中包含的
	 */
	private String classname;
	private UFBoolean enableds;
	private String height;
	private String width;
	private String ileft;
	private String id;
	private String positions;
	private UFBoolean visibles;
	
	private String parentid;
	private String widgetid;

	@Override
	public String getPKFieldName() {
		return "pk_gridcomp";
	}
	@Override
	public String getTableName() {
		return "pa_gridcomp";
	}
	public String getPk_gridcomp() {
		return pk_gridcomp;
	}
	public void setPk_gridcomp(String pk_gridcomp) {
		this.pk_gridcomp = pk_gridcomp;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public UFBoolean getEditables() {
		return editables;
	}
	public void setEditables(UFBoolean editables) {
		this.editables = editables;
	}
	public UFBoolean getMultiselects() {
		return multiselects;
	}
	public void setMultiselects(UFBoolean multiselects) {
		this.multiselects = multiselects;
	}
	public String getRowheight() {
		return rowheight;
	}
	public void setRowheight(String rowheight) {
		this.rowheight = rowheight;
	}
	public String getHeaderrowheight() {
		return headerrowheight;
	}
	public void setHeaderrowheight(String headerrowheight) {
		this.headerrowheight = headerrowheight;
	}
	public UFBoolean getShownumcols() {
		return shownumcols;
	}
	public void setShownumcols(UFBoolean shownumcols) {
		this.shownumcols = shownumcols;
	}
	public UFBoolean getShowsumrows() {
		return showsumrows;
	}
	public void setShowsumrows(UFBoolean showsumrows) {
		this.showsumrows = showsumrows;
	}
	public String getPagesize() {
		return pagesize;
	}
	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}
	public UFBoolean getSimplepagebars() {
		return simplepagebars;
	}
	public void setSimplepagebars(UFBoolean simplepagebars) {
		this.simplepagebars = simplepagebars;
	}
	public String getGroupcolumns() {
		return groupcolumns;
	}
	public void setGroupcolumns(String groupcolumns) {
		this.groupcolumns = groupcolumns;
	}
	public UFBoolean getSortables() {
		return sortables;
	}
	public void setSortables(UFBoolean sortables) {
		this.sortables = sortables;
	}
	public UFBoolean getShowheaders() {
		return showheaders;
	}
	public void setShowheaders(UFBoolean showheaders) {
		this.showheaders = showheaders;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getShowcolumns() {
		return showcolumns;
	}
	public void setShowcolumns(String showcolumns) {
		this.showcolumns = showcolumns;
	}
	public String getOddtype() {
		return oddtype;
	}
	public void setOddtype(String oddtype) {
		this.oddtype = oddtype;
	}
	public UFBoolean getPagenationtops() {
		return pagenationtops;
	}
	public void setPagenationtops(UFBoolean pagenationtops) {
		this.pagenationtops = pagenationtops;
	}
	public UFBoolean getShowcolinfos() {
		return showcolinfos;
	}
	public void setShowcolinfos(UFBoolean showcolinfos) {
		this.showcolinfos = showcolinfos;
	}

	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getIleft() {
		return ileft;
	}
	public void setIleft(String ileft) {
		this.ileft = ileft;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPositions() {
		return positions;
	}
	public void setPositions(String positions) {
		this.positions = positions;
	}
	public UFBoolean getVisibles() {
		return visibles;
	}
	public void setVisibles(UFBoolean visibles) {
		this.visibles = visibles;
	}
	public UFBoolean getEnableds() {
		return enableds;
	}
	public void setEnableds(UFBoolean enableds) {
		this.enableds = enableds;
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
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}

}
