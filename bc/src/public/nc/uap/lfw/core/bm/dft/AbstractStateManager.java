package nc.uap.lfw.core.bm.dft;

import nc.uap.lfw.core.bm.IStateManager;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.page.LfwWidget;

public abstract class AbstractStateManager implements IStateManager {
	protected Dataset getCtrlDataset(LfwWidget widget){
		Dataset[] dss = widget.getViewModels().getDatasets();
		if(dss == null || dss.length == 0)
			return null;
		Dataset findDs = null;
		for (int i = 0; i < dss.length; i++) {
			Dataset ds = dss[i];
			if(ds instanceof IRefDataset)
				continue;
			if(ds.isControlwidgetopeStatus())
				return ds;
			findDs = ds;
		}
		return findDs;
	}
}
