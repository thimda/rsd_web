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
public class PaFormElementVO extends SuperVO {
	
	private static final long serialVersionUID = 1L;

	private String pk_formelement;
	
	private String pk_parent;
	
	private Integer rowspan;
	private Integer colspan;
	private Integer inputwidth;
	private String i18nname;
	private String itext;
	private String value;
	private String showvalue;
	private String description;
	private String langdir;
	private String labelcolor;
	private String editortype;
	private String refnode;
	private String relationfield;
	private String datatype;
	private String field;
	private String refcombodata; 
	private int indexs; 
	private String datadivheight; 
	private String defaultvalue; 
	private UFBoolean editables;
	private UFBoolean imageonlys;
	private String maxlength; 
	private UFBoolean selectonlys;
	private UFBoolean nextlines;
	private String maxvalue;
	private String minvalue; 
	private String precisions;
	private String hidebarindices;
	private String hideimageindices;
	private UFBoolean focuss;
	private String tip;
	private String bindid;
	private UFBoolean nullables;
	private UFBoolean attachnexts;
	private String inputassistant;
	protected String height;
	private String elewidth;
	private String classname;	//UI的属性
	/**
	 * widget中包含的属性
	 */
	private String id;
	private UFBoolean enableds;
	private UFBoolean visibles;
	private String width;
	
	private String parentid;
	private String widgetid;
	
	@Override
	public String getPKFieldName() {
		return "pk_formelement";
	}
	@Override
	public String getTableName() {
		return "pa_formelement";
	}

	public String getPk_formelement() {
		return pk_formelement;
	}
	public void setPk_formelement(String pk_formelement) {
		this.pk_formelement = pk_formelement;
	}
	public String getPk_parent() {
		return pk_parent;
	}
	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}
	public Integer getRowspan() {
		return rowspan;
	}
	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}
	public Integer getColspan() {
		return colspan;
	}
	public void setColspan(Integer colspan) {
		this.colspan = colspan;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getShowvalue() {
		return showvalue;
	}
	public void setShowvalue(String showvalue) {
		this.showvalue = showvalue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLangdir() {
		return langdir;
	}
	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}
	public String getLabelcolor() {
		return labelcolor;
	}
	public void setLabelcolor(String labelcolor) {
		this.labelcolor = labelcolor;
	}
	public String getEditortype() {
		return editortype;
	}
	public void setEditortype(String editortype) {
		this.editortype = editortype;
	}
	public String getRefnode() {
		return refnode;
	}
	public void setRefnode(String refnode) {
		this.refnode = refnode;
	}
	public String getRelationfield() {
		return relationfield;
	}
	public void setRelationfield(String relationfield) {
		this.relationfield = relationfield;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getRefcombodata() {
		return refcombodata;
	}
	public void setRefcombodata(String refcombodata) {
		this.refcombodata = refcombodata;
	}
	public int getIndexs() {
		return indexs;
	}
	public void setIndexs(int indexs) {
		this.indexs = indexs;
	}
	public String getDatadivheight() {
		return datadivheight;
	}
	public void setDatadivheight(String datadivheight) {
		this.datadivheight = datadivheight;
	}
	public String getDefaultvalue() {
		return defaultvalue;
	}
	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}
	public UFBoolean getEditables() {
		return editables;
	}
	public void setEditables(UFBoolean editables) {
		this.editables = editables;
	}
	public UFBoolean getImageonlys() {
		return imageonlys;
	}
	public void setImageonlys(UFBoolean imageonlys) {
		this.imageonlys = imageonlys;
	}
	public String getMaxlength() {
		return maxlength;
	}
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	public UFBoolean getSelectonlys() {
		return selectonlys;
	}
	public void setSelectonlys(UFBoolean selectonlys) {
		this.selectonlys = selectonlys;
	}
	public UFBoolean getNextlines() {
		return nextlines;
	}
	public void setNextlines(UFBoolean nextlines) {
		this.nextlines = nextlines;
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
	public String getHidebarindices() {
		return hidebarindices;
	}
	public void setHidebarindices(String hidebarindices) {
		this.hidebarindices = hidebarindices;
	}
	public String getHideimageindices() {
		return hideimageindices;
	}
	public void setHideimageindices(String hideimageindices) {
		this.hideimageindices = hideimageindices;
	}
	public UFBoolean getFocuss() {
		return focuss;
	}
	public void setFocuss(UFBoolean focuss) {
		this.focuss = focuss;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getBindid() {
		return bindid;
	}
	public void setBindid(String bindid) {
		this.bindid = bindid;
	}
	public UFBoolean getNullables() {
		return nullables;
	}
	public void setNullables(UFBoolean nullables) {
		this.nullables = nullables;
	}
	public UFBoolean getAttachnexts() {
		return attachnexts;
	}
	public void setAttachnexts(UFBoolean attachnexts) {
		this.attachnexts = attachnexts;
	}
	public String getInputassistant() {
		return inputassistant;
	}
	public void setInputassistant(String inputassistant) {
		this.inputassistant = inputassistant;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
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
	public Integer getInputwidth() {
		return inputwidth;
	}
	public void setInputwidth(Integer inputwidth) {
		this.inputwidth = inputwidth;
	}
	public String getElewidth() {
		return elewidth;
	}
	public void setElewidth(String elewidth) {
		this.elewidth = elewidth;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}

}
