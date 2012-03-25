package nc.uap.lfw.core.bm.dft;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * 初始态（没有选中行），单选，多选时可用的状态管理
 *
 */
public class Init_Ss_Ms_StateManager extends AbstractStateManager {

	@Override
	/**
	 * 这种场景，只需判断是否编辑态，如果是编辑态，则按钮不可用，否则可用
	 */
	public IStateManager.State getState(WebComponent target, LfwWidget widget) {
		Dataset ds = getCtrlDataset(widget);
		if(ds == null)
			return IStateManager.State.ENABLED;
		return ds.isEnabled() ? IStateManager.State.DISABLED : IStateManager.State.ENABLED;
	}

}
