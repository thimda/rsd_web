package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.ViewContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;

/**
 * ��Ŀ�����ݼ����һ�в�ʹ֮�ɱ༭�������߼�
 * @author dengjt
 *
 */
public class UifDatasetEnableCmd extends UifCommand {
	private String datasetId;
	public UifDatasetEnableCmd(String dsId){
		datasetId = dsId;
	}
	@Override
	public void execute() {
		ViewContext widgetctx = getLifeCycleContext().getViewContext();
		Dataset ds = widgetctx.getView().getViewModels().getDataset(datasetId);
		Row row = ds.getEmptyRow();
		processRow(ds, row);
		ds.addRow(row);
		ds.setRowSelectIndex(ds.getRowCount() - 1);
		ds.setEnabled(true);
	}
	
	/**
	 * �ṩ��һ��������
	 * @param ds
	 * @param row
	 */
	protected void processRow(Dataset ds, Row row) {
		
	}

}
