package nc.uap.lfw.core.bm.dft;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwWidget;

/**
 * ±‡º≠Ã¨ø…”√≈–∂œ∆˜
 *
 */
public class Edit_StateManager extends AbstractStateManager {

	@Override
	public IStateManager.State getState(WebComponent target, LfwWidget widget) {
		Dataset ds = getCtrlDataset(widget);
		if(ds == null)
			return IStateManager.State.ENABLED;
		return ds.isEnabled() ? IStateManager.State.ENABLED : IStateManager.State.DISABLED;
	}
}
