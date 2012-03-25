package nc.uap.lfw.mock;

import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.event.AppRequestProcessor;
import nc.uap.lfw.core.event.conf.DatasetListener;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.MouseListener;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.page.PageMeta;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIWidget;

/**
 * 自定义参照的表型参照
 * @author zhangxya
 *
 */
public class MockGridPageModel extends PageModel {
	
	private static final String GRID = "grid";
	private static final String GRIDDS = "masterDs";
	private static final String CANCELBT = "cancelbt";
	private static final String OKBT = "okbt";
	private static final String WIDGET_ID = "main";
	private static final String ID_FIELD = "id";
	private static final String LOAD_CODE = "code";
	private static final String LOAD_NAME = "name";
	@Override
	protected IUIMeta createUIMeta(PageMeta pm) {
		UIMeta uimeta = new UIMeta();
		
		UIWidget widget = new UIWidget();
		widget.setId(WIDGET_ID);
		UIMeta wuimeta = new UIMeta();
		wuimeta.setId(WIDGET_ID + "_meta");
		widget.setUimeta(wuimeta);
		
		constructViewUI(wuimeta);
		
		uimeta.setReference(1);
		uimeta.setElement(widget);
		return uimeta;
	}

	private void constructViewUI(UIMeta wuimeta) {
		UIFlowvLayout flvLayout = new UIFlowvLayout();
		flvLayout.setId("flowv1");
		flvLayout.setWidgetId(WIDGET_ID);
		wuimeta.setElement(flvLayout);
		
		UIFlowvPanel flvPanel1 = new UIFlowvPanel();
		flvPanel1.setId("flowvp1");
		flvLayout.addPanel(flvPanel1);
		
		UIGridComp uiGrid = new UIGridComp();
		uiGrid.setWidgetId(WIDGET_ID);
		uiGrid.setId(GRID);
		flvPanel1.setElement(uiGrid);
		
		UIFlowvPanel flvPanel2 = new UIFlowvPanel();
		flvPanel2.setId("flowvp2");
		flvPanel2.setHeight("30");
		flvLayout.addPanel(flvPanel2);
		wuimeta.setElement(flvLayout);
		
		UIFlowhLayout flhLayout = new UIFlowhLayout();
		flhLayout.setId("flowh1");
		flhLayout.setWidgetId(WIDGET_ID);
		UIFlowhPanel flhP1 = new UIFlowhPanel();
		flhP1.setId("flowhp1");
		flhLayout.addPanel(flhP1);
		
		UIFlowhPanel flhP2 = new UIFlowhPanel();
		flhP2.setId("flowhp2");
		flhP2.setWidth("120");
		flhLayout.addPanel(flhP2);
		
		UIFlowhPanel flhP3 = new UIFlowhPanel();
		flhP3.setId("flowhp3");
		flhP3.setWidth("120");
		flhLayout.addPanel(flhP3);
		
		flvPanel2.setElement(flhLayout);
		UIButton okbt = new UIButton();
		okbt.setWidgetId(WIDGET_ID);
		okbt.setId(OKBT);
		flhP2.setElement(okbt);
		
		UIButton cancelbt = new UIButton();
		cancelbt.setWidgetId(WIDGET_ID);
		cancelbt.setId(CANCELBT);
		flhP3.setElement(cancelbt);
	}

	@Override
	protected PageMeta createPageMeta() {
		PageMeta pm = new PageMeta();
		pm.setId(getWebContext().getPageId());
		pm.setProcessorClazz(AppRequestProcessor.class.getName());
		LfwWidget widget = new LfwWidget();
		constructWidget(widget);
		widget.setId(WIDGET_ID);
		pm.addWidget(widget);
		
		Dataset ds = new Dataset();
		ds.setId(GRIDDS);
		ds.setLazyLoad(false);
		constructDataset(ds);
		widget.getViewModels().addDataset(ds);
		
		GridComp grid = new GridComp();
		grid.setId(GRID);
		grid.setDataset(GRIDDS);
		constructGrid(grid);
		
		widget.getViewComponents().addComponent(grid);
		
		ButtonComp okbt = new ButtonComp();
		constructOkButton(okbt);
		widget.getViewComponents().addComponent(okbt);
		
		ButtonComp cancelbt = new ButtonComp();
		constructCancelButton(cancelbt);
		widget.getViewComponents().addComponent(cancelbt);
		return pm;
	}

	protected void constructWidget(LfwWidget widget) {
	}

	/**
	 * 构建grid
	 * @param tree
	 */
	protected void constructGrid(GridComp grid) {
		GridColumn columnId = generateGridColumn(ID_FIELD);
		grid.addColumn(columnId);
		
		GridColumn columnCode = generateGridColumn(LOAD_CODE);
		grid.addColumn(columnCode);
		
		GridColumn columnName = generateGridColumn(LOAD_NAME);
		grid.addColumn(columnName);
		
	}

	private GridColumn generateGridColumn(String id) {
		GridColumn columnId = new GridColumn();
		columnId.setId(id);
		columnId.setText(id);
		columnId.setWidth(120);
		columnId.setDataType(StringDataTypeConst.STRING);
		columnId.setVisible(true);
		columnId.setEditable(false);
		return columnId;
	}

	protected void constructOkButton(ButtonComp okbt) {
		okbt.setId(OKBT);
		okbt.setText("确定");
		
		EventConf event = new EventConf();
		event.setName(MouseListener.ON_CLICK);
		event.setMethodName("okButtonClick");
		
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule pwr = new WidgetRule();
		pwr.setId("main");
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(GRIDDS);
		submitRule.addWidgetRule(pwr);
		pwr.addDsRule(dsRule);
		event.setSubmitRule(submitRule);
		
		
		EventSubmitRule parentSum = new EventSubmitRule();
		WidgetRule pwidgetRule = new WidgetRule();
		parentSum.addWidgetRule(pwidgetRule);
		pwidgetRule.setId("main");
		submitRule.setParentSubmitRule(parentSum);
		
		event.setJsEventClaszz(MouseListener.class.getName());
		okbt.addEventConf(event);
	}

	protected void constructCancelButton(ButtonComp cancelbt) {
		cancelbt.setId(CANCELBT);
		cancelbt.setText("取消");
		
		EventConf event = new EventConf();
		event.setName(MouseListener.ON_CLICK);
		event.setMethodName("cancelButtonClick");
		event.setJsEventClaszz(MouseListener.class.getName());
		cancelbt.addEventConf(event);
	}



	protected void constructDataset(Dataset ds) {
		FieldSet fs = ds.getFieldSet();
		Field idField = new Field();
		idField.setId(ID_FIELD);
		idField.setText("ID");
		fs.addField(idField);
		
		Field labelField = new Field();
		labelField.setId(LOAD_CODE);
		labelField.setText("Code");
		fs.addField(labelField);
		
		Field loadField = new Field();
		loadField.setId(LOAD_NAME);
		loadField.setText("name");
		fs.addField(loadField);
		constructDsLoadEvent(ds);
	}

	/**
	 * 创建ds事件加载
	 * @param ds
	 */
	private void constructDsLoadEvent(Dataset ds) {
		EventConf event = new EventConf();
		event.setName(DatasetListener.ON_DATA_LOAD);
		event.setMethodName("dataLoad");
		event.setJsEventClaszz(DatasetListener.class.getName());
		ds.addEventConf(event);
	}
}
