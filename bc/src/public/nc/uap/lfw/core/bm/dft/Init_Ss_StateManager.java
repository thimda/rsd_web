package nc.uap.lfw.core.bm.dft;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * ����ʼ̬,��ѡ̬�����ж���
 *
 */
public class Init_Ss_StateManager extends AbstractStateManager {

	@Override
	/**
	 * �����ѡ���У����ߴ��ڱ༭̬����ť������
	 */
	public IStateManager.State getState(WebComponent target, LfwWidget widget) {
		Dataset ds = getCtrlDataset(widget);
		if(ds == null)
			return IStateManager.State.ENABLED;
		Row[] rs = ds.getSelectedRows();
		if(rs == null)
			return IStateManager.State.ENABLED;
		return rs.length == 1 ? IStateManager.State.ENABLED : IStateManager.State.DISABLED;
	}
}
