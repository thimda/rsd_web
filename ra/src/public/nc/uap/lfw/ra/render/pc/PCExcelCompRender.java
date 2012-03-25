package nc.uap.lfw.ra.render.pc;

import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IExcelColumn;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.IEventSupport;
import nc.uap.lfw.core.event.conf.ExcelCellListener;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.core.tags.ExcelModelUtil;
import nc.uap.lfw.core.tags.GridModelUtil;
import nc.uap.lfw.jsp.uimeta.UIComponent;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIExcelComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.render.UINormalComponentRender;
import nc.uap.lfw.ra.render.UIRender;

/**
 * @author renxh
 * excel控件渲染器
 * @param <T>
 * @param <K>
 */
public class PCExcelCompRender extends UINormalComponentRender<UIExcelComp,ExcelComp> {

	public PCExcelCompRender(UIExcelComp uiEle,ExcelComp webEle, UIMeta uimeta, PageMeta pageMeta, UIRender<? extends UIElement, ? extends WebElement> parentPanel) {
		super(uiEle, webEle, uimeta, pageMeta, parentPanel);
	}
	@Override
	public String generateBodyHtml() {

		return super.generateBodyHtml();
	}
	@Override
	public String generateBodyHtmlDynamic() {

		return super.generateBodyHtmlDynamic();
	}
	@Override
	public String generateBodyScript() {
		ExcelComp excel = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		Dataset ds = this.getDataset();

		List<IExcelColumn> headers = excel.getColumnList();
		StringBuffer buf = new StringBuffer();
		if (!headers.isEmpty()) {
			buf.append("var ").append(getVarId()).append(" = new ExcelComp(document.getElementById(\"").append(getDivId()).append("\"),\"")
					.append(excel.getId()).append("\",0,0,\"100%\",\"100%\",\"relative\",").append(excel.isEditable()).append(",").append(
							excel.isMultiSelect()).append(",").append(excel.isShowNumCol()).append(",").append(excel.isShowSumRow()).append(",");
			if (ds.getPageSize() == -1)
				buf.append("null,");
			else {
				buf.append("{pageSize:").append(ds.getPageSize()).append(",simplePageBar:" + (excel.isSimplePageBar() ? "true" : "false") + "},");
			}
			if (excel.getGroupColumns() == null || excel.getGroupColumns().equals(""))
				buf.append("null");
			else
				buf.append("'" + excel.getGroupColumns() + "'");
			buf.append(",").append(excel.isSortable()).append(",'").append(uiComp.getClassName() == null ? "" : uiComp.getClassName()).append("',")
					.append(excel.isPagenationTop()).append(",").append(excel.isShowColInfo()).append(",'").append(excel.getOddType()).append("',");
			if (excel.getGroupColumns() != null && excel.getGroupColumns().length() > 0)
				buf.append(true);
			else
				buf.append(false);
			buf.append(",").append(excel.isShowHeader()).append(");\n");

			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append(getVarId() + ".widget=" + widget + ";\n");
			buf.append(widget + ".addComponent(" + getVarId() + ");\n");

			// 设置行高
			String rowHeight = excel.getRowHeight();
			if (rowHeight != null && rowHeight != "")
				buf.append(getVarId() + ".setRowHeight(" + rowHeight + ");\n");
			// 设置表头行高
			String headerRowHeight = excel.getHeaderRowHeight();
			if (headerRowHeight != null && headerRowHeight != "")
				buf.append(getVarId() + ".setHeaderRowHeight(" + headerRowHeight + ");\n");

			String modelStr = ExcelModelUtil.generateExcelModel(ds, headers, getCurrWidget());
			buf.append(modelStr);
			buf.append(getVarId() + ".setModel(model);\n");

		}
		return buf.toString();
	}
	
//	@Override
//	public String generateBodyScriptDynamic() {
//		ExcelComp excel = this.getWebElement();
//		UIComponent uiComp = this.getUiElement();
//		Dataset ds = this.getDataset();
//
//		List<IExcelColumn> headers = excel.getColumnList();
//		StringBuffer buf = new StringBuffer();
//		if (!headers.isEmpty()) {
//			buf.append("var ").append(getVarId()).append(" = new ExcelComp(").append(getDivId()).append(",\"")
//					.append(excel.getId()).append("\",0,0,\"100%\",\"100%\",\"relative\",").append(excel.isEditable()).append(",").append(
//							excel.isMultiSelect()).append(",").append(excel.isShowNumCol()).append(",").append(excel.isShowSumRow()).append(",");
//			if (ds.getPageSize() == -1)
//				buf.append("null,");
//			else {
//				buf.append("{pageSize:").append(ds.getPageSize()).append(",simplePageBar:" + (excel.isSimplePageBar() ? "true" : "false") + "},");
//			}
//			if (excel.getGroupColumns() == null || excel.getGroupColumns().equals(""))
//				buf.append("null");
//			else
//				buf.append("'" + excel.getGroupColumns() + "'");
//			buf.append(",").append(excel.isSortable()).append(",'").append(uiComp.getClassName() == null ? "" : uiComp.getClassName()).append("',")
//					.append(excel.isPagenationTop()).append(",").append(excel.isShowColInfo()).append(",'").append(excel.getOddType()).append("',");
//			if (excel.getGroupColumns() != null && excel.getGroupColumns().length() > 0)
//				buf.append(true);
//			else
//				buf.append(false);
//			buf.append(",").append(excel.isShowHeader()).append(");\n");
//
//			String widget = WIDGET_PRE + this.getCurrWidget().getId();
//			buf.append(getVarId() + ".widget=" + widget + ";\n");
//			buf.append(widget + ".addComponent(" + getVarId() + ");\n");
//
//			// 设置行高
//			String rowHeight = excel.getRowHeight();
//			if (rowHeight != null && rowHeight != "")
//				buf.append(getVarId() + ".setRowHeight(" + rowHeight + ");\n");
//			// 设置表头行高
//			String headerRowHeight = excel.getHeaderRowHeight();
//			if (headerRowHeight != null && headerRowHeight != "")
//				buf.append(getVarId() + ".setHeaderRowHeight(" + headerRowHeight + ");\n");
//
//			String modelStr = ExcelModelUtil.generateExcelModel(ds, headers, getCurrWidget());
//			buf.append(modelStr);
//			buf.append(getVarId() + ".setModel(model);\n");
//
//		}
//		return buf.toString();
//	}

