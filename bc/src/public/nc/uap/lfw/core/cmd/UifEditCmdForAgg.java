package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;

/**
 * ¾ÛºÏvoµÄ±à¼­²Ù×÷
 * @author zhangxya
 *
 */
public class UifEditCmdForAgg extends UifCommand {
	
	private String datasetId;
	private String billId;
	

	public UifEditCmdForAgg(String dsId, String billId){
		this.datasetId = dsId;
		this.billId = billId;
	}
	
	public void execute() {
		CmdInvoker.invoke(new UifDsLoadRowEnabledCmd(datasetId, billId));
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(datasetId));
	}

}
