package nc.uap.lfw.core.bm.dft;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * ��ʼ̬��û��ѡ���У�����ѡ����ѡʱ���õ�״̬����
 *
 */
public class Init_Ss_Ms_StateManager extends AbstractStateManager {

	@Override
	/**
	 * ���ֳ�����ֻ���ж��Ƿ�༭̬������Ǳ༭̬����ť�����ã��������
	 */
	public IStateManager.State getState(WebComponent target, LfwWidget widget) {
		Dataset ds = getCtrlDataset(widget);
		if(ds == null)
			return IStateManager.State.ENABLED;
		return ds.isEnabled() ? IStateManager.State.DISABLED : IStateManager.State.ENABLED;
	}

}
