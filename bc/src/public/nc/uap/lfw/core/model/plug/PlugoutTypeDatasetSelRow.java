package nc.uap.lfw.core.model.plug;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.WidgetRule;
import nc.uap.lfw.core.page.PlugoutDescItem;
public class PlugoutTypeDatasetSelRow implements IPlugoutType {
	@Override public Object fetchContent(PlugoutDescItem item, ViewContext viewCtx) {
		String dsId = item.getSource();
		Dataset ds = viewCtx.getView().getViewModels().getDataset(dsId);
		Row row = ds.getSelectedRow();
		TranslatedRow transRow = translate(ds, row);
		return transRow;
	}
	private TranslatedRow translate(Dataset ds, Row row) {
		TranslatedRow transRow = new TranslatedRow();
		if (row == null) {
			return transRow;
		}
		Field[] fields = ds.getFieldSet().getFields();
		for (int i = 0; i < fields.length; i++) {
			transRow.setValue(fields[i].getId(), row.getValue(i));
		}
		return transRow;
	}
	@Override public void buildSourceWidgetRule(WidgetRule widgetRule, String source) {
		DatasetRule dsr = new DatasetRule();
		dsr.setId(source);
		dsr.setType(DatasetRule.TYPE_CURRENT_LINE);
		widgetRule.addDsRule(dsr);
	}
}
