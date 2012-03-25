package nc.uap.lfw.core.cmd;

import nc.uap.lfw.core.cmd.base.UifCommand;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwWidget;
import nc.uap.lfw.core.serializer.impl.Dataset2XmlSerializer;

/**
 * 将Dataset中的FieldRelation序列化到当前选择行中
 * @author zhangxya
 *
 */
public class UifDatasetFieldRelCmd extends UifCommand{

	
	private String datasetId;
	
	public UifDatasetFieldRelCmd(String dsId) {
		this.datasetId = dsId;
	}
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		LfwWidget widget = getLifeCycleContext().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset(datasetId);
		new Dataset2XmlSerializer().dealFieldRelation(ds, ds.getFieldRelations().getFieldRelations());
	}

	
	
	
}
