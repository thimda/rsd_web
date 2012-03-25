package nc.uap.lfw.core.event.deft;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.ctx.LfwPageContext;

public class EmptyRowServerListener extends DefaultDatasetServerListener {

	public EmptyRowServerListener(LfwPageContext pagemeta, String widgetId) {
		super(pagemeta, widgetId);
	}

	@Override
	public void onDataLoad(DataLoadEvent se) {
		Dataset ds = se.getSource();
		ds.addRow(ds.getEmptyRow());
		ds.setRowSelectIndex(0);
		ds.setEnabled(true);
	}

}
