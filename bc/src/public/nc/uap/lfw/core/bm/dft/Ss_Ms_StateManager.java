package nc.uap.lfw.core.bm.dft;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * ����ѡ̬,��ѡ̬�����ж���
 *
 */
public class Ss_Ms_StateManager extends AbstractStateManager {

	@Override
	public IStateManager.State getState(WebComponent target, LfwWidget widget) {
		Dataset ds = getCtrlDataset(widget);
		if(ds == null)
			return IStateManager.State.ENABLED;
		Row[] rs = ds.getSelectedRows();
		if(rs == null)
			return IStateManager.State.DISABLED;
		return rs.length > 0 ? IStateManager.State.ENABLED : IStateManager.State.DISABLED;
	}
}
