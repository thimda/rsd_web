package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.data.Dataset;


/**
 * 保存并新增命令，用于单行卡片保存后的新增逻辑
 *
 */
public class UifSaveAddCmd extends UifSaveCmd {
	public UifSaveAddCmd(String masterDsId,String[] detailDsIds, String aggVoClazz) {
		super(masterDsId, detailDsIds, aggVoClazz);
	}
	
	public UifSaveAddCmd(String masterDsId,String[] detailDsIds, String aggVoClazz, boolean bodyNotNull) {
		super(masterDsId, detailDsIds, aggVoClazz, bodyNotNull);
	}

	@Override
	protected void onAfterSave(Dataset masterDs, Dataset[] detailDss) {
		super.onAfterSave(masterDs, detailDss);
		CmdInvoker.invoke(new UifDatasetEnableCmd(masterDs.getId()));
	}
}
