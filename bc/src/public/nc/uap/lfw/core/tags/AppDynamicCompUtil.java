package nc.uap.lfw.core.tags;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.ToolBarItem;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctx.ApplicationContext;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.model.util.ElementObserver;
import nc.uap.lfw.core.page.LfwWidget;

import org.json.JSONObject;

/**
 * ��̬��������ű�����
 * 
 * @author dingrf
 *
 */
public class AppDynamicCompUtil {
	private static final String DYNAMICFIELD = ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "_dynamicfield";
	private static final String REPLACE_COMBODATA = "replace_combodata";
	private ApplicationContext appCtx; 
	private ViewContext viewCtx;
	public AppDynamicCompUtil(ApplicationContext appCtx, ViewContext viewCtx) {
		this.appCtx = appCtx;
		this.viewCtx = viewCtx;
	}
	/**
	 * ��������Toolbar����Ľű�
	 * @return
	 */
	public String generateAddToolbarItemScript(String toolbarId, ToolBarItem item, String widgetId) {
		StringBuffer buf = new StringBuffer();
		ToolbarCompTag tag = new ToolbarCompTag();
		if (item.getType() != null && ToolBarItem.BUTTON_TYPE.equals(item.getType())) {  // ��ť����
			String itemShowId = "tbi_" + item.getId();
			buf.append("var ")
			    .append(itemShowId)
			    .append(" = ")
			    .append("pageUI.getWidget('" + widgetId + "').getComponent('" + toolbarId + "')")
				.append(".addButton(\"")
				.append(item.getId())
				.append("\", \"")
				.append(tag.translate(item.getI18nName(), item.getText(), item.getLangDir()))
				.append("\", \"")
				.append(tag.translate(item.getTip(), item.getTipI18nName(), item.getLangDir()))
				.append("\", \"")
//				.append(tag.getRealImgPath(item.getRefImg()))
				.append(item.getRealRefImg())
				.append("\", \"")
				.append(item.getAlign()) // �˴����õ�alignΪ��ť��Toolbar�е�λ��
				.append("\", ")
				.append(item.isWithSep())
				.append(");\n");
			
			tag.setId(toolbarId);
			String event = tag.addEventSupport(item, widgetId, itemShowId, toolbarId);
			buf.append(event);
		}
		return buf.toString();
	}
	
	public String modifyGridStruct(String widgetId, Dataset ds, GridComp grid, Field[] fields){
		return modifyGridStruct(widgetId, ds, grid, fields, null, -1, true);
	}
	
	public String modifyGridStruct(String widgetId, Dataset ds, GridComp grid, Field[] fields, boolean clearOriginal){
		return modifyGridStruct(widgetId, ds, grid, fields, null, -1, clearOriginal);
	}
	
	public String modifyGridStruct(String widgetId, Dataset ds, GridComp grid, Field[] fields, int colIndex, boolean clearOriginal){
		return modifyGridStruct(widgetId, ds, grid, fields, null, colIndex, clearOriginal);
	}
	