	protected String getSourceType(IEventSupport ele) {
		return LfwPageContext.SOURCE_TYPE_GRID;
	}

	/* (non-Javadoc)
	 * @see nc.uap.lfw.ra.render.UIRender#addProxyParam(java.lang.StringBuffer, java.lang.String)
	 */
	protected void addProxyParam(StringBuffer buf, String eventName) {
		if (ExcelCellListener.AFTER_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', cellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', cellEvent.colIndex);\n");
			buf.append("proxy.addParam('newValue', cellEvent.newValue);\n");
			buf.append("proxy.addParam('oldValue', cellEvent.oldValue);\n");
		} else if (ExcelCellListener.BEFORE_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', cellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', cellEvent.colIndex);\n");
		} else if (ExcelCellListener.ON_CELL_CLICK.equals(eventName) || ExcelCellListener.CELL_EDIT.equals(eventName)) {
			buf.append("proxy.addParam('rowIndex', cellEvent.rowIndex);\n");
			buf.append("proxy.addParam('colIndex', cellEvent.colIndex);\n");
		}
	}

	@Override
	public void notifyUpdate(UIMeta uiMeta, PageMeta pageMeta, Object obj){
		if (obj instanceof Map){
			Map<String,Object> map = (Map<String,Object>)obj;
			String widgetId = (String)map.get("widgetId");
			LfwWidget widget = getPageMeta().getWidget(widgetId);
			String excelId = (String)map.get("excelId");
			ExcelComp excel = (ExcelComp)widget.getViewComponents().getComponent(excelId);
			String type = (String)map.get("type");
			if (type != null && type.equals("addColumn")){
				IExcelColumn column = (IExcelColumn)map.get("column");
				String script = this.addExcelColumnScript(widget, excel, column);			
				this.addDynamicScript(script);
			}
		}
	}
	
	/**
	 * 动态增加column
	 * 
	 * @param widget
	 * @param grid
	 * @param column
	 * @return
	 */
	private String addExcelColumnScript(LfwWidget widget, ExcelComp excel,
			IExcelColumn column) {
		StringBuffer buf = new StringBuffer();
		String varEc = "$c_"+ excel.getId();
		String varModel = "model";
		if(widget != null){
			buf.append("var "+varEc).append(" = pageUI.getWidget('" + widget.getId() + "').getComponent('" + excel.getId() + "');\n");
			buf.append("var "+varModel+" = " + varEc + ".model;\n");
			Dataset ds = widget.getViewModels().getDataset(excel.getDataset());
			String varDs = "$c_" + ds.getId();
			buf.append(" "+ExcelModelUtil.generalExcelHeader(ds, widget, column) + ";\n");
			buf.append("var "+varDs).append(" = pageUI.getWidget('" + widget.getId() + "').getDataset('" + ds.getId() + "');\n");
			buf.append(varModel).append(".setDataSet("+varDs+");\n");
			buf.append(varEc).append(".setModel("+varModel+");\n");
		}
		return buf.toString();
	}
	
	
}
