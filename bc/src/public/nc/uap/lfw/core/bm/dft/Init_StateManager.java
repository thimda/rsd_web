package nc.uap.lfw.core.bm.dft;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * 仅初始态可用判断器
 *
 */
public class Init_StateManager extends AbstractStateManager {

	@Override
	/**
	 * 如果有选中行，或者处于编辑态，则按钮不可用
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