	public String modifyGridStruct(String widgetId, Dataset ds, GridComp grid, Field[] fields, IGridColumn[] cols, int colIndex, boolean clearOriginal){
		ds.clear();
		if(clearOriginal){
			FieldSet fs = ds.getFieldSet();
			int count = fs.getFieldCount();
			for (int i = count - 1; i >= 0; i--) {
				Field gc = fs.getField(i);
				if(gc.getExtendAttributeValue(DYNAMICFIELD) != null){
					fs.removeField(gc);
				}
			}
			for (int i = 0; i < fields.length; i++) {
				fields[i].setExtendAttribute(DYNAMICFIELD, "1");
				fs.addField(fields[i]);
			}
			
			count = grid.getColumnCount();
			for (int i = count - 1; i >= 0; i --) {
				IGridColumn col = grid.getColumn(i);
				if(((ExtendAttributeSupport)col).getExtendAttributeValue(DYNAMICFIELD) != null)
					grid.removeColumn(col);
			}
			
			if(cols == null){
				cols = generateColsFromFields(fields);
			}
			
			int index = colIndex;
			if(colIndex == -1)
				index = grid.getColumnCount();
			for (int i = 0; i < cols.length; i++) {
				grid.insertColumn(index ++, cols[i]);
			}
		}
		
		/**
		 * ���µ�ҳ���ʼ�ṹ��
		 */
		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget(widgetId);
		widget.getViewComponents().getComponentsMap().remove(grid.getId());
		widget.getViewComponents().addComponent((WebComponent) grid.clone());
		
		widget.getViewModels().removeDataset(ds.getId());
		widget.getViewModels().addDataset((Dataset) ds.clone());
		appCtx.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').getDataset('" + ds.getId() + "').clear()");
		appCtx.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').getDataset('" + ds.getId() + "').modifyStruct(" + DatasetMetaUtil.generateMeta(ds) + ")");
		String modelScript = GridModelUtil.generateGridModel(ds, grid, widget);
		appCtx.addBeforeExecScript(modelScript);
		appCtx.addBeforeExecScript("pageUI.getWidget('" + widgetId + "').getComponent('" + grid.getId() + "').setModel(model)");
		
		ds.setCurrentKey(Dataset.MASTER_KEY);
		return null;
	}
	
	private IGridColumn[] generateColsFromFields(Field[] fields) {
		IGridColumn[] cols = new IGridColumn[fields.length];
		for (int i = 0; i < fields.length; i++) {
			GridColumn col = new GridColumn();
			cols[i] = col;
			col.setId(fields[i].getId());
			col.setText(fields[i].getText());
			col.setI18nName(fields[i].getI18nName());
			String dataType = fields[i].getDataType();
			if(dataType == null || dataType.equals(""))
				dataType = StringDataTypeConst.STRING;
			col.setDataType(dataType);
			col.setEditorType(EditorTypeConst.STRINGTEXT);
			col.setExtendAttribute(DYNAMICFIELD, "1");
			col.setField(fields[i].getId());
		}
		return cols;
	}
	/**
	 * ˢ�����ݼ�
	 * @param dataset
	 */
	public void refreshDataset(Dataset dataset){
		refreshDataset(dataset, null);
	}
	
	/**
	 * ˢ�����ݼ�
	 * @param dataset
	 * @param pageIndex
	 */
	public void refreshDataset(Dataset dataset, Integer pageIndex) {
		String currentKey = dataset.getCurrentKey();
		if(pageIndex != null && pageIndex != 0){
			appCtx.addExecScript("pageUI.getWidget('" + viewCtx.getId() + "').getDataset('" + dataset.getId() + "')" +
					".setCurrentPage('" + currentKey + "'," + pageIndex + ", null, null, true" + ")");
		}
		else{
			dataset.removeRowSet(currentKey);
			appCtx.addExecScript("pageUI.getWidget('" + viewCtx.getId() + "').getDataset('" + dataset.getId() + "')" +
					".setCurrentPage('" + currentKey + "')");
			
		}
	}
	
	/**
	 * ����gridĳһ�е���ʾ����
	 * 
	 * @param widgetId
	 * @param gridId
	 * @param columnId
	 * @param text
	 */
	public void replaceGridColumnText(String widgetId,String gridId,String columnId,String text){
		appCtx.addExecScript("pageUI.getWidget('" + widgetId + "').getComponent('" + gridId + "').getBasicHeaderById('" + columnId + "').replaceText('" + text + "')");
	} 
	
	
	/**
	 * ����ĳ��tabҳǩ
	 * @param tabId
	 */
	public void activeTabItem(String widgetId,String tabId, String tabIndex){
		appCtx.addExecScript("pageUI.getWidget('" + widgetId + "').getTab('" + tabId + "').activeTab('" + tabIndex + "')");
	}
	
