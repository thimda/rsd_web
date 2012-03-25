/**
 * 
 */
package nc.uap.lfw.pa.ctr.vo;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;


/**
 * @author wupeng1
 * @version 6.0 2011-8-5
 * @since 1.6
 */
public class PaGridColumnVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	
	private String pk_gridcolumn;
	private String id;
	private String field;
	private String langdir;
	private String i18nname;
	private String itext;
	private int width;
	private String datatype;
	private UFBoolean sortables; 
	private UFBoolean visibles;
	private UFBoolean editables;
	private String columbgcolor;
	private String textalign;
	private String textcolor;
	private UFBoolean fixedheaders;
	private String editortype;
	private String rendertype;
	private String refcombodata;
	private String refnode;
	private UFBoolean imageonlys;
	private String maxvalue;
	private String minvalue;
	private String precisions;
	private String maxlength;
	private UFBoolean nullables;
	private UFBoolean sumcols;
	private UFBoolean autoexpands;
	private String colmngroup;
	private String gridcomp;
	private String columngroup;
	private String pk_parent;
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_gridcolumn";
	}
	@Override
	public String getTableName() {
		return "pa_gridcolumn";
	}
	public String getPk_gridcolumn() {
		return pk_gridcolumn;
	}
	public void setPk_gridcolumn(String pk_gridcolumn) {
		this.pk_gridcolumn = pk_gridcolumn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getLangdir() {
		return langdir;
	}
	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}
	public String getI18nname() {
		return i18nname;
	}
	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}
	public String getItext() {
		return itext;
	}
	public void setItext(String itext) {
		this.itext = itext;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public UFBoolean getSortables() {
		return sortables;
	}
	public void setSortables(UFBoolean sortables) {
		this.sortables = sortables;
	}
	public UFBoolean getVisibles() {
		return visibles;
	}
	public void setVisibles(UFBoolean visibles) {
		this.visibles = visibles;
	}
	public UFBoolean getEditables() {
		return editables;
	}
	public void setEditables(UFBoolean editables) {
		this.editables = editables;
	}
	public String getColumbgcolor() {
		return columbgcolor;
	}
	public void setColumbgcolor(String columbgcolor) {
		this.columbgcolor = columbgcolor;
	}
	public String getTextalign() {
		return textalign;
	}
	public void setTextalign(String textalign) {
		this.textalign = textalign;
	}
	public String getTextcolor() {
		return textcolor;
	}
	public void setTextcolor(String textcolor) {
		this.textcolor = textcolor;
	}
	public UFBoolean getFixedheaders() {
		return fixedheaders;
	}
	public void setFixedheaders(UFBoolean fixedheaders) {
		this.fixedheaders = fixedheaders;
	}
	public String getEditortype() {
		return editortype;
	}
	public void setEditortype(String editortype) {
		this.editortype = editortype;
	}
	public String getRendertype() {
		return rendertype;
	}
	public void setRendertype(String rendertype) {
		this.rendertype = rendertype;
	}
	public String getRefcombodata() {
		return refcombodata;
	}
	public void setRefcombodata(String refcombodata) {
		this.refcombodata = refcombodata;
	}
	public String getRefnode() {
		return refnode;
	}
	public void setRefnode(String refnode) {
		this.refnode = refnode;
	}
	public UFBoolean getImageonlys() {
		return imageonlys;
	}
	public void setImageonlys(UFBoolean imageonlys) {
		this.imageonlys = imageonlys;
	}
	public String getMaxvalue() {
		return maxvalue;
	}
	public void setMaxvalue(String maxvalue) {
		this.maxvalue = maxvalue;
	}
	public String getMinvalue() {
		return minvalue;
	}
	public void setMinvalue(String minvalue) {
		this.minvalue = minvalue;
	}
	public String getPrecisions() {
		return precisions;
	}
	public void setPrecisions(String precisions) {
		this.precisions = precisions;
	}
	public String getMaxlength() {
		return maxlength;
	}
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	public UFBoolean getNullables() {
		return nullables;
	}
	public void setNullables(UFBoolean nullables) {
		this.nullables = nullables;
	}
	public UFBoolean getSumcols() {
		return sumcols;
	}
	public void setSumcols(UFBoolean sumcols) {
		this.sumcols = sumcols;
	}
	public UFBoolean getAutoexpands() {
		return autoexpands;
	}
	public void setAutoexpands(UFBoolean autoexpands) {
		this.autoexpands = autoexpands;
	}
	public String getColmngroup() {
		return colmngroup;
	}
	public void setColmngroup(String colmngroup) {
		this.colmngroup = colmngroup;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public String getGridcomp() {
		return gridcomp;
	}
	public void setGridcomp(String gridcomp) {
		this.gridcomp = gridcomp;
	}
	public String getColumngroup() {
		return columngroup;
	}
	public void setColumngroup(String columngroup) {
		this.columngroup = columngroup;
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
}
