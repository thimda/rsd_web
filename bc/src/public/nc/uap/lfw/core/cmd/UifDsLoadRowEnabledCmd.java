package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.data.Dataset;

/**
 * ����dataset��ʹ��ɱ༭
 * @author zhangxya
 *
 */
public class UifDsLoadRowEnabledCmd extends UifDatasetLoadRowCmd {

	@Override
	protected void postProcessRowSelect(Dataset ds) {
		if(ds.getCurrentRowCount() > 0){
			ds.setRowSelectIndex(0);
		}
		ds.setEnabled(true);
	}

	public UifDsLoadRowEnabledCmd(String dsId, String billId) {
		super(dsId, billId);
	}

	@Override
	public void execute() {
		super.execute();
	}

}
