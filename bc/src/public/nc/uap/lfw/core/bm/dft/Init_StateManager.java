package nc.uap.lfw.core.bm.dft;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * ����ʼ̬�����ж���
 *
 */
public class Init_StateManager extends AbstractStateManager {

	@Override
	/**
	 * �����ѡ���У����ߴ��ڱ༭̬����ť������
	 */
	public IStateManager.State getState(WebComponent target, LfwWidget widget) {
		Dataset ds = getCtrlDataset(widget);
		if(ds == null)
			return IStateManager.State.ENABLED;
		Row r = ds.getSelectedRow();
		if(r == null)
			return IStateManager.State.ENABLED;
		return ds.isEnabled() ? IStateManager.State.DISABLED : IStateManager.State.ENABLED;
	}

}