	/**
	 * ��̬�޸���������ֵ
	 * @param oriId
	 * @param widgetId
	 * @param cd
	 */
	public void replaceComboData(String oriId, String widgetId, ComboData cd){
		StringBuffer buf = new StringBuffer();
		String[] keyValue = getMergedCDkeyValues(cd);
		String pageUI = "pageUI";
		LfwWidget widget = LfwRuntimeEnvironment.getWebContext().getPageMeta().getWidget(widgetId);
		if (widget == null && LfwRuntimeEnvironment.getWebContext().getParentPageMeta() != null){
			widget = LfwRuntimeEnvironment.getWebContext().getParentPageMeta().getWidget(widgetId);
			pageUI = "parent.pageUI";
		}
		if (keyValue == null){
			buf.append(pageUI).append(".getWidget('")
			.append(widgetId)
			.append("').replaceComboData('")
			.append(oriId)
			.append("');\n");
		}
		else{
			buf.append(pageUI).append(".getWidget('")
			.append(widgetId)
			.append("').replaceComboData('")
			.append(oriId)
			.append("',")
			.append(keyValue[1])
			.append(",")
			.append(keyValue[0])
			.append(");\n");
		}
		
		ComboData oriCb = widget.getViewModels().getComboData(oriId);
		oriCb.setExtendAttribute(ElementObserver.OBS_IN, "1");
		oriCb.removeAllComboItems();
		CombItem[] items = cd.getAllCombItems();
		if(items != null){
			for (int i = 0; i < items.length; i++) {
				oriCb.addCombItem(items[i]);
			}
		}
		oriCb.removeExtendAttribute(ElementObserver.OBS_IN);
		
		String oldScript = (String) appCtx.getAppAttribute(REPLACE_COMBODATA + widgetId + "_" + oriId);
		if(oldScript != null){
			if(appCtx.getExecScript() != null){
				for(int i = 0; i < appCtx.getExecScript().size(); i++){
					String script = appCtx.getExecScript().get(i);
					if(script.equals(oldScript)){
						appCtx.removeExecScript(i);
						break;
					}
				}
			}
		}
		appCtx.addExecScript(buf.toString());
		appCtx.addAppAttribute(REPLACE_COMBODATA + widgetId + "_" + oriId, buf.toString());
		
	}
	
	/**
	 * ����dataset��metaData��ؽű�
	 * 
	 * @param dataset
	 */
	public void generateDsMetaDataScript(Dataset dataset){
		int fieldCount = dataset.getFieldSet().getFieldCount();
		//"filed1":"4","filed2":4
		JSONObject jsonObj = new JSONObject();
		JSONObject precisionObj = new JSONObject();
		for(int i = 0; i < fieldCount; i ++){
			Field field = dataset.getFieldSet().getField(i);
			if (field.isCtxChanged() && field.checkCtxPropertyChanged("precision")){
				String precision = field.getPrecision();
				if (precision != null){
					precisionObj.put(field.getId(), precision);
				}
			}
		}
		if (precisionObj.length() > 0)
			jsonObj.put("precision", precisionObj);
		if (jsonObj.length() > 0)
			appCtx.addBeforeExecScript("pageUI.getWidget('" + viewCtx.getId() + "').getDataset('" + dataset.getId() + "').setMeta(" + jsonObj.toString() + ");");
	}
	
	private static String[] getMergedCDkeyValues(ComboData cd) {
		CombItem[] items = cd.getAllCombItems();
		if (items == null)
			return null;
		StringBuffer keyBuf = new StringBuffer();
		StringBuffer valueBuf = new StringBuffer();
		keyBuf.append("[");
		valueBuf.append("[");
		for (int i = 0; i < items.length; i++) {
			CombItem item = items[i];
			keyBuf.append("'")
			      .append(item.getValue())
			      .append("'");
			
			valueBuf.append("'")
		      .append(item.getText())
		      .append("'");
			
			if(i != (items.length - 1)){
				keyBuf.append(",");
				valueBuf.append(",");
			}
		}
		keyBuf.append("]");
		valueBuf.append("]");
		return new String[]{keyBuf.toString(), valueBuf.toString()};
	}
}
