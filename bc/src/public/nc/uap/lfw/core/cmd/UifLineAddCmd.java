package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.uif.listener.IBodyInfo;

/**
 * "新增行"菜单逻辑处理
 * @author gd 2010-4-1
 *
 */
public class UifLineAddCmd extends UifLineInsertCmd {
	public UifLineAddCmd(IBodyInfo bodyInfo) {
		super(bodyInfo);
	}
	protected int getInsertIndex(Dataset ds) {
		return ds.getCurrentRowCount();
	}

}
