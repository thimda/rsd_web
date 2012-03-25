package nc.uap.lfw.core.model.plug;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.page.PlugoutDescItem;

public class PlugoutTypeDatasetMutlSelRow implements IPlugoutType {

	@Override
	public void buildSourceWidgetRule(WidgetRule widgetRule, String source) {
		DatasetRule dsr = new DatasetRule();
		dsr.setId(source);
		dsr.setType(DatasetRule.TYPE_ALL_LINE);
		widgetRule.addDsRule(dsr);
	}

	@Override
	public Object fetchContent(PlugoutDescItem item, ViewContext viewCtx) {
		String dsId = item.getSource();
		Dataset ds = viewCtx.getView().getViewModels().getDataset(dsId);
		Row[] rows = ds.getAllSelectedRows();
		TranslatedRows transRows = translate(ds, rows);
		return transRows;
	}

	private TranslatedRows translate(Dataset ds, Row[] rows) {
		TranslatedRows transRows = new TranslatedRows();
		Field[] fields = ds.getFieldSet().getFields();
		for (int i = 0; i < fields.length; i++) {
			transRows.setValue(fields[i].getId(), getValueList(rows, i));
		}
		return transRows;
	}

	private List<Object> getValueList(Row[] rows, int index) {
		List<Object> valueList = new ArrayList<Object>();
		for (int i = 0; i< rows.length; i++){
			valueList.add(rows[i].getValue(index));
		}
		return valueList;
	}
	
}
