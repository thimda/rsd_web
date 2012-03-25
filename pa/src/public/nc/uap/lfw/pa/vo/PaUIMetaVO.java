/**
 * 
 */
package nc.uap.lfw.pa.vo;

import nc.vo.pub.lang.UFBoolean;

/**
 * @author wupeng1
 * @version 6.0 2011-7-20
 * @since 1.6
 */
public class PaUIMetaVO extends PaLayoutPanelVO {

	private static final long serialVersionUID = 1L;
	private String pk_uimeta;
	private String widgetid;
	private String folderpath;
	private Integer jquery;
	private Integer chart;
	private String includejs;
	private String includecss;
	private Integer jseditor;
	private Integer jsexcel;
	private String generateclass;
	private String tabbody;
	private UFBoolean flowmode;
	
	private String pk_child;
	private String childtype;
	
	private String id;
	private String pk_parent;
	private String parentid;
	
	
	@Override
	public String getPKFieldName() {
		return "pk_uimeta";
	}


	@Override
	public String getTableName() {
		return "pa_uimeta";
	}


	public String getPk_uimeta() {
		return pk_uimeta;
	}


	public void setPk_uimeta(String pk_uimeta) {
		this.pk_uimeta = pk_uimeta;
	}


	public String getIncludejs() {
		return includejs;
	}


	public void setIncludejs(String includejs) {
		this.includejs = includejs;
	}


	public String getIncludecss() {
		return includecss;
	}


	public void setIncludecss(String includecss) {
		this.includecss = includecss;
	}

	public String getPk_parent() {
		return pk_parent;
	}


	public void setPk_parent(String pk_parent) {
		this.pk_parent = pk_parent;
	}


	public String getGenerateclass() {
		return generateclass;
	}


	public void setGenerateclass(String generateclass) {
		this.generateclass = generateclass;
	}

	public String getPk_child() {
		return pk_child;
	}


	public void setPk_child(String pk_child) {
		this.pk_child = pk_child;
	}


	public String getChildtype() {
		return childtype;
	}


	public void setChildtype(String childtype) {
		this.childtype = childtype;
	}


	public String getTabbody() {
		return tabbody;
	}


	public void setTabbody(String tabbody) {
		this.tabbody = tabbody;
	}


	public String getWidgetid() {
		return widgetid;
	}


	public void setWidgetid(String widgetid) {
		this.widgetid = widgetid;
	}


	public String getFolderpath() {
		return folderpath;
	}


	public void setFolderpath(String folderpath) {
		this.folderpath = folderpath;
	}


	public String getParentid() {
		return parentid;
	}


	public void setParentid(String parentid) {
		this.parentid = parentid;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Integer getJquery() {
		return jquery;
	}


	public void setJquery(Integer jquery) {
		this.jquery = jquery;
	}


	public Integer getChart() {
		return chart;
	}


	public void setChart(Integer chart) {
		this.chart = chart;
	}


	public Integer getJseditor() {
		return jseditor;
	}


	public void setJseditor(Integer jseditor) {
		this.jseditor = jseditor;
	}


	public Integer getJsexcel() {
		return jsexcel;
	}


	public void setJsexcel(Integer jsexcel) {
		this.jsexcel = jsexcel;
	}


	public UFBoolean getFlowmode() {
		return flowmode;
	}


	public void setFlowmode(UFBoolean flowmode) {
		this.flowmode = flowmode;
	}

}
