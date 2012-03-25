package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;

/**
 * �ۺ�VO�������¼�
 * @author zhangxya
 *
 */
public class UifAddCmdForAgg extends UifCommand{

	private String dsId;
	
	public UifAddCmdForAgg(String dsId){
		this.dsId = dsId;
	}
	public UifAddCmdForAgg(String dsId, String navDatasetId, String navStr){
		this.dsId = dsId;
	}
	
	@Override
	public void execute() {
		CmdInvoker.invoke(new UifAddCmd(dsId));
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(dsId));
		
	}

}
