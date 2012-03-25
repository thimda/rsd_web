package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.tags.AppDynamicCompUtil;

public class UifRefreshDatasetCmd extends UifCommand {

	private String dsId;
	private int pageIndex;
	
	public UifRefreshDatasetCmd(String dsId, int pageIndex){
		this.dsId = dsId;
		this.pageIndex = pageIndex;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Dataset dataset = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset(dsId);
		AppDynamicCompUtil appUtil = new AppDynamicCompUtil(AppLifeCycleContext.current().getApplicationContext(), AppLifeCycleContext.current().getViewContext());
		appUtil.refreshDataset(dataset, pageIndex);
	}
	

}
